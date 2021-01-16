package commons.cell.notimmune;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import commons.cell.Cell;
import commons.cell.EmptyCell;
import commons.cell.immune.MastCell;
import commons.cell.immune.TCell;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;

public class RenalCellCarcinoma extends NotImmune implements Cloneable{

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
		List<TCell> immuneList = CellUtils.getSpecificCellsNearby(grid, this, TCell.class);
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
		List<BloodCell> bloodList = CellUtils.getSpecificCellsNearby(grid, this, BloodCell.class);
		int newReprotime = (bloodList.isEmpty()) ? this.reproTime : Math.max(this.reproTime - bloodList.size(), 1);
		if (this.getAge() > 0 && this.getAge() % newReprotime == 0) {
			reproduce();
		} else {
			List<MastCell> mastList = CellUtils.getSpecificCellsNearby(grid, this, MastCell.class);
			if (mastList.stream().filter(mast -> mast.isProTumor()).findAny().isPresent()) {      
				reproduce();
			}
		}
	}

	// creates new RenalCellCarcinoma, to simulate reproduction of the tumor
	private void reproduce() {
		List<EmptyCell> list = CellUtils.getSpecificCellsNearby(grid, this, EmptyCell.class);
		// if there is at least one emptyCell in my neighbors I grow up
		if (!list.isEmpty()) {
			// check how many times I can actually reproduce
			int count = this.reproFactor < list.size() ? this.reproFactor : list.size();
			Collections.shuffle(list);
			for (int i = 0; i < count; i++) {
				RenalCellCarcinoma rcc = null;
				try {
					rcc = this.clone();
				} catch (CloneNotSupportedException e) {
					System.out.println("Impossibile clonare rcc");
				}
				float mutation = random.nextFloat();
				if (!rcc.isSelf() && mutation < mutationPercentage) {
					rcc.setSelf(true);
				}
				CellUtils.replaceCell(this.getGrid(), list.get(i), rcc);
			}
		}
	}
	
	@Override
	protected RenalCellCarcinoma clone() throws CloneNotSupportedException {
		RenalCellCarcinoma cell = (RenalCellCarcinoma) super.clone();
		cell.setAge(0);
		return cell;
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
