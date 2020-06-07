/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
import java.sql.*;
/**
 *
 * @author simon
 */
public class Connexion {
    private final Connection conn;
    private final Statement stmt;
    
    /**
     *Constructeur
     * @param nameDatabase
     * @param loginDatabase
     * @param passwordDatabase
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connexion(String nameDatabase, String loginDatabase, String passwordDatabase) throws SQLException, ClassNotFoundException{
        // chargement driver "com.mysql.jdbc.Driver"
        Class.forName("com.mysql.jdbc.Driver");

        // url de connexion "jdbc:mysql://localhost:3305/usernameECE"
        String urlDatabase = "jdbc:mysql://localhost:3306/" + nameDatabase + "?autoReconnect=true&useSSL=false";
       // String urlDatabase = "jdbc:mysql://localhost:3308/jps?characterEncoding=latin1";

        //création d'une connexion JDBC à la base 
        conn = DriverManager.getConnection(urlDatabase, loginDatabase, passwordDatabase);

        // création d'un ordre SQL (statement)
        stmt = conn.createStatement();
    }
     
    /**
     *
     * @return Connection   l'Objet connexion de la classe
     */
    public Connection getConnection(){
        return conn;
    }
     
    /**
     *effectue la requête passée en paramètre et retourne le résultat
     * @param requete
     * @return
     * @throws SQLException
     */
    public ResultSet effectuerRequete(String requete) throws SQLException{
         // récupération de l'ordre de la requete
        return stmt.executeQuery(requete);
    }
     
    /**
     *effectue l'update passée en paramètre et retourne true en cas de succès, false sinon
     * @param requete
     * @return
     * @throws SQLException
     */
    public boolean effectuerUpdate(String requete) throws SQLException{
        return stmt.executeUpdate(requete) != 0;
    }
}
