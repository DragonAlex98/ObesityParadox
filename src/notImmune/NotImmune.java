package notImmune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class NotImmune extends Cell {

	public NotImmune(int lifespan, Grid<Object> grid) {
		super(lifespan, grid);
	}

}
