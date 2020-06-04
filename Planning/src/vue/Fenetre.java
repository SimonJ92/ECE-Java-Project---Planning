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
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import javax.swing.*;
import java.util.*;
import javax.imageio.ImageIO;

/**
 *
 * @author simon
 */
public class Fenetre extends JFrame implements ActionListener{
    //dimensions de la fenêtre
    private int largeur = 1920;
    private int hauteur = 1040;
    
    //images
    private BufferedImage navBarHomeIcon;
    
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
    private JPanel panneauAccueil;
    private JPanel panneauLogin;
    private JPanel panneauEDTGrilleSalle;
    private JPanel panneauEDTGrilleEnseignant;
    private JPanel panneauEDTGrilleEtudiant;
    private JPanel panneauRecherche; // 3 recherches : Elève, Salle et Enseignant
    private JPanel panneauModifSeance;
    
    //connexion utilisateur
    private Utilisateur connectedUser;
    
    
    //ÉLÉMENTS DES PANELS
    
    //À utiliser quand on veut faire une modification ou consulter un EDT/récap
    private int selectedWeek = 0;
    private Salle salleSelection;
    private Utilisateur enseignantSelection;
    private Etudiant etudiantSelection;
    private Seance seanceSelection;
    
    //Barres de navigation
    private BarreNav barreNav1;
    private JButton barreNav1BoutonHome;
    private JButton barreNav1BoutonDeco;
    private BarreNav barreNav2;
    private JButton barreNav2BoutonEDT;
    private JButton barreNav2BoutonRecap;
    private JButton barreNav2BoutonRecherche;
    
    //Accueil
    private JLabel accueilLabelConnectedUser;
    private JPanel accueilEDT;
    private JLabel accueilEDTTitre;
    private ArrayList<JButton> accueilEDTListeCours;
    private MyDate accueilDateJour;
    private JLabel[] accueilEDTLabelsHeures;    //15 label pour les heures
    
    //Panneau Login
    private JLabel loginTitre;
    private JLabel loginErreurMessage;
    private JLabel loginEmailLabel;
    private JTextField loginEmail;
    private JLabel loginPasswordLabel;
    private JPasswordField loginPassword;
    private JCheckBox loginVoirPassword;
    private JButton loginBoutonValider;
    
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
    private JComboBox modifChoixSite;
    private JComboBox modifChoixSalle;
    private JLabel modifErrorField;
    private JList modifChoixEnseignant;
    private JList modifChoixEnseignantSelection;
    private JButton modifBoutonAjouterEnseignant;
    private JButton modifBoutonSupprimerEnseignantSelection;
    private JComboBox modifChoixPromotion;
    private JList modifChoixGroupe;
    private JList modifChoixGroupeSelection;
    private JButton modifBoutonAjouterGroupe;
    private JButton modifBoutonSupprimerGroupeSelection;
    private JButton modifBoutonEnregistrer;
    
    //...
    
    //Constructeur à appeler pour démarrer l'appli
    public Fenetre(Connexion myConnexion){
        try{
            //récupération de la connexion à la BDD
            this.connexion = myConnexion;
            
            //Initialisation de l'utilisateur connecté
            connectedUser = new Utilisateur();
            
            //Initialisation des images
            navBarHomeIcon = ImageIO.read(new File("C:\\Users\\simon\\OneDrive\\Bureau\\ECE-Java-Project---Planning\\home-icon.png")); //icone pour le bouton vers l'accueil
            
            //initialisation des différents panels et leurs composants
            initComponent();

            //paramétrage de la fenêtere
            setSize(largeur,hauteur);
            setTitle("Planning");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setVisible(true);

            //On définit le panneau d'accueil (à changer)
            cardLayout.show(global, "Login");

            //On affiche le panneau global
            setContentPane(global);
        }
        catch(SQLException | IOException e){
            System.out.println("la "+e.toString()); //Supprimer le là : juste utile pour coder
        }
    }

    //Gestion du comportement dans l'appli
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();
        
        //BARRES DE NAVIGATION
        try{
            if(source == barreNav1BoutonDeco){
                connectedUser = null;
                remplirPanneauLogin();
                cardLayout.show(global,"Login");
            }
            else if(source == barreNav2BoutonRecherche){
                remplirRecherche();
                cardLayout.show(global, "Recherche");
            }
        }
        catch(Exception e){
            System.out.println(e.toString());
        }
        
        //LOGIN
        try{
            if(source == loginBoutonValider){
                Utilisateur tempConnectedUser = login(loginEmail.getText(),loginPassword.getText());
                if(tempConnectedUser != null){
                    connectedUser = tempConnectedUser;
                    remplirAccueil();
                    cardLayout.show(global, "Accueil");
                }else{
                    loginErreurMessage.setText("Erreur : l'identifiant ou le mot de passe est erroné. Veuillez réessayer");
                }
            }
            else if(source == loginVoirPassword){
                //cochage de case au nivau du mdp  qui affichera le mdr en toute lettre  avec (char)0 et cache avec ...UIManager...
                loginPassword.setEchoChar(loginVoirPassword.isSelected()?(char)0:(Character)UIManager.get("PasswordField.echoChar"));
            }
        }
        catch(SQLException e){
            System.out.println(e.toString()); 
        }
        
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
                DefaultListModel modifChoixEnseignantModel = new DefaultListModel();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                                                                 + "WHERE enseignant.ID_COURS = "+((Cours)modifChoixCours.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    modifChoixEnseignantModel.addElement(utilisateurDAO.find(resultatEvent.getInt("ID")));
                }
                modifChoixEnseignant.setModel(modifChoixEnseignantModel);
                modifChoixEnseignantSelection.setModel(new DefaultListModel());
            }
            else if(source == modifChoixSite){
                modifChoixSalle.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = "+((Site)modifChoixSite.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    modifChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
                }
            }
            else if(source == modifBoutonAjouterEnseignant){
                DefaultListModel modifChoixEnseignantModel = (DefaultListModel)modifChoixEnseignant.getModel();
                DefaultListModel modifChoixEnseignantSelectionModel = (DefaultListModel)modifChoixEnseignantSelection.getModel();
                for(Object enseignantSelectionne : modifChoixEnseignant.getSelectedValuesList()) {
                    modifChoixEnseignantSelectionModel.addElement((Utilisateur)enseignantSelectionne);
                    modifChoixEnseignantModel.removeElement((Utilisateur)enseignantSelectionne);
                }
            }
            else if(source == modifBoutonSupprimerEnseignantSelection){
                DefaultListModel modifChoixEnseignantModel = (DefaultListModel)modifChoixEnseignant.getModel();
                DefaultListModel modifChoixEnseignantSelectionModel = (DefaultListModel)modifChoixEnseignantSelection.getModel();
                for(Object enseignantSelectionne : modifChoixEnseignantSelection.getSelectedValuesList()) {
                    modifChoixEnseignantModel.addElement((Utilisateur)enseignantSelectionne);
                    modifChoixEnseignantSelectionModel.removeElement((Utilisateur)enseignantSelectionne);
                }
            }
            else if(source == modifChoixPromotion){
                DefaultListModel modifChoixGroupeModel = new DefaultListModel();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)modifChoixPromotion.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    modifChoixGroupeModel.addElement(groupeDAO.find(resultatEvent.getInt("ID")));
                }
                modifChoixGroupe.setModel(modifChoixGroupeModel);
                modifChoixGroupeSelection.setModel(new DefaultListModel());
            }
            else if(source == modifBoutonAjouterGroupe){
                DefaultListModel modifChoixGroupeModel = (DefaultListModel)modifChoixGroupe.getModel();
                DefaultListModel modifChoixGroupeSelectionModel = (DefaultListModel)modifChoixGroupeSelection.getModel();
                for(Object groupeSelectionne : modifChoixGroupe.getSelectedValuesList()) {
                    modifChoixGroupeSelectionModel.addElement((Groupe)groupeSelectionne);
                    modifChoixGroupeModel.removeElement((Groupe)groupeSelectionne);
                }
            }
            else if(source == modifBoutonSupprimerGroupeSelection){
                DefaultListModel modifChoixGroupeModel = (DefaultListModel)modifChoixGroupe.getModel();
                DefaultListModel modifChoixGroupeSelectionModel = (DefaultListModel)modifChoixGroupeSelection.getModel();
                for(Object groupeSelectionne : modifChoixGroupeSelection.getSelectedValuesList()) {
                    modifChoixGroupeModel.addElement((Groupe)groupeSelectionne);
                    modifChoixGroupeSelectionModel.removeElement((Groupe)groupeSelectionne);
                }
            }
            else if(source == modifBoutonEnregistrer){
                modifEnregistrer();
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
        panneauAccueil = new JPanel();
        panneauLogin = new JPanel();
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
        
        /*Il faudra peut être ne laisser que le panneau d'accueil généré au lancement, puis générer le reste par la suite*/
        
        //Accueil
        remplirAccueil();
        
        //Panel Login
        remplirPanneauLogin();
        
        //Panel Emploi du temps - Grille - Salle
        remplirEDTGrilleSalle();

        //Panel Emploi du temps - Grille - Enseignant
        remplirEDTGrilleEnseignant();

        //Panel Emploi du temps - Grille - Étudiant
        remplirEDTGrilleEtudiant();

        //Panel Recherche
        remplirRecherche();

        //Panel Modification de Séance
        remplirModifSeance();
        
        //...

        //Initialisation du conteneur global des panneaux
        cardLayout = new CardLayout();
        global = new JPanel(cardLayout);

        //On va ajouter dans le panel global tous les différents panels
        global.add(panneauAccueil, "Accueil");
        global.add(panneauLogin,"Login");
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
    private void addMenuBars(JPanel panel){
        
        //Initialisation des composants du panneau
        barreNav1 = new BarreNav(Color.WHITE);
        //barreNav1BoutonHome = new JButton("Accueil");
        barreNav1BoutonHome = new JButton(new ImageIcon(navBarHomeIcon));
        barreNav1BoutonDeco = new JButton("Déconnexion");    
        barreNav2 = new BarreNav(Color.BLACK);

        barreNav2BoutonEDT = new JButton("Emploi du temps");
        barreNav2BoutonRecap = new JButton("Récapitulatif des cours");
        barreNav2BoutonRecherche = new JButton("Recherche");
        
        //On retire les éventuels ActionListeners
        barreNav1BoutonHome.removeActionListener(this);
        barreNav1BoutonDeco.removeActionListener(this);
        barreNav2BoutonEDT.removeActionListener(this);
        barreNav2BoutonRecap.removeActionListener(this);
        barreNav2BoutonRecherche.removeActionListener(this);
        
        //ELEMENTS DES BARRES DE NAVIGATION
        
        //Barre 1
        barreNav1.setLayout(null);
        barreNav1.setBounds(0, 0, largeur, 50);
        barreNav1.setFont(new Font("Sans Serif", Font.BOLD, 16));
        
        barreNav1BoutonHome.setBounds(100, 5, 40, 40);  //anciennement 80/40
        barreNav1BoutonHome.setContentAreaFilled(false);
        barreNav1BoutonHome.setBorder(BorderFactory.createEmptyBorder());
        barreNav1.add(barreNav1BoutonHome);
        
        barreNav1BoutonDeco.setBounds(largeur - 145, 5, 120, 40);
        barreNav1BoutonDeco.setFont(new Font("Sans Serif",Font.BOLD,16));
        barreNav1BoutonDeco.setBackground(Color.red);
        barreNav1BoutonDeco.setBorder(BorderFactory.createEmptyBorder());
        barreNav1.add(barreNav1BoutonDeco);
        
        barreNav1.setBounds(0, 0, largeur, 50);
        panel.add(barreNav1);
        
        //Barre 2
        barreNav2.setLayout(null);
        barreNav2.setBounds(0, 51, largeur, 50);
        barreNav2.setFont(new Font("Sans Serif", Font.BOLD, 16));
        
        barreNav2BoutonEDT.setBounds(10, 5, 150, 40);
        barreNav2BoutonEDT.setBorder(BorderFactory.createEmptyBorder());
        barreNav2BoutonEDT.setBackground(Color.BLACK);
        barreNav2BoutonEDT.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                barreNav2BoutonEDT.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                barreNav2BoutonEDT.setBackground(Color.BLACK);
            }
        });
        barreNav2BoutonEDT.setFont(new Font("Sans Serif",Font.BOLD,18));
        barreNav2BoutonEDT.setForeground(Color.WHITE);
        barreNav2.add(barreNav2BoutonEDT);
        
        barreNav2BoutonRecap.setBounds(180, 5, 200, 40);
        barreNav2BoutonRecap.setBorder(BorderFactory.createEmptyBorder());
        barreNav2BoutonRecap.setBackground(Color.BLACK);
        barreNav2BoutonRecap.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                barreNav2BoutonRecap.setBackground(Color.LIGHT_GRAY);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                barreNav2BoutonRecap.setBackground(Color.BLACK);
            }
        });
        barreNav2BoutonRecap.setFont(new Font("Sans Serif",Font.BOLD,18));
        barreNav2BoutonRecap.setForeground(Color.WHITE);
        barreNav2.add(barreNav2BoutonRecap);
        
        if (connectedUser.getDroit() == 1 || connectedUser.getDroit() == 2) {
            barreNav2BoutonRecherche.setBounds(400, 5, 130, 40);
            barreNav2BoutonRecherche.setBorder(BorderFactory.createEmptyBorder());
            barreNav2BoutonRecherche.setBackground(Color.BLACK);
            barreNav2BoutonRecherche.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    barreNav2BoutonRecherche.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    barreNav2BoutonRecherche.setBackground(Color.BLACK);
                }
            });
            barreNav2BoutonRecherche.setFont(new Font("Sans Serif", Font.BOLD, 18));
            barreNav2BoutonRecherche.setForeground(Color.WHITE);
            barreNav2.add(barreNav2BoutonRecherche);
        }
        barreNav2.setBounds(0, 51, largeur, 50);
        panel.add(barreNav2);
        
        //Ré-activation des ActionListeners
        barreNav1BoutonHome.addActionListener(this);
        barreNav1BoutonDeco.addActionListener(this);
        barreNav2BoutonEDT.addActionListener(this);
        barreNav2BoutonRecap.addActionListener(this);
        barreNav2BoutonRecherche.addActionListener(this);
    }
    
    //private JButton boutonCours;      TO REMOVE
    //TODO
    private void remplirAccueil() throws SQLException{
        panneauAccueil.removeAll();
        panneauAccueil.setLayout(null);
        
        addMenuBars(panneauAccueil); //peut être modifier la position selon les ActionListeners
        
        int widthEDT = 275;
        int heightEDT = 700;
        
        //Initialisation des composants du panneau
        accueilLabelConnectedUser = new JLabel("",SwingConstants.CENTER); //set text ensuite, en fonctiond e l'utilisateur connecté 
        accueilEDT = new PanneauEDTAccueil();
        accueilEDTTitre = new JLabel("Emploi du temps du jour",SwingConstants.CENTER);
        accueilEDTListeCours = new ArrayList<>();
        accueilDateJour = new MyDate();
        accueilEDTLabelsHeures = new JLabel[15];    //15 label pour les heures
        
        //On retire les éventuels ActionListeners
        
        
        //ELEMENTS DE LA PAGE
        accueilLabelConnectedUser.setText("Bienvenue, "+connectedUser.toString()+"");
        accueilLabelConnectedUser.setFont(new Font("Sans Serif", Font.BOLD, 32));
        accueilLabelConnectedUser.setBounds(largeur/2 - 250, 300, 500, 50);
        panneauAccueil.add(accueilLabelConnectedUser);
        
        if (connectedUser.getDroit() == 3 || connectedUser.getDroit() == 4) {  //Si l'utilisateur connecté est un prof ou un élève, on affiche son emploi du temps du jour
            //Panneau d'emploi du temps
            accueilEDT.setBounds(10, 200, widthEDT, heightEDT);
            accueilEDT.setBackground(Color.white);
            accueilEDT.setLayout(null);

            accueilEDTTitre.setBounds(0, 5, widthEDT, 25);
            accueilEDTTitre.setFont(new Font("Sans Serif", Font.BOLD, 18));
            accueilEDT.add(accueilEDTTitre);

            //Création des labels des heures pour l'EDT
            (accueilEDTLabelsHeures[0] = new JLabel("08h30")).setBounds(5, 22, 40, 25); //On définit la position et la taille du label créé en début de ligne
            (accueilEDTLabelsHeures[1] = new JLabel("10h00")).setBounds(5, 97, 40, 25);
            (accueilEDTLabelsHeures[2] = new JLabel("10h15")).setBounds(5, 110, 40, 25);
            (accueilEDTLabelsHeures[3] = new JLabel("11h45")).setBounds(5, 185, 40, 25);
            (accueilEDTLabelsHeures[4] = new JLabel("12h00")).setBounds(5, 197, 40, 25);
            (accueilEDTLabelsHeures[5] = new JLabel("13h30")).setBounds(5, 272, 40, 25);
            (accueilEDTLabelsHeures[6] = new JLabel("13h45")).setBounds(5, 285, 40, 25);
            (accueilEDTLabelsHeures[7] = new JLabel("15h15")).setBounds(5, 360, 40, 25);
            (accueilEDTLabelsHeures[8] = new JLabel("15h30")).setBounds(5, 372, 40, 25);
            (accueilEDTLabelsHeures[9] = new JLabel("17h00")).setBounds(5, 447, 40, 25);
            (accueilEDTLabelsHeures[10] = new JLabel("17h15")).setBounds(5, 459, 40, 25);
            (accueilEDTLabelsHeures[11] = new JLabel("18h45")).setBounds(5, 535, 40, 25);
            (accueilEDTLabelsHeures[12] = new JLabel("19h00")).setBounds(5, 547, 40, 25);
            (accueilEDTLabelsHeures[13] = new JLabel("20h30")).setBounds(5, 622, 40, 25);
            (accueilEDTLabelsHeures[14] = new JLabel("21h30")).setBounds(5, 672, 40, 25);
            
            //On ajoute les labels sur le sous-panneau
            for (int i = 0; i < 15; ++i) {
                accueilEDT.add(accueilEDTLabelsHeures[i]);
            }
            
            //On va récupérer les cours de l'étudiant/du prof et les afficher plus ou moins haut selon l'heure
            if(connectedUser.getDroit() == 3){  //Connecté en tant que prof
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_enseignants WHERE id_enseignant = "+connectedUser.getId());
            }else{  //Connecté en tant qu'élève
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_groupes "
                                                                + "WHERE id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+")");
            }
            
            Seance tempSeanceAccueil;
            JButton tempCoursAccueil;
            int hauteurDebut;
            int hauteurCours;
            String descriptionCoursAccueil;
            while(resultatFenetre.next()){
                tempSeanceAccueil = seanceDAO.find(resultatFenetre.getInt("ID_SEANCE"));
                
                //on compte le nombre de minutes et on le multiplie par le nombre de pixels représentés par 1mn. On enlève ensuite l'offset de 8h30
                hauteurDebut = (int)(((tempSeanceAccueil.getHeureDebut().getHeure() * 60 + tempSeanceAccueil.getHeureDebut().getMinutes()) - (8*60 + 30)) * 0.833); 
                //similairement, on calcule la hauteur de la fin du cours, à laquelle on soustrait la hauteur du début pour obtenir la taille
                hauteurCours = ((int)(((tempSeanceAccueil.getHeureFin().getHeure() * 60 + tempSeanceAccueil.getHeureFin().getMinutes()) - (8*60 + 30)) * 0.833)) - hauteurDebut;
                
                //CREATION DE LA DESCRIPTION DU COURS
                //nom
                descriptionCoursAccueil = "<html><center>"+tempSeanceAccueil.getCours().getNom()+" : "+tempSeanceAccueil.getTypeCours().getNom()+"<br>-";
                
                //profs
                resultatEvent = statementEvent.executeQuery("SELECT nom FROM utilisateur JOIN seance_enseignants ON id = id_enseignant WHERE id_seance = "+tempSeanceAccueil.getId());
                while(resultatEvent.next()){
                    descriptionCoursAccueil += resultatEvent.getString("NOM")+"-";
                }
                descriptionCoursAccueil += "<br>-";
                
                //groupes
                resultatEvent = statementEvent.executeQuery("SELECT nom FROM groupe JOIN seance_groupes ON id = id_groupe WHERE id_seance = "+tempSeanceAccueil.getId());
                while(resultatEvent.next()){
                    descriptionCoursAccueil += resultatEvent.getString("NOM")+"-";
                }
                descriptionCoursAccueil += "<br>";
                
                //salles
                resultatEvent = statementEvent.executeQuery("SELECT * FROM salle JOIN seance_salles ON id = id_salle WHERE id_seance = "+tempSeanceAccueil.getId());
                while(resultatEvent.next()){
                    descriptionCoursAccueil += resultatEvent.getString("NOM")+"  ("+siteDAO.find(resultatEvent.getInt("ID_SITE")).getNom()+")";
                }
                descriptionCoursAccueil += "<br>";
                
                
                tempCoursAccueil = new JButton(descriptionCoursAccueil+"</center></html>");
                tempCoursAccueil.setBounds(45, 35 + hauteurDebut, 217, hauteurCours);
                tempCoursAccueil.setHorizontalAlignment(SwingConstants.CENTER);
                tempCoursAccueil.setFont(new Font("Sans Serif", Font.BOLD, 11));
                accueilEDTListeCours.add(tempCoursAccueil);
            }
            for(JButton Element : accueilEDTListeCours){
                accueilEDT.add(Element);
            }

            panneauAccueil.add(accueilEDT);

        }
        
        /*JLabel verif = new JLabel("Droit utilisateur : "+connectedUser.getDroit());
        panneauAccueil.add(verif);
        
        Seance seanceTest = seanceDAO.find(1);
        
        boutonCours = new JButton("<html>"+seanceTest.toString()+"</html>");
        boutonCours.setBounds(largeur/2, hauteur/2, 150, 200);
        panneauAccueil.add(boutonCours);
        
        CoursEDT testListener = new CoursEDT(seanceTest,1);
        boutonCours.addActionListener(testListener);*/
    }
    
    private void remplirPanneauLogin(){
        panneauLogin.removeAll();
        
        panneauLogin.setLayout(null);  
        
        //Initialisation des composants du panneau
        loginTitre = new JLabel("Connexion");
        loginErreurMessage = new JLabel("");
        loginEmailLabel = new JLabel("Email : ");
        loginEmail = new JTextField();
        loginPasswordLabel = new JLabel("Mot de passe : ");
        loginPassword = new JPasswordField();
        loginVoirPassword = new JCheckBox("Voir le mot de passe",false);
        loginBoutonValider = new JButton("Se connecter");
        
        //On retire les éventuels ActionListeners
        loginBoutonValider.removeActionListener(this);
        
        //CHAMPS LOGIN
        
        //Titre de la page
        loginTitre.setFont(new Font("Sans Serif", Font.BOLD, 32));
        loginTitre.setBounds(largeur/2 - 87, hauteur/2 - 250, 175, 50);
        panneauLogin.add(loginTitre);
        
        //message d'erreur
        loginErreurMessage.setForeground(Color.red);
        loginErreurMessage.setBounds(largeur/2 - 250, hauteur/2 - 200, 500, 25);
        panneauLogin.add(loginErreurMessage);
        
        //Label de l'email
        loginEmailLabel.setBounds(largeur/2 - 225,hauteur/2 - 125, 75, 25);
        panneauLogin.add(loginEmailLabel);
        
        //Entrée de l'email
        loginEmail.setBounds(largeur/2 - 125,hauteur/2 - 125, 250, 25);
        panneauLogin.add(loginEmail);
        
        //Label du mot de passe
        loginPasswordLabel.setBounds(largeur/2 - 225,hauteur/2 - 75, 100, 25);
        panneauLogin.add(loginPasswordLabel);
        
        //Entrée du mot de passe
        loginPassword.setBounds(largeur/2 - 125,hauteur/2 - 75, 250, 25);
        panneauLogin.add(loginPassword);
        
        //Checkbox voir le mote de passe
        loginVoirPassword.setBounds(largeur/2 - 125,hauteur/2 - 25, 250, 25);
        panneauLogin.add(loginVoirPassword);
        
        //Bouton de validation
        loginBoutonValider.setBounds(largeur/2 - 125, hauteur/2 + 25, 250, 50);
        panneauLogin.add(loginBoutonValider);
        
        loginBoutonValider.addActionListener(this);
        loginVoirPassword.addActionListener(this);
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
        panneauRecherche.removeAll();
        
        //Initialisation des composants du panneau
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
        
        //On retire les éventuels ActionListeners
        rechercheChoixSite.removeActionListener(this);
        rechercheChoixPromotion.removeActionListener(this);
        rechercheChoixGroupe.removeActionListener(this);
        boutonRechercherSalle.removeActionListener(this);
        boutonRechercherEnseignant.removeActionListener(this);
        boutonRechercherEtudiant.removeActionListener(this);
        
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
        
        //Ré-activation des ActionListeners
        rechercheChoixSite.addActionListener(this);
        rechercheChoixPromotion.addActionListener(this);
        rechercheChoixGroupe.addActionListener(this);
        boutonRechercherSalle.addActionListener(this);
        boutonRechercherEnseignant.addActionListener(this);
        boutonRechercherEtudiant.addActionListener(this);
    }
    
    //TODO (ne pas oublier de définir une taille pour les Jlist : elles peuvent être invisibles si elles sont vides)
    private void remplirModifSeance() throws SQLException{
        panneauModifSeance.removeAll();
        //Initialisation des composants du panneau
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
        modifChoixEnseignant = new JList();
        modifChoixEnseignantSelection = new JList();
        modifBoutonAjouterEnseignant = new JButton("Ajouter les enseignants à la séance");
        modifBoutonSupprimerEnseignantSelection = new JButton("Supprimer les enseignants de la séance");
        modifChoixPromotion = new JComboBox();
        modifChoixGroupe = new JList();
        modifChoixGroupeSelection = new JList();
        modifBoutonAjouterGroupe = new JButton("Ajouter les groupes à la séance");
        modifBoutonSupprimerGroupeSelection = new JButton("Supprimer les groupes de la séance");
        modifChoixSite = new JComboBox();
        modifChoixSalle = new JComboBox();
        modifBoutonEnregistrer = new JButton("Enregistrer");
        modifErrorField = new JLabel("");
        
        //On retire les éventuels ActionListeners
        modifChoixAnnee.removeActionListener(this);
        modifChoixMois.removeActionListener(this);
        modifChoixCours.removeActionListener(this);
        modifChoixSite.removeActionListener(this);
        modifBoutonAjouterEnseignant.removeActionListener(this);
        modifBoutonSupprimerEnseignantSelection.removeActionListener(this);
        modifChoixPromotion.removeActionListener(this);
        modifBoutonAjouterGroupe.removeActionListener(this);
        modifBoutonSupprimerGroupeSelection.removeActionListener(this);
        modifBoutonEnregistrer.removeActionListener(this);

        //CHAMPS SEANCE
        //seanceSelection = seanceDAO.find(1); //à supprimer, mais pratique pour coder
        
        panneauModifSeance.removeAll();
        panneauModifSeance.add(new JLabel("Modifier Séance : "+seanceSelection.toString()));    //À supprimer
        
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
        for(int i=0;i<modifChoixCours.getItemCount();++i){
            if(((Cours)modifChoixCours.getItemAt(i)).getId() == seanceSelection.getCours().getId()){
                modifChoixCours.setSelectedIndex(i);
            }
        }
        panneauModifSeance.add(modifChoixCours);
        
        //Type de cours
        modifChoixTypeCours.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM type_cours");
        while (resultatFenetre.next()) {
            modifChoixTypeCours.addItem(typeCoursDAO.find(resultatFenetre.getInt("ID")));
        }
        for(int i=0;i<modifChoixTypeCours.getItemCount();++i){
            if(((TypeCours)modifChoixTypeCours.getItemAt(i)).getId() == seanceSelection.getTypeCours().getId()){
                modifChoixTypeCours.setSelectedIndex(i);
            }
        }
        panneauModifSeance.add(modifChoixTypeCours);
        
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
        
        //Enseignants
        DefaultListModel modifChoixEnseignantModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                                                         + "WHERE enseignant.ID_COURS = "+((Cours)modifChoixCours.getSelectedItem()).getId()+" "
                                                         + "AND id not in (SELECT id_enseignant FROM seance_enseignants "
                                                         + "WHERE id_seance = "+seanceSelection.getId()+")");
        while (resultatFenetre.next()) {
            modifChoixEnseignantModel.addElement(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixEnseignant = new JList(modifChoixEnseignantModel);
        panneauModifSeance.add(modifChoixEnseignant);
        
        //Enseignants sélectionnés
        DefaultListModel modifChoixEnseignantSelectionModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_enseignant FROM seance_enseignants "
                                                         + "WHERE id_seance = "+seanceSelection.getId());
        while (resultatFenetre.next()) {
            modifChoixEnseignantSelectionModel.addElement(utilisateurDAO.find(resultatFenetre.getInt("ID_ENSEIGNANT")));
        }
        modifChoixEnseignantSelection = new JList(modifChoixEnseignantSelectionModel);
        panneauModifSeance.add(modifChoixEnseignantSelection);
        
        //Boutons de gestion des enseignants
        panneauModifSeance.add(modifBoutonAjouterEnseignant);
        panneauModifSeance.add(modifBoutonSupprimerEnseignantSelection);
        
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
        
        //Groupes
        DefaultListModel modifChoixGroupeModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = "+((Promotion)modifChoixPromotion.getSelectedItem()).getId()+" "
                                                        +"AND id not in (SELECT id_groupe FROM seance_groupes "
                                                         + "WHERE id_seance = "+seanceSelection.getId()+")");
        while (resultatFenetre.next()) {
            modifChoixGroupeModel.addElement(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixGroupe = new JList(modifChoixGroupeModel);
        panneauModifSeance.add(modifChoixGroupe);
        
        //Groupes sélectionnés
        DefaultListModel modifChoixGroupeSelectionModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_groupe FROM seance_groupes "
                                                         + "WHERE id_seance = "+seanceSelection.getId());
        while (resultatFenetre.next()) {
            modifChoixGroupeSelectionModel.addElement(groupeDAO.find(resultatFenetre.getInt("ID_GROUPE")));
        }
        modifChoixGroupeSelection = new JList(modifChoixGroupeSelectionModel);
        panneauModifSeance.add(modifChoixGroupeSelection);
        
        //Boutons de gestion des groupes
        panneauModifSeance.add(modifBoutonAjouterGroupe);
        panneauModifSeance.add(modifBoutonSupprimerGroupeSelection);
        
        //Bouton d'enregistrement
        panneauModifSeance.add(modifBoutonEnregistrer);
        
        //Affichage d'erreur (qui pourra être modifié dans l'ActionListener du bouton
        panneauModifSeance.add(modifErrorField);
        
        
        //Ré-activation des ActionListeners
        modifChoixAnnee.addActionListener(this);
        modifChoixMois.addActionListener(this);
        modifChoixCours.addActionListener(this);
        modifChoixSite.addActionListener(this);
        modifBoutonAjouterEnseignant.addActionListener(this);
        modifBoutonSupprimerEnseignantSelection.addActionListener(this);
        modifChoixPromotion.addActionListener(this);
        modifBoutonAjouterGroupe.addActionListener(this);
        modifBoutonSupprimerGroupeSelection.addActionListener(this);
        modifBoutonEnregistrer.addActionListener(this);
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
        
        //Vérifier que la fin soit après le début
        if(modifHeureFin.compareTo(modifHeureDebut) == -1){
            messageErreur = "ERREUR : L'heure de fin doit se situer après l'heure de début";
        }else if((int)modifChoixHeureDebut.getValue() < 7 || (int)modifChoixHeureDebut.getValue() > 20 ||
                    (int)modifChoixMinutesDebut.getValue() < 0 || (int)modifChoixMinutesDebut.getValue() > 59 ||
                    (int)modifChoixHeureFin.getValue() < 7 || (int)modifChoixHeureFin.getValue() > 20 ||
                    (int)modifChoixMinutesFin.getValue() < 0 || (int)modifChoixMinutesFin.getValue() > 59){
            messageErreur = "ERREUR : Les heures doivent être comprises entre 7h et 20h. Les minutes doivent être comprises entre 0mn et 59mn";
        }else if(modifDate.getJourDeLaSemaine() == 6 || modifDate.getJourDeLaSemaine() == 7){
            messageErreur = "ERREUR : Vous ne pouvez pas ajouter de cours les Samedis et Dimanches";
        }
        else{
            //Vérifier que les enseignants choisis n'ont pas cours à cette heure là
            int nombreSeancesEnseignant = 0;
            for(int i=0; i<modifChoixEnseignantSelection.getModel().getSize();++i){
                resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_enseignants on id = id_seance "
                                                        + "WHERE ((heure_debut >= TIME('"+modifHeureDebut+"') AND heure_debut <= TIME('"+modifHeureDebut+"')) "
                                                        + "OR (heure_fin >= TIME('"+modifHeureFin+"') AND heure_fin <= TIME('"+modifHeureFin+"'))) "
                                                        + "AND id_enseignant = "+((Utilisateur)modifChoixEnseignantSelection.getModel().getElementAt(i)).getId()+" "
                                                        + "AND id != "+seanceSelection.getId());
                if(resultatEvent.first()){
                    nombreSeancesEnseignant += resultatEvent.getInt("nombreSeances");
                }
            }
            if(nombreSeancesEnseignant > 0){
                messageErreur = "ERREUR : un des enseignants a déjà un cours dans cette période";
            }else if(nombreSeancesEnseignant < 0){
                System.out.println("Erreur dans l'enregistrement des données : nombreSeancesEnseignant vaut <0 après la requête");
            }else if(nombreSeancesEnseignant == 0){
                //Vérifier que les groupes choisis n'a pas de cours à cette heure là
                int nombreSeancesGroupe = 0;
                for(int i=0; i<modifChoixGroupeSelection.getModel().getSize();++i){
                    resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_groupes on id = id_seance "
                                                            + "WHERE ((heure_debut >= TIME('"+modifHeureDebut+"') AND heure_debut <= TIME('"+modifHeureDebut+"')) "
                                                            + "OR (heure_fin >= TIME('"+modifHeureFin+"') AND heure_fin <= TIME('"+modifHeureFin+"'))) "
                                                            + "AND id_groupe = "+((Groupe)modifChoixGroupeSelection.getModel().getElementAt(i)).getId()+" "
                                                            + "AND id != "+seanceSelection.getId());
                    if(resultatEvent.first()){
                        nombreSeancesGroupe += resultatEvent.getInt("nombreSeances");
                    }
                }
                if(nombreSeancesGroupe > 0){
                    messageErreur = "ERREUR : Un des groupes a déjà un cours dans cette période";
                }else if(nombreSeancesGroupe < 0){
                    System.out.println("Erreur dans l'enregistrement des données : nombreSeancesGroupe vaut <0 après la requête");
                }else if(nombreSeancesGroupe == 0){
                    //Vérifier que la salle choisie n'a pas de cours à cette heure là
                    int nombreSeancesSalle = -1;
                    //On cherche le nombre de salles qui ont le même id que la salle choisie et qui sont libres entre les heures choisies
                    //On s'assure de retirer l'id de la salle selectionnée du compte
                    resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_salles on id = id_seance "
                                                                + "WHERE ((heure_debut >= TIME('"+modifHeureDebut+"') AND heure_debut <= TIME('"+modifHeureDebut+"')) "
                                                                + "OR (heure_fin >= TIME('"+modifHeureFin+"') AND heure_fin <= TIME('"+modifHeureFin+"'))) "
                                                                + "AND id_salle = "+((Salle)modifChoixSalle.getSelectedItem()).getId()+" "
                                                                + "AND id != "+seanceSelection.getId());
                    if(resultatEvent.first()){
                        nombreSeancesSalle = resultatEvent.getInt("nombreSeances");
                    }
                    if(nombreSeancesSalle > 0){
                        messageErreur = "ERREUR : la salle a déjà un cours dans cette période";
                    }else if(nombreSeancesSalle == -1){
                        System.out.println("Erreur dans l'enregistrement des données : nombreSeancesSalle vaut -1 après la requête");
                    }else if(nombreSeancesSalle == 0){
                         //Vérifier que la capacité de la salle est suffisante pour le nombre d'élèves du groupe
                        int totalEtudiants = 0;
                        for(int i=0; i<modifChoixGroupeSelection.getModel().getSize();++i){
                            //Pour chaque groupe, on ajoute le nombre d'élèves appartenant au groupe au totalEtudiants
                            resultatEvent = statementEvent.executeQuery("SELECT COUNT(*) as nombreEtudiants FROM etudiant "
                                                                        + "WHERE id_groupe = "+((Groupe)modifChoixGroupeSelection.getModel().getElementAt(i)).getId());
                            if(resultatEvent.first()){
                                totalEtudiants += resultatEvent.getInt("nombreEtudiants");
                            }
                        }
                        //On cherche s'il existe une salle avec l'id de la salle choisie, tout en s'assurant que sa capacité est suffisante pour le total des élèves
                        boolean valide = false;
                        resultatEvent = statementEvent.executeQuery("SELECT * FROM salle "
                                                                    + "WHERE id = "+((Salle)modifChoixSalle.getSelectedItem()).getId()+" "
                                                                    + "AND capacite >= "+totalEtudiants);
                       if(resultatEvent.first()){   
                           valide = true;   //Pour pouvoir réutiliser le résultat par la suite
                       }
                       if(valide){
                            //TOUT EST OK POUR METTRE À JOUR
                            seanceSelection.setSemaine(modifDate.getSemaineDeAnnee());
                            seanceSelection.setDate(modifDate);
                            seanceSelection.setHeureDebut(modifHeureDebut);
                            seanceSelection.setHeureFin(modifHeureFin);
                            seanceSelection.setEtat(parseEtat((String)modifChoixEtat.getSelectedItem()));
                            seanceSelection.setCours((Cours)modifChoixCours.getSelectedItem());
                            seanceSelection.setTypeCours((TypeCours)modifChoixTypeCours.getSelectedItem());
                            if(seanceSelection.getId() == 0){
                                //Cas d'une création de salle
                                if(seanceDAO.create(seanceSelection)){  //Mise à jour des infos de la séance
                                    //On récupère l'id de la dernière séance crée, siot cele que l'on vient d'ajouter
                                    resultatEvent = statementEvent.executeQuery("SELECT MAX(id) as idCree from seance");
                                    if(resultatEvent.first()){
                                        seanceSelection.setId(resultatEvent.getInt("idCree"));
                                    }
                                    gererTablesSeances();
                                }else{
                                    messageErreur = "ERREUR : Erreur lors de la création de la séance";
                                }
                            }else{
                                //Cas d'une modification de salle
                                if(seanceDAO.update(seanceSelection)){  //Mise à jour des infos de la séance
                                    gererTablesSeances();
                                }else{
                                    messageErreur = "ERREUR : Erreur lors de la mise à jour de la séance";
                                }
                            }
                        }else{
                            messageErreur = "ERREUR : la salle n'a pas assez de place pour tous les élèves";
                        }
                    }
                }
            }
        }
        
        //On recrée le panel pour éviter tout problème d'affichage
        global.remove(panneauModifSeance);
        panneauModifSeance = new JPanel();
        remplirModifSeance();
        global.add(panneauModifSeance,"ModifSeance");
        cardLayout.show(global,"ModifSeance");
        
        //On définit le message d'erreur
        modifErrorField.setText(messageErreur);
    }
    
    private void gererTablesSeances() throws SQLException {
        //déletion de tous les séances_enseignants en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_enseignants WHERE id_seance = "+seanceSelection.getId());
        //ajout des séances_enseignants à ajouter
        for (int i = 0; i < modifChoixEnseignantSelection.getModel().getSize(); ++i) {
            //create lance des SQLExceptions, donc les erreurs sont gérées
            seanceEnseignantsDAO.create(new SeanceEnseignants(seanceSelection, (Utilisateur) modifChoixEnseignantSelection.getModel().getElementAt(i)));
        }

        //déletion de tous les séances_groupes en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_groupes WHERE id_seance = "+seanceSelection.getId());
        //ajout des séances_groupes à ajouter
        for (int i = 0; i < modifChoixGroupeSelection.getModel().getSize(); ++i) {
            //create lance des SQLExceptions, donc les erreurs sont gérées
            seanceGroupesDAO.create(new SeanceGroupes(seanceSelection, (Groupe) modifChoixGroupeSelection.getModel().getElementAt(i)));
        }
        
        //déletion de tous les séances_salles en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_salles WHERE id_seance = "+seanceSelection.getId());
        //ajout des séances_salles à ajouter
        //create lance des SQLExceptions, donc les erreurs sont gérées
        seanceSallesDAO.create(new SeanceSalles(seanceSelection, (Salle) modifChoixSalle.getSelectedItem()));
    }
    
    
    
    
    
    
    
    class CoursEDT implements ActionListener {

        private Seance seance;
        private int numeroBoutonCours;

        public CoursEDT(Seance seance, int numeroBoutonCours) {
            this.seance = seance;
            this.numeroBoutonCours = numeroBoutonCours;
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if(ev != null){
                dialogueCoursEDT(numeroBoutonCours);
            }
        }

    }
    
    private void dialogueCoursEDT(int numeroBoutonCours){
        JDialog fenetreDialogue = new JDialog(this);
        fenetreDialogue.setLayout(null);
        JLabel testDialogue = new JLabel("test");
        testDialogue.setBounds(250, 250, 100, 25);
        fenetreDialogue.add(testDialogue);
        fenetreDialogue.setSize(500,500);
        fenetreDialogue.setTitle("Détails du cours");
        //fenetreDialogue.setLocationRelativeTo(boutonCours); //Remplacer par un élément d'une arraylist de boutons
        fenetreDialogue.setVisible(true);
        
        //fenetreDialogue.dispose(); pour fermer la fenetre
        //TODO
        
    }
}