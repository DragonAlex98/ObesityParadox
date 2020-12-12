package utils;

import java.util.ArrayList;
import java.util.List;

import cell.Cell;
import cell.notImmune.RenalCellCarcinoma;

/**
 * Singleton class that represents the list of all detected RCC 
 * The list must be updated whenever an rcc is detected or killed
 * 
 * @author Marco
 *
 */
public class DetectedRCCList {

	private static DetectedRCCList instance;

	private List<Cell> rccList = new ArrayList<>();

	private DetectedRCCList() {
	}

	public static DetectedRCCList getInstance() {
		if (instance == null) {
			instance = new DetectedRCCList();
		}
		return instance;
	}

	public void add(Cell rcc) {
		if (rcc instanceof RenalCellCarcinoma) {
			rccList.add(rcc);
		}
	}

	public void remove(Cell rcc) {
		if (rcc instanceof RenalCellCarcinoma) {
			rccList.remove(rcc);
		}
	}

	public List<Cell> getRccList() {
		return rccList;
	}

}
