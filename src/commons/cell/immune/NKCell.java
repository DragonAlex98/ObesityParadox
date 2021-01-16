package commons.cell.immune;

import java.util.List;
import java.util.Random;

import commons.cell.Cell;
import commons.cell.DeadCell;
import commons.cell.notimmune.RenalCellCarcinoma;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;

public class NKCell extends Immune {

	private float killProb = 0.5f;

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public NKCell(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void actIfActive() {
		List<RenalCellCarcinoma> rccList = CellUtils.getSpecificCellsNearby(grid, this, RenalCellCarcinoma.class);
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
