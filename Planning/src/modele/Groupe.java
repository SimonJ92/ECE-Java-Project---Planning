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
    
    /**
     *Constructeur
     * @param id
     * @param nom
     * @param promotion
     */
    public Groupe(int id, String nom, Promotion promotion){
        this.id = id;
        this.nom = nom;
        this.promotion = promotion;
    }
    
    /**
     *Constructeur par défaut
     */
    public Groupe(){
        this.promotion = new Promotion();
    };
    
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
     * @param promotion
     */
    public void setPromotion(Promotion promotion){
        this.promotion = promotion;
    }
    
    /**
     *
     * @return int l'id du groupe
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return String le nom du groupe
     */
    public String getNom(){
        return this.nom;
    }
    
    /**
     *
     * @return Promotion la promotion du groupe
     */
    public Promotion getPromotion(){
        return this.promotion;
    }
    
    @Override
    public String toString(){
        return getPromotion().toString()+" "+getNom();
    }
}
