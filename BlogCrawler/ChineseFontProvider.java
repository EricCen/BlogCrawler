package com.ericcen.BlogCrawler;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontProvider;
import com.itextpdf.text.pdf.BaseFont;

/**
 * Created by eric on 2015/3/7.
 */
public class ChineseFontProvider implements FontProvider {
    @Override
    public boolean isRegistered(String s) {
        return false;
    }

    @Override
    public Font getFont(String s, String s2, boolean b, float v, int i, BaseColor baseColor) {
        BaseFont bfChinese =null;
        try {
            bfChinese=BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Font FontChinese = new Font(bfChinese, 12, Font.NORMAL);
        return FontChinese;
    }
}
