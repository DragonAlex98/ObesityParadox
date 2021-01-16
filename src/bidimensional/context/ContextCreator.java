package bidimensional.context;

import java.util.Random;

import commons.cell.Cell;
import commons.cell.EmptyCell;
import commons.cell.immune.CD4;
import commons.cell.immune.CD8;
import commons.cell.immune.Dendritic;
import commons.cell.immune.M1;
import commons.cell.immune.M2;
import commons.cell.immune.MastCell;
import commons.cell.immune.NKCell;
import commons.cell.immune.PlasmacytoidDendritic;
import commons.cell.immune.Th1;
import commons.cell.immune.Treg;
import commons.cell.notimmune.Adipocyte;
import commons.cell.notimmune.BloodCell;
import commons.cell.notimmune.RenalCellCarcinoma;
import commons.util.CellUtils;
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
		context.setId("ObesityParadox");

		Parameters params = RunEnvironment.getInstance().getParameters();
		
		GridFactory gridFactory = GridFactoryFinder.createGridFactory(null);
		Grid<Cell> grid = gridFactory.createGrid("grid", context, new GridBuilderParameters<Cell>(new StickyBorders(), new SimpleGridAdder<Cell>(), false, params.getInteger("width"), params.getInteger("height")));
		
		float percentageOfImmuneCellsToCreate = 0.10f;
		int size = grid.getDimensions().getWidth() * grid.getDimensions().getHeight();

		System.out.println("Altezza: " + grid.getDimensions().getHeight() + ", Larghezza: " + grid.getDimensions().getWidth());
		
		Orientation bloodVesselOrientation = Orientation.valueOf(params.getString("bloodVesselOrientation"));
		System.out.println("Orientamento vaso sanguigno: " + bloodVesselOrientation);
		
		int bloodCellsToCreate = 0;
		switch (bloodVesselOrientation) {
		case NORTH:
			bloodCellsToCreate = grid.getDimensions().getWidth();
			for (int i = 0; i < bloodCellsToCreate; i++) {
				BloodCell bloodCell = new BloodCell(grid);
				context.add(bloodCell);
				grid.moveTo(bloodCell, i, grid.getDimensions().getHeight()-1);
				context.add(bloodCell);
			}
			break;
		case SOUTH:
			bloodCellsToCreate = grid.getDimensions().getWidth();
			for (int i = 0; i < bloodCellsToCreate; i++) {
				BloodCell bloodCell = new BloodCell(grid);
				context.add(bloodCell);
				grid.moveTo(bloodCell, i, 0);
				context.add(bloodCell);
			}
			break;
		case WEST:
			bloodCellsToCreate = grid.getDimensions().getHeight();
			for (int i = 0; i < bloodCellsToCreate; i++) {
				BloodCell bloodCell = new BloodCell(grid);
				context.add(bloodCell);
				grid.moveTo(bloodCell, 0, i);
				context.add(bloodCell);
			}
			break;
		case EAST:
			bloodCellsToCreate = grid.getDimensions().getHeight();
			for (int i = 0; i < bloodCellsToCreate; i++) {
				BloodCell bloodCell = new BloodCell(grid);
				context.add(bloodCell);
				grid.moveTo(bloodCell, grid.getDimensions().getWidth()-1, i);
				context.add(bloodCell);
			}
			break;
		}
		
		System.out.println("Numero cellule Blood create: " + bloodCellsToCreate);
		
		Orientation lymphNodeOrientation = Orientation.valueOf(params.getString("lymphNodeOrientation"));
		System.out.println("Orientamento linfonodo: " + lymphNodeOrientation);
		
		if (bloodVesselOrientation == lymphNodeOrientation) {
			System.out.println("Il vaso sanguigno non si può trovare nella stessa direzione del linfonodo!");
			return null;
		}
		
		int bmi = params.getInteger("bmi");
		System.out.println("BMI: " + bmi);
		
		int reproTime = params.getInteger("reproTime");
		System.out.println("Tempo riproduzione tumore: " + reproTime);
		
		int reproFactor = params.getInteger("reproFactor");
		System.out.println("Fattore riproduzione tumore: " + reproFactor);
		
		float mutationPercentage = params.getFloat("mutationPercentage");
		System.out.println("Percentuale mutazione tumore: " + mutationPercentage);
		
		float disableTCellPercentage = params.getFloat("disableTCellPercentage");
		System.out.println("Percentuale disabilitazione TCell: " + disableTCellPercentage);
		
		float rccPercentage = 0.02f;//params.getFloat("rccPercentage");
		System.out.println("Percentuale cellule RCC: " + rccPercentage*100 + "%");
		int rccCellsToCreate = Math.max(2, (int)(size * rccPercentage));
		System.out.println("Numero cellule RCC da creare: " + rccCellsToCreate);
		
		double cd4cd8ratio = CellUtils.limitVariableToBMI(bmi, 1.5f, 2.5f);
		
		float cd4Percentage = 0.25f;//params.getFloat("cd4Percentage");
		System.out.println("Percentuale cellule CD4: " + cd4Percentage*100 + "%");
		int cd4CellsToCreate = Math.max(1, (int)(size * cd4Percentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule CD4 da creare: " + cd4CellsToCreate);
		
		double cd8Percentage = cd4Percentage / cd4cd8ratio;//params.getFloat("cd8Percentage");
		System.out.println("Percentuale cellule CD8: " + cd8Percentage*100 + "%");
		int cd8CellsToCreate = Math.max(1, (int)(size * cd8Percentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule CD8 da creare: " + cd8CellsToCreate);
		
		double mastPercentage = CellUtils.limitVariableToBMI(bmi, 0.03f, 0.08f);//params.getFloat("mastPercentage");
		System.out.println("Percentuale cellule Mast: " + mastPercentage*100 + "%");
		int mastCellsToCreate = Math.max(1, (int)(size * mastPercentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule Mast da creare: " + mastCellsToCreate);

		double dendriticPercentage = CellUtils.limitVariableToBMI(bmi, 0.02f, 0.05f);//params.getFloat("dendriticPercentage");
		System.out.println("Percentuale cellule Dendritic: " + dendriticPercentage*100 + "%");
		int dendriticCellsToCreate = Math.max(1, (int)(size * dendriticPercentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule Dendritic da creare: " + dendriticCellsToCreate);
		
		double plasmacytoidPercentage = CellUtils.limitVariableToBMI(bmi, 0.02f, 0.08f);//params.getFloat("plasmacytoidPercentage");
		System.out.println("Percentuale cellule Plasmacytoid: " + plasmacytoidPercentage*100 + "%");
		int plasmacytoidCellsToCreate = Math.max(1, (int)(size * plasmacytoidPercentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule Plasmacytoid da creare: " + plasmacytoidCellsToCreate);

		double nkPercentage = CellUtils.inverseLimitVariableToBMI(bmi, 0.07f, 0.15f);//params.getFloat("nkPercentage");
		System.out.println("Percentuale cellule NK: " + nkPercentage*100 + "%");
		int nkCellsToCreate = Math.max(1, (int)(size * nkPercentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule NK da creare: " + nkCellsToCreate);

		double m1Percentage = CellUtils.limitVariableToBMI(bmi, 0.04f, 0.09f);//params.getFloat("m1Percentage");
		System.out.println("Percentuale cellule M1: " + m1Percentage*100 + "%");
		int m1CellsToCreate = Math.max(1, (int)(size * m1Percentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule M1 da creare: " + m1CellsToCreate);
		
		float m2Percentage = 0.075f;//params.getFloat("m2Percentage");
		System.out.println("Percentuale cellule M2: " + m2Percentage*100 + "%");
		int m2CellsToCreate = Math.max(1, (int)(size * m2Percentage * percentageOfImmuneCellsToCreate));
		System.out.println("Numero cellule M2 da creare: " + m2CellsToCreate);

		float th1Percentage = 0f;//params.getFloat("th1Percentage");
		System.out.println("Percentuale cellule Th1: " + th1Percentage*100 + "%");
		int th1CellsToCreate = (int)(size * th1Percentage);
		System.out.println("Numero cellule Th1 da creare: " + th1CellsToCreate);
		
		float tregPercentage = 0f;//params.getFloat("tregPercentage");
		System.out.println("Percentuale cellule Treg: " + tregPercentage*100 + "%");
		int tregCellsToCreate = (int)(size * tregPercentage);
		System.out.println("Numero cellule Treg da creare: " + tregCellsToCreate);
		
		double adipocytePercentage = CellUtils.limitVariableToBMI(bmi, 0.03f, 0.07f);
		System.out.println("Percentuale cellule Adipocyte: " + adipocytePercentage*100 + "%");
		int adipocyteCellsToCreate = Math.max(1, (int)(size * adipocytePercentage));
		System.out.println("Numero cellule Adipocyte da creare: " + adipocyteCellsToCreate);

		int left = size - cd8CellsToCreate - mastCellsToCreate - adipocyteCellsToCreate - rccCellsToCreate - cd4CellsToCreate - dendriticCellsToCreate - bloodCellsToCreate - th1CellsToCreate - m1CellsToCreate - m2CellsToCreate - nkCellsToCreate - tregCellsToCreate - plasmacytoidCellsToCreate;
		
		if (left < 0) {
			System.out.println("Numero di cellule da creare maggiore del numero di celle della griglia!");
			return null;
		}
		
		System.out.println("Numero cellule Empty da creare: " + left);
		
		int seed = params.getInteger("randomSeed");
		Random random = new Random(seed);

		for (int i = 0; i < rccCellsToCreate; i++) {
			RenalCellCarcinoma rcc = new RenalCellCarcinoma(10, grid, reproTime, reproFactor, mutationPercentage, disableTCellPercentage);
			context.add(rcc);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(rcc, x, y));
			context.add(rcc);
		}

		for (int i = 0; i < cd8CellsToCreate; i++) {
			CD8 cd8 = new CD8(10, grid);
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
			Adipocyte adipocyte = new Adipocyte(grid);
			context.add(adipocyte);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(adipocyte, x, y));
			context.add(adipocyte);
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
			Dendritic dendritic = new Dendritic(10, grid, cd4cd8ratio, lymphNodeOrientation);
			context.add(dendritic);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(dendritic, x, y));
			context.add(dendritic);
		}

		for (int i = 0; i < th1CellsToCreate; i++) {
			Th1 th1 = new Th1(10, grid);
			context.add(th1);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(th1, x, y));
			context.add(th1);
		}
		
		for (int i = 0; i < m1CellsToCreate; i++) {
			M1 m1 = new M1(10, grid);
			context.add(m1);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(m1, x, y));
			context.add(m1);
		}
		
		for (int i = 0; i < m2CellsToCreate; i++) {
			M2 m2 = new M2(10, grid);
			context.add(m2);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(m2, x, y));
			context.add(m2);
		}
		
		for (int i = 0; i < nkCellsToCreate; i++) {
			NKCell nk = new NKCell(10, grid);
			context.add(nk);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(nk, x, y));
			context.add(nk);
		}
		
		for (int i = 0; i < tregCellsToCreate; i++) {
			Treg treg = new Treg(10, grid);
			context.add(treg);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(treg, x, y));
			context.add(treg);
		}
		
		for (int i = 0; i < plasmacytoidCellsToCreate; i++) {
			PlasmacytoidDendritic plasma = new PlasmacytoidDendritic(10, grid, cd4cd8ratio, lymphNodeOrientation);
			context.add(plasma);
			int x;
			int y;
			do {
				x = random.nextInt(grid.getDimensions().getWidth());
				y = random.nextInt(grid.getDimensions().getHeight());
			} while (!grid.moveTo(plasma, x, y));
			context.add(plasma);
		}

		int emptyCellsToCreate = 0;
		if (left > 0) {
			for (int x = 0; x < grid.getDimensions().getWidth(); x++) {
				for (int y = 0; y < grid.getDimensions().getHeight(); y++) {
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

		if (RunEnvironment.getInstance().isBatch()) {
			RunEnvironment.getInstance().endAt(100);
		}

		return context;
	}
}
