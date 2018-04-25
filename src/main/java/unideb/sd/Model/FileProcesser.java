package unideb.sd.Model;

/*
 * #%L
 * TXSimilator
 * %%
 * Copyright (C) 2017-2018 Debreceni Egyetem, Informatikai Kar
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class FileProcesser {

    private static String[] data = new String[4];
    private static String[] valaszto = new String[3];
    public static List<LocalDateTime> local = new ArrayList<>();
    private static double[] localdata;
    public static List<List<Double>> superdatam = new ArrayList<List<Double>>();
    public static List<Double> wrapper;
    private static int firstcolumn;
    private static int secoundcolumn;
    private static int thirdcolumn;
    private static int forthcolumn;
    private static String cvsSplitBy;

    public static void readcsv(String FilePath) {
        File file = new File(FilePath);
        BufferedReader br = null;
        String line = "";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                data = line.split(cvsSplitBy);
                valaszto[0] = data[firstcolumn];
                local.add(LocalDateTime.parse(data[secoundcolumn], DateTimeFormatter.ISO_ZONED_DATE_TIME));
                valaszto[1] = data[thirdcolumn];
                valaszto[2] = data[forthcolumn];
                localdata = Arrays.asList(valaszto).stream().mapToDouble(Double::parseDouble).toArray();
                wrapper = Arrays.stream(localdata).boxed().collect(Collectors.toList());
                superdatam.add(wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static List<List<Double>> getIdsandCoordinates() {
        return superdatam;
    }

    public static List<LocalDateTime> getDates() {
        return local;
    }

    public static void setFirstcolumn(int firstcolumn) {
        FileProcesser.firstcolumn = firstcolumn;
    }

    public static void setSecoundcolumn(int secoundcolumn) {
        FileProcesser.secoundcolumn = secoundcolumn;
    }

    public static void setThirdcolumn(int thirdcolumn) {
        FileProcesser.thirdcolumn = thirdcolumn;
    }

    public static void setForthcolumn(int forthcolumn) {
        FileProcesser.forthcolumn = forthcolumn;
    }

    public static String getCvsSplitBy() {
        return cvsSplitBy;
    }

    public static void setCvsSplitBy(String cvsSplitBy) {
        FileProcesser.cvsSplitBy = cvsSplitBy;
    }

    public static Map<LocalDate, Long> datamaker() {
        List<LocalDate> localdate = new ArrayList<>();
        for (int i = 0; i < local.size(); i++) {
            localdate.add(local.get(i).toLocalDate());
        }
        Map<LocalDate, Long> result = new HashMap<>();
        result = localdate.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<LocalDate, Long> treeMaper = new TreeMap<>(result);
        return treeMaper;
    }

    public static Map<Integer, Long> hourmaker() { //7 napra vonatkozó
        List<Integer> localtime = new ArrayList<>();
        for (int i = 0; i < local.size(); i++) {
            localtime.add(local.get(i).getHour());
        }
        Map<Integer, Long> result = new HashMap<>();
        result = localtime.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Integer, Long> treeMaper = new TreeMap<>(result);
        return treeMaper;
    }

    public static Map<Integer, Long> minutemaker() { //5 napra vonatkozó
        List<Integer> localtime = new ArrayList<>();
        for (int i = 0; i < local.size(); i++) {
            localtime.add(local.get(i).getMinute());
        }
        Map<Integer, Long> result = new HashMap<>();
        result = localtime.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        Map<Integer, Long> treeMaper = new TreeMap<>(result);
        return treeMaper;
    }

    public static void showError() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        //alert.setHeaderText("Exception happened during file read, please try to change on FileSettings");
        alert.setHeaderText("No File added yet!");
        
        Exception ex = new Exception("File Could not be read!");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();

    }
}
