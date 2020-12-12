package utils;

import java.util.ArrayList;
import java.util.List;

import cell.Cell;
import cell.notImmune.Adipocyte;

/**
 * Singleton class that represents the list of all adipocytes in the grid
 * The list must be updated whenever an adipocyte is added to or removed from the grid
 * 
 * @author Marco
 *
 */
public class AdipocyteList {

	private static AdipocyteList instance;
	
	private List<Cell> adipocyteList = new ArrayList<>();
	
	private AdipocyteList() {
	}
	
	public static AdipocyteList getInstance() {
		if (instance == null) {
            instance = new AdipocyteList();
        }
        return instance;
	}
	
	public void add(Cell adipocyte) {
		if (adipocyte instanceof Adipocyte) {
			adipocyteList.add(adipocyte);
		}
	}
	
	public void remove(Cell adipocyte) {
		if (adipocyte instanceof Adipocyte) {
			adipocyteList.remove(adipocyte);
		}
	}

	public List<Cell> getAdipocyteList() {
		return adipocyteList;
	}	

}
