/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;
import java.text.ParseException;
import modele.*;
import controleur.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
           DAO<Cours> coursDAO = new CoursDAO(new Connexion("planning", "root", ""));
           System.out.println(coursDAO.delete(new Cours(7,"Prog C")));
       } catch (SQLException e) {
            System.out.println(e.toString());
        } catch (ClassNotFoundException e) {
            System.out.println(e.toString());
        }
    }
    
}
