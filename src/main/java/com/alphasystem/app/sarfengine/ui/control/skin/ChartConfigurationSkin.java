package com.alphasystem.app.sarfengine.ui.control.skin;

import com.alphasystem.app.sarfengine.ui.control.ChartConfigurationView;
import com.alphasystem.fx.ui.util.FontSizeStringConverter;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirection;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirective;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * @author sali
 */
public class ChartConfigurationSkin extends SkinBase<ChartConfigurationView> {

    private static final Long[] SIZE_ARRAY = new Long[]{8L, 9L, 10L, 11L, 12L, 14L, 16L, 18L, 20L, 22L, 24L, 26L, 28L, 30L, 36L, 48L, 72L};

    /**
     * @param control chart configuration control
     */
    public ChartConfigurationSkin(ChartConfigurationView control) {
        super(control);

        BorderPane mainPane = new BorderPane();
        mainPane.setCenter(initializePane());
        getChildren().add(mainPane);
    }

    private BorderPane initializeConfigurationPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        ChartConfigurationView view = getSkinnable();

        int row = 0;
        CheckBox checkBox = new CheckBox("Omit Abbreviated Conjugation");
        checkBox.selectedProperty().bindBidirectional(view.omitAbbreviatedConjugationProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Detailed Conjugation");
        checkBox.selectedProperty().bindBidirectional(view.omitDetailedConjugationProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        checkBox = new CheckBox("Omit Table of Content");
        checkBox.selectedProperty().bindBidirectional(view.omitTocProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Title");
        checkBox.selectedProperty().bindBidirectional(view.omitTitleProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        checkBox = new CheckBox("Omit Header");
        checkBox.selectedProperty().bindBidirectional(view.omitHeaderProperty());
        gridPane.add(checkBox, 0, row);

        checkBox = new CheckBox("Omit Sarf Term Caption");
        checkBox.selectedProperty().bindBidirectional(view.omitSarfTermCaptionProperty());
        gridPane.add(checkBox, 1, row);

        row++;
        Label label = new Label("Sort Directive:");
        gridPane.add(label, 0, row);
        ComboBox<SortDirective> sortDirectiveComboBox = new ComboBox<>(observableArrayList(SortDirective.values()));
        sortDirectiveComboBox.valueProperty().bindBidirectional(view.sortDirectiveProperty());
        sortDirectiveComboBox.getSelectionModel().select(0);
        label.setLabelFor(sortDirectiveComboBox);

        label = new Label("Sort Direction:");
        gridPane.add(label, 1, row);
        ComboBox<SortDirection> sortDirectionComboBox = new ComboBox<>(observableArrayList(SortDirection.values()));
        sortDirectionComboBox.valueProperty().bindBidirectional(view.sortDirectionProperty());
        sortDirectionComboBox.getSelectionModel().select(0);
        label.setLabelFor(sortDirectionComboBox);

        row++;
        gridPane.add(sortDirectiveComboBox, 0, row);
        gridPane.add(sortDirectionComboBox, 1, row);

        gridPane.getStyleClass().add("view");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));
        borderPane.setCenter(gridPane);

        return borderPane;
    }

    private BorderPane initializeFontPane() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        ChartConfigurationView view = getSkinnable();

        Label label = new Label("Arabic Font Family:");
        gridPane.add(label, 0, 0);
        ComboBox<String> arabicFontFamilyComboBox = new ComboBox<>();
        arabicFontFamilyComboBox.getItems().addAll(Font.getFamilies());
        arabicFontFamilyComboBox.setValue(view.getArabicFontFamily());
        label.setLabelFor(arabicFontFamilyComboBox);
        arabicFontFamilyComboBox.valueProperty().bindBidirectional(view.arabicFontFamilyProperty());
        gridPane.add(arabicFontFamilyComboBox, 1, 0);

        label = new Label("Translation Font Family:");
        gridPane.add(label, 2, 0);
        ComboBox<String> translationFontFamilyComboBox = new ComboBox<>();
        translationFontFamilyComboBox.getItems().addAll(Font.getFamilies());
        translationFontFamilyComboBox.setValue(view.getTranslationFontFamily());
        label.setLabelFor(translationFontFamilyComboBox);
        translationFontFamilyComboBox.valueProperty().bindBidirectional(view.translationFontFamilyProperty());
        gridPane.add(translationFontFamilyComboBox, 3, 0);

        label = new Label("UI Arabic Font Size:");
        gridPane.add(label, 0, 1);
        ComboBox<Long> uiArabicFontSizeComboBox = new ComboBox<>();
        uiArabicFontSizeComboBox.setEditable(true);
        uiArabicFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        uiArabicFontSizeComboBox.setConverter(new FontSizeStringConverter());
        uiArabicFontSizeComboBox.setValue(view.getArabicUiFontSize());
        label.setLabelFor(uiArabicFontSizeComboBox);
        uiArabicFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setArabicUiFontSize(newValue));
        view.arabicUiFontSizeProperty().addListener((observable, oldValue, newValue) -> uiArabicFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(uiArabicFontSizeComboBox, 1, 1);

        label = new Label("UI Translation Font Size:");
        gridPane.add(label, 2, 1);
        ComboBox<Long> uiTranslationFontSizeComboBox = new ComboBox<>();
        uiTranslationFontSizeComboBox.setEditable(true);
        uiTranslationFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        uiTranslationFontSizeComboBox.setConverter(new FontSizeStringConverter());
        uiTranslationFontSizeComboBox.setValue(view.getTranslationUiFontSize());
        label.setLabelFor(uiTranslationFontSizeComboBox);
        uiTranslationFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setTranslationUiFontSize(newValue));
        view.translationUiFontSizeProperty().addListener((observable, oldValue, newValue) -> uiTranslationFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(uiTranslationFontSizeComboBox, 3, 1);

        label = new Label("UI Heading Font Size:");
        gridPane.add(label, 0, 2);
        ComboBox<Long> uiHeadingFontSizeComboBox = new ComboBox<>();
        uiHeadingFontSizeComboBox.setEditable(true);
        uiHeadingFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        uiHeadingFontSizeComboBox.setConverter(new FontSizeStringConverter());
        uiHeadingFontSizeComboBox.setValue(view.getHeadingUiFontSize());
        label.setLabelFor(uiHeadingFontSizeComboBox);
        uiHeadingFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setHeadingUiFontSize(newValue));
        view.headingUiFontSizeProperty().addListener((observable, oldValue, newValue) -> uiHeadingFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(uiHeadingFontSizeComboBox, 1, 2);

        label = new Label("Word Arabic Font Size:");
        gridPane.add(label, 2, 2);
        ComboBox<Long> wordArabicFontSizeComboBox = new ComboBox<>();
        wordArabicFontSizeComboBox.setEditable(true);
        wordArabicFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        wordArabicFontSizeComboBox.setConverter(new FontSizeStringConverter());
        wordArabicFontSizeComboBox.setValue(view.getArabicFontSize());
        label.setLabelFor(wordArabicFontSizeComboBox);
        wordArabicFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setArabicFontSize(newValue));
        view.translationFontSizeProperty().addListener((observable, oldValue, newValue) -> wordArabicFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(wordArabicFontSizeComboBox, 3, 2);

        label = new Label("Word Translation Font Size:");
        gridPane.add(label, 0, 3);
        ComboBox<Long> wordTranslationFontSizeComboBox = new ComboBox<>();
        wordTranslationFontSizeComboBox.setEditable(true);
        wordTranslationFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        wordTranslationFontSizeComboBox.setConverter(new FontSizeStringConverter());
        wordTranslationFontSizeComboBox.setValue(view.getTranslationFontSize());
        label.setLabelFor(wordTranslationFontSizeComboBox);
        wordTranslationFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setTranslationFontSize(newValue));
        view.translationFontSizeProperty().addListener((observable, oldValue, newValue) -> wordTranslationFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(wordTranslationFontSizeComboBox, 1, 3);

        label = new Label("Word Heading Font Size:");
        gridPane.add(label, 2, 3);
        ComboBox<Long> wordHeadingFontSizeComboBox = new ComboBox<>();
        wordHeadingFontSizeComboBox.setEditable(true);
        wordHeadingFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        wordHeadingFontSizeComboBox.setConverter(new FontSizeStringConverter());
        wordHeadingFontSizeComboBox.setValue(view.getHeadingFontSize());
        label.setLabelFor(wordHeadingFontSizeComboBox);
        wordHeadingFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setHeadingFontSize(newValue));
        view.headingFontSizeProperty().addListener((observable, oldValue, newValue) -> wordHeadingFontSizeComboBox.setValue((Long) newValue));
        gridPane.add(wordHeadingFontSizeComboBox, 3, 3);


        gridPane.getStyleClass().add("view");
        BorderPane borderPane = new BorderPane();
        borderPane.setPadding(new Insets(20, 20, 20, 20));
        borderPane.setCenter(gridPane);

        return borderPane;
    }

    private BorderPane initializePane() {
        BorderPane borderPane = new BorderPane();

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getSelectionModel().selectFirst();

        tabPane.getTabs().add(new Tab("Chart Configuration", initializeConfigurationPane()));
        tabPane.getTabs().add(new Tab("Font Configuration", initializeFontPane()));

        borderPane.setCenter(tabPane);

        return borderPane;
    }
}
