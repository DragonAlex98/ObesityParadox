package commons.cell.immune;

import java.util.List;

import commons.cell.Cell;
import commons.cell.DeadCell;
import commons.cell.EmptyCell;
import commons.util.CellUtils;
import repast.simphony.space.grid.Grid;

public class M2 extends Immune {

	public M2(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public void ingestCell(Cell cell) {
		CellUtils.replaceCell(this.grid, cell, new EmptyCell(this.grid));
	}

	@Override
	public void actIfActive() {
		CellUtils.releaseTNFAlpha(grid, this);
		CellUtils.releaseIL10(grid, this);
		CellUtils.releaseTGFbeta(grid, this);
		
		this.setActive(false);
	}
	
	@Override
	public void act() {
		super.act();

		List<DeadCell> deadList = CellUtils.getSpecificCellsNearby(grid, this, DeadCell.class);
		deadList.forEach(deadCell -> ingestCell(deadCell));
	}
}
