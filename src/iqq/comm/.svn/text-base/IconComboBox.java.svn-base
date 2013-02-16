/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.comm;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;

/**
 *
 * @author chenzhihui
 */
public class IconComboBox extends JComboBox implements Icon {
     private int x_pos;  
    private int y_pos;  
    private int width;  
    private int height;  
    private Icon icon;

    public IconComboBox(){}
    
    public IconComboBox(Icon fileIcon) {  
        this.icon = fileIcon;  
        width = 16;  
        height = 16;
    }
    public IconComboBox(URL url) {  
        this(new ImageIcon(url));
    }
    public void paintIcon(Component c, Graphics g, int x, int y) {  
        this.x_pos = x;  
        this.y_pos = y;  
        if (icon != null) {  
             icon.paintIcon(c, g, x, y);    
        }  
    }  
    public int getIconWidth() {  
        return width ;  
    }  
    public int getIconHeight() {  
        return height;  
    }  
    public Rectangle getBounds() {  
        return new Rectangle(x_pos, y_pos, width, height);  
    }  
    
    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }
}
