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
public class Salle {
    private int id = 0;
    private String nom = "";
    private int capacite = 0;
    private Site site;
    
    /**
     *Constructeur
     * @param id
     * @param nom
     * @param capacite
     * @param site
     */
    public Salle(int id, String nom, int capacite, Site site){
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.site = site;
    }
    
    /**
     *Constructeur par défaut
     */
    public Salle(){
        this.site = new Site();
    }
    
    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }
    
    /**
     *
     * @param nom
     */
    public void setNom(String nom){
        this.nom = nom;
    }
    
    /**
     *
     * @param capacite
     */
    public void setCapacite(int capacite){
        this.capacite = capacite;
    }
    
    /**
     *
     * @param site
     */
    public void setSite(Site site){
        this.site = site;
    }
    
    /**
     *
     * @return
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return String le nom de la salle
     */
    public String getNom(){
        return this.nom;
    }
    
    /**
     *
     * @return int la capacité de la salle
     */
    public int getCapacite(){
        return this.capacite;
    }
    
    /**
     *
     * @return Site le site de la salle
     */
    public Site getSite(){
        return this.site;
    }
    
    @Override
    public String toString(){
        return getNom()+" ("+getSite().toString()+")";
    }
}
