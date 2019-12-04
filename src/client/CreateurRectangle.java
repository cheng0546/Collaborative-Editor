package client;

import serveur.RemoteDessinServeur;

public class CreateurRectangle implements CreateurDessin {
	
	public Rectangle creerDessin (RemoteDessinServeur proxy, int r, int g, int b) {
		return new Rectangle(proxy, r, g, b);
	}

}
