<%@page import="dto.VerificationDeltaChampsDTO"%>
<%@page import="dto.DetailsLivraisonDTO"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    VerificationDeltaChampsDTO champs = (VerificationDeltaChampsDTO) request.getSession().getAttribute("VerificationDeltaChamps");
%>


<div class="centre">
    <div class="message"></div>
    <br>
    <div class="titre">Résultat de comparaison du pack <b><%= champs.getPackName() %></b>: <%= champs.getEnvironnementSourceName() %> VS <%= champs.getEnvironnementDestinationName() %></div>

    <span id="resultatAnalysePack">
        Le pack <b><%= champs.getPackName()%></b> a été téléchargé depuis l'environnement <%= champs.getEnvironnementSourceName()%>
    </span>          
    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Annuler" onclick="document.location.href = './VerificationDeltaForm.do';" />
        <input type="button" class="boutonValider"  value="Télécharger le résultat" onclick="document.location.href = './DownloadComparaisonDelta.do';" />
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>