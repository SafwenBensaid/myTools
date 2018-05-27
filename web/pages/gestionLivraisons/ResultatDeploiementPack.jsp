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
%>


<div class="centre">
    <span>
        <%
            String resultatDeploiement = detailsLivraison.getResultatDeploiement();
            boolean erreurDeploiement = true;
            if (!resultatDeploiement.contains("erreurs")) {
                erreurDeploiement = false;
            }
            out.println(resultatDeploiement);
        %>


        <p class="conteneurBouton">
            <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './getAllMilestonesForm.do?acteur=OV';" />
            <script>
                var contextPath = "<%=request.getContextPath()%>";
            </script>
            <input type="button" class="boutonValider boutonValiderSubmit"  value="Re-Déployer le Pack" onclick="submitDeploiementPack(contextPath);" />

            <script>
                var contextPath = "<%=request.getContextPath()%>";
            </script>

            <%
                if (detailsLivraison.getCircuit().equals("HOTFIX")) {
            %>
            <input type="button" class="boutonValider boutonValiderSubmit" id="genererMsgTrac" value="Générer Message TRAC" onclick="submitGenererMessageTrac(contextPath);" />
            <%            } else {
                    if (erreurDeploiement == false) {
                        out.println("<input type=\"button\" class=\"boutonValider boutonValiderSubmit\"  value=\"Versionner le Pack\" onclick=\"submitVersionnerPack(contextPath);\" />");
                    }
                }
            %>



        </p>
    </span>
</div>

<script>
                $(document).ready(function() {
                    selectMenu('menuT24');
                    $(".accordionButton").first().click();
                    try {
                        if ($(".contenu").html().indexOf("échoué") === -1) {
                            $("#genererMsgTrac").click();
                        }
                    }
                    catch (ex) {
                        console.log("externe", ex.message);
                    }
                });
</script>
