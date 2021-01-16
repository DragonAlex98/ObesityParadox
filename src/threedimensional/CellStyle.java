package threedimensional;

import java.awt.Color;
import java.awt.Font;

import org.jogamp.java3d.Shape3D;

import repast.simphony.visualization.visualization3D.AppearanceFactory;
import repast.simphony.visualization.visualization3D.ShapeFactory;
import repast.simphony.visualization.visualization3D.style.Style3D;
import repast.simphony.visualization.visualization3D.style.TaggedAppearance;
import repast.simphony.visualization.visualization3D.style.TaggedBranchGroup;

public class CellStyle implements Style3D<Cell> {

	public TaggedBranchGroup getBranchGroup(Cell agent, TaggedBranchGroup taggedGroup) {
		if (taggedGroup == null || taggedGroup.getTag() == null) {
			taggedGroup = new TaggedBranchGroup("DEFAULT");
			if (agent.getState() != 0) {
				Shape3D sphere;
				sphere = ShapeFactory.createSphere(.03f, "DEFAULT");
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

		if (agent.getState() == 1) {
			AppearanceFactory.setMaterialAppearance(taggedAppearance.getAppearance(), Color.GREEN);
			return taggedAppearance;
		} else {
			return null;
		}
	}

	public float[] getScale(Cell agent) {
		return null;
	}
}
