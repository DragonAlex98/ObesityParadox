package commons.cell.immune;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import bidimensional.context.Orientation;
import commons.cell.Cell;
import commons.cell.EmptyCell;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import threedimensional.context.Orientation3D;

public class PlasmacytoidDendritic extends Dendritic {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public PlasmacytoidDendritic(int lifespan, Grid<Cell> grid, double cd4cd8ratio, Orientation lympOrientation) {
		super(lifespan, grid, cd4cd8ratio, lympOrientation);
	}

	public PlasmacytoidDendritic(int lifespan, Grid<Cell> grid, double cd4cd8ratio, Orientation3D lympOrientation3d) {
		super(lifespan, grid, cd4cd8ratio, lympOrientation3d);
	}

	@Override
	public void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		if (tCellToSpawn == 0) {
			return;
		}
		Collections.shuffle(emptyCellsList, random);
		int numberOfNKToSpawn = Math.max(1, tCellToSpawn / 5);
		for (int i = 0; i < numberOfNKToSpawn; i++) {
			NKCell newNk = new NKCell(10, grid);
			newNk.setActive(true);
			CellUtils.replaceCell(grid, emptyCellsList.get(i), newNk);
		}
		if (tCellToSpawn - numberOfNKToSpawn == 0) {
			return;
		}
		
		double newRatio = this.getCD4CD8Ratio();
		if (this.getCD4CD8Ratio() < 1) {
			newRatio = 1 / this.getCD4CD8Ratio();
		}
		int numberOfCD8ToSpawn = (int) ((tCellToSpawn - numberOfNKToSpawn) / (1 + newRatio));
		int numberOfCD4ToSpawn = tCellToSpawn - numberOfNKToSpawn - numberOfCD8ToSpawn;
		if (this.getCD4CD8Ratio() < 1) {
			int temp = numberOfCD4ToSpawn;
			numberOfCD4ToSpawn = numberOfCD8ToSpawn;
			numberOfCD8ToSpawn = temp;
		}
		
		for (int i = 0; i < numberOfCD4ToSpawn; i++) {
			CD4 newCD4 = new CD4(10, grid);
			newCD4.setActive(true);
			CellUtils.replaceCell(grid, emptyCellsList.get(i+numberOfNKToSpawn), newCD4);
		}
		
		
		for (int i = 0; i < numberOfCD8ToSpawn; i++) {
			CD8 newCD8 = new CD8(10, grid);
			newCD8.setActive(true);
			CellUtils.replaceCell(grid, emptyCellsList.get(i+numberOfNKToSpawn+numberOfCD4ToSpawn), newCD8);
		}
	}
}
