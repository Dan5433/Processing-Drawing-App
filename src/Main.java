package src;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import src.Drawable.Drawable;

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

    private final Stack<Drawable> drawables = new Stack<>();
    private final Stack<Drawable> undoneDrawables = new Stack<>();

    private Tool selectedTool = Tool.values()[0];
    private PremadeColor strokeColor = PremadeColor.BLACK;
    private PremadeColor fillColor = PremadeColor.WHITE;
    private PropertyChangeMode propertyChangeMode = PropertyChangeMode.FILL_COLOR;
    private int strokeWeight = 1;

    private int startX;
    private int startY;

    public void settings() {
//        size(800, 450);
        fullScreen();
    }

    public void setup() {
        rectMode(CORNERS);
        ellipseMode(CORNERS);
    }

    public void draw() {
        background(PremadeColor.WHITE.getColor());

        for (Drawable drawable : drawables)
            drawable.draw(this);

        fill(getFillColor());
        stroke(getStrokeColor());
        strokeWeight(strokeWeight);

        if (!mousePressed || mouseButton != LEFT)
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
        if (mouseButton != LEFT)
            return;

        pushDrawing();
        cursor(ARROW);
    }

    public void keyPressed(KeyEvent event) {
        if (event.isControlDown() || event.isMetaDown())
            controlModifiedKeyPress(event);
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
        }
    }

    void pushDrawing() {
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

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
            System.out.println("Error setting UI: " + ignored);
        }

        JFileChooser chooser = new JFileChooser();
        String extension = isAltPressed ? "pde" : "json";
        String description = isAltPressed ? "Processing Sketch (*.pde)" : "Drawing App Sketch (*.json)";

        FileNameExtensionFilter filter = new FileNameExtensionFilter(description, extension);
        chooser.setDialogType(JFileChooser.SAVE_DIALOG);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setDialogTitle(isAltPressed ? "Export As Processing Sketch" : "Save As Drawing App Sketch");
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setSelectedFile(Paths.get(chooser.getCurrentDirectory().getAbsolutePath(), "drawing." + extension).toFile());
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(filter);
        chooser.setVisible(true);

        int result = chooser.showSaveDialog(null);

        if (result != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith("." + extension))
            file = new File(file.getAbsolutePath() + "." + extension);

        if (isAltPressed)
            exportAsProcessingCode(file);
        else
            saveSerialized(file);
    }

    public void exportAsProcessingCode(File file) {
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

    enum PropertyChangeMode {
        FILL_COLOR,
        STROKE_COLOR,
        STROKE_WEIGHT
    }
}