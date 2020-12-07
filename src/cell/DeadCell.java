package cell;

import repast.simphony.space.grid.Grid;

public class DeadCell extends Cell {

	public DeadCell(int lifespan, Grid<Object> grid) {
		super(lifespan, grid);
		this.setAlive(false);
	}

}
