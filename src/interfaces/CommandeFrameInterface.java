package interfaces;

import Appli.data.Commande;

import java.util.List;

public interface CommandeFrameInterface {
    void display();

    void setCommandes(List<Commande> commandes);
}
