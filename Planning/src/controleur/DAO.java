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
    
    /**
     *Constructeur
     * @param connexion
     */
    public DAO(Connexion connexion){
        this.connexion = connexion;
    }
    
    /**
     *
     * @param obj l'objet à créer
     * @return true ou false selon le succès
     */
    public abstract boolean create(T obj);
    
    /**
     *
     * @param obj l'objet à supprimer
     * @return true ou false selon le succès
     */
    public abstract boolean delete(T obj);
    
    /**
     *
     * @param obj l'objet à update
     * @return true ou false selon le succès
     */
    public abstract boolean update(T obj);
    
    /**
     *
     * @param id
     * @return l'objet dont l'id a été passé en paramètre
     */
    public abstract T find(int id);
    
    /**
     *
     * @param id1
     * @param id2
     * @return l'objet dont les id ont été passés en paramètres
     */
    public abstract T find(int id1, int id2);
}
