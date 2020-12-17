package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class PlasmacitoidDendritic extends Dendritic {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public PlasmacitoidDendritic(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		for (int i = 0; i < tCellToSpawn; i++) {
			int r = random.nextInt(3);
			if (r == 0) {
				CellUtils.replaceCell(grid, this, new CD4(1000, grid));
			}
			if (r == 1) {
				CellUtils.replaceCell(grid, this, new CD8(1000, grid, 3));
			}
			if (r == 2) {
				CellUtils.replaceCell(grid, this, new NKCell(1000, grid, 3));
			}
		}
	}
}
