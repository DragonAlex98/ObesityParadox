package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

public abstract class Immune extends Cell {
	
	public Immune(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	@ScheduledMethod(start = 1, interval = 1)
	public void moveTo() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.getGrid(), this);
		List<EmptyCell> emptyCells = CellUtils.filterNeighbors(neighbors, EmptyCell.class);
		
		Parameters params = RunEnvironment.getInstance().getParameters();
		int seed = params.getInteger("randomSeed");
		Random random = new Random(seed);
		
		if (!emptyCells.isEmpty()) {
			EmptyCell emptyCellToReplace = emptyCells.get(random.nextInt(emptyCells.size()));
			CellUtils.moveCell(this.getGrid(), this, emptyCellToReplace);			
		}
	}
	
	// inhibit or excite another immune cell
	public abstract void act();	

}
