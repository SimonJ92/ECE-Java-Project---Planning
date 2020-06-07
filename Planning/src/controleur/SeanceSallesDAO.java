/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Salle;
import modele.Seance;
import modele.SeanceSalles;

/**
 *
 * @author simon
 */
public class SeanceSallesDAO extends DAO<SeanceSalles>{

    /**
     *Constructeur
     * @param connexion
     */
    public SeanceSallesDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(SeanceSalles obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO seance_salles VALUES('"+obj.getSeance().getId()+"', '"+obj.getSalle().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(SeanceSalles obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM seance_salles WHERE id_seance = "+obj.getSeance().getId()+" AND id_salle = "+obj.getSalle().getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(SeanceSalles obj) {
        throw new UnsupportedOperationException("This method isn't needed."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceSalles find(int id) {
        throw new UnsupportedOperationException("The finding method requires 2 arguments."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SeanceSalles find(int idSeance, int idSalle) {
        SeanceSalles seanceSalles = new SeanceSalles();
        
        try{
            DAO<Seance> seanceDAO = new SeanceDAO(connexion);
            DAO<Salle> salleDAO = new SalleDAO(connexion);
            Seance seance = seanceDAO.find(idSeance);
            Salle salle = salleDAO.find(idSalle);
            seanceSalles = new SeanceSalles(
                        seance,
                        salle
                );
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        return seanceSalles;
    }
    
}
