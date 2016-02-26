package checkers;

public class AIPlayer extends Player {

	AIPlayer(Board game, Side color, BoardDisplay gui) {
		super(game, color);
		_gui = gui;
	}

	@Override
	void takeTurn() {
		_gui.disableMouse();
		getBoard().doMove(getAction(getBoard(), 4));
	}

	Move getAction(Board gameState, int depth) {
		int best = Integer.MIN_VALUE;
		Move shouldDo = null;
		for (Move action : gameState.getMoves()) {
			int temp = minimizer(gameState.generateSuccessor(action), depth);
			if (temp > best) {
				best = temp;
				shouldDo = action;
			}
		}
		try {
	   		Thread.sleep(500);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		return shouldDo;
	}

	/* Maximizing layer of minimax. */
	private int maximizer(Board state, int depth) {
			// Terminal node
		if (depth == 0 || state.over() > 0) {
			return state.eval();
		}
		int best = Integer.MIN_VALUE;
		for (Move action : state.getMoves()) {
			best = Math.max(best, minimizer(state.generateSuccessor(action), depth));
		}
		return best;
	}

	/* Minimizing layer of minimax. */
	private int minimizer(Board state, int depth) {
		if (depth == 0 || state.over() > 0) {
			return state.eval();
		}
		int worst = Integer.MAX_VALUE;
		for (Move action : state.getMoves()) {
			worst = Math.min(worst, maximizer(state.generateSuccessor(action), depth - 1));
		}
		return worst;
	}

	private BoardDisplay _gui;
}