package cell.immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public class NKCell extends Immune{
	
	private double killProb;

	public NKCell(int lifespan, Grid<Cell> grid, double killProb) {
		super(lifespan, grid);
		this.killProb = killProb;
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
