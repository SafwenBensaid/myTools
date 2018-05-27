<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    String resutatEtudeIntersection = (String) request.getSession().getAttribute("resutatEtudeIntersection");
    
%>


<div class="centre">

    <div class="titre">Résultat d'étude d'intersection</div>

    <span id="resultatAnalysePack">
        <% //detailsLivraison.getResultatAnalysePack()
            String resultatAnalyse = resutatEtudeIntersection.trim();

            resultatAnalyse = resultatAnalyse.replace("\r", "");
            String[] tab = resultatAnalyse.split("\n");

            for (int i = 0; i < tab.length; i++) {
                if (tab[i].length() > 0) {                    
                    out.println(tab[i] + "<br>");
                }else{
                    out.println(tab[i] + "<br>");
                }
            }
        %>
    </span>          
    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Fin" onclick="document.location.href = './EtudeIntersectionInputObjetsPath.do';" />
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>