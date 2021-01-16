package threedimensional;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class DeadCell extends Cell {

	private Grid<Cell> grid;

	public DeadCell(Grid<Cell> grid) {
		this.grid = grid;
		this.setState(0);
	}

	@ScheduledMethod(start = 0, interval = 1, priority = 4)
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

	@ScheduledMethod(start = 0, interval = 1, priority = 1)
	public void step2() {
		if (this.getState() == 1) {
			GridPoint gpt = grid.getLocation(this);
			Context<Cell> context = ContextUtils.getContext(this);
			context.remove(this);
			LivingCell livingCell = new LivingCell(grid);
			context.add(livingCell);
			grid.moveTo(livingCell, gpt.getX(), gpt.getY(), gpt.getZ());
			context.add(livingCell);
		}
	}
}