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
    <div class="titre">Résultat de l'export de la collection <b><%= detailsLivraisonHraccess.getListeObjets() %></b> à partir de l'environnement <%= detailsLivraisonHraccess.getEnvironnementSource() %></div>
    <br><br>
    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            boolean erreurExecution = true;
            
            String resultatExport = detailsLivraisonHraccess.getResultatExport(); 
            if (!resultatExport.contains("erreur")) {
                    erreurExecution = false;       
            }
            if (erreurExecution == true) {
                out.println("<span class=\"rouge\">");
            }

            resultatExport = resultatExport.replace("\r", "");
            String[] tab = resultatExport.split("\n");
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

    <br><br>
    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './getAllTicketsHraccessForm.do?acteur=OVHR';" />
        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>
        <input type="button" class="boutonValider boutonValiderSubmit"  value="Importer le Pack" onclick="submitImporterPackHraccess(contextPath);" />

        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>
    </p>

</div>



<script>
    $(document).ready(function() {
        selectMenu('menuHRaccess');
    });
</script>