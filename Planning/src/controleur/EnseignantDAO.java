/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Cours;
import modele.Enseignant;
import modele.Utilisateur;

/**
 *
 * @author simon
 */
public class EnseignantDAO extends DAO<Enseignant>{

    /**
     *Constructeur
     * @param connexion
     */
    public EnseignantDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Enseignant obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO enseignant VALUES('"+obj.getIdUtilisateur()+"', '"+obj.getCours().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Enseignant obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM enseignant WHERE id_utilisateur = "+obj.getIdUtilisateur()+" AND id_cours = "+obj.getCours().getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Enseignant obj) {
        throw new UnsupportedOperationException("This method isn't needed."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Enseignant find(int id) {
        throw new UnsupportedOperationException("The finding method requires 2 arguments."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public Enseignant find(int idUtilisateur, int idCours) {
        Enseignant enseignant = new Enseignant();
        
        try{
            DAO<Cours> coursDAO = new CoursDAO(connexion);
            DAO<Utilisateur> utilisateurDAO = new UtilisateurDAO(connexion);
            Cours cours = coursDAO.find(idCours);
            Utilisateur utilisateur = utilisateurDAO.find(idUtilisateur);
            enseignant = new Enseignant(
                        idUtilisateur,
                        cours,
                        utilisateur.getEmail(),
                        utilisateur.getPasswd(),
                        utilisateur.getNom(),
                        utilisateur.getPrenom(),
                        utilisateur.getDroit()
                );
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        return enseignant;
    }

}
