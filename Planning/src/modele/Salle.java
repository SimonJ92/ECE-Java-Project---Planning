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
    
    public Salle(int id, String nom, int capacite, Site site){
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.site = site;
    }
    
    public Salle(){
        this.site = new Site();
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setCapacite(int capacite){
        this.capacite = capacite;
    }
    
    public void setSite(Site site){
        this.site = site;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNom(){
        return this.nom;
    }
    
    public int getCapacite(){
        return this.capacite;
    }
    
    public Site getSite(){
        return this.site;
    }
}
