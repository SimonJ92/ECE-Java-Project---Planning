/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;
import modele.*;
import controleur.*;
import java.sql.SQLException;
import java.text.ParseException;

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
           DAO<Seance> seanceDAO = new SeanceDAO(new Connexion("planning", "root", ""));
           Seance seance = new Seance(1, 1, new MyDate(21,05,2020), new MyHour(8,30), new MyHour(10,0), 1, new Cours(1,"Electromagnetisme"), new TypeCours(1,"Cours interactif"));
           System.out.println(seanceDAO.delete(seance));
           System.out.println(seanceDAO.create(seance));
           System.out.println(seanceDAO.update(seance));
           System.out.println(seanceDAO.find(1).getDate().toString());
           System.out.println(seanceDAO.find(1).getHeureFin().toString());
       } catch (SQLException | ClassNotFoundException | ParseException e) {
            System.out.println(e.toString());
        }
    }
    
}
