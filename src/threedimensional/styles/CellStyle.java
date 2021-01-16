package threedimensional.styles;

import java.awt.Color;
import java.awt.Font;

import org.jogamp.java3d.Shape3D;

import commons.cell.Cell;
import commons.cell.DeadCell;
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
import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

public class CellStyle implements Style3D<Cell> {

	public TaggedBranchGroup getBranchGroup(Cell agent, TaggedBranchGroup taggedGroup) {
		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
			if (!(agent instanceof EmptyCell)) {
				Shape3D sphere = ShapeFactory.createSphere(.03f, "DEFAULT");
				sphere.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
				taggedGroup.getBranchGroup().addChild(sphere);
			}

			return taggedGroup;
		}
		return null;
	}

	public float[] getRotation(Cell agent) {
		return null;
	}

	public String getLabel(Cell agent, String currentLabel) {
		return null; 
	}

	public Color getLabelColor(Cell agent, Color currentColor) {
		return Color.YELLOW;
	}

	public Font getLabelFont(Cell agent, Font currentFont) {
		return null;
	}

	public LabelPosition getLabelPosition(Cell agent, LabelPosition curentPosition) {
		return LabelPosition.NORTH;
	}

	public float getLabelOffset(Cell agent) {
		return .035f;
	}

	public TaggedAppearance getAppearance(Cell agent, TaggedAppearance taggedAppearance, Object shapeID) {
		if (taggedAppearance == null) {
			taggedAppearance = new TaggedAppearance();
		}

		if (agent instanceof Adipocyte) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(1f, 1f, 0f));
			return taggedAppearance;
		} else if (agent instanceof BloodCell) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(1f, 0f, 0f));
			return taggedAppearance;
		} else if (agent instanceof CD4) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 1f, 1f));
			return taggedAppearance;
		} else if (agent instanceof CD8) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0.5f, 0.5f, 0.5f));
			return taggedAppearance;
		} else if (agent instanceof DeadCell) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 0f, 0f));
			return taggedAppearance;
		} else if (agent instanceof Dendritic) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(1.0f, 0f, 1.0f));
			return taggedAppearance;
		} else if (agent instanceof M1) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(1.0f, 0.5f, 0f));
			return taggedAppearance;
		} else if (agent instanceof M2) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 0.5f, 0.5f));
			return taggedAppearance;
		} else if (agent instanceof MastCell) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 1.0f, 0f));
			return taggedAppearance;
		} else if (agent instanceof NKCell) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 0.63f, 0.88f));
			return taggedAppearance;
		} else if (agent instanceof PlasmacytoidDendritic) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0.15f, 0f, 0.4f));
			return taggedAppearance;
		} else if (agent instanceof RenalCellCarcinoma) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0.5f, 0f, 0.5f));
			return taggedAppearance;
		} else if (agent instanceof Th1) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0f, 0f, 1.0f));
			return taggedAppearance;
		} else if (agent instanceof Treg) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), new Color(0.44f, 0.18f, 0.15f));
			return taggedAppearance;
		} else {
			return null;
		}
	}

	public float[] getScale(Cell agent) {
		return null;
	}
}
