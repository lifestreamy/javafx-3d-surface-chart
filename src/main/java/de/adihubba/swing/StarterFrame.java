package de.adihubba.swing;

import de.adihubba.delauney.Point;
import de.adihubba.javafx.jfx3d.DelauneyModifier;
import de.adihubba.javafx.jfx3d.Mesh3DChartPanel;
import javafx.geometry.Point3D;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Math.exp;
import static java.lang.Math.sin;

public class StarterFrame extends JFrame {

    private static final List<Point3D> final_Points = new ArrayList<>();

    public StarterFrame() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    //EDITED BY Timofey Kochetkov https://github.com/TimofeyKochetkov/javafx-3d-surface-chart

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        double time;
        System.out.println("Enter the value of TIME parameter");
        time = Double.parseDouble(sc.nextLine());
        System.out.printf("Time == %f \n", time);
        System.out
                .println("Enter the path to the folder containing formatted files with coordinates");
        File[] files = new File(sc.nextLine()).listFiles();
        if (files == null) System.out.println("No files were added to the plotting");
        else {
            int c = 1;
            for (File file : files) {
                try {
                    final_Points.addAll(getPointsFromFile(file));
                    System.out.println(file.toString() + " --- File No. " + c++);
                } catch (IOException e) {
                    System.out.println("Error with parsing the file");
                }
            }
        }
        new StarterFrame().showChart(time);
    }

    public static List<Point3D> getPointsFromFile(File file) throws IOException {
        List<String> lines = Files.lines(file.toPath()).collect(Collectors.toList());
        List<Point3D> points = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            String[] words = lines.get(i).split(" ");
            for (int j = 0; j < words.length; j++) {
                points.add(new Point3D(
                        (float) (j * Math.PI) / (words.length - 1),
                        Float.parseFloat(words[j]),
                        (float) (i * Math.PI) / (lines.size() - 1)
                ));
            }
        }
        return points;
    }

    public void showChart(double time) {
        final Mesh3DChartPanel chart = new Mesh3DChartPanel();
        //region chart
        chart.setDelauneyModifier(new MyDelauneyModifier());
        chart.setAxisTitleX("X-Axis");
        chart.setAxisTitleY("Y-Axis");
        chart.setAxisTitleZ("Z-Axis");
        chart.addMeshControlPanel();

        chart.showMeshPanel(getPointsForPlot(time));
        //endregion

        getContentPane().add(chart);
        setSize(1200, 800);
        setVisible(true);
    }

    @SuppressWarnings("SameReturnValue")
    private List<Point3D> getPointsForPlot(double time) {
        List<Point3D> result = new ArrayList<>();
        double lower_bound = 0;
        double upper_bound = Math.PI;
        int dots_count = 100;
        double step = (upper_bound - lower_bound) / (double) dots_count;
        for (double x = 0; x <= Math.PI; x += step) {
            for (double y = 0; y <= Math.PI; y += step) {
                result.add(new Point3D(x, calculateFunc(x, y, time), y));
            }
        }
        final_Points.addAll(result);
        return final_Points;
    }

    /**
     * @param time - time parameter
     * @return f(x, y, t) = (e^(-t)-e^(-2t)) * sin x * sin y
     */

    private double calculateFunc(double x, double y, double time) {
        return (exp(-time) - exp(-2 * time)) * sin(x) * sin(y);
    }

    /**
     * Change Y and Z axis
     */
    public static class MyDelauneyModifier implements DelauneyModifier {

        @Override
        public Point convertPoint3d4Delauney(Point3D point) {
            return new Point(point.getX(), point.getZ(), point.getY());
        }

        @Override
        public Point3D convertPointFromDelauney(Point coord) {
            return new Point3D(coord.getX(), coord.getZ(), coord.getY());
        }

    }

}
