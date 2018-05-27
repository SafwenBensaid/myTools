/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package strutsForms;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import java.io.File;
import java.util.List;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author 04486
 */
public class TraitementLivraisonAutoForm extends org.apache.struts.action.ActionForm {

    public TraitementLivraisonAutoForm() {
        super();
        // TODO Auto-generated constructor stub
    }
    //spécifie le type de livrable
    private String[] typeDuBloc;
    private String[] count;
    //OBJETS T24
    private String[] selectedMnemonic;
    private String[] packName;
    private String[] nbrIter;
    private String[] OBJETST24;
    private String[] warningT24;
    //EXECUTION ROUTINE T24
    private String[] EXECUTIONROUTINET24;
    //DOLLAR U
    private String[] DOLLARUservice;
    private String[] DOLLARUordre;
    private String[] DOLLARUstage;
    //CREATION INDEXES
    private String[] CREATIONINDEXES;
    //REVERSE OBJETS T24
    private String[] REVERSEOBJETST24;
    //SUPRESSION OBJETS T24
    private String[] SUPRESSIONOBJETST24;
    //REBUILD SYSTEM
    private String[] REBUILDSYSTEM;
    //EXECUTION PROGRAMME JSH
    private String[] EXECUTIONPROGRAMMEJSH;
    //CREATION DOSSIERS
    private String[] CREATIONDOSSIERSchemin;
    private String[] CREATIONDOSSIERSdroitR;
    private String[] CREATIONDOSSIERSdroitW;
    private String[] CREATIONDOSSIERSdroitX;
    //BROWSER IB
    private String[] BROWSERIBexport;
    private String[] BROWSERIBtag;
    //BROWSER T24
    private String[] BROWSERT24export;
    private String[] BROWSERT24tag;
    //STREAMSERV TRANSACTIONNEL
    private String[] STREAMSERVTRANSACTIONNELexport;
    private String[] STREAMSERVTRANSACTIONNELprojetSS;
    private String[] STREAMSERVTRANSACTIONNELlienTagSS;
    //STREAMSERV BATCH
    private String[] STREAMSERVBATCHexport;
    private String[] STREAMSERVBATCHprojetSS;
    private String[] STREAMSERVBATCHlienTagSS;
    //CREATION COMPTES
    private List<FormFile> CREATIONCOMPTES;
    //TRANSFERT FICHIERS
    private List<FormFile> TRANSFERTFICHIERSfichier;
    private String[] TRANSFERTFICHIERSchemin;
    private String[] TRANSFERTFICHIERSModeTransfert;
    //AUTRE LIVRABLE
    private String[] AUTRELIVRABLE;
    //ticket details
    private String circuit;
    private String numLivraison;
    private String contenuDesLivrables;
    private String numAnomalie;
    private String phase;
    private String priority;
    private String milestone;
    private String component;
    private String natureTraitement;
    private String natureLivraison;
    private String niveauProjet;

    //A ajouter les autres actions manuelles
    public String[] getTypeDuBloc() {
        return typeDuBloc;
    }

    public void setTypeDuBloc(String[] typeDuBloc) {
        this.typeDuBloc = typeDuBloc;
    }

    public String[] getCount() {
        return count;
    }

    public void setCount(String[] count) {
        this.count = count;
    }

    public String[] getSelectedMnemonic() {
        return selectedMnemonic;
    }

    public void setSelectedMnemonic(String[] selectedMnemonic) {
        this.selectedMnemonic = selectedMnemonic;
    }

    public String[] getPackName() {
        return packName;
    }

    public void setPackName(String[] packName) {
        this.packName = packName;
    }

    public String[] getNbrIter() {
        return nbrIter;
    }

    public void setNbrIter(String[] nbrIter) {
        this.nbrIter = nbrIter;
    }

    public String[] getOBJETST24() {
        return OBJETST24;
    }

    public void setOBJETST24(String[] OBJETST24) {
        this.OBJETST24 = OBJETST24;
    }

    public String[] getWarningT24() {
        return warningT24;
    }

    public void setWarningT24(String[] warningT24) {
        this.warningT24 = warningT24;
    }

    public String[] getEXECUTIONROUTINET24() {
        return EXECUTIONROUTINET24;
    }

    public void setEXECUTIONROUTINET24(String[] EXECUTIONROUTINET24) {
        this.EXECUTIONROUTINET24 = EXECUTIONROUTINET24;
    }

    public String[] getDOLLARUservice() {
        return DOLLARUservice;
    }

    public void setDOLLARUservice(String[] DOLLARUservice) {
        this.DOLLARUservice = DOLLARUservice;
    }

    public String[] getDOLLARUordre() {
        return DOLLARUordre;
    }

    public void setDOLLARUordre(String[] DOLLARUordre) {
        this.DOLLARUordre = DOLLARUordre;
    }

    public String[] getDOLLARUstage() {
        return DOLLARUstage;
    }

    public void setDOLLARUstage(String[] DOLLARUstage) {
        this.DOLLARUstage = DOLLARUstage;
    }

    public String[] getCREATIONINDEXES() {
        return CREATIONINDEXES;
    }

    public void setCREATIONINDEXES(String[] CREATIONINDEXES) {
        this.CREATIONINDEXES = CREATIONINDEXES;
    }

    public String[] getREVERSEOBJETST24() {
        return REVERSEOBJETST24;
    }

    public void setREVERSEOBJETST24(String[] REVERSEOBJETST24) {
        this.REVERSEOBJETST24 = REVERSEOBJETST24;
    }

    public String[] getSUPRESSIONOBJETST24() {
        return SUPRESSIONOBJETST24;
    }

    public void setSUPRESSIONOBJETST24(String[] SUPRESSIONOBJETST24) {
        this.SUPRESSIONOBJETST24 = SUPRESSIONOBJETST24;
    }

    public String[] getREBUILDSYSTEM() {
        return REBUILDSYSTEM;
    }

    public void setREBUILDSYSTEM(String[] REBUILDSYSTEM) {
        this.REBUILDSYSTEM = REBUILDSYSTEM;
    }

    public String[] getEXECUTIONPROGRAMMEJSH() {
        return EXECUTIONPROGRAMMEJSH;
    }

    public void setEXECUTIONPROGRAMMEJSH(String[] EXECUTIONPROGRAMMEJSH) {
        this.EXECUTIONPROGRAMMEJSH = EXECUTIONPROGRAMMEJSH;
    }

    public String[] getCREATIONDOSSIERSchemin() {
        return CREATIONDOSSIERSchemin;
    }

    public void setCREATIONDOSSIERSchemin(String[] CREATIONDOSSIERSchemin) {
        this.CREATIONDOSSIERSchemin = CREATIONDOSSIERSchemin;
    }

    public String[] getCREATIONDOSSIERSdroitR() {
        return CREATIONDOSSIERSdroitR;
    }

    public void setCREATIONDOSSIERSdroitR(String[] CREATIONDOSSIERSdroitR) {
        this.CREATIONDOSSIERSdroitR = CREATIONDOSSIERSdroitR;
    }

    public String[] getCREATIONDOSSIERSdroitW() {
        return CREATIONDOSSIERSdroitW;
    }

    public void setCREATIONDOSSIERSdroitW(String[] CREATIONDOSSIERSdroitW) {
        this.CREATIONDOSSIERSdroitW = CREATIONDOSSIERSdroitW;
    }

    public String[] getCREATIONDOSSIERSdroitX() {
        return CREATIONDOSSIERSdroitX;
    }

    public void setCREATIONDOSSIERSdroitX(String[] CREATIONDOSSIERSdroitX) {
        this.CREATIONDOSSIERSdroitX = CREATIONDOSSIERSdroitX;
    }

    public String[] getBROWSERIBexport() {
        return BROWSERIBexport;
    }

    public void setBROWSERIBexport(String[] BROWSERIBexport) {
        this.BROWSERIBexport = BROWSERIBexport;
    }

    public String[] getBROWSERIBtag() {
        return BROWSERIBtag;
    }

    public void setBROWSERIBtag(String[] BROWSERIBtag) {
        this.BROWSERIBtag = BROWSERIBtag;
    }

    public String[] getBROWSERT24export() {
        return BROWSERT24export;
    }

    public void setBROWSERT24export(String[] BROWSERT24export) {
        this.BROWSERT24export = BROWSERT24export;
    }

    public String[] getBROWSERT24tag() {
        return BROWSERT24tag;
    }

    public void setBROWSERT24tag(String[] BROWSERT24tag) {
        this.BROWSERT24tag = BROWSERT24tag;
    }

    public String[] getSTREAMSERVTRANSACTIONNELexport() {
        return STREAMSERVTRANSACTIONNELexport;
    }

    public void setSTREAMSERVTRANSACTIONNELexport(String[] STREAMSERVTRANSACTIONNELexport) {
        this.STREAMSERVTRANSACTIONNELexport = STREAMSERVTRANSACTIONNELexport;
    }

    public String[] getSTREAMSERVTRANSACTIONNELprojetSS() {
        return STREAMSERVTRANSACTIONNELprojetSS;
    }

    public void setSTREAMSERVTRANSACTIONNELprojetSS(String[] STREAMSERVTRANSACTIONNELprojetSS) {
        this.STREAMSERVTRANSACTIONNELprojetSS = STREAMSERVTRANSACTIONNELprojetSS;
    }

    public String[] getSTREAMSERVTRANSACTIONNELlienTagSS() {
        return STREAMSERVTRANSACTIONNELlienTagSS;
    }

    public void setSTREAMSERVTRANSACTIONNELlienTagSS(String[] STREAMSERVTRANSACTIONNELlienTagSS) {
        this.STREAMSERVTRANSACTIONNELlienTagSS = STREAMSERVTRANSACTIONNELlienTagSS;
    }

    public String[] getSTREAMSERVBATCHexport() {
        return STREAMSERVBATCHexport;
    }

    public void setSTREAMSERVBATCHexport(String[] STREAMSERVBATCHexport) {
        this.STREAMSERVBATCHexport = STREAMSERVBATCHexport;
    }

    public String[] getSTREAMSERVBATCHprojetSS() {
        return STREAMSERVBATCHprojetSS;
    }

    public void setSTREAMSERVBATCHprojetSS(String[] STREAMSERVBATCHprojetSS) {
        this.STREAMSERVBATCHprojetSS = STREAMSERVBATCHprojetSS;
    }

    public String[] getSTREAMSERVBATCHlienTagSS() {
        return STREAMSERVBATCHlienTagSS;
    }

    public void setSTREAMSERVBATCHlienTagSS(String[] STREAMSERVBATCHlienTagSS) {
        this.STREAMSERVBATCHlienTagSS = STREAMSERVBATCHlienTagSS;
    }

    public List<FormFile> getCREATIONCOMPTES() {
        return CREATIONCOMPTES;
    }

    public void setCREATIONCOMPTES(List<FormFile> CREATIONCOMPTES) {
        this.CREATIONCOMPTES = CREATIONCOMPTES;
    }

    public List<FormFile> getTRANSFERTFICHIERSfichier() {
        return TRANSFERTFICHIERSfichier;
    }

    public void setTRANSFERTFICHIERSfichier(List<FormFile> TRANSFERTFICHIERSfichier) {
        this.TRANSFERTFICHIERSfichier = TRANSFERTFICHIERSfichier;
    }

    public String[] getTRANSFERTFICHIERSchemin() {
        return TRANSFERTFICHIERSchemin;
    }

    public void setTRANSFERTFICHIERSchemin(String[] TRANSFERTFICHIERSchemin) {
        this.TRANSFERTFICHIERSchemin = TRANSFERTFICHIERSchemin;
    }

    public String[] getTRANSFERTFICHIERSModeTransfert() {
        return TRANSFERTFICHIERSModeTransfert;
    }

    public void setTRANSFERTFICHIERSModeTransfert(String[] TRANSFERTFICHIERSModeTransfert) {
        this.TRANSFERTFICHIERSModeTransfert = TRANSFERTFICHIERSModeTransfert;
    }

    public String[] getAUTRELIVRABLE() {
        return AUTRELIVRABLE;
    }

    public void setAUTRELIVRABLE(String[] AUTRELIVRABLE) {
        this.AUTRELIVRABLE = AUTRELIVRABLE;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public String getNumLivraison() {
        return numLivraison;
    }

    public void setNumLivraison(String numLivraison) {
        this.numLivraison = numLivraison;
    }

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public String getNumAnomalie() {
        return numAnomalie;
    }

    public void setNumAnomalie(String numAnomalie) {
        this.numAnomalie = numAnomalie;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMilestone() {
        return milestone;
    }

    public void setMilestone(String milestone) {
        this.milestone = milestone;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getNatureTraitement() {
        return natureTraitement;
    }

    public void setNatureTraitement(String natureTraitement) {
        this.natureTraitement = natureTraitement;
    }

    public String getNatureLivraison() {
        return natureLivraison;
    }

    public void setNatureLivraison(String natureLivraison) {
        this.natureLivraison = natureLivraison;
    }

    public String getNiveauProjet() {
        return niveauProjet;
    }

    public void setNiveauProjet(String niveauProjet) {
        this.niveauProjet = niveauProjet;
    }

    /**
     * This is the action called from the Struts framework.
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param request The HTTP Request we are processing.
     * @return
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        /*
         if (getName() == null || getName().length() < 1) {
         errors.add("Name", new ActionMessage("error.name.required"));
         // TODO: add 'error.name.required' key to your resources
         }*/
        return errors;
    }
}
