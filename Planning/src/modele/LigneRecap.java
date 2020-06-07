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
public class LigneRecap {
    private String matiere;
    private String premiereSeance;
    private String derniereSeance;
    private String duree;
    private String nombre;
    
    public LigneRecap(String matiere,String premiereSeance, String derniereSeance, String duree, String nombre){
        this.matiere = matiere;
        this.premiereSeance = premiereSeance;
        this.derniereSeance = derniereSeance;
        this.duree = duree;
        this.nombre = nombre;
    }
    
    //getters
    public String getMatiere(){
        return this.matiere;
    }
    
    public String getPremiereSeance(){
        return this.premiereSeance;
    }
    
    public String getDerniereSeance(){
        return this.derniereSeance;
    }
    
    public String getDuree(){
        return this.duree;
    }
    
    public String getNombre(){
        return this.nombre;
    }
    
    //setters
    public void setMatiere(String matiere){
        this.matiere = matiere;
    }
    
    public void setPremiereSeance(String premiereSeance){
        this.premiereSeance = premiereSeance;
    }
    
    public void setDerniereSeance(String derniereSeance){
        this.derniereSeance = derniereSeance;
    }
    
    public void setDuree(String duree){
        this.duree = duree;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}
