package bidimensional.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Stream;

import bidimensional.cell.Cell;
import bidimensional.cell.EmptyCell;
import bidimensional.cell.immune.CD8;
import bidimensional.cell.immune.Dendritic;
import bidimensional.cell.immune.Immune;
import bidimensional.cell.immune.M1;
import bidimensional.cell.immune.M2;
import bidimensional.cell.immune.NKCell;
import bidimensional.cell.immune.PlasmacytoidDendritic;
import bidimensional.cell.immune.TCell;
import bidimensional.cell.immune.Th1;
import repast.simphony.context.Context;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridPoint;
import repast.simphony.util.ContextUtils;

public class CellUtils {

	/**
	 * To retrieve the neighbors of a specific cell
	 * 
	 * @param grid The grid where the cells are living.
	 * @param cell Cell used to find its neighbors
	 * @return An Iterable in which there are all the neighbors of the input cell
	 */
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

	
	/**
	 * To retrieve a list of neighbors of a certain Class
	 * 
	 * @param <T> Type of the Class used to filter the list of neighbors
	 * @param neighbors List of neighbors to filter.
	 * @param cls The cell type to look for.
	 * @return The list of neighbors containing only the cell of type T
	 */
	public static <T extends Cell> List<T> filterNeighbors(Iterable<Cell> neighbors, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		for (Cell c : neighbors) {
			if (cls.isInstance(c)) {
				list.add(cls.cast(c));
			}
		}
		return list;
	}

	/**
	 * Method to move a cell in an empty cell location
	 * 
	 * @param <T> Cell to move
	 * @param grid The grid where the cell is living.
	 * @param cellToMove Cell to move towards the emptyCellToMoveTo
	 * @param emptyCellToMoveTo Empty cell to replace with the cellToMove
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cell> void moveCell(Grid<Cell> grid, T cellToMove, EmptyCell emptyCellToMoveTo) {
		GridPoint oldPoint = grid.getLocation(cellToMove);
		Context<Cell> context = ContextUtils.getContext(cellToMove);
		replaceCell(grid, emptyCellToMoveTo, cellToMove);
		
		EmptyCell emptyCell = new EmptyCell(grid);
		context.add(emptyCell);
		grid.moveTo(emptyCell, oldPoint.getX(), oldPoint.getY());
		context.add(emptyCell);
	}

	/**
	 * Method to replace a cell with another cell
	 * Can also be used to spawn new cell
	 * 
	 * @param <T> Type of the Cell to be replaced
	 * @param <S> Type of the Cell that must replace the old one
	 * @param grid The grid where the cell is living.
	 * @param cellToReplace Cell to be replaced
	 * @param cellToCreate Cell that must replace the old one
	 */
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
	 * Returns the distance within which substances are released.
	 * 
	 * @param grid The grid where the cells are living.
	 * @return the distance within which the substances are released.
	 */
	public static double getDistanceToReleaseSubstances(Grid<Cell> grid) {
		return Math.max(2, Math.min(grid.getDimensions().getWidth(), grid.getDimensions().getHeight()) / 5);
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
	private static <T extends Cell, S extends Immune> void releaseSubstanceWithinDistance(Grid<Cell> grid, T caller, Class<S> cellTypeToStimulate, Consumer<S> action) {
		// Questo metodo � privato perch� per ora non ha utilit� al di fuori di qui.
		Stream<S> cellList = CellUtils.getSpecificCells(grid, caller, cellTypeToStimulate);
		cellList.filter(element -> grid.getLocation(caller) != grid.getLocation(element) && grid.getDistance(grid.getLocation(caller), grid.getLocation(element)) <= getDistanceToReleaseSubstances(grid)).forEach(action);
	}
	
	/**
	 * Simulate the release of IFN-gamma, stimulating macrophages living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the macrophages cells are activated.
	 */
	public static <T extends Cell> void releaseIFNGamma(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, M1.class, element -> element.setActive(true));
		releaseSubstanceWithinDistance(grid, caller, NKCell.class, element -> element.setActive(true));
		// releaseSubstanceWithinDistance(grid, caller, M2.class, distance, element -> element.setActive(true));
	}
	
	/**
	 * Simulate the release of TNF-beta, stimulating CD8+ T Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseTNFBeta(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, CD8.class, element -> element.increaseCellGrowth(0.05f));
	}

	/**
	 * Simulate the release of TNF-aplha, stimulating Dendritic Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseTNFAlpha(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, Dendritic.class, element -> element.setActive(true));
	}
	
	/**
	 * Simulate the release of Mast Cell mediators, stimulating T Cells living within the specified distance.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the CD8+ T Cells are activated.
	 */
	public static <T extends Cell> void releaseMediators(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, TCell.class, element -> element.setActive(true));
	}

	/**
	 * Simulate the release of TGF-beta, suppressing T Cells proliferation and activation (and differentiation).
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the T Cells are suppressed.
	 */
	public static <T extends Cell> void releaseTGFbeta(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, TCell.class, element -> {
			element.setActive(false);
			element.decreaseCellGrowth(0.05f);
		});
		releaseSubstanceWithinDistance(grid, caller, M2.class, element -> element.setActive(true));
	}
	
	/**
	 * Simulate the release of IL-10, suppressing macrophages, dendritic cells and Th1 cells.
	 * 
	 * @param <T> Type of object calling this method.
	 * @param grid The grid where the cells are living.
	 * @param caller The caller of the method.
	 * @param distance The distance within which the macrophages, dendritic cells and Th1 cells are suppressed.
	 */
	public static <T extends Cell> void releaseIL10(Grid<Cell> grid, T caller) {
		releaseSubstanceWithinDistance(grid, caller, M1.class, element -> element.setActive(false));
		releaseSubstanceWithinDistance(grid, caller, M2.class, element -> element.setActive(true));
		releaseSubstanceWithinDistance(grid, caller, Th1.class, element -> element.setActive(false));

		releaseSubstanceWithinDistance(grid, caller, Dendritic.class, element -> element.setActive(false));
		releaseSubstanceWithinDistance(grid, caller, PlasmacytoidDendritic.class, element -> element.setActive(false));
		
	}
	
	/**
	 * Return a variable that is within the range [minPerc, maxPerc] that is directly related with the BMI value.
	 * An example: BMI that ranges between 15 and 40 and the percentage of adipocyte that ranges between 0.02 and 0.08.
	 * <ul>
	 * <h1>
	 * BMI: 15
	 * </h1>
	 * limitVariableToBMI(15, 0.02, 0.08) => 0.02
	 * </ul>
	 * 
	 * <ul>
	 * <h1>
	 * BMI: 27
	 * </h1>
	 * limitVariableToBMI(27, 0.02, 0.08) => 0.049
	 * </ul>
	 * 
	 * <ul>
	 * <h1>
	 * BMI: 40
	 * </h1>
	 * limitVariableToBMI(40, 0.02, 0.08) => 0.08
	 * </ul>
	 * 
	 * @param bmi the bmi value
	 * @param minPerc the minimum percentage of the variable
	 * @param maxPerc the maximum percentage of the variable
	 * @return the percentage 
	 */
	public static double limitVariableToBMI(int bmi, double minPerc, double maxPerc) {
		int maxBMI = 40;
		int minBMI = 15;
		return Math.round((maxPerc - ((double)(maxBMI - bmi) / (maxBMI - minBMI)) * (maxPerc - minPerc)) * 100.0) / 100.0;
	}
	
	/**
	 * Return a variable that is within the range [minPerc, maxPerc] that is inversely related with the BMI value.
	 * An example: BMI that ranges between 15 and 40 and the percentage of natural killer that ranges between 0.02 and 0.08.
	 * <ul>
	 * <h1>
	 * BMI: 15
	 * </h1>
	 * inverseLimitVariableToBMI(15, 0.02, 0.08) => 0.08
	 * </ul>
	 * 
	 * <ul>
	 * <h1>
	 * BMI: 27
	 * </h1>
	 * inverseLimitVariableToBMI(27, 0.02, 0.08) => 0.051
	 * </ul>
	 * 
	 * <ul>
	 * <h1>
	 * BMI: 40
	 * </h1>
	 * inverseLimitVariableToBMI(40, 0.02, 0.08) => 0.02
	 * </ul>
	 * 
	 * @param bmi the bmi value
	 * @param minPerc the minimum percentage of the variable
	 * @param maxPerc the maximum percentage of the variable
	 * @return the percentage 
	 */
	public static double inverseLimitVariableToBMI(int bmi, double minPerc, double maxPerc) {
		int maxBMI = 40;
		int minBMI = 15;
		return Math.round((maxPerc + minPerc - (maxPerc - ((double)(maxBMI - bmi) / (maxBMI - minBMI)) * (maxPerc - minPerc))) * 100.0) / 100.0;
	}
}
