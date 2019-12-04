package client ;

import java.awt.Color ;
import java.rmi.RemoteException ;

import javax.swing.JPanel ;

import serveur.RemoteDessinServeur ;

//---------------------------------------------------------------------------

public class DessinClient extends JPanel {

   /**
    * 
    */
   private static final long serialVersionUID = 1L ;

   DeplacementListener deplacementListener ;
   RemoteDessinServeur proxy ;
   PointDeRedimension pointDeRedimension;

   
   //---------------------------------------------------------------------------

   // constructeur Dessin : param¨¦tr¨¦ par une instance de RemoteDessin permettant d'envoyer des informations au r¨¦f¨¦rent sur le serveur 
   public DessinClient (RemoteDessinServeur proxy) {
	   this.proxy = proxy ;
	   setOpaque (true) ;
	   //setBackground (Color.black) ;
	   deplacementListener = new DeplacementListener (this) ;
	   addMouseListener (deplacementListener) ;
	   addMouseMotionListener (deplacementListener) ;
   }
   
   // m¨¦thode permettant de "retailler" un Dessin :
   // - elle r¨¦alise un "setBounds" pour fournir un retour visuel imm¨¦diat
   // - ensuite elle propage le changement au r¨¦f¨¦rent, vie une remote invocation sur le proxy  
   public void setProxyBounds (int x, int y, int w, int h) {
	   setBounds (x, y, w, h) ;
	   try {
		   proxy.setBounds (x, y, w, h) ;
	   } catch (RemoteException e) {
		   e.printStackTrace();
	   }
   }

   // m¨¦thode permettant de d¨¦placer un Dessin :
   // - elle r¨¦alise un "setLocation" pour fournir un retour visuel imm¨¦diat
   // - ensuite elle propage le changement au r¨¦f¨¦rent, vie une remote invocation sur le proxy  
   public void setProxyLocation (int x, int y) {
	   setLocation (x, y) ;
	   try {
		   proxy.setLocation (x, y) ;
	   } catch (RemoteException e) {
		   e.printStackTrace();
	   }
   }

}

//---------------------------------------------------------------------------
