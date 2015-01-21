/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mynews.models;

import java.net.URL;

/**
 *
 * @author RESEARCH2
 */
public class ModelNews {
    private String feedDescription;
    private String feedLink;
    private String feedTitle;
    private String feedCopyright;
    private String feedAuthor;
    private String[] articleTitle;
    private String[] articleAuthor;
    private URL[] articleImage;
    private String[] articleBody;
    private String[] articleDate;

    public String getFeedDescription() {
        return feedDescription;
    }

    public void setFeedDescription(String feedDescription) {
        this.feedDescription = feedDescription;
    }

    public String getFeedLink() {
        return feedLink;
    }

    public void setFeedLink(String feedLink) {
        this.feedLink = feedLink;
    }

    public String getFeedTitle() {
        return feedTitle;
    }

    public void setFeedTitle(String feedTitle) {
        this.feedTitle = feedTitle;
    }

    public String getFeedCopyright() {
        return feedCopyright;
    }

    public void setFeedCopyright(String feedCopyright) {
        this.feedCopyright = feedCopyright;
    }

    public String getFeedAuthor() {
        return feedAuthor;
    }

    public void setFeedAuthor(String feedAuthor) {
        this.feedAuthor = feedAuthor;
    }

    public String[] getArticleTitle() {
        return articleTitle;
    }

    public void setArticleTitle(String[] articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String[] getArticleAuthor() {
        return articleAuthor;
    }

    public void setArticleAuthor(String[] articleAuthor) {
        this.articleAuthor = articleAuthor;
    }

    public URL[] getArticleImage() {
        return articleImage;
    }

    public void setArticleImage(URL[] articleImage) {
        this.articleImage = articleImage;
    }



    public String[] getArticleBody() {
        return articleBody;
    }

    public void setArticleBody(String[] articleBody) {
        this.articleBody = articleBody;
    }

    public String[] getArticleDate() {
        return articleDate;
    }

    public void setArticleDate(String[] articleDate) {
        this.articleDate = articleDate;
    }

    
    
    
    
}
