<%-- 
    Document   : AdministrationNiveauProjet
    Created on : 30 juil. 2013, 08:37:43
    Author     : karim
--%>


<%@page import="javax.persistence.Query"%>
<%@page import="tools.DataBaseTools"%>
<%@page import="entitiesMysql.Niveauprojet"%>
<%@page import="entitiesMysql.Users"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="tools.Configuration"%>
<!DOCTYPE html>
<script type="text/javascript" src="javascript/validationParametrage.js"></script>
<script>

    function toInputNivProjet(our, c) {
        removeErrorFormFromLastElementIfAllEmpty(".ParamtablePereNivProjet");
        var i = "";
        //var contenu = $(our).html().trim();
        var contenu = $(our).text().trim();
        var id = $(our).attr('id');
        if (c === 'select') {
            i = $(" .add #selectValuesNP").html();
        } else {
            i = "<input type='text' id='" + id + "' value='" + contenu + "' class=" + c + " onblur='toTextNivProjet($(this),\"" + c + "\");' onkeypress=\"miseAjourInput(this);\" />";
        }
        i = "<td>" + i + "</td>";
        if (!EmptyInput(".ParamtablePereNivProjet") || $('.add #sitedeveloppement').val() !== 'not') {

            $('.ParamtablePereNivProjet .ToAdd').each(function() {
                $(this).addClass('FormAddError');
            });
            scrollto(".ParamtablePereNivProjet .add");
        } else if ($('.ParamtablePereNivProjet .aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.inputError');
            toTextNivProjet($('.ParamtablePereNivProjet .aff input')[0], c, contenu);
            if ($('.ParamtablePereNivProjet .inputError').size() === 0) {
                $(our).replaceWith(i);
                $('.aff input').focus();
            } else {
                scrollto('.inputError');
            }
        } else if ($('.ParamtablePereNivProjet .aff select').size() === 1) {
            $('.ParamtablePereNivProjet .aff select').addClass('inputError');
            scrollto('.inputError');
            toTextNivProjet($('.ParamtablePereNivProjet .aff select'));
            if ($('.ParamtablePereNivProjet .inputError').size() === 0) {
                $(our).replaceWith(i);
                $('.aff input').focus();
            } else {
                scrollto('.inputError');
            }
        } else {
            $(our).replaceWith(i);
            $('.aff input').focus();
        }
        $(".aff #sitedeveloppement").attr('onBlur', 'toTextNivProjet(this,"select")');
        $('#sitedeveloppement').click(function() {
            RemoveErrorForm(".ParamtablePereNivProjet");
            $(this).removeClass('inputError');
        });

    }
    function toTextNivProjet(par, c, ancienVal) {
        var contenu = $(par).val();
        var theId = $(par).attr('id');
        var row = $(par).closest('.containId').attr('id');
        if (theId === 'nom') {
            $(par).closest('.containId').attr('id', contenu);
        }
        var i = "<td class='tdContenu' id='" + theId + "'  ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf'> " + contenu + "</span></td>";
        //tester avant si seulement il y a changement    
        if ((ancienVal !== contenu) && (contenu.trim().length > 0)) {
            updateValidation(theId, contenu);
        }
        // if (contenu === 'not' || contenu.length === 0) {
        //   $(par).addClass('inputError');
        //}
        if ($('.ParamtablePereNivProjet .inputError').size() === 0) {
            $(par).replaceWith(i);
            //mise a jours dans la base de donnée 
            if (ancienVal !== contenu) {
                var contextPath = "<%=request.getContextPath()%>";
                var url = contextPath + '/AdministrationNivProjetServlet?id=' + row + '&typeModf=modf&champ=' + theId + '&val=' + contenu;
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
    }
    function removeElementNivProjet(img) {
        var row = $(img).closest('.containId').attr('id');
        var toDisplay = $(img).closest('.containId').find('#abreviation span').text();
        var r = confirm("Etes vous sur: supprimer le niveau projet  " + toDisplay + "!");
        if (r === true)
        {
            $(img).closest('.containId').remove();
            //maj bdd
            var contextPath = "<%=request.getContextPath()%>";
            var url = contextPath + '/AdministrationNivProjetServlet?id=' + row + '&typeModf=supp';
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
    function annulerNivProjet(img) {
        RemoveErrorForm(".ParamtablePereNivProjet");
        $('.ParamtablePereNivProjet .ToAdd').each(function() {
            $(this).removeClass('FormAddError');
        });

        $(".ParamtablePereNivProjet .add input").each(function() {
            $(this).val('');
            $(this).removeClass("inputError");
        });
        $('.ParamtablePereNivProjet .add select').removeClass("inputError");
        $('.ParamtablePereNivProjet .add select').val('not');
        $('.ParamtablePereNivProjet .add').removeClass('formError');
        $('.ParamtablePereNivProjet .add').addClass('SimplPack');
    }
    function ntNivProjet(img) {
        RemoveErrorForm(".ParamtablePereNivProjet");
        if ($('.ParamtablePereNivProjet .aff input').size() === 1) {
            $('.ParamtablePereNivProjet .aff input').addClass('inputError');
            scrollto(".ParamtablePereNivProjet .aff .inputError");
        } else {
            validerNivProjet();
            if ($('.ParamtablePereNivProjet .inputError')[0]) {
                scrollto(".ParamtablePereNivProjet .add .inputError");
            } else {
                //ajout dans ma base de données
                var myTab = new Object();
                $('.ParamtablePereNivProjet input').each(function() {
                    var contenu = $(this).val();
                    var theId = $(this).attr('id');
                    myTab[theId] = contenu;
                });
                myTab['sitedeveloppement'] = $('.ParamtablePereNivProjet .add select').val();

                var jsonData = JSON.stringify(myTab);
                $.getJSON("AdministrationNivProjetServlet", {typeModf: "export", json: jsonData}, function(data) {
                    var url = window.location.href.split("?")[0] + "?idAccordeon=gestionNiveauxProjets";
                    document.location.href = url;
                });
            }
        }
    }
    function verfiFormNP() {
        if ($('.ParamtablePereNivProjet .aff input').size() === 1) {//s'il y une autre input deja ouverte
            scrollto('.aff input');
        }
        RemoveErrorForm(".ParamtablePereNivProjet");
    }

</script>
<table class="ParamtablePereNivProjet">
    <thead>
        <tr>
            <th class='imageContenu'></th>
            <th class='imageContenu'></th>
            <th class='tdContenu'>Nom</th>
            <th class='tdContenu'>Abréviation</th>
            <th class='tdContenu'>Actif</th>
            <th class='tdContenu'>Release</th>
            <th class='tdContenu'>Métier</th>
            <th class='tdContenu'>Type projet</th>
            <th class='tdContenu'>Site développement</th>
        </tr>
    </thead>
    <tbody>
        <%
            //pour initialiser la liste en cas de supression ou modification ou ajouts
            List<Niveauprojet> projetList = null;
            DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
            Query q = dbTools.em.createNamedQuery("Niveauprojet.findAll");
            projetList = (List<Niveauprojet>) q.getResultList();
            for (Niveauprojet np : projetList) {
                if (np.getActif().equals("CR")){
                    out.print("<tr id='" + np.getNom() + "' class='containId aff couleur1'>");
                }else if (np.getActif().equals("CP")){
                    out.print("<tr id='" + np.getNom() + "' class='containId aff couleur2'>");
                } else if (np.getActif().equals("PR")){
                    out.print("<tr id='" + np.getNom() + "' class='containId aff couleur3'>");
                } else if (np.getActif().equals("ND")){
                    out.print("<tr id='" + np.getNom() + "' class='containId aff couleur4'>");
                } 
                
                
                out.print(" <td></td>"
                        + "<td><img src='images/remove.png' class='imageRemove'  onclick='removeElementNivProjet(this);'/></td>"
                        + "<td class='tdContenu' id='nom'  ondblclick='toInputNivProjet($(this).first(), \"se\");'><span id='" + np.getNom() + "' class='valueOf'> " + np.getNom() + " </span></td>"
                        + "<td class='tdContenu' id='abreviation'  ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf'> " + np.getAbreviation() + "</span></td>"
                        + "<td class='tdContenu' id='actif' ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf'> " + np.getActif() + "</span></td>"
                        + "<td class='tdContenu' id='releaseprevue' ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf' > " + np.getReleaseprevue() + "</span></td>"
                        + "<td class='tdContenu' id='metier' ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf'> " + np.getMetier() + "</span></td>"
                        + "<td class='tdContenu' id='typeprojet' ondblclick='toInputNivProjet($(this).first(), \"se\");'><span class='valueOf'> " + np.getTypeprojet() + "</span></td>"
                        + "<td class='tdContenu' id='sitedeveloppement' ondblclick='toInputNivProjet($(this).first(), \"select\");'><span class='valueOf'> " + np.getSitedeveloppement() + "</span></td>"
                        + "</tr>");
            }
            dbTools.closeRessources();
        %>
        <tr class="add">
            <td class='imageContenu'> <img src='images/add.png' class='imageAdd' onclick="ntNivProjet(this);" /> </td>
            <td class='imageContenu'><img src='images/remove.png' class='imageRemove'  onclick='annulerNivProjet(this);'/></td>

            <td class="ToAdd tdContenu nom"><input class='inputContenu' type="text" id="nom" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu abreviation"><input class='inputContenu' type="text" id="abreviation" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu actif"><input class='inputContenu' type="text" id="actif" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu releaseprevue"><input class='inputContenu' type="text" id="releaseprevue" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu metier"><input class='inputContenu' type="text" id="metier" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td class="ToAdd tdContenu typeprojet"><input class='inputContenu' type="text" id="typeprojet" onclick="verfiFormNP();" onkeypress="miseAjourInput(this);"/></td>
            <td id="selectValuesNP" class="ToAdd tdContenu sitedeveloppement"><select class='inputContenu' id="sitedeveloppement" onclick="miseAjourInput(this);">
                    <option value="not" selected>choisir...</option>
                    <option value="BIAT" >BIAT</option>
                    <option value="BFI" >BFI</option>
                    <option value="TEMENOS" >TEMENOS</option>
                </select></td>
        </tr>
    </tbody>
    <div id="resultFromServ" hidden="true"></div>
</table>