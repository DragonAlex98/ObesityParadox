package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.DeadCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class M2 extends Immune {


	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	private double proTumourProbability = RunEnvironment.getInstance().getParameters()
			.getInteger("obesityMacrophagePhenotypeProbability");

	public M2(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public void ingestCell() {
		
	}

	@Override
	public void actIfActive() {
		if(!isActive()) move()
		else {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		
		CellUtils.releaseTNFAlpha(grid, this, 5.0);
		CellUtils.releaseIL10(grid, this, 3.0);
		CellUtils.releaseTGFbeta(grid, this, 3.0);
		
			// KILL => ANTIGEN PRESENTATION => T CELL PROLIFERATION
			// NOT SELF?
			// PROBABILITY

		}
	}
}
