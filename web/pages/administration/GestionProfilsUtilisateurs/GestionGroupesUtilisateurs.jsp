
<%@page import="tools.Configuration"%>
<%@page import="entitiesMysql.Groupe"%>
<%@page import="java.util.*"%>
<div class="centre">
    <div class="titre">Gestion des groupes d'utilisateurs</div><br>
</div>
<%----%>
<table class="roundCornerTableWithButtons" id="gestionGroupesUsers">
    <thead>
        <tr>
            <th class="imageContenu" ></th>
            <th class="imageContenu"></th>
            <th class="tdContenu">Nom du groupe</th>
            <th class="tdContenu">Gestion des environnements</th>
            <th class="tdContenu">Gestion des fonctionnalités</th>
        </tr>
    </thead>
    <tbody id="bodyTable">
        <%-- affichage existants--%>
        <%
            for (Map.Entry<String, Groupe> groupeEntry : Configuration.groupMap.entrySet()) {
                out.print("<tr id='" + groupeEntry.getKey() + "' class='containId'>");
                out.print("<td class='imageContenu'></td>");
                out.print("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);'/></td>");
                out.print("<td class='tdContenu' id='groupe'>" + groupeEntry.getKey() + "</td>");

                out.print("<td class='tdContenu'><div class='center'>");
                out.print("<a href='#?w=630' rel='popup_gestion_environnements' id='" + groupeEntry.getKey() + "' class='poplight' onclick=remplirPopupGestion(this,'affichageEnvironnements','popup_gestion_environnements','environnements');>");
                out.print("<img class ='icone' src='images/clouds.png'/>");
                out.print("</a>");
                out.print("</div></td>");

                out.print("<td class='tdContenu'><div class='center'>");
                out.print("<a href='#?w=630' rel='popup_gestion_fonctionnalites' id='" + groupeEntry.getKey() + "' class='poplight' onclick=remplirPopupGestion(this,'affichageFonctionnalites','popup_gestion_fonctionnalites','fonctionnalites');>");
                out.print("<img class ='icone' src='images/fonctionnalites.png'/>");
                out.print("</a>");
                out.print("</div></td>");

                out.print("</tr>");
            }
        %>


        <tr id="trAjout">
            <td class='imageContenu'>
            </td>
            <td class='imageContenu'>
                <img src='images/add.png' class='imageAdd' onclick='addGroupe(this);'>
            </td>
            <td id='nomGroupeTd'>
                <input type='text' id='nomGroupe' value='' />
            </td>
            <td class='tdContenu'>

            </td>
            <td class='tdContenu'>
            </td>
        </tr>

    </tbody>
</table>
<div class='center'>
    <div class='alertErreur rouge'></div>
    <span class='resultatInsertion vert logInsertion'></span>
</div>

<div id="popup_creation_nouveau_groupe" class="popup_block">
    new groupe   
</div>

<div id="popup_gestion_environnements" class="popup_block">
    <!-- gestion environnements  -->
</div>        

<div id="popup_gestion_fonctionnalites" class="popup_block">
    <!-- gestion fonctionnalites   -->
</div>        



<script>





                    function addGroupe(imageAjout) {
                        var contextPath = "<%=request.getContextPath()%>";
                        var groupeName = $(imageAjout).closest("tr").children("#nomGroupeTd").children("input").val().trim();
                        if (groupeName.length === 0) {
                            afficherMessageErreur("Veuillez saisir le nom du groupe");
                        } else {
                            $.ajax({
                                type: "GET",
                                url: contextPath + '/ServletGestionGroupesUtilisateurs?typeModf=addNewGroupe&groupeName=' + groupeName,
                                success: function(response) {
                                    var trAjout = $("#trAjout");
                                    $("#trAjout").replaceWith(response);
                                    $("#bodyTable").append(trAjout);
                                    $("#nomGroupe").val("");
                                    afficherMessageSucces("<br>Mise à jour effectuée avec succès");
                                },
                                error: function(e) {
                                    //alert('Error: ' + e);
                                }
                            });
                        }
                    }

                    function removeUserHasEnv(img) {
                        var id = $(img).closest('.containId').attr('id');
                        var toDisplay = $(img).closest('.containId').find('#groupe').text();
                        var r = confirm("Supprimer   " + toDisplay + "??");
                        if (r === true)
                        {
                            $(img).closest('.containId').remove();

                            var contextPath = "<%=request.getContextPath()%>";
                            $.ajax({
                                type: "GET",
                                url: contextPath + '/ServletGestionGroupesUtilisateurs?id=' + id + '&typeModf=supp',
                                success: function(response) {
                                    afficherMessageSucces("<br>Mise à jour effectuée avec succès");
                                },
                                error: function(e) {
                                    //alert('Error: ' + e);
                                }
                            });
                        }
                    }

                    function remplirPopupGestion(baliseA, typeModf, popupDivId, classCheckBox) {

                        var groupeName = $(baliseA).attr("id");
                        var contextPath = "<%=request.getContextPath()%>";
                        $.ajax({
                            type: "GET",
                            url: contextPath + '/ServletGestionGroupesUtilisateurs?typeModf=' + typeModf + '&groupeName=' + groupeName + '&classCheckBox=' + classCheckBox,
                            success: function(response) {
                                $("#" + popupDivId).html(response);
                                redimentionnerLaPopup(baliseA);
                                /*                            
                                 if (typeModf === "affichageFonctionnalites") {
                                 
                                 var largeurFirstTh = $("#tableDroitsFonctionnalites thead tr th:first-child").width();
                                 var largeurFirstTd = $("#tableDroitsFonctionnalites tbody :last-child td:first-child").width();
                                 var largeurScrollBar = largeurFirstTh - largeurFirstTd;
                                 
                                 
                                 $("#tableDroitsFonctionnalites thead tr th:first-child").width(largeurFirstTd);
                                 $("#tableDroitsFonctionnalites thead tr th:nth-child(2)").width($("#tableDroitsFonctionnalites thead tr th:nth-child(2)").width() + largeurScrollBar);
                                 }
                                 */
                            },
                            error: function(e) {
                                //alert('Error: ' + e);
                            }
                        });
                    }

                    function miseAJourGroupeHasEnvironnements(groupeName, classCheckBox) {
                        var parametres = preparerParametres(classCheckBox);
                        var contextPath = "<%=request.getContextPath()%>";
                        $.ajax({
                            type: "GET",
                            url: contextPath + '/ServletGestionGroupesUtilisateurs?typeModf=majEnvironnements&' + parametres + '&groupeName=' + groupeName,
                            success: function(response) {

                            },
                            error: function(e) {
                                //alert('Error: ' + e);
                            }
                        });
                        afficherMessageSucces("<br>Mise à jour effectuée avec succès");
                    }

                    function miseAJourGroupeHasFonctionnalites(groupeName, classCheckBox) {
                        var parametres = preparerParametres(classCheckBox);
                        var contextPath = "<%=request.getContextPath()%>";
                        $.ajax({
                            type: "GET",
                            url: contextPath + '/ServletGestionGroupesUtilisateurs?typeModf=majFonctionnalites&' + parametres + '&groupeName=' + groupeName,
                            success: function(response) {

                            },
                            error: function(e) {
                                //alert('Error: ' + e);
                            }
                        });
                        afficherMessageSucces("<br>Mise à jour effectuée avec succès");
                    }



                    $(document).ready(function() {
                        selectMenu('menuT24');
                    });
</script>        