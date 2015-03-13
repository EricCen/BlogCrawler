package com.ericcen.BlogCrawler;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;

import java.io.*;
import java.net.URL;

/**
 * Created by eric on 2015/3/13.
 */
public class HtmlToPdf {
    private String htmlUrl;
    private static String DICTIONARY = "HtmlToPDF";
    private CleanerProperties props;
    private PrettyXmlSerializer xmlSerializer;
    private HtmlCleaner htmlCleaner;

    public HtmlToPdf(String htmlUrl) {
        this.htmlUrl = htmlUrl;
        initCleanerProperties();
        initDirectory();
        initXmlSerializer();
        initHtmlCleaner();
    }

    private void initCleanerProperties() {
        props = new CleanerProperties();
        // set some properties to non-default values
        props.setTranslateSpecialEntities(true);
        props.setTransResCharsToNCR(true);
        props.setOmitComments(true);
    }

    private void initXmlSerializer() {
        xmlSerializer = new PrettyXmlSerializer(props);
    }

    private void initDirectory() {
        new File(DICTIONARY).mkdir();
    }

    private void initHtmlCleaner() {
        htmlCleaner = new HtmlCleaner(props);
    }

    public void saveHtmlToPdf() throws IOException, DocumentException {
        TagNode tagNode = htmlCleaner.clean(
                new URL(htmlUrl));
        String title = htmlUrl.substring(htmlUrl.lastIndexOf("/",htmlUrl.length() - 1));
        String htmlFileName = buildUpHtmlFileName(title);
        xmlSerializer.writeToFile(tagNode, htmlFileName, "utf-8");
        saveAsPdf(title,htmlFileName);
    }

    private void saveAsPdf(String title, String htmlFileName) throws IOException, DocumentException {
        String pdfFileName = buildUpPdfFileName(title);
        OutputStream os = new FileOutputStream(pdfFileName);
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        document.open();
        InputStream is = new FileInputStream(htmlFileName);
        try {
            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is);
        } catch (Exception e) {
            System.out.println("Skip PDF created for :" + htmlUrl);
            return;
        } finally {
            document.close();
            is.close();
            os.close();
        }
        System.out.println("PDF created for :" + htmlUrl);

    }

    private String buildUpHtmlFileName(String title) {
        return DICTIONARY + "/" + title + ".html";
    }

    private String buildUpPdfFileName(String title) {
        return DICTIONARY + "/" + title + ".pdf";
    }


    public static void main(String[] args) throws IOException, DocumentException {
       /* HtmlToPdf htmlToPdf = new HtmlToPdf();
        htmlToPdf.saveHtmlToPdf();*/
    }
}
