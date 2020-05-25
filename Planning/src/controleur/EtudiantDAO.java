/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Etudiant;
import modele.Groupe;
import modele.Utilisateur;

/**
 *
 * @author simon
 */
public class EtudiantDAO extends DAO<Etudiant>{

    public EtudiantDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Etudiant obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO etudiant VALUES('', '"+obj.getNumero()+"', '"+obj.getGroupe().getId()+"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Etudiant obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM etudiant WHERE id_utilisateur = "+obj.getIdUtilisateur());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Etudiant obj) {
        try{
            return connexion.effectuerUpdate("UPDATE etudiant SET numero = '"+obj.getNumero()+"', id_groupe = "+obj.getGroupe().getId()+" where id_utilisateur = "+obj.getIdUtilisateur());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Etudiant find(int id) {
        Etudiant etudiant = new Etudiant();
        DAO<Groupe> groupeDAO = new GroupeDAO(connexion);
        DAO<Utilisateur> utilisateurDAO = new UtilisateurDAO(connexion);
        try{
            Statement stmt2 = connexion.getConnection().createStatement();
            ResultSet resultat = stmt2.executeQuery("SELECT * FROM etudiant WHERE id_utilisateur = "+id);
            if(resultat.first()){
                Groupe groupe = groupeDAO.find(resultat.getInt("ID_GROUPE"));
                Utilisateur utilisateur = utilisateurDAO.find(id);
                etudiant = new Etudiant(
                            id,
                            resultat.getInt("NUMERO"),
                            groupe,
                            utilisateur.getEmail(),
                            utilisateur.getPasswd(),
                            utilisateur.getNom(),
                            utilisateur.getPrenom(),
                            utilisateur.getDroit()
                    );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return etudiant;
    }

    @Override
    public Etudiant find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
