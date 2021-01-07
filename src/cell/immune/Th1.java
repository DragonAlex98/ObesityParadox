package cell.immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

/**
 * This cell represent one of the differentiation of a CD4+ T Cell, for instance a Th1 Cell.
 * <br><br>
 * This cell release IFN-gamma and TNF-beta, the first triggers the activation of macrophages and the
 * second triggers the activation of CD8+ T Cell. Release of IL-2 and IL-10 (where the first one is a
 * growth and differentiation factor for T cells) has been reported.
 * <br><br>
 * Actually the activation of CD8+ T Cell is a lot more complicated, as explained in {@code CD8}, since
 * it should undergo a two signal model for its activation. However in this project its activation is
 * simplified.
 * 
 * @author RICCARDO
 *
 */
public class Th1 extends TCell {

	public Th1(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	/**
	 * When active release IFN-gamma and TNF-beta, activating macrophages and CD8+ T Cells within
	 * a certain distance.
	 */
	@Override
	public void actIfActive() {
		CellUtils.releaseIFNGamma(grid, this);
		CellUtils.releaseTNFBeta(grid, this);
		
		this.setActive(false);
	}

}
