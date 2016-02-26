package checkers;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observer;
import java.util.Observable;

import java.util.ArrayList;
import static checkers.Side.*;

/* GUI class that will actually display the board and also,
 * registers mouse clicks and sends them to the current game. 
 * @author David Kong
 */
public class BoardDisplay extends JPanel implements MouseListener, Observer {

	BoardDisplay() {
		mouse = true;
		setLayout(new GridLayout(8, 8, 0, 0));
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (r%2==c%2) {
					panels[r][c] = new Square(brown, r, c);
					this.add(panels[r][c]);
					panels[r][c].addMouseListener(this);
				}
				else {
					panels[r][c] = new Square(tan, r, c);
					this.add(panels[r][c]);
					panels[r][c].addMouseListener(this);
				}
			}
		}
		_board = new Board();
		_board.addObserver(this);
	}

	void newGame() {
		String[] options = {"1p vs 2p", "1p vs COM"};
		int n = JOptionPane.showOptionDialog(this, "Select Game Type", "New Game", JOptionPane.DEFAULT_OPTION,
												JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (n == 0) {
			_board.setPlayers(new HumanPlayer(_board, RED, this), new HumanPlayer(_board, BLACK, this));
		}
		else if (n == 1) {
            _board.setPlayers(new HumanPlayer(_board, RED, this), new AIPlayer(_board, BLACK, this));
		}
		_board.newBoard();
    }

	void gameOver(int end) {
		String message = "";
		if (end == 1) {
			message = "Player 1 Won!";
		}
		else if (end == 2) {
			message = "Player 2 Won!";
		}
		String[] options = {"Play Again", "Quit"};
		int n = JOptionPane.showOptionDialog(this, message, "Game Over", JOptionPane.DEFAULT_OPTION,
												JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (n == 0) {
			newGame();
		}
		else {
			System.exit(0);
		}
	}

	void setPieces() {
		for (int r = 0; r < 8; r++) {
			for (int c = r%2; c < 8; c+=2) {
				panels[r][c].setPiece(_board.get(r, c));
				panels[r][c].clear();
			}
		}
		paintImmediately(0, 0, this.getWidth(), this.getHeight());
	}

	@Override
	public void update(Observable obs, Object obj) {
        setPieces();
		if (_board.over() > 0) {
			gameOver(_board.over());
		}
        _board.finishTurn();
	}

	void doMove(Move m) {
		_board.doMove(m);
	}

	@Override
    public void mouseClicked(MouseEvent e) {
    	if (mouse) {
    		// If no square has been selected yet, set a mouse click on a valid
    		// square as the starting position
    		if (first_click == null) {
    			first_click = (Square) e.getSource();
    			Coordinate start = first_click.getCoordinate();
    			for (Move m : _board.getMoves()) {
    				if (m.getStart().equals(start)) {
    					potential.add(m);
    				}
    			}
    			if (!potential.isEmpty()) {
    				first_click.select();
    				first_click.repaint();
    				for (Move m : potential) {
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].setPlayable();
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].repaint();
    				}
    			}
    			else {
    				first_click = null;
    			}
    		}

    		// If the same square is clicked again, deselect the square.
    		else if (first_click.getCoordinate().equals(((Square) e.getSource()).getCoordinate())) {
    			first_click.clear();
    			first_click.repaint();
    			for (Move m : potential) {
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].clear();
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].repaint();
    				}
    			first_click = null;
    			potential.clear();
    		}

    		else {
    			Square second = (Square) e.getSource();
    			for (Move m : potential) {
    				if (m.getEnd().equals(second.getCoordinate())) {
    					doMove(m);
    					first_click = null;
    					potential.clear();
    					return;
    				}
    			}
    			ArrayList<Move> p2 = new ArrayList<Move>();
    			for (Move m : _board.getMoves()) {
    				if (m.getStart().equals(second.getCoordinate())) {
    					p2.add(m);
    				}
    			}
    			if (!p2.isEmpty()) {
    				for (Move m2 : potential) {
    					panels[m2.getEnd().getRow()][m2.getEnd().getCol()].clear();
    					panels[m2.getEnd().getRow()][m2.getEnd().getCol()].repaint();
    				}
    				first_click.clear();
    				first_click.repaint();
    				first_click = second;
    				first_click.select();
    				first_click.repaint();
    				for (Move m : p2) {
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].setPlayable();
    					panels[m.getEnd().getRow()][m.getEnd().getCol()].repaint();
    				}
    				potential = p2;
    			}
    		}
    	}
    }

    void enableMouse() {
    	mouse = true;
    }

    void disableMouse() {
    	mouse = false;
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {}
    @Override
    public void mouseExited(MouseEvent arg0) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent arg0) {}

	Color brown = new Color(139,69,19);
	Color tan = new Color(255,228,181);
	private Square[][] panels = new Square[8][8];
	/* The game we are playing */
	private Board _board;
	/* Square corresponding to the first click of a move */
	private Square first_click = null;

	ArrayList<Move> potential = new ArrayList<Move>();
	/* Turn the mouse "on and off" to make sure no errant mouse clicks
	 * whilst AI take a turn affects the game. */
	private boolean mouse;
}