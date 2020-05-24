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
import java.text.ParseException;
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
    
    //Modification de Séance
    private JComboBox modifChoixAnnee;
    private JComboBox modifChoixMois;
    private JComboBox modifChoixJour;
    private JSpinner modifChoixHeureDebut;
    private JSpinner modifChoixMinutesDebut;
    private JSpinner modifChoixHeureFin;
    private JSpinner modifChoixMinutesFin;
    private JComboBox modifChoixEtat;
    private JComboBox modifChoixCours;
    private JComboBox modifChoixTypeCours;
    private JComboBox modifChoixEnseignant;
    private JComboBox modifChoixPromotion;
    private JComboBox modifChoixGroupe;
    private JComboBox modifChoixSite;
    private JComboBox modifChoixSalle;
    private JButton modifBoutonEnregistrer;
    private JLabel modifErrorField;
    
    
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
            cardLayout.show(global, "ModifSeance");

            //On affiche le panneau global
            setContentPane(global);
        }
        catch(SQLException e){
            System.out.println("la "+e.toString()); //Supprimer le là : juste utile pour coder
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
        }
        catch(SQLException e){
            System.out.println(e.toString()); 
        }
        
        //MODIFICATION SEANCE
        try{
            if(source == modifChoixAnnee || source == modifChoixMois){
                modifFillDaysOfMonth();
            }
            else if(source == modifChoixCours){
                modifChoixEnseignant.removeAllItems();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                                                            + "WHERE enseignant.ID_COURS = "+((Cours)modifChoixCours.getSelectedItem()).getId());
                while(resultatEvent.next()){
                    modifChoixEnseignant.addItem(utilisateurDAO.find(resultatEvent.getInt("ID")));
                }
            }
            else if(source == modifChoixPromotion){
                modifChoixGroupe.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)modifChoixPromotion.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    modifChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
                }
            }
            else if(source == modifChoixSite){
                modifChoixSalle.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)modifChoixSite.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    modifChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
                }
            }
            else if(source == modifBoutonEnregistrer){
                if(seanceSelection.getId() != 0){ //Cas d'une modification des données
                    modifEnregistrer();
                }else{  //Cas d'une création de séance
                    modifCreer();
                }
            }
        }
        catch(SQLException | ParseException e){
            System.out.println(e.toString());
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
        
        remplirRecherche();
        
        rechercheChoixSite.addActionListener(this);
        rechercheChoixPromotion.addActionListener(this);
        rechercheChoixGroupe.addActionListener(this);
        boutonRechercherSalle.addActionListener(this);
        boutonRechercherEnseignant.addActionListener(this);
        boutonRechercherEtudiant.addActionListener(this);

        //Panel Modification de Séance
        modifChoixAnnee = new JComboBox();
        modifChoixMois = new JComboBox();
        modifChoixJour = new JComboBox();
        modifChoixHeureDebut = new JSpinner();
        modifChoixMinutesDebut = new JSpinner();
        modifChoixHeureFin = new JSpinner();
        modifChoixMinutesFin = new JSpinner();
        modifChoixEtat = new JComboBox();
        modifChoixCours = new JComboBox();
        modifChoixTypeCours = new JComboBox();
        modifChoixEnseignant = new JComboBox();
        modifChoixPromotion = new JComboBox();
        modifChoixGroupe = new JComboBox();
        modifChoixSite = new JComboBox();
        modifChoixSalle = new JComboBox();
        modifBoutonEnregistrer = new JButton("Enregistrer");
        modifErrorField = new JLabel("");
        
        remplirModifSeance();
        
        modifChoixAnnee.addActionListener(this);
        modifChoixMois.addActionListener(this);
        modifChoixCours.addActionListener(this);
        modifChoixPromotion.addActionListener(this);
        modifChoixSite.addActionListener(this);        
        modifBoutonEnregistrer.addActionListener(this);
        
        //...

        //Initialisation du conteneur global des panneaux
        cardLayout = new CardLayout();
        global = new JPanel(cardLayout);

        //On va ajouter dans le panel global tous les différents panels
        global.add(panneauEDTGrilleSalle, "EDTGrilleSalle");
        global.add(panneauEDTGrilleEnseignant, "EDTGrilleEnseignant");
        global.add(panneauEDTGrilleEtudiant, "EDTGrilleEtudiant");
        global.add(panneauRecherche, "Recherche");
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
        //Semaine
        rechercheChoixSemaine.removeAllItems();
        for (int i = 1; i <= 52; ++i) {
            rechercheChoixSemaine.addItem(i);
        }
        panneauRecherche.add(rechercheChoixSemaine);
        
        //Site de la salle
        rechercheChoixSite.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            rechercheChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixSite);

        //Salle
        rechercheChoixSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site)rechercheChoixSite.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixSalle);

        //Enseignant
        rechercheChoixEnseignant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur WHERE droit = 3");
        while (resultatFenetre.next()) {
            rechercheChoixEnseignant.addItem(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixEnseignant);

        //Promotion du groupe de l'élève
        rechercheChoixPromotion.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
        while (resultatFenetre.next()) {
            rechercheChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixPromotion);

        //Groupe de l'élève
        rechercheChoixGroupe.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) rechercheChoixPromotion.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        panneauRecherche.add(rechercheChoixGroupe);

        //Élève
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
    }
    
    //TODO (ne pas oublier de définir une taille pour les TextFields)
    private void remplirModifSeance() throws SQLException{
        seanceSelection = seanceDAO.find(1); //à supprimer, mais pratique pour coder
        
        panneauModifSeance.removeAll();
        panneauModifSeance.add(new JLabel("Modifier Séance : "+seanceSelection.toString()));
        
        //Année
        modifChoixAnnee.removeAllItems();
        for (int i = 2010; i <= 2030; ++i) {
            modifChoixAnnee.addItem(i);
        }
        if(seanceSelection.getId() != 0){   //si une séance a été selectionné = si on est en modification
            modifChoixAnnee.setSelectedItem(seanceSelection.getDate().getAnnee());
        }
        panneauModifSeance.add(modifChoixAnnee);
        
        //Mois
        modifChoixMois.removeAllItems();
        for (int i = 1; i <= 12; ++i) {
            modifChoixMois.addItem(i);
        }
        if(seanceSelection.getId() != 0){   //si une séance a été selectionné = si on est en modification
            modifChoixMois.setSelectedItem(seanceSelection.getDate().getMois());
        }
        panneauModifSeance.add(modifChoixMois);
        
        //Jour
        modifFillDaysOfMonth();
        if(seanceSelection.getId() != 0){   //si une séance a été selectionné = si on est en modification
            modifChoixJour.setSelectedItem(seanceSelection.getDate().getJour());
        }
        panneauModifSeance.add(modifChoixJour);
        
        //Heure de début de séance
        JSpinner.NumberEditor heureDebutSpinnerEditor = new JSpinner.NumberEditor(modifChoixHeureDebut);
        modifChoixHeureDebut.setEditor(heureDebutSpinnerEditor);
        heureDebutSpinnerEditor.getModel().setMinimum(7);
        heureDebutSpinnerEditor.getModel().setMaximum(20);
        heureDebutSpinnerEditor.getModel().setStepSize(1);
        heureDebutSpinnerEditor.getModel().setValue(seanceSelection.getHeureDebut().getHeure());
        panneauModifSeance.add(modifChoixHeureDebut);
        
        //Minute de début de séance
        JSpinner.NumberEditor minutesDebutSpinnerEditor = new JSpinner.NumberEditor(modifChoixMinutesDebut);
        modifChoixMinutesDebut.setEditor(minutesDebutSpinnerEditor);
        minutesDebutSpinnerEditor.getModel().setMinimum(0);
        minutesDebutSpinnerEditor.getModel().setMaximum(59);
        minutesDebutSpinnerEditor.getModel().setStepSize(1);
        minutesDebutSpinnerEditor.getModel().setValue(seanceSelection.getHeureDebut().getMinutes());
        panneauModifSeance.add(modifChoixMinutesDebut);
        
        //Heure de fin de séance
        JSpinner.NumberEditor heureFinSpinnerEditor = new JSpinner.NumberEditor(modifChoixHeureFin);
        modifChoixHeureFin.setEditor(heureFinSpinnerEditor);
        heureFinSpinnerEditor.getModel().setMinimum(7);
        heureFinSpinnerEditor.getModel().setMaximum(20);
        heureFinSpinnerEditor.getModel().setStepSize(1);
        heureFinSpinnerEditor.getModel().setValue(seanceSelection.getHeureFin().getHeure());
        panneauModifSeance.add(modifChoixHeureFin);
        
        //Minute de fin de séance
        JSpinner.NumberEditor minutesFinSpinnerEditor = new JSpinner.NumberEditor(modifChoixMinutesFin);
        modifChoixMinutesFin.setEditor(minutesFinSpinnerEditor);
        minutesFinSpinnerEditor.getModel().setMinimum(0);
        minutesFinSpinnerEditor.getModel().setMaximum(59);
        minutesFinSpinnerEditor.getModel().setStepSize(1);
        minutesFinSpinnerEditor.getModel().setValue(seanceSelection.getHeureFin().getMinutes());
        panneauModifSeance.add(modifChoixMinutesFin);
        
        //État de la séance
        modifChoixEtat.removeAllItems();
        modifChoixEtat.addItem("En cours de validation");
        modifChoixEtat.addItem("Validé");
        modifChoixEtat.addItem("Annulé");
        switch(seanceSelection.getEtat()){
            case 0:
                break;
            case 1:
                modifChoixEtat.setSelectedItem("En cours de validation");
                break;
            case 2:
                modifChoixEtat.setSelectedItem("Validé");
                break;
            case 3:
                modifChoixEtat.setSelectedItem("Annulé");
                break;
            default:
                System.out.println("Erreur dans le choix de l'état : la séance selectionné a un état qui n'est ni 0,1,2,3");
                break;
        }
        panneauModifSeance.add(modifChoixEtat);
        
        //Cours de la séance
        modifChoixCours.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM cours");
        while (resultatFenetre.next()) {
            modifChoixCours.addItem(coursDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixCours.setSelectedItem(seanceSelection.getCours());
        panneauModifSeance.add(modifChoixCours);
        
        //Type de cours
        modifChoixTypeCours.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM type_cours");
        while (resultatFenetre.next()) {
            modifChoixTypeCours.addItem(typeCoursDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixCours.setSelectedItem(seanceSelection.getTypeCours());
        panneauModifSeance.add(modifChoixTypeCours);
        
        //Enseignant
        modifChoixEnseignant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                                                         + "WHERE enseignant.ID_COURS = "+((Cours)modifChoixCours.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            modifChoixEnseignant.addItem(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur JOIN seance_enseignants ON id = id_enseignant where id_seance = "+seanceSelection.getId());
        if(resultatFenetre.first()){
            for(int i=0;i<modifChoixEnseignant.getItemCount();++i){
                if(((Utilisateur)modifChoixEnseignant.getItemAt(i)).getId() == resultatFenetre.getInt("ID")){
                    modifChoixEnseignant.setSelectedIndex(i);
                }
            }
        }
        panneauModifSeance.add(modifChoixEnseignant);
        
        //Promotion du groupe
        modifChoixPromotion.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
        while (resultatFenetre.next()) {
            modifChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT idpromotion FROM groupe JOIN seance_groupes ON id = id_groupe where id_seance = "+seanceSelection.getId());
        if(resultatFenetre.first()){
            for(int i=0;i<modifChoixPromotion.getItemCount();++i){
                if(((Promotion)modifChoixPromotion.getItemAt(i)).getId() == resultatFenetre.getInt("IDPROMOTION")){
                    modifChoixPromotion.setSelectedIndex(i);
                }
            }
        }
        panneauModifSeance.add(modifChoixPromotion);
        
        //Groupe
        modifChoixGroupe.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)modifChoixPromotion.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            modifChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe JOIN seance_groupes ON id = id_groupe where id_seance = "+seanceSelection.getId());
        if(resultatFenetre.first()){
            for(int i=0;i<modifChoixGroupe.getItemCount();++i){
                if(((Groupe)modifChoixGroupe.getItemAt(i)).getId() == resultatFenetre.getInt("ID")){
                    modifChoixGroupe.setSelectedIndex(i);
                }
            }
        }
        panneauModifSeance.add(modifChoixGroupe);
        
        //Site de la salle
        modifChoixSite.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            modifChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id_site FROM salle JOIN seance_salles ON id = id_salle where id_seance = "+seanceSelection.getId());
        if(resultatFenetre.first()){
            for(int i=0;i<modifChoixSite.getItemCount();++i){
                if(((Site)modifChoixSite.getItemAt(i)).getId() == resultatFenetre.getInt("ID_SITE")){
                    modifChoixSite.setSelectedIndex(i);
                }
            }
        }
        panneauModifSeance.add(modifChoixSite);
        
        //Salle
        modifChoixSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)modifChoixSite.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            modifChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle JOIN seance_salles ON id = id_salle where id_seance = "+seanceSelection.getId());
        if(resultatFenetre.first()){
            for(int i=0;i<modifChoixSalle.getItemCount();++i){
                if(((Salle)modifChoixSalle.getItemAt(i)).getId() == resultatFenetre.getInt("ID")){
                    modifChoixSalle.setSelectedIndex(i);
                }
            }
        }
        panneauModifSeance.add(modifChoixSalle);
        
        //Bouton d'enregistrement
        panneauModifSeance.add(modifBoutonEnregistrer);
        
        //Affichage d'erreur (qui pourra être modifié dans l'ActionListener du bouton
        panneauModifSeance.add(modifErrorField);
    }
    
    private void modifFillDaysOfMonth(){
        modifChoixJour.removeAllItems();
        int limiteJours = 0;
        switch((int)modifChoixMois.getSelectedItem()){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                limiteJours = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                limiteJours = 30;
                break;
            case 2:
                if ((((int)modifChoixAnnee.getSelectedItem() % 4 == 0) && !((int)modifChoixAnnee.getSelectedItem() % 100 == 0)) 
                        || ((int)modifChoixAnnee.getSelectedItem() % 400 == 0))   //voir définition d'une année bissextile
                    limiteJours = 29;
                else
                    limiteJours = 28;
                break;
            default:
                System.out.println("Erreur au niveau du mois dans la modification : cela devrait être impossible");
                break;
        }
        
        for(int i = 1; i <= limiteJours; ++i) {
            modifChoixJour.addItem(i);
        }
    }
    
    private int parseEtat(String etat){
        switch(etat){
            case "En cours de validation":
                return 1;
            case "Validé":
                return 2;
            case "Annulé":
                return 3;
            default:
                return 0;   //error case         
        }
    }
    
    private void modifEnregistrer() throws SQLException, ParseException{
        String messageErreur = "";
        
        //Date
        MyDate today = new MyDate();
        MyDate modifDate = new MyDate((int)modifChoixJour.getSelectedItem(),
                                        (int)modifChoixMois.getSelectedItem(),
                                        (int)modifChoixAnnee.getSelectedItem());
        messageErreur += (modifDate.compareTo(today) == -1)?"Attention : la nouvelle date est déjà passée.":"";

        //Heure de début
        MyHour modifHeureDebut = new MyHour((int)modifChoixHeureDebut.getValue(),(int)modifChoixMinutesDebut.getValue());
        
        //Heure de fin
        MyHour modifHeureFin = new MyHour((int)modifChoixHeureFin.getValue(),(int)modifChoixMinutesFin.getValue());
        
        //check que la fin soit après le début
        messageErreur = (modifHeureFin.compareTo(modifHeureDebut) == -1)?"ERREUR : L'heure de fin doit se situer après l'heure de début":messageErreur;
        
        
        
       modifErrorField.setText(messageErreur);
    }
    
    private void modifCreer() throws SQLException{
        
    }
}
