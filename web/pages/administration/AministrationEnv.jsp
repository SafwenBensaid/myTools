<%-- s
    Document   : AministrationEnv
    Created on : 20 juil. 2013, 12:01:28
    Author     : karim
--%>
<%@page import="tools.Tools"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="entitiesMysql.Environnement"%>
<%@page import="tools.DataBaseTools"%>
<%@page import="java.util.Map"%>
<%@page import="dto.EnvironnementDTO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="tools.Configuration"%>
<!DOCTYPE html>
<script type="text/javascript" src="javascript/validationParametrage.js"></script>
<script>
    window.onload = function() {
        addForm();
        managePaddingForEmptySpans();
    }
    function toInput(our, c) {
        removeErrorFormFromLastElementIfAllEmpty(".ParamtablePere");
        //vérifier une seule input        
        if (!EmptyInput(".ParamtablePere")) {
            $('.ParamtablePere .add fieldset').removeClass('SimplPack');
            $('.ParamtablePere .add fieldset').addClass('formError');
            //aller vers la classe add
            scrollto(".ParamtablePere .add");
        } else if ($('.ParamtablePere .aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.ParamtablePere .inputError');
            var contenu = $('.ParamtablePere .aff input').val();
            toText($('.ParamtablePere .aff input')[0], c, contenu);
            if ($('.ParamtablePere .inputError').size() === 0) {
                var contenu = $(our).html().trim();
                var id = $(our).parent().attr('id');
                var i = "<input type='text' id='" + id + "' value='" + contenu + "' class=" + c + " onblur='toText($(this),\"" + c + "\",\"" + contenu + "\");') onkeypress=\"miseAjourInput(this);\" />";
                $(our).replaceWith(i);
                $('.ParamtablePere .aff input').focus();
            } else {
                scrollto('.ParamtablePere .inputError');
            }
        } else {
            var contenu = $(our).html().trim();
            var id = $(our).parent().attr('id');
            var i = "<input type='text' id='" + id + "' value='" + contenu + "' class=" + c + " onblur='toText($(this),\"" + c + "\",\"" + contenu + "\");')  onkeypress=\"miseAjourInput(this);\" />";
            $(our).replaceWith(i);
            $('.aff input').focus();
        }

    }
    function toText(par, c, ancienVal) {
        var contenu = $(par).val();
        //alert(ancienVal + " -> " + contenu);


        var theId = $(par).parent().attr('id');
        var row = $(par).closest('.containId').attr('id');

        if (theId === 'nom') {
            $(par).closest('.containId').attr('id', contenu);
        }

        var i = "<span class='valueOf' ondblclick='toInput($(this), \"" + c + "\");'> " + contenu + "</span>";
        //tester avant si seu lement il y a changement

        if ((ancienVal !== contenu) && (contenu.trim().length > 0)) {
            updateValidation(theId, contenu);
        }
        if ($('.inputError').size() === 0) {
            $(par).replaceWith(i);
            //mise a jours dans la base de donnée
            if (ancienVal !== contenu) {
                contenu = contenu.replace("&", "ET_COMMERCIAL");
                contenu = contenu.replace("#", "DIESE");
                contenu = contenu.replace("+", "PLUS");
                contenu = contenu.replace("$", "DOLLAR");
                //alert(contenu);
                var contextPath = "<%=request.getContextPath()%>";
                var url = contextPath + '/AdministrationServlet?id=' + row + '&typeModf=modf&champ=' + theId + '&val=' + contenu;
                if (window.XMLHttpRequest) {
                    requete = new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    requete = new ActiveXObject("Microsoft.XMLHTTP");
                }
                requete.open("GET", url, true);
                requete.onreadystatechange = majIHM;
                requete.send(null);
            }
        }
        managePaddingForEmptySpans();
    }
    function removeElement(img) {
        var row = $(img).closest('.containId').attr('id');
        var r = confirm("Etes vous sur: supprimer l'environnement " + row + "!");
        if (r == true)
        {
            $(img).closest('.containId').remove();
            //maj bdd
            var contextPath = "<%=request.getContextPath()%>";
            var url = contextPath + '/AdministrationServlet?id=' + row + '&typeModf=supp';
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
    function verfiForm() {
        if ($('.aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.aff input');
        }
        $('.ParamtablePere .add fieldset').removeClass('formError');
        $('.ParamtablePere .add fieldset').addClass('SimplPack');
    }
    function annuler(img) {
        $(".ParamtablePere .add input").each(function() {
            $(this).val('');
            $(this).removeClass("inputError");
        });
        $('.ParamtablePere .add fieldset').removeClass('formError');
        $('.ParamtablePere .add fieldset').addClass('SimplPack');
    }
    function addForm() {
        var contenu = '<tr class="containId add">\n\
                    <td><img src="images/add.png" class="imageAdd" onclick="addEnvironnement(this);"></td>\n\
                    <td><img src="images/remove.png" class="imageRemove" onclick="annuler(this);"></td>\n\
                    <td>\n\
                    <fieldset class="SimplPack">\n\
                    <legend>Environnement: <input type="text" class="smallInput" id="nom" onclick="verfiForm();" onkeypress="miseAjourInput(this);">  </legend>\n\
                    <table class="contenu">\n\
                    <tbody><tr>\n\
                        <td class="tdContenu"><span class="titleOf">Adresse IP:</span> <input type="text"  class="FirstColInput" id="url" onclick="verfiForm();" onkeypress="miseAjourInput(this);"></td>\n\
                         <td class="tdContenu"><span class="titleOf">Utilisateur système:</span> <input type="text" class="FirstColInput" id="envUserName" onclick="verfiForm();" onkeypress="miseAjourInput(this);"></td>\n\
                        <td class="tdContenu"><span class="titleOf">Mot de passe système:</span> <input type="text" class="FirstColInput" id="envPassword" onclick="verfiForm();" onkeypress="miseAjourInput(this);"> </td>\n\
                    </tr>\n\
                    <tr>\n\
                        <td class="tdContenu"><span class="titleOf">Abreviation:</span> <input type="text"class="FirstColInput" id="abreviationNom" onclick="verfiForm();" onkeypress="miseAjourInput(this);"> </td>\n\
                        <td class="tdContenu"><span class="titleOf">Type:</span> <input type="text" class="FirstColInput" id="type" onclick="verfiForm();" onkeypress="miseAjourInput(this);"></td>\n\
                        <td class="tdContenu"></td>\n\
                    </tr>\n\
                    </tbody></table>\n\
                    </fieldset>\n\
                    </td>\n\
                    </tr>';

        $(".ParamtablePere").children('tbody').append(contenu);
        $(".ParamtablePere .imageAdd").each(function() {
            $(this).hide();
        });
        $(".ParamtablePere tr:last-child td:first-child img").show();
    }
    function addEnvironnement(imgAjout) {
        //enlever borudre rouge de la formulaire
        $('.ParamtablePere .add fieldset').removeClass('formError');
        $('.ParamtablePere .add fieldset').addClass('SimplPack');

        if ($('.aff input').size() === 1) {
            $('.aff input').addClass('inputError');
            scrollto(".ParamtablePere .aff .inputError");
        } else {
            validerEnv();
            if ($('.inputError')[0]) {
                scrollto(".ParamtablePere .add .inputError");
            } else {
                //ajout dans ma base de données
                var myTab = new Object();
                $('.ParamtablePere input').each(function() {
                    var contenu = $(this).val();
                    var theId = $(this).attr('id');
                    myTab[theId] = contenu;
                });
                var jsonData = JSON.stringify(myTab);

                $.getJSON("AdministrationServlet", {typeModf: "export", json: jsonData}, function(data) {
                    var url = window.location.href.split("?")[0] + "?idAccordeon=gestionDesEnvironnements";
                    document.location.href = url;
                });
            }
        }


    }

    function managePaddingForEmptySpans() {
        $(".ParamtablePere .contenu .valueOf").each(function() {
            $(this).addClass("emptyPadding");
        });
    }



</script>
<table class="ParamtablePere">

    <%
        //pour initialiser la liste en cas de supression ou modification ou ajouts
        Configuration.initialisation();
        String connectedUser = Tools.getConnectedLogin();
        Environnement u = null;
        for (Map.Entry<String, Environnement> entry : Configuration.allEnvironnementMap.entrySet()) {
            u = entry.getValue();
            u.setEnvUserName(u.getEnvUserName().replace("&#", "&amp;#"));
            u.setEnvPassword(u.getEnvPassword().replace("&#", "&amp;#"));

            out.print("<tr id='" + u.getNom() + "' class='containId aff'>"
                    + " <td>  </td>         "
                    + "<td><img src='images/remove.png' class='imageRemove'  onclick='removeElement(this);'/></td>"
                    + "<td><fieldset class='SimplPack'><legend  id='nom'>Environnement: <span id='" + u.getNom() + "' class='valueOf' ondblclick='toInput($(this), \"smallInput\");'> " + u.getNom() + " </span>  </legend> <table class='contenu'><tr>"
                    + "<td class='tdContenu' id='url'><span class='titleOf'>Adresse IP:</span> <span class='valueOf' ondblclick='toInput($(this), \"FirstColInput\");'> " + u.getUrl() + "</span></td>"
                    + "<td class='tdContenu' id='envUserName'><span class='titleOf'>Utilisateur système:</span> <span class='valueOf' ondblclick='toInput($(this), \"FirstColInput\");'> " + u.getEnvUserName() + "</span></td>"
                    + "<td class='tdContenu' id='envPassword'><span class='titleOf'>Mot de passe système:</span> <span class='valueOf' ondblclick='toInput($(this), \"FirstColInput\");'> " + u.getEnvPassword() + "</span> </td>"
                    + "<tr> <td class='tdContenu' id='abreviationNom'><span class='titleOf'>Abreviation:</span> <span class='valueOf' ondblclick='toInput($(this), \"FirstColInput\");'>" + u.getAbreviationNom() + "</span> </td>"
                    + "<td class='tdContenu' id='type'><span class='titleOf'>Type:</span> <span class='valueOf' ondblclick='toInput($(this), \"FirstColInput\");'> " + u.getType() + "</span></td>"
                    + "<td class='tdContenu'></td>"
                    + "</tr></table> </fieldset> </td>"
                    + "</tr>");
        }
    %>
    <div id="resultFromServ" hidden="true"></div>
</table>