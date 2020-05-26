/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author simon
 */
public class MyHour implements Comparable<MyHour>{
    private Date date;
    
    public MyHour(int heure,int minutes) throws ParseException{
        String currentDateString = "01/01/2000 "+heure+":"+minutes;
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = sd.parse(currentDateString);
    }
    
    public MyHour(){
        this.date = new Date();
        try{
            setHeure(8);
        }
        catch(ParseException e){
            System.out.print(e.toString());
            
        }
    }
    
    public int getHeure(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.HOUR_OF_DAY);
    }
    
    public int getMinutes(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"));
        cal.setTime(date);
        return cal.get(Calendar.MINUTE);
    }
    
    public void setHeure(int heure) throws ParseException{
        String currentDateString = "01/01/2000 "+heure+":"+getMinutes();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = sd.parse(currentDateString);
    }
    
    public void setMinutes(int minutes) throws ParseException{
        String currentDateString = "01/01/2000 "+getHeure()+":"+minutes;
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        date = sd.parse(currentDateString);
    }
    
    @Override
    public String toString(){
        if(getMinutes()>10)
            return getHeure()+":"+getMinutes()+":00";
        else
            return getHeure()+":0"+getMinutes()+":00";
    }

    @Override
    public int compareTo(MyHour t) {
        if((this.getHeure() == t.getHeure())
                &&(this.getMinutes() == t.getMinutes())){
            return 0;
        }
        if(this.getHeure() > t.getHeure()){
            return 1;
        }
        if(this.getMinutes() > t.getMinutes()){
            return 1;
        }
        return -1;
    }
}
