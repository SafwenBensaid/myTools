/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dto.EnvironnementDTO;
import dto.TripleDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import servlets.GestionLivraisonControlerServlet;
import threads.DeploiementEnMasseThread;
import threads.TestAuthentificationThread;
import threads.TestCobThread;
import threads.ManageLogThread;
import threads.TestExistencePackThread;
import threads.UploadPacksParalleleThread;

/**
 *
 * @author 04486
 */
public class DeploiementParalleleTools {

    public String[] testCobEnCours(String[] enironnementsCiblesNameArray, String connectedUser) {
        String environnementsCobEnCours = "";
        String environnementsProbleme = "";
        //Test d'authentification multithreads
        List<TestCobThread> listeThreadsTestCob = new ArrayList<TestCobThread>();
        boolean cobEnCours = false;
        boolean problemeEnv = false;
        try {
            List<EnvironnementDTO> enironnementsCiblesList = new ArrayList<>();
            for (String envName : enironnementsCiblesNameArray) {
                enironnementsCiblesList.add(Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName));
            }

            for (EnvironnementDTO envDto : enironnementsCiblesList) {
                TestCobThread th = new TestCobThread(envDto);
                th.setName("Thread " + envDto.getNom() + ", TEST_COB_EN_COURS");
                listeThreadsTestCob.add(th);
            }
            launchThreads(listeThreadsTestCob);
            //fin test cob
            for (TestCobThread th : listeThreadsTestCob) {
                if (th.problemeEnvironnement == true) {
                    problemeEnv = true;
                    environnementsProbleme += th.env.getNom() + " ";
                }
                if (th.CobEnCours == true) {
                    cobEnCours = true;
                    environnementsCobEnCours += th.env.getNom() + " ";
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            String msg = "enironnementsCiblesNameArray: ";
            for (String envName : enironnementsCiblesNameArray) {
                msg += envName + " ";
            }
            msg += " / connectedUser: " + connectedUser + " // " + tools.Tools.getStackTrace(exep);
            tools.Tools.traiterException(msg);
        }
        servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);

        if (problemeEnv || cobEnCours) {
            if (problemeEnv) {
                return new String[]{"Problème environnement:", "PROBLEME: jpqn : Cannot find proc 'loginproc' in file... sur (" + environnementsCobEnCours.trim() + ")"};
            }
            if (cobEnCours) {
                return new String[]{"Cob En Cours", "Un Cob est en cours d'exécution sur (" + environnementsCobEnCours.trim() + ")"};
            }
        } else {
            return null;
        }
        return null;
    }

    public static List<TripleDTO> genererDtoList(String[] packsNamesList, String[] mnemonicList, String[] nbrIterList) {
        // Lier les triplets (PackName, mnemonic, nbr iter)
        List<TripleDTO> packName_companyMnemonic_nbrIter_liste = new ArrayList<TripleDTO>();
        for (int i = 0; i < packsNamesList.length; i++) {
            packName_companyMnemonic_nbrIter_liste.add(new TripleDTO(packsNamesList[i], mnemonicList[i], nbrIterList[i]));
        }
        return packName_companyMnemonic_nbrIter_liste;
    }

    public Map<String, String[]> deploiementParalleleMultiPack(String connectedUser, String environnementSourceName, String dossierDeBaseDuPack, String cheminAbsoluPack, String[] packsNamesList, String[] environnementsCiblesElements, List<TripleDTO> packName_companyMnemonic_nbrIter_liste, boolean displayHtmlTag, boolean... transfertPack) {

        //si env != VERSION
        //dossierDeBaseDuPack : "PACK.TAF"
        //cheminAbsoluPack : ""

        //retour:
        //en cas de prob:          Map<"PROBLEME", [titreProb,descriptionProb]>
        //en cas de deploiement    Map<"RESULTAT", [resDep,probDep]>

        //Reset restore Map:
        Configuration.resetDeploiementMap(connectedUser);
        Map<String, String[]> resultMap = new HashMap<String, String[]>();
        resultMap.put("PROBLEME", new String[]{"", ""});
        resultMap.put("RESULTAT_HTML_COMPLET", new String[]{""});
        resultMap.put("RESULTAT", new String[]{"", ""});
        resultMap.put("ENV_DEP_OK", new String[0]);
        String resultatGlobalHTMLString = null;
        String problemesDeploiementsString = null;
        List<String> envDepOkList = new ArrayList<>();
        try {
            Configuration.initialisation();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);
            EnvironnementDTO environnementSource = Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(environnementSourceName);
            for (int i = 0; i < environnementsCiblesElements.length; i++) {
                environnementsCiblesElements[i] = environnementsCiblesElements[i].trim();
            }
            String compressedPackName = "TAF-" + System.currentTimeMillis();
            Map<String, String> errorMap = new HashMap<String, String>();
            //remplissage des environnements
            List<EnvironnementDTO> enironnementsCiblesList = new ArrayList<EnvironnementDTO>();
            for (String envName : environnementsCiblesElements) {
                enironnementsCiblesList.add(Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).get(envName));
            }
            servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);

            //Test d'authentifiaction
            String[] resTab = testAuthentification(enironnementsCiblesList, connectedUser);
            if (resTab != null) {
                resultMap.put("PROBLEME", resTab);
                return resultMap;
                //return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
            }

            //test existance pack Environnement
            resTab = testExistancePlusieursDossiers(environnementSource, dossierDeBaseDuPack, true, connectedUser, packsNamesList);
            if (resTab != null) {
                resultMap.put("PROBLEME", resTab);
                return resultMap;
                //return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
            }

            if (transfertPack.length == 0) {
                //téléchargement des pack en local
                FtpTools ftpTools = new FtpTools();
                StringBuilder packsConcatenes = new StringBuilder();
                for (int i = 0; i < packsNamesList.length; i++) {
                    packsConcatenes.append(packsNamesList[i]);
                    packsConcatenes.append(" ");
                }
                if (environnementSource.getNom().equals("VERSIONNING")) {
                    ftpTools.downloadFolder(environnementSource, cheminAbsoluPack, compressedPackName, packsConcatenes.toString(), false, true, packsNamesList);
                } else {
                    ftpTools.downloadFolder(environnementSource, "PACK.TAF", compressedPackName, packsConcatenes.toString(), false, true, packsNamesList);
                }

                //Upload des packs sur les différents environnements
                resTab = uploadPacks(enironnementsCiblesList, "PACK.TAF", compressedPackName, true, packsNamesList, connectedUser);
                if (resTab != null) {
                    resultMap.put("PROBLEME", resTab);
                    return resultMap;
                    //return Tools.redirectionPageErreurs(resTab[0], resTab[1], mapping, request, response, connectedUser);
                }
            }
            //Initialiser les Threads de déploiement            
            List<DeploiementEnMasseThread> deploiementThreadList = new ArrayList<DeploiementEnMasseThread>();
            for (EnvironnementDTO envDeploiement : enironnementsCiblesList) {
                deploiementThreadList.add(new DeploiementEnMasseThread(envDeploiement, packName_companyMnemonic_nbrIter_liste, packsNamesList, compressedPackName, connectedUser));
            }

            //Lancer les threads de déploiement 
            servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);
            ManageLogThread.afficherLogThreads("****************** 1 : start : DeploiementEnMasseThread ********************");
            for (DeploiementEnMasseThread th : deploiementThreadList) {
                th.setName("Thread " + th.environnementDeploiement.getNom() + ", DEPLOIEMENT_PACKS ");
                ManageLogThread.afficherLogThreads(th.getName() + " started");
                th.start();
            }
            //Attendre la fin du déploiement sur tous les environnements
            ManageLogThread.afficherLogThreads("****************** 2 : attendre Fin DeploiementEnMasseThread ******************** \nNumber of started threads = " + deploiementThreadList.size());
            attendreFinThreadsDeploiement(deploiementThreadList);
            ManageLogThread.afficherLogThreads("****************** 3 : Finish : DeploiementEnMasseThread ********************");


            //Collecter le code HTML du résultat
            StringBuilder resultatGlobalHTML = new StringBuilder();
            StringBuilder problemesDeploiements = new StringBuilder();
            for (DeploiementEnMasseThread threadDeploiement : deploiementThreadList) {
                resultatGlobalHTML.append(threadDeploiement.resultatDeploiementHTML);
                problemesDeploiements.append(threadDeploiement.problemesDeDeploiement);
                if (threadDeploiement.problemesDeDeploiement.length() == 0) {
                    envDepOkList.add(threadDeploiement.environnementDeploiement.getNom());
                }
            }
            resultatGlobalHTMLString = resultatGlobalHTML.toString();
            problemesDeploiementsString = problemesDeploiements.toString();
            if (problemesDeploiementsString.endsWith("<br>")) {
                problemesDeploiementsString = problemesDeploiementsString.subSequence(0, problemesDeploiementsString.length() - 5).toString();
            }


            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);


        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        String resultatHtmlComplet = genererMessageHtml(resultatGlobalHTMLString, problemesDeploiementsString, displayHtmlTag);
        resultMap.put("RESULTAT_HTML_COMPLET", new String[]{resultatHtmlComplet});
        resultMap.put("RESULTAT", new String[]{resultatGlobalHTMLString, problemesDeploiementsString});
        resultMap.put("ENV_DEP_OK", envDepOkList.toArray(new String[envDepOkList.size()]));
        return resultMap;
    }

    public static String genererMessageHtml(String resultatGlobalHTMLString, String problemesDeploiements, boolean displayHtmlTag) {
        StringBuilder sbRes = new StringBuilder();
        try {
            if (displayHtmlTag) {
                sbRes.append("\n\n{{{\n#!html\n");
            }
            sbRes.append("<div class='center'>");
            sbRes.append("    <table  class='tablePrincipale'>");
            sbRes.append("        <tr>");
            sbRes.append("            <td class='conteneurWrapper'>");
            if (problemesDeploiements != null && problemesDeploiements.length() > 0) {
                sbRes.append("<br>");
                sbRes.append("<div class='centre'>");
                sbRes.append("<span id='resultatAnalysePack'>");
                sbRes.append(problemesDeploiements);
                sbRes.append("</span>");
                sbRes.append("</div>");
            }
            sbRes.append("                <div id='wrapper'>");
            sbRes.append(resultatGlobalHTMLString);
            sbRes.append("                </div>");
            sbRes.append("            </td>");
            sbRes.append("        </tr>");
            sbRes.append("    </table>");
            sbRes.append("</div>");
            sbRes.append("<br>");
            if (displayHtmlTag) {
                sbRes.append("\n}}}");
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return sbRes.toString();
    }

    public String[] testExistancePlusieursDossiers(EnvironnementDTO envDto, String dossierDeBase, boolean logParallele, String connectedUser, String... packNameArray) throws IOException {
        String resultatCommande;
        StringBuilder resultatFinal = new StringBuilder();
        StringBuilder resultatAnalyse = new StringBuilder();
        try {
            List<TestExistencePackThread> listeThreadsTestExistencePack = new ArrayList<TestExistencePackThread>();
            //Test d'existence packs multithreads
            for (String packName : packNameArray) {
                if (logParallele) {
                    servlets.AfficherMessageEtatAvancement.appendLogmessage("Environnement " + envDto.getNom() + " : Test d'existence de(s) pack(s) sous le dossier " + dossierDeBase + " de l'environnement " + envDto.getNom() + " ...<br>", connectedUser);
                } else {
                    servlets.AfficherMessageEtatAvancement.setLogmessage("Test d'existence de(s) pack(s) sous le dossier " + dossierDeBase + " de l'environnement " + envDto.getNom(), connectedUser);
                }
                TestExistencePackThread th = new TestExistencePackThread(envDto, dossierDeBase, packName);
                th.setName("Thread " + envDto.getNom() + ", TEST_EXISTANCE_PACK");
                listeThreadsTestExistencePack.add(th);
            }
            launchThreads(listeThreadsTestExistencePack);
            //fin de Test d'existence packs multithreads
            //Debut analyse résultat d'upload

            for (TestExistencePackThread th : listeThreadsTestExistencePack) {
                if (th.resultatFinal.toString().trim().length() > 0) {
                    resultatAnalyse.append(th.resultatFinal.toString().trim());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

        if (resultatAnalyse.toString().trim().length() > 0) {
            return new String[]{"Dossier(s) inéxistant(s)", resultatAnalyse.toString()};
        } else {
            return null;
        }
    }

    public String[] testAuthentification(List<EnvironnementDTO> enironnementsCiblesList, String connectedUser) throws IOException {

        //Test d'authentification multithreads
        List<TestAuthentificationThread> listeThreadsAuthentification = new ArrayList<TestAuthentificationThread>();
        boolean problemeAuthentification = false;
        StringBuilder resultatAuthentification = new StringBuilder();
        try {
            /*
             if (!environnementSource.getNom().equals("VERSIONNING")) {
             listeThreadsAuthentification.add(new TestAuthentificationThread(environnementSource, connectedUser));
             }
             */
            for (EnvironnementDTO env : enironnementsCiblesList) {
                listeThreadsAuthentification.add(new TestAuthentificationThread(env, connectedUser));
            }

            ManageLogThread.afficherLogThreads("****************** 1 : start TestAuthentificationThread ********************");
            for (TestAuthentificationThread th : listeThreadsAuthentification) {
                th.setName("Thread " + th.env.getNom());
                ManageLogThread.afficherLogThreads(th.getName() + " started");
                th.start();
            }
            //Attendre la fin de l'authentification sur tous les environnements
            ManageLogThread.afficherLogThreads("****************** 2 : attendre Fin TestAuthentificationThread ******************** \nNumber of started threads = " + listeThreadsAuthentification.size());
            attendreFinThreadsAuthentificationSelfService(listeThreadsAuthentification);
            ManageLogThread.afficherLogThreads("****************** 3 : Finish : TestAuthentificationThread ********************");

            //fin test d'authentification
            //Debut analyse résultat d'authentification

            for (TestAuthentificationThread th : listeThreadsAuthentification) {
                Tools.showConsolLog(th.env.getNom() + "   " + th.resultatTestAuthentification);
                if (th.resultatTestAuthentification.contains("Problème d'authentification")) {
                    problemeAuthentification = true;
                    resultatAuthentification.append(th.resultatTestAuthentification);
                    resultatAuthentification.append("<br>");
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);
        if (problemeAuthentification) {
            return new String[]{"Erreur d'authentification", resultatAuthentification.toString()};
        } else {
            return null;
        }
    }

    public String[] uploadPacks(List<EnvironnementDTO> enironnementsCiblesList, String serverDirDestination, String packName, boolean logParallele, String[] listePacksCompressed, String connectedUser) throws IOException {
        //Upload des packs sur les différents environnements
        List<UploadPacksParalleleThread> listeThreadsUploadPacks = new ArrayList<UploadPacksParalleleThread>();
        StringBuilder resultatUpload = new StringBuilder();
        try {
            for (EnvironnementDTO envDto : enironnementsCiblesList) {
                UploadPacksParalleleThread th = new UploadPacksParalleleThread(envDto, serverDirDestination, packName, logParallele, listePacksCompressed, connectedUser);
                th.setName("Thread " + envDto.getNom() + ", UPLOAD_PACK");
                listeThreadsUploadPacks.add(th);
            }
            launchThreads(listeThreadsUploadPacks);
            //fin d'upload
            //Debut analyse résultat d'upload

            for (UploadPacksParalleleThread th : listeThreadsUploadPacks) {
                if (!(th.erreurTransfertPack == null) && th.erreurTransfertPack.trim().length() > 0) {
                    resultatUpload.append(th.erreurTransfertPack.trim());
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }

        if (resultatUpload.toString().trim().length() > 0) {
            return new String[]{"Problème de transfert", resultatUpload.toString()};
        } else {
            return null;
        }
    }

    public void launchThreads(List<? extends Thread> listeThreads) {
        if (listeThreads != null && !listeThreads.isEmpty()) {
            Class<? extends Thread> threadClass = listeThreads.get(0).getClass();
            ManageLogThread.afficherLogThreads("****************** 1 : start " + threadClass.getSimpleName() + " ********************");
            for (Thread th : listeThreads) {
                ManageLogThread.afficherLogThreads(th.getName() + " started");
                th.start();
            }
            //Attendre la fin de l'authentification sur tous les environnements
            ManageLogThread.afficherLogThreads("****************** 2 : attendre Fin Threads " + threadClass.getSimpleName() + " ******************** \nNumber of started threads = " + listeThreads.size());
            attendreFinThreads(listeThreads);
            ManageLogThread.afficherLogThreads("****************** 3 : Finish " + threadClass.getSimpleName() + " ********************");
        };
    }

    public void attendreFinThreads(List<? extends Thread> listeThreads) {
        //Attendre la fin de l'authentification sur tous les environnements
        long start_time = System.currentTimeMillis();
        long wait_time = 60000;
        long end_time = start_time + wait_time;
        int nbrThreadTestAuthentificationFinis = 0;
        while (nbrThreadTestAuthentificationFinis < listeThreads.size()) {
            nbrThreadTestAuthentificationFinis = 0;
            for (Thread th : listeThreads) {
                if (!th.isAlive()) {
                    nbrThreadTestAuthentificationFinis++;
                    if (!th.getName().endsWith(" finished")) {
                        th.setName(th.getName() + " finished");
                        ManageLogThread.afficherLogThreads(th.getName());
                    }
                }
            }
            if (System.currentTimeMillis() > end_time) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }

    public void attendreFinThreadsDeploiement(List<DeploiementEnMasseThread> listeThreads) {
        //Attendre la fin de l'authentification sur tous les environnements
        int nbrThreadTestAuthentificationFinis = 0;
        while (nbrThreadTestAuthentificationFinis < listeThreads.size()) {
            nbrThreadTestAuthentificationFinis = 0;
            for (DeploiementEnMasseThread th : listeThreads) {
                if (th.endDeploy == true) {
                    nbrThreadTestAuthentificationFinis++;
                    if (!th.getName().endsWith(" finished")) {
                        th.setName(th.getName() + " finished");
                        ManageLogThread.afficherLogThreads(th.getName());
                    }
                }
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }

    public void attendreFinThreadsAuthentificationSelfService(List<TestAuthentificationThread> listeThreadsAuthentification) {
        //Attendre la fin de l'authentification sur tous les environnements
        int nbrThreadTestAuthentificationFinis = 0;
        long start_time = System.currentTimeMillis();
        long wait_time = 30000;
        long end_time = start_time + wait_time;
        while (nbrThreadTestAuthentificationFinis < listeThreadsAuthentification.size()) {
            nbrThreadTestAuthentificationFinis = 0;
            for (TestAuthentificationThread th : listeThreadsAuthentification) {
                if (!th.isAlive()) {
                    nbrThreadTestAuthentificationFinis++;
                    if (!th.getName().endsWith(" finished")) {
                        th.setName(th.getName() + " finished");
                        ManageLogThread.afficherLogThreads(th.getName());
                    }
                }
            }
            if (System.currentTimeMillis() > end_time) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
    }
}
