package gui;

import java.awt.*;
import javax.swing.*;


public class Coordinates extends JInternalFrame 
{
	private TextArea r_coordinates;
    public Coordinates()
    {
        super("Новое окошечко", true, true, true, true);
        r_coordinates = new TextArea("");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(r_coordinates, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}