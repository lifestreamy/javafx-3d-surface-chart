package de.adihubba.javafx.jfx3d;


import de.adihubba.delauney.Point;
import javafx.geometry.Point3D;


/**
 * Conversion between the different Point classes.
 * Sometimes the delauney algorithm only works if we change the axis. In addition these methods can change the axis.
 */
public interface DelauneyModifier {

    Point convertPoint3d4Delauney(Point3D point);

    Point3D convertPointFromDelauney(Point coord);

}
