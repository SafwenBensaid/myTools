/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dataBaseTracRequests.DataBaseTracRequests;
import dataBaseTracRequests.AppelRequetes;
import dto.NumeLivraisonRevisionDTO;
import dto.ResultatAnalyseIntersectionDTO;
import dto.EnvironnementDTO;
import dto.QuadripleDTO;
import dto.StructureMessageLogDTO;
import dto.StructureResultatAnalyseIntersectionDTO;
import entitiesMysql.Etudeintersection;
import java.util.*;
import t24Scripts.T24Scripts;
import tools.Tools;


/**
 *
 * @author 04486
 */
public class EtudeIntersectionTools {

    String numeroTicketTrac;
    List listeObjets;
    String nomProjet;

    public EtudeIntersectionTools(String numeroTicketTrac) {
        this.numeroTicketTrac = numeroTicketTrac;
    }

    public String getNumeroTicketTrac() {
        return numeroTicketTrac;
    }

    public String traiterEtudeIntersectionInput(String objetsT24, String packName, String environnementSourceName, String connectedUser, String sourceEtude) {
        Tools tools = new Tools();
        T24Scripts t24Scripts = new T24Scripts();
        String lesObjets = null;
        if (sourceEtude.equals("OBJETS")) {
            Tools.showConsolLog("########################################\n" + objetsT24 + "\n########################################");
            lesObjets = tools.traiterString(objetsT24);
            lesObjets = tools.remplacerCaracteresSpeciaux(lesObjets);
        } else if (sourceEtude.equals("PACK")) {
            String pathDuPackEnvSource = "PACK.TAF/";
            String pathDuPackEnvSource1 = "PACK.TAF";
            String auxString;
            String auxTab[];
            List<String> listOfFiles = new ArrayList<String>();
            FtpTools ftpTools = new FtpTools();
            if (!packName.startsWith("TAF-")) {
                packName = "TAF-" + packName;
            }
            Tools.showConsolLog("########################################\n" + packName + "\n########################################");
            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(environnementSourceName);


            //test existance dossier Environnement             
            boolean packExists = false;
            String dossierDeBaseDuPack = "PACK.TAF";
            packExists = t24Scripts.testExistanceDossier(environnementSource, packName, dossierDeBaseDuPack);
            if (packExists == false) {
                Tools.traiterException("Le dossier <b>" + dossierDeBaseDuPack + "/" + packName + "</b> n'existe pas sur l'environnement " + environnementSource.getNom());
                return "Dossier inéxistant";
            }
            //fin test existance dossier Environnement 

            long millisecondsTime = System.currentTimeMillis();

            String resultatListerFichiers = t24Scripts.executerCommandeListEnvironnement(environnementSource, pathDuPackEnvSource + packName, "find ./ -type f |awk '{ print \"ls \",$1 }'|bash > ../VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
            ftpTools.downloadFile(environnementSource, pathDuPackEnvSource1, "VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
            String[] fileTab = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + "/VERIFICATIONDELTAOV_" + millisecondsTime + ".txt");
            Tools.showConsolLog("__________________________");
            for (int i = 0; i < fileTab.length; i++) {
                if ((fileTab[i].trim().length() > 0)) {
                    auxString = fileTab[i].trim();
                    auxTab = auxString.split("./");
                    auxString = auxTab[auxTab.length - 1];
                    auxString = auxString.replaceFirst("-", ">");
                    auxString = tools.remplacerCaracteresSpeciaux(auxString);
                    if (auxString.contains(">")) {
                        //Tools.showConsolLog("###" + auxString);
                        listOfFiles.add(auxString);
                    }
                }
            }
            Tools.showConsolLog("_____________list Size_____________" + listOfFiles.size());
            lesObjets = tools.traiterListString(listOfFiles);
        }
        return lesObjets;
    }

    public List<StructureResultatAnalyseIntersectionDTO> separerObjets(String lesObjets) {
        List<StructureResultatAnalyseIntersectionDTO> liste = new ArrayList<StructureResultatAnalyseIntersectionDTO>();
        String objLog = null;
        try {
            String[] tabObj = lesObjets.split("\n");
            String auxObjType;
            String auxObjName;
            for (String aux : tabObj) {
                objLog = aux;
                auxObjType = aux.split(">")[0];
                auxObjName = aux.split(">")[1];
                liste.add(new StructureResultatAnalyseIntersectionDTO(auxObjType, auxObjName));
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(objLog + "#_#_#\n<br>" + lesObjets + "\n\n\n" + tools.Tools.getStackTrace(exep));
        }
        return liste;
    }

    public List<StructureResultatAnalyseIntersectionDTO> analyseIntersectionBI(List<StructureResultatAnalyseIntersectionDTO> listeBI, List<StructureResultatAnalyseIntersectionDTO> liste, Tools tools, String nomProjet, String connectedUser) {
        try {
            String pathDepot;
            pathDepot = Configuration.parametresList.get("cheminDepotDeltaProjets") + "/" + nomProjet + "/TAF-FULLPACK";
            Tools.showConsolLog(pathDepot);
            EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            String resultat;
            for (StructureResultatAnalyseIntersectionDTO structure : liste) {
                this.nomProjet = structure.getTypeObj() + "-" + structure.getNomObj();
                listeObjets = Configuration.mapObjetsParProjet.get(nomProjet);
                if (!listeObjets.contains(this.nomProjet)) {
                    //si l'objet ne figure pas dans la BI, ne pas faire svn log de cet objet sur la BI
                    //Tools.showConsolLog("l'objet " + this.nomProjet + " n'existe pas sur la BI " + nomProjet);
                    continue;
                }

                StructureResultatAnalyseIntersectionDTO structureBI = (StructureResultatAnalyseIntersectionDTO) structure.clone();

                servlets.AfficherMessageEtatAvancement.setLogmessage("Etude d'intersection de l'objet " + structureBI.getTypeObj() + "-" + structureBI.getNomObj() + " sur la branche individuelle " + nomProjet, connectedUser);

                String commandeSvnLog = "svn log --incremental   ";
                commandeSvnLog += structureBI.getTypeObj() + "/" + structureBI.getTypeObj() + "-" + structureBI.getNomObj();
                Tools.showConsolLog("_______________________________________________" + nomProjet + "_______________________________________________");
                Tools.showConsolLog(commandeSvnLog);
                T24Scripts t24Scripts = new T24Scripts();
                resultat = t24Scripts.executerCommandeListEnvironnement(envirVersionning, pathDepot, commandeSvnLog);
                Tools.showConsolLog(resultat);
                if (resultat.contains("is not under version control") || !resultat.contains("------------------------------------------------------------------------")) {
                    structureBI.setResultatEtudeIntersection(resultat);
                    Tools.showConsolLog("******************************************************\n" + resultat + "\n******************************************************");
                } else {
                    resultat = resultat.replaceAll("END.PROCES", "################");
                    resultat = resultat.replaceFirst("------------------------------------------------------------------------", "################");
                    resultat = resultat.split("################")[1].trim();
                    structureBI.setResultatEtudeIntersection(resultat);
                    structureBI.setNomProjet(nomProjet);
                    Tools.showConsolLog("******************************************************\n" + resultat + "\n******************************************************");
                    structureBI = analyseMessageLog(structureBI, tools, nomProjet);
                }
                listeBI.add(structureBI);
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return listeBI;
    }

    public StructureResultatAnalyseIntersectionDTO analyseMessageLog(StructureResultatAnalyseIntersectionDTO structure, Tools tools, String typeProjet) {
        try {
            String[] messagesAux;
            String[] messagesLogTab;
            messagesLogTab = structure.getResultatEtudeIntersection().split("------------------------------------------------------------------------");
            List<StructureMessageLogDTO> structureMessageLogList = new ArrayList<StructureMessageLogDTO>();
            for (String message : messagesLogTab) {
                if (message.trim().length() > 0) {
                    try {
                        StructureMessageLogDTO structureMessageLog = new StructureMessageLogDTO();
                        structureMessageLog.setMessageLog(message);
                        structureMessageLog.setMessageCommit(getMessageCommit(message));
                        structureMessageLog.setRevision(getRevision(message));
                        messagesAux = structureMessageLog.getMessageCommit().split(":");

                        Tools.showConsolLog("^^^^^^^Message Commit:^^^^^^^^");
                        Tools.showConsolLog(structureMessageLog.getMessageCommit());
                        for (int i = 0; i < messagesAux.length; i++) {
                            Tools.showConsolLog("i: " + i + " msg:    Â¤" + messagesAux[i].trim() + "Â¤");
                        }
                        Tools.showConsolLog("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");

                        structureMessageLog.setNomPack(messagesAux[0].trim());
                        structureMessageLog.setNbObj(messagesAux[1].trim());
                        structureMessageLog.setDate(messagesAux[2].trim());
                        structureMessageLog.setTime(messagesAux[3].trim());
                        structureMessageLog.setLivraison(messagesAux[4].trim());
                        structureMessageLog.setAction(messagesAux[5].trim());
                        structureMessageLog.setNomProjet(messagesAux[6].trim());
                        // pour que les noms de projets apparaissent dans le rapport final
                        structure.setNomProjet(messagesAux[6].trim());
                        // fin
                        structureMessageLogList.add(structureMessageLog);
                    } catch (ArrayIndexOutOfBoundsException exep) {
                        exep.printStackTrace();
                        Tools.traiterException(Tools.getStackTrace(exep));
                    } catch (Exception exep) {
                        exep.printStackTrace();
                        Tools.traiterException(Tools.getStackTrace(exep));
                    }
                }
            }
            Collections.sort(structureMessageLogList);
            structure.setStructureMessageLog(structureMessageLogList);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return structure;
    }

    public String getMessageCommit(String ch) {
        String resultat = null;
        try {
            String[] tabString = ch.split("\n");
            resultat = tabString[tabString.length - 1].replace("\r", "");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat;
    }

    public String getRevision(String ch) {
        String[] tabString1 = null;
        try {
            ch = ch.replace("\r", "");
            String[] tabString = ch.split("\n");
            List<String> listeAux = new ArrayList<String>();
            for (String element : tabString) {
                if (element.trim().length() > 0) {
                    listeAux.add(element.trim());
                }
            }
            String firstLine = listeAux.get(0);
            tabString1 = firstLine.split(" ");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return tabString1[0].replace("r", "");
    }

    public List<ResultatAnalyseIntersectionDTO> analyserLesStructuresDeLog(List<StructureResultatAnalyseIntersectionDTO> listeStructureResultatAnalyseIntersection, String typeProjet, String connectedUser) {
        List<ResultatAnalyseIntersectionDTO> listeTiquetsQuiOntCauseIntersection = new ArrayList<ResultatAnalyseIntersectionDTO>();
        try {
            servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse des résultats obtenus", connectedUser);
            String revisionBaseRelease = Configuration.parametresList.get("revisionBaseRelease");
            for (StructureResultatAnalyseIntersectionDTO structure : listeStructureResultatAnalyseIntersection) {
                ResultatAnalyseIntersectionDTO resultatAnalyseIntersection = new ResultatAnalyseIntersectionDTO();
                resultatAnalyseIntersection.setTypeObj(structure.getTypeObj());
                resultatAnalyseIntersection.setNomObj(structure.getNomObj());
                List<NumeLivraisonRevisionDTO> listeNumLivraisonRevision = new ArrayList<NumeLivraisonRevisionDTO>();
                resultatAnalyseIntersection.setListeNumLivraisonRevision(listeNumLivraisonRevision);
                listeTiquetsQuiOntCauseIntersection.add(resultatAnalyseIntersection);
                List<StructureMessageLogDTO> listStructureMessageLog = structure.getStructureMessageLog();
                for (StructureMessageLogDTO structureMessageLog : listStructureMessageLog) {
                    if (typeProjet.equals("RELEASE")) {
                        if (structureMessageLog.getAction().equals("HF") || structureMessageLog.getRevision().equals(revisionBaseRelease)) {
                            break;
                        }
                    }
                    if (!structureMessageLog.getNomProjet().contains("HARM HOTFIX")) {
                        listeNumLivraisonRevision.add(new NumeLivraisonRevisionDTO(structureMessageLog.getLivraison(), structureMessageLog.getRevision(), structureMessageLog.getNomProjet(), structureMessageLog.getNomPack()));
                    }
                }
            }
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return listeTiquetsQuiOntCauseIntersection;
    }

    public List<Etudeintersection> setNumAnomalieId(List<Etudeintersection> listEtudeIntersection) {
        try {
            if (!listEtudeIntersection.isEmpty()) {
                List<Integer> listeIdTicketsLivraisons = new ArrayList<Integer>();
                for (Etudeintersection etudeintersection : listEtudeIntersection) {
                    listeIdTicketsLivraisons.add(Integer.parseInt(etudeintersection.getLivraisonIntersection().replace("#", "").trim()));
                }
                DataBaseTracRequests req = new DataBaseTracRequests();
                Map<String, Object> mapAnomaliesOfLivraison = req.getAnomaliesByLivraisons(listeIdTicketsLivraisons);;
                for (Etudeintersection etudeintersection : listEtudeIntersection) {
                    etudeintersection.setAnomalieIntersection(((String) mapAnomaliesOfLivraison.get(etudeintersection.getLivraisonIntersection().replace("#", "").trim())));
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return listEtudeIntersection;
    }

    public List<Etudeintersection> setAnomaliePriority(List<Etudeintersection> listEtudeIntersection) {
        Map<String, Object> coupleAnomaliePriorite = null;
        try {
            if (!listEtudeIntersection.isEmpty()) {
                List<Integer> listeIdTicketsAnomalies = new ArrayList<Integer>();
                for (Etudeintersection etudeintersection : listEtudeIntersection) {
                    if (etudeintersection.getLivraisonIntersection().contains("0000")) {
                        etudeintersection.setAnomalieIntersection("0");
                    }
                    listeIdTicketsAnomalies.add(Integer.parseInt(etudeintersection.getAnomalieIntersection().replace("#", "").trim()));
                }
                coupleAnomaliePriorite = AppelRequetes.getAnomaliesPrioritieByAnomalieIds(listeIdTicketsAnomalies);
                for (Etudeintersection etudeintersection : listEtudeIntersection) {
                    etudeintersection.setAnomalieIntersectionPriority((((String) coupleAnomaliePriorite.get(etudeintersection.getAnomalieIntersection()).toString().replace("#", "").trim())));
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return listEtudeIntersection;
    }

    public QuadripleDTO analyserResultatEtudeIntersectionFinal(List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionRelease, List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionProjet, List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersectionUpgrade, String packName, String livraisonHotfix, String connectedUser, Date dateEtude) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Analyse des tickets de livraison et d'anomalies contenant les objets qui ont causé des intersections", connectedUser);
        List<Etudeintersection> listEtudeIntersectionRelease = new ArrayList<>();
        List<Etudeintersection> listEtudeIntersectionProjet = new ArrayList<>();
        List<Etudeintersection> listEtudeIntersectionUpgrade = new ArrayList<>();
        try {
            analyserResultatEtudeIntersectionFinalParCircuit(resultatAnalyseIntersectionRelease, listEtudeIntersectionRelease, "CR", packName, livraisonHotfix, connectedUser, dateEtude);
            analyserResultatEtudeIntersectionFinalParCircuit(resultatAnalyseIntersectionProjet, listEtudeIntersectionProjet, "CP", packName, livraisonHotfix, connectedUser, dateEtude);
            analyserResultatEtudeIntersectionFinalParCircuit(resultatAnalyseIntersectionUpgrade, listEtudeIntersectionUpgrade, "CU", packName, livraisonHotfix, connectedUser, dateEtude);
            //ajouter les id des tickets d'anomalies correspendants
            listEtudeIntersectionRelease = setNumAnomalieId(listEtudeIntersectionRelease);
            listEtudeIntersectionProjet = setNumAnomalieId(listEtudeIntersectionProjet);
            listEtudeIntersectionUpgrade = setNumAnomalieId(listEtudeIntersectionUpgrade);

            listEtudeIntersectionRelease = setAnomaliePriority(listEtudeIntersectionRelease);
            listEtudeIntersectionProjet = setAnomaliePriority(listEtudeIntersectionProjet);
            listEtudeIntersectionUpgrade = setAnomaliePriority(listEtudeIntersectionUpgrade);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return genererMessageFinalEtudeIntersection(listEtudeIntersectionRelease, listEtudeIntersectionProjet, listEtudeIntersectionUpgrade);
    }

    public void analyserResultatEtudeIntersectionFinalParCircuit(List<ResultatAnalyseIntersectionDTO> resultatAnalyseIntersection, List<Etudeintersection> listEtudeIntersection, String circuit, String packName, String livraisonHotfix, String user, Date dateEtude) {
        String nomObjet = "";
        String typeObjet = "";
        try {
            for (ResultatAnalyseIntersectionDTO elementAnalyseIntersection : resultatAnalyseIntersection) {
                if (elementAnalyseIntersection.getListeNumLivraisonRevision().size() > 0) {
                    nomObjet = elementAnalyseIntersection.getNomObj();
                    typeObjet = elementAnalyseIntersection.getTypeObj();
                    for (NumeLivraisonRevisionDTO numLivraisonRevision : elementAnalyseIntersection.getListeNumLivraisonRevision()) {
                        listEtudeIntersection.add(new Etudeintersection(typeObjet, nomObjet, circuit, numLivraisonRevision.getPackName(), numLivraisonRevision.getRevision(), numLivraisonRevision.getNumLivraison(), null, null, livraisonHotfix, numLivraisonRevision.getNomProjet(), user, dateEtude));
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public QuadripleDTO genererMessageFinalEtudeIntersection(List<Etudeintersection> listEtudeIntersectionRelease, List<Etudeintersection> listEtudeIntersectionProjet, List<Etudeintersection> listEtudeIntersectionUpgrade) {
        StringBuilder resultat = new StringBuilder();
        String resultetIntersectionRelease = null;
        String resultetIntersectionProjet = null;
        String resultetIntersectionUpgrade = null;
        try {
            if (!numeroTicketTrac.equals("")) {
                resultat.append("\n<b><center>Résultat d'étude d'intersection du ticket " + numeroTicketTrac + "</center></b>\n");
            }
            resultat.append("\n<span class='couleurRelease'><b>================ Résultat d'étude d'intersection avec le circuit RELEASE ================</b></span>\n\n");
            resultetIntersectionRelease = genererMessageFinalEtudeIntersectionIndividuelle(listEtudeIntersectionRelease, "RELEASE");
            if (resultetIntersectionRelease.trim().equals("")) {
                resultat.append("Aucune intersection avec le circuit RELEASE");
            } else {
                resultat.append(resultetIntersectionRelease.trim());
            }

            resultat.append("\n\n<span class='couleurProjet'><b>================ Résultat d'étude d'intersection avec le circuit PROJET ================</b></span>\n");
            resultetIntersectionProjet = genererMessageFinalEtudeIntersectionIndividuelle(listEtudeIntersectionProjet, "PROJET");
            if (resultetIntersectionProjet.trim().equals("")) {
                resultat.append("Aucune intersection avec le circuit PROJET");
            } else {
                resultat.append(resultetIntersectionProjet.trim());
            }


            //////////////

            resultat.append("\n\n<span class='couleurProjet'><b>================ Résultat d'étude d'intersection avec le circuit UPGRADE ================</b></span>\n");
            resultetIntersectionUpgrade = genererMessageFinalEtudeIntersectionIndividuelle(listEtudeIntersectionUpgrade, "UPGRADE");
            if (resultetIntersectionUpgrade.trim().equals("")) {
                resultat.append("Aucune intersection avec le circuit UPGRADE");
            } else {
                resultat.append(resultetIntersectionUpgrade.trim());
            }

            Tools.showConsolLog(resultat.toString());
            //servlets.AfficherMessageEtatAvancement.setLogmessage("");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        //S1: CR   S2:CP  S3:CU  S4:Res global
        QuadripleDTO quadripleDTO = new QuadripleDTO(resultetIntersectionRelease, resultetIntersectionProjet, resultetIntersectionUpgrade, resultat.toString().trim());
        return quadripleDTO;
    }

    public String genererMessageFinalEtudeIntersectionIndividuelle(List<Etudeintersection> listEtudeIntersection, String circuit) {

        StringBuilder resultat = new StringBuilder();
        try {
            Collections.sort(listEtudeIntersection);
            String ticketDeLivraison;
            int ticketDeLivraisonInt;
            int ticketAnomalie;
            String priorityAnomalie;
            String projet;
            //DataBaseRequests requetes = new DataBaseRequests();
            DataBaseTools dbTools;
            List<String> nomsReleaseUtulises = new ArrayList<String>();


            if (listEtudeIntersection.isEmpty()) {
                return "";
            } else {
                for (Etudeintersection elementRelease : listEtudeIntersection) {
                    ticketDeLivraison = elementRelease.getLivraisonIntersection();
                    ///@@@###@@@///
                    ticketDeLivraisonInt = Integer.parseInt(ticketDeLivraison.replace("#", "").trim());
                    ticketAnomalie = Integer.parseInt(elementRelease.getAnomalieIntersection());
                    priorityAnomalie = elementRelease.getAnomalieIntersectionPriority();
                    projet = elementRelease.getNomBrancheIndividuelle();
                    if (projet.trim().equals("OV")) {
                        priorityAnomalie = "<span class='rouge'>" + priorityAnomalie + "</span>";
                    }
                    if (priorityAnomalie.equals("APPLIQUEE SUR PROD")) {
                        continue;
                    }
                    if (!nomsReleaseUtulises.contains(elementRelease.getTypeObjet() + ">" + elementRelease.getNomObjet())) {
                        nomsReleaseUtulises.add(elementRelease.getTypeObjet() + ">" + elementRelease.getNomObjet());
                        resultat.append("\nL'objet ").append(elementRelease.getTypeObjet()).append(">").append(elementRelease.getNomObjet()).append(" a été livré dans le(s) ticket(s):\n");
                        if (circuit.equals("RELEASE") && elementRelease.getNomPack().contains("LIVR")) {
                            resultat.append("<span class='couleurRelease'>");
                        } else {
                            resultat.append("<span class='couleurProjet'>");
                        }
                        resultat.append("(Ticket de livraison: " + ticketDeLivraison + " / Ticket d'anomalie: #" + ticketAnomalie + " : " + priorityAnomalie + " / Révision: r" + elementRelease.getRevisionIntersection());
                        resultat.append(" / projet: " + elementRelease.getNomBrancheIndividuelle() + " _ " + Configuration.getNomProjetParAbreviation(elementRelease.getNomBrancheIndividuelle()));
                        resultat.append(")</span>\n");
                    } else {
                        if (circuit.equals("RELEASE") && elementRelease.getNomPack().contains("LIVR")) {
                            resultat.append("<span class='couleurRelease'>");
                        } else {
                            resultat.append("<span class='couleurProjet'>");
                        }
                        resultat.append("(Ticket de livraison: " + ticketDeLivraison + " / Ticket d'anomalie: #" + ticketAnomalie + " : " + priorityAnomalie + " / Révision: r" + elementRelease.getRevisionIntersection());
                        resultat.append(" / projet: " + elementRelease.getNomBrancheIndividuelle() + " _ " + Configuration.getNomProjetParAbreviation(elementRelease.getNomBrancheIndividuelle()));
                        resultat.append(")</span>\n");
                    }
                    dbTools = new DataBaseTools(Configuration.puOvTools);
                    dbTools.StoreObjectIntoDataBase(elementRelease);
                    dbTools.closeRessources();
                }
            }
            resultat.append("\n");
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultat.toString();
    }
}
