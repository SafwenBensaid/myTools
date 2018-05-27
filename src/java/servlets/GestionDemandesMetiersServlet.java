/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dataBaseTracRequests.AppelRequetes;
import dataBaseTracRequests.DataBaseTracGenericRequests;
import dto.TripleDTO;
import entitiesTrac.Ticket;
import entitiesTrac.TicketCustom;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import static servlets.AutorisationHotfixServlet.ConvertTimeTracToJavaDate;
import tools.Configuration;
import tools.DataBaseTools;
import tools.GenericTools;
import tools.GetHotfixesPipesTool;
import tools.Tools;
import tools.UpdateTicketsTools;

/**
 *
 * @author 04494
 */
@WebServlet(name = "GestionDemandesMetiersServlet", urlPatterns = {"/GestionDemandesMetiersServlet"})
public class GestionDemandesMetiersServlet extends HttpServlet {

    public static List<Object> objectsListToBePersistedOnTheDataBaseTrac;
    public static List<Object> objectsListToBeMergedOnTheDataBaseTrac;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        //boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        //System.out.println(isMultipart);
        //System.out.println("Here in servlet class");
        try {
            String resultat = "";
            String pu = "";
            String action = request.getParameter("action");

            if (action.equals("insertFormation")) {
                pu = "dbFormationPU";
                action = "insert";
            } else {
                pu = Configuration.puGestionDesDemandes;
            }
            //String[] customFieldTab = new String[]{"appexterne", "autres", "chiffragemoa", "chiffragemoabdd", "chiffragemoabfi", "chiffragemoacdc", "chiffragemoacomptabilite", "chiffragemoaengagement", "chiffragemoainternational", "chiffragemoamdp", "chiffragemoe", "client_impact", "commentaire", "commentairemoa", "commentairemoe", "commentairepmo", "comptable", "contraintes", "date_app_prod", "date_demande", "date_demarrage", "date_livraison", "dircoordination_infrastructure_production", "dircoordinationetudesdevsi", "dirdevsi", "directions_moa", "directions_moe", "dirprogicielshorsc24intranet", "dirreferentielsidecisionnel", "dirurbanisationsi", "editique", "fonctionnel", "habilitation", "impact_autres", "impact_performance", "impact_pnb", "impact_productivite", "impact_qs_client", "impact_reglementaire", "impact_risque", "impacteng", "impactsi", "interfaces", "interv_externes", "invest", "metier_concerne", "migration", "nature_traitement", "niveauprojet", "num_release", "partieprenante", "planification", "projettrac", "refticket", "resmoa", "responsablepmo", "tarification", "type_traitement", "vis_a_vis_moa", "visavis_comptable", "visavis_dsi", "visavis_metier", "visavis_technique"};
            List<String> listTicketCustom = new DataBaseTracGenericRequests<String>().getList_TYPE_OfnamedQuery(pu, "TicketCustom.findAllCustomFields", null);
            String[] customFieldTab = listTicketCustom.toArray(new String[0]);

            Tools tools = new Tools();
            String connectedUser = tools.getConnectedLogin();
            boolean hasRoleGestionDesDemandes = tools.hasRole("GESTION_DES_DEMANDES");

            Map<String, Object> detailsSpecificProject = new HashMap<>();

            switch (action) {
                case "insert":
                    resultat = insertBesoin(pu, request, customFieldTab, connectedUser);
                    break;
                case "update":
                case "loadOne":
                case "loadAll":
                case "validate":
                    List<Integer> listDemandesMetier = new DataBaseTracGenericRequests<Integer>().getList_TYPE_OfnamedQuery(pu, "TicketCustom.findAllDemandesMetier", null);

                    //Organiser les besoin par ticket Custom
                    Map<Integer, Map<String, Object>> demandesMetierDetails = AppelRequetes.getTicketCustomByTicketIdAndNames(listDemandesMetier, pu, Configuration.tracGestionDemandes, customFieldTab);
                    //Trier les besoins par status
                    Map<String, List<Map<String, Object>>> mapDemandesMetier = new GetHotfixesPipesTool().triDemandesMetierSelonStatus(demandesMetierDetails);

                    String idTicket = request.getParameter("idTicket");
                    if (idTicket != null && !idTicket.isEmpty()) {
                        detailsSpecificProject = demandesMetierDetails.get(Integer.valueOf(idTicket));
                    }

                    if (action.equals("update")) {
                        resultat = updateBesoin(pu, request, detailsSpecificProject, connectedUser);
                    } else if (action.equals("loadOne")) {
                        boolean ticketHasAttachement = ticketHasAttachement(pu, idTicket);
                        resultat = getHtmlSpecificProject(hasRoleGestionDesDemandes, detailsSpecificProject, ticketHasAttachement, connectedUser);
                    } else if (action.equals("loadAll")) {
                        resultat = getHtmlDemandesMetierTable(hasRoleGestionDesDemandes, mapDemandesMetier, connectedUser, request);
                    } else if (action.equals("validate")) {
                        //recuperer les checked box (html)
                        String ticketsValidated = request.getParameter("ticketsValidated");
                        String ticketsRejected = request.getParameter("ticketsRejected");
                        //valider les mise a jour (maj satatut ticket)
                        resultat = updateTickets(pu, ticketsValidated, ticketsRejected, connectedUser, demandesMetierDetails);
                    }
                    break;
            }
            out.println(resultat);
        } finally {
            out.close();
        }
    }

    private Boolean ticketHasAttachement(String pu, String idTicket) {
        List<String> listTicketsWithAttachement = new DataBaseTracGenericRequests<String>().getList_TYPE_OfnamedQuery(pu, "Attachment.findAllTicketId", null);
        return listTicketsWithAttachement.contains(idTicket);
    }

    private String updateTickets(String pu, String ticketsValidated, String ticketsRejected, String connectedUser, Map<Integer, Map<String, Object>> demandesMetierDetails) {
        String resultat = "";
        boolean resultatValidation = true;
        Map<String, String> workflowMap = new HashMap<>();
        workflowMap.put("NOUVEAU_BESOIN", "VALIDE_METIER");
        workflowMap.put("VALIDE_METIER", "VALIDE_IMPACT");
        workflowMap.put("VALIDE_IMPACT", "VALIDE_MOA");
        workflowMap.put("VALIDE_MOA", "VALIDE_MOE");
        workflowMap.put("VALIDE_MOE", "VALIDE_PMO");

        try {
            //******************************** update trac  ********************************
            String[] tabTicketsValidated = ticketsValidated.split("V");
            String[] tabTicketsRejected = ticketsRejected.split("A");
            Map<String, Object> detailsSpecificProject = new HashMap<>();
            //Validation
            for (String idTicket : tabTicketsValidated) {
                if (!idTicket.isEmpty()) {
                    detailsSpecificProject = demandesMetierDetails.get(Integer.valueOf(idTicket));
                    String oldStruts = ((Ticket) detailsSpecificProject.get("Ticket")).getStatus();
                    String newStatus = workflowMap.get(oldStruts);
                    String messageTrac = "== Ticket validé ==";
                    DataBaseTracGenericRequests.updateTicketTracGeneral(pu, Integer.parseInt(idTicket), connectedUser, messageTrac, null, null, newStatus, null, null);
                }
            }
            //Annulation
            for (String idTicket : tabTicketsRejected) {
                if (!idTicket.isEmpty()) {
                    String newStatus = "NON_VALIDE";
                    String messageTrac = "== Ticket Annulé ==";
                    DataBaseTracGenericRequests.updateTicketTracGeneral(pu, Integer.parseInt(idTicket), connectedUser, messageTrac, null, null, newStatus, null, null);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(ex));
            resultatValidation = false;
        }
        if (resultatValidation) {
            resultat = "mise à  jour des tickets effetuée avec succés";
        } else {
            resultat = "echec de mise à  jour des tickets des tickets";
        }
        return resultat;
    }

    public String insertBesoin(String pu, HttpServletRequest request, String[] customFieldTab, String connectedUser) {
        String resultat = "";
        Map<String, String> resultMap = new HashMap<>();
        DataBaseTools dbTools = new DataBaseTools(pu);
        Integer nbrTickets = getNbrTickets(dbTools);
        Ticket ticket = new Ticket(nbrTickets + 1);
        objectsListToBePersistedOnTheDataBaseTrac = new ArrayList<Object>();
        //Calcul Trac Date Now
        long timeTrac = UpdateTicketsTools.generateTracDateNow();
        //remplissage l'objet ticket
        ticket.setSummary(request.getParameter("summary"));
        ticket.setDescription(request.getParameter("description"));
        ticket.setType(request.getParameter("type"));
        ticket.setPriority(request.getParameter("priority"));
        ticket.setMilestone(request.getParameter("milestone"));
        ticket.setComponent(request.getParameter("component"));
        ticket.setStatus(request.getParameter("status"));
        ticket.setChangetime(timeTrac);

        ticket.setReporter(connectedUser);
        ticket.setTime(timeTrac);
        ticket.setOwner("");
        ticket.setCc("");
        ticket.setSeverity("");
        ticket.setResolution("");
        ticket.setKeywords("");
        ticket.setVersion("");

        objectsListToBePersistedOnTheDataBaseTrac.add(ticket);

        if (customFieldTab != null) {
            TicketCustom ticketCustom = new TicketCustom();
            for (String customField : customFieldTab) {
                if (request.getParameterMap().containsKey(customField)) {
                    ticketCustom = Tools.createTicketCustom(ticket, customField, request.getParameter(customField));
                } else {
                    ticketCustom = Tools.createTicketCustom(ticket, customField, "");
                }
                objectsListToBePersistedOnTheDataBaseTrac.add(ticketCustom);
            }
        }

        dbTools.updateDataBaseTrac(dbTools);
        dbTools.closeRessources();

        resultMap.put("message", "Besoin ajouté avec succès");
        resultMap.put("idTicket", ticket.getId().toString());
        resultat = Tools.objectToJsonString(resultMap);
        return resultat;
    }

    public String updateBesoin(String pu, HttpServletRequest request, Map<String, Object> demandesMetierDetails, String connectedUser) {
        Ticket ticket = (Ticket) demandesMetierDetails.get("Ticket");
        GenericTools gtTick = new GenericTools<>(Ticket.class);
        Map<String, Object> fieldMap = gtTick.convertDtoToMap(ticket);
        List<TripleDTO> fieldList = new ArrayList<>();
        List<TripleDTO> customFieldList = new ArrayList<>();
        String field, oldValue, newValue = "";
        String resultat = "OK";
        Map<String, String> resultMap = new HashMap<>();

        try {
            for (Object key : fieldMap.keySet()) {
                field = key.toString();
                if (request.getParameterMap().containsKey(field)) {
                    if (fieldMap.get(field) != null) {
                        oldValue = fieldMap.get(field).toString();
                    } else {
                        oldValue = "";
                    }
                    newValue = request.getParameter(field);
                    if (!oldValue.equals(newValue) && newValue != null) {
                        fieldList.add(new TripleDTO(field, oldValue, newValue));
                    }
                }
            }

            for (Object key : demandesMetierDetails.keySet()) {
                if (!key.equals("Ticket") && request.getParameterMap().containsKey(key.toString())) {
                    field = key.toString();
                    System.out.println("field " + field);
                    if (demandesMetierDetails.get(field) != null) {
                        oldValue = demandesMetierDetails.get(field).toString();
                    } else {
                        oldValue = "";
                    }
                    newValue = request.getParameter(field);
                    if (!oldValue.equals(newValue) && newValue != null) {
                        customFieldList.add(new TripleDTO(field, oldValue, newValue));
                    }
                }
            }
            //Get ticket to Update
            String updateResult = DataBaseTracGenericRequests.updateTicketTracGenerique(pu, ticket.getId(), connectedUser, "", fieldList, customFieldList);


            if (updateResult.equals("OK")) {
                resultat = "Enregistrement effectué";
            } else {
                resultat = "Problème lors de la mise à jour du Besoin";
            }

            resultMap.put("message", resultat);
            resultMap.put("idTicket", ticket.getId().toString());
            resultat = Tools.objectToJsonString(resultMap);
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return resultat;
    }

    public Integer getNbrTickets(DataBaseTools dbTools) {
        Integer nbrTickets = null;
        try {
            Query q = dbTools.em.createNamedQuery("Ticket.countAll");
            q.setHint(QueryHints.REFRESH, HintValues.TRUE);
            nbrTickets = (Integer) q.getSingleResult();
        } catch (Exception ex) {
            ex.printStackTrace();
            Tools.traiterException(Tools.getStackTrace(ex));
        }
        return nbrTickets;
    }

    public String getHtmlSpecificProject(boolean authorizedAdmin, Map<String, Object> mapDemandesMetier, boolean ticketHasAttachement, String connectedUser) {
        //specificationBloc
        Ticket ticket = (Ticket) mapDemandesMetier.get("Ticket");

        //impactBloc
        String impact_reglementaire = (String) mapDemandesMetier.get("impact_reglementaire");
        String impact_pnb = (String) mapDemandesMetier.get("impact_pnb");
        String impact_qs_client = (String) mapDemandesMetier.get("impact_qs_client");
        String impact_productivite = (String) mapDemandesMetier.get("impact_productivite");
        String impact_risque = (String) mapDemandesMetier.get("impact_risque");
        String impact_performance = (String) mapDemandesMetier.get("impact_performance");
        String impact_autres = (String) mapDemandesMetier.get("impact_autres");
        String contraintesImpact = (String) mapDemandesMetier.get("contraintesImpact");
        String metier_concerne = (String) mapDemandesMetier.get("metier_concerne");
        String client_impact = (String) mapDemandesMetier.get("client_impact");
        String date_realisation = (String) mapDemandesMetier.get("date_realisation");

        //partiesPrenantes
        String resmoa = (String) mapDemandesMetier.get("resmoa");
        String vis_a_vis_moa = (String) mapDemandesMetier.get("vis_a_vis_moa");
        String directions_moa = (String) mapDemandesMetier.get("directions_moa");
        String directions_moe = (String) mapDemandesMetier.get("directions_moe");
        String visavis_technique = (String) mapDemandesMetier.get("visavis_technique");
        String visavis_comptable = (String) mapDemandesMetier.get("visavis_comptable");
        String visavis_dsi = (String) mapDemandesMetier.get("visavis_dsi");
        String interv_externes = (String) mapDemandesMetier.get("interv_externes");//REMOVED

        //impactSI
        String fonctionnel = (String) mapDemandesMetier.get("fonctionnel");
        String habilitation = (String) mapDemandesMetier.get("habilitation");
        String editique = (String) mapDemandesMetier.get("editique");
        String comptable = (String) mapDemandesMetier.get("comptable");
        String tarification = (String) mapDemandesMetier.get("tarification");
        String interfaces = (String) mapDemandesMetier.get("interfaces");
        String migration = (String) mapDemandesMetier.get("migration");
        String autres = (String) mapDemandesMetier.get("autres");
        String appexterne = (String) mapDemandesMetier.get("appexterne");

        //chiffrageMoa
        String lig_0_col_1 = (String) mapDemandesMetier.get("lig_0_col_1");
        String lig_0_col_2 = (String) mapDemandesMetier.get("lig_0_col_2");
        String lig_1_col_1 = (String) mapDemandesMetier.get("lig_1_col_1");
        String lig_1_col_2 = (String) mapDemandesMetier.get("lig_1_col_2");
        String lig_2_col_1 = (String) mapDemandesMetier.get("lig_2_col_1");
        String lig_2_col_2 = (String) mapDemandesMetier.get("lig_2_col_2");
        String lig_3_col_1 = (String) mapDemandesMetier.get("lig_3_col_1");
        String lig_3_col_2 = (String) mapDemandesMetier.get("lig_3_col_2");
        String lig_4_col_1 = (String) mapDemandesMetier.get("lig_4_col_1");
        String lig_4_col_2 = (String) mapDemandesMetier.get("lig_4_col_2");
        String lig_5_col_1 = (String) mapDemandesMetier.get("lig_5_col_1");
        String lig_5_col_2 = (String) mapDemandesMetier.get("lig_5_col_2");
        String lig_6_col_1 = (String) mapDemandesMetier.get("lig_6_col_1");
        String lig_6_col_2 = (String) mapDemandesMetier.get("lig_6_col_2");
        String lig_somme_1 = (String) mapDemandesMetier.get("lig_somme_1");
        String lig_somme_2 = (String) mapDemandesMetier.get("lig_somme_2");

        //chiffrageMoe
        String lig_0_col_3 = (String) mapDemandesMetier.get("lig_0_col_3");
        String lig_0_col_4 = (String) mapDemandesMetier.get("lig_0_col_4");
        String lig_1_col_3 = (String) mapDemandesMetier.get("lig_1_col_3");
        String lig_1_col_4 = (String) mapDemandesMetier.get("lig_1_col_4");
        String lig_2_col_3 = (String) mapDemandesMetier.get("lig_2_col_3");
        String lig_2_col_4 = (String) mapDemandesMetier.get("lig_2_col_4");
        String lig_3_col_3 = (String) mapDemandesMetier.get("lig_3_col_3");
        String lig_3_col_4 = (String) mapDemandesMetier.get("lig_3_col_4");
        String lig_4_col_3 = (String) mapDemandesMetier.get("lig_4_col_3");
        String lig_4_col_4 = (String) mapDemandesMetier.get("lig_4_col_4");
        String lig_5_col_3 = (String) mapDemandesMetier.get("lig_5_col_3");
        String lig_5_col_4 = (String) mapDemandesMetier.get("lig_5_col_4");
        String lig_somme_3 = (String) mapDemandesMetier.get("lig_somme_3");
        String lig_somme_4 = (String) mapDemandesMetier.get("lig_somme_4");

        //planification
        String niveauprojet = (String) mapDemandesMetier.get("niveauprojet");
        String projettrac = (String) mapDemandesMetier.get("projettrac");
        String nature_traitement = (String) mapDemandesMetier.get("nature_traitement");
        String refticket = (String) mapDemandesMetier.get("refticket");//REMOVED
        String type_traitement = (String) mapDemandesMetier.get("type_traitement");
        String num_release = (String) mapDemandesMetier.get("num_release");
        String date_demarrage = (String) mapDemandesMetier.get("date_demarrage");
        String date_app_prod = (String) mapDemandesMetier.get("date_app_prod");
        String invest = (String) mapDemandesMetier.get("invest");
        String commentairepmo = (String) mapDemandesMetier.get("commentairepmo");
        String responsablepmo = (String) mapDemandesMetier.get("responsablepmo");//REMOVED
        String owner = ticket.getOwner();

        Map<String, String> resultMap = new HashMap<>();
        StringBuilder sbResult = new StringBuilder();
        String resultat = "";

        //specificationBloc
        sbResult.append("<input type='hidden' id='fieldMETIER-id' value='").append(ticket.getId()).append("'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-time' value='" + ticket.getTime() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-changetime' value='" + ticket.getChangetime() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-severity' value='" + ticket.getSeverity() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-reporter' value='" + ticket.getReporter() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-owner' value='" + ticket.getOwner() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-cc' value='" + ticket.getCc() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-version' value='" + ticket.getVersion() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-status' value='" + ticket.getStatus() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-resolution' value='" + ticket.getResolution() + "'/>");
        sbResult.append("<input type='hidden' id='fieldMETIER-keywords' value='" + ticket.getKeywords() + "'/>");

        sbResult.append("<tbody>");
        sbResult.append("<tr>");
        sbResult.append("<th><label for='fieldMETIER-summary'>Intitulé du besoin:</label></th>");
        sbResult.append("<td class='fullrow' colspan='3'>");
        sbResult.append("<input type='text' id='fieldMETIER-summary' name='field_summary' size='70' value='");
        sbResult.append(ticket.getSummary());
        sbResult.append("'>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<th><label for='fieldMETIER-description'>Description:</label></th>");
        sbResult.append("<td class='fullrow' colspan='3'>");
        sbResult.append("<fieldset>");
        sbResult.append("<div class='wikitoolbar'><a href='#' id='strong' title='Texte en gras: '''Exemple'''' tabindex='400'></a><a href='#' id='em' title='Texte en italique: ''Exemple''' tabindex='400'></a><a href='#' id='heading' title='Titre: == Exemple ==' tabindex='400'></a><a href='#' id='link' title='Lien: [http://www.exemple.com/ Exemple]' tabindex='400'></a><a href='#' id='code' title='Bloc de code: {{{ exemple }}}' tabindex='400'></a><a href='#' id='hr' title='Filet horizontal: ----' tabindex='400'></a><a href='#' id='np' title='Nouveau paragraphe' tabindex='400'></a><a href='#' id='br' title='Saut de ligne: [[BR]]' tabindex='400'></a><a href='#' id='img' title='Image: [[Image()]]' tabindex='400'></a></div><div class='trac-resizable'><div><textarea id='fieldMETIER-description' name='field_description' class='wikitext trac-resizable' rows='10' cols='68'>");
        sbResult.append(ticket.getDescription());
        sbResult.append("</textarea><div class='trac-grip' style='margin-left: 2px; margin-right: -8px;'></div></div></div>");
        sbResult.append("</fieldset>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-milestone'>Axe Métier:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-milestone' name='field_milestone'>");
        sbResult.append("<option value='" + ticket.getMilestone() + "'>" + ticket.getMilestone() + "</option><option value='ASSURANCES BIAT'>ASSURANCES BIAT</option><option value='BIAT CAPITAL'>BIAT CAPITAL</option><option value='DEPARTEMENT CONTROLE DE GESTION'>DEPARTEMENT CONTROLE DE GESTION</option><option value='DEPARTEMENT CONTROLE GENERAL'>DEPARTEMENT CONTROLE GENERAL</option><option value='DEPARTEMENT DES OPERATIONS BANCAIRES'>DEPARTEMENT DES OPERATIONS BANCAIRES</option><option value='DEPARTEMENT FINANCE - COMPTABILITE'>DEPARTEMENT FINANCE - COMPTABILITE</option><option value='DEPARTEMENT MAITRISE D'OUVRAGE ET COORDINATION METIERS'>DEPARTEMENT MAITRISE D'OUVRAGE ET COORDINATION METIERS</option><option value='DEPARTEMENT RECOUVREMENT ET CONTENTIEUX'>DEPARTEMENT RECOUVREMENT ET CONTENTIEUX</option><option value='DEPARTEMENT RISQUES'>DEPARTEMENT RISQUES</option><option value='DEPARTEMENT SYSTEMES D'INFORMATION'>DEPARTEMENT SYSTEMES D'INFORMATION</option><option value='DGA BANQUE DE DETAIL'>DGA BANQUE DE DETAIL</option><option value='DGA RESSOURCES'>DGA RESSOURCES</option><option value='DIRECTION CENTRALE PLANIFICATION &amp; BUDGET'>DIRECTION CENTRALE PLANIFICATION &amp; BUDGET</option><option value='DIRECTION CENTRALE RESSOURCES HUMAINES'>DIRECTION CENTRALE RESSOURCES HUMAINES</option><option value='DIRECTION GENERALE'>DIRECTION GENERALE</option><option value='DIRECTION PMO BANQUE'>DIRECTION PMO BANQUE</option><option value='EQUIPE OPTIMISATION DES PROCESS'>EQUIPE OPTIMISATION DES PROCESS</option><option value='EQUIPE REFONTE'>EQUIPE REFONTE</option><option value='POLE GRANDES ENTREPRISES ET INSTITUTIONNELS'>POLE GRANDES ENTREPRISES ET INSTITUTIONNELS</option><option value='POLE INVESTISSEURS'>POLE INVESTISSEURS</option><option value='POLE STRATEGIE ET BANQUE DE FINANCEMENT ET D'INVESTISSEMENT'>POLE STRATEGIE ET BANQUE DE FINANCEMENT ET D'INVESTISSEMENT</option><option value='PROTECTRICE'>PROTECTRICE</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMETIER-component'>Activité:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMETIER-component' name='field_component'>");
        sbResult.append("<option value='" + ticket.getComponent() + "'>" + ticket.getComponent() + "</option><option value='ASSURANCES BIAT'>ASSURANCES BIAT</option><option value='COORDINATION ETUDES ET DEVELOPPEMENTS SI'>COORDINATION ETUDES ET DEVELOPPEMENTS SI</option><option value='COORDINATION INFRASTRUCTURE &amp; PRODUCTION'>COORDINATION INFRASTRUCTURE &amp; PRODUCTION</option><option value='DIRECTION ACHAT'>DIRECTION ACHAT</option><option value='DIRECTION ADMINISTATION DES CREDITS'>DIRECTION ADMINISTATION DES CREDITS</option><option value='DIRECTION ADMINISTRATION &amp; INTEGRATION DES SYSTEMES'>DIRECTION ADMINISTRATION &amp; INTEGRATION DES SYSTEMES</option><option value='DIRECTION ADMINISTRATION DES CREDITS'>DIRECTION ADMINISTRATION DES CREDITS</option><option value='DIRECTION ADMINISTRATIVE ET FINANCIERE DU PERSONNEL'>DIRECTION ADMINISTRATIVE ET FINANCIERE DU PERSONNEL</option><option value='DIRECTION ANIMATION COMMERCIALE ET PILOTAGE'>DIRECTION ANIMATION COMMERCIALE ET PILOTAGE</option><option value='DIRECTION ANIMATION QUALITE'>DIRECTION ANIMATION QUALITE</option><option value='DIRECTION BACK OFFICE DES MARCHES'>DIRECTION BACK OFFICE DES MARCHES</option><option value='DIRECTION CENTRALE PLANIFICATION ET BUDGET'>DIRECTION CENTRALE PLANIFICATION ET BUDGET</option><option value='DIRECTION CENTRALE RESSOURCES HUMAINES'>DIRECTION CENTRALE RESSOURCES HUMAINES</option><option value='DIRECTION CONCEPTION &amp; ORGANISATION COMPTABLE'>DIRECTION CONCEPTION &amp; ORGANISATION COMPTABLE</option><option value='DIRECTION CONDUITE DU CHANGEMENT ET PROCEDURES'>DIRECTION CONDUITE DU CHANGEMENT ET PROCEDURES</option><option value='DIRECTION CONTROLE DES DEPENSES'>DIRECTION CONTROLE DES DEPENSES</option><option value='DIRECTION CONTROLE GESTION &amp; PRICING'>DIRECTION CONTROLE GESTION &amp; PRICING</option><option value='DIRECTION COORDINATION ET PILOTAGE'>DIRECTION COORDINATION ET PILOTAGE</option><option value='DIRECTION CORPORATE FINANCE'>DIRECTION CORPORATE FINANCE</option><option value='DIRECTION CREDIT GRANDES ENTREPRISES'>DIRECTION CREDIT GRANDES ENTREPRISES</option><option value='DIRECTION CREDITS'>DIRECTION CREDITS</option><option value='DIRECTION DE L'AUDIT'>DIRECTION DE L'AUDIT</option><option value='DIRECTION DE L'EXPLOITATION &amp; DE LA SUPERVISION'>DIRECTION DE L'EXPLOITATION &amp; DE LA SUPERVISION</option><option value='DIRECTION DE L'INSPECTION'>DIRECTION DE L'INSPECTION</option><option value='DIRECTION DE L'ORGANISATION'>DIRECTION DE L'ORGANISATION</option><option value='DIRECTION DE LA CLIENTELE PRIVEE'>DIRECTION DE LA CLIENTELE PRIVEE</option><option value='DIRECTION DE LA COMMUNICATION'>DIRECTION DE LA COMMUNICATION</option><option value='DIRECTION DE LA CONFORMITE'>DIRECTION DE LA CONFORMITE</option><option value='DIRECTION DE LA FORMATION &amp; DU DEVELOPPEMENT DES COMPETENCES'>DIRECTION DE LA FORMATION &amp; DU DEVELOPPEMENT DES COMPETENCES</option><option value='DIRECTION DE LA LOGISTIQUE'>DIRECTION DE LA LOGISTIQUE</option><option value='DIRECTION DE LA LOGISTIQUE ET DES OPERATIONS SFAX ET SUD'>DIRECTION DE LA LOGISTIQUE ET DES OPERATIONS SFAX ET SUD</option><option value='DIRECTION DE LA PLANIFICATION'>DIRECTION DE LA PLANIFICATION</option><option value='DIRECTION DE LA SECURITE ET DE CONTINUITE D'ACTIVITE'>DIRECTION DE LA SECURITE ET DE CONTINUITE D'ACTIVITE</option><option value='DIRECTION DES AFFAIRES JURIDIQUES'>DIRECTION DES AFFAIRES JURIDIQUES</option><option value='DIRECTION DES GARANTIES'>DIRECTION DES GARANTIES</option><option value='DIRECTION DES INSTITUTIONNELS'>DIRECTION DES INSTITUTIONNELS</option><option value='DIRECTION DES SERVICES ET DU SUPPORT INFORMATIQUES'>DIRECTION DES SERVICES ET DU SUPPORT INFORMATIQUES</option><option value='DIRECTION DES TRANSFERTS'>DIRECTION DES TRANSFERTS</option><option value='DIRECTION DES TRE ET DE CLIENTELE PARTICULIERS NON RESIDENTE'>DIRECTION DES TRE ET DE CLIENTELE PARTICULIERS NON RESIDENTE</option><option value='DIRECTION DEVELOPPEMENT'>DIRECTION DEVELOPPEMENT</option><option value='DIRECTION DEVELOPPEMENT A L'INTERNATIONAL'>DIRECTION DEVELOPPEMENT A L'INTERNATIONAL</option><option value='DIRECTION DEVELOPPEMENT DE LA PME'>DIRECTION DEVELOPPEMENT DE LA PME</option><option value='DIRECTION DEVELOPPEMENTS SI'>DIRECTION DEVELOPPEMENTS SI</option><option value='DIRECTION DU BUDGET'>DIRECTION DU BUDGET</option><option value='DIRECTION DU COMMERCE EXTERIEUR'>DIRECTION DU COMMERCE EXTERIEUR</option><option value='DIRECTION DU CONTENTIEUX'>DIRECTION DU CONTENTIEUX</option><option value='DIRECTION DU CONTROLE PERMANENT'>DIRECTION DU CONTROLE PERMANENT</option><option value='DIRECTION DU COOMMERCE EXTERIEUR'>DIRECTION DU COOMMERCE EXTERIEUR</option><option value='DIRECTION DU RECOUVREMENT'>DIRECTION DU RECOUVREMENT</option><option value='DIRECTION ENQUETE ET ANTI-BLANCHIMENT'>DIRECTION ENQUETE ET ANTI-BLANCHIMENT</option><option value='DIRECTION EXPLOITATION &amp; SUPPORT ADMINISTRATIF'>DIRECTION EXPLOITATION &amp; SUPPORT ADMINISTRATIF</option><option value='DIRECTION FRONT OFFICE DES MARCHES'>DIRECTION FRONT OFFICE DES MARCHES</option><option value='DIRECTION GESTION DES CARRIERES'>DIRECTION GESTION DES CARRIERES</option><option value='DIRECTION GROUPES &amp; GRANDES ENTREPRISES 1'>DIRECTION GROUPES &amp; GRANDES ENTREPRISES 1</option><option value='DIRECTION GROUPES &amp; GRANDES ENTREPRISES 2'>DIRECTION GROUPES &amp; GRANDES ENTREPRISES 2</option><option value='DIRECTION INGENIERIE IT RESEAU &amp; SECURITE'>DIRECTION INGENIERIE IT RESEAU &amp; SECURITE</option><option value='DIRECTION JUSTIFICATION ET CONTROLE COMPTABLE'>DIRECTION JUSTIFICATION ET CONTROLE COMPTABLE</option><option value='DIRECTION LOGISTIQUE &amp; OPERATIONS SOUSSE CENTRE &amp; SAHEL'>DIRECTION LOGISTIQUE &amp; OPERATIONS SOUSSE CENTRE &amp; SAHEL</option><option value='DIRECTION MARKETING'>DIRECTION MARKETING</option><option value='DIRECTION METHODES ET OUTILS'>DIRECTION METHODES ET OUTILS</option><option value='DIRECTION MOA BANQUE DE DETAIL &amp; NVX. CANAUX DE DISTRIB.'>DIRECTION MOA BANQUE DE DETAIL &amp; NVX. CANAUX DE DISTRIB.</option><option value='DIRECTION MOA BFI ET FONCTIONS SUPPORT'>DIRECTION MOA BFI ET FONCTIONS SUPPORT</option><option value='DIRECTION MOA ENGAGEMENTS ET RISQUES'>DIRECTION MOA ENGAGEMENTS ET RISQUES</option><option value='DIRECTION MOA INTERNATIONAL'>DIRECTION MOA INTERNATIONAL</option><option value='DIRECTION MOA MOYENS DE PAIEMENT'>DIRECTION MOA MOYENS DE PAIEMENT</option><option value='DIRECTION MOYENS DE PAIEMENT'>DIRECTION MOYENS DE PAIEMENT</option><option value='DIRECTION PARTICIPATIONS &amp; CAPITAL INVESTISSEMENT'>DIRECTION PARTICIPATIONS &amp; CAPITAL INVESTISSEMENT</option><option value='DIRECTION PMO BANQUE'>DIRECTION PMO BANQUE</option><option value='DIRECTION POLITIQUE CREDIT &amp; GESTION PORTEFEUILLE'>DIRECTION POLITIQUE CREDIT &amp; GESTION PORTEFEUILLE</option><option value='DIRECTION PRODUCTIVITE'>DIRECTION PRODUCTIVITE</option><option value='DIRECTION PROGICIELS HORS C24 &amp; INTRANET'>DIRECTION PROGICIELS HORS C24 &amp; INTRANET</option><option value='DIRECTION REFERENTIEL SI ET DECISIONNEL'>DIRECTION REFERENTIEL SI ET DECISIONNEL</option><option value='DIRECTION REPORTING &amp; CONTROLE'>DIRECTION REPORTING &amp; CONTROLE</option><option value='DIRECTION RISQUE DE MARCHE'>DIRECTION RISQUE DE MARCHE</option><option value='DIRECTION RISQUE OPERATIONNEL'>DIRECTION RISQUE OPERATIONNEL</option><option value='DIRECTION RISQUES CREDIT PME &amp; CLIENTELE DE DETAIL'>DIRECTION RISQUES CREDIT PME &amp; CLIENTELE DE DETAIL</option><option value='DIRECTION RISQUES DE CREDIT GROUPES &amp; GRANDES ENTREPRISES'>DIRECTION RISQUES DE CREDIT GROUPES &amp; GRANDES ENTREPRISES</option><option value='DIRECTION SITUATIONS COMPTABLES ET FISCALES'>DIRECTION SITUATIONS COMPTABLES ET FISCALES</option><option value='DIRECTION URBANISATION DU SI ET INTEGRATION'>DIRECTION URBANISATION DU SI ET INTEGRATION</option><option value='EQUIPE OPTIMISATION DES PROCESS'>EQUIPE OPTIMISATION DES PROCESS</option><option value='EQUIPE REFONTE'>EQUIPE REFONTE</option><option value='POLE GRANDES ENTREPRISES ET INSTITUTIONNELS'>POLE GRANDES ENTREPRISES ET INSTITUTIONNELS</option><option value='POLE INVESTISSSEUR'>POLE INVESTISSSEUR</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-type'>Type du besoin:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-type' name='field_type'>");
        sbResult.append("<option value='" + ticket.getType() + "'>" + ticket.getType() + "</option><option value='REGLEMENTAIRE'>REGLEMENTAIRE</option><option value='METIER'>METIER</option><option value='STRATEGIQUE'>STRATEGIQUE</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-priority'>Priorité du besoin:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-priority' name='field_priority'>");
        sbResult.append("<option value='" + ticket.getPriority() + "'>" + ticket.getPriority() + "</option><option value='BLOQUANTE'>BLOQUANTE</option><option value='MAJEURE'>MAJEURE</option><option value='MINEURE'>MINEURE</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        sbResult.append("</tbody>");
        resultMap.put("specificationBloc", sbResult.toString());
        sbResult.setLength(0);

        //impactBloc
        sbResult.append("<tbody>");
        sbResult.append("<tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-impact_reglementaire'>Impact Réglementaire:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-impact_reglementaire' name='field_impact_reglementaire'>");
        sbResult.append("<option>" + impact_reglementaire + "</option>");
        sbResult.append("<option value='OUI'>OUI</option><option value='NON'>NON</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMETIER-impact_pnb'>Impact PNB:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMETIER-impact_pnb' name='field_impact_pnb'>");
        sbResult.append("<option>" + impact_pnb + "</option>");
        sbResult.append("<option value='1 : (0 - 50 000 TND)'>1 : (0 - 50 000 TND)</option><option value='2 : (50 000 - 100 000 TND)'>2 : (50 000 - 100 000 TND)</option><option value='3 : (100 000 - 300 000 TND)'>3 : (100 000 - 300 000 TND)</option><option value='4 : (> 300 000 TND)'>4 : (&gt; 300 000 TND)</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-impact_qs_client'>Impact Qualité Client:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-impact_qs_client' name='field_impact_qs_client'>");
        sbResult.append("<option>" + impact_qs_client + "</option>");
        sbResult.append("<option value='1 : (Impact Faible -Cible Limitée)'>1 : (Impact Faible -Cible Limitée)</option><option value='2 : (Impact Moyen - Cible Limitée)'>2 : (Impact Moyen - Cible Limitée)</option><option value='3 : (Impact Important - Cible Importante)'>3 : (Impact Important - Cible Importante)</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMETIER-impact_productivite'>Impact Productivité:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMETIER-impact_productivite' name='field_impact_productivite'>");
        sbResult.append("<option>" + impact_productivite + "</option>");
        sbResult.append("<option value='1 : (0-2 ETP)'>1 : (0-2 ETP)</option><option value='2 : (3 - 5 ETP)'>2 : (3 - 5 ETP)</option><option value='3 : (6 - 10 ETP)'>3 : (6 - 10 ETP)</option><option value='4 : (> 10 ETP)'>4 : (&gt; 10 ETP)</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-impact_risque'>Impact Risque:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-impact_risque' name='field_impact_risque'>");
        sbResult.append("<option>" + impact_risque + "</option>");
        sbResult.append("<option value='1 : (Impact Faible -Cible Limitée)'>1 : (Impact Faible -Cible Limitée)</option><option value='2 : (Impact Moyen - Cible Limitée)'>2 : (Impact Moyen - Cible Limitée)</option><option value='3 : (Impact Important - Cible Importante)'>3 : (Impact Important - Cible Importante)</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMETIER-impact_performance'>Impact Performance SI:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMETIER-impact_performance' name='field_impact_performance'>");
        sbResult.append("<option>" + impact_performance + "</option>");
        sbResult.append("<option value='OUI'>OUI</option><option value='NON'>NON</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-impact_autres'>Autres Impacts:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<input type='text' id='fieldMETIER-impact_autres' name='field_impact_autres' size='70' value='");
        sbResult.append(impact_autres);
        sbResult.append("'>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-contraintesImpact'>Contraintes/Commentaires:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='fullrow' colspan='3'>");
        sbResult.append("<fieldset>");
        sbResult.append("<div class='wikitoolbar'><a href='#' id='strong' title='Texte en gras: '''Exemple'''' tabindex='400'></a><a href='#' id='em' title='Texte en italique: ''Exemple''' tabindex='400'></a><a href='#' id='heading' title='Titre: == Exemple ==' tabindex='400'></a><a href='#' id='link' title='Lien: [http://www.exemple.com/ Exemple]' tabindex='400'></a><a href='#' id='code' title='Bloc de code: {{{ exemple }}}' tabindex='400'></a><a href='#' id='hr' title='Filet horizontal: ----' tabindex='400'></a><a href='#' id='np' title='Nouveau paragraphe' tabindex='400'></a><a href='#' id='br' title='Saut de ligne: [[BR]]' tabindex='400'></a><a href='#' id='img' title='Image: [[Image()]]' tabindex='400'></a></div><div class='trac-resizable'><div><textarea id='fieldMETIER-contraintesImpact' name='field_description' class='wikitext trac-resizable' rows='10' cols='68'>");
        sbResult.append(contraintesImpact);
        sbResult.append("</textarea><div class='trac-grip' style='margin-left: 2px; margin-right: -8px;'></div></div></div>");
        sbResult.append("</fieldset>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-metier_concerne'>Métiers concernés/impactés:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMETIER-metier_concerne' name='field_metier_concerne'>");
        sbResult.append("<option>" + metier_concerne + "</option>");
        sbResult.append("<option>DEPARTEMENT_CONTROLE_DE_GESTION</option><option>DEPARTEMENT_CONTROLE_GENERAL</option><option>DEPARTEMENT_DES_OPERATIONS_BANCAIRES</option><option>DEPARTEMENT_FINANCE_-_COMPTABILITE</option><option>DEPARTEMENT_MAITRISE_D'OUVRAGE_ET_COORDINATION_METIERS</option><option>DEPARTEMENT_RECOUVREMENT_ET_CONTENTIEUX</option><option>DEPARTEMENT_RISQUES</option><option>DEPARTEMENT_SYSTEMES_D'INFORMATION</option><option>DGA_BANQUE_DE_DETAIL</option><option>DGA_RESSOURCES</option><option>DIRECTION_GENERALE</option><option>POLE_STRATEGIE_ET_BANQUE_DE_FINANCEMENT_ET_D'INVESTISSEMENT</option><option>DIRECTION_CENTRALE_PLANIFICATION_&amp;_BUDGET</option><option>DIRECTION_CENTRALE_RESSOURCES_HUMAINES</option><option>POLE_GRANDES_ENTREPRISES_ET_INSTITUTIONNELS</option><option>POLE_INVESTISSEURS</option><option>ASSURANCE_BIAT</option><option>BIAT_CAPITAL</option><option>LA_PROTECTRICE</option><option>DIRECTION_PMO_BANQUE</option><option>EQUIPE_REFONTE</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMETIER-client_impact'>Clients Impactés:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMETIER-client_impact' name='field_client_impact'>");
        sbResult.append("<option>" + client_impact + "</option>");
        sbResult.append("<option>Clients_BDD</option><option>Clients_PGEI</option><option>Clients_Personne_Physique</option><option>Clients_Profesionnel</option><option>Clients_Personne_Morale</option><option>Autres</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        sbResult.append("<tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMETIER-date_realisation'>Date de réalisation souhaitée: </label>");
        sbResult.append("</th>");
        sbResult.append("<td class='tdDemi'>");
        sbResult.append("<p class='contenu'> ");
        sbResult.append("<input type='text' id='fieldMETIER-date_realisation'  class='datePicker' value='");
        sbResult.append(date_realisation);
        sbResult.append("'>");
        sbResult.append("</p>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        sbResult.append("</tbody>");
        
        sbResult.append("<tr><td  align='center' colspan='4'>");
        sbResult.append("<input type='button' class='boutonValider' id='bouttonMETIER-validerBesoin'  value='enregistrer les modifications' onclick=\"mergeBesoinIntoDBbyField('METIER', 'update');\"/>");
        sbResult.append("</td></tr></tbody>");
        
        resultMap.put("impactBloc", sbResult.toString());
        sbResult.setLength(0);

        //partiesPrenantes
        sbResult.append("<tbody><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-resmoa'>Direction MOA:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-resmoa' name='fieldMOA_resmoa'>");
        sbResult.append("<option>" + resmoa + "</option>");
        sbResult.append("<option>" + resmoa + "</option><option value='DIRECTION_CONCEPTION_ET_ORGANISATION_COMPTABLE'>DIRECTION_CONCEPTION_ET_ORGANISATION_COMPTABLE</option><option value='DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION'>DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION</option><option value='DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT'>DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT</option><option value='DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES'>DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES</option><option value='DIRECTION_MOA_INTERNATIONAL'>DIRECTION_MOA_INTERNATIONAL</option><option value='DIRECTION_MOA_MOYENS_DE_PAIEMENT'>DIRECTION_MOA_MOYENS_DE_PAIEMENT</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-vis_a_vis_moa'>Vis-à-vis MOA:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-vis_a_vis_moa' name='fieldMOA_vis_a_vis_moa'>");
        sbResult.append("<option>" + vis_a_vis_moa + "</option>");
        sbResult.append("<option value='abdeljlil.regaieg'>abdeljlil.regaieg</option><option value='abdessattar.guetari'>abdessattar.guetari</option><option value='ali.dallagi'>ali.dallagi</option><option value='aymen.loukil'>aymen.loukil</option><option value='bassem.hamdi'>bassem.hamdi</option><option value='bechir.nouioui'>bechir.nouioui</option><option value='dhouha.ayadi'>dhouha.ayadi</option><option value='faouzi.fourati'>faouzi.fourati</option><option value='hassen.ghorbel'>hassen.ghorbel</option><option value='hassen.longo'>hassen.longo</option><option value='imen.selmi'>imen.selmi</option><option value='intissar.abdelli'>intissar.abdelli</option><option value='jalel.ellouze'>jalel.ellouze</option><option value='jamel.ben.tkhayat'>jamel.ben.tkhayat</option><option value='kamel.malouche'>kamel.malouche</option><option value='kaouther.hachicha'>kaouther.hachicha</option><option value='lassaad.hakimi'>lassaad.hakimi</option><option value='makki.sahnoun'>makki.sahnoun</option><option value='marouene.ben.soltane'>marouene.ben.soltane</option><option value='mohamed.bouattour'>mohamed.bouattour</option><option value='mohamed.el.euch'>mohamed.el.euch</option><option value='mohamed.tamedda'>mohamed.tamedda</option><option value='mohamed.tarik.ben.jalloun'>mohamed.tarik.ben.jalloun</option><option value='mohamed.walid.hamdi'>mohamed.walid.hamdi</option><option value='mongi.bouassida'>mongi.bouassida</option><option value='najoua.ellouze'>najoua.ellouze</option><option value='nasreddine.rouabeh'>nasreddine.rouabeh</option><option value='niazi.jedidi'>niazi.jedidi</option><option value='rachid.fetoui'>rachid.fetoui</option><option value='radhouane.khayati'>radhouane.khayati</option><option value='rakia.moalla'>rakia.moalla</option><option value='ramy.aydi'>ramy.aydi</option><option value='rym.ben.ayed.mhiri'>rym.ben.ayed.mhiri</option><option value='rym.ben.yahia'>rym.ben.yahia</option><option value='safa.benlatifa'>safa.benlatifa</option><option value='sami.babbou'>sami.babbou</option><option value='sonia.moalla'>sonia.moalla</option><option value='tarek.elleuch'>tarek.elleuch</option><option value='tarek.sellami'>tarek.sellami</option><option value='thouraya.zghal'>thouraya.zghal</option><option value='yamen.khemir'>yamen.khemir</option><option value='yosra.grami'>yosra.grami</option><option value='zeineb.labidi'>zeineb.labidi</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-directions_moa'>Autre direction(s) MOA:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-directions_moa' name='fieldMOA_directions_moa'>");
        sbResult.append("<option>" + directions_moa + "</option><option>DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION</option><option>DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT</option><option>DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES</option><option>DIRECTION_MOA_INTERNATIONAL</option><option>DIRECTION_MOA_MOYENS_DE_PAIEMENT</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-directions_moe'>Direction(s) MOE:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-directions_moe' name='fieldMOA_directions_moe'>");
        sbResult.append("<option>" + directions_moe + "</option><option>COORDINATION_INFRASTRUCTURE_ET_PRODUCTION</option><option>DIRECTION_DEVELOPPEMENTS_SI</option><option>DIRECTION_PROLOGICIELS_HORS_C24_ET_INTRANET</option><option>DIRECTION_REFERENTIEL_SI_ET_DECISIONNEL</option><option>DIRECTION_URBANISATION_DU_SI_ET_INTEGRATION</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-visavis_technique'>Vis-à-vis Technique:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-visavis_technique' name='fieldMOA_visavis_technique'>");
        sbResult.append("<option>" + visavis_technique + "</option>");
        sbResult.append("<option value='abdelkhalek.chaari'>abdelkhalek.chaari</option><option value='ahmed.daadaa'>ahmed.daadaa</option><option value='akram.lassoued'>akram.lassoued</option><option value='amir.ktari'>amir.ktari</option><option value='anis.sboui'>anis.sboui</option><option value='aymen.dammak'>aymen.dammak</option><option value='chedly.ben.ammar'>chedly.ben.ammar</option><option value='emna.ben.chabchoubi'>emna.ben.chabchoubi</option><option value='faten.zhioua'>faten.zhioua</option><option value='ferjani.riahi'>ferjani.riahi</option><option value='hafedh.boukadida'>hafedh.boukadida</option><option value='hatem.chaaben'>hatem.chaaben</option><option value='ibtihel.troudi'>ibtihel.troudi</option><option value='ikbel.hamdi'>ikbel.hamdi</option><option value='khalil.fekih'>khalil.fekih</option><option value='khalid.ka'>khalid.ka</option><option value='laroussi.jlidi'>laroussi.jlidi</option><option value='meha.meftah'>meha.meftah</option><option value='mehdi.zaibi'>mehdi.zaibi</option><option value='mohamed.benabdesslem'>mohamed.benabdesslem</option><option value='mohamed.fares.bouzaienne'>mohamed.fares.bouzaienne</option><option value='mohsen.ouertani'>mohsen.ouertani</option><option value='moncef.mallek'>moncef.mallek</option><option value='mondher.moalla'>mondher.moalla</option><option value='najoua.jaibi'>najoua.jaibi</option><option value='omar.mahmoud'>omar.mahmoud</option><option value='salem.belhajsalem'>salem.belhajsalem</option><option value='sami.fe'>sami.fe</option><option value='sami.lassoued'>sami.lassoued</option><option value='sami.loukil'>sami.loukil</option><option value='sarra.sakesli'>sarra.sakesli</option><option value='skander.amdouni'>skander.amdouni</option><option value='walid.be'>walid.be</option><option value='yessine.bouafif'>yessine.bouafif</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-visavis_comptable'>Vis-à-vis Comptable:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-visavis_comptable' name='fieldMOA_visavis_comptable'>");
        sbResult.append("<option>" + visavis_comptable + "</option>");
        sbResult.append("<option value='bassem.hamdi'>bassem.hamdi</option><option value='intissar.abdelli'>intissar.abdelli</option><option value='lassaad.hakimi'>lassaad.hakimi</option><option value='nasreddine.rouabeh'>nasreddine.rouabeh</option><option value='niazi.jedidi'>niazi.jedidi</option><option value='radhouane.khayati'>radhouane.khayati</option><option value='tarek.elleuch'>tarek.elleuch</option><option value='yamen.khemiri'>yamen.khemiri</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-visavis_dsi'>Vis-à-vis DSI:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-visavis_dsi' name='fieldMOA_visavis_dsi'>");
        sbResult.append("<option>" + visavis_dsi + "</option>");
        sbResult.append("<option value='ahmed.ayadi'>ahmed.ayadi</option><option value='anis.benhamdene'>anis.benhamdene</option><option value='anis.garbouj'>anis.garbouj</option><option value='aymen.ben.bnina'>aymen.ben.bnina</option><option value='bilel.ismail'>bilel.ismail</option><option value='chaouki.bouchouicha'>chaouki.bouchouicha</option><option value='charfeddine.mabrouk'>charfeddine.mabrouk</option><option value='fethi.cheikh'>fethi.cheikh</option><option value='ghassen.bezzine'>ghassen.bezzine</option><option value='hamdi.belhadjali'>hamdi.belhadjali</option><option value='kamel.mabrouk'>kamel.mabrouk</option><option value='lassaad.srasra'>lassaad.srasra</option><option value='leila.mehri'>leila.mehri</option><option value='moez.bensalem'>moez.bensalem</option><option value='mohamed.bahy'>mohamed.bahy</option><option value='mongi.guesmi'>mongi.guesmi</option><option value='najeh.ennajeh'>najeh.ennajeh</option><option value='radhia.bencheikh'>radhia.bencheikh</option><option value='riadh.anouar.ben.dakhlia'>riadh.anouar.ben.dakhlia</option><option value='sami.bouaine'>sami.bouaine</option><option value='sinda.rahmouni'>sinda.rahmouni</option><option value='sofiene.moalla'>sofiene.moalla</option><option value='tarek.ayadi'>tarek.ayadi</option><option value='thameur.bensalem'>thameur.bensalem</option><option value='wajih.mili'>wajih.mili</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        //sbResult.append("<tr><th class='col1'>");
        //sbResult.append("<label for='fieldMOA-interv_externes'>Intervenants externes:</label>");
        //sbResult.append("</th>");
        //sbResult.append("<td class='col1' colspan='3'><div style='display: none;'>");
        //sbResult.append("<div class='wikitoolbar'><a href='#' id='strong' title='Texte en gras: '''Exemple'''' tabindex='400'></a><a href='#' id='em' title='Texte en italique: ''Exemple''' tabindex='400'></a><a href='#' id='heading' title='Titre: == Exemple ==' tabindex='400'></a><a href='#' id='link' title='Lien: [http://www.exemple.com/ Exemple]' tabindex='400'></a><a href='#' id='code' title='Bloc de code: {{{ exemple }}}' tabindex='400'></a><a href='#' id='hr' title='Filet horizontal: ----' tabindex='400'></a><a href='#' id='np' title='Nouveau paragraphe' tabindex='400'></a><a href='#' id='br' title='Saut de ligne: [[BR]]' tabindex='400'></a><a href='#' id='img' title='Image: [[Image()]]' tabindex='400'></a></div><div class='trac-resizable'><div><textarea id='fieldMOA-interv_externes' name='fieldMOA_interv_externes' cols='10' rows='1' class='wikitext trac-resizable' columns='true'>||=Nom=||=Jour Homme=||=Budget=||</textarea><div class='trac-grip' style='margin-left: 2px; margin-right: 1161px;'></div></div></div>");
        //sbResult.append("</div><table class='fieldoftable wiki' for='fieldMOA-interv_externes'><tbody><tr class='fieldoftableline'><th>Nom</th><th>Jour Homme</th><th>Budget</th></tr><tr class='lastline fieldoftableline'><td><input type='text' value=''></td><td><input type='text' value=''></td><td><input type='text' value=''></td></tr></tbody></table></td>");
        //sbResult.append("</tr>");
        sbResult.append("</tbody>");
        resultMap.put("partiesPrenantesBloc", sbResult.toString());
        sbResult.setLength(0);

        //impactSI
        sbResult.append("<tbody><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-fonctionnel'>Impact SI Fonctionnel:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-fonctionnel' name='fieldMOA_fonctionnel'>");
        sbResult.append("<option>" + fonctionnel + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-habilitation'>Impact SI Habilitation:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-habilitation' name='fieldMOA_habilitation'>");
        sbResult.append("<option>" + habilitation + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-editique'>Impact SI Editique:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-editique' name='fieldMOA_editique'>");
        sbResult.append("<option>" + editique + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-comptable'>Impact SI Comptable:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-comptable' name='fieldMOA_comptable'>");
        sbResult.append("<option>" + comptable + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-tarification'>Impact SI Tarification:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-tarification' name='fieldMOA_tarification'>");
        sbResult.append("<option>" + tarification + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-interfaces'>Impact SI Interfaces:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-interfaces' name='fieldMOA_interfaces'>");
        sbResult.append("<option>" + interfaces + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-migration'>Impact SI Migration:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldMOA-migration' name='fieldMOA_migration'>");
        sbResult.append("<option>" + migration + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldMOA-autres'>Impact SI Autres:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldMOA-autres' name='fieldMOA_autres'>");
        sbResult.append("<option>" + autres + "</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldMOA-appexterne'>Syst/Application externe:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<input type='text' id='fieldMOA-appexterne' name='fieldMOA_appexterne' value='");
        sbResult.append(appexterne);
        sbResult.append("'>");
        sbResult.append("</td>");
        sbResult.append("<th class='col2'>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        
        sbResult.append("<tr><td  align='center' colspan='4'>");
        sbResult.append("<input type='button' class='boutonValider' id='bouttonMOA-validerBesoin'  value='enregistrer les modifications' onclick=\"mergeBesoinIntoDBbyField('MOA', 'update');\"/>");
        sbResult.append("</td></tr></tbody>");
        
        resultMap.put("impactSIBloc", sbResult.toString());
        sbResult.setLength(0);

        //chiffrageMoa
        sbResult.append("<thead>");
        sbResult.append("<tr style='background-color: #F4F7EB'>");
        sbResult.append("<th>");
        sbResult.append("Chiffrage des travaux par Domaine");
        sbResult.append("</th>");
        sbResult.append("<th>");
        sbResult.append("j/h estimé");
        sbResult.append("</th>");
        sbResult.append("<th class='dial'>");
        sbResult.append("j/h realisé");
        sbResult.append("</th>");
        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA BDD</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_0_col_1' class='ciffrageMOA single-line'>" + lig_0_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_0_col_2' class='ciffrageMOA dial'>" + lig_0_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA BFI</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_1_col_1' class='ciffrageMOA single-line'>" + lig_1_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_1_col_2' class='ciffrageMOA dial'>" + lig_1_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA Comptabilite</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_2_col_1' class='ciffrageMOA single-line'>" + lig_2_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_2_col_2' class='ciffrageMOA dial'>" + lig_2_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA Engagement</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_3_col_1' class='ciffrageMOA single-line'>" + lig_3_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_3_col_2' class='ciffrageMOA dial'>" + lig_3_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA International</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_4_col_1' class='ciffrageMOA single-line'>" + lig_4_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_4_col_2'class='ciffrageMOA dial'>" + lig_4_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA MDP</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_5_col_1' class='ciffrageMOA single-line'>" + lig_5_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_5_col_2' class='ciffrageMOA dial'>" + lig_5_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOA' style='background-color: #F4F7EB'>MOA CDC</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOA-lig_6_col_1' class='ciffrageMOA single-line'>" + lig_6_col_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_6_col_2' class='ciffrageMOA dial'>" + lig_6_col_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='total' style='background-color: #F4F7EB'>Total (j/h)</td>");
        sbResult.append("<td id='fieldCMOA-lig_somme_1' class='ciffrageMOA dial'>" + lig_somme_1 + "</td>");
        sbResult.append("<td id='fieldCMOA-lig_somme_2' class='ciffrageMOA dial'>" + lig_somme_2 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("</tbody>");
 
        sbResult.append("<tfoot><tr><td  align='center' colspan='3'>");
        sbResult.append("<input type='button' class='boutonValider' id='bouttonCMOA-validerBesoin'  value='enregistrer les modifications' onclick=\"mergeBesoinIntoDBbyField('CMOA', 'update');\"/>");
        sbResult.append("</td></tr></tfoot>");
        
        resultMap.put("chiffrageMoaBloc", sbResult.toString());
        sbResult.setLength(0);

        //chiffrageMoe
        sbResult.append("<thead>");
        sbResult.append("<tr style='background-color: rgb(250, 242, 237)'>");
        sbResult.append("<th>");
        sbResult.append("Chiffrage des travaux par Domaine");
        sbResult.append("</th>");
        sbResult.append("<th>");
        sbResult.append("j/h estimé");
        sbResult.append("</th>");
        sbResult.append("<th class='dial'>");
        sbResult.append("j/h realisé");
        sbResult.append("</th>");
        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Coordination Etudes et Dev SI</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_0_col_3' class='ciffrageMOE single-line' value='" + lig_0_col_3 + "'>" + lig_0_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_0_col_4' class='ciffrageMOE dial' value='" + lig_0_col_4 + "'>" + lig_0_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Coordination Infrastructure et Production</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_1_col_3' class='ciffrageMOE single-line'>" + lig_1_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_1_col_4' class='ciffrageMOE dial'>" + lig_1_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Développements SI</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_2_col_3' class='ciffrageMOE single-line'>" + lig_2_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_2_col_4' class='ciffrageMOE dial'>" + lig_2_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Progiciels hors C24 et Intranet</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_3_col_3' class='ciffrageMOE single-line'>" + lig_3_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_3_col_4' class='ciffrageMOE dial'>" + lig_3_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Referentiel SI et Décisionnel</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_4_col_3' class='ciffrageMOE single-line'>" + lig_4_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_4_col_4' class='ciffrageMOE dial'>" + lig_4_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='tdChiffrageMOE' style='background-color: rgb(250, 242, 237)'>Dir Urbanisation du SI et Intégration</td>");
        sbResult.append("<td contenteditable='true' id='fieldCMOE-lig_5_col_3' class='ciffrageMOE single-line'>" + lig_5_col_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_5_col_4' class='ciffrageMOE dial'>" + lig_5_col_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("<tr>");
        sbResult.append("<td id='total' style='background-color: rgb(250, 242, 237)'>Total (j/h)</td>");
        sbResult.append("<td id='fieldCMOE-lig_somme_3' class='ciffrageMOE dial'>" + lig_somme_3 + "</td>");
        sbResult.append("<td id='fieldCMOE-lig_somme_4' class='ciffrageMOE dial'>" + lig_somme_4 + "</td>");
        sbResult.append("</tr>");
        sbResult.append("</tbody>");
        
        sbResult.append("<tfoot><tr><td  align='center' colspan='3'>");
        sbResult.append("<input type='button' class='boutonValider' id='bouttonCMOE-validerBesoin'  value='enregistrer les modifications' onclick=\"mergeBesoinIntoDBbyField('CMOE', 'update');\"/>");
        sbResult.append("</td></tr></tfoot>");

        resultMap.put("chiffrageMoeBloc", sbResult.toString());
        sbResult.setLength(0);

        //planification
        sbResult.append("<tbody><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldPMO-niveauprojet'>Niveau Projet:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldPMO-niveauprojet' name='fieldPMO_niveauprojet'>");
        sbResult.append("<option>" + niveauprojet + "</option>");
        sbResult.append("<option value='AMELIORATION FONCTIONNEMENT CHEQUEPARGNE'>AMELIORATION FONCTIONNEMENT CHEQUEPARGNE</option><option value='AMELIORATION PROCESSUS GESTION DES SUSPENS'>AMELIORATION PROCESSUS GESTION DES SUSPENS</option><option value='AMELIORATION_CARTE_DE_CREDIT'>AMELIORATION_CARTE_DE_CREDIT</option><option value='AMELIORATION_OD_RDI'>AMELIORATION_OD_RDI</option><option value='BANQUE PRIVEE'>BANQUE PRIVEE</option><option value='BFI MARCHE ETAPE 2'>BFI MARCHE ETAPE 2</option><option value='BFI TITRE 1-2-3'>BFI TITRE 1-2-3</option><option value='BFI_INTRANET'>BFI_INTRANET</option><option value='BIAT CASH'>BIAT CASH</option><option value='BORNE INTERACTIVE ETAPE1'>BORNE INTERACTIVE ETAPE1</option><option value='CARTE CORPORATE'>CARTE CORPORATE</option><option value='CARTE DE CREDIT SAFIR'>CARTE DE CREDIT SAFIR</option><option value='CARTE PREPAYEE INTERNATIONALE'>CARTE PREPAYEE INTERNATIONALE</option><option value='CARTE TECHNOLOGIQUE'>CARTE TECHNOLOGIQUE</option><option value='CARTE VISA EXPRESS CPTE EN DVS'>CARTE VISA EXPRESS CPTE EN DVS</option><option value='CASH POOLING'>CASH POOLING</option><option value='CHEQUIER PERSONNALISE'>CHEQUIER PERSONNALISE</option><option value='CLOTURE DE COMPTE AVEC CONTROLE DES INSTANCES'>CLOTURE DE COMPTE AVEC CONTROLE DES INSTANCES</option><option value='CMT en Devise OD'>CMT en Devise OD</option><option value='COMPENSATION MONETIQUE NATIONALE'>COMPENSATION MONETIQUE NATIONALE</option><option value='COMPENSATION_MONETIQUE_INTERNATIONALE'>COMPENSATION_MONETIQUE_INTERNATIONALE</option><option value='COMPTABILITE MANUELLE'>COMPTABILITE MANUELLE</option><option value='COMPTES DORMANTS COMPTABILITE'>COMPTES DORMANTS COMPTABILITE</option><option value='CONDITIONS PREFERENTIELLES POUR COMPTE EN DEVISE'>CONDITIONS PREFERENTIELLES POUR COMPTE EN DEVISE</option><option value='COURS DE DESAGREGATION'>COURS DE DESAGREGATION</option><option value='CREDIT RENOV'>CREDIT RENOV</option><option value='CREDITS EN DEVISE'>CREDITS EN DEVISE</option><option value='CRM GESTION DES CONTACTES ETAPE 3'>CRM GESTION DES CONTACTES ETAPE 3</option><option value='CRM GESTION DES CONTACTS ETAPE2'>CRM GESTION DES CONTACTS ETAPE2</option><option value='DECLARATION > 5000 TND SOUS DELEGATAIRE'>DECLARATION &gt; 5000 TND SOUS DELEGATAIRE</option><option value='DEVELOPPEMENT PME'>DEVELOPPEMENT PME</option><option value='E-BANKING'>E-BANKING</option><option value='E-BANKING ETAPE2'>E-BANKING ETAPE2</option><option value='EDITION ECHELLES INTERET'>EDITION ECHELLES INTERET</option><option value='ENGAGEMENT LOT3 PROCESSUS CREDITS DE GESTION'>ENGAGEMENT LOT3 PROCESSUS CREDITS DE GESTION</option><option value='ENGAGEMENT PROCESSUS DE CREDIT DE GESTION PHASE 1'>ENGAGEMENT PROCESSUS DE CREDIT DE GESTION PHASE 1</option><option value='ENRICHISSEMENT OD INTERNATIONAL'>ENRICHISSEMENT OD INTERNATIONAL</option><option value='ENTREE EN RELATION A DISTANCE TRE'>ENTREE EN RELATION A DISTANCE TRE</option><option value='ENTREE EN RELATION A DISTANCE TRE.2'>ENTREE EN RELATION A DISTANCE TRE.2</option><option value='EXECDENTS DE CAISSE'>EXECDENTS DE CAISSE</option><option value='FIABILISATION_DES_DONNEES_CLIENT'>FIABILISATION_DES_DONNEES_CLIENT</option><option value='GAB CASH DEPOSIT'>GAB CASH DEPOSIT</option><option value='GAB CHANGE'>GAB CHANGE</option><option value='Gestion des frais de tenue de comptes professionnels en DVS'>Gestion des frais de tenue de comptes professionnels en DVS</option><option value='GESTION DES PACKS'>GESTION DES PACKS</option><option value='GESTION RETOUR COURRIER'>GESTION RETOUR COURRIER</option><option value='GESTION_DES_CONTACTS'>GESTION_DES_CONTACTS</option><option value='GESTION_DES_CONTACTS'>GESTION_DES_CONTACTS</option><option value='GIP'>GIP</option><option value='IMPAYES EN DEVISE'>IMPAYES EN DEVISE</option><option value='INTEGRATION T24-HR ACCESS'>INTEGRATION T24-HR ACCESS</option><option value='INTEGRATION_OPVCM'>INTEGRATION_OPVCM</option><option value='INTERFACE MEGARA SVT T24'>INTERFACE MEGARA SVT T24</option><option value='INTERNATIONAL GBIE'>INTERNATIONAL GBIE</option><option value='INTERNATIONAL REMISE IMPORT'>INTERNATIONAL REMISE IMPORT</option><option value='INTERNATIONAL_CDI'>INTERNATIONAL_CDI</option><option value='INTERNATIONAL_CGBIR'>INTERNATIONAL_CGBIR</option><option value='INTERNATIONAL_F1F2'>INTERNATIONAL_F1F2</option><option value='INTERNATIONAL_FDE'>INTERNATIONAL_FDE</option><option value='INTERNATIONAL_FDI'>INTERNATIONAL_FDI</option><option value='INTERNATIONAL_GF'>INTERNATIONAL_GF</option><option value='INTERNATIONAL_GFI'>INTERNATIONAL_GFI</option><option value='INTERNATIONAL_RE'>INTERNATIONAL_RE</option><option value='INTERNATIONAL_TCE'>INTERNATIONAL_TCE</option><option value='INTERNATIONAL_TE'>INTERNATIONAL_TE</option><option value='INTERNATIONAL_TR'>INTERNATIONAL_TR</option><option value='INTERNATIONAL_TR_ETAPE2'>INTERNATIONAL_TR_ETAPE2</option><option value='INTERNATIONAL_TRANSVERSE'>INTERNATIONAL_TRANSVERSE</option><option value='KYC'>KYC</option><option value='LOI FATCA'>LOI FATCA</option><option value='MAINTENANCE'>MAINTENANCE</option><option value='MAINTENANCE-MDP'>MAINTENANCE-MDP</option><option value='MEGARA'>MEGARA</option><option value='MIGRATION COMPLEMENT DE CREDITS'>MIGRATION COMPLEMENT DE CREDITS</option><option value='MOBILE BANKING'>MOBILE BANKING</option><option value='MODULE GENERIQUE DE COMMUNICATION AVEC T24'>MODULE GENERIQUE DE COMMUNICATION AVEC T24</option><option value='NEGO DES DEVISES AUPRES DE BNQS CONSOEURS'>NEGO DES DEVISES AUPRES DE BNQS CONSOEURS</option><option value='NOSTRO'>NOSTRO</option><option value='OD_BFI_TITRES (GR96)'>OD_BFI_TITRES (GR96)</option><option value='OD_GR84_ETAPE2'>OD_GR84_ETAPE2</option><option value='OD_INT_CDE'>OD_INT_CDE</option><option value='OD_INT_ENCAISS.CHQ (GR84)'>OD_INT_ENCAISS.CHQ (GR84)</option><option value='OPERATIONS ANNEXES CHANGE'>OPERATIONS ANNEXES CHANGE</option><option value='OPTION DE CHANGE'>OPTION DE CHANGE</option><option value='PACK BUSNESS'>PACK BUSNESS</option><option value='PACK LOW COST'>PACK LOW COST</option><option value='PACK OFFRE HAUT DE GAMME.2'>PACK OFFRE HAUT DE GAMME.2</option><option value='PACK PRO SANTE'>PACK PRO SANTE</option><option value='PACK TOUNESSNA DEVISES'>PACK TOUNESSNA DEVISES</option><option value='PACKS OFFRE HAUT DE GAMME'>PACKS OFFRE HAUT DE GAMME</option><option value='PACKS OFFRE JEUNES'>PACKS OFFRE JEUNES</option><option value='PRELEVEMENT EMIS ET RECUS HORS PLATEFORME'>PRELEVEMENT EMIS ET RECUS HORS PLATEFORME</option><option value='PROCESS CREDITS DE GESTION REVOLVING'>PROCESS CREDITS DE GESTION REVOLVING</option><option value='PROCESSUS CREDITS A L IMMOBILIER'>PROCESSUS CREDITS A L IMMOBILIER</option><option value='PROCESSUS CREDITS INVESTISSEMENT'>PROCESSUS CREDITS INVESTISSEMENT</option><option value='PROCESSUS CREDITS SUR RESSOURCES SPECIALES'>PROCESSUS CREDITS SUR RESSOURCES SPECIALES</option><option value='PROCESSUS DES CREDIT REVOLVING'>PROCESSUS DES CREDIT REVOLVING</option><option value='PROJET BFI MARCHE FOREX'>PROJET BFI MARCHE FOREX</option><option value='PROJET CHEQUE 32/33'>PROJET CHEQUE 32/33</option><option value='PROJET CONTENTIEUX LOT1'>PROJET CONTENTIEUX LOT1</option><option value='PROJET DECLARATION MONATANT > 5000 TND'>PROJET DECLARATION MONATANT &gt; 5000 TND</option><option value='PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT1'>PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT1</option><option value='PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT2'>PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT2</option><option value='RATIO_DE_LIQUIDITE'>RATIO_DE_LIQUIDITE</option><option value='RECOUVREMENT AGENCE'>RECOUVREMENT AGENCE</option><option value='REFERENTIEL ENGAGEMENTS ETAPE 1'>REFERENTIEL ENGAGEMENTS ETAPE 1</option><option value='REFONTE DECLARATION DE L?EMPLOYEUR LOT2'>REFONTE DECLARATION DE L?EMPLOYEUR LOT2</option><option value='REFONTE_DECLARATION_DE_L_EMPLOYEUR'>REFONTE_DECLARATION_DE_L_EMPLOYEUR</option><option value='REMPLACEMENT GRAPPE 91/39(OD / CDE)'>REMPLACEMENT GRAPPE 91/39(OD / CDE)</option><option value='REMPLACEMENT GRAPPE 95(OD)'>REMPLACEMENT GRAPPE 95(OD)</option><option value='REMPLACEMENT_GRAPPE97_OD'>REMPLACEMENT_GRAPPE97_OD</option><option value='REVISION OPERATION CLOTURE DE COMPTE'>REVISION OPERATION CLOTURE DE COMPTE</option><option value='SITE DE TELECHARGEMENT'>SITE DE TELECHARGEMENT</option><option value='TARIFICATION / ECHELLES INTERET'>TARIFICATION / ECHELLES INTERET</option><option value='TARIFICATION PREFERENTIELLE PAR TRANSACTION'>TARIFICATION PREFERENTIELLE PAR TRANSACTION</option><option value='TELEMATIQUE 1'>TELEMATIQUE 1</option><option value='TOILETTAGE CHEQUES EN BO'>TOILETTAGE CHEQUES EN BO</option><option value='TRAITEMENT DES CARTES DE CREDIT SUR T24'>TRAITEMENT DES CARTES DE CREDIT SUR T24</option><option value='TRAITEMENT OBLIGATION CAUTIONNEE VIA TTN'>TRAITEMENT OBLIGATION CAUTIONNEE VIA TTN</option><option value='TRANSFERT AU CONTENTIEUX ETAPE 2'>TRANSFERT AU CONTENTIEUX ETAPE 2</option><option value='TRANSFERT ENGAGEMENTS COMPTE A COMPTE'>TRANSFERT ENGAGEMENTS COMPTE A COMPTE</option><option value='TRANSFERT ENGAGEMENTS ENTRE AGENCES'>TRANSFERT ENGAGEMENTS ENTRE AGENCES</option><option value='TRANSFERT TOUNESSNA'>TRANSFERT TOUNESSNA</option><option value='UTILISATION CHEQUIER BIAT/BCT'>UTILISATION CHEQUIER BIAT/BCT</option><option value='VIREMENTS RECUS VERS SC'>VIREMENTS RECUS VERS SC</option><option value='VIREMENTS_HORS_PLATEFORME'>VIREMENTS_HORS_PLATEFORME</option><option value='WESTERNE UNION'>WESTERNE UNION</option><option value='WS MULTIVIR T24'>WS MULTIVIR T24</option><option value='WS MXP T24'>WS MXP T24</option><option value='WS__MXP_T24_CHARGEMENT'>WS__MXP_T24_CHARGEMENT</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldPMO-projettrac'>Projet TRAC concerné:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldPMO-projettrac' name='fieldPMO_projettrac'>");
        sbResult.append("<option>" + projettrac + "</option>");
        sbResult.append("<option value=''></option><option value='ANOMALIES_T24'>ANOMALIES_T24</option><option value='BFI_CARTAGO_INTRANET'>BFI_CARTAGO_INTRANET</option><option value='BFI_TITRE'>BFI_TITRE</option><option value='CONTENTIEUX'>CONTENTIEUX</option><option value='GTI_WINSERGE'>GTI_WINSERGE</option><option value='HR_ACCESS'>HR_ACCESS</option><option value='MXP'>MXP</option><option value='OGC'>OGC</option><option value=''></option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldPMO-nature_traitement'>Nature de traitement:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldPMO-nature_traitement' name='fieldPMO_nature_traitement'>");
        sbResult.append("<option>" + nature_traitement + "</option>");
        sbResult.append("<option value=''></option><option value='PROJET'>PROJET</option><option value='MAINTENANCE'>MAINTENANCE</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        //sbResult.append("</tr>");
        //sbResult.append("<tr><th class='col2'>");
        //sbResult.append("<label for='fieldPMO-refticket'>Ref ticket maintenance:</label>");
        //sbResult.append("</th>");
        //sbResult.append("<td class='col2'>");
        //sbResult.append("<input type='text' id='fieldPMO-refticket' name='fieldPMO_refticket'>");
        //sbResult.append("</td>");
        //sbResult.append("</tr>");
        sbResult.append("<tr><th class='col1'>");
        sbResult.append("<label for='fieldPMO-type_traitement'>Type de traitement:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col1'>");
        sbResult.append("<select id='fieldPMO-type_traitement' name='fieldPMO_type_traitement'>");
        sbResult.append("<option>" + type_traitement + "</option>");
        sbResult.append("<option value=''></option><option value='RELEASE'>RELEASE</option><option value='HOTFIX'>HOTFIX</option><option value='MODIFICATION_A_CHAUD'>MODIFICATION_A_CHAUD</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr><tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldPMO-num_release'>Num Release:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldPMO-num_release' name='fieldPMO_num_release'>");
        sbResult.append("<option>" + num_release + "</option>");
        sbResult.append("<option value='R11'>R11</option><option value='R12'>R12</option><option value='R13'>R13</option><option value='R14'>R14</option><option value='R15'>R15</option><option value='R16'>R16</option><option value='R17'>R17</option><option value='R18'>R18</option><option value='R19'>R19</option><option value='R20'>R20</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        sbResult.append("<tr>");
        sbResult.append("<th class='col1'>");
        sbResult.append("<label for='fieldPMO-date_demarrage'>Date de démarrage:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='tdDemi'>");
        sbResult.append("<p class='contenu'> ");
        sbResult.append("<input type='text' class='datePicker' id='fieldPMO-date_demarrage' value='");
        sbResult.append(date_demarrage);
        sbResult.append("'>");
        sbResult.append("</p>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        sbResult.append("<tr>");
        sbResult.append("<th class='col2'>");
        sbResult.append("<label for='fieldPMO-date_app_prod'>Date Application en Prod:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='tdDemi'>");
        sbResult.append("<p class='contenu'> ");
        sbResult.append("<input type='text'class='datePicker' id='fieldPMO-date_app_prod' value='");
        sbResult.append(date_app_prod);
        sbResult.append("'>");
        sbResult.append("</p>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        sbResult.append("<tr>");
        sbResult.append("<th><label for='fieldPMO-invest'>Investissement:</label></th>");
        sbResult.append("<td class='fullrow' colspan='3'>");
        sbResult.append("<fieldset>");
        sbResult.append("<div class='wikitoolbar'><a href='#' id='strong' title='Texte en gras: '''Exemple'''' tabindex='400'></a><a href='#' id='em' title='Texte en italique: ''Exemple''' tabindex='400'></a><a href='#' id='heading' title='Titre: == Exemple ==' tabindex='400'></a><a href='#' id='link' title='Lien: [http://www.exemple.com/ Exemple]' tabindex='400'></a><a href='#' id='code' title='Bloc de code: {{{ exemple }}}' tabindex='400'></a><a href='#' id='hr' title='Filet horizontal: ----' tabindex='400'></a><a href='#' id='np' title='Nouveau paragraphe' tabindex='400'></a><a href='#' id='br' title='Saut de ligne: [[BR]]' tabindex='400'></a><a href='#' id='img' title='Image: [[Image()]]' tabindex='400'></a></div><div class='trac-resizable'><div><textarea id='fieldPMO-invest' name='fieldPMO_invest' class='wikitext trac-resizable' cols='120' rows='5'>");
        sbResult.append(invest);
        sbResult.append("</textarea><div class='trac-grip' style='margin-left: 2px; margin-right: -8px;'></div></div></div>");
        sbResult.append("</fieldset>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        sbResult.append("<tr>");
        sbResult.append("<th><label for='fieldPMO-commentairepmo'>Commentaires:</label></th>");
        sbResult.append(" <td class='fullrow' colspan='3'>");
        sbResult.append("<fieldset>");
        sbResult.append("<div class='wikitoolbar'><a href='#' id='strong' title='Texte en gras: '''Exemple'''' tabindex='400'></a><a href='#' id='em' title='Texte en italique: ''Exemple''' tabindex='400'></a><a href='#' id='heading' title='Titre: == Exemple ==' tabindex='400'></a><a href='#' id='link' title='Lien: [http://www.exemple.com/ Exemple]' tabindex='400'></a><a href='#' id='code' title='Bloc de code: {{{ exemple }}}' tabindex='400'></a><a href='#' id='hr' title='Filet horizontal: ----' tabindex='400'></a><a href='#' id='np' title='Nouveau paragraphe' tabindex='400'></a><a href='#' id='br' title='Saut de ligne: [[BR]]' tabindex='400'></a><a href='#' id='img' title='Image: [[Image()]]' tabindex='400'></a></div><div class='trac-resizable'><div><textarea id='fieldPMO-commentairepmo' name='fieldPMO_commentairepmo' class='wikitext trac-resizable' rows='10' cols='68'>");
        sbResult.append(commentairepmo);
        sbResult.append("</textarea><div class='trac-grip' style='margin-left: 2px; margin-right: -8px;'></div></div></div>");
        sbResult.append("</fieldset>");
        sbResult.append("</td>");
        sbResult.append("</tr>");

        //sbResult.append("<tr><th class='col1'>");
        //sbResult.append("<label for='fieldPMO-responsablepmo'>Responsable PMO:</label>");
        //sbResult.append("</th>");
        //sbResult.append("<td class='col1'>");
        //sbResult.append("<input type='text' id='fieldPMO-responsablepmo' name='fieldPMO_responsablepmo' value=''>");
        //sbResult.append("</td>");
        //sbResult.append("</tr><tr>");
        sbResult.append("<tr><th class='col2'>");
        sbResult.append("<label for='fieldPMO-owner'>Propriétaire:</label>");
        sbResult.append("</th>");
        sbResult.append("<td class='col2'>");
        sbResult.append("<select id='fieldPMO-owner' name='fieldPMO_owner'>");
        sbResult.append("<option>" + owner + "</option>");
        sbResult.append("<option value='< default >'>&lt; default &gt;</option><option value='abdeljalil.regaieg'>abdeljalil.regaieg</option><option value='abdeljlil.regaieg'>abdeljlil.regaieg</option><option value='ahmed.lasram'>ahmed.lasram</option><option value='ali'>ali</option><option value='anis.moalla'>anis.moalla</option><option value='fares hamza'>fares hamza</option><option value='fares.hamza'>fares.hamza</option><option value='faten slim'>faten slim</option><option value='faten.slim'>faten.slim</option><option value='ferjani.riahi'>ferjani.riahi</option><option value='hamza ouri (talys consulting)'>hamza ouri (talys consulting)</option><option value='kaouther.hachicha'>kaouther.hachicha</option><option value='moe1'>moe1</option><option value='mohamed.bouattour'>mohamed.bouattour</option><option value='mohsen ouertani'>mohsen ouertani</option><option value='mohsen.ouertani'>mohsen.ouertani</option><option value='molka borchani'>molka borchani</option><option value='molka.borchani'>molka.borchani</option><option value='moncef mallek'>moncef mallek</option><option value='mouna.laroussi'>mouna.laroussi</option><option value='nasreddine.rouabeh'>nasreddine.rouabeh</option><option value='nizar.jarboui'>nizar.jarboui</option><option value='raafet'>raafet</option><option value='raafet dormok'>raafet dormok</option><option value='riadh lamouri'>riadh lamouri</option><option value='safwen ben said'>safwen ben said</option><option value='safwen.ben.said'>safwen.ben.said</option><option value='safwen.bensaid'>safwen.bensaid</option><option value='samir jribi'>samir jribi</option><option value='sonia.moalla'>sonia.moalla</option><option value='yamen khemiri'>yamen khemiri</option>");
        sbResult.append("</select>");
        sbResult.append("</td>");
        sbResult.append("</tr>");
        
        sbResult.append("<tr><td  align='center' colspan='2'>");
        sbResult.append("<input type='button' class='boutonValider' id='bouttonPMO-validerBesoin'  value='enregistrer les modifications' onclick=\"mergeBesoinIntoDBbyField('PMO', 'update');mergeBesoinIntoDBbyField('METIER', 'insertFormation');\"/>");
        sbResult.append("</td></tr></tbody>");
        
        resultMap.put("planificationBloc", sbResult.toString());
        sbResult.setLength(0);

        //autre
        sbResult.append("<tbody>");
        if (ticketHasAttachement) {
            sbResult.append("<tr>");
            sbResult.append("<td class='fieldcontainer col2'>");
            sbResult.append("<center><input type='button' class='boutonValider' id='attachfilebutton'  value='Consulter le ticket du Besoin' onclick=\"openTracTicketInNewTab(\'").append(ticket.getId().toString()).append("\',\'GESTION_DEMANDES\');\"/> </center>");
            sbResult.append("</td>");
            sbResult.append("<td class='fieldcontainer col2'>     ");
            sbResult.append("<center><input type='button' class='boutonValider' id='attachfilebutton'  value='Voir fichiers joints' onclick=\"openTracTicketInNewTab(\'").append(ticket.getId().toString()).append("\',\'SEE_ATTACHEMENT\');\"/> </center>");
            sbResult.append("</td>");
            sbResult.append("<tr>");
        } else {
            sbResult.append("</tr>");
            sbResult.append("<td class='fieldcontainer col2'>");
            sbResult.append("<center><input type='button' class='boutonValider' id='attachfilebutton'  value='Consulter le ticket du Besoin' onclick=\"openTracTicketInNewTab(\'").append(ticket.getId().toString()).append("\',\'GESTION_DEMANDES\');\"/> </center>");
            sbResult.append("</td>");
            sbResult.append("<td class='fieldcontainer col2'>");
            sbResult.append("<center><input type='button' class='boutonValider' id='attachfilebutton'  value='Joindre un fichier' onclick=\"openTracTicketInNewTab(\'").append(ticket.getId().toString()).append("\',\'NEW_ATTACHEMENT\');\"/> </center>");
            sbResult.append("</td>");
            sbResult.append("</tr>");
        }
        sbResult.append("</tbody>");
        resultMap.put("autreBloc", sbResult.toString());
        System.out.println(sbResult.toString());
        sbResult.setLength(0);

        resultat = Tools.objectToJsonString(resultMap);
        return resultat;
    }

    public String getHtmlDemandesMetierTable(boolean authorizedAdmin, Map<String, List<Map<String, Object>>> mapDemandesMetier, String connectedUser, HttpServletRequest request) {
        String typeReporting = request.getParameter("typeReporting");
        int comp = 0;
        String cle = typeReporting;
        int largeur = 9;
        int nbrreportings = 1;
        Map<String, String> statusTypeReportingMap = new HashMap<>();
        statusTypeReportingMap.put("NOUVEAU_BESOIN", "Besoins en cours de rédaction / en attente de validation Métier");
        statusTypeReportingMap.put("VALIDE_METIER", "Besoins en attente de prise en charge par la MOA");
        statusTypeReportingMap.put("VALIDE_IMPACT", "Besoins en attente de chiffrage par les équipes Métiers");
        statusTypeReportingMap.put("VALIDE_MOA", "Besoins en attente de chiffrage par les équipes IT");
        statusTypeReportingMap.put("VALIDE_MOE", "Besoin en attente de planification");
        statusTypeReportingMap.put("VALIDE_PMO", "Besoins Métier validés et plannifiés");
        statusTypeReportingMap.put("NON_VALIDE", "Besoins Métier Annulés");
        StringBuilder sbResult = new StringBuilder();
        sbResult.append("<table id='tableTicketsHotfix' class='roundCornerTable'>");
        sbResult.append("<thead>");
        sbResult.append("<tr>");
        sbResult.append("<th>Nbr</th>");
        sbResult.append("<th>ID Projet</th>");
        sbResult.append("<th>Créée par</th>");
        sbResult.append("<th>Intitulé</th>");
        sbResult.append("<th>Axe Métier</th>");
        sbResult.append("<th>Status</th>");
        sbResult.append("<th>Desctiption</th>");
        if (!cle.equals("VALIDE_PMO") && !cle.equals("NON_VALIDE")) {
            sbResult.append("<th>Validation</th>");
            sbResult.append("<th>Annulation</th>");
        }
        sbResult.append("</tr>");
        sbResult.append("</thead>");
        sbResult.append("<tbody style='background-color: white;'>");
        for (int i = 0; i < nbrreportings; i++) {
            if (typeReporting.equals("ALL")) {
                nbrreportings = 6;
                if (i == 0) {
                    cle = "NOUVEAU_BESOIN";
                } else if (i == 1) {
                    cle = "VALIDE_METIER";
                } else if (i == 2) {
                    cle = "VALIDE_IMPACT";
                } else if (i == 3) {
                    cle = "VALIDE_MOA";
                } else if (i == 4) {
                    cle = "VALIDE_MOE";
                } else if (i == 5) {
                    cle = "VALIDE_PMO";
                } else if (i == 6) {
                    cle = "NON_VALIDE";
                }
            }

            sbResult.append("<tr class='titre'><td colspan='").append(largeur).append("'>" + statusTypeReportingMap.get(cle) + "</td></tr>");
            comp = 0;
            if (mapDemandesMetier.containsKey(cle) && mapDemandesMetier.get(cle).size() > 0) {
                for (Map<String, Object> mapDetails : mapDemandesMetier.get(cle)) {
                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                    Integer idTicket = ticket.getId();
                    comp++;


                    //formattage date 
                    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
                    //récupération de la date courante 
                    Date currentTime = new Date();
                    //on crée la chaîne à partir de la date  
                    String today = formatter.format(currentTime);

                    String dateCreation = ConvertTimeTracToJavaDate(ticket.getTime() / 1000).substring(0, 10);

                    if (dateCreation.equals(today) && ticket.getReporter().equals(connectedUser)) {
                        sbResult.append("<tr class='couleur2'>");
                    } else if (dateCreation.equals(today) && !ticket.getReporter().equals(connectedUser)) {
                        sbResult.append("<tr class='couleur4'>");
                    } else if (!dateCreation.equals(today) && ticket.getReporter().equals(connectedUser)) {
                        sbResult.append("<tr class='couleur5'>");
                    } else {
                        sbResult.append("<tr>");
                    }

                    sbResult.append("<td>");
                    sbResult.append(comp).append(")");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append("<a class='lien numAnmalie'  onclick='OpenTicket(" + idTicket + ", \"" + cle + "\");'>");
                    sbResult.append("#").append(idTicket);
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticket.getReporter());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticket.getSummary());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticket.getMilestone());
                    sbResult.append("</td>");

                    sbResult.append("<td>");
                    sbResult.append(ticket.getStatus());
                    sbResult.append("</td>");

                    sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px; margin: auto'>");
                    sbResult.append("<a class='conteneur_info_bull'>");
                    sbResult.append("<img class='info-icon' src='images/info.png' alt='info'>");
                    sbResult.append("<span>");
                    sbResult.append(ticket.getDescription());
                    sbResult.append("</span>");
                    sbResult.append("</a>");
                    sbResult.append("</td>");

                    if (!cle.equals("VALIDE_PMO") && !cle.equals("NON_VALIDE")) {
                        //******************************** debut checkbox validation ********************************
                        sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto'>");
                        if (authorizedAdmin) {
                            sbResult.append("<input type='checkbox' id='V" + idTicket + "' class='active_checkbox'");
                            sbResult.append("/>");
                            sbResult.append("<label for='V" + idTicket + "' class='css-label bout1'></label>");
                            sbResult.append("<input type='hidden' value='aval' id='ticketValide' name='ticketValide' class='ticketValide'/>");
                        } else {
                            sbResult.append("<center><img src='images/bloquer.png'  height='22' width='22'></center>");
                        }
                        sbResult.append("</td>");
                        //******************************** fin checkbox validation **********************************

                        //******************************** debut checkbox annulation *********************************                    
                        sbResult.append("<td style='padding-bottom: 2px; padding-top: 2px;  margin: auto; padding-left:0px;'>");
                        if (authorizedAdmin) {
                            sbResult.append("<input type='checkbox' id='A" + idTicket + "' class='active_checkbox'");
                            sbResult.append("/>");
                            sbResult.append("<label for='A" + idTicket + "' class='css-label bout2'></label>");
                            sbResult.append("<input type='hidden' value='rejet' id='ticketRejet' name='ticketRejet' class='ticketRejet'/>");
                        } else {
                            sbResult.append("<center><img src='images/trash.png'  height='22' width='22'></center>");
                        }
                        sbResult.append("</td>");
                        //******************************** fin checkbox annulation ********************************
                    }
                    sbResult.append("</tr>");
                }
            } else {
                sbResult.append("<tr><td colspan='").append(largeur).append("'>Aucun ticket à traiter</td></tr>");
            }
        }
        sbResult.append("</tbody>");
        sbResult.append("</table>");

        if (authorizedAdmin) {
            sbResult.append("<p><center><span id='messageResultatPersist' class='vert clignotant'></span></center></p>");
            sbResult.append("<div class='center'>");
            sbResult.append("<br><br><input type='button' class='boutonValider' id='validerDemandeCob' value='enregistrer les modifications' onclick='validerTickets();' />");
            sbResult.append("</div>");
        }

        return sbResult.toString();

    }
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
