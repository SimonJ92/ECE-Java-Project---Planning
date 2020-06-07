/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 *
 * @author simon
 */
public class MyDate implements Comparable<MyDate>{
    private Date date;
    
    /**
     *Constructeur
     * @param jour
     * @param mois
     * @param annee
     * @throws ParseException
     */
    public MyDate(int jour,int mois, int annee) throws ParseException{
        String currentDateString = mois+"/"+jour+"/"+annee;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    /**
     *Constructeur par défaut
     */
    public MyDate(){
        this.date = new Date();
    }
    
    /**
     *
     * @return int le jour du mois
     */
    public int getJour(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    /**
     *1-Lundi 2-Mardi ... 7-Dimanche
     * @return int le jour de la semaine
     */
    public int getJourDeLaSemaine(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int jour = cal.get(Calendar.DAY_OF_WEEK) - 1; // 1-Lundi 2-Mardi ... 7-Dimanche
        if(jour == 0) jour=7;
        return jour;
    }
    
    /**
     *
     * @return int semaine de l'année
     */
    public int getSemaineDeAnnee(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    /**
     *
     * @return int mois
     */
    public int getMois(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    /**
     *
     * @return int année
     */
    public int getAnnee(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    /**
     *
     * @param jour
     * @throws ParseException
     */
    public void setJour(int jour) throws ParseException{
        String currentDateString = getMois()+"/"+jour+"/"+getAnnee();
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    /**
     *
     * @param mois
     * @throws ParseException
     */
    public void setMois(int mois) throws ParseException{
        String currentDateString = mois+"/"+getJour()+"/"+getAnnee();
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    /**
     *
     * @param annee
     * @throws ParseException
     */
    public void setAnnee(int annee) throws ParseException{
        String currentDateString = getMois()+"/"+getJour()+"/"+annee;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    @Override
    public String toString(){
        return getAnnee()+"-"+getMois()+"-"+getJour();
    }

    @Override
    public int compareTo(MyDate t) {
        if((this.getAnnee() == t.getAnnee())
                &&(this.getMois() == t.getMois())
                &&(this.getJour() == t.getJour())){
            return 0;
        }
        if(this.getAnnee() > t.getAnnee()){
            return 1;
        }
        if(this.getMois() > t.getMois()){
            return 1;
        }
        if(this.getJour() > t.getJour()){
            return 1;
        }
        return -1;
    }
}
