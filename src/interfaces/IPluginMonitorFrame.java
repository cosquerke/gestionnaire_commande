package interfaces;

/**
 * L'interface définit les méthodes nécessaires pour afficher et mettre à jour 
 * les plugins dans la fenetre de monitoring des plugins.
 */
public interface IPluginMonitorFrame {
    /**
     * Affiche la fenetre du monitor de plugins.
     */
    void display();
    
    /**
     * Met à jour la liste des plugins dans la fenetre du monitor de plugins.
     */
    void setPlugins();
}