package com.alphasystem.app.sarfengine.ui.control;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEnginePreferences;
import com.alphasystem.app.sarfengine.ui.control.skin.ChartConfigurationSkin;
import com.alphasystem.morphologicalanalysis.morphology.model.ChartConfiguration;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirection;
import com.alphasystem.morphologicalanalysis.morphology.model.support.SortDirective;
import com.alphasystem.util.GenericPreferences;
import javafx.beans.property.*;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

import static com.alphasystem.util.AppUtil.getResource;

/**
 * @author sali
 */
public class ChartConfigurationView extends Control {

    private final MorphologicalEnginePreferences preferences = (MorphologicalEnginePreferences) GenericPreferences.getInstance();
    private final ObjectProperty<ChartConfiguration> chartConfiguration = new SimpleObjectProperty<>();
    private final BooleanProperty omitToc = new SimpleBooleanProperty();
    private final BooleanProperty omitAbbreviatedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitDetailedConjugation = new SimpleBooleanProperty();
    private final BooleanProperty omitTitle = new SimpleBooleanProperty();
    private final BooleanProperty omitHeader = new SimpleBooleanProperty();
    private final BooleanProperty omitSarfTermCaption = new SimpleBooleanProperty();
    private final ObjectProperty<SortDirective> sortDirective = new SimpleObjectProperty<>();
    private final ObjectProperty<SortDirection> sortDirection = new SimpleObjectProperty<>();
    private final StringProperty arabicFontFamily = new SimpleStringProperty(this, "arabicFontFamily");
    private final StringProperty translationFontFamily = new SimpleStringProperty(this, "translationFontFamily");
    private final LongProperty arabicFontSize = new SimpleLongProperty(this, "arabicFontSize");
    private final LongProperty arabicUiFontSize = new SimpleLongProperty(this, "arabicUiFontSize");
    private final LongProperty translationFontSize = new SimpleLongProperty(this, "translationFontSize");
    private final LongProperty translationUiFontSize = new SimpleLongProperty(this, "translationUiFontSize");

    public ChartConfigurationView() {
        chartConfigurationProperty().addListener((o, ov, nv) -> {
            setOmitAbbreviatedConjugation(nv.isOmitAbbreviatedConjugation());
            setOmitDetailedConjugation(nv.isOmitDetailedConjugation());
            setOmitHeader(nv.isOmitHeader());
            setOmitSarfTermCaption(nv.isOmitSarfTermCaption());
            setOmitTitle(nv.isOmitTitle());
            setOmitToc(nv.isOmitToc());
            setSortDirection(nv.getSortDirection());
            setSortDirective(nv.getSortDirective());
            setArabicFontFamily(nv.getArabicFontFamily());
            setTranslationFontFamily(nv.getTranslationFontFamily());
            setArabicFontSize(nv.getArabicFontSize());
            setArabicUiFontSize(preferences.getArabicFontSize());
            setTranslationFontSize(nv.getTranslationFontSize());
            setTranslationUiFontSize(preferences.getEnglishFontSize());
        });

        omitAbbreviatedConjugationProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitAbbreviatedConjugation(nv));
        omitDetailedConjugationProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitDetailedConjugation(nv));
        omitHeaderProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitHeader(nv));
        omitSarfTermCaptionProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitSarfTermCaption(nv));
        omitTitleProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitTitle(nv));
        omitTocProperty().addListener((o, ov, nv) -> getChartConfiguration().setOmitToc(nv));
        sortDirectionProperty().addListener((o, ov, nv) -> getChartConfiguration().setSortDirection(nv));
        sortDirectiveProperty().addListener((o, ov, nv) -> getChartConfiguration().setSortDirective(nv));
        arabicFontFamilyProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setArabicFontFamily(nv);
            preferences.setArabicFontName(nv);
        });
        translationFontFamilyProperty().addListener((o, ov, nv) -> {
            getChartConfiguration().setTranslationFontFamily(nv);
            preferences.setEnglishFontName(nv);
        });
        arabicFontSizeProperty().addListener((o, ov, nv) -> getChartConfiguration().setArabicFontSize((Long) nv));
        translationFontSizeProperty().addListener((o, ov, nv) -> getChartConfiguration().setTranslationFontSize((Long) nv));
        arabicUiFontSizeProperty().addListener((o, ov, nv) -> preferences.setArabicFontSize((Long) nv));
        translationUiFontSizeProperty().addListener((o, ov, nv) -> preferences.setEnglishFontSize((Long) nv));

        setChartConfiguration(null);
        setMinWidth(600);
    }

    @Override
    public String getUserAgentStylesheet() {
        return getResource("arabic-ui-support.css").toExternalForm();
    }


    @Override
    protected Skin<?> createDefaultSkin() {
        return new ChartConfigurationSkin(this);
    }

    public final ChartConfiguration getChartConfiguration() {
        return chartConfiguration.get();
    }

    public final void setChartConfiguration(ChartConfiguration chartConfiguration) {
        this.chartConfiguration.set(chartConfiguration == null ? new ChartConfiguration() : chartConfiguration);
    }

    public final ObjectProperty<ChartConfiguration> chartConfigurationProperty() {
        return chartConfiguration;
    }

    public final BooleanProperty omitTocProperty() {
        return omitToc;
    }

    public final void setOmitToc(boolean omitToc) {
        this.omitToc.set(omitToc);
    }

    public final BooleanProperty omitAbbreviatedConjugationProperty() {
        return omitAbbreviatedConjugation;
    }

    public final void setOmitAbbreviatedConjugation(boolean omitAbbreviatedConjugation) {
        this.omitAbbreviatedConjugation.set(omitAbbreviatedConjugation);
    }

    public final BooleanProperty omitDetailedConjugationProperty() {
        return omitDetailedConjugation;
    }

    public final void setOmitDetailedConjugation(boolean omitDetailedConjugation) {
        this.omitDetailedConjugation.set(omitDetailedConjugation);
    }

    public final BooleanProperty omitTitleProperty() {
        return omitTitle;
    }

    public final void setOmitTitle(boolean omitTitle) {
        this.omitTitle.set(omitTitle);
    }

    public final BooleanProperty omitHeaderProperty() {
        return omitHeader;
    }

    public final void setOmitHeader(boolean omitHeader) {
        this.omitHeader.set(omitHeader);
    }

    public final BooleanProperty omitSarfTermCaptionProperty() {
        return omitSarfTermCaption;
    }

    public final void setOmitSarfTermCaption(boolean omitSarfTermCaption) {
        this.omitSarfTermCaption.set(omitSarfTermCaption);
    }

    public final ObjectProperty<SortDirective> sortDirectiveProperty() {
        return sortDirective;
    }

    public final void setSortDirective(SortDirective sortDirective) {
        this.sortDirective.set(sortDirective);
    }

    public final ObjectProperty<SortDirection> sortDirectionProperty() {
        return sortDirection;
    }

    public final void setSortDirection(SortDirection sortDirection) {
        this.sortDirection.set(sortDirection);
    }

    public final String getArabicFontFamily() {
        return arabicFontFamily.get();
    }

    public final StringProperty arabicFontFamilyProperty() {
        return arabicFontFamily;
    }

    public final void setArabicFontFamily(String arabicFontFamily) {
        this.arabicFontFamily.set(arabicFontFamily);
    }

    public final String getTranslationFontFamily() {
        return translationFontFamily.get();
    }

    public final StringProperty translationFontFamilyProperty() {
        return translationFontFamily;
    }

    public final void setTranslationFontFamily(String translationFontFamily) {
        this.translationFontFamily.set(translationFontFamily);
    }

    public final long getArabicFontSize() {
        return arabicFontSize.get();
    }

    public final LongProperty arabicFontSizeProperty() {
        return arabicFontSize;
    }

    public final void setArabicFontSize(long arabicFontSize) {
        this.arabicFontSize.set(arabicFontSize);
    }

    public final long getArabicUiFontSize() {
        return arabicUiFontSize.get();
    }

    public final LongProperty arabicUiFontSizeProperty() {
        return arabicUiFontSize;
    }

    public final void setArabicUiFontSize(long arabicUiFontSize) {
        this.arabicUiFontSize.set(arabicUiFontSize);
    }

    public final long getTranslationFontSize() {
        return translationFontSize.get();
    }

    public final LongProperty translationFontSizeProperty() {
        return translationFontSize;
    }

    public final void setTranslationFontSize(long translationFontSize) {
        this.translationFontSize.set(translationFontSize);
    }

    public final long getTranslationUiFontSize() {
        return translationUiFontSize.get();
    }

    public final LongProperty translationUiFontSizeProperty() {
        return translationUiFontSize;
    }

    public final void setTranslationUiFontSize(long translationUiFontSize) {
        this.translationUiFontSize.set(translationUiFontSize);
    }
}
