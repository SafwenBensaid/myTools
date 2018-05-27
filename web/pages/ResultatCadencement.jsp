<%@page import="java.util.Map"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<div class="centre">
    <%
        String packName = (String) request.getSession().getAttribute("packName");
        Map<String, Integer> sortedCadencementDeltaMap = (Map<String, Integer>) request.getSession().getAttribute("sortedCadencementDeltaMap");
    %>
    <div class="titre">Résultat de génération du cadencement Delta relatif à l'export <b> <%= packName%> </b></div>
    </br>
    <span id="resultatAnalysePack">
        <%
            for (String type : sortedCadencementDeltaMap.keySet()) {
                out.println("<br> " + type);
            }
        %>
    </span>
    </br>

    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Retour" onclick="document.location.href = './Cadencement.do';" />
    </p>
</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>