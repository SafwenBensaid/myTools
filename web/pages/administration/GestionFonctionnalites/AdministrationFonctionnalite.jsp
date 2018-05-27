<%@page import="java.util.Map.Entry"%>
<%@page import="entitiesMysql.Fonctionalite"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@page import="java.util.*"%>
<%@page import="tools.Configuration"%>
<%@page language="java"%>
<%@taglib uri="http://struts.apache.org/tags-html" prefix="html" %>

<script>
    function addFonctionnalite(img) {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        var nomFonct = $('.roundCornerTableWithButtons .add #nom').val().toUpperCase().replace(/ /g, "_");
        var descriptionFonct = $('.roundCornerTableWithButtons .add #description').val();
        var typeFonct = $('.roundCornerTableWithButtons .add #type option:selected').val();
        var trToBeAdded = "<tr id='" + nomFonct + "' class='containId " + nomFonct + " " + typeFonct + "'><td class='imageContenu'></td><td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeFonctionnalites(this);'></td><td class='tdContenu' id='nom'><span class='valueOf'>" + nomFonct + "</span></td>    <td class='tdContenu' id='description'><span class='valueOf' title='" + descriptionFonct + "'>" + descriptionFonct + "</span></td><td class='tdContenu' id='type'><span class='valueOf' title='" + typeFonct + "'>" + typeFonct + "</span></td></tr>";
        //var trAjout = $(img).closest("tr")[0].outerHTML;
        var lastTr = $("#ParamtablePereGestionFonctionnalites tbody").find("." + typeFonct).last();
        var lastTrHtml = lastTr[0].outerHTML;
        $.ajax({
            type: "POST",
            url: contextPath + '/AdministrationFonctionnalitesServlet?nomFonct=' + nomFonct + '&descriptionFonct=' + descriptionFonct + '&typeFonct=' + typeFonct + '&typeModf=add',
            success: function() {
                lastTr.replaceWith(lastTrHtml + trToBeAdded);
                //$('tr.add').replaceWith(trToBeAdded);
                //$("#bodyId").append(trAjout);
                $("#loadingAnimationConteneur").hide();
                loadEditable();
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
        $('.add input').val('');
        $('.add select').val('');

    }
    function editfonctionnalite(nom, description, type)
    {
        var contextPath = "<%=request.getContextPath()%>";
        var id = $("img").closest('.containId').attr('id');
        $.ajax({
            type: "POST",
            url: contextPath + '/AdministrationFonctionnalitesServlet?id=' + id + '&nom=' + nom + '&description=' + description + '&type=' + type + '&typeModf=edit',
            success: function() {
                window.location.reload(true);
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
    function removeFonctionnalites(img) {
        var fonct = $(img).closest('.containId').find('#nom').text();
        var rp = confirm("Supprimer " + fonct + "?");
        if (rp === true)
        {
            $.ajax({
                type: "POST",
                url: contextPath + '/AdministrationFonctionnalitesServlet?fonct=' + fonct + '&typeModf=supp',
                success: function() {
                    $(img).closest('.containId').remove();
                    $("#loadingAnimationConteneur").hide();
                },
                error: function(e) {
                    //alert('Error: ' + e);
                }
            });
        }
    }
    function reinitialiserChampsAjout(img) {
        RemoveErrorForm(".roundCornerTableWithButtons");
        $('.roundCornerTableWithButtons .ToAdd').each(function() {
            $(this).removeClass('FormAddError');
        });
        $(".roundCornerTableWithButtons .add input").each(function() {
            $(this).val('');
            $(this).removeClass("inputError");
        });
        $('.roundCornerTableWithButtons .add select').removeClass("inputError");
        $('.roundCornerTableWithButtons .add select').val('');
        $('.roundCornerTableWithButtons .add').removeClass('formError');
        $('.roundCornerTableWithButtons .add').addClass('SimplPack');
    }

    function loadEditable()
    {
        try {
            $("tr").closest('.containId').children('#description').children('.valueOf').editable(function(value, settings) {
                var nom = $(this).closest('.containId').attr('id');
                var type = $(this).closest('.containId').children('#type').children('.valueOf').html().trim();
                var description = value;
                editfonctionnalite(nom, description, type);
                contenuDescription = value;
                return (value);
            }, {
                submit: 'OK'
            });
            $("tr").closest('.containId').children("#type").children(".valueOf").editable(function(value, settings) {
                var nom = $(this).closest('.containId').children("#nom").children(".valueOf").html().trim();
                var description = $(this).closest('.containId').children("#description").children(".valueOf").html().trim();
                var type = $(this).find("input").val().toUpperCase().replace(/ /g, "_");
                editfonctionnalite(nom, description, type);
                return value.toUpperCase().replace(/ /g, "_");
            }, {
                submit: 'OK'
            })
        } catch (e) {
            postMessage('Error occured in XMLHttpRequest: ' + xmlhttp.statusText + '  ReadyState: ' + xmlhttp.readyState + ' Status:' + xmlhttp.status + ' E: ' + e + ' Msg:' + e.message);
        }
    }
    $(document).ready(function() {
        $("#loadingAnimationConteneur").hide();
        loadEditable();
    });
</script>
<div class="centre">
    <div class="titre">Gestion des Fonctionnalités</div><br>
</div>
<table class="roundCornerTableWithButtons" id="ParamtablePereGestionFonctionnalites">
    <thead>
        <tr>
            <th class="imageContenu" ></th>
            <th class="imageContenu"></th>
            <th class="tdContenu">Fonctionnalité </th>
            <th class="tdContenu">Description </th>
            <th class="tdContenu">Type </th>
        </tr>
    </thead>
    <tbody id="bodyId" class="bodyClass">
        <tr class="titre">
            <td colspan="2">

            </td>
            <td colspan="3" class="separateur">
                Menus
            </td>
        </tr>
        <%
            for (Map.Entry<String, Fonctionalite> fctEntry : Configuration.allFonctionalitesMap.entrySet()) {
                if (fctEntry.getValue().getType().equals("MENU")) {
                    out.print("<tr id='" + fctEntry.getKey() + "' class='containId " + fctEntry.getKey() + " " + fctEntry.getValue().getType() + "'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeFonctionnalites(this);'/></td>"
                            + "<td class='tdContenu' id='nom'><span class='valueOf'>" + fctEntry.getValue().getName() + "</span></td>"
                            + "<td class='tdContenu' id='description'><span class='valueOf' >" + fctEntry.getValue().getDescription() + "</span></td>"
                            + "<td class='tdContenu' id='type'><span class='valueOf'>" + fctEntry.getValue().getType() + "</span></td>"
                            + "</tr>");
                }
            }
        %>
        <tr class="titre">
            <td colspan="2">

            </td>
            <td colspan="3" class="separateur">
                Sous Menus
            </td>
        </tr>
        <%
            for (Map.Entry<String, Fonctionalite> fctEntry : Configuration.allFonctionalitesMap.entrySet()) {
                if (fctEntry.getValue().getType().equals("SOUS_MENU")) {
                    out.print("<tr id='" + fctEntry.getKey() + "' class='containId " + fctEntry.getKey() + " " + fctEntry.getValue().getType() + "'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeFonctionnalites(this);'/></td>"
                            + "<td class='tdContenu' id='nom'><span class='valueOf'>" + fctEntry.getValue().getName() + "</span></td>"
                            + "<td class='tdContenu' id='description'><span class='valueOf' title='" + fctEntry.getValue().getDescription() + "'>" + fctEntry.getValue().getDescription() + "</span></td>"
                            + "<td class='tdContenu' id='type'><span class='valueOf'>" + fctEntry.getValue().getType() + "</span></td>"
                            + "</tr>");
                }
            }
        %>
        <tr class="titre">
            <td colspan="2">

            </td>
            <td colspan="3" class="separateur">
                Boutons
            </td>
        </tr>
        <%
            for (Map.Entry<String, Fonctionalite> fctEntry : Configuration.allFonctionalitesMap.entrySet()) {
                if (fctEntry.getValue().getType().equals("BOUTON")) {
                    out.print("<tr id='" + fctEntry.getKey() + "' class='containId " + fctEntry.getKey() + " " + fctEntry.getValue().getType() + "'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeFonctionnalites(this);'/></td>"
                            + "<td class='tdContenu' id='nom'><span class='valueOf'>" + fctEntry.getValue().getName() + "</span></td>"
                            + "<td class='tdContenu' id='description'><span class='valueOf'>" + fctEntry.getValue().getDescription() + "</span></td>"
                            + "<td class='tdContenu' id='type'><span class='valueOf'>" + fctEntry.getValue().getType() + "</span></td>"
                            + "</tr>");
                }
            }
            out.print("<tr class='add'>"
                    + "<td class='imageContenu'><img src='images/add.png' class='imageAdd' onclick='addFonctionnalite(this);'></td>"
                    + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='reinitialiserChampsAjout(this);'></td>"
                    + "<td><input class='inputContenu' id='nom' type='text'></td>"
                    + "<td><input class='inputContenu' id='description' type='text'</td>"
                    + "<td><select name='type' id='type'>"
                    + "<option></option>");

            for (Map.Entry<String, List<Fonctionalite>> fctEntry : Configuration.allFonctionalitesMapOrdredByType.entrySet()) {
                out.print("<option>" + fctEntry.getKey() + "</option>");
            }
            out.print("</select></td>"
                    + "</tr>");
        %>
    </tbody>
</table>
<div id="loadingAnimationConteneur">
    <div class="center">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>
</div>
<script>
    $(document).ready(function() {
        selectMenu('menuT24');
    });
</script>