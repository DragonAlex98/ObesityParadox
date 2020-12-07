package GameOfLifeProva;

import repast.simphony.space.grid.Grid;

public class RenalCellCarcinoma extends NotImmune {
	
	// number of ticks to wait for the next tumor expansion
	private int repro_time = 0;
	
	// how much it expands
	private int repro_factor = 0;
	
	public RenalCellCarcinoma(boolean is_alive, int lifespan, boolean is_foreign, Grid<Object> grid) {
		super(is_alive, lifespan, is_foreign, grid);
		// TODO Auto-generated constructor stub
	}

	//check if the time to expand has arrived or if there is a Mast Cell in its neighborhood
	public void grow() {
		
	}
	
	// creates new RenalCellCarcinoma
	private RenalCellCarcinoma reproduce() {
		return null;
	}
	
}
