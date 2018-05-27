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
    String environnementDeDeploiement = "";
    if (detailsLivraison.getCircuit().equals("RELEASE") || detailsLivraison.getCircuit().equals("HOTFIX")) {
        environnementDeDeploiement = "ASS";
    } else if (detailsLivraison.getCircuit().equals("PROJET")) {
        environnementDeDeploiement = "ASS2";
    }    
    String resultatAnalyse = detailsLivraison.getResultatAnalysePack();    
%>



<div class="centre">
    <div class="titre">Résultat d'analyse du pack <b><%= detailsLivraison.getPackName()%></b></div>
    <%
        if (detailsLivraison.isCusExists() == false) {
    %>
    <div class="message">Le pack <b><%= detailsLivraison.getPackName()%></b> a été déplacé du dossier PACK.TAF de l'environnement <%= detailsLivraison.getServerName()%> vers le dossier PACK.DEP de l'environnement <%= environnementDeDeploiement%></div>
    <%
    } else {
    %>

    <div class="message">
        Le pack <b><%= detailsLivraison.getPackName()%></b> a été déplacé du dossier PACK.TAF de l'environnement <%= detailsLivraison.getServerName()%> vers le dossier PACK.DEP de l'environnement <%= environnementDeDeploiement%>.
        <br/>Puisqu'il contient des objets FILE.CONTROL de type CUS, il a été défalqué en deux packs (<b><%= detailsLivraison.getPackName()%>.BNK</b> et <b><%= detailsLivraison.getPackName()%>.TN1</b>)
    </div>

    <%
        }
    %>
    <br>



    <% //detailsLivraison.getResultatAnalysePack()
            

        resultatAnalyse = resultatAnalyse.replace("\r", "");
        String[] tab = resultatAnalyse.split("\n");

        for (int i = 0; i < tab.length; i++) {
            if (tab[i].length() > 0) {
                out.println(tab[i] + "<br>");
            }
        }
    %>

    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './getAllMilestonesForm.do?acteur=OV';" />
        <%
            if (!resultatAnalyse.contains("Attention:")) {

        %>
        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>
        <input type='button' class='boutonValider boutonValiderSubmit'  value='Déploiement' onclick='submitDeploiementPack(contextPath);' />
        <%
            }
        %>
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>