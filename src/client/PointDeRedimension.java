package client;

import java.awt.Color;

import javax.swing.JPanel;

public class PointDeRedimension extends JPanel {
	
	int x, y;
	RedimensionListener redimensionListener;
	
	public PointDeRedimension (int x, int y) {
		
		setBackground(Color.BLACK);
		setBounds(x, y, 10, 10);
		redimensionListener = new RedimensionListener(this);
		addMouseListener(redimensionListener);
		addMouseMotionListener(redimensionListener);
	}
	
}
