package client;

import serveur.RemoteDessinServeur;

public class CreateurRectangleCreux implements CreateurDessin {
	
	public RectangleCreux creerDessin (RemoteDessinServeur proxy, int r, int g, int b) {
		return new RectangleCreux(proxy, r, g, b);
	}

}
