/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package planning;
import modele.*;
import controleur.*;
import vue.*;
import java.sql.SQLException;

/**Classe où l'on va lancer l'application
 *
 * @author simon
 */
public class Planning {

    /**
     * @param args the command line arguments 
     */
    @SuppressWarnings("Convert2Lambda")
    public static void main(String[] args) {
       try{
           Connexion connexion = new Connexion("planning", "root", "");
           
           //Création de la fenêtre
           java.awt.EventQueue.invokeLater(new Runnable() { //Permet de garder le visuel fluide tout en effectuant des actions en arrière plan
                @Override
                public void run() {
                    new Fenetre(connexion).setVisible(true);
                }
            });
           
           
       } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.toString());
        }
       
       
    }
    
}
