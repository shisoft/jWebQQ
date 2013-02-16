/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package iqq.util;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import iqq.service.LoginService;
import iqq.ui.LoginPanel;

/**
 *
 * @author chenzhihui
 */
public class BrowserUtil {

    public static void openURL(String urlStr) {
        //urlStr = LoginService.getInstance().login2(urlStr);
        try {
            // TODO add your handling code here:
            URI url = new URI(urlStr);
            Log.println(url);
            java.awt.Desktop.getDesktop().browse(url);
        } catch (IOException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(LoginPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        BrowserUtil.openURL("file://" + QQEnvironment.getConfigPath() + "index.html");
    }
}
