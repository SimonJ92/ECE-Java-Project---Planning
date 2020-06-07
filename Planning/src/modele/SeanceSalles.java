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
public class SeanceSalles {
    private Seance seance;
    private Salle salle;
    
    /**
     *Constructeur
     * @param seance
     * @param salle
     */
    public SeanceSalles(Seance seance,Salle salle){
        this.seance = seance;
        this.salle = salle;
    }
    
    /**
     *Constructeur par défaut
     */
    public SeanceSalles(){
        this.seance = new Seance();
        this.salle = new Salle();
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
     * @param salle
     */
    public void setSalle(Salle salle){
        this.salle = salle;
    }
    
    /**
     *
     * @return Seance   la séance consernée
     */
    public Seance getSeance(){
        return this.seance;
    }
    
    /**
     *
     * @return Salle la salle concernée
     */
    public Salle getSalle(){
        return this.salle;
    }
    
    @Override
    public String toString(){
        return getSeance().getCours().getNom()+" - "+getSalle().toString();
    }
}
