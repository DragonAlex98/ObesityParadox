package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public abstract class Immune extends Cell {
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));
	
	private boolean active = false;

	public Immune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	/**
	 * Move to a near position
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void move() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<EmptyCell> emptyCells = CellUtils.filterNeighbors(neighbors, EmptyCell.class);

		if (!emptyCells.isEmpty()) {
			EmptyCell emptyCellToReplace = emptyCells.get(random.nextInt(emptyCells.size()));
			CellUtils.moveCell(this.getGrid(), this, emptyCellToReplace);
		}
	}
	
	/**
	 * Triggers the effect of the cell.
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public void act() {
		if (!this.active) {
			actIfNotActive();
		}
		if (this.active) {
			actIfActive();
		}
	}
	
	public abstract void actIfActive();
	
	public void actIfNotActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		if (!rccList.isEmpty()) {
			this.active = true;
		}
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
