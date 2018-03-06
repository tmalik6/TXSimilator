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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class MainApp extends Application {
        
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainApp.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setTitle("TXSimilator");
        primaryStage.setScene(scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> {
            if (key.getCode() == KeyCode.P) {
                if (Controller.isPause) {
                    Controller.Start();
                    Controller.isPause = false;
                } else {
                    Controller.Pause();
                    Controller.isPause = true;
                } 
            }
        });
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
