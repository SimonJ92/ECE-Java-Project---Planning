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
public class Enseignant extends Utilisateur{
    private Cours cours;
    
    public Enseignant(int idUtilisateur, Cours cours, String email, String passwd, String nom, String prenom, int droit){
        super(idUtilisateur, email, passwd,nom,prenom,droit);
        this.cours = cours;
    }
    
    public Enseignant(){
        this.cours = new Cours();
    };
    
    public void setIdUtilisateur(int idUtilisateur){
        super.setId(idUtilisateur);
    }
    
    public void setCours(Cours cours){
        this.cours = cours;
    }
    
    @Override
    public void setEmail(String email){
        super.setEmail(email);
    }
    
    @Override
    public void setPasswd(String passwd){
        super.setPasswd(passwd);
    }
    
    @Override
    public void setNom(String nom){
        super.setNom(nom);
    }
    
    @Override
    public void setPrenom(String prenom){
        super.setPrenom(prenom);
    }
    
    @Override
    public void setDroit(int droit){
        super.setDroit(droit);
    }
    
    public int getidUtilisateur(){
        return super.getid();
    }
    
    public Cours getCours(){
        return this.cours;
    }
    
    @Override
    public String getEmail(){
        return super.getEmail();
    }
    
    @Override
    public String getPasswd(){
        return super.getPasswd();
    }
    
    @Override
    public String getNom(){
        return super.getNom();
    }
    
    @Override
    public String getPrenom(){
        return super.getPrenom();
    }
    
    @Override
    public int getdroit(){
        return super.getdroit();
    }
}
