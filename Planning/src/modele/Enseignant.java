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
public class Enseignant {
    private int idUtilisateur = 0;
    private int idCours = 0;
    
    public Enseignant(int idUtilisateur, int idCours){
        this.idUtilisateur = idUtilisateur;
        this.idCours = idCours;
    }
    
    public Enseignant() {};
    
    public void setIdUtilisateur(int idUtilisateur){
        this.idUtilisateur = idUtilisateur;
    }
    
    public void setIdCours(int idCours){
        this.idCours = idCours;
    }
    
    public int getIdUtilisateur(){
        return this.idUtilisateur;
    }
    
    public int getIdCours(){
        return this.idCours;
    }
}
