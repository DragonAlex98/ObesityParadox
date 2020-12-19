package cell.notImmune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import cell.immune.MastCell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class RenalCellCarcinoma extends NotImmune {

	// number of ticks to wait for the next tumor expansion
	private int reproTime = 0;

	// how much it expands
	private int reproFactor = 0;
	
	// percentage of mutation, to simulate the fact that I can hide from the immune system
	private double mutationPercentage = 0.20;
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public RenalCellCarcinoma(int lifespan, Grid<Cell> grid, int reproTime, int reproFactor) {
		super(lifespan, grid);
		this.reproTime = reproTime;
		this.reproFactor = reproFactor;
		// I'm foreign by default
		this.setSelf(false);
	}
	
	// TODO how to disable Immune Cell

	// grow method to check the time to reproduce or the presence of a pro Tumor Mast Cell
	@ScheduledMethod(start = 1, interval = 1)
	public void grow() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		if (this.getAge() % reproTime == 0) {
			reproduce(neighbors);
		} else {
			List<MastCell> mastList = CellUtils.filterNeighbors(neighbors, MastCell.class);
			if (!mastList.isEmpty() && mastList.stream().filter(mast -> mast.isProTumor()).findAny().isPresent()) {      
				reproduce(neighbors);
			}
		}
	}

	// creates new RenalCellCarcinoma, to simulate reproduction of the tumor
	private void reproduce(Iterable<Cell> neighbors) {
		List<EmptyCell> list = CellUtils.filterNeighbors(neighbors, EmptyCell.class);
		// if there is at least one emptyCell in my neighbors I grow up
		if (!list.isEmpty()) {
			// check how many times I can actually reproduce
			int count = this.reproFactor < list.size() ? this.reproFactor : list.size();
			for (int i = 0; i < count; i++) {
				RenalCellCarcinoma rcc = new RenalCellCarcinoma(this.getLifespan(), this.getGrid(), this.reproTime,
						this.reproFactor);
				double mutation = random.nextDouble();
				if(mutation < mutationPercentage) {
					rcc.setSelf(true);
				}
				CellUtils.replaceCell(this.getGrid(), list.get(i), rcc);
			}
		}
	}

	public int getReproTime() {
		return reproTime;
	}

	public void setReproTime(int reproTime) {
		this.reproTime = reproTime;
	}

	public int getReproFactor() {
		return reproFactor;
	}

	public void setReproFactor(int reproFactor) {
		this.reproFactor = reproFactor;
	}

}
