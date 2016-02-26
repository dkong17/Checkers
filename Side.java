package checkers;

enum Side {
	RED, BLACK, EMPTY;

	/* Class should only be used to return the opposite side
	 * of a player. */
	Side opposite() {
		switch (this) {
			case RED:
				return BLACK;
			case BLACK:
				return RED;
			default:
				return EMPTY;
		}
	}

	boolean playableSquare(Side side) {
		return this == side;
	}
}