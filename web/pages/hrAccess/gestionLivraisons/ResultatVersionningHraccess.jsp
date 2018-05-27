<%@page import="dto.DetailsLivraisonHraccessDTO"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    DetailsLivraisonHraccessDTO detailsLivraisonHraccess = (DetailsLivraisonHraccessDTO) request.getSession().getAttribute("detailsLivraisonHraccess");
%>

<div class="centre">
    <div class="message">La livraison n° <b><%= detailsLivraisonHraccess.getNumLivraison()%></b> a été traitée avec succès </div>    
    <br>
    <div class="titre">Résultat Export des Entités</div>

    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            String message = detailsLivraisonHraccess.getResultatExport();

            message = message.replace("\r", "");
            String[] tab = message.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i].replace("%_%", "<br><br>") + "<br>");
                }
            }
        %>
    </span>    
    <br>
    <div class="titre">Résultat Import des Entités</div>

    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            message = detailsLivraisonHraccess.getResultatImport();

            message = message.replace("\r", "");
            tab = message.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i].replace("%_%", "<br><br>") + "<br>");
                }
            }
        %>
    </span>     
    <br>
    <div class="titre">Résultat de Versionning du pack <b><%= detailsLivraisonHraccess.getPackName() %></b> sur le dépot SVN Hraccess.</div>
    <span id="resultatAnalysePack">
        <%  boolean erreurExecution = true;
            
            String resultatVersionning = detailsLivraisonHraccess.getResultatVersionning(); 
            if (!resultatVersionning.contains("erreur")) {
                    erreurExecution = false;       
            }
            if (erreurExecution == true) {
                out.println("<span class=\"rouge\">");
            }

            resultatVersionning = resultatVersionning.replace("\r", "");
            tab = resultatVersionning.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                }
            }

            if (erreurExecution == true) {
                out.println("</span>");
            }
        %>
    </span>   
    <br>
    <div class="titre">Message à écrire dans le ticket TRAC</div><br>

    <% //detailsLivraison.getResultatAnalysePack()
        message = detailsLivraisonHraccess.getMessageTrac().trim();
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
        <textarea id="resultatAnalysePackTextarea" class="common" style="min-height: 50px"><%= message%></textarea>   
    </span>



    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './getAllTicketsHraccessForm.do?acteur=OVHR';" />
        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>
        <input type="button" class="boutonValider boutonValiderSubmit"  value="Mise à jour du ticket trac" onclick="updateTicketHraccess('<%= detailsLivraisonHraccess.getNumLivraison()%>', '<%= detailsLivraisonHraccess.getMessageTrac()%>', contextPath);" />
    </p>

</div>



<script>
            $(document).ready(function() {
                selectMenu('menuHRaccess');
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