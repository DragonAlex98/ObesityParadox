package threedimensional.cell;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class LivingCell extends Cell {

	private Grid<Cell> grid;

	public LivingCell(Grid<Cell> grid) {
		this.grid = grid;
		this.setState(1);
	}

	@ScheduledMethod(start = 0, interval = 1, priority = 3)
	public void step1() {
		MooreQuery<Cell> query = new MooreQuery<Cell>(grid, this, 1, 1, 1);
		int neighbours = 0;
		for (Object o : query.query()) {
			if (o instanceof LivingCell) {
				neighbours++;
			}
		}

		if (neighbours >= 8 && neighbours <= 11) {
			this.setState(1);
		} else {
			this.setState(0);
		}
	}

	@ScheduledMethod(start = 0, interval = 1, priority = 2)
	public void step2() {
		if (this.getState() == 0) {
			GridPoint gpt = grid.getLocation(this);
			Context<Cell> context = ContextUtils.getContext(this);
			context.remove(this);
			DeadCell deadCell = new DeadCell(grid);
			context.add(deadCell);
			grid.moveTo(deadCell, gpt.getX(), gpt.getY(), gpt.getZ());
			context.add(deadCell);
		}
	}
}
