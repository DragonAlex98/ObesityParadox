package cell.immune;

import java.util.List;

import cell.Cell;
import cell.DeadCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

/**
 * Implementation of a CD8+ Cytotoxic T Cell. The implementation of a CD8+ Naive T Cell is not present
 * because they are not represented in the grid when they are in the Naive state. In this state they are
 * supposed to be in the lymphnode. When they are activated from an APC they move from the lymphnode to the grid,
 * so the grid will only contain already active cells.
 * 
 * @author RICCARDO
 *
 */
public class CD8 extends TCell {
	
	private double killProb;
	
	public CD8(int lifespan, Grid<Cell> grid, double kill_prob) {
		super(lifespan, grid);
		this.killProb = kill_prob;
	}
	
	public void act() {
		// TODO Auto-generated method stub
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		
		if (rccList.isEmpty()) {
			// se non ho rcc vicine mi muovo verso la rcc più vicina
		} else {
			// se ho rcc vicine, ne killo una
			CellUtils.replaceCell(this.grid, rccList.get(0), new DeadCell(this.grid));
		}
		
		
	}

	public double getKillProb() {
		return killProb;
	}

	public void setKillProb(double killProb) {
		this.killProb = killProb;
	}

}
