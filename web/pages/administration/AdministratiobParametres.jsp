<%-- 
    Document   : AdministratiobParametres
    Created on : 30 juil. 2013, 08:34:52
    Author     : karim
--%>

<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="entitiesMysql.Parametres"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="tools.Configuration"%>
<!DOCTYPE html>
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
        //tester avant si seulement il y a changement
        if (contenu.length === 0) {
            $('.ParamtablePereParam .aff input[id="' + theId + '"]').addClass("inputError");
        }
        if ($('.ParamtablePereParam .inputError').size() === 0) {
            $(par).replaceWith(i);
            //mise a jours dans la base de donnée
            var contextPath = "<%=request.getContextPath()%>";
            var url = contextPath + '/AdministrationParametresServlet?id=' + row + '&typeModf=modf&champ=' + theId + '&val=' + contenu;
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

</script>
<table class="ParamtablePereParam">
    <thead>
        <tr>
            <th class='imageContenu'></th>
            <th class='imageContenu'></th>
            <th class='tdContenu'>Clé</th>
            <th class='tdContenu' colspan="3">Valeur</th>
        </tr>
    </thead>
    <tbody>
        <%
            Set cles = Configuration.parametresList.keySet();
            Iterator it = cles.iterator();
            while (it.hasNext()) {
                Object cle = it.next();
                if (!cle.equals("OS") && !cle.equals("espaceLocal")) {
                    out.print("<tr id='" + cle + "' class='containId aff'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove'  onclick='removeElementParam(this);'/></td>"
                            + "<td class='tdContenu' id='cle' ondblclick='toInputParam($(this).find(\"span:first-child\"));'><span id='" + cle + "' class='valueOf' > " + cle + " </span>"
                            + "<td class='tdContenu' id='valeur' colspan='3' ondblclick='toInputParam($(this).find(\"span:first-child\"));'><span class='valueOf'> " + Configuration.parametresList.get(cle) + "</span></td>"
                            + "</tr>");
                }
            }
        %>
        <tr class="addParam add">
            <td class='imageContenu'><img src='images/add.png' class='imageAdd' onclick='ntParam(this);'/></td>
            <td class='imageContenu'><img src='images/remove.png' class='imageRemove'  onclick='annulerParam(this);'/></td>

            <td class="ToAdd tdContenu"><input class='inputContenu' type="text" id="cle" onclick="verfiFormParam();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu" colspan="3"><input class='inputContenu' type="text" id="valeur" onclick="verfiFormParam();" onkeypress="miseAjourInput(this);"/></td>
        </tr>
    </tbody>
    <div id="resultFromServ" hidden="true"></div>
</table>