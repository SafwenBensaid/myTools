/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author 04486
 */
import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.livraison.*;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import entitiesTrac.TicketChange;
import entitiesTrac.TicketCustom;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class ManipulationObjectsTool {

    public File serialisation(Object objectToBeSerialised, String filename, String folderName) {
        File fichier = null;
        try {
            String filePath = Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator");
            if (folderName != null) {
                filePath += folderName + System.getProperty("file.separator");

                File folder = new File(filePath);
                if (!folder.exists()) {
                    folder.mkdir();
                }
            }
            filePath += filename;

            fichier = new File(filePath);
            // ouverture d'un flux sur un fichier
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(fichier));
                oos.writeObject(objectToBeSerialised);

            } catch (IOException ex) {
            } finally {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException ex) {
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return fichier;
    }

    public static byte[] serialisation(Object objectToBeSerialised) {
        byte[] fichierEnByte = null;
        try {
            File fichier = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "SERIALISEDOBJECT");
            // ouverture d'un flux sur un fichier
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(fichier));
                oos.writeObject(objectToBeSerialised);

            } catch (IOException ex) {
            } finally {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException ex) {
                }
            }
            fichierEnByte = convertFileToByte(fichier);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return fichierEnByte;
    }

    public Object convertByteToObject(byte[] serializedObjectByteArray, Integer... numeroLivraison) {
        Object obj = null;
        try {
            File file = createFile(serializedObjectByteArray);
            obj = deserialisation(file, numeroLivraison);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return obj;
    }

    public static byte[] convertFileToByte(File file) {
        byte[] b = new byte[(int) file.length()];
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            fileInputStream.read(b);
            fileInputStream.close();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return b;
    }

    public Object deserialisation(File file, Integer... numeroLivraison) {
        FileInputStream fichier = null;
        Object obj = null;
        try {
            if (!file.exists()) {
                return null;
            }
            fichier = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fichier);
            obj = (Object) ois.readObject();

            return obj;
        } catch (EOFException exep) {
            exep.printStackTrace();
            Tools.showConsolLog("On a lu toutes les données");
        } catch (Exception exep) {
            exep.printStackTrace();
            String msg = "";
            if (numeroLivraison.length > 0) {
                msg += "Ticket qui a causé le probleme: " + numeroLivraison[0];
            }
            tools.Tools.traiterException(msg + "\nLa longueur du fichier est:" + file.length() + "\n" + tools.Tools.getStackTrace(exep));
        } finally {
            try {
                fichier.close();
            } catch (Exception ex) {
                ex.printStackTrace();
                String msg = "";
                if (numeroLivraison.length > 0) {
                    msg += "Ticket qui a causé le probleme: " + numeroLivraison[0];
                }
                tools.Tools.traiterException(msg + "\nLa longueur du fichier est:" + file.length() + "\n" + tools.Tools.getStackTrace(ex));
            }
        }
        return obj;
    }

    public File createFile(byte[] serializedObjectByteArray) {
        FileOutputStream fileOuputStream = null;
        File file = null;
        try {
            file = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "SERIALISEDOBJECT");
            fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(serializedObjectByteArray);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                fileOuputStream.close();
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return file;
    }

    public Map<String, Map<String, String>> getAllHotfixTicketsDetails(int... iterations) {

        Map<String, Map<String, String>> mapTickets = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> mapTicketsDateDeploiement = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiement = new LinkedHashMap<String, Map<String, String>>();
        List<Integer> listeIdTickets = new ArrayList<Integer>();
        String pu = Configuration.puLivraisons;
        try {
            List<TicketCustom> listeTicketCustom = AppelRequetes.getTicketCustomListOfNamedQuery(pu, Configuration.tracLivraisons, "TicketCustom.findAllHotfixTickets");
            for (TicketCustom tick : listeTicketCustom) {
                if (mapTickets.containsKey(tick.getTicket().toString())) {
                    mapTickets.get(tick.getTicket().toString()).put(tick.getName(), tick.getValue());
                } else {
                    Map<String, String> mapAux = new HashMap<String, String>();
                    mapAux.put(tick.getName(), tick.getValue());
                    mapAux.put("type", tick.getTicketPointer().getType());
                    mapAux.put("priority", tick.getTicketPointer().getPriority());
                    mapAux.put("summary", tick.getTicketPointer().getSummary());
                    mapAux.put("milestone", tick.getTicketPointer().getMilestone());
                    mapAux.put("owner", tick.getTicketPointer().getOwner());
                    mapAux.put("reporter", tick.getTicketPointer().getReporter());
                    mapTickets.put(tick.getTicket().toString(), mapAux);
                    listeIdTickets.add(tick.getTicket());
                }
            }
            //Tools.showConsolLog(mapTickets.toString());
            mapTicketsDateDeploiement = getDeployementDate(pu, listeIdTickets, "DEPLOYEE");
            mapTicketsDateEnvoiPourDeploiement = getDeployementDate(pu, listeIdTickets, "PRET POUR DEPLOIEMENT");


            Iterator<Map.Entry<String, Map<String, String>>> iter = mapTickets.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, Map<String, String>> entryGlob = iter.next();
                /*
                 Tools.showConsolLog("___________________________________");
                 Tools.showConsolLog(entryGlob.getKey());
                 Tools.showConsolLog(entryGlob.getValue().get("nature_liv"));
                 Tools.showConsolLog("___________________________________");
                 */
                if (entryGlob.getValue().get("nature_liv") == null) {
                    iter.remove();
                } else if (entryGlob.getValue().size() < 5) {
                    //supprimer les anciens tickets mal configurés
                    iter.remove();
                } else if (entryGlob.getValue().get("nature_liv").equals("HARMONISATION_C.RELEASE") || entryGlob.getValue().get("nature_liv").equals("HARMONISATION_C.PROJET")) {
                    //supprimer les tickets d'harmonisation
                    iter.remove();
                } else {
                    if ((entryGlob.getValue().get("type").equals("HOT FIXE TEST") || entryGlob.getValue().get("type").equals("ACTION A CHAUD TEST")) && entryGlob.getValue().get("priority").equals("DEPLOYEE")) {
                        entryGlob.getValue().put("etat", "OV_HF_NON_QUALIFIEE");
                    } else if ((entryGlob.getValue().get("type").equals("HOT FIXE PROD") || entryGlob.getValue().get("type").equals("ACTION A CHAUD PROD")) && entryGlob.getValue().get("priority").equals("OBJET LIVREE")) {
                        entryGlob.getValue().put("etat", "OV_HF_QUALIFIEE");
                    } else if ((entryGlob.getValue().get("type").equals("HOT FIXE PROD") || entryGlob.getValue().get("type").equals("ACTION A CHAUD PROD")) && entryGlob.getValue().get("priority").equals("PRET POUR DEPLOIEMENT")) {
                        entryGlob.getValue().put("etat", "EXPLOITATION_HF_NON_DEPLOYEE");
                    } else if ((entryGlob.getValue().get("type").equals("HOT FIXE PROD") || entryGlob.getValue().get("type").equals("ACTION A CHAUD PROD")) && entryGlob.getValue().get("priority").equals("DEPLOYEE")) {
                        entryGlob.getValue().put("etat", "EXPLOITATION_HF_DEPLOYEE");
                    }
                    if (mapTicketsDateDeploiement.containsKey(entryGlob.getKey())) {
                        entryGlob.getValue().put("date_deploiement", mapTicketsDateDeploiement.get(entryGlob.getKey()).get("time"));
                        //Tools.showConsolLog(entryGlob.getKey() + " : " + mapTicketsDateDeploiement.get(entryGlob.getKey()));
                    } else {
                        entryGlob.getValue().put("date_deploiement", "dd-MM-yyyy HH:mm");
                    }
                    if (mapTicketsDateEnvoiPourDeploiement.containsKey(entryGlob.getKey())) {
                        entryGlob.getValue().put("date_envoi_pour_deploiement", mapTicketsDateEnvoiPourDeploiement.get(entryGlob.getKey()).get("time"));
                        //Tools.showConsolLog(entryGlob.getKey() + " : " + mapTicketsDateDeploiement.get(entryGlob.getKey()));
                    } else {
                        entryGlob.getValue().put("date_envoi_pour_deploiement", "dd-MM-yyyy HH:mm");
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapTickets;
    }

    public Map<String, Map<String, String>> getTicketsHotfixAHarmoniserUpgrade(int... iterations) {
        Map<String, Map<String, String>> mapTickets = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> mapTicketsDateDeploiement = new LinkedHashMap<String, Map<String, String>>();
        Map<String, Map<String, String>> mapTicketsDateEnvoiPourDeploiement = new LinkedHashMap<String, Map<String, String>>();
        List<Integer> listeIdTickets = new ArrayList<Integer>();
        String pu = Configuration.puLivraisons;
        try {
            List<TicketCustom> listeTicketCustomUpgrade0 = AppelRequetes.getTicketCustomListOfNamedQuery(pu, Configuration.tracLivraisons, "TicketCustom.findAllHotfixAHarmoniserUpgrade0");
            if (!listeTicketCustomUpgrade0.isEmpty()) {
                for (TicketCustom ticketCustom : listeTicketCustomUpgrade0) {
                    listeIdTickets.add(ticketCustom.getTicket());
                }
                //Getting Custom Fields for parentHF
                String[] cles = new String[]{"ticket_origine", "inter_cr", "inter_cp", "projet", "contenu_des_livrables", "ticket_appl_prod", "biatref", "harm_upgrade"};
                Map<Integer, Map<String, Object>> ticketCustomLivraisonsMap = AppelRequetes.getTicketCustomByTicketIdAndNames(listeIdTickets, Configuration.puLivraisons, Configuration.tracLivraisons, cles);

                for (Map.Entry<Integer, Map<String, Object>> ticketEntry : ticketCustomLivraisonsMap.entrySet()) {
                    String ref = ticketEntry.getValue().get("biatref").toString();
                    if (!ref.equals("1")) {
                        listeIdTickets.remove(ticketEntry.getKey());
                    }
                }
                if (!listeIdTickets.isEmpty()) {
                    List<TicketCustom> listeTicketCustomHarm = AppelRequetes.getHotfixAharmoniserUpgrade(listeIdTickets);

                    for (TicketCustom tick : listeTicketCustomHarm) {
                        if (mapTickets.containsKey(tick.getTicket().toString())) {
                            mapTickets.get(tick.getTicket().toString()).put(tick.getName(), tick.getValue());
                        } else {
                            Map<String, String> mapAux = new HashMap<String, String>();
                            mapAux.put(tick.getName(), tick.getValue());
                            mapAux.put("type", tick.getTicketPointer().getType());
                            mapAux.put("priority", tick.getTicketPointer().getPriority());
                            mapAux.put("summary", tick.getTicketPointer().getSummary());
                            mapAux.put("milestone", tick.getTicketPointer().getMilestone());
                            mapAux.put("owner", tick.getTicketPointer().getOwner());
                            mapAux.put("reporter", tick.getTicketPointer().getReporter());
                            mapTickets.put(tick.getTicket().toString(), mapAux);
                            listeIdTickets.add(tick.getTicket());
                        }
                    }
                    //Tools.showConsolLog(mapTickets.toString());
                    mapTicketsDateDeploiement = getDeployementDate(pu, listeIdTickets, "DEPLOYEE");
                    mapTicketsDateEnvoiPourDeploiement = getDeployementDate(pu, listeIdTickets, "PRET POUR DEPLOIEMENT");


                    Iterator<Map.Entry<String, Map<String, String>>> iter = mapTickets.entrySet().iterator();
                    while (iter.hasNext()) {
                        Map.Entry<String, Map<String, String>> entryGlob = iter.next();
                        if (!entryGlob.getValue().get("biatref").equals("1")) {
                            iter.remove();
                        }
                        if (entryGlob.getValue().get("nature_liv") == null) {
                            iter.remove();
                        } else if (entryGlob.getValue().size() < 5) {
                            //supprimer les anciens tickets mal configurés
                            iter.remove();
                        } else if (entryGlob.getValue().get("nature_liv").equals("HARMONISATION_C.RELEASE") || entryGlob.getValue().get("nature_liv").equals("HARMONISATION_C.PROJET") || entryGlob.getValue().get("nature_liv").equals("HARMONISATION_C.UPGRADE")) {
                            //supprimer les tickets d'harmonisation
                            iter.remove();
                        } else {
                            if ((entryGlob.getValue().get("type").equals("HOT FIXE PROD") || entryGlob.getValue().get("type").equals("ACTION A CHAUD PROD")) && entryGlob.getValue().get("priority").equals("DEPLOYEE")) {
                                entryGlob.getValue().put("etat", "HARMONISATION_UPGRADE");
                            }
                            if (mapTicketsDateDeploiement.containsKey(entryGlob.getKey())) {
                                entryGlob.getValue().put("date_deploiement", mapTicketsDateDeploiement.get(entryGlob.getKey()).get("time"));
                                //Tools.showConsolLog(entryGlob.getKey() + " : " + mapTicketsDateDeploiement.get(entryGlob.getKey()));
                            } else {
                                entryGlob.getValue().put("date_deploiement", "dd-MM-yyyy HH:mm");
                            }
                            if (mapTicketsDateEnvoiPourDeploiement.containsKey(entryGlob.getKey())) {
                                entryGlob.getValue().put("date_envoi_pour_deploiement", mapTicketsDateEnvoiPourDeploiement.get(entryGlob.getKey()).get("time"));
                                //Tools.showConsolLog(entryGlob.getKey() + " : " + mapTicketsDateDeploiement.get(entryGlob.getKey()));
                            } else {
                                entryGlob.getValue().put("date_envoi_pour_deploiement", "dd-MM-yyyy HH:mm");
                            }
                        }
                    }
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapTickets;
    }

    public Map<String, Map<String, String>> getDeployementDate(String pu, List<Integer> listeIdTickets, String newValue, int... iterations) {
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("Africa/Tunis"));
        Map<String, Map<String, String>> mapTickets = new LinkedHashMap<String, Map<String, String>>();
        List<TicketChange> listeTicketChange = null;
        try {
            if (listeIdTickets.size() > 0) {
                listeTicketChange = AppelRequetes.getDeployementDateRequest(pu, listeIdTickets, newValue);
                String ticket = null;
                Long time = null;
                String responsable = null;
                long longDate = 0;
                boolean putt = false;
                for (TicketChange ticketChange : listeTicketChange) {
                    ticket = ticketChange.getTicket().toString();
                    time = ticketChange.getTime();
                    responsable = ticketChange.getAuthor();
                    if (mapTickets.containsKey(ticket)) {
                        if (Long.parseLong(mapTickets.get(ticket).get("time")) < time) {
                            putt = true;
                        }
                    } else {
                        putt = true;
                    }
                    if (putt == true) {
                        Map<String, String> mapAux = new HashMap<String, String>();
                        mapAux.put("time", time.toString().substring(0, 13));
                        mapAux.put("responsable", responsable);
                        mapTickets.put(ticket, mapAux);
                    }
                }
                for (Map.Entry<String, Map<String, String>> entry : mapTickets.entrySet()) {
                    longDate = Long.parseLong(entry.getValue().get("time"));
                    String newZealandTime = formatter.format(new Date(longDate));
                    mapTickets.get(entry.getKey()).put("time", newZealandTime);
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return mapTickets;
    }

    public Map<String, String[]> traiterLivrable(Livraison livraison, String acteur, String connectedUser, String[] tabEnvNameDestinationDeploiementLivraison, String environnementSourceName, boolean addTafTireToPack) {
        boolean actionManuelleNontraitable = false;
        boolean problemeExecution = false;
        boolean livraisonsSelfServiceBloqueesEtActionManuelleExiste = false;
        Map<String, String[]> resultMap = new HashMap<>();
        String[] envDepOkArray = tabEnvNameDestinationDeploiementLivraison;
        List<Object> livrables = livraison.getLivrables();
        String titreProb = "";
        String corpsProb = "";
        String resultatOK = "";
        String resultatKO = "";
        String resultatHtml = "";

        if (livrables == null) {
            if (Configuration.etatCircuitMap.get("LIVRAISON_SELF_SERVICE").trim().equals("OFF")) {
                //Le bouton marche arret de livraisons self service est désactivé
                if (livrables == null) {
                    String selectedMnemonic = livraison.getCompanyDeploiement();
                    String packName = livraison.getNomPack();
                    String nbrIter = Integer.toString(livraison.getNombreIterations());
                    String objetsT24 = livraison.getListeObjets();
                    String warningsT24 = "";
                    if (selectedMnemonic != null && packName != null && nbrIter != null && objetsT24 != null) {
                        livrables = new ArrayList<>();
                        T24 t24 = new T24(selectedMnemonic, packName, nbrIter, objetsT24, warningsT24);
                        livrables.add(t24);
                    }
                }
                if (!livraison.getContenuLivrables().equals("OBJETS T24")) {
                    livraisonsSelfServiceBloqueesEtActionManuelleExiste = true;
                }
            }
        }
        //il ne faut pas faire else car livrables peut etre remplie
        if (livrables != null) {
            for (Object livDto : livrables) {
                Map<String, String[]> auxmap = null;
                String msgErreur = "";
                String remarque = "";
                if (livraisonsSelfServiceBloqueesEtActionManuelleExiste == true) {
                    msgErreur = "<br><div class='errorBold'>Le process de deploiement automatique a ete interrompu a cause de:<li>Le process Livraison Self Service est desactive.</li><li>Le contenu des livrables est différent de 'Objets T24'.</li><br>A effectuer l action manuelle automatiquement</div>";
                }
                if (livDto instanceof T24) {
                    T24 liv = (T24) livDto;
                    if (addTafTireToPack) {
                        liv.setPackName("TAF-" + liv.getPackName());
                    }
                    auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison, environnementSourceName);
                } else {
                    if (livDto instanceof RoutineT24) {
                        remarque += "<span class='hiddenSpan'>POUR VERIFICATION OV</span>";
                        RoutineT24 liv = (RoutineT24) livDto;
                        auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison);
                        if (auxmap.get("RESULTAT")[1].length() > 0) {
                            problemeExecution = true;
                            msgErreur = "<br><div class='errorBold'>Le process de deploiement automatique a ete interrompu a cause du probleme d'execution de la routine!</div>";
                        }
                    } else if (livDto instanceof ServiceT24 && acteur.equals("OV")) {
                        ServiceT24 liv = (ServiceT24) livDto;
                        auxmap = liv.traiter();
                    } else if (livDto instanceof ExecutionProgrammeJSH) {
                        remarque += "<span class='hiddenSpan'>POUR VERIFICATION OV</span>";
                        ExecutionProgrammeJSH liv = (ExecutionProgrammeJSH) livDto;
                        auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison);
                    } else if (livDto instanceof CreationDossiers) {
                        remarque += "<span class='hiddenSpan'>POUR VERIFICATION OV</span>";
                        CreationDossiers liv = (CreationDossiers) livDto;
                        auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison);
                    } else if (livDto instanceof TransfertFichier) {
                        remarque += "<span class='hiddenSpan'>POUR VERIFICATION OV</span>";
                        TransfertFichier liv = (TransfertFichier) livDto;
                        auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison);
                    } else if (livDto instanceof RebuildSystem) {
                        remarque += "<span class='hiddenSpan'>POUR VERIFICATION OV</span>";
                        RebuildSystem liv = (RebuildSystem) livDto;
                        auxmap = liv.traiter(connectedUser, tabEnvNameDestinationDeploiementLivraison);
                    } else {
                        actionManuelleNontraitable = true;
                        msgErreur = "<br><div class='errorBold'>Le process de deploiement automatique a ete interrompu a cause de l'action manuelle non encore automatisee!</div>";
                        auxmap = new HashMap<>();
                        auxmap.put("PROBLEME", new String[]{"", ""});
                        auxmap.put("RESULTAT", new String[]{"", ""});
                        auxmap.put("RESULTAT_HTML_COMPLET", new String[]{livDto.toString().replace("<span class='errorBold'>", "<span class='errorBold'> (A traiter manuellement)")});
                        auxmap.put("ENV_DEP_OK", new String[0]);
                    }
                }
                titreProb += auxmap.get("PROBLEME")[0];
                corpsProb += auxmap.get("PROBLEME")[1];
                resultatOK += auxmap.get("RESULTAT")[0];
                resultatKO += auxmap.get("RESULTAT")[1];
                resultatHtml += auxmap.get("RESULTAT_HTML_COMPLET")[0] + msgErreur + remarque;
                envDepOkArray = intersectionArrays(envDepOkArray, auxmap.get("ENV_DEP_OK"));
                if (actionManuelleNontraitable || problemeExecution || livraisonsSelfServiceBloqueesEtActionManuelleExiste) {
                    break;
                }
            }
        } else {
            titreProb = "LIVRABLE NULL: ";
            corpsProb = "Le livrable de la Livraison #" + livraison.getNumeroLivraison() + " est null car elle a été livrée par le développeur manuellement, prière de la traiter manuellement.";
        }
        resultMap.put("PROBLEME", new String[]{titreProb, corpsProb});
        resultMap.put("RESULTAT", new String[]{resultatOK, resultatKO});
        resultMap.put("RESULTAT_HTML_COMPLET", new String[]{"\n\n{{{\n#!html\n" + resultatHtml + "\n}}}"});
        resultMap.put("ENV_DEP_OK", envDepOkArray);


        return resultMap;
    }

    private String[] intersectionArrays(String[] array1, String[] array2) {
        Set<String> s1 = new HashSet<String>(Arrays.asList(array1));
        Set<String> s2 = new HashSet<String>(Arrays.asList(array2));
        s1.retainAll(s2);
        String[] result = s1.toArray(new String[s1.size()]);
        return result;
    }

    public String versionning(Livraison livraison, String circuit, String niveauProjet, String numTicket, String connectedUser, String messageTrac) {
        String resultatVersionning = "";
        List<Object> livrables = livraison.getLivrables();
        List<T24> t24List = new ArrayList<>();
        if (livrables != null) {
            for (Object livDto : livrables) {
                if (livDto instanceof T24) {
                    T24 liv = (T24) livDto;
                    if (messageTrac.contains(liv.getPackName())) {
                        t24List.add(liv);
                    }
                }
            }
        }
        if (!t24List.isEmpty()) {
            resultatVersionning = callVersionning(t24List, circuit, niveauProjet, numTicket, connectedUser);
        }
        return resultatVersionning;
    }

    public String callVersionning(List<T24> t24List, String circuit, String niveauProjet, String numTicket, String connectedUser) {
        StringBuilder resultat = new StringBuilder();
        resultat.append("	<div class='center'>");
        resultat.append("		<table  class='tablePrincipale'>");
        resultat.append("			<tr>");
        resultat.append("				<td class='conteneurWrapper'>");
        resultat.append("					<div id='wrapper'>");
        resultat.append("						<div class='accordionButton on'>VERSIONNING<span class='errorBold'></span></div>");
        resultat.append("						<div class='accordionContent' style='display: block;'>");
        for (T24 t24 : t24List) {
            String resultatVersionning = t24.versionning(circuit, niveauProjet, numTicket, connectedUser);
            resultat.append("							<fieldset class='fieldSetResultatDeploiement'>");
            resultat.append("								<legend class='legend1'>").append("TAF-").append(t24.getPackName()).append("</legend>");
            resultat.append("								<p class='contenu'>");
            resultat.append("									").append(resultatVersionning.replaceAll("\n", "<br>"));
            resultat.append("								</p>");
            resultat.append("							</fieldset>");
        }
        resultat.append("						</div>");
        resultat.append("					</div>");
        resultat.append("				</td>");
        resultat.append("			</tr>");
        resultat.append("		</table>");
        resultat.append("	</div>");
        return resultat.toString();
    }
}