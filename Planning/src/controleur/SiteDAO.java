/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Site;
/**
 *
 * @author simon
 */
public class SiteDAO extends DAO<Site>{
    
    public SiteDAO(Connexion connexion){
        super(connexion);
    }
    
    @Override
    public boolean create(Site obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO site VALUES('', '"+obj.getNom()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }    }

    @Override
    public boolean delete(Site obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM site WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Site obj) {
        try{
            return connexion.effectuerUpdate("UPDATE site SET nom = '"+obj.getNom()+"' where id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Site find(int id) {
        Site site = new Site();
        
        try{
            ResultSet resultat = connexion.effectuerRequete("SELECT * FROM site WHERE id = "+id);
            if(resultat.first()){
                site = new Site(
                        id,
                        resultat.getString("NOM")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return site;
    }

    @Override
    public Site find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
