package serveur ;

import java.io.Serializable ;
import java.net.InetAddress;
import java.rmi.Naming ;
import java.rmi.RemoteException ;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap ;
import java.util.List;

import client.CreateurDessin;
import communication.EmetteurUnicast ;

//classe d'¨¦diteur pr¨¦sente sur le serveur :
//- pour pouvoir invoquer des m¨¦thodes ¨¤ distance, elle doit ¨¦tendre UnicastRemote object ou impl¨¦menter l'interface Remote
//- ici elle fait les deux (car l'interface RemoteEditeurServeur ¨¦tend l'interface Remote)
//- la classe doit ¨¦galement etre Serializable si on veut la transmettre sur le r¨¦seau
public class EditeurServeur extends UnicastRemoteObject implements RemoteEditeurServeur, Serializable {

	// le nom du serveur
	protected String nomServeur ;

	// le port sur lequel est d¨¦clar¨¦ le serveur
	protected int portRMI ;
	
	// la machine sur laquelle se trouve le serveur
	protected String nomMachineServeur ;

	// un entier pour g¨¦n¨¦rer des noms de dessins diff¨¦rents
	protected int idDessin ;

	// un entier pour g¨¦n¨¦rer des noms de dessins diff¨¦rents
	protected int portEmission ;
	
	// un diffuseur ¨¤ une liste d'abonn¨¦s
	private List<EmetteurUnicast> emetteurs ;
	
	// une strutcure pour stocker tous les dessins et y acc¨¦der facilement 
	private HashMap<String, RemoteDessinServeur> dessinsPartages = new HashMap<String, RemoteDessinServeur> () ;

	// le constructeur du serveur : il le d¨¦clare sur un port rmi de la machine d'ex¨¦cution
	protected EditeurServeur (String nomServeur, String nomMachineServeur, int portRMIServeur,	int portEmissionUpdate) throws RemoteException {
		this.nomServeur = nomServeur ;
		this.nomMachineServeur = nomMachineServeur ;
		this.portRMI = portRMIServeur ;
		this.portEmission = portEmissionUpdate ;
		emetteurs = new ArrayList<EmetteurUnicast> () ;
		try {
			// attachcement sur serveur sur un port identifi¨¦ de la machine d'ex¨¦cution
			Naming.rebind ("//" + nomMachineServeur + ":" + portRMIServeur + "/" + nomServeur, this) ;
			System.out.println ("pret pour le service") ;
		} catch (Exception e) {
			System.out.println ("pb RMICentralManager") ;
		}
	}

	@Override
	public int getRMIPort () {
		return portRMI ;
	}

	// m¨¦thode permettant d'enregistrer un dessin sur un port rmi sur la machine du serveur :
	// - comme cela on pourra ¨¦galement invoquer directement des m¨¦thodes en rmi ¨¦galement sur chaque dessin
	public void registerObject (RemoteDessinServeur dessin) {
		try {
			Naming.rebind ("//" + nomMachineServeur + ":" + portRMI + "/" + dessin.getName (), dessin) ;
			System.out.println ("ajout de l'objet " + dessin.getName () + " sur le serveur " + nomMachineServeur + "/"+ portRMI) ;
			System.out.println ("objet " + dessin.getName () + " enregistr¨¦ sur le serveur " + nomMachineServeur + "/"+ portRMI) ;
			System.out.println ("CLIENT/SERVER : objet " + dessin.getName () + " enregistr¨¦ en x " + dessin.getX () + " et y " + dessin.getY ()) ;
		} catch (Exception e) {
			e.printStackTrace () ;
			try {
				System.out.println ("¨¦chec lors de l'ajout de l'objet " + dessin.getName () + " sur le serveur " + nomMachineServeur + "/"+ portRMI) ;
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L ;

	// m¨¦thodes permettant d'ajouter un nouveau dessin dans le syst¨¨me
	@Override
	public synchronized RemoteDessinServeur addDessin (int x, int y, int w, int h, CreateurDessin createurDessin, int r, int g, int b) throws RemoteException {
		// cr¨¦ation d'un nouveau nom, unique, destin¨¦ ¨¤ servir de cl¨¦ d'acc¨¨s au dessin
		// et cr¨¦ation d'un nouveau dessin de ce nom et associ¨¦ ¨¦galement ¨¤ un ¨¦metteur multicast...
		// attention : la classe Dessin utilis¨¦e ici est celle du package serveur (et pas celle du package client)
		RemoteDessinServeur dessin = new DessinServeur ("dessin" + nextId (), emetteurs, createurDessin, r, g, b) ;
		// enregistrement du dessin pour acc¨¨s rmi distant
		registerObject (dessin) ;
		// ajout du dessin dans la liste des dessins pour acc¨¨s plus efficace au dessin
		dessinsPartages.put (dessin.getName (), dessin) ;
		System.out.println ("addDessin : sharedDessins = " + dessinsPartages) ;
		// renvoi du dessin ¨¤ l'¨¦diteur local appelant : l'¨¦diteur local r¨¦cup¨¨rera seulement un RemoteDessin
		// sur lequel il pourra invoquer des m¨¦thodes en rmi et qui seront relay¨¦es au r¨¦f¨¦rent associ¨¦ sur le serveur  
		return dessin ;
	}

	// m¨¦thode permettant d'acc¨¦der ¨¤ un proxy d'un des dessins
	@Override
	public synchronized RemoteDessinServeur getDessin (String name) throws RemoteException {
		//System.out.println ("getDessin " + name + " dans dessinsPartag¨¦s = " + dessinsPartages) ;
		return dessinsPartages.get (name) ;
	}

	// m¨¦thode qui incr¨¦mente le compteur de dessins pour avoir un id unique pour chaque dessin :
	// dans une version ult¨¦rieure avec r¨¦cup¨¦ration de dessins ¨¤ aprtir d'une sauvegarde, il faudra ¨¦galement avoir sauvegard¨¦ ce nombre...
	public int nextId () {
		idDessin++ ; 
		return idDessin ; 
	}

	// m¨¦thode permettant de r¨¦cup¨¦rer la liste des dessins : utile lorsqu'un ¨¦diteur client se connecte 
	@Override
	public synchronized ArrayList<RemoteDessinServeur> getDessinsPartages () throws RemoteException {
		return new ArrayList<RemoteDessinServeur> (dessinsPartages.values()) ;
	}

	// m¨¦thode indiquant quel est le port d'¨¦mission/r¨¦ception ¨¤ utiliser pour le client qui rejoint le serveur
	// on utilise une valeur arbitraitre de port qu'on incr¨¦mente de 1 ¨¤ chaque arriv¨¦e d'un nouveau client
	// cela permettra d'avoir plusieurs clients sur la mÃªme machine, chacun avec un canal de communication distinct
	// sur un port diff¨¦rent des autres clients
	@Override
	public int getPortEmission (InetAddress adresseClient) throws RemoteException {
		EmetteurUnicast emetteur = new EmetteurUnicast (adresseClient, portEmission++) ;
		emetteurs.add (emetteur) ;
		return (emetteur.getPortEmission ()) ;
	}

	// m¨¦thode permettant juste de v¨¦rifier que le serveur est lanc¨¦
	@Override
	public void answer (String question) throws RemoteException {
		System.out.println ("SERVER : the question was : " + question) ;   
	}

}
