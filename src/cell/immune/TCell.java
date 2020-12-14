package cell.immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class TCell extends Immune {
	
	// define if a tcell is active or not
	private boolean active = false;

	public TCell(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
