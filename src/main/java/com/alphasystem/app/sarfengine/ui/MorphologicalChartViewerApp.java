package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationBuilder;
import com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationRoots;
import com.alphasystem.app.morphologicalengine.conjugation.model.NounRootBase;
import com.alphasystem.app.morphologicalengine.guice.GuiceSupport;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartControl;
import com.alphasystem.app.morphologicalengine.util.VerbalNounFactory;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.alphasystem.app.morphologicalengine.conjugation.builder.ConjugationHelper.getConjugationRoots;
import static com.alphasystem.arabic.model.NamedTemplate.FORM_I_CATEGORY_I_GROUP_A_TEMPLATE;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;

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

        ConjugationBuilder conjugationBuilder = GuiceSupport.getInstance().getConjugationBuilder();

        final NounRootBase[] verbalNouns = {VerbalNounFactory.getByVerbalNoun(VerbalNoun.VERBAL_NOUN_V1),
                VerbalNounFactory.getByVerbalNoun(VerbalNoun.VERBAL_NOUN_V27)};
        final ConjugationRoots conjugationRoots = getConjugationRoots(FORM_I_CATEGORY_I_GROUP_A_TEMPLATE, "To Mercy",
                verbalNouns, null);

        MorphologicalChartControl morphologicalChartControl = new MorphologicalChartControl();
        morphologicalChartControl.setMorphologicalChart(conjugationBuilder.doConjugation(conjugationRoots,
                ArabicLetterType.RA, ArabicLetterType.HHA, ArabicLetterType.MEEM, null));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(morphologicalChartControl);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);
        Scene scene = new Scene(scrollPane);
        primaryStage.setMaximized(true);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

}
