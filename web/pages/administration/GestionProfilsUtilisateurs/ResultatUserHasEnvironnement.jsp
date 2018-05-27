<%@page import="tools.Tools"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.*"%>
<%@page import="entitiesMysql.Environnement"%>
<%@page import="tools.Configuration"%>
<%@page import="dto.EnvironnementDTO"%>
<%@page language="java"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<%
    String connectedUser = Tools.getConnectedLogin();
    //login,envName,EnvDto (les env dont chaque user a le droit selon le groupe)
    Map<String, Map<String, Environnement>> envMap = (Map<String, Map<String, Environnement>>) Configuration.environnementMapDutyGroup;
    Map<String, Map<String, EnvironnementDTO>> envDTOMap = (Map<String, Map<String, EnvironnementDTO>>) Configuration.environnementDTOMapUserHasEnvironnement;
    //<user,son group list>
    Map<String, List<String>> usersGroupMap = (Map<String, List<String>>) Configuration.usersGroupMap;
    boolean isSuperAdmin = usersGroupMap.get(connectedUser).contains("SUPER_ADMIN");
    boolean isAdmin = usersGroupMap.get(connectedUser).contains("ADMIN");
%>

<script>
    function annulerUserHasEnv(img) {
        RemoveErrorForm(".roundCornerTableWithButtons");
        $('.roundCornerTableWithButtons .ToAdd').each(function() {
            $(this).removeClass('FormAddError');
        });
        if (<%=isSuperAdmin%>)
        {
            $(".roundCornerTableWithButtons .add input ").each(function() {
                $(this).val('');
                $(this).removeClass("inputError");
            });
            $('.roundCornerTableWithButtons .add select').removeClass("inputError");
            $('.roundCornerTableWithButtons .add select').val('');
            $('.ParamtablePereNivProjet .add').removeClass('formError');
        }
        else {
            $('.roundCornerTableWithButtons .add select').val('');
            $('.roundCornerTableWithButtons .add #login').val('');
            $('.roundCornerTableWithButtons .add #pass').val('');
            $('.roundCornerTableWithButtons .add select').removeClass("inputError");
            $('.roundCornerTableWithButtons .add #login').removeClass("inputError");
            $('.roundCornerTableWithButtons .add #pass').removeClass("inputError");
        }
    }
    function genererSeparationTr(login) {
        var resultat = "";

        if (login !== "") {
            resultat += "<tr class='separationTr' id='separateur_" + login.replace(new RegExp("\\.", "g"), "_") + "'>";
        } else {
            resultat += "<tr class='separationTr'>";
        }
        resultat += "<td style='padding: 0px;'></td>";
        resultat += "<td style='padding: 0px;'></td>";
        resultat += "<td class='separationTd' colspan='4' style='background-color:#FFDA8C;padding-top: 1px'></td>";
        resultat += "</tr>";
        return resultat;
    }
    function removeUserHasEnv(img) {
        var id = $(img).closest('.containId').attr('id');
        var nomEnvironnementA_Supprimer = $(img).closest('.containId').children("#env").children(".valueOf").html().trim();
        if (nomEnvironnementA_Supprimer === "VERSIONNING") {
            alert("L'environnement VERSIONNING ne peut pas être supprimé");
        } else {
            $("#loadingAnimationConteneur").show();
            var userModif = id.replace(new RegExp("\\.", "g"), "_");
            var tr = $("#bodyId").find("." + userModif);
            var firstTr = $("#bodyId tr[id='" + id + "']:first");
            //alert($(firstTr).html());
            //alert("tr\n\n" + $(tr).html());
            var toDisplay = $(img).closest('.containId').find('#env span').text();
            var r = confirm("Supprimer   " + toDisplay + "??");
            if (r === true)
            {
                var contextPath = "<%=request.getContextPath()%>";
                $.ajax({
                    type: "POST",
                    url: contextPath + '/UserHasEnvironnementServlet?id=' + id + '&envName=' + toDisplay + '&typeModf=supp',
                    success: function(response) {
                        var objetJson = JSON.parse(response);
                        var TR_Affichee = objetJson["TR_Affichee"];
                        //alert("tr\n\n" + TR_Affichee);
                        var TR_Ajout = objetJson["TR_Ajout"];
                        //remplacer la premiere tr du user courant par <response>
                        $(firstTr).replaceWith("<tr id='reponse' ></tr>");
                        //remplacer toutes les autres tr du user courant
                        $("#bodyId tr[id='" + id + "']").each(function() {
                            //alert("ok");
                            $(this).remove();
                        });

                        //$("#reponse").replaceWith(TR_Affichee + genererSeparationTr(id));
                        $("#reponse").replaceWith(TR_Affichee);
                        $("#loadingAnimationConteneur").hide();
                        var trAjout = $('.add');

                        $("#separateur_" + userModif).each(function() {
                            $(this).remove();
                        });



                        //var lastSeparator = $('.add + .separationTr');
                        //$(lastSeparator).remove();
                        if ($('.add').html() !== null) {
                            trAjout.replaceWith(TR_Ajout);
                            chargerTrAdd();
                        } else {
                            $("#bodyId").append(TR_Ajout);
                        }
                    },
                    error: function(e) {
                        //alert('Error: ' + e);
                    }
                });
            }
        }
    }
    function addUserHasEnv(img) {
        RemoveErrorForm(".roundCornerTableWithButtons");
        if ($('.roundCornerTableWithButtons .aff input').size() === 1) {
            $('.roundCornerTableWithButtons .aff input').addClass('inputError');
            scrollto(".roundCornerTableWithButtons .aff .inputError");
        } else {
            validerUserHasEnvironnement();
            if ($('.roundCornerTableWithButtons .inputError')[0]) {
                scrollto(".roundCornerTableWithButtons .add .inputError");
            } else {
                $("#loadingAnimationConteneur").show();
                var user = $('.roundCornerTableWithButtons .add .user #user').val().trim();
                var envName = $('.roundCornerTableWithButtons .add .env option:selected').val().trim();
                var browserLogin = $('.roundCornerTableWithButtons .add #login').val().trim();
                var browserPass = $('.roundCornerTableWithButtons .add #pass').val().trim();
                var userModif = user.replace(new RegExp("\\.", "g"), "_");
                var tr = $("#bodyId").find("." + userModif);
                var firstTr = $("#bodyId tr[id='" + user + "']:first");

                browserPass = browserPass.replace("&", "ET_COMMERCIAL");
                browserPass = browserPass.replace("#", "DIESE");
                browserPass = browserPass.replace("+", "PLUS");
                browserPass = browserPass.replace("$", "DOLLAR");

                $.ajax({
                    type: "POST",
                    url: contextPath + '/UserHasEnvironnementServlet?user=' + user + '&envName=' + envName + '&browserLogin=' + browserLogin + '&browserPass=' + browserPass + '&typeModf=add',
                    success: function(response) {
                        var objetJson = JSON.parse(response);
                        var TR_Affichee = objetJson["TR_Affichee"];
                        var TR_Ajout = objetJson["TR_Ajout"];
                        //remplacer la premiere tr du user courant par <response>
                        $(firstTr).replaceWith("<tr id='reponse' ></tr>");
                        //remplacer toutes les autres tr du user courant
                        $("#bodyId tr[id='" + user + "']").each(function() {
                            //alert("ok");
                            $(this).remove();
                        });

                        /*
                         $(tr).replaceWith("<tr id='reponse' ></tr>");
                         $("." + userModif).each(function() {
                         $(this).remove();
                         });
                         */
                        $("#reponse").replaceWith(TR_Affichee);
                        $("#loadingAnimationConteneur").hide();
                        var trAjout = $('.add');
                        var lastSeparator = $('.add + .separationTr');
                        lastSeparator.remove();
                        if ($('.add').html() !== null) {
                            trAjout.replaceWith(TR_Ajout);
                        } else {
                            $("#bodyId").append(TR_Ajout);
                        }
                        $("#ParamtablePereUserHasEnvironnement .ToAdd #user option:selected").each(function() {
                            selectEnvForSelectedUser($(this).text());
                        });
                        $("#loadingAnimationConteneur").hide();
                    },
                    error: function(e) {
                        //alert('Error: ' + e);
                    }
                });
            }
        }
    }
    function chargerTrAdd() {
        $("#loadingAnimationConteneur").show();
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "POST",
            url: contextPath + '/UserHasEnvironnementServlet?typeModf=initAdd',
            success: function(response) {
                var trAjout = $('.add');
                if ($('.add').html() !== null) {
                    trAjout.replaceWith(response);
                } else {
                    $("#bodyId").append(response);
                }
                $("#ParamtablePereUserHasEnvironnement .ToAdd #user").change(function() {
                    $("#ParamtablePereUserHasEnvironnement .ToAdd #user option:selected").each(function() {
                        selectEnvForSelectedUser($(this).text());
                    });
                }).trigger("change");
                $("#loadingAnimationConteneur").hide();
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
    function selectEnvForSelectedUser(selectedUser) {
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "POST",
            url: contextPath + '/UserHasEnvironnementServlet?selectedUser=' + selectedUser + '&typeModf=affListEnvByUser',
            success: function(response) {
                $(".roundCornerTableWithButtons .add #env").html(response);
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
    function editUserHasEnv(userLogin, envName, browserLogin, browserPass)
    {
        browserPass = browserPass.replace("&", "ET_COMMERCIAL");
        browserPass = browserPass.replace("#", "DIESE");
        browserPass = browserPass.replace("+", "PLUS");
        browserPass = browserPass.replace("$", "DOLLAR");
        var contextPath = "<%=request.getContextPath()%>";
        $.ajax({
            type: "POST",
            url: contextPath + '/UserHasEnvironnementServlet?user=' + userLogin + '&browserLogin=' + browserLogin + '&browserPass=' + browserPass + '&envName=' + envName + '&typeModf=edit',
            success: function(response) {
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
</script>
<div class="centre">
    <div class="titre">Gestion du profil d'utilisateur</div><br>
    <div id="login_"/>
    <div id="pass_"/>
</div>
<%----%>
<table class="roundCornerTableWithButtons" id="ParamtablePereUserHasEnvironnement">
    <thead>
        <tr>
            <th class="imageContenu" ></th>
            <th class="imageContenu"></th>
            <th class="tdContenu">Login utilisateur</th>
            <th class="tdContenu">Environnements</th>
            <th class="tdContenu">Login T24</th>
            <th class="tdContenu">Pass T24</th>
        </tr>
    </thead>
    <tbody id="bodyId">
        <%-- affichage existants--%>
        <%  Map<String, EnvironnementDTO> auxDTOMap = null;
            int i = 0;
            Set<String> keys = usersGroupMap.keySet();
            Iterator it = keys.iterator();
            for (Map.Entry<String, Map<String, EnvironnementDTO>> entry : envDTOMap.entrySet()) {
                auxDTOMap = entry.getValue();
                boolean lire = false;
                if ((isAdmin && entry.getKey().startsWith("OVTOOLS")) || entry.getKey().equals(connectedUser)) {
                    lire = true;
                }
                if ((entry.getKey().equals("anonymousUser")) || envDTOMap.get(connectedUser).isEmpty()) {
                    lire = false;
                }

                /*
                 if (!isSuperAdmin && !entry.getKey().equals(connectedUser) ||) {
                 continue;
                 }
                 */
                if (lire) {
                    for (Map.Entry<String, EnvironnementDTO> en : auxDTOMap.entrySet()) {
                        out.print("<tr id='" + entry.getKey() + "' class='containId aff " + entry.getKey().replaceAll("\\.", "_") + "'>");
                        out.print("<td class='imageContenu'></td>");
                        if (en.getKey().equals("VERSIONNING")) {
                            out.print("<td class='imageContenu'><img src='images/remove-disabled.png' class='imageRemove'  id='delBtn' name='delBtn'/></td>");
                        } else {
                            out.print("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);' id='delBtn' name='delBtn'/></td>");
                        }
                        //out.print("<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeUserHasEnv(this);' id='delBtn' name='delBtn'/></td>");
                        if (i == 0) {
                            out.print("<td class='tdContenu' id='user' style='vertical-align: middle' rowspan='" + auxDTOMap.size() + "'><span class='valueOf'> " + entry.getKey() + "</span></td>");
                        }
                        out.print("<td class='tdContenu' id='env'><span class='valueOf'>" + en.getKey() + " </span></td>");
                        out.print("<td class='tdContenu' id='login'><span class='valueOf userT24Login'>" + en.getValue().getBrowserUser() + " </span></td>");
                        out.print("<td class='tdContenu' id='pass'><span class='valueOf userT24Password'>" + en.getValue().getBrowserPassword() + " </span></td>");
                        out.print("</tr>");
                        i++;
                    }
                    out.print("<tr class='separationTr' id='separateur_" + entry.getKey().replaceAll("\\.", "_") + "'>"
                            + "<td style='padding: 0px;'></td>"
                            + "<td style='padding: 0px;'></td>"
                            + "<td class='separationTd' colspan='4' style='background-color:#FFDA8C;padding-top: 1px' ></td>"
                            + "</tr>");
                }
                i = 0;
            }
            out.print("");
        %>
        <%-- ajouts new--%>
        <tr class="add" id="trAjout">
        </tr>
    </tbody>
</table>
<div id="loadingAnimationConteneur">
    <div class='center'>
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>
</div>
<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        $("#loadingAnimationConteneur").hide();
        chargerTrAdd();
        //edition dans document ready
        $(".userT24Login").editable(function(value, settings) {
            var login = $(this).closest("tr").attr("id");
            var envName = $(this).closest("tr").children("#env").children("span").html().trim();
            var pass = $(this).closest("tr").children("#pass").children("span").html().trim();
            editUserHasEnv(login, envName, value, pass);
            return (value);
        }, {
            submit: 'OK',
            style: 'inherit'
        });
        $(".userT24Password").editable(function(value, settings) {
            var login = $(this).closest("tr").attr("id");
            var envName = $(this).closest("tr").children("#env").children("span").html().trim();
            var browserPassword = $(this).closest("tr").children("#login").children("span").html().trim();
            var pass = value;
            editUserHasEnv(login, envName, browserPassword, pass);
            return (value);
        }, {
            submit: 'OK',
            tooltip: '-'
        });
        //hide delete button for versionning environnement
        /*  $("tr").closest('.containId').each(function() {
         var envName = $(this).closest("tr").children("#env").children("span").html().trim();
         if (envName === 'VERSIONNING')
         {
         $(this).closest("tr").children(".imageContenu").children(".imageRemove").hide();
         }
         });*/
    });
</script>