package bidimensional.cell.immune;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import bidimensional.cell.Cell;
import bidimensional.cell.EmptyCell;
import bidimensional.context.Orientation;
import bidimensional.utils.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;

/**
 * @author Marco
 *
 */
public class Dendritic extends Immune {
	
	// ratio CD4/CD8
	private double cd4cd8ratio;
	
	// the lymph node orientation
	private static Orientation lymphNodeOrientation = Orientation
			.valueOf(RunEnvironment.getInstance().getParameters().getString("lymphNodeOrientation"));

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));
	
	// max number of Tcell on the grid
	private static float tCellSpawnPercentage = 0.10f;

	public Dendritic(int lifespan, Grid<Cell> grid, double cd4cd8ratio) {
		super(lifespan, grid);
		this.cd4cd8ratio = cd4cd8ratio;
	}

	/**
	 * If I am active I move towards the lymph node
	 */
	@Override
	public void move() {
		if (this.isActive()) {
			GridPoint gpt = getTarget();
			CellUtils.moveTowards(this.grid, this, gpt);
			return;
		}
		super.move();
	}

	/**
	 * To get the X Y of the lymph node, depend on lymphNodeOrientation
	 * 
	 * @return The GridPoinf of the lymph node
	 */
	private GridPoint getTarget() {
		GridPoint gpt = null;
		switch (lymphNodeOrientation) {
		case NORTH:
			gpt = new GridPoint(this.grid.getLocation(this).getX(), this.grid.getDimensions().getHeight());
			break;
		case EAST:
			gpt = new GridPoint(this.grid.getDimensions().getWidth(), this.grid.getLocation(this).getY());
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

	//
	/**
	 * To random spawn a number "tCellToSpawn" of CD8 and CD4
	 * 
	 * @param emptyCellsList The empty cell list
	 * @param tCellToSpawn   Number of Tcell to spawn
	 */
	public void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		double newRatio = cd4cd8ratio;
		if (cd4cd8ratio < 1) {
			newRatio = 1 / cd4cd8ratio;
		}
		int numberOfCD8ToSpawn = (int) (tCellToSpawn / (1 + newRatio));
		int numberOfCD4ToSpawn = tCellToSpawn - numberOfCD8ToSpawn;
		if (cd4cd8ratio < 1) {
			int temp = numberOfCD4ToSpawn;
			numberOfCD4ToSpawn = numberOfCD8ToSpawn;
			numberOfCD8ToSpawn = temp;
		}
		
		Collections.shuffle(emptyCellsList, random);
		for (int i = 0; i < numberOfCD8ToSpawn; i++) {
			TCell newCD8 = new CD8(10, this.grid);
			newCD8.setActive(true);
			CellUtils.replaceCell(this.grid, emptyCellsList.get(i), newCD8);
		}
		for (int i = 0; i < numberOfCD4ToSpawn; i++) {
			TCell newCD4 = new CD4(10, this.grid);
			newCD4.setActive(true);
			CellUtils.replaceCell(this.grid, emptyCellsList.get(i+numberOfCD8ToSpawn), newCD4);
		}
	}

	/**
	 * To retrieve the max number of Tcell to spawn, depends on the grid dimension
	 * and current Tcell
	 * 
	 * @return The max number of Tcell to spawn
	 */
	private Long tCellToSpawn() {
		long tCellNumber = CellUtils.getSpecificCells(this.grid, this, TCell.class).count();
		GridDimensions dim = this.grid.getDimensions();
		Long tCellMaxNumber = (long) (dim.getHeight() * dim.getWidth() * tCellSpawnPercentage) - tCellNumber;
		return tCellMaxNumber;
	}

	/**
	 * Check if I am on the edge, depends on lymphNodeOrientation
	 * 
	 * @return True is I am on edge
	 */
	private boolean isOnEdge() {
		return (lymphNodeOrientation == Orientation.NORTH && this.getGrid().getLocation(this).getY() == this.getGrid().getDimensions().getHeight() - 1)
				|| (lymphNodeOrientation == Orientation.SOUTH && this.getGrid().getLocation(this).getY() == 0)
				|| (lymphNodeOrientation == Orientation.WEST && this.getGrid().getLocation(this).getX() == 0)
				|| (lymphNodeOrientation == Orientation.EAST && this.getGrid().getLocation(this).getX() == this.getGrid().getDimensions().getWidth() - 1);
	}

	/**
	 * If I am active dendritic I look for the nearest Tcell to set them as active.
	 *
	 * If I am on edge I become not active and spawn Tcell
	 */
	@Override
	public void actIfActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<TCell> tCellList = CellUtils.filterNeighbors(neighbors, TCell.class);
		if (!tCellList.isEmpty()) {
			tCellList.forEach(t -> t.setActive(true));
		}

		if (isOnEdge()) {
			this.setActive(false); // no empty cell no spawn
			List<EmptyCell> emptyCellsList = CellUtils.getSpecificCells(this.grid, this, EmptyCell.class)
					.collect(Collectors.toList());
			if (emptyCellsList.isEmpty()) {
				return;
			}
			int numberOfTCellToSpawn = tCellToSpawn().intValue();
			if (numberOfTCellToSpawn <= 0) {
				return;
			} // random value of Tcell to spwan, from 0 to "numberOfTCellToSpawn"
			int maxTCellToSpawn = Math.max(1, random.nextInt(numberOfTCellToSpawn));

			int tCellToSpawn = maxTCellToSpawn <= emptyCellsList.size() ? maxTCellToSpawn : emptyCellsList.size();
			spawnTCell(emptyCellsList, tCellToSpawn);
		}
	}
	
	public double getCD4CD8Ratio() {
		return this.cd4cd8ratio;
	}
}