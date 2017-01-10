package com.alphasystem.app.sarfengine.ui;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author sali
 */
public class MorphologicalEngineApp extends Application {

    private static final String OPEN_ARG = "open";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Morphological Engine UI");

        final Parameters parameters = getParameters();
        Map<String, String> namedParameters = (parameters == null) ? new HashMap<>() : parameters.getNamed();
        boolean noArg = namedParameters.isEmpty();
        if (noArg) {
            showStage(primaryStage, null);
        } else {
            Set<Map.Entry<String, String>> entries = namedParameters.entrySet();
            for (Map.Entry<String, String> parameter : entries) {
                String name = parameter.getKey();
                String value = parameter.getValue();
                if (isBlank(name) || isBlank(value)) {
                    continue;
                }
                if(OPEN_ARG.equals(name)){
                    open(primaryStage, value);
                }
            }
        }
    }

    private void showStage(Stage primaryStage, File file) {
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        MorphologicalEnginePane pane = new MorphologicalEnginePane(file);
        Scene scene = new Scene(pane);
        scene.getStylesheets().addAll("/styles/glyphs_custom.css");
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnShowing(event -> pane.initDependencies(primaryStage));
    }

    private void open(Stage primaryStage, String fileName) {
        showStage(primaryStage, new File(fileName));
    }

}
