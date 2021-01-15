package cell;

import java.util.Random;

import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public abstract class Cell {

	// lifeSpan of a cell
	private int lifeSpan = -1;

	// age of the cell
	private int age = 0;

	// whether the cell is foreign or not, only tumor is false
	private boolean self = true;

	// the grid where the cell lives
	protected Grid<Cell> grid = null;
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public Cell(int lifespan, Grid<Cell> grid) {
		if (lifespan != -1) {
			this.lifeSpan = 20 + random.nextInt(lifespan);
		}
		this.grid = grid;
	}

	// check age and eventually die
	@ScheduledMethod(start = 1, interval = 1, priority = 2)
	public boolean checkAge() {
		if (this.age == this.lifeSpan && !(this instanceof DeadCell)) {
			this.die();
		}
		return false;
	}

	// kill the cell
	public void die() {
		DeadCell deadCell = new DeadCell(this.grid);
		CellUtils.replaceCell(this.grid, this, deadCell);
	}
	
	// if I am alive, my age will be incremented
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void increaseAge() {
		this.age++;
	}

	public int getLifespan() {
		return lifeSpan;
	}

	public void setLifespan(int lifespan) {
		this.lifeSpan = lifespan;
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean foreign) {
		this.self = foreign;
	}

	public Grid<Cell> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Cell> grid) {
		this.grid = grid;
	}

	public int getLifeSpan() {
		return lifeSpan;
	}

	public void setLifeSpan(int lifeSpan) {
		this.lifeSpan = lifeSpan;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		return this.getClass().toString()+" [lifeSpan=" + lifeSpan + ", age=" + age + ", self=" + self + "]";
	}

	public Integer getBMI() {
		return RunEnvironment.getInstance().getParameters().getInteger("bmi");
	}
}
