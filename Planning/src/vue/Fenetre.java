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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;


/**
 *
 * @author simon
 */
public class Fenetre extends JFrame implements ActionListener {
    
    //dimensions de la fenêtre
    private int largeur = 1920;
    private int hauteur = 1040;

    //images
    private BufferedImage navBarHomeIcon;
    private BufferedImage navBarLogoEce;
    private BufferedImage imageEce;
    private BufferedImage imageInseecU;

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
    private JPanel panneauEDTGrille;    //utilisé pour les emplois du temps des profs, élèves et salles
    private JPanel panneauEDTListe;
    private JPanel panneauRecherche; // 3 recherches : Elève, Salle et Enseignant
    private JPanel panneauModifSeance;
    private JPanel panneauRecapCours;

   //couleurs du thème (de quoi faire un arc en ciel <3 ):
    Color vertEce = new Color(4, 115, 123); //comme sur le logo..                         / POLICE BLANCHE
    Color vert1 = new Color(154,205,50);    //vert clair                                  / POLICE NOIRE
    Color vert2 = new Color(107,142,35);    //olive                                       / LES DEUX
    Color rouge1 = new Color(178,34,34);    //rouge brique                                / POLICE BLANCHE
    Color rouge2 = new Color(220,20,60);    //crimson, en gros écarlate quoi..            / LES DEUX
    Color bleu1 = new Color(135,206,250);   //lightskyblue, bleu ciel pour les billingues / POLICE NOIRE
    Color bleu2 = new Color(65,105,225);    //bleu royal                                  / POLICE BLANCHE
    Color jaune1 = new Color(255,255,102);  //poussin clair                               / POLICE NOIRE
    Color jaune2 = new Color(255,215,0);    //or                                          / POLICE NOIRE
    Color rose1 = new Color(240,128,128);   //corail clair                                / LES DEUX
    Color rose2 = new Color(255,160,122);   //saumon clair                                / POLICE NOIRE
    Color orange1 = new Color(255,165,0);   //orange classique                            / LES DEUX
    Color orange2 = new Color(255,69,0);    //orange sanguine                             / POLICE BLANCHE
    Color violet1 = new Color(186,85,211);  //orchidée moyenne                            / LES DEUX
    Color violet2 = new Color(199,21,133);  //fuushia foncé                               / POLICE BLANCHE
    Color marron1 = new Color(210,105,30);  //chocolat (dolce en vrai mdr)                / LES DEUX
    Color marron2 = new Color(188,143,143); // marron rosé, c'était beau en vrai...       / LES DEUX 
    
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
    private JButton barreNav2BoutonCreer;
    private JLabel barreNav1Logo;

    //Accueil
    private JLabel accueilLabelConnectedUser;
    private JPanel accueilEDT;
    private JLabel accueilEDTTitre;
    private ArrayList<JButton> accueilEDTListeCours;
    private MyDate accueilDateJour;
    private JLabel[] accueilEDTLabelsHeures;    //15 label pour les heures
    private JLabel accueilLogoEce;
    private JLabel accueilLogoInseecU;
    private JFreeChart accueilGraphesEtudiantsParPromo;
    private JFreeChart accueilGraphesGroupesParPromo;
    private JFreeChart accueilGraphesSeancesParEnseignant;
    private JFreeChart accueilGraphesSeancesParCours;

    //Panneau Login
    private JLabel loginTitre;
    private JLabel loginErreurMessage;
    private JLabel loginEmailLabel;
    private JTextField loginEmail;
    private JLabel loginPasswordLabel;
    private JPasswordField loginPassword;
    private JCheckBox loginVoirPassword;
    private JButton loginBoutonValider;
    private JLabel loginLogoEce;
    private JLabel loginLogoInseecU;

    //Emploi du temps - Grille
    private JComboBox EDTGrilleChoixTypeEDT;
    private PanneauEDTGrille EDTGrilleEDT;
    private ArrayList<JButton> EDTGrilleListeCours;
    private JLabel[] EDTGrilleLabelsHeures;    //15 label pour les heures
    private JPanel EDTGrillePanneauSemaines;
    private JLabel[] EDTGrilleLabelsJours;
    
    //Emploir de temps - Liste
    private JComboBox EDTListeChoixTypeEDT;
    private JScrollPane EDTListeEDTContainer;
    private JPanel EDTListeEDT;
    private ArrayList<JPanel> EDTListeListeCours;
    private JPanel EDTListePanneauSemaines;

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
    private JLabel rechercheTitre;
    private JLabel rechercheLabelSemaine;
    private JLabel rechercheLabelSalle;
    private JLabel rechercheLabelEnseignant;
    private JLabel rechercheLabelEtudiant;

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
    private JLabel modifAnnonceDebut;
    private JLabel modifLabelDate;
    private JLabel modifLabelHeureDebut;
    private JLabel modifLabelHeureFin;
    private JLabel modifLabelEtatCours;
    private JLabel modifLabelChoixCours;
    private JLabel modifLabelChoixSalle;
    private JLabel modifLabelChoixProf;
    private JLabel modifLabelChoixPromo;
    private JLabel modifLabelTypeCours;
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
    private JScrollPane modifChoixProfSocket;
    private JScrollPane modifAddProfSocket;
    private JScrollPane modifChoixGroupeSocket;
    private JScrollPane modifAddGroupeSocket;

    //Récapitulatif des cours
    private JScrollPane recapListeCoursContainer;
    private JPanel recapListeCours;
    private JLabel recapTitre;
    private JTable recapTable;
    private ArrayList<LigneRecap> recapDonneesTable;
    private JLabel recapTotalHeures;
    
    //Constructeur à appeler pour démarrer l'appli
    public Fenetre(Connexion myConnexion) {
        try {
            //récupération de la connexion à la BDD
            this.connexion = myConnexion;

            //Initialisation de l'utilisateur connecté
            connectedUser = new Utilisateur();

            //Initialisation des images
            navBarHomeIcon = ImageIO.read(new File("src/packageImages/home-icon.png")); //icone pour le bouton vers l'accueil
            navBarLogoEce = ImageIO.read(new File("src/packageImages/logo_ECE_Paris_3.jpg"));
            imageEce = ImageIO.read(new File("src/packageImages/logo_ece.png"));
            imageInseecU = ImageIO.read(new File("src/packageImages/logo_Inseec_U.png"));
            
            //initialisation des différents panels et leurs composants
            initComponent();

            //paramétrage de la fenêtere
            setSize(largeur, hauteur);
            setTitle("Planning");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setResizable(true);
            setVisible(true);

            //On définit le panneau d'accueil (à changer)
            cardLayout.show(global, "Login");

            //On affiche le panneau global
            setContentPane(global);
        } catch (SQLException | IOException e) {
            System.out.println("la " + e.toString()); //Supprimer le là : juste utile pour coder
        }
    }

    //Gestion du comportement dans l'appli
    @Override
    public void actionPerformed(ActionEvent evt) {
        Object source = evt.getSource();

        //BARRES DE NAVIGATION
        try {
            //ramène l'utilisateur à l'écran d'accueil
            if (source == barreNav1BoutonHome) {
                remplirAccueil();
                cardLayout.show(global, "Accueil");
            } //déconnecte l'utilisateur et le ramène à l'cran de connexion
            else if (source == barreNav1BoutonDeco) {
                connectedUser = null;
                remplirPanneauLogin();
                cardLayout.show(global, "Login");
            } //amène l'utilisateur vers son emploi du temps
            else if (source == barreNav2BoutonEDT) {
                selectedWeek = accueilDateJour.getSemaineDeAnnee();
                if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
                    enseignantSelection = connectedUser;
                    remplirEDTGrille("enseignant");
                    cardLayout.show(global, "EDTGrille");
                } else {  //connecté en tant qu'étudiant
                    etudiantSelection = etudiantDAO.find(connectedUser.getId());
                    remplirEDTGrille("etudiant");
                    cardLayout.show(global, "EDTGrille");
                }
            } //amène l'utilisateur vers le récapitulatif de ses cours
            else if (source == barreNav2BoutonRecap) {
                remplirRecapCours();
                cardLayout.show(global, "RecapCours");
            } //amène l'utilisateur vers le panneau de recherche
            else if (source == barreNav2BoutonRecherche) {
                remplirRecherche();
                cardLayout.show(global, "Recherche");
            } //amène l'utilisateur vers la page de création d'une séance
            else if (source == barreNav2BoutonCreer) {
                seanceSelection = new Seance();
                global.remove(panneauModifSeance);
                panneauModifSeance = new JPanel();
                remplirModifSeance();
                global.add(panneauModifSeance, "ModifSeance");
                cardLayout.show(global, "ModifSeance");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        //LOGIN
        try {
            //tente de connecter l'utilisateur avec l'identifiant et le mot de passe qu'il a saisi
            if (source == loginBoutonValider) {
                Utilisateur tempConnectedUser = login(loginEmail.getText(), new String(loginPassword.getPassword()));
                if (tempConnectedUser != null) {
                    connectedUser = tempConnectedUser;
                    remplirAccueil();
                    cardLayout.show(global, "Accueil");
                } else {
                    loginErreurMessage.setText("Erreur : l'identifiant ou le mot de passe est erroné. Veuillez réessayer");
                }
            } //affiche le mot de passe en toutes lettres si la case est cochée
            else if (source == loginVoirPassword) {
                //cochage de case au nivau du mdp  qui affichera le mdr en toute lettre  avec (char)0 et cache avec ...UIManager...
                loginPassword.setEchoChar(loginVoirPassword.isSelected() ? (char) 0 : (Character) UIManager.get("PasswordField.echoChar"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        
        
        //RECHERCHE
        try {
            //adaptation de la page à la modification du choix du site
            if (source == rechercheChoixSite) {
                rechercheChoixSalle.removeAllItems();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site) rechercheChoixSite.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    rechercheChoixSalle.addItem(salleDAO.find(resultatEvent.getInt("ID")));
                }
            } //adaptation de la page à la modification du choix de la promotion
            else if (source == rechercheChoixPromotion) {
                rechercheChoixGroupe.removeActionListener(this);
                rechercheChoixGroupe.removeAllItems();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) rechercheChoixPromotion.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    rechercheChoixGroupe.addItem(groupeDAO.find(resultatEvent.getInt("ID")));
                }

                rechercheChoixEtudiant.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = " + ((Groupe) rechercheChoixGroupe.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
                }
                rechercheChoixGroupe.addActionListener(this);
            } //adaptation de la page à la modification du choix du groupe
            else if (source == rechercheChoixGroupe) {
                rechercheChoixEtudiant.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = " + ((Groupe) rechercheChoixGroupe.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
                }
            } //Effectuer la recherche d'une salle
            else if (source == boutonRechercherSalle) {
                salleSelection = (Salle) rechercheChoixSalle.getSelectedItem();
                selectedWeek = (int) rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrille("salle");
                cardLayout.show(global, "EDTGrille");
            } //effectuer la recherche d'un enseignant
            else if (source == boutonRechercherEnseignant) {
                enseignantSelection = (Utilisateur) rechercheChoixEnseignant.getSelectedItem();
                selectedWeek = (int) rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrille("enseignant");
                cardLayout.show(global, "EDTGrille");
            } //effectuer la recherche d'un élève
            else if (source == boutonRechercherEtudiant) {
                etudiantSelection = (Etudiant) rechercheChoixEtudiant.getSelectedItem();
                selectedWeek = (int) rechercheChoixSemaine.getSelectedItem();
                remplirEDTGrille("etudiant");
                cardLayout.show(global, "EDTGrille");
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        //MODIFICATION SEANCE
        try {
            //adaptation de la page sur la modification du choix d'un mois ou d'une année
            if (source == modifChoixAnnee || source == modifChoixMois) {
                modifFillDaysOfMonth();
            } //adaptation de la page sur une modification du choix du cours
            else if (source == modifChoixCours) {
                DefaultListModel modifChoixEnseignantModel = new DefaultListModel();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                        + "WHERE enseignant.ID_COURS = " + ((Cours) modifChoixCours.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    modifChoixEnseignantModel.addElement(utilisateurDAO.find(resultatEvent.getInt("ID")));
                }
                modifChoixEnseignant.setModel(modifChoixEnseignantModel);
                modifChoixEnseignantSelection.setModel(new DefaultListModel());
            } //adaptation de la page sur une modification du choix d'un site
            else if (source == modifChoixSite) {
                modifChoixSalle.removeAllItems();
                resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site) modifChoixSite.getSelectedItem()).getId());
                while (resultatFenetre.next()) {
                    modifChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
                }
            } //réaction de'ajout d'un enseignant à la colonne des enseignants sélectionnés
            else if (source == modifBoutonAjouterEnseignant) {
                DefaultListModel modifChoixEnseignantModel = (DefaultListModel) modifChoixEnseignant.getModel();
                DefaultListModel modifChoixEnseignantSelectionModel = (DefaultListModel) modifChoixEnseignantSelection.getModel();
                for (Object enseignantSelectionne : modifChoixEnseignant.getSelectedValuesList()) {
                    modifChoixEnseignantSelectionModel.addElement((Utilisateur) enseignantSelectionne);
                    modifChoixEnseignantModel.removeElement((Utilisateur) enseignantSelectionne);
                }
            } //réaction de suppression d'un enseignant de la colonne des enseignants sélectionnés
            else if (source == modifBoutonSupprimerEnseignantSelection) {
                DefaultListModel modifChoixEnseignantModel = (DefaultListModel) modifChoixEnseignant.getModel();
                DefaultListModel modifChoixEnseignantSelectionModel = (DefaultListModel) modifChoixEnseignantSelection.getModel();
                for (Object enseignantSelectionne : modifChoixEnseignantSelection.getSelectedValuesList()) {
                    modifChoixEnseignantModel.addElement((Utilisateur) enseignantSelectionne);
                    modifChoixEnseignantSelectionModel.removeElement((Utilisateur) enseignantSelectionne);
                }
            } //adaptation de la page sur une modification du choix de promotion
            else if (source == modifChoixPromotion) {
                DefaultListModel modifChoixGroupeModel = new DefaultListModel();
                resultatEvent = statementEvent.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) modifChoixPromotion.getSelectedItem()).getId());
                while (resultatEvent.next()) {
                    modifChoixGroupeModel.addElement(groupeDAO.find(resultatEvent.getInt("ID")));
                }
                modifChoixGroupe.setModel(modifChoixGroupeModel);
                modifChoixGroupeSelection.setModel(new DefaultListModel());
            } //réaction d'ajout d'un groupe à la colonne des groupes sélectionnés
            else if (source == modifBoutonAjouterGroupe) {
                DefaultListModel modifChoixGroupeModel = (DefaultListModel) modifChoixGroupe.getModel();
                DefaultListModel modifChoixGroupeSelectionModel = (DefaultListModel) modifChoixGroupeSelection.getModel();
                for (Object groupeSelectionne : modifChoixGroupe.getSelectedValuesList()) {
                    modifChoixGroupeSelectionModel.addElement((Groupe) groupeSelectionne);
                    modifChoixGroupeModel.removeElement((Groupe) groupeSelectionne);
                }
            } //réaction de suppresison d'un groupe de la colonne des groupes sélectionnés
            else if (source == modifBoutonSupprimerGroupeSelection) {
                DefaultListModel modifChoixGroupeModel = (DefaultListModel) modifChoixGroupe.getModel();
                DefaultListModel modifChoixGroupeSelectionModel = (DefaultListModel) modifChoixGroupeSelection.getModel();
                for (Object groupeSelectionne : modifChoixGroupeSelection.getSelectedValuesList()) {
                    modifChoixGroupeModel.addElement((Groupe) groupeSelectionne);
                    modifChoixGroupeSelectionModel.removeElement((Groupe) groupeSelectionne);
                }
            } //Réaction à l'enregistrement d'une modification de séance
            else if (source == modifBoutonEnregistrer) {
                modifEnregistrer();
            }
        } catch (SQLException | ParseException e) {
            System.out.println(e.toString());
        }
    }

    //initialise les DAOs
    private void initDAO() {
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

    //initialise tous les panels (vides)
    private void initPanels() {
        panneauAccueil = new JPanel();
        panneauLogin = new JPanel();
        panneauEDTGrille = new JPanel();
        panneauEDTListe = new JPanel();
        panneauRecherche = new JPanel();
        panneauModifSeance = new JPanel();
        panneauRecapCours = new JPanel();
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

        //Initialisation du panel Login
        remplirPanneauLogin();

        //Initialisation du conteneur global des panneaux
        cardLayout = new CardLayout();
        global = new JPanel(cardLayout);

        //On va ajouter dans le panel global tous les différents panels
        global.add(panneauAccueil, "Accueil");
        global.add(panneauLogin, "Login");
        global.add(panneauEDTGrille, "EDTGrille");
        global.add(panneauEDTListe, "EDTListe");
        global.add(panneauRecherche, "Recherche");
        global.add(panneauModifSeance, "ModifSeance");
        global.add(panneauRecapCours, "RecapCours");
    }

    //teste si un utilisateur avec cette addresse mail et ce mot de passe existe. Retourne l'utilisateur si oui, null si non
    private Utilisateur login(String email, String passwd) throws SQLException {
        Utilisateur utilisateur = null;
        resultatFenetre = statementFenetre.executeQuery("SELECT * FROM utilisateur WHERE email = '" + email + "' AND passwd = '" + passwd + "'");
        if (resultatFenetre.first()) {
            utilisateur = utilisateurDAO.find(resultatFenetre.getInt("ID"));
        }
        resultatFenetre.close();
        return utilisateur;
    }

    //TODO : ajouter un logo ?
    private void addMenuBars(JPanel panel) {

        //Initialisation des composants du panneau
        barreNav1 = new BarreNav(Color.WHITE);
        barreNav1BoutonHome = new JButton(new ImageIcon(navBarHomeIcon));
        barreNav1BoutonDeco = new JButton("Déconnexion");
        barreNav1Logo = new JLabel(new ImageIcon(navBarLogoEce)); 
        
        barreNav2 = new BarreNav(Color.BLACK);
        barreNav2BoutonEDT = new JButton("Emploi du temps");
        barreNav2BoutonRecap = new JButton("Récapitulatif des cours");
        barreNav2BoutonRecherche = new JButton("Recherche");
        barreNav2BoutonCreer = new JButton("Ajouter une séance");

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
        //logo ece 
        barreNav1Logo.setBounds(1, 1, 48, 48);
        barreNav1.add(barreNav1Logo);  

        //bouton de retour à la page d'accueil
        barreNav1BoutonHome.setBounds(55, 5, 40, 40);
        barreNav1BoutonHome.setContentAreaFilled(false);
        barreNav1BoutonHome.setBorder(BorderFactory.createEmptyBorder());
        
        barreNav1BoutonHome.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                barreNav1BoutonHome.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                barreNav1BoutonHome.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
        barreNav1.add(barreNav1BoutonHome);

        //Bouton de déconnexion
        barreNav1BoutonDeco.setBounds(largeur - 145, 5, 120, 40);
        barreNav1BoutonDeco.setFont(new Font("Sans Serif", Font.BOLD, 16));
        barreNav1BoutonDeco.setBackground(rouge1);
        barreNav1BoutonDeco.setForeground(Color.WHITE);
        barreNav1BoutonDeco.setBorder(BorderFactory.createEmptyBorder());
        barreNav1.add(barreNav1BoutonDeco);

        panel.add(barreNav1);

        //Barre 2
        barreNav2.setLayout(null);
        barreNav2.setBounds(0, 50, largeur, 50);
        barreNav2.setFont(new Font("Sans Serif", Font.BOLD, 16));

        //Si on est connecté en tant que prof ou élève, on a le choix de voir son emploi du temps ou le récap des cours
        if (connectedUser.getDroit() == 3 || connectedUser.getDroit() == 4) {
            //bouton de l'emploi du temps de l'utilisateur
            barreNav2BoutonEDT.setBounds(10, 0, 150, 50);
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
            barreNav2BoutonEDT.setFont(new Font("Sans Serif", Font.BOLD, 18));
            barreNav2BoutonEDT.setForeground(Color.WHITE);
            barreNav2.add(barreNav2BoutonEDT);

            //bouton du récapitulatif des cours
            barreNav2BoutonRecap.setBounds(180, 0, 200, 50);
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
            barreNav2BoutonRecap.setFont(new Font("Sans Serif", Font.BOLD, 18));
            barreNav2BoutonRecap.setForeground(Color.WHITE);
            barreNav2.add(barreNav2BoutonRecap);
        }

        //Si on est connecté en tant qu'admin ou responsable pédagogique, on peut effectuer une recherche
        if (connectedUser.getDroit() == 1 || connectedUser.getDroit() == 2) {
            //bouton de recherche
            barreNav2BoutonRecherche.setBounds(10, 0, 130, 50);
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
        
        //Seul l'admin a le droit de créer des séances
        if(connectedUser.getDroit() == 1){  //si on est connecté en tant qu'admin
            //Bouton de création de séance
            barreNav2BoutonCreer.setBounds(160, 0, 200, 50);
            barreNav2BoutonCreer.setBorder(BorderFactory.createEmptyBorder());
            barreNav2BoutonCreer.setBackground(Color.BLACK);
            barreNav2BoutonCreer.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    barreNav2BoutonCreer.setBackground(Color.LIGHT_GRAY);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    barreNav2BoutonCreer.setBackground(Color.BLACK);
                }
            });
            barreNav2BoutonCreer.setFont(new Font("Sans Serif", Font.BOLD, 18));
            barreNav2BoutonCreer.setForeground(Color.WHITE);
            barreNav2.add(barreNav2BoutonCreer);
        }
        barreNav2.setBounds(0, 51, largeur, 50);
        panel.add(barreNav2);

        //Ré-activation des ActionListeners
        barreNav1BoutonHome.addActionListener(this);
        barreNav1BoutonDeco.addActionListener(this);
        barreNav2BoutonEDT.addActionListener(this);
        barreNav2BoutonRecap.addActionListener(this);
        barreNav2BoutonRecherche.addActionListener(this);
        barreNav2BoutonCreer.addActionListener(this);
    }

    private void remplirAccueil() throws SQLException {
        panneauAccueil.removeAll();
        panneauAccueil.setLayout(null);
        addMenuBars(panneauAccueil);

        int widthEDT = 275;
        int heightEDT = 700;

        //Initialisation des composants du panneau
        accueilLabelConnectedUser = new JLabel("", SwingConstants.CENTER); //set text ensuite, en fonction de l'utilisateur connecté 
        accueilEDT = new PanneauEDTAccueil();
        accueilEDTTitre = new JLabel("Emploi du temps du jour", SwingConstants.CENTER);
        accueilEDTListeCours = new ArrayList<>();
        accueilDateJour = new MyDate();
        accueilEDTLabelsHeures = new JLabel[15];    //15 label pour les heures
        accueilLogoEce = new JLabel(new ImageIcon(imageEce));
        accueilLogoInseecU = new JLabel(new ImageIcon(imageInseecU));
        
        //On retire les éventuels ActionListeners
        //...
        
        //ELEMENTS DE LA PAGE
        accueilLabelConnectedUser.setText("Bienvenue, " + connectedUser.toString() + "");
        accueilLabelConnectedUser.setFont(new Font("Sans Serif", Font.BOLD, 32));
        accueilLabelConnectedUser.setBounds(largeur / 2 - 250, 300, 500, 50);
        panneauAccueil.add(accueilLabelConnectedUser);
        
        //logos
        accueilLogoEce.setBounds(largeur/2 - 200, 150, 400, 100);
        accueilLogoInseecU.setBounds(largeur - 210, 100, 200, 125);
        panneauAccueil.add(accueilLogoEce);
        panneauAccueil.add(accueilLogoInseecU);

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
            if (connectedUser.getDroit() == 3) {  //Connecté en tant que prof
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_enseignants JOIN seance ON id_seance = id "
                        + "WHERE id_enseignant = " + connectedUser.getId() + " "
                        + "AND date = DATE(NOW())");
            } else {  //Connecté en tant qu'élève
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_groupes JOIN seance ON id_seance = id "
                        + "WHERE date = DATE(NOW()) "
                        + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = " + connectedUser.getId() + ")");
            }

            //variable pour stocker les paramètres du bouton (donc d'un cours)
            Seance tempSeanceAccueil;
            JButton tempCoursAccueil;
            int hauteurDebut;
            int hauteurCours;
            String descriptionCoursAccueil;
            while (resultatFenetre.next()) {
                //On récupère la scéance
                tempSeanceAccueil = seanceDAO.find(resultatFenetre.getInt("ID_SEANCE"));

                //on compte le nombre de minutes et on le multiplie par le nombre de pixels représentés par 1mn. On enlève ensuite l'offset de 8h30
                hauteurDebut = (int) (((tempSeanceAccueil.getHeureDebut().getHeure() * 60 + tempSeanceAccueil.getHeureDebut().getMinutes()) - (8 * 60 + 30)) * 0.833);
                //similairement, on calcule la hauteur de la fin du cours, à laquelle on soustrait la hauteur du début pour obtenir la taille
                hauteurCours = ((int) (((tempSeanceAccueil.getHeureFin().getHeure() * 60 + tempSeanceAccueil.getHeureFin().getMinutes()) - (8 * 60 + 30)) * 0.833)) - hauteurDebut;

                //CREATION DE LA DESCRIPTION DU COURS
                //nom
                descriptionCoursAccueil = "<html><center>" + tempSeanceAccueil.getCours().getNom() + " : " + tempSeanceAccueil.getTypeCours().getNom() + "-";

                //état
                switch (tempSeanceAccueil.getEtat()) {
                    case 1:
                        descriptionCoursAccueil += "En cours de validation<br>-";
                        break;
                    case 2:
                        descriptionCoursAccueil += "Validé<br>-";
                        break;
                    case 3:
                        descriptionCoursAccueil += "Annulé<br>-";
                        break;
                }

                //profs
                resultatEvent = statementEvent.executeQuery("SELECT nom FROM utilisateur JOIN seance_enseignants ON id = id_enseignant WHERE id_seance = " + tempSeanceAccueil.getId());
                while (resultatEvent.next()) {
                    descriptionCoursAccueil += resultatEvent.getString("NOM") + "-";
                }
                descriptionCoursAccueil += "<br>-";

                //groupes
                resultatEvent = statementEvent.executeQuery("SELECT nom FROM groupe JOIN seance_groupes ON id = id_groupe WHERE id_seance = " + tempSeanceAccueil.getId());
                while (resultatEvent.next()) {
                    descriptionCoursAccueil += resultatEvent.getString("NOM") + "-";
                }
                descriptionCoursAccueil += "<br>";

                //salles
                resultatEvent = statementEvent.executeQuery("SELECT * FROM salle JOIN seance_salles ON id = id_salle WHERE id_seance = " + tempSeanceAccueil.getId());
                while (resultatEvent.next()) {
                    descriptionCoursAccueil += resultatEvent.getString("NOM") + "  (" + siteDAO.find(resultatEvent.getInt("ID_SITE")).getNom() + ")";
                }
                descriptionCoursAccueil += "<br>";

                //On crée le bouton avec les paramètres déterminés précédemment
                tempCoursAccueil = new JButton(descriptionCoursAccueil + "</center></html>");
                tempCoursAccueil.setBounds(45, 35 + hauteurDebut, 217, hauteurCours);
                tempCoursAccueil.setHorizontalAlignment(SwingConstants.CENTER);
                tempCoursAccueil.setFont(new Font("Sans Serif", Font.BOLD, 11));
                tempCoursAccueil.setMargin(new Insets(0, 0, 0, 0));
                accueilEDTListeCours.add(tempCoursAccueil);
            }
            for (JButton Element : accueilEDTListeCours) {
                accueilEDT.add(Element);
            }

            panneauAccueil.add(accueilEDT);
        }
        
        //On ajoute aux élèves un graphe du nombre de séances qu'ils ont par cours
        if(connectedUser.getDroit() == 4){
            //Nombre de séances par cours
            DefaultCategoryDataset seancesParCoursDataSet = new DefaultCategoryDataset();
            resultatFenetre = statementFenetre.executeQuery("SELECT DISTINCT cours.NOM as nomCours, COUNT(seance.ID) as nbSeances "
                    + "FROM cours JOIN seance on cours.ID = seance.ID_COURS JOIN seance_groupes on seance_groupes.ID_SEANCE = seance.ID "
                    + "WHERE seance_groupes.ID_GROUPE = "+etudiantDAO.find(connectedUser.getId()).getGroupe().getId()+" "
                    + "GROUP BY cours.NOM;");
            int compte =0;
            while(resultatFenetre.next()){
                seancesParCoursDataSet.setValue(resultatFenetre.getInt("nbSeances"), "Nombre d'étudiants", resultatFenetre.getString("nomCours"));
                compte++;
            }
            accueilGraphesSeancesParCours = ChartFactory.createBarChart(
                    "Nombre de séances par cours",
                    "Cours",
                    "Nombre de séances", 
                    seancesParCoursDataSet, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true,
                    false);     
            ChartPanel chartContainerSeancesParCours = new ChartPanel(accueilGraphesSeancesParCours);
            chartContainerSeancesParCours.setBounds(largeur/2 - compte*100, hauteur/2, compte*200, hauteur/2 - 200);
            panneauAccueil.add(chartContainerSeancesParCours);
        }
        
        //Si on est connecté en tant que référent pédagogique ou en tant qu'admin, on affiche des graphes sur les données
        if(connectedUser.getDroit() == 1 || connectedUser.getDroit() == 2){
            //Nombre d'étudiants par promotion
            DefaultCategoryDataset etudiantsParPromoDataSet = new DefaultCategoryDataset();
            resultatFenetre = statementFenetre.executeQuery("SELECT promotion.NOM as nomPromo, COUNT(DISTINCT etudiant.NUMERO) as nbEtudiants "
                    + "FROM promotion JOIN groupe ON groupe.IDPROMOTION = promotion.ID JOIN etudiant ON groupe.ID = etudiant.ID_GROUPE "
                    + "GROUP BY groupe.IDPROMOTION");
            while(resultatFenetre.next()){
                etudiantsParPromoDataSet.setValue(resultatFenetre.getInt("nbEtudiants"), "Nombre d'étudiants", resultatFenetre.getString("nomPromo"));
            }
            accueilGraphesEtudiantsParPromo = ChartFactory.createBarChart(
                    "Nombre d'étudiants par promotion",
                    "Promotion",
                    "Nombre d'étudiants", 
                    etudiantsParPromoDataSet, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true,
                    false);     
            ChartPanel chartContainerEtudiantsParPromo = new ChartPanel(accueilGraphesEtudiantsParPromo);
            chartContainerEtudiantsParPromo.setBounds(largeur/4 - 300, hauteur/2, 500, hauteur/2 - 200);
            panneauAccueil.add(chartContainerEtudiantsParPromo);
            
            //Nombre de groupe par promo
            DefaultCategoryDataset groupesParPromoDataSet = new DefaultCategoryDataset();
            resultatFenetre = statementFenetre.executeQuery("SELECT promotion.NOM as nomPromo, COUNT(groupe.ID) as nbGroupes "
                    + "FROM promotion JOIN groupe ON groupe.IDPROMOTION = promotion.ID GROUP BY promotion.NOM");
            while(resultatFenetre.next()){
                groupesParPromoDataSet.setValue(resultatFenetre.getInt("nbGroupes"), "Nombre de groupes", resultatFenetre.getString("nomPromo"));
            }
            accueilGraphesGroupesParPromo = ChartFactory.createBarChart(
                    "Nombre de groupes par promotion",
                    "Promotion",
                    "Nombre de groupes", 
                    groupesParPromoDataSet, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true,
                    false);     
            ChartPanel chartContainerGroupeParPromo = new ChartPanel(accueilGraphesGroupesParPromo);
            chartContainerGroupeParPromo.setBounds(largeur/2 - 250, hauteur/2, 500, hauteur/2 - 200);
            panneauAccueil.add(chartContainerGroupeParPromo);
            
            //Nombre de séances par enseignant
            DefaultCategoryDataset seanceParEnseignantDataSet = new DefaultCategoryDataset();
            resultatFenetre = statementFenetre.executeQuery("SELECT utilisateur.PRENOM as prenomEnseignant, utilisateur.NOM as nomEnseignant, COUNT(*) as nbSeances "
                    + "FROM seance_enseignants JOIN utilisateur ON seance_enseignants.ID_ENSEIGNANT = utilisateur.ID GROUP BY seance_enseignants.ID_ENSEIGNANT;");
            while(resultatFenetre.next()){
                seanceParEnseignantDataSet.setValue(resultatFenetre.getInt("nbSeances"), "Nombre de séances", 
                        resultatFenetre.getString("prenomEnseignant")+resultatFenetre.getString("nomEnseignant"));
            }
            accueilGraphesSeancesParEnseignant = ChartFactory.createBarChart(
                    "Nombre de séances par enseignant",
                    "Enseignant",
                    "Nombre de séances", 
                    seanceParEnseignantDataSet, 
                    PlotOrientation.VERTICAL, 
                    true, 
                    true,
                    false);     
            ChartPanel chartContainerSeancesParEnseignant = new ChartPanel(accueilGraphesSeancesParEnseignant);
            chartContainerSeancesParEnseignant.setBounds(3*largeur/4 - 200, hauteur/2, 500, hauteur/2 - 200);
            panneauAccueil.add(chartContainerSeancesParEnseignant);
        }
    }

    private void remplirPanneauLogin() {
        panneauLogin.removeAll();
        panneauLogin.setLayout(null);

        //Initialisation des composants du panneau
        loginTitre = new JLabel("Connexion");
        loginErreurMessage = new JLabel("");
        loginEmailLabel = new JLabel("Email : ");
        loginEmail = new JTextField();
        loginPasswordLabel = new JLabel("Mot de passe : ");
        loginPassword = new JPasswordField();
        loginVoirPassword = new JCheckBox("Voir le mot de passe", false);
        loginBoutonValider = new JButton("Se connecter");
        loginLogoEce = new JLabel( new ImageIcon(imageEce));
        loginLogoInseecU = new JLabel(new ImageIcon(imageInseecU));
        
        //On retire les éventuels ActionListeners
        loginBoutonValider.removeActionListener(this);

        //CHAMPS LOGIN
        //Titre de la page
        loginTitre.setFont(new Font("Sans Serif", Font.BOLD, 32));
        loginTitre.setBounds(largeur / 2 - 87, hauteur / 2 - 250, 175, 50);
        panneauLogin.add(loginTitre);
        
        //logos
        loginLogoEce.setBounds(largeur/2 - 200,   800, 400,100);
        loginLogoInseecU.setBounds(largeur/2 - 100,   100, 200, 125);
        panneauLogin.add(loginLogoEce);
        panneauLogin.add(loginLogoInseecU);

        //message d'erreur
        loginErreurMessage.setForeground(Color.red);
        loginErreurMessage.setBounds(largeur / 2 - 250, hauteur / 2 - 200, 500, 25);
        panneauLogin.add(loginErreurMessage);

        //Label de l'email
        loginEmailLabel.setBounds(largeur / 2 - 225, hauteur / 2 - 125, 75, 25);
        panneauLogin.add(loginEmailLabel);

        //Entrée de l'email
        loginEmail.setBounds(largeur / 2 - 125, hauteur / 2 - 125, 250, 25);
        panneauLogin.add(loginEmail);

        //Label du mot de passe
        loginPasswordLabel.setBounds(largeur / 2 - 225, hauteur / 2 - 75, 100, 25);
        panneauLogin.add(loginPasswordLabel);

        //Entrée du mot de passe
        loginPassword.setBounds(largeur / 2 - 125, hauteur / 2 - 75, 250, 25);
        panneauLogin.add(loginPassword);

        //Checkbox voir le mote de passe
        loginVoirPassword.setBounds(largeur / 2 - 125, hauteur / 2 - 25, 250, 25);
        panneauLogin.add(loginVoirPassword);

        //Bouton de validation
        loginBoutonValider.setBounds(largeur / 2 - 125, hauteur / 2 + 25, 250, 50);
        loginBoutonValider.setBackground(vertEce);
        loginBoutonValider.setForeground(Color.WHITE);
        loginBoutonValider.setFont(new Font("Sans Serif", Font.BOLD, 30) );
        panneauLogin.add(loginBoutonValider);

        //Ré-activation des ActionListeners
        loginBoutonValider.addActionListener(this);
        loginVoirPassword.addActionListener(this);
    }

    private void remplirEDTGrille(String typeEDT) throws SQLException {
        panneauEDTGrille.removeAll();
        panneauEDTGrille.setLayout(null);
        addMenuBars(panneauEDTGrille);

        //Initialisation des composants du panneau
        EDTGrilleChoixTypeEDT = new JComboBox();
        EDTGrilleEDT = new PanneauEDTGrille();
        EDTGrilleListeCours = new ArrayList<>();
        EDTGrilleLabelsHeures = new JLabel[15];
        EDTGrillePanneauSemaines = new JPanel();
        EDTGrilleLabelsJours = new JLabel[6];

        //On retire les éventuels ActionListeners
        EDTGrilleChoixTypeEDT.removeActionListener(this);

        //ÉLÉMENTS DE LA PAGE
        //Choix du type d'emploi du temps
        EDTGrilleChoixTypeEDT.removeAllItems();
        EDTGrilleChoixTypeEDT.addItem("En grille");
        EDTGrilleChoixTypeEDT.addItem("En liste");
        EDTGrilleChoixTypeEDT.setBounds(20, 150, 100, 30);
        EDTGrilleChoixTypeEDT.setBackground(Color.white);
        panneauEDTGrille.add(EDTGrilleChoixTypeEDT);

        //Semaines
        EDTGrillePanneauSemaines.setLayout(null);
        EDTGrillePanneauSemaines.setBounds(0, 200, largeur, 40);
        JButton tempWeekButton;
        for (int i = 31; i <= 52; ++i) {
            tempWeekButton = new JButton("<html>" + i + " </html>");
            tempWeekButton.addActionListener(new SemaineEDT(i, typeEDT, "grille"));
            tempWeekButton.setBounds((i - 31) * (largeur / 52 + 1), 0, largeur / 52, 40);
            tempWeekButton.setMargin(new Insets(0, 0, 0, 0));
            EDTGrillePanneauSemaines.add(tempWeekButton);
        }
        for (int i = 1; i < 31; ++i) {
            tempWeekButton = new JButton("<html><center>" + i + " </center></html>");
            tempWeekButton.addActionListener(new SemaineEDT(i, typeEDT, "grille"));
            tempWeekButton.setBounds((i + 21) * (largeur / 52 + 1), 0, largeur / 52, 40);
            tempWeekButton.setMargin(new Insets(0, 0, 0, 0));
            EDTGrillePanneauSemaines.add(tempWeekButton);
        }
        panneauEDTGrille.add(EDTGrillePanneauSemaines);

        //Initialisation du sous-panneau qui contient l'EDT
        EDTGrilleEDT.setBounds(largeur / 2 - 696, 250, 1392, 700);
        EDTGrilleEDT.setBackground(Color.WHITE);
        EDTGrilleEDT.setLayout(null);

        //On crée un objet Calendar, que l'on fixe sur le premier jour de la semaine souhaitée
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek);
        tempCalendar.set(Calendar.DAY_OF_WEEK, tempCalendar.getFirstDayOfWeek());

        //Création des labels des heures pour l'EDT
        (EDTGrilleLabelsHeures[0] = new JLabel("08h30")).setBounds(5, 22, 40, 25); //On définit la position et la taille du label créé en début de ligne
        (EDTGrilleLabelsHeures[1] = new JLabel("10h00")).setBounds(5, 97, 40, 25);
        (EDTGrilleLabelsHeures[2] = new JLabel("10h15")).setBounds(5, 110, 40, 25);
        (EDTGrilleLabelsHeures[3] = new JLabel("11h45")).setBounds(5, 185, 40, 25);
        (EDTGrilleLabelsHeures[4] = new JLabel("12h00")).setBounds(5, 197, 40, 25);
        (EDTGrilleLabelsHeures[5] = new JLabel("13h30")).setBounds(5, 272, 40, 25);
        (EDTGrilleLabelsHeures[6] = new JLabel("13h45")).setBounds(5, 285, 40, 25);
        (EDTGrilleLabelsHeures[7] = new JLabel("15h15")).setBounds(5, 360, 40, 25);
        (EDTGrilleLabelsHeures[8] = new JLabel("15h30")).setBounds(5, 372, 40, 25);
        (EDTGrilleLabelsHeures[9] = new JLabel("17h00")).setBounds(5, 447, 40, 25);
        (EDTGrilleLabelsHeures[10] = new JLabel("17h15")).setBounds(5, 459, 40, 25);
        (EDTGrilleLabelsHeures[11] = new JLabel("18h45")).setBounds(5, 535, 40, 25);
        (EDTGrilleLabelsHeures[12] = new JLabel("19h00")).setBounds(5, 547, 40, 25);
        (EDTGrilleLabelsHeures[13] = new JLabel("20h30")).setBounds(5, 622, 40, 25);
        (EDTGrilleLabelsHeures[14] = new JLabel("21h30")).setBounds(5, 672, 40, 25);

        //On ajoute les labels sur le sous-panneau
        for (int i = 0; i < 15; ++i) {
            EDTGrilleEDT.add(EDTGrilleLabelsHeures[i]);
        }

        //On ajoute les cours dans l'EDT
        switch (typeEDT) {
            case "etudiant":
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_groupes JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = " + etudiantSelection.getId() + ")");
                break;
            case "enseignant":
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_enseignants JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_enseignant = " + enseignantSelection.getId());
                break;
            case "salle":
                resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_salles JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_salle = " + salleSelection.getId());
                break;
            default:
                System.out.println("Erreur de type d'emploi du temps");
        }
        
        //Comme on va récupérer les cours par date, on va se positionner sur la bonne année
        if(selectedWeek > 30){
            tempCalendar.add(Calendar.YEAR, -1);
            tempCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek);
            tempCalendar.set(Calendar.DAY_OF_WEEK, tempCalendar.getFirstDayOfWeek());
        }
        
         //Création des lables des jours pour l'EDT
        for (int i = 0; i < 6; ++i) {
            EDTGrilleLabelsJours[i] = new JLabel("" + tempCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (tempCalendar.get(Calendar.MONTH) + 1));
            EDTGrilleLabelsJours[i].setBounds(45 + (i + 1) * 109 + i * 109 - 20, 10, 40, 25); //On définit la position et la taille du label créé en début de ligne
            tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
            EDTGrilleEDT.add(EDTGrilleLabelsJours[i]);
        }

        Seance tempSeance;
        JButton tempCours;
        int hauteurDebut;
        int hauteurCours;
        String descriptionCours;
        EDTGrilleListeCours.clear();
        int compte = 0;
        while (resultatFenetre.next()) {
            tempSeance = seanceDAO.find(resultatFenetre.getInt("ID_SEANCE"));

            //on compte le nombre de minutes et on le multiplie par le nombre de pixels représentés par 1mn. On enlève ensuite l'offset de 8h30
            hauteurDebut = (int) (((tempSeance.getHeureDebut().getHeure() * 60 + tempSeance.getHeureDebut().getMinutes()) - (8 * 60 + 30)) * 0.833);
            //similairement, on calcule la hauteur de la fin du cours, à laquelle on soustrait la hauteur du début pour obtenir la taille
            hauteurCours = ((int) (((tempSeance.getHeureFin().getHeure() * 60 + tempSeance.getHeureFin().getMinutes()) - (8 * 60 + 30)) * 0.833)) - hauteurDebut;

            //CREATION DE LA DESCRIPTION DU COURS
            //nom
            descriptionCours = "<html><center>" + tempSeance.getCours().getNom() + " : " + tempSeance.getTypeCours().getNom() + "-";

            //état
            switch (tempSeance.getEtat()) {
                case 1:
                    descriptionCours += "En cours de validation<br>-";
                    break;
                case 2:
                    descriptionCours += "Validé<br>-";
                    break;
                case 3:
                    descriptionCours += "Annulé<br>-";
                    break;
            }

            //profs
            resultatEvent = statementEvent.executeQuery("SELECT nom FROM utilisateur JOIN seance_enseignants ON id = id_enseignant "
                    + "WHERE id_seance = " + tempSeance.getId());
            while (resultatEvent.next()) {
                descriptionCours += resultatEvent.getString("NOM") + "-";
            }
            descriptionCours += "<br>-";

            //groupes
            resultatEvent = statementEvent.executeQuery("SELECT nom FROM groupe JOIN seance_groupes ON id = id_groupe WHERE id_seance = " + tempSeance.getId());
            while (resultatEvent.next()) {
                descriptionCours += resultatEvent.getString("NOM") + "-";
            }
            descriptionCours += "<br>";

            //salles
            resultatEvent = statementEvent.executeQuery("SELECT * FROM salle JOIN seance_salles ON id = id_salle WHERE id_seance = " + tempSeance.getId());
            while (resultatEvent.next()) {
                descriptionCours += resultatEvent.getString("NOM") + "  (" + siteDAO.find(resultatEvent.getInt("ID_SITE")).getNom() + ")";
            }
            descriptionCours += "<br>";

            //création du bouton avec les paramètres déterminés plus tot
            tempCours = new JButton(descriptionCours + "</center></html>");
            tempCours.setBounds(45 + (tempSeance.getDate().getJourDeLaSemaine() - 1) * 218, 35 + hauteurDebut, 217, hauteurCours);
            tempCours.setHorizontalAlignment(SwingConstants.CENTER);
            tempCours.setFont(new Font("Sans Serif", Font.BOLD, 11));
            tempCours.setMargin(new Insets(0, 0, 0, 0));
            tempCours.addActionListener(new CoursEDT(tempSeance, compte, typeEDT, "grille"));
            EDTGrilleListeCours.add(tempCours);
            compte++;
        }
        for (JButton Element : EDTGrilleListeCours) {
            EDTGrilleEDT.add(Element);
        }
        panneauEDTGrille.add(EDTGrilleEDT);

        //Ré-activation des ActionListeners
        EDTGrilleChoixTypeEDT.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    //possibilité de changer vers l'emploi du temps en format Liste
                    switch ((String) EDTGrilleChoixTypeEDT.getSelectedItem()) {
                        case "En grille":
                            break;
                        case "En liste":
                            remplirEDTListe(typeEDT);
                            cardLayout.show(global, "EDTListe");
                            break;
                        default:
                            System.out.println("Erreur lors du choix de type d'emploi du temps");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }
    
    private void remplirEDTListe(String typeEDT) throws SQLException{
        panneauEDTListe.removeAll();
        panneauEDTListe.setLayout(null);
        addMenuBars(panneauEDTListe);
        
        //Initialisation des composants du panneau
        EDTListeChoixTypeEDT = new JComboBox();
        EDTListeEDTContainer = new JScrollPane();
        EDTListeEDT = new JPanel();
        EDTListeListeCours = new ArrayList<>();
        EDTListePanneauSemaines = new JPanel();

        //On retire les éventuels ActionListeners
        EDTListeChoixTypeEDT.removeActionListener(this);
        
        //ÉLÉMENTS DE LA PAGE
        //Choix du type d'emploi du temps
        EDTListeChoixTypeEDT.removeAllItems();
        EDTListeChoixTypeEDT.addItem("En grille");
        EDTListeChoixTypeEDT.addItem("En liste");
        EDTListeChoixTypeEDT.setSelectedItem("En liste");
        EDTListeChoixTypeEDT.setBounds(20, 150, 100, 30);
        EDTListeChoixTypeEDT.setBackground(Color.white);
        panneauEDTListe.add(EDTListeChoixTypeEDT);
        
        //Semaines
        EDTListePanneauSemaines.setLayout(null);
        EDTListePanneauSemaines.setBounds(0, 200, largeur, 40);
        JButton tempWeekButton;
        for (int i = 31; i <= 52; ++i) {
            tempWeekButton = new JButton("<html>" + i + " </html>");
            tempWeekButton.addActionListener(new SemaineEDT(i, typeEDT, "liste"));
            tempWeekButton.setBounds((i - 31) * (largeur / 52 + 1), 0, largeur / 52, 40);
            tempWeekButton.setMargin(new Insets(0, 0, 0, 0));
            EDTListePanneauSemaines.add(tempWeekButton);
        }
        for (int i = 1; i < 31; ++i) {
            tempWeekButton = new JButton("<html><center>" + i + " </center></html>");
            tempWeekButton.addActionListener(new SemaineEDT(i, typeEDT, "liste"));
            tempWeekButton.setBounds((i + 21) * (largeur / 52 + 1), 0, largeur / 52, 40);
            tempWeekButton.setMargin(new Insets(0, 0, 0, 0));
            EDTListePanneauSemaines.add(tempWeekButton);
        }
        panneauEDTListe.add(EDTListePanneauSemaines);
        
        //On crée un objet Calendar, que l'on fixe sur le premier jour de la semaine souhaitée
        Calendar tempCalendar = Calendar.getInstance();
        tempCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek);
        tempCalendar.set(Calendar.DAY_OF_WEEK, tempCalendar.getFirstDayOfWeek());
        //System.out.println(tempCalendar.getTime());
        
        //Initialiser le panneau où l'on affiche les séances, en fonction du nombre de séancez et du nombre de jours avec séances
        switch (typeEDT) {
            case "etudiant":
                resultatFenetre = statementFenetre.executeQuery("SELECT COUNT(id_seance) as nombreSeances, COUNT(DISTINCT date) as nbJoursRemplis "
                        + "FROM seance_groupes JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = " + etudiantSelection.getId() + ")");
                break;
            case "enseignant":
                resultatFenetre = statementFenetre.executeQuery("SELECT COUNT(id_seance) as nombreSeances, COUNT(DISTINCT date) as nbJoursRemplis "
                        + "FROM seance_enseignants JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_enseignant = " + enseignantSelection.getId());
                break;
            case "salle":
                resultatFenetre = statementFenetre.executeQuery("SELECT COUNT(id_seance) as nombreSeances, COUNT(DISTINCT date) as nbJoursRemplis "
                        + "FROM seance_salles JOIN seance ON id_seance = id "
                        + "WHERE WEEK(date,3) = " + selectedWeek + " "
                        + "AND YEAR(date) = " + ((selectedWeek < 31) ? (tempCalendar.get(Calendar.YEAR)) : (tempCalendar.get(Calendar.YEAR) - 1)) + " "
                        + "AND id_salle = " + salleSelection.getId());
                break;
            default:
                System.out.println("Erreur de type d'emploi du temps");
        }
        if(resultatFenetre.first()){
            //Initialisation du sous-panneau qui contient l'EDT et qui est scrollable
            if((resultatFenetre.getInt("nbJoursRemplis"))*50 + (resultatFenetre.getInt("nombreSeances"))*40 < 700){
                EDTListeEDTContainer.setBounds(20, 250, 1000, (resultatFenetre.getInt("nbJoursRemplis"))*50 + (resultatFenetre.getInt("nombreSeances"))*40);
            }else{
                EDTListeEDTContainer.setBounds(20, 250, 1000, 700);
            }
            
            //Initialisation du sous-panneau contenu
            EDTListeEDT.setLayout(null);
        }
        EDTListeEDT.setBackground(Color.WHITE);
        
        //Comme on va récupérer les cours par date, on va se positionner sur la bonne année
        if(selectedWeek > 30){
            tempCalendar.add(Calendar.YEAR, -1);
            tempCalendar.set(Calendar.WEEK_OF_YEAR, selectedWeek);
            tempCalendar.set(Calendar.DAY_OF_WEEK, tempCalendar.getFirstDayOfWeek());
        }
        
        
        int compte = 5;  //suivi du nombre de pixels parcourus à la verticale
        int compteCours = 0;
        JPanel tempPanel;
        JLabel tempLabelJour;
        JLabel tempLabel[] = new JLabel[7]; //7 labels temporaires pour les détails du cour
        Seance tempSeance;
        String tempContenu;
        EDTListeListeCours.clear();
        
        //Remplir la liste des cours
        for(int i=0;i<5;++i){
            //récupération des séances du jour
            switch (typeEDT) {
                case "etudiant":
                    resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_groupes JOIN seance ON id_seance = id "
                            + "WHERE date = '"+new java.sql.Date(tempCalendar.getTimeInMillis())+"' "
                            + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = " + etudiantSelection.getId() + ") ORDER BY heure_debut ASC");
                    break;
                case "enseignant":
                    resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_enseignants JOIN seance ON id_seance = id "
                            + "WHERE date = '"+new java.sql.Date(tempCalendar.getTimeInMillis())+"' "
                            + "AND id_enseignant = " + enseignantSelection.getId()+" ORDER BY heure_debut ASC");
                    break;
                case "salle":
                    resultatFenetre = statementFenetre.executeQuery("SELECT id_seance FROM seance_salles JOIN seance ON id_seance = id "
                            + "WHERE date = '"+new java.sql.Date(tempCalendar.getTimeInMillis())+"' "
                            + "AND id_salle = " + salleSelection.getId()+" ORDER BY heure_debut ASC");
                    break;
                default:
                    System.out.println("Erreur de type d'emploi du temps");
            }
            if(resultatFenetre.isBeforeFirst()){    //On ne crée des éléments que s'il y a au moins 1 séance pour le jour
                //On affiche le nom du jour en premier
                tempPanel = new JPanel();
                tempPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                tempPanel.setLayout(null);
                tempPanel.removeAll();
                tempPanel.setBounds(5, compte, 975, 30);
                compte += 30;
                tempPanel.setBackground(bleu1);
                
                tempLabelJour = new JLabel("",SwingConstants.LEFT);
                tempLabelJour.setBounds(5, 3, 500, 25);
                tempLabelJour.setText("<html><b>"+parseDayOfTheWeek(tempCalendar.get(Calendar.DAY_OF_WEEK))
                        +" "+tempCalendar.get(Calendar.DAY_OF_MONTH)
                        +" "+parseMonth(tempCalendar.get(Calendar.MONTH))
                        +" "+tempCalendar.get(Calendar.YEAR)
                        +"</b></html>");
                tempPanel.add(tempLabelJour);
                
                EDTListeEDT.add(tempPanel);
                while(resultatFenetre.next()){
                    tempSeance = seanceDAO.find(resultatFenetre.getInt("ID_SEANCE"));
                    
                    //onn affiche chaque séance du jour
                    tempPanel = new JPanel();
                    tempPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                    tempPanel.setLayout(null);
                    tempPanel.removeAll();
                    tempPanel.setBounds(5, compte, 975, 35);
                    compte += 35;
                    tempPanel.setBackground(Color.LIGHT_GRAY);
                    
                    //heure
                    tempLabel[0]= new JLabel("");
                    tempLabel[0].setText(tempSeance.getHeureDebut()+" - "+tempSeance.getHeureFin());
                    tempLabel[0].setBounds(2, 3, 138, 30);
                    tempPanel.add(tempLabel[0]);
                    
                    //Nom du cours
                    tempLabel[1]= new JLabel("");
                    tempLabel[1].setText(tempSeance.getCours().getNom());
                    tempLabel[1].setBounds(140, 3, 140, 30);
                    tempPanel.add(tempLabel[1]);
                    
                    //Profs
                    tempLabel[2]= new JLabel("");
                    tempContenu = "<html>";
                    resultatEvent = statementEvent.executeQuery("SELECT nom FROM utilisateur JOIN seance_enseignants ON id = id_enseignant "
                            + "WHERE id_seance = " + tempSeance.getId());
                    while (resultatEvent.next()) {
                        tempContenu += resultatEvent.getString("NOM") + "<br>";
                    }
                    tempContenu += "</html>";
                    tempLabel[2].setText(tempContenu);
                    tempLabel[2].setBounds(280, 3, 100, 30);
                    tempPanel.add(tempLabel[2]);
                    
                    //Groupes
                    tempLabel[3]= new JLabel("");
                    tempContenu = "<html>";
                     resultatEvent = statementEvent.executeQuery("SELECT id_groupe FROM seance_groupes WHERE id_seance = " + tempSeance.getId());
                    while (resultatEvent.next()) {
                        tempContenu += groupeDAO.find(resultatEvent.getInt("ID_GROUPE")).getNom() + " (promo. "+groupeDAO.find(resultatEvent.getInt("ID_GROUPE")).getPromotion().getNom()+")<br>";
                    }
                    tempContenu += "</html>";
                    tempLabel[3].setText(tempContenu);
                    tempLabel[3].setBounds(380, 3, 140, 30);
                    tempPanel.add(tempLabel[3]);
                    
                    //Salles
                    tempLabel[4]= new JLabel("");
                    tempContenu = "<html>";
                     resultatEvent = statementEvent.executeQuery("SELECT id_salle FROM seance_salles WHERE id_seance = " + tempSeance.getId());
                    while (resultatEvent.next()) {
                        tempContenu += salleDAO.find(resultatEvent.getInt("ID_SALLE")).getNom() + " ("+salleDAO.find(resultatEvent.getInt("ID_SALLE")).getSite().getNom()+")<br>";
                    }
                    tempContenu += "</html>";
                    tempLabel[4].setText(tempContenu);
                    tempLabel[4].setBounds(520, 3, 150, 30);
                    tempPanel.add(tempLabel[4]);
                    
                    //Type cours
                    tempLabel[5]= new JLabel("");
                    tempLabel[5].setText(tempSeance.getTypeCours().getNom());
                    tempLabel[5].setBounds(670, 3, 130, 30);
                    tempPanel.add(tempLabel[5]);
                    
                    //Etat cours
                    tempLabel[6]= new JLabel("");
                    tempContenu = "";
                    switch (tempSeance.getEtat()) {
                        case 1:
                            tempContenu += "En cours de validation";
                            break;
                        case 2:
                            tempContenu += "Validé";
                            break;
                        case 3:
                            tempContenu += "Annulé";
                            break;
                    }
                    tempLabel[6].setText(tempContenu);
                    tempLabel[6].setBounds(800, 3, 160, 30);
                    tempPanel.add(tempLabel[6]);

                    tempPanel.addMouseListener(new CoursEDTListe(tempSeance, compteCours, typeEDT, "liste"));
                    
                    EDTListeListeCours.add(tempPanel);
                    compteCours++;
                }
                compte += 5;
            }
            
            //on passe au jour suivant
            tempCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        
        for(JPanel Element : EDTListeListeCours){
            EDTListeEDT.add(Element);
        }
        
        EDTListeEDTContainer.setViewportView(EDTListeEDT);
        panneauEDTListe.add(EDTListeEDTContainer);
        
        //Ré-activation des ActionListeners
        EDTListeChoixTypeEDT.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent e){
                try {
                    //possibilité de changer vers l'emploi du temps en format Liste
                    switch ((String) EDTListeChoixTypeEDT.getSelectedItem()) {
                        case "En grille":
                            remplirEDTGrille(typeEDT);
                            cardLayout.show(global, "EDTGrille");
                            break;
                        case "En liste":
                            break;
                        default:
                            System.out.println("Erreur lors du choix de type d'emploi du temps");
                    }
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }
    
    
    private void remplirRecherche() throws SQLException {
        panneauRecherche.removeAll();
        panneauRecherche.setLayout(null);
        addMenuBars(panneauRecherche);

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
        rechercheTitre = new JLabel("Recherche spécifique",SwingConstants.CENTER);
        rechercheLabelSemaine = new JLabel("Semaine :");
        rechercheLabelSalle = new JLabel("Salle :");
        rechercheLabelEnseignant = new JLabel("Enseignant :");
        rechercheLabelEtudiant = new JLabel("Etudiant :");

        //On retire les éventuels ActionListeners
        rechercheChoixSite.removeActionListener(this);
        rechercheChoixPromotion.removeActionListener(this);
        rechercheChoixGroupe.removeActionListener(this);
        boutonRechercherSalle.removeActionListener(this);
        boutonRechercherEnseignant.removeActionListener(this);
        boutonRechercherEtudiant.removeActionListener(this);

        //Titre de la page
        rechercheTitre.setFont(new Font("Sans Serif", Font.ITALIC, 30));
        rechercheTitre.setBounds(largeur / 2 - 350, hauteur / 7, 700, 30);
        panneauRecherche.add(rechercheTitre);

        //Semaine
        rechercheChoixSemaine.removeAllItems();
        for (int i = 1; i <= 52; ++i) {
            rechercheChoixSemaine.addItem(i);
        }
        rechercheLabelSemaine.setFont(new Font("Sans Serif", Font.BOLD, 20));
        rechercheLabelSemaine.setBounds(largeur / 2 - 65, hauteur / 3 - 55, 100, 30);
        rechercheChoixSemaine.setBackground(Color.white);
        rechercheChoixSemaine.setBounds(largeur / 2 - 50, hauteur / 3 - 25, 50, 30);
        panneauRecherche.add(rechercheChoixSemaine);
        panneauRecherche.add(rechercheLabelSemaine);

        //Site de la salle
        rechercheChoixSite.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            rechercheChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        rechercheChoixSite.setBackground(Color.white);
        rechercheChoixSite.setBounds(largeur / 2 - 625, hauteur / 2 - 50, 75, 25);
        panneauRecherche.add(rechercheChoixSite);

        //Salle
        rechercheChoixSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site) rechercheChoixSite.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
        }
        rechercheLabelSalle.setFont(new Font("Sans Serif", Font.BOLD, 20));
        rechercheLabelSalle.setBounds(largeur / 2 - 525, hauteur / 2 - 75, 100, 25);
        rechercheChoixSalle.setBackground(Color.white);
        rechercheChoixSalle.setBounds(largeur / 2 - 525, hauteur / 2 - 50, 125, 25);
        panneauRecherche.add(rechercheChoixSalle);
        panneauRecherche.add(rechercheLabelSalle);

        //Enseignant
        rechercheChoixEnseignant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur WHERE droit = 3");
        while (resultatFenetre.next()) {
            rechercheChoixEnseignant.addItem(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        rechercheLabelEnseignant.setFont(new Font("Sans Serif", Font.BOLD, 20));
        rechercheLabelEnseignant.setBounds(largeur / 2 - 92, hauteur / 2 - 75, 150, 25);
        rechercheChoixEnseignant.setBackground(Color.white);
        rechercheChoixEnseignant.setBounds(largeur / 2 - 92, hauteur / 2 - 50, 135, 25);
        panneauRecherche.add(rechercheChoixEnseignant);
        panneauRecherche.add(rechercheLabelEnseignant);

        //Promotion du groupe de l'élève
        rechercheChoixPromotion.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
        while (resultatFenetre.next()) {
            rechercheChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
        }
        rechercheChoixPromotion.setBackground(Color.white);
        rechercheChoixPromotion.setBounds(largeur / 2 + 265, hauteur / 2 - 50, 60, 25);
        panneauRecherche.add(rechercheChoixPromotion);

        //Groupe de l'élève
        rechercheChoixGroupe.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) rechercheChoixPromotion.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixGroupe.addItem(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        rechercheChoixGroupe.setBackground(Color.white);
        rechercheChoixGroupe.setBounds(largeur / 2 + 350, hauteur / 2 - 50, 100, 25);
        panneauRecherche.add(rechercheChoixGroupe);

        //Élève
        rechercheChoixEtudiant.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_utilisateur FROM etudiant WHERE id_groupe = " + ((Groupe) rechercheChoixGroupe.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            rechercheChoixEtudiant.addItem(etudiantDAO.find(resultatFenetre.getInt("ID_UTILISATEUR")));
        }
        rechercheLabelEtudiant.setFont(new Font("Sans Serif", Font.BOLD, 20));
        rechercheLabelEtudiant.setBounds(largeur / 2 + 475, hauteur / 2 - 75, 100, 25);
        rechercheChoixEtudiant.setBackground(Color.white);
        rechercheChoixEtudiant.setBounds(largeur / 2 + 475, hauteur / 2 - 50, 200, 25);
        panneauRecherche.add(rechercheChoixEtudiant);
        panneauRecherche.add(rechercheLabelEtudiant);

        //Boutons de recherche
        boutonRechercherSalle.setBackground(vertEce);
        boutonRechercherSalle.setForeground(Color.WHITE);
        boutonRechercherSalle.setBounds(largeur / 2 - 600, hauteur / 2, 175, 50);
        panneauRecherche.add(boutonRechercherSalle);
        
        boutonRechercherEnseignant.setBackground(vertEce);
        boutonRechercherEnseignant.setForeground(Color.WHITE);
        boutonRechercherEnseignant.setBounds(largeur / 2 - 125, hauteur / 2, 200, 50);
        panneauRecherche.add(boutonRechercherEnseignant);
        
        boutonRechercherEtudiant.setBackground(vertEce);
        boutonRechercherEtudiant.setForeground(Color.WHITE);
        boutonRechercherEtudiant.setBounds(largeur / 2 + 393, hauteur / 2, 175, 50);
        panneauRecherche.add(boutonRechercherEtudiant);
        
        //Ré-activation des ActionListeners
        rechercheChoixSite.addActionListener(this);
        rechercheChoixPromotion.addActionListener(this);
        rechercheChoixGroupe.addActionListener(this);
        boutonRechercherSalle.addActionListener(this);
        boutonRechercherEnseignant.addActionListener(this);
        boutonRechercherEtudiant.addActionListener(this);
    }
    
    //Correction bug redirection
    private void remplirModifSeance() throws SQLException {
        panneauModifSeance.removeAll();
        panneauModifSeance.setLayout(null); //On définit un absolute Layout
        addMenuBars(panneauModifSeance);

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
        modifAnnonceDebut = new JLabel("", SwingConstants.CENTER);
        modifLabelDate = new JLabel("Date : ");
        modifLabelHeureDebut = new JLabel("Heure de début");
        modifLabelHeureFin = new JLabel("Heure de fin");
        modifLabelEtatCours = new JLabel("Etat de la procédure : ");
        modifLabelChoixCours = new JLabel("Cours de la séance: ");
        modifLabelChoixSalle = new JLabel("Etablissement et salle : ");
        modifLabelChoixProf = new JLabel("Intervenants : ");
        modifLabelChoixPromo = new JLabel("Promotion et groupes : ");
        modifLabelTypeCours = new JLabel("Type de séance : ");
        modifChoixProfSocket = new JScrollPane();
        modifAddProfSocket = new JScrollPane();
        modifChoixGroupeSocket = new JScrollPane();
        modifAddGroupeSocket = new JScrollPane();

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
        //Titre
        if (seanceSelection.getId() != 0) {
            modifAnnonceDebut.setText("Modification de  la Séance " + seanceSelection.toString());
        } else {
            modifAnnonceDebut.setText("Création d'une nouvelle séance");
        }
        modifAnnonceDebut.setBounds(largeur / 2 - 450, hauteur / 10 - 5, 900, 30);
        panneauModifSeance.add(modifAnnonceDebut);

        //Année
        modifChoixAnnee.removeAllItems();
        for (int i = 2010; i <= 2030; ++i) {
            modifChoixAnnee.addItem(i);
        }
        if (seanceSelection.getId() != 0) {   //si une séance a été selectionné = si on est en modification
            modifChoixAnnee.setSelectedItem(seanceSelection.getDate().getAnnee());
        }
        modifChoixAnnee.setBounds(largeur / 2 + 22, hauteur / 10 + 50, 60, 25);
        modifChoixAnnee.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixAnnee);

        //Mois
        modifChoixMois.removeAllItems();
        for (int i = 1; i <= 12; ++i) {
            modifChoixMois.addItem(i);
        }
        if (seanceSelection.getId() != 0) {   //si une séance a été selectionné = si on est en modification
            modifChoixMois.setSelectedItem(seanceSelection.getDate().getMois());
        }
        modifChoixMois.setBounds(largeur / 2 - 20, hauteur / 10 + 50, 40, 25);
        modifChoixMois.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixMois);

        //Jour
        modifFillDaysOfMonth();
        if (seanceSelection.getId() != 0) {   //si une séance a été selectionné = si on est en modification
            modifChoixJour.setSelectedItem(seanceSelection.getDate().getJour());
        }
        modifLabelDate.setBounds(largeur / 2 - 182, hauteur / 10 + 50, 90, 30);
        modifChoixJour.setBounds(largeur / 2 - 62, hauteur / 10 + 50, 40, 25);
        modifChoixJour.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixJour);
        panneauModifSeance.add(modifLabelDate);

        //Heure de début de séance
        JSpinner.NumberEditor heureDebutSpinnerEditor = new JSpinner.NumberEditor(modifChoixHeureDebut);
        modifChoixHeureDebut.setEditor(heureDebutSpinnerEditor);
        heureDebutSpinnerEditor.getModel().setMinimum(7);
        heureDebutSpinnerEditor.getModel().setMaximum(20);
        heureDebutSpinnerEditor.getModel().setStepSize(1);
        heureDebutSpinnerEditor.getModel().setValue(seanceSelection.getHeureDebut().getHeure());
        modifChoixHeureDebut.setBounds(largeur / 2 - 92, hauteur / 10 + (hauteur / 10), 40, 25);
        modifLabelHeureDebut.setBounds(largeur / 2 - 182, hauteur / 10 + (hauteur / 10), 90, 30);
        panneauModifSeance.add(modifChoixHeureDebut);
        panneauModifSeance.add(modifLabelHeureDebut);

        //Minute de début de séance
        JSpinner.NumberEditor minutesDebutSpinnerEditor = new JSpinner.NumberEditor(modifChoixMinutesDebut);
        modifChoixMinutesDebut.setEditor(minutesDebutSpinnerEditor);
        minutesDebutSpinnerEditor.getModel().setMinimum(0);
        minutesDebutSpinnerEditor.getModel().setMaximum(59);
        minutesDebutSpinnerEditor.getModel().setStepSize(1);
        minutesDebutSpinnerEditor.getModel().setValue(seanceSelection.getHeureDebut().getMinutes());
        modifChoixMinutesDebut.setBounds(largeur / 2 - 50, hauteur / 10 + (hauteur / 10), 40, 25);
        panneauModifSeance.add(modifChoixMinutesDebut);

        //Heure de fin de séance
        JSpinner.NumberEditor heureFinSpinnerEditor = new JSpinner.NumberEditor(modifChoixHeureFin);
        modifChoixHeureFin.setEditor(heureFinSpinnerEditor);
        heureFinSpinnerEditor.getModel().setMinimum(7);
        heureFinSpinnerEditor.getModel().setMaximum(20);
        heureFinSpinnerEditor.getModel().setStepSize(1);
        heureFinSpinnerEditor.getModel().setValue(seanceSelection.getHeureFin().getHeure());
        modifChoixHeureFin.setBounds(largeur / 2 + 10, hauteur / 10 + (hauteur / 10), 40, 25);
        panneauModifSeance.add(modifChoixHeureFin);

        //Minute de fin de séance
        JSpinner.NumberEditor minutesFinSpinnerEditor = new JSpinner.NumberEditor(modifChoixMinutesFin);
        modifChoixMinutesFin.setEditor(minutesFinSpinnerEditor);
        minutesFinSpinnerEditor.getModel().setMinimum(0);
        minutesFinSpinnerEditor.getModel().setMaximum(59);
        minutesFinSpinnerEditor.getModel().setStepSize(1);
        minutesFinSpinnerEditor.getModel().setValue(seanceSelection.getHeureFin().getMinutes());
        modifChoixMinutesFin.setBounds(largeur / 2 + 52, hauteur / 10 + (hauteur / 10), 40, 25);
        modifLabelHeureFin.setBounds(largeur / 2 + 94, hauteur / 10 + (hauteur / 10), 100, 30);
        panneauModifSeance.add(modifChoixMinutesFin);
        panneauModifSeance.add(modifLabelHeureFin);

        //État de la séance
        modifChoixEtat.removeAllItems();
        modifChoixEtat.addItem("En cours de validation");
        modifChoixEtat.addItem("Validé");
        modifChoixEtat.addItem("Annulé");
        switch (seanceSelection.getEtat()) {
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
        modifChoixEtat.setBounds(largeur / 2 - 73, hauteur / 10 + 2 * (hauteur / 10), 154, 30);
        modifLabelEtatCours.setBounds(largeur / 2 - 65, hauteur / 10 + 2 * (hauteur / 10) - 40, 150, 30);
        modifChoixEtat.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixEtat);
        panneauModifSeance.add(modifLabelEtatCours);

        //Cours de la séance
        modifChoixCours.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM cours");
        while (resultatFenetre.next()) {
            modifChoixCours.addItem(coursDAO.find(resultatFenetre.getInt("ID")));
        }
        for (int i = 0; i < modifChoixCours.getItemCount(); ++i) {
            if (((Cours) modifChoixCours.getItemAt(i)).getId() == seanceSelection.getCours().getId()) {
                modifChoixCours.setSelectedIndex(i);
            }
        }
        modifChoixCours.setBounds(largeur / 2 - 73, hauteur / 10 + 3 * (hauteur / 10), 154, 30);
        modifLabelChoixCours.setBounds(largeur / 2 - 50, hauteur / 10 + 3 * (hauteur / 10) - 40, 150, 30);
        modifChoixCours.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixCours);
        panneauModifSeance.add(modifLabelChoixCours);

        //Type de cours
        modifChoixTypeCours.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM type_cours");
        while (resultatFenetre.next()) {
            modifChoixTypeCours.addItem(typeCoursDAO.find(resultatFenetre.getInt("ID")));
        }
        for (int i = 0; i < modifChoixTypeCours.getItemCount(); ++i) {
            if (((TypeCours) modifChoixTypeCours.getItemAt(i)).getId() == seanceSelection.getTypeCours().getId()) {
                modifChoixTypeCours.setSelectedIndex(i);
            }
        }
        modifChoixTypeCours.setBounds(largeur / 2 - 73, hauteur / 10 + 4 * (hauteur / 10), 154, 30);
        modifLabelTypeCours.setBounds(largeur / 2 - 50, hauteur / 10 + 4 * (hauteur / 10) - 40, 100, 30);
        modifChoixTypeCours.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixTypeCours);
        panneauModifSeance.add(modifLabelTypeCours);

        //Site de la salle
        modifChoixSite.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM site");
        while (resultatFenetre.next()) {
            modifChoixSite.addItem(siteDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id_site FROM salle JOIN seance_salles ON id = id_salle where id_seance = " + seanceSelection.getId());
        if (resultatFenetre.first()) {
            for (int i = 0; i < modifChoixSite.getItemCount(); ++i) {
                if (((Site) modifChoixSite.getItemAt(i)).getId() == resultatFenetre.getInt("ID_SITE")) {
                    modifChoixSite.setSelectedIndex(i);
                }
            }
        }
        modifChoixSite.setBounds(largeur / 2 - 94, hauteur / 10 + 5 * (hauteur / 10), 70, 30);
        modifLabelChoixSalle.setBounds(largeur / 2 - 65, hauteur / 10 + 5 * (hauteur / 10) - 40, 150, 30);
        modifChoixSite.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixSite);
        panneauModifSeance.add(modifLabelChoixSalle);

        //Salle
        modifChoixSalle.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle WHERE id_site = " + ((Site) modifChoixSite.getSelectedItem()).getId());
        while (resultatFenetre.next()) {
            modifChoixSalle.addItem(salleDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM salle JOIN seance_salles ON id = id_salle where id_seance = " + seanceSelection.getId());
        if (resultatFenetre.first()) {
            for (int i = 0; i < modifChoixSalle.getItemCount(); ++i) {
                if (((Salle) modifChoixSalle.getItemAt(i)).getId() == resultatFenetre.getInt("ID")) {
                    modifChoixSalle.setSelectedIndex(i);
                }
            }
        }
        modifChoixSalle.setBounds(largeur / 2 - 20, hauteur / 10 + 5 * (hauteur / 10), 130, 30);
        modifChoixSalle.setBackground(Color.WHITE);
        panneauModifSeance.add(modifChoixSalle);

        //Enseignants
        DefaultListModel modifChoixEnseignantModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM utilisateur JOIN enseignant ON utilisateur.id = enseignant.id_utilisateur "
                + "WHERE enseignant.ID_COURS = " + ((Cours) modifChoixCours.getSelectedItem()).getId() + " "
                + "AND id not in (SELECT id_enseignant FROM seance_enseignants "
                + "WHERE id_seance = " + seanceSelection.getId() + ")");
        while (resultatFenetre.next()) {
            modifChoixEnseignantModel.addElement(utilisateurDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixEnseignant = new JList(modifChoixEnseignantModel);
        modifLabelChoixProf.setBounds(largeur / 2 - 40, hauteur / 10 + 6 * (hauteur / 10) - 40, 80, 30);
        modifChoixProfSocket.setBounds(largeur / 2 - 132, hauteur / 10 + 6 * (hauteur / 10), 130, 30);
        modifChoixProfSocket.setViewportView(modifChoixEnseignant);
        panneauModifSeance.add(modifLabelChoixProf);
        panneauModifSeance.add(modifChoixProfSocket);

        //Enseignants sélectionnés
        DefaultListModel modifChoixEnseignantSelectionModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_enseignant FROM seance_enseignants "+ "WHERE id_seance = " + seanceSelection.getId());
        while (resultatFenetre.next()) {
            modifChoixEnseignantSelectionModel.addElement(utilisateurDAO.find(resultatFenetre.getInt("ID_ENSEIGNANT")));
        }
        modifChoixEnseignantSelection = new JList(modifChoixEnseignantSelectionModel);
        modifAddProfSocket.setBounds(largeur / 2 + 22, hauteur / 10 + 6 * (hauteur / 10), 130, 30);
        modifAddProfSocket.setViewportView(modifChoixEnseignantSelection);
        panneauModifSeance.add(modifAddProfSocket);

        //Boutons de gestion des enseignants
        modifBoutonAjouterEnseignant.setBounds(largeur / 2 + 156, hauteur / 10 + 6 * (hauteur / 10) - 10, 300, 20);
        modifBoutonAjouterEnseignant.setBackground(vertEce);
        modifBoutonAjouterEnseignant.setForeground(Color.WHITE);
        panneauModifSeance.add(modifBoutonAjouterEnseignant);

        modifBoutonSupprimerEnseignantSelection.setBounds(largeur / 2 + 156, hauteur / 10 + 6 * (hauteur / 10) + 10, 300, 20);
        modifBoutonSupprimerEnseignantSelection.setBackground(vertEce);
        modifBoutonSupprimerEnseignantSelection.setForeground(Color.WHITE);
        panneauModifSeance.add(modifBoutonSupprimerEnseignantSelection);

        //Promotion du groupe
        modifChoixPromotion.removeAllItems();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM promotion");
        while (resultatFenetre.next()) {
            modifChoixPromotion.addItem(promotionDAO.find(resultatFenetre.getInt("ID")));
        }
        resultatFenetre = statementFenetre.executeQuery("SELECT idpromotion FROM groupe JOIN seance_groupes ON id = id_groupe where id_seance = " + seanceSelection.getId());
        if (resultatFenetre.first()) {
            for (int i = 0; i < modifChoixPromotion.getItemCount(); ++i) {
                if (((Promotion) modifChoixPromotion.getItemAt(i)).getId() == resultatFenetre.getInt("IDPROMOTION")) {
                    modifChoixPromotion.setSelectedIndex(i);
                }
            }
        }
        modifChoixPromotion.setBounds(largeur / 2 - 104, hauteur / 10 + 7 * (hauteur / 10), 60, 25);
        modifLabelChoixPromo.setBounds(largeur / 2 - 65, hauteur / 10 + 7 * (hauteur / 10) - 30, 150, 30);
        modifChoixPromotion.setBackground(Color.WHITE);
        panneauModifSeance.add(modifLabelChoixPromo);
        panneauModifSeance.add(modifChoixPromotion);

        //Groupes
        DefaultListModel modifChoixGroupeModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id FROM groupe WHERE idpromotion = " + ((Promotion) modifChoixPromotion.getSelectedItem()).getId() + " "
                + "AND id not in (SELECT id_groupe FROM seance_groupes "
                + "WHERE id_seance = " + seanceSelection.getId() + ")");
        while (resultatFenetre.next()) {
            modifChoixGroupeModel.addElement(groupeDAO.find(resultatFenetre.getInt("ID")));
        }
        modifChoixGroupe = new JList(modifChoixGroupeModel);
        modifChoixGroupeSocket.setBounds(largeur / 2 - 14, hauteur / 10 + 7 * (hauteur / 10), 80, 30);
        modifChoixGroupeSocket.setViewportView(modifChoixGroupe);
        panneauModifSeance.add(modifChoixGroupeSocket);

        //Groupes sélectionnés
        DefaultListModel modifChoixGroupeSelectionModel = new DefaultListModel();
        resultatFenetre = statementFenetre.executeQuery("SELECT id_groupe FROM seance_groupes "
                + "WHERE id_seance = " + seanceSelection.getId());
        while (resultatFenetre.next()) {
            modifChoixGroupeSelectionModel.addElement(groupeDAO.find(resultatFenetre.getInt("ID_GROUPE")));
        }
        modifChoixGroupeSelection = new JList(modifChoixGroupeSelectionModel);
        modifAddGroupeSocket.setBounds(largeur / 2 + 73, hauteur / 10 + 7 * (hauteur / 10), 80, 30);
        modifAddGroupeSocket.setViewportView(modifChoixGroupeSelection);
        panneauModifSeance.add(modifAddGroupeSocket);

        //Boutons de gestion des groupes
        modifBoutonAjouterGroupe.setBounds(largeur / 2 + 156, hauteur / 10 + 7 * (hauteur / 10) - 10, 300, 20);
        modifBoutonAjouterGroupe.setBackground(vertEce);
        modifBoutonAjouterGroupe.setForeground(Color.WHITE);
        panneauModifSeance.add(modifBoutonAjouterGroupe);

        modifBoutonSupprimerGroupeSelection.setBounds(largeur / 2 + 156, hauteur / 10 + 7 * (hauteur / 10) + 10, 300, 20);
        modifBoutonSupprimerGroupeSelection.setBackground(vertEce);
        modifBoutonSupprimerGroupeSelection.setForeground(Color.WHITE);
        panneauModifSeance.add(modifBoutonSupprimerGroupeSelection);

        //Bouton d'enregistrement
        modifBoutonEnregistrer.setBounds(largeur / 2 - 50, hauteur / 10 + 8 * (hauteur / 10), 100, 50);
        modifBoutonEnregistrer.setBackground(vertEce);
        modifBoutonEnregistrer.setForeground(Color.WHITE);
        panneauModifSeance.add(modifBoutonEnregistrer);

        //Affichage d'erreur (qui pourra être modifié dans l'ActionListener du bouton
        modifErrorField.setBounds(largeur / 2 + 52, hauteur / 10 + 8 * (hauteur / 10), 400, 50);
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

    private void modifFillDaysOfMonth() {
        modifChoixJour.removeAllItems();
        int limiteJours = 0;
        switch ((int) modifChoixMois.getSelectedItem()) {
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
                if ((((int) modifChoixAnnee.getSelectedItem() % 4 == 0) && !((int) modifChoixAnnee.getSelectedItem() % 100 == 0))
                        || ((int) modifChoixAnnee.getSelectedItem() % 400 == 0)) //voir définition d'une année bissextile
                {
                    limiteJours = 29;
                } else {
                    limiteJours = 28;
                }
                break;
            default:
                System.out.println("Erreur au niveau du mois dans la modification : cela devrait être impossible");
                break;
        }

        for (int i = 1; i <= limiteJours; ++i) {
            modifChoixJour.addItem(i);
        }
    }

    //retourne le chiffre correspondant à la String de l'état passée en paramètre
    private int parseEtat(String etat) {
        switch (etat) {
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

    private void modifEnregistrer() throws SQLException, ParseException {
        String messageErreur = "";

        //Date
        MyDate today = new MyDate();
        MyDate modifDate = new MyDate((int) modifChoixJour.getSelectedItem(),(int) modifChoixMois.getSelectedItem(),(int) modifChoixAnnee.getSelectedItem());
        messageErreur += (modifDate.compareTo(today) == -1) ? "Attention : la nouvelle date est déjà passée." : "";

        //Heure de début
        MyHour modifHeureDebut = new MyHour((int) modifChoixHeureDebut.getValue(), (int) modifChoixMinutesDebut.getValue());

        //Heure de fin
        MyHour modifHeureFin = new MyHour((int) modifChoixHeureFin.getValue(), (int) modifChoixMinutesFin.getValue());

        //Vérifier que la fin soit après le début
        if (modifHeureFin.compareTo(modifHeureDebut) == -1) {
            messageErreur = "ERREUR : L'heure de fin doit se situer après l'heure de début";
        } else if ((int) modifChoixHeureDebut.getValue() < 7 || (int) modifChoixHeureDebut.getValue() > 20
                || (int) modifChoixMinutesDebut.getValue() < 0 || (int) modifChoixMinutesDebut.getValue() > 59
                || (int) modifChoixHeureFin.getValue() < 7 || (int) modifChoixHeureFin.getValue() > 20
                || (int) modifChoixMinutesFin.getValue() < 0 || (int) modifChoixMinutesFin.getValue() > 59) {
            messageErreur = "ERREUR : Les heures doivent être comprises entre 7h et 20h. Les minutes doivent être comprises entre 0mn et 59mn";
        } else if (modifDate.getJourDeLaSemaine() == 6 || modifDate.getJourDeLaSemaine() == 7) {
            messageErreur = "ERREUR : Vous ne pouvez pas ajouter de cours les Samedis et Dimanches";
        } else {
            //Vérifier que les enseignants choisis n'ont pas cours à cette heure là
            int nombreSeancesEnseignant = 0;
            for (int i = 0; i < modifChoixEnseignantSelection.getModel().getSize(); ++i) {
                resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_enseignants on id = id_seance "
                        + "WHERE ((heure_debut >= TIME('" + modifHeureDebut + "') AND heure_debut <= TIME('" + modifHeureDebut + "')) "
                        + "OR (heure_fin >= TIME('" + modifHeureFin + "') AND heure_fin <= TIME('" + modifHeureFin + "'))) "
                        + "AND id_enseignant = " + ((Utilisateur) modifChoixEnseignantSelection.getModel().getElementAt(i)).getId() + " "
                        + "AND id != " + seanceSelection.getId());
                if (resultatEvent.first()) {
                    nombreSeancesEnseignant += resultatEvent.getInt("nombreSeances");
                }
            }
            if (nombreSeancesEnseignant > 0) {
                messageErreur = "ERREUR : un des enseignants a déjà un cours dans cette période";
            } else if (nombreSeancesEnseignant < 0) {
                System.out.println("Erreur dans l'enregistrement des données : nombreSeancesEnseignant vaut <0 après la requête");
            } else if (nombreSeancesEnseignant == 0) {
                //Vérifier que les groupes choisis n'a pas de cours à cette heure là
                int nombreSeancesGroupe = 0;
                for (int i = 0; i < modifChoixGroupeSelection.getModel().getSize(); ++i) {
                    resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_groupes on id = id_seance "
                            + "WHERE ((heure_debut >= TIME('" + modifHeureDebut + "') AND heure_debut <= TIME('" + modifHeureDebut + "')) "
                            + "OR (heure_fin >= TIME('" + modifHeureFin + "') AND heure_fin <= TIME('" + modifHeureFin + "'))) "
                            + "AND id_groupe = " + ((Groupe) modifChoixGroupeSelection.getModel().getElementAt(i)).getId() + " "
                            + "AND id != " + seanceSelection.getId());
                    if (resultatEvent.first()) {
                        nombreSeancesGroupe += resultatEvent.getInt("nombreSeances");
                    }
                }
                if (nombreSeancesGroupe > 0) {
                    messageErreur = "ERREUR : Un des groupes a déjà un cours dans cette période";
                } else if (nombreSeancesGroupe < 0) {
                    System.out.println("Erreur dans l'enregistrement des données : nombreSeancesGroupe vaut <0 après la requête");
                } else if (nombreSeancesGroupe == 0) {
                    //Vérifier que la salle choisie n'a pas de cours à cette heure là
                    int nombreSeancesSalle = -1;
                    //On cherche le nombre de salles qui ont le même id que la salle choisie et qui sont libres entre les heures choisies
                    //On s'assure de retirer l'id de la salle selectionnée du compte
                    resultatEvent = statementEvent.executeQuery("SELECT COUNT(id) as nombreSeances FROM seance JOIN seance_salles on id = id_seance "
                            + "WHERE ((heure_debut >= TIME('" + modifHeureDebut + "') AND heure_debut <= TIME('" + modifHeureDebut + "')) "
                            + "OR (heure_fin >= TIME('" + modifHeureFin + "') AND heure_fin <= TIME('" + modifHeureFin + "'))) "
                            + "AND id_salle = " + ((Salle) modifChoixSalle.getSelectedItem()).getId() + " "
                            + "AND id != " + seanceSelection.getId());
                    if (resultatEvent.first()) {
                        nombreSeancesSalle = resultatEvent.getInt("nombreSeances");
                    }
                    if (nombreSeancesSalle > 0) {
                        messageErreur = "ERREUR : la salle a déjà un cours dans cette période";
                    } else if (nombreSeancesSalle == -1) {
                        System.out.println("Erreur dans l'enregistrement des données : nombreSeancesSalle vaut -1 après la requête");
                    } else if (nombreSeancesSalle == 0) {
                        //Vérifier que la capacité de la salle est suffisante pour le nombre d'élèves du groupe
                        int totalEtudiants = 0;
                        for (int i = 0; i < modifChoixGroupeSelection.getModel().getSize(); ++i) {
                            //Pour chaque groupe, on ajoute le nombre d'élèves appartenant au groupe au totalEtudiants
                            resultatEvent = statementEvent.executeQuery("SELECT COUNT(*) as nombreEtudiants FROM etudiant "
                                    + "WHERE id_groupe = " + ((Groupe) modifChoixGroupeSelection.getModel().getElementAt(i)).getId());
                            if (resultatEvent.first()) {
                                totalEtudiants += resultatEvent.getInt("nombreEtudiants");
                            }
                        }
                        //On cherche s'il existe une salle avec l'id de la salle choisie, tout en s'assurant que sa capacité est suffisante pour le total des élèves
                        boolean valide = false;
                        resultatEvent = statementEvent.executeQuery("SELECT * FROM salle "
                                + "WHERE id = " + ((Salle) modifChoixSalle.getSelectedItem()).getId() + " "
                                + "AND capacite >= " + totalEtudiants);
                        if (resultatEvent.first()) {
                            valide = true;   //Pour pouvoir réutiliser le résultat par la suite
                        }
                        if (valide) {
                            //TOUT EST OK POUR METTRE À JOUR
                            seanceSelection.setSemaine(modifDate.getSemaineDeAnnee());
                            seanceSelection.setDate(modifDate);
                            seanceSelection.setHeureDebut(modifHeureDebut);
                            seanceSelection.setHeureFin(modifHeureFin);
                            seanceSelection.setEtat(parseEtat((String) modifChoixEtat.getSelectedItem()));
                            seanceSelection.setCours((Cours) modifChoixCours.getSelectedItem());
                            seanceSelection.setTypeCours((TypeCours) modifChoixTypeCours.getSelectedItem());
                            if (seanceSelection.getId() == 0) {
                                //Cas d'une création de salle
                                if (seanceDAO.create(seanceSelection)) {  //Mise à jour des infos de la séance
                                    //On récupère l'id de la dernière séance crée, siot cele que l'on vient d'ajouter
                                    resultatEvent = statementEvent.executeQuery("SELECT MAX(id) as idCree from seance");
                                    if (resultatEvent.first()) {
                                        seanceSelection.setId(resultatEvent.getInt("idCree"));
                                    }
                                    gererTablesSeances();
                                } else {
                                    messageErreur = "ERREUR : Erreur lors de la création de la séance";
                                }
                            } else {
                                //Cas d'une modification de salle
                                if (seanceDAO.update(seanceSelection)) {  //Mise à jour des infos de la séance
                                    gererTablesSeances();
                                } else {
                                    messageErreur = "ERREUR : Erreur lors de la mise à jour de la séance";
                                }
                            }
                        } else {
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
        global.add(panneauModifSeance, "ModifSeance");
        cardLayout.show(global, "ModifSeance");

        //On définit le message d'erreur
        modifErrorField.setText(messageErreur);
    }

    //Utilisé pour la modification d'une séance ; update les liens de la séance avec groupes, enseignants et salles
    private void gererTablesSeances() throws SQLException {
        //déletion de tous les séances_enseignants en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_enseignants WHERE id_seance = " + seanceSelection.getId());
        //ajout des séances_enseignants à ajouter
        for (int i = 0; i < modifChoixEnseignantSelection.getModel().getSize(); ++i) {
            //create lance des SQLExceptions, donc les erreurs sont gérées
            seanceEnseignantsDAO.create(new SeanceEnseignants(seanceSelection, (Utilisateur) modifChoixEnseignantSelection.getModel().getElementAt(i)));
        }

        //déletion de tous les séances_groupes en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_groupes WHERE id_seance = " + seanceSelection.getId());
        //ajout des séances_groupes à ajouter
        for (int i = 0; i < modifChoixGroupeSelection.getModel().getSize(); ++i) {
            //create lance des SQLExceptions, donc les erreurs sont gérées
            seanceGroupesDAO.create(new SeanceGroupes(seanceSelection, (Groupe) modifChoixGroupeSelection.getModel().getElementAt(i)));
        }

        //déletion de tous les séances_salles en rapport avec la séance
        statementEvent.executeUpdate("DELETE FROM seance_salles WHERE id_seance = " + seanceSelection.getId());
        //ajout des séances_salles à ajouter
        //create lance des SQLExceptions, donc les erreurs sont gérées
        seanceSallesDAO.create(new SeanceSalles(seanceSelection, (Salle) modifChoixSalle.getSelectedItem()));
    }

    //TODO
    private void remplirRecapCours() throws SQLException {
        panneauRecapCours.removeAll();
        panneauRecapCours.setLayout(null);
        addMenuBars(panneauRecapCours);

        //Initialisation des composants du panneau
        recapListeCoursContainer = new JScrollPane();
        recapListeCours = new JPanel();
        recapTitre = new JLabel("",SwingConstants.CENTER);
        recapTable = new JTable();
        recapDonneesTable = new ArrayList<>();
        recapTotalHeures = new JLabel("Total : ",SwingConstants.CENTER);
        
        //ELEMENTS DE LA PAGE
        
        //Titre
        Calendar tempCalendar = Calendar.getInstance();
        String tempTitre = "Récapitulatif des cours entre le lundi ";
        if(tempCalendar.get(Calendar.WEEK_OF_YEAR) < 31){   //si on est dans la deuxième partie de l'année
            tempCalendar.add(Calendar.YEAR, -1);    //on se place à la première semaine  de cours de l'année précédente
            tempCalendar.set(Calendar.WEEK_OF_YEAR, 31);
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            tempTitre+= tempCalendar.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempCalendar.get(Calendar.MONTH))+" "+tempCalendar.get(Calendar.YEAR)+" et le samedi ";
            
            tempCalendar.add(Calendar.YEAR, 1);    //on se place à la dernière semaine de cours de l'année actuelle
            tempCalendar.set(Calendar.WEEK_OF_YEAR, 30);
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            tempTitre+= tempCalendar.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempCalendar.get(Calendar.MONTH))+" "+tempCalendar.get(Calendar.YEAR);
        }else{  //on est dans le première partie de l'année   
            tempCalendar.set(Calendar.WEEK_OF_YEAR, 31);//on se place à la première semaine  de cours de l'année actuelle
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            tempTitre+= tempCalendar.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempCalendar.get(Calendar.MONTH))+" "+tempCalendar.get(Calendar.YEAR)+" et le samedi ";
            
            tempCalendar.add(Calendar.YEAR, 1);    //on se place à la dernière semaine de cours de l'année suivante
            tempCalendar.set(Calendar.WEEK_OF_YEAR, 30);
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            tempTitre+= tempCalendar.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempCalendar.get(Calendar.MONTH))+" "+tempCalendar.get(Calendar.YEAR);
        }
        
        
        recapTitre.setBounds(largeur/2-700, 150, 1400, 40);
        recapTitre.setFont(new Font("Sans Serif", Font.BOLD, 20));
        recapTitre.setText(tempTitre);
        panneauRecapCours.add(recapTitre);
        
        //Récapitulatifs des cours
        if(connectedUser.getDroit() == 3){  //connecté en tant que prof
            resultatFenetre = statementFenetre.executeQuery("SELECT COUNT(DISTINCT id_cours) as nbCours FROM seance JOIN seance_enseignants ON id = id_seance "
                                                          + "WHERE id_enseignant = "+connectedUser.getId());
        }else{  //connecté en tant qu'étudiant
            resultatFenetre = statementFenetre.executeQuery("SELECT COUNT(DISTINCT id_cours) as nbCours FROM seance JOIN seance_groupes ON id = id_seance "
                                                          + "WHERE id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+")");
        }
        if(resultatFenetre.first()){
            if((resultatFenetre.getInt("nbCours")+2)*30 < 700){
                recapListeCoursContainer.setBounds(largeur/2 - 500, 250, 1000, (resultatFenetre.getInt("nbCours"))*30 + 23);
            }else{
                recapListeCoursContainer.setBounds(largeur/2 - 500, 250, 1000, 700);
            }
        }
        recapListeCours.setBackground(Color.WHITE);
        
        String colonnesTable[] = {"Matières","Première séance","Dernière séance", "Durée", "N."};
        DefaultTableModel tableModel = new DefaultTableModel(colonnesTable, 0);
        
        LigneRecap tempLigne;
        String tempMatiere,tempPremiereSeance,tempDerniereSeance,tempDuree,tempNombre;
        
        if(connectedUser.getDroit() == 3){  //connecté en tant que prof
            resultatFenetre = statementFenetre.executeQuery("SELECT DISTINCT id_cours FROM seance JOIN seance_enseignants ON id = id_seance "
                                                          + "WHERE id_enseignant = "+connectedUser.getId());
        }else{  //connecté en tant qu'étudiant
            resultatFenetre = statementFenetre.executeQuery("SELECT DISTINCT id_cours FROM seance JOIN seance_groupes ON id = id_seance "
                                                          + "WHERE id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+")");
        }
        
        while(resultatFenetre.next()){
            //Matière
            tempMatiere = coursDAO.find(resultatFenetre.getInt("ID_COURS")).getNom();
            
            
            //Première séance
            tempPremiereSeance = "";
            
            if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
                resultatEvent = statementEvent.executeQuery("SELECT * FROM seance JOIN seance_enseignants ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_enseignant = " + connectedUser.getId()+" ORDER BY date ASC, heure_debut ASC limit 1");
            } else {  //connecté en tant qu'étudiant
                resultatEvent = statementEvent.executeQuery("SELECT * FROM seance JOIN seance_groupes ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+") ORDER BY date ASC, heure_debut ASC limit 1");
            }
            if(resultatEvent.first()){
                Calendar tempDate = Calendar.getInstance();
                tempDate.setTime(resultatEvent.getDate("DATE"));
                tempPremiereSeance += parseDayOfTheWeek(tempDate.get(Calendar.DAY_OF_WEEK))+" "+tempDate.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempDate.get(Calendar.MONTH))
                        +" "+tempDate.get(Calendar.YEAR);
                
                tempDate.setTime(resultatEvent.getTime("HEURE_DEBUT"));
                tempPremiereSeance += " de "+tempDate.get(Calendar.HOUR_OF_DAY)+"h"+tempDate.get(Calendar.MINUTE);
                
                tempDate.setTime(resultatEvent.getTime("HEURE_FIN"));
                tempPremiereSeance += " à "+tempDate.get(Calendar.HOUR_OF_DAY)+"h"+((tempDate.get(Calendar.MINUTE) < 10)?"0":"")+tempDate.get(Calendar.MINUTE);
            }
            
            tempDerniereSeance = "";
            if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
                resultatEvent = statementEvent.executeQuery("SELECT * FROM seance JOIN seance_enseignants ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_enseignant = " + connectedUser.getId()+" ORDER BY date DESC, heure_debut DESC limit 1");
            } else {  //connecté en tant qu'étudiant
                resultatEvent = statementEvent.executeQuery("SELECT * FROM seance JOIN seance_groupes ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+") ORDER BY date DESC, heure_debut DESC limit 1");
            }
            if(resultatEvent.first()){
                Calendar tempDate = Calendar.getInstance();
                tempDate.setTime(resultatEvent.getDate("DATE"));
                tempDerniereSeance += parseDayOfTheWeek(tempDate.get(Calendar.DAY_OF_WEEK))+" "+tempDate.get(Calendar.DAY_OF_MONTH)+" "+parseMonth(tempDate.get(Calendar.MONTH))
                        +" "+tempDate.get(Calendar.YEAR);
                
                tempDate.setTime(resultatEvent.getTime("HEURE_DEBUT"));
                tempDerniereSeance += " de "+tempDate.get(Calendar.HOUR_OF_DAY)+"h"+((tempDate.get(Calendar.MINUTE) < 10)?"0":"")+tempDate.get(Calendar.MINUTE);
                
                tempDate.setTime(resultatEvent.getTime("HEURE_FIN"));
                tempDerniereSeance += " à "+tempDate.get(Calendar.HOUR_OF_DAY)+"h"+((tempDate.get(Calendar.MINUTE) < 10)?"0":"")+tempDate.get(Calendar.MINUTE);
            }
            
            int tempDureeHeures = 0;
            int tempDureeMinutes = 0;
            tempDuree = "";
            if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
                resultatEvent = statementEvent.executeQuery("SELECT (TIMEDIFF(heure_fin,heure_debut)) as dureeTot FROM seance JOIN seance_enseignants ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_enseignant = " + connectedUser.getId());
            } else {  //connecté en tant qu'étudiant
                resultatEvent = statementEvent.executeQuery("SELECT (TIMEDIFF(heure_fin,heure_debut)) as dureeTot FROM seance JOIN seance_groupes ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+")");
            }
            Calendar tempCalDuree = Calendar.getInstance();
            while(resultatEvent.next()){
                tempCalDuree.setTime(resultatEvent.getTime("dureeTot"));
                tempDureeHeures += tempCalDuree.get(Calendar.HOUR_OF_DAY);
                tempDureeMinutes += tempCalDuree.get(Calendar.MINUTE);
                tempDureeHeures += tempDureeMinutes/60;
                tempDureeMinutes %= 60;
            }
            tempDuree += tempDureeHeures+"h"+((tempDureeMinutes < 10)?"0":"")+tempDureeMinutes;
            
            tempNombre = "";
            if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
                resultatEvent = statementEvent.executeQuery("SELECT COUNT(*) as nbSeances FROM seance JOIN seance_enseignants ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_enseignant = " + connectedUser.getId());
            } else {  //connecté en tant qu'étudiant
                resultatEvent = statementEvent.executeQuery("SELECT COUNT(*) as nbSeances FROM seance JOIN seance_groupes ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_cours = "+resultatFenetre.getInt("ID_COURS")+" "
                    + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = "+connectedUser.getId()+")");
            }
            if(resultatEvent.first()){
                tempNombre += resultatEvent.getInt("nbSeances");
            }
            
            tempLigne = new LigneRecap(tempMatiere, tempPremiereSeance, tempDerniereSeance, tempDuree, tempNombre);
            recapDonneesTable.add(tempLigne);
        }
        
        //On ajoute les lignes au tableau
        for(LigneRecap Element : recapDonneesTable){
            String[] ligneData = {Element.getMatiere(),Element.getPremiereSeance(),Element.getDerniereSeance(),Element.getDuree(),Element.getNombre()};
            tableModel.addRow(ligneData);
        }
        
        recapTable = new JTable(tableModel);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        for (int columnIndex = 0; columnIndex < tableModel.getColumnCount(); columnIndex++)
        {
            recapTable.getColumnModel().getColumn(columnIndex).setCellRenderer( centerRenderer );
        }
        
        recapTable.setRowHeight(30);
        recapTable.getColumnModel().getColumn(0).setPreferredWidth(180);
        recapTable.getColumnModel().getColumn(1).setPreferredWidth(320);
        recapTable.getColumnModel().getColumn(2).setPreferredWidth(320);
        recapTable.getColumnModel().getColumn(3).setPreferredWidth(40);
        recapTable.getColumnModel().getColumn(4).setPreferredWidth(15);
        recapListeCoursContainer.setViewportView(recapTable);
        panneauRecapCours.add(recapListeCoursContainer);
        
        //Calcul du nombre total d'heures;
        int tempDureeHeures = 0;
        int tempDureeMinutes = 0;
        if (connectedUser.getDroit() == 3) {  //connecté en tant que prof
            resultatEvent = statementEvent.executeQuery("SELECT (TIMEDIFF(heure_fin,heure_debut)) as dureeTot FROM seance JOIN seance_enseignants ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_enseignant = " + connectedUser.getId());
        } else {  //connecté en tant qu'étudiant
            resultatEvent = statementEvent.executeQuery("SELECT (TIMEDIFF(heure_fin,heure_debut)) as dureeTot FROM seance JOIN seance_groupes ON id = id_seance "
                    + "WHERE ((YEAR(date) = YEAR(DATE(NOW()))-1 AND semaine > 30) OR (YEAR(date) <= YEAR(DATE(NOW())) AND semaine < 31)) "
                    + "AND id_groupe = (SELECT id_groupe FROM etudiant WHERE id_utilisateur = " + connectedUser.getId() + ")");
        }
        Calendar tempCalDuree = Calendar.getInstance();
        while (resultatEvent.next()) {
            tempCalDuree.setTime(resultatEvent.getTime("dureeTot"));
            tempDureeHeures += tempCalDuree.get(Calendar.HOUR_OF_DAY);
            tempDureeMinutes += tempCalDuree.get(Calendar.MINUTE);
            tempDureeHeures += tempDureeMinutes / 60;
            tempDureeMinutes %= 60;
        }
        recapTotalHeures.setText("Total : " + tempDureeHeures + "h" + ((tempDureeMinutes < 10) ? "0" : "") + tempDureeMinutes);
        recapTotalHeures.setBounds(1470, 235, 200, 50);
        recapTotalHeures.setFont(new Font("Sans Serif", Font.BOLD, 18));
        panneauRecapCours.add(recapTotalHeures);
    }
    
    //Retourne le nom français du jour de la semaine en fonction d'une valeur DAY_OF_THE_WEEK d'un objet calendar
    private String parseDayOfTheWeek(int dayNumber){
        switch(dayNumber){
            case 1:
                return "dimanche";
            case 2:
                return "lundi";
            case 3:
                return "mardi";
            case 4:
                return "mercredi";
            case 5:
                return "jeudi";
            case 6:
                return "vendredi";
            case 7:
                return "samedi";
            default:
                return "";
        }
    }
    
    //Retourne le nom français du mois de l'année en fonction d'une valeur MONTH d'un objet calendar
    private String parseMonth(int monthNumber){
        switch(monthNumber){
            case 0:
                return "janvier";
            case 1:
                return "février";
            case 2:
                return "mars";
            case 3:
                return "avril";
            case 4:
                return "mai";
            case 5:
                return "juin";
            case 6:
                return "juillet";
            case 7:
                return "août";
            case 8:
                return "septembre";
            case 9:
                return "octobre";
            case 10:
                return "novembre";
            case 11:
                return "décembre";
            default:
                return "";
        }
    }

    class SemaineEDT implements ActionListener {

        private final int numSemaine;
        private final String typeEDT;
        private final String formeEDT;

        public SemaineEDT(int numSemaine, String typeEDT, String formeEDT) {
            this.numSemaine = numSemaine;
            this.typeEDT = typeEDT;
            this.formeEDT = formeEDT;
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            selectedWeek = numSemaine;
            try {
                switch(formeEDT){
                    case "grille":
                        global.remove(panneauEDTGrille);
                        panneauEDTGrille = new JPanel();
                        remplirEDTGrille(typeEDT);
                        global.add(panneauEDTGrille, "EDTGrille");
                        cardLayout.show(global, "EDTGrille");
                        break;
                    case "liste":
                        global.remove(panneauEDTListe);
                        panneauEDTListe = new JPanel();
                        remplirEDTListe(typeEDT);
                        global.add(panneauEDTListe, "EDTListe");
                        cardLayout.show(global, "EDTListe");
                        break;
                    default:
                        System.out.println("Erreur lors du renseignement de la forme d'EDT pour une semaine");
                        break;
                }
                
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    class CoursEDT implements ActionListener {

        private final Seance seance;
        private final int numeroBoutonCours;    //pour centrer la fenêtre de dialogue sur le bouton cliqué
        private final String typeEDT;
        private final String formeEDT;

        public CoursEDT(Seance seance, int numeroBoutonCours, String typeEDT, String formeEDT) {
            this.seance = seance;
            this.numeroBoutonCours = numeroBoutonCours;
            this.typeEDT = typeEDT;
            this.formeEDT = formeEDT;
        }

        @Override
        public void actionPerformed(ActionEvent ev) {
            if (ev != null) {
                dialogueCoursEDT(seance, numeroBoutonCours, typeEDT, formeEDT);
            }
        }
    }
    
    class CoursEDTListe extends MouseAdapter{
        
        private final Seance seance;
        private final int numeroPanneauCours;    //pour centrer la fenêtre de dialogue sur le bouton cliqué
        private final String typeEDT;
        private final String formeEDT;
        
        public CoursEDTListe(Seance seance, int numeroPanneauCours, String typeEDT, String formeEDT){
            this.seance = seance;
            this.numeroPanneauCours = numeroPanneauCours;
            this.typeEDT = typeEDT;
            this.formeEDT = formeEDT;
        }
        
        @Override
        public void mouseClicked(MouseEvent ev){
            if (ev != null) {
                dialogueCoursEDT(seance, numeroPanneauCours, typeEDT, formeEDT);
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent ev) {
            EDTListeListeCours.get(numeroPanneauCours).setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        public void mouseExited(MouseEvent ev) {
            EDTListeListeCours.get(numeroPanneauCours).setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    private void dialogueCoursEDT(Seance seance, int numeroBoutonCours, String typeEDT, String formeEDT) {
        JDialog fenetreDialogue = new JDialog(this);
        fenetreDialogue.setLayout(null);
        int largeurDialogue = 500;
        int hauteurDialogue = 365;
        fenetreDialogue.setSize(largeurDialogue, hauteurDialogue);
        fenetreDialogue.setTitle("Détails du cours");

        String tempContenuLabel;

        //COMPOSANTS DE LA FENÊTRE DE DIALOGUE
        try {
            //Matière
            JLabel dialogueMatiere = new JLabel("<html><b>Matière :</b> " + seance.getCours().getNom() + "<html>");
            dialogueMatiere.setBounds(10, 10, largeurDialogue - 10, 25);
            dialogueMatiere.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueMatiere);

            //Enseignants
            resultatEvent = statementEvent.executeQuery("SELECT id_enseignant FROM seance_enseignants WHERE id_seance =" + seance.getId());
            tempContenuLabel = "<html><b>Enseignant(s) :</b> ";
            while (resultatEvent.next()) {
                tempContenuLabel += utilisateurDAO.find(resultatEvent.getInt("ID_ENSEIGNANT")).toString();
                if (!resultatEvent.isLast()) {
                    tempContenuLabel += ", ";
                }
            }
            JLabel dialogueEnseignants = new JLabel(tempContenuLabel + "<html>");
            dialogueEnseignants.setBounds(10, 45, largeurDialogue - 10, 25);
            dialogueEnseignants.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueEnseignants);

            //Public
            resultatEvent = statementEvent.executeQuery("SELECT id_groupe FROM seance_groupes WHERE id_seance =" + seance.getId());
            tempContenuLabel = "<html><b>Public :</b> ";
            while (resultatEvent.next()) {
                tempContenuLabel += groupeDAO.find(resultatEvent.getInt("ID_GROUPE")).getNom();
                if (!resultatEvent.isLast()) {
                    tempContenuLabel += ", ";
                }
            }
            JLabel dialoguePublic = new JLabel(tempContenuLabel + "<html>");
            dialoguePublic.setBounds(10, 80, largeurDialogue - 10, 25);
            dialoguePublic.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialoguePublic);

            //Date et heures
            JLabel dialogueTime = new JLabel("<html><b>Le :</b> " + seance.getDate().getJour() + "/" + seance.getDate().getMois() + "/" + seance.getDate().getAnnee() + " "
                    + "de " + seance.getHeureDebut().toString() + " à " + seance.getHeureFin().toString() + "<html>");
            dialogueTime.setBounds(10, 115, largeurDialogue - 10, 25);
            dialogueTime.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueTime);

            //Lieu
            resultatEvent = statementEvent.executeQuery("SELECT id_salle FROM seance_salles WHERE id_seance =" + seance.getId());
            tempContenuLabel = "<html><b>Lieu :</b> ";
            while (resultatEvent.next()) {
                tempContenuLabel += salleDAO.find(resultatEvent.getInt("ID_Salle")).getNom() + " (" + salleDAO.find(resultatEvent.getInt("ID_Salle")).getSite().getNom() + ")";
            }
            JLabel dialogueLieu = new JLabel(tempContenuLabel + "<html>");
            dialogueLieu.setBounds(10, 150, largeurDialogue - 10, 25);
            dialogueLieu.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueLieu);

            //Type de cours
            JLabel dialogueTypeCours = new JLabel("<html><b>Type de cours :</b> " + seance.getTypeCours().toString() + "<html>");
            dialogueTypeCours.setBounds(10, 185, largeurDialogue - 10, 25);
            dialogueTypeCours.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueTypeCours);

            //État de validation
            switch (seance.getEtat()) {
                case 1:
                    tempContenuLabel = "<html><b>État de validation :</b> En cours de validation";
                    break;
                case 2:
                    tempContenuLabel = "<html><b>État de validation :</b> Validé";
                    break;
                case 3:
                    tempContenuLabel = "<html><b>État de validation :</b> Annulé";
                    break;
                default:
                    tempContenuLabel = "<html><b>État de validation :</b> ERREUR LORS DU SWITCH DE L'ETAT DE LA SEANCE";
                    break;
            }
            JLabel dialogueEtat = new JLabel(tempContenuLabel + "<html>");
            dialogueEtat.setBounds(10, 220, largeurDialogue - 10, 25);
            dialogueEtat.setFont(new Font("Sans Serif", Font.PLAIN, 16));
            fenetreDialogue.add(dialogueEtat);

            if (connectedUser.getDroit() == 1) {
                //Bouton modifier
                JButton dialogueBoutonModifier = new JButton("<html><b>Modifier la séance</b><html>");
                dialogueBoutonModifier.setBounds(5, 275, (largeurDialogue - 35) / 2, 40);
                dialogueBoutonModifier.setFont(new Font("Sans Serif", Font.PLAIN, 16));
                dialogueBoutonModifier.setBackground(vertEce);
                dialogueBoutonModifier.setForeground(Color.WHITE);
                fenetreDialogue.add(dialogueBoutonModifier);
                dialogueBoutonModifier.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        try {
                            seanceSelection = seance;
                            remplirModifSeance();
                            cardLayout.show(global, "ModifSeance");
                            fenetreDialogue.dispose();
                        } catch (SQLException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                });

                //Bouton supprimer
                JButton dialogueBoutonSupprimer = new JButton("<html><b>Supprimer la séance</b><html>");
                dialogueBoutonSupprimer.setBounds(5 + (largeurDialogue - 35) / 2 + 10, 275, (largeurDialogue - 35) / 2, 40);
                dialogueBoutonSupprimer.setFont(new Font("Sans Serif", Font.PLAIN, 16));
                dialogueBoutonSupprimer.setBackground(Color.RED);
                dialogueBoutonSupprimer.setForeground(Color.WHITE);
                fenetreDialogue.add(dialogueBoutonSupprimer);
                dialogueBoutonSupprimer.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        try {
                            seanceDAO.delete(seance);
                            global.remove(panneauEDTGrille);
                            panneauEDTGrille = new JPanel();
                            remplirEDTGrille(typeEDT);
                            global.add(panneauEDTGrille, "EDTGrille");
                            cardLayout.show(global, "EDTGrille");
                            fenetreDialogue.dispose();
                        } catch (SQLException ex) {
                            System.out.println(ex.toString());
                        }
                    }
                });
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }

        switch(formeEDT){
            case "grille":
                fenetreDialogue.setLocationRelativeTo(EDTGrilleListeCours.get(numeroBoutonCours));
                break;
            case "liste":
                fenetreDialogue.setLocationRelativeTo(EDTListeListeCours.get(numeroBoutonCours));
                break;
            default:
                System.out.println("Erreur lors du renseignement de la forme de l'EDT pour un cours");
        }
        
        fenetreDialogue.setVisible(true);
    }
}
