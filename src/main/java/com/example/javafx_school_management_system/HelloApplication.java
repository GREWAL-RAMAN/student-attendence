package com.example.javafx_school_management_system;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {
    public static Scene scene;
    static int Num=0;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"), 1370, 650);
        stage.setMinHeight(500);
        stage.setMinWidth(800);
        stage.setMaximized(true);
        stage.setScene(scene);
        Num=1;

        stage.show();

    }

    static void setRoot(String fxml) throws IOException {
        Num=2;
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    @Override
    public void stop(){
        if(Num==2){connection.closeConn();}
        System.out.println("Stage is closing");
        // Save file
    }

    public static void main(String[] args) {
        launch();
    }
}