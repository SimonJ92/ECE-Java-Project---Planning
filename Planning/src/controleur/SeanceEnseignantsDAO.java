/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Seance;
import modele.SeanceEnseignants;
import modele.Utilisateur;

/**
 *
 * @author simon
 */
public class SeanceEnseignantsDAO extends DAO<SeanceEnseignants>{

    public SeanceEnseignantsDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(SeanceEnseignants obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO seance_enseignants VALUES('"+obj.getSeance().getId()+"', '"+obj.getUtilisateur().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(SeanceEnseignants obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM seance_enseignants WHERE id_seance = "+obj.getSeance().getId()+" AND id_enseignant = "+obj.getUtilisateur().getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(SeanceEnseignants obj) {
        throw new UnsupportedOperationException("This method isn't needed."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceEnseignants find(int id) {
        throw new UnsupportedOperationException("The finding method requires 2 arguments."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceEnseignants find(int idSeance, int idUtilisateur) {
        SeanceEnseignants seanceEnseignants = new SeanceEnseignants();
        
        try{
            DAO<Seance> seanceDAO = new SeanceDAO(connexion);
            DAO<Utilisateur> utilisateurDAO = new UtilisateurDAO(connexion);
            Seance seance = seanceDAO.find(idSeance);
            Utilisateur utilisateur = utilisateurDAO.find(idUtilisateur);
            seanceEnseignants = new SeanceEnseignants(
                        seance,
                        utilisateur
                );
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        return seanceEnseignants;
    }
    
}
