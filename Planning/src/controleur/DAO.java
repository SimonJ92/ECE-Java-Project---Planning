/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;
/**
 *
 * @author simon
 * @param <T>
 */
public abstract class DAO<T> {
    protected Connexion connexion = null;
    
    public DAO(Connexion connexion){
        this.connexion = connexion;
    }
    
    public abstract boolean create(T obj);
    
    public abstract boolean delet(T obj);
    
    public abstract boolean update(T obj);
    
    public abstract T find(int id);
}
