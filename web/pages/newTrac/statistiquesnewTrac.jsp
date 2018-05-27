<%@page import="java.util.List"%>
<%@page import="tools.Tools"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<table class="tablePrincipale">

    <tr>
        <td colspan="2" >
            <p class="grandTitre"></p> 
        </td>
    </tr>

    <tr id="loadingAnimationConteneur">
        <td colspan="2" >
            <%@include file="/pages/loadingAnimation.jsp" %>
        </td>
    </tr>

    <tr>
        <td class="conteneurWrapper">
            <div id="wrapper">

            </div>
        </td>            
    </tr>
</table>

<script>
    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
        return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }

    $(document).ready(function() {
        var menuItem = getParameterByName('menuItem');
        var circuit = getParameterByName('circuit');
        var critereTri = getParameterByName('critereTri');
        //alert(menuItem);
        selectMenu(menuItem);
        if (circuit === "HOTFIX") {
            $(".grandTitre").html("Statistiques circuit hotfix par " + critereTri.replace("nature_traitement", "nature de traitement"));
        } else if (circuit === "RELEASE") {
            $(".grandTitre").html("Statistiques circuit release par " + critereTri.replace("nature_traitement", "nature de traitement"));
        } else if (circuit === "INCIDENT") {
            $(".grandTitre").html("Statistiques de l'état d'avancement de toutes les incidents SI par niveau d'escalade");
        } else if (circuit === "DEMANDE") {
            $(".grandTitre").html("Statistiques de l'état d'avancement de tous les besoins métiers par Domaine");
        }

        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        showDiagrammePieNewTrac(contextPath, "AfficherDigrammePie");



    });
</script>