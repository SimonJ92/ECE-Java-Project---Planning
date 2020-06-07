/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

/**Représente un utilisateur
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
    
    /**Constructeur
     *
     * @param id
     * @param email
     * @param passwd
     * @param nom
     * @param prenom
     * @param droit
     */
    public Utilisateur(int id,String email, String passwd, String nom, String prenom, int droit){
        this.id = id;
        this.email = email;
        this.passwd = passwd;
        this.nom = nom;
        this.prenom = prenom;
        this.droit = droit;
    }
    
    /**
     *Constructeur par défaut
     */
    public Utilisateur()    {};
    
    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }
    
    /**
     *
     * @param email
     */
    public void setEmail(String email){
        this.email = email;
    }
    
    /**
     *
     * @param passwd
     */
    public void setPasswd(String passwd){
        this.passwd = passwd;
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
     * @param prenom
     */
    public void setPrenom(String prenom){
        this.prenom = prenom;
    }
    
    /**
     *
     * @param droit
     */
    public void setDroit(int droit){
        this.droit = droit;
    }
    
    /**
     *
     * @return  int l'id de l'utilisateur
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return String l'email de l'utilisateur
     */
    public String getEmail(){
        return this.email;
    }
    
    /**
     *
     * @return String le password de l'utilisateur
     */
    public String getPasswd(){
        return this.passwd;
    }
    
    /**
     *
     * @return  String le nom de l'utilisateur
     */
    public String getNom(){
        return this.nom;
    }
    
    /**
     *
     * @return String le prenom de l'utilisateur
     */
    public String getPrenom(){
        return this.prenom;
    }
    
    /**
     *
     * @return int le droit de l'utilisateur
     */
    public int getDroit(){
        return this.droit;
    }
    
    /**
     *
     * @return String de l'objet décrit
     */
    @Override
    public String toString(){
        return getPrenom()+" "+getNom();
    }
}
