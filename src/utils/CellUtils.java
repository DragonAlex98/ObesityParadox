package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import cell.Cell;
import cell.EmptyCell;
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
		GridPoint newPoint = grid.getLocation(emptyCellToMoveTo);
		Context<Cell> context = ContextUtils.getContext(cellToMove);
		replaceCell(grid, emptyCellToMoveTo, cellToMove);
		
		EmptyCell emptyCell = new EmptyCell(grid);
		context.add(emptyCell);
		grid.moveTo(emptyCell, oldPoint.getX(), oldPoint.getY());
		context.add(emptyCell);
		
		System.out.println("Spostata cellula da: (" + oldPoint.getX() + ", " + oldPoint.getY() + ") a (" + newPoint.getX() + ", " + newPoint.getY() + ")");
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

	// method to get all cells in the grid of a specific type
	@SuppressWarnings("unchecked")
	public static <T extends Cell, S extends Cell> Stream<Cell> getSpecificCells(Grid<Cell> grid, T caller, Class<S> cellTypeToFind) {
		Context<Cell> context = ContextUtils.getContext(caller);
		return context.getObjectsAsStream(cellTypeToFind.asSubclass(Cell.class));
	}
}
