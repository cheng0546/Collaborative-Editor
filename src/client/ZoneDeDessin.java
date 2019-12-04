package client;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import client.EditeurClient;
import client.CreateurDessin;

//---------------------------------------------------------------------------
// ZoneDeDessin : version simplifi¨¦e de ce qui a ¨¦t¨¦ fait en TP ¨¦diteur de dessin

public class ZoneDeDessin extends JPanel {

	private static final long serialVersionUID = 4588428293101431953L ;
	private EditeurClient editeur ; 
	private DessinListener dessinListener;	

	//-----------------------------------------------------------------------
	// le constructeur :
	// - il instancie et positionne les listeners de cr¨¦ations de dessins
	// - il supprime le layout manager pour g¨¦rer lui meme les dessins
	//-----------------------------------------------------------------------

	public ZoneDeDessin (EditeurClient editeur) {
		this.editeur = editeur ;
		dessinListener = new DessinListener () ;
		addMouseListener (dessinListener) ;
		addMouseMotionListener (dessinListener) ;
		setPreferredSize (new Dimension (300, 200)) ;
		setForeground (Color.black) ;
		setBackground (Color.white) ;
		setLayout (null) ;
		setCursor (Cursor.getPredefinedCursor (Cursor.CROSSHAIR_CURSOR)) ;
		
		//---------------------------------------------------------------------------------------------
		
		// Creation de menu bar
		JPanel menu = new JPanel();
		menu.setLayout(new GridLayout(1, 3, 10, 10));
		editeur.getContentPane().add(menu, BorderLayout.NORTH);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setPreferredSize(new Dimension(300,50));
		menuBar.setBackground(Color.WHITE);
		menu.add(menuBar);
		
		// Creation de menu de type
		
		JMenu shapeMenu = new JMenu("Type");
		shapeMenu.setPreferredSize(new Dimension(200,50));
		menuBar.add(shapeMenu);
		JMenuItem mnRectangle = new JMenuItem("Rectangle");
		mnRectangle.setPreferredSize(new Dimension(200,50));
		addShapeActionListenerForMenu(mnRectangle, new CreateurRectangle());
		shapeMenu.add(mnRectangle);
		JMenuItem mnElipse = new JMenuItem("Elipse");
		mnElipse.setPreferredSize(new Dimension(200,50));
		addShapeActionListenerForMenu(mnElipse, new CreateurElipse());
		shapeMenu.add(mnElipse);
		JMenuItem mnRectangleCreux = new JMenuItem("Rectangle creux");
		mnRectangleCreux.setPreferredSize(new Dimension(200,50));
		addShapeActionListenerForMenu(mnRectangleCreux, new CreateurRectangleCreux());
		shapeMenu.add(mnRectangleCreux);
		JMenuItem mnElipseCreuse = new JMenuItem("Elipse creuse");
		mnElipseCreuse.setPreferredSize(new Dimension(200,50));
		addShapeActionListenerForMenu(mnElipseCreuse, new CreateurElipseCreuse());
		shapeMenu.add(mnElipseCreuse);
		
		// Creation de menu de couleur
		
		JMenu colorMenu = new JMenu("Couleur");
		colorMenu.setPreferredSize(new Dimension(200,50));
		menuBar.add(colorMenu);
		JMenuItem mnYellow = new JMenuItem("Jaune");
		mnYellow.setPreferredSize(new Dimension(200,50));
		addColorActionListenerForMenu(mnYellow, 255, 255, 0);
		colorMenu.add(mnYellow);
		JMenuItem mnBlue = new JMenuItem("Bleu");
		mnBlue.setPreferredSize(new Dimension(200,50));
		addColorActionListenerForMenu(mnBlue, 0, 0, 255);
		colorMenu.add(mnBlue);
		JMenuItem mnGreen = new JMenuItem("Vert");
		mnGreen.setPreferredSize(new Dimension(200,50));
		addColorActionListenerForMenu(mnGreen, 0, 255, 0);
		colorMenu.add(mnGreen);
		JMenuItem mnRed = new JMenuItem("Rough");
		mnRed.setPreferredSize(new Dimension(200,50));
		addColorActionListenerForMenu(mnRed, 255, 0, 0);
		colorMenu.add(mnRed);
		JMenuItem mnOther = new JMenuItem("Autre");
		mnOther.setPreferredSize(new Dimension(200,50));
				mnOther.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Color selectedColor = JColorChooser.showDialog(editeur, "Others color", null);
						if (selectedColor == null) {
		                    return;
		                }
						int r = selectedColor.getRed();
		                int g = selectedColor.getGreen();
		                int b = selectedColor.getBlue();
		                setDessinColor(r, g, b);
					}
				});
		colorMenu.add(mnOther);
		
		// Creation de boutons de type de dessin
		
		JPanel btnGroup = new JPanel();
		btnGroup.setLayout(new GridLayout(4, 1, 10, 5));
		editeur.getContentPane().add(btnGroup, BorderLayout.WEST);
		
		JButton btnRectangle= new JButton("Rectangle");
		addShapeActionListenerForButton(btnRectangle, new CreateurRectangle());
		btnGroup.add(btnRectangle);
		JButton btnElipse= new JButton("Elipse");
		addShapeActionListenerForButton(btnElipse, new CreateurElipse());
		btnGroup.add(btnElipse);
		JButton btnRectangleCreux= new JButton("Rectangle Creux");
		addShapeActionListenerForButton(btnRectangleCreux, new CreateurRectangleCreux());
		btnGroup.add(btnRectangleCreux);
		JButton btnElipseCreuse= new JButton("Elipse Creuse");
		addShapeActionListenerForButton(btnElipseCreuse, new CreateurElipseCreuse());
		btnGroup.add(btnElipseCreuse);
		
		// Creation de boutons de couleur
		
		JPanel colorGroup = new JPanel();
		colorGroup.setLayout(new GridLayout(5, 1, 10, 5));
		editeur.getContentPane().add(colorGroup, BorderLayout.EAST);
		
		JButton btnYellow= new JButton("Jaune");
		addColorActionListenerForButton(btnYellow, 255, 255, 0);
		colorGroup.add(btnYellow);
		JButton btnBlue= new JButton("Bleu");
		addColorActionListenerForButton(btnBlue, 0, 0, 255);
		colorGroup.add(btnBlue);
		JButton btnGreen= new JButton("Vert");
		addColorActionListenerForButton(btnGreen, 0, 255, 0);
		colorGroup.add(btnGreen);
		JButton btnRed= new JButton("Rough");
		addColorActionListenerForButton(btnRed, 255, 0, 0);
		colorGroup.add(btnRed);
		JButton btnOther= new JButton("Autre");
		btnOther.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Color selectedColor = JColorChooser.showDialog(editeur, "Others color", null);
				if (selectedColor == null) {
                    return;
                }
				int r = selectedColor.getRed();
                int g = selectedColor.getGreen();
                int b = selectedColor.getBlue();
                setDessinColor(r, g, b);
			}
		});
		colorGroup.add(btnOther);
		
		//---------------------------------------------------------------------------------------------
		
	}

	//-----------------------------------------------------------------------
	// la m¨¦thode de d¨¦termination du dessin ¨¤ r¨¦aliser
	//-----------------------------------------------------------------------
	
	public void addShapeActionListenerForButton (JButton bouton, CreateurDessin createurDessin) {
		bouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setDessinType(createurDessin);
			}
		});
	}
	
	public void addColorActionListenerForButton (JButton bouton, int r, int g, int b) {
		bouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setDessinColor(r, g, b);
			}
		});
	}
	
	public void addShapeActionListenerForMenu (JMenuItem menu, CreateurDessin createurDessin) {
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setDessinType(createurDessin);
			}
		});
	}
	
	public void addColorActionListenerForMenu (JMenuItem menu, int r, int g, int b) {
		menu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setDessinColor(r, g, b);
			}
		});
	}
	
	//Transmettre le CreateurDessin au MouseListener et au MouseMotionListener
	
	public void setDessinType (CreateurDessin createurDessin) {
		dessinListener.saveDessinType(createurDessin);
	}
	
	//Transmettre la couleur au MouseListener et au MouseMotionListener
	
	public void setDessinColor (int r, int g, int b) {
		dessinListener.saveDessinColor(r, g, b);
	}
	
	//-----------------------------------------------------------------------
	// les listeners g¨¦n¨¦riques de cr¨¦ation de dessins
	//-----------------------------------------------------------------------

	class DessinListener implements MouseListener, MouseMotionListener {

		Point debut, fin ;
		DessinClient dessinEnCours ;
		CreateurDessin createurDessin = new CreateurRectangle();
		DessinClient dessin ;
		int r = 0, g = 0, b = 0;
		
		public void saveDessinType (CreateurDessin createurDessin) {
			this.createurDessin = createurDessin;
		}
		
		public void saveDessinColor (int r, int g, int b) {
			this.r = r;
			this.g = g;
			this.b = b;
		}

		@Override
		public void mouseEntered (MouseEvent e) {
		}

		@Override
		public void mouseExited (MouseEvent e) {
		}

		@Override
		public void mousePressed (MouseEvent e) {
			this.debut = e.getPoint () ;
			fin = e.getPoint () ;
			// cr¨¦ation d'un dessin via l'¨¦diteur local : il fera les appels n¨¦c¨¦ssaires au seveur distant 
			dessinEnCours = editeur.creerDessin (debut.x, debut.y, 0, 0, this.createurDessin, this.r, this.g, this.b) ;
			dessinEnCours.setOpaque(false);
			// ajout de le "pr¨¦sentation" du dessin
			add (dessinEnCours, 0) ;
			// mise ¨¤ jour des limites du dessin courant, ne sert pas ¨¤ grand chose puisque w et h valent 0
			dessinEnCours.setProxyBounds (debut.x, debut.y, 0, 0) ;
		}

		@Override
		public void mouseReleased (MouseEvent e) {
			this.fin = e.getPoint () ;
			PointDeRedimension hautGauche = new PointDeRedimension(this.debut.x-10, this.debut.y-10);
			dessinEnCours.add(hautGauche, 0);
			PointDeRedimension hautDroite = new PointDeRedimension(this.fin.x+10, this.debut.y-10);
			dessinEnCours.add(hautDroite, 0);
			PointDeRedimension basGauche = new PointDeRedimension(this.debut.x-10, this.fin.y+10);
			dessinEnCours.add(basGauche, 0);
			PointDeRedimension basDroite = new PointDeRedimension(this.fin.x+10, this.fin.y+10);
			dessinEnCours.add(basDroite, 0);
			System.out.println(basDroite);
		}

		@Override
		public void mouseDragged (MouseEvent e) {
			//System.out.println ("mouseDragged dans ZoneDeDessin") ;
			fin = e.getPoint () ;
			if (dessinEnCours != null) {
				// mise ¨¤ jour des limites du dessin via une m¨¦thode sp¨¦cifiquequi fera ¨¦galement une mise ¨¤ jour sur le serveur
				dessinEnCours.setProxyBounds (Math.min (debut.x, fin.x),
											  Math.min (debut.y, fin.y),
											  Math.abs (fin.x - debut.x),
											  Math.abs (fin.y - debut.y)) ;
			}
		}

		@Override
		public void mouseMoved (MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

	}

}

//---------------------------------------------------------------------------


