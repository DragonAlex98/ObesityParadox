package cell.immune;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import cell.Cell;
import cell.EmptyCell;
import context.Orientation;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import utils.CellUtils;

public class Dendritic extends Immune {

	private static float cd4cd8ratio = RunEnvironment.getInstance().getParameters().getFloat("cd4cd8Ratio");

	private static Orientation lymphNodeOrientation = Orientation.valueOf(RunEnvironment.getInstance().getParameters().getString("lymphNodeOrientation"));

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	private static float tCellSpawnPercentage = RunEnvironment.getInstance().getParameters().getFloat("tCellSpawnPercentage");

	public Dendritic(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void move() {
		if (this.isActive()) {
			// only active, move to lymph node
			GridPoint gpt = getTarget();
			CellUtils.moveTowards(this.grid, this, gpt);
			return;
		}

		// not active, random move to
		super.move();
	}

	// to get the X Y of the lymph node
	private GridPoint getTarget() {
		GridPoint gpt = null;
		switch (lymphNodeOrientation) {
		case NORTH:
			gpt = new GridPoint(this.grid.getLocation(this).getX(), this.grid.getDimensions().getHeight());
			break;
		case EAST:
			gpt = new GridPoint(this.grid.getDimensions().getWidth(), this.grid.getLocation(this).getY() );
			break;
		case SOUTH:
			gpt = new GridPoint(this.grid.getLocation(this).getX(), -1);
			break;
		case WEST:
			gpt = new GridPoint(-1, this.grid.getLocation(this).getY());
			break;
		}
		return gpt;
	}

	// to random spawn a number "tCellToSpawn" of Tcell
	public void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		int numberOfCD8ToSpawn = (int) (tCellToSpawn / (1+cd4cd8ratio));
		int numberOfCD4ToSpawn = tCellToSpawn - numberOfCD8ToSpawn;
		for (int i = 0; i < numberOfCD8ToSpawn; i++) {
			TCell newCD8 = new CD8(1000, this.grid, 3);
			newCD8.setActive(true);
			CellUtils.replaceCell(this.grid, emptyCellsList.get(i), newCD8);
		}
		for (int i = 0; i < numberOfCD4ToSpawn; i++) {
			TCell newCD4 = new CD4(1000, this.grid);
			newCD4.setActive(true);
			CellUtils.replaceCell(this.grid, emptyCellsList.get(i), newCD4);
		}
	}

	// to retrieve the max number of Tcell to spawn
	private Long tCellToSpawn() {
		long tCellNumber = CellUtils.getSpecificCells(this.grid, this, TCell.class).count();
		GridDimensions dim = this.grid.getDimensions();
		Long tCellMaxNumber = (long) (dim.getHeight() * dim.getWidth() * tCellSpawnPercentage) - tCellNumber;
		return tCellMaxNumber;
	}

	// check if I am on the edge
	private boolean isOnEdge() {
		return (lymphNodeOrientation == Orientation.NORTH && this.getGrid().getLocation(this).getY() == this.getGrid().getDimensions().getHeight() - 1)
				|| (lymphNodeOrientation == Orientation.SOUTH && this.getGrid().getLocation(this).getY() == 0)
				|| (lymphNodeOrientation == Orientation.WEST && this.getGrid().getLocation(this).getX() == 0)
				|| (lymphNodeOrientation == Orientation.EAST && this.getGrid().getLocation(this).getX() == this.getGrid().getDimensions().getWidth() - 1);
	}

	// check for RCC, if an RCC is detected I become mature
	@Override
	public void actIfActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<TCell> tCellList = CellUtils.filterNeighbors(neighbors, TCell.class);
		if (!tCellList.isEmpty()) {
			tCellList.forEach(t -> t.setActive(true));
		}
		
		// remains active until it spawns tcells
		if (isOnEdge()) { // on edge, spawn tcell
			List<EmptyCell> emptyCellsList = CellUtils.getSpecificCells(this.grid, this, EmptyCell.class)
					.collect(Collectors.toList());
			if (emptyCellsList.isEmpty()) {
				return;
			}
			int numberOfTCellToSpawn = tCellToSpawn().intValue();
			if (numberOfTCellToSpawn <= 0) {
				return;
			}
			System.out.println(numberOfTCellToSpawn);
			int maxTCellToSpawn = random.nextInt(numberOfTCellToSpawn);
			int tCellToSpawn = maxTCellToSpawn <= emptyCellsList.size() ? maxTCellToSpawn
					: emptyCellsList.size();
			spawnTCell(emptyCellsList, tCellToSpawn);

			this.setActive(false);
		}
	}
}
