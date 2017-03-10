/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package decisiontree;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author khaidzir
 */
public class TreeJPanel extends javax.swing.JPanel {

    static final int NODE_WIDTH = 80, NODE_HEIGHT = 25;
    static final int DIST_VER = 60, DIST_HOR = 40;
    static final int MARGIN_X = DIST_HOR, MARGIN_Y = DIST_VER;
    
    Color greenColor = new Color(0, 255, 0, 255);
    
    Node tree;
    ArrayList<JLabel> attributes;
    int treeLevel;
    int widthArea, heightArea;
    boolean treepainted;
    ArrayList<ArrayList<Point>> lines;
    
    /**
     * Creates new form TreeJPanel
     */
    public TreeJPanel() {
        tree = null;
        initComponents();
        this.setLayout(null);
        setBackground(Color.WHITE);
    }
    
    public void setTree(Node tree) {
        this.tree = tree;
    }
    
    public void reset() {
        treeLevel = 0;
        lines = new ArrayList<>();
        removeAll();
        if (tree != null) {
            treepainted = false;
            tree.calculateNLeaf();
            calculateTreeLevel(tree, 0);
            calculateArea();
            System.out.println("Jumlah daun : " + tree.nLeaf + ", level pohon : " + treeLevel +
                    ", widtharea : " + widthArea + ", heightarea : " + heightArea);            
            setPreferredSize(new Dimension(widthArea, heightArea));
            lines = new ArrayList<>();
        }
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tree != null) {
            if (!treepainted) {
                doDrawing();
            }
            drawLineEdges((Graphics2D)g);
        }
    }
    
    private void calculateTreeLevel(Node currNode, int level) {
        currNode.nodeLevel = level;
        if (currNode.isClass) {            
            if (treeLevel < (level+1)) treeLevel = level+1;
        } else {
            for(Edge e : currNode.edges) {
                calculateTreeLevel(e.nextNode, level+1);
            }
        }
    }
    
    private void doDrawing() {      
        drawTree();
        treepainted = true;
    }
    
    private void drawTree() {
        // Setup node and label
        tree.xCoorArea = MARGIN_X;
        tree.widthArea = widthArea - 2*MARGIN_X;
        JLabel rootLabel = new JLabel("<html><font color='red'>["+tree.id+"]</font> "+tree.attributeName+"</html>");
        add(rootLabel);
        setNodeLabel(tree, rootLabel, NODE_WIDTH, NODE_HEIGHT);
        
        // Make queue and add node and label
        Queue<Node> qNode = new LinkedList<>();
        Queue<JLabel> qLabel = new LinkedList<>();
        qNode.add(tree);
        qLabel.add(rootLabel);
        while(!qNode.isEmpty()) {
            Node n = qNode.poll();
            JLabel label = qLabel.poll();
            if (n.edges != null) {
                int lastX = n.xCoorArea;
                for(Edge e : n.edges) {
                    // Setup node and label
                    e.nextNode.xCoorArea = lastX;
                    e.nextNode.widthArea = ((e.nextNode.nLeaf*n.widthArea)/n.nLeaf);
                    lastX += e.nextNode.widthArea;
                    JLabel newLabel;
                    if (!e.nextNode.isClass) {
                        newLabel = new JLabel("<html><font color='red'>["+e.nextNode.id+"]</font> "
                                + e.nextNode.attributeName +"</html>");
                    } else {
                        newLabel = new JLabel(e.nextNode.attributeName);                        
                    }
                    add(newLabel);
                    setNodeLabel(e.nextNode, newLabel, NODE_WIDTH, NODE_HEIGHT); 
                    
                    // Add line desc.
                    ArrayList<Point> newLine = new ArrayList<>();
                    addNewLineEdge(label, newLabel, e.value, newLine);
                    
                    //Add to queue
                    qNode.add(e.nextNode);
                    qLabel.add(newLabel);
                }
            }
        }
    }
    
    private void calculateArea() {
        this.widthArea = tree.nLeaf*NODE_WIDTH + (tree.nLeaf-1)*DIST_HOR + 2*MARGIN_X;
        this.heightArea = treeLevel*NODE_HEIGHT + (treeLevel-1)*DIST_VER + 2*MARGIN_Y;
    }
    
    private void setNodeLabel(Node node, JLabel label, int width, int height) {
        label.setSize(width, height);
        label.setLocation( node.xCoorArea+(node.widthArea-label.getWidth())/2, 
                            MARGIN_Y + node.nodeLevel*(NODE_HEIGHT+DIST_VER) );
        label.setBorder(BorderFactory.createLineBorder(node.isClass? greenColor : Color.BLACK, 1));
        label.setHorizontalAlignment(JLabel.CENTER);
    }
    
    private void drawLineEdges(Graphics2D g2d) {
        for(ArrayList<Point> arrP : lines) {
            g2d.drawLine(arrP.get(0).x, arrP.get(0).y, arrP.get(1).x, arrP.get(1).y);
        }
    }

    private void addNewLineEdge(JLabel parentLabel, JLabel childLabel, String attrVal, ArrayList<Point> arrP) {
        int x1 = parentLabel.getX()+parentLabel.getWidth()/2;
        int y1 = parentLabel.getY()+parentLabel.getHeight();
        int x2 = childLabel.getX()+childLabel.getWidth()/2;
        int y2 = childLabel.getY();
        arrP.add(new Point(x1,y1));
        arrP.add(new Point(x2,y2));
        lines.add(arrP);
        
        //Add attribute value
        JLabel attrLabel = new JLabel(attrVal);
        add(attrLabel);
        attrLabel.setSize(NODE_WIDTH, NODE_HEIGHT);
        attrLabel.setLocation( ((x1+x2)/2) - attrLabel.getWidth()/2, 
                                (y1+y2)/2 - attrLabel.getHeight()/2 );
        attrLabel.setHorizontalAlignment(JLabel.CENTER);
        attrLabel.setForeground(Color.BLUE);
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
