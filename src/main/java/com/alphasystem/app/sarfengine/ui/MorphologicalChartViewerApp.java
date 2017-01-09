package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.docx.MorphologicalChartEngine;
import com.alphasystem.app.sarfengine.ui.control.MorphologicalChartViewerControl;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationData;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * @author sali
 */
public class MorphologicalChartViewerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Morphological Chart Viewer");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.setWidth(bounds.getWidth() / 4);
        primaryStage.setHeight(bounds.getHeight() / 4);

        ConjugationTemplate conjugationTemplate = new ConjugationTemplate();
        ConjugationData conjugationData = new ConjugationData();
        conjugationData.setTemplate(NamedTemplate.FORM_IV_TEMPLATE);
        conjugationData.setTranslation("To Submit");
        // conjugationData.addVerbalNouns(VerbalNoun.VERBAL_NOUN_V1, VerbalNoun.VERBAL_NOUN_V27);
        conjugationData.setRootLetters(new RootLetters(ArabicLetterType.HAMZA, ArabicLetterType.MEEM, ArabicLetterType.NOON));
        conjugationTemplate.getData().add(conjugationData);

        MorphologicalChartEngine engine = new MorphologicalChartEngine(conjugationTemplate);
        final MorphologicalChart morphologicalChart = engine.createMorphologicalCharts().get(0);

        MorphologicalChartViewerControl morphologicalChartViewer = new MorphologicalChartViewerControl();
        morphologicalChartViewer.setMorphologicalChart(morphologicalChart);

        Scene scene = new Scene(morphologicalChartViewer);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
