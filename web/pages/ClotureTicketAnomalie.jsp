<br>
<div class='grandTitre' style="font-weight: bold;font-family:Verdana;">
    Clôture d'un ticket<br><br>
</div>
<table id="tablePrincipaleEnv">
    <tr id="cadreActivationParCircuit">
        <td class="cadreTrac suiviAnomalie">
            <table class="tabSuiviAnomalie">
                <br><br><br>
                <tr>
                    <td class="titreGrand">Analyse et clôture d'un ticket<br><br><br>
                    </td>
                </tr>
                <tr>
                    <td class="petitTitreBleu">
                        Numéro du ticket: #
                        <input type="text" id="numLivraison" size="4"/>
                        <input type='button' id="afficherTicket" value="Afficher" class="boutonValiderStandard" onclick="afficherAnomalie();"/>
                        <input type='button' id="cloturerTicket" value="Cloturer" class="boutonValiderStandard" onclick="analyseEtCloturerAnomalie();"/>
                    </td>
                </tr>
                <tr>
                    <td class="petitTitreNoir" id="resultatAnalyse">
                        <br><br><br><br>&nbsp;<br><br><br><br>
                    </td>
                </tr>

            </table>
        </td> 
    </tr>
</table>

<span id="conteneurAnimation">
    <%@include file="/pages/loadingAnimation.jsp" %>
</span>

<SCRIPT language="Javascript">
                            $(document).ready(function() {
                                selectMenu('menuT24');
                                $("#conteneurAnimation").hide();
                            });

                            function afficherAnomalie() {
                                var ticketId = $("#numLivraison").val();
                                if (ticketId === "") {
                                    alert("Veuillez indiquer le numéro du ticket anomalie");
                                } else {
                                    openTracTicketInNewTab(ticketId, "ANOMALIE");
                                }
                            }

                            function analyseEtCloturerAnomalie() {
                                $("#conteneurAnimation").show();
                                var contextPath = "<%=request.getContextPath()%>";
                                var ticketId = $("#numLivraison").val().replace(/\s+/g, '');
                                if (ticketId === "") {
                                    alert("Veuillez indiquer le numéro du ticket anomalie");
                                } else {
                                    $.ajax({
                                        type: "POST",
                                        url: contextPath + "/AnalyserTicketAnomalie",
                                        data: "ticketId=" + ticketId + "&action=analyseEtCloture",
                                        success: function(response) {
                                            $("#conteneurAnimation").hide();
                                            $("#resultatAnalyse").html("<br><br><br><br>" + response + "<br><br><br><br>");
                                        },
                                        error: function(e) {
                                            //alert('Error: ' + e);
                                        }
                                    });
                                }
                            }
</SCRIPT>
