package interfaces;

import Appli.data.Commande;

import javax.swing.*;
import java.util.List;

public interface CommandeFrameInterface {
    void display();

    void setCommandes(List<Commande> commandes);
}
