/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Groupe;
import modele.Seance;
import modele.SeanceGroupes;

/**
 *
 * @author simon
 */
public class SeanceGroupesDAO extends DAO<SeanceGroupes>{

    public SeanceGroupesDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(SeanceGroupes obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO seance_groupes VALUES('"+obj.getSeance().getId()+"', '"+obj.getGroupe().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(SeanceGroupes obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM seance_groupes WHERE id_seance = "+obj.getSeance().getId()+" AND id_groupe = "+obj.getGroupe().getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(SeanceGroupes obj) {
        throw new UnsupportedOperationException("This method isn't needed."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceGroupes find(int id) {
        throw new UnsupportedOperationException("The finding method requires 2 arguments."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceGroupes find(int idSeance, int idGroupe) {
        SeanceGroupes seanceGroupes = new SeanceGroupes();
        
        try{
            DAO<Seance> seanceDAO = new SeanceDAO(connexion);
            DAO<Groupe> groupeDAO = new GroupeDAO(connexion);
            Seance seance = seanceDAO.find(idSeance);
            Groupe groupe = groupeDAO.find(idGroupe);
            seanceGroupes = new SeanceGroupes(
                        seance,
                        groupe
                );
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        return seanceGroupes;
    }
    
}
