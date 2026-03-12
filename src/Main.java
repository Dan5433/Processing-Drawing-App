package src;

import processing.core.PApplet;
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

    private int startX = -1;
    private int startY = -1;

    private final Stack<PVector> polygonVertices = new Stack<>();

    public void settings() {
//        size(800, 450);
        fullScreen();
    }

    public void setup() {
        rectMode(CORNERS);
        ellipseMode(CORNERS);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting UI: " + e);
        }
    }

    public void draw() {
        background(PremadeColor.WHITE.getColor());

        for (Drawable drawable : drawables)
            drawable.draw(this);

        fill(getFillColor());
        stroke(getStrokeColor());
        strokeWeight(strokeWeight);

        if (polygonVertices.isEmpty() && (!mousePressed || mouseButton != LEFT) && startX < 0 && startY < 0)
            selectedTool.drawPreview(this);
        else
            selectedTool.draw(this);


        if (!polygonVertices.isEmpty() && (selectedTool == Tool.POLYGON || selectedTool == Tool.CURVED_POLYGON))
            updatePolygon();
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
                if (selectedTool != Tool.POLYGON && selectedTool != Tool.CURVED_POLYGON)
                    break;

                if (selectedTool == Tool.CURVED_POLYGON && polygonVertices.isEmpty())
                    polygonVertices.push(new PVector(mouseX, mouseY));
                polygonVertices.push(new PVector(mouseX, mouseY));
                break;
            case RIGHT:
                if (!polygonVertices.isEmpty() || startX >= 0 || startY >= 0)
                    break;
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
        if (mouseButton != LEFT || selectedTool == Tool.POLYGON || selectedTool == Tool.CURVED_POLYGON)
            return;

        pushDrawing();
    }

    public void keyPressed(KeyEvent event) {
        if (!polygonVertices.isEmpty() || mousePressed)
            return;

        if (event.isControlDown() || event.isMetaDown())
            controlModifiedKeyPress(event);
    }

    private void updatePolygon() {
        beginShape();
        for (PVector vertex : polygonVertices) {
            if (selectedTool == Tool.POLYGON)
                vertex(vertex.x, vertex.y);
            else
                curveVertex(vertex.x, vertex.y);
        }

        if (cannotClosePolygon()) {
            if (selectedTool == Tool.POLYGON)
                vertex(mouseX, mouseY);
            else {
                curveVertex(mouseX, mouseY);
                curveVertex(mouseX, mouseY);
            }
            endShape(OPEN);
            return;
        }

        PVector startVertex = polygonVertices.getFirst();
        boolean isMouseCloseToStart = Utils.isPointWithinDistance(mouseX, mouseY,
                (int) startVertex.x, (int) startVertex.y, POLYGON_CLOSE_SNAP_DISTANCE);
        if (isMouseCloseToStart) {
            if (mousePressed) {
                polygonVertices.pop();
                if (selectedTool == Tool.CURVED_POLYGON) {
                    polygonVertices.push(startVertex);
                    polygonVertices.push(startVertex);
                }

                pushDrawing();
            } else {
                if (selectedTool == Tool.POLYGON)
                    vertex(startVertex.x, startVertex.y);
                else {
                    curveVertex(startVertex.x, startVertex.y);
                    curveVertex(startVertex.x, startVertex.y);
                }
            }
        } else {
            if (selectedTool == Tool.POLYGON)
                vertex(mouseX, mouseY);
            else {
                curveVertex(mouseX, mouseY);
                curveVertex(mouseX, mouseY);
            }
        }
        endShape(OPEN);
    }

    private void controlModifiedKeyPress(KeyEvent event) {
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

    private void pushDrawing() {
        cursor(ARROW);
        drawables.push(selectedTool.getDrawable(this));
        undoneDrawables.clear();
        polygonVertices.clear();

        startX = -1;
        startY = -1;
    }

    private void undo() {
        if (drawables.isEmpty())
            return;

        undoneDrawables.push(drawables.pop());
    }

    private void redo() {
        if (undoneDrawables.isEmpty())
            return;

        drawables.push(undoneDrawables.pop());
    }

    private void save(boolean isAltPressed) {
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

    private void load() {
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

    private void exportAsProcessingCode(File file) {
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

    private void saveSerialized(File file) {
        if (file == null)
            return;

        JSONArray array = new JSONArray();
        for (Drawable drawable : drawables)
            array.append(drawable.toJson());

        saveJSONArray(array, file.getAbsolutePath());
    }

    private boolean cannotClosePolygon() {
        return (selectedTool == Tool.POLYGON && polygonVertices.size() < 3)
                || (selectedTool == Tool.CURVED_POLYGON && polygonVertices.size() < 4);
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

    public PVector[] getPolygonVerticesArray() {
        return polygonVertices.toArray(new PVector[0]);
    }

    public Stack<PVector> getPolygonVertices() {
        return polygonVertices;
    }

    enum PropertyChangeMode {
        FILL_COLOR,
        STROKE_COLOR,
        STROKE_WEIGHT
    }
}