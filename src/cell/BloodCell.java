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

	public BloodCell(Grid<Cell> grid) {
		super(-1, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1)
	public void checkForAngiogenesis() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		if (rccList.size() >= 3) {
			RenalCellCarcinoma rccToReplaceWithBlood = rccList.get(random.nextInt(rccList.size()));
			CellUtils.replaceCell(grid, rccToReplaceWithBlood, new BloodCell(this.grid));
		}
	}
}
