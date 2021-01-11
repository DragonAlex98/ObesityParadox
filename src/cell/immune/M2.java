package cell.immune;

import java.util.List;

import cell.Cell;
import cell.DeadCell;
import cell.EmptyCell;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

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
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		List<DeadCell> deadList = CellUtils.filterNeighbors(neighbors, DeadCell.class);

		deadList.forEach(deadCell -> ingestCell(deadCell));
	}
}
