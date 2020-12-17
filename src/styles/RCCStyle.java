package styles;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class RCCStyle extends DefaultStyleOGL2D {

	@Override
	public float getScale(Object object) {
		return 2f;
	}
	
	@Override
	public Color getColor(Object o) {
		return new Color(255, 0, 255);
	}
}
