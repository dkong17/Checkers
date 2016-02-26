package checkers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import static checkers.Side.*;

public class Square extends JPanel {

	public Square(Color color, int r, int c) {
		this.setBackground(color);
		x = r;
		y = c;
		piece = new Piece(EMPTY);
		playable = false;
		selected = false;
	}

	Coordinate getCoordinate() {
		return new Coordinate(x, y);
	}

	public void setPiece(Piece p) {
		piece = p;
	}

	public Piece getPiece() {
		return piece;
	}

	void setPlayable() {
		playable = true;
	}

	void select() {
		selected = true;
	}

	void clear() {
		playable = false;
		selected = false;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int h = this.getHeight();
		int w = this.getWidth();
		int diameter = Math.min(this.getWidth(), this.getHeight())-4;
        int[] x = {w/2 - diameter/4, w/2 - diameter/4, w/2 - diameter/8, w/2, w/2 + diameter/8, w/2 + diameter/4, w/2 + diameter/4};
        int[] y = {h/2 + diameter/4, h/2 - diameter/4, h/2, h/2 - diameter/4, h/2, h/2 - diameter/4, h/2 + diameter/4};

        if (piece.getSide() != EMPTY) {
        	if (piece.getSide() == RED) {
				g.setColor(Color.red);
			}
			else {
				g.setColor(Color.black);
			}
			g.fillOval(this.getWidth()/2 - diameter/2, this.getHeight()/2 - diameter/2, diameter, diameter);
			if (piece.isKing()) {
				g.setColor(gold);
				g.fillPolygon(x, y, 7);
			}
        }

        if (playable) {
        	BasicStroke stroke = new BasicStroke(5);
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setColor(Color.yellow);
  			g2.setStroke(stroke);
  			g2.drawRect(0, 0, this.getWidth(), this.getHeight());
        }

        if (selected) {
        	BasicStroke stroke = new BasicStroke(5);
        	Graphics2D g2 = (Graphics2D) g;
        	g2.setColor(Color.cyan);
  			g2.setStroke(stroke);
  			g2.drawRect(0, 0, this.getWidth(), this.getHeight());
        }
	}

	Color gold = new Color(255,215,0);
	private Piece piece;
	private boolean playable, selected;
	/* Column and Row corresponding to placement on board.*/
	private int x, y;
}
