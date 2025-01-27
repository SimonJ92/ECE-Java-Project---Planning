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
public class Etudiant extends Utilisateur{
    private int numero = 0;
    private Groupe groupe;
    
    /**
     *Constructeur
     * @param idUtilisateur
     * @param numero
     * @param groupe
     * @param email
     * @param passwd
     * @param nom
     * @param prenom
     * @param droit
     */
    public Etudiant(int idUtilisateur, int numero, Groupe groupe, String email, String passwd, String nom, String prenom, int droit){
        super(idUtilisateur, email, passwd,nom,prenom,droit);
        this.numero = numero;
        this.groupe = groupe;
    }
    
    /**
     *Constructeur par défaut
     */
    public Etudiant(){
        this.groupe = new Groupe();
    };
    
    /**
     *
     * @param idUtilisateur
     */
    public void setIdUtilisateur(int idUtilisateur){
        super.setId(idUtilisateur);
    }
    
    /**
     *
     * @param numero
     */
    public void setNumero(int numero){
        this.numero = numero;
    }
    
    /**
     *
     * @param groupe
     */
    public void setGroupe(Groupe groupe){
        this.groupe = groupe;
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
    
    /**
     *
     * @return int l'id de l'utilisateur
     */
    public int getIdUtilisateur(){
        return super.getId();
    }
    
    /**
     *
     * @return int le numéro de l'étudiant
     */
    public int getNumero(){
        return this.numero;
    }
    
    /**
     *
     * @return Groupe le groupe de l'étudiant
     */
    public Groupe getGroupe(){
        return this.groupe;
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
    public int getDroit(){
        return super.getDroit();
    }
    
    @Override
    public String toString(){
        return super.toString()+" - Promo "+getGroupe().getPromotion().getNom()+" "+getGroupe().getNom();
    }
}
