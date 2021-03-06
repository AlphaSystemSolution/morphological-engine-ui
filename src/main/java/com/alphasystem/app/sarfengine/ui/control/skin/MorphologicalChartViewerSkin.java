package com.alphasystem.app.sarfengine.ui.control.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartControl;
import com.alphasystem.app.sarfengine.ui.control.MorphologicalChartViewerControl;
import com.alphasystem.fx.ui.Browser;
import com.alphasystem.fx.ui.util.UiUtilities;
import javafx.concurrent.Worker;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebEngine;

import static java.lang.String.format;

/**
 * @author sali
 */
public class MorphologicalChartViewerSkin extends SkinBase<MorphologicalChartViewerControl> {

    private static final String MAWRID_READER_URL_PREFIX = System.getProperty("mawrid-reader.url", "http://ejtaal.net/");
    private static final String MAWRID_READER_URL = MAWRID_READER_URL_PREFIX + "aa/index.html#bwq=";

    /**
     * Constructor for all SkinBase instances.
     *
     * @param control The control for which this Skin should attach to.
     */
    public MorphologicalChartViewerSkin(MorphologicalChartViewerControl control) {
        super(control);
        getChildren().setAll(new SkinView(control));
    }

    private class SkinView extends BorderPane {

        private final MorphologicalChartViewerControl control;
        private final MorphologicalChartControl morphologicalChartControl;
        private final Browser browser;
        private final TabPane tabPane;
        private Tab morphologicalChartTab;
        private Tab dictionaryTab;

        private SkinView(MorphologicalChartViewerControl control) {
            this.control = control;
            this.morphologicalChartControl = new MorphologicalChartControl();
            this.browser = new Browser();
            this.tabPane = new TabPane();
            this.control.morphologicalChartProperty().addListener((observable, oldValue, newValue) -> initialize(newValue));
            setup();
        }

        private void setup() {
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

            BorderPane borderPane = new BorderPane();
            borderPane.setCenter(UiUtilities.wrapInScrollPane(morphologicalChartControl));
            morphologicalChartTab = new Tab("Morphological Chart", borderPane);
            morphologicalChartTab.setDisable(true);
            tabPane.getTabs().add(morphologicalChartTab);

            dictionaryTab = new Tab("Dictionary", browser);
            dictionaryTab.setDisable(true);
            tabPane.getTabs().add(dictionaryTab);

            setCenter(tabPane);
        }

        private void initialize(MorphologicalChart morphologicalChart) {
            tabPane.getSelectionModel().select(0);
            boolean disable = morphologicalChart == null;
            if (disable) {
                morphologicalChartTab.setDisable(true);
                dictionaryTab.setDisable(true);
                return;
            }
            morphologicalChartControl.setMorphologicalChart(null);
            morphologicalChartControl.setMorphologicalChart(morphologicalChart);
            morphologicalChartTab.setDisable(false);

            String searchString = morphologicalChart.getRootLetters().toMawridSearchString();

            String url = format("%s%s", MAWRID_READER_URL, searchString);
            final WebEngine webEngine = browser.getWebEngine();
            webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
                if (Worker.State.FAILED.equals(newValue)) {
                    dictionaryTab.setDisable(true);
                }
                // System.out.println(String.format("The state is %s", newValue));
            });
            dictionaryTab.setDisable(false);
            browser.loadUrl(url);
        }

    }
}
