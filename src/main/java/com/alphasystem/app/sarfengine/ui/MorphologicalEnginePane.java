package com.alphasystem.app.sarfengine.ui;

import com.alphasystem.ApplicationException;
import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.docx.MorphologicalChartEngine;
import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.app.morphologicalengine.util.TemplateReader;
import com.alphasystem.app.sarfengine.ui.control.*;
import com.alphasystem.app.sarfengine.ui.control.model.TabInfo;
import com.alphasystem.app.sarfengine.ui.control.model.TableModel;
import com.alphasystem.arabic.model.NamedTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationData;
import com.alphasystem.morphologicalanalysis.morphology.model.ConjugationTemplate;
import com.alphasystem.morphologicalanalysis.morphology.model.RootLetters;
import com.alphasystem.morphologicalanalysis.morphology.model.support.VerbalNoun;
import com.alphasystem.util.GenericPreferences;
import de.jensd.fx.glyphs.GlyphIcons;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import static com.alphasystem.app.sarfengine.ui.Global.*;
import static com.alphasystem.arabic.ui.ComboBoxHelper.createComboBox;
import static de.jensd.fx.glyphs.GlyphsDude.setIcon;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.CLONE;
import static de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon.REMOVE;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.ADD_BOX;
import static de.jensd.fx.glyphs.materialicons.MaterialIcon.SETTINGS_APPLICATIONS;
import static java.lang.Math.max;
import static java.lang.String.format;
import static javafx.application.Platform.runLater;
import static javafx.collections.FXCollections.observableArrayList;
import static javafx.geometry.NodeOrientation.RIGHT_TO_LEFT;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import static javafx.scene.control.Alert.AlertType.ERROR;
import static javafx.scene.control.ButtonType.*;
import static javafx.scene.control.ContentDisplay.GRAPHIC_ONLY;
import static javafx.scene.control.ScrollPane.ScrollBarPolicy.AS_NEEDED;
import static javafx.scene.control.SelectionMode.SINGLE;
import static javafx.scene.control.TabPane.TabClosingPolicy.SELECTED_TAB;
import static javafx.scene.input.KeyCode.E;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCombination.ALT_DOWN;
import static javafx.scene.input.KeyCombination.CONTROL_DOWN;
import static javafx.scene.text.TextAlignment.CENTER;
import static javafx.stage.Screen.getPrimary;
import static org.apache.commons.io.FilenameUtils.getBaseName;

/**
 * @author sali
 */
class MorphologicalEnginePane extends BorderPane {

    private static final double DEFAULT_MIN_HEIGHT = 500.0;
    private static final double ROW_SIZE = 40.0;
    private static final Background BACKGROUND = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
    private static int counter = 1;

    private final TabPane tabPane;
    private final FileSelectionDialog fileSelectionDialog;
    private final ChartConfigurationDialog chartConfigurationDialog;
    private final MorphologicalChartViewerControl morphologicalChartViewer;
    private final Stage chartStage;
    private final TemplateReader templateReader = TemplateReader.getInstance();
    private final MorphologicalEnginePreferences preferences;

    MorphologicalEnginePane() {
        preferences = GenericPreferences.getInstance(MorphologicalEnginePreferences.class);
        tabPane = new TabPane();
        tabPane.setTabClosingPolicy(SELECTED_TAB);
        tabPane.setBackground(BACKGROUND);
        newAction();

        chartConfigurationDialog = new ChartConfigurationDialog();
        fileSelectionDialog = new FileSelectionDialog(new TabInfo());
        morphologicalChartViewer = new MorphologicalChartViewerControl();
        chartStage = new Stage();
        initViewerStage();

        setCenter(tabPane);
        setTop(createToolBar());
        setBackground(BACKGROUND);
    }

    private static String getTabTitle(File file) {
        return (file == null) ? format("Untitled %s", counter++) : getBaseName(file.getAbsolutePath());
    }

    private static double calculateTableHeight(int numOfRows) {
        double height = (numOfRows * ROW_SIZE) + ROW_SIZE;
        height = roundTo100(height);
        return max(height, DEFAULT_MIN_HEIGHT);
    }

    private Tab getCurrentTab() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    private TabInfo getTabUserData() {
        Tab currentTab = getCurrentTab();
        return (currentTab == null) ? null : ((TabInfo) currentTab.getUserData());
    }

    @SuppressWarnings({"unchecked"})
    private TableView<TableModel> getCurrentTable() {
        TableView<TableModel> tableView = null;
        Tab currentTab = getCurrentTab();
        if (currentTab != null) {
            ScrollPane scrollPane = (ScrollPane) currentTab.getContent();
            tableView = (TableView<TableModel>) scrollPane.getContent();
        }
        return tableView;
    }

    private void makeDirty(boolean dirty) {
        TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            tabInfo.setDirty(dirty);
        }
    }

    private Tab createTab(File file, ConjugationTemplate template) {
        Tab tab = new Tab(getTabTitle(file), createTable(template));
        TabInfo value = new TabInfo();
        if (file != null) {
            value.setSarfxFile(file);
            value.setDocxFile(TemplateReader.getDocxFile(file));
        }
        tab.setUserData(value);
        tab.setOnCloseRequest(event -> {
            TabInfo tabInfo = getTabUserData();
            if (tabInfo != null && tabInfo.getDirty()) {
                Alert alert = new Alert(CONFIRMATION);
                alert.setContentText("Do you want to save data before closing?");
                alert.getButtonTypes().setAll(YES, NO, CANCEL);
                Optional<ButtonType> result = alert.showAndWait();
                ButtonType buttonType = result.get();
                ButtonData buttonData = buttonType.getButtonData();
                String text = buttonType.getText();
                if (buttonData.isDefaultButton()) {
                    saveAction(SaveMode.SAVE);
                } else if (text.equals("Cancel")) {
                    event.consume();
                }
            }

        });
        return tab;
    }

    private ScrollPane createTable(ConjugationTemplate conjugationTemplate) {
        if (conjugationTemplate == null) {
            ChartConfiguration chartConfiguration = new ChartConfiguration();
            chartConfiguration.setArabicFontFamily(preferences.getArabicFontName());
            chartConfiguration.setTranslationFontFamily(preferences.getEnglishFontName());
            conjugationTemplate = new ConjugationTemplate();
            conjugationTemplate.setChartConfiguration(chartConfiguration);
        }
        ObservableList<TableModel> tableModels = observableArrayList();
        List<ConjugationData> dataList = conjugationTemplate.getData();
        if (dataList.isEmpty()) {
            dataList.add(new ConjugationData());
        }
        dataList.forEach(data -> tableModels.add(new TableModel(data)));

        double boundsWidth = getPrimary().getVisualBounds().getWidth();

        TableView<TableModel> tableView = new TableView<>(tableModels);
        tableView.setBackground(BACKGROUND);
        tableView.getSelectionModel().setSelectionMode(SINGLE);
        tableView.setEditable(true);
        initializeTable(tableView, boundsWidth);

        tableView.setFixedCellSize(ROW_SIZE);
        tableView.setPrefSize(boundsWidth, calculateTableHeight(tableModels.size()));
        ScrollPane scrollPane = new ScrollPane(tableView);
        scrollPane.setVbarPolicy(AS_NEEDED);
        scrollPane.setHbarPolicy(AS_NEEDED);

        return scrollPane;
    }

    private Button createButton(String tooltip, GlyphIcons icon, EventHandler<ActionEvent> event) {
        Button button = new Button();
        button.setTooltip(new Tooltip(tooltip));
        setIcon(button, icon, "2em");
        button.setOnAction(event);
        return button;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        toolBar.getItems().add(createButton("Create New File", FontAwesomeIcon.FILE_ALT, event -> newAction()));
        toolBar.getItems().add(createButton("Open File", FontAwesomeIcon.FOLDER_OPEN_ALT, event -> openAction()));

        MenuButton menuButton = new MenuButton();
        menuButton.setText("Save");
        setIcon(menuButton, FontAwesomeIcon.SAVE, "2em");

        MenuItem menuItem = new MenuItem("Save");

        menuItem.setAccelerator(new KeyCodeCombination(S, CONTROL_DOWN));
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE));
        setIcon(menuItem,  FontAwesomeIcon.SAVE);
        menuButton.getItems().add(menuItem);

        menuItem = new MenuItem("Save As ...");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE_AS));
        menuItem.setAccelerator(new KeyCodeCombination(S, ALT_DOWN, CONTROL_DOWN));
        menuButton.getItems().add(menuItem);

        menuItem = new MenuItem("Save Selected Data ...");
        menuItem.setOnAction(event -> saveAction(SaveMode.SAVE_SELECTED));
        menuButton.getItems().add(menuItem);
        toolBar.getItems().add(menuButton);

        menuButton = new MenuButton();
        setIcon(menuButton, MaterialDesignIcon.EXPORT, "2em");
        menuButton.setTooltip(new Tooltip("Export Conjugation to external format"));

        menuItem = new MenuItem();
        setIcon(menuItem, FontAwesomeIcon.FILE_WORD_ALT);
        menuItem.setAccelerator(new KeyCodeCombination(E, CONTROL_DOWN, ALT_DOWN));
        menuItem.setOnAction(event -> saveAction(SaveMode.EXPORT_TO_WORD));
        menuButton.getItems().add(menuItem);
        toolBar.getItems().add(menuButton);

        toolBar.getItems().add(new Separator());
        toolBar.getItems().add(createButton("Add new Row", ADD_BOX, event -> addNewRowAction()));
        toolBar.getItems().add(createButton("Duplicate Selected Row(s)", CLONE, event -> duplicateRowAction()));
        toolBar.getItems().add(createButton("Remove Selected Row", REMOVE, event -> removeRowAction()));
        toolBar.getItems().add(new Separator());
        toolBar.getItems().add(createButton("View / Edit Chart Configuration", SETTINGS_APPLICATIONS, event -> updateChartConfiguration()));

        return toolBar;
    }

    private void addNewRowAction() {
        TableView<TableModel> tableView = getCurrentTable();
        if (tableView != null) {
            ObservableList<TableModel> items = tableView.getItems();
            items.add(new TableModel());
            tableView.setPrefHeight(calculateTableHeight(items.size()));
        }
    }

    private void duplicateRowAction() {
        TableView<TableModel> currentTable = getCurrentTable();
        if (currentTable != null) {
            ObservableList<TableModel> items = currentTable.getItems();
            if (items != null && !items.isEmpty()) {
                ListIterator<TableModel> listIterator = items.listIterator();
                while (listIterator.hasNext()) {
                    TableModel model = listIterator.next();
                    if (model.isChecked()) {
                        listIterator.add(new TableModel(model));
                        model.setChecked(false);
                    } // end of if "model.isChecked()"
                } // end of "while"
            } // end of if "items != null && !items.isEmpty()"
        } // end of if "currentTable != null"
    }

    private void removeRowAction() {
        TableView<TableModel> currentTable = getCurrentTable();
        if (currentTable != null) {
            ObservableList<TableModel> items = currentTable.getItems();
            if (items != null && !items.isEmpty()) {
                ListIterator<TableModel> listIterator = items.listIterator();
                while (listIterator.hasNext()) {
                    TableModel model = listIterator.next();
                    if (model.isChecked()) {
                        listIterator.remove();
                    }
                }
            }
        }
    }

    private void updateChartConfiguration() {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            chartConfigurationDialog.setChartConfiguration(tabInfo.getChartConfiguration());
            Optional<ChartConfiguration> result = chartConfigurationDialog.showAndWait();
            result.ifPresent(tabInfo::setChartConfiguration);
        }

    }

    /**
     * New action.
     *
     * @see MorphologicalEnginePane#openAction(boolean)
     */
    private void newAction() {
        openAction(false);
    }

    /**
     * Open action.
     *
     * @see MorphologicalEnginePane#openAction(boolean)
     */
    private void openAction() {
        openAction(true);
    }

    /**
     * Performs either <strong>OPEN</strong> or <strong>NEW</strong> action. If the <code>showDialog</code> parameter
     * is true then file chooser dialog will get displayed and this method will behave like typical
     * <strong>OPEN</strong> action, if the given parameter is passed then this method will behave like typical
     * <strong>NEW</strong> action.
     * <div>
     * In case of "open" action in following case file not be opened:
     * <ul>
     * <li>If user canceled the file dialog</li>
     * <li>If errors occur reading file</li>
     * </ul>
     * </div>
     *
     * @param showDialog true for "open" action, false for "new" action
     */
    private void openAction(final boolean showDialog) {
        File file = null;
        if (showDialog) {
            file = FILE_CHOOSER.showOpenDialog(getScene().getWindow());
            if (file == null) {
                // use might have cancel the dialog
                return;
            }
        }
        FileOpenService service = new FileOpenService(file);
        service.setOnSucceeded(event -> {
            Tab tab = (Tab) event.getSource().getValue();
            if (tab != null) {
                tabPane.getTabs().add(tab);
                tabPane.getSelectionModel().select(tab);
            }
            changeToDefaultCursor();
        });
        service.setOnFailed(event -> showErrorServiceFailed(event, "Error occurred while opening document."));
        service.start();
    }

    private void saveAction(SaveMode saveMode) {
        final TabInfo tabInfo = getTabUserData();
        if (tabInfo != null) {
            if (showDialogIfApplicable(saveMode, tabInfo)) {
                changeToWaitCursor();
                runLater(saveData(saveMode, tabInfo));
            } // end of if "doSave"
        } // end of if "tabInfo != null"
    }

    private Runnable saveData(final SaveMode saveMode, final TabInfo tabInfo) {
        return () -> {
            TableView<TableModel> tableView = getCurrentTable();
            ObservableList<TableModel> items = tableView.getItems();
            final ObservableList<TableModel> currentItems = observableArrayList();
            if (SaveMode.SAVE_SELECTED.equals(saveMode)) {
                items.forEach(tableModel -> {
                    if (tableModel.isChecked()) {
                        currentItems.add(tableModel);
                    }
                });
            } else {
                currentItems.addAll(items);
            }
            try {
                File sarfxFile = tabInfo.getSarfxFile();
                ConjugationTemplate conjugationTemplate = getConjugationTemplate(currentItems,
                        tabInfo.getChartConfiguration());
                templateReader.saveFile(sarfxFile, conjugationTemplate);
                if(SaveMode.EXPORT_TO_WORD.equals(saveMode)) {
                    saveAsDocx(tabInfo, conjugationTemplate);
                }

                Tab currentTab = getCurrentTab();
                currentTab.setText(TemplateReader.getFileNameNoExtension(sarfxFile));
                if (SaveMode.SAVE_SELECTED.equals(saveMode)) {
                    tableView.getItems().clear();
                    tableView.getItems().addAll(currentItems);
                }
            } catch (ApplicationException e) {
                changeToDefaultCursor();
                e.printStackTrace();
                showError(e);
            }
            changeToDefaultCursor();
        };
    }

    private void saveAsDocx(final TabInfo tabInfo, final ConjugationTemplate conjugationTemplate) {
        Service<Void> service = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                return new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        changeToWaitCursor();
                        MorphologicalChartEngine engine = new MorphologicalChartEngine(conjugationTemplate);
                        engine.createDocument(tabInfo.getDocxFile().toPath());
                        return null;
                    }
                };
            }
        };
        service.setOnSucceeded(event -> {
            makeDirty(false);
            changeToDefaultCursor();
            Alert alert = new Alert(CONFIRMATION);
            final String message = format("Document \"%s\" has been published.%s Would like to open it?",
                    tabInfo.getDocxFile().getName(), System.lineSeparator());
            alert.setContentText(message);
            Optional<ButtonType> result = alert.showAndWait();
            result.ifPresent(buttonType -> {
                try {
                    Desktop.getDesktop().open(tabInfo.getDocxFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

        });
        service.setOnFailed(event -> {
            showError(event.getSource().getException());
            //showErrorServiceFailed(event, "Error occurred while publishing document.");
        });
        service.start();
    }

    private void showErrorServiceFailed(@SuppressWarnings({"unused"}) WorkerStateEvent event, String message) {
        changeToDefaultCursor();
        Alert alert = new Alert(ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(Throwable ex) {
        ex.printStackTrace();
        Alert alert = new Alert(ERROR);
        alert.setContentText(format("%s:%s", ex.getClass().getName(), ex.getMessage()));
        alert.showAndWait();
    }

    private ConjugationTemplate getConjugationTemplate(ObservableList<TableModel> items,
                                                       ChartConfiguration chartConfiguration) {
        ConjugationTemplate template = new ConjugationTemplate();
        items.forEach(tableModel -> template.getData().add(tableModel.getConjugationData()));
        template.setChartConfiguration(chartConfiguration);
        return template;
    }

    private boolean showDialogIfApplicable(SaveMode saveMode, TabInfo tabInfo) {
        File sarfxFile = tabInfo.getSarfxFile();
        boolean showDialog = sarfxFile == null || SaveMode.SAVE_AS.equals(saveMode) ||
                SaveMode.SAVE_SELECTED.equals(saveMode);
        if (showDialog) {
            fileSelectionDialog.setTabInfo(tabInfo);
            Optional<TabInfo> result = fileSelectionDialog.showAndWait();
            result.ifPresent(ti -> {
                tabInfo.setSarfxFile(ti.getSarfxFile());
                tabInfo.setDocxFile(ti.getDocxFile());
                tabInfo.setDirty(false);
            });
        }// end of if "showDialog"
        return tabInfo.getSarfxFile() != null;
    }

    @SuppressWarnings("unchecked")
    private void initializeTable(TableView<TableModel> tableView, double boundsWidth) {
        double largeColumnWidth = (boundsWidth * 20) / 100;
        double mediumColumnWidth = (boundsWidth * 8) / 100;
        double smallColumnWidth = (boundsWidth * 4) / 100;

        // start adding columns
        TableColumn<TableModel, Boolean> checkedColumn = createCheckedColumn(smallColumnWidth);
        TableColumn<TableModel, RootLetters> rootLettersColumn = createRootLettersColumn(largeColumnWidth);
        TableColumn<TableModel, NamedTemplate> templateColumn = createTemplateColumn(largeColumnWidth);
        TableColumn<TableModel, String> translationColumn = createTranslationColumn(mediumColumnWidth);
        TableColumn<TableModel, ObservableList<VerbalNoun>> verbalNounsColumn = createVerbalNounsColumn(largeColumnWidth);

        //TODO: figure out how to refresh Verbal Noun column with new values
        templateColumn.setOnEditCommit(event -> {
            makeDirty(true);
            NamedTemplate newValue = event.getNewValue();
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTemplate(newValue);

            // TODO: figure out how to update table
            List<VerbalNoun> verbalNouns = VerbalNoun.getByTemplate(newValue);

            // clear the currently selected verbal nouns first then add new values, if there is no verbal noun mapped
            // then our list should be empty
            selectedItem.getVerbalNouns().clear();
            if (verbalNouns != null) {
                selectedItem.getVerbalNouns().addAll(verbalNouns);
            }
        });

        TableColumn<TableModel, Boolean> removePassiveLineColumn = createRemovePassiveLineColumn(mediumColumnWidth);
        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = createSkipRuleProcessingColumn(mediumColumnWidth);
        final TableColumn<TableModel, Boolean> viewConjugationColumn = createViewConjugationColumn(mediumColumnWidth);

        tableView.getColumns().addAll(checkedColumn, rootLettersColumn, templateColumn, translationColumn,
                verbalNounsColumn, removePassiveLineColumn, skipRuleProcessingColumn, viewConjugationColumn);
    }

    private TableColumn<TableModel, Boolean> createCheckedColumn(double smallColumnWidth) {
        TableColumn<TableModel, Boolean> checkedColumn = new TableColumn<>();
        checkedColumn.setPrefWidth(smallColumnWidth);
        checkedColumn.setEditable(true);
        checkedColumn.setCellValueFactory(new PropertyValueFactory<>("checked"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                checkedColumn.getTableView().getItems().get(index).checkedProperty();
        checkedColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return checkedColumn;
    }

    private TableColumn<TableModel, RootLetters> createRootLettersColumn(double largeColumnWidth) {
        TableColumn<TableModel, RootLetters> rootLettersColumn = new TableColumn<>();
        rootLettersColumn.setText("Root Letters");
        rootLettersColumn.setPrefWidth(largeColumnWidth);
        rootLettersColumn.setEditable(true);
        rootLettersColumn.setCellValueFactory(new PropertyValueFactory<>("rootLetters"));
        rootLettersColumn.setCellFactory(RootLettersTableCell::new);
        rootLettersColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setRootLetters(event.getNewValue());
        });
        return rootLettersColumn;
    }

    private TableColumn<TableModel, NamedTemplate> createTemplateColumn(double largeColumnWidth) {
        TableColumn<TableModel, NamedTemplate> templateColumn = new TableColumn<>();
        templateColumn.setText("Family");
        templateColumn.setEditable(true);
        templateColumn.setPrefWidth(largeColumnWidth);
        templateColumn.setCellValueFactory(new PropertyValueFactory<>("template"));
        templateColumn.setCellFactory(column -> new ComboBoxTableCell<TableModel, NamedTemplate>(NamedTemplate.values()) {
            private final Text labelText;
            private final Text arabicText;
            private final ComboBox<NamedTemplate> comboBox;

            {
                setContentDisplay(GRAPHIC_ONLY);
                setNodeOrientation(RIGHT_TO_LEFT);
                setAlignment(Pos.CENTER);
                comboBox = createComboBox(NamedTemplate.values());
                arabicText = new Text();
                arabicText.setFont(preferences.getArabicFont());
                arabicText.setTextAlignment(CENTER);
                arabicText.setNodeOrientation(RIGHT_TO_LEFT);
                labelText = new Text();
                labelText.setTextAlignment(CENTER);
                labelText.setFont(preferences.getEnglishFont());

                comboBox.getSelectionModel().selectedItemProperty().addListener((o, ov, nv) -> commitEdit(nv));
            }

            @Override
            public void startEdit() {
                if (!isEditable() || !getTableView().isEditable() || !getTableColumn().isEditable()) {
                    return;
                }

                comboBox.getSelectionModel().select(getItem());

                super.startEdit();
                setText(null);
                setGraphic(comboBox);
            }

            @Override
            public void updateItem(NamedTemplate item, boolean empty) {
                super.updateItem(item, empty);

                TextFlow textFlow = new TextFlow();
                Node graphic = null;
                if (item != null && !empty) {
                    labelText.setText(format("(%s) ", item.getCode()));
                    arabicText.setText(item.toLabel().toUnicode());
                    textFlow.getChildren().addAll(arabicText, createSpaceLabel(), labelText);
                    graphic = new Group(textFlow);
                }
                setGraphic(graphic);
            }
        });
        return templateColumn;
    }

    private TableColumn<TableModel, String> createTranslationColumn(double mediumColumnWidth) {
        TableColumn<TableModel, String> translationColumn = new TableColumn<>();
        translationColumn.setText("Translation");
        translationColumn.setPrefWidth(mediumColumnWidth);
        translationColumn.setEditable(true);
        translationColumn.setCellValueFactory(new PropertyValueFactory<>("translation"));
        translationColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        translationColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.setTranslation(event.getNewValue());
        });
        return translationColumn;
    }

    private TableColumn<TableModel, ObservableList<VerbalNoun>> createVerbalNounsColumn(double largeColumnWidth) {
        TableColumn<TableModel, ObservableList<VerbalNoun>> verbalNounsColumn = new TableColumn<>();
        verbalNounsColumn.setText("Verbal Nouns");
        verbalNounsColumn.setPrefWidth(largeColumnWidth);
        verbalNounsColumn.setEditable(true);
        verbalNounsColumn.setCellValueFactory(new PropertyValueFactory<>("verbalNouns"));
        verbalNounsColumn.setCellFactory(VerbalNounTableCell::new);
        verbalNounsColumn.setOnEditCommit(event -> {
            makeDirty(true);
            TableView<TableModel> table = event.getTableView();
            TableModel selectedItem = table.getSelectionModel().getSelectedItem();
            selectedItem.getVerbalNouns().clear();
            selectedItem.getVerbalNouns().addAll(event.getNewValue());
        });
        return verbalNounsColumn;
    }

    private TableColumn<TableModel, Boolean> createRemovePassiveLineColumn(double mediumColumnWidth) {
        TableColumn<TableModel, Boolean> removePassiveLineColumn = new TableColumn<>();
        removePassiveLineColumn.setText("Remove\nPassive\nLine");
        removePassiveLineColumn.setPrefWidth(mediumColumnWidth);
        removePassiveLineColumn.setEditable(true);
        removePassiveLineColumn.setCellValueFactory(new PropertyValueFactory<>("removePassiveLine"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                removePassiveLineColumn.getTableView().getItems().get(index).removePassiveLineProperty();
        removePassiveLineColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        removePassiveLineColumn.setOnEditCommit(event -> makeDirty(true));
        return removePassiveLineColumn;
    }

    private TableColumn<TableModel, Boolean> createSkipRuleProcessingColumn(double mediumColumnWidth) {
        TableColumn<TableModel, Boolean> skipRuleProcessingColumn = new TableColumn<>();
        skipRuleProcessingColumn.setText("Skip\nRule\nProcessing");
        skipRuleProcessingColumn.setPrefWidth(mediumColumnWidth);
        skipRuleProcessingColumn.setEditable(true);
        skipRuleProcessingColumn.setCellValueFactory(new PropertyValueFactory<>("skipRuleProcessing"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index ->
                skipRuleProcessingColumn.getTableView().getItems().get(index).skipRuleProcessingProperty();
        skipRuleProcessingColumn.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return skipRuleProcessingColumn;
    }

    private TableColumn<TableModel, Boolean> createViewConjugationColumn(double width) {
        final TableColumn<TableModel, Boolean> column = new TableColumn<>();
        column.setText("View\nConjugation");
        column.setPrefWidth(width);
        column.setEditable(true);
        column.setCellValueFactory(new PropertyValueFactory<>("viewConjugation"));
        final Callback<Integer, ObservableValue<Boolean>> cb = index -> {
            final TableModel tableModel = column.getTableView().getItems().get(index);
            final BooleanProperty viewConjugationProperty = tableModel.viewConjugationProperty();
            if (viewConjugationProperty.get()) {
                final RootLetters rootLetters = tableModel.getRootLetters();
                final NamedTemplate template = tableModel.getTemplate();
                if (rootLetters == null || template == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Both RootLetters and Form has to populated.");
                    alert.show();
                    viewConjugationProperty.setValue(false);
                } else {
                    runLater(() -> updateViewer(tableModel));
                    viewConjugationProperty.setValue(false);
                }
            }
            return viewConjugationProperty;
        };
        column.setCellFactory(param -> new CheckBoxTableCell<>(cb));
        return column;
    }

    private void updateViewer(TableModel tableModel) {
        ConjugationTemplate conjugationTemplate = new ConjugationTemplate();
        conjugationTemplate.getData().add(tableModel.getConjugationData());

        MorphologicalChartEngine engine = new MorphologicalChartEngine(conjugationTemplate);
        final MorphologicalChart morphologicalChart = engine.createMorphologicalCharts().get(0);
        morphologicalChartViewer.setMorphologicalChart(null);
        morphologicalChartViewer.setMorphologicalChart(morphologicalChart);
        chartStage.show();
    }

    private void initViewerStage() {
        chartStage.setTitle("Morphological Chart Viewer");
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        chartStage.setX(bounds.getMinX());
        chartStage.setY(bounds.getMinY());
        chartStage.setWidth(bounds.getWidth());
        chartStage.setHeight(bounds.getHeight());

        chartStage.setWidth(bounds.getWidth() / 4);
        chartStage.setHeight(bounds.getHeight() / 4);

        Scene scene = new Scene(morphologicalChartViewer);
        chartStage.setMaximized(true);
        chartStage.setScene(scene);
    }

    void setDialogOwner(Stage primaryStage) {
        Window owner = fileSelectionDialog.getOwner();
        if (owner == null) {
            fileSelectionDialog.initOwner(primaryStage);
        }
        owner = chartConfigurationDialog.getOwner();
        if (owner == null) {
            chartConfigurationDialog.initOwner(primaryStage);
        }
    }

    private void changeCursor(Cursor cursor) {
        Scene scene = getScene();
        if (scene != null) {
            scene.setCursor(cursor);
        }
    }

    private void changeToDefaultCursor() {
        changeCursor(Cursor.DEFAULT);
    }

    private void changeToWaitCursor() {
        changeCursor(Cursor.WAIT);
    }

    private enum SaveMode {
        SAVE, SAVE_AS, SAVE_SELECTED, EXPORT_TO_WORD
    }

    private class FileOpenService extends Service<Tab> {

        private final File file;

        private FileOpenService(File file) {
            this.file = file;
        }

        @Override
        protected Task<Tab> createTask() {
            return new Task<Tab>() {
                @Override
                protected Tab call() throws Exception {
                    changeToWaitCursor();
                    ConjugationTemplate template = file == null ? null : templateReader.readFile(file);
                    return createTab(file, template);
                } // end of method "call"
            }; // end of anonymous class "Task"
        } // end of method "createTask"
    } // end of class "FileOpenService"

}
