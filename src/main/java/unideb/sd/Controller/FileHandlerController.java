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
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import unideb.sd.Model.FileProcesser;
import static unideb.sd.Main.MainApp.SecoundStage;

public class FileHandlerController implements Initializable {

    public static ObservableList<String> CBvalues = FXCollections.observableArrayList("ID", "DateTime", "Latitude", "Longitude");
    public static ObservableList<String> Splitvalues = FXCollections.observableArrayList(",", ";", "TAB");
    private static final Logger logger = LoggerFactory.getLogger(FileHandlerController.class);
    private final FileChooser fileChooser = new FileChooser();
    private static String FilePath = "NOFILE";
    private static String FileName;
    private String ChoosenOne;
    private String validatecombination;

    @FXML
    private Button AddButton;

    @FXML
    private Button AddCloseButton;

    @FXML
    private Button ExitButton;

    @FXML
    private Button ChooseButton;

    @FXML
    private Label FilenameLabel;

    @FXML
    private ChoiceBox FirstCB;

    @FXML
    private ChoiceBox SecoundCB;

    @FXML
    private ChoiceBox ThirdCB;

    @FXML
    private ChoiceBox ForthCB;
    
    @FXML
    private ChoiceBox SplitCB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FilenameLabel.setText("No file selected yet");
        setupEventHandlers();
        setupCBitems();
    }

    private void setupEventHandlers() {
        ChooseButton.setOnAction((final ActionEvent e) -> {
            configureFileChooser(fileChooser);
            File file = fileChooser.showOpenDialog(SecoundStage);
            if (file != null) {
                FilePath = file.getAbsolutePath();
                FileName = file.getName();
                FilenameLabel.setText(FileName);
            }
        });
        //FirstCB.getSelectionModel().selectedIndexProperty().addListener(ChangeListener<? super T>)
    }

    private static void configureFileChooser(final FileChooser fileChooser) {
        fileChooser.setTitle("Choose Input File");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
    }

    private void setupCBitems() {
        FirstCB.setItems(CBvalues);
        SecoundCB.setItems(CBvalues);
        ThirdCB.setItems(CBvalues);
        ForthCB.setItems(CBvalues);
        SplitCB.setItems(Splitvalues);
    }

    @FXML
    private void ExitButtonAction(ActionEvent event) {
        Stage stage = (Stage) ExitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void AddButtonAction(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE") && ValidaterandSetter()) {
            MapViewController.initializefile(FilePath, FileName);
        } else {
            logger.error("No file added yet or invalid combination used");
        }
    }

    @FXML
    private void AddCloseButtonAction(ActionEvent event) {
        if (!FilePath.equalsIgnoreCase("NOFILE") && ValidaterandSetter()) {
            MapViewController.initializefile(FilePath, FileName);
            Stage stage = (Stage) AddCloseButton.getScene().getWindow();
            stage.close();
        } else {
            logger.error("No file added yet or invalid combination used");
        }
    }

    private boolean ValidaterandSetter() {
        setValidatecombination(null);

        ChoosenOne = (String) FirstCB.getValue();
        Switcher(ChoosenOne, 1);
        setValidatecombination(ChoosenOne);

        ChoosenOne = (String) SecoundCB.getValue();
        Switcher(ChoosenOne, 2);
        setValidatecombination(ChoosenOne);

        ChoosenOne = (String) ThirdCB.getValue();
        Switcher(ChoosenOne, 3);
        setValidatecombination(ChoosenOne);

        ChoosenOne = (String) ForthCB.getValue();
        Switcher(ChoosenOne, 4);
        setValidatecombination(ChoosenOne);
        
        ChoosenOne = (String) SplitCB.getValue();
        FileProcesser.setCvsSplitBy(ChoosenOne);

        if (StringUtils.countMatches(getValidatecombination(), "ID") > 1) {
            return false;
        } else if (StringUtils.countMatches(getValidatecombination(), "DateTime") > 1) {
            return false;
        } else if (StringUtils.countMatches(getValidatecombination(), "Latitude") > 1) {
            return false;
        } else if (StringUtils.countMatches(getValidatecombination(), "Longitude") > 1) {
            return false;
        } else {
            return true;
        }        
    }

    public String getValidatecombination() {
        return validatecombination;
    }

    public void setValidatecombination(String validation) {
        if (validation == null) {
            validatecombination = null;
        }
        else validatecombination += validation;
    }

    /*Icantfindbettersolutionforthis*/
    private void Switcher(String switcher, int columnumber) {
        switch (columnumber) {
            case 1:
                switch (switcher) {
                    case "ID":
                        FileProcesser.setFirstcolumn(0);
                        break;
                    case "DateTime":
                        FileProcesser.setFirstcolumn(1);
                        break;
                    case "Latitude":
                        FileProcesser.setFirstcolumn(2);
                        break;
                    case "Longitude":
                        FileProcesser.setFirstcolumn(3);
                        break;
                }
            case 2:
                switch (switcher) {
                    case "ID":
                        FileProcesser.setSecoundcolumn(0);
                        break;
                    case "DateTime":
                        FileProcesser.setSecoundcolumn(1);
                        break;
                    case "Latitude":
                        FileProcesser.setSecoundcolumn(2);
                        break;
                    case "Longitude":
                        FileProcesser.setSecoundcolumn(3);
                        break;
                }
            case 3:
                switch (switcher) {
                    case "ID":
                        FileProcesser.setThirdcolumn(0);
                        break;
                    case "DateTime":
                        FileProcesser.setThirdcolumn(1);
                        break;
                    case "Latitude":
                        FileProcesser.setThirdcolumn(2);
                        break;
                    case "Longitude":
                        FileProcesser.setThirdcolumn(3);
                        break;
                }
            case 4:
                switch (switcher) {
                    case "ID":
                        FileProcesser.setForthcolumn(0);
                        break;
                    case "DateTime":
                        FileProcesser.setForthcolumn(1);
                        break;
                    case "Latitude":
                        FileProcesser.setForthcolumn(2);
                        break;
                    case "Longitude":
                        FileProcesser.setForthcolumn(3);
                        break;
                }
        }
    }
}
