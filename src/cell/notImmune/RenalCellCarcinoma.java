package cell.notImmune;

import java.util.List;
import java.util.Random;

import cell.BloodCell;
import cell.Cell;
import cell.EmptyCell;
import cell.immune.MastCell;
import cell.immune.TCell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class RenalCellCarcinoma extends NotImmune {

	// number of ticks to wait for the next tumor expansion
	private int reproTime;

	// how much it expands
	private int reproFactor;
	
	// percentage of mutation, to simulate the fact that I can hide from the immune system
	private float mutationPercentage;
	
	private float disableTCellPercetage;
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public RenalCellCarcinoma(int lifespan, Grid<Cell> grid, int reproTime, int reproFactor, float mutationPercentage, float disableTCellPercetage) {
		super(lifespan, grid);
		this.reproTime = reproTime;
		this.reproFactor = reproFactor;
		this.mutationPercentage = mutationPercentage;
		this.disableTCellPercetage = disableTCellPercetage;
		// I'm foreign by default
		this.setSelf(false);
	}

	// method to disable TCells with a 50/50 probability
	@ScheduledMethod(start = 1, interval = 1, priority = 3)
	public void disableTCells() {
		if (this.isSelf()) {
			return;
		}
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<TCell> immuneList = CellUtils.filterNeighbors(neighbors, TCell.class);
		immuneList.stream().filter(i -> i.isActive()).forEach(i -> {
			float disable = random.nextFloat();
			if (disable < this.disableTCellPercetage) {
				i.setActive(false);				
			}
		});
	}

	// grow method to check the time to reproduce or the presence of a pro Tumor Mast Cell
	@ScheduledMethod(start = 1, interval = 1, priority = 4)
	public void grow() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<BloodCell> bloodList = CellUtils.filterNeighbors(neighbors, BloodCell.class);
		int newReprotime = (this.reproTime == 1 || bloodList.isEmpty()) ? this.reproTime : this.reproTime - 1;
		if (this.getAge() > 0 && this.getAge() % newReprotime == 0) {
			reproduce();
		} else {
			List<MastCell> mastList = CellUtils.filterNeighbors(neighbors, MastCell.class);
			if (!mastList.isEmpty() && mastList.stream().filter(mast -> mast.isProTumor()).findAny().isPresent()) {      
				reproduce();
			}
		}
	}

	// creates new RenalCellCarcinoma, to simulate reproduction of the tumor
	private void reproduce() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<EmptyCell> list = CellUtils.filterNeighbors(neighbors, EmptyCell.class);
		// if there is at least one emptyCell in my neighbors I grow up
		if (!list.isEmpty()) {
			// check how many times I can actually reproduce
			int count = this.reproFactor < list.size() ? this.reproFactor : list.size();
			for (int i = 0; i < count; i++) {
				RenalCellCarcinoma rcc = new RenalCellCarcinoma(10, this.getGrid(), this.reproTime,
						this.reproFactor, this.mutationPercentage, this.disableTCellPercetage);
				float mutation = random.nextFloat();
				if (mutation < mutationPercentage) {
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
