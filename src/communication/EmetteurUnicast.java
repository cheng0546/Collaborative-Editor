package communication;

import java.io.ByteArrayOutputStream ;
import java.io.IOException;
import java.io.ObjectOutputStream ;
import java.io.Serializable ;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.HashMap ;

//---------------------------------------------------------------------
//classe permettant d'envoyer des messages diffus¨¦s ¨¤ une adresse (en multicast)

public class EmetteurUnicast implements Serializable {

	private static final long serialVersionUID = 1L;
	
	// le port de diffusion
	private int portEmission ;
	
	// l'adresse syst¨¨me du groupe de diffusion
	private InetAddress adresseEmission ;
	
	// la socket de diffusion
	private transient DatagramSocket socketEmission ;

	// le constructeur du diffuseur ¨¤ partir du nom de groupe et du port de diffusion 
	public EmetteurUnicast (final InetAddress adresseDestinataire, final int portEmission) throws RemoteException {
		this.portEmission = portEmission ;
		System.out.println ("Emetteur sur le port " + portEmission + " a destination du client " + adresseDestinataire) ;
		adresseEmission = adresseDestinataire ;
		socketEmission = null ;
		try {
			// cr¨¦ation d'une socket d'envoi de message, l'adresse du destinataire est indiqu¨¦e seulement au moment de l'envoi
			socketEmission = new DatagramSocket () ;
		} catch (IOException e) {
			e.printStackTrace () ;
		}
		System.out.println ("socket emission : " + socketEmission.getLocalPort() + " " + adresseEmission) ;
	}

	// m¨¦thode envoyant un message de haut niveau en le traduisant en flux bas niveau
	public void diffuseMessage (String command, String name, HashMap<String, Object> hm) {
		// d¨¦claration du flux de bas niveau
		ByteArrayOutputStream baos = new ByteArrayOutputStream () ;
		ObjectOutputStream oos ;
		try {
			// association d'un flux de plus haut niveau ¨¤ ce flux de bas niveau
			oos = new ObjectOutputStream (baos) ;
			// ¨¦criture des objets, au format ad¨¦quat, dans le flux : il faudra les relire dans le meme ordre !!!
			oos.writeObject (command) ;
			oos.writeObject (name) ;
			oos.writeObject (hm) ;
			oos.flush () ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		// cr¨¦ation d'un paquet d'information ¨¤ envoyer, ¨¤ partir du flux de bas niveau
		DatagramPacket paquet = new DatagramPacket (baos.toByteArray (), baos.toByteArray ().length, adresseEmission, portEmission) ;
		try {
			// envoi effectif du message
			socketEmission.send (paquet) ;
		} catch (IOException e) {
			e.printStackTrace () ;
		}
	}

	public int getPortEmission () throws RemoteException {
		return (portEmission) ;
	}

}
