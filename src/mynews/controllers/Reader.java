/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.controllers;

import com.sun.syndication.feed.module.mediarss.MediaEntryModule;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mynews.models.NewsContentModel;
import mynews.utils.SystemProxySettings;

/**
 * @author Hanumant Shikhare
 */
public class Reader {

    //private NewsModel news;
    private NewsContentModel model;
    private int index = 0;
    private String[] articleAuthor;
    private String[] articleTitle;
    private String[] articleDate;
    private URL[] articleImage;
    private String[] articleBody;

    public Reader() {
    }

    public NewsContentModel[] readFeeds(String cat, int index) {
        List<NewsContentModel> news = new ArrayList<>();
        try {
            SystemProxySettings.setProxySettings();
            URL url = new URL("http://feeds.abcnews.com/abcnews/" + cat);
            try (XmlReader reader = new XmlReader(url)) {
                SyndFeed feed = new SyndFeedInput().build(reader);
                int feedSize = feed.getEntries().size();
                System.out.println("Number of entries..." + feedSize);
                articleAuthor = new String[feedSize];
                articleTitle = new String[feedSize];
                articleDate = new String[feedSize];
                articleImage = new URL[feedSize];
                articleBody = new String[feedSize];

                for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                    model = new NewsContentModel();
                    SyndEntry entry = (SyndEntry) i.next();
                    model.setArticleAuthor(entry.getAuthor());
                    model.setArticleTitle(entry.getTitle());
                    model.setBarticleDate(entry.getPublishedDate().toString());
                    MediaEntryModule m = (MediaEntryModule) entry.getModule(MediaEntryModule.URI);
                    //System.out.println("Iterator" + i + "Index: " + index + "image: " + m.getMetadata().getThumbnail()[5].getUrl().toURL());
                    model.setArticleImage(m.getMetadata().getThumbnail()[5].getUrl().toURL());
                    model.setArticleBody(entry.getDescription().getValue());
                    news.add(model);

                }
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex);
            } catch (FeedException ex) {
                System.out.println(ex);
            }
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
        NewsContentModel newsArray[] = new NewsContentModel[news.size()];
        news.toArray(newsArray);
        return newsArray;
    }

    public void readFeed(String cat, int index) {
        try {
            SystemProxySettings.setProxySettings();
            URL url = new URL("http://feeds.abcnews.com/abcnews/" + cat);
            try (XmlReader reader = new XmlReader(url)) {
                SyndFeed feed = new SyndFeedInput().build(reader);
//            System.out.println("Copyright: " + feed.getCopyright());
//            System.out.println("Description: " + feed.getDescription());
//            System.out.println("Link: " + feed.getLink());
//            System.out.println("Feed Title: " + feed.getTitle());
//                news.setFeedAuthor(feed.getAuthor());
//                news.setFeedCopyright(feed.getCopyright());
//                news.setFeedDescription(feed.getDescription());
//                news.setFeedLink(feed.getLink());
//                news.setFeedTitle(feed.getTitle());
                int feedSize = feed.getEntries().size();
                System.out.println("Number of entries..." + feedSize);
                articleAuthor = new String[feedSize];
                articleTitle = new String[feedSize];
                articleDate = new String[feedSize];
                articleImage = new URL[feedSize];
                articleBody = new String[feedSize];

                for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                    SyndEntry entry = (SyndEntry) i.next();
                    articleAuthor[index] = entry.getAuthor();
                    articleTitle[index] = entry.getTitle();
                    articleDate[index] = entry.getPublishedDate().toString();
                    //System.out.println("title: "+entry.getTitle());
                    //System.out.println("author: "+entry.getAuthor());
                    MediaEntryModule m = (MediaEntryModule) entry.getModule(MediaEntryModule.URI);
                    //System.out.println("Iterator" + i + "Index: " + index + "image: " + m.getMetadata().getThumbnail()[5].getUrl().toURL());
                    articleImage[index] = m.getMetadata().getThumbnail()[5].getUrl().toURL();
                    articleBody[index] = entry.getDescription().getValue();
                    index++;

                }
//                news.setArticleAuthor(articleAuthor);
//                news.setArticleBody(articleBody);
//                news.setArticleImage(articleImage);
//                news.setArticleTitle(articleTitle);
//                news.setArticleDate(articleDate);
            } catch (IOException ex) {
                System.out.println(ex);
            } catch (IllegalArgumentException ex) {
                System.out.println(ex);
            } catch (FeedException ex) {
                System.out.println(ex);
            }
        } catch (MalformedURLException ex) {
            System.out.println(ex);
        }
    }

//    public static void main(String[] args) {
//        try {
//            Reader r = new Reader(null);
//            r.readFeed();
//        } catch (Exception ex) {
//            Logger.getLogger(Reader.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
