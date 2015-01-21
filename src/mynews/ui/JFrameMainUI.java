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
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicComboPopup;
import mynews.controllers.ControllerArchives;
import mynews.controllers.Reader;
import mynews.models.ModelNews;
import mynews.utils.CustomScrollbarUI;
import mynews.utils.SystemTime;
import mynews.utils.TestInternetConnection;
import mynews.utils.TweakUI;

/**
 *
 * @author RESEARCH2
 */
public class JFrameMainUI extends javax.swing.JFrame {

    private Image image = null;
    private int feedSize;
    private int index = 0;
    private ModelNews mn = new ModelNews();
    private String timeStamp;
    private Timestamp stamp;
    private final WebMenuItem wmiAbout;
    private final WebMenuItem wmiGreenPolicy;
    private final WebMenuItem wmiExport;
    private final WebMenuItem wmiBackup;
    private final WebMenuItem wmiNewNote;
    private final WebMenuItem wmiTags;
    private JDialog gp;
    private Reader newsReader;
    private String newsCat;

    ;    /**
     * Creates new form JFrameMainUI
     */
    public JFrameMainUI() {
        this.newsCat = "internationalheadlines";
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            initComponents();
            setJtextPane();
            jLabelLoadingFeed.setVisible(false);
            TweakUI.centerParent(this);
            testNews(5, newsCat);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JFrameMainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.newsReader = new Reader(mn);
        this.wmiTags = new WebMenuItem("Preferrences", Hotkey.T);
        this.wmiNewNote = new WebMenuItem("Save News", Hotkey.V);
        this.wmiBackup = new WebMenuItem("Backup the Notes", Hotkey.ALT_B);
        this.wmiExport = new WebMenuItem("Export to a File", Hotkey.ALT_A);
        this.wmiAbout = new WebMenuItem("About MyNews", Hotkey.ESCAPE);
        this.wmiGreenPolicy = new WebMenuItem("Our Green Policy", Hotkey.ALT_Y);
        //jPanel3.paintComponents(Graphics g);
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        jScrollPane1.getVerticalScrollBar().setUI(new CustomScrollbarUI(Color.WHITE));
        Object popup = webComboBoxCat.getUI().getAccessibleChild(webComboBoxCat, 0);
        Component c = ((Container) popup).getComponent(0);
        if (c instanceof JScrollPane) {
            JScrollPane spane = (JScrollPane) c;
            spane.getVerticalScrollBar().setUI(new CustomScrollbarUI(Color.WHITE));
            JScrollBar scrollBar = spane.getVerticalScrollBar();
            Dimension scrollBarDim = new Dimension(5, scrollBar
                    .getPreferredSize().height);
            scrollBar.setPreferredSize(scrollBarDim);
        }
        BasicComboPopup pop = (BasicComboPopup) popup;
        pop.setBorder(null);
        customJButtonEffects();
        greenPolicy();
        ActionListener taskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (TestInternetConnection.isNetAvailable()) {
                    jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/online.png")));
                    jLabelStatus.setText("[ Online ]");
                    jLabelStatus.setToolTipText("Connection established!");
                } else {
                    jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/offline.png")));
                    jLabelStatus.setText("[ Offline ]");
                    jLabelStatus.setToolTipText("Connection cannot be established!");
                }
                SystemTime time = new SystemTime();
                jLabelTime.setText(time.getTime());
            }
        };
        Timer timer = new Timer(1000, taskPerformer);
        timer.setRepeats(true);
        timer.start();
        loadNewsCategory();

    }

    private JFrameMainUI thisJFrame() {
        return this;
    }

    private void loadNewsCategory() {
        webComboBoxCat.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    System.out.println("selected cat: " + e.getItem());
                    int selIndex = webComboBoxCat.getSelectedIndex();
                    System.out.println("index: " + selIndex);
                    switch (selIndex) {
                        case 1:
                            newsCat = "topstories";
                            new LoadFeed(newsCat).execute();
                            break;
                        case 2:
                            newsCat = "worldheadlines";
                            new LoadFeed(newsCat).execute();
                            break;
                        case 3:
                            newsCat = "usheadlines";
                            new LoadFeed(newsCat).execute();
                            break;
                        case 4:
                            newsCat = "politicsheadlines";
                            new LoadFeed(newsCat).execute();
                            break;
                    }
                    jLabelFeedLink.setText("http://feeds.abcnews.com/abcnews/"+newsCat);
                }
            }
        });
    }

    private void setJtextPane() {
        jTextPaneArticleContents.setContentType("text/html");
        jTextPaneArticleContents.setText("<html><head></head>"
                + "<body>"
                + "<p style=\"margin-top: 0;font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">"
                + "</p>"
                + "</body>"
                + "</html>");
    }

    private void greenPolicy() {
        wmiGreenPolicy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog gp = new JDialog(thisJFrame());
                gp.setLayout(new BorderLayout());
                JPanelGreenPolicy jpgp = new JPanelGreenPolicy();
                gp.setSize(313, 350);
                System.out.println(jpgp.getWidth());
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
                System.out.println("loc (" + locx + " , " + locy + ")\nwidth: " + w + " height: " + h + " d h: " + gp.getHeight() + " d w: " + gp.getWidth());
                gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
                gp.setUndecorated(true);
                gp.setOpacity((float) 1.0);
                gp.setVisible(true);
            }
        });
        wmiAbout.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                final JDialog gp = new JDialog(thisJFrame());
                gp.setLayout(new BorderLayout());
                JPanelAbout jpgp = new JPanelAbout();
                gp.setSize(449, 390);
                System.out.println(jpgp.getWidth());
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
                System.out.println("loc (" + locx + " , " + locy + ")\nwidth: " + w + " height: " + h + " d h: " + gp.getHeight() + " d w: " + gp.getWidth());
                gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
                gp.setUndecorated(true);
                gp.setOpacity((float) 1.0);
                gp.setVisible(true);
            }
        });

    }

    private void customJButtonEffects() {

        for (Component comp : jPanelBottom.getComponents()) {
            if (comp instanceof JButton) {
                JButton myButton = (JButton) comp;
                myButton.setFocusPainted(false);
                myButton.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        JButton btn = (JButton) e.getSource();
                        btn.setOpaque(true);
                        btn.setBackground(new Color(0, 204, 255));
                        System.out.println(e.getSource());
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

    private class LoadFeed extends SwingWorker<Boolean, Integer> {

        private String cat;

        public LoadFeed(String cat) {
            this.cat = cat;
        }

        @Override
        @SuppressWarnings("SleepWhileInLoop")
        protected Boolean doInBackground() throws Exception {
            testNews(index, cat);
            return true;
        }

        @Override
        protected void done() {
            jLabelLoadingFeed.setVisible(false);
        }

        @Override
        protected void process(List<Integer> chunks) {
            for (int i : chunks) {

            }
        }

    }

    private class Download extends SwingWorker<Boolean, Integer> {

        @Override
        @SuppressWarnings("SleepWhileInLoop")
        protected Boolean doInBackground() throws Exception {
            downloadNews();
            return true;
        }

        @Override
        protected void done() {
            jLabelLoadingFeed.setVisible(false);
        }

        @Override
        protected void process(List<Integer> chunks) {
            for (int i : chunks) {

            }
        }

    }

    private void testNews(int index, String cat) {
        try {
            stamp = new Timestamp(System.currentTimeMillis());
            timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
            jLabelLoadingFeed.setText("Loading News. Hold on!");
            newsReader.readFeed(cat);
            feedSize = mn.getArticleTitle().length;
            System.out.println("image: " + mn.getArticleImage()[index]);
            image = ImageIO.read(mn.getArticleImage()[index]);
            System.out.println(image);
            image = image.getScaledInstance(jPanelTop.getWidth(), jPanelTop.getHeight(), Image.SCALE_SMOOTH);
            jTextPaneArticleContents.setText(template(mn.getArticleBody()[index].trim()));
            jLabelArticleTitle.setText(mn.getArticleTitle()[index]);
            jLabelArticleDate.setText(mn.getArticleDate()[index]);
            jLabelFeedLink.setText(mn.getFeedLink());
            jPanelTop.repaint();
            this.revalidate();
            this.pack();

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void downloadNews() {
        Date date = new Date(stamp.getTime());
        System.out.println(date);
        InputStream in = null;
        try {
            in = new BufferedInputStream(mn.getArticleImage()[index].openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1 != (n = in.read(buf))) {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response1 = out.toByteArray();
            FileOutputStream fos = new FileOutputStream("images/" + timeStamp + ".jpg");
            fos.write(response1);
            fos.close();
            // Write article.
            BufferedWriter outfile = new BufferedWriter(new FileWriter("articles/" + timeStamp + ".html"));
            String article = jTextPaneArticleContents.getText();
            String newArticle = article.replaceAll("<p style=\"margin-top: 0\">", "<p style=\"margin-top: 0;font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">");
            outfile.write(newArticle);
            outfile.close();

        } catch (IOException ex) {
            System.out.println(ex);
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }

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

    private void archivesUI() {
        gp = new JDialog(thisJFrame());
        gp.setLayout(new BorderLayout());
        JPanelArchives jpgp = new JPanelArchives();
        ControllerArchives ca = new ControllerArchives(this, jpgp);
        gp.setSize(913, 500);
        System.out.println(jpgp.getWidth());
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
        System.out.println("loc (" + locx + " , " + locy + ")\nwidth: " + w + " height: " + h + " d h: " + gp.getHeight() + " d w: " + gp.getWidth());
        gp.setLocation((locx + w / 2) - (gp.getWidth() / 2), (locy + h / 2) - (gp.getHeight() / 2));
        gp.setUndecorated(true);
        gp.setOpacity((float) 1.0);
        gp.setVisible(true);
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
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanelHome = new javax.swing.JPanel();
        jButtonNext = new javax.swing.JButton();
        jButtonPrev = new javax.swing.JButton();
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
        jLabel4 = new javax.swing.JLabel();
        webSplitButtonMenu = new com.alee.extended.button.WebSplitButton();
        jPanel4 = new javax.swing.JPanel();
        jLabelArticleTitle = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabelArticleDate = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneArticleContents = new javax.swing.JTextPane();
        jLabelFeedLink = new javax.swing.JLabel();
        webComboBoxCat = new com.alee.laf.combobox.WebComboBox();
        jPanelBottom = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jLabelLoadingFeed = new javax.swing.JLabel();
        jLabelStatus = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();

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

        jButtonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/next.png"))); // NOI18N
        jButtonNext.setContentAreaFilled(false);
        jButtonNext.setFocusPainted(false);
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

        jButtonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/prev.png"))); // NOI18N
        jButtonPrev.setContentAreaFilled(false);
        jButtonPrev.setFocusPainted(false);
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

        jPanel6.setBackground(new java.awt.Color(255, 255, 255));
        jPanel6.setOpaque(false);

        jLabelTime.setFont(new java.awt.Font("Dotum", 0, 48)); // NOI18N
        jLabelTime.setForeground(new java.awt.Color(255, 255, 255));
        jLabelTime.setText("03:25");

        jLabel4.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Sep 12, 2014");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabelTime)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabelTime, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jLabel4)
                .addGap(0, 37, Short.MAX_VALUE))
        );

        webSplitButtonMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/menu_32.png"))); // NOI18N
        webSplitButtonMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                webSplitButtonMenuActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelTopLayout = new javax.swing.GroupLayout(jPanelTop);
        jPanelTop.setLayout(jPanelTopLayout);
        jPanelTopLayout.setHorizontalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelTopLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanelTopLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(webSplitButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelTopLayout.setVerticalGroup(
            jPanelTopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelTopLayout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(webSplitButtonMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabelImage, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));

        jLabelArticleTitle.setFont(new java.awt.Font("Ebrima", 0, 24)); // NOI18N
        jLabelArticleTitle.setForeground(new java.awt.Color(255, 102, 0));
        jLabelArticleTitle.setText("Trout, Strasburg Giving Playoffs a Fresh Look");

        jLabelArticleDate.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabelArticleDate.setForeground(new java.awt.Color(0, 153, 204));
        jLabelArticleDate.setText("Sep 12, 2014 8:30am");

        jScrollPane1.setBorder(null);

        jTextPaneArticleContents.setBorder(null);
        jTextPaneArticleContents.setContentType("text/html"); // NOI18N
        jTextPaneArticleContents.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jTextPaneArticleContents.setForeground(new java.awt.Color(51, 51, 51));
        jTextPaneArticleContents.setText("<html>\r\n  <head>\r\n  </head>\r\n  <body>\r\n<p style=\"font-family:Lao UI, Helvetica, Arial, Luxi Sans, sans-serif;color:#404040\">\ntest\n    </p>\r\n  </body>\r\n</html>\r\n");
        jScrollPane1.setViewportView(jTextPaneArticleContents);

        jLabelFeedLink.setFont(new java.awt.Font("Lao UI", 0, 12)); // NOI18N
        jLabelFeedLink.setForeground(new java.awt.Color(204, 0, 153));
        jLabelFeedLink.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabelFeedLink.setText("Sep 12, 2014 8:30am");

        webComboBoxCat.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " News Category", " Top Stories", " World Headlines", " US Headlines", " Politics Headlines", " The Blotter from Brian Ross", " Money Headlines", " Technology Headlines", " Health Headlines", " Entertainment Headlines", " Travel Headlines", " ESPN Sports", " World News Headlines", " 20/20 Headlines", " Primetime Headlines", " Nightline Headlines", " Good Morning America Headlines", " This Week Headlines " }));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 707, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabelArticleTitle, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 528, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabelArticleDate, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabelFeedLink, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(webComboBoxCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jLabelArticleTitle)
                .addGap(1, 1, 1)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(webComboBoxCat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelFeedLink, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabelArticleDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanelBottom.setBackground(new java.awt.Color(255, 255, 255));
        jPanelBottom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jPanelBottomMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jPanelBottomMouseExited(evt);
            }
        });

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/power_32.png"))); // NOI18N
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/download_32.png"))); // NOI18N
        jButton4.setBorderPainted(false);
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/lock_32.png"))); // NOI18N
        jButton5.setBorderPainted(false);
        jButton5.setContentAreaFilled(false);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/settings_32.png"))); // NOI18N
        jButton6.setBorderPainted(false);
        jButton6.setContentAreaFilled(false);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/archive_32.png"))); // NOI18N
        jButton8.setBorderPainted(false);
        jButton8.setContentAreaFilled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/delete_32.png"))); // NOI18N
        jButton9.setBorderPainted(false);
        jButton9.setContentAreaFilled(false);

        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/print_32.png"))); // NOI18N
        jButton10.setBorderPainted(false);
        jButton10.setContentAreaFilled(false);

        jLabelLoadingFeed.setFont(new java.awt.Font("Lao UI", 0, 10)); // NOI18N
        jLabelLoadingFeed.setForeground(new java.awt.Color(51, 51, 51));
        jLabelLoadingFeed.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/feed_loader.GIF"))); // NOI18N
        jLabelLoadingFeed.setText("Loading News. Hold On!");
        jLabelLoadingFeed.setIconTextGap(10);

        jLabelStatus.setFont(new java.awt.Font("Lao UI", 0, 10)); // NOI18N
        jLabelStatus.setForeground(new java.awt.Color(102, 102, 102));
        jLabelStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mynews/resources/offline.png"))); // NOI18N
        jLabelStatus.setText("[ Offline ]");
        jLabelStatus.setIconTextGap(0);

        javax.swing.GroupLayout jPanelBottomLayout = new javax.swing.GroupLayout(jPanelBottom);
        jPanelBottom.setLayout(jPanelBottomLayout);
        jPanelBottomLayout.setHorizontalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelBottomLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton4)
                .addGap(36, 36, 36)
                .addComponent(jLabelLoadingFeed, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jLabelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addContainerGap())
        );
        jPanelBottomLayout.setVerticalGroup(
            jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelBottomLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelLoadingFeed))
                    .addGroup(jPanelBottomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelStatus, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(1, 1, 1))
        );

        jSeparator2.setForeground(new java.awt.Color(153, 153, 153));

        javax.swing.GroupLayout jPanelHomeLayout = new javax.swing.GroupLayout(jPanelHome);
        jPanelHome.setLayout(jPanelHomeLayout);
        jPanelHomeLayout.setHorizontalGroup(
            jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelBottom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator2)
            .addGroup(jPanelHomeLayout.createSequentialGroup()
                .addGroup(jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanelTop, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanelHomeLayout.createSequentialGroup()
                        .addComponent(jButtonPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButtonNext, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanelHomeLayout.setVerticalGroup(
            jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelHomeLayout.createSequentialGroup()
                .addComponent(jPanelTop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanelHomeLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanelHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButtonPrev, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButtonNext, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(59, 59, 59))
                    .addGroup(jPanelHomeLayout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanelBottom, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHome, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        // TODO add your handling code here:
        jLabelLoadingFeed.setVisible(true);
        if (index >= feedSize) {
            index = 0;
        }
        //loadImage(images[index]);
        new LoadFeed(newsCat).execute();
        System.out.println("index: " + index);
        index++;
        //jPanelTop.repaint();


    }//GEN-LAST:event_jButtonNextActionPerformed

    private void jButtonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonPrevActionPerformed
        // TODO add your handling code here:
        jLabelLoadingFeed.setVisible(true);
        if (index < 0) {
            index = feedSize;
        }
        new LoadFeed(newsCat).execute();
        index--;
//        //jPanelTop.repaint();
//        this.revalidate();
//        this.pack();

    }//GEN-LAST:event_jButtonPrevActionPerformed

    private void jPanelBottomMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelBottomMouseEntered
        // TODO add your handling code here:
        jPanelBottom.setBackground(new Color(255, 140, 0));
    }//GEN-LAST:event_jPanelBottomMouseEntered

    private void jPanelBottomMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanelBottomMouseExited
        // TODO add your handling code here:
        jPanelBottom.setBackground(new Color(255, 255, 255));
    }//GEN-LAST:event_jPanelBottomMouseExited

    private void webSplitButtonMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_webSplitButtonMenuActionPerformed
        // TODO add your handling code here:
        final WebPopupMenu popupMenu = new WebPopupMenu();
        popupMenu.add(wmiNewNote);
        popupMenu.add(wmiTags);
        popupMenu.addSeparator();
        popupMenu.add(wmiAbout);
        popupMenu.add(wmiGreenPolicy);
        webSplitButtonMenu.setPopupMenu(popupMenu);
    }//GEN-LAST:event_webSplitButtonMenuActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        jLabelLoadingFeed.setText("Downloading News..");
        jLabelLoadingFeed.setVisible(true);
        new Download().execute();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        archivesUI();

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButtonPrevMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPrevMouseEntered
        // TODO add your handling code here:
        jButtonPrev.setOpaque(true);
        jButtonPrev.setBackground(new Color(0, 204, 255));
    }//GEN-LAST:event_jButtonPrevMouseEntered

    private void jButtonPrevMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonPrevMouseExited
        // TODO add your handling code here:
        jButtonPrev.setOpaque(false);
    }//GEN-LAST:event_jButtonPrevMouseExited

    private void jButtonNextMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNextMouseEntered
        // TODO add your handling code here:
        jButtonNext.setOpaque(true);
        jButtonNext.setBackground(new Color(0, 204, 255));
    }//GEN-LAST:event_jButtonNextMouseEntered

    private void jButtonNextMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButtonNextMouseExited
        // TODO add your handling code here:
        jButtonNext.setOpaque(false);
    }//GEN-LAST:event_jButtonNextMouseExited

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JFrameMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JFrameMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JFrameMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JFrameMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrameMainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JButton jButtonNext;
    private javax.swing.JButton jButtonPrev;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabelArticleDate;
    private javax.swing.JLabel jLabelArticleTitle;
    private javax.swing.JLabel jLabelFeedLink;
    private javax.swing.JLabel jLabelImage;
    private javax.swing.JLabel jLabelLoadingFeed;
    private javax.swing.JLabel jLabelStatus;
    private javax.swing.JLabel jLabelTime;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanelBottom;
    private javax.swing.JPanel jPanelHome;
    private javax.swing.JPanel jPanelTop;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextPane jTextPaneArticleContents;
    private com.alee.laf.combobox.WebComboBox webComboBoxCat;
    private com.alee.extended.button.WebSplitButton webSplitButtonMenu;
    // End of variables declaration//GEN-END:variables
}
