package gameClient.gui;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;
import gameClient.Arena;
import gameClient.CL_Agent;
import gameClient.CL_Pokemon;
import gameClient.util.Point3D;
import gameClient.util.Range;
import gameClient.util.Range2D;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class represents a very simple GUI class to present a
 * game on a graph - you are welcome to use this class - yet keep in mind
 * that the code is not well written in order to force you improve the
 * code and not to take it "as is".
 */
public class MyFrame extends JFrame {
    private Image dbImage;
    private Graphics dbg;
    private int _ind;
    private Arena _ar;
    private gameClient.util.Range2Range _w2f;
    int grade;

    public MyFrame(String title) {
        super(title);
        int _ind = 0;
    }

    public void update(Arena ar) {
        this._ar = ar;
        updateFrame();
    }

    private void updateFrame() {
        Range rx = new Range(20, this.getWidth() - 20);
        Range ry = new Range(this.getHeight() - 10, 150);
        Range2D frame = new Range2D(rx, ry);
        directed_weighted_graph g = _ar.getGraph();
        _w2f = Arena.w2f(g, frame);
    }

    /**
     * Create double buffering.
     * According to:
     * https://www.youtube.com/watch?v=4T3WJEH7zrc
     *
     * @param g
     */
    public void paint(Graphics g) {
        dbImage = createImage(getWidth(), getHeight());
        dbg = dbImage.getGraphics();
        paintComponent(dbg);
        g.drawImage(dbImage, 0, 0, this);

    }

    public void paintComponent(Graphics g) {
        int w = this.getWidth();
        int h = this.getHeight();
        g.clearRect(0, 0, w, h);
        //	updateFrame();
        drawPokemons(g);
        drawGraph(g);
        drawAgants(g);
        drawInfo(g);

    }

    private void drawInfo(Graphics g ) {
        List<String> str = _ar.get_info();
        g.setColor(Color.black);
        for (int i = 0; i < str.size(); i++) {
            g.drawString(str.get(i), 100, 60 + i * 20);
        }
        g.setFont(new Font("MV Boli", Font.PLAIN, 20));
        // g.drawString(""+_ar.getTimeToEnd(),this.getWidth()-200 , 60);
        g.drawString("" + _ar.getTimeToEnd() / 1000 / 60 + ":" + _ar.getTimeToEnd() / 1000 % 60 + ":" + _ar.getTimeToEnd() / 10 % 100, this.getWidth() - 200, 60);
        g.drawString("moves: " + _ar.getMovesSoFar(), this.getWidth() - 200, 100);
        g.drawString("grade: " + grade, this.getWidth() - 200, 140);


    }

    private void drawGraph(Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        Iterator<node_data> iter = gg.getV().iterator();
        while (iter.hasNext()) {
            node_data n = iter.next();
            g.setColor(Color.blue);
            drawNode(n, 5, g);
            Iterator<edge_data> itr = gg.getE(n.getKey()).iterator();
            while (itr.hasNext()) {
                edge_data e = itr.next();
                g.setColor(Color.gray);
                drawEdge(e, g);
            }
        }
    }

    private void drawPokemons(Graphics g) {

        List<CL_Pokemon> fs = _ar.getPokemons();
        if (fs != null) {
            Iterator<CL_Pokemon> itr = fs.iterator();

            while (itr.hasNext()) {

                CL_Pokemon f = itr.next();
                Point3D c = f.getLocation();
                int r = 10;
                g.setColor(Color.green);
                if (f.getType() < 0) {
                    g.setColor(Color.orange);
                }
                if (c != null) {

                    geo_location fp = this._w2f.world2frame(c);
                    g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                    //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);

                }
            }
        }
    }

    private void drawAgants(Graphics g) {
        grade = 0;
        List<String> infoGame = new ArrayList<>();

        List<CL_Agent> rs = _ar.getAgents();
        //	Iterator<OOP_Point3D> itr = rs.iterator();
        g.setColor(Color.red);
        int i = 0;
        while (rs != null && i < rs.size()) {
            geo_location c = rs.get(i).getLocation();
            int r = 8;
            i++;
            if (c != null) {

                geo_location fp = this._w2f.world2frame(c);
                g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
                g.drawString("" + i, (int) fp.x(), (int) fp.y() - 4 * r);
                grade += rs.get(i - 1).getValue();
                infoGame.add("Agent " + i + ": value: " + Double.toString(rs.get(i - 1).getValue()));

            }
        }
        _ar.set_info(infoGame);

    }

    private void drawNode(node_data n, int r, Graphics g) {
        geo_location pos = n.getLocation();
        geo_location fp = this._w2f.world2frame(pos);
        g.fillOval((int) fp.x() - r, (int) fp.y() - r, 2 * r, 2 * r);
        g.drawString("" + n.getKey(), (int) fp.x(), (int) fp.y() - 4 * r);
    }

    private void drawEdge(edge_data e, Graphics g) {
        directed_weighted_graph gg = _ar.getGraph();
        geo_location s = gg.getNode(e.getSrc()).getLocation();
        geo_location d = gg.getNode(e.getDest()).getLocation();
        geo_location s0 = this._w2f.world2frame(s);
        geo_location d0 = this._w2f.world2frame(d);
        g.drawLine((int) s0.x(), (int) s0.y(), (int) d0.x(), (int) d0.y());
        //	g.drawString(""+n.getKey(), fp.ix(), fp.iy()-4*r);
    }
}