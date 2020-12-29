package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.DeadCell;
import cell.EmptyCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class M1 extends Immune {


	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	private double proTumourProbability = RunEnvironment.getInstance().getParameters()
			.getFloat("obesityMacrophagePhenotypeProbability");

	public M1(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	public void ingestCell(Cell cell) {
		CellUtils.replaceCell(this.grid, cell, new EmptyCell(this.grid));
	}

	@Override
	public void actIfActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);

		CellUtils.releaseIFNGamma(grid, this, 3.0);
		CellUtils.releaseTNFAlpha(grid, this, 5.0);
		// KILL => ANTIGEN PRESENTATION => T CELL PROLIFERATION
		// NOT SELF?
		// PROBABILITY
	}
	
	@Override
	public void act() {
		super.act();
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		List<DeadCell> deadList = CellUtils.filterNeighbors(neighbors, DeadCell.class);

		deadList.forEach(deadCell -> ingestCell(deadCell));
	}
}
