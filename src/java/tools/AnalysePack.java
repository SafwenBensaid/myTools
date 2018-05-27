/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dto.*;
import java.util.*;
import t24Scripts.T24Scripts;

/**
 *
 * @author 04486
 */
public class AnalysePack {

    /**
     * @param args the command line arguments
     */
    public boolean packTn1Vide = false;
    public String problemeNbrObjetsTransferesDifferents = "";
    String connectedUser = null;
    T24Scripts t24Scripts = null;

    public AnalysePack() {
        connectedUser = "OVTOOLS";
    }

    public String setNbrObjetsPack(String ch, int nbrObjetsEnvironnementSource) {
        int nbrObjetsEnvironnementDestination = 0;
        StringBuilder sbRes = new StringBuilder();
        try {
            String[] tab = ch.split("\n");
            for (String aux : tab) {
                if (aux.trim().startsWith("#_#")) {
                    try {
                        nbrObjetsEnvironnementDestination = Integer.parseInt(aux.trim().replace("#_#", ""));
                        if (nbrObjetsEnvironnementSource != nbrObjetsEnvironnementDestination) {
                            problemeNbrObjetsTransferesDifferents = "<span style='color:red'><br/><b>Attention:<br></b>Le nombre d'objets fournis est différent du nombre d'objets packagés<br>Nombre d'objets fournis = " + nbrObjetsEnvironnementSource + "<br>Nombre d'objets packagés = " + nbrObjetsEnvironnementDestination + "</span>";
                        }
                    } catch (Exception e) {
                        nbrObjetsEnvironnementDestination = 0;
                        if (nbrObjetsEnvironnementSource != nbrObjetsEnvironnementDestination) {
                            problemeNbrObjetsTransferesDifferents = "<span style='color:red'><br/><b>Attention:<br></b>Veuillez vérifier le nombre d'objets fournis par rapport au nombre d'objets packagés<br>Nombre d'objets fournis = " + nbrObjetsEnvironnementSource + "<br>Nombre d'objets packagés = " + nbrObjetsEnvironnementDestination + "</span>";
                        }
                    }
                    Tools.showConsolLog("****" + nbrObjetsEnvironnementDestination + "****");
                } else {
                    sbRes.append(aux.trim());
                    sbRes.append("\n");
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return sbRes.toString();
    }

    public String setTableauTypeVerif(String resultatCommandes, String packName) {

        Set<String> setOfUsedTypes = new TreeSet<String>();
        StringBuilder sbResVerif = new StringBuilder();
        StringBuilder sbResCoherence = new StringBuilder();
        StringBuilder sbResTotal = new StringBuilder();
        try {
            String[] tab = resultatCommandes.split("\n");
            String type = null;
            for (String aux : tab) {
                if (aux.trim().contains("O_O")) {
                    type = aux.trim().replace("O_O", "");
                    if (!setOfUsedTypes.contains(type)) {
                        setOfUsedTypes.add(type);
                        sbResVerif.append("<tr>");
                        //
                        sbResVerif.append("<td>");
                        sbResVerif.append(type);
                        sbResVerif.append("</td>");
                        //
                        sbResVerif.append("<td>");
                        sbResVerif.append(Configuration.typesReglesMap.get(type));
                        sbResVerif.append("</td>");
                        //
                        sbResVerif.append("</tr>");
                    }
                } else if (aux.trim().contains("X_X")) {
                    type = aux.trim().replace("X_X", "");
                    if (!setOfUsedTypes.contains(type)) {
                        setOfUsedTypes.add(type);
                        sbResCoherence.append("<tr>");
                        //
                        sbResCoherence.append("<td>");
                        sbResCoherence.append(type.replaceAll("PACK.NAME", packName));
                        sbResCoherence.append("</td>");
                        //
                        sbResCoherence.append("<td>");
                        sbResCoherence.append(Configuration.observationReglesMap.get(type).replaceAll("PACK.NAME", packName));
                        sbResCoherence.append("</td>");
                        //
                        sbResCoherence.append("</tr>");
                    }
                }
            }
            if (sbResVerif.length() > 0) {
                sbResTotal.append("<div class='titreAnalysePack'>Contrôle Normes et méthodes</div>");
                sbResTotal.append("<table id='resultatVerifObjets'  class='roundCornerTable'>");
                sbResTotal.append("<thead><tr><th>Type d'objet</th><th>Contrainte technique</th></tr></thead><tbody>");
                sbResTotal.append(sbResVerif);
                sbResTotal.append("</tbody></table>");
            }
            if (sbResCoherence.length() > 0) {
                sbResTotal.append("<div class='titreAnalysePack'>Contrôle de Cohérence</div>");
                sbResTotal.append("<table id='resultatVerifObjets'  class='roundCornerTable'>");
                sbResTotal.append("<thead><tr><th>Obsertvation</th><th>Contrainte technique</th></tr></thead><tbody>");
                sbResTotal.append(sbResCoherence);
                sbResTotal.append("</tbody></table>");
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return sbResTotal.toString().trim().replace("\n", "<br>");
    }

    public String analysePack(String circuit, String packName, int nbrObjetsEnvironnementSource, boolean arrayformat) {
        String resultat = null;


        resultat = "";

        try {
            servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse du pack " + packName, connectedUser);
            T24Scripts t24Scripts = new T24Scripts();

            EnvironnementDTO env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            resultat = t24Scripts.executerCommandeListEnvironnementAvecRegex(connectedUser, env, "/work", "./ANALYSE_PACK " + packName + " " + circuit, "#####START_ANALYSE_PACK#####", null, "#####END_ANALYSE_PACK#####");
            resultat = setNbrObjetsPack(resultat, nbrObjetsEnvironnementSource);


            //resultat += "\n_______________ <b>VERIF CUS</b> ________________\n";
            resultat += verifCUS_HM(circuit, packName).trim();
            if (arrayformat) {
                resultat = setTableauTypeVerif(resultat, packName);
            }
            resultat += problemeNbrObjetsTransferesDifferents;
            Tools.showConsolLog("****\n" + resultat + "\n****");
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

        return resultat.trim();
    }

    public String verifCUS_HM(String circuit, String packName) {

        String resCommande = "";
        Tools tools = new Tools();
        T24Scripts t24Scripts = new T24Scripts();
        EnvironnementDTO env = null;
        try {
            if (circuit.equals("PROJET")) {
                env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASS2");
            } else if (circuit.equals("RELEASE")) {
                env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASS");
            } else if (circuit.equals("UPGRADE")) {
                env = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("ASSU");
            }


            resCommande = t24Scripts.executerCommandeListEnvironnementAvecRegex(connectedUser, env, "/DEPT24", "./ANALYSE_PACK " + packName + " " + circuit, "#####START_ANALYSE_PACK#####", null, "#####END_ANALYSE_PACK#####");

            if (resCommande.contains("FILE.CONTROL de type CUS")) {
                // Tester si le pack TN1 est vide ou pas
                if (resCommande.contains("est vide")) {
                    packTn1Vide = true;
                    //resCommande += "Le pack TAF-PACK.NAME.TN1 est vide";
                }
            }
            //resultat+=resultatVerifHM;
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return resCommande;
    }
}
