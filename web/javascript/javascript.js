var contextPath;
// gestion menu
var loadingAnimation1 = 0;

function setContextPath(valeur) {
    contextPath = valeur;
}

function redimentionnerLaPopup(baliseA) {
    var popID = $(baliseA).attr('rel'); //Get Popup Name
    var popURL = $(baliseA).attr('href'); //Get Popup href to define size

    //Pull Query & Variables from href URL

    var popWidth = 550; //Gets the first query string value
    //Fade in the Popup and add close button
    $('#' + popID).fadeIn().css({
        'width': Number(popWidth)
    }).prepend('<a href="#" class="close"><img src="images/icon-close.png" class="btn_close" title="Close Window" alt="Close" /></a>');
    //Define margin for center alignment (vertical + horizontal) - we add 80 to the height/width to accomodate for the padding + border width defined in the css
    var popMargTop = ($('#' + popID).height() + 80) / 2;
    var popMargLeft = ($('#' + popID).width() + 80) / 2;
    //Apply Margin to Popup
    $('#' + popID).css({
        'margin-top': -popMargTop,
        'margin-left': -popMargLeft
    });
    //Fade in Background
    $('body').append('<div id="fade"></div>'); //Add the fade layer to bottom of the body tag.
    $('#fade').css({
        'filter': 'alpha(opacity=80)'
    }).fadeIn(); //Fade in the fade layer 

    return false;
}

function preparerParametres(classCheckBox) {
    var parametres = "checked=";
    $("." + classCheckBox).each(function() {
        if (this.checked) {
            parametres += $(this).attr("id") + "@";
        }
    });
    parametres = parametres.substring(0, parametres.length - 1);
    return parametres;
}

function afficherMessageErreur(message) {
    $(".alertErreur").html("<br>" + message);
    $(".alertErreur").fadeIn(1000).delay(800).fadeOut(1500);
    setTimeout(function() {
        $('#fade , .popup_block').fadeOut(function() {
            $('#fade, a.close').remove();
        });
    }, 2000);
}

function afficherMessageSucces(message) {
    $(".logInsertion").html("<br>" + message);
    $(".logInsertion").fadeIn(1000).delay(800).fadeOut(1500);
    setTimeout(function() {
        $('#fade , .popup_block').fadeOut(function() {
            $('#fade, a.close').remove();
        });
    }, 2000);
}

function validerUserHasEnvironnement() {
    $('.roundCornerTableWithButtons input').each(function() {
        $(this).removeClass("inputError");
    });
    $('.roundCornerTableWithButtons .add input').each(function() {
        if ($(this).val().length === 0) {
            $(this).addClass("inputError");
        }
    });
    if ($('.roundCornerTableWithButtons .add select').val() === '') {
        $('.roundCornerTableWithButtons .add select').addClass('inputError');
    }
}

function miseAJourPackNameWidhoutArgs(inputSufixe) {
    var inputPackName = $(inputSufixe).parent().find('.packName');
    $(inputPackName).attr('readonly', false);
    if ($(inputSufixe).val().length === 0) {
        $(inputPackName).val($("#packNameHidden").val());
    } else {
        suffixe = $(inputSufixe).val();
        if (suffixe.length > 6) {
            $(inputSufixe).val(suffixe.substr(0, 6));
        }

        $(inputPackName).val($("#packNameHidden").val() + "." + $(inputSufixe).val());
    }
    $(inputPackName).attr('readonly', true);
}

function miseAJourPackName(packNameObject, regex) {
    var nomPack = $(packNameObject).val();
    var firstIndesTaf = nomPack.indexOf(regex);
    if (firstIndesTaf !== 0) {
        nomPack = regex + nomPack;
    }
    if (nomPack.length > 25) {
        nomPack = nomPack.substr(0, 25);
    }
    $(packNameObject).val(nomPack);
    var nomLastPack = $(packNameObject).val();
    nomLastPack = nomLastPack.replace("TAF-", "");
    nomLastPack = $.trim(nomLastPack);
    if (nomLastPack.length > 0) {
        $(packNameObject).removeClass("inputError");
    }
}

////fin  gestion menu

/* ce bloc est ajout� par anis pour �viter la descente brusque des sous-menus*/

/*
 $(document).ready( function(){
 
 // open and close list when button is clicked
 $(".has-sub").hover(
 function() {            
 $(this).children('ul').slideDown(300);
 }, function() {
 $(this).children('ul').slideUp(300);   
 }
 );
 });
 */
$(document).ready(function() {

    $("#popup_objectList .boutonValider").click(
            function() {
                $(".btn_close").click();
            }
    );

    // open and close list when button is clicked
    $(".has-sub").click(
            function() {
                if ($(this).is('.on')) {
                    $(this).children('ul').slideUp(300);
                    $(this).addClass("off");
                    $(this).removeClass("on");
                } else if ($(this).is('.off')) {
                    $(".has-sub").each(function() {
                        $(this).children('ul').slideUp(300);
                        $(this).addClass("off");
                        $(this).removeClass("on");
                    });
                    $(this).children('ul').slideDown(300);
                    $(this).addClass("on");
                    $(this).removeClass("off");
                }
            }
    );

    $(".titreMenus span").click(function() {
        var idzoneMenu = $("." + $(this).attr("class") + "_menu").attr("id");
        if ($("#" + idzoneMenu).hasClass("open")) {
            $("#" + idzoneMenu).slideUp(600);
            $("#" + idzoneMenu).removeClass("open");
        } else {
            $('.menuvertical').each(function() {
                if ($(this).attr("id") !== idzoneMenu) {
                    $(this).slideUp(600);
                    $(this).removeClass("open");
                } else {
                    $(this).slideDown(1000);
                    $(this).addClass("open");
                }
            });
        }
    });
});



function selectMenu(menuId) {
    //alert("menuId:"+menuId);


    $(".menuItem").each(function() {
        $(this).removeClass("pressed");
    });
    $("#" + menuId).addClass("pressed");
    $("#" + menuId).closest(".menuvertical").prev("div").children("span").click();

    //alert("#" + menuId+":     "+$("#" + menuId).attr("class"));   
    //alert("menuId:"+menuId+":");
    //alert($("#" + menuId).closest( ".menuvertical" ).html());            
    //alert($("#" + menuId).closest( ".menuvertical" ).prev("div").children("span").html());    
}

function selectCircuit(id) {
    document.getElementById('RELEASE').className = "notSelected";
    document.getElementById('PROJET').className = "notSelected";
    document.getElementById('HOTFIX').className = "notSelected";
    document.getElementById('UPGRADE').className = "notSelected";
    document.getElementById(id).className = "selected";
    document.getElementById("CB_" + id).checked = true;
    $('input[name="circuit"]').attr('disabled', true);
    $("#CB_" + id).attr('disabled', false);
    afficherChampsSelonCircuit(id);
}

function afficherChampsSelonCircuit(id) {
    if (id === 'HOTFIX') {
        $("#niveauProjet").val("MAINTENANCE");
        $("#niveauProjetTr").hide();
    } else if (id === 'PROJET' || id === 'RELEASE') {
        $("#niveauProjetTr").show();
    }
}


function selectTri(id) {
    document.getElementById('OUI').className = "notSelected";
    document.getElementById('NON').className = "notSelected";
    document.getElementById(id).className = "selected";
    document.getElementById("CB_" + id).checked = true;
}


function selectCompany(id) {
    document.getElementById('TN1').className = "notSelected";
    document.getElementById('BNK').className = "notSelected";
    document.getElementById('Autre').className = "notSelected";
    document.getElementById(id).className = "selected";
    document.getElementById("CB_" + id).checked = true;
    if (id === 'TN1' || id === 'BNK') {
        $("#autreMnemonic").val("");
    }
}

function selectCompanyMultiple(input, now) {
    $(input).prop('checked', true);
    if ($(input).attr('value').startsWith('TN1') || $(input).attr('value').startsWith('BNK')) {
        $(".autreMnemonic" + now).val("");
        $(".autreMnemonic" + now).css("background-color", "#ffffff");
        $(input).parent().parent().find('.selectedMnemonic').val($(input).attr('value'));
    }
}

function selectObjets_Pack(id) {

    document.getElementById('OBJETS').className = "notSelected";
    document.getElementById('PACK').className = "notSelected";
    document.getElementById(id).className = "selected";
    document.getElementById("CB_" + id).checked = true;
    if (id === "OBJETS") {
        $("#environnementSourceName").val("");
        $("#nomPack").val("");
        $("#numHotfix").val("");
        $("#trEnvSource").hide();
        $("#trNomPack").hide();
        $("#trBoutonPack").hide();
        $("#trNumHotfix").hide();
        $("#trBoutonListeObjets").show();
    } else {
        $("#trEnvSource").show();
        $("#trNomPack").show();
        $("#trBoutonPack").show();
        $("#trNumHotfix").show();
        $("#trBoutonListeObjets").hide();
    }
}

$(document).ready(function() {
    $("#trDossierSource").hide();
    $("#trDossierSourcePath").hide();
    $("#trDepotVersionning").hide();
    $("#trDepotVersionning2").hide();
    //When you click on a link with class of poplight and the href starts with a # 
    $('a.poplight[href^=#]').click(function() {
        var popID = $(this).attr('rel'); //Get Popup Name
        var popURL = $(this).attr('href'); //Get Popup href to define size

        //Pull Query & Variables from href URL
        var query = popURL.split('?');
        var dim = query[1].split('&');
        var popWidth = dim[0].split('=')[1]; //Gets the first query string value

        //Fade in the Popup and add close button
        $('#' + popID).fadeIn().css({
            'width': Number(popWidth)
        }).prepend('<a href="#" class="close"><img src="images/icon-close.png" class="btn_close" title="Close Window" alt="Close" /></a>');
        //Define margin for center alignment (vertical + horizontal) - we add 80 to the height/width to accomodate for the padding + border width defined in the css
        var popMargTop = ($('#' + popID).height() + 80) / 2;
        var popMargLeft = ($('#' + popID).width() + 80) / 2;
        //Apply Margin to Popup
        $('#' + popID).css({
            'margin-top': -popMargTop,
            'margin-left': -popMargLeft
        });
        //Fade in Background
        $('body').append('<div id="fade"></div>'); //Add the fade layer to bottom of the body tag.
        $('#fade').css({
            'filter': 'alpha(opacity=80)'
        }).fadeIn(); //Fade in the fade layer 

        return false;
    });

    /*
     $( document ).on( "click", "a.close, #fade", function() {
     $('#fade , .popup_block').fadeOut(function() {
     $('#fade, a.close').remove();
     }); //fade them both out
     
     return false;
     });
     */

    //Close Popups and Fade Layer

    $('a.close, #fadeX').live('click', function() { //When clicking on the close or fade layer...
        $('#fade , .popup_block').fadeOut(function() {
            $('#fade, a.close').remove();
        }); //fade them both out

        return false;
    });

    $('#environnementSource').change(function() {
        if ($(this).val() === 'VERSIONNING') {
            $("#racineSrc").html("Nom du Pack");
            $("#racineSrcPath").html("Chemin absolu du Pack : /");
            $("#dossierSource").val("TAF-");
            $("#dossierSourcePath").val("");
            $("#trDossierSource").show();
            $("#trDepotVersionning").show();
            $("#trDossierSourcePath").show();
        } else if ($(this).val() === '') {
            $("#racineSrc").html("");
            $("#racineSrcPath").html("");
            $("#trDossierSource").hide();
            $("#trDossierSourcePath").hide();
        } else {
            $("#racineSrc").html("Nom du Pack");
            $("#racineSrcPath").html("Chemin absolu du Pack : /");
            $("#dossierSource").val("TAF-");
            $("#dossierSourcePath").val("");
            $("#trDossierSource").show();
            $("#trDossierSourcePath").hide();
        }
        //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
        if ($(this).val() !== '') {
            if ($(this).val() === $('#environnementDestinationName').val()) {
                $(this).val("");
                $("#racineSrc").html("");
                $("#racineSrcPath").html("");
                $("#trDossierSource").hide();
                $("#trDossierSourcePath").hide();
            }
        }
    });

    $('#environnementDestinationName').change(function() {
        //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
        if ($(this).val() !== '') {
            if ($(this).val() === $('#environnementSourceName').val()) {
                $(this).val("");
            }
        }
    });

    $('.boutonValiderSubmit').click(function() {
        $(this).attr("disabled", true);
    });
});
///////////////////////////// Invoquer les servlets ///////////////////

function getXMLObject()  //XML OBJECT
{
    var xmlHttp = false;
    try {
        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
    }
    catch (e) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
        }
        catch (e2) {
            xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
        }
    }
    if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
        xmlHttp = new XMLHttpRequest(); //For Mozilla, Opera Browsers
    }
    return xmlHttp; // Mandatory Statement returning the ajax object created
}

var xmlhttp = new getXMLObject(); //xmlhttp holds the ajax object

function invoquerServletGetAllCompaniesMnemonics(mnemonic, contextPath) {
    autreMnemonic = $(mnemonic).val();
    mnemonicClass = $(mnemonic).attr('class');
    $("#loadingAnimationConteneur").show();
    $(".groupe0").hide();
    //$(".groupe1").hide();
    $(".erreur1").text("");
    if (xmlhttp) {
        xmlhttp.open("GET", contextPath + "/GetAllCompaniesMnemonics?mnemonic=" + autreMnemonic, true); //gettime will be the servlet name
        xmlhttp.onreadystatechange = function() {
            traitementApresAppelServletGetAllCompaniesMnemonics(contextPath, mnemonicClass);
        };
        //xmlhttp.onreadystatechange = traitementApresAppelServletGetAllCompaniesMnemonics;
        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xmlhttp.send(null);
    }
}

function traitementApresAppelServletGetAllCompaniesMnemonics(contextPath, mnemonicClass) {

    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var valRetour = (xmlhttp.responseText).trim();
            if (valRetour === "ECHEC AUTHENTIFICATION") {
                document.location.href = contextPath + "/AfficherMessageErreurs.do";
                $("." + mnemonicClass).parent().parent().find('#selectedMnemonic').val("");
            } else if (valRetour === "MNEMONIC OK") {
                $(".groupe0").show();
                $("." + mnemonicClass).css("background-color", "#EAF7D9");
                $("." + mnemonicClass).parent().parent().find('#selectedMnemonic').val($("." + mnemonicClass).val());
            } else {
                $(".erreur1").text(valRetour);
                $("." + mnemonicClass).css("background-color", "#E55451");
                $("." + mnemonicClass).parent().parent().find('#selectedMnemonic').val("");
            }
        }
        else {
            $("#loadingAnimationConteneur").hide();
            alert("Error during AJAX call. Please try again");
        }
        $("#loadingAnimationConteneur").hide();
    }
}

function extractUrlParams() {
    //extraire le context de l'application pour faire appel aux servlets
    var t = location.search.substring(1).split('&');
    var f = [];
    for (var i = 0; i < t.length; i++) {
        var x = t[ i ].split('=');
        f[x[0]] = x[1];
    }
    return f;
}

function invoquerServletGetTicketDetailsById(contextPath, acteur) {
    ticketId = $("#numLivraison").val();
    $(".groupe1").hide();
    $("#loadingAnimationConteneur").show();
    if (is_int(ticketId) === true) {
        $(".erreur").text("");
        if (xmlhttp) {
            xmlhttp.open("GET", contextPath + "/GetTicketDetailsById?ticketId=" + ticketId + "&acteur=" + acteur, true); //gettime will be the servlet name
            xmlhttp.onreadystatechange = traitementApresAppelServletGetTicketDetailsById;
            xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xmlhttp.send(null);
        }
    } else {
        $("#loadingAnimationConteneur").hide();
        $(".erreur").append("Le numéro de livraison doit être un entier");
    }
}

function is_int(value) {
    if ((parseFloat(value) == parseInt(value)) && !isNaN(value)) {
        return true;
    } else {
        return false;
    }
}

function traitementApresAppelServletGetTicketDetailsById() {

    if (xmlhttp.readyState == 4) {
        if (xmlhttp.status == 200) {
            var champsValeurs = (xmlhttp.responseText).split("$*$");
            var champs = champsValeurs[0].split("%_%");
            var erreurs = champsValeurs[1];



            if (erreurs.trim().length > 0) {
                $(".groupe1").hide();
                $(".erreur").append(erreurs);
            } else {
                $("#traitementLivraisons").show();
                $(".groupe1").show();
                $("#niveauProjet").val(champs[0]);
                $("#numAnomalie").val(champs[1]);
                $("#phase").val(champs[2]);
                $("#priority").val(champs[3]);
                $("#milestone").val(champs[4]);
                $("#component").val(champs[5]);
                $("#natureTraitement").val(champs[6]);
                $("#natureLivraison").val(champs[7]);
                $("#packName").val(champs[8]);
                $("#packNameHidden").val(champs[8]);
                if (champs[9] !== "OBJETS T24" && champs[9] !== "null") {
                    $("#contenuDesLivrables").addClass("clignotant");
                    $("#contenuDesLivrables").addClass("vert");
                    $("#messageAlert").html("Attention, à ne pas oublier l'action manuelle: " + champs[9]);
                }
                $("#contenuDesLivrables").val(champs[9]);
                $(".readonly").attr('readonly', true);
                if ($("#phase").val() === "QUALIFICATION" || $("#phase").val() === "CERTIFICATION") {
                    selectCircuit("RELEASE");
                } else if ($("#phase").val() === "HOT FIXE TEST" || $("#phase").val() === "ACTION A CHAUD TEST" || $("#phase").val() === "HOT FIXE PROD") {
                    selectCircuit("HOTFIX");
                } else if ($("#phase").val() === "QUALIFICATION_PROJET") {
                    selectCircuit("PROJET");
                } else if ($("#phase").val() === "QUALIFICATION_UPGRADE" || $("#phase").val() === "CERTIFICATION_UPGRADE") {
                    selectCircuit("UPGRADE");
                }


                //si c'est un ticket d'harmonisation, ne pas afficher le bouton de la liste des objets                            
                if ($("#phase").val() === "HOT FIXE PROD") {
                    $("#trBoutonListeObjets").remove();
                }

            }
            $("#loadingAnimationConteneur").hide();
        }
        else {
            $("#loadingAnimationConteneur").hide();
            alert("Error during AJAX call. Please try again");
        }
    }
}

var compteurInvoquerServletWorkflowDataManage;

function invoquerServletWorkflowDataManage(parametres) {
    compteurInvoquerServletWorkflowDataManage = 0;
    if (xmlhttp) {
        xmlhttp.open("GET", contextPath + "/ServletWorkflowDataManage?" + parametres, true); //gettime will be the servlet name
        xmlhttp.onreadystatechange = traitementApresInvoquerServletWorkflowDataManage;
        xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
        xmlhttp.send(null);
    }
}

function traitementApresInvoquerServletWorkflowDataManage() {
    if (compteurInvoquerServletWorkflowDataManage === 0) {
        compteurInvoquerServletWorkflowDataManage++;
        //Désactiver les 4 boutons de réinitialisation des threads
        $('#THREAD_HF').removeAttr('checked');
        $('#THREAD_HARM_UPGRADE').removeAttr('checked');
        $('#THREAD_IE').removeAttr('checked');
        $('#THREAD_OV').removeAttr('checked');
        $(".logInsertion").fadeIn(1000).delay(800).fadeOut(1500);
    }
}

function openTracTicketInNewTab(idTicket, nature) {
    idTicket = idTicket.replace(/#/g, "");
    var a = document.createElement("a");
    if (nature === "LIVRAISON") {
        a.href = "http://172.28.70.74/trac/livraisons_t24/ticket/" + idTicket;
    } else if (nature === "ANOMALIE") {
        a.href = "http://172.28.70.74/trac/anomalies_t24/ticket/" + idTicket;
    } else if (nature === "HRACCESS") {
        a.href = "http://172.28.70.74/trac/HR_ACCESS/ticket/" + idTicket;
    } else if (nature === "GESTION_DEMANDES") {
        a.href = "http://172.28.70.74/trac/GESTION_DEMANDES_OVTOOLS/ticket/" + idTicket;
    } else if (nature === "SEE_ATTACHEMENT") {
        a.href = "http://172.28.70.74/trac/GESTION_DEMANDES_OVTOOLS/attachment/ticket/" + idTicket;
    } else if (nature === "NEW_ATTACHEMENT") {
        a.href = "http://172.28.70.74/trac/GESTION_DEMANDES_OVTOOLS/attachment/ticket/" + idTicket + "/?action=new&attachfilebutton=Joindre+un+fichier/";
    } 
    
    var evt = document.createEvent("MouseEvents");
    //the tenth parameter of initMouseEvent sets ctrl key
    evt.initMouseEvent("click", true, true, window, 0, 0, 0, 0, 0,
            true, false, false, false, 0, null);
    a.dispatchEvent(evt);
}

function invoquerServletGetTicketHraccessDetailsById(contextPath) {
    ticketId = $("#numLivraison").val();
    natureTraitement = $("#natureTraitement").val();
    $(".groupe1").hide();
    $("#loadingAnimationConteneur").show();
    if (is_int(ticketId) == true) {
        $(".erreur").text("");
        $.ajax({
            type: "POST",
            url: contextPath + "/GetTicketHraccessDetailsByIdServlet",
            data: "ticketId=" + ticketId + "&natureTraitement=" + natureTraitement,
            success: function(response) {
                traitementApresAppelServletGetTicketDetailsById;
            },
            error: function(e) {
                //alert('Error: ' + e);
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 3000);
            }
        });
    } else {
        $("#loadingAnimationConteneur").hide();
        $(".erreur").append("Le numéro de livraison doit être un entier");
    }
}