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
    } else if (detailsLivraison.getCircuit().equals("UPGRADE")) {
        environnementDeDeploiement = "ASSU";
    }
%>

<div class="centre">
    <%
        String resultatDeploiement = "";
        if (detailsLivraison.isCusExists() == false) {
    %>
    <div class="titre">Résultat de versionning du pack <b><%= detailsLivraison.getPackName()%></b> sur le dépôt de versionning</div>
    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            String resultatVersionning = detailsLivraison.getResultatVersionning();

            resultatVersionning = resultatVersionning.replace("\r", "");
            String[] tab = resultatVersionning.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                }
            }
        %>
    </span>   
    <%
    } else {
    %>
    <div class="titre">Résultat de versionning du pack <b><%= detailsLivraison.getPackName()%>.BNK</b> sur le dépôt de versionning</div>
    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            String resultatVersionning = detailsLivraison.getResultatVersionning().split("%_%")[0];

            resultatVersionning = resultatVersionning.replace("\r", "");
            String[] tab = resultatVersionning.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                }
            }
        %>
    </span>         
    <br/>
    <div class="titre">Résultat de versionning du pack <b><%= detailsLivraison.getPackName()%>.TN1</b> sur le dépôt de versionning</div>
    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            resultatVersionning = detailsLivraison.getResultatVersionning().split("%_%")[1];

            resultatVersionning = resultatVersionning.replace("\r", "");
            tab = resultatVersionning.split("\n");
            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                }
            }
        %>
    </span> 
    <%
        }
    %>
    <p class="conteneurBouton">
        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>

        <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './getAllMilestonesForm.do?acteur=OV';" />

        <input type="button" class="boutonValider boutonValiderSubmit"  value="Générer le message TRAC" onclick="submitGenererMessageTrac(contextPath);" />
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>