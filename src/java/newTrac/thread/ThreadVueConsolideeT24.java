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
import tools.Configuration;

/**
 *
 * @author 04486
 */
public class ThreadVueConsolideeT24 extends Thread {

    private final String namedQueryDev = "TicketCustom.findAllTicketsCustomOfDEVT24Liv";
    private final String namedQueryOV = "TicketCustom.findAllTicketsCustomOfOVT24Liv";
    private final String namedQueryIE = "TicketCustom.findAllTicketsCustomOfIET24Liv";
    private final String namedQueryTest = "TicketCustom.findAllTicketsCustomOfTestT24Ano";
    private final String namedQueryPretPourProd = "TicketCustom.findAllTicketsCustomOfProdT24Ano";
    private final String[] clesLivraisonAnomalie = new String[]{"parents", "t_liv", "ticket_origine"};
    private final String[] cles = new String[]{"parents", "nature_traitement", "changetime"};
    private final String cr = "RELEASE";
    private final String chf = "HOTFIX";
    private final String cp = "PROJET";
    private final String cu = "UPGRADE";
    private String persistenceUnitLivraison;
    private String persistenceUnitAnomalie;
    private String projectName;
    private Map<String, Map<String, RepresentationListTicketDTO>> projetActuelMap;
    private Map<String, StringBuilder> htmlMap;
    private Map<String, StringBuilder> htmlPopusMap;

    public ThreadVueConsolideeT24(VueConsolideeDTO vueConsolideeDTO, Map<String, Map<String, RepresentationListTicketDTO>> projetActuelMap, Map<String, StringBuilder> htmlMap, Map<String, StringBuilder> htmlPopusMap) {
        this.persistenceUnitLivraison = Configuration.puLivraisons;
        this.persistenceUnitAnomalie = Configuration.puAnomalies;
        this.projectName = vueConsolideeDTO.getProjectName();
        this.projetActuelMap = projetActuelMap;
        this.htmlMap = htmlMap;
        this.htmlPopusMap = htmlPopusMap;
    }

    @Override
    public void run() {
        try {
            ///<idTicket,map<clé,valeur>>
            Map<Integer, Map<String, Object>> mapTicketsCustomDev = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnitLivraison, Configuration.tracLivraisons, namedQueryDev, clesLivraisonAnomalie, "type");
            Map<Integer, Map<String, Object>> mapTicketsCustomOV = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnitLivraison, Configuration.tracLivraisons, namedQueryOV, clesLivraisonAnomalie, "type");
            Map<Integer, Map<String, Object>> mapTicketsCustomIE = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnitLivraison, Configuration.tracLivraisons, namedQueryIE, clesLivraisonAnomalie, "type");
            Map<Integer, Map<String, Object>> mapTicketsCustomTest = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnitAnomalie, Configuration.tracAnomalies, namedQueryTest, clesLivraisonAnomalie, "version");
            Map<Integer, Map<String, Object>> mapTicketsCustomPretPourProd = ToolsNewTrac.executeQueryThenAnalyseTickets(persistenceUnitAnomalie, Configuration.tracAnomalies, namedQueryPretPourProd, clesLivraisonAnomalie, "version");

            triTicketsSelonActeurEtCircuit(mapTicketsCustomDev, "DEV");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomOV, "OV");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomIE, "IE");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomTest, "TEST");
            triTicketsSelonActeurEtCircuit(mapTicketsCustomPretPourProd, "PROD");
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
        sbResult.append(new DiagramsPieToolsNewTrac().genererTableT24Html(ListTickets, cles, "livraisons_t24", "anomalies_t24").replaceAll("class='tableTicketsSummary'", "class='tableTicketsSummary tableDefilante'"));
        sbResult.append("</div>");
        return sbResult;
    }

    private StringBuilder genererStructureHtml() {
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<tr>");
        sbResult.append("<td class='projectName'>").append("T24").append("</td>");
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
        Map<String, RepresentationListTicketDTO> responsableMapCP;
        Map<String, RepresentationListTicketDTO> responsableMapCU;
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
        if (projetActuelMap.containsKey(cp)) {
            responsableMapCP = projetActuelMap.get(cp);
        } else {
            responsableMapCP = new LinkedHashMap<String, RepresentationListTicketDTO>();
            projetActuelMap.put(cp, responsableMapCP);
        }
        if (projetActuelMap.containsKey(cu)) {
            responsableMapCU = projetActuelMap.get(cu);
        } else {
            responsableMapCU = new LinkedHashMap<String, RepresentationListTicketDTO>();
            projetActuelMap.put(cu, responsableMapCU);
        }


        List<Map<String, Object>> listTicketsCR = new ArrayList<>();
        List<Map<String, Object>> listTicketsCHF = new ArrayList<>();
        List<Map<String, Object>> listTicketsCP = new ArrayList<>();
        List<Map<String, Object>> listTicketsCU = new ArrayList<>();
        RepresentationListTicketDTO representationListTicketDtoCR = null;
        RepresentationListTicketDTO representationListTicketDtoCHF = null;
        RepresentationListTicketDTO representationListTicketDtoCP = null;
        RepresentationListTicketDTO representationListTicketDtoCU = null;

        Iterator<Map.Entry<Integer, Map<String, Object>>> iter = mapTicketsCustom.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, Map<String, Object>> entry = iter.next();
            String natureTraitement = entry.getValue().get("nature_traitement").toString();
            if (natureTraitement.equals("QUALIFICATION_UPGRADE") || natureTraitement.equals("CERTIFICATION_UPGRADE") || natureTraitement.equals("UPGRADE A QUALIFIER") || natureTraitement.equals("UPGRADE A CERTIFIER")) {
                listTicketsCU.add(entry.getValue());
            } else if (natureTraitement.equals("HOT FIXE POUR MISE EN PROD") || natureTraitement.equals("ACTION A CHAUD POUR MISE EN PROD") || natureTraitement.equals("HOT FIXE TEST") || natureTraitement.equals("ACTION A CHAUD TEST")) {
                listTicketsCHF.add(entry.getValue());
            } else if (natureTraitement.equals("RELEASE A QUALIFIER") || natureTraitement.equals("RELEASE A CERTIFIER") || natureTraitement.equals("QUALIFICATION") || natureTraitement.equals("CERTIFICATION")) {
                listTicketsCR.add(entry.getValue());
            } else if (natureTraitement.equals("PROJET A QUALIFIER") || natureTraitement.equals("QUALIFICATION_PROJET")) {
                listTicketsCP.add(entry.getValue());
            }
        }
        representationListTicketDtoCR = new RepresentationListTicketDTO(listTicketsCR);
        representationListTicketDtoCHF = new RepresentationListTicketDTO(listTicketsCHF);
        representationListTicketDtoCP = new RepresentationListTicketDTO(listTicketsCP);
        representationListTicketDtoCU = new RepresentationListTicketDTO(listTicketsCU);
        responsableMapCR.put(responsableTechnique, representationListTicketDtoCR);
        responsableMapCHF.put(responsableTechnique, representationListTicketDtoCHF);
        responsableMapCP.put(responsableTechnique, representationListTicketDtoCP);
        responsableMapCU.put(responsableTechnique, representationListTicketDtoCU);
    }
}