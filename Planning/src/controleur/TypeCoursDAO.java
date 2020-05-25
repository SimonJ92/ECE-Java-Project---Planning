/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.TypeCours;

/**
 *
 * @author simon
 */
public class TypeCoursDAO extends DAO<TypeCours>{
    
    public TypeCoursDAO(Connexion connexion){
        super(connexion);
    }
    
    @Override
    public boolean create(TypeCours obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO type_cours VALUES('', '"+obj.getNom()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(TypeCours obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM type_cours WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(TypeCours obj) {
        try{
            return connexion.effectuerUpdate("UPDATE type_cours SET nom = '"+obj.getNom()+"' where id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public TypeCours find(int id) {
        TypeCours typeCours = new TypeCours();
        
        try{
            ResultSet resultat = connexion.effectuerRequete("SELECT * FROM type_cours WHERE id = "+id);
            if(resultat.first()){
                typeCours = new TypeCours(
                        id,
                        resultat.getString("NOM")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return typeCours;
    }

    @Override
    public TypeCours find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
