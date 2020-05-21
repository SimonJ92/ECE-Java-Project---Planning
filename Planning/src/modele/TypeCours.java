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
public class TypeCours {
    private int id = 0;
    private String nom = "";
    
    public TypeCours(int id, String nom){
        this.id = id;
        this.nom = nom;
    }
    
    public TypeCours()  {};
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getNom(){
        return this.nom;
    }
}
