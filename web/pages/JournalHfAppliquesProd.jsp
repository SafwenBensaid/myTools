<br><br>
<div class='grandTitre'>
    Journal des Hotfix appliqués sur PROD
    <br>
</div>
<div id='conteneurAnimation'>
    <div id="loadingAnimationConteneur" class="center">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>
</div>

<br><br><br>    

<table class="tablePrincipale">
    <tbody>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Date de début:</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type="text" class="datePicker" id="date_depart"/>
                </p>
            </td>
        </tr>
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Date de fin:</p></td>
            <td  class="tdDemi">
                <p class="contenu">                       
                    <input type="text"  class="datePicker" id="date_fin"/>
                </p>
            </td>
        </tr>

        <tr class="groupe1" id="checkBox">
            <td colspan="2">
                <table  id="listePacksDetailsTable" border="0">
                    <tbody>
                        <tr class="groupe1">
                            <td  class="tdDemi"><p class="titres">Type: </p></td>
                            <td style="text-align: left" class="tdDemi textClass" id="tdTypeRequete">
                                <span  id="CuratifsUniquement" class="notSelected" onclick="selectOption('CuratifsUniquement', $(this).parent());">
                                    <input type= "radio"   name="typeRequete" value="CuratifsUniquement" onclick="selectOption('PalliatifsUniquement', $(this).parent().parent());" id="CB_CuratifsUniquement">
                                    Curatifs
                                </span> 
                                <span  id="PalliatifsUniquement" class="notSelected" onclick="selectOption('PalliatifsUniquement', $(this).parent());">
                                    <input type= "radio" name="typeRequete" value="PalliatifsUniquement" onclick="selectOption('PalliatifsUniquement', $(this).parent().parent());" id="CB_PalliatifsUniquement">
                                    Palliatifs
                                </span>
                            </td>    
                        </tr>
                    </tbody>
                </table>
            </td> 
        </tr>

        <tr class="groupe1" id="trBoutonListeObjets"  >
            <td colspan="2" style="text-align: center">                                       
                <input type="button" name="Valider" value="Valider" class="boutonValider" onclick="loadAllTicketsTable();">                    
            </td>
        </tr>
    </tbody>
</table>
<br>
<br>
<center>
    <div id="resultatHotfix" style="max-width: 900px;overflow: auto;overflow-y: hidden;text-align: center"/>
</center>

<SCRIPT type="text/javascript" src="javascript/common.js"></SCRIPT>
<SCRIPT language="Javascript">

                                    function selectOption(id, currentTd) {
                                        ($(currentTd).children('span')).each(function() {
                                            $(this).attr("class", "notSelected");
                                        });
                                        $(currentTd).children('#' + id).attr("class", "selected");
                                        $(currentTd).children('span').children('#CB_' + id).attr('checked', true);
                                    }

                                    $('#date_depart').datetimepicker({
                                        formatTime: 'H:i',
                                        formatDate: 'd-m-Y',
                                        defaultTime: '00:00',
                                        format: 'd-m-Y H:i',
                                        step: 10
                                    });
                                    $('#date_fin').datetimepicker({
                                        formatTime: 'H:i',
                                        formatDate: 'd-m-Y',
                                        defaultTime: '23:50',
                                        format: 'd-m-Y H:i',
                                        step: 10
                                    });

                                    $(document).ready(function() {
                                        $("#loadingAnimationConteneur").hide();
                                        var menuItem = location.search.substring(1).split("&")[1].split("=")[1];
                                        selectMenu(menuItem);
                                        if (menuItem == 'menuT24') {
                                            $("#checkBox").show();
                                            $("#CuratifsUniquement").click();
                                        } else {
                                            $("#checkBox").hide();
                                        }
                                    });

                                    function loadAllTicketsTable() {
                                        var contextPath = "<%=request.getContextPath()%>";
                                        var projet = location.search.substring(1).split("&")[0].split("=")[1];
                                        var date_depart = $('#date_depart').val();
                                        var date_fin = $('#date_fin').val();
                                        var typeRequete = "";
                                        if (projet == 'LIVRAISONS_T24') {
                                            typeRequete = $('input[type=radio][name=typeRequete]:checked').attr('value');
                                        }
                                        var dataContent = "projet=" + projet + "&date_depart=" + date_depart + "&date_fin=" + date_fin + "&typeRequete=" + typeRequete;
                                        $("#loadingAnimationConteneur").show();
                                        $.ajax({
                                            type: "POST",
                                            url: contextPath + "/HistoriqueHotfixProdServlet",
                                            data: dataContent,
                                            success: function(response) {
                                                $("#loadingAnimationConteneur").hide();
                                                $("#resultatHotfix").html(response);
                                            },
                                            error: function(e) {
                                                //alert('Error: ' + e);
                                            }
                                        });
                                    }

</SCRIPT>
