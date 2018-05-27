<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/trac.css">
<link rel="stylesheet"  type="text/css" href="css/ticket.css">
<link rel="stylesheet" type="text/css" href="css/styleMenuHorizontal.css"/>


<p class="grandTitre">Gestion des Demandes Metiers</p>

<center>
    <table>
        <tr>
            <td class="conteneurWrapper">
                <div id="wrapper">
                    <div class="accordionButton" id="specProjet"></div>
                    <div class="accordionContent" id="specProjetContent">

                        <!-------------------------- Start Form Metier -------------------------->
                        <table>
                            <tbody>
                                <tr>
                                    <th class='col1'>
                            <div class='bouton'>
                                <div class='showhide'>
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='specification'>
                                    <label class='showhide-label' for='specification'>
                                        <div class='showhide-inner'></div>
                                        <div class='showhide-switch'></div>
                                    </label>
                                </div>
                            </div>
                            </th>
                            <td class='col1'>
                                <div class='titre couleur5'>
                                    Identification du Besoin
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id='specificationBloc'>    
                        </table>

                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='impact'>                        
                                    <label class="showhide-label" for="impact">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th><td class="col1">
                                <div class='titre couleur5'>
                                    Impact/Enjeux
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id="impactBloc">
                        </table>
                        <!-------------------------- End Form Metier -------------------------->

                        <!-------------------------- Start Form MOA -------------------------->
                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='partiesPrenantes'>                        
                                    <label class="showhide-label" for="partiesPrenantes">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur2'>
                                    Parties Prenantes
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id="partiesPrenantesBloc">
                        </table>


                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='impactSI'>                        
                                    <label class="showhide-label" for="impactSI">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur2'>
                                    Impact SI
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id="impactSIBloc">
                        </table>

                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='chiffrageMoa'>                        
                                    <label class="showhide-label" for="chiffrageMoa">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur2'>
                                    Chiffrage des travaux MOA
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id='chiffrageMoaBloc' class='tableParamStyle roundCornerTable'>
                        </table>
                        <!-------------------------- End Form MOA -------------------------->


                        <!-------------------------- Start Form MOE -------------------------->
                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='chiffrageMoe'>                        
                                    <label class="showhide-label" for="chiffrageMoe">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur4'>
                                    Chiffrage des travaux MOE
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>


                        <table id='chiffrageMoeBloc' class='tableParamStyle roundCornerTable'>
                        </table>

                        <!-------------------------- End Form MOE -------------------------->

                        <!-------------------------- Start Form PMO -------------------------->
                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='planification'>                        
                                    <label class="showhide-label" for="planification">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur3'>
                                    Planification
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id="planificationBloc">
                        </table>
                        <!-------------------------- End Form PMO -------------------------->

                        <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='autre'>                        
                                    <label class="showhide-label" for="autre">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th>
                            <td class="col1">
                                <div class='titre couleur5'>
                                    Autre
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>

                        <table id="autreBloc">
                        </table>

                        <center><span id='messageResultatPersist' class='vert clignotant'></span></center>

                    </div>

                    <div class="accordionButton" id="historiqueModification">Historique des modifications</div>
                    <div class="accordionContent" id="historiqueModificationContent"> </div>

                </div>
            </td>            
        </tr>
    </table>
</center>

<div id="loadingAnimationConteneur" class="center">
    <%@include file="/pages/loadingAnimation.jsp" %>
</div>

<script>
    function check_charcount(content_id, max, e)
    {
        if (e.which != 8 && $('#' + content_id).text().length > max)
        {
            e.preventDefault();
        }
    }
    $(document).ready(function() {
        var idTicket = window.location.href.split("=")[1].split("&")[0];
        loadBesoinFromDB(idTicket);
        selectMenu('menuSuiviActiviteDemandes');
        $("#specProjet").hide();
        $("#historiqueModification").hide();
        $("#autreBloc").hide();

        //$("#partiesPrenantes").hide();
        //$("#impactSI").hide();
        //$("#chiffrageMoa").hide();
        //$("#chiffrageMoe").hide();
        //$("#planification").hide();
        //$("#partiesPrenantes").prop('checked', true);
    });
    function loadBesoinFromDB(idTicket) {
        var contextPath = "<%=request.getContextPath()%>";
        //alert('loading ticket ' + idTicket);
        $.ajax({
            type: "POST",
            url: contextPath + "/GestionDemandesMetiersServlet",
            data: "action=loadOne&idTicket=" + idTicket,
            success: function(response) {
                $("#loadingAnimationConteneur").hide();
                $("#specProjet").show();
                $("#specProjet").addClass('over');
                $("#specProjetContent").show();
                var objetJson = JSON.parse(response);
                $("#specificationBloc").html(objetJson["specificationBloc"]);
                $("#impactBloc").html(objetJson["impactBloc"]);
                $("#partiesPrenantesBloc").html(objetJson["partiesPrenantesBloc"]);
                $("#impactSIBloc").html(objetJson["impactSIBloc"]);
                $("#chiffrageMoaBloc").html(objetJson["chiffrageMoaBloc"]);
                $("#chiffrageMoeBloc").html(objetJson["chiffrageMoeBloc"]);
                $("#planificationBloc").html(objetJson["planificationBloc"]);
                $("#autreBloc").html(objetJson["autreBloc"]);


                var param = window.location.href.split("=")[2];
                if (param !== "NOUVEAU_BESOIN") {
                    $("#specificationBloc").hide();
                    $("#impactBloc").hide();
                    $('[id*="fieldMETIER-"]').attr("disabled", "disabled");
                    $('[id*="bouttonMETIER-"]').hide();
                }
                if (param !== "VALIDE_METIER") {
                    $("#partiesPrenantesBloc").hide();
                    $("#impactSIBloc").hide();
                    $('[id*="fieldMOA-"]').attr("disabled", "disabled");
                    $('[id*="bouttonMOA-"]').hide();
                }
                if (param !== "VALIDE_IMPACT") {
                    $("#chiffrageMoaBloc").hide().addClass("dial");
                    $('[id*="fieldCMOA-lig_"]').attr("contenteditable", false);
                     $('[id*="bouttonCMOA-"]').hide();
                }
                if (param !== "VALIDE_MOA") {
                    $("#chiffrageMoeBloc").hide().addClass("dial");
                    $('[id^=fieldCMOE-lig_]').attr("contenteditable", false);
                    $('[id*="bouttonCMOE-"]').hide();
                }
                if (param !== "VALIDE_MOE") {
                    $("#planificationBloc").hide();
                    $('[id*="fieldPMO-"]').attr("disabled", "disabled");
                    $('[id*="bouttonPMO-"]').hide();
                }

                //Defnir nbr de colonnes de calcul de somme
                var nbrColonneDeCalcul = 4;
                //Initialisation colonnes de calcul de somme
                for (i = 1; i <= nbrColonneDeCalcul; i++) {
                    var somme = 0;
                    $('[id*="_col_' + i + '"]').each(function() {
                        var val = $(this).text();
                        if ($.isNumeric(val)) {
                            somme += parseFloat(val);
                        } else {
                            //alert('Veuillez entrer une valeur numérique.');
                        }
                        //limit the size of cell to 10 characters
                        var content_id = $(this).attr('id');
                        max = 7;
                        //binding keyup/down events on the contenteditable div
                        $('#' + content_id).keyup(function(e) {
                            check_charcount(content_id, max, e);
                        });
                        $('#' + content_id).keydown(function(e) {
                            check_charcount(content_id, max, e);
                        });
                    });
                    $('[id*="lig_somme_' + i + '"]').text(somme);
                }
                //Calcul de somme pour chaque modification
                $("td").on("keyup", function() {
                    if ($(this).hasClass("ciffrageMOA")) {
                        var num_col = $(this).index();
                    } else if ($(this).hasClass("ciffrageMOE")) {
                        var num_col = $(this).index() + 2;
                    }
                    var somme = 0;
                    $('[id*="_col_' + num_col + '"]').each(function() {
                        var val = $(this).text();
                        if ($.isNumeric(val)) {
                            somme += parseFloat(val);
                        } else if (!val.trim()) {
                            //var is empty or whitespace
                            somme += parseFloat(0);
                        } else {
                            alert('Veuillez entrer une valeur numérique.');
                        }
                    });
                    $('[id*="lig_somme_' + num_col + '"]').text(somme);
                });
                $("#specProjet").html("Spécification du Besoin <strong> #" + idTicket + ": " + $("#fieldMETIER-summary").val() + "</strong>");
                //DATE TIME PICKER
                ActivateDateTimePicker('fieldMETIER-date_realisation');
                ActivateDateTimePicker('fieldPMO-date_demarrage');
                ActivateDateTimePicker('fieldPMO-date_app_prod');
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }

    function mergeBesoinIntoDBbyField(bloc, action) {
        //alert(action);
        var contextPath = "<%=request.getContextPath()%>";
        var idTicket = window.location.href.split("=")[1];
        var field, value = "";
        var dataContent = "action=" + action + "&idTicket=" + idTicket;
        //alert(dataContent);
        $('[id^=field' + bloc + '-]').each(function() {
            field = this.id.split('-').pop();
            if (field.match("^lig_")) {
                //read html if id starts with lig_
                value = $('#' + this.id).html();
            } else {
                //read val if not
                value = $('#' + this.id).val();
            }
            //alert(field + ' ' + value);
            dataContent = dataContent + "&" + field + "=" + value;
        });
        //alert(dataContent);
        $.ajax({
            type: "POST",
            url: contextPath + "/GestionDemandesMetiersServlet",
            data: dataContent,
            success: function(response) {
                //alert(response);
                var objetJson = JSON.parse(response);
                $("#messageResultatPersist").text(objetJson["message"]);
                $("#messageResultatPersist").show();
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 3000);
            },
            error: function(e) {
                //alert('Error: '+e);
            }
        });
    }

    $("input").click(function() {
        $("#" + this.id + "Bloc").toggle();
    });
</script>
