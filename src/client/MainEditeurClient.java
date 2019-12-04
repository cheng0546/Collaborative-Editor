package client ;

public class MainEditeurClient {

	// main permettant de lancer un ¨¦diteur local 
	public static void main (String [] args) {
		// le nom de la machine qui h¨¦berge le serveur distant
		// String nomMachineServeur = "10.29.227.68" ;
		String nomMachineServeur = "localhost" ; // mettre l'adresse IP de votre serveur ici
		// le numro de port sur lequel est d¨¦clar¨¦ le serveur distant
		int portRMIServeur = 2010 ;
		// le nom du serveur distant
		String nomEditeurCollaboratif = "EditeurCollaboratif" ;
		// le nom de l'¨¦diteur local (ne sert pas ¨¤ grand chose, du moins pour le moment)
		//String nomMachineClient = "10.29.227.68" ;
		String nomMachineClient = "localhost" ; // mettre l'adresse IP de votre client ici
		System.out.println ("Connexion ¨¤ un serveur avec les caract¨¦ristiques :") ;
		System.out.println ("machine du client : " + nomMachineClient) ;
		System.out.println ("nom du serveur distant : " + nomMachineServeur) ;
		System.out.println ("port rmi du serveur : " + portRMIServeur) ;
		System.out.println ("nom de l'univers partag¨¦ : " + nomEditeurCollaboratif) ;
		// instanciation d'un client d¨¦port¨¦ qui fera le lien avec le navigateur
		new EditeurClient (nomMachineClient, nomEditeurCollaboratif, nomMachineServeur, portRMIServeur) ;

	}

}
