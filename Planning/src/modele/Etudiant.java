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
public class Etudiant {
    private int idUtilisateur = 0;
    private int numero = 0;
    private int idGroupe = 0;
    
    public Etudiant(int idUtilisateur, int numero, int idGroupe){
        this.idUtilisateur = idUtilisateur;
        this.numero = numero;
        this.idGroupe = idGroupe;
    }
    
    public Etudiant()   {};
    
    public void setIdUtilisateur(int idUtilisateur){
        this.idUtilisateur = idUtilisateur;
    }
    
    public void setNumero(int numero){
        this.numero = numero;
    }
    
    public void setIdGroupe(int idGroupe){
        this.idGroupe = idGroupe;
    }
    
    public int getidUtilisateur(){
        return this.idUtilisateur;
    }
    
    public int getNumero(){
        return this.numero;
    }
    
    public int getIdGroupe(){
        return this.idGroupe;
    }
}
