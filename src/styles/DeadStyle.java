package styles;

import java.awt.Color;
import java.io.IOException;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;
import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class DeadStyle extends DefaultStyleOGL2D {
	
	private ShapeFactory2D shapeFactory;

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
		if (spatial == null) {
			try {
				spatial = this.shapeFactory.createImage("icons/dead.png");
			} catch (IOException e) {
				System.out.println("Immagine adipocita non trovata!");
				e.printStackTrace();
			}
		}
		return spatial;
	}
}
