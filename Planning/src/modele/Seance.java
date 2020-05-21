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
    private MyDate date;
    private MyHour heureDebut;
    private MyHour heureFin;
    private int etat = 0;
    private Cours cours;
    private TypeCours typeCours;
    
    public Seance(int id, int semaine, MyDate date, MyHour heureDebut, MyHour heureFin, int etat, Cours cours, TypeCours typeCours){
        this.id = id;
        this.semaine = semaine;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.etat = etat;
        this.cours = cours;
        this.typeCours = typeCours;
    }
    
    public Seance(){
        this.date = new MyDate();
        this.heureDebut = new MyHour();
        this.heureFin = new MyHour();
        this.cours = new Cours();
        this.typeCours = new TypeCours();
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setSemaine(int semaine){
        this.semaine = semaine;
    }
    
    public void setDate(MyDate date){
        this.date = date;
    }
    
    public void setHeureDebut(MyHour heureDebut){
        this.heureDebut = heureDebut;
    }
    
    public void setHeureFin(MyHour heureFin){
        this.heureFin = heureFin;
    }
    
    public void setEtat(int etat){
        this.etat = etat;
    }
    
    public void setCours(Cours cours){
        this.cours = cours;
    }
    
    public void setTypeCours(TypeCours typeCours){
        this.typeCours = typeCours;
    }
    
    public int getId(){
        return this.id;
    }
    
    public int getSemaine(){
        return this.semaine;
    }
    
    public MyDate getDate(){
        return this.date;
    }
    
    public MyHour getHeureDebut(){
        return this.heureDebut;
    }
    
    public MyHour getHeureFin(){
        return this.heureFin;
    }
    
    public int getEtat(){
        return this.etat;
    }
    
    public Cours getCours(){
        return this.cours;
    }
    
    public TypeCours getTypeCours(){
        return this.typeCours;
    }
}
