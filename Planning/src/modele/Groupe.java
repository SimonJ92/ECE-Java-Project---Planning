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
public class Groupe {
    private int id = 0;
    private String nom = "";
    private int idPromotion = 0;
    
    public Groupe(int id, String nom, int idPromotion){
        this.id = id;
        this.nom = nom;
        this.idPromotion = idPromotion;
    }
    
    public Groupe() {};
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setIdPromotion(int idPromotion){
        this.idPromotion = idPromotion;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNom(){
        return this.nom;
    }
    
    public int getIdPromotion(){
        return this.idPromotion;
    }
}
