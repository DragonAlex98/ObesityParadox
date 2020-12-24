package cell.immune;

import cell.Cell;
import repast.simphony.space.grid.Grid;
import utils.CellUtils;

/**
 * Tregs suppress activation, proliferation and cytokine production of CD4+ T cells and CD8+ T cells,
 * and are thought to suppress B cells and dendritic cells. Tregs can produce soluble messengers which have
 * a suppressive function, including TGF-beta, IL-10 and adenosine.
 * 
 * TGF-beta suppress proliferation and differentiation and some other function of a majority of cells.
 * Similarly, but in lower quantity, IL-10 is able to suppress cythokine synthesis of IFN-gamma, IL-2, IL-3, TNF-alpha and GM-CSF,
 * produced by cells like macrophages and some kind of T helper cell.

 * @author RICCARDO
 *
 */
public class Treg extends TCell {

	public Treg(int lifespan, Grid<Cell> grid) {
		super(lifespan, grid);
	}

	/**
	 * When active suppress other immune cells proliferation and activation.
	 */
	@Override
	public void actIfActive() {
		CellUtils.releaseTGFbeta(grid, this, 5.0);
		CellUtils.releaseIL10(grid, this, 3.0);
	}

}
