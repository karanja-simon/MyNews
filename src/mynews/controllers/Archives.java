/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.controllers;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.SwingWorker;
import mynews.ui.JFrameMainUI;
import mynews.ui.JPanelArchives;
import mynews.ui.JPanelArticles;
import mynews.utils.Commons;

/**
 *
 * @author RESEARCH2
 */
public class Archives {

    private final JFrameMainUI mainUI;
    private final JPanelArchives view;
    private final ArrayList<File> articles;
    private final ArrayList<File> images;
    private final ArrayList<File> titles;
    private final ArrayList<String> articleTitles;

    public Archives(JFrameMainUI mainUI, JPanelArchives view) {
        this.mainUI = mainUI;
        this.view = view;
        this.articles = new ArrayList<>();
        this.images = new ArrayList<>();
        this.titles = new ArrayList<>();
        this.articleTitles = new ArrayList<>();

    }

    /**
     * Starts the SwingWorker
     */
    public void startBackgroundWorker() {
        new BackgroundWorker().execute();
    }

    /**
     * <p>
     * Loads Article form the Online feed through a background swing worker
     * thread, When the loading is complete set the loading status to false to
     * notify the view that the fetching is complete</p>
     * <p>
     * Depending on the request, through the overloaded constructor, the swing
     * worker can either retrieve the news article for display, or download the
     * news article locally</p>
     */
    private class BackgroundWorker extends SwingWorker<Boolean, String> {

        //JDialogLoader loader = new JDialogLoader(mainUI, true);

        public BackgroundWorker() {
        }

        @Override
        @SuppressWarnings("SleepWhileInLoop")
        protected Boolean doInBackground() throws Exception {

            loadArticles();
            loadArticlesImages();
            loadArticlesTitles();
            publish("Loading news...");
            return true;
        }

        @Override
        protected void done() {
            displayArticlesOnUI();
            view.setLoadingStatus(articles.size() + " Articles Retrieved");
        }

        @Override
        protected void process(List<String> chunks) {
            for (String i : chunks) {
                System.out.println();
            }
        }

    }

    /**
     * Load articles from articles directory and add them to articles list The
     * articles have timestamp as their file names with a matching timestamp on
     * the images folder to match the article with its associated image
     */
    private void loadArticles() {
        File folder = new File("articles/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                articles.add(file);

            }
        }
    }

    /**
     * Load articles from articles directory and add them to articles list The
     * articles have timestamp as their file names with a matching timestamp on
     * the images folder to match the article with its associated image
     */
    private void loadArticlesTitles() {
        File folder = new File("titles/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                titles.add(file);

            }
        }
        extractArticleTitles();
    }

    /**
     * Extracts article titles from article files
     */
    private void extractArticleTitles() {
        try {
            for (File title : titles) {
                BufferedReader br = new BufferedReader(new FileReader("titles/" + title.getName()));
                String line;
                while ((line = br.readLine()) != null) {
                    line += br.readLine();
                    articleTitles.add(line.replaceAll("null", ""));
                }
                br.close();
            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    /**
     * Load images from images directory and add them to articles list The
     * articles have timestamp as their file names with a matching timestamp on
     * the images folder to match the article with its associated image
     */
    private void loadArticlesImages() {
        File folder = new File("images/");
        File[] listOfFiles = folder.listFiles();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                images.add(file);
            }
        }

    }

    /**
     * Shows the loaded articles with their corresponding images on the UI for
     * the views to select their desired article for reading
     */
    private void displayArticlesOnUI() {

        for (int i = 0; i < articles.size(); i++) {
            final int index = i;
            try {
                final JPanelArticles jpa = new JPanelArticles();
                Image image = ImageIO.read(images.get(i).toURI().toURL()).getScaledInstance(181, 151, Image.SCALE_SMOOTH);
                jpa.getjTextPaneArticle().setPage(articles.get(i).toURI().toURL());
                jpa.getjLabelImage().setIcon(new ImageIcon(image));
                jpa.getjLabelDate().setText("  " + Commons.timeFromTimestamp(articles.get(i).getName().substring(0, articles.get(i).getName().lastIndexOf('.'))));
                view.addGB(view.getjPanelHome(), jpa, 0, i);
                jpa.setArticleTitle(articleTitles.get(index));
                final Image imageB = ImageIO.read(images.get(i).toURI().toURL()).getScaledInstance(mainUI.getjPanelTop().getWidth(), mainUI.getjPanelTop().getHeight(), Image.SCALE_SMOOTH);
                jpa.getjButtonRead().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        String article = jpa.getjTextPaneArticle().getText();
                        String newArticle = article.replaceAll("<p style=\"margin-top: 0\">", "<p style=\"margin-top: 0;font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">");
                        mainUI.getjTextPaneArticleContents().setText(newArticle);
                        mainUI.setImage(imageB);
                        mainUI.setArticleTitle(articleTitles.get(index));
                        mainUI.getjPanelTop().repaint();
                        mainUI.getGp().dispose();
                    }
                });

            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

}
