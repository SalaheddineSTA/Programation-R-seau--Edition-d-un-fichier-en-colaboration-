/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project1;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author saleh
 */
public class Response {
 
    public void ErrorRegestration(String ErrorMsg,JPanel panel){
       if (panel!= null ){
           JPanel pp = new JPanel();
           JLabel lbl = new JLabel(ErrorMsg);
           pp.setBounds(30, 80, 270, 40);
           lbl.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
           lbl.setForeground(new java.awt.Color(255, 255, 255));
           lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error.png")));
           lbl.setBounds(pp.getX()+3,pp.getY()+2, 270, 40);
           pp.setBackground(new Color(255,0,0));
           pp.setName("error");
           pp.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0,0 , 0, new java.awt.Color(255 , 255, 255)));           
           pp.add(lbl);
           JPanel pr=(JPanel) panel.getComponentAt(pp.getX()+3,pp.getY()+2);
           if(pr!=null) panel.remove(panel.getComponentAt(pp.getX()+3,pp.getY()+2));
           panel.add(pp);
           panel.repaint();
           panel.validate();
        
       }
    }
    public void SuccessRegestration(String Msg,JPanel panel){
           if (panel!= null ){
           JPanel pp = new JPanel();
           JLabel lbl = new JLabel(Msg);
           pp.setBounds(30, 80, 270, 40);
           lbl.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
           lbl.setForeground(new java.awt.Color(255, 255, 255));
           lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error.png")));
           lbl.setBounds(pp.getX()+3,pp.getY()+2, 270, 40);
           pp.setBackground(new Color(0,255,0));
           pp.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0,0 , 0, new java.awt.Color(255 , 255, 255)));           
           pp.add(lbl);
           JPanel pr=(JPanel) panel.getComponentAt(pp.getX()+3,pp.getY()+2);
           if(pr!=null) panel.remove(panel.getComponentAt(pp.getX()+3,pp.getY()+2));
           panel.add(pp);
           panel.repaint();
           panel.validate();
           
       }
      }
    public void SuccessCreation(String Msg,JPanel panel){
           if (panel!= null ){
           JPanel pp = new JPanel();
           JLabel lbl = new JLabel(Msg);
           pp.setBounds(300, 200, 270, 40);
           lbl.setFont(new java.awt.Font("Segoe UI Semilight", 1, 18)); // NOI18N
           lbl.setForeground(new java.awt.Color(255, 255, 255));
           lbl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/error.png")));
           lbl.setBounds(pp.getX()+200,pp.getY()+200, 270, 40);
           pp.setBackground(new Color(0,255,0));
           pp.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 0,0 , 0, new java.awt.Color(255 , 255, 255)));           
           pp.add(lbl);
/*           JPanel pr=(JPanel) panel.getComponentAt(pp.getX()+250,pp.getY()+100);
           if(pr!=null) panel.remove(panel.getComponentAt(pp.getX()+250,pp.getY()+100));
           panel.add(pp);
           panel.repaint();
           panel.validate();*/
           
       }
      }
    
}
    

