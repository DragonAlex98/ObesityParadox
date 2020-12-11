package notImmune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class NotImmune extends Cell {

	public NotImmune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

}
