/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Utilisateur;

/**
 *
 * @author simon
 */
public class UtilisateurDAO extends DAO<Utilisateur>{

    public UtilisateurDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Utilisateur obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO utilisateur VALUES('', '"
                                                                               +obj.getEmail()+"', '"
                                                                               +obj.getPasswd()+"', '"
                                                                               +obj.getNom()+"', '"
                                                                               +obj.getPrenom()+"', '"
                                                                               +obj.getDroit()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Utilisateur obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM utilisateur WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Utilisateur obj) {
        try{
            return connexion.effectuerUpdate("UPDATE utilisateur SET email = '"+obj.getEmail()+"',"
                                                                 +" passwd = '"+obj.getPasswd()+"',"
                                                                 +" nom = '"+obj.getNom()+"',"
                                                                 +" prenom = '"+obj.getPrenom()+"',"
                                                                 +" droit = '"+obj.getDroit()+"'"
                                                                 + " WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Utilisateur find(int id) {
        Utilisateur utilisateur = new Utilisateur();
        
        try{
            ResultSet resultat = connexion.effectuerRequete("SELECT * FROM utilisateur WHERE id = "+id);
            if(resultat.first()){
                utilisateur = new Utilisateur(
                        id,
                        resultat.getString("EMAIL"),
                        resultat.getString("PASSWD"),
                        resultat.getString("NOM"),
                        resultat.getString("PRENOM"),
                        resultat.getInt("DROIT")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return utilisateur;
    }

    @Override
    public Utilisateur find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
