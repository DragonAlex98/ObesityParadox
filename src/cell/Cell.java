package cell;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;

public abstract class Cell {
	// whether a cell is alive or not
	private boolean alive = true;

	// lifespan of a cell
	private int lifeSpan = 0;

	// whether the cell is foreign or not
	private boolean foreign = false;

	// the grid where the cell lives
	private Grid<Cell> grid = null;

	// age of the cell
	private int age = 0;

	public Cell(int lifespan, Grid<Cell> grid) {
		this.lifeSpan = lifespan;
		this.grid = grid;
	}

	// check age and eventually die
	public boolean checkAge() {
		return this.age == this.lifeSpan;
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void increaseAge() {
		if (this.alive) {
			this.age++;
		}
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getLifespan() {
		return lifeSpan;
	}

	public void setLifespan(int lifespan) {
		this.lifeSpan = lifespan;
	}

	public boolean isForeign() {
		return foreign;
	}

	public void setForeign(boolean foreign) {
		this.foreign = foreign;
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
	
}
