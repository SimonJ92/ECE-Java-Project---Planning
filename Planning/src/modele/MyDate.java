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
public class MyDate {
    private Date date;
    
    public MyDate(int jour,int mois, int annee) throws ParseException{
        String currentDateString = mois+"/"+jour+"/"+annee;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    public MyDate(){
        this.date = new Date();
    }
    
    public int getJour(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
    
    public int getJourDeLaSemaine(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        int jour = cal.get(Calendar.DAY_OF_WEEK) - 1; // 1-Lundi 2-Mardi ... 7-Dimanche
        if(jour == 0) jour=7;
        return jour;
    }
    
    public int getSemaineDeAnnee(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
    
    public int getMois(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1;
    }
    
    public int getAnnee(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.YEAR);
    }
    
    public void setJour(int jour) throws ParseException{
        String currentDateString = getMois()+"/"+jour+"/"+getAnnee();
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    public void setMois(int mois) throws ParseException{
        String currentDateString = mois+"/"+getJour()+"/"+getAnnee();
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
    
    public void setAnnee(int annee) throws ParseException{
        String currentDateString = getMois()+"/"+getJour()+"/"+annee;
        SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
        date = sd.parse(currentDateString);
    }
}
