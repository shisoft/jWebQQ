package iqq.comm;


import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/** 
* The class which generates the 'X' icon for the tabs. The constructor 
* accepts an icon which is extra to the 'X' icon, so you can have tabs 
* like in JBuilder. This value is null if no extra icon is required. 
*/  
public class CloseTabIcon implements Icon {
    
    private int x_pos;  
    private int y_pos;  
    private int width;  
    private int height;  
    private Icon fileIcon;  
    public CloseTabIcon(Icon fileIcon) {  
        this.fileIcon = fileIcon;  
        width = 16;  
        height = 16;
    }
    public CloseTabIcon(URL url) {  
        this(new ImageIcon(url));
    }
    public void paintIcon(Component c, Graphics g, int x, int y) {  
        this.x_pos = x;  
        this.y_pos = y;  
        if (fileIcon != null) {  
             fileIcon.paintIcon(c, g, x, y);    
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
}