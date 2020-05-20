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
    private int idSite = 0;
    
    public Salle(int id, String nom, int capacite, int idSite){
        this.id = id;
        this.nom = nom;
        this.capacite = capacite;
        this.idSite = idSite;
    }
    
    public Salle()  {};
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setCapacite(int capacite){
        this.capacite = capacite;
    }
    
    public void setIdSite(int idSite){
        this.idSite = idSite;
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
    
    public int getIdSite(){
        return this.idSite;
    }
}
