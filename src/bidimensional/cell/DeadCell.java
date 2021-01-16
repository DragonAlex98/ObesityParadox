package bidimensional.cell;

import bidimensional.utils.CellUtils;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.space.grid.Grid;

public class DeadCell extends Cell {

	public DeadCell(Grid<Cell> grid) {
		super(-1, grid);
	}
	
	@ScheduledMethod(start = 1, interval = 1, priority = 1)
	public void tickAndCheckDeath() {
		//TODO: decide how to calculate the value
		if (this.getAge() == 10) {
			CellUtils.replaceCell(this.grid, this, new EmptyCell(this.grid));
		}
	}

}
