/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsActions;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.CoupleDTO;
import dto.livraison.*;
import entitiesMysql.Livraison;
import entitiesTrac.Ticket;
import java.io.File;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import java.util.*;
import org.apache.commons.io.FileUtils;
import strutsForms.TraitementLivraisonAutoForm;
import tools.AttachmentsTools;
import tools.Configuration;
import tools.DataBaseTools;
import tools.ManipulationObjectsTool;
import tools.Tools;
import org.apache.struts.upload.FormFile;
import static tools.Configuration.mapCorrespondanceTypesLivrable;

/**
 *
 * @author 04486
 */
public class TraitementLivraisonAutoAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, InterruptedException {

        String connectedUser = null;
        TraitementLivraisonAutoForm inF = null;
        try {
            inF = (TraitementLivraisonAutoForm) form;
            if (inF == null || inF.getNumLivraison() == null || inF.getTypeDuBloc() == null || inF.getCircuit() == null || inF.getPhase() == null) {
                return mapping.findForward("login");
            }
            connectedUser = Tools.getConnectedLogin();
            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, true);

            /*
             if (true) {
             return mapping.findForward("test");
             }
             */
            //String[] packsNamesList = new String[]{"TAF-" + livraison.getPackName()};
            //spécifie le type de livrable    

            String circuit = inF.getCircuit();
            String typeTicket = inF.getPhase();
            int numTicketLivraison = Integer.parseInt(inF.getNumLivraison());
            Integer numeroAnomalie = Integer.parseInt(inF.getNumAnomalie().trim());
            //ticket anomalie
            DataBaseTools dbTools = new DataBaseTools(Configuration.puAnomalies);
            Ticket ticketAnomalies = DataBaseTracGenericRequests.getTicketById(dbTools, numeroAnomalie);
            dbTools.closeRessources();
            //ticket livraison
            dbTools = new DataBaseTools(Configuration.puLivraisons);
            Ticket ticketLivraison = DataBaseTracGenericRequests.getTicketById(dbTools, numTicketLivraison);
            dbTools.closeRessources();
            String namedQuery = "TicketCustom.findByTicketAndName";
            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("ticket", ticketLivraison.getId());
            paramMap.put("name", "contenu_des_livrables");
            String contenudesLivrables = "OBJETS T24";
            /*
             try {
             List<TicketCustom> tcList = new DataBaseTracGenericRequests<TicketCustom>().getList_TYPE_OfnamedQuery(Configuration.livraisonsPU, namedQuery, paramMap);
             contenudesLivrables = tcList.get(0).getValue();
             } catch (Exception ex) {
             }
             */
            //
            String[] typeDuBloc = inF.getTypeDuBloc();
            String[] count = inF.getCount();
            //OBJETS T24
            String[] selectedMnemonic = inF.getSelectedMnemonic();
            String[] packName = inF.getPackName();
            String[] nbrIter = inF.getNbrIter();
            String[] objetsT24 = inF.getOBJETST24();
            String[] warningsT24 = inF.getWarningT24();
            //EXECUTION ROUTINE T24
            String[] executionRoutineT24 = inF.getEXECUTIONROUTINET24();
            //DOLLAR U
            String[] serviceT24service = inF.getDOLLARUservice();
            String[] serviceT24ordre = inF.getDOLLARUordre();
            String[] serviceT24stage = inF.getDOLLARUstage();
            //CREATION INDEXES
            String[] CREATIONINDEXES = inF.getCREATIONINDEXES();
            //REVERSE OBJETS T24
            String[] REVERSEOBJETST24 = inF.getREVERSEOBJETST24();
            //SUPRESSION OBJETS T24
            String[] SUPRESSIONOBJETST24 = inF.getSUPRESSIONOBJETST24();
            //REBUILD SYSTEM
            String[] REBUILDSYSTEM = inF.getREBUILDSYSTEM();
            //EXECUTION PROGRAMME JSH
            String[] EXECUTIONPROGRAMMEJSH = inF.getEXECUTIONPROGRAMMEJSH();

            //CREATION DOSSIERS
            String[] creationDossierSchemin = inF.getCREATIONDOSSIERSchemin();
            String[] creationDossierSdroitR = inF.getCREATIONDOSSIERSdroitR();
            String[] creationDossierSdroitW = inF.getCREATIONDOSSIERSdroitW();
            String[] creationDossierSdroitX = inF.getCREATIONDOSSIERSdroitX();
            //BROWSER IB
            String[] browserIBexport = inF.getBROWSERIBexport();
            String[] browserIBtag = inF.getBROWSERIBtag();
            //BROWSER T24
            String[] browserT24Export = inF.getBROWSERT24export();
            String[] browserT24tag = inF.getBROWSERT24tag();
            //STREAMSERV TRANSACTIONNEL
            String[] streamServeTransactionnelExport = inF.getSTREAMSERVTRANSACTIONNELexport();
            String[] streamServeTransactionnelprojetSS = inF.getSTREAMSERVTRANSACTIONNELprojetSS();
            String[] streamServeTransactionnelLienTagSS = inF.getSTREAMSERVTRANSACTIONNELlienTagSS();
            //STREAMSERV BATCH
            String[] streamServeBatchExport = inF.getSTREAMSERVBATCHexport();
            String[] streamServeBatchProjetSS = inF.getSTREAMSERVBATCHprojetSS();
            String[] streamServeBatchLienTagSS = inF.getSTREAMSERVBATCHlienTagSS();
            //CREATION COMPTES;
            List<FormFile> creationComptes = inF.getCREATIONCOMPTES();
            //TRANSFERT FICHIERS;
            List<FormFile> transfertFichierFichier = inF.getTRANSFERTFICHIERSfichier();
            String[] transfertFichierChemin = inF.getTRANSFERTFICHIERSchemin();
            String[] transfertFichierModeTransfert = inF.getTRANSFERTFICHIERSModeTransfert();
            //AUTRE LIVRABLE
            String[] autreLivrable = inF.getAUTRELIVRABLE();


            String devServerName = "";
            if (circuit.equals("RELEASE")) {
                devServerName = "DEVR";
            } else if (circuit.equals("PROJET")) {
                devServerName = "DEV2";
            } else if (circuit.equals("HOTFIX")) {
                devServerName = "DEVH";
            }
            List<Object> livraisonList = new ArrayList<>();
            int nbrRoutine = 0;
            int nbrT24 = 0;
            int nbrServicesT24 = 0;
            int nbrIndex = 0;
            int nbrReverseObj = 0;
            int nbrSuppObj = 0;
            int nbrRebuildSystem = 0;
            int nbrExecJsh = 0;

            int nbrCreationDossiers = 0;
            int nbrBrowserIb = 0;
            int nbrBrowserT24 = 0;
            int nbrStreamServeTransactionnel = 0;
            int nbrStreamServeBatch = 0;
            int nbrComptes = 0;
            int nbrAutreLivrable = 0;
            int nbrTransfertFichiers = 0;

            for (int i = 0; i < typeDuBloc.length; i++) {
                String type = typeDuBloc[i];
                int nbr = Integer.parseInt(count[i]);
                if (type.equals("OBJETS T24")) {
                    T24 t24 = new T24();
                    t24.setSelectedMnemonic(selectedMnemonic[nbrT24]);
                    t24.setPackName(packName[nbrT24]);
                    t24.setNbrIter(nbrIter[nbrT24]);
                    t24.setObjetsT24(objetsT24[nbrT24]);
                    t24.setWarningsT24(warningsT24[nbrT24]);
                    livraisonList.add(t24);
                    nbrT24++;
                } else if (type.equals("EXECUTION ROUTINE T24")) {
                    for (int j = nbrRoutine; j < nbrRoutine + nbr; j++) {
                        RoutineT24 routine = new RoutineT24(executionRoutineT24[j]);
                        livraisonList.add(routine);
                    }
                    nbrRoutine += nbr;
                } else if (type.equals("DOLLAR U")) {
                    ServiceT24 service = new ServiceT24();
                    service.setServiceT24service(serviceT24service[nbrServicesT24]);
                    service.setServiceT24ordre(serviceT24ordre[nbrServicesT24]);
                    service.setServiceT24stage(serviceT24stage[nbrServicesT24]);
                    livraisonList.add(service);
                    nbrServicesT24++;
                } else if (type.equals("CREATION INDEXES")) {
                    CreationIndex ci = new CreationIndex();
                    ci.setCreationIndexes(CREATIONINDEXES[nbrIndex]);
                    livraisonList.add(ci);
                    nbrIndex++;
                } else if (type.equals("REVERSE OBJETS T24")) {
                    ReversObjT24 rev = new ReversObjT24();
                    rev.setReverseObjetsT24(REVERSEOBJETST24[nbrReverseObj]);
                    livraisonList.add(rev);
                    nbrReverseObj++;
                } else if (type.equals("SUPRESSION OBJETS T24")) {
                    SuppressionObjT24 supObj = new SuppressionObjT24();
                    supObj.setSupressionObjetsT24(SUPRESSIONOBJETST24[nbrSuppObj]);
                    livraisonList.add(supObj);
                    nbrSuppObj++;
                } else if (type.equals("REBUILD SYSTEM")) {
                    RebuildSystem rs = new RebuildSystem();
                    rs.setRebuildSystem(REBUILDSYSTEM[nbrRebuildSystem]);
                    livraisonList.add(rs);
                    nbrRebuildSystem++;
                } else if (type.equals("EXECUTION PROGRAMME JSH")) {
                    for (int j = nbrExecJsh; j < nbrExecJsh + nbr; j++) {
                        ExecutionProgrammeJSH jsh = new ExecutionProgrammeJSH(EXECUTIONPROGRAMMEJSH[j]);
                        livraisonList.add(jsh);
                    }
                    nbrExecJsh += nbr;
                } else if (type.equals("CREATION DOSSIERS")) {
                    CreationDossiers cd = new CreationDossiers();
                    String chemin = creationDossierSchemin[nbrCreationDossiers].trim();
                    if (chemin.startsWith("/")) {
                        chemin = chemin.replaceFirst("/", "");
                    }
                    cd.setCreationDossiersChemin(chemin);
                    cd.setCreationDossiersDroitR(creationDossierSdroitR[nbrCreationDossiers]);
                    cd.setCreationDossiersDroitW(creationDossierSdroitW[nbrCreationDossiers]);
                    cd.setCreationDossiersDroitX(creationDossierSdroitX[nbrCreationDossiers]);
                    livraisonList.add(cd);
                    nbrCreationDossiers++;
                } else if (type.equals("BROWSER IB")) {
                    BrowserIB ib = new BrowserIB();
                    ib.setBrowserIBexport(browserIBexport[nbrBrowserIb]);
                    ib.setBrowserIBtag(browserIBtag[nbrBrowserIb]);
                    livraisonList.add(ib);
                    nbrBrowserIb++;
                } else if (type.equals("BROWSER T24")) {
                    BrowserT24 bT24 = new BrowserT24();
                    bT24.setBrowserT24export(browserT24Export[nbrBrowserT24]);
                    bT24.setBrowserT24tag(browserT24tag[nbrBrowserT24]);
                    livraisonList.add(bT24);
                    nbrBrowserT24++;
                } else if (type.equals("STREAMSERV TRANSACTIONNEL")) {
                    StreamServeTransactionnel sst = new StreamServeTransactionnel();
                    sst.setStreamServeTransactionnelExport(streamServeTransactionnelExport[nbrStreamServeTransactionnel]);
                    sst.setStreamServeTransactionnelprojetSS(streamServeTransactionnelprojetSS[nbrStreamServeTransactionnel]);
                    sst.setStreamServeTransactionnelLienTagSS(streamServeTransactionnelLienTagSS[nbrStreamServeTransactionnel]);
                    livraisonList.add(sst);
                    nbrStreamServeTransactionnel++;
                } else if (type.equals("STREAMSERV BATCH")) {
                    StreamServeBatch ssb = new StreamServeBatch();
                    ssb.setStreamServeBatchExport(streamServeBatchExport[nbrStreamServeBatch]);
                    ssb.setStreamServeBatchprojetSS(streamServeBatchProjetSS[nbrStreamServeBatch]);
                    ssb.setStreamServeBatchlienTagSS(streamServeBatchLienTagSS[nbrStreamServeBatch]);
                    livraisonList.add(ssb);
                    nbrStreamServeBatch++;
                } else if (type.equals("CREATION COMPTES")) {
                    CreationCompte cc = new CreationCompte();
                    FormFile fileForm = creationComptes.get(nbrComptes);
                    String destinationFolderAttachments = Configuration.parametresList.get("cheminAttachmentsT24");
                    File file = AttachmentsTools.createFileFromFormFile(fileForm.getFileName(), fileForm.getFileData(), destinationFolderAttachments, inF.getNumLivraison());
                    cc.setCREATIONCOMPTES(file);
                    cc.setNumTicket(numTicketLivraison);
                    livraisonList.add(cc);
                    AttachmentsTools.createAttachment(Configuration.puLivraisons, fileForm.getFileName(), fileForm.getFileData(), inF.getNumLivraison(), destinationFolderAttachments, connectedUser);
                    nbrComptes++;
                } else if (type.equals("TRANSFERT FICHIERS")) {
                    TransfertFichier tf = new TransfertFichier();
                    String destinationFolderAttachments = Configuration.parametresList.get("cheminAttachmentsT24");
                    FormFile fileForm = transfertFichierFichier.get(nbrTransfertFichiers);
                    File file = AttachmentsTools.createFileFromFormFile(fileForm.getFileName(), fileForm.getFileData(), destinationFolderAttachments, inF.getNumLivraison());
                    tf.setTRANSFERTFICHIERSfichier(file);
                    tf.setTRANSFERTFICHIERSchemin(transfertFichierChemin[nbrTransfertFichiers]);
                    tf.setTRANSFERTFICHIERSModeTransfert(transfertFichierModeTransfert[nbrTransfertFichiers]);
                    tf.setNumTicket(numTicketLivraison);
                    livraisonList.add(tf);
                    AttachmentsTools.createAttachment(Configuration.puLivraisons, fileForm.getFileName(), fileForm.getFileData(), inF.getNumLivraison(), destinationFolderAttachments, connectedUser);
                    nbrTransfertFichiers++;
                } else if (type.equals("AUTRE LIVRABLE")) {
                    AutreLivrable cc = new AutreLivrable();
                    cc.setAutreLivrable(autreLivrable[nbrAutreLivrable]);
                    livraisonList.add(cc);
                    nbrAutreLivrable++;
                }
            }

            //Message TRAC Hotfix pour Fethi Cheikh
            String messageTracExploitation = "A déployer sur '''PROD''' : \n{{{\n#!html\n ";
            for (Object liv : livraisonList) {
                if (liv instanceof T24) {
                    T24 t24 = (T24) liv;
                    messageTracExploitation += t24.toDo();
                    if (t24.getWarningsT24() != null && t24.getWarningsT24().trim().length() > 0) {
                        messageTracExploitation += t24.getAlert();
                    }
                } else {
                    messageTracExploitation += liv.toString();
                    if (contenudesLivrables.equals("OBJETS T24")) {
                        String classname = liv.getClass().getSimpleName();
                        if (mapCorrespondanceTypesLivrable.containsKey(classname)) {
                            classname = mapCorrespondanceTypesLivrable.get(classname);
                        }
                        contenudesLivrables = classname;
                    }
                }
            }
            messageTracExploitation += " \n }}}\n";
            //Message TRAC
            String messageTracTicket = "A livrer depuis l'environnement '''" + devServerName + ":'''\n{{{\n#!html\n ";
            for (Object liv : livraisonList) {
                messageTracTicket += liv.toString();
            }
            messageTracTicket += " \n }}}\n";
            List<CoupleDTO> customFieldList = new ArrayList<>();
            customFieldList.add(new CoupleDTO("contenu_des_livrables", contenudesLivrables));
            DataBaseTracGenericRequests.updateTicketTracGeneral(Configuration.puLivraisons, numTicketLivraison, connectedUser, messageTracTicket, "raafet dormok", "OBJET LIVREE", null, null, customFieldList);
            //Insertion dans la table livraison
            dbTools = new DataBaseTools(Configuration.puOvTools);
            Livraison livraison = dbTools.em.find(Livraison.class, numTicketLivraison);
            if (livraison == null) {
                livraison = new Livraison(numTicketLivraison);
            }
            byte[] serializedObject = ManipulationObjectsTool.serialisation(livraisonList);
            livraison.setNumeroLivraison(numTicketLivraison);
            livraison.setNumeroAnomalie(numeroAnomalie);
            livraison.setOwner(ticketAnomalies.getOwner());
            livraison.setReporter(ticketAnomalies.getReporter());
            livraison.setType(typeTicket);
            livraison.setLivrables(serializedObject);
            livraison.setContenuLivrables(contenudesLivrables);
            livraison.setMessageTrac(messageTracExploitation);
            dbTools.update(livraison);
            dbTools.closeRessources();

            servlets.AfficherMessageEtatAvancement.executionEnCours.put(connectedUser, false);
            servlets.AfficherMessageEtatAvancement.setLogmessage("", connectedUser);

            response.setContentType("text/text;charset=utf-8");
            response.setHeader("cache-control", "no-cache");

        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException("Livraison: #" + inF.getNumLivraison() + " ___ connectedUser:" + connectedUser + "|||" + tools.Tools.getStackTrace(exep));
        }
        return mapping.findForward("SelfServiceLivraisonCDD");
    }
}
