/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import com.amoebacode.ftp.FTPClient;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import org.apache.struts.action.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import strutsForms.*;
import tools.*;
import dto.*;
import t24Scripts.*;

/**
 *
 * @author 04486
 */
public class ConstitutionPackMultiprojetsAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        //Vider les variables de la session
        SessionTools.clearSessionVariables(request);

        String connectedUser = Tools.getConnectedLogin();

        try {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            ConstitutionPackMultiprojetsForm inF = (ConstitutionPackMultiprojetsForm) form;
            String packName = inF.getNomPack();
            String[] projetsCiblesElements = inF.getProjetsCiblesElements().split("/");
            String tri = inF.getTri();
            Boolean triExportParType = false;
            if (tri.equals("OUI")) {
                triExportParType = true;
            } else {
                triExportParType = false;
            }
            EnvironnementDTO envVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            T24Scripts t24Scripts = new T24Scripts();
            t24Scripts.executerCommandeListEnvironnement(envVersionning, "/work", "rm -rf " + packName, "mkdir " + packName);



            EtudeIntersectionTools etudeIntersectionTools = new EtudeIntersectionTools("");
            Tools tools = new Tools();
            ConstitutionPackMultiprojetsAction constituationpackMultiprojets = new ConstitutionPackMultiprojetsAction();
            Map<String, StructureResultatAnalyseIntersectionDTO> mapStructure = constituationpackMultiprojets.chargerTousObjetsDeChaqueProjet(projetsCiblesElements, tools);
            List<StructureResultatAnalyseIntersectionDTO> listeStructures = constituationpackMultiprojets.analyseIntersectionBI(etudeIntersectionTools, mapStructure, tools);
            constituationpackMultiprojets.exportObjectsToFolder(connectedUser, listeStructures, packName, envVersionning, t24Scripts, tools, triExportParType);

            StringBuilder projects = new StringBuilder();
            for (String proj : projetsCiblesElements) {
                projects.append(proj + " ");
            }

            Properties prop = new Properties();
            prop.put("packName", packName);
            prop.put("projectsNames", projects.toString());
            request.getSession().setAttribute("ResultatConstitutionPackMultiprojets", prop);

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
        }

        return mapping.findForward("ResultatConstitutionPackMultiprojets");
    }
    /*
     public static void main(String[] args) {
     Boolean triExportParType = true;
     Configuration.initialisation();

     String packName = "TAF-PACK.ANIS.COPIE.X";

     Environnement envVersionning = Configuration.environnementList.get("VERSIONNING");
     T24Scripts t24Scripts = new T24Scripts();
     //t24Scripts.executerCommandeListEnvironnement(envVersionning, "/work", "rm -rf " + packName, "mkdir " + packName);



     EtudeIntersectionTools etudeIntersectionTools = new EtudeIntersectionTools();
     Tools tools = new Tools();
     String[] projetsCiblesElements = {"INGI", "INTR"};
     ConstitutionPackMultiprojetsAction constituationpackMultiprojets = new ConstitutionPackMultiprojetsAction();
     Map<String, StructureResultatAnalyseIntersectionDTO> mapStructure = constituationpackMultiprojets.chargerTousObjetsDeChaqueProjet(projetsCiblesElements, tools);
     List<StructureResultatAnalyseIntersectionDTO> listeStructures = constituationpackMultiprojets.analyseIntersectionBI(etudeIntersectionTools, mapStructure, tools);
     constituationpackMultiprojets.exportObjectsToFolder(listeStructures, packName, envVersionning, t24Scripts, tools, triExportParType);

     }
     */

    public void exportObjectsToFolder(String connectedUser, List<StructureResultatAnalyseIntersectionDTO> listeStructures, String packName, EnvironnementDTO envVersionning, T24Scripts t24Scripts, Tools tools, Boolean triExportParType) {
        long time = System.currentTimeMillis();
        try {
            List<String> commandeList = new ArrayList<String>();
            commandeList.add("rm -rf " + packName);
            commandeList.add("mkdir " + packName);

            if (triExportParType == true) {
                Set<String> objectTypesSet = new TreeSet<String>();
                for (StructureResultatAnalyseIntersectionDTO structure : listeStructures) {
                    objectTypesSet.add(structure.getTypeObj());
                }
                for (String nomProjet : objectTypesSet) {
                    commandeList.add("mkdir " + packName + "/" + nomProjet);
                }
            }

            for (StructureResultatAnalyseIntersectionDTO structure : listeStructures) {
                if (structure.getListeNomsProjets().size() > 1) {
                    Tools.showConsolLog(structure.getTypeObj() + "-" + structure.getNomObj() + "   " + structure.getNomProjet() + "  " + structure.getLastRevision());
                }
                if (triExportParType == true) {
                    commandeList.add("cp /VERSDATA/LWorkings/LW_RELEASE/depot_release/branches/DELTA.PROJET/" + structure.getNomProjet() + "/TAF-FULLPACK/" + structure.getTypeObj() + "/" + structure.getTypeObj() + "-" + structure.getNomObj() + " /work/" + packName + "/" + structure.getTypeObj());
                } else {
                    commandeList.add("cp /VERSDATA/LWorkings/LW_RELEASE/depot_release/branches/DELTA.PROJET/" + structure.getNomProjet() + "/TAF-FULLPACK/" + structure.getTypeObj() + "/" + structure.getTypeObj() + "-" + structure.getNomObj() + " /work/" + packName);
                }
            }
            File file = null;

            try {
                file = tools.createFile(Configuration.parametresList.get("espaceLocal") + "/commandsFileOvTools" + time);
                tools.writeInFile(file, commandeList);
            } catch (IOException ex) {
                Logger.getLogger(ConstitutionPackMultiprojetsAction.class.getName()).log(Level.SEVERE, null, ex);
            }
            FtpTools ftpTools = new FtpTools();
            ftpTools.uploadFileToServerDirectory(connectedUser, file, "/work", envVersionning, false, false);
            String[] commandArray = new String[3];
            commandArray[0] = "chmod 777 commandsFileOvTools" + time;
            commandArray[1] = "./commandsFileOvTools" + time;
            commandArray[2] = "rm -f ./commandsFileOvTools" + time;
            t24Scripts.executerCommandeListEnvironnement(envVersionning, "/work", commandArray);
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        try {
            //supprimer le fichier du serveur
            FTPClient ftpClient = new FTPClient(true);
            ftpClient.openConnection(envVersionning.getUrl(), 21);
            ftpClient.login(envVersionning.getEnvUserName(), envVersionning.getEnvPassword());
            ftpClient.changeDirectory("/work");
            ftpClient.deleteFile("commandsFileOvTools" + time);
            Tools.showConsolLog("###################################");
        } catch (Exception ex) {
            //si le fichier est introuvable, ne rien faire
        }
    }

    public Map<String, StructureResultatAnalyseIntersectionDTO> chargerTousObjetsDeChaqueProjet(String[] projetsCiblesElements, Tools tools) {
        servlets.AfficherMessageEtatAvancement.setLogmessage("Chargement de tous les objets des Branches individuelles", Tools.getConnectedLogin());
        String connectedUser = Tools.getConnectedLogin();
        T24Scripts t24Scripts = new T24Scripts();
        String resultatListerFichiers;
        FtpTools ftpTools = new FtpTools();
        String objectType;
        String objectName;
        String projectName;
        String auxTab[];
        List<String> listeObjets = new ArrayList<String>();
        Map<String, StructureResultatAnalyseIntersectionDTO> mapStructure = new LinkedHashMap<String, StructureResultatAnalyseIntersectionDTO>();
        try {
            String commande = "find ";
            for (String projName : projetsCiblesElements) {
                commande += " ./" + projName;
            }
            long millisecondsTime = System.currentTimeMillis();
            commande += " -type f |awk '{ print \"ls \",$1 }'|grep -v svn|bash > /work/listObjets_" + millisecondsTime + ".txt";

            Tools.showConsolLog(commande);

            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
            resultatListerFichiers = t24Scripts.executerCommandeListEnvironnement(environnementSource, Configuration.parametresList.get("cheminDepotDeltaProjets"), commande);
            ftpTools.downloadFile(environnementSource, "/work", "listObjets_" + millisecondsTime + ".txt");


            String[] fileTab = tools.convertFileContentToTab(Configuration.parametresList.get("espaceLocal") + "/listObjets_" + millisecondsTime + ".txt");
            Tools.showConsolLog("__________________________");
            for (int i = 0; i < fileTab.length; i++) {
                if ((fileTab[i].trim().length() > 0)) {
                    if (fileTab[i].contains("/")) {
                        objectName = fileTab[i].trim();
                        auxTab = objectName.split("/");
                        objectType = auxTab[auxTab.length - 1].split("-")[0];
                        objectName = auxTab[auxTab.length - 1].replace(objectType + "-", "");
                        projectName = auxTab[1];
                        //Tools.showConsolLog(projectName + " :: " + objectType + "-" + objectName);

                        if (mapStructure.containsKey(objectType + "-" + objectName)) {
                            mapStructure.get(objectType + "-" + objectName).getListeNomsProjets().add(projectName);
                        } else {
                            StructureResultatAnalyseIntersectionDTO structure = new StructureResultatAnalyseIntersectionDTO(objectType, objectName);
                            structure.setNomProjet(projectName);
                            List<String> listeNomsProjets = new ArrayList<String>();
                            listeNomsProjets.add(projectName);
                            structure.setListeNomsProjets(listeNomsProjets);
                            mapStructure.put(objectType + "-" + objectName, structure);
                        }
                    }
                }
            }

            for (String type_nom_objet : mapStructure.keySet()) {
                StructureResultatAnalyseIntersectionDTO structure = mapStructure.get(type_nom_objet);
                if (structure.getListeNomsProjets().size() > 1) {
                    System.out.print(type_nom_objet + ":");
                    for (String proj : structure.getListeNomsProjets()) {
                        System.out.print(proj + "#");
                    }
                    Tools.showConsolLog("");
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return mapStructure;
    }

    public List<StructureResultatAnalyseIntersectionDTO> analyseIntersectionBI(EtudeIntersectionTools etudeIntersectionTools, Map<String, StructureResultatAnalyseIntersectionDTO> mapStructure, Tools tools) {
        String pathDepot;
        String connectedUser = Tools.getConnectedLogin();
        EnvironnementDTO envirVersionning = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get("VERSIONNING");
        String resultat;
        List<StructureResultatAnalyseIntersectionDTO> listeStructures = new ArrayList<StructureResultatAnalyseIntersectionDTO>();
        try {
            for (String type_nom_objet : mapStructure.keySet()) {
                StructureResultatAnalyseIntersectionDTO structure = mapStructure.get(type_nom_objet);

                //Si l'objet existe dans plusieurs projets, faire un svn log sur chaque branche
                if (structure.getListeNomsProjets().size() > 1) {
                    for (String projetCourant : structure.getListeNomsProjets()) {
                        pathDepot = Configuration.parametresList.get("cheminDepotDeltaProjets") + "/" + projetCourant + "/TAF-FULLPACK";
                        Tools.showConsolLog(pathDepot);



                        servlets.AfficherMessageEtatAvancement.setLogmessage("Etude d'intersection de l'objet " + structure.getTypeObj() + "-" + structure.getNomObj() + " sur la branche individuelle " + projetCourant, Tools.getConnectedLogin());

                        String commandeSvnLog = "svn log --incremental -r HEAD:PREV  ";
                        commandeSvnLog += structure.getTypeObj() + "/" + structure.getTypeObj() + "-" + structure.getNomObj();
                        Tools.showConsolLog("_______________________________________________" + projetCourant + "_______________________________________________");
                        Tools.showConsolLog(commandeSvnLog);
                        T24Scripts t24Scripts = new T24Scripts();
                        resultat = t24Scripts.executerCommandeListEnvironnement(envirVersionning, pathDepot, commandeSvnLog);
                        Tools.showConsolLog(resultat);
                        if (resultat.contains("is not under version control") || !resultat.contains("------------------------------------------------------------------------")) {
                            structure.setResultatEtudeIntersection(resultat);
                            Tools.showConsolLog("******************************************************\n" + resultat + "\n******************************************************");
                        } else {
                            resultat = resultat.replaceAll("END.PROCES", "################");
                            resultat = resultat.replaceFirst("------------------------------------------------------------------------", "################");
                            resultat = resultat.split("################")[1].trim();

                            StructureResultatAnalyseIntersectionDTO structureAux = (StructureResultatAnalyseIntersectionDTO) structure.clone();

                            structureAux.setResultatEtudeIntersection(resultat);
                            Tools.showConsolLog("******************************************************\n" + resultat + "\n******************************************************");
                            structureAux = etudeIntersectionTools.analyseMessageLog(structureAux, tools, projetCourant);


                            int oldRevision = structure.getLastRevision();
                            int newRevision = Integer.parseInt(structureAux.getStructureMessageLog().get(0).getRevision());
                            if (newRevision > oldRevision) {
                                structure = structureAux;
                                structure.setLastRevision(newRevision);
                                structure.setNomProjet(projetCourant);
                            }
                        }
                    }
                }
                listeStructures.add(structure);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(exep));
        }
        return listeStructures;

    }
}
