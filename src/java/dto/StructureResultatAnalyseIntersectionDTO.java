/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 04486
 */
public class StructureResultatAnalyseIntersectionDTO implements Cloneable {

    private String typeObj;
    private String nomObj;
    private String resultatEtudeIntersection;
    private List<StructureMessageLogDTO> structureMessageLog;
    private String nomProjet;
    //Pour le module Constitution Pack Multi projets
    //Ceux sont tous les projets qui contiennent cet objet
    private List<String> listeNomsProjets;
    private int lastRevision;

    public StructureResultatAnalyseIntersectionDTO(String typeObj, String nomObj) {
        this.typeObj = typeObj;
        this.nomObj = nomObj;
        this.resultatEtudeIntersection = "";
        this.structureMessageLog = new ArrayList<StructureMessageLogDTO>();
        this.nomProjet = "";
    }

    public String getTypeObj() {
        return typeObj;
    }

    public void setTypeObj(String typeObj) {
        this.typeObj = typeObj;
    }

    public String getNomObj() {
        return nomObj;
    }

    public void setNomObj(String nomObj) {
        this.nomObj = nomObj;
    }

    public String getResultatEtudeIntersection() {
        return resultatEtudeIntersection;
    }

    public void setResultatEtudeIntersection(String resultatEtudeIntersection) {
        this.resultatEtudeIntersection = resultatEtudeIntersection;
    }

    public List<StructureMessageLogDTO> getStructureMessageLog() {
        return structureMessageLog;
    }

    public void setStructureMessageLog(List<StructureMessageLogDTO> structureMessageLog) {
        this.structureMessageLog = structureMessageLog;
    }

    public String getNomProjet() {
        return nomProjet;
    }

    public void setNomProjet(String nomProjet) {
        this.nomProjet = nomProjet;
    }

    public List<String> getListeNomsProjets() {
        return listeNomsProjets;
    }

    public void setListeNomsProjets(List<String> listeNomsProjets) {
        this.listeNomsProjets = listeNomsProjets;
    }

    public int getLastRevision() {
        return lastRevision;
    }

    public void setLastRevision(int lastRevision) {
        this.lastRevision = lastRevision;
    }

    public Object clone() {
        StructureResultatAnalyseIntersectionDTO structure = null;
        try {
            // On récupère l'instance à renvoyer par l'appel de la 
            // méthode super.clone()
            structure = (StructureResultatAnalyseIntersectionDTO) super.clone();
        } catch (CloneNotSupportedException cnse) {
            // Ne devrait jamais arriver car nous implémentons 
            // l'interface Cloneable
            cnse.printStackTrace(System.err);
        }

        // On clone l'attribut de type Patronyme qui n'est pas immuable.
        structure.structureMessageLog = ((List<StructureMessageLogDTO>) ((ArrayList<StructureMessageLogDTO>) structureMessageLog).clone());

        // on renvoie le clone
        return structure;
    }
}
