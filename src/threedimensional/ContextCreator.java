package threedimensional;

import java.util.Random;

import repast.simphony.context.Context;
import repast.simphony.context.space.grid.GridFactory;
import repast.simphony.context.space.grid.GridFactoryFinder;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.grid.Grid;
import repast.simphony.space.grid.GridBuilderParameters;
import repast.simphony.space.grid.InfiniteBorders;
import repast.simphony.space.grid.SimpleGridAdder;

public class ContextCreator implements ContextBuilder<Cell> {

	@Override
	public Context<Cell> build(Context<Cell> context) {
		context.setId("ObesityParadox3D");

		Parameters params = RunEnvironment.getInstance().getParameters();

		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Cell> grid = gridFactory.createGrid("grid3d", context, new GridBuilderParameters<Cell>(new InfiniteBorders<>(), new SimpleGridAdder<Cell>(), false, params.getInteger("width"), params.getInteger("height"), params.getInteger("depth")));

		System.out.println("Altezza: " + grid.getDimensions().getHeight() + ", Larghezza: " + grid.getDimensions().getWidth() + ", Profondità: " + grid.getDimensions().getDepth());
		
		float percentage = params.getFloat("percentage");
		System.out.println("Percentuale cellule vive: " + percentage*100 + "%");
		
		int liveCellsToCreate = (int)((grid.getDimensions().getWidth() * grid.getDimensions().getHeight() * grid.getDimensions().getDepth()) * percentage);
		System.out.println("Numero cellule vive da creare: " + liveCellsToCreate);
		
		int deadCellsToCreate = grid.getDimensions().getHeight() * grid.getDimensions().getWidth() * grid.getDimensions().getDepth() - liveCellsToCreate;
		System.out.println("Numero cellule morte da creare: " + deadCellsToCreate);
		
		int seed = params.getInteger("randomSeed");
		
		for (int i = 0; i < liveCellsToCreate; i++) {
			LivingCell live = new LivingCell(grid);
			context.add(live);
			Random r = new Random(seed);
			int x;
			int y;
			int z;
			do {
				x = r.nextInt(grid.getDimensions().getHeight());
				y = r.nextInt(grid.getDimensions().getWidth());
				z = r.nextInt(grid.getDimensions().getDepth());
			} while (!grid.moveTo(live, x, y, z));
			context.add(live);
		}

		for (int x = 0; x < grid.getDimensions().getHeight(); x++) {
			for (int y = 0; y < grid.getDimensions().getWidth(); y++) {
				for (int z = 0; z < grid.getDimensions().getDepth(); z++) {
					DeadCell dead = new DeadCell(grid);
					context.add(dead);
					if (grid.moveTo(dead, x, y, z)) {
						context.add(dead);
					} else {
						context.remove(dead);
					}
				}
			}
		}

		return context;
	}
}
