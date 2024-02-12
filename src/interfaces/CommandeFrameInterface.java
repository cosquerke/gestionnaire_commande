package interfaces;

import Appli.data.Commande;

import java.util.List;

/**
 * L'interface définit les méthodes nécessaires pour afficher et modifier les commandes dans une fenetre.
 */
public interface CommandeFrameInterface {
    /**
     * Affiche la fenetre des commandes.
     */
    void display();

    /**
     * Modifie les commandes à afficher dans la fenetre.
     * @param commandes La liste des commandes à afficher.
     */
    void setCommandes(List<Commande> commandes);
}
