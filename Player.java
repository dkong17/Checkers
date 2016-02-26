package checkers;

abstract class Player {
	/* The side that I'm on */
	private final Side _side;
	/* The game that I'm in */
	private Board _game;

	Player(Board game, Side color) {
		_game = game;
		_side = color;
	}

	Board getBoard() {
		return _game;
	}

	final Side getSide() {
		return _side;
	}

	/* Ask the board to make a move for us. */
	abstract void takeTurn();
}