package notImmune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public class Adipocyte extends NotImmune {

	public Adipocyte(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

}
