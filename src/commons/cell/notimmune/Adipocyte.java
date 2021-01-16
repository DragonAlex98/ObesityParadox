package commons.cell.notimmune;

import commons.cell.Cell;
import repast.simphony.space.grid.Grid;

public class Adipocyte extends NotImmune {

	public Adipocyte(Grid<Cell> grid) {
		super(-1, grid);
	}

}
