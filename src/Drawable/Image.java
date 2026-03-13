package src.Drawable;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.data.JSONObject;
import src.Drawable.Abstract.SinglePointDrawable;

import java.nio.file.Files;
import java.nio.file.Path;

public class Image extends SinglePointDrawable {
    protected PImage image;
    protected String path;

    public Image(PImage image, String path, int x, int y, int fillColor, int strokeColor, int strokeWeight) {
        super(x, y, fillColor, strokeColor, strokeWeight);
        this.image = image;
        this.path = path;
    }

    public Image(JSONObject json) {
        super(json);
        this.path = json.getString("path");
    }

    @Override
    public void draw(PApplet app, PGraphics canvas) {
        if (image == null) {
            if (!Files.exists(Path.of(path)))
                image = app.createImage(0, 0, PConstants.RGB);
            else
                image = app.loadImage(path);
        }
        canvas.image(image, x, y);
    }

    @Override
    public String toProcessingCode() {
        if (!Files.exists(Path.of(path)))
            return "";

        String escapedPath = path.replace("\\", "\\\\");
        return String.format("image(loadImage(\"%s\"),%d,%d);", escapedPath, x, y);
    }

    @Override
    public JSONObject toJson() {
        if (!Files.exists(Path.of(path)))
            return null;

        JSONObject json = super.toJson();
        json.setString("path", path);
        return json;
    }
}
