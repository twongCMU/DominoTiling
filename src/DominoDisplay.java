import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

public class DominoDisplay extends JDialog implements ActionListener{

    // JDialog requires this
    private static final long serialVersionUID = 1L;

    JFrame frameMain;
    JPanel panelMain;
    JPanel[] panelTiles;
    int _numTiles;
    JButton closeButton;

    public DominoDisplay(int numTiles) {
	int i = 0;

	_numTiles = numTiles;

	panelMain = new JPanel();
	panelMain.setLayout(null);
	panelMain.setLocation(0,0);
	panelMain.setSize(1200,100);

	panelTiles = new JPanel[numTiles];
	for (i = 0; i < numTiles; i++) {
	    panelTiles[i] = new JPanel();
	    panelTiles[i].setBackground(Color.yellow);
	    panelTiles[i].setLocation(30*i, 30);
	    panelTiles[i].setSize(30, 30);
	    panelTiles[i].setVisible(true);
	    panelTiles[i].setOpaque(true);
	    panelTiles[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
	    panelMain.add(panelTiles[i]);
	}
	closeButton = new JButton("Close");
	closeButton.addActionListener(this);
	closeButton.setVisible(true);
	closeButton.setLocation(30*(numTiles+1),30);
	closeButton.setSize(75,30);

	panelMain.add(closeButton);
	panelMain.setOpaque(true);
	panelMain.revalidate();
	panelMain.repaint();
	panelMain.setVisible(true);

	JFrame.setDefaultLookAndFeelDecorated(true);
	frameMain = new JFrame("Running");
	frameMain.setContentPane(panelMain);
	frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frameMain.setSize(1200,100);
        frameMain.setVisible(true);
    }

    public void updateDisplay(byte[] tileArray) {
	int i;
	for (i = 0; i < _numTiles; i++) {
	    if (tileArray[i] != Domino.TILE_EMPTY){ 
		panelTiles[i].setBackground(Color.black);
	    }
	}
	panelMain.revalidate();
	panelMain.repaint();
    }

    public void close(double occupyPct) {
	// auto-close is kind of annoying since we don't have good feedback that the
	// run is complete
	//	frameMain.dispose();
	frameMain.setTitle("DONE! Occupation rate: " + occupyPct);
    }

    public void actionPerformed(ActionEvent e) {
	frameMain.dispose();
    }
}
