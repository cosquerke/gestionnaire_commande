package Appli.data;

public class Personne {
	private String nom;
	
	
	public Personne(String nom_) {
		// TODO Auto-generated constructor stub
		this.nom = nom_;
	}
	
	public String toString(){
		return getNom();
	}
	
	public String getNom() {
		return this.nom;
	}

}
