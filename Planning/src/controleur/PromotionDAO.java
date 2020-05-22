/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Promotion;

/**
 *
 * @author simon
 */
public class PromotionDAO extends DAO<Promotion>{

    public PromotionDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Promotion obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO promotion VALUES('"+obj.getId()+"', '"+obj.getNom()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Promotion obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM promotion WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Promotion obj) {
        try{
            return connexion.effectuerUpdate("UPDATE promotion SET nom = '"+obj.getNom()+"' where id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Promotion find(int id) {
        Promotion promotion = new Promotion();
        
        try{
            ResultSet resultat = connexion.effectuerRequete("SELECT * FROM promotion WHERE id = "+id);
            if(resultat.first()){
                promotion = new Promotion(
                        id,
                        resultat.getString("NOM")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return promotion;
    }

    @Override
    public Promotion find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
