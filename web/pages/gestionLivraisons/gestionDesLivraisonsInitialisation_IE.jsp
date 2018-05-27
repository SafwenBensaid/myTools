<%@page import="entitiesTrac.Ticket"%>
<%@page import="threads.AutomatisationDeploiementIeThread"%>
<%@page import="tools.Configuration"%>
<%@page import="java.util.*"%>
<%@page import="tools.Tools"%>
<%@page import="dto.*"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%

    Map<String, List<Map<String, Object>>> listPipeTickets = (Map<String, List<Map<String, Object>>>) request.getSession().getAttribute("mapPipeTickets");
    List<Map<String, Object>> listRelease = listPipeTickets.get(Configuration.parametresList.get("phaseRelease"));
    List<Map<String, Object>> listProjet = listPipeTickets.get("QUALIFICATION_PROJET");
    List<Map<String, Object>> listHotfix = listPipeTickets.get("HOT FIXE TEST");
    List<Map<String, Object>> listUpgrade = null;
    String phaseUpgrade = Configuration.parametresList.get("phaseUpgrade");
    if (phaseUpgrade.equals("QUALIFICATION_UPGRADE")) {
        listUpgrade = listPipeTickets.get("QUALIFICATION_UPGRADE");
    } else if (phaseUpgrade.equals("CERTIFICATION_UPGRADE")) {
        listUpgrade = listPipeTickets.get("CERTIFICATION_UPGRADE");
    } else {
        Tools.alertParEmail("PROBLEME PARAMETRAGE", "Veuillez vérifier le champ <b>phaseupgrade</b> dans la base de données de paramétrage, elle doit être QUALIFICATION_UPGRADE ou CERTIFICATION_UPGRADE");
    }
    //pipe d'harmonisation upgrade
    List<Map<String, Object>> listHarmonistaionUpgrade = listPipeTickets.get("HARMONISATION_C.UPGRADE");

    List<Map<String, Object>> listHotfixHarmRelease = listPipeTickets.get("HARMONISATION_C.RELEASE");
    List<Map<String, Object>> listHotfixHarmProjet = listPipeTickets.get("HARMONISATION_C.PROJET");
    List<Map<String, Object>> listActionsChaudTest = listPipeTickets.get("ACTION A CHAUD TEST");
    List<Map<String, Object>> listHotfixHarmOV = listPipeTickets.get("HARMONISATION_HF_OV");

%>

<html:form action="/GestionLivraisonsInputPacksEnvIE_Form" styleId="GestionLivraisonsInputPacksEnvIE_Form">
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Intégration des livraisons dans les serveurs de tests (I&E)</p></td>            
        </tr>

        <tr class="numLivraisonDiv groupe0">
            <td  class="tdDemi"><p class="titres">Numéro de livraison</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <html:text styleId="numLivraison" property="numLivraison" style="width:80px;margin-left: 5px;"/>      
                    <script>
                        var contextPath = "<%=request.getContextPath()%>";
                    </script>
                    <input type="button" class="boutonValiderPetit" id="getLivraisonDetails" onclick="invoquerServletGetTicketDetailsById(contextPath, 'OV');" style="margin-left: 10px" value="Valider" />
                    <%
                        String acteur = request.getParameter("acteur");
                        out.print("<input type='button' class='boutonValiderPetit' id='resetLivraisonsList' onclick='window.location = \"./getAllMilestonesForm.do?acteur=" + acteur + "\"' style='margin-left: 10px' value='Réinitialiser' />");
                    %>

                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Circuit</p></td>
            <td  class="tdDemi">                
                <p class="contenu">
                    <span id="RELEASE" class="notSelected" ><input type= "radio" name="circuit" value="RELEASE" onclick="selectCircuit('RELEASE');" id="CB_RELEASE"> RELEASE</span>
                    <span  style="margin-left: 23px" id="PROJET" class="notSelected" ><input type= "radio" name="circuit" value="PROJET" onclick="selectCircuit('PROJET');" id="CB_PROJET">PROJET</span>
                    <span  style="margin-left: 23px" id="HOTFIX" class="notSelected" ><input type= "radio" name="circuit" value="HOTFIX" onclick="selectCircuit('HOTFIX');" id="CB_HOTFIX">HOTFIX</span>
                    <span  style="margin-left: 3px" id="UPGRADE" class="notSelected" ><input type= "radio" name="circuit" value="UPGRADE" onclick="selectCircuit('UPGRADE');" id="CB_UPGRADE">UPGRADE</span>
                </p>
            </td>
        </tr>

        <tr id="niveauProjetTr" class="groupe1">
            <td  class="tdDemi"><p class="titres">Niveau Projet</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="niveauProjet" property="niveauProjet" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>

        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Contenu des livrables</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <html:text styleClass="readonly" styleId="contenuDesLivrables" property="contenuDesLivrables" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>

        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Numéro d'anomalie</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <html:text styleClass="readonly" styleId="numAnomalie" property="numAnomalie" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>

        <tr id="phaseTr" class="groupe1">
            <td  class="tdDemi"><p class="titres">Type</p></td>
            <td  class="tdDemi">
                <p class="contenu">                        
                    <html:text styleClass="readonly" styleId="phase" property="phase" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>



        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Priorité</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="priority" property="priority" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Milestone</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="milestone" property="milestone" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Component</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="component" property="component" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nature traitement</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="natureTraitement" property="natureTraitement" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nature de livraison</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="natureLivraison" property="natureLivraison" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>

        <tr>
            <td colspan="2">
                <div class="erreur">

                </div>
                <div class="erreur1">

                </div>
            </td>
        </tr>
        <tr id="loadingAnimationConteneur">
            <td colspan="2" >
                <%@include file="/pages/loadingAnimation.jsp" %>
            </td>
        </tr>
        <tr>
            <td colspan="2">
        <center  id="messageAlert" class="rouge">

        </center>
    </td>
</tr>
<sec:authorize access='hasAnyRole("BOUTON_PASSER_LIVRAISON_IE")'>
    <tr class="groupe1">
        <td colspan="2">
            <p class="bouton">                       
                <html:submit property="valider" value="Suivant" styleClass="boutonValider" />                    
            </p>
        </td>
    </tr>
</sec:authorize>
</table>






<br/><br/>            
<div id="conteneurTablesTickets">
    <body onload="slideMenu.build('sm', 770, 10, 10, 1)"/>

    <ul id="sm" class="sm" style="text-align: center; margin: 0 auto" >
        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextRelease" >
                            <%
                                Integer cmpNotif = 0;
                                String flag = "";
                                if (Configuration.etatCircuitMap.get("CR_DEP_TEST").trim().equals("OFF")) {
                                    flag = "OFF";
                                } else if (AutomatisationDeploiementIeThread.alertEnvoyeeCircuitBloqueCobMap.get("RELEASE") == true) {
                                    flag = "COB";
                                } else {
                                    if (listHotfixHarmRelease != null && listHotfixHarmRelease.size() > 0) {
                                        cmpNotif += listHotfixHarmRelease.size();
                                    }
                                    if (listRelease != null && listRelease.size() > 0) {
                                        cmpNotif += listRelease.size();
                                    }
                                    if (cmpNotif > 0) {
                                        flag = cmpNotif.toString();

                                    }
                                }
                                if (flag.trim().length() > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + flag + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Release
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Type du livrable
                                        </th>
                                        <th>
                                            Livré le
                                        </th>
                                        <th>
                                            Livré par
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%


                                        int cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Hotfix à harmoniser sur le C.R</td></tr>");
                                        if (listHotfixHarmRelease == null || listHotfixHarmRelease.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listHotfixHarmRelease) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr " + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }

                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Release</td></tr>");
                                        if (listRelease == null || listRelease.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listRelease) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>


        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextProjet">
                            <%
                                cmpNotif = 0;
                                flag = "";
                                if (Configuration.etatCircuitMap.get("CP_DEP_TEST").trim().equals("OFF")) {
                                    flag = "OFF";
                                } else if (AutomatisationDeploiementIeThread.alertEnvoyeeCircuitBloqueCobMap.get("PROJET") == true) {
                                    flag = "COB";
                                } else {
                                    if (listHotfixHarmProjet != null && listHotfixHarmProjet.size() > 0) {
                                        cmpNotif += listHotfixHarmProjet.size();
                                    }
                                    if (listProjet != null && listProjet.size() > 0) {
                                        cmpNotif += listProjet.size();
                                    }
                                    if (cmpNotif > 0) {
                                        flag = cmpNotif.toString();

                                    }
                                }
                                if (flag.trim().length() > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + flag + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Projet
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Type du livrable
                                        </th>
                                        <th>
                                            Livré le
                                        </th>
                                        <th>
                                            Livré par
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Hotfix à harmoniser sur le C.P</td></tr>");
                                        if (listHotfixHarmProjet == null || listHotfixHarmProjet.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listHotfixHarmProjet) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }

                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Projet</td></tr>");
                                        if (listProjet == null || listProjet.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listProjet) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>


        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextHotfix">
                            <%
                                cmpNotif = 0;
                                flag = "";
                                if (Configuration.etatCircuitMap.get("HF_DEP_TEST").trim().equals("OFF")) {
                                    flag = "OFF";
                                } else if (AutomatisationDeploiementIeThread.alertEnvoyeeCircuitBloqueCobMap.get("HOTFIX") == true) {
                                    flag = "COB";
                                } else {
                                    if (listActionsChaudTest != null && listActionsChaudTest.size() > 0) {
                                        cmpNotif += listActionsChaudTest.size();
                                    }
                                    if (listHotfix != null && listHotfix.size() > 0) {
                                        cmpNotif += listHotfix.size();
                                    }
                                    if (cmpNotif > 0) {
                                        flag = cmpNotif.toString();

                                    }
                                }
                                if (flag.trim().length() > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + flag + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Hotfix
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Type du livrable
                                        </th>
                                        <th>
                                            Livré le
                                        </th>
                                        <th>
                                            Livré par
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Actions à chaud Test</td></tr>");
                                        if (listActionsChaudTest == null || listActionsChaudTest.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listActionsChaudTest) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }

                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Hotfix Test</td></tr>");
                                        if (listHotfix == null || listHotfix.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listHotfix) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>

        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextUpgrade">
                            <%
                                cmpNotif = 0;
                                flag = "";
                                if (Configuration.etatCircuitMap.get("CU_DEP_TEST").trim().equals("OFF")) {
                                    flag = "OFF";
                                } else if (AutomatisationDeploiementIeThread.alertEnvoyeeCircuitBloqueCobMap.get("UPGRADE") == true) {
                                    flag = "COB";
                                } else {
                                    if (listUpgrade != null && listUpgrade.size() > 0) {
                                        cmpNotif += listUpgrade.size();
                                    }
                                    if (cmpNotif > 0) {
                                        flag = cmpNotif.toString();

                                    }
                                }
                                if (flag.trim().length() > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + flag + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Upgrade
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Type du livrable
                                        </th>
                                        <th>
                                            Livré le
                                        </th>
                                        <th>
                                            Livré par
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
cmp = 1;
out.println("<tr class='titre'><td colspan='8'>Tickets HOTFIX à harmoniser sur le circuit Upgrade</td></tr>");
if (listHarmonistaionUpgrade == null || listHarmonistaionUpgrade.size() == 0) {
    out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
} else {
    for (Map<String, Object> mapDetails : listHarmonistaionUpgrade) {
        Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
        Integer numTicketLivraison = ticketLivraison.getId();
                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
            cmp++;
    }
}
out.println("<tr class='titre'><td colspan='8'>Tickets Upgrade</td></tr>");
if (listUpgrade == null || listUpgrade.size() == 0) {
    out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
} else {
    for (Map<String, Object> mapDetails : listUpgrade) {
        Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
        Integer numTicketLivraison = ticketLivraison.getId();
                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCoursIE(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTimeIE") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
            cmp++;
    }
}
%>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>

    </ul>
</div>



</html:form>

<script>

                        function refresh() {
                            setTimeout(function() {
                                if ($('#conteneurTablesTickets').css('display') !== 'none') {
                                    $("#resetLivraisonsList").click();
                                    refresh();
                                }
                            }, 60000);
                        }

                        $(document).ready(function() {
                            selectMenu('menuT24');
                            $(".groupe1").hide();
                            $("#loadingAnimationConteneur").hide();
                            $("#getLivraisonDetails").click(function() {
                                $("#conteneurTablesTickets").hide();
                                openTracTicketInNewTab($("#numLivraison").val(), "LIVRAISON");
                            });
                            refresh();
                        });

                        function preparerLivraison(idTicket, idAnomalie) {
                            idTicket = idTicket.replace(/#/g, "");
                            if (idAnomalie !== 0) {
                                idAnomalie = idAnomalie.replace(/#/g, "");
                            }
                            $("#numLivraison").val(idTicket);
                            $("#getLivraisonDetails").click();
                            if (idAnomalie !== 0) {
                                openTracTicketInNewTab(idAnomalie, "ANOMALIE");
                            }
                        }

</script>