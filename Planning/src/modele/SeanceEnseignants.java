/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *
 * @author simon
 */
public class SeanceEnseignants {
    private Seance seance;
    private Utilisateur utilisateur;
    
    public SeanceEnseignants(Seance seance,Utilisateur enseignant){
        this.seance = seance;
        this.utilisateur = enseignant;
    }
    
    public SeanceEnseignants(){
        this.seance = new Seance();
        this.utilisateur = new Enseignant();
    }
    
    public void setSeance(Seance seance){
        this.seance = seance;
    }
    
    public void setEnseignant(Utilisateur enseignant){
        this.utilisateur = enseignant;
    }
    
    public Seance getSeance(){
        return this.seance;
    }
    
    public Utilisateur getUtilisateur(){
        return this.utilisateur;
    }
}
