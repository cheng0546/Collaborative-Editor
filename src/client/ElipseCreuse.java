package client;

import java.awt.Color;
import java.awt.Graphics;

import serveur.RemoteDessinServeur;

public class ElipseCreuse extends DessinClient {
	private static final long serialVersionUID = 1L ;
	
	DeplacementListener deplacementListener ;
	RemoteDessinServeur proxy ;
	
	int r, g, b;
	
	ElipseCreuse (RemoteDessinServeur proxy, int r, int g, int b) {
		super(proxy);
		this.r = r;
		this.g = g;
		this.b = b;
	}
	
	@Override
	public void paint (Graphics g) {
		super.paint(g);
		g.setColor(new Color(this.r, this.g, this.b));
		g.drawOval(0, 0, getWidth()-1, getHeight()-1);
	}

}
