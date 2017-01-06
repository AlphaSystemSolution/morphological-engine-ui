package com.alphasystem.app.sarfengine.ui.control.skin;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.morphologicalengine.conjugation.model.RootLetters;
import com.alphasystem.app.morphologicalengine.ui.MorphologicalChartControl;
import com.alphasystem.app.sarfengine.ui.control.MorphologicalChartViewerControl;
import com.alphasystem.arabic.model.ArabicLetterType;
import com.alphasystem.fx.ui.Browser;
import com.alphasystem.fx.ui.util.UiUtilities;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import static java.lang.String.format;

/**
 * @author sali
 */
public class MorphologicalChartViewerSkin extends SkinBase<MorphologicalChartViewerControl> {

    private static final String MAWRID_READER_URL = "http://ejtaal.net/aa/index.html#bwq=";

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
        private Tab morphologicalChartTab;
        private Tab dictionaryTab;

        private SkinView(MorphologicalChartViewerControl control) {
            this.control = control;
            this.morphologicalChartControl = new MorphologicalChartControl();
            this.browser = new Browser();
            this.control.morphologicalChartProperty().addListener((observable, oldValue, newValue) -> initialize(newValue));
            setup();
        }

        private void setup() {
            TabPane tabPane = new TabPane();
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
            boolean disable = morphologicalChart == null;
            if (disable) {
                morphologicalChartTab.setDisable(true);
                dictionaryTab.setDisable(true);
                return;
            }
            morphologicalChartControl.setMorphologicalChart(null);
            morphologicalChartControl.setMorphologicalChart(morphologicalChart);
            morphologicalChartTab.setDisable(false);

            final RootLetters rootLetters = morphologicalChart.getRootLetters();
            final ArabicLetterType fourthRadical = rootLetters.getFourthRadical();
            String fr = format("%s", (fourthRadical == null) ? "" : fourthRadical.toCode());
            String searchString = format("%s%s%s%s", rootLetters.getFirstRadical().toCode(),
                    rootLetters.getSecondRadical().toCode(), rootLetters.getThirdRadical().toCode(), fr);
            String url = format("%s%s", MAWRID_READER_URL, searchString);
            browser.loadUrl(url);
            dictionaryTab.setDisable(false);
        }

    }
}
