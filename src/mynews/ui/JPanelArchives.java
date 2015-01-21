/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mynews.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import mynews.utils.CustomScrollbarUI;

/**
 *
 * @author RESEARCH2
 */
public class JPanelArchives extends javax.swing.JPanel {
    private final GridBagConstraints gbc = new GridBagConstraints();

    /**
     * Creates new form JPanelArchives
     */
    public JPanelArchives() {
        initComponents();
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.BOTH;
        jScrollPane1.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        jScrollPane1.getVerticalScrollBar().setUI(new CustomScrollbarUI(Color.WHITE));
    }

    public JPanel getjPanelHome() {
        return jPanelHome;
    }

    public void setjPanelHome(JPanel jPanelHome) {
        this.jPanelHome = jPanelHome;
    }
    
    public void addGB(Container cont, Component comp, int x, int y) {
        if (!(cont.getLayout() instanceof GridBagLayout)) {
            cont.setLayout(new GridBagLayout());
        }
        gbc.gridx = x;
        gbc.gridy = y;
        cont.add(comp, gbc);
        this.revalidate();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jPanelHome = new javax.swing.JPanel();

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        jPanelHome.setLayout(new java.awt.GridBagLayout());
        jScrollPane1.setViewportView(jPanelHome);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 752, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanelHome;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}