<%-- 
    Document   : AdministrationTypeRegle
    Created on : 10 avr. 2014, 09:43:28
    Author     : Imen
--%>
<%@page import="entitiesMysql.TypesRegle"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Map"%>
<%@page import="tools.Configuration"%>
<script>
    function addTRegle(img) {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        var cle = $('.roundCornerTableWithButtons .add #cle').val().toUpperCase().replace(/ /g, ".");
        var regle = $('.roundCornerTableWithButtons .add #regle').val();
        var nature = $('.roundCornerTableWithButtons .add #nature option:selected').val();
        var trToBeAdded = "<tr id='" + cle + "' class='containId " + cle + " " + nature + "'><td class='imageContenu'></td><td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeTRegle(this);'></td><td class='tdContenu' id='cle'><span class='valueOf'>" + cle + "</span></td><td class='tdContenu' id='regle'><span class='valueOf' title='" + regle + "'>" + regle + "</span></td><td class='tdContenu' id='nature'><span class='valueOf' title='" + nature + "'>" + nature + "</span></td></tr>";
        //var trAjout = $(img).closest("tr")[0].outerHTML;
        var lastTr = $("#ParamtablePereGestionTypeRegle tbody").find("." + nature).last();
        var lastTrHtml = lastTr[0].outerHTML;
        $.ajax({
            type: "POST",
            url: contextPath + '/AdministrationTypesReglesServlet?cle=' + cle + '&regle=' + regle + '&nature=' + nature + '&typeModf=add',
            success: function() {
                lastTr.replaceWith(lastTrHtml + trToBeAdded);
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

    function editTRegle(cle, regle, nature)
    {
        var contextPath = "<%=request.getContextPath()%>";
        //   var cle = $("img").closest('.containId').attr('id');
        $.ajax({
            type: "POST",
            url: contextPath + '/AdministrationTypesReglesServlet?cle=' + cle + '&regle=' + regle + '&nature=' + nature + '&typeModf=edit',
            success: function() {
                window.location.reload(true);
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }
    function removeTRegle(img) {
        var cle = $(img).closest('.containId').find('#cle').text();
        var rp = confirm("Supprimer " + cle + "?");
        if (rp === true)
        {
            $.ajax({
                type: "POST",
                url: contextPath + '/AdministrationTypesReglesServlet?cle=' + cle + '&typeModf=supp',
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
            $("tr").closest('.containId').children('#regle').children('.valueOf').editable(function(value, settings) {
                var cle = $(this).closest('.containId').attr('id');
                var regle = value;
                var nature = $(this).closest('.containId').children('#nature').children('.valueOf').html().trim();
                editTRegle(cle, value, nature);
                return (value);
            }, {
                submit: 'OK'
            });
            $("tr").closest('.containId').children("#nature").children(".valueOf").editable(function(value, settings) {
                var cle = $(this).closest('.containId').children("#cle").children(".valueOf").html().trim();
                var regle = $(this).closest('.containId').children("#regle").children(".valueOf").html().trim();
                var nature = value.toUpperCase().replace(/ /g, "_");
                editTRegle(cle, regle, nature);
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
        selectMenu('menuT24');
    });

</script>
<div class="centre">
    <div class="titre">Gestion des contraintes d'intégrité</div><br>
</div>
<table class="roundCornerTableWithButtons TypeRegleTab" id="ParamtablePereGestionTypeRegle">
    <thead>
        <tr>
            <th class="imageContenu" ></th>
            <th class="imageContenu"></th>
            <th class="tdContenu">Clé </th>
            <th class="tdContenu">Règle</th>
            <th class="tdContenu">Nature </th>
        </tr>
    </thead>
    <tbody id="bodyId" class="bodyClass">
        <tr class="titre">
            <td colspan="2">
            </td>
            <td colspan="3" class="separateur2" style="font-weight: bold; padding-left: 15px">
                Types T24
            </td>
        </tr>
        <%
            for (Map.Entry<String, TypesRegle> fctEntry : Configuration.alltypesReglesMap.entrySet()) {
                if (fctEntry.getValue().getNature().equals("TYPE_T24")) {
                    out.print("<tr id='" + fctEntry.getKey() + "' class='containId " + fctEntry.getKey() + " " + fctEntry.getValue().getNature() + "'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeTRegle(this);'/></td>"
                            + "<td class='tdContenu' id='cle'><span class='valueOf'>" + fctEntry.getValue().getCle() + "</span></td>"
                            + "<td class='tdContenu' id='regle'><span class='valueOf' >" + fctEntry.getValue().getRegle() + "</span></td>"
                            + "<td class='tdContenu' id='nature'><span class='valueOf'>" + fctEntry.getValue().getNature() + "</span></td>"
                            + "</tr>");
                }
            }
        %>
        <tr class="titre">
            <td colspan="2">
            </td>
            <td colspan="3" class="separateur2" style="font-weight: bold; padding-left: 15px">
                Observations
            </td>
        </tr>
        <%
            for (Map.Entry<String, TypesRegle> fctEntry : Configuration.alltypesReglesMap.entrySet()) {
                if (fctEntry.getValue().getNature().equals("OBSERVATION")) {
                    out.print("<tr id='" + fctEntry.getKey() + "' class='containId " + fctEntry.getKey() + " " + fctEntry.getValue().getNature() + "'>"
                            + " <td class='imageContenu'></td>"
                            + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='removeTRegle(this);'/></td>"
                            + "<td class='tdContenu' id='cle'><span class='valueOf'>" + fctEntry.getValue().getCle() + "</span></td>"
                            + "<td class='tdContenu' id='regle'><span class='valueOf' >" + fctEntry.getValue().getRegle() + "</span></td>"
                            + "<td class='tdContenu' id='nature'><span class='valueOf'>" + fctEntry.getValue().getNature() + "</span></td>"
                            + "</tr>");
                }
            }
            out.print("<tr class='add'>"
                    + "<td class='imageContenu'><img src='images/add.png' class='imageAdd' onclick='addTRegle(this)'></td>"
                    + "<td class='imageContenu'><img src='images/remove.png' class='imageRemove' onclick='reinitialiserChampsAjout(this);'></td>"
                    + "<td><input class='inputContenu' id='cle' type='text'></td>"
                    + "<td><input class='inputContenu' id='regle' type='text'</td>"
                    + "<td><select name='type' id='nature'>"
                    + "<option></option>");

            out.print("<option>TYPE_T24</option>");
            out.print("<option>OBSERVATION</option>");

            out.print("</select></td>"
                    + "</tr>");
        %>
    </tbody>
</table>
<div id="loadingAnimationConteneur">
    <div class="center">
        <%--@include file="/pages/loadingAnimation.jsp" --%>
    </div>
</div>

