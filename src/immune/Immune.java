package immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class Immune extends Cell {
	
	public Immune(int lifespan, Grid<Object> grid) {
		super(lifespan, grid);
	}
	
	// move to a neighbor position
	public abstract void move_to();
	
    // check if a neighbor is foreign or not	
	public abstract void check_neighbor();
	
	// inhibit or excite another immune cell
	public abstract void act();
	
	// check age and eventually die
	public void check_age() {

	}

}
