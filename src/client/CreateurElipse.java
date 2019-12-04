package client;

import serveur.RemoteDessinServeur;

public class CreateurElipse implements CreateurDessin {
	
	public Elipse creerDessin (RemoteDessinServeur proxy, int r, int g, int b) {
		return new Elipse(proxy, r, g, b);
	}

}
