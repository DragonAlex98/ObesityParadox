package bidimensional.cell.immune;

import bidimensional.cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class TCell extends Immune {

	public TCell(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}
}
