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
public class Promotion {
    private int id = 0;
    private String nom = "";
    
    /**
     *Constructeur
     * @param id
     * @param nom
     */
    public Promotion(int id, String nom){
        this.id = id;
        this.nom = nom;
    }
    
    /**
     *Constructeur par défaut
     */
    public Promotion()  {};
    
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
     * @return
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return
     */
    public String getNom(){
        return this.nom;
    }
    
    @Override
    public String toString(){
        return getNom();
    }
}
