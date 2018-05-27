$(document).ready(function() {
    $('input,select').change(function() {
        //alert($(this).val()); // get the current value of the input field.
        if ($(this).val().length > 0) {
            $(this).removeClass("inputError");
        }
    });
    $('input').keypress(function() {
        //alert($(this).val()); // get the current value of the input field.
        if ($(this).val().length > 0) {
            $(this).removeClass("inputError");
        }
    });
    $('input[type=number]').keyup(function() {
        //alert($(this).val()); // get the current value of the input field.
        if ($(this).val().trim() !== 1 || $(this).val().trim() !== 2 || $(this).val().trim() !== 3 || $(this).val().trim() !== 4 || $(this).val().trim() !== 5) {
            $(this).val(1);
        }
    });
});

function validerDeploiementParalleleMultipacks(contextPath) {
    var canAdd = verifEmptyPacksNames();
    var erreurs = "";
    $('.erreur').each(function() {
        erreurs += $(this).text();
    });
    erreurs = $.trim(erreurs);

    var envSource = $.trim($("#environnementSourceName").val());
    if (envSource === "") {
        erreurs += "Veuillez selectionner l'environnement source\n";
        $("#environnementSourceName").addClass("inputError");
    } else {
        $("#environnementSourceName").removeClass("inputError");
    }
    if (envSource === "VERSIONNING") {
        var dossierSourcePath = $("#dossierSourcePath").val();
        if (dossierSourcePath === "") {
            erreurs += "Veuillez selectionner le chemin du pack\n";
            $("#dossierSourcePath").addClass("inputError");
        } else {
            $("#dossierSourcePath").removeClass("inputError");
        }
    }

    var countEnvironnements = $("#environnementsCibles").children("li").length;
    if (countEnvironnements === 0) {
        erreurs += "Veuillez selectionner au moins un environnement\n";
        $("#environnementsCibles").css("background-color", "#FFCCCC");
        $("#environnementsCibles").css("border-color", "red");
    }

    if (erreurs.trim().length === 0 && canAdd === true) {
        submitDataDeployerNPacksSurNEnvironnements(contextPath);
    }
}

function validerChampsGestionLivraisons(contextPath, verifierListeObjets) {
    var alertMessage = "";
    var circuitValue = $('input[type=radio][name=circuit]:checked').attr('value'); //null si vide
    if (circuitValue === null) {
        alertMessage += "Veuillez selectionner le circuit\n";
    }

    var sendTicketToIE = $('input[type=checkbox][name=sendTicketToIE]').attr('checked'); //true or false 
    var writeTextOnTicket = $('input[type=checkbox][name=writeTextOnTicket]').attr('checked'); //true or false 

    var mnemonicValue = $('input[type=radio][name=mnemonic]:checked').attr('value'); //null si vide
    var autreMnemonic = $("#autreMnemonic").val();

    if (mnemonicValue === "Autre") {
        if (autreMnemonic === "") {
            alertMessage += "Veuillez saisir le Mnemonic Company\n";
        }
    }

    var milestone = $("#milestone").val();
    if (milestone === "") {
        alertMessage += "Veuillez selectionner le milestone\n";
    }
    //alert(milestone); // "" si vide
    var numLivraison = $("#numLivraison").val();
    if (numLivraison === "") {
        alertMessage += "Veuillez selectionner le numéro de la livraison\n";
    }
    var niveauProjet = $("#niveauProjet").val();
    if (niveauProjet === "") {
        alertMessage += "Veuillez selectionner le niveau projet\n";
    }
    if (verifierListeObjets === false) {
        var textAreaObjectList = $("#textAreaObjectList").val();
        if (textAreaObjectList === "") {
            alertMessage += "Veuillez saisir la liste des objets\n";
        }
    }
    var numAnomalie = $("#numAnomalie").val();
    if (numAnomalie === "") {
        alertMessage += "Veuillez saisir le numéro d'anomalie\n";
    }
    var phase = $("#phase").val();
    if (phase === "" && circuitValue === "RELEASE") {
        alertMessage += "Veuillez selectionner la phase\n";
    }
    if (alertMessage.length > 0) {
        $('#fade , .popup_block').fadeOut(function() {
            $('#fade, a.close').remove();
        }); //fade them both out 
        alert(alertMessage);
    } else {
        $('#fade , .popup_block').fadeOut(function() {
            $('#fade, a.close').remove();
        });
        submitDataGestionDesLivraisonsInput(contextPath);
    }

}

function validerChampsComparaisonDelta(contextPath) {

    var alertMessage = "";

    var envSource = $("#environnementSourceName").val();
    if (envSource === "") {
        alertMessage += "Veuillez selectionner l'environnement source\n";
    }

    var dossierSource = $("#dossierSource").val();
    if (envSource !== "" && dossierSource === "") {
        alertMessage += "Veuillez selectionner le dossier source\n";
    }

    var envDestination = $("#environnementDestinationName").val();
    if (envDestination === "") {
        alertMessage += "Veuillez selectionner l'environnement destination\n";
    }


    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitComparaisonPacks(contextPath);
    }
}

function validerChampsFormerUnPack(contextPath) {

    var alertMessage = "";

    var envSource = $("#environnementSource").val();
    if (envSource === "") {
        alertMessage += "Veuillez selectionner l'environnement source\n";
    }

    if (envSource === "VERSIONNING") {
        var depot = $("#selectedDepot").val();
        if (depot === "") {
            alertMessage += "Veuillez selectionner le dépôt\n";
        }
    }

    var nomPack = $("#nomPack").val();
    if (nomPack === "") {
        alertMessage += "Veuillez selectionner le nom du pack\n";
    }

    var textAreaObjectList = $("#textAreaObjectList").val();
    if (textAreaObjectList === "") {
        alertMessage += "Veuillez saisir la liste des objets\n";
    }

    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitFormerUnPack(contextPath);
    }
}


function validerChampsComparaisonObjets(contextPath) {

    var alertMessage = "";

    var envSource1 = $("#environnementSourceName1").val();
    if (envSource1 === "") {
        alertMessage += "Veuillez selectionner l'environnement source 1\n";
    }

    var envSource2 = $("#environnementSourceName2").val();
    if (envSource2 === "") {
        alertMessage += "Veuillez selectionner l'environnement source 2\n";
    }

    var textAreaObjectList = $("#textAreaObjectList").val();
    if (textAreaObjectList === "") {
        alertMessage += "Veuillez saisir la liste des objets\n";
    }

    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitComparaisonObjets(contextPath);
    }
}

function validerChampsEtudeIntersectionPack(contextPath) {

    var alertMessage = "";

    var envSource = $("#environnementSourceName").val();
    if (envSource === "") {
        alertMessage += "Veuillez selectionner l'environnement source\n";
    }

    var packName = $("#nomPack").val();
    if (envSource !== "" && packName === "") {
        alertMessage += "Veuillez selectionner le nom du pack\n";
    }

    var numHotfix = $("#numHotfix").val();
    if (numHotfix === "") {
        alertMessage += "Veuillez selectionner le numéro de HOTFIX\n";
    }

    var userName = $("#userName").val();
    if (userName === "") {
        alertMessage += "Veuillez selectionner votre nom\n";
    }

    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitDataEtudeIntersection(contextPath);
    }
}

function validerChampsEtudeIntersectionObjets(contextPath) {

    var alertMessage = "";

    var textAreaObjectList = $("#textAreaObjectList").val();
    if (textAreaObjectList === "") {
        alertMessage += "Veuillez saisir la liste des objets\n";
    }

    var userName = $("#userName").val();
    if (userName === "") {
        alertMessage += "Veuillez selectionner votre nom\n";
    }

    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitDataEtudeIntersection(contextPath);
    }
}

function validerChampsCadencementDelta(contextPath) {
    $("#loadingAnimationConteneur").show();
    var alertMessage = "";
    var nomPack = $("#nomPack").val();
    if (nomPack === "") {
        $("#loadingAnimationConteneur").hide();
        alertMessage += "Veuillez selectionner le nom du pack contenant l'export sous /work \n";
    }
    if (alertMessage.length > 0) {
        alert(alertMessage);
    } else {
        submitCadencementDelta(contextPath);
    }
}

function validerChampsGestionLivraisonsHraccess(contextPath) {
    var numLivraison = $('#numLivraison').val();
    var natureTraitement = $('#natureTraitement').val();
    var packName = $('#packName').val();
    var textAreaObjectList = $("#textAreaObjectList").val();

    if (numLivraison === "") {
        alertMessage += "Veuillez selectionner le numéro de Livraison\n";
    }
    if (natureTraitement === "") {
        alertMessage += "Veuillez selectionner la nature de Traitement\n";
    }
    if (packName === "") {
        alertMessage += "Veuillez selectionner le nom du pack\n";
    }
    if (textAreaObjectList === "") {
        alertMessage += "Veuillez saisir la liste des objets\n";
    }
    submitDataGestionDesLivraisonsHraccessInput(contextPath);
}
