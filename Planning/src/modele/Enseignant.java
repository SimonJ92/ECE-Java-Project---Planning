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
    
    /**
     *Constructeur
     * @param idUtilisateur
     * @param cours
     * @param email
     * @param passwd
     * @param nom
     * @param prenom
     * @param droit
     */
    public Enseignant(int idUtilisateur, Cours cours, String email, String passwd, String nom, String prenom, int droit){
        super(idUtilisateur, email, passwd,nom,prenom,droit);
        this.cours = cours;
    }
    
    /**
     *Constructeur par défaut
     */
    public Enseignant(){
        this.cours = new Cours();
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
     * @param cours
     */
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
    
    /**
     *
     * @return int l'id de l'enseignant
     */
    public int getIdUtilisateur(){
        return super.getId();
    }
    
    /**
     *
     * @return Cours le cours de l'enseignant
     */
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
    public int getDroit(){
        return super.getDroit();
    }
    
    @Override
    public String toString(){
        return super.toString()+" : "+getCours().getNom();
    }
}
