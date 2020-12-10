package notImmune;

import java.util.ArrayList;
import java.util.List;

import immune.MastCelll;
import repast.simphony.context.Context;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class RenalCellCarcinoma extends NotImmune {

	// number of ticks to wait for the next tumor expansion
	private int repro_time = 0;

	// how much it expands
	private int repro_factor = 0;

	public RenalCellCarcinoma(int lifespan, Grid<Object> grid, int repro_time, int repro_factor) {
		super(lifespan, grid);
		this.repro_time = repro_time;
		this.repro_factor = repro_factor;
	}
	
	//TODO  Can RCC dead? How can it reproduce?
	
	// after merge, move this method to superclass Cell, now it is located into Immune Class
	private Iterable<RenalCellCarcinoma> check_neighbor() {
		MooreQuery<RenalCellCarcinoma> query = new MooreQuery(this.getGrid(), this);
		Iterable<RenalCellCarcinoma> neighbors = query.query();
		return neighbors;
	}

	// check if the time to expand has arrived or if there is a Mast Cell in its neighborhood
	public void grow() {
		Iterable<RenalCellCarcinoma> neighbors = check_neighbor();
		if (RunEnvironment.getInstance().getCurrentSchedule().getTickCount() % repro_time == 0.0) {
			check_reproduction(neighbors);
		} else {
			for (Object o : neighbors) {
				if (o instanceof MastCelll) {
					check_reproduction(neighbors);
					break;
				}
			}
		}
	}

	// creates new RenalCellCarcinoma
	private void check_reproduction(Iterable<RenalCellCarcinoma> neighbors) {
		List<Adipocyte> list = new ArrayList<Adipocyte>();
		for (Object o : neighbors) {
			if (o instanceof Adipocyte) {
				list.add((Adipocyte) o);
			}
		}// if there is at least one adipocyte in my neighbors I grow up
		if (!list.isEmpty()) {
			int count = this.repro_factor < list.size() ? this.repro_factor : list.size();
			for (int i = 0; i < count; i++) {
				reproduce(list.get(i));
			}
		}		
	}

	// replace adipocyte with new RCC, update the context
	private void reproduce(Adipocyte adipocyte) {
		GridPoint gpt = this.getGrid().getLocation(adipocyte);
		Context<Object> context = ContextUtils.getContext(adipocyte);
		context.remove(adipocyte);
		RenalCellCarcinoma rcc = new RenalCellCarcinoma(this.getLifespan(), this.getGrid(), this.repro_time, this.repro_factor);
		context.add(rcc);
		this.getGrid().moveTo(rcc, gpt.getX(), gpt.getY());
		context.add(rcc);
	}
	
	public int getRepro_time() {
		return repro_time;
	}

	public void setRepro_time(int repro_time) {
		this.repro_time = repro_time;
	}

	public int getRepro_factor() {
		return repro_factor;
	}

	public void setRepro_factor(int repro_factor) {
		this.repro_factor = repro_factor;
	}

}
