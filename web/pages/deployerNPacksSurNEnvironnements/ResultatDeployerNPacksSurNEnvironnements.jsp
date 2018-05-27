<%@page import="dto.CoupleDTO"%>
<%@page import="tools.SessionTools"%>
<%@page import="tools.Tools"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
    String resultatDeploiement = (String) request.getSession().getAttribute("resultatDeploiement");
    String problemesDeploiements = (String) request.getSession().getAttribute("problemesDeploiements");
    CoupleDTO circuit_livraison = (CoupleDTO) request.getSession().getAttribute("circuit_livraison");
%>
<div class='center'>
    <table  class="tablePrincipale">
        <tr>
            <td class="conteneurWrapper">
                <br>
                <div class="grandTitre">
                    <%
                        if (circuit_livraison == null) {
                            out.print("Résultat du déploiement parallèle multipacks");
                        } else {
                            out.print("Résultat d'intégration de la livraisons #" + circuit_livraison.getValeur2() + " sur le circuit " + circuit_livraison.getValeur1());
                        }
                    %>

                </div>
                <%
                if(problemesDeploiements!=null && problemesDeploiements.length()>0){
                    out.print("<br>");
                    out.print("<div class='centre'>");
                    out.print("<span id='resultatAnalysePack'>");
                    out.print(problemesDeploiements);
                    out.print("</span>");
                    out.print("</div>");
                }
                %>    

                <div id="wrapper">
                    <%
                        out.print(resultatDeploiement);
                    %>
                </div>
            </td>            
        </tr>
    </table>

    <br>            
    <p class="conteneurBouton">
        <script>
            var contextPath = "<%=request.getContextPath()%>";
        </script>
        <%
            if (circuit_livraison != null) {
                out.print("<div  class='boutonValider' style='padding-top: 3px;padding-bottom: 3px' onclick='document.location.href = \"./getAllMilestonesForm.do?acteur=IE\";'>Annuler</div>");
                out.print("<div  class='boutonValider' id='genererMessageTrac' style='padding-top: 3px;padding-bottom: 3px' onclick='document.location.href = \"./MessageTracIntegration.do\";'>Générer Message Trac</div>");
            }
        %>

    </p>


</div>




<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        $(".pageCorps").show();
        $(".pageCorps2").hide();
        var tabparametres = window.location.href.split("=").length;
        if (tabparametres > 1) {
            $("#" + window.location.href.split("=")[1]).click();
        } else {
            $("#gestionDesEnvironnements").click();
        }
        $(".accordionButton").first().click();
    });

</script>