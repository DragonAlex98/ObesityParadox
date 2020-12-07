package immune;

import repast.simphony.space.grid.Grid;

public abstract class TCell extends Immune {

	public TCell(int lifespan, Grid<Object> grid) {
		super(lifespan, grid);
	}

}
