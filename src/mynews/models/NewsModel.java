/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.models;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.imageio.ImageIO;
import mynews.controllers.Reader;

/**
 *
 * @author RESEARCH2
 */
public class NewsModel {

    private String feedDescription;
    private String feedLink;
    private String feedTitle;
    private String feedCopyright;
    private String feedAuthor;
    private String articleTitle;
    private String articleAuthor;
    private String articleBody;
    private String articleDate;
    private String articleCategory = "entertainmentheadlines";
    private Image articleImage;
    private URL articleImageUrl;
    private int articlesLength;
    private final Reader newsReader;

    public NewsModel() {
        this.newsReader = new Reader();
    }

    public String getFeedDescription() {
        return feedDescription;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public String getFeedCopyright() {
        return feedCopyright;
    }

    public String getFeedAuthor() {
        return feedAuthor;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }

    public String getArticleBody() {
        return articleBody;
    }

    public String getArticleDate() {
        return articleDate;
    }

    public void setArticleCategory(String articleCategory) {
        this.articleCategory = articleCategory;
    }

    public String getArticleCategory() {
        return articleCategory;
    }

    public Image getArticleImage() {
        return articleImage;
    }

    public int getArticlesLength() {
        return articlesLength;
    }

    /**
     * Loads the requested news article from the NewsConentModel, and extracts
     * the relevant section and metadata associated with the news article
     *
     * @param articleIndex Article index to load ie. from 0 - articlesLength
     */
    public void loadNewsArticle(int articleIndex) {
        try {
            System.out.println("index cat : " + articleIndex);
            System.out.println("Articles category::" + articleCategory);
            NewsContentModel news[] = newsReader.readFeeds(articleCategory, articleIndex);
            articleDate = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            articlesLength = news.length;
            System.out.println(news[articleIndex].getArticleBody());
            System.out.println("------------------------");
            this.articleImageUrl = news[articleIndex].getArticleImage();
            this.articleImage = ImageIO.read(news[articleIndex].getArticleImage());
            this.articleBody = template(news[articleIndex].getArticleBody());
            this.articleTitle = news[articleIndex].getArticleTitle();
            this.articleDate = news[articleIndex].getBarticleDate();
            this.feedLink = news[articleIndex].getFeedLink();

        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void downloadNews() {
        Timestamp stamp = new Timestamp(System.currentTimeMillis());
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        Date date = new Date(stamp.getTime());
        System.out.println(date);
        try {
            ByteArrayOutputStream out;
            try (InputStream in = new BufferedInputStream(articleImageUrl.openStream())) {
                out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int n = 0;
                while (-1 != (n = in.read(buf))) {
                    out.write(buf, 0, n);
                }
                out.close();
            }
            byte[] response1 = out.toByteArray();
            try (FileOutputStream fos = new FileOutputStream("images/" + timeStamp + ".jpg")) {
                fos.write(response1);
            }
            // Write article content.
            try (BufferedWriter outfile = new BufferedWriter(new FileWriter("articles/" + timeStamp + ".html"))) {
                outfile.write(articleBody);
            }
            // Write Article title.
            try (BufferedWriter outfile = new BufferedWriter(new FileWriter("titles/" + timeStamp + ".html"))) {
                outfile.write(articleTitle);
            }

        } catch (IOException ex) {
            System.out.println(ex);
        }
        System.out.println("Download complete!");
    }

    /**
     * Generates a html document template and insert the body content
     *
     * @param text
     * @return
     */
    private String template(String text) {
        String template = "<html><head></head>"
                + "<body>"
                + "<p style=\"margin-top: 0;font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">"
                + text
                + "</p>"
                + "</body>"
                + "</html>";
        return template;
    }

}
