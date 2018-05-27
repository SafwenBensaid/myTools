<%-- 
    Document   : AccesAuxEnvironnements
    Created on : 30 mars 2014, 18:48:26
    Author     : Imen
--%>
<SCRIPT language="Javascript">
    var analyseLancee = "false;"
</script>
<br>
<div class='grandTitre' style="font-weight: bold;font-family:Verdana;">
    Accès aux environnements
</div>
<table id="tablePrincipaleEnv">

    <tr id="cadreActivationParCircuit">
        <td class="cadre" colspan="3">
            <div class="petitTitre">
            </div>
            <div class="divtab">
                <table class="envtab">
                    <thead>
                        <tr>
                            <th></th>
                            <th class="cadreTh caseGrandTitre">Circuit</th>
                            <th class="cadreTh caseGrandTitre" id="developpement">Développement</th>
                            <th class="cadreTh caseGrandTitre" id="packaging">Packaging</th>
                            <th class="cadreTh caseGrandTitre" colspan="2" id="integration">Integration</th>
                            <th class="cadreTh caseGrandTitre">Réference</th>
                        </tr>
                    </thead>
                    <tr>
                        <td style="height:100px;width:160px;" class='caseGrandTitre'>Développement Urgent</td>
                        <td style="background-color:#A7D1D9;" class='casePetitTitre'>HOTFIX</td>
                        <td style="background-color:#A7D1D9"> <a href="http://172.28.70.5:8080/R09DVH/servlet/BrowserServlet" class="myButton1"  target="_blank" id="DEVH">DEVH</a></td>
                        <td rowspan="3" style="background-image:linear-gradient(#A7D1D9, #E0D9BC);"> <a href="http://172.28.70.5:8080/R09ASS/servlet/BrowserServlet" class="myButton1"  target="_blank" id="ASS">ASS</a></td>
                        <td style="background-color:#A7D1D9"> <a href="http://172.28.70.5:8080/R09INV/servlet/BrowserServlet" class="myButton1"  target="_blank" id="INV">INV</a></td>
                        <td style="background-color:#A7D1D9"> <a href="http://172.28.70.5:8080/R09RAP/servlet/BrowserServlet" class="myButton1" target="_blank" id="RAP">RAP</a></td>
                        <td style="background-image:linear-gradient(#A7D1D9,#E0D9BC,#D1BCD1 );" rowspan="6"> <a href="http://172.28.70.5:8080/R09INTG/servlet/BrowserServlet" class="myButton1" target="_blank"  >INTG</a></td>
                    </tr>
                    <tr>
                        <td rowspan="4" class='caseGrandTitre'>Développement Planifié</td>
                        <td rowspan="2" style="background-color:#E0D9BC" class='casePetitTitre'>RELEASE</td>
                        <td rowspan="2" style="background-color:#E0D9BC"> <a href="http://172.28.70.5:8080/R09DVR/servlet/BrowserServlet" class="myButton1" target="_blank" id="DEVR">DEVR</a></td>
                        <td style="background-color:#E0D9BC"> <a href="http://172.28.70.5:8080/R09QL1/servlet/BrowserServlet" class="myButton1" target="_blank" id="QL1">QL1</a></td>
                        <td rowspan="2" style="background-color:#E0D9BC"> <a href="http://172.28.70.5:8080/R09MGR/servlet/BrowserServlet" class="myButton1"  target="_blank" id="MIGR">MIGR</a></td>

                    </tr>
                    <tr>
                        <td style="background-color:#E0D9BC"><a href="http://172.28.70.5:8080/R09CRT/servlet/BrowserServlet" class="myButton1" target="_blank" id="CRT">CRT</a></td>
                    </tr>
                    <tr>
                        <td rowspan="2" style="background-color:#D1BCD1" class='casePetitTitre'>PROJET</td>
                        <td rowspan="2" style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09DEV2/servlet/BrowserServlet" target="_blank" class="myButton1" id="DEV2">DEV2</a></td>
                        <td rowspan="2" style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09ASS2/servlet/BrowserServlet" target="_blank" class="myButton1" id="ASS2">ASS2</a></td>
                        <td style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09QL2/servlet/BrowserServlet" class="myButton1" target="_blank" id="QL2">QL2</a></td>
                        <td style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09MIGP/servlet/BrowserServlet" class="myButton1" target="_blank" id="MIGP">MIGP</a></td>
                    </tr>
                    <tr>
                        <td style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09TF1/servlet/BrowserServlet" class="myButton1" target="_blank" id="TF1">TF1</a></td>
                        <td style="background-color:#D1BCD1"><a href="http://172.28.70.5:8080/R09IF2/servlet/BrowserServlet" class="myButton1" target="_blank" id="IF2">IF2</a></td>
                    </tr>
                </table>
            </div>
        </td>
    </tr>
    <tr id="cadreActivationParCircuit">
        <td class="cadreTrac trac" style="width:195px">
            <a href="http://172.28.70.74/trac/anomalies_t24" target="_blank"> <img src="images/anom.jpg" class="btnTrac"/></a>
            <a href="http://172.28.70.74/trac/livraisons_t24" target="_blank"> <img src="images/liv.jpg" class="btnTrac"/></a>
        </td>
        <td class="cadreTrac suiviAnomalie">
            <table class="tabSuiviAnomalie">
                <tr>
                    <td class="titreGrand">Suivi de l'état d'avancement d'une anomalie</td>
                </tr>
                <tr>
                    <td class="petitTitreBleu">
                        Numéro d'anomalie: #
                        <input type="text" id="numLivraison" size="4"/>
                        <input type='button' id="afficherTicket" value="Afficher" class="boutonValiderStandard" onclick="afficherAnomalie();"/> 
                        <input type='button' id="analyserTicket" value="Analyser" class="boutonValiderStandard" onclick="reinitialiser();
        analyserAnomalie();"/>                
                        <!--input type='button' id="cloturerTicket" value="Cloturer" class="boutonValiderStandard" onclick="reinitialiser();cloturerAnomalie();"/-->                        

                    </td>
                </tr>
                <tr>
                    <td class="petitTitreNoir" id="resultatAnalyse">
                        &nbsp;
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
    function analyserAnomalie() {
        var ticketId = $("#numLivraison").val();
        if (ticketId === "") {
            alert("Veuillez indiquer le numéro du ticket anomalie");
        } else {
            analyseLancee = true;
            $("#conteneurAnimation").show();
            var contextPath = "<%=request.getContextPath()%>";
            if (is_int(ticketId) === true) {
                if (xmlhttp) {
                    xmlhttp.open("GET", contextPath + "/AnalyserTicketAnomalie?ticketId=" + ticketId + "&action=analyse", true); //gettime will be the servlet name
                    xmlhttp.onreadystatechange = traitementApresAnalyserTicketAnomalie;
                    xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                    xmlhttp.send(null);
                }
            } else {
                $("#resultatAnalyse").html("Le numéro de l'anomalie doit être un entier");
            }
        }
    }

    function traitementApresAnalyserTicketAnomalie() {
        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200) {
                var objetJson = JSON.parse(xmlhttp.responseText);
                var message = objetJson["MESSAGE"];
                var environnements = objetJson["ENVIRONNEMENTS"];
                var idCase = objetJson["TITRE"];
                var cloture = objetJson["CLOTURE"];
                if (message !== null && message !== "") {
                    $("#resultatAnalyse").html(message);
                }
                if (idCase !== null && idCase !== "") {
                    $("#" + idCase).addClass("clignotant");
                    $("#" + idCase).addClass("vert");
                }
                if (cloture !== null && cloture !== "0") {
                    $("#cloturerTicket").show();
                }
                if (environnements !== null && environnements !== "") {
                    var environnementsArray = environnements.split(";");

                    if (environnements === "TOUS") {
                        $(".myButton1").each(function() {
                            $(this).addClass("vert");
                        });
                    } else {
                        for (var i = 0; i < environnementsArray.length; i++) {
                            $(".myButton1").each(function() {
                                if (environnementsArray[i] === $(this).attr("id")) {
                                    $(this).addClass("vert");
                                }
                            });
                        }
                    }
                }

            }
            else {
                $("#loadingAnimationConteneur").hide();
                alert("Error during AJAX call. Please try again");
            }
            $("#conteneurAnimation").hide();
        }
    }

    function afficherAnomalie() {
        var ticketId = $("#numLivraison").val();
        if (ticketId === "") {
            alert("Veuillez indiquer le numéro du ticket anomalie");
        } else {
            openTracTicketInNewTab(ticketId, "ANOMALIE");
        }
    }

    function cloturerAnomalie() {
        $("#conteneurAnimation").show();
        var contextPath = "<%=request.getContextPath()%>";
        var ticketId = $("#numLivraison").val();
        if (ticketId === "") {
            alert("Veuillez indiquer le numéro du ticket anomalie");
        } else {
            $.ajax({
                type: "POST",
                url: contextPath + "/AnalyserTicketAnomalie",
                data: "ticketId=" + ticketId + "&action=cloture",
                success: function(response) {
                    $("#conteneurAnimation").hide();
                    $("#resultatAnalyse").html(response);
                },
                error: function(e) {
                    //alert('Error: ' + e);
                }
            });
        }
    }

    function reinitialiser() {
        $("#cloturerTicket").hide();
        $(".myButton1").each(function() {
            $(this).removeClass("vert");
        });
        $("#resultatAnalyse").html("&nbsp;");
        $(".caseGrandTitre").removeClass("clignotant");
        $(".caseGrandTitre").removeClass("vert");
    }

    $(document).ready(function() {
        selectMenu('menuT24');
        $("#conteneurAnimation").hide();
        $("#cloturerTicket").hide();
        $("#numLivraison").click(function() {
            if (analyseLancee === true) {
                $("#numLivraison").val("");
                reinitialiser();
                analyseLancee = false;
            }

        });
    });

</SCRIPT>
