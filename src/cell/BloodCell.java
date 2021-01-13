package cell;

import java.util.List;
import java.util.Random;

import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public class BloodCell extends Cell {
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));
	
	private float angiogenesisProbability = 0.5f;

	public BloodCell(Grid<Cell> grid) {
		super(-1, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkForAngiogenesis() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		if (rccList.size() >= 3) {
			if (random.nextFloat() < angiogenesisProbability) {
				RenalCellCarcinoma rccToReplaceWithBlood = rccList.get(random.nextInt(rccList.size()));
				CellUtils.replaceCell(grid, rccToReplaceWithBlood, new BloodCell(this.grid));				
			}
		}
	}
}
