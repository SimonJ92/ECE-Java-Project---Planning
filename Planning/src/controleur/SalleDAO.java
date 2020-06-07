/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Salle;
import modele.Site;

/**
 *
 * @author simon
 */
public class SalleDAO extends DAO<Salle>{

    /**
     *Constructeur
     * @param connexion
     */
    public SalleDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Salle obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO salle VALUES('0', '"+obj.getNom()+"', '"+obj.getCapacite()+"', '"+obj.getSite().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Salle obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM salle WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Salle obj) {
        try{
            return connexion.effectuerUpdate("UPDATE salle SET nom = '"+obj.getNom()+"', capacite = "+obj.getCapacite()+" where id_site = "+obj.getSite().getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Salle find(int id) {
        Salle salle = new Salle();
        DAO<Site> siteDAO = new SiteDAO(connexion);
        try{
            Statement stmt2 = connexion.getConnection().createStatement();
            ResultSet resultat = stmt2.executeQuery("SELECT * FROM salle WHERE id = "+id);
            if(resultat.first()){
                Site site = siteDAO.find(resultat.getInt("ID_SITE"));
                salle = new Salle(
                            id,
                            resultat.getString("NOM"),
                            resultat.getInt("CAPACITE"),
                            site
                    );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return salle;
    }

    @Override
    public Salle find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
