package src.Drawable.Factory;

import processing.data.JSONObject;
import src.Drawable.Abstract.Drawable;
import src.Drawable.*;

public final class DrawableFactory {
    public static Drawable create(JSONObject json) {
        return switch (json.getString(Drawable.DRAWABLE_TYPE_JSON_KEY)) {
            case "Point" -> new Point(json);
            case "Line" -> new Line(json);
            case "Rectangle" -> new Rectangle(json);
            case "Ellipse" -> new Ellipse(json);
            case "Triangle" -> new Triangle(json);
            case "Polygon" -> new Polygon(json);
            case "CurvedPolygon" -> new CurvedPolygon(json);
            case "Image" -> new Image(json);
            default ->
                    throw new IllegalStateException("Unexpected drawable type: " + json.getString(Drawable.DRAWABLE_TYPE_JSON_KEY));
        };
    }
}
