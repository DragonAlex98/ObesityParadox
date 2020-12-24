package styles;

import java.awt.Color;

import repast.simphony.visualizationOGL2D.DefaultStyleOGL2D;

public class Th1Style extends DefaultStyleOGL2D {

	@Override
	public float getScale(Object object) {
		return 2f;
	}
	
	@Override
	public Color getColor(Object o) {
		return new Color(255, 128, 0);
	}
}
