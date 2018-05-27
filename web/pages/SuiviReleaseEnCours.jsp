<%@page import="java.util.List"%>
<%@page import="tools.Tools"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<script>
    function getParametre() {
        var parameters = location.search.substring(1).split("=");
        return (unescape(parameters[1]));
    }
</script>

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
                <!--
                <div class='accordionButton' id='generalReport'>Domaine Engagement</div>
                <div class='accordionContent'>
                    <center>

                        <fieldset class='fieldsetContainer'>
                            <legend class='fieldsetLegende'> titreDiagramme </legend>
                            <div id='flotcontainer' class='flotcontainerClass'></div>
                        </fieldset>
                        
                        <div><span class="afficherDetails">Afficher Détails</span><span class="cacherDetails">Cacher Détails</span></div>
                        <span class='conteneurTable'>
                        <div class='titrePriority'><span class='titreSpan'>Priorité:</span><span class='valeurSpan'> APPLIQUEE SUR PROD</span></div>
                        <table class='tableTicketsSummary'>
                            <thead>
                                <tr class='tableTicketsSummaryTr'>
                                    <th class='tableTicketsSummaryTh' colspan='1'>Anomalie</th>
                                    <th class='tableTicketsSummaryTh' colspan='1'>Livraison</th>
                                    <th class='tableTicketsSummaryTh' colspan='7'>Summary</th>
                                    <th class='tableTicketsSummaryTh' colspan='2'>Reporter</th>
                                    <th class='tableTicketsSummaryTh' colspan='2'>Niveau Projet</th>
                                    <th class='tableTicketsSummaryTh' colspan='1'>Statut</th>
                                    <th class='tableTicketsSummaryTh' colspan='2'>Priority</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr class='tableTicketsSummaryTr'>
                                    <td class='tableTicketsSummaryTd' colspan='1'><a href='http://172.28.70.7/testing_t24/ticket/14667'  target="_blank">#14667</a></th>
                                    <td class='tableTicketsSummaryTd' colspan='1'><a href='http://172.28.70.74/trac/livraisons_t24/ticket/8312'  target="_blank">#8312</a></th>
                                    <td class='tableTicketsSummaryTd' colspan='7'>T24 n'affiche pas le résultat de la requete 'Liste des EPS avec provision sup à l'encours ' BIAT.MD.ETAT.EPS.PROV.SUP.ENCOURS.ENQ ( voir imprime écran dans document attaché )</th>
                                    <td class='tableTicketsSummaryTd' colspan='2'>akram.lassoued</th>
                                    <td class='tableTicketsSummaryTd' colspan='2'>BFI Marché forex</th>
                                    <td class='tableTicketsSummaryTd' colspan='1'>assigned</th>
                                    <td class='tableTicketsSummaryTd' colspan='2'>QUALIFIEE</th>
                                </tr>                                
                            </tbody>
                        </table>
                        </span>
                        <div><span class="cacherDetails">Cacher Détails</span></div>
                    </center>

                </div>
                -->
            </div>
        </td>            
    </tr>
</table>

<script>
    $(document).ready(function() {

        var filtre = getParametre();

        selectMenu('menuT24');

        if (filtre === "maintenance") {
            $(".grandTitre").html("Statistiques release en cours (projet " + filtre + ")");
        } else if (filtre === "circuitProjet") {
            $(".grandTitre").html("Statistiques circuit Projet");
        } else {
            $(".grandTitre").html("Statistiques release en cours par " + filtre);
        }


        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        showDiagrammePie(contextPath, "AfficherDigrammePie");
    });
</script>

<!-- 
<script type="text/javascript">
    function dessinerTest() {
        $(function() {
            var data = [
                {"data": 222, "label": "QUALIFIEE"},
                {"data": 56, "label": "A DEFINIR"},
                {"data": 3, "label": "INFORMATION LIVREE"},
                {"data": 3, "label": "ANNULEE"},
                {"data": 6, "label": "DEVELOPPEMENT"},
                {"data": 7, "label": "A LIVRER"},
                {"data": 1, "label": "APPLIQUEE SUR PROD"},
                {"data": 4, "label": "INFORMATION REQUISE"},
                {"data": 13, "label": "RETOURNEE"},
                {"data": 79, "label": "A QUALIFIER"}
            ];

            var options = {
                series: {
                    pie: {
                        show: true,
                        radius: 1,
                        tilt: 1,
                        label: {
                            show: true,
                            radius: 1,
                            formatter: function(label, series) {

                                $(".legendLabel").each(function() {
                                    if ($(this).html() === label) {
                                        $(this).html("<span style='color:#666666;width:25px;display: inline-block;'>" + Math.round(series.percent) + "%</span><span style='color:#343971'>" + label + "</span>");
                                    }
                                });

                                return "";
                            },
                            background: {opacity: 0.8}
                        }
                    }
                }
            };
            $.plot($("#flotcontainer"), data, options);
        });
    }
</script>
-->