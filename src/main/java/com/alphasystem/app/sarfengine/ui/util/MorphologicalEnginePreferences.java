package com.alphasystem.app.sarfengine.ui.util;

import com.alphasystem.app.morphologicalengine.ui.util.MorphologicalEngineUIPreferences;

/**
 * @author sali
 */
public class MorphologicalEnginePreferences extends MorphologicalEngineUIPreferences {

    private static final String ARABIC_WORD_HEADING_FONT_SIZE = "arabicWordHeadingFontSize";
    private static final String ARABIC_WORD_FONT_SIZE = "arabicWordFontSize";
    private static final String ENGLISH_WORD_FONT_SIZE = "englishWordFontSize";

    public MorphologicalEnginePreferences() {
        super(MorphologicalEnginePreferences.class);
    }

    public long getArabicWordFontSize(){
        return getFontNode().getLong(ARABIC_WORD_FONT_SIZE, 16);
    }

    public void setArabicWordFontSize(long size){
        getFontNode().putLong(ARABIC_WORD_FONT_SIZE, size);
    }

    public long getArabicWordHeadingFontSize(){
        return getFontNode().getLong(ARABIC_WORD_HEADING_FONT_SIZE, 60);
    }

    public void setArabicWordHeadingFontSize(long size){
        getFontNode().putLong(ARABIC_WORD_HEADING_FONT_SIZE, size);
    }

    public long getEnglishWordFontSize(){
        return getFontNode().getLong(ENGLISH_WORD_FONT_SIZE, 24);
    }

    public void setEnglishWordFontSize(long size){
        getFontNode().putLong(ENGLISH_WORD_FONT_SIZE, size);
    }

}
