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
    private Enseignant enseignant;
    
    public SeanceEnseignants(Seance seance,Enseignant enseignant){
        this.seance = seance;
        this.enseignant = enseignant;
    }
    
    public SeanceEnseignants(){
        this.seance = new Seance();
        this.enseignant = new Enseignant();
    }
    
    public void setSeance(Seance seance){
        this.seance = seance;
    }
    
    public void setEnseignant(Enseignant enseignant){
        this.enseignant = enseignant;
    }
    
    public Seance getSeance(){
        return this.seance;
    }
    
    public Enseignant getEnseignant(){
        return this.enseignant;
    }
}
