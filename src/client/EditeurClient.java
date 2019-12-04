package client ;

import java.awt.Color;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.Naming ;
import java.rmi.RemoteException ;
import java.util.ArrayList;
import java.util.HashMap ;

import javax.swing.JFrame ;

import client.CreateurDessin;
import client.EditeurClient;

import communication.RecepteurUnicast ;
import serveur.RemoteDessinServeur ;
import serveur.RemoteEditeurServeur ;

// Classe d'¨¦didion locale :
// - elle propage toutes ses actions au serveur via des "remote invocations"
// - elle recoit ¨¦galement des mises ¨¤ jour en provenance du serveur en mode "unicast"

public class EditeurClient extends JFrame {

	private static final long serialVersionUID = 1L ;
	
	// le Thread pour pouvoir recevoir des mises ¨¤ jour en provenance du serveur
	private Thread threadRecepteur ;
	
	// le r¨¦cepteur de messages diffus¨¦s aux abonn¨¦s
	private RecepteurUnicast recepteurUnicast ;
	
	// le serveur distant qui centralise toutes les informations
	private RemoteEditeurServeur serveur ;
	
	// le nom de la machine qui h¨¦berge l'¨¦diteur local
	protected String nomMachineClient ;
	
	// le port rmi sur lequel est d¨¦clar¨¦ le serveur distant
	protected int portRMI ;
	
	// le nom de la machine sur laquelle se trouve le serveur distant :
	// - le syst¨¨me saura calculer l'adresse IP de la machine ¨¤ partir de son nom
	// - sinon on met directement l'adresse IP du serveur dans cette chaine de caract¨¨re 
	protected String nomMachineServeur ;
	
	// l'¨¦l¨¦ment visuel dans lequel on va manipuler des dessins 
	private ZoneDeDessin zdd ;
	
	// une table pour stocker tous les dessins produits :
	// - elle est redondante avec le contenu de la ZoneDe Dessin
	// - mais elle va permettre d'acc¨¦der plus rapidement ¨¤ un Dessin ¨¤ partir de son nom
	private HashMap<String, DessinClient> dessins = new HashMap<String, DessinClient> () ;

	// Constructeur ¨¤ qui on transmet les informations suivantes :
	// - nom de l'¨¦diteur
	// - nom du serveur distant
	// - nom de la machine sur laquelle se trouve le serveur
	// - num¨¦ro de port sur lequel est d¨¦clar¨¦ le serveur sur la machine distante

	EditeurClient (final String nomMachineClient, final String nomEditeurServeur, final String nomMachineServeur, final int portRMIServeur) {

		setDefaultLookAndFeelDecorated(true);
		setTitle("Editeur de dessin");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.nomMachineClient = nomMachineClient ;

		// cr¨¦ation d'une ZoneDeDessin
		zdd = new ZoneDeDessin (this) ;
		getContentPane().add (zdd) ;
		setSize (300, 300) ;

		try {
			// tentative de connexion au serveur distant
			serveur = (RemoteEditeurServeur)Naming.lookup ("//" + nomMachineServeur + ":" + portRMIServeur + "/" + nomEditeurServeur) ;
			// invocation d'une premi¨¨re m¨¦thode juste pour test
			serveur.answer ("hello from " + getName ()) ;
			// r¨¦cup¨¦ration de tous les dessins d¨¦j¨¤ pr¨¦sents sur le serveur
			ArrayList<RemoteDessinServeur> dessinsDistants = serveur.getDessinsPartages() ;
			// ajout de tous les dessins dans la zone de dessin
			for (RemoteDessinServeur rd : dessinsDistants) {
				ajouterDessin (rd, rd.getName (), rd.getX (), rd.getY (), rd.getWidth(), rd. getHeight (), rd. getCreateurDessin (), rd. getRed (), rd. getGreen (), rd. getBlue ()) ;
			}
		} catch (Exception e) {
			System.out.println ("probleme liaison CentralManager") ;
			e.printStackTrace () ;
			System.exit (1) ;
		}
		try {
			// cr¨¦ation d'un r¨¦cepteur unicast en demandant l'information de num¨¦ro port au serveur
			// en meme temps on transmet au serveur l'adresse IP de la machine du client au serveur
			// de facon ¨¤ ce que ce dernier puisse par la suite envoyer des messages de mise ¨¤ jour ¨¤ ce r¨¦cepteur 
			recepteurUnicast = new RecepteurUnicast (InetAddress.getByName (nomMachineClient), serveur.getPortEmission (InetAddress.getByName (nomMachineClient))) ;
			// on aimerait bien demander automatiquement quel est l'adresse IP de la machine du client,
			// mais le probl¨¨me est que celle-ci peut avoir plusieurs adresses IP (filaire, wifi, ...)
			// et qu'on ne sait pas laquelle sera retourn¨¦e par InetAddress.getLocalHost ()...
			//recepteurUnicast = new RecepteurUnicast (serveur.getPortEmission (InetAddress.getLocalHost ())) ;
			recepteurUnicast.setClientLocal (this) ;
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// cr¨¦ation d'un Thread pour pouvoir recevoir les messages du serveur en parall¨¨le des interactions avec les dessins
		threadRecepteur = new Thread (recepteurUnicast) ;
		// d¨¦marrage effectif du Thread
		threadRecepteur.start () ;
		
		// demande d'affichage de l'¨¦diteur
		// - faite "seulement"/"tardivement" ici pour que tous les objets r¨¦cup¨¦r¨¦s du serveur apparaissent bien du premier coup
		setSize(900, 675);
		setVisible (true) ;
	}

	// m¨¦thode permettant de mettre ¨¤ jour les limites d'un dessin
	// - elle sera appel¨¦e suite ¨¤ la r¨¦ception d'un message diffus¨¦ par le serveur
	
	public synchronized void objectUpdateBounds (String objectName, int x, int y, int w, int h) {
		// r¨¦cup¨¦ration du dessin ¨¤ partir de son nom
		DessinClient dessinToUpdate = dessins.get (objectName) ;
		if (dessinToUpdate != null) {
			dessinToUpdate.setBounds (x, y, w, h) ;
		}
	}

	// m¨¦thode permettant de mettre ¨¤ jour la position d'un dessin
	// - elle sera appel¨¦e suite ¨¤ la r¨¦ception d'un message diffus¨¦ par le serveur  
	public synchronized void objectUpdateLocation (String objectName, int x, int y) {
		// r¨¦cup¨¦ration du dessin ¨¤ partir de son nom
		DessinClient dessinToUpdate = dessins.get (objectName) ;
		if (dessinToUpdate != null) {
			dessinToUpdate.setLocation (x, y) ;
		}		
	}

	// m¨¦thode permettant de cr¨¦er un nouveau dessin suite ¨¤ une interaction de l'utilisateur
	// - elle va devoir transmettre cette cr¨¦ation au serveur et lui demander un proxy pour ce dessin   
	public synchronized DessinClient creerDessin (int x, int y, int w, int h, CreateurDessin createurDessin, int r, int g, int b) {
		RemoteDessinServeur proxy = null ;
		String proxyName = null ;
		try {
			// ajout distant d'un dessin et r¨¦cup¨¦ration de son proxy 
			proxy = serveur.addDessin (x, y, w, h, createurDessin, r, g, b);
			// r¨¦cup¨¦ration du nom attribu¨¦ par le serveur :
			// - on le fait ici pour ne pas multiplier les blocs "try catch" n¨¦cessaires ¨¤ chaque invocation distante de m¨¦thode 
			proxyName = proxy.getName () ;
		} catch (RemoteException e) {
			e.printStackTrace();
		} 
		// cr¨¦ation d'un nouveau dessin
		//DessinClient dessin = new DessinClient (proxy) ;
		DessinClient dessin = createurDessin.creerDessin(proxy, r, g, b);
		System.out.println("cr¨¦ation du dessin " + proxyName) ;
		// ajout du dessin dans la liste des dessins s'il n'y est pas d¨¦j¨¤ :
		// - on a pu recevoir une information de cr¨¦ation en parall¨¨le vie le Thread de r¨¦ception de messages diffus¨¦s par le serveur
		if (! dessins.containsKey (proxyName)) {
			dessins.put (proxyName, dessin) ;
		} else {
			System.out.println ("dessin " + proxyName + " ¨¦tait d¨¦j¨¤ cr¨¦¨¦") ;
		}
		return dessin ;
	}

	// m¨¦thode permettant d'ajouter localement un dessin d¨¦j¨¤ pr¨¦sent sur le serveur
	// - elle sera appel¨¦e suite ¨¤ la r¨¦ception d'un message diffus¨¦ par le serveur
	public synchronized void ajouterDessin (String proxyName, int x, int y, int w, int h, CreateurDessin createurDessin, int r, int g, int b) {
		System.out.println("ajout du dessin " + proxyName);
		// on ne l'ajoute que s'il n'est pas d¨¦j¨¤ pr¨¦sent
		// - il pourrait d¨¦j¨¤ etre pr¨¦sent si il avait ¨¦t¨¦ cr¨¦¨¦ localement par une interaction dans cet ¨¦diteur local
		if (! dessins.containsKey (proxyName)) {
			RemoteDessinServeur proxy = null ;
			try {
				// r¨¦cup¨¦ration du proxy via une demande au serveur
				proxy = serveur.getDessin (proxyName);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			if (proxy == null) {
				System.out.println("proxy " + proxyName + " null");
			}
			// ajout du dessin
			ajouterDessin (proxy, proxyName, x, y, w, h, createurDessin, r, g, b) ;
		} else {
			System.out.println ("dessin " + proxyName + " ¨¦tait d¨¦j¨¤ pr¨¦sent") ;
		}
	}

	// m¨¦thode d'ajout d'un dessin : factorisation de code
	public void ajouterDessin (RemoteDessinServeur proxy, String proxyName, int x, int y, int w, int h, CreateurDessin createurDessin, int r, int g, int b) {
		// cr¨¦ation effective d'un dessin
		DessinClient dessin = createurDessin.creerDessin(proxy, r, g, b) ;
		dessin.setOpaque (false) ;
		// ajout du dessin dans la liste des dessins
		dessins.put (proxyName, dessin) ;
		// initialisation des limites du dessin
		dessin.setBounds(x, y, w, h) ;
		// ajout du dessin dans la zone de dessin, au premier plan (grace au "0" dans le add)
		zdd.add (dessin, 0) ;
	}
	
}
