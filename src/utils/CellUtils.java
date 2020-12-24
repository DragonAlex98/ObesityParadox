package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import cell.Cell;
import cell.EmptyCell;
import cell.immune.CD8;
import cell.immune.Dendritic;
import cell.immune.Immune;
import cell.immune.M1;
import cell.immune.PlasmacitoidDendritic;
import cell.immune.TCell;
import cell.immune.Th1;
import repast.simphony.context.Context;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class CellUtils {

	// find my neighbors
	public static Iterable<Cell> getNeighbors(Grid<Cell> grid, Cell cell) {
		MooreQuery<Cell> query = new MooreQuery<Cell>(grid, cell);
		Iterable<Cell> neighbors = query.query();
		return neighbors;
	}
	
	/**
	 * Returns true if the caller has some cell of the specified type in its neighbor.
	 * 
	 * @param <T> Type of the caller.
	 * @param <S> Type of the cell to find.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param cls
	 * @return True if there are neighbor of type S near T.
	 */
	public static <T extends Cell, S extends Cell> boolean hasSpecificCellsNearby(Grid<Cell> grid, T caller, Class<S> cls) {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, caller);
		List<S> rccList = CellUtils.filterNeighbors(neighbors, cls);
		
		return !rccList.isEmpty();
	}
	
	/**
	 * Returns the list of neighbor of the caller cell that are of the specific type.
	 * 
	 * @param <T> Type of the caller.
	 * @param <S> Type of the cell to find.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param cls The cell type to look for.
	 * @return The {@code List} of S that are neighbor of T.
	 */
	public static <T extends Cell, S extends Cell> List<S> getSpecificCellsNearby(Grid<Cell> grid, T caller, Class<S> cls) {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, caller);
		return CellUtils.filterNeighbors(neighbors, cls);
	}

	// list neighbors of a certain Class
	public static <T extends Cell> List<T> filterNeighbors(Iterable<Cell> neighbors, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		for (Cell c : neighbors) {
			if (cls.isInstance(c)) {
				list.add(cls.cast(c));
			}
		}
		return list;
	}

	// method to move a cell in an empty cell location
	@SuppressWarnings("unchecked")
	public static <T extends Cell> void moveCell(Grid<Cell> grid, T cellToMove, EmptyCell emptyCellToMoveTo) {
		GridPoint oldPoint = grid.getLocation(cellToMove);
		//GridPoint newPoint = grid.getLocation(emptyCellToMoveTo);
		Context<Cell> context = ContextUtils.getContext(cellToMove);
		replaceCell(grid, emptyCellToMoveTo, cellToMove);
		
		EmptyCell emptyCell = new EmptyCell(grid);
		context.add(emptyCell);
		grid.moveTo(emptyCell, oldPoint.getX(), oldPoint.getY());
		context.add(emptyCell);
		
		//System.out.println("Spostata cellula da: (" + oldPoint.getX() + ", " + oldPoint.getY() + ") a (" + newPoint.getX() + ", " + newPoint.getY() + ")");
	}

	// method to replace a cell with another
	// can also be used to spawn new cell
	@SuppressWarnings("unchecked")
	public static <T extends Cell, S extends Cell> void replaceCell(Grid<Cell> grid, T cellToReplace, S cellToCreate) {
		GridPoint gpt = grid.getLocation(cellToReplace);
		Context<Cell> context = ContextUtils.getContext(cellToReplace);
		context.remove(cellToReplace);
		context.add(cellToCreate);
		grid.moveTo(cellToCreate, gpt.getX(), gpt.getY());
		context.add(cellToCreate);
	}

	/**
	 * Method to get all cells in the grid of a specific type
	 * 
	 * @param <T> Type of the caller.
	 * @param <S> Type of the cell to find.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param cellTypeToFind The cell type to find as a {@code Class}
	 * @return a {@code Stream} of {@code Cell}s.
	 * 
	 * @see Cell
	 * @see Class
	 * @see Stream
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cell, S extends Cell> Stream<S> getSpecificCells(Grid<Cell> grid, T caller, Class<S> cellTypeToFind) {
		Context<Cell> context = ContextUtils.getContext(caller);
		return (Stream<S>) context.getObjectsAsStream(cellTypeToFind.asSubclass(Cell.class));
	}
	
	/**
	 * Move towards a target cell, moving to the empty cell nearest to the target cell.
	 * 
	 * @param <T> Type of the caller.
	 * @param <S> Type of the cell to move towards.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param target The target cell to move towards.
	 */
	public static <T extends Cell, S extends Cell> void moveTowards(Grid<Cell> grid, T caller, S target) {
		moveTowards(grid, caller, grid.getLocation(target));
	}
	
	/**
	 * Move towards a target cell, moving to the empty cell nearest to the target cell.
	 * 
	 * @param <T> Type of the caller.
	 * @param <S> Type of the cell to move towards.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param target The point of the grid to move towards.
	 */
	public static <T extends Cell, S extends Cell> void moveTowards(Grid<Cell> grid, T caller, GridPoint target) {
		Iterable<Cell> neighbors = CellUtils.getNeighbors(grid, caller);

		List<EmptyCell> emptyCellList = CellUtils.filterNeighbors(neighbors, EmptyCell.class);

		double distanceToTarget = grid.getDistance(grid.getLocation(caller), target);
		
		AtomicReference<EmptyCell> bestEmptyCell = new AtomicReference<>();
		AtomicReference<Double> newDistance = new AtomicReference<Double>(distanceToTarget);
		
		emptyCellList.forEach(cell -> {
			double distance = grid.getDistance(grid.getLocation(cell), target);
			
			if (distance < newDistance.get()) {
				newDistance.set(distance);
				bestEmptyCell.set(cell);
			}
		});
		
		if (bestEmptyCell.get() != null)
			moveCell(grid, caller, bestEmptyCell.get());
	}
	
	/**
	 * Simulate the release of a general substance within a specified distance and the activation of
	 * an immune cell as a consequence of this release.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param <S> Type of cell to stimulate.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param cellTypeToStimulate The cell type to stimulate.
	 * @param distance The distance within which the immune cells specified are activated.
	 */
	private static <T extends Cell, S extends Immune> void releaseSubstanceWithinDistance(Grid<Cell> grid, T caller, Class<S> cellTypeToStimulate, Double distance) {
		// Questo metodo � privato perch� per ora non ha utilit� al di fuori di qui.
		Stream<S> cellList = CellUtils.getSpecificCells(grid, caller, cellTypeToStimulate);
		cellList.filter(element -> grid.getDistance(grid.getLocation(caller), grid.getLocation(element)) <= distance).forEach(element -> element.setActive(true));
	}
	
	/**
	 * Simulate the release of IFN-gamma, stimulating macrophages living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the macrophages cells are activated.
	 */
	public static <T extends Cell> void releaseIFNGamma(Grid<Cell> grid, T caller, Double distance) {
		releaseSubstanceWithinDistance(grid, caller, M1.class, distance);
	}
	
	/**
	 * Simulate the release of TNF-beta, stimulating CD8+ T Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseTNFBeta(Grid<Cell> grid, T caller, Double distance) {
		releaseSubstanceWithinDistance(grid, caller, CD8.class, distance);
	}

	/**
	 * Simulate the release of TNF-aplha, stimulating Dendritic Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseTNFAlpha(Grid<Cell> grid, T caller, Double distance) {
		releaseSubstanceWithinDistance(grid, caller, Dendritic.class, distance);
	}
	
	/**
	 * Simulate the release of Mast Cell mediators, stimulating T Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseMediators(Grid<Cell> grid, T caller, Double distance) {
		releaseSubstanceWithinDistance(grid, caller, TCell.class, distance);
	}

	/**
	 * Simulate the release of TGF-beta, suppressing T Cells proliferation and activation (and differentiation).
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the T Cells are suppressed.
	 */
	public static <T extends Cell> void releaseTGFbeta(Grid<Cell> grid, T caller, Double distance) {
		// inibisce l'attivazione delle t cells
		releaseSubstanceWithinDistance(grid, caller, TCell.class, distance);
		
		// sopprime la proliferazione delle cellule
		// TODO gestire questa cosa quando verr� implementata la proliferazione
	}
	
	/**
	 * Simulate the release of IL-10, suppressing macrophages, dendritic cells and Th1 cells.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the macrophages, dendritic cells and Th1 cells are suppressed.
	 */
	public static <T extends Cell> void releaseIL10(Grid<Cell> grid, T caller, Double distance) {
		// TODO aggiungere M2 quando saranno presenti
		releaseSubstanceWithinDistance(grid, caller, M1.class, distance);
		releaseSubstanceWithinDistance(grid, caller, Th1.class, distance);
		
		releaseSubstanceWithinDistance(grid, caller, Dendritic.class, distance);
		releaseSubstanceWithinDistance(grid, caller, PlasmacitoidDendritic.class, distance);
		
	}
}
