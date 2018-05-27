/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.EnvironnementDTO;
import t24Scripts.T24Scripts;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class TestExistencePackThread extends Thread {

    public EnvironnementDTO env;
    public String dossierDeBase;
    public String packName;
    public StringBuilder resultatFinal;

    public TestExistencePackThread(EnvironnementDTO env, String dossierDeBase, String packName) {
        this.env = env;
        this.dossierDeBase = dossierDeBase;
        this.packName = packName;
    }

    @Override
    public void run() {
        try {
            String resultatCommande;
            T24Scripts t24Scripts = new T24Scripts();
            resultatFinal = new StringBuilder();
            resultatCommande = t24Scripts.executerCommandeListEnvironnement(env, dossierDeBase, "if test -d " + packName + "; then echo 'FOLDER EXISTS'; else echo 'FOLDER DOES NOT EXISTS' ;fi");
            if (resultatCommande.contains("FOLDER EXISTS")) {
                Tools.showConsolLog("Le dossier " + packName + " existe");
            } else {
                Tools.showConsolLog("Le dossier " + packName + " n'existe pas");
                resultatFinal.append("Le pack <b>");
                resultatFinal.append(dossierDeBase);
                resultatFinal.append("/");
                resultatFinal.append(packName);
                resultatFinal.append("</b> n'existe pas sous l'environnement ");
                resultatFinal.append(env.getNom());
                resultatFinal.append("</b><br>");
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
