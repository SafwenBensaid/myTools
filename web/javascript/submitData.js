
function updateTicket(numTicketLivraison, messageTrac, circuit, contenuDesLivrables, nePasEcraserLivrable, contextPath) {
    var dataContent = "numTicketLivraison=" + numTicketLivraison + "&messageTrac=" + messageTrac + "&circuit=" + circuit + "&contenuDesLivrables=" + contenuDesLivrables + "&nePasEcraserLivrable=" + nePasEcraserLivrable;
    $("#conteneurAnimation").show();
    $(".boutonValider").hide();

    $.ajax({
        type: "POST",
        url: contextPath + "/UpdateTicketLivraison",
        data: dataContent,
        success: function(response) {
            $("#conteneurAnimation").hide();
            if ((response.indexOf("KO") !== -1)) {
                $("#messageResultatPersist").addClass("clignotant");
                $("#messageResultatPersist").addClass("rouge");
                $("#messageResultatPersist").html("La mise a jours du ticket TRAC a échoué, veuillez réessayer");
                $(".boutonValider").show();
            } else {
                $("#messageResultatPersist").addClass("clignotant");
                $("#messageResultatPersist").addClass("vert");
                $("#messageResultatPersist").html("Le ticket TRAC a été mis a jours avec succès");

                setTimeout(function() {
                    window.location = contextPath + "/getAllMilestonesForm.do?acteur=OV";
                }, 1000);
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}


function disableButtons() {
    $("input[type=button]").each(function() {
        $(this).attr("disabled", "true");
    });
    $('html, body').animate({
        scrollTop: $("#ovMail").offset().top
    }, 2000);
}

function enableButtons() {
    $("input[type=button]").each(function() {
        $(this).removeAttr('disabled');
    });
}

// ETUDE D'INTERSECTION
function submitDataEtudeIntersection(contextPath) {
    //alert(contextPath);
    disableButtons();
    var textAreaObjectList = $('#textAreaObjectList').val();
    var objetsSource = $('input[type=radio][name=objetsSource]:checked').attr('value');
    var environnementSourceName = $('#environnementSourceName').val();
    var nomPack = $('#nomPack').val();
    var numHotfix = $('#numHotfix').val();
    var userName = $('#userName').val();
    var dataContent1 = "textAreaObjectList=" + textAreaObjectList + "&objetsSource=" + objetsSource + "&environnementSourceName=" + environnementSourceName + "&nomPack=" + nomPack + "&numHotfix=" + numHotfix + "&userName=" + userName;

    $.ajax({
        type: "POST",
        url: contextPath + "/LancerEtudeIntersectionInputObjets.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResutatEtudeIntersection.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

//GESTION DES LIVRAISONS

function submitDataGestionDesLivraisonsInput(contextPath) {
    //alert(contextPath);
    disableButtons();
    var circuit = $('input[type=radio][name=circuit]:checked').attr('value');
    var milestone = $('#milestone').val();
    var numLivraison = $('#numLivraison').val();
    var niveauProjet = $('#niveauProjet').val();
    var numAnomalie = $('#numAnomalie').val();
    var suffixe = $('#suffixe').val();
    var textAreaObjectList = $('#textAreaObjectList').val();
    //var fileName = $('#fileName').val();
    var phase = $('#phase').val();
    var mnemonic = $('input[type=radio][name=mnemonic]:checked').attr('value');
    var autreMnemonic = $('#autreMnemonic').val();
    //var selectedCircuit = $('#selectedCircuit').val();
    var priority = $('#priority').val();
    var component = $('#component').val();
    var natureTraitement = $('#natureTraitement').val();
    var natureLivraison = $('#natureLivraison').val();
    var packName = $('#packName').val();
    var packNameHidden = $('#packNameHidden').val();
    var contenuDesLivrables = $('#contenuDesLivrables').val();
    var textAreaManuel = $('#textAreaManuel').val();
    var sendTicketToIE = $('input[type=checkbox][name=sendTicketToIE]').is(':checked'); //true or false 
    var writeTextOnTicket = $('input[type=checkbox][name=writeTextOnTicket]').is(':checked'); //true or false 
    var nePasEcraserLivrable = $('input[type=checkbox][name=nePasEcraserLivrable]').is(':checked'); //true or false 


    var dataContent1 = "circuit=" + circuit;
    dataContent1 += "&milestone=" + milestone;
    dataContent1 += '&numLivraison=' + numLivraison;
    dataContent1 += '&niveauProjet=' + niveauProjet;
    dataContent1 += '&numAnomalie=' + numAnomalie;
    dataContent1 += '&suffixe=' + suffixe;
    dataContent1 += '&textAreaObjectList=' + textAreaObjectList;
    //dataContent1 += '&fileName=' + fileName;
    dataContent1 += '&phase=' + phase;
    dataContent1 += '&mnemonic=' + mnemonic;
    dataContent1 += '&autreMnemonic=' + autreMnemonic;
    //dataContent1 += '&selectedCircuit=' + selectedCircuit;
    dataContent1 += '&priority=' + priority;
    dataContent1 += '&component=' + component;
    dataContent1 += '&natureTraitement=' + natureTraitement;
    dataContent1 += '&natureLivraison=' + natureLivraison;
    dataContent1 += '&packName=' + packName;
    dataContent1 += '&packNameHidden=' + packNameHidden;
    dataContent1 += '&contenuDesLivrables=' + contenuDesLivrables;
    dataContent1 += '&textAreaManuel=' + textAreaManuel;
    dataContent1 += '&sendTicketToIE=' + sendTicketToIE;
    dataContent1 += '&writeTextOnTicket=' + writeTextOnTicket;
    dataContent1 += '&nePasEcraserLivrable=' + nePasEcraserLivrable;

    $.ajax({
        type: "POST",
        url: contextPath + "/createObjectFileForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else if ((response.indexOf("PACK_DEPLOYED") !== -1)) {
                window.location = contextPath + "/resultatDeploiementPack.do";
            } else {
                if (textAreaManuel.trim().length === 0) {
                    window.location = contextPath + "/analysePack.do";
                } else {
                    // lorsqu'il s'agit juste de notifier dans le wiki
                    $("#messageResultat").show();
                    $("#messageResultat").addClass("clignotant");
                    $("#messageResultat").addClass("vert");
                    $("#messageResultat").html("Le ticket TRAC a été mis a jours avec succès");
                    setTimeout(function() {
                        window.location = contextPath + "/gestionDesLivraisonsOV.do";
                    }, 2500);

                }
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });

}

function submitDeploiementPack(contextPath) {
    disableButtons();
    $.ajax({
        type: "POST",
        url: contextPath + "/deployerPackForm.do",
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/resultatDeploiementPack.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function submitVersionnerPack(contextPath) {
    disableButtons();
    $.ajax({
        type: "POST",
        url: contextPath + "/versionnerPackForm.do",
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/genererMessageTracForm.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function submitGenererMessageTrac(contextPath) {
    disableButtons();
    $.ajax({
        type: "POST",
        url: contextPath + "/genererMessageTracForm.do",
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/resultatMessageTrac.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

// FORMER UN PACK

function submitFormerUnPack(contextPath) {
    disableButtons();
    var environnementSource = $('#environnementSource').val();
    var selectedDepot = $('#selectedDepot').val();
    var nomPack = $('#nomPack').val();
    var textAreaObjectList = $('#textAreaObjectList').val();
    var dataContent1 = "environnementSource=" + environnementSource + "&nomPack=" + nomPack + "&textAreaObjectList=" + textAreaObjectList + "&selectedDepot=" + selectedDepot;

    $.ajax({
        type: "POST",
        url: contextPath + "/FormerUnPackForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatCreerUnPack.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

// COMPARAISON DE PACKS

function submitComparaisonPacks(contextPath) {
    disableButtons();
    var environnementSourceName = $('#environnementSourceName').val();
    var dossierSource = $('#dossierSource').val();
    var dossierSourcePath = $('#dossierSourcePath').val();
    var environnementDestinationName = $('#environnementDestinationName').val();
    var tri = $('input[type=radio][name=tri]:checked').attr('value');

    var dataContent1 = "environnementSourceName=" + environnementSourceName;
    dataContent1 += "&dossierSource=" + dossierSource;
    dataContent1 += "&dossierSourcePath=" + dossierSourcePath;
    dataContent1 += "&environnementDestinationName=" + environnementDestinationName;
    dataContent1 += "&tri=" + tri;
    $.ajax({
        type: "POST",
        url: contextPath + "/VerificationDeltaDownloadPackForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatComparaisonDelta.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

// COMPARAISON D'OBJETS

function submitComparaisonObjets(contextPath) {
    disableButtons();
    var selectedDepot = $('#selectedDepot').val();
    var selectedDepot2 = $('#selectedDepot2').val();
    var environnementSourceName1 = $('#environnementSourceName1').val();
    var environnementSourceName2 = $('#environnementSourceName2').val();
    var nomPack = $('#nomPack').val();
    var textAreaObjectList = $('#textAreaObjectList').val();
    var tri = $('input[type=radio][name=tri]:checked').attr('value');

    var dataContent1 = "selectedDepot=" + selectedDepot;
    dataContent1 += "&selectedDepot2=" + selectedDepot2;
    dataContent1 += "&environnementSourceName1=" + environnementSourceName1;
    dataContent1 += "&environnementSourceName2=" + environnementSourceName2;
    dataContent1 += "&nomPack=" + nomPack;
    dataContent1 += "&textAreaObjectList=" + textAreaObjectList;
    dataContent1 += "&tri=" + tri;
    $.ajax({
        type: "POST",
        url: contextPath + "/ComparaisonObjetsForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatComparaisonObjets.do";
            }

        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

//Constitution pack multiprojets

function submitDataConstitutionPackMultiprojets(contextPath) {
    disableButtons();

    var tri = $('input[type=radio][name=tri]:checked').attr('value');
    var nomPack = $('#nomPack').val();
    //var projetsCiblesElements = $('#projetsCiblesElements').val();
    //var stockProjetsElements = $('#stockProjetsElements').val();

    try {
        var projetsCiblesElements = "";
        $('.projetsCiblesElements').each(function() {
            projetsCiblesElements += $(this).val() + "/";
        });
        var indexVirgule = projetsCiblesElements.lastIndexOf('/');
        projetsCiblesElements = projetsCiblesElements.substr(0, indexVirgule);


        var stockProjetsElements = "";
        $('.stockProjetsElements').each(function() {
            stockProjetsElements += $(this).val() + "/";
        });
        var indexVirgule = stockProjetsElements.lastIndexOf('/');
        stockProjetsElements = stockProjetsElements.substr(0, indexVirgule);

    } catch (e) {
        alert(e.message);
    }


    var dataContent1 = "tri=" + tri + "&nomPack=" + nomPack + "&projetsCiblesElements=" + projetsCiblesElements + "&stockProjetsElements=" + stockProjetsElements;


    $.ajax({
        type: "POST",
        url: contextPath + "/ConstitutionPackMultiprojetsForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatConstitutionPackMultiprojets.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

// submitDataDeployerNPacksSurNEnvironnements

function submitDataDeployerNPacksSurNEnvironnements(contextPath) {
    disableButtons();


    var environnementSourceName = $('#environnementSourceName').val();
    var dossierSourcePath = $('#dossierSourcePath').val();

    try {
        //liste des environnementsCiblesElements
        var environnementsCiblesElements = "";
        $('.environnementsCiblesElements').each(function() {
            environnementsCiblesElements += $(this).val() + "#_#";
        });
        var indexVirgule = environnementsCiblesElements.lastIndexOf("#_#");
        environnementsCiblesElements = environnementsCiblesElements.substr(0, indexVirgule);

        //liste des stockEnvironnementsElements
        var stockEnvironnementsElements = "";
        $('.stockEnvironnementsElements').each(function() {
            stockEnvironnementsElements += $(this).val() + "#_#";
        });
        var indexVirgule = stockEnvironnementsElements.lastIndexOf("#_#");
        stockEnvironnementsElements = stockEnvironnementsElements.substr(0, indexVirgule);

        //liste des nomPack
        var nomPack = "";
        $('.nomPack').each(function() {
            nomPack += $(this).val() + "#_#";
        });
        var indexVirgule = nomPack.lastIndexOf("#_#");
        nomPack = nomPack.substr(0, indexVirgule);

        //liste des autreMnemonic
        var autreMnemonic = "";
        $('.autreMnemonic').each(function() {
            autreMnemonic += $(this).val() + "#_#";
        });
        var indexVirgule = autreMnemonic.lastIndexOf("#_#");
        autreMnemonic = autreMnemonic.substr(0, indexVirgule);

        //liste des nbrIter
        var nbrIter = "";
        $('.nbrIter').each(function() {
            nbrIter += $(this).val() + "#_#";
        });
        var indexVirgule = nbrIter.lastIndexOf("#_#");
        nbrIter = nbrIter.substr(0, indexVirgule);

    } catch (e) {
        alert(e.message);
    }


    var dataContent1 = "environnementSourceName=" + environnementSourceName + "&dossierSourcePath=" + dossierSourcePath + "&environnementsCiblesElements=" + environnementsCiblesElements + "&stockEnvironnementsElements=" + stockEnvironnementsElements + "&nomPack=" + nomPack + "&autreMnemonic=" + autreMnemonic + "&nbrIter=" + nbrIter;


    $.ajax({
        type: "POST",
        url: contextPath + "/LancerDeployerNPacksSurNEnvironnementsForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatDeployerNPacksSurNEnvironnements.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function submitCadencementDelta(contextPath) {
    disableButtons();

    var nomPack = $('#nomPack').val();
    var dataContent1 = "nomPack=" + nomPack;
    $.ajax({
        type: "POST",
        url: contextPath + "/CadencementDeltaForm.do",
        data: dataContent1,
        success: function(response) {
            $("#loadingAnimationConteneur").hide();
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/ResultatCadencement.do";
            }

        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function updateTicketHraccess(numTicketLivraison, messageTrac, contextPath) {
    var dataContent = "numTicketLivraison=" + numTicketLivraison + "&messageTrac=" + messageTrac;
    $("#conteneurAnimation").show();

    $.ajax({
        type: "POST",
        url: contextPath + "/UpdateTicketHraccess",
        data: dataContent,
        success: function(response) {
            $("#conteneurAnimation").hide();
            if ((response.indexOf("KO") !== -1)) {
                $("#messageResultatPersist").addClass("clignotant");
                $("#messageResultatPersist").addClass("rouge");
                $("#messageResultatPersist").html("La mise a jours du ticket TRAC a échoué, veuillez réessayer");
            } else {
                $("#messageResultatPersist").addClass("clignotant");
                $("#messageResultatPersist").addClass("vert");
                $("#messageResultatPersist").html("Le ticket TRAC a été mis a jours avec succès");
                setTimeout(function() {
                    window.location = contextPath + "/getAllTicketsHraccessForm.do?acteur=OVHR";
                }, 1000);
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function submitDataGestionDesLivraisonsHraccessInput(contextPath) {
    disableButtons();
    var numLivraison = $('#numLivraison').val();
    var natureTraitement = $('#natureTraitement').val();
    var packName = $('#packName').val();
    var textAreaObjectList = $("#textAreaObjectList").val();

    var dataContent1 = "numLivraison=" + numLivraison;
    dataContent1 += "&natureTraitement=" + natureTraitement;
    dataContent1 += '&packName=' + packName;
    dataContent1 += '&textAreaObjectList=' + textAreaObjectList;

    $.ajax({
        type: "POST",
        url: contextPath + "/exporterPackHraccessForm.do",
        data: dataContent1,
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else if ((response.indexOf("PACK_DEPLOYED") !== -1)) {
                window.location = contextPath + "/resultatDeploiementPack.do";
            } else {
                window.location = contextPath + "/resultatExportHraccess.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });

}

function submitImporterPackHraccess(contextPath) {
    disableButtons();
    $.ajax({
        type: "POST",
        url: contextPath + "/importerPackHraccessForm.do",
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/resultatImportHraccess.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}

function submitVersionnerPackHraccess(contextPath) {
    disableButtons();
    $.ajax({
        type: "POST",
        url: contextPath + "/versionnerPackHraccessForm.do",
        success: function(response) {
            stopWorker();
            if ((response.indexOf("PROBLEME_EXISTE") !== -1)) {
                window.location = contextPath + "/AfficherMessageErreurs.do";
            } else {
                window.location = contextPath + "/resultatVersionningHraccess.do";
            }
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}
