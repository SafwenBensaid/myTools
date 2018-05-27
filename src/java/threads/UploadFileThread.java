/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.EnvironnementDTO;
import java.io.File;
import t24Scripts.T24Scripts;
import tools.FtpTools;

/**
 *
 * @author 04486
 */
public class UploadFileThread extends Thread {

    public File TRANSFERTFICHIERSfichier;
    private String TRANSFERTFICHIERSchemin;
    private String TRANSFERTFICHIERSModeTransfert;
    public EnvironnementDTO env;
    public boolean problemExist;
    public String resultatHTML;
    public String resultatCommande;

    public UploadFileThread(File TRANSFERTFICHIERSfichier, String TRANSFERTFICHIERSchemin, String TRANSFERTFICHIERSModeTransfert, EnvironnementDTO env) {
        this.TRANSFERTFICHIERSfichier = TRANSFERTFICHIERSfichier;
        this.TRANSFERTFICHIERSchemin = TRANSFERTFICHIERSchemin;
        this.TRANSFERTFICHIERSModeTransfert = TRANSFERTFICHIERSModeTransfert.trim();
        this.env = env;
        problemExist = false;
    }

    @Override
    public void run() {
        try {
            //transfert
            FtpTools ftpTools = new FtpTools();
            boolean binaire = true;
            if (TRANSFERTFICHIERSModeTransfert.equals("ASCII")) {
                binaire = false;
            }
            ftpTools.uploadFileToServerDirectory("OVTOOLS", TRANSFERTFICHIERSfichier, TRANSFERTFICHIERSchemin, env, false, binaire);
            //test succès
            T24Scripts t24Scripts = new T24Scripts();
            String[] commande = new String[1];
            String chemin = TRANSFERTFICHIERSchemin.trim();
            if (chemin.endsWith("/")) {
                chemin += TRANSFERTFICHIERSfichier.getName();
            } else {
                chemin += "/" + TRANSFERTFICHIERSfichier.getName();
            }
            commande[0] = "if test -f " + chemin + "; then echo 'FILE EXISTS'; else echo 'FILE DOES NOT EXISTS' ;fi";
            resultatCommande = t24Scripts.executerCommandeListEnvironnement(env, "", commande);
            if (resultatCommande.contains("FILE DOES NOT EXISTS")) {
                problemExist = true;
                resultatCommande = "Le fichier n'a pas été transféré";
            } else {
                resultatCommande = "Le fichier a été transféré avec succès";
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
        resultat.append("								<legend class='legend1'>").append(env.getNom()).append(": ").append(TRANSFERTFICHIERSfichier.getName()).append("</legend>");
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
