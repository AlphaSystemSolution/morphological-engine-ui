package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.morphologicalengine.conjugation.model.MorphologicalChart;
import com.alphasystem.app.sarfengine.ui.control.skin.MorphologicalChartViewerSkin;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

/**
 * @author sali
 */
public class MorphologicalChartViewerControl extends Control {

    private final ObjectProperty<MorphologicalChart> morphologicalChart = new SimpleObjectProperty<>(this, "morphologicalChart");

    public MorphologicalChartViewerControl(){
        setSkin(createDefaultSkin());
    }

    public MorphologicalChart getMorphologicalChart() {
        return morphologicalChart.get();
    }

    public ObjectProperty<MorphologicalChart> morphologicalChartProperty() {
        return morphologicalChart;
    }

    public void setMorphologicalChart(MorphologicalChart morphologicalChart) {
        this.morphologicalChart.set(morphologicalChart);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new MorphologicalChartViewerSkin(this);
    }
}
