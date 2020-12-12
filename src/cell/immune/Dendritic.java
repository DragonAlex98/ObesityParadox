package cell.immune;

import java.util.List;

import cell.Cell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.space.grid.Grid;
import utils.CellSpawner;
import utils.CellUtils;
import utils.DetectedRCCList;

public class Dendritic extends Immune {

	private boolean mature;

	public Dendritic(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
		this.mature = false;
	}

	@Override
	public void moveTo() {
		if (this.mature) {
			if (isOnEdge()) {
				CellSpawner.tCellSpawner();
				this.mature = false;
			} else {
				// TODO altrimenti vai a dx, swap
			}
		} else {
			// super.moveTO()
		}
	}

	// check if I am on the edge, x=0 or y=0 or x=width-1 or y=height-1
	private boolean isOnEdge() {
		boolean isOnEdge = this.getGrid().getLocation(this).getX() == this.getGrid().getDimensions().getWidth() - 1
				|| this.getGrid().getLocation(this).getX() == 0
				|| this.getGrid().getLocation(this).getY() == this.getGrid().getDimensions().getHeight() - 1
				|| this.getGrid().getLocation(this).getY() == 0;
		return isOnEdge;
	}

	// check for RCC, if an RCC is detected I become mature and update detectedRCC
	@Override
	public void act() {
		if (!this.mature) {
			Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
			List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
			if (!rccList.isEmpty()) {
				this.mature = true;
				for (RenalCellCarcinoma rcc : rccList) {
					DetectedRCCList.getInstance().add(rcc);
				}
			}
		}
	}

	public boolean isMature() {
		return mature;
	}

	public void setMature(boolean mature) {
		this.mature = mature;
	}

}
