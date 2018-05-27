/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.EnvironnementDTO;
import t24Scripts.T24Scripts;

/**
 *
 * @author 04486
 */
public class ExecuterCommandeJshOrT24Thread extends Thread {

    public String resultatCommande;
    public EnvironnementDTO env;
    public String[] commande;
    String expectedPattern;
    //T24 or JSH
    public String nature;
    public boolean problemExist;
    public String resultatHTML;

    public ExecuterCommandeJshOrT24Thread(EnvironnementDTO env, String[] commande, String nature, String expectedPattern) {
        this.env = env;
        this.commande = commande;
        this.nature = nature;
        this.expectedPattern = expectedPattern;
        problemExist = false;
    }

    @Override
    public void run() {
        try {
            T24Scripts t24Scripts = new T24Scripts();
            if (nature.equals("T24")) {
                resultatCommande = t24Scripts.executerCommandeEnvironnementT24(env, commande[0], expectedPattern).trim();
                if (!resultatCommande.contains("La commande a ete executee avec succes")) {
                    problemExist = true;
                }
            } else if (nature.equals("JSH")) {
                resultatCommande = t24Scripts.executerCommandeEnvironnementJSH(env, commande[0], expectedPattern).trim();
            } else if (nature.equals("REBUILD_SYSTEM")) {
                //commande[0] : name of SS
                resultatCommande = t24Scripts.executerCommanderEBUILDsYSTEMT24(env, commande[0]).trim();
            } else if (nature.equals("CREATION_DOSSIER")) {
                //resultatCommande = t24Scripts.executerCommandeEnvironnementJSH(env, commande, expectedPattern).trim();                
                resultatCommande = t24Scripts.executerCommandeListEnvironnement(env, "", commande);
                if (resultatCommande.contains("FOLDER DOES NOT EXISTS")) {
                    problemExist = true;
                    resultatCommande = "Le dossier n'a pas été créé";
                } else {
                    resultatCommande = "Le dossier a été créé avec succès";
                }
            }
            resultatHTML = resultatHtml();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public String resultatHtml() {
        StringBuilder resultat = new StringBuilder();
        resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
        resultat.append("								<legend class='legend1'>").append(env.getNom()).append(": ").append(commande[0].replace("mkdir -p ", "")).append("</legend>");
        if (problemExist) {
            resultat.append("                                                               <span style='color:red'>");
        }
        resultat.append("                                                                   ").append(resultatCommande);
        if (problemExist) {
            resultat.append("                                                               </span>");
        }
        resultat.append("							</fieldset>");
        return resultat.toString();
    }
}
