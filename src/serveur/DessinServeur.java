package serveur ;

import java.io.Serializable ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject ;
import java.util.HashMap ;
import java.util.List;

import client.CreateurDessin;
import communication.EmetteurUnicast ;

// classe de Dessin pr¨¦sente sur le serveur :
// - pour pouvoir invoquer des m¨¦thodes ¨¤ distance, elle doit ¨¦tendre UnicastRemote object ou impl¨¦menter l'interface Remote
// - ici elle fait les deux (car l'interface RemoteDessin ¨¦tend l'interface Remote)
// - la classe doit ¨¦galement etre Serializable si on veut la transmettre sur le r¨¦seau
public class DessinServeur extends UnicastRemoteObject implements RemoteDessinServeur, Serializable {

	// les attrribus minimaux d'un Dessin
	private int x ;
	private int y ;
	private int w ;
	private int h ;
	private CreateurDessin createurDessin ;
	private int r ;
	private int g ;
	private int b ;
	private String name ;

	// un attribut permettant au Dessin de diffuser directement ses mises ¨¤ jour, sans passer par le serveur associ¨¦
	// - cet attribut n'est pas Serializable, du coup on le d¨¦clare transient pour qu'il ne soit pas inclu dans la s¨¦rialisation
	protected transient List<EmetteurUnicast> emetteurs ;

	private static final long serialVersionUID = 1L ;

	// constructeur du Dessin sur le serveur : il diffuse alors qu'il faut cr¨¦er un nouveau dessin sur tous les clients 
	public DessinServeur (String name, List<EmetteurUnicast> senders, CreateurDessin createurDessin, int r, int g, int b) throws RemoteException {
		this.emetteurs = senders ;
		this.name = name ;
		this.createurDessin = createurDessin;
		this.r = r;
		this.g = g;
		this.b = b;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (0)) ;
		hm.put ("y", new Integer (0)) ;
		hm.put ("w", new Integer (0)) ;
		hm.put ("h", new Integer (0)) ;
		hm.put ("createurDessin", createurDessin);
		hm.put ("r", r) ;
		hm.put ("g", g) ;
		hm.put ("b", b) ;
		
		// envoi des mises ¨¤ jour ¨¤ tous les clients, via la liste des ¨¦metteurs
		for (EmetteurUnicast sender : senders) {
			sender.diffuseMessage ("Dessin", getName (), hm) ;
		}
	}

	public String getName () throws RemoteException {
		return name ;
	}

	// m¨¦thode qui met ¨¤ jour les limites du Dessin, qui diffuse ensuite ce changement ¨¤ tous les ¨¦diteurs clients
	public void setBounds (int x, int y, int w, int h) throws RemoteException {
		//System.out.println (getName() + " setBounds : " + x + " " + y + " " + w + " " + h) ;
		this.x = x ;
		this.y = y ;
		this.w = w ;
		this.h = h ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (x)) ;
		hm.put ("y", new Integer (y)) ;
		hm.put ("w", new Integer (w)) ;
		hm.put ("h", new Integer (h)) ;
		// envoi des mises ¨¤ jour ¨¤ tous les clients, via la liste des ¨¦metteurs
		for (EmetteurUnicast sender : emetteurs) {
			sender.diffuseMessage ("Bounds", getName (), hm) ;
		}
	}

	// m¨¦thode qui met ¨¤ jour la position du Dessin, qui diffuse ensuite ce changement ¨¤ tous les ¨¦diteurs clients
	public void setLocation (int x, int y) throws RemoteException {
		//System.out.println (getName() + " setLocation : " + x + " " + y) ;
		this.x = x ;
		this.y = y ;
		HashMap<String, Object> hm = new HashMap <String, Object> () ;
		hm.put ("x", new Integer (x)) ;
		hm.put ("y", new Integer (y)) ;
		// envoi des mises ¨¤ jour ¨¤ tous les clients, via la liste des ¨¦metteurs
		for (EmetteurUnicast emetteur : emetteurs) {
			emetteur.diffuseMessage ("Location", getName (), hm) ;
		}
	}

	@Override
	public int getX() throws RemoteException {
		return x ;
	}

	@Override
	public int getY() throws RemoteException {
		return y ;
	}

	@Override
	public int getWidth() throws RemoteException {
		return w ;
	}

	@Override
	public int getHeight() throws RemoteException {
		return h ;
	}
	
	@Override
	public CreateurDessin getCreateurDessin() throws RemoteException {
		return createurDessin ;
	}
	
	@Override
	public int getRed() throws RemoteException {
		return r ;
	}
	
	@Override
	public int getGreen() throws RemoteException {
		return g ;
	}
	
	@Override
	public int getBlue() throws RemoteException {
		return b ;
	}

}