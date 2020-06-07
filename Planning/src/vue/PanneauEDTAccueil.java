/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

/**
 *
 * @author simon
 */
public class PanneauEDTAccueil extends JPanel {

    /**Dessine la grille de l'emploi du temps sur le panneau
     *
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.lightGray);
        g.fillRect(45, 35, 217, 650);
        g.setColor(Color.black);
        g.drawRect(45, 35, 217, 650);
        g.drawRect(44, 34, 219, 652);
        
        for(int i=1;i<=12;++i){
            g.drawLine(45, 35 + i*50, 262, 35+ i*50);
        }
    }
}
