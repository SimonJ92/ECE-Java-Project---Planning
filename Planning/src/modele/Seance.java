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
    private int semaine = 0;
    private MyDate date;
    private MyHour heureDebut;
    private MyHour heureFin;
    private int etat = 0;
    private Cours cours;
    private TypeCours typeCours;
    
    /**
     *Constructeur
     * @param id
     * @param semaine
     * @param date
     * @param heureDebut
     * @param heureFin
     * @param etat
     * @param cours
     * @param typeCours
     */
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
    
    /**
     *Constructeur par défaut
     */
    public Seance(){
        this.date = new MyDate();
        this.heureDebut = new MyHour();
        this.heureFin = new MyHour();
        this.cours = new Cours();
        this.typeCours = new TypeCours();
    }
    
    /**
     *
     * @param id
     */
    public void setId(int id){
        this.id = id;
    }
    
    /**
     *
     * @param semaine
     */
    public void setSemaine(int semaine){
        this.semaine = semaine;
    }
    
    /**
     *
     * @param date
     */
    public void setDate(MyDate date){
        this.date = date;
    }
    
    /**
     *
     * @param heureDebut
     */
    public void setHeureDebut(MyHour heureDebut){
        this.heureDebut = heureDebut;
    }
    
    /**
     *
     * @param heureFin
     */
    public void setHeureFin(MyHour heureFin){
        this.heureFin = heureFin;
    }
    
    /**
     *
     * @param etat
     */
    public void setEtat(int etat){
        this.etat = etat;
    }
    
    /**
     *
     * @param cours
     */
    public void setCours(Cours cours){
        this.cours = cours;
    }
    
    /**
     *
     * @param typeCours
     */
    public void setTypeCours(TypeCours typeCours){
        this.typeCours = typeCours;
    }
    
    /**
     *
     * @return int  l'id de la séance
     */
    public int getId(){
        return this.id;
    }
    
    /**
     *
     * @return int la semaine de ma séance
     */
    public int getSemaine(){
        return this.semaine;
    }
    
    /**
     *
     * @return MyDate la date de la séance
     */
    public MyDate getDate(){
        return this.date;
    }
    
    /**
     *
     * @return MyHour l'heure de début de la séance
     */
    public MyHour getHeureDebut(){
        return this.heureDebut;
    }
    
    /**
     *
     * @return MyHour l'heure de fin de la séance
     */
    public MyHour getHeureFin(){
        return this.heureFin;
    }
    
    /**
     *
     * @return int l'état de validation de la séance
     */
    public int getEtat(){
        return this.etat;
    }
    
    /**
     *
     * @return Cours le cours de la séance
     */
    public Cours getCours(){
        return this.cours;
    }
    
    /**
     *
     * @return TypeCours le type de cours de la séance
     */
    public TypeCours getTypeCours(){
        return this.typeCours;
    }
    
    @Override
    public String toString(){
        String temp = getCours().getNom()+" - "+getTypeCours().getNom()+" : le "+getDate().toString()+"(semaine "+getSemaine()+") de "+getHeureDebut().toString()+" à "+getHeureFin()+" - ";
        switch(getEtat()){
            case 0:
                break;
            case 1:
                temp += "En cours de validation";
                break;
            case 2:
                temp += "Validé";
                break;
            case 3:
                temp += "Annulé";
                break;
        }
        return temp;
    }
}
