/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**
 *Représente un type de cours
 * @author simon
 */
public class TypeCours {
    private int id = 0;
    private String nom = "";
    
    /**
     *Constructeur
     * @param id
     * @param nom
     */
    public TypeCours(int id, String nom){
        this.id = id;
        this.nom = nom;
    }
    
    /**
     *Constructeur par défaut
     */
    public TypeCours()  {};
    
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
     * @return int l'id du type de cours
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return String le nom du type de cours
     */
    public String getNom(){
        return this.nom;
    }
    
    @Override
    public String toString(){
        return getNom();
    }
}
