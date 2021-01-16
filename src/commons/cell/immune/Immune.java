package commons.cell.immune;

import java.util.List;
import java.util.Random;

import commons.cell.Cell;
import commons.cell.EmptyCell;
import commons.cell.notimmune.RenalCellCarcinoma;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;

public abstract class Immune extends Cell implements Cloneable {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	private float cellGrowth = RunEnvironment.getInstance().getParameters().getFloat("immuneCellGrowth");

	// by default I am not active, If true I can act
	private boolean active = false;

	public Immune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}
	
	private boolean checkProliferation() {
		return this.getAge() % (int) (cellGrowth * this.getLifespan()) == 0;
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void proliferate() {
		if (this.checkProliferation()) {
			List<EmptyCell> emptyCells = CellUtils.getSpecificCellsNearby(grid, this, EmptyCell.class);
			if (!emptyCells.isEmpty()) {
				try {
					CellUtils.replaceCell(grid, emptyCells.get(random.nextInt(emptyCells.size())), this.clone());
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
		List<EmptyCell> emptyCells = CellUtils.getSpecificCellsNearby(grid, this, EmptyCell.class);

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
		List<RenalCellCarcinoma> rccList = CellUtils.getSpecificCellsNearby(grid, this, RenalCellCarcinoma.class);
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
	
	public float getCellGrowth() {
		return cellGrowth;
	}

	public void increaseCellGrowth(float cellGrowth) {
		this.cellGrowth -= cellGrowth;
		if(this.cellGrowth < 0.4f) {
			this.cellGrowth = 0.4f;
		}
	}
	
	public void decreaseCellGrowth(float cellGrowth) {
		this.cellGrowth += cellGrowth;
		if(this.cellGrowth > 0.9f) {
			this.cellGrowth = 0.9f;
		}
	}

	public void setCellGrowth(float cellGrowth) {
		this.cellGrowth = cellGrowth;
	}

	@Override
	protected Immune clone() throws CloneNotSupportedException {
		Immune cell = (Immune) super.clone();
		cell.setActive(false);
		cell.setAge(0);
		cell.setSelf(true);
		cell.setCellGrowth(RunEnvironment.getInstance().getParameters().getFloat("immuneCellGrowth"));
		return cell;
	}
	
}
