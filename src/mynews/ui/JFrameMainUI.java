/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.ui;

import com.alee.laf.menu.WebMenuItem;
import com.alee.laf.menu.WebPopupMenu;
import com.alee.managers.hotkey.Hotkey;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicComboPopup;
import mynews.controllers.Archives;
import mynews.utils.CustomScrollbarUI;
import mynews.utils.EnglishNumberToWord;
import mynews.utils.SystemTime;
import mynews.utils.TestInternetConnection;
import mynews.utils.TweakUI;

/**
 *
 * @author RESEARCH2
 */
public class JFrameMainUI extends javax.swing.JFrame {

    private Image image = null;
    private int index = 0;
    private int counter = 10;
    private int totalArticles = 0;
    private WebMenuItem wmiAbout;
    private WebMenuItem wmiGreenPolicy;
    private WebMenuItem wmiNewNote;
    private WebMenuItem wmiTags;
    private WebMenuItem wmiClose;
    private WebMenuItem wmiHelp;
    private JDialog gp;
    private boolean netStatus = false;

    /**
     * Creates new form JFrameMainUI
     *
     * @throws java.lang.ClassNotFoundException
     * @throws javax.swing.UnsupportedLookAndFeelException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     */
    public JFrameMainUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        setUIFont(new javax.swing.plaf.FontUIResource("Segoe UI Semilight", Font.PLAIN, 11));
        initComponents();
        initWebMenu();
        TweakUI.centerParent(this);
        image = new ImageIcon(getClass().getResource("/mynews/resources/mynews-d.png")).getImage();
        jLabelLoadingFeed.setVisible(false);
        initCustomScrollBar();
        createJBtnHoverEffects();
        initClockAndNetworkTimer(true);
    }

    /**
     * Returns an this class object
     *
     * @return
     */
    private JFrameMainUI thisJFrame() {
        return this;
    }

    /**
     * Returns the internet connection status
     *
     * @return true, if code can access the feed server i.e.
     * http://www.abcnews/feed/, false otherwise
     */
    public boolean isNetOn() {
        return netStatus;
    }

    /**
     * Add an ActionListener to the Next button
     *
     * @param al JButton ActionListener
     */
    public void addBtnNextActionListener(ActionListener al) {
        jButtonNext.addActionListener(al);
    }

    /**
     * Add an ActionListener to the Download button
     *
     * @param al JButton ActionListener
     */
    public void addBtnDownloadActionListener(ActionListener al) {
        jButtonDownload.addActionListener(al);
    }

    /**
     * Add an ActionListener to the Archives button
     *
     * @param al JButton ActionListener
     */
    public void addBtnArchivesActionListener(ActionListener al) {
        jButtonArchives.addActionListener(al);
    }

    /**
     * Add an ActionListener to the Previous button
     *
     * @param al JButton ActionListener
     */
    public void addBtnPrevActionListener(ActionListener al) {
        jButtonPrev.addActionListener(al);
    }

    /**
     * Add an ActionListener to the Print button
     *
     * @param al JButton ActionListener
     */
    public void addBtnPrintActionListener(ActionListener al) {
        jButtonPrint.addActionListener(al);
    }

    /**
     * Sets the article content/body
     *
     * @param news Article body
     */
    public void setArticleContent(String news) {
        jTextPaneArticleContents.setText(news);
    }

    /**
     * Sets the article feed url
     *
     * @param feedUrl Feed url
     */
    public void setArticleFeedUrl(String feedUrl) {
        jLabelFeedLink.setText(feedUrl);
    }

    /**
     * Sets the title of the news article
     *
     * @param title Article title
     */
    public void setArticleTitle(String title) {
        jLabelArticleTitle.setText(title);
    }

    /**
     * Set the number of articles downloaded
     *
     * @param totalArticlesDownloaded Number of articles retrieved from the feed
     */
    public void setTotalArticles(int totalArticlesDownloaded) {
        totalArticles = totalArticlesDownloaded;
        setCurrentArticleIndex(0, totalArticles);
    }

    /**
     * Sets the number of the currently viewed article
     *
     * @param currentArticleIndex Number of the current article displayed
     * @param articlesLength Total number of articles available
     */
    public void setCurrentArticleIndex(int currentArticleIndex, int articlesLength) {
        System.out.println("cur: "+currentArticleIndex+" total: "+articlesLength);
        jLabelCurrentArticle.setText((currentArticleIndex + 1) + " Of " + articlesLength + " Articles");
    }

    /**
     * Sets the date the article was published
     *
     * @param date
     */
    public void setArticleDate(String date) {
        jLabelArticleDate.setText(date);
    }

    /**
     * Sets the image associated with a give article/news item.
     *
     * @param img Article image
     */
    public void setArticleImage(Image img) {
        image = img.getScaledInstance(jPanelTop.getWidth(), jPanelTop.getHeight(), Image.SCALE_SMOOTH);
        jPanelTop.repaint();
    }

    /**
     * Sets the image associated with a give article/news item.
     *
     * @param img Article image
     */
    public void setArticleImage(Image img, int imageWidth, int imageHeight) {
        image = img.getScaledInstance(jPanelTop.getWidth(), jPanelTop.getHeight(), Image.SCALE_SMOOTH);
        System.out.println("h:" + jPanelTop.getHeight() + "w:" + jPanelTop.getWidth());
        jPanelTop.repaint();
    }

    /**
     * Get the index of the next article. If the index of the next article
     * exceeds the number of articles available, the reset the index to 0.
     *
     * @return Index of the next article.
     */
    public int getNextArticleIndex() {
        index = (index > totalArticles) ? index = 0 : index + 1;
        setCurrentArticleIndex(index, totalArticles);
        return index;
    }

    /**
     * Get the index of the previous article. If the index of the last article
     * is 0, then reset index to 0 to avoid getting ArrayOutOfBound Exception
     *
     * @return Index of the previous article.
     */
    public int getPrevArticleIndex() {
        index = (index == 0) ? index = 0 : index - 1;
        setCurrentArticleIndex(index, totalArticles);
        return index;
    }

    /**
     * Show/hide the loading feed spinner
     *
     * @param status
     */
    public void isLoadingArticle(boolean status) {
        jLabelLoadingFeed.setVisible(status);
    }

    /**
     * Gets the index of the selected news item
     *
     * @return
     */
    public int getSelectedNewsCategoryIndex() {
        return jComboBoxNewsCategory.getSelectedIndex();
    }

    /**
     * Adds itemListener to the categories JComboBox
     *
     * @param il
     */
    public void addJComboItemListener(ItemListener il) {
        jComboBoxNewsCategory.addItemListener(il);
    }

    /**
     * Sets the feed link URL
     *
     * @param link
     */
    public void setFeedLink(String link) {
        jLabelFeedLink.setText(link);
    }

    public void addWmiGreenPolicyActionListener(ActionListener al) {
        wmiGreenPolicy.addActionListener(al);
    }

    public void addWmiAboutActionListener(ActionListener al) {
        wmiAbout.addActionListener(al);

    }
     /**
     * Creates and shows the Green Policy dialog
     */
    public void initGreenPolicyDialog() {
        final JDialog gp = new JDialog(thisJFrame());
        gp.setLayout(new BorderLayout());
        JPanelGreenPolicy jpgp = new JPanelGreenPolicy();
        gp.setSize(313, 310);
        gp.add(jpgp);
        gp.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gp.dispose();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
        });
        int w = thisJFrame().getWidth();
        int h = thisJFrame().getHeight();
        int locx = (int) thisJFrame().getLocation().getX();
        int locy = (int) thisJFrame().getLocation().getY();
        gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
        gp.setUndecorated(true);
        gp.setOpacity((float) 1.0);
        gp.setVisible(true);
    }

    /**
     * Creates and shows the About dialog.
     */
    public void initAboutDialog() {
        final JDialog gp = new JDialog(thisJFrame());
        gp.setLayout(new BorderLayout());
        JPanelAbout jpgp = new JPanelAbout();
        gp.setSize(282, 390);
        gp.add(jpgp);
        gp.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gp.dispose();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
        });
        int w = thisJFrame().getWidth();
        int h = thisJFrame().getHeight();
        int locx = (int) thisJFrame().getLocation().getX();
        int locy = (int) thisJFrame().getLocation().getY();
        gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
        gp.setUndecorated(true);
        gp.setOpacity((float) 1.0);
        gp.setVisible(true);
    }

    /**
     * Creates the hover effects on the buttons contained on the bottom JPanel
     * i.e. The button changes color when the user hovers the mouse on top
     */
    private void createJBtnHoverEffects() {

        for (Component comp : jPanelBottom.getComponents()) {
            if (comp instanceof JButton) {
                JButton myButton = (JButton) comp;
                myButton.setFocusPainted(false);
                myButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JButton btn = (JButton) e.getSource();
                        btn.setOpaque(true);
                        btn.setBackground(new Color(204, 204, 204));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        JButton btn = (JButton) e.getSource();
                        btn.setOpaque(false);
                    }
                });
            }
        }
    }

    public void loadArticlesFromAchives() {
        gp = new JDialog(thisJFrame());
        gp.setLayout(new BorderLayout());
        JPanelArchives jpgp = new JPanelArchives();
        gp.setSize(800, 500);
        gp.add(jpgp);
        gp.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                gp.dispose();
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
            }
        });
        int w = thisJFrame().getWidth();
        int h = thisJFrame().getHeight();
        int locx = (int) thisJFrame().getLocation().getX();
        int locy = (int) thisJFrame().getLocation().getY();
        gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
        gp.setUndecorated(true);
        gp.setOpacity((float) 1.0);
        gp.setVisible(true);
        Archives ca = new Archives(this, jpgp);
        ca.startBackgroundWorker();
        
    }

    public JDialog getGp() {
        return gp;
    }

    public void setGp(JDialog gp) {
        this.gp = gp;
    }

    private JFrameMainUI me() {
        return this;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public JPanel getjPanelTop() {
        return jPanelTop;
    }

    public void setjPanelTop(JPanel jPanelTop) {
        this.jPanelTop = jPanelTop;
    }

    public JTextPane getjTextPaneArticleContents() {
        return jTextPaneArticleContents;
    }

    public void setjTextPaneArticleContents(JTextPane jTextPaneArticleContents) {
        this.jTextPaneArticleContents = jTextPaneArticleContents;
    }

    /**
     * Build and Initialize WebMenu dropdown menu items
     */
    public final void initWebMenu() {
        this.wmiTags = new WebMenuItem("Preferrences", Hotkey.T);
        this.wmiNewNote = new WebMenuItem("Save News", Hotkey.V);
        this.wmiAbout = new WebMenuItem("About MyNews", Hotkey.ESCAPE);
        this.wmiGreenPolicy = new WebMenuItem("Our Green Policy", Hotkey.ALT_Y);
        this.wmiClose = new WebMenuItem("You sure you want to switch off?", Hotkey.ALT_C);
        this.wmiHelp = new WebMenuItem("Help/Tutorial", Hotkey.ALT_H);
    }

    /**
     * Customize the JScrollBar with custom colors and styles by setting the
     * scrollbar UI with a CustomScrollbarUI() class
     */
    public final void initCustomScrollBar() {
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        jScrollPane1.getVerticalScrollBar().setUI(new CustomScrollbarUI(Color.WHITE));
        Object popup = jComboBoxNewsCategory.getUI().getAccessibleChild(jComboBoxNewsCategory, 0);
        Component c = ((Container) popup).getComponent(0);
        if (c instanceof JScrollPane) {
            JScrollPane spane = (JScrollPane) c;
            spane.getVerticalScrollBar().setUI(new CustomScrollbarUI(Color.WHITE));
            JScrollBar scrollBar = spane.getVerticalScrollBar();
            Dimension scrollBarDim = new Dimension(10, scrollBar.getPreferredSize().height);
            scrollBar.setPreferredSize(scrollBarDim);
        }
        BasicComboPopup pop = (BasicComboPopup) popup;
        pop.setBorder(null);
    }

    /**
     * Initialize a Timer thats updates the application time/clock, and checks
     * the internet connection persistently
     *
     * @param repeat
     */
    public final void initClockAndNetworkTimer(boolean repeat) {
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (TestInternetConnection.isNetAvailable()) {
                    netStatus = true;
                    jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/online.png")));
                    jLabelStatus.setText("[ Online ]");
                    jLabelStatus.setToolTipText("Connection established!");
                } else {
                    if (counter == 0) {
                        counter = 10;
                    }
                    netStatus = false;
                    jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/offline.png")));
                    jLabelStatus.setText("Retry in " + counter + " secs");
                    jLabelStatus.setToolTipText("Connection cannot be established!");
                    counter--;
                }
                SystemTime time = new SystemTime();
                jLabelDate.setText(time.getFullDate());
                jLabelTime.setText(TimeToWords());
            }
        };
        Timer timer = new Timer(1000, taskPerformer);
        timer.setRepeats(repeat);
        timer.start();
    }

    /**
     * Converts time to word. i.e. 12:39 will be converted to Twelve Thirty Nine
     *
     * @return Time in word format
     */
    private String TimeToWords() {
        SystemTime time = new SystemTime();
        EnglishNumberToWord entw = new EnglishNumberToWord();
        String min;
        String hrs;
        String am_pm;
        if (time.getMin() < 10) {
            min = "oh" + entw.convert(time.getMin());
        } else {
            min = entw.convert(time.getMin());
        }
        if (time.getHrs() > 12) {
            hrs = entw.convert(time.getHrs() - 12);
            am_pm = "pm";
        } else {
            hrs = entw.convert(time.getHrs());
            am_pm = "am";
        }
        return "<html>" + hrs + " <font color=\"#ee5500\">" + min + "</font> <font color=\"#00a5f8\" size = \"4\">" + am_pm + "</font>";
    }

    /**
     * Sets the whole UI to use a custom font
     *
     * @param f Font resource to use on the UI
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource f) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value != null && value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, f);
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelHome = new javax.swing.JPanel();
        jPanelTop = new javax.swing.JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(image, 0, 0, null);
            }
        };
        jLabelImage = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jLabelTime = new javax.swing.JLabel();
        webSplitButtonMenu = new com.alee.extended.button.WebSplitButton();
        jButton1 = new javax.swing.JButton();
        jLabelDate = new javax.swing.JLabel();
        jPanelCenter = new javax.swing.JPanel();
        jLabelArticleTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelArticleDate = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneArticleContents = new javax.swing.JTextPane();
        jLabelFeedLink = new javax.swing.JLabel();
        jButtonNext = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
        jComboBoxNewsCategory = new javax.swing.JComboBox();
        jPanelBottom = new javax.swing.JPanel();
        jButtonDownload = new javax.swing.JButton();
        jButtonLock = new javax.swing.JButton();
        jButtonSettings = new javax.swing.JButton();
        jButtonArchives = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButtonPrint = new javax.swing.JButton();
        jLabelLoadingFeed = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jLabelCurrentArticle = new javax.swing.JLabel();
        filler2 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 0), new java.awt.Dimension(0, 0), new java.awt.Dimension(32767, 32767));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel1MousePressed(evt);
            }
        });
        jPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel1MouseDragged(evt);
            }
        });

        jPanelHome.setBackground(new java.awt.Color(255, 255, 255));
        jPanelHome.setLayout(new java.awt.BorderLayout());

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setOpaque(false);

        jLabelTime.setFont(new java.awt.Font("Segoe UI Semilight", 0, 28)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTime.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelTime.setText("03:25");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addContainerGap(86, Short.MAX_VALUE)
                .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jLabelTime))
        );

        webSplitButtonMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/menu_32.png"))); // NOI18N
        webSplitButtonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webSplitButtonMenuActionPerformed(evt);
            }
        });

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/power_32.png"))); // NOI18N
        jButton1.setBorderPainted(false);
        jButton1.setContentAreaFilled(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabelDate.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabelDate.setForeground(new java.awt.Color(255, 255, 255));
        jLabelDate.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelDate.setText("March 12, 2015");

        javax.swing.GroupLayout jPanelTopLayout = new javax.swing.GroupLayout(jPanelTop);
        jPanelTop.setLayout(jPanelTopLayout);
        jPanelTopLayout.setHorizontalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTopLayout.createSequentialGroup()
                        .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 584, Short.MAX_VALUE)
                        .addComponent(jLabelDate, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelTopLayout.createSequentialGroup()
                        .addComponent(webSplitButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(683, 683, 683)
                        .addComponent(jButton1)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTopLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanelTopLayout.setVerticalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopLayout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(webSplitButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelTopLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(61, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTopLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelDate)
                        .addGap(1, 1, 1)
                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2))))
        );

        jPanelHome.add(jPanelTop, java.awt.BorderLayout.NORTH);

        jPanelCenter.setBackground(new java.awt.Color(255, 255, 255));

        jLabelArticleTitle.setFont(new java.awt.Font("Ebrima", 0, 24)); // NOI18N
        jLabelArticleTitle.setForeground(new java.awt.Color(255, 102, 0));
        jLabelArticleTitle.setText("<html>MyNews&trade;");

        jLabelArticleDate.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabelArticleDate.setForeground(new java.awt.Color(0, 153, 204));
        jLabelArticleDate.setText("March 12, 2015 11:30AM");

        jScrollPane1.setBorder(null);

        jTextPaneArticleContents.setBorder(null);
        jTextPaneArticleContents.setContentType("text/html"); // NOI18N
        jTextPaneArticleContents.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jTextPaneArticleContents.setForeground(new java.awt.Color(51, 51, 51));
        jTextPaneArticleContents.setText("<html>\r\n  <head>\r\n  </head>\r\n  <body>\r\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">\nWelcome, and thankyou for using MyNews&trade;. This is a FREE software that delivers RSS news  (currently) from abc news feed [ <a href=\"#\">http://abcnews.go.com/Site/page/rss--3520115</a>.\n</p>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#FF6600; font-size: 12px;\">\nWhat's an RSS?\n</p>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">\nRSS (Rich Site Summary); originally RDF Site Summary; often called Really Simple Syndication, uses a family of standard web feed formats to publish frequently\n updated information: blog entries, news headlines, audio, video. An RSS document (called “feed”, “web feed”, or “channel”) includes full or summarized text\n, and metadata, like publishing date and author’s name.\nRSS feeds enable publishers to syndicate data automatically. A standard XML file format ensures compatibility with many different machines/programs. RSS feeds \nalso benefit users who want to receive timely updates from favourite websites or to aggregate data from many sites. Read More <a href=\"#\">http://en.wikipedia.org/wiki/RSS</a>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#FF6600; font-size: 12px;\">\nAbout MyNews&trade;?\n</p>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">\nThis is Java SE back-end and Java Swing UI application, that works by parsing RSS (Really Simple Syndicate)  from the abc news  feed. (Am currently working on multiple feeds). \nThe heart of the application is the ROME XML parser, that reads the feed output xml file and parses the tags to generate the new content and metadata. Once the feed is stripped\n off the xml tags, then the news content is simply passed to the UI to be displayed. See more on myblog <a href=\"#\">https://iworkslabs.wordpress.com/2015/03/11/mynews/</a>\n</p>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#FF6600; font-size: 12px;\">\nHow to Use MyNews&trade;?\n</p>\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">\nNo special configuration is needed to run MyNews. If you are behind a proxy, then you will need to supply the proxy settings by clicking on the cog button. After suppling the proxy\ndetails, you will need to restart the application for the changes to take effect.\n</p>\n  </body>\r\n</html>\r\n");
        jScrollPane1.setViewportView(jTextPaneArticleContents);

        jLabelFeedLink.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabelFeedLink.setForeground(new java.awt.Color(204, 0, 153));
        jLabelFeedLink.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelFeedLink.setText("https://iworkslabs.wordpress.com/2015/03/11/mynews/");

        jButtonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/linea-next.png"))); // NOI18N
        jButtonNext.setContentAreaFilled(false);
        jButtonNext.setFocusPainted(false);
        jButtonNext.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/linea_next_color.png"))); // NOI18N
        jButtonNext.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/linea_next_color.png"))); // NOI18N
        jButtonNext.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonNextMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonNextMouseExited(evt);
            }
        });
        jButtonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonNextActionPerformed(evt);
            }
        });

        jButtonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/linea-prev.png"))); // NOI18N
        jButtonPrev.setContentAreaFilled(false);
        jButtonPrev.setFocusPainted(false);
        jButtonPrev.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/linea_prev_color.png"))); // NOI18N
        jButtonPrev.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jButtonPrevMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jButtonPrevMouseExited(evt);
            }
        });
        jButtonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonPrevActionPerformed(evt);
            }
        });

        jComboBoxNewsCategory.setEditable(true);
        jComboBoxNewsCategory.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " News Category", " Top Stories", " World Headlines", " US Headlines", " Politics Headlines", " Entertainment Headlines", " International Headlines", " ESPN Sports", " Technology Headlines", " Money Headlines", " Health Headlines", " Travel Headlines" }));

        javax.swing.GroupLayout jPanelCenterLayout = new javax.swing.GroupLayout(jPanelCenter);
        jPanelCenter.setLayout(jPanelCenterLayout);
        jPanelCenterLayout.setHorizontalGroup(
            jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenterLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addComponent(jButtonPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanelCenterLayout.createSequentialGroup()
                                .addComponent(jLabelArticleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelFeedLink, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComboBoxNewsCategory, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 744, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addComponent(jLabelArticleTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 615, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );

        jPanelCenterLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jButtonNext, jButtonPrev});

        jPanelCenterLayout.setVerticalGroup(
            jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelCenterLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabelArticleTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabelFeedLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelArticleDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBoxNewsCategory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanelCenterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jButtonPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanelCenterLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(14, 14, 14))
            .addGroup(jPanelCenterLayout.createSequentialGroup()
                .addGap(138, 138, 138)
                .addComponent(jButtonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(96, 96, 96))
        );

        jPanelCenterLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonNext, jButtonPrev});

        jPanelHome.add(jPanelCenter, java.awt.BorderLayout.CENTER);

        jPanelBottom.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelBottomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelBottomMouseExited(evt);
            }
        });

        jButtonDownload.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/download_32.png"))); // NOI18N
        jButtonDownload.setBorderPainted(false);
        jButtonDownload.setContentAreaFilled(false);
        jButtonDownload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDownloadActionPerformed(evt);
            }
        });

        jButtonLock.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/lock_32.png"))); // NOI18N
        jButtonLock.setBorderPainted(false);
        jButtonLock.setContentAreaFilled(false);
        jButtonLock.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonLockActionPerformed(evt);
            }
        });

        jButtonSettings.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/settings_32.png"))); // NOI18N
        jButtonSettings.setBorderPainted(false);
        jButtonSettings.setContentAreaFilled(false);
        jButtonSettings.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSettingsActionPerformed(evt);
            }
        });

        jButtonArchives.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/archive_32.png"))); // NOI18N
        jButtonArchives.setBorderPainted(false);
        jButtonArchives.setContentAreaFilled(false);
        jButtonArchives.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonArchivesActionPerformed(evt);
            }
        });

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/delete_32.png"))); // NOI18N
        jButtonDelete.setBorderPainted(false);
        jButtonDelete.setContentAreaFilled(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });

        jButtonPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/print_32.png"))); // NOI18N
        jButtonPrint.setBorderPainted(false);
        jButtonPrint.setContentAreaFilled(false);

        jLabelLoadingFeed.setFont(new java.awt.Font("Segoe UI Semilight", 0, 11)); // NOI18N
        jLabelLoadingFeed.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLoadingFeed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/feed_loader.GIF"))); // NOI18N
        jLabelLoadingFeed.setText("Loading Article. Hold On!");
        jLabelLoadingFeed.setIconTextGap(10);

        jLabelStatus.setFont(new java.awt.Font("Segoe UI Semilight", 0, 10)); // NOI18N
        jLabelStatus.setForeground(new java.awt.Color(102, 102, 102));
        jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/offline.png"))); // NOI18N
        jLabelStatus.setText("[ Offline ]");
        jLabelStatus.setIconTextGap(0);

        jLabelCurrentArticle.setFont(new java.awt.Font("Segoe UI Semilight", 0, 11)); // NOI18N
        jLabelCurrentArticle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabelCurrentArticle.setText("0 Of 0 Articles");

        javax.swing.GroupLayout jPanelBottomLayout = new javax.swing.GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButtonPrint)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonArchives)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonDownload)
                .addGap(18, 18, Short.MAX_VALUE)
                .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(filler2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelLoadingFeed, javax.swing.GroupLayout.DEFAULT_SIZE, 192, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jLabelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonSettings)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButtonLock)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabelCurrentArticle, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelBottomLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {filler2, jLabelLoadingFeed});

        jPanelBottomLayout.setVerticalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBottomLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButtonLock)
                    .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jButtonPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButtonDownload, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonArchives, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonSettings, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabelCurrentArticle, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelBottomLayout.createSequentialGroup()
                        .addComponent(jLabelLoadingFeed)
                        .addGap(1, 1, 1)
                        .addComponent(filler2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0))
        );

        jPanelBottomLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButtonArchives, jButtonDelete, jButtonDownload, jButtonLock, jButtonPrint, jButtonSettings, jLabelCurrentArticle, jLabelStatus});

        jPanelHome.add(jPanelBottom, java.awt.BorderLayout.SOUTH);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHome, javax.swing.GroupLayout.PREFERRED_SIZE, 849, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MousePressed
        // TODO add your handling code here:
        TweakUI.onMousePressed(this, evt);
    }//GEN-LAST:event_jPanel1MousePressed

    private void jPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel1MouseDragged
        // TODO add your handling code here:
        TweakUI.onMouseDragged(this, evt);
    }//GEN-LAST:event_jPanel1MouseDragged

    private void jButtonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonNextActionPerformed


    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButtonPrevActionPerformed

    private void jPanelBottomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelBottomMouseEntered

    }//GEN-LAST:event_jPanelBottomMouseEntered

    private void jPanelBottomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelBottomMouseExited

    }//GEN-LAST:event_jPanelBottomMouseExited

    private void webSplitButtonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webSplitButtonMenuActionPerformed
        // TODO add your handling code here:
        final WebPopupMenu popupMenu = new WebPopupMenu();
        popupMenu.add(wmiNewNote);
        popupMenu.add(wmiTags);
        popupMenu.addSeparator();
        popupMenu.add(wmiAbout);
        popupMenu.add(wmiGreenPolicy);
        popupMenu.addSeparator();
        popupMenu.add(wmiHelp);
        popupMenu.add(wmiClose);
        wmiClose.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        webSplitButtonMenu.setPopupMenu(popupMenu);
    }//GEN-LAST:event_webSplitButtonMenuActionPerformed

    private void jButtonDownloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDownloadActionPerformed
        // TODO add your handling code here:
        JDialogLock dialog = new JDialogLock(this, true, jButtonDownload);
        dialog.setLabelMsg("Download this Article?");
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonDownloadActionPerformed

    private void jButtonArchivesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonArchivesActionPerformed
        // TODO add your handling code here:
        //loadArticlesFromAchives();

    }//GEN-LAST:event_jButtonArchivesActionPerformed

    private void jButtonPrevMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPrevMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrevMouseEntered

    private void jButtonPrevMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPrevMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonPrevMouseExited

    private void jButtonNextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNextMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNextMouseEntered

    private void jButtonNextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNextMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_jButtonNextMouseExited

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        System.exit(0);
//        jPanelTop.setSize(new Dimension(this.getWidth(), 100));
//        jPanelCenter.setSize(new Dimension(this.getWidth(), 500));
//        this.revalidate();
//        this.repaint();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButtonSettingsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSettingsActionPerformed
        // TODO add your handling code here:
        JDialogSettings dialog = new JDialogSettings(this, true, jButtonSettings);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonSettingsActionPerformed

    private void jButtonLockActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonLockActionPerformed
        // TODO add your handling code here:
        JDialogLock dialog = new JDialogLock(this, true, jButtonLock);
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonLockActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed
        // TODO add your handling code here:
        JDialogLock dialog = new JDialogLock(this, true, jButtonDelete);
        dialog.setLabelMsg("You can only Delete archived Articles!");
        dialog.setVisible(true);
    }//GEN-LAST:event_jButtonDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.Box.Filler filler2;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButtonArchives;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JButton jButtonDownload;
    private javax.swing.JButton jButtonLock;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JButton jButtonPrint;
    private javax.swing.JButton jButtonSettings;
    private javax.swing.JComboBox jComboBoxNewsCategory;
    private javax.swing.JLabel jLabelArticleDate;
    private javax.swing.JLabel jLabelArticleTitle;
    private javax.swing.JLabel jLabelCurrentArticle;
    private javax.swing.JLabel jLabelDate;
    private javax.swing.JLabel jLabelFeedLink;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JLabel jLabelLoadingFeed;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelCenter;
    private javax.swing.JPanel jPanelHome;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextPane jTextPaneArticleContents;
    private com.alee.extended.button.WebSplitButton webSplitButtonMenu;
    // End of variables declaration//GEN-END:variables
}
