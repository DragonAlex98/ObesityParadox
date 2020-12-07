package immune;

import repast.simphony.space.grid.Grid;

public class CD8 extends TCell {
	
	private double kill_prob;
	
	public CD8(int lifespan, Grid<Object> grid, double kill_prob) {
		super(lifespan, grid);
		this.kill_prob = kill_prob;
	}

	@Override
	public void move_to() {
		// TODO Auto-generated method stub

	}

	@Override
	public void check_neighbor() {
		// TODO Auto-generated method stub

	}

	@Override
	public void act() {
		// TODO Auto-generated method stub

	}

	public double getKill_prob() {
		return kill_prob;
	}

	public void setKill_prob(double kill_prob) {
		this.kill_prob = kill_prob;
	}

}
