/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import javax.swing.SwingWorker;
import mynews.models.NewsModel;
import mynews.ui.JFrameMainUI;

/**
 *
 * @author RESEARCH2
 */
public class MainUIController {
    
    private final NewsModel model;
    private final JFrameMainUI view;
    
    public MainUIController(NewsModel myModel, JFrameMainUI myView) {
        this.model = myModel;
        this.view = myView;
        if (view.isNetOn()) {
            startBackgroundWorkerThread(0, 0);
            view.isLoadingArticle(true);
        }
        view.addBtnNextActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                view.isLoadingArticle(true);
                startBackgroundWorkerThread(0, view.getNextArticleIndex());
            }
        });
        view.addBtnPrevActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                view.isLoadingArticle(true);
                startBackgroundWorkerThread(0, view.getPrevArticleIndex());
            }
        });
        view.addBtnDownloadActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                startBackgroundWorkerThread(2, 0);
            }
        });
        view.addBtnArchivesActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                view.loadArticlesFromAchives();
            }
        });
        view.addWmiAboutActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                view.initAboutDialog();
            }
        });
        view.addWmiGreenPolicyActionListener(new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent e) {
                view.initGreenPolicyDialog();
            }
        });
        view.addJComboItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    switch (view.getSelectedNewsCategoryIndex()) {
                        case 1:
                            model.setArticleCategory("topstories");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 2:
                            model.setArticleCategory("worldheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 3:
                            model.setArticleCategory("usheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 4:
                            model.setArticleCategory("politicsheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 5:
                            model.setArticleCategory("entertainmentheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 6:
                            model.setArticleCategory("internationalheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 7:
                            model.setArticleCategory("sportsheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 8:
                            model.setArticleCategory("technologyheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 9:
                            model.setArticleCategory("moneyheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 10:
                            model.setArticleCategory("healthheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                        case 11:
                            model.setArticleCategory("travelheadlines");
                            view.isLoadingArticle(true);
                            startBackgroundWorkerThread(0, 0);
                            break;
                    }
                    view.setFeedLink("http://feeds.abcnews.com/abcnews/" + model.getArticleCategory());
                }
            }
        });
    }

    /**
     * Schedules the requested Article on the swing worker background worker and
     * fire the execute method to start retrieving the Article for display or
     * download to the local drive
     *
     * @param taskType Type of task to perform i.e. 0/1 for loading article, 2
     * for downloading
     * @param articleIndex The index of the Article to download
     */
    public final void startBackgroundWorkerThread(int taskType, int articleIndex) {
        new BackgroundWorker(taskType, articleIndex).execute();
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
    private class BackgroundWorker extends SwingWorker<Boolean, Integer> {
        
        private int articleIndex = 0;
        private int taskType = 0;
        
        public BackgroundWorker(int articleIndex) {
            this.articleIndex = articleIndex;
        }
        
        public BackgroundWorker(int taskType, int articleIndex) {
            this.articleIndex = articleIndex;
            this.taskType = taskType;
        }
        
        @Override
        @SuppressWarnings("SleepWhileInLoop")
        protected Boolean doInBackground() throws Exception {
            switch (taskType) {
                case 0:
                    model.loadNewsArticle(articleIndex);
                    break;
                case 1:
                    model.loadNewsArticle(articleIndex);
                    break;
                case 2:
                    model.downloadNews();
                    break;
                case 3:
                    //view.loadArticlesFromAchives();
                    break;
                default:
                    System.out.println("No defined category");
            }
            return true;
        }
        
        @Override
        protected void done() {
            view.isLoadingArticle(false);
            view.setTotalArticles(model.getArticlesLength());
            displayNews();
        }
        
        @Override
        protected void process(List<Integer> chunks) {
            for (int i : chunks) {
                
            }
        }
        
    }

    /**
     * Displays the news on the UI for the users to view
     */
    private void displayNews() {
        view.setArticleImage(model.getArticleImage());
        view.setArticleTitle(model.getArticleTitle());
        view.setArticleContent(model.getArticleBody());
        view.setArticleDate(model.getArticleDate());
    }

//    private void archivesUI() {
//        final JDialog gp = new JDialog(view);
//        gp.setLayout(new BorderLayout());
//        JPanelArchives jpgp = new JPanelArchives();
//        ControllerArchives ca = new ControllerArchives(, jpgp);
//        gp.setSize(800, 500);
//        System.out.println(jpgp.getWidth());
//        gp.add(jpgp);
//        gp.addWindowFocusListener(new WindowFocusListener() {
//            @Override
//            public void windowLostFocus(WindowEvent e) {
//                gp.dispose();
//            }
//
//            @Override
//            public void windowGainedFocus(WindowEvent e) {
//            }
//        });
//        int w = thisJFrame().getWidth();
//        int h = thisJFrame().getHeight();
//        int locx = (int) thisJFrame().getLocation().getX();
//        int locy = (int) thisJFrame().getLocation().getY();
//        gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
//        gp.setUndecorated(true);
//        gp.setOpacity((float) 1.0);
//        gp.setVisible(true);
//    }
}
