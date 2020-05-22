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
           DAO<SeanceEnseignants> seanceEnseignantsDAO = new SeanceEnseignantsDAO(new Connexion("planning", "root", ""));
           Seance seance = new Seance(1, 1, new MyDate(21,05,2020), new MyHour(8,30), new MyHour(10,0), 1, new Cours(1,"Electromagnetisme"), new TypeCours(1,"Cours interactif"));
           Utilisateur utilisateur = new Utilisateur(1, "trminot@gmail.com", "tm", "Minot", "Thierry", 3);
           SeanceEnseignants seanceEnseignants = new SeanceEnseignants(seance, utilisateur);
           System.out.println(seanceEnseignantsDAO.delete(seanceEnseignants));
           System.out.println(seanceEnseignantsDAO.create(seanceEnseignants));
           System.out.println(seanceEnseignantsDAO.find(1,1).getUtilisateur().getNom());
           System.out.println(seanceEnseignantsDAO.find(1,1).getSeance().getDate().toString());
       } catch (SQLException | ClassNotFoundException | ParseException e) {
            System.out.println(e.toString());
        }
    }
    
}
