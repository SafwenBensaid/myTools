function afficherDetails(obj) {
    $(obj).closest(".accordionContent").find(".afficherDetails").hide();
    $(obj).closest(".accordionContent").find(".cacherDetails").each(function() {
        $(this).show();
    });
    $(obj).closest(".accordionContent").find(".conteneurTable").show();
}
function cacherDetails(obj) {
    $(obj).closest(".accordionContent").find(".afficherDetails").show();
    $(obj).closest(".accordionContent").find(".cacherDetails").hide();
    $(obj).closest(".accordionContent").find(".conteneurTable").hide();
}

function getParametre() {
    var parameters = location.search.substring(1).split("=");
    return (unescape(parameters[1]));
}



function showDiagrammePieNewTrac(contextPath, titre) {
    var url = contextPath + "/AfficherDigrammePieNewtrac" + unescape(location.search);
    showDiagrammePieGenerique(contextPath, titre, url);
}

function showDiagrammePie(contextPath, titre) {
    var url = contextPath + "/AfficherDigrammePie?filtre=" + getParametre();
    showDiagrammePieGenerique(contextPath, titre, url);
}

function showDiagrammePieGenerique(contextPath, titre, url) {
    $.ajax({
        type: "POST",
        url: url,
        data: "GENERAL_DIAGRAM",
        success: function(response) {

            if (response.trim() !== "AUCUN_TICKET_A_TRAITER") {
                var objetJson = JSON.parse(response);


                for (var i = 0; i < objetJson.length; i++) {

                    obj = objetJson[i];
                    var dataDiagramme = obj["dataDiagramme"];
                    var titreDiagramme = obj["titreDiagramme"];
                    var titreAccordionButton = obj["titreAccordionButton"];
                    // ligne inévitable pour la mise en page (pour que les diagrammes soient alignés)
                    dataDiagramme.push({'data': 0, 'label': 'XXXXXXXXXXXXXXXXXXXXXXXXXXXXXX'});
                    // fin
                    var nbrTickets = 0;
                    for (var j = 0; j < dataDiagramme.length; j++) {
                        nbrTickets += dataDiagramme[j]['data'];
                    }
                    if (nbrTickets === 1) {
                        titreAccordionButton += " <span style='opacity:0.7;'> (" + nbrTickets + " Ticket)</span>";
                    } else {
                        titreAccordionButton += " <span style='opacity:0.7;'> (" + nbrTickets + " Tickets)</span>";
                    }
                    //alert(titreDiagramme+"   "+JSON.stringify(dataDiagramme));
                    /*
                     var dataDiagrammeComplet = [
                     {'data': 0, 'label': 'A DEFINIR'},
                     {'data': 0, 'label': 'DEVELOPPEMENT'},
                     {'data': 0, 'label': 'RETOURNEE'},
                     {'data': 0, 'label': 'QUALIFIEE'},
                     {'data': 0, 'label': 'CLARIFIEE'},
                     {'data': 0, 'label': 'INFORMATION LIVREE'},
                     {'data': 0, 'label': 'EN VEILLE'},
                     {'data': 0, 'label': 'ANNULEE'},
                     {'data': 0, 'label': 'CERTIFIEE'},
                     {'data': 0, 'label': 'A QUALIFIER'},
                     {'data': 0, 'label': 'A CERTIFIER'},
                     {'data': 0, 'label': 'A LIVRER'},
                     {'data': 0, 'label': 'REMPLACEE'},
                     {'data': 0, 'label': 'APPLIQUEE SUR PROD'},
                     {'data': 0, 'label': 'LIVRAISON CONFIRMEE'},
                     {'data': 0, 'label': 'INFORMATION REQUISE'}
                     
                     ];
                     */

                    var contenu = "<div class='accordionButton' id='generalReport" + i + "'>" + titreAccordionButton + "</div>";
                    contenu += "<div class='accordionContent'>";
                    contenu += "<center><fieldset class='fieldsetContainer'>";
                    contenu += "<legend class='fieldsetLegende' id=\"" + titreDiagramme + "\">" + titreDiagramme + "</legend>";
                    contenu += "<div id='flotcontainer" + i + "' class='flotcontainerClass'></div>";
                    contenu += "</fieldset>";
                    if (i > 0) {
                        var contenuTableHTML = obj["contenuTableHTML"];
                        contenu += "<div><span class='afficherDetails' onclick='afficherDetails(this);'>Afficher Détails</span><span class='cacherDetails' onclick='cacherDetails(this);'>Cacher Détails</span></div><span class='conteneurTable'>";
                        contenu += contenuTableHTML;
                        contenu += "</span><div><span class='cacherDetails' onclick='cacherDetails(this);'>Cacher Détails</span></div>";
                    }

                    contenu += "</center>";
                    contenu += "</div>";



                    $("#wrapper").append(contenu);
                    cacherDetails($(".cacherDetails"));
                    $("#loadingAnimationConteneur").hide();


                    $(function() {

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
                                                var titreDiagrammeEnCours = $(this).closest(".fieldsetContainer").find(".fieldsetLegende").attr("id");
                                                //alert(titreDiagrammeEnCours +"###"+titreDiagramme);
                                                if ($(this).html() === label && titreDiagramme.indexOf(titreDiagrammeEnCours) != -1) {

                                                    $(this).html("<span style='color:#666666;width:38px;display: inline-block;'>" + (series.percent).toFixed(1) + "%</span><span style='color:#666666;width:65px;display: inline-block;'>(" + series.nbrElmnts + " ticket)</span><span style='color:#343971'>" + label + "</span>");
                                                }
                                            });
                                            /*
                                             $(".legendLabel").each(function() {
                                             var titreDiagrammeEnCours =$(this).closest(".fieldsetContainer").find(".fieldsetLegende").html();
                                             if ($(this).html() === label && titreDiagrammeEnCours === titreDiagramme) {
                                             $(this).html("<span style='color:#666666;width:38px;display: inline-block;'>" + (series.percent).toFixed(1) + "%</span><span style='color:#666666;width:65px;display: inline-block;'>(" + series.nbrElmnts + " ticket)</span><span style='color:#343971'>" + label + "</span>");
                                             $(this).closest(".fieldsetContainer").find(".fieldsetLegende").html("ANIS "+series.nbrGlobal);
                                             }
                                             });
                                             */
                                            return "";
                                        },
                                        background: {opacity: 0.8}
                                    }
                                }
                            }
                        };

                        if (nbrTickets > 0) {
                            $.plot($("#flotcontainer" + i), dataDiagramme, options);
                        } else {
                            $("#flotcontainer" + i).html("<div style='width: 150px; height: 22px; top: 5px; right: 5px; opacity: 0.85; border: 1px solid rgb(52, 57, 113); border-radius: 5px; background-color: rgb(255, 255, 255);padding:5px;margin-left:8px'>Aucun ticket à afficher</div>");
                        }

                    });

                }

                $(".legendLabel").each(function() {
                    if ($(this).html().indexOf('%') === -1) {
                        $(this).closest("tr").remove();
                    } else {
                        $(this).css("width", "185px");
                    }
                });
                $(".legend").each(function() {
                    var legendLabWidth = $(this).find("table:first tr:first td:eq(1)").width();
                    //alert(tableWidth);
                    $(this).find("table:first").css("width", (legendLabWidth + 110) + "px");
                    //le div cadre bleu                
                    $(this).find("div:first").css("width", (legendLabWidth + 50) + " px");
                    $(this).find("div:first").css("border", "1px #343971 solid");
                    $(this).find("div:first").css("border-radius", "5px");
                });
                $(".legendColorBox").each(function() {
                    $(this).css("width", "14px");
                    $(this).find("div:first").css("width", "14px");
                });

                $(".legend").each(function() {
                    var tableHeight = $(this).find("table:first").height();
                    $(this).find("div:first").height(tableHeight);
                });



                reloadAccordeon();
                $("#generalReport" + 0).click();
            } else {
                $("#wrapper").html("<center>Aucun ticket à traiter</center>");
                $("#loadingAnimationConteneur").hide();
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}