/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import static threads.AutomatisationDeploiementIeThread.alertParEmail;
import tools.Configuration;

/**
 *
 * @author 04486
 */
public class SurveillanceThread extends Thread {

    private int compteurOldValue = -1;

    @Override
    public void run() {
        while (true) {
            if (compteurOldValue == AutomatisationDeploiementIeThread.compteur) {
                //restartThread
                Configuration.automatisationDeploiementIeThread = new AutomatisationDeploiementIeThread();
                Configuration.automatisationDeploiementIeThread.start();
                alertParEmail(null, "Thread IE redemarre", "Bonjour,<br>Suite à un blocage au niveau du Thread de déploiement IE, le thread a été redemarré automatiquement", "safwen.bensaid@biat.com.tn");
            }
            compteurOldValue = AutomatisationDeploiementIeThread.compteur;
            try {
                Thread.sleep(1000 * 60 * 30);
            } catch (InterruptedException ex) {
                Logger.getLogger(SurveillanceThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
