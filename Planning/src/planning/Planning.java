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
           DAO<Salle> salleDAO = new SalleDAO(new Connexion("planning", "root", ""));
           Salle salle = new Salle(6, "G 0010", 30, new Site(4, "Eifeel 4"));
           System.out.println(salleDAO.delete(salle));
           System.out.println(salleDAO.create(salle));
           System.out.println(salleDAO.update(salle));
           System.out.println(salleDAO.find(6).getSite().getNom());
           System.out.println(salleDAO.find(6).getNom());
       } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
    
}
