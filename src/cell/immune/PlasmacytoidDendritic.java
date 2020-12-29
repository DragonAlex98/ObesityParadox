package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class PlasmacytoidDendritic extends Dendritic {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public PlasmacytoidDendritic(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		for (int i = 0; i < tCellToSpawn; i++) {
			int r = random.nextInt(3);
			if (r == 0) {
				CellUtils.replaceCell(grid, this, new CD4(10, grid));
			}
			if (r == 1) {
				CellUtils.replaceCell(grid, this, new CD8(10, grid, 0.8f));
			}
			if (r == 2) {
				CellUtils.replaceCell(grid, this, new NKCell(10, grid, 0.5f));
			}
		}
	}
}
