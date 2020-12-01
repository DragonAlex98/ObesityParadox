package GameOfLifeProva;

import java.util.Random;

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

public class ContextCreator implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		context.setId("GameOfLifeProva");

		Parameters params = RunEnvironment.getInstance().getParameters();
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Object> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Object>(new StickyBorders(), new SimpleGridAdder<Object>(), false, params.getInteger("width"), params.getInteger("height")));

		System.out.println("Altezza: " + grid.getDimensions().getHeight() + ", Larghezza: " + grid.getDimensions().getWidth());
		
		float percentage = params.getFloat("percentage");
		System.out.println("Percentuale cellule vive: " + percentage*100 + "%");
		
		int liveCellsToCreate = (int)((grid.getDimensions().getWidth() * grid.getDimensions().getHeight()) * percentage);
		System.out.println("Numero cellule vive da creare: " + liveCellsToCreate);
		
		int seed = params.getInteger("randomSeed");
		
		for (int i = 0; i < liveCellsToCreate; i++) {
			Living live = new Living(grid);
			context.add(live);
			Random r = new Random(seed);
			int row;
			int column;
			do {
				row = r.nextInt(grid.getDimensions().getHeight());
				column = r.nextInt(grid.getDimensions().getWidth());
			} while (!grid.moveTo(live, column, row));
			context.add(live);
		}
		
		int deadCellCount = 0;
		for (int r = 0; r < grid.getDimensions().getHeight(); r++) {
			for (int c = 0; c < grid.getDimensions().getWidth(); c++) {
				Dead dead = new Dead(grid);
				context.add(dead);
				if (grid.moveTo(dead, c, r)) {
					context.add(dead);
					deadCellCount++;
				} else {
					context.remove(dead);
				}
			}
		}
		System.out.println("Numero cellule morte da creare: " + deadCellCount);

		return context;
	}
}
