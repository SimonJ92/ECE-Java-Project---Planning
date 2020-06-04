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
public class PanneauEDTGrille extends JPanel{
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawRect(44, 34, 1309, 652);
        for(int i=0;i<6;++i){
            g.setColor(Color.lightGray);
            g.fillRect(45 + i*218, 35, 217, 650);
            g.setColor(Color.black);
            g.drawRect(45 + i*218, 35, 217, 650);
        }
        
        for(int i=1;i<=12;++i){
            g.drawLine(45, 35 + i*50, 1347, 35+ i*50);
        }
    }
}
