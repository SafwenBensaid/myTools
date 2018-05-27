<%@page import="tools.Tools"%>
<%@page import="java.util.*"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    
    
    String errorName="";
    String errorValue="";
    Map<String,String> errorMap = (Map<String,String>) request.getSession().getAttribute("erreurNameErreurValue");
    errorName = errorMap.get("errorName");
    errorValue = errorMap.get("errorValue");
    String connectedUser = Tools.getConnectedLogin();
    Tools.clearMapTicketsEnCours(connectedUser);
%>


<div class="centre">

    <div class="titre">Erreur: <%= errorName%></div>

    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            String resultatAnalyse = errorValue;

            resultatAnalyse = resultatAnalyse.replace("\r", "");
            String[] tab = resultatAnalyse.split("\n");

            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {
                    out.println(tab[i] + "<br>");
                } else {
                    out.println("<br>");
                }
            }
        %>
    </span>          
    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="OK" onclick="document.location.href = './accueil.do';" />
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('indefined');
            });
</script>