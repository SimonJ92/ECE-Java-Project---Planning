/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.util.*;

/**
 *
 * @author simon
 */
public class Seance {
    private int id = 0;
    private int semaine =0;
    private Date date;
    private Date heureDebut;
    private Date heureFin;
    private int etat = 0;
    private int idCours = 0;
    private int idType;
    
    public Seance(int id, int semaine, Date date, Date heureDebut, Date heureFin, int etat, int idCours, int idType){
        this.id = id;
        this.semaine = semaine;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.etat = etat;
        this.idCours = idCours;
        this.idType = idType;
    }
    
    public Seance(){
        this.date = new Date();
        this.heureDebut = new Date();
        this.heureFin = new Date();
    }
    
    
}
