package com.ericcen.BlogCrawler;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eric on 2015/3/6.
 */
public class RssParser {
    private static String ITEM_TAG_NAME = "item";
    private static String LINK_TAG_NAME = "link";
    private static String TITLE_TAG_NAME = "title";
    private String rssUrl;

    public RssParser(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<Item> getAllArticleLinks() throws IOException, ParserConfigurationException, SAXException {
        List<Item> allArticle = new ArrayList<Item>();
        URL  url = new URL(rssUrl);

        InputStream is = url.openStream();

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document doc = builder.parse(is);

        Element root = doc.getDocumentElement();
        NodeList nodeList = root.getElementsByTagName(ITEM_TAG_NAME);

        for(int i = 0; i <nodeList.getLength(); i++){
          Element item = (Element)nodeList.item(i);
          NodeList child = item.getChildNodes();
          String link = null;
          String title = null;
          for(int j = 0; j < child.getLength(); j++){
              Node node = child.item(j);
              if(node.getNodeName().equals(LINK_TAG_NAME)){
                  link =node.getFirstChild().getNodeValue();
              }
              if(node.getNodeName().equals(TITLE_TAG_NAME)){
                 title = node.getFirstChild().getNodeValue();
              }
          }
            Item rssItem = new Item(link,title);
            allArticle.add(rssItem);

        }
        return allArticle;
    }
}
