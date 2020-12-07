package GameOfLifeProva;

import repast.simphony.space.grid.Grid;

public abstract class Cell {
	// wheter a cell is alive or not
	private boolean is_alive = true;
	
	// lifespan of a cell
	private int lifespan = 0;
	
	//wheter the cell is foreign or not
	private boolean is_foreign = false;
	
	// the grid where the cell lives
	private Grid<Object> grid = null;

	public Cell(boolean is_alive, int lifespan, boolean is_foreign, Grid<Object> grid) {
		this.is_alive = is_alive;
		this.lifespan = lifespan;
		this.is_foreign = is_foreign;
		this.grid = grid;
	}
	
}
