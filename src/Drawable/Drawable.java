package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

import java.lang.reflect.Field;

public abstract class Drawable {
    protected int fillColor;
    protected int strokeColor;
    protected int strokeWeight;

    protected Drawable(int fillColor, int strokeColor, int strokeWeight) {
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWeight = strokeWeight;
    }

    public abstract void draw(PApplet app);

    public abstract String toProcessingCode();

    public JSONObject toJson() {
        Class<? extends Drawable> drawableClass = getClass();

        JSONObject json = new JSONObject();

        json.setString("type", drawableClass.getSimpleName());
        serializeToJson(drawableClass, json);

        Class<?> parentClass = drawableClass.getSuperclass();
        while (parentClass != null) {
            serializeToJson(parentClass, json);
            parentClass = parentClass.getSuperclass();
        }

        return json;
    }

    private void serializeToJson(Class<?> drawableClass, JSONObject jsonObject) {
        for (Field field : drawableClass.getDeclaredFields()) {
            Object value = null;

            try {
                value = field.get(this);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            if (value != null)
                jsonObject.put(field.getName(), value);
        }
    }

    public int getFillColor() {
        return fillColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getStrokeWeight() {
        return strokeWeight;
    }
}
