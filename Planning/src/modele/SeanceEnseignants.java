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
    
    /**
     *Constructeur
     * @param seance
     * @param enseignant
     */
    public SeanceEnseignants(Seance seance,Utilisateur enseignant){
        this.seance = seance;
        this.utilisateur = enseignant;
    }
    
    /**
     *Constructeur par défaut
     */
    public SeanceEnseignants(){
        this.seance = new Seance();
        this.utilisateur = new Enseignant();
    }
    
    /**
     *
     * @param seance
     */
    public void setSeance(Seance seance){
        this.seance = seance;
    }
    
    /**
     *
     * @param enseignant
     */
    public void setEnseignant(Utilisateur enseignant){
        this.utilisateur = enseignant;
    }
    
    /**
     *
     * @return la séance concernée
     */
    public Seance getSeance(){
        return this.seance;
    }
    
    /**
     *
     * @return l'utilisateur concerné
     */
    public Utilisateur getUtilisateur(){
        return this.utilisateur;
    }
    
    @Override
    public String toString(){
        return getSeance().getCours().getNom()+" - "+getUtilisateur().toString();
    }
}
