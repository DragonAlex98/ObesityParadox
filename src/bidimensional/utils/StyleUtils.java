package bidimensional.utils;

import java.io.IOException;

import saf.v3d.ShapeFactory2D;
import saf.v3d.scene.VSpatial;

public class StyleUtils {

	public static VSpatial getCellImage(ShapeFactory2D factory, String cellType) {
		try {
			return factory.createImage("icons/" + cellType + ".png");
		} catch (IOException e) {
			System.out.println("Immagine" + cellType +  "non trovata!");
			e.printStackTrace();
			return null;
		}
	}
}
