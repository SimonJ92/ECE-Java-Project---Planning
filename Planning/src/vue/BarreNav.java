/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vue;

import java.awt.*;
import javax.swing.BorderFactory;
import javax.swing.JMenuBar;

/**
 *
 * @author simon
 */
public class BarreNav extends JMenuBar{
    Color backgroundColor;
    
    /**Constructeur
     *
     * @param couleur   la couleur de la barre de navigation
     */
    public BarreNav(Color couleur){
        backgroundColor = couleur;
        setBorder(BorderFactory.createEmptyBorder());
    }
    
    /**Dessine la barre de navigation avec la couleur choisie
     *
     * @param g
     */
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(backgroundColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }
}
