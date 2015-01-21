/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.controllers;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import mynews.ui.JFrameMainUI;
import mynews.ui.JPanelArchives;
import mynews.ui.JPanelArticles;
import mynews.utils.Commons;

/**
 *
 * @author RESEARCH2
 */
public class ControllerArchives {

    private final JFrameMainUI mainUI;
    private final JPanelArchives view;
    private final ArrayList<File> articles;
    private final ArrayList<File> images;

    public ControllerArchives(JFrameMainUI mainUI, JPanelArchives view) {
        this.mainUI = mainUI;
        this.view = view;
        this.articles = new ArrayList<>();
        this.images = new ArrayList<>();
        loadArchivedArticles();
        loadArticlesImages();
        displayArticles();
    }

    private void loadArchivedArticles() {
        File folder = new File("articles/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                System.out.println(file.getName());
                articles.add(file);

            }
        }
    }

    private void loadArticlesImages() {
        File folder = new File("images/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                images.add(file);
            }
        }

    }

    private void displayArticles() {

        for (int i = 0; i < articles.size(); i++) {
            try {
                final JPanelArticles jpa = new JPanelArticles();
                Image image = ImageIO.read(images.get(i).toURI().toURL()).getScaledInstance(181, 151, Image.SCALE_SMOOTH);
                jpa.getjTextPaneArticle().setPage(articles.get(i).toURI().toURL());
                jpa.getjLabelImage().setIcon(new ImageIcon(image));
                jpa.getjLabelDate().setText("  " + Commons.timeFromTimestamp(articles.get(i).getName().substring(0, articles.get(i).getName().lastIndexOf('.'))));
                view.addGB(view.getjPanelHome(), jpa, 0, i);
                final Image imageB = ImageIO.read(images.get(i).toURI().toURL()).getScaledInstance(mainUI.getjPanelTop().getWidth(), mainUI.getjPanelTop().getHeight(), Image.SCALE_SMOOTH);
                jpa.getjButtonRead().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String article = jpa.getjTextPaneArticle().getText();
                        String newArticle = article.replaceAll("<p style=\"margin-top: 0\">", "<p style=\"margin-top: 0;font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">");
                        mainUI.getjTextPaneArticleContents().setText(newArticle);
                        mainUI.setImage(imageB);
                        mainUI.getjPanelTop().repaint();
                        mainUI.getGp().dispose();
                    }
                });

            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }


}
