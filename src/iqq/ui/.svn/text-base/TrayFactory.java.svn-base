package iqq.ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import iqq.util.ErrorMessage;
import iqq.util.ThreadUtil;

public class TrayFactory {

    private TrayIcon trayIcon = null;
    private JFrame comp = null;
    private String trayImageName = null;
    private String tooltip = null;
    private ImageIcon trayImageIcon = null;
    //托盘菜单
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem restoreItem = new JMenuItem("还原窗口");
    JMenuItem exitItem = new JMenuItem("退出QQ");
    private boolean isSupported = false;
    private static TrayFactory trayFactory = null;

    public static TrayFactory getInstance() {
        if (trayFactory == null) {
            trayFactory = new TrayFactory();
        }
        return trayFactory;
    }

    private void setTrayFactory(JFrame comp, String trayImageName, String tooltip) {
        this.comp = comp;
        this.trayImageName = trayImageName;
        this.tooltip = tooltip;

        init();
    }

    private void setTrayFactory(JFrame comp, ImageIcon trayImageIcon, String tooltip) {
        this.comp = comp;
        this.trayImageIcon = trayImageIcon;
        this.tooltip = tooltip;

        init();
    }

    public boolean isIsSupported() {
        return isSupported;
    }

    public void setIsSupported(boolean isSupported) {
        this.isSupported = isSupported;
    }

    private void init() {
        if (SystemTray.isSupported()) {
            isSupported = true;
            SystemTray tray = SystemTray.getSystemTray();

            //图标
            restoreItem.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(
                    getClass().getResource("/iqq/res/images/icon/back.gif"))));
            exitItem.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().createImage(
                    getClass().getResource("/iqq/res/images/icon/exit.gif"))));
            
            restoreItem.addActionListener(new RestoreItemActionListener());
            exitItem.addActionListener(new ExitItemActionListener());

            popupMenu.add(restoreItem);
            popupMenu.addSeparator();
            popupMenu.add(exitItem);

            Image image = null;
            if (trayImageIcon != null) {
                image = trayImageIcon.getImage();
                trayIcon = new TrayIcon(image, tooltip, null);

            } else {
                image = Toolkit.getDefaultToolkit().createImage(
                        getClass().getResource(trayImageName));
                trayIcon = new TrayIcon(image, tooltip, null);
            }
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new TrayIconMouseListener());
            //trayIcon.addActionListener(new TrayIconActionListener());

            try {
                tray.add(trayIcon);
            } catch (AWTException e) {
                System.err.println(e);
            }
            comp.addWindowStateListener(new WindowStateListener() { // 状态监听器

                public void windowStateChanged(WindowEvent e) {
                    comp.setExtendedState(JFrame.NORMAL);
                    comp.setState(JFrame.NORMAL);
                }
            });
        } else {
            isSupported = false;
            ErrorMessage.show("系统不支持系统托盘！");
        }
    }

    private class RestoreItemActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            openFrame();
        }
    }

    private class ExitItemActionListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {
            ThreadUtil.shutdown();
            System.exit(0);
        }
    }

    private class TrayIconMouseListener extends MouseAdapter {

        public void mousePressed(MouseEvent me) {
            if (me.isPopupTrigger()) {
                popupMenu.setLocation(me.getX()-11, me.getY()+9);
                popupMenu.setInvoker(popupMenu);
                popupMenu.setVisible(true);
                return;
            }
            if (me.getButton() == MouseEvent.BUTTON1) {
                if (comp.isVisible()) {
                    closeFrame();
                } else {
                    openFrame();
                }
            }
        }
    }
    /*
     * private class TrayIconActionListener implements ActionListener { public
     * void actionPerformed(ActionEvent ae) { comp.setVisible(true); } }
     */

    public void createTray(JFrame comp, String trayImageName, String tooltip) {
        TrayFactory.getInstance().setTrayFactory(comp, trayImageName, tooltip);
    }

    public void createTray(JFrame comp, ImageIcon trayImageIcon, String tooltip) {
        TrayFactory.getInstance().setTrayFactory(comp, trayImageIcon, tooltip);
    }

    public void openFrame() {
        comp.setExtendedState(JFrame.NORMAL);
        comp.setState(JFrame.NORMAL);
        comp.setVisible(true);
    }

    public void closeFrame() {
        comp.setExtendedState(JFrame.NORMAL);
        comp.setState(JFrame.NORMAL);
        comp.setVisible(false);
    }
}