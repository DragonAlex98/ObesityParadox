package cell.immune;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public abstract class Immune extends Cell implements Cloneable {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	// by default I am not active, If true I can act
	private boolean active = false;
	
	private int cellGrowth = (int) (0.66 * this.getLifespan());

	public Immune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}
	
	private boolean checkProliferation() {
		if (this.getAge() % this.cellGrowth == 0)
			return true;
		
		return false;
	}
	
	//@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void proliferate() {
		if (this.checkProliferation()) {
			List<EmptyCell> eCells = CellUtils.getSpecificCellsNearby(grid, this, EmptyCell.class);
			if (!eCells.isEmpty()) {
				try {
					CellUtils.replaceCell(grid, eCells.get(0), this.clone());
				} catch (CloneNotSupportedException e) {
					System.out.println("Impossibile clonare la cellula");
				}
			}
		}
	}

	/**
	 * Move randomly to a near empty position
	 */
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
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
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void act() {
		if (!this.active) {
			actIfNotActive();
		}
		if (this.active) {
			actIfActive();
		}
	}

	/**
	 * Action to perfom if I am active, each immune cell have different behavior
	 */
	public abstract void actIfActive();

	/**
	 * Action to perfom if I am not active, by default I look for the nearest
	 * not_self RCC and I become mature is a non_self Rcc is close to me
	 */
	public void actIfNotActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		if (!rccList.isEmpty() && rccList.stream().filter(rcc -> !rcc.isSelf()).findAny().isPresent()) {
			this.active = true;
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	@Override
	protected Immune clone() throws CloneNotSupportedException {
		Immune cell = (Immune) super.clone();
		cell.setActive(false);
		cell.setAge(0);
		cell.setSelf(true);
		return cell;
	}
	
}
