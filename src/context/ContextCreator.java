package context;

import java.util.Random;

import cell.Cell;
import cell.EmptyCell;
import cell.immune.CD4;
import cell.immune.CD8;
import cell.immune.Dendritic;
import cell.immune.MastCell;
import cell.notImmune.Adipocyte;
import cell.notImmune.RenalCellCarcinoma;
import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.SimpleGridAdder;
import repast.simphony.space.grid.StickyBorders;

public class ContextCreator implements ContextBuilder<Cell> {

	@Override
	public Context<Cell> build(Context<Cell> context) {
		context.setId("GameOfLifeProva");

		Parameters params = RunEnvironment.getInstance().getParameters();
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Cell> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Cell>(new StickyBorders(), new SimpleGridAdder<Cell>(), false, params.getInteger("width"), params.getInteger("height")));
		
		int size = grid.getDimensions().getWidth() * grid.getDimensions().getHeight();

		System.out.println("Altezza: " + grid.getDimensions().getHeight() + ", Larghezza: " + grid.getDimensions().getWidth());
		
		Orientation bloodVesselOrientation = Orientation.valueOf(params.getString("bloodVesselOrientation"));
		System.out.println("Orientamento vaso sanguigno: " + bloodVesselOrientation);
		
		Orientation lymphNodeOrientation = Orientation.valueOf(params.getString("lymphNodeOrientation"));
		System.out.println("Orientamento linfonodo: " + lymphNodeOrientation);
		
		int reproTime = params.getInteger("reproTime");
		System.out.println("Tempo riproduzione tumore: " + reproTime);
		
		int reproFactor = params.getInteger("reproFactor");
		System.out.println("Fattore riproduzione tumore: " + reproFactor);
		
		if (bloodVesselOrientation == lymphNodeOrientation) {
			System.out.println("Il vaso sanguigno non si può trovare nella stessa direzione del linfonodo!");
			return null;
		}
		
		float cd8Percentage = params.getFloat("cd8Percentage");
		System.out.println("Percentuale cellule CD8: " + cd8Percentage*100 + "%");
		int cd8CellsToCreate = (int)(size * cd8Percentage);
		System.out.println("Numero cellule CD8 da creare: " + cd8CellsToCreate);
		
		float mastPercentage = params.getFloat("mastPercentage");
		System.out.println("Percentuale cellule Mast: " + mastPercentage*100 + "%");
		int mastCellsToCreate = (int)(size * mastPercentage);
		System.out.println("Numero cellule Mast da creare: " + mastCellsToCreate);
		
		float adipocytePercentage = params.getFloat("adipocytePercentage");
		System.out.println("Percentuale cellule Adipocyte: " + adipocytePercentage*100 + "%");
		int adipocyteCellsToCreate = (int)(size * adipocytePercentage);
		System.out.println("Numero cellule Adipocyte da creare: " + adipocyteCellsToCreate);
		
		float rccPercentage = params.getFloat("rccPercentage");
		System.out.println("Percentuale cellule RCC: " + rccPercentage*100 + "%");
		int rccCellsToCreate = (int)(size * rccPercentage);
		System.out.println("Numero cellule RCC da creare: " + rccCellsToCreate);

		float cd4Percentage = params.getFloat("cd4Percentage");
		System.out.println("Percentuale cellule CD4: " + cd4Percentage*100 + "%");
		int cd4CellsToCreate = (int)(size * cd4Percentage);
		System.out.println("Numero cellule CD4 da creare: " + cd4CellsToCreate);

		float dendriticPercentage = params.getFloat("dendriticPercentage");
		System.out.println("Percentuale cellule Dendritic: " + dendriticPercentage*100 + "%");
		int dendriticCellsToCreate = (int)(size * dendriticPercentage);
		System.out.println("Numero cellule Dendritic da creare: " + dendriticCellsToCreate);
		
		int left = size - cd8CellsToCreate - mastCellsToCreate - adipocyteCellsToCreate - rccCellsToCreate - cd4CellsToCreate - dendriticCellsToCreate;
		
		if (left < 0) {
			System.out.println("Numero di cellule da creare maggiore del numero di celle della griglia!");
			return null;
		}
		
		System.out.println("Numero cellule Empty da creare: " + left);
		
		int seed = params.getInteger("randomSeed");
		Random random = new Random(seed);
		
		for (int i = 0; i < cd8CellsToCreate; i++) {
			CD8 cd8 = new CD8(10, grid, 1.0);
			context.add(cd8);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(cd8, x, y));
			context.add(cd8);
		}
		
		for (int i = 0; i < mastCellsToCreate; i++) {
			MastCell mast = new MastCell(10, grid);
			context.add(mast);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(mast, x, y));
			context.add(mast);
		}
		
		for (int i = 0; i < adipocyteCellsToCreate; i++) {
			Adipocyte adipocyte = new Adipocyte(10, grid);
			context.add(adipocyte);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(adipocyte, x, y));
			context.add(adipocyte);
		}
		
		for (int i = 0; i < rccCellsToCreate; i++) {
			RenalCellCarcinoma rcc = new RenalCellCarcinoma(10, grid, reproTime, reproFactor);
			context.add(rcc);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(rcc, x, y));
			context.add(rcc);
		}
		
		for (int i = 0; i < cd4CellsToCreate; i++) {
			CD4 cd4 = new CD4(10, grid);
			context.add(cd4);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(cd4, x, y));
			context.add(cd4);
		}
		
		for (int i = 0; i < dendriticCellsToCreate; i++) {
			Dendritic dendritic = new Dendritic(10, grid);
			context.add(dendritic);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(dendritic, x, y));
			context.add(dendritic);
		}
		
		int emptyCellsToCreate = 0;
		if (left > 0) {
			for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
				for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
					EmptyCell empty = new EmptyCell(grid);
					context.add(empty);
					if (grid.moveTo(empty, x, y)) {
						context.add(empty);
						emptyCellsToCreate++;
					} else {
						context.remove(empty);
					}
				}
			}
		}
		
		System.out.println("Numero cellule Empty create: " + emptyCellsToCreate);

		return context;
	}
}
