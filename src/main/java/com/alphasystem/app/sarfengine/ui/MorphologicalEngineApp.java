package com.alphasystem.app.sarfengine.ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author sali
 */
public class MorphologicalEngineApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Morphological Engine UI");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        MorphologicalEnginePane pane = new MorphologicalEnginePane();
        Scene scene = new Scene(pane);
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnShowing(event -> pane.setDialogOwner(primaryStage));
    }

}
