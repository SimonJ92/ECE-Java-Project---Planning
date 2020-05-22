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
    
    public SeanceGroupes(Seance seance,Groupe groupe){
        this.seance = seance;
        this.groupe = groupe;
    }
    
    public SeanceGroupes(){
        this.seance = new Seance();
        this.groupe = new Groupe();
    }
    
    public void setSeance(Seance seance){
        this.seance = seance;
    }
    
    public void setGroupe(Groupe groupe){
        this.groupe = groupe;
    }
    
    public Seance getSeance(){
        return this.seance;
    }
    
    public Groupe getGroupe(){
        return this.groupe;
    }
    
    @Override
    public String toString(){
        return getSeance().getCours().getNom()+" - "+getGroupe().toString();
    }
}
