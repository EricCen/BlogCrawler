package com.ericcen.BlogCrawler;

import com.itextpdf.text.DocumentException;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by eric on 2015/3/13.
 */
public class HtmlToPdfGui {
    public static void main(String[] args){
        JFrame frame = new JFrame("Html To PDF");
        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridLayout(1,1));
        JPanel inputPanel = new JPanel();
        final JTextField input = new JTextField(30);
        Document document = new PlainDocument();
        input.setDocument(document);
        inputPanel.add(input);
        contentPane.add(inputPanel);
        JButton button = new JButton("Save as PDF");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String htmlUrl = input.getText();
                HtmlToPdf htmlToPdf = new HtmlToPdf(htmlUrl);
                try {
                    htmlToPdf.saveHtmlToPdf();
                } catch (IOException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.add(button);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

}
