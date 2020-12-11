package cell;

import repast.simphony.space.grid.Grid;

public class DeadCell extends Cell {

	public DeadCell(Grid<Cell> grid) {
		super(-1, grid);
	}

}
