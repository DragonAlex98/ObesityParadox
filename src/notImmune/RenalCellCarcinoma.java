package notImmune;

import repast.simphony.space.grid.Grid;

public class RenalCellCarcinoma extends NotImmune {
	
	// number of ticks to wait for the next tumor expansion
	private int repro_time = 0;
	
	// how much it expands
	private int repro_factor = 0;
	
	public RenalCellCarcinoma(int lifespan, Grid<Object> grid, int repro_time, int repro_factor) {
		super(lifespan, grid);
		this.repro_time = repro_time;
		this.repro_factor = repro_factor;
	}

	//check if the time to expand has arrived or if there is a Mast Cell in its neighborhood
	public void grow() {
		
	}
	
	// creates new RenalCellCarcinoma
	private RenalCellCarcinoma reproduce() {
		return null;
	}

	public int getRepro_time() {
		return repro_time;
	}

	public void setRepro_time(int repro_time) {
		this.repro_time = repro_time;
	}

	public int getRepro_factor() {
		return repro_factor;
	}

	public void setRepro_factor(int repro_factor) {
		this.repro_factor = repro_factor;
	}
	
	
	
}
