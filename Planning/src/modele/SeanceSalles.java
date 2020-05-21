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
    
    public SeanceSalles(Seance seance,Salle salle){
        this.seance = seance;
        this.salle = salle;
    }
    
    public SeanceSalles(){
        this.seance = new Seance();
        this.salle = new Salle();
    }
    
    public void setSeance(Seance seance){
        this.seance = seance;
    }
    
    public void setSalle(Salle salle){
        this.salle = salle;
    }
    
    public Seance getSeance(){
        return this.seance;
    }
    
    public Salle getSalle(){
        return this.salle;
    }
}
