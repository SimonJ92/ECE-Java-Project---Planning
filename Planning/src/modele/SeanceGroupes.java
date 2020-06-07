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
public class SeanceGroupes {
    private Seance seance;
    private Groupe groupe;
    
    /**
     *Constructeur
     * @param seance
     * @param groupe
     */
    public SeanceGroupes(Seance seance,Groupe groupe){
        this.seance = seance;
        this.groupe = groupe;
    }
    
    /**
     *Constructeur
     */
    public SeanceGroupes(){
        this.seance = new Seance();
        this.groupe = new Groupe();
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
     * @param groupe
     */
    public void setGroupe(Groupe groupe){
        this.groupe = groupe;
    }
    
    /**
     *
     * @return Seance   la séance concernée
     */
    public Seance getSeance(){
        return this.seance;
    }
    
    /**
     *
     * @return Groupe   le groupe concerné
     */
    public Groupe getGroupe(){
        return this.groupe;
    }
    
    @Override
    public String toString(){
        return getSeance().getCours().getNom()+" - "+getGroupe().toString();
    }
}
