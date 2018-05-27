/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package newTrac.thread;

import dto.RepresentationListTicketDTO;
import dto.VueConsolideeDTO;
import java.util.*;
import newTrac.DiagramsPieToolsNewTrac;
import newTrac.ToolsNewTrac;
import servlets.GenererVueConsolideeServlet;

/**
 *
 * @author 04486
 */
public class ThreadVueConsolidee extends Thread {

    private final String namedQueryDev = "TicketCustom.findAllTicketsCustomOfDEV";
    private final String namedQueryOV = "TicketCustom.findAllTicketsCustomOfOV";
    private final String namedQueryIE = "TicketCustom.findAllTicketsCustomOfIE";
    private final String namedQueryTest = "TicketCustom.findAllTicketsCustomOfTEST";
    private final String namedQueryPretPourProd = "TicketCustom.findAllTicketsCustomOfPretPourProd";
    private final String[] cles = new String[]{"parents", "nature_traitement", "changetime"};
    private final String cr = "RELEASE";
    private final String chf = "HOTFIX";
    private final String cp = "PROJET";
    private final String cu = "UPGRADE";
    private String persistenceUnit;
    private String projectName;
    private String adresseIP;
    private Map<String, Map<String, RepresentationListTicketDTO>> projetActuelMap;
    private Map<String, StringBuilder> htmlMap;
    private Map<String, StringBuilder> htmlPopusMap;

    public ThreadVueConsolidee(VueConsolideeDTO vueConsolideeDTO, Map<String, Map<String, RepresentationListTicketDTO>> projetActuelMap, Map<String, StringBuilder> htmlMap, Map<String, StringBuilder> htmlPopusMap) {
        this.persistenceUnit = vueConsolideeDTO.getPersistenceUnit();
        this.projectName = vueConsolideeDTO.getProjectName();
        this.adresseIP = vueConsolideeDTO.getAdresseIP();
        this.projetActuelMap = projetActuelMap;
        this.htmlMap = htmlMap;
        this.htmlPopusMap = htmlPopusMap;
    }

    @Override
    public void run() {
        try {
            ///<idTicket,map<clé,valeur>>
            Map<Integer, Map<String, Object>> mapTicketsCustomDev = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, "", namedQueryDev, cles, null);
            Map<Integer, Map<String, Object>> mapTicketsCustomOV = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, "", namedQueryOV, cles, null);
            Map<Integer, Map<String, Object>> mapTicketsCustomIE = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, "", namedQueryIE, cles, null);
            Map<Integer, Map<String, Object>> mapTicketsCustomTest = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, "", namedQueryTest, cles, null);
            Map<Integer, Map<String, Object>> mapTicketsCustomPretPourtest = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnit, "", namedQueryPretPourProd, cles, null);

            triTicketsSelonActeurEtCircuit(mapTicketsCustomDev, "DEV");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomOV, "OV");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomIE, "IE");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomTest, "TEST");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomPretPourtest, "PROD");
            //préparer le code HTML
            htmlMap.put(projectName, genererStructureHtml());
            //annoncer la fin
            GenererVueConsolideeServlet.endProcess();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    private StringBuilder generatePopupHtml(String idPopup, List<Map<String, Object>> ListTickets, String circuit) {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<div class='").append(idPopup).append("'  style='display: none'>");
        sbResult.append("<p class='titrePopup'>Projet: ").append(projectName).append(" / Circuit: ").append(circuit).append("</p>");
        sbResult.append(new DiagramsPieToolsNewTrac().genererTableHtml(ListTickets, cles, projectName, adresseIP).replaceAll("class='tableTicketsSummary'", "class='tableTicketsSummary tableDefilante'"));
        sbResult.append("</div>");
        return sbResult;
    }

    private StringBuilder genererStructureHtml() {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<tr>");
        sbResult.append("<td class='projectName'>").append(projectName.replaceAll("_", " ").replace("ICR", "ICR / PCA").replace("BFI CARTAGO INTRANET", "BFI C. INTRANET").toUpperCase()).append("</td>");
        int compteurId = 0;
        for (Map.Entry<String, Map<String, RepresentationListTicketDTO>> entryCircuit : projetActuelMap.entrySet()) {
            String circuit = entryCircuit.getKey();
            Map<String, RepresentationListTicketDTO> responsablesmap = entryCircuit.getValue();
            if (responsablesmap == null) {
                sbResult.append("<td class='hachures traitEpaisGauche' colspan='4'></td>");
            } else {
                int compteurCouleur = 1;
                String classeTrait;
                for (Map.Entry<String, RepresentationListTicketDTO> entryResponsable : responsablesmap.entrySet()) {
                    String responsable = entryResponsable.getKey();
                    if (responsable.equals("PROD") && !circuit.equals("HOTFIX")) {
                        break;
                    }
                    RepresentationListTicketDTO listeTicketsDto = entryResponsable.getValue();
                    String idcase = projectName.replaceAll(" ", "") + compteurId;
                    htmlPopusMap.put(idcase, generatePopupHtml(idcase, listeTicketsDto.getListTickets(), circuit));
                    if (compteurCouleur == 1) {
                        classeTrait = " traitEpaisGauche";
                    } else {
                        classeTrait = "";
                    }
                    int nbrTickets = listeTicketsDto.getListTickets().size();
                    String contenuCase;
                    if (nbrTickets == 0) {
                        contenuCase = "0";
                    } else {
                        contenuCase = "<a href='#' id='" + idcase + "' class='nbrTickets'  onclick='openPopup(this);'>";
                        contenuCase += nbrTickets;
                        contenuCase += "</a>";
                    }
                    sbResult.append("<td class='couleur").append(compteurCouleur).append(classeTrait).append("'>").append(contenuCase).append("</td>");
                    compteurCouleur++;
                    compteurId++;
                }
            }
        }
        sbResult.append("</tr>");
        return sbResult;
    }

    private void triTicketsSelonActeurEtCircuit(Map<Integer, Map<String, Object>> mapTicketsCustom, String responsableTechnique) {
        Map<String, RepresentationListTicketDTO> responsableMapCR;
        Map<String, RepresentationListTicketDTO> responsableMapCHF;
        if (projetActuelMap.containsKey(cr)) {
            responsableMapCR = projetActuelMap.get(cr);
        } else {
            responsableMapCR = new LinkedHashMap<String, RepresentationListTicketDTO>();
            projetActuelMap.put(cr, responsableMapCR);
        }
        if (projetActuelMap.containsKey(chf)) {
            responsableMapCHF = projetActuelMap.get(chf);
        } else {
            responsableMapCHF = new LinkedHashMap<String, RepresentationListTicketDTO>();
            projetActuelMap.put(chf, responsableMapCHF);
        }
        if (!projetActuelMap.containsKey(cp)) {
            projetActuelMap.put(cp, null);
        }
        if (!projetActuelMap.containsKey(cu)) {
            projetActuelMap.put(cu, null);
        }

        List<Map<String, Object>> listTicketsCR = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> listTicketsCHF = new ArrayList<Map<String, Object>>();
        RepresentationListTicketDTO representationListTicketDtoCR = null;
        RepresentationListTicketDTO representationListTicketDtoCHF = null;

        Iterator<Map.Entry<Integer, Map<String, Object>>> iter = mapTicketsCustom.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<String, Object>> entry = iter.next();
            //pb dav + profilage
            String natureTraitement = entry.getValue().get("nature_traitement").toString();
            if (natureTraitement.equals("HOTFIX") || natureTraitement.equals("ACTION A CHAUD")) {
                listTicketsCHF.add(entry.getValue());
            } else if (natureTraitement.equals("RELEASE")) {
                listTicketsCR.add(entry.getValue());
            }
        }
        representationListTicketDtoCR = new RepresentationListTicketDTO(listTicketsCR);
        representationListTicketDtoCHF = new RepresentationListTicketDTO(listTicketsCHF);
        responsableMapCR.put(responsableTechnique, representationListTicketDtoCR);
        responsableMapCHF.put(responsableTechnique, representationListTicketDtoCHF);
    }
}