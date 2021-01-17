package commons.cell.immune;

import java.util.List;

import commons.cell.Cell;
import commons.cell.DeadCell;
import commons.cell.EmptyCell;
import commons.util.CellUtils;
import repast.simphony.space.grid.Grid;

public abstract class Macrophage extends Immune {

	public Macrophage(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public void ingestCell(Cell cell) {
		CellUtils.replaceCell(this.grid, cell, new EmptyCell(this.grid));
	}
	
	@Override
	public void act() {
		super.act();

		List<DeadCell> deadList = CellUtils.getSpecificCellsNearby(grid, this, DeadCell.class);
		deadList.forEach(deadCell -> ingestCell(deadCell));
	}
}
