/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
import modele.Cours;
/**
 *
 * @author simon
 */
public class CoursDAO extends DAO<Cours>{
    public CoursDAO(Connexion connexion){
        super(connexion);
    }
    
    @Override
    public boolean create(Cours obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean delet(Cours obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update(Cours obj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Cours find(int id) {
        Cours cours = new Cours();
        
        try{
            ResultSet resultat = connexion.effectuerRequete("SELECT * FROM cours WHERE id = "+id);
            if(resultat.first()){
                cours = new Cours(
                        id,
                        resultat.getString("NOM")
                );
            }
        }
        catch(SQLException e){
            System.out.println(e.toString());
        }
        
        return cours;
    }
    
}
