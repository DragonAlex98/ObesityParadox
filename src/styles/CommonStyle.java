package styles;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;
import utils.StyleUtils;

public abstract class CommonStyle extends DefaultStyleOGL2D {
	
	private ShapeFactory2D shapeFactory;
	private String cellType = null;

	@Override
	public float getScale(Object object) {
		return 0.25f;
	}
	
	@Override
	public Color getColor(Object o) {
		return null;
	}
	
	@Override
	public void init(ShapeFactory2D factory) {
		this.shapeFactory = factory;
	}
	
	@Override
	public VSpatial getVSpatial(Object agent, VSpatial spatial) {
		if (cellType == null) {
			return null;
		}
		return (spatial == null) ? StyleUtils.getCellImage(shapeFactory, cellType) : spatial;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}
}
