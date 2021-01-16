package commons.cell;

import repast.simphony.space.grid.Grid;

public class EmptyCell extends Cell {

	public EmptyCell(Grid<Cell> grid) {
		super(-1, grid);
	}
}
