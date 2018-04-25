package unideb.sd.Controller;
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
import java.time.LocalDate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unideb.sd.Model.FileProcesser;

public class BarchartActions {
    private static final Logger logger = LoggerFactory.getLogger(BarchartActions.class);
        
    public static void BarchartActionsGeneral(Map<Integer, Long> LocalMapom, String felirat, String xlabel, String FilePath, Stage StagetoShow) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            Map<Integer, Long> LocalMap = LocalMapom;
            Set s = LocalMap.entrySet();
            Iterator it = s.iterator();
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart bc = new BarChart(xAxis, yAxis);
            bc.setTitle(felirat); 
            xAxis.setLabel(xlabel); 
            //xAxis.setTickLabelRotation(90);
            yAxis.setLabel("Number");

            XYChart.Series<LocalDate, Long> series1 = new XYChart.Series<>();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                series1.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
            }
            Scene scene = new Scene(bc, 600, 600);
            bc.getData().addAll(series1);
            bc.setLegendVisible(false);            
            StagetoShow.setTitle(felirat);
            StagetoShow.setScene(scene);
            //scene.getStylesheets().add("barchartsample/Chart.css");
            StagetoShow.show();
        } else {
            logger.error("No File Selected Yet!");
            FileProcesser.showError(2);
        }
    }
    public static void BarchartActionsLocalDate(Map<LocalDate, Long> LocalMapom, String felirat, String xlabel, String FilePath, Stage StagetoShow) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            Map<LocalDate, Long> LocalMap = LocalMapom;
            Set s = LocalMap.entrySet();
            Iterator it = s.iterator();
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart bc = new BarChart(xAxis, yAxis);
            bc.setTitle(felirat); 
            xAxis.setLabel(xlabel);
            //xAxis.setTickLabelRotation(90);
            yAxis.setLabel("Number");

            XYChart.Series<LocalDate, Long> series1 = new XYChart.Series<>();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                series1.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
            }
            Scene scene = new Scene(bc, 600, 600);
            bc.getData().addAll(series1);            
            bc.setLegendVisible(false);            
            StagetoShow.setTitle(felirat);
            StagetoShow.setScene(scene);
            //scene.getStylesheets().add("barchartsample/Chart.css");
            StagetoShow.show();
        } else {
            logger.error("No File Selected Yet!");
            FileProcesser.showError(2);
        }
    }
}
