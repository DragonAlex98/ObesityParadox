package commons.cell.notimmune;

import java.util.List;
import java.util.Random;

import commons.cell.Cell;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;

public class BloodCell extends Cell {
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));
	
	private float angiogenesisProbability = 0.5f;

	public BloodCell(Grid<Cell> grid) {
		super(-1, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkForAngiogenesis() {
		List<RenalCellCarcinoma> rccList = CellUtils.getSpecificCellsNearby(grid, this, RenalCellCarcinoma.class);
		int limit = 3;
		if (!CellUtils.isGridBiDimensional(grid)) {
			limit = 9;
		}
		if (rccList.size() >= limit) {
			if (random.nextFloat() < angiogenesisProbability) {
				RenalCellCarcinoma rccToReplaceWithBlood = rccList.get(random.nextInt(rccList.size()));
				CellUtils.replaceCell(grid, rccToReplaceWithBlood, new BloodCell(this.grid));				
			}
		}
	}
}
