package serveur;

import java.rmi.Remote ;
import java.rmi.RemoteException ;

import client.CreateurDessin;

// interface qui d¨¦crit les services offerts par un proxy de Dessin (ex¨¦cut¨¦s cot¨¦ serveur, utilis¨¦s cot¨¦ client) :
// - quand un client demandera ¨¤ acc¨¦der ¨¤ distance ¨¤ un tel dessin, il r¨¦cup¨¦rera un proxy de ce dessin
// - il sera possible d'invoquer des m¨¦thodes sur ce proxy
// - les m¨¦thodes seront en fait ex¨¦cut¨¦es cot¨¦ serveur, sur le r¨¦f¨¦rent 

public interface RemoteDessinServeur extends Remote {

   void setLocation (int x, int y) throws RemoteException ;
   void setBounds (int x, int y, int w, int h) throws RemoteException ;
   String getName () throws RemoteException ;
   int getX () throws RemoteException ;
   int getY () throws RemoteException ;
   int getWidth () throws RemoteException ;
   int getHeight () throws RemoteException ;
   CreateurDessin getCreateurDessin () throws RemoteException ;
   int getRed () throws RemoteException ;
   int getGreen () throws RemoteException ;
   int getBlue () throws RemoteException ;

}
