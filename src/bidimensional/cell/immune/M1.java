package bidimensional.cell.immune;

import java.util.List;

import bidimensional.cell.Cell;
import bidimensional.cell.DeadCell;
import bidimensional.cell.EmptyCell;
import bidimensional.utils.CellUtils;
import repast.simphony.space.grid.Grid;

public class M1 extends Immune {

	public M1(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public void ingestCell(Cell cell) {
		CellUtils.replaceCell(this.grid, cell, new EmptyCell(this.grid));
	}

	@Override
	public void actIfActive() {
		CellUtils.releaseIFNGamma(grid, this);
		CellUtils.releaseTNFAlpha(grid, this);
		
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