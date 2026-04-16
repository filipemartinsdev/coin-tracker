package com.cointracker;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class CoinTrackerApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CoinTrackerApplication.class.getResource("main.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Coin Tracker v1.0.0");
        stage.setScene(scene);
        stage.show();
    }
}
