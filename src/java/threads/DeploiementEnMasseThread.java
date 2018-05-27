/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import t24Scripts.T24Scripts;
import tools.*;


/*
 Le script qui va nous permettre le déploiement en masse est disponible sous le serveur VERSION sous /DEPT24 :
 DEPDEVASS_PARAM_SANS_TRANSFERT 


 Ce script prend 9 paramètres en entrée :

 DEPDEVASS_PARAM_SANS_TRANSFERT  $1 $2 $3 $4 $5 $6 $7 $8 $9

 Avec :

 $1 : Le pack à déployer 
 $2 : Login système
 $3 : Password système 
 $4 : Login T24 BROWSER
 $5 : Password T24 BROWSER
 $6 : Adresse IP de l’env
 $7 : Nom de l’env
 $8 : Company avec laquelle on fera le déploiement
 $9 : Abréviation de l’environnement : ce paramètre sera concaténer 
 * à la valeur « newlogbig » pour constituer un répertoire de LOG pour chaque env  (exple : newlogbigASS)

 NB : chaque environnement doit avoir un répertoire newlogbig$9 sous /DEPT24 du serveur VERSION 

 */
/**
 *
 * @author 04486
 */
public class DeploiementEnMasseThread extends Thread {

    public EnvironnementDTO environnementDeploiement;
    List<TripleDTO> packName_companyMnemonic_nbrIter_liste_liste; // val1 : packname  // val2: mnemonic 
    public String[] packsNamesList;
    String compressedPackName;
    List<PackNameListDeployOutPut> resultatDeploiementEnvCourant;
    public StringBuilder resultatDeploiementHTML;
    public boolean endDeploy = false;
    public String connectedUser;
    public StringBuilder problemesDeDeploiement;
    public boolean sessionExpiree;
    /*
     String packsName; //$1
     String loginSystem; //$2
     String passwordSystem; //$3
     String loginBrowser; //$4
     String passwordBrowser; //$5
     String adresseIpEnvironnement; //$6
     String abreviationNomEnvironnement; //$7 exp biatass
     String companyMnemonic; //$8
     String nomEnvironnement; //$9 exp ASS
     */

    public DeploiementEnMasseThread(EnvironnementDTO environnementDeploiement, List<TripleDTO> packName_companyMnemonic_nbrIter_liste_liste, String[] packsNamesList, String compressedPackName, String connectedUser) {
        this.environnementDeploiement = environnementDeploiement;
        this.packName_companyMnemonic_nbrIter_liste_liste = packName_companyMnemonic_nbrIter_liste_liste;
        this.packsNamesList = packsNamesList;
        this.compressedPackName = compressedPackName;
        this.connectedUser = connectedUser;
        sessionExpiree = false;
    }

    @Override
    public void run() {
        processDeploiement();
    }

    private void processDeploiement(int... iterations) {
        try {
            resultatDeploiementHTML = new StringBuilder();
            problemesDeDeploiement = new StringBuilder();
            //Upload des packs sur l'environnement sous le dossier PACK.TAF

            /*
             FtpTools ftpTools = new FtpTools(connectedUser);
             String erreurTransfertPack = ftpTools.uploaddCompressedFolderThenUntarIt(environnementDeploiement, "PACK.TAF", compressedPackName, true, packsNamesList);
             */


            //fin upload packs
            //Start deploy
            String companyMnemonic;
            String packName;
            int nbrIter;
            String commande;
            String resultatDeploiement;
            T24Scripts t24Scripts = new T24Scripts(connectedUser);
            EnvironnementDTO envVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get("OVTOOLS").get("VERSIONNING");
            resultatDeploiementEnvCourant = new ArrayList<>();
            for (TripleDTO packName_companyMnemonic_nbrIter_liste : packName_companyMnemonic_nbrIter_liste_liste) {
                packName = packName_companyMnemonic_nbrIter_liste.getValeur1();
                companyMnemonic = packName_companyMnemonic_nbrIter_liste.getValeur2();
                nbrIter = Integer.parseInt(packName_companyMnemonic_nbrIter_liste.getValeur3());
                PackNameListDeployOutPut packNameListDeployOutPut = new PackNameListDeployOutPut(packName, companyMnemonic, new ArrayList<String>());
                for (int i = 0; i < nbrIter; i++) {
                    commande = "DEPDEVASS_PARAM_SANS_TRANSFERT \"" + packName.replace("TAF-", "") + "\" \"" + environnementDeploiement.getEnvUserName() + "\" \"" + environnementDeploiement.getEnvPassword().replace("$", "\\$") + "\" \"" + environnementDeploiement.getBrowserUser() + "\" \"" + environnementDeploiement.getBrowserPassword().replace("$", "\\$") + "\" \"" + environnementDeploiement.getUrl() + "\" \"" + environnementDeploiement.getAbreviationNom() + "\" \"" + companyMnemonic + "\" \"" + environnementDeploiement.getNom() + "\"";
                    ManageLogThread.ecrireLogFichier(environnementDeploiement.getNom(), "\n\n\n\n\n\n\n\n***************************************************************************************************************\n" + commande);
                    servlets.AfficherMessageEtatAvancement.deploiementEnCours.get(connectedUser).put(environnementDeploiement.getNom(), true);
                    if (nbrIter > 1) {
                        servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : Déploiement du pack " + packName + " sur l'environnement " + environnementDeploiement.getNom() + "(itération : " + (i + 1) + ") ...<br>", connectedUser);
                    } else {
                        servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : Déploiement du pack " + packName + " sur l'environnement " + environnementDeploiement.getNom() + " ...<br>", connectedUser);
                    }
                    if (sessionExpiree == false) {
                        resultatDeploiement = t24Scripts.executerCommandeListEnvironnementAvecRegex(connectedUser, envVersionning, "/DEPT24", commande, "#_#_#_# DEBUT RESULTAT #_#_#_#", environnementDeploiement.getNom(), "#_#_#_# FIN RESULTAT #_#_#_#", "Sorry, but your session is no longer active", "SECURITY.VIOLATION", "Login incorrect", "Choose a new password", "Impossible d'effectuer une restaur");
                        if (resultatDeploiement.contains("Impossible d'effectuer une restaur")) {
                            resultatDeploiement = "Désolé, votre utilisateur T24 est mal configuré, en effet il <b>ne doit avoir aucun menu</b> et sa <b>langue doit être égale à 1</b>";
                            servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : Désolé, votre utilisateur T24 est mal configuré, en effet il <b>ne doit avoir aucun menu</b> et sa <b>langue doit être égale à 1</b> ...<br>", connectedUser);
                            sessionExpiree = true;
                        } else if (resultatDeploiement.contains("Sorry, but your session is no longer active")) {
                            try {
                                //sleep 20 sec
                                Thread.sleep(20 * 1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(DeploiementEnMasseThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (iterations.length == 0) {
                                processDeploiement(0);
                            } else if (iterations[0] < 10) {
                                processDeploiement((iterations[0] + 1));
                            } else {
                                resultatDeploiement = "<b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b>";
                                servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : <b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b> ...<br>", connectedUser);
                                sessionExpiree = true;
                            }
                        } else if (resultatDeploiement.contains("SECURITY.VIOLATION")) {
                            resultatDeploiement = "<b>SECURITY.VIOLATION</b>";
                            servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : <b>SECURITY.VIOLATION</b> ...<br>", connectedUser);
                            sessionExpiree = true;
                        } else if (resultatDeploiement.contains("Login incorrect")) {
                            resultatDeploiement = "<b>Votre login ou mot de passe système sont incorrectes [Login incorrect]</b>";
                            servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : <b>Votre login ou mot de passe système sont incorrectes [Login incorrect]</b> ...<br>", connectedUser);
                            sessionExpiree = true;
                        }
                    } else {
                        resultatDeploiement = "<b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b>";
                        servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + environnementDeploiement.getNom() + " : <b>Désolé, votre session T24 a expiré [T24: Sorry, but your session is no longer active]</b> ...<br>", connectedUser);
                        sessionExpiree = true;
                    }
                    servlets.AfficherMessageEtatAvancement.deploiementEnCours.get(connectedUser).put(environnementDeploiement.getNom(), false);
                    packNameListDeployOutPut.getDeployOutput().add(resultatDeploiement);
                    //Reset restore Map:
                    Configuration.resetDeploiementMap(connectedUser);
                }
                resultatDeploiementEnvCourant.add(packNameListDeployOutPut);
            }
            //Générer le contenu HTML
            Tools tools = new Tools();
            resultatDeploiementHTML = genererResultatDeploiementHTML(resultatDeploiementEnvCourant, resultatDeploiementHTML, environnementDeploiement, tools);
            endDeploy = true;
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public StringBuilder genererResultatDeploiementHTML(List<PackNameListDeployOutPut> resultatDeploiementEnvCourant, StringBuilder resultatDeploiementHTML, EnvironnementDTO env, Tools tools) {
        boolean problemeDeploiementExiste = false;
        try {
            resultatDeploiementHTML.append("<div class='accordionButton on'>Résultat du déploiement sur L'environnement " + env.getNom() + "</div>");
            resultatDeploiementHTML.append("<div class='accordionContent' style='display: block;'>");
            // boucle packs
        /*
             if (erreurTransfertPack.trim().length() > 0) {
             resultatDeploiementHTML.append("<span class='rouge'><center>");
             resultatDeploiementHTML.append(erreurTransfertPack);
             resultatDeploiementHTML.append("</center></span><br>");
             }
             */
            String msg;
            for (PackNameListDeployOutPut packDetails : resultatDeploiementEnvCourant) {
                resultatDeploiementHTML.append("<fieldset class='fieldSetResultatDeploiement'>");
                resultatDeploiementHTML.append("<legend class='legend1'>").append(packDetails.getPackName()).append(" / ").append(packDetails.getMnemonic()).append("</legend>");
                int comp = 0;
                for (String resultat : packDetails.getDeployOutput()) {
                    comp++;
                    problemeDeploiementExiste = tools.testIfResultatDeploiementContainsProblems(resultat);
                    if (problemeDeploiementExiste == true) {
                        msg = "<span class='rouge'>" + resultat.replace("\n", "<br>") + "</span><br>";
                    } else {
                        msg = resultat.replace("\n", "<br>");
                    }

                    if (packDetails.getDeployOutput().size() == 1) {
                        resultatDeploiementHTML.append("<p class='contenu'>");
                        if (problemeDeploiementExiste == true) {
                            resultatDeploiementHTML.append("<span class='rouge'>");
                            resultatDeploiementHTML.append(msg);
                            resultatDeploiementHTML.append("</span>");

                            problemesDeDeploiement.append("<span class='rouge'>Le déploiement du pack <b>");
                            problemesDeDeploiement.append(packDetails.getPackName());
                            problemesDeDeploiement.append("</b> sur l'environnement <b>");
                            problemesDeDeploiement.append(env.getNom());
                            problemesDeDeploiement.append("</b> avec le mnemonic <b>");
                            problemesDeDeploiement.append(packDetails.getMnemonic());
                            problemesDeDeploiement.append("</b> à l'itération <b>");
                            problemesDeDeploiement.append(comp);
                            problemesDeDeploiement.append("</b> a échoué</span><br>");
                        } else {
                            resultatDeploiementHTML.append(msg);
                        }
                        resultatDeploiementHTML.append("</p>");
                    } else {
                        resultatDeploiementHTML.append("<fieldset class='fieldsetContenu'>");
                        resultatDeploiementHTML.append("<legend class='legend2'>Itération " + comp + "</legend>");
                        //problemeDeploiementExiste
                        if (msg.contains("RESTORE3")) {
                            String auxTab[] = msg.split("RESTORE3");
                            String aux = auxTab[auxTab.length - 1];
                            if (aux.contains("erreurs")) {
                                problemeDeploiementExiste = true;
                            }
                        }
                        if (!msg.contains("RESTORE2") || !msg.contains("RESTORE3")) {
                            problemeDeploiementExiste = true;
                        }

                        if (problemeDeploiementExiste == true) {
                            resultatDeploiementHTML.append("<span class='rouge'>");
                            resultatDeploiementHTML.append(msg);
                            resultatDeploiementHTML.append("</span>");

                            problemesDeDeploiement.append("<span class='rouge'>Le déploiement du pack <b>");
                            problemesDeDeploiement.append(packDetails.getPackName());
                            problemesDeDeploiement.append("</b> sur l'environnement <b>");
                            problemesDeDeploiement.append(env.getNom());
                            problemesDeDeploiement.append("</b> avec le mnemonic <b>");
                            problemesDeDeploiement.append(packDetails.getMnemonic());
                            problemesDeDeploiement.append("</b> à l'itération <b>");
                            problemesDeDeploiement.append(comp);
                            problemesDeDeploiement.append("</b> a échoué</span><br>");
                        } else {
                            resultatDeploiementHTML.append(msg);
                        }
                        resultatDeploiementHTML.append("</fieldset>");
                    }
                }
                resultatDeploiementHTML.append("</fieldset>");
            }
            // fin boucle
            resultatDeploiementHTML.append("</div>");
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return resultatDeploiementHTML;
    }
}