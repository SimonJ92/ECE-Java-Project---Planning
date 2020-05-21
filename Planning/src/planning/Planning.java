/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;
import modele.*;
import controleur.*;
import java.sql.SQLException;

/**
 *
 * @author simon
 */
public class Planning {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       try{
           DAO<Utilisateur> utilisateurDAO = new UtilisateurDAO(new Connexion("planning", "root", ""));
           System.out.println(utilisateurDAO.create(new Utilisateur(18,"empty","empty","empty","empty",4)));
           System.out.println(utilisateurDAO.update(new Utilisateur(18,"empty","empty","moi","empty",4)));
           System.out.println(utilisateurDAO.find(18).getNom());
           System.out.println(utilisateurDAO.delete(new Utilisateur(18,"empty","empty","empty","empty",4)));
       } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
    
}
