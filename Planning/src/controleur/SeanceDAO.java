/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import java.text.ParseException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import modele.Cours;
import modele.MyDate;
import modele.MyHour;
import modele.Seance;
import modele.TypeCours;

/**
 *
 * @author simon
 */
public class SeanceDAO extends DAO<Seance>{

    public SeanceDAO(Connexion connexion) {
        super(connexion);
    }

    @Override
    public boolean create(Seance obj) {
        try{
            return connexion.effectuerUpdate("INSERT INTO seance VALUES('"+""
                                                                          +"', '"+obj.getDate().getSemaineDeAnnee()
                                                                          +"', '"+obj.getDate().toString()
                                                                          +"', '"+obj.getHeureDebut().toString()
                                                                          +"', '"+obj.getHeureFin().toString()
                                                                          +"', '"+obj.getEtat()
                                                                          +"', '"+obj.getCours().getId()
                                                                          +"', '"+obj.getTypeCours().getId()
                                                                          +"')");
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean delete(Seance obj) {
        try{
            return connexion.effectuerUpdate("DELETE FROM seance WHERE id = "+obj.getId());
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public boolean update(Seance obj) {
        try{
            return connexion.effectuerUpdate("UPDATE seance SET semaine = '"+obj.getDate().getSemaineDeAnnee()
                                                           +"', date = '"+obj.getDate().toString()
                                                           +"', heure_debut = '"+obj.getHeureDebut().toString()
                                                           +"', heure_fin = '"+obj.getHeureFin().toString()
                                                           +"', etat = '"+obj.getEtat()
                                                           +"', id_cours = '"+obj.getCours().getId()
                                                           +"', id_type = '"+obj.getTypeCours().getId()
                                                           +"' WHERE id = "+obj.getId()
                                                            );
        }
        catch(SQLException e){
            System.out.println(e.toString());
            return false;
        }
    }

    @Override
    public Seance find(int id) {
        Seance seance = new Seance();
        DAO<Cours> coursDAO = new CoursDAO(connexion);
        DAO<TypeCours> typeCoursDAO = new TypeCoursDAO(connexion);
        try{
            Statement stmt2 = connexion.getConnection().createStatement();
            ResultSet resultat = stmt2.executeQuery("SELECT * FROM seance WHERE id = "+id);
            if(resultat.first()){
                Cours cours = coursDAO.find(resultat.getInt("ID_COURS"));
                TypeCours typeCours = typeCoursDAO.find(resultat.getInt("ID_TYPE"));
                
                //préparation à la décomposition de la date
                Calendar date = Calendar.getInstance();
                date.setTime(resultat.getDate("DATE"));
                
                //préparation à la décomposition de la date
                Calendar heureDebut = Calendar.getInstance();
                heureDebut.setTime(resultat.getTime("HEURE_DEBUT"));
                
                //préparation à la décomposition de la date
                Calendar heureFin = Calendar.getInstance();
                heureFin.setTime(resultat.getTime("HEURE_FIN"));
                
                seance = new Seance(
                            id,
                            resultat.getInt("SEMAINE"),
                            new MyDate(date.get(Calendar.DAY_OF_MONTH),date.get(Calendar.MONTH) + 1,date.get(Calendar.YEAR)),
                            new MyHour(heureDebut.get(Calendar.HOUR_OF_DAY), heureDebut.get(Calendar.MINUTE)),
                            new MyHour(heureFin.get(Calendar.HOUR_OF_DAY), heureFin.get(Calendar.MINUTE)),
                            resultat.getInt("ETAT"),
                            cours,
                            typeCours
                    );
            }
        }
        catch(SQLException | ParseException e){
            System.out.println(e.toString());
        }
        
        return seance;
    }

    @Override
    public Seance find(int id1, int id2) {
        throw new UnsupportedOperationException("The finding method requires 1 argument."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
