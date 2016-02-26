package checkers;

import javax.swing.*;
import javax.swing.AbstractAction;
import java.awt.event.*;
import java.awt.*;

/* Top level Display for the checkers game. The actual board components
 * are in the BoardDisplay class.
 * @author David Kong
 */

 public class Main extends JFrame {
 	
 	public Main() {
        super("Checkers");
    }
     
    public static void main(String[] args) {
        Main frame = new Main();
        final BoardDisplay board = new BoardDisplay();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        menuBar.add(menu);
        JMenuItem item = new JMenuItem(new AbstractAction("New Game") {
            public void actionPerformed(ActionEvent e) {
                board.newGame();
            }
        });
        menu.add(item);
        item = new JMenuItem(new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menu.add(item);
        frame.setJMenuBar(menuBar);
        frame.setBounds(500, 300, 400, 425);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(board);
        frame.setResizable(true);
        frame.setVisible(true);
        board.newGame();
    }
}