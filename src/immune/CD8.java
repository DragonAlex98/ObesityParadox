package immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public class CD8 extends TCell {
	
	private double killProb;
	
	public CD8(int lifespan, Grid<Cell> grid, double kill_prob) {
		super(lifespan, grid);
		this.killProb = kill_prob;
	}

	@Override
	public void moveTo() {
		// TODO Auto-generated method stub

	}

	@Override
	public void act() {
		// TODO Auto-generated method stub

	}

	public double getKillProb() {
		return killProb;
	}

	public void setKillProb(double killProb) {
		this.killProb = killProb;
	}

}
