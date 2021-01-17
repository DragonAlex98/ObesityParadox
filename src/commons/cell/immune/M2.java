package commons.cell.immune;

import commons.cell.Cell;
import commons.util.CellUtils;
import repast.simphony.space.grid.Grid;

public class M2 extends Macrophage {

	public M2(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@Override
	public void actIfActive() {
		CellUtils.releaseTNFAlpha(grid, this);
		CellUtils.releaseIL10(grid, this);
		CellUtils.releaseTGFbeta(grid, this);
		
		this.setActive(false);
	}
}
