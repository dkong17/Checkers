package checkers;

import static checkers.Side.*;

public class Piece {
	/* Piece has a side and may be a king. */
	private Side _side;
	private boolean king;

	Piece(Side side) {
		_side = side;
		king = false;
	}

	Piece copy() {
		Piece p = new Piece(_side);
		p.king = this.king;
		return p;
	}

	void makeKing() {
		king = true;
	}

	void makeNormal() {
		king = false;
	}

	Side getSide() {
		return _side;
	}

	void setEmpty() {
		_side = EMPTY;
	}

	void setSide(Side s) {
		_side = s;
	}

	boolean isKing() {
		return king;
	}
}