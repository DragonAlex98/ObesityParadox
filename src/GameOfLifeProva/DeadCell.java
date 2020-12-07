package GameOfLifeProva;

import repast.simphony.space.grid.Grid;

public class DeadCell extends Cell {

	public DeadCell(boolean is_alive, int lifespan, boolean is_foreign, Grid<Object> grid) {
		super(false, lifespan, is_foreign, grid);
	}

}
