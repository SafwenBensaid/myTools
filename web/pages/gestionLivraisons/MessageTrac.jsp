<%@page import="tools.Configuration"%>
<%@page import="tools.Tools"%>
<%@page import="dto.DetailsLivraisonDTO"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    String connectedUser = Tools.getConnectedLogin();
    DetailsLivraisonDTO detailsLivraison = Configuration.usersDetailsLivraisonsMap.get(connectedUser);
    //String messageTrac = "Le packaging de la '''LIVRAISON CERTIFICATION TICKET 23609''' s'est effectué correctement. [[BR]]\nA déployer uniquement sur '''QL1, MIGR et CRT''' avec la company '''TN1''' [[BR]]\nLe pack est sous bnk.run/PACK.TAF/'''LIVR.TLM1.17197''' [[BR]]\nnb: Le pack est versionné sur le dépôt_release au niveau de la branche C.RELEASE QL1 à la revision \n [http://172.28.70.74/release_t24/changeset/16112 rev 16112]";
    //DetailsLivraisonDTO detailsLivraison = new DetailsLivraisonDTO("TAF-LIVR.BFIM.14331", "RELEASE", "22321", "CERTIFACATION", "BFIM", "14331", "resultat analyse pack", false, "DEVR", "", "", "", messageTrac, "TN1", "OBJETS T24", "");
%>


<div class="centre">
    <div class="message">La livraison n° <b><%= detailsLivraison.getNumTicket()%></b> a été traitée avec succès </div>

    <br>
    <div class="titre">Résultat d'analyse d'objets</div>

    <% //detailsLivraison.getResultatAnalysePack()
        String message = detailsLivraison.getResultatAnalysePack();

        message = message.replace("\r", "");
        String[] tab = message.split("\n");
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].length() > 0) {
                out.println(tab[i] + "<br>");
            }
        }
        if (message.trim().length() == 0) {
    %>
    <span id="resultatAnalysePack">
        Rien à signaler
    </span>
    <%        }
    %>






    <br>
    <div class="titre">Résultat du packaging</div>

    <% //detailsLivraison.getResultatAnalysePack()
        message = detailsLivraison.getResultatDeploiement();

        message = message.replace("\r", "");
        tab = message.split("\n");
        for (int i = 0; i < tab.length; i++) {
            if (tab[i].length() > 0) {
                out.println(tab[i].replace("%_%", "<br><br>") + "<br>");
            }
        }
    %>

    <%
        if (!detailsLivraison.getCircuit().equals("HOTFIX")) {
            out.println("<br>");
            out.println("<div class='titre'>Résultat du versionning</div>");
            out.println("<span id='resultatAnalysePack'>");
            message = detailsLivraison.getResultatVersionning();
            message = message.replace("\r", "");
            tab = message.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                }
            }
            out.println("</span>");
        }
    %>
    <br>
    <div class="titre">Message à écrire dans le ticket TRAC</div><br>

    <% //detailsLivraison.getResultatAnalysePack()
        message = detailsLivraison.getMessageTrac().trim();

        message = message.replace("\r", "");

    %>
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
        <textarea id="resultatAnalysePackTextarea" class="common"><%= message%></textarea>   
    </span>

    <%
        String contenuDesLivrables = detailsLivraison.getContenuDesLivrables();
        if (!contenuDesLivrables.trim().equals("OBJETS T24")) {
            out.println("<div class='rouge' style='text-align:center;margin-top:15px'>Attention, à ne pas oublier de mentionner l'action manuelle (" + contenuDesLivrables + ") dans le WIKI</div>");
        }
    %>


    <p>
        <span id="messageResultatPersist" class="vert clignotant">

        </span>
    <div id="conteneurAnimation" style="display: none">
        <%@include file="/pages/loadingAnimation1.jsp" %>
    </div>
</p>
<script>
    var contextPath = "<%=request.getContextPath()%>";
</script>
<p class="conteneurBouton">
    <!-- <input type="button" class="boutonValider"  value="Fin" onclick="document.location.href = './getAllMilestonesForm.do?acteur=OV';" /> -->
    <input type="button" class="boutonValider"  value="Mise à jour du ticket trac" onclick="updateTicket('<%= detailsLivraison.getNumTicket()%>', $('#resultatAnalysePackTextarea').val(), '<%= detailsLivraison.getCircuit()%>', '<%= detailsLivraison.getContenuDesLivrables()%>', '<%= detailsLivraison.isNePasEcraserLivrable() %>', contextPath)" />
</p>

</div>

<script>





    $(document).ready(function() {
        $(".accordionButton").first().click();
        selectMenu('menuT24');
        resizeTextArea();
    });

    function resizeTextArea() {
        var txt = $('#resultatAnalysePackTextarea');
        var hiddenDiv = $(document.createElement('div'));
        txt.addClass('txtstuff');
        hiddenDiv.addClass('hiddendiv common');
        $('#conteneurMessageTrac').append(hiddenDiv);
        var content = $('#resultatAnalysePackTextarea').val();
        content = content.replace(/\n/g, '<br>');
        hiddenDiv.html(content + '<br class="lbr">');
        $('#resultatAnalysePackTextarea').css('height', hiddenDiv.height());
    }

    $('#resultatAnalysePackTextarea').keyup(function() {
        resizeTextArea();
    });

    $(".wikiButton").click(function() {
        var cursorStartPosition = document.getElementById("resultatAnalysePackTextarea").selectionStart;
        var cursorEndPosition = document.getElementById("resultatAnalysePackTextarea").selectionEnd;
        var textAreaTxt = jQuery("#resultatAnalysePackTextarea").val();
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
        $("#resultatAnalysePackTextarea").val(textAreaTxt.substring(0, cursorStartPosition) + textStart + textAreaTxt.substring(cursorStartPosition, cursorEndPosition) + textEnd + textAreaTxt.substring(cursorEndPosition));
        $('#resultatAnalysePackTextarea').selectRange(cursorposition, cursorposition);
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

