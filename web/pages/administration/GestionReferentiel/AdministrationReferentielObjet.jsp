<%@taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@page import="java.util.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>


<div class='center'>
    <p class="grandTitre">Administration du référentiel d'objets</p>     
    <div id="loadingAnimationConteneur">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>   

    <table>

        <tr> 
            <td class="conteneurWrapper">
                <div id="wrapper">




                </div>
            </td>            
        </tr>
    </table>
</div>

<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        $("#loadingAnimationConteneur").show();
        chargerAccordeons();
    });


    function chargerAccordeons() {
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "POST",
            url: contextPath + "/ServletInitialisationReferentielAction",
            success: function(response) {
                try {
                    $("#wrapper").html(response);
                    reloadAccordeon();
                    $("#loadingAnimationConteneur").hide();
                    $("#contenuAccordeonRelease").load(contextPath + "/pages/administration/GestionReferentiel/ContenuAccordeonGestionReferentielObjet.jsp?circuit=Release");
                    $("#contenuAccordeonProjet").load(contextPath + "/pages/administration/GestionReferentiel/ContenuAccordeonGestionReferentielObjet.jsp?circuit=Projet");
                    $("#contenuAccordeonHotfix").load(contextPath + "/pages/administration/GestionReferentiel/ContenuAccordeonGestionReferentielObjet.jsp?circuit=Production");
                    $("#referentielRelease").addClass('over');
                    $("#contenuAccordeonRelease").show();

                } catch (e) {
                    alert(e.message);
                }
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
</script>