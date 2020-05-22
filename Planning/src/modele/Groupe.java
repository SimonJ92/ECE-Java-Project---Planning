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
    private Promotion promotion;
    
    public Groupe(int id, String nom, Promotion promotion){
        this.id = id;
        this.nom = nom;
        this.promotion = promotion;
    }
    
    public Groupe(){
        this.promotion = new Promotion();
    };
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setPromotion(Promotion promotion){
        this.promotion = promotion;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNom(){
        return this.nom;
    }
    
    public Promotion getPromotion(){
        return this.promotion;
    }
    
    @Override
    public String toString(){
        return getPromotion().toString()+" "+getNom();
    }
}
