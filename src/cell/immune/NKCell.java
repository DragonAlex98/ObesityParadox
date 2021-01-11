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

	private float killProb = 0.5f;

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public NKCell(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
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
			CellUtils.releaseIFNGamma(grid, this);
			CellUtils.releaseTNFAlpha(grid, this);
			this.setActive(false);
		}
	}
}
