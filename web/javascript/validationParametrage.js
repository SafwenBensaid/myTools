function validerNivProjet() {
    $('.ParamtablePereNivProjet input').each(function() {
        $(this).removeClass("inputError");
    });
    $('.ParamtablePereNivProjet .add input').each(function() {
        if ($(this).val().length === 0) {
            $(this).addClass("inputError");
        }
    });
    if ($('.ParamtablePereNivProjet .add select').val() === 'not') {
        $('.ParamtablePereNivProjet .add select').addClass('inputError');
    }
}
/*function imageAddManip() {
 $(".imageAdd").each(function() {
 $(this).hide();
 });
 $(".ParamtablePere tr:last-child td:first-child img").show();
 $(".ParamtablePereUser tr:last-child td:first-child img").show();
 $(".ParamtablePereParam tr:last-child td:first-child img").show();
 $(".ParamtablePereNivProjet tr:last-child td:first-child img").show();
 }*/
function EmptyInput(verif) {
    //return true if all inputs are empty
    var empty = true;
    $(verif + " .add input").each(function() {
        var val = $(this).val();
        if (val.length !== 0) {
            empty = false;
        }
    });
    return empty;
}
function miseAjourInput(me) {
    $(me).removeClass('inputError');
}
function scrollto(toElement) {
    alert("validationParametrage.js L38");
    $('html, body').animate({
        scrollTop: $(toElement).offset().top
    }, 2000);
}
function validerEnv() {
    //initialisation
    $('.add input').each(function() {
        $(this).removeClass("inputError");
    });
    //nom
    nomEnvValidation(".add");
    loginValidation("envUserName", ".add");
    passValidation("envPassword", ".add");
    ipValidation(".add");
    //abreviation et type

    var type = $('.add input[id="type"]').val();
    if (type.length === 0) {
        $('.add input[id="type"]').addClass("inputError");
    }
    var abr = $('.add input[id="abreviationNom"]').val();
    if (abr.length === 0) {
        $('.add input[id="abreviationNom"]').addClass("inputError");
    }


}
function nomEnvValidation(type) {
    var nomRegex = /^[a-zA-Z0-9]{3,18}$/g;
    var nom = $(type + ' input[id="nom"]').val();
    if (nom.length === 0 /*|| !(nomRegex.test(nom)) || verfierDansBdd(nom, "nom") > 1*/) {
        $(type + ' input[id="nom"]').addClass("inputError");
    }
}
function ipValidation(type) {
    var ipv4 = /^[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}$/g;
    var ip = $(type + ' input[id="url"]').val();
    if (ip.length === 0 || !(ipv4.test(ip)) /*|| verfierDansBdd(ip,"url")!==0*/) {
        $(type + ' input[id="url"]').addClass("inputError");
    }
}
function loginValidation(champ, type) {
    var loginRegex = /^[a-zA-Z0-9]{3,18}$/g;
    var val = $(type + ' input[id="' + champ + '"]').val();
    if (val.length === 0 /*|| !(loginRegex.test(val)) || verfierDansBdd(val,champ)!==0*/) {
        $(type + ' input[id="' + champ + '"]').addClass("inputError");
    }
}
function passValidation(champ, type) {
    var passRegex = /^[a-zA-Z0-9_-]{3,18}$/g;
    var val = $(type + ' input[id="' + champ + '"]').val();
    if (val.length === 0 /*|| !(passRegex.test(val))*/) {
        $(type + ' input[id="' + champ + '"]').addClass("inputError");
    }
}
function verfierDansBdd(val, champ) {
    //Je pense que cette fonction n'est pas utilisée
    alert("Je pense que cette fonction n'est pas utilisée");
    var exist = 0;
    //vérifier l existance dans la bdd
    $.ajaxSetup({async: false});
    $.ajax({
        url: 'AdministrationServlet?val=' + val + '&champ=' + champ + '&typeModf=verifExistance',
        cache: false,
        success: function(data) {
            var res = data.trim();
            var fail = "fail";
            if (res === fail) {
                alert(' existe déjà dans la base de données!');
                exist = 2;
            } else if (res === "ok")
                exist = 0;
            else
                exist = 1;
        }
    });
    return exist;
}
function updateValidation(theId, val) {

    $('.aff input').each(function() {
        $(this).removeClass("inputError");
    });

    if (theId.indexOf('Pass') !== -1) {
        passValidation(theId, ".aff");
    } else if (theId.indexOf('User') !== -1) {
        loginValidation(theId, ".aff");
    } else if (theId.indexOf('url') !== -1) {
        ipValidation(".aff");
    } else if (theId === "nom") {
        nomEnvValidation(".aff");
    } else {
        if (val.length === 0) {
            $('.aff input[id="' + theId + '"]').addClass("inputError");
        }
    }
}
function RemoveErrorForm(classNameOfParent) {
    //supprimer le contour de toute la tr
    try {
        $(classNameOfParent + ' .ToAdd').each(function() {
            $(this).removeClass('FormAddError');
        });
    } catch (ex) {
        alert("RemoveErrorForm: " + ex.message);
    }
}

function RemoveAllInputErrorForm(classNameOfParent) {
    //supprimer le contour de tous les input
    try {
        $(classNameOfParent + ' input').each(function() {
            $(this).removeClass("inputError");
        });
    } catch (ex) {
        alert("RemoveErrorForm: " + ex.message);
    }
}

function EmptyAllInputErrorForm() {
    //supprimer le contour de tous les input
    try {
        $("input[type='text']").each(function() {
            $(this).val("");
        });
    } catch (ex) {
        alert("RemoveErrorForm: " + ex.message);
    }
}

function removeErrorFormFromLastElementIfAllEmpty(className) {
    try {
        if (EmptyInput(className) === true) {
            RemoveAllInputErrorForm(className);
        }
    } catch (ee) {
        alert("removeErrorFormFromLastElementIfAllEmpty: " + ee.message);
    }
}
function validerParam() {
    var cle = $('.ParamtablePereParam input[id="cle"]').val();
    if (cle.length === 0 /*|| !(nomRegex.test(nom)) || verfierDansBdd(nom, "nom") > 1*/) {
        $('.ParamtablePereParam input[id="cle"]').addClass("inputError");
    }
    var valeur = $('.ParamtablePereParam input[id="valeur"]').val();
    if (valeur.length === 0 /*|| !(nomRegex.test(nom)) || verfierDansBdd(nom, "nom") > 1*/) {
        $('.ParamtablePereParam input[id="valeur"]').addClass("inputError");
    }
}
