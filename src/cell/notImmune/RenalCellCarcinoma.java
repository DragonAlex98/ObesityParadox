package cell.notImmune;

import java.util.List;

import cell.Cell;
import cell.immune.MastCell;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class RenalCellCarcinoma extends NotImmune {

	// number of ticks to wait for the next tumor expansion
	private int reproTime = 0;

	// how much it expands
	private int reproFactor = 0;

	public RenalCellCarcinoma(int lifespan, Grid<Cell> grid, int reproTime, int reproFactor) {
		super(lifespan, grid);
		this.reproTime = reproTime;
		this.reproFactor = reproFactor;
	}

	// TODO Can RCC dead? How can it reproduce?

	// check the time to reproduce or the presence of a Mast Cell
	public void grow() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		if (this.getAge() % reproTime == 0) {
			checkReproduction(neighbors);
		} else {
			List<MastCell> mastList = CellUtils.filterNeighbors(neighbors, MastCell.class);
			if (!mastList.isEmpty()) {
				checkReproduction(neighbors);
			}
		}
	}

	// creates new RenalCellCarcinoma if it is possible
	private void checkReproduction(Iterable<Cell> neighbors) {
		List<Adipocyte> list = CellUtils.filterNeighbors(neighbors, Adipocyte.class);
		// if there is at least one adipocyte in my neighbors I grow up
		if (!list.isEmpty()) {
			// check how many times I can actually reproduce
			int count = this.reproFactor < list.size() ? this.reproFactor : list.size();
			for (int i = 0; i < count; i++) {
				RenalCellCarcinoma rcc = new RenalCellCarcinoma(this.getLifespan(), this.getGrid(), this.reproTime,
						this.reproFactor);
				CellUtils.replaceCell(this.getGrid(), list.get(i), rcc);
			}
		}
	}

	public int getReproTime() {
		return reproTime;
	}

	public void setReproTime(int reproTime) {
		this.reproTime = reproTime;
	}

	public int getReproFactor() {
		return reproFactor;
	}

	public void setReproFactor(int reproFactor) {
		this.reproFactor = reproFactor;
	}

}
