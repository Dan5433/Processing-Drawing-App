package src;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;

public final class Utils {
    public static int clamp(int min, int max, int value) {
        if (value < min)
            value = min;

        if (value > max)
            value = max;

        return value;
    }

    public static <T extends Enum<T>> T incrementEnum(T[] values, int currentIndex) {
        int newIndex = currentIndex + 1;

        if (newIndex > values.length - 1) {
            newIndex = 0;
        }

        return values[newIndex];
    }

    public static void writeFillColor(PrintWriter writer, int color) {
        writer.printf("fill(%d);\n", color);
    }

    public static void writeStrokeColor(PrintWriter writer, int color) {
        writer.printf("stroke(%d);\n", color);
    }

    public static void writeStrokeWeight(PrintWriter writer, int weight) {
        writer.printf("strokeWeight(%d);\n", weight);
    }

    public static File runFileChooser(JFileChooser chooser, String extension, FileNameExtensionFilter filter) {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        chooser.setSelectedFile(Paths.get(chooser.getCurrentDirectory().getAbsolutePath(), "drawing." + extension).toFile());
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(filter);
        chooser.setVisible(true);

        int result = chooser.getDialogType() == JFileChooser.OPEN_DIALOG
                ? chooser.showOpenDialog(null)
                : chooser.showSaveDialog(null);

        if (result != JFileChooser.APPROVE_OPTION)
            return null;

        return chooser.getSelectedFile();
    }

    public static boolean isPointWithinDistance(int x, int y, int compareX, int compareY, int distance) {
        return Math.pow(compareX - x, 2) + Math.pow(compareY - y, 2) <= Math.pow(distance, 2);
    }
}
