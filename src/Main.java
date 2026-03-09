package src;

import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;
import processing.data.JSONArray;
import processing.data.JSONObject;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import src.Drawable.Drawable;
import src.Drawable.DrawableFactory;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import java.util.Stack;

public final class Main extends PApplet {
    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }

    public final int PREVIEW_SIZE = 15;
    public final int MAX_STROKE_WEIGHT = 12;
    public final int POLYGON_CLOSE_SNAP_DISTANCE = 25;

    private final Stack<Drawable> drawables = new Stack<>();
    private final Stack<Drawable> undoneDrawables = new Stack<>();

    private Tool selectedTool = Tool.values()[0];
    private PremadeColor strokeColor = PremadeColor.BLACK;
    private PremadeColor fillColor = PremadeColor.WHITE;
    private PropertyChangeMode propertyChangeMode = PropertyChangeMode.FILL_COLOR;
    private int strokeWeight = 1;

    private int startX;
    private int startY;

    private final Stack<PVector> polygonVertices = new Stack<>();
    private PShape polygon;

    public void settings() {
//        size(800, 450);
        fullScreen();
    }

    public void setup() {
        rectMode(CORNERS);
        ellipseMode(CORNERS);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            System.out.println("Error setting UI: " + ignored);
        }
    }

    public void draw() {
        background(PremadeColor.WHITE.getColor());

        for (Drawable drawable : drawables)
            drawable.draw(this);

        fill(getFillColor());
        stroke(getStrokeColor());
        strokeWeight(strokeWeight);

        if (polygon == null && (!mousePressed || mouseButton != LEFT))
            selectedTool.drawPreview(this);
        else
            selectedTool.draw(this);
    }

    public void mouseWheel(MouseEvent event) {
        int scroll = event.getCount();
        int newColorIndex;

        switch (propertyChangeMode) {
            case FILL_COLOR:
                newColorIndex = Utils.clamp(0, PremadeColor.getMax(), fillColor.ordinal() - scroll);
                fillColor = PremadeColor.values()[newColorIndex];
                break;
            case STROKE_COLOR:
                newColorIndex = Utils.clamp(0, PremadeColor.getMax(), strokeColor.ordinal() - scroll);
                strokeColor = PremadeColor.values()[newColorIndex];
                break;
            case STROKE_WEIGHT:
                strokeWeight = Utils.clamp(1, MAX_STROKE_WEIGHT, strokeWeight - scroll);
                strokeWeight(strokeWeight);
                break;
        }
    }

    public void mousePressed() {
        switch (mouseButton) {
            case LEFT:
                cursor(CROSS);
                startX = mouseX;
                startY = mouseY;
                if (selectedTool != Tool.POLYGON)
                    break;

                polygonVertices.push(new PVector(mouseX, mouseY));
                UpdatePolygon();

                break;
            case RIGHT:
                selectedTool = Utils.incrementEnum(Tool.values(), selectedTool.ordinal());
                break;
            case CENTER:
                propertyChangeMode = Utils.incrementEnum(PropertyChangeMode.values(), propertyChangeMode.ordinal());
                break;
        }
    }

    public void mouseDragged() {
        if (mouseButton != LEFT || selectedTool != Tool.POINT)
            return;

        pushDrawing();
    }

    public void mouseReleased() {
        if (mouseButton != LEFT || selectedTool == Tool.POLYGON)
            return;

        pushDrawing();
    }

    @Override
    public void mouseMoved() {
        if (polygon == null || selectedTool != Tool.POLYGON)
            return;

        UpdatePolygon();
    }

    public void keyPressed(KeyEvent event) {
        if (event.isControlDown() || event.isMetaDown())
            controlModifiedKeyPress(event);
    }

    void UpdatePolygon() {
        polygon = createShape();
        polygon.beginShape();
        for (PVector vertex : polygonVertices)
            polygon.vertex(vertex.x, vertex.y);
        polygon.vertex(mouseX, mouseY);
        polygon.endShape(OPEN);

        if (polygonVertices.size() < 3)
            return;

        boolean isMouseCloseToStart = Utils.isPointWithinDistance(mouseX, mouseY,
                (int) polygon.getVertexX(0), (int) polygon.getVertexY(0), POLYGON_CLOSE_SNAP_DISTANCE);
        if (isMouseCloseToStart) {
            if (mousePressed)
                pushDrawing();
            else
                polygon.setVertex(polygonVertices.size(), polygon.getVertex(0));
        }
    }

    void controlModifiedKeyPress(KeyEvent event) {
        switch (Character.toLowerCase(keyCode)) {
            case 'z':
                undo();
                break;
            case 'y':
                redo();
                break;
            case 's':
                save(event.isAltDown());
                break;
            case 'l':
                load();
                break;
        }
    }

    void pushDrawing() {
        cursor(ARROW);
        polygon = null;
        drawables.push(selectedTool.getDrawable(this));
        undoneDrawables.clear();
    }

    void undo() {
        if (drawables.isEmpty())
            return;

        undoneDrawables.push(drawables.pop());
    }

    void redo() {
        if (undoneDrawables.isEmpty())
            return;

        drawables.push(undoneDrawables.pop());
    }

    void save(boolean isAltPressed) {
        if (drawables.isEmpty())
            return;

        JFileChooser chooser = new JFileChooser();
        String extension = isAltPressed ? "pde" : "json";
        String description = isAltPressed ? "Processing Sketch (*.pde)" : "Drawing App Sketch (*.json)";

        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setDialogTitle(isAltPressed ? "Export As Processing Sketch" : "Save As Drawing App Sketch");

        File file = Utils.runFileChooser(chooser, extension, filter);
        if (file == null)
            return;

        if (!file.getName().toLowerCase().endsWith("." + extension))
            file = new File(file.getAbsolutePath() + "." + extension);

        if (isAltPressed)
            exportAsProcessingCode(file);
        else
            saveSerialized(file);
    }

    void load() {
        JFileChooser chooser = new JFileChooser();
        String extension = "json";
        String description = "Drawing App Sketch (*.json)";

        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setDialogType(JFileChooser.OPEN_DIALOG);
        chooser.setDialogTitle("Load Drawing App Sketch Json");

        File file = Utils.runFileChooser(chooser, extension, filter);
        if (file == null)
            return;

        for (JSONObject serializedDrawable : loadJSONArray(file).objectValues())
            drawables.push(DrawableFactory.create(serializedDrawable));
    }

    void exportAsProcessingCode(File file) {
        if (file == null)
            return;

        PrintWriter writer = createWriter(file.getAbsolutePath());

        writer.println("fullScreen();");
        writer.println("background(255);");
        writer.println("ellipseMode(CORNERS);");
        writer.println("rectMode(CORNERS);");
        writer.println();

        Drawable first = drawables.peek();
        int currentFillColor = first.getFillColor();
        int currentStrokeColor = first.getStrokeColor();
        int currentStrokeWeight = first.getStrokeWeight();

        Utils.writeFillColor(writer, currentFillColor);
        Utils.writeStrokeColor(writer, currentStrokeColor);
        Utils.writeStrokeWeight(writer, currentStrokeWeight);
        for (Drawable drawable : drawables) {
            int drawableFillColor = drawable.getFillColor();
            int drawableStrokeColor = drawable.getStrokeColor();
            int drawableStrokeWeight = drawable.getStrokeWeight();

            if (drawableFillColor != currentFillColor) {
                currentFillColor = drawableFillColor;
                Utils.writeFillColor(writer, currentFillColor);
            }
            if (drawableStrokeColor != currentStrokeColor) {
                currentStrokeColor = drawableStrokeColor;
                Utils.writeStrokeColor(writer, currentStrokeColor);
            }
            if (drawableStrokeWeight != currentStrokeWeight) {
                currentStrokeWeight = drawableStrokeWeight;
                Utils.writeStrokeWeight(writer, currentStrokeWeight);
            }

            writer.println(drawable.toProcessingCode());
        }

        writer.flush();
        writer.close();
    }

    void saveSerialized(File file) {
        if (file == null)
            return;

        JSONArray array = new JSONArray();
        for (Drawable drawable : drawables)
            array.append(drawable.toJson());

        saveJSONArray(array, file.getAbsolutePath());
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getFillColor() {
        if (keyPressed && keyCode == SHIFT)
            return strokeColor.getColor();

        return fillColor.getColor();
    }

    public int getStrokeColor() {
        if (keyPressed && keyCode == SHIFT)
            return fillColor.getColor();

        return strokeColor.getColor();
    }

    public int getStrokeWeight() {
        return strokeWeight;
    }

    public PShape getPolygon() {
        return polygon;
    }

    public PVector[] getPolygonVertices() {
        PVector[] vertices = new PVector[polygon.getVertexCount()];
        for (int i = 0; i < vertices.length; i++) {
            vertices[i] = polygon.getVertex(i);
        }
        return vertices;
    }

    enum PropertyChangeMode {
        FILL_COLOR,
        STROKE_COLOR,
        STROKE_WEIGHT
    }
}