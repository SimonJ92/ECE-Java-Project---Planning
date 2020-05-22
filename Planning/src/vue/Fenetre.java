/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;
import controleur.*;
import modele.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
import javax.swing.*;
import java.util.*;

/**
 *
 * @author simon
 */
public class Fenetre extends JFrame implements ActionListener{
    private Connexion connexion;
    private Statement statementFenetre;
    private Statement statementEvent;
    private ResultSet resultatFenetre;
    private ResultSet resultatEvent;
    
    //Layout global
    private CardLayout cardLayout;  //Pour gérer les transitions entre les différents écrans
    private JPanel global;
    
    //Panels
    private JPanel panneauRecherche; // 3 recherches : Elève, Salle et Enseignant
    
    
    //Éléments des panels
    JComboBox rechercheChoixSite;
    JComboBox rechercheChoixSalle;
    
    private JComboBox combo1;
    private JComboBox combo2;
    
    
    //Constructeur à appeler pour démarrer l'appli
    public Fenetre(Connexion myConnexion){
        try{
            //récupération de la connexion à la BDD
            this.connexion = myConnexion;
            statementFenetre = connexion.getConnection().createStatement();
            statementEvent = connexion.getConnection().createStatement();

            //paramétrage de la fenêtere
            setSize(1920,1040);
            setTitle("Planning");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setVisible(true);

            //Initialisation des Panels
            panneauRecherche = new JPanel();

            rechercheChoixSite = new JComboBox();
            rechercheChoixSalle = new JComboBox();
            
            DAO<Site> siteDAO = new SiteDAO(connexion);
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
            while(resultatFenetre.next()){
                rechercheChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixSite);
            rechercheChoixSite.addActionListener(this);
            
            DAO<Salle> salleDAO = new SalleDAO(connexion);
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)rechercheChoixSite.getSelectedItem()).getId());
            while(resultatFenetre.next()){
                rechercheChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixSalle);
            rechercheChoixSalle.addActionListener(this);


            //Initialisation du conteneur global des panneaux
            cardLayout = new CardLayout();
            global = new JPanel(cardLayout);

            //On va ajouter dans le panel global tous les différents panels
            global.add(panneauRecherche, "recherche");

            //On définit le panneau d'accueil
            cardLayout.show(global, "recherche");

            //On affiche le panneau global
            setContentPane(global);
        }
        catch(SQLException e){
            System.out.println("la "+e.toString());
        }
    }

    //Gestion du comportement dans l'appli
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        try{
            if(source == rechercheChoixSite){
                rechercheChoixSalle.removeAllItems();
                 DAO<Salle> salleDAO = new SalleDAO(connexion);
                resultatEvent = statementEvent.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)rechercheChoixSite.getSelectedItem()).getId());
                while(resultatEvent.next()){
                    rechercheChoixSalle.addItem(salleDAO.find(resultatEvent.getInt("ID")));
                }
            }
        }
        catch(SQLException e){
            System.out.println("ici "+e.toString());
        }
    }
}
