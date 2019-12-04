package communication;


import java.io.ByteArrayInputStream ;
import java.io.ObjectInputStream ;
import java.net.DatagramPacket ;
import java.net.DatagramSocket;
import java.net.InetAddress ;
import java.util.HashMap ;

import client.CreateurDessin;
import client.EditeurClient ;

//---------------------------------------------------------------------
// classe permettant de recevoir des messages diffus¨¦s ¨¤ une adresse (en multicast)

public class RecepteurUnicast extends Thread implements Runnable {

	// la socket sur laquelle on va lire les messages
	private transient DatagramSocket socketReception ;

	// l'¨¦diteur local qu'on pr¨¦viendra suite aux messages recus
	private EditeurClient clientLocal ;
	public void setClientLocal (EditeurClient clientLocal) {
		this.clientLocal = clientLocal ;
	}

	// les donn¨¦es ¨¤ r¨¦cup¨¦rer conform¨¦ment au format des donn¨¦es envoy¨¦es :
	// - une chaine de caract¨¨re pour d¨¦crire l'action ¨¤ r¨¦aliser
	private String command = new String () ;
	// - une chaine de caract¨¨re pour d¨¦terminer l'objet coble du message
	private String name = new String () ;
	// - une HashMap dans laquelle on r¨¦cup¨¨rera les param¨¨tres n¨¦cessaires aux actions ¨¤ effectuer
	private HashMap<String, Object> hm = new HashMap<String, Object> () ;

	public RecepteurUnicast (final InetAddress adresseReception, final int portReception) {
		socketReception = null ;
		try {
			// cr¨¦ation d'une socket de r¨¦ception des messages adress¨¦s ¨¤ ce groupe de diffusion
			socketReception = new DatagramSocket (portReception, adresseReception) ;
			System.out.println ("socket r¨¦ception : " + socketReception.getLocalPort() + " " + socketReception.getInetAddress ()) ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}

	// m¨¦thode de r¨¦ception du message : on extrait d'un flux de tr¨¨s bas niveau des informations format¨¦es correctement 
	@SuppressWarnings ("unchecked")
	public void recevoir () {
		try {
			// r¨¦ception d'un paquet bas niveau
			byte [] message = new byte [1024] ;
			DatagramPacket paquet = new DatagramPacket (message, message.length) ;
			socketReception.receive (paquet) ;
			// extraction des informations au format type ¨¤ partir du paquet
			ByteArrayInputStream bais = new ByteArrayInputStream (paquet.getData ()) ;
			ObjectInputStream ois = new ObjectInputStream (bais) ;
			command = (String)ois.readObject () ;
			name = (String)ois.readObject () ;
			hm = (HashMap<String, Object>)ois.readObject () ;
		} catch (Exception e) {
			e.printStackTrace () ;
		}
	}

	// m¨¦thode qui permet de recevoir des messages en parral¨¨le des interactions utilisateur
	public void run () {
		while (true) {
			// r¨¦ception effective du message et d¨¦codage dans command, name et hm
			recevoir () ;
			// traitement du message en fonction de son intitul¨¦
			if (command.equals ("Bounds")) {
				//System.out.println ("received Bounds") ;
				// mise ¨¤ jour des limites d'un dessin
				clientLocal.objectUpdateBounds(name, (int)hm.get ("x"), (int)hm.get ("y"), (int)hm.get ("w"), (int)hm.get ("h"));
			} else if (command.equals ("Location")) {
				//System.out.println ("received Location") ;
				// mise ¨¤ jour de la positiuon d'un dessin
				clientLocal.objectUpdateLocation (name, (int)hm.get ("x"), (int)hm.get ("y"));
			} else if (command.equals ("Dessin")) {
				//System.out.println ("received Dessin") ;
				// ajout d'un dessin
				clientLocal.ajouterDessin (name, (int)hm.get ("x"), (int)hm.get ("y"), (int)hm.get ("w"), (int)hm.get ("h"), (CreateurDessin)hm.get ("createurDessin"), (int)hm.get ("r"), (int)hm.get ("g"), (int)hm.get ("b"));
			}
		}
	}

}

//---------------------------------------------------------------------
