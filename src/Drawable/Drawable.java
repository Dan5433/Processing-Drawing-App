package src.Drawable;

import processing.core.PApplet;
import processing.data.JSONObject;

import java.lang.reflect.Field;

public abstract class Drawable {
    protected int startX;
    protected int startY;

    protected int mouseX;
    protected int mouseY;

    protected int strokeColor;
    protected int fillColor;
    protected int strokeWeight;

    protected int thickness;

    abstract public void draw(PApplet app);

    public abstract String toProcessingCode();

    public Drawable(int startX, int startY, int mouseX, int mouseY, int strokeColor, int fillColor, int strokeWeight) {
        this.startX = startX;
        this.startY = startY;
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.strokeColor = strokeColor;
        this.fillColor = fillColor;
        this.strokeWeight = strokeWeight;
    }

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

    public int getFillColor() {
        return fillColor;
    }

    public int getStrokeColor() {
        return strokeColor;
    }

    public int getStrokeWeight() {
        return strokeWeight;
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
}
