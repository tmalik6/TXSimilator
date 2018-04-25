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
import unideb.sd.Model.HeatMapPoint;
import unideb.sd.Model.Taxi;
import com.sothawo.mapjfx.Coordinate;
import com.sothawo.mapjfx.CoordinateLine;
import com.sothawo.mapjfx.MapLabel;
import com.sothawo.mapjfx.MapView;
import com.sothawo.mapjfx.event.MapLabelEvent;
import com.sothawo.mapjfx.event.MapViewEvent;
import com.sothawo.mapjfx.event.MarkerEvent;
import com.sothawo.mapjfx.offline.OfflineCache;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import static unideb.sd.Main.MainApp.SecoundStage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import unideb.sd.Model.FileProcesser;

public class MapViewController {

    private static final Logger logger = LoggerFactory.getLogger(MapViewController.class);
    //
    public static Timeline animation;
    public static String FilePath = "NOFILE";
    public static String FileName;
    private final FileChooser fileChooser = new FileChooser();
    public static boolean isPause = true;
    private static final int ZOOM_DEFAULT = 18;
    //
    public LocalDateTime Ido = LocalDateTime.of(2007, Month.JANUARY, 1, 0, 0, 0);
    private static final Coordinate coordMapCenter = new Coordinate(37.786956, -122.440279);
    public List<Taxi> TaxiList = new ArrayList<>();
    public List<CoordinateLine> CoordinateLineList = new ArrayList<>();
    public static List<LocalDateTime> TimeStamps = new ArrayList<>();
    public static List<List<Double>> IdsandCoordinates = new ArrayList<List<Double>>();
    public List<HeatMapPoint> Points = new ArrayList<>();
    public int heatmapCounter = 0;
    private int LayoutX = 280; //280
    private int LayoutY = 1;   //1
    private int ScalaX = 6;    //6
    private int ScalaY = 6;    //6

    @FXML
    private MapView mapView;

    @FXML
    private HBox topControls;

    @FXML
    private Slider sliderZoom;

    @FXML
    private Accordion leftControls;

    @FXML
    private TitledPane optionsLocations;

    @FXML
    private Button buttonMapCenter;

    @FXML
    private Button taxiadd;

    @FXML
    private Button BCactionhour;

    @FXML
    private Button BCactionminute;

    @FXML
    private Button BCactiondate;

    @FXML
    private Button RestartAnimation;

    @FXML
    private Button previousLinesButton;

    @FXML
    private Button nextLinesButton;

    @FXML
    private Button Addfile;

    @FXML
    private Button buttonZoom;

    @FXML
    private Button HeatMapButton;

    @FXML
    private TextField LayoutXTF;

    @FXML
    private TextField LayoutYTF;

    @FXML
    private TextField ScalaXTF;

    @FXML
    private TextField ScalaYTF;

    @FXML
    private Label labelCenter;

    @FXML
    private Label labelZoom;

    @FXML
    private Label labelEvent;

    @FXML
    private Label IdoLabel;

    @FXML
    private Label ErrorLabel;

    @FXML
    private Label AnimationLabel;

    public MapViewController() {
        animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> moveSC()));
        animation.setCycleCount(Timeline.INDEFINITE);
    }

    public void initialize() {
        logger.trace("begin initialize cache and map");

        final OfflineCache offlineCache = mapView.getOfflineCache();
        final String cacheDir = System.getProperty("java.io.tmpdir") + "\\mapjfx-cache";
        logger.info("Following dir will be used: " + cacheDir);
        try {
            Files.createDirectories(Paths.get(cacheDir));
            offlineCache.setCacheDirectory(cacheDir);
            offlineCache.setActive(true);
        } catch (IOException e) {
            logger.warn("Problem with OfflineCache", e);
        }

        mapView.setCustomMapviewCssURL(getClass().getResource("/custom_mapview.css"));

        leftControls.setExpandedPane(optionsLocations);

        // set the controls to disabled, this will be changed when the MapView is intialized
        setControlsDisable(true);

        buttonMapCenter.setOnAction(event -> mapView.setCenter(coordMapCenter));

        buttonZoom.setOnAction(event -> mapView.setZoom(ZOOM_DEFAULT));
        sliderZoom.valueProperty().bindBidirectional(mapView.zoomProperty());

        // bind the map's center and zoom properties to the corresponding labels and format them
        labelCenter.textProperty().bind(Bindings.format("center: %s", mapView.centerProperty()));
        labelZoom.textProperty().bind(Bindings.format("zoom: %.0f", mapView.zoomProperty()));
        mapView.initializedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                afterMapIsInitialized();
            }
        });
        setupEventHandlers();
        //ErrorLabel.setText("Input File Needed");

        logger.trace("start map initialization");
        mapView.initialize();
        logger.debug("initialization finished");
    }

    private void setupEventHandlers() {
        mapView.addEventHandler(MapViewEvent.MAP_EXTENT, event -> {
            event.consume();
            mapView.setExtent(event.getExtent());
        });

        mapView.addEventHandler(MapViewEvent.MAP_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: map right clicked at: " + event.getCoordinate());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MarkerEvent.MARKER_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: marker right clicked: " + event.getMarker().getId());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_CLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label clicked: " + event.getMapLabel().getText());
        });
        mapView.addEventHandler(MapLabelEvent.MAPLABEL_RIGHTCLICKED, event -> {
            event.consume();
            labelEvent.setText("Event: label right clicked: " + event.getMapLabel().getText());
        });
        Addfile.setOnAction((final ActionEvent e) -> {
            Parent root;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FileHandler.fxml"));
                root = loader.load();
                Scene scene = new Scene(root);
                SecoundStage.setScene(scene);
                SecoundStage.show();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(FileHandlerController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        logger.trace("map handlers initialized");
    }

    /*
    ErrorLabel.setText("Input file: " + FileName); !!!
     */
    private void setControlsDisable(boolean flag) {
        topControls.setDisable(flag);
        leftControls.setDisable(flag);
    }

    public static void initializefile(String FilePathP, String FileNameP) {
        FilePath = FilePathP;
        FileName = FileNameP;
        FileProcesser.readcsv(FilePath);
        TimeStamps = FileProcesser.getDates();
        IdsandCoordinates = FileProcesser.getIdsandCoordinates();
    }

    private void afterMapIsInitialized() {
        logger.debug("setting center and enabling controls...");
        mapView.setZoom(ZOOM_DEFAULT);
        mapView.setCenter(coordMapCenter);
        setControlsDisable(false);

    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose Input File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    private Optional<CoordinateLine> loadCoordinateLine(URL url) {
        try (
                Stream<String> lines = new BufferedReader(
                        new InputStreamReader(url.openStream(), StandardCharsets.UTF_8)).lines()) {
            return Optional.of(new CoordinateLine(
                    lines.map(line -> line.split(";")).filter(array -> array.length == 2)
                            .map(values -> new Coordinate(Double.valueOf(values[0]), Double.valueOf(values[1])))
                            .collect(Collectors.toList())));
        } catch (IOException | NumberFormatException e) {
            logger.error("load {}", url, e);
        }
        return Optional.empty();
    }

    public void moveSC() {
        Ido = Ido.plusSeconds(1);
        IdoLabel.setText(Ido.toString());
        removemarker(false);
        for (int i = 0; i < TimeStamps.size(); i++) {
            if (Ido.equals(TimeStamps.get(i))) {
                addmarker(IdsandCoordinates.get(i).get(0), IdsandCoordinates.get(i).get(1), IdsandCoordinates.get(i).get(2));
            }
        }
    }

    public void addmarker(double id, double lati, double longi) {
        Taxi MKA = new Taxi(lati, longi);
        MKA.setDate(Ido);
        MKA.setId((int) id);
        MKA.setLati(lati);
        MKA.setLongi(longi);
        MKA.setCoordTAXD(lati, longi);
        String idlabel = (int) id + "";
        MKA.setLabelTax(new MapLabel(idlabel).setPosition(MKA.getCoordTAX()).setVisible(true));
        TaxiList.add(MKA);
        mapView.addMarker(MKA.getMarker());
        mapView.addLabel(MKA.getLabelTax());
    }

    private void removemarker(boolean all) {
        if (!all) {
            for (int i = 0; i < TaxiList.size(); i++) {
                if (TaxiList.get(i).getDate().equals(Ido.minusSeconds(4))) {
                    mapView.removeMarker(TaxiList.get(i).getMarker());
                    mapView.removeLabel(TaxiList.get(i).getLabelTax());
                }
            }
        } else {
            for (int i = 0; i < TaxiList.size(); i++) {
                mapView.removeMarker(TaxiList.get(i).getMarker());
                mapView.removeLabel(TaxiList.get(i).getLabelTax());

            }
        }
    }

    public static void Pause() {
        animation.pause();
    }

    public static void Start() {
        animation.play();
    }

    @FXML
    private void taxiaddAction(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            if (isPause) {
                Start();
                isPause = false;
                AnimationLabel.setText("Animation Started");
                taxiadd.setText("Pause Animation");
            } else {
                Pause();
                isPause = true;
                AnimationLabel.setText("Animation Paused");
                taxiadd.setText("Start Animation");
            }
        } else {
            logger.error("No File Selected Yet!");
            FileProcesser.showError(2);
        }
    }

    public void RestartAnimation() {
        if (isPause && !FilePath.equalsIgnoreCase("NOFILE")) {
            Ido = LocalDateTime.of(2007, Month.JANUARY, 1, 0, 0, 0);
            IdoLabel.setText(Ido.toString());
            AnimationLabel.setText("Animation Restarted");
            removemarker(true);
        } else {
            logger.error("Animation should be paused or No input File");
            FileProcesser.showError(2);
        }
    }

    @FXML
    private void BCactionhour(ActionEvent event) {
        Map<Integer, Long> LocalMap = FileProcesser.hourmaker();
        BarchartActions.BarchartActionsGeneral(LocalMap, "Data per Hour", "Hours", FilePath, SecoundStage);
    }

    @FXML
    private void BCactionminute(ActionEvent event) {
        Map<Integer, Long> LocalMap = FileProcesser.minutemaker();
        BarchartActions.BarchartActionsGeneral(LocalMap, "Data per minutes", "Minutes", FilePath, SecoundStage);
    }

    @FXML
    private void BCactiondate(ActionEvent event) {
        Map<LocalDate, Long> LocalMap = FileProcesser.datamaker();
        BarchartActions.BarchartActionsLocalDate(LocalMap, "Data per Day", "Dates", FilePath, SecoundStage);
    }

    @FXML
    private void BCactiondays(ActionEvent event) {
        Map<Integer, Long> LocalMap = FileProcesser.hourmaker();
        //BCaction2(LocalMap);
    }

    @FXML
    private void RestartAnimationButton(ActionEvent event) {
        RestartAnimation();
    }

    @FXML
    private void previousLinesButtonAction(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            if (heatmapCounter == 0) {
                logger.error("counter already Zero");
            } else {
                heatmapCounter = heatmapCounter - 100;
                ShowHeatMap();
            }
        } else {
            FileProcesser.showError(2);
        }
    }

    @FXML
    private void nextLinesButtonAction(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            if (heatmapCounter == 2500) {
                logger.error("Max Counter reached");
            } else {
                heatmapCounter = heatmapCounter + 100;
                ShowHeatMap();
            }
        } else {
            FileProcesser.showError(2);
        }

    }

    @FXML
    private void HeatMapButton(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE")) {
            RestartAnimation();
            ShowHeatMap();
            //adding this for testing Layouts and Scalas
            /*if (!LayoutXTF.getText().isEmpty()){
            LayoutX = Integer.parseInt(LayoutXTF.getText());
            }
            if (!LayoutYTF.getText().isEmpty()){
               LayoutY = Integer.parseInt(LayoutYTF.getText());
            }
            if (!ScalaXTF.getText().isEmpty()){
               ScalaX = Integer.parseInt(ScalaXTF.getText());
            }
            if (!ScalaXTF.getText().isEmpty()){
               ScalaX = Integer.parseInt(ScalaXTF.getText());
            }*/
 /*Coordinate coord;
            Marker Markers;
            for (int i = 0; i < TimeStamps.size(); i++) {
                if (TimeStamps.get(i).isBefore(Ido.plusHours(1))) {
                    //addmarker(IdsandCoordinates.get(i).get(0), IdsandCoordinates.get(i).get(1), IdsandCoordinates.get(i).get(2));  
                    coord = new Coordinate(IdsandCoordinates.get(i).get(1), IdsandCoordinates.get(i).get(2));
                    //Markers = Marker.createProvided(Marker.Provided.BLUE).setVisible(true);
                    Markers = new Marker(getClass().getResource("/ksc.png"), -20, -20).setPosition(coord).setVisible(true);
                    mapView.addMarker(Markers);
                }
            }*/
        } else {
            FileProcesser.showError(2);
        }
    }

    private void cleanHeatMap() {
        for (int i = 0; i < CoordinateLineList.size(); i++) {
            mapView.removeCoordinateLine(CoordinateLineList.get(i));
        }

    }

    private void heatMapGenerator(int k) {
        List<Coordinate> coordinates = new ArrayList<>();
        for (int i = 0; i < TimeStamps.size(); i++) {
            if (IdsandCoordinates.get(i).get(0) == k) {
                Coordinate coord = new Coordinate(IdsandCoordinates.get(i).get(1), IdsandCoordinates.get(i).get(2));
                coordinates.add(coord);
            }
        }
        CoordinateLine coordinateLine = new CoordinateLine(coordinates);
        coordinateLine.setColor(Color.BLUEVIOLET).setWidth(3).setVisible(true);
        CoordinateLineList.add(coordinateLine);
    }

    public void ShowHeatMap() {
        cleanHeatMap();
        for (int i = heatmapCounter; i < heatmapCounter + 100; i++) {
            heatMapGenerator(i);
            mapView.addCoordinateLine(CoordinateLineList.get(i));
        }
    }

    public void ShowHeatMapOld() {
        final double SCALE_DELTA = 1.1;
        Pane HM = new Pane();
        //Points.add(new HeatMapPoint(100, 100));
        for (int i = 0; i < TimeStamps.size(); i++) {
            if (TimeStamps.get(i).isBefore(Ido.plusHours(1))) {
                HeatMapPoint HMP = new HeatMapPoint((Math.abs(IdsandCoordinates.get(i).get(1))), (Math.abs(IdsandCoordinates.get(i).get(2))));
                //System.out.println("1: " + (Math.abs(IdsandCoordinates.get(i).get(1))) + " 2:" + (Math.abs(IdsandCoordinates.get(i).get(2)) ));
                Points.add(HMP);
            }
        }
        System.out.println("Eredeti:" + HM.getLayoutY());
        HM.setLayoutX(LayoutX);
        HM.setLayoutY(LayoutY);
        System.out.println("Layoutx:" + HM.getLayoutX());
        System.out.println(HM.getLayoutY());
        HM.setScaleX(ScalaX);
        HM.setScaleY(ScalaY);
        HM.getChildren().addAll(Points);
        HM.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                event.consume();

                if (event.getDeltaY() == 0) {
                    return;
                }

                double scaleFactor
                        = (event.getDeltaY() > 0)
                        ? SCALE_DELTA
                        : 1 / SCALE_DELTA;

                HM.setScaleX(HM.getScaleX() * scaleFactor);
                HM.setScaleY(HM.getScaleY() * scaleFactor);
            }
        });
        Group group = new Group(HM);
        Scene scene = new Scene(group, 500, 500);
        SecoundStage.setTitle("HeatMap");
        SecoundStage.setScene(scene);
        SecoundStage.show();
    }
}
