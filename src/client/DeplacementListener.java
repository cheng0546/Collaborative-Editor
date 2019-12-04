package client ;

import java.awt.Cursor;
import java.awt.Point ;
import java.awt.event.MouseEvent ;
import java.awt.event.MouseListener ;
import java.awt.event.MouseMotionListener ;

//---------------------------------------------------------------------------

class DeplacementListener implements MouseListener, MouseMotionListener {

   Point offset ;
   DessinClient dessin ;

   public DeplacementListener (DessinClient d) {
      dessin = d ;
   }

   public void mouseClicked (MouseEvent e) {
   }

   public void mouseEntered (MouseEvent e) {
   }

   public void mouseExited (MouseEvent e) {
   }

   public void mousePressed (MouseEvent e) {
      offset = e.getPoint () ;
   }

   public void mouseReleased (MouseEvent e) {
   }

   public void mouseDragged (MouseEvent e) {
      dessin.setProxyLocation (dessin.getX () + e.getX () - offset.x, dessin.getY () + e.getY () - offset.y) ;
   }

   public void mouseMoved (MouseEvent e) {
   }

}

//---------------------------------------------------------------------------
