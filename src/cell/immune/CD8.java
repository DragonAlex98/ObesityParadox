package cell.immune;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import cell.Cell;
import cell.DeadCell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

/**
 * Implementation of a CD8+ Cytotoxic T Cell. The implementation of a CD8+ Naive T Cell is not present
 * because they are not represented in the grid when they are in the Naive state. In this state they are
 * supposed to be in the lymphnode. When they are activated from an APC they move from the lymphnode to the grid,
 * so the grid will only contain already active cells.
 * 
 * @author RICCARDO
 *
 */
public class CD8 extends TCell {
	
	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));
	
	private double killProb;
	
	public CD8(int lifespan, Grid<Cell> grid, double killProb) {
		super(lifespan, grid);
		this.killProb = killProb;
	}
	
	/**
	 * Default behaviour if it is not active (Naive).
	 * Otherwise it moves towards the nearest RCC cell.
	 */
	@Override
	public void move() {
		if (!this.isActive()) {
			super.move();
			return;
		}
	
		Stream<RenalCellCarcinoma> rccInGrid = CellUtils.getSpecificCells(this.grid, this, RenalCellCarcinoma.class);
		
		AtomicReference<RenalCellCarcinoma> nearestRcc = new AtomicReference<>();
		AtomicReference<Double> distanceToNearest = new AtomicReference<Double>(Double.POSITIVE_INFINITY);
		
		rccInGrid.forEach(elem -> {
			double distance = this.grid.getDistance(this.grid.getLocation(this), this.grid.getLocation(elem));
			
			if (distance < distanceToNearest.get()) {
				distanceToNearest.set(distance);
				nearestRcc.set(elem);
			}
		});
		
		if (nearestRcc.get() != null)
			CellUtils.moveTowards(this.grid, this, nearestRcc.get());
	}

	/**
	 * Kills a RCC near to this cell.
	 */
	@Override
	public void actIfActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(this.grid, this);
		
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		
		rccList.removeIf(r -> r.isSelf());
		if (!rccList.isEmpty()) {
			if (random.nextFloat() < killProb) {
				CellUtils.replaceCell(this.grid, rccList.get(random.nextInt(rccList.size())), new DeadCell(this.grid));				
			}
		}
	}

	public double getKillProb() {
		return killProb;
	}

	public void setKillProb(double killProb) {
		this.killProb = killProb;
	}
	
	@Override
	protected CD8 clone() throws CloneNotSupportedException {
		CD8 cell = (CD8) super.clone();
		cell.setKillProb(0);
		return cell;
	}
}
