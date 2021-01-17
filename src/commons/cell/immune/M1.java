package commons.cell.immune;

import commons.cell.Cell;
import commons.util.CellUtils;
import repast.simphony.space.grid.Grid;

public class M1 extends Macrophage {

	public M1(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void actIfActive() {
		CellUtils.releaseIFNGamma(grid, this);
		CellUtils.releaseTNFAlpha(grid, this);
		
		this.setActive(false);
	}
}
