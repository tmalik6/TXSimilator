package unideb.sd;

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

public class readtsvbc {

    private static String[] data = new String[4];
    private static String[] valaszto = new String[3];
    public static List<LocalDateTime> local = new ArrayList<>();
    private static double[] localdata;
    public static List<List<Double>> superdatam = new ArrayList<List<Double>>();
    public static List<Double> wrapper;
    private static int szamlalo = -1;

    public static void readcsv() {
        File file = new File(Controller.FilePath);
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                szamlalo++;
                data = line.split(cvsSplitBy);
                valaszto[0] = data[0];
                local.add(LocalDateTime.parse(data[1], DateTimeFormatter.ISO_ZONED_DATE_TIME));
                valaszto[1] = data[2];
                valaszto[2] = data[3];
                localdata = Arrays.asList(valaszto).stream().mapToDouble(Double::parseDouble).toArray();
                wrapper = Arrays.stream(localdata).boxed().collect(Collectors.toList());
                superdatam.add(wrapper);                
            }
        } catch (Exception e) {
            e.printStackTrace();
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

    public static List<List<Double>> getIdsandCoordinates(){
        return superdatam;
    }
    
    public static List<LocalDateTime> getDates() {
        return local;
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
}
