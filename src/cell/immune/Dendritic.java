package cell.immune;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import cell.Cell;
import cell.EmptyCell;
import cell.notImmune.RenalCellCarcinoma;
import context.Orientation;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridDimensions;
import repast.simphony.space.grid.GridPoint;
import utils.CellUtils;

public class Dendritic extends Immune {

	private boolean mature;

	private Orientation lymphNodeOrientation;

	private Random random;

	public Dendritic(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
		this.mature = false;
		Parameters params = RunEnvironment.getInstance().getParameters();
		this.random = new Random(params.getInteger("randomSeed"));
		this.lymphNodeOrientation = Orientation.valueOf(params.getString("lymphNodeOrientation"));
	}

	@Override
	public void moveTo() {
		if (this.mature) {
			if (isOnEdge()) { // mature && on edge, spawn tcell

				List<EmptyCell> emptyCellsList = CellUtils.getSpecificCells(this.grid, this, EmptyCell.class)
						.collect(Collectors.toList());
				if (!emptyCellsList.isEmpty()) {
					int maxTCellToSpawn = random.nextInt(tCellToSpawn().intValue());
					int tCellToSpawn = maxTCellToSpawn <= emptyCellsList.size() ? maxTCellToSpawn
							: emptyCellsList.size();
					spawnTCell(emptyCellsList, tCellToSpawn);
				}

				this.mature = false;
				return;
			}
			
			// only mature, move to lymph node
			GridPoint gpt = getTarget();
			CellUtils.moveTowards(this.grid, this, gpt);
			return;
		}

		// not mature, random move to
		super.moveTo();
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
	private void spawnTCell(List<EmptyCell> emptyCellsList, int tCellToSpawn) {
		for (int i = 0; i < tCellToSpawn; i++) {
			TCell newTCell = null;
			int tCellTpye = random.nextInt(3);
			switch (tCellTpye) {
			case 0:
				newTCell = new CD8(1000, this.grid, 3); // TODO prendere parametri passati in input
			case 1:
				newTCell = new Th1(1000, this.grid);
			case 2:
				newTCell = new Treg(1000, this.grid);
			}
			CellUtils.replaceCell(this.grid, emptyCellsList.get(i), newTCell);
		}
	}

	// to retrieve the max number of Tcell to spawn
	private Long tCellToSpawn() {
		long tCellNumber = CellUtils.getSpecificCells(this.grid, this, TCell.class).count();
		GridDimensions dim = this.grid.getDimensions();
		int tCellPercentage = 10; // TODO prendere da parameters
		Long tCellMaxNumber = (dim.getHeight() * dim.getWidth() / 100 * tCellPercentage) - tCellNumber;
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
	public void act() {
		if (!this.mature) {
			Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
			List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
			if (!rccList.isEmpty()) {
				this.mature = true;
			}
		}
	}

	public boolean isMature() {
		return mature;
	}

	public void setMature(boolean mature) {
		this.mature = mature;
	}

}
