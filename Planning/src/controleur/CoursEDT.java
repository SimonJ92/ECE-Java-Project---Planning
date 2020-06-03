/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controleur;

import java.awt.event.*;
import modele.Seance;

/**
 *
 * @author simon
 */
public class CoursEDT implements ActionListener{
    private Seance seance;
    
    public CoursEDT(Seance seance){
        this.seance = seance;
        
    }
    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
