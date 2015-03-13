package com.ericcen.BlogCrawler;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.PrettyXmlSerializer;
import org.htmlcleaner.TagNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by eric on 2015/3/6.
 */
public class BlogCrawler {
    private static String BLOG_RSS_URL = "http://www.aaronsw.com/2002/feeds/pgessays.rss";
    private static String DICTIONARY = "PaulGrahamEssays";
    private CleanerProperties props;
    private RssParser rssParser;
    private PrettyXmlSerializer xmlSerializer;
    private HtmlCleaner htmlCleaner;

    public BlogCrawler() {
        initCleanerProperties();
        initDirectory();
        initXmlSerializer();
        initHtmlCleaner();
        initRssParser();
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

    private void initRssParser() {
        rssParser = new RssParser(BLOG_RSS_URL);
    }

    private void crawlAndSaveToPDF() throws ParserConfigurationException, SAXException, IOException, DocumentException, InterruptedException {
        List<Item> allArticleItems = rssParser.getAllArticleLinks();
        for (Item item : allArticleItems) {
            System.out.println("Start to crawl " + item.getTitle() + " from " + item.getLink());
            String link = item.getLink();
            String title = item.getTitle().replaceAll("[?]", "").replaceAll("\"", "");
            TagNode tagNode = null;
            try {
                tagNode = htmlCleaner.clean(
                        new URL(link)
                );
            } catch (UnknownHostException ex) {
                Thread.sleep(200000);
                tagNode = htmlCleaner.clean(
                        new URL(link)
                );
            }

            String htmlFileName = buildUpHtmlFileName(title);
            xmlSerializer.writeToFile(tagNode, htmlFileName, "utf-8");

            saveAsPdf(item, title, htmlFileName);
        }
    }

    private void saveAsPdf(Item item, String title, String htmlFileName) throws IOException, DocumentException {
        String pdfFileName = buildUpPdfFileName(title);
        OutputStream os = new FileOutputStream(pdfFileName);
        Document document = new Document();
        PdfWriter pdfWriter = PdfWriter.getInstance(document, os);
        document.open();
        document.add(new Paragraph(item.getTitle()));
        InputStream is = new FileInputStream(htmlFileName);
        try {
            XMLWorkerHelper.getInstance().parseXHtml(pdfWriter, document, is);
        } catch (Exception e) {
            System.out.println("Skip PDF created for :" + item.getTitle());
            return;
        } finally {
            document.close();
            is.close();
            os.close();
        }
        System.out.println("PDF created for :" + item.getTitle());

    }

    private String buildUpHtmlFileName(String title) {
        return DICTIONARY + "/" + title + ".html";
    }

    private String buildUpPdfFileName(String title) {
        return DICTIONARY + "/" + title + ".pdf";
    }


    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, DocumentException, InterruptedException {
        BlogCrawler blogCrawler = new BlogCrawler();
        blogCrawler.crawlAndSaveToPDF();
    }

}
