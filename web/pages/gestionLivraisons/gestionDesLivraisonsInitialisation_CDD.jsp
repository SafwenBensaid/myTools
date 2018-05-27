<%@page import="entitiesTrac.Ticket"%>
<%@page import="tools.Tools"%>
<%@page import="tools.Configuration"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<link rel="stylesheet" type="text/css" href="http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.2/css/bootstrap-combined.min.css">


<%
    Map<String, List<Map<String, Object>>> listPipeTickets = (Map<String, List<Map<String, Object>>>) request.getSession().getAttribute("mapPipeTickets");
    List<Map<String, Object>> listRelease = listPipeTickets.get(Configuration.parametresList.get("phaseRelease"));
    List<Map<String, Object>> listProjet = listPipeTickets.get("QUALIFICATION_PROJET");
    List<Map<String, Object>> listHotfix = listPipeTickets.get("HOT FIXE TEST");
    List<Map<String, Object>> listActionsAChaudTest = listPipeTickets.get("ACTION A CHAUD TEST");
    if (listActionsAChaudTest != null) {
        if (listHotfix != null) {
            listHotfix.addAll(listActionsAChaudTest);
        } else {
            listHotfix = listActionsAChaudTest;
        }
    }
    List<Map<String, Object>> listUpgrade = null;
    String phaseUpgrade = Configuration.parametresList.get("phaseUpgrade");
    if (phaseUpgrade.equals("QUALIFICATION_UPGRADE")) {
        listUpgrade = listPipeTickets.get("QUALIFICATION_UPGRADE");
    } else if (phaseUpgrade.equals("CERTIFICATION_UPGRADE")) {
        listUpgrade = listPipeTickets.get("CERTIFICATION_UPGRADE");
    } else {
        Tools.alertParEmail("PROBLEME PARAMETRAGE", "Veuillez vérifier le champ <b>phaseupgrade</b> dans la base de données de paramétrage, elle doit être QUALIFICATION_UPGRADE ou CERTIFICATION_UPGRADE");
    }
    //pipe d'harmonisation upgrade
    List<Map<String, Object>> listHarmonistaionUpgrade = listPipeTickets.get("HARMONISATION_C.UPGRADE");
    
    String connectedUser = Tools.getConnectedLogin();
    Map<String, List<String>> usersGroupMap = (Map<String, List<String>>) Configuration.usersGroupMap;
    boolean isAdmin = usersGroupMap.get(connectedUser).contains("ADMIN");

%>



<style>
    .entry:not(:first-of-type)
    {
        margin-top: 10px;
    }

    .row
    {
        margin-left: 10px !important;
    }
    .leftBox{
        width:250px;
        float:left;
        height:100%;
        margin-left: 20px;
    }

    .rightBox{
        margin-left:250px;
    }
    /*sortable*/
    .placeholder {
        border: 1px solid green;
        background-color: white;
        -webkit-box-shadow: 0px 0px 10px #888;
        -moz-box-shadow: 0px 0px 10px #888;
        box-shadow: 0px 0px 10px #888;
        width: 570px;
    }
    .tile {
        height: 100px;
    }
    .imageRemove{
        margin-left: -15px;
    }
    .well{
        padding-top: 0px;
    }
    .control-group {
        margin-bottom: 0px !important;
    }
    input, .uneditable-input {
        width: 505px;
        max-width: 505px;
    }
    textarea{
        width: 539px;
        max-width: 539px;
        margin-bottom: 0px;
        min-height: 150px;
    }
    .topmenu ul {
        margin-left: 0px !important;
    }
    hr{
        border-top: 1px solid #35427E;            
        border-bottom: 1px solid #35427E;
        height: 2px;
        background-color: white;
    }
    .input-append, .input-prepend {    
        font-size: 14px;
        color: #35427E;
        text-decoration: none;
    }
    .titrepetit{
        margin-right: 5px;
    }

    select, textarea, input[type="text"], input[type="password"], input[type="datetime"], input[type="datetime-local"], input[type="date"], input[type="month"], input[type="time"], input[type="week"], input[type="number"], input[type="email"], input[type="url"], input[type="search"], input[type="tel"], input[type="color"], .uneditable-input {
        margin-bottom: 0px;
    }

    .input_red:-moz-placeholder, .input_red::-webkit-input-placeholder  {
        color: #e60000;
    }

    .droit{
        width:26px !important;
        height:14px;
    }
    .typeDetails{
        margin-bottom: 10px
    }
    select{
        width: 260px;
    }
    .leftSpace{
        margin-left: 4px !important;
    }
    .animation{
        text-align: center;
    }
</style>

<script>

    /*
     var arr = [ "one", "two", "three", "four", "five" ];
     var obj = { one:1, two:2, three:3, four:4, five:5 };
     
     jQuery.each(arr, function() {
     $("#" + this).text("My id is " + this + ".");
     return (this != "four"); // will stop running to skip "five"
     });
     
     jQuery.each(obj, function(i, val) {
     $("#" + i).append(document.createTextNode(" - " + val));
     });
     */

    var problemeAuthentification = true;
    var allTypesRegles;
    var problemeLivraison = true;

    function GetAllTypesRegles(contextPath) {
        $.ajax({
            type: "POST",
            url: contextPath + "/GetAllTypesRegles",
            //async: false,
            success: function(response) {
                allTypesRegles = response;
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }

    function json2array(json) {
        var result = [];
        var keys = Object.keys(json);
        keys.forEach(function(key) {
            alert();
        });
    }

    $(document).ready(function() {
        var contextPath = "<%=request.getContextPath()%>";
        GetAllTypesRegles(contextPath);
        //alert(allTypesRegles["REJET"]["CUSTOMER"]);

        window.countElementsIn = function(fieldcontainerSel, patternSel) {
            var parentE = jQuery(fieldcontainerSel);
            var parentCnt = parentE.length;
            if (parentCnt > 0) {
                return parentE.find(patternSel);
            }
            return 0;
        };


        window.genInput = function(name, val, extraClass, id, placeholder, type, extraAttributes) {
            var ch = '<input required autocomplete="off" name="' + name + '" value="' + val + '" class="input form-control ' + extraClass + ' ' + id + '" id="' + id + '" placeholder="' + placeholder + '" type="' + type + '" ';
            for (var key in extraAttributes)
            {
                var value = extraAttributes[key];
                ch += key + '=\"' + value + '\" ';
            }
            ch += '/>';
            return ch;
        };

        window.genInputWithErrorDiv = function(name, val, extraClass, id, placeholder, type, extraAttributes) {
            var ch = '<div class="control-group">';
            ch += '<input required autocomplete="off" name="' + name + '" value="' + val + '" class="input form-control ' + extraClass + ' ' + id + '" id="' + id + '" placeholder="' + placeholder + '" type="' + type + '" ';
            for (var key in extraAttributes)
            {
                var value = extraAttributes[key];
                ch += key + '=\"' + value + '\" ';
            }
            ch += '/>';
            ch += '<span class="help-inline"></span></div>';
            return ch;
        };

        window.genSelect = function(id, defaultVal, values) {
            var ch = '<select required id="' + id + '" name="' + id + '">';
            ch += '<option value="" disabled selected>' + defaultVal + '</option>';
            for (var val in values)
            {
                ch += '<option value="' + values[val] + '">' + values[val] + '</option>';
            }
            ch += '</select>';
            return ch;
        };

        window.genTextArea = function(name, val, extraClass, id, placeholder) {
            return '<div class="control-group"><textarea required class="input form-control ' + extraClass + '" id="' + id + '"  name="' + name + '" value="' + val + '" placeholder="' + placeholder + '" data-items="8"></textarea><span class="help-inline"></span></div>';
        };

        window.genBtn = function(label, extraClass, actionClick) {
            return '<button class="btn ' + extraClass + '" onclick="' + actionClick + ';">' + label + '</button>';
        };

        window.genFileSelector = function(id, name) {
            return '<input required type="file" id="' + id + '" name="' + name + '"/>';
        };

        window.genNewRow = function(name, val, extraClass, id, placeholder, type) {
            //en cliquant sur le bouton add "+"
            switch (type) {
                case 'text':
                    return '<div class="row-fluid record"><div class="span12">' + genInput(name, val, extraClass, id, placeholder, 'text') + ' ' + genBtn('-', 'btn-danger remove-parent', 'removeInput(this)') + '</div></div>';
                    break;
                default:
                    return 'XXX';
            }
            console.log('Check your code Im broken...');
            return ''; // should never happen
        };
    });
    function addElement(btnAdd) {
        //(name, val, extraClass, id, placeholder, type) {
        var me = jQuery(btnAdd);
        var count = $(me).closest(".typeDetails").find(".count");
        var valCount = $(count).val();
        var addTo = $(me).parent().parent().find('.fieldcontainer');
        var existingInput = $(me).parent().prev().last(".row-fluid").find(".input");
        var valPrecedent = $(existingInput).val();
        var newName = $(existingInput).attr('name');
        var newId = $(existingInput).attr('id');
        var newPlaceholder = $(existingInput).attr('placeholder');
        var newType = $(existingInput).attr('type');
        if ($.trim(valPrecedent).length > 0) {
            var newRec = genNewRow(newName, valPrecedent, '', newId, newPlaceholder, newType);
            jQuery(addTo).append(newRec);
            $(me).parent().prev().find(".row-fluid:last-of-type").find(".input").val('');
            valCount++;
            $(count).val(valCount);
        }
    }
    function removeInput(btn) {
        var nbrInput = countElementsIn(jQuery(btn).parent().parent().parent().parent().parent(), ".input").length;
        var count = $(btn).closest(".typeDetails").find(".count");
        var valCount = $(count).val();
        valCount--;
        $(count).val(valCount);
        if (nbrInput > 1) {
            jQuery(btn).closest(".row-fluid").remove();
        } else {
            jQuery(btn).parent().find(".input").val("");
        }
    }

    $(function() {
        $(".grid").sortable({
            tolerance: 'pointer',
            revert: 'true',
            placeholder: 'span2 well placeholder tile',
            forceHelperSize: true,
            cancel: ".control-group",
            update: function(event, ui) {
                //var currPos2 = ui.item.index();
                gestionSuffixe();
            }
        });
    });

    function createLivraisonDiv(option, idDiv_OBJETST24) {
        var now = $.now();
        var selectedOptionSansEspace = option.replace(/ /g, '');
        if (option.trim().length > 0) {
            var newContent = "<div class='well span6 div_" + selectedOptionSansEspace + "' id='div_" + selectedOptionSansEspace + now + "'><img src='images/remove.png' class='imageRemove'  onclick='removeElement(this);'/><span class='titreSouligne'>" + option + "</span><input type='hidden' name='typeDuBloc' id='typeDuBloc' value='" + option + "'><div class='typeDetails' id='" + now + "'></div></div>";
            if (idDiv_OBJETST24 === null) {
                $('#rowsContainer').append(newContent);
            } else {
                $("<p>Test</p>").insertBefore(".inner");
                $(newContent).insertBefore('#' + idDiv_OBJETST24);
            }

            //$('option:selected', this).remove();//
            var newField = createField(option, '', option);
            $('#' + now).html(newField);
            $("#typeLivrable").val($("#typeLivrable option:first").val());
            gestionSuffixe();
            // à ajouter routines, etc ...
            if (option === 'OBJETS T24') {
                if (problemeAuthentification === true) {
                    testConnexion();
                }
            }
        }
        return "div_" + selectedOptionSansEspace + now;
    }



    function testConnexion() {
        var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
        var errorDiv = $(".t24livraison").last().parent();
        startStopAnimation($(".animation"), true, true);
        return $.ajax({
            type: "POST",
            url: contextPath + "/GestionLivraisonControlerServlet",
            data: "action=testConnexion&circuit=" + circuit,
            success: function(response) {
                startStopAnimation($(".animation"), false, true);
                if (response.indexOf("Problème") >= 0) {
                    problemeAuthentification = true;
                    appendMessage(errorDiv, response, "error");
                } else {
                    problemeAuthentification = false;
                }
            },
            error: function(e) {
                problemeAuthentification = true;

                return $.ajax({
                    type: "POST",
                    url: contextPath + "/CatchProblemeUserSelfService",
                    data: "action=notificationProbleme&circuit=" + circuit,
                    success: function() {
                        //startStopAnimation($(".animation"), false, true);
                    },
                    error: function(e) {
                        //alert('Error: ' + e);
                    }
                });


                appendMessage(errorDiv, "Votre user T24 contient un problème, veuillez contacter l'administrateur", "error");
                startStopAnimation($(".animation"), false, true);
            },
            timeout: 90000 // sets timeout to 90 seconds
        });
    }

    $(document).ready(function()
    {
        $("#SubmitSpan").hide();
        $(".groupe1").hide();
        $("#loadingAnimationConteneur").hide();
        $("#traitementLivraisons").hide();
        $('#typeLivrable').change(function() {
            var option = $(this).find('option:selected').val();
            createLivraisonDiv(option, null);
        });
        $("#getLivraisonDetails").click(function() {
            $("#conteneurTablesTickets").hide();
            openTracTicketInNewTab($("#numLivraison").val(), "LIVRAISON");
        });
        /*
         $('#LivrerTicketAction').submit(function(event) {
         var nbrDivT24 = $(".t24livraison").length;
         if (nbrDivT24 === 0) {
         testErreurs();
         }
         if (problemeLivraison === true) {
         validateT24Type();
         event.preventDefault();
         } else {
         //$(this)[0].submit();
         event.preventDefault();
         alert("submit");
         submitData();
         }
         });*/




        $('#LivrerTicketAction').submit(function(event) {
            if ($("#btnSubmit").is(":visible") === false) {
                validateAllFoldersPath();
                validateT24Type();
                var nbrDivT24 = $(".t24livraison").length + $(".folderPath").length + $(".REBUILDSYSTEM").length;
                if (nbrDivT24 === 0) {
                    $("#ValidationSpan").hide();
                    $("#SubmitSpan").show();
                    startStopAnimation(null, true, false);
                }
                event.preventDefault();
            } else {
                setAlertMessages();
                $(this)[0].submit();
            }
        });
    });

    function setAlertMessages() {
        $(".t24livraison").each(function() {
            var warningDiv = $(this).parent().next();
            var warningMessage = $(warningDiv).find('.help-inline').html();
            $(warningDiv).find('.warningT24').val(warningMessage);
        });
    }

    function startStopAnimation(animation, stop, endGlobal) {
        if (stop === true) {
            $("#rowsContainer input, #rowsContainer textarea").each(function() {
                $(this).attr('readonly', 'readonly');
            });
            $('.imageRemove').each(function() {
                $(this).hide();
            });
            $('#ValidationSpan').hide();
            $('select').each(function() {
                $(this).attr('readonly', 'readonly');
            });
            if (animation !== null) {
                $(animation).show();
            }
        } else {
            if (endGlobal === true) {
                $("#rowsContainer input, #rowsContainer textarea").each(function() {
                    if (!$(this).hasClass("mustBeDisabled")) {
                        $(this).removeAttr('readonly');
                    }
                });
                $('.imageRemove').each(function() {
                    $(this).show();
                });
                $('#ValidationSpan').show();
                $('select').each(function() {
                    $(this).removeAttr('readonly');
                });
            }
            if (animation !== null) {
                $(animation).hide();
            }
        }
    }

    function testErreurs() {
        var msgErreur = "";
        $(".error .help-inline").each(function() {
            msgErreur += $(this).html().trim();
        });

        if (msgErreur.length === 0) {
            problemeLivraison = false;
            $("#ValidationSpan").hide();
            $("#SubmitSpan").show();
            startStopAnimation(null, true, false);
        } else {
            problemeLivraison = true;
        }
    }
    /*
     function submitData() {
     var dataContent = getParametersForm();
     $.ajax({
     type: "POST",
     url: "TraitementLivraisonAutoForm.do",
     data: dataContent,
     success: function(response) {
     stopWorker();
     window.location = contextPath + "/test.do";
     },
     error: function(e) {
     //alert('Error: ' + e);
     }
     });
     }
     */
    function getParametersForm() {
        var dataContent = "";
        $("#LivrerTicketAction input, #LivrerTicketAction textarea").each(function() {
            var id = $(this).attr('id');
            if (typeof id !== 'undefined' & id !== null) {
                var val = $(this).val();
                dataContent += id + "=" + val + "&";
            }
        });
        $("#rowsContainer select").each(function() {
            var id = $(this).attr('id');
            if (typeof id !== 'undefined' & id !== null) {
                var val = $(this).find(":selected").text();
                dataContent += id + "=" + val + "&";
            }
        });
        var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
        dataContent += "circuit=" + circuit;
        return dataContent;
    }

    function validateAllFoldersPath() {
        var errorMessage;
        var nbrDivT24 = $(".t24livraison").length;
        var nbrItemsFolder = $('.folderPath').length;
        var cmpItemsFolder = 0;
        $(".folderPath").each(function() {
            errorMessage = "";
            var animation = $(this).parent().parent().find('.animation');
            var errorDiv = $(this).parent();
            var text = $(this).val();
            errorDiv.removeClass("error");
            var textArea = $(this);
            $(errorDiv).find('.help-inline').empty();
            startStopAnimation(animation, true, false);
            var promise = callValidateFolderPath(text, errorDiv);
            promise.done(function() {
                cmpItemsFolder++;
                if (cmpItemsFolder === nbrItemsFolder) {
                    if (nbrDivT24 === 0) {
                        //execution terminee
                        startStopAnimation(animation, false, true);
                        endLog();
                        testErreurs();
                    } else {
                        startStopAnimation(animation, true, true);
                        $(animation).hide();
                    }
                }
            });
        });
    }

    function callValidateFolderPath(text, errorDiv) {
        var deferredObject = $.Deferred();
        $.when(validateFolderPath(text, errorDiv)).done(function() {
            deferredObject.resolve();
        });
        return deferredObject.promise();
    }

    function validateFolderPath(folder, errorDiv) {
        var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
        startStopAnimation($(".animation"), true, true);
        return $.ajax({
            type: "POST",
            url: contextPath + "/GestionLivraisonControlerServlet",
            data: "action=testExistFolder&folder=" + folder + "&circuit=" + circuit,
            success: function(response) {
                if (response.trim().length > 0) {
                    appendMessage($(errorDiv), response, "error");
                }
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }

    function validateT24Type() {
        $("#ticketManuel").val("AUTOMATIQUE");
        var alertMessage;
        var errorMessage;
        var message;
        var nbrItemsT24 = $('.t24livraison').length;
        var cmpItemsT24 = 0;
        var nbrItemsSS = $('.REBUILDSYSTEM').length;
        var cmpItemsSS = 0;
        var nbrDivT24 = $(".t24livraison").length;
        var nbrItemsFolder = $('.folderPath').length;
        $(".t24format").each(function() {
            errorMessage = "";
            alertMessage = "";
            message = "";
            var lexicalError = false;
            var lexicalSSError = false;
            var chRes = "";
            var contenu = $(this).val();
            var splitted = contenu.split("\n");
            var sansLignesVides = splitted.filter(function(v) {
                return v !== '';
            });
            var trimed = sansLignesVides.map(Function.prototype.call, String.prototype.trim);
            //validation lexicale
            for (var i = 0; i < trimed.length; i++) {
                trimed[i] = trimed[i].replace(/\s/g, "");
                if (trimed[i].match(">$")) {
                    lexicalError = true;
                }
                if ($(this).hasClass("REBUILDSYSTEM")) {
                    if (!trimed[i].match("^STANDARD.SELECTION>")) {
                        lexicalSSError = true;
                    }
                }
                chRes += trimed[i] += "\n";
                var nbrSup = (trimed[i].match(/>/g) || []).length;
                if (nbrSup < 1 || nbrSup > 1) {
                    lexicalError = true;
                }
            }
            $(this).val(chRes.trim());
            var animation = $(this).parent().parent().find('.animation');
            var warningDiv = $(this).parent().next();
            var errorDiv = $(this).parent();
            errorDiv.removeClass("error");
            $(warningDiv).find('.help-inline').empty();
            $(errorDiv).find('.help-inline').empty();
            var textArea = $(this);
            if (lexicalError === true || lexicalSSError === true) {
                if (lexicalError === true) {
                    appendMessage(errorDiv, "<li>Chaque ligne doit respecter ce format XXXX>YYYY</li>", "error");
                }
                if (lexicalSSError === true) {
                    appendMessage(errorDiv, "<li>Chaque ligne doit respecter ce format STANDARD.SELECTION>YYYY</li>", "error");
                }
            } else {
                $(warningDiv).find('.help-inline').empty();
                $(errorDiv).find('.help-inline').empty();
                if ($(this).hasClass("t24livraison")) {
                    startStopAnimation(animation, true, false);
                    //validation synthaxique
                    for (var i = 0; i < trimed.length; i++) {
                        var type = getObjectType(trimed[i]).trim();
                        var objName = getObjectName(trimed[i]).trim();
                        validateEbCompositeScreenTypes(type, this);
                        validateRelatedRoutinesTypes(type);
                        validateRelatedRebuildSystem(type, objName);
                        validateRejectedTypes(type, errorDiv);
                        validateDomainesTypes(type, errorDiv);
                        validateBiatStBatchTypes(type, objName, this, errorDiv);
                        validateCompaniesTypes(type, objName, this, errorDiv);
                        validateManualActionsTypes(type, warningDiv);
                    }
                    var selectedCompany = $(this).parent().parent().find('.selectedMnemonic').val();
                    var packName = $(this).parent().parent().find('.packName').val();
                    var promise = appelValidateServicesDepotTypes(chRes, animation, warningDiv);
                    promise.done(function() {
                        var promise1 = pmTransfertAnalyse(warningDiv, errorDiv, animation, selectedCompany, packName, chRes, textArea);
                        promise1.done(function() {
                            cmpItemsT24++;
                            if (cmpItemsT24 === nbrItemsT24) {
                                //execution terminee
                                startStopAnimation(animation, false, true);
                                endLog();
                                testErreurs();
                            } else {
                                //execution en cours
                                startStopAnimation(animation, false, false);
                            }
                        });
                    });
                } else if ($(this).hasClass("REBUILDSYSTEM")) {
                    cmpItemsSS++;
                    if (cmpItemsSS === nbrItemsSS) {
                        if (nbrDivT24 === 0 && nbrItemsFolder === 0) {
                            //execution terminee
                            testErreurs();
                        }
                    }
                }
            }
        });
    }



    function reviserLivrables() {
        startStopAnimation(null, false, true);
        $("#ValidationSpan").show();
        $("#SubmitSpan").hide();
        problemeLivraison = true;
    }

    function endLog() {
        var contextPath = "<%=request.getContextPath()%>";
        var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
        $.ajax({
            type: "POST",
            url: contextPath + "/GestionLivraisonControlerServlet",
            data: "action=endLog&circuit=" + circuit,
            success: function(response) {
                //alert("logEndede");
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }

    function createRoutineIfNotExist(routine) {
        var routineExists = false;
        var allRoutines = [];
        $('.EXECUTIONROUTINET24').each(function() {
            allRoutines.push($(this).val());
            if (jQuery.inArray(routine, allRoutines) >= 0) {
                routineExists = true;
            }
        });
        if (routineExists === false) {
            if ($('.EXECUTIONROUTINET24').length > 0) {
                $(".EXECUTIONROUTINET24").last().parent().parent().parent().parent().find('.add').click();
            } else {
                createLivraisonDiv("EXECUTION ROUTINE T24", null);
            }
            $(".EXECUTIONROUTINET24").last().val(routine);
        }
        //si t24 après routines => echange de positions
        var posRoutines = $(".div_EXECUTIONROUTINET24").last().position()["top"];
        var posT24 = $(".div_OBJETST24").last().position()["top"];
        if (posRoutines < posT24) {
            rtn = $(".div_EXECUTIONROUTINET24").last();
            t24 = $(".div_OBJETST24").last();
            rtn.insertAfter(t24);
        }
    }

    function validateManualActionsTypes(type, warningDiv) {
        if (allTypesRegles.hasOwnProperty("MANUEL")) {
            var alertMessage = "";
            var manuelles = allTypesRegles["MANUEL"];
            var keys = Object.keys(manuelles);
            keys.forEach(function(key) {
                if (type === key) {
                    alertMessage += "<li><b>" + type + ":</b> " + manuelles[key] + "</li>";
                }
            });
            if (alertMessage.length > 0) {
                alertMessage = "<b>Ce ticket sera traite manuellement car:</b>" + alertMessage;
                appendMessage(warningDiv, alertMessage, "warning");
            }
        }
    }

    function validateDomainesTypes(type, errorDiv) {
        var errorMessage = "";
        jQuery.each(allTypesRegles, function(cle, valeur) {
            if (cle.match("^DOMAINE ")) {
                var keys = Object.keys(valeur);
                keys.forEach(function(typeObjets) {
                    if (type === typeObjets) {
                        if (!cle.match($("#milestone").val())) {
                            //alert(type+":PB");
                            errorMessage += "<li>" + valeur[typeObjets] + "</li>";
                        }
                        //alert(valeur[typeObjets]);
                    }
                });
            }
        });
        if (errorMessage.length > 0) {
            appendMessage(errorDiv, errorMessage, "error");
        }
    }

    function validateRelatedRoutinesTypes(type) {
        if (allTypesRegles.hasOwnProperty("ROUTINE")) {
            var routines = allTypesRegles["ROUTINE"];
            var keys = Object.keys(routines);
            keys.forEach(function(key) {
                if (type === key) {
                    createRoutineIfNotExist(routines[key]);
                }
            });
        }
    }

    function validateRelatedRebuildSystem(type, objName) {
        if (type === "LOCAL.REF.TABLE") {
            var rebuildSystemExists = false;
            var allRebuildSystem = [];
            $('.REBUILDSYSTEM').each(function() {
                allRebuildSystem.push($(this).val());
                if (jQuery.inArray("STANDARD.SELECTION>" + objName, allRebuildSystem) >= 0) {
                    rebuildSystemExists = true;
                }
            });
            if (rebuildSystemExists === false) {
                createLivraisonDiv("REBUILD SYSTEM", null);
                $(".REBUILDSYSTEM").last().val("STANDARD.SELECTION>" + objName);
            }
        }
    }

    function validateEbCompositeScreenTypes(type, textArea) {
        if (type === "EB.COMPOSITE.SCREEN") {
            $(textArea).parent().parent().find('.nbrIter').val(2);
        }
    }

    function validateRejectedTypes(type, errorDiv) {
        if (allTypesRegles.hasOwnProperty("REJET")) {
            var errorMessage = "";
            var rejets = allTypesRegles["REJET"];
            var keys = Object.keys(rejets);
            keys.forEach(function(key) {
                if (type === key) {
                    errorMessage += "<li>" + rejets[key] + "</li>";
                }
            });
            if (errorMessage.length > 0) {
                appendMessage(errorDiv, errorMessage, "error");
            }
        }
    }

    function getObjectType(obj) {
        var splitted = obj.split(">");
        return splitted[0];
    }

    function getObjectName(obj) {
        var splitted = obj.split(">");
        return splitted[1];
    }

    function validateBiatStBatchTypes(type, objName, textArea, errorDiv) {
        var errorMessage = "";
        var serviceExists = false;
        try {
            $('.DOLLARUservice').each(function() {
                if ($(this).val().trim().indexOf(type + ">" + objName) === 0) {
                    serviceExists = true;
                }
            });
        }
        catch (err) {
            alert(err.message);
        }
        if (type === "BIAT.ST.BATCH") {
            if (!serviceExists) {
                createLivraisonDiv("DOLLAR U", null);
                $(".DOLLARUservice").last().val(type + ">" + objName);
                errorMessage += "<li>Le service <b>" + type + ">" + objName + "</b> a ete ajoute au bloc <b>DOLLAR U</b>, priere de le paramètrer</li>";
            }
        }

        if (errorMessage.length > 0) {
            appendMessage(errorDiv, errorMessage, "error");
        }
    }

    function validateCompaniesTypes(type, objName, textArea, errorDiv) {
        var errorMessage = "";
        var selectedCompany = $(textArea).parent().parent().find('.selectedMnemonic').val();
        try {
            if (((type === "ASSET.CLASS.PARAMETER" || type === "TELLER.PARAMETER" || type === "PD.PARAMETER") && objName === "TN0010001") || (type === "EB.FINANCIAL.SYSTEM" && objName === "T24")) {
                if (selectedCompany !== "BNK") {
                    errorMessage += "<li>L'objet <b>" + type + ">" + objName + "</b> doit etre deploye avec la company <b>BNK</b></li>";
                }
            }
            if (type === "TAX.GEN.CONDITION" || type === "FT.GEN.CONDITION") {
                if (selectedCompany !== "TN1" && selectedCompany !== "BNK") {
                    errorMessage += "<li>L'objet <b>" + type + ">" + objName + "</b> doit être deploye avec la company <b>TN1</b> ou <b>BNK</b></li>";
                }
            }
        }
        catch (err) {
            alert(err.message);
        }
        if (errorMessage.length > 0) {
            appendMessage(errorDiv, errorMessage, "error");
        }
    }

    function pmTransfertAnalyse(warningDiv, errorDiv, animation, selectedCompany, packName, objectList, textArea) {
        var deferredObject = $.Deferred();
        $.when(callPmTransfertAnalyse(warningDiv, errorDiv, animation, selectedCompany, packName, objectList, textArea)).done(function() {
            deferredObject.resolve();
        });
        return deferredObject.promise();
    }

    function callPmTransfertAnalyse(warningDiv, errorDiv, animation, selectedCompany, packName, objectList, textArea) {
        var msgErreur = $(errorDiv).find('.help-inline').html().replace("&gt;", ">").trim();
        if (msgErreur.length === 0) {
            var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
            return $.ajax({
                type: "POST",
                url: contextPath + "/GestionLivraisonControlerServlet",
                data: "action=pmTransfertAnalyse&circuit=" + circuit + "&selectedCompany=" + selectedCompany + "&packName=" + packName + "&objectList=" + objectList,
                success: function(jsonArrayResponse) {
                    var tn1 = jsonArrayResponse["TN1"];
                    var bnk = jsonArrayResponse["BNK"];
                    var packNameTn1 = jsonArrayResponse["PACK_NAME_TN1"];
                    var packNameBnk = jsonArrayResponse["PACK_NAME_BNK"];
                    var problemeObjets = jsonArrayResponse["PROBLEME_OBJETS"];
                    var structurePure = jsonArrayResponse["STRUCTURE_PURE"];
                    var newPostPack = jsonArrayResponse["NEW_POST_PACK"];
                    var postPack = jsonArrayResponse["POST_PACK"];
                    if (problemeObjets.length > 0) {
                        appendMessage(errorDiv, problemeObjets, "error");
                    } else {
                        var packName = $(errorDiv).parent().find(".packName");
                        var suffixe = $(errorDiv).parent().find(".suffixe");
                        if (structurePure.length > 0) {
                            //packName
                            $(packName).val(packNameBnk);
                            $(suffixe).val("***");
                            //cocher BNK
                            var bnkRadio = $(errorDiv).parent().find(".BNK");
                            var nowBnk = $(bnkRadio).attr("name").replace("mnemonic", "");
                            selectCompanyMultiple(bnkRadio, nowBnk);
                        }
                        if (tn1.length > 0) {
                            $(textArea).val(tn1);
                            $(packName).val(packNameTn1);
                            $(suffixe).val("***");
                        }
                        if (bnk.length > 0) {
                            var div_OBJETST24 = $(".div_OBJETST24").closest(".div_OBJETST24");
                            var idDiv_OBJETST24 = div_OBJETST24.attr("id");
                            var idNewDiv_OBJETST24 = createLivraisonDiv("OBJETS T24", idDiv_OBJETST24);
                            //new suffixe
                            var newSuffixe = $("#" + idNewDiv_OBJETST24).find(".suffixe");
                            var newPackName = $("#" + idNewDiv_OBJETST24).find(".packName");
                            $(newSuffixe).val("***");
                            $(newPackName).val(packNameBnk);
                            //cocher new BNK
                            var newBnkRadio = $("#" + idNewDiv_OBJETST24).find(".BNK");
                            var nowBnk = $(newBnkRadio).attr("name").replace("mnemonic", "");
                            selectCompanyMultiple(newBnkRadio, nowBnk);
                            //remplir les obj T24
                            var newTextArea = $("#" + idNewDiv_OBJETST24).find(".t24livraison");
                            newTextArea.val(bnk);
                        }
                        if (newPostPack.length > 0) {
                            var warningMessage = "<br>Attention, il y a une <b>dependance de programmes</b>, il vaut mieux relivrer ces objets:<br>" + newPostPack;
                            appendMessage(warningDiv, warningMessage, "warning");
                        }
                        if (postPack.length > 0) {
                            var warningMessage = "<br>Attention, il y a une <b>modification de structure de table</b>, il vaut mieux relivrer ces objets:<br>" + postPack;
                            appendMessage(warningDiv, warningMessage, "warning");
                        }
                    }
                },
                error: function(e) {
                    //alert('Error: ' + e);
                }
            });
        }
    }


    function appelValidateServicesDepotTypes(chRes, animation, warningDiv) {
        var deferredObject = $.Deferred();
        $.when(validateServicesDepotTypes(chRes, animation, warningDiv)).done(function() {
            ////$('#typeLivrable').removeAttr('disabled');
            ////$('.imageRemove').show();
            deferredObject.resolve();
        });
        return deferredObject.promise();
    }
    /*
     function appelValidateServicesDepotTypes(type, objName, animation, warningDiv) {
     var deferredObject = $.Deferred();
     if (type === "BATCH" || type === "TSA.SERVICE") {
     $.when(validateServicesDepotTypes(type, objName, animation, warningDiv)).done(function() {
     var message = $(warningDiv).html();
     $('#typeLivrable').removeAttr('disabled');
     $('.imageRemove').show();
     deferredObject.resolve();                
     });
     } else {
     deferredObject.resolve();
     }
     return deferredObject.promise();
     }
     */


    function validateServicesDepotTypes(chRes, animation, warningDiv) {
        var message = "";
        ////$(animation).show();
        ////$('#typeLivrable').attr('disabled', 'disabled');
        ////$('.imageRemove').hide();
        var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
        return $.ajax({
            type: "POST",
            url: contextPath + "/GestionLivraisonControlerServlet",
            data: "action=testBiatStBatch&chRes=" + chRes + "&circuit=" + circuit,
            success: function(response) {
                ////$(animation).hide();
                if (response.trim().length > 0) {
                    appendMessage($(warningDiv), response, "warning");
                }
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });

    }

    function appendMessage(messageDiv, message, extraClass) {
        var contenuInput = $(messageDiv).find('.help-inline').html().replace("&gt;", ">");
        if (contenuInput.indexOf(message) < 0) {
            $(messageDiv).removeClass("error");
            $(messageDiv).removeClass("warning");
            $(messageDiv).find('.help-inline').append(message);
        }
        $(messageDiv).addClass(extraClass);
    }
    /*
     function displayMessage(controlGroupDiv, extraClass) {
     var problem = false;
     var message = $(controlGroupDiv).find('.help-inline').html();
     if (message.length > 0) {      //message = prefixeMessage + "<ul>" + message + "</ul>";
     //message = "<ul>" + message + "</ul>";
     problem = true;      $(controlGroupDiv).addClass(extraClass);
     $(controlGroupDiv).find('.help-inline').empty();
     $(controlGroupDiv).find('.help-inline').append(message);
     $(controlGroupDiv).find('.help-inline').show();
     } else {
     $(controlGroupDiv).removeClass(extraClass);
     $(controlGroupDiv).find('.help-inline').hide();
     }      }      */


    /*
     function displayMessage(controlGroupDiv, message, prefixeMessage, extraClass, extraMessage) {
     var problem = false;
     var toBeDisplayedMessage = "";
     if (message.length > 0 || extraMessage.length > 0) {
     if (message.length > 0) {
     problem = true;
     toBeDisplayedMessage += prefixeMessage;
     toBeDisplayedMessage += "<ul>" + message + "</ul>";
     }
     if (extraMessage.length > 0) {
     toBeDisplayedMessage += extraMessage;
     }
     $(controlGroupDiv).addClass(extraClass);
     $(controlGroupDiv).find('.help-inline').empty();
     $(controlGroupDiv).find('.help-inline').append(toBeDisplayedMessage);
     $(controlGroupDiv).find('.help-inline').show();
     } else {
     $(controlGroupDiv).removeClass(extraClass);
     $(controlGroupDiv).find('.help-inline').hide();
     }
     return problem;
     }
     */
    function removeElement(img) {
        var typeLivToBeDeleted = $(img).parent().find('.titreSouligne').html();
        $(img).parent().remove();
        gestionSuffixe();
    }

    function gestionSuffixe() {
        var comp = 0;
        $(".suffixe").each(function() {
            if ($(this).val() !== "***") {
                comp++;
                $(this).val(comp);
                miseAJourPackNameWidhoutArgs(this);
            }
        });
    }


    function createField(fieldName, fieldTitre, selectedOption) {
        var selectedOptionSansEspace = selectedOption.replace(/ /g, '');
        var now = $.now();
        fieldName += now;
        var newField = "<div class='row'>";
        newField += "    <input type='hidden' name='count' id='count' class='count' value='1' />";
        newField += "    <div class='control-group' id='fields'>";
        newField += "        <label class='control-label' for='" + fieldName + "'>" + fieldTitre + "</label>";
        newField += "        <div class='controls' id='profs'>";
        //newField += "            <form class='input-append'>";
        newField += "                <div class='row-fluid'>";
        newField += "                    <div class='fieldcontainer'>";
        newField += "                       <input type='hidden' class='selectedOptionSansEspace' value='" + selectedOption + "'>";
        if (selectedOption === 'EXECUTION PROGRAMME JSH' || selectedOption === 'EXECUTION ROUTINE T24') {

            if (selectedOption === 'EXECUTION ROUTINE T24') {
                placeholder = 'Routine T24';
            } else if (selectedOption === 'EXECUTION PROGRAMME JSH') {
                placeholder = 'Programme JSH';
            }
            newField += '<div class="row-fluid record"><div class="span12">';
            newField += genInput(selectedOptionSansEspace, '', '', selectedOptionSansEspace, placeholder, 'text');
            newField += genBtn('-', 'btn-danger remove-parent leftSpace', 'removeInput(this)');
            newField += '</div></div>';
            newField += "            </div>";
            newField += "            <div class='row-fluid record'>";
            newField += "                   <button class='btn add' type='button' onclick='addElement(this);'>+</button>";
        } else if (selectedOption === 'REBUILD SYSTEM') {
            newField += genInputWithErrorDiv(selectedOptionSansEspace, '', 't24format', selectedOptionSansEspace, 'STANDARD.SELECTION>XXX', 'text');
        } else if (selectedOption === 'CREATION INDEXES') {
            newField += genTextArea(selectedOptionSansEspace, "", "", selectedOptionSansEspace, "Liste d'indexes");
        } else if (selectedOption === 'CREATION COMPTES') {
            newField += genFileSelector(selectedOptionSansEspace, selectedOptionSansEspace);
        } else if (selectedOption === 'TRANSFERT FICHIERS') {
            newField += genFileSelector(selectedOptionSansEspace + "fichier", selectedOptionSansEspace + "fichier");
            //newField += genInput(selectedOptionSansEspace + "chemin", "", "folderPath", selectedOptionSansEspace + "chemin", "Chemin relatif à partir du dossier bnk.run/", 'text');
            newField += genInputWithErrorDiv(selectedOptionSansEspace + "chemin", "", "folderPath", selectedOptionSansEspace + "chemin", "Chemin relatif à partir du dossier bnk.run/", 'text');
            newField += "<div class='animation' style='display:none'><img src = 'images/loading-icon.gif' width = '40' heigth='40'/></div>";
            var modetransfert = new Array();
            modetransfert[0] = 'BINAIRE';
            modetransfert[1] = 'ASCII';
            newField += genSelect(selectedOptionSansEspace + "ModeTransfert", 'Selectionnez le mode de transfert', modetransfert);
        } else if (selectedOption === 'DOLLAR U') {
            var stage = new Array();
            stage[0] = 'Adhoc';
            stage[1] = 'Avant Cob';
            stage[2] = 'Apres Cob';
            stage[3] = 'Avant Ctos';
            stage[4] = 'Apres Ctos';
            stage[5] = 'Avant Ctos 1';
            stage[6] = 'Apres Ctos 1';
            stage[7] = 'INV';
            stage[8] = 'RAP';
            newField += genInput(selectedOptionSansEspace + "service", "", "", selectedOptionSansEspace + "service", "Nom du service", 'text');
            newField += genInput(selectedOptionSansEspace + "ordre", "", "", selectedOptionSansEspace + "ordre", "Ordre", 'text');
            newField += genSelect(selectedOptionSansEspace + "stage", 'Selectionnez un stage', stage);
        } else if (selectedOption === 'CREATION DOSSIERS') {
            var extraAttributes = {"min": "0", "max": "7", "step": "1"};
            //newField += genInput(selectedOptionSansEspace + "chemin", "", "folderPath", selectedOptionSansEspace + "chemin", "Chemin relatif à partir du dossier bnk.run/", 'text');
            newField += genInputWithErrorDiv(selectedOptionSansEspace + "chemin", "", "folderPath", selectedOptionSansEspace + "chemin", "Chemin relatif à partir du dossier bnk.run/", 'text');
            newField += "<div class='animation' style='display:none'><img src = 'images/loading-icon.gif' width = '40' heigth='40'/></div>";
            newField += '<br>Droits:  &emsp;  R ' + genInput(selectedOptionSansEspace + 'droitR', '7', 'droit', selectedOptionSansEspace + 'droitR', '', 'number', extraAttributes);
            newField += '  W ' + genInput(selectedOptionSansEspace + 'droitW', '7', 'droit', selectedOptionSansEspace + 'droitW', '', 'number', extraAttributes);
            newField += '  X ' + genInput(selectedOptionSansEspace + 'droitX', '7', 'droit', selectedOptionSansEspace + 'droitX', '', 'number', extraAttributes);
        } else if (selectedOption === 'BROWSER IB' || selectedOption === 'BROWSER T24') {
            newField += genInput(selectedOptionSansEspace + "export", "", "", selectedOptionSansEspace + "export", "Chemin de l'export sur le partage", 'text');
            newField += genInput(selectedOptionSansEspace + "tag", "", "", selectedOptionSansEspace + "tag", "Lien du tag", 'text');
        } else if (selectedOption === 'STREAMSERV TRANSACTIONNEL' || selectedOption === 'STREAMSERV BATCH') {
            newField += genInput(selectedOptionSansEspace + "export", "", "", selectedOptionSansEspace + "export", "Chemin de l'export sur le partage", 'text');
            newField += genInput(selectedOptionSansEspace + "projetSS", "", "", selectedOptionSansEspace + "projetSS", "Projet Stream Serve", 'text');
            newField += genInput(selectedOptionSansEspace + "lienTagSS", "", "", selectedOptionSansEspace + "lienTagSS", "Lien du Tag", 'text');
        } else if ((selectedOption === 'REVERSE OBJETS T24') || (selectedOption === 'SUPRESSION OBJETS T24')) {
            newField += genTextArea(selectedOptionSansEspace, "", "t24format", selectedOptionSansEspace, "Objets T24");
        } else if (selectedOption === 'OBJETS T24') {
            var contextPath = "<%=request.getContextPath()%>";
            newField += "                    <p></p>";
            newField += "                    <div class='contenu'>";
            newField += "                       <span class='titrepetit'>Mnemonic Company</span>";
            newField += "                       <span>";
            newField += "                           <label class=\"btn btn-default blue\" id='TN1'>";
            newField += "                               <input required type= 'radio' checked='checked' name='mnemonic" + now + "' class='TN1' value='TN1' onclick='selectCompanyMultiple(this, " + now + ");' id='CB_TN1'>";
            newField += "                               TN1";
            newField += "                    	</label>";
            newField += "                    	<label class=\"btn btn-default blue\" id='BNK'>";
            newField += "                               <input required type= 'radio' name='mnemonic" + now + "' class='BNK' value='BNK' onclick='selectCompanyMultiple(this, " + now + ");' id='CB_BNK'>";
            newField += "                               BNK";
            newField += "                    	</label>	";
            newField += "                    	<label class=\"btn btn-default blue\" id='Autre'>";
            newField += "                               <input required type= 'radio' name='mnemonic" + now + "' class='mnemonic" + now + "' value='Autre' onclick='selectCompanyMultiple(this, " + now + ");'  id='CB_Autre'>";
            newField += "                               AUTRE";
            newField += "                               <input id='autreMnemonic' class='autreMnemonic" + now + "' onclick='$(\".mnemonic" + now + "\").click();'  onchange='invoquerServletGetAllCompaniesMnemonics(this,contextPath);' style='width:50px; height:16px;'>";
            newField += "                    	</label>";
            newField += "                    	<input type='hidden' class='selectedMnemonic' id='selectedMnemonic' name='selectedMnemonic' value='TN1' />";
            newField += "                       </span>";
            newField += "                    </div>";
            newField += "                    <p></p>";
            newField += "                    <div class='contenu'>";
            newField += "                       <span class='titrepetit'>Pack</span>";
            newField += "                       <input required type='text' readonly='readonly' id='packName' name='packName' class='readonly packName mustBeDisabled' value='" + $("#packNameHidden").val() + "' style='width:162px;'/>";
            newField += "                       <span style='margin-left: 8px;'  class='titrepetit'>Suffixe</span>";
            newField += "                       <input required type='text' disabled id='suffixe'  class='suffixe mustBeDisabled' onkeypress='miseAJourPackNameWidhoutArgs(this);' onkeydown='miseAJourPackNameWidhoutArgs(this);' onkeyup='miseAJourPackNameWidhoutArgs(this);' style='width:40px;'/>";
            newField += "                       <span style='margin-left: 8px;'  class='titrepetit'>Nbr iter</span>";
            var extraAttributes = {"min": "1", "max": "3", "step": "1"};
            newField += genInput('nbrIter', '1', 'droit nbrIter', 'nbrIter', '', 'number', extraAttributes);
            newField += "                    </div>";
            newField += "                    <p></p>";
            newField += genTextArea(selectedOptionSansEspace, '', 't24format t24livraison', selectedOptionSansEspace, selectedOption);
            newField += '<div class="control-group"><input type="hidden" class="warningT24" id="warningT24" name="warningT24"/><span class="help-inline"></span></div>';
            newField += "<div class='animation' style='display:none'><img src = 'images/loading-icon.gif' width = '40' heigth='40'/></div>";
            //newField += "<input type='hidden' class='message' val=''/>";
        } else if (selectedOption === 'AUTRE LIVRABLE') {
            newField += '<div class="wikitoolbar">';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="strong" title="Bold text: \'\'\'Example\'\'\'" tabindex="400" style="background-image: url(\'images/strong.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="em" title="Italic text: \'\'Example\'\'" tabindex="400" style="background-image: url(\'images/em.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="heading" title="Heading: == Example ==" tabindex="400" style="background-image: url(\'images/heading.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="link" title="Link: [http://www.example.com/ Example]" tabindex="400" style="background-image: url(\'images/link.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="hr" title="Horizontal rule: ----" tabindex="400" style="background-image: url(\'images/hr.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="np" title="New paragraph" tabindex="400" style="background-image: url(\'images/np.png\')" />';
            newField += '    <input type="button" onclick="wikiButtonClick(this)" class="wikiButton" id="br" title="Line break: [[BR]]" tabindex="400" style="background-image: url(\'images/br.png\')" />';
            newField += '</div>';
            newField += '<span id="conteneurMessageTrac">';
            newField += genTextArea(selectedOptionSansEspace, "", "common wikiTextArea", selectedOptionSansEspace, "Description");
            newField += '</span>';
        }
        newField += "                    </div>";
        newField += "                </div>";
        //newField += "            </form>";
        newField += "        </div>";
        newField += "    </div>";
        newField += "</div>";
        return newField;
    }

    function preparerLivraison(idTicket, idAnomalie) {
        idTicket = idTicket.replace(/#/g, "");
        if (idAnomalie !== 0) {
            idAnomalie = idAnomalie.replace(/#/g, "");
        }
        $("#numLivraison").val(idTicket);
        $("#getLivraisonDetails").click();
        if (idAnomalie !== 0) {
            openTracTicketInNewTab(idAnomalie, "ANOMALIE");
        }
    }

</script>



<p class="grandTitre">Livraisons Self-Service (CDD)</p>

<html:form styleId="LivrerTicketAction" action="TraitementLivraisonAutoForm.do" method="post" enctype="multipart/form-data">
    <table class="tablePrincipale">
        <tr class="numLivraisonDiv groupe0">
            <td  class="tdDemi"><p class="titres">Numéro de livraison</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <%
                        if (isAdmin) {
                    %>
                    <input type = "text" id="numLivraison" name="numLivraison" style="width:80px;margin-left: 5px;margin-bottom: 0px"/>      
                    <%                        } else {
                    %>
                    <input type = "text" id="numLivraison" name="numLivraison" readonly='readonly' style="width:80px;margin-left: 5px;margin-bottom: 0px"/>      
                    <%                            }
                    %>


                    <script>
                        var contextPath = "<%=request.getContextPath()%>";</script>
                    <input type="button" class="boutonValiderPetit" id="getLivraisonDetails" onclick="invoquerServletGetTicketDetailsById(contextPath, 'CDD');" style="margin-left: 10px" value="Valider" />
                    <input type="button" class="boutonValiderPetit" id="resetLivraisonsList" onclick="window.location = './getAllMilestonesForm.do?acteur=CDD'" style="margin-left: 10px" value="Réinitialiser" />
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Circuit</p></td>
            <td  class="tdDemi">                
                <p class="contenu">
                    <span id="RELEASE" class="notSelected" ><input type= "radio" style="margin-top: -3px" name="circuit" value="RELEASE" onclick="selectCircuit('RELEASE');" id="CB_RELEASE"> RELEASE</span>
                    <span  style="margin-left: 3px" id="PROJET" class="notSelected" ><input type= "radio" style="margin-top: -3px" name="circuit" value="PROJET" onclick="selectCircuit('PROJET');" id="CB_PROJET"> PROJET</span>
                    <span  style="margin-left: 3px" id="HOTFIX" class="notSelected" ><input type= "radio" style="margin-top: -3px" name="circuit" value="HOTFIX" onclick="selectCircuit('HOTFIX');" id="CB_HOTFIX"> HOTFIX</span>
                    <span  style="margin-left: 3px" id="UPGRADE" class="notSelected" ><input type= "radio" style="margin-top: -3px" name="circuit" value="UPGRADE" onclick="selectCircuit('UPGRADE');" id="CB_UPGRADE">UPGRADE</span>
                </p>
            </td>
        </tr>

        <tr id="niveauProjetTr" class="groupe1">
            <td  class="tdDemi"><p class="titres">Niveau Projet</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="niveauProjet" id="niveauProjet" style="width:310px;margin-left: 5px;"/>
                    <input type = 'hidden' styleClass="readonly" name="contenuDesLivrables" id="contenuDesLivrables" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>
        <!--
                        <tr class="groupe1">
                    <td  class="tdDemi"><p class="titres">Contenu des livrables</p></td>
                    <td  class="tdDemi">
                        <p class="contenu">
                            <input type = 'hidden' styleClass="readonly" name="contenuDesLivrables" id="contenuDesLivrables" style="width:310px;margin-left: 5px;"/>                     
                        </p>
                    </td>
                </tr>
        -->
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Numéro d'anomalie</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <input type = 'text' styleClass="readonly" name="numAnomalie" id="numAnomalie" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>

        <tr id="phaseTr" class="groupe1">
            <td  class="tdDemi"><p class="titres">Type</p></td>
            <td  class="tdDemi">
                <p class="contenu">                        
                    <input type = 'text' styleClass="readonly" name="phase" id="phase" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>



        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Priorité</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="priority" id="priority" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Milestone</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="milestone" id="milestone" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Component</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="component" id="component" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nature traitement</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="natureTraitement" id="natureTraitement" style="width:310px;margin-left: 5px;"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Nature de livraison</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type = 'text' styleClass="readonly" name="natureLivraison" id="natureLivraison" style="width:310px;margin-left: 5px;"/>
                    <input type='hidden' id='packNameHidden' />
                    <input type='hidden' id='ticketManuel' />                    
                </p>
            </td>
        </tr>
        <tr id="loadingAnimationConteneur">
            <td colspan="2" >
                <%@include file="/pages/loadingAnimation.jsp" %>
            </td>
        </tr>
        <tr>
            <td colspan="2">
        <center  id="messageAlert" class="rouge">

            </div>
            </td>
            </tr>
    </table>

    <!-- ######################################################################## -->



    <div id="traitementLivraisons">
        <hr>
        <div class="leftBox">
            <select id='typeLivrable'>
                <option value='' disabled selected>Selectionnez un type</option>
                <option value='OBJETS T24'>OBJETS T24</option>
                <option value='EXECUTION ROUTINE T24'>EXECUTION ROUTINE T24</option>
                <!--<option value='DOLLAR U'>DOLLAR U</option>-->
                <option value='CREATION INDEXES'>CREATION INDEXES</option>
                <option value='REVERSE OBJETS T24'>REVERSE OBJETS T24</option>
                <option value='SUPRESSION OBJETS T24'>SUPRESSION OBJETS T24</option>
                <option value='REBUILD SYSTEM'>REBUILD SYSTEM</option>
                <option value='EXECUTION PROGRAMME JSH'>EXECUTION PROGRAMME JSH</option>                
                <option value='CREATION DOSSIERS'>CREATION DOSSIERS</option>
                <option value='BROWSER IB'>BROWSER IB</option>
                <option value='BROWSER T24'>BROWSER T24</option>
                <option value='STREAMSERV TRANSACTIONNEL'>STREAMSERV TRANSACTIONNEL</option>
                <option value='STREAMSERV BATCH'>STREAMSERV BATCH</option>                
                <option value='CREATION COMPTES'>CREATION COMPTES</option>
                <!--option value='TRANSFERT FICHIERS'>TRANSFERT FICHIERS</option-->
                <option value='AUTRE LIVRABLE'>AUTRE LIVRABLE</option>
            </select>      
        </div>
        <div class="rightBox">
            <div class="row grid span7" id="rowsContainer">
            </div>
        </div>
    </div>
    <p></p>
    <div style="clear: both"></div>
    <table class="tablePrincipale">
        <tr class="groupe1" style="display: table-row;">
            <td colspan="2">
                <p class="bouton">
                    <span id="ValidationSpan">
                        <button type="submit" class="btn btn-primary" id="btnValidation">Validation des livrables</button>
                    </span>
                    <span id="SubmitSpan">
                        <button type="button" class="btn btn-primary" id="btnReviser" onclick="reviserLivrables();">Réviser les livrables</button>
                        <button type="submit" class="btn btn-primary" id="btnSubmit" >Traiter la livraison</button>
                    </span>
                </p>
            </td>
        </tr>
    </table>

    <!--
   
    <input type="file" id="CREATIONCOMPTES" name="CREATIONCOMPTES"/>
    <input type="file" id="TRANSFERTFICHIERSfichier" name="TRANSFERTFICHIERSfichier"/>
    -->
    <!--</form>-->
</html:form>


<br/>
<div id="conteneurTablesTickets">
    <body onload="slideMenu.build('sm', 770, 10, 10, 1)"/>

    <ul id="sm" class="sm" style="text-align: center; margin: 0 auto" >
        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextRelease" >
                            <%
                                int cmpNotif = 0;
                                if (listRelease != null && listRelease.size() > 0) {
                                    cmpNotif += listRelease.size();
                                }
                                if (cmpNotif > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Release
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Reporter
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        int cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Release</td></tr>");
                                        if (listRelease == null || listRelease.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listRelease) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("reporter") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>


        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextProjet">
                            <%
                                cmpNotif = 0;
                                if (listProjet != null && listProjet.size() > 0) {
                                    cmpNotif += listProjet.size();
                                }
                                if (cmpNotif > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Projet
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Reporter
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Projet</td></tr>");
                                        if (listProjet == null || listProjet.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listProjet) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("reporter") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>


        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextHotfix">
                            <%
                                cmpNotif = 0;
                                if (listHotfix != null && listHotfix.size() > 0) {
                                    cmpNotif += listHotfix.size();
                                }
                                if (cmpNotif > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Hotfix
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Reporter
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Hotfix Test</td></tr>");
                                        if (listHotfix == null || listHotfix.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listHotfix) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("reporter") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>



        <li>
            <div class="eneloppe">
                <table class="tableEneloppe" cellpadding=0 cellspacing=0>
                    <tr>
                        <td class="titreBloc titreTextUpgrade">
                            <%
                                cmpNotif = 0;
                                if (listUpgrade != null && listUpgrade.size() > 0) {
                                    cmpNotif += listUpgrade.size();
                                }
                                if (cmpNotif > 0) {
                                    out.println("<div id='msg-icon' class='msg-iconh'><id class='counter' style=''>" + cmpNotif + "</id></div>");
                                }
                            %>
                        </td>
                        <td class="corpsBloc">
                            <div class="titreAnalysePack">
                                Circuit Upgrade
                            </div>
                            <table id="tableTicketsRelease" class="roundCornerTable tableTicketsReleaseLarge">
                                <thead>
                                    <tr>
                                        <th>
                                            Nbr
                                        </th>
                                        <th>
                                            Livraison
                                        </th>
                                        <th>
                                            Anomalie
                                        </th>
                                        <th>
                                            Projet
                                        </th>
                                        <th>
                                            Reporter
                                        </th>
                                        <th>
                                            Owner
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <%
                                        cmp = 1;
                                        out.println("<tr class='titre'><td colspan='8'>Tickets HOTFIX à harmoniser sur le circuit Upgrade</td></tr>");
                                        if (listHarmonistaionUpgrade == null || listHarmonistaionUpgrade.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listHarmonistaionUpgrade) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr class='clignotantBleu'><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("reporter") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                        out.println("<tr class='titre'><td colspan='8'>Tickets Upgrade</td></tr>");
                                        if (listUpgrade == null || listUpgrade.size() == 0) {
                                            out.println("<tr><td colspan='8' style='padding-left: 25px'>Aucun ticket à traiter</td></tr>");
                                        } else {
                                            for (Map<String, Object> mapDetails : listUpgrade) {
                                                Ticket ticketLivraison = (Ticket) mapDetails.get("Ticket");
                                                Integer numTicketLivraison = ticketLivraison.getId();
                                                out.println("<tr" + Tools.traiterClignotantLivraisonEnCours(numTicketLivraison.toString()) + "><td>" + cmp + ")</td><td ><a class='lien' onclick='preparerLivraison($(this).html(),0);'>#" + numTicketLivraison + "</a></td>   <td><a class='lien' onclick='preparerLivraison($(this).parent().prev(\"td\").children(\"a\").html(),$(this).html());'>#" + mapDetails.get("ticket_origine") + "</a></td>   <td title='" + (String) mapDetails.get("projet") + "'>" + Configuration.getAbreviationProjetParNiveauProjet((String) mapDetails.get("projet")) + "</td><td>" + mapDetails.get("reporter") + "</td><td>" + mapDetails.get("owner") + "</td></tr>");
                                                cmp++;
                                            }
                                        }
                                    %>
                                </tbody>
                            </table>                        


                        </td>
                    </tr>
                </table>             
            </div>
        </li>
    </ul>
</div>


<div class="erreur1 erreur">

</div>

<script>
                        $(document).ready(function() {
                            selectMenu('menuT24');
                        });
                        // Formatage WIKI
                        function wikiButtonClick(boutonWiki) {
                            var textArea = $(boutonWiki).parent().next().find('textarea');
                            var idTextArea = $(textArea).attr("id");
                            var cursorStartPosition = document.getElementById(idTextArea).selectionStart;
                            var cursorEndPosition = document.getElementById(idTextArea).selectionEnd;
                            var textAreaTxt = jQuery(textArea).val();
                            var textStart = "";
                            var textEnd = "";
                            var cursorposition = 0;
                            if ($(boutonWiki).attr("id") === "strong") {
                                textStart = "<b>";
                                textEnd = "</b>";
                            } else if ($(boutonWiki).attr("id") === "em") {
                                textStart = "<i>";
                                textEnd = "</i>";
                            } else if ($(boutonWiki).attr("id") === "heading") {
                                textStart = "<h3> ";
                                textEnd = " </h3>";
                            } else if ($(boutonWiki).attr("id") === "link") {
                                textStart = "[";
                                textEnd = "]";
                            } else if ($(boutonWiki).attr("id") === "hr") {
                                textStart = "<hr>";
                                textEnd = "";
                            } else if ($(boutonWiki).attr("id") === "np") {
                                textStart = "<p>";
                                textEnd = "</p>";
                            } else if ($(boutonWiki).attr("id") === "br") {
                                textStart = "<br>";
                                textEnd = "";
                            }
                            cursorposition = cursorStartPosition + textStart.length;
                            $(textArea).val(textAreaTxt.substring(0, cursorStartPosition) + textStart + textAreaTxt.substring(cursorStartPosition, cursorEndPosition) + textEnd + textAreaTxt.substring(cursorEndPosition));
                            $(textArea).selectRange(cursorposition, cursorposition);
                        }

                        $.fn.selectRange = function(start, end) {
                            if (!end)
                                end = start;
                            return this.each(function() {
                                if (this.setSelectionRange) {
                                    this.focus();
                                    this.setSelectionRange(start, end);
                                } else if (this.createTextRange) {
                                    var range = this.createTextRange();
                                    range.collapse(true);
                                    range.moveEnd('character', end);
                                    range.moveStart('character', start);
                                    range.select();
                                }
                            });
                        };
                        // FIN Formatage WIKI
</script>