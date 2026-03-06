package src.Drawable;

import processing.data.JSONObject;

public final class DrawableFactory {
    public static Drawable create(JSONObject json) {
        return switch (json.getString(Drawable.DRAWABLE_TYPE_JSON_KEY)) {
            case "Point" -> new Point(json);
            case "Line" -> new Line(json);
            case "Rectangle" -> new Rectangle(json);
            case "Ellipse" -> new Ellipse(json);
            case "Triangle" -> new Triangle(json);
            default ->
                    throw new IllegalStateException("Unexpected drawable type: " + json.getString(Drawable.DRAWABLE_TYPE_JSON_KEY));
        };
    }
}
