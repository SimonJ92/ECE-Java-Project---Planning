/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Groupe;
import modele.Promotion;

/**
 *
 * @author simon
 */
public class GroupeDAO extends DAO<Groupe>{

    public GroupeDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Groupe obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO groupe VALUES('', '"+obj.getNom()+"', '"+obj.getPromotion().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Groupe obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM groupe WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Groupe obj) {
        try{
            return connexion.effectuerUpdate("UPDATE groupe SET nom = '"+obj.getNom()+"', idpromotion = "+obj.getPromotion().getId()+" where id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override   
    public Groupe find(int id) {
        Groupe groupe = new Groupe();
        DAO<Promotion> promotionDAO = new PromotionDAO(connexion);
        try{
            Statement stmt2 = connexion.getConnection().createStatement();
            ResultSet resultat = stmt2.executeQuery("SELECT * FROM groupe WHERE id = "+id);
            if(resultat.first()){
                Promotion promotion = promotionDAO.find(resultat.getInt("IDPROMOTION"));
                groupe = new Groupe(
                            id,
                            resultat.getString("NOM"),
                            promotion
                    );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return groupe;
    }

    @Override
    public Groupe find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
