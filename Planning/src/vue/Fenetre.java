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
    private JPanel panneauModifSalle;
    private JPanel panneauModifEnseignant;
    private JPanel panneauModifEtudiant;
    private JPanel panneauModifSeance;
    
    
    //ÉLÉMENTS DES PANELS
    
    //À utiliser quand on veut faire une modification ou consulter un EDT/récap
    private int selectedWeek = 0;
    private Salle salleSelection;
    private Utilisateur enseignantSelection;
    private Etudiant etudiantSelection;
    private Seance seanceSelection;
    
    //Emploi du temps - Grille - Salle
    
    
    //Emploi du temps - Grille - Enseignant
    
    
    //Emploi du temps - Grille - Étudiant
    
    
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
    private JButton boutonAllerModifierSalle;
    private JButton boutonAllerModifierEnseignant;
    private JButton boutonAllerModifierEtudiant;
    
    //Modification de Salle
    private JTextField modificationNomSalle;
    private JTextField modificationCapaciteSalle;
    private JComboBox modificationSiteSalle;
    private JButton modificationSalleConfirmer;
    
    //Modification d'Enseignant
    
    
    //Modification d'Étudiant
    
    
    //Modification de Séance
    
    
    
    //Constructeur à appeler pour démarrer l'appli
    public Fenetre(Connexion myConnexion){
        try{
            //récupération de la connexion à la BDD
            this.connexion = myConnexion;
            
            //initialisation des différents panels et leurs composants
            initComponent();

            //paramétrage de la fenêtere
            setSize(1920,1040);
            setTitle("Planning");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setVisible(true);

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
        
        //RECHERCHE
        try{
            //Dépendances des ComboBoxs
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
            
            //Effectuer la recherche
            else if(source == boutonRechercherSalle){
                salleSelection = (Salle)rechercheChoixSalle.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleSalle();
                cardLayout.show(global, "EDTGrilleSalle");
            }
            else if(source == boutonRechercherEnseignant){
                enseignantSelection = (Utilisateur)rechercheChoixEnseignant.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleEnseignant();
                cardLayout.show(global, "EDTGrilleEnseignant");
            }
            else if(source == boutonRechercherEtudiant){
                etudiantSelection = (Etudiant)rechercheChoixEtudiant.getSelectedItem();
                selectedWeek = (int)rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrilleEtudiant();
                cardLayout.show(global, "EDTGrilleEtudiant");
            }
            
            //Effectuer une modification sur l'élément
            else if(source == boutonAllerModifierSalle){
                salleSelection = (Salle)rechercheChoixSalle.getSelectedItem();
                remplirModifSalle();
                cardLayout.show(global, "ModifSalle");
            }
            else if(source == boutonAllerModifierEnseignant){
                enseignantSelection = (Utilisateur)rechercheChoixEnseignant.getSelectedItem();
                remplirModifEnseignant();
                cardLayout.show(global, "ModifEnseignant");
            }
            else if(source == boutonAllerModifierEtudiant){
                etudiantSelection = (Etudiant)rechercheChoixEtudiant.getSelectedItem();
                remplirModifEtudiant();
                cardLayout.show(global, "ModifEtudiant");
            }
        }
        catch(SQLException e){
            System.out.println("ici "+e.toString()); 
        }
        
        //MODIFICATIONS
        try{
            
        }
        catch(Exception e){
            
        }
    }
    
    //Fonctions
    private void initDAO(){
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
    }
    
    private void initPanels(){
        panneauEDTGrilleSalle = new JPanel();
        panneauEDTGrilleEnseignant = new JPanel();
        panneauEDTGrilleEtudiant = new JPanel();
        panneauRecherche = new JPanel();
        panneauModifSalle = new JPanel();
        panneauModifEnseignant = new JPanel();
        panneauModifEtudiant = new JPanel();
        panneauModifSeance = new JPanel();
    }
    
    private void initComponent() throws SQLException {
        //Éléments de connexion BDD
        statementFenetre = connexion.getConnection().createStatement();
        statementEvent = connexion.getConnection().createStatement();

        //DAO
        initDAO();
        
        //INITIALISATION DES PANELS
        initPanels();
        
        //Éléments de recherche/modification
        selectedWeek = 0;
        salleSelection = new Salle();
        enseignantSelection = new Utilisateur();
        etudiantSelection = new Etudiant();
        seanceSelection = new Seance();
        
        //Panel Emploi du temps - Grille - Salle
        remplirEDTGrilleSalle();

        //Panel Emploi du temps - Grille - Enseignant
        remplirEDTGrilleEnseignant();

        //Panel Emploi du temps - Grille - Étudiant
        remplirEDTGrilleEtudiant();

        //Panel Recherche
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
        boutonAllerModifierSalle = new JButton("Modifier les informations de la salle");
        boutonAllerModifierEnseignant = new JButton("Modifier les informations de l'enseignant");
        boutonAllerModifierEtudiant = new JButton("Modifier les informations de l'étudiant");
        
        remplirRecherche();
        
        rechercheChoixSite.addActionListener(this);
        rechercheChoixPromotion.addActionListener(this);
        rechercheChoixGroupe.addActionListener(this);
        boutonRechercherSalle.addActionListener(this);
        boutonRechercherEnseignant.addActionListener(this);
        boutonRechercherEtudiant.addActionListener(this);
        boutonAllerModifierSalle.addActionListener(this);
        boutonAllerModifierEnseignant.addActionListener(this);
        boutonAllerModifierEtudiant.addActionListener(this);

        //Panel Modification de Salle
        modificationNomSalle = new JTextField();
        modificationCapaciteSalle = new JTextField();
        modificationSiteSalle = new JComboBox();
        modificationSalleConfirmer = new JButton("Appliquer les modifications");
        
        remplirModifSalle();
        
        modificationNomSalle.addActionListener(this);
        modificationCapaciteSalle.addActionListener(this);
        modificationSiteSalle.addActionListener(this);
        modificationSalleConfirmer.addActionListener(this);

        //Panel Modification d'Enseignant
        remplirModifEnseignant();

        //Panel Modification d'Étudiant
        remplirModifEtudiant();
        
        //Panel Modification de Séance
        remplirModifSeance();

        //Initialisation du conteneur global des panneaux
        cardLayout = new CardLayout();
        global = new JPanel(cardLayout);

        //On va ajouter dans le panel global tous les différents panels
        global.add(panneauEDTGrilleSalle, "EDTGrilleSalle");
        global.add(panneauEDTGrilleEnseignant, "EDTGrilleEnseignant");
        global.add(panneauEDTGrilleEtudiant, "EDTGrilleEtudiant");
        global.add(panneauRecherche, "Recherche");
        global.add(panneauModifSalle, "ModifSalle");
        global.add(panneauModifEnseignant, "ModifEnseignant");
        global.add(panneauModifEtudiant, "ModifEtudiant");
        global.add(panneauModifSeance, "ModifSeance");
    }
    
    
    private Utilisateur login(String email, String passwd) throws SQLException{
        Utilisateur utilisateur = null;
        resultatFenetre = statementFenetre.executeQuery("SELECT * FROM utilisateur WHERE email = '"+email+"' AND passwd = '"+passwd+"'");
        if(resultatFenetre.first()){
            utilisateur = utilisateurDAO.find(resultatFenetre.getInt("ID"));
        }
        resultatFenetre.close();
        return utilisateur;
    }
    
    //TODO
    private void remplirEDTGrilleSalle(){
        panneauEDTGrilleSalle.removeAll();
        panneauEDTGrilleSalle.add(new JLabel("EDT Salle : "+salleSelection.toString()));
        panneauEDTGrilleSalle.add(new JLabel("Semaine : "+selectedWeek));
    }
    
    //TODO
    private void remplirEDTGrilleEnseignant(){
        panneauEDTGrilleEnseignant.removeAll();
        panneauEDTGrilleEnseignant.add(new JLabel("EDT Enseignant : "+enseignantSelection.toString()));
        panneauEDTGrilleEnseignant.add(new JLabel("Semaine : "+selectedWeek));
    }
    
    //TODO
    private void remplirEDTGrilleEtudiant(){
        panneauEDTGrilleEtudiant.removeAll();
        panneauEDTGrilleEtudiant.add(new JLabel("EDT Etudiant : "+etudiantSelection.toString()));
        panneauEDTGrilleEtudiant.add(new JLabel("Semaine : "+selectedWeek));
    }
    
    //TODO
    private void remplirRecherche() throws SQLException {
        rechercheChoixSemaine.removeAll();
        rechercheChoixSemaine.removeAllItems();
        for (int i = 1; i <= 52; ++i) {
            rechercheChoixSemaine.addItem(i);
        }
        panneauRecherche.add(rechercheChoixSemaine);
        
        rechercheChoixSite.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            rechercheChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixSite);

        rechercheChoixSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site) rechercheChoixSite.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixSalle);

        rechercheChoixEnseignant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur WHERE droit = 3");
        while (resultatFenetre.next()) {
            rechercheChoixEnseignant.addItem(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixEnseignant);

        rechercheChoixPromotion.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
        while (resultatFenetre.next()) {
            rechercheChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixPromotion);

        rechercheChoixGroupe.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) rechercheChoixPromotion.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixGroupe);

        rechercheChoixEtudiant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = " + ((Groupe) rechercheChoixGroupe.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
        }
        panneauRecherche.add(rechercheChoixEtudiant);

        //Boutons de recherche
        panneauRecherche.add(boutonRechercherSalle);
        panneauRecherche.add(boutonRechercherEnseignant);
        panneauRecherche.add(boutonRechercherEtudiant);
        
        //Boutons de modification
        panneauRecherche.add(boutonAllerModifierSalle);
        panneauRecherche.add(boutonAllerModifierEnseignant);
        panneauRecherche.add(boutonAllerModifierEtudiant);
    }
    
    //TODO (ne pas oublier de définir une taille pour les TextFields)
    private void remplirModifSalle() throws SQLException{
        panneauModifSalle.removeAll();
        
        modificationNomSalle.setText(salleSelection.getNom());
        panneauModifSalle.add(modificationNomSalle);
        
        modificationCapaciteSalle.setText(""+salleSelection.getCapacite());
        panneauModifSalle.add(modificationCapaciteSalle);
        
        /*private JComboBox modificationSiteSalle;*/
        modificationSiteSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            modificationSiteSalle.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauModifSalle.add(modificationSiteSalle);
        
        panneauModifSalle.add(modificationSalleConfirmer);
    }
    
    //TODO
    private void remplirModifEnseignant(){
        panneauModifEnseignant.removeAll();
        panneauModifEnseignant.add(new JLabel("Modifier Enseignant : "+enseignantSelection.toString()));
    }
    
    //TODO
    private void remplirModifEtudiant(){
        panneauModifEtudiant.removeAll();
        panneauModifEtudiant.add(new JLabel("Modifier Etudiant : "+etudiantSelection.toString()));
    }
    
    private void remplirModifSeance(){
        panneauModifSeance.removeAll();
        panneauModifSeance.add(new JLabel("Modifier Séance : "+seanceSelection.toString()));
    }
}
