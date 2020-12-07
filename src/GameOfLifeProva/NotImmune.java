package GameOfLifeProva;

import repast.simphony.space.grid.Grid;

public abstract class NotImmune extends Cell {

	public NotImmune(boolean is_alive, int lifespan, boolean is_foreign, Grid<Object> grid) {
		super(is_alive, lifespan, is_foreign, grid);
		// TODO Auto-generated constructor stub
	}

}
