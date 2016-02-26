package checkers;

/** A Player that gets its moves from manual input.
 *  @author David Kong
 */
class HumanPlayer extends Player {

    /** A new player initially playing COLOR taking manual input of
     *  moves from GAME's input source. */
    HumanPlayer(Board game, Side color, BoardDisplay gui) {
        super(game, color);
        _gui = gui;
    }

    @Override
    /* The human player has to get manual input from the GUI, so we
     * ask the GUI to make the move for us */
    void takeTurn() {
        _gui.enableMouse();
    }

    private BoardDisplay _gui;

}
