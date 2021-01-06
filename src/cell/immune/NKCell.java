package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.DeadCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class NKCell extends Immune {

	private float killProb;

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public NKCell(int lifespan, Grid<Cell> grid, float killProb) {
		super(lifespan, grid);
		this.killProb = killProb;
	}

	@Override
	public void actIfActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		float kill = random.nextFloat();
		if (!rccList.isEmpty()) {
			if (kill < killProb) {
				CellUtils.replaceCell(this.grid, rccList.get(random.nextInt(rccList.size())), new DeadCell(this.grid));
			}
			CellUtils.releaseIFNGamma(grid, this, 5.0);
			CellUtils.releaseTNFAlpha(grid, this, 5.0);
			this.setActive(false);
		}
	}

	public double getKillProb() {
		return killProb;
	}

	public void setKillProb(float killProb) {
		this.killProb = killProb;
	}
	
	@Override
	protected NKCell clone() throws CloneNotSupportedException {
		NKCell cell = (NKCell) super.clone();
		cell.setKillProb(RunEnvironment.getInstance().getParameters().getFloat("nkKillProb"));
		return cell;
	}

}
