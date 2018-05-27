<%@page import="entitiesTrac.Ticket"%>
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

<html:form action="/createObjectFileForm" styleId="createObjectFileForm">
    <br/><br/>
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Recette technique des livraisons (OV)</p></td>            
        </tr>
        <tr>
            <td  class="tdDemi"><p class="titres">Mnemonic Company</p></td>
            <td  class="tdDemi">                
                <p class="contenu">
                    <span id="TN1" class="notSelected" onclick="selectCompany('TN1');"><input type= "radio" name="mnemonic" value="TN1" onclick="selectCompany('TN1');" id="CB_TN1"> TN1</span>
                    <span  style="margin-left: 32px" id="BNK" class="notSelected" onclick="selectCompany('BNK');"><input type= "radio" name="mnemonic" value="BNK" onclick="selectCompany('BNK');" id="CB_BNK">BNK</span>
                    <script>
                        var contextPath = "<%=request.getContextPath()%>";
                    </script>
                    <span  style="margin-left: 16px" id="Autre" class="notSelected" onclick="selectCompany('Autre');"><input type= "radio" name="mnemonic" value="Autre" onclick="selectCompany('Autre');"  id="CB_Autre">AUTRE<html:text styleId="autreMnemonic" onchange="invoquerServletGetAllCompaniesMnemonics(this,contextPath);" property="autreMnemonic" style="width:52px; height:14px;margin-left: 5px"/></span>
                </p>
            </td>
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
                    <input type="button" class="boutonValiderPetit" id="resetLivraisonsList" onclick="window.location = './getAllMilestonesForm.do?acteur=OV'" style="margin-left: 10px" value="Réinitialiser" />
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Circuit</p></td>
            <td  class="tdDemi">                
                <p class="contenu">
                    <span id="RELEASE" class="notSelected" ><input type= "radio" name="circuit" value="RELEASE" onclick="selectCircuit('RELEASE');" id="CB_RELEASE"> RELEASE</span>
                    <span  style="margin-left: 3px" id="PROJET" class="notSelected" ><input type= "radio" name="circuit" value="PROJET" onclick="selectCircuit('PROJET');" id="CB_PROJET">PROJET</span>
                    <span  style="margin-left: 3px" id="HOTFIX" class="notSelected" ><input type= "radio" name="circuit" value="HOTFIX" onclick="selectCircuit('HOTFIX');" id="CB_HOTFIX">HOTFIX</span>
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









        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nom du pack</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <input type="hidden" id="packNameHidden" />
                    <html:text styleClass="readonly" styleId="packName" property="packName" style="width:185px;margin-left: 5px;"/>  
                    <span style="margin-left: 6px;">Suffixe</span><html:text styleId="suffixe" onkeypress="miseAJourPackName();" onkeydown="miseAJourPackName();" onkeyup="miseAJourPackName();" property="suffixe" style="width:65px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>

        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Ne pas ecraser le livrable</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <input type="checkbox" id="nePasEcraserLivrable" name="nePasEcraserLivrable" value="nePasEcraserLivrable">                    
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

            </div>
            </td>
            </tr>
            <sec:authorize access='hasAnyRole("BOUTON_PASSER_LIVRAISON")'>
                <tr class="groupe1" id="trBoutonListeObjets">
                    <td colspan="2">
                        <p class="bouton">
                            <a href="#?w=630" rel="popup_objectList" class="poplight">                        
                                <html:button property="listeObjets" value="Liste des objets" styleClass="boutonValider" />
                            </a>
                <center><span id="messageResultat" class="clignotant"></span></center>
                </p>
                </td>
                </tr>
            </sec:authorize>
            <tr class="groupe1">
                <td colspan="2">
                    <p class="bouton">
                        <a href="#?w=630" rel="popup_notificationManuel" class="poplight">                        
                            <html:button property="notificationmanuel" value="Notifier le manuel" styleClass="boutonValider" />
                        </a>
            <center><span id="messageResultat" class="clignotant"></span></center>
            </p>
            </td>
            </tr>
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
                                    int cmpNotif = 0;
                                    if (listHotfixHarmRelease != null && listHotfixHarmRelease.size() > 0) {
                                        cmpNotif += listHotfixHarmRelease.size();
                                    }
                                    if (listRelease != null && listRelease.size() > 0) {
                                        cmpNotif += listRelease.size();
                                    }
                                    if (cmpNotif > 0) {
                                        out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
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
                                                Priority
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
                                                    out.println("<tr " + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                    if (listHotfixHarmProjet != null && listHotfixHarmProjet.size() > 0) {
                                        cmpNotif += listHotfixHarmProjet.size();
                                    }
                                    if (listProjet != null && listProjet.size() > 0) {
                                        cmpNotif += listProjet.size();
                                    }
                                    if (cmpNotif > 0) {
                                        out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
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
                                                Priority
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                    if (listHotfixHarmOV != null && listHotfixHarmOV.size() > 0) {
                                        cmpNotif += listHotfixHarmOV.size();
                                    }
                                    if (listActionsChaudTest != null && listActionsChaudTest.size() > 0) {
                                        cmpNotif += listActionsChaudTest.size();
                                    }
                                    if (listHotfix != null && listHotfix.size() > 0) {
                                        cmpNotif += listHotfix.size();
                                    }
                                    if (cmpNotif > 0) {
                                        out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
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
                                                Priority
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <% cmp = 1;
                                            out.println("<tr class='titre'><td colspan='8'>Tickets Hotfix à harmoniser sur l'environnement de Référence</td></tr>");
                                            if (listHotfixHarmOV == null || listHotfixHarmOV.size() == 0) {
                                                out.println("<td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listHotfixHarmOV) {
                                                    Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                    Integer numTicketLivraison = ticketLivraison.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
                                                    cmp++;
                                                }
                                            }

                                            cmp = 1;
                                            out.println("<tr class='titre'><td colspan='8'>Tickets Actions à chaud Test</td></tr>");
                                            if (listActionsChaudTest == null || listActionsChaudTest.size() == 0) {
                                                out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listActionsChaudTest) {
                                                    Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                    Integer numTicketLivraison = ticketLivraison.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                            <td class="titreBloc titreTextUpgrade" >
                                <%
                                    cmpNotif = 0;
                                    if (listUpgrade != null && listUpgrade.size() > 0) {
                                        cmpNotif += listUpgrade.size();
                                    }
                                    if (cmpNotif > 0) {
                                        out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
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
                                                Priority
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("contenu_des_livrables") + "</td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + ticketLivraison.getPriority().replaceAll("PROBLEME DE DEPLOIEMENT", "PROBLEME_DEP") + "</td></tr>");
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
    <div id="popup_objectList" class="popup_block">
        <p class="titrePopup">Introduire la liste d'objets</p>
        <div class='center'>
            <html:textarea styleId="textAreaObjectList" property="textAreaObjectList"/>            
        </div>
        <script>
                        var contextPath = "<%=request.getContextPath()%>";
        </script>
        <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Former le pack" onclick="$('#textAreaManuel').val('');
                            validerChampsGestionLivraisons(contextPath, false);" /></p>
    </div>

    <div id="popup_notificationManuel" class="popup_block">
        <p class="titrePopup">Manuel de la livraison</p>


        <div class="wikitoolbar">
            <input type="button" class="wikiButton" id="strong" title="Bold text: '''Example'''" tabindex="400" style="background-image: url('images/strong.png')" />

            <input type="button" class="wikiButton" id="em" title="Italic text: ''Example''" tabindex="400" style="background-image: url('images/em.png')" />

            <input type="button" class="wikiButton" id="heading" title="Heading: == Example ==" tabindex="400" style="background-image: url('images/heading.png')" />

            <input type="button" class="wikiButton" id="link" title="Link: [http://www.example.com/ Example]" tabindex="400" style="background-image: url('images/link.png')" />

            <input type="button" class="wikiButton" id="code" title="Code block: {{{ example }}}" tabindex="400" style="background-image: url('images/code.png')" />

            <input type="button" class="wikiButton" id="hr" title="Horizontal rule: ----" tabindex="400" style="background-image: url('images/hr.png')" />

            <input type="button" class="wikiButton" id="np" title="New paragraph" tabindex="400" style="background-image: url('images/np.png')" />

            <input type="button" class="wikiButton" id="br" title="Line break: [[BR]]" tabindex="400" style="background-image: url('images/br.png')" />
        </div>
        <span id="conteneurMessageTrac">
            <textarea id="textAreaManuel" class="common" style="min-height: 50px"></textarea>   
        </span>
        <p>
            <input type= "checkbox" name="sendTicketToIE" value="Envoyer le ticket à I&E" id="sendTicketToIE">
            <span class ="textClass">
                Envoyer le ticket à I&E pour déploiement
            </span>
            <br>
            <input type= "checkbox" name="writeTextOnTicket" value="writeTextOnTicket" id="writeTextOnTicket">
            <span class ="textClass">
                Notifier les changements dans le ticket TRAC
            </span>
        </p>
        <p>
            <span id="messageResultatPersist" class="vert clignotant">

            </span>

        <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Enregistrer la livraison" onclick="validerChampsGestionLivraisons(contextPath, true);" /></p>
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
                            selectCompany('TN1');
                            $(".groupe1").hide();
                            $("#loadingAnimationConteneur").hide();
                            $("#getLivraisonDetails").click(function() {
                                $("#conteneurTablesTickets").hide();
                                openTracTicketInNewTab($("#numLivraison").val(), "LIVRAISON");
                            });
                            resizeTextArea();
                            refresh();
                        });
                        function miseAJourPackName() {
                            $("#packName").attr('readonly', false);
                            if ($("#suffixe").val().length === 0) {
                                $("#packName").val($("#packNameHidden").val());
                            } else {
                                suffixe = $("#suffixe").val();
                                if (suffixe.length > 6) {
                                    $("#suffixe").val(suffixe.substr(0, 6));
                                }

                                $("#packName").val($("#packNameHidden").val() + "." + $("#suffixe").val());
                            }
                            $("#packName").attr('readonly', true);
                        }


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

                        function resizeTextArea() {
                            var txt = $('#textAreaManuel');
                            var hiddenDiv = $(document.createElement('div'));
                            txt.addClass('txtstuff');
                            hiddenDiv.addClass('hiddendiv common');
                            $('#conteneurMessageTrac').append(hiddenDiv);
                            var content = $('#textAreaManuel').val();
                            content = content.replace(/\n/g, '<br>');
                            hiddenDiv.html(content + '<br class="lbr">');
                            $('#textAreaManuel').css('height', hiddenDiv.height());
                        }

                        $('#textAreaManuel').keyup(function() {
                            resizeTextArea();
                        });

                        $(".wikiButton").click(function() {
                            var cursorStartPosition = document.getElementById("textAreaManuel").selectionStart;
                            var cursorEndPosition = document.getElementById("textAreaManuel").selectionEnd;
                            var textAreaTxt = jQuery("#textAreaManuel").val();
                            var textStart = "";
                            var textEnd = "";
                            var cursorposition = 0;
                            if ($(this).attr("id") === "strong") {
                                textStart = "'''";
                                textEnd = "'''";
                            } else if ($(this).attr("id") === "em") {
                                textStart = "''";
                                textEnd = "''";
                            } else if ($(this).attr("id") === "heading") {
                                textStart = "\n== ";
                                textEnd = " ==\n";
                            } else if ($(this).attr("id") === "link") {
                                textStart = "[";
                                textEnd = "]";
                            } else if ($(this).attr("id") === "link") {
                                textStart = "[";
                                textEnd = "]";
                            } else if ($(this).attr("id") === "code") {
                                textStart = "\n{{{\n";
                                textEnd = "\n}}}\n";
                            } else if ($(this).attr("id") === "hr") {
                                textStart = "\n----\n";
                                textEnd = "";
                            } else if ($(this).attr("id") === "np") {
                                textStart = "\n\n";
                                textEnd = "";
                            } else if ($(this).attr("id") === "br") {
                                textStart = "[[BR]]\n";
                                textEnd = "";
                            }
                            cursorposition = cursorStartPosition + textStart.length;
                            $("#textAreaManuel").val(textAreaTxt.substring(0, cursorStartPosition) + textStart + textAreaTxt.substring(cursorStartPosition, cursorEndPosition) + textEnd + textAreaTxt.substring(cursorEndPosition));
                            $('#textAreaManuel').selectRange(cursorposition, cursorposition);
                            resizeTextArea();
                        });

                        $.fn.selectRange = function(start, end) {
                            if (!end)
                                end = start;
                            return this.each(function() {
                                if (this.setSelectionRange) {
                                    this.focus();
                                    this.setSelectionRange(start, end);
                                } else if (this.createTextRange) {
                                    var range = this.createTextRange();
                                    range.collapse(true);
                                    range.moveEnd('character', end);
                                    range.moveStart('character', start);
                                    range.select();
                                }
                            });
                        };


</script>