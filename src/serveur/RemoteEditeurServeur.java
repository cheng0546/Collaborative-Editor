package serveur ;

import java.net.InetAddress;
import java.rmi.Remote;
import java.rmi.RemoteException ;
import java.util.ArrayList;

import client.CreateurDessin;

// interface qui d¨¦crit les services offerts par un proxy d'¨¦diteur(cot¨¦ serveur) :
// - quand un client demandera ¨¤ acc¨¦der ¨¤ distance ¨¤ un tel ¨¦diteur, il r¨¦cup¨¦rera un proxy de cet ¨¦diteur
// - il sera possible d'invoquer des m¨¦thodes sur ce proxy
// - les m¨¦thodes seront en fait ex¨¦cut¨¦es cot¨¦ serveur, sur le r¨¦f¨¦rent 

public interface RemoteEditeurServeur extends Remote {

   int getPortEmission (InetAddress clientAdress) throws RemoteException ;
   void answer (String question) throws RemoteException ;
   int getRMIPort () throws RemoteException ;
   RemoteDessinServeur addDessin (int x, int y, int w, int h, CreateurDessin createurDessin, int r, int g, int b) throws RemoteException ;
   ArrayList <RemoteDessinServeur> getDessinsPartages () throws RemoteException ;
   RemoteDessinServeur getDessin (String name) throws RemoteException ;

}
