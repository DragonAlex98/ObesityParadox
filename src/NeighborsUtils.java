import java.util.ArrayList;
import java.util.List;

import cell.Cell;
import repast.simphony.query.space.grid.MooreQuery;
import repast.simphony.space.grid.Grid;


public class NeighborsUtils {
	
	// find my neighbors
	public static Iterable<Cell> find_neighbor(Grid<Object> grid, Cell cell) {
		MooreQuery<Cell> query = new MooreQuery(grid, cell);
		Iterable<Cell> neighbors = query.query();
		return neighbors;
	}
	
	// list neighbors of a certain Class
	public static <T> List<Cell> list_neighbor(Iterable<Cell> neighbors, Class cls ) {
		List<Cell> list = new ArrayList<Cell>();
		for (Cell c : neighbors) {
			if (cls.isInstance(c)) {
				list.add(c);
			} 
		}
		return list;
	}

}
