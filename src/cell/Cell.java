package cell;

import repast.simphony.space.grid.Grid;

public abstract class Cell {
	// whether a cell is alive or not
	private boolean alive = true;
	
	// lifespan of a cell
	private int lifespan = 0;
	
	//whether the cell is foreign or not
	private boolean foreign = false;
	
	// the grid where the cell lives
	private Grid<Object> grid = null;
	

	public Cell(int lifespan, Grid<Object> grid) {
		this.lifespan = lifespan;
		this.grid = grid;
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	public int getLifespan() {
		return lifespan;
	}

	public void setLifespan(int lifespan) {
		this.lifespan = lifespan;
	}

	public boolean isForeign() {
		return foreign;
	}

	public void setForeign(boolean foreign) {
		this.foreign = foreign;
	}

	public Grid<Object> getGrid() {
		return grid;
	}

	public void setGrid(Grid<Object> grid) {
		this.grid = grid;
	}


}
