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

        int row = 0;
        Label label = new Label("Arabic Font Family:");
        gridPane.add(label, 0, row);
        ComboBox<String> arabicFontFamilyComboBox = new ComboBox<>();
        arabicFontFamilyComboBox.getItems().addAll(Font.getFamilies());
        arabicFontFamilyComboBox.getSelectionModel().selectFirst();
        label.setLabelFor(arabicFontFamilyComboBox);
        arabicFontFamilyComboBox.valueProperty().bindBidirectional(view.arabicFontFamilyProperty());

        label = new Label("Translation Font Family");
        gridPane.add(label, 1, row);
        ComboBox<String> translationFontFamilyComboBox = new ComboBox<>();
        translationFontFamilyComboBox.getItems().addAll(Font.getFamilies());
        translationFontFamilyComboBox.getSelectionModel().selectFirst();
        label.setLabelFor(translationFontFamilyComboBox);
        translationFontFamilyComboBox.valueProperty().bindBidirectional(view.translationFontFamilyProperty());

        row++;
        gridPane.add(arabicFontFamilyComboBox, 0, row);
        gridPane.add(translationFontFamilyComboBox, 1, row);

        row++;
        label = new Label("UI Arabic Font Size:");
        gridPane.add(label, 0, row);
        ComboBox<Long> uiArabicFontSizeComboBox = new ComboBox<>();
        uiArabicFontSizeComboBox.setEditable(true);
        uiArabicFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        uiArabicFontSizeComboBox.setConverter(new FontSizeStringConverter());
        uiArabicFontSizeComboBox.getSelectionModel().select(view.getArabicUiFontSize());
        label.setLabelFor(uiArabicFontSizeComboBox);
        uiArabicFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setArabicUiFontSize(newValue));
        view.arabicUiFontSizeProperty().addListener((observable, oldValue, newValue) -> uiArabicFontSizeComboBox.setValue((Long) newValue));

        label = new Label("UI Translation Font Size:");
        gridPane.add(label, 1, row);
        ComboBox<Long> uiTranslationFontSizeComboBox = new ComboBox<>();
        uiTranslationFontSizeComboBox.setEditable(true);
        uiTranslationFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        uiTranslationFontSizeComboBox.setConverter(new FontSizeStringConverter());
        uiTranslationFontSizeComboBox.getSelectionModel().select(view.getTranslationUiFontSize());
        label.setLabelFor(uiTranslationFontSizeComboBox);
        uiTranslationFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setTranslationUiFontSize(newValue));
        view.translationUiFontSizeProperty().addListener((observable, oldValue, newValue) -> uiTranslationFontSizeComboBox.setValue((Long) newValue));

        row++;
        gridPane.add(uiArabicFontSizeComboBox, 0, row);
        gridPane.add(uiTranslationFontSizeComboBox, 1, row);

        row++;
        label = new Label("Word Arabic Font Size:");
        gridPane.add(label, 0, row);
        ComboBox<Long> wordArabicFontSizeComboBox = new ComboBox<>();
        wordArabicFontSizeComboBox.setEditable(true);
        wordArabicFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        wordArabicFontSizeComboBox.setConverter(new FontSizeStringConverter());
        wordArabicFontSizeComboBox.getSelectionModel().select(view.getArabicFontSize());
        label.setLabelFor(wordArabicFontSizeComboBox);
        wordArabicFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setArabicFontSize(newValue));
        view.translationFontSizeProperty().addListener((observable, oldValue, newValue) -> wordArabicFontSizeComboBox.setValue((Long) newValue));

        label = new Label("Word Translation Font Size:");
        gridPane.add(label, 1, row);
        ComboBox<Long> wordTranslationFontSizeComboBox = new ComboBox<>();
        wordTranslationFontSizeComboBox.setEditable(true);
        wordTranslationFontSizeComboBox.getItems().addAll(SIZE_ARRAY);
        wordTranslationFontSizeComboBox.setConverter(new FontSizeStringConverter());
        wordTranslationFontSizeComboBox.getSelectionModel().select(view.getTranslationFontSize());
        label.setLabelFor(wordTranslationFontSizeComboBox);
        wordTranslationFontSizeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> view.setTranslationFontSize(newValue));
        view.translationFontSizeProperty().addListener((observable, oldValue, newValue) -> wordTranslationFontSizeComboBox.setValue((Long) newValue));

        row++;
        gridPane.add(wordArabicFontSizeComboBox, 0, row);
        gridPane.add(wordTranslationFontSizeComboBox, 1, row);

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
