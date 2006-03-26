package e.gui;

import e.util.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * A "Help" menu for a GUI application.
 */
public class HelpMenu {
    private String applicationName;
    private String websiteUrl;
    private String changeLogUrl;
    
    public HelpMenu(String applicationName) {
        this.applicationName = applicationName;
    }
    
    public void setWebsite(String url) {
        this.websiteUrl = url;
    }
    
    public void setChangeLog(String url) {
        this.changeLogUrl = url;
    }
    
    public JMenu makeJMenu() {
        JMenu menu = new JMenu("Help");
        
        // We don't support this yet, because we've got nothing to point it to.
        //menu.add(new PlaceholderAction(applicationName + " Help"));
        //menu.addSeparator();
        
        final String logFilename = System.getProperty("e.util.Log.filename");
        if (logFilename != null) {
            menu.add(new ShowLogAction(logFilename));
            menu.addSeparator();
        }
        
        if (websiteUrl != null) {
            menu.add(new WebLinkAction("Go To Website", websiteUrl));
        }
        if (changeLogUrl != null) {
            menu.add(new WebLinkAction("View Change Log", changeLogUrl));
        }
        
        // We don't support this yet, because we've got nothing to point it to.
        if (menu.getItemCount() > 0) {
            menu.addSeparator();
        }
        menu.add(new PlaceholderAction("View Bugs List"));
        // FIXME: anyone else using HelpMenu is going to want some control over this. "bk sendbug" and Safari's "Report Bugs to Apple..." are both good role models.
        menu.add(new WebLinkAction("Report a Bug", "mailto:software@jessies.org?subject=" + AboutBox.getSharedInstance().getBugReportSubject()));
        
        if (GuiUtilities.isMacOs() == false) {
            // GNOME and Win32 users expect a link to the application's about box on the help menu.
            menu.add(new AboutBoxAction());
        }
        
        return menu;
    }
    
    private class AboutBoxAction extends AbstractAction {
        private AboutBoxAction() {
            String name = "About";
            if (GuiUtilities.isWindows()) {
                name += " " + applicationName;
            }
            putValue(NAME, name);
            GnomeStockIcon.useStockIcon(this, "gtk-about");
        }
        
        public boolean isEnabled() {
            return AboutBox.getSharedInstance().isConfigured();
        }
        
        public void actionPerformed(ActionEvent e) {
            AboutBox.getSharedInstance().show();
        }
    }
    
    private class ShowLogAction extends AbstractAction {
        private String filename;
        
        public ShowLogAction(String filename) {
            this.filename = filename;
            putValue(NAME, "Show Log Messages");
        }
        
        public void actionPerformed(ActionEvent e) {
            // We used to use WebLinkAction with a file: URL so that the user's default text viewer would be used.
            // Sadly, on Win32, another process can't open the log file while we've got it open for writing.
            
            // FIXME: if we had some kind of file alteration monitor, we could update as the log gets written to.
            JFrameUtilities.showTextWindow(null, applicationName + " Log", StringUtilities.readFile(filename));
        }
    }
}
