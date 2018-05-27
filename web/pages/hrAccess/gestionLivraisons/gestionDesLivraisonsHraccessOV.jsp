<%@page import="entitiesTrac.Ticket"%>
<%@page import="tools.Configuration"%>
<%@page import="java.util.*"%>
<%@page import="tools.Tools"%>
<%@page import="dto.*"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%
    Map<String, List<Map<String, Object>>> listPipeTickets = (Map<String, List<Map<String, Object>>>) request.getSession().getAttribute("mapPipeTicketsHraccess");

    List<Map<String, Object>> listHotfixHarmRelease = listPipeTickets.get("PIPE_OV_HARM_CR");
    List<Map<String, Object>> listRelease = listPipeTickets.get("PIPE_OV_RELEASE");
    List<Map<String, Object>> listActionsAchaud = listPipeTickets.get("PIPE_OV_ACTION_A_CHAUD");
    List<Map<String, Object>> listHotfix = listPipeTickets.get("PIPE_OV_HOTFIX");
%>

<html:form action="/exporterPackHraccessForm" styleId="exporterPackHraccessForm">
    <br/><br/>
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Recette technique des livraisons HR Access (OV)</p> <br><br></td>            
        </tr>

        <tr class="numLivraisonDiv groupe0">
            <td  class="tdDemi"><p class="titres">Numéro de livraison</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <html:text styleId="numLivraison" property="numLivraison" style="width:80px;margin-left: 5px;"/>      
                    <script>
                        var contextPath = "<%=request.getContextPath()%>";
                    </script>
                    <input type="button" class="boutonValiderPetit" id="getLivraisonDetails" onclick="invoquerServletGetTicketHraccessDetailsById(contextPath);" style="margin-left: 10px" value="Valider" />
                    <input type="button" class="boutonValiderPetit" id="resetLivraisonsList" onclick="window.location = './getAllTicketsHraccessForm.do?acteur=OVHR'" style="margin-left: 10px" value="Réinitialiser" />
                </p>
            </td>
        </tr> 

        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nature traitement</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <html:text styleClass="readonly" styleId="natureTraitement" property="natureTraitement" style="width:175px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>

        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nom du pack</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <input type="hidden" id="packNameHidden" />
                    <html:text styleClass="readonly" styleId="packName" property="packName" style="width:175px;margin-left: 5px;"/>  
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
                                <html:button property="listeObjets" value="Collection à livrer" styleClass="boutonValider" />
                            </a>
                <center><span id="messageResultat" class="clignotant"></span></center>
                </p>
                </td>
                </tr>
            </sec:authorize>
    </table>

    <br/><br/>            
    <div id="conteneurTablesTickets">
        <body onload="slideMenu.build('sm', 860, 10, 10, 1)"/>

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
                                        <%  int cmp = 1;
                                            out.println("<tr class='titre'><td colspan='5'>Tickets Hotfix à harmoniser sur le C.R</td></tr>");
                                            if (listHotfixHarmRelease == null || listHotfixHarmRelease.size() == 0) {
                                                out.println("<tr><td colspan='5' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listHotfixHarmRelease) {
                                                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                                                    String natureTraitement = (String) mapDetails.get("nature_traitement");
                                                    Integer numTicket = ticket.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicket.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraisonHraccess($(this).html(),\"" + natureTraitement + "\");'>#" + numTicket + "</a></td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                    cmp++;
                                                }
                                            }

                                            cmp = 1;
                                            out.println("<tr class='titre'><td colspan='5'>Tickets Release</td></tr>");
                                            if (listRelease == null || listRelease.size() == 0) {
                                                out.println("<tr><td colspan='5' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listRelease) {
                                                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                                                    String natureTraitement = (String) mapDetails.get("nature_traitement");
                                                    Integer numTicket = ticket.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicket.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraisonHraccess($(this).html(),\"" + natureTraitement + "\");'>#" + numTicket + "</a></td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
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
                                    if (listActionsAchaud != null && listActionsAchaud.size() > 0) {
                                        cmpNotif += listActionsAchaud.size();
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
                                    Circuit Hotix
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
                                        <%  cmp = 1;
                                            out.println("<tr class='titre'><td colspan='5'>Tickets Actions à chaud Test</td></tr>");
                                            if (listActionsAchaud == null || listActionsAchaud.size() == 0) {
                                                out.println("<tr><td colspan='5' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listActionsAchaud) {
                                                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                                                    String natureTraitement = (String) mapDetails.get("nature_traitement");
                                                    Integer numTicket = ticket.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicket.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraisonHraccess($(this).html(),\"" + natureTraitement + "\");'>#" + numTicket + "</a></td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                    cmp++;
                                                }
                                            }

                                            cmp = 1;
                                            out.println("<tr class='titre'><td colspan='5'>Tickets Hotfix Test</td></tr>");
                                            if (listHotfix == null || listHotfix.size() == 0) {
                                                out.println("<tr><td colspan='5' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                            } else {
                                                for (Map<String, Object> mapDetails : listHotfix) {
                                                    Ticket ticket = (Ticket) mapDetails.get("Ticket");
                                                    String natureTraitement = (String) mapDetails.get("nature_traitement");
                                                    Integer numTicket = ticket.getId();
                                                    out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicket.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraisonHraccess($(this).html(),\"" + natureTraitement + "\");'>#" + numTicket + "</a></td><td>" + mapDetails.get("deliveryTime") + "</td><td>" + mapDetails.get("responsable") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
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
        <p class="titrePopup">Introduire le nom de la Collection</p>
        <div class='center'>
            <html:textarea styleId="textAreaObjectList" property="textAreaObjectList"/>            
        </div>
        <script>
                        var contextPath = "<%=request.getContextPath()%>";
        </script>
        <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Former le pack" onclick="validerChampsGestionLivraisonsHraccess(contextPath);" /></p>
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
                            selectMenu('menuHRaccess');
                            $(".groupe1").hide();
                            $("#loadingAnimationConteneur").hide();
                            $("#getLivraisonDetails").click(function() {
                                $("#conteneurTablesTickets").hide();
                                openTracTicketHraccessInNewTab($("#numLivraison").val());
                            });
                            resizeTextArea();
                            refresh();
                        });

                        function preparerLivraisonHraccess(idTicket, natureTraitement) {
                            idTicket = idTicket.replace(/#/g, "");
                            $("#numLivraison").val(idTicket);
                            $("#natureTraitement").val(natureTraitement);
                            //Nom du pack
                            packName = "";
                            if (natureTraitement == "RELEASE") {
                                packName = "LIVR." + idTicket;
                            } else if (natureTraitement == "HOTFIX") {
                                packName = "LIVH." + idTicket;
                            }
                            $("#packName").val(packName);
                            $(".groupe1").show();
                            $("#conteneurTablesTickets").hide();
                            //$("#getLivraisonDetails").click();
                            openTracTicketInNewTab(idTicket, "HRACCESS");
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