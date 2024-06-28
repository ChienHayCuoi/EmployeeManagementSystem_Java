/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package JCalendar;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author whyia
 */
public class Cell extends JPanel{
    private JLabel label;
    public Cell(String s){
        setLayout(new BorderLayout());
        String htmlS = "<html><div style='text-align: center;'>" 
                + s.replaceAll("\n", "<br>") + "</div></html>";
        // Initialize the label with the provided text
        label = new JLabel(htmlS, SwingConstants.CENTER);

        // Add the label to the panel
        add(label, BorderLayout.CENTER);
    }
}
