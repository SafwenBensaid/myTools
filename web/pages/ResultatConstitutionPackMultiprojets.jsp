<%@page import="java.util.Properties"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    Properties properties = (Properties) request.getSession().getAttribute("ResultatConstitutionPackMultiprojets");
    String packName = properties.getProperty("packName");
    String projectsNames = properties.getProperty("projectsNames");
%>



<div class="centre">
    <div class="message"></div>
    <br>
    <div class="titre">Résultat de constitution du Pack Multiprojets</div>

    <span id="resultatAnalysePack">
        Le pack <b><%= packName %></b> a été créé dans l'environnement de Versionning sous le dossier /work<br>
        Les projets qui ont été unis sont: <b><%= projectsNames %></b>
    </span>          
    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Fin" onclick="document.location.href = './ConstitutionPackMultiprojets.do';" />        
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>