package cell.immune;

import java.util.List;
import java.util.Random;

import cell.Cell;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

/**
 * MCs may mobilize and modulate the activity of T cells, regulatory T (Treg)
 * cells, antigen-presenting cells (APC) and myeloid-derived suppressor cells
 * (MDSCs) with their soluble mediators or through cell-cell contact.
 * Interacting with other immune cells, MC may regulate both innate and adaptive
 * immunity, tuning the host responses toward developing cancers and influencing
 * the clinical outcome of several tumors. During degranulation, MCs release a
 * large variety of mediators that could intervene in the immune response in an
 * unpredictable manner. Also, the context and the timing of MC activation play
 * a fundamental role on the positive or negative effect of MCs on tumor growth
 * and progression.
 * 
 * @author Marco
 *
 */
public class MastCell extends Immune {

	// if I am pro tumor or anti tumor
	private boolean proTumor = false;

	// percentage of become pro tumor
	private static float proTumorPercentage = 0.5f;

	private static Random random = new Random(RunEnvironment.getInstance().getParameters().getInteger("randomSeed"));

	public MastCell(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	/**
	 * If I am active and antiTumor I activate Tcell in my neighbor to help the
	 * immune response
	 */
	@Override
	public void actIfActive() {
		if (!proTumor) {
			CellUtils.releaseMediators(this.grid, this);
			this.setActive(false);
		}
	}

	/**
	 * I become mature is a non_self Rcc is close to me, and I become proTumor
	 * randomly
	 */
	@Override
	public void actIfNotActive() {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, this);
		List<RenalCellCarcinoma> rccList = CellUtils.filterNeighbors(neighbors, RenalCellCarcinoma.class);
		if (!rccList.isEmpty() && rccList.stream().filter(rcc -> !rcc.isSelf()).findAny().isPresent()) {
			this.setActive(true);
			// TODO is it correct?? or it must be moved into the constructor??
			float randomProTumor = random.nextFloat();
			if (randomProTumor < proTumorPercentage) {
				this.proTumor = true;
			}
		}
	}

	public boolean isProTumor() {
		return proTumor;
	}

	public void setProTumor(boolean proTumor) {
		this.proTumor = proTumor;
	}
	
	@Override
	protected MastCell clone() throws CloneNotSupportedException {
		MastCell cell = (MastCell) super.clone();
		cell.setProTumor(this.proTumor);;
		return cell;
	}

}
