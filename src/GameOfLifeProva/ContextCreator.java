package GameOfLifeProva;

import java.util.Random;

import cell.Cell;
import cell.immune.CD8;
import cell.immune.MastCell;
import cell.notImmune.Adipocyte;
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
		
		float cd8Percentage = params.getFloat("cd8Percentage");
		System.out.println("Percentuale cellule CD8: " + cd8Percentage*100 + "%");
		int cd8CellsToCreate = (int)(size * cd8Percentage);
		System.out.println("Numero cellule CD8 da creare: " + cd8CellsToCreate);
		
		float mastPercentage = params.getFloat("mastPercentage");
		System.out.println("Percentuale cellule Mast: " + mastPercentage*100 + "%");
		int mastCellsToCreate = (int)(size * mastPercentage);
		System.out.println("Numero cellule Mast da creare: " + mastCellsToCreate);
		
		int left = size - cd8CellsToCreate - mastCellsToCreate;
		
		if (left < 0) {
			System.out.println("Numero di cellule da creare maggiore del numero di celle della griglia!");
			return null;
		}
		
		System.out.println("Numero cellule Adipocyte da creare: " + left);
		
		int seed = params.getInteger("randomSeed");
		
		for (int i = 0; i < cd8CellsToCreate; i++) {
			CD8 cd8 = new CD8(10, grid, 1.0);
			context.add(cd8);
			Random r = new Random(seed);
			int row;
			int column;
			do {
				row = r.nextInt(grid.getDimensions().getHeight());
				column = r.nextInt(grid.getDimensions().getWidth());
			} while (!grid.moveTo(cd8, column, row));
			context.add(cd8);
		}
		
		for (int i = 0; i < mastCellsToCreate; i++) {
			MastCell mast = new MastCell(10, grid);
			context.add(mast);
			Random r = new Random(seed);
			int row;
			int column;
			do {
				row = r.nextInt(grid.getDimensions().getHeight());
				column = r.nextInt(grid.getDimensions().getWidth());
			} while (!grid.moveTo(mast, column, row));
			context.add(mast);
		}
		
		int adipocyteCellsToCreate = 0;
		if (left > 0) {
			for (int r = 0; r < grid.getDimensions().getHeight(); r++) {
				for (int c = 0; c < grid.getDimensions().getWidth(); c++) {
					Adipocyte adipocyte = new Adipocyte(10, grid);
					context.add(adipocyte);
					if (grid.moveTo(adipocyte, c, r)) {
						context.add(adipocyte);
						adipocyteCellsToCreate++;
					} else {
						context.remove(adipocyte);
					}
				}
			}
		}		
		
		System.out.println("Numero cellule Adipocyte create: " + adipocyteCellsToCreate);

		return context;
	}
}
