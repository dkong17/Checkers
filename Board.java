package checkers;

import java.util.ArrayList;
import java.util.Observable;
import static checkers.Side.*;

/* Holds all the data about our board.
 * @author: David Kong
 */
public class Board extends Observable {

	Board() {
		board = new Piece[8][8];
	}

	/* Get the value at r, c. */
	Piece get(int r, int c) {
		return board[r][c];
	}

	/* Evaluation of the game state. Positive for the BLACK player and negative for the RED.
	 * If the game is over, return +/- 10 (unreachable value) based on the winner. */
	int eval() {
		int count = 0;
		if (_exit == 1) {
			return -10;
		}
		if (_exit == 2)  {
			return 10;
		}
		for (Piece[] pa : board) {
			for (Piece p : pa) {
				if (p.getSide() == RED) {
					count -= 1;
				}
				else if (p.getSide() == BLACK) {
					count += 1;
				}
			}
		}
		return count;
	}

	Side getTurn() {
		return turn;
	}

	void setPlayers(Player player1, Player player2) {
		p1 = player1;
		p2 = player2;
	}

	/* Sets board to the initial state. */
	void newBoard() {
		playingBoard = true;
		turn = RED;
		_exit = -1;
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (r%2==c%2) {
					if (r < 3) {
						board[r][c] = new Piece(BLACK);
					}
					else if (r > 4) {
						board[r][c] = new Piece(RED);
					}
					else {
						board[r][c]	= new Piece(EMPTY);
					}
				}
				else {
					board[r][c] = new Piece(EMPTY);
				}
			}
		}
		getLegalMoves();
		announce();
	}

	boolean isLegalMove(Move move) {
		return legalMoves.contains(move);
	}

	boolean legalJump(Move move, Piece piece) {
		if (!piece.isKing()) {
			if (piece.getSide() == RED) {
				if (move.getEnd().getRow() >= move.getStart().getRow()) {
					return false;
				}
			}
			else if (move.getEnd().getRow() <= move.getStart().getRow()) {
				return false;
			}
		}
		if (move.outOfBounds()) {
			return false;
		}
		if (!move.isJump()) {
			return false;
		}
		if (board[move.getEnd().getRow()][move.getEnd().getCol()].getSide() != EMPTY) {
			return false;
		}
		int middleCol = (move.getEnd().getCol() + move.getStart().getCol()) / 2;
		int middleRow = (move.getEnd().getRow() + move.getStart().getRow()) / 2;
		if (board[middleRow][middleCol].getSide() != piece.getSide().opposite()) {
			return false;
		}

		return true;
	}

	boolean legalRegular(Move move, Piece piece) {
		if (!piece.isKing()) {
			if (piece.getSide() == RED) {
				if (move.getEnd().getRow() >= move.getStart().getRow()) {
					return false;
				}
			}
			else if (move.getEnd().getRow() <= move.getStart().getRow()) {
				return false;
			}
		}
		if (move.outOfBounds()) {
			return false;
		}
		if (!((Math.abs(move.getEnd().getRow() - move.getStart().getRow()) == 1) && 
			(Math.abs(move.getEnd().getCol() - move.getStart().getCol()) == 1))) {
			return false;
		}
		if (board[move.getEnd().getRow()][move.getEnd().getCol()].getSide() != EMPTY) {
			return false;
		}

		return true;
	}

	int addJumps(Coordinate start, Piece piece) {
		int added = 0;
		int r = start.getRow();
		int c = start.getCol();

		if (legalJump(new Move(start, r+2, c+2), board[r][c])) {
			legalMoves.add(new Move(start, r+2, c+2));
			added+=1;
		}
		if (legalJump(new Move(start, r-2, c+2), board[r][c])) {
			legalMoves.add(new Move(start, r-2, c+2));
			added+=1;
		}
		if (legalJump(new Move(start, r+2, c-2), board[r][c])) {
			legalMoves.add(new Move(start, r+2, c-2));
			added+=1;
		}
		if (legalJump(new Move(start, r-2, c-2), board[r][c])) {
			legalMoves.add(new Move(start, r-2, c-2));
			added+=1;
		}
		return added;
	}

	/* The list of legal moves should always be consistent with the most
	 * recent board state and TURN variable. */
	void getLegalMoves() {
		legalMoves.clear();

		// First check for any possible jumps
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c].getSide() == turn) {
					Coordinate start = new Coordinate(r, c);
					addJumps(start, board[r][c]);
				}
			}
		}

		// short circuit if there exist possible jumps.
		if (!legalMoves.isEmpty()) {
			return;
		}

		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				if (board[r][c].getSide() == turn) {
					Coordinate start = new Coordinate(r, c);
					if (legalRegular(new Move(start, r+1, c+1), board[r][c])) {
						legalMoves.add(new Move(start, r+1, c+1));
					}
					if (legalRegular(new Move(start, r+1, c-1), board[r][c])) {
						legalMoves.add(new Move(start, r+1, c-1));
					}
					if (legalRegular(new Move(start, r-1, c+1), board[r][c])) {
						legalMoves.add(new Move(start, r-1, c+1));
					}
					if (legalRegular(new Move(start, r-1, c-1), board[r][c])) {
						legalMoves.add(new Move(start, r-1, c-1));
					}
				}
			}
		}

		/* If the current player no longer has any valid moves, game is over
		 * and the other player has won. */
		if (legalMoves.isEmpty()) {
			gameWon();
		}
	}

	void doMove(Move move) {
		Coordinate s = move.getStart();
		Coordinate e = move.getEnd();
		Piece p = board[s.getRow()][s.getCol()];
		Piece emptyPiece = new Piece(EMPTY);
		board[e.getRow()][e.getCol()] = p;
		board[s.getRow()][s.getCol()] = emptyPiece;
		// If piece reaches the end of the board, it should become a king.
		if ((e.getRow() == 7 || e.getRow() == 0) && !(board[e.getRow()][e.getCol()].isKing())) {
			board[e.getRow()][e.getCol()].makeKing();
		}
		if (move.isJump()) {
			int middleCol = (e.getCol() + s.getCol()) / 2;
			int middleRow = (e.getRow() + s.getRow()) / 2;
			board[middleRow][middleCol].setEmpty();
			// After a jump, any potential continuing jumps need to be checked.
			legalMoves.clear();
			if (addJumps(e, board[e.getRow()][e.getCol()]) == 0 || 
				((e.getRow() == 7 || e.getRow() == 0) && !p.isKing())) {
				turn = turn.opposite();
				getLegalMoves();
			}
		}
		else {
			turn = turn.opposite();
			getLegalMoves();
		}
		announce();
	}

	/* Return a new Board instance after performing the MOVE on this game state. */
	Board generateSuccessor(Move m) {
		Board b = new Board();
		b.copy(this);
		b.doMove(m);
		return b;
	}

	/* Copy the pieces from another board to THIS. */
	void copy(Board b) {
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 8; c++) {
				this.board[r][c] = b.get(r, c).copy();
			}
		}
		this._exit = b.over();
		this.turn = b.getTurn();
		this.legalMoves = new ArrayList<Move>(b.getMoves());
	}

	void finishTurn() {
		if (!playingBoard) {
			return;
		}
		if (turn == RED) {
			p1.takeTurn();
		}
		else {
			p2.takeTurn();
		}
	}

	ArrayList<Move> getMoves() {
		return legalMoves;
	}

	void gameWon() {
		if (turn == BLACK) {
			_exit = 1;
			return;
		}
		_exit = 2;
	}

	int over() {
		return _exit;
	}

	private void announce() {
		setChanged();
		notifyObservers();
	}

	/* The board */
	private Piece[][] board;

	/* The players; p1 should always be RED side and p2 BLACK side */
	private Player p1, p2;

	/* Set of legal moves for a given state. */
	private ArrayList<Move> legalMoves = new ArrayList<Move>();

	/* Whose turn it is. */
	private Side turn;

	/* Exit is negative when game is playing; 1 indicates RED winner; 2 indicates BLACK winner */
	private int _exit;

	/* Signifies this is the board that the game is being played on. */
	private boolean playingBoard;
}
