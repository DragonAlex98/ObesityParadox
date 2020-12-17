package cell.immune;

import java.util.Random;

import cell.Cell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class CD4 extends TCell {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public CD4(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void actIfActive() {
		int r = random.nextInt(3);
		if (r == 0) {
			CellUtils.replaceCell(grid, this, new Th1(1000, grid));
		}
		if (r == 1) {
			CellUtils.replaceCell(grid, this, new Th2(1000, grid));
		}
		if (r == 2) {
			CellUtils.replaceCell(grid, this, new Treg(1000, grid));
		}
	}
}
