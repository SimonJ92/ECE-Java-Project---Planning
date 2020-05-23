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
    
    //Éléments de connexion à la base de données
    private Connexion connexion;
    private Statement statementFenetre;
    private Statement statementEvent;
    private ResultSet resultatFenetre;
    private ResultSet resultatEvent;
    
    //DAO
    private DAO<Cours> coursDAO;
    private DAO<Enseignant> enseignantDAO;
    private DAO<Etudiant> etudiantDAO;
    private DAO<Groupe> groupeDAO;
    private DAO<Promotion> promotionDAO;
    private DAO<Salle> salleDAO;
    private DAO<Seance> seanceDAO;
    private DAO<SeanceEnseignants> seanceEnseignantsDAO;
    private DAO<SeanceGroupes> seanceGroupesDAO;
    private DAO<SeanceSalles> seanceSallesDAO;
    private DAO<Site> siteDAO;
    private DAO<TypeCours> typeCoursDAO;
    private DAO<Utilisateur> utilisateurDAO;
    
    //Layout global
    private CardLayout cardLayout;  //Pour gérer les transitions entre les différents écrans
    private JPanel global;
    
    //Panels
    private JPanel panneauEDTGrilleSalle;
    private JPanel panneauEDTGrilleEnseignant;
    private JPanel panneauEDTGrilleEtudiant;
    private JPanel panneauRecherche; // 3 recherches : Elève, Salle et Enseignant
    
    
    //ÉLÉMENTS DES PANELS
    
    private int selectedWeek = 0;
    //Emploi du temps - Grille - Salle
    private Salle salleSelection;
    
    //Emploi du temps - Grille - Enseignant
    private Utilisateur enseignantSelection;
    
    //Emploi du temps - Grille - Étudiant
    private Etudiant etudiantSelection;
    
    //Recherche
    private JComboBox rechercheChoixSemaine;
    private JComboBox rechercheChoixSite;
    private JComboBox rechercheChoixSalle;
    private JComboBox rechercheChoixEnseignant;
    private JComboBox rechercheChoixPromotion;
    private JComboBox rechercheChoixGroupe;
    private JComboBox rechercheChoixEtudiant;
    private JButton boutonRechercherSalle;
    private JButton boutonRechercherEnseignant;
    private JButton boutonRechercherEtudiant;
    
    //...
    
    
    //Constructeur à appeler pour démarrer l'appli
    public Fenetre(Connexion myConnexion){
        try{
            //récupération de la connexion à la BDD
            this.connexion = myConnexion;
            statementFenetre = connexion.getConnection().createStatement();
            statementEvent = connexion.getConnection().createStatement();
            
            //Initialisation des DAO
            coursDAO = new CoursDAO(connexion);
            enseignantDAO = new EnseignantDAO(connexion);
            etudiantDAO = new EtudiantDAO(connexion);
            groupeDAO = new GroupeDAO(connexion);
            promotionDAO = new PromotionDAO(connexion);
            salleDAO = new SalleDAO(connexion);
            seanceDAO = new SeanceDAO(connexion);
            seanceEnseignantsDAO = new SeanceEnseignantsDAO(connexion);
            seanceGroupesDAO = new SeanceGroupesDAO(connexion);
            seanceSallesDAO = new SeanceSallesDAO(connexion);
            siteDAO = new SiteDAO(connexion);
            typeCoursDAO = new TypeCoursDAO(connexion);
            utilisateurDAO = new UtilisateurDAO(connexion);

            //paramétrage de la fenêtere
            setSize(1920,1040);
            setTitle("Planning");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setVisible(true);

            //INITIALISATION DES PANELS
            
            //Panel Emploi du temps - Grille - Salle
            panneauEDTGrilleSalle = new JPanel();
            
            salleSelection = new Salle();
            //Autres éléments de la page
            remplirEDTGrilleSalle(panneauEDTGrilleSalle);
            
            
            //Panel Emploi du temps - Grille - Enseignant
            panneauEDTGrilleEnseignant = new JPanel();
            
            enseignantSelection = new Utilisateur();
            //Autres éléments de la page
            remplirEDTGrilleEnseignant(panneauEDTGrilleEnseignant);
            
            
            //Panel Emploi du temps - Grille - Étudiant
            panneauEDTGrilleEtudiant = new JPanel();
            
            etudiantSelection = new Etudiant();
            //Autres éléments de la page
            remplirEDTGrilleEtudiant(panneauEDTGrilleEtudiant);
            
            
            //Panel Recherche
            panneauRecherche = new JPanel();

            rechercheChoixSemaine = new JComboBox();
            rechercheChoixSite = new JComboBox();
            rechercheChoixSalle = new JComboBox();
            rechercheChoixEnseignant = new JComboBox();
            rechercheChoixPromotion = new JComboBox();
            rechercheChoixGroupe = new JComboBox();
            rechercheChoixEtudiant = new JComboBox();
            boutonRechercherSalle = new JButton("Rechercher une salle");
            boutonRechercherEnseignant = new JButton("Rechercher un enseignant");
            boutonRechercherEtudiant = new JButton("Rechercher un etudiant");
            
            
            for(int i = 1 ; i <= 52 ; ++i){
                rechercheChoixSemaine.addItem(i);
            }
            panneauRecherche.add(rechercheChoixSemaine);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
            while(resultatFenetre.next()){
                rechercheChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixSite);
            rechercheChoixSite.addActionListener(this);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)rechercheChoixSite.getSelectedItem()).getId());
            while(resultatFenetre.next()){
                rechercheChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixSalle);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur WHERE droit = 3");
            while(resultatFenetre.next()){
                rechercheChoixEnseignant.addItem(utilisateurDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixEnseignant);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
            while(resultatFenetre.next()){
                rechercheChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixPromotion);
            rechercheChoixPromotion.addActionListener(this);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)rechercheChoixPromotion.getSelectedItem()).getId());
            while(resultatFenetre.next()){
                rechercheChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
            }
            panneauRecherche.add(rechercheChoixGroupe);
            rechercheChoixGroupe.addActionListener(this);
            
            resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = "+((Groupe)rechercheChoixGroupe.getSelectedItem()).getId());
            while(resultatFenetre.next()){
                rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
            }
            panneauRecherche.add(rechercheChoixEtudiant);
            
            panneauRecherche.add(boutonRechercherSalle);
            boutonRechercherSalle.addActionListener(this);
            
            panneauRecherche.add(boutonRechercherEnseignant);
            boutonRechercherEnseignant.addActionListener(this);
            
            panneauRecherche.add(boutonRechercherEtudiant);
            boutonRechercherEtudiant.addActionListener(this);
            
            //..

            //Initialisation du conteneur global des panneaux
            cardLayout = new CardLayout();
            global = new JPanel(cardLayout);

            //On va ajouter dans le panel global tous les différents panels
            global.add(panneauEDTGrilleSalle, "EDTGrilleSalle");
            global.add(panneauEDTGrilleEnseignant, "EDTGrilleEnseignant");
            global.add(panneauEDTGrilleEtudiant, "EDTGrilleEtudiant");
            global.add(panneauRecherche, "Recherche");

            //On définit le panneau d'accueil (à changer)
            cardLayout.show(global, "Recherche");

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
            
            //Recherche
            if(source == rechercheChoixSite){
                rechercheChoixSalle.removeAllItems();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)rechercheChoixSite.getSelectedItem()).getId());
                while(resultatEvent.next()){
                    rechercheChoixSalle.addItem(salleDAO.find(resultatEvent.getInt("ID")));
                }
            }
            else if(source == rechercheChoixPromotion){
                rechercheChoixGroupe.removeActionListener(this);
                rechercheChoixGroupe.removeAllItems();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)rechercheChoixPromotion.getSelectedItem()).getId());
                while(resultatEvent.next()){
                    rechercheChoixGroupe.addItem(groupeDAO.find(resultatEvent.getInt("ID")));
                }
                
                
                rechercheChoixEtudiant.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = "+((Groupe)rechercheChoixGroupe.getSelectedItem()).getId());
                while(resultatFenetre.next()){
                    rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
                }
                rechercheChoixGroupe.addActionListener(this);
            }
            else if(source == rechercheChoixGroupe){
                rechercheChoixEtudiant.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = "+((Groupe)rechercheChoixGroupe.getSelectedItem()).getId());
                while(resultatFenetre.next()){
                    rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
                }
            }
            else if(source == boutonRechercherSalle){
                salleSelection = (Salle)rechercheChoixSalle.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleSalle(panneauEDTGrilleSalle);
                cardLayout.show(global, "EDTGrilleSalle");
            }
            else if(source == boutonRechercherEnseignant){
                enseignantSelection = (Utilisateur)rechercheChoixEnseignant.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleEnseignant(panneauEDTGrilleEnseignant);
                cardLayout.show(global, "EDTGrilleEnseignant");
            }
            else if(source == boutonRechercherEtudiant){
                etudiantSelection = (Etudiant)rechercheChoixEtudiant.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleEtudiant(panneauEDTGrilleEtudiant);
                cardLayout.show(global, "EDTGrilleEtudiant");
            }
            
            
        }
        catch(SQLException e){
            System.out.println("ici "+e.toString()); 
        }
    }
    
    private void remplirEDTGrilleSalle(JPanel jpanel){  //TODO
        jpanel.removeAll();
        jpanel.add(new JLabel("EDT Salle : "+salleSelection.toString()));
        jpanel.add(new JLabel("Semaine : "+selectedWeek));
    }
    
    private void remplirEDTGrilleEnseignant(JPanel jpanel){  //TODO
        jpanel.removeAll();
        jpanel.add(new JLabel("EDT Enseignant : "+enseignantSelection.toString()));
        jpanel.add(new JLabel("Semaine : "+selectedWeek));
    }
    
    private void remplirEDTGrilleEtudiant(JPanel jpanel){  //TODO
        jpanel.removeAll();
        jpanel.add(new JLabel("EDT Etudiant : "+etudiantSelection.toString()));
        jpanel.add(new JLabel("Semaine : "+selectedWeek));
    }
}
