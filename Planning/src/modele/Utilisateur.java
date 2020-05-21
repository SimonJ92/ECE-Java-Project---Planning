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
public class Utilisateur {
    private int id = 0;
    private String email = "";
    private String passwd = "";
    private String nom = "";
    private String prenom = "";
    private int droit = 0;
    
    public Utilisateur(int id,String email, String passwd, String nom, String prenom, int droit){
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.nom = nom;
        this.prenom = prenom;
        this.droit = droit;
    }
    
    public Utilisateur()    {};
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setEmail(String email){
        this.email = email;
    }
    
    public void setPasswd(String passwd){
        this.passwd = passwd;
    }
    
    public void setNom(String nom){
        this.nom = nom;
    }
    
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    
    public void setDroit(int droit){
        this.droit = droit;
    }
    
    public int getId(){
        return this.id;
    }
    
    public String getEmail(){
        return this.email;
    }
    
    public String getPasswd(){
        return this.passwd;
    }
    
    public String getNom(){
        return this.nom;
    }
    
    public String getPrenom(){
        return this.prenom;
    }
    
    public int getDroit(){
        return this.droit;
    }
}
