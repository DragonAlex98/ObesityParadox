package cell.immune;

import java.util.Collections;
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
		if(tCellToSpawn == 0) {
			return;
		}
		Collections.shuffle(emptyCellsList);
		for (int i = 0; i < tCellToSpawn; i++) {
			int r = random.nextInt(3);
			if (r == 0) {
				CD4 newCD4 = new CD4(10, grid);
				newCD4.setActive(true);
				CellUtils.replaceCell(grid, emptyCellsList.get(i), newCD4);
			}
			if (r == 1) {
				CD8 newCD8 = new CD8(10, grid, RunEnvironment.getInstance().getParameters().getFloat("cd8KillProb"));
				newCD8.setActive(true);
				CellUtils.replaceCell(grid, emptyCellsList.get(i), newCD8);
			}
			if (r == 2) {
				NKCell newNk = new NKCell(10, grid, RunEnvironment.getInstance().getParameters().getFloat("nkKillProb"));
				newNk.setActive(true);
				CellUtils.replaceCell(grid, emptyCellsList.get(i), newNk);
			}
		}
	}
}
