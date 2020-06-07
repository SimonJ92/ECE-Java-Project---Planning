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
    
    /**
     *Constructeur par défaut
     * @param matiere
     * @param premiereSeance
     * @param derniereSeance
     * @param duree
     * @param nombre
     */
    public LigneRecap(String matiere,String premiereSeance, String derniereSeance, String duree, String nombre){
        this.matiere = matiere;
        this.premiereSeance = premiereSeance;
        this.derniereSeance = derniereSeance;
        this.duree = duree;
        this.nombre = nombre;
    }
    
    //getters

    /**
     *
     * @return String la matière
     */
    public String getMatiere(){
        return this.matiere;
    }
    
    /**
     *
     * @return String la première séance
     */
    public String getPremiereSeance(){
        return this.premiereSeance;
    }
    
    /**
     *
     * @return String la dernière séance
     */
    public String getDerniereSeance(){
        return this.derniereSeance;
    }
    
    /**
     *
     * @return String la durée cumulée
     */
    public String getDuree(){
        return this.duree;
    }
    
    /**
     *
     * @return String le nombre de séances
     */
    public String getNombre(){
        return this.nombre;
    }
    
    //setters

    /**
     *
     * @param matiere
     */
    public void setMatiere(String matiere){
        this.matiere = matiere;
    }
    
    /**
     *
     * @param premiereSeance
     */
    public void setPremiereSeance(String premiereSeance){
        this.premiereSeance = premiereSeance;
    }
    
    /**
     *
     * @param derniereSeance
     */
    public void setDerniereSeance(String derniereSeance){
        this.derniereSeance = derniereSeance;
    }
    
    /**
     *
     * @param duree
     */
    public void setDuree(String duree){
        this.duree = duree;
    }
    
    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
}
