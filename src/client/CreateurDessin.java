package client;

import java.io.Serializable;

import serveur.RemoteDessinServeur;

public interface CreateurDessin extends Serializable {
	public DessinClient creerDessin(RemoteDessinServeur proxy, int r, int g, int b);
}
