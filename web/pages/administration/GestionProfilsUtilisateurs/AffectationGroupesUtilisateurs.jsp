<%@page import="entitiesMysql.*"%>
<%@page import="java.util.*"%>
<%@page import="tools.Configuration"%>

<div class="centre" style="margin-top: 25px;">
    <div class="grandTitre">Gestion Utilisateurs</div><br>
</div>
<%----%>
<table class="roundCornerTableWithButtons" id="UserHasGroup">
    <thead>
        <tr>
            <th class="imageContenu" ></th>
            <th class="imageContenu"></th>
            <th class="tdContenu">Nom et prénom</th>
            <th class="tdContenu">Login</th>
            <th class="tdContenu">Email</th>
        </tr>
    </thead>
    <tbody id="bodyId">
        <%
            Configuration.getAllUsers();

            for (Map.Entry<String, Users> entry : Configuration.usersMap.entrySet()) {
                out.print("<tr id='" + entry.getValue().getLogin() + "'>");

                // out.print("<td class='imageContenu'><img src='images/edit.png' class='imageRemove' onclick=remplirPopup(this,'affichageGroupes','popup_gestion_groupes','users_groups'); id='editBtn' name='editBtn'/></td>");

                out.print("<td class='imageContenu'><div class='center'>");
                out.print("<a href='#?w=630' rel='popup_gestion_groupes' id='" + entry.getValue().getLogin() + "' class='poplight' onclick=remplirPopup(this,'affichageGroupes','popup_gestion_groupes','groupes');>");
                out.print("<img class ='imageRemove' src='images/edit.png'/>");
                out.print("</a>");
                out.print("</div></td>");




                out.print("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);' id='delBtn' name='delBtn'/></td>");

                out.print("<td class='tdContenu' id='nom.prenom'><span class='valueOf'>" + entry.getValue().getNomPrenom() + " </span></td>");
                out.print("<td class='tdContenu' id='login'><span class='valueOf'>" + entry.getValue().getLogin() + " </span></td>");
                out.print("<td class='tdContenu' id='email'><span class='valueOf'>" + entry.getValue().getEmail() + " </span></td>");
                out.print("</tr>");
            }
        %>        
    </tbody>
</table>


<div id="popup_gestion_groupes" class="popup_block">
    <!-- gestion fonctionnalites   -->
</div>  





<div id="loadingAnimationConteneur">
    <div class="center">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>
</div>



<script>

    function miseAJourUsersHasGroupes(userLogin, classCheckBox) {
        var parametres = preparerParametres(classCheckBox);
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "GET",
            url: contextPath + '/ServletGestionAffectationGroupesUtilisateurs?typeModf=majGroupes&' + parametres + '&userLogin=' + userLogin,
            success: function(response) {
                afficherMessageSucces("<br>Mise à jour effectuée avec succès");
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });

    }

    function remplirPopup(baliseA, typeModf, popupDivId, classCheckBox) {
        var userLogin = $(baliseA).attr("id");
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "GET",
            url: contextPath + '/ServletGestionAffectationGroupesUtilisateurs?typeModf=' + typeModf + '&userLogin=' + userLogin + '&classCheckBox=' + classCheckBox,
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

    function removeUserHasEnv(img) {
        var login = $(img).closest('tr').attr('id');
        var r = confirm("Supprimer   " + login + "??");
        var dataContent = "login=" + login + "&modif=suppression";

        if (r === true)
        {
            $("#loadingAnimationConteneur").show();
            var contextPath = "<%=request.getContextPath()%>";
            $.ajax({
                type: "GET",
                url: contextPath + '/ServletGestionUtilisateur',
                data: dataContent,
                success: function(response) {
                    afficherMessageSucces(response);
                    $("#loadingAnimationConteneur").hide();
                    $(img).closest('tr').remove();
                },
                error: function(e) {
                    //alert('Error: ' + e);
                }
            });
        }
    }



    $(document).ready(function() {
        selectMenu('menuT24');
        $("#loadingAnimationConteneur").hide();
    });
</script>    