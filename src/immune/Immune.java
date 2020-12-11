package immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;

public abstract class Immune extends Cell {
	
	public Immune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}
	
	// move to a neighbor position
	public abstract void moveTo();
	
	// inhibit or excite another immune cell
	public abstract void act();	

}
