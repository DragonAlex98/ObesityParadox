package commons.cell.immune;

import java.util.Random;

import commons.cell.Cell;
import commons.util.CellUtils;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;

/**
 * This is the representation of a CD4+ Naive T Cell. This kind of cell can be
 * activated from any APC that presents Class II MHC proteins, such as Dendritic
 * Cells, macrophages, B cells and so on. <br>
 * <br>
 * This cell has to pass two phases in order to be activated (Activation and
 * Survival) and then it differentiate into Th1, Th2 or Treg (Regulatory T
 * Cells). <br>
 * <br>
 * We will not represent the first two phases, but the activation will happen
 * when the cell bump into one of the APC cells introduced above.
 * 
 * @author RICCARDO
 *
 */
public class CD4 extends TCell {

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	private float tregPercentage = 0.05f;

	public CD4(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	/**
	 * When active differentiate into Th1 or Treg.
	 */
	@Override
	public void actIfActive() {
		float r = random.nextFloat();
		if (r > tregPercentage) {
			CellUtils.replaceCell(grid, this, new Th1(10, grid));
		} else {
			CellUtils.replaceCell(grid, this, new Treg(10, grid));
		}
	}
}
