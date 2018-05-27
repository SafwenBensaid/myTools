<%@page import="java.util.Iterator"%>
<%@page import="tools.Configuration"%>
<%@page import="java.util.Map"%>
<%@page import="tools.Tools"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashMap"%>
<%@page import="entitiesMysql.Parametres"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<!DOCTYPE html>

<br><br>
<div class="grandTitre">
    Fixation des délais de prise en charge
</div>
<br><br><br>
<div id="resultatTableDelais">

</div>

<script>
    $(document).ready(function() {
        loadTableDelais(true);
    });

    function loadTableDelais(cyclique) {
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "POST",
            url: contextPath + "/DelaisGestionIncidentServlet",
            data: "action=load",
            success: function(response) {
                $("#loadingAnimationConteneur").hide();
                $("#resultatTableDelais").html(response);

                if (cyclique === true) {
                    setTimeout(function() {
                        loadTableDelais(true);
                    }, 60000);
                }
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
</script>

<style>
    .propagation {
        background-color: #eee6d8;
        text-align: center !important;
        font-size: 12px !important;
        font-weight: bold !important;
        font-family: cambria, tahoma, arial , sans-serif !important;
        color: #343971 !important;
    }
    .dpc thead tr th, .dpc tbody tr td{
        width: 110px !important;
        vertical-align: middle;
    }
    .dpc thead tr, .dpc tbody tr{
        height: 40px !important;
    }
    .dpc thead:first-child tr th:first-child, .dpc tbody:first-child tr td:first-child {
        width: 100px !important;
    }
    .diagonal {
        border-bottom: 40px solid #eee6d8;
        border-right: 105px solid rgba(0, 0, 0, 0);
        display: block;
        height: 0;
        width: 40px;
        float:left;
        margin-left : -42px;
    }
    .b1{
        margin-top: 3px;
        margin-left : 90px;
    }
    .b2{       
        margin-top: 7px;
        margin-left : 44px;
    }
    .inputContenu{
        width: 52px;
        margin-left: 27px;
    }

    .valueOf{
        margin-left: 27px;
    }

</style>


<script type="text/javascript" src="javascript/validationParametrage.js"></script>
<script>
    function toInputParam(our) {
        removeErrorFormFromLastElementIfAllEmpty(".ParamtablePereParam");
        var contenu = $(our).html().trim();
        var id = $(our).parent().attr('id');
        var i = "<input type='text' id='" + id + "' value='" + contenu + "' class='inputContenu' onblur='toTextParam($(this));' onkeypress=\"miseAjourInput(this);\"/>";
        if (!EmptyInput(".ParamtablePereParam")) {
            $('.ParamtablePereParam .ToAdd').each(function() {
                $(this).addClass('FormAddError');
            });
            scrollto(".ParamtablePereParam .addParam");
        } else if ($('.ParamtablePereParam .aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.ParamtablePereParam .inputError');
            toTextParam($('.ParamtablePereParam .aff input')[0]);
            if ($('.ParamtablePereParam .inputError').size() === 0) {
                $(our).replaceWith(i);
                $('.ParamtablePereParam .aff input').focus();
            } else {
                scrollto('.ParamtablePereParam .inputError');
            }
        } else {
            $(our).replaceWith(i);
            $('.ParamtablePereParam .aff input').focus();
        }
    }

    function toTextParam(par) {
        var contenu = $(par).val();
        var theId = $(par).parent().attr('id');
        var row = $(par).closest('.containId').attr('id');
        if (theId === 'cle') {
            $(par).closest('.containId').attr('id', contenu);
        }
        var i = "<span class='valueOf' ondblclick='toInputParam($(this));'> " + contenu + "</span>";
        var priorite = theId;
        var type = row;
        var delais = contenu;

        //tester avant si seulement il y a changement
        if (contenu.length === 0) {
            $('.ParamtablePereParam .aff input[id="' + theId + '"]').addClass("inputError");
        }
        if ($('.ParamtablePereParam .inputError').size() === 0) {
            $(par).replaceWith(i);
            //mise a jours dans la base de donnée
            var contextPath = "<%=request.getContextPath()%>";
            $("#loadingAnimationConteneur1").show();
            $.ajax({
                type: "POST",
                url: contextPath + "/DelaisGestionIncidentServlet",
                data: "action=submit&priorite=" + priorite + "&type=" + type + "&delais=" + delais,
                success: function(response) {
                    $("#loadingAnimationConteneur1").hide();
                    $("#messageResultatPersist").text(response);
                    $("#messageResultatPersist").show();
                    loadTableDelais(false);
                    setTimeout(function() {
                        $("#messageResultatPersist").hide();
                    }, 3000);
                },
                error: function(e) {
                    //alert('Error: ' + e);
                    setTimeout(function() {
                        $("#messageResultatPersist").hide();
                    }, 3000);
                }
            });
        }
    }

    function removeElementParam(img) {
        var row = $(img).closest('.containId').attr('id');
        var r = confirm("Etes vous sur: supprimer cette parametre: " + row + "!");
        if (r == true)
        {
            $(img).closest('.containId').remove();
            //maj bdd
            var contextPath = "<%=request.getContextPath()%>";
            var url = contextPath + '/AdministrationParametresServlet?id=' + row + '&typeModf=supp';
            if (window.XMLHttpRequest) {
                requete = new XMLHttpRequest();
            } else if (window.ActiveXObject) {
                requete = new ActiveXObject("Microsoft.XMLHTTP");
            }
            requete.open("GET", url, true);
            requete.onreadystatechange = majIHM;
            requete.send(null);
        }
        imageAddManip();
    }
    function majIHM() {
        var message = "";
        if (requete.readyState == 4) {
            if (requete.status == 200) {
                // exploitation des données de la réponse
                var messageTag = requete.responseXML.getElementsByTagName("message")[0];
                message = messageTag.childNodes[0].nodeValue;
                document.getElementById("resultFromServ").innerHTML = "<em>" + message + "</em>";
            }
        }
    }
    function annulerParam(img) {
        RemoveErrorForm(".ParamtablePereParam");
        $('.ParamtablePereParam .ToAdd').each(function() {
            $(this).removeClass('FormAddError');
        });

        $(".ParamtablePereParam .addParam input").each(function() {
            $(this).val('');
            $(this).removeClass("inputError");
        });
        $('.ParamtablePereParam .addParam').removeClass('formError');
        $('.ParamtablePereParam .addParam').addClass('SimplPack');
    }
    function ntParam(img) {
        //si la formulaire entourer pas le rouge
        RemoveErrorForm(".ParamtablePereParam");
        if ($('.ParamtablePereParam .aff input').size() === 1) {
            $('.ParamtablePereParam .aff input').addClass('inputError');
            scrollto(".ParamtablePereParam .aff .inputError");
        } else {
            validerParam();
            if ($('.ParamtablePereParam .inputError')[0]) {
                scrollto(".ParamtablePereParam .addParam .inputError");
            } else {
                //ajout dans ma base de données
                var myTab = new Object();
                $('.ParamtablePereParam input').each(function() {
                    var contenu = $(this).val();
                    var theId = $(this).attr('id');
                    myTab[theId] = contenu;
                });
                var jsonData = JSON.stringify(myTab);
                $.getJSON("AdministrationParametresServlet", {typeModf: "export", json: jsonData}, function(data) {
                    var url = window.location.href.split("?")[0] + "?idAccordeon=gestionDesParametres";
                    document.location.href = url;
                });
            }
        }
    }
    function verfiFormParam() {
        if ($('.ParamtablePereParam .aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.ParamtablePereParam .aff input');
        }
        RemoveErrorForm(".ParamtablePereParam");
    }
    $(document).ready(function() {
        selectMenu('menuAdministration');
    });
</script>

