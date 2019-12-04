package client;

import serveur.RemoteDessinServeur;

public class CreateurElipseCreuse implements CreateurDessin {
	
	public ElipseCreuse creerDessin (RemoteDessinServeur proxy, int r, int g, int b) {
		return new ElipseCreuse(proxy, r, g, b);
	}

}
