<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>



<html:form action="/ComparaisonObjetsForm" styleId="ComparaisonObjetsForm">
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Comparaison d'objets</p></td>            
        </tr>
        <tr>
            <td class="tdDemi"><p class="titres">Environnement source 1</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementSourceName1" styleId="environnementSourceName1" value="environnementList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationComparaisonObjetsForm" property="environnementList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>
        <tr id="trDepotVersionning">
            <td class="tdDemi"><p class="titres">Dépôt de versionning 1</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="selectedDepot" styleId="selectedDepot" value="depotList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationComparaisonObjetsForm" property="depotList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>

        <tr>
            <td class="tdDemi"><p class="titres">Environnement source 2</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementSourceName2" styleId="environnementSourceName2" value="environnementList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationComparaisonObjetsForm" property="environnementList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>

        <tr id="trDepotVersionning2">
            <td class="tdDemi"><p class="titres">Dépôt de versionning 2</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="selectedDepot2" styleId="selectedDepot2" value="depotList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationComparaisonObjetsForm" property="depotList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>

        <tr>
            <td class="tdDemi"><p class="titres">Nom du pack</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:text styleId="nomPack"  onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" property="nomPack" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>  

        <tr>
            <td class="tdDemi"><p class="titres">Trier l'export par type</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <span id="OUI" class="notSelected" onclick="selectTri('OUI');"><input type= "radio" name="tri" value="OUI" onclick="selectTri('OUI');" id="CB_OUI">OUI</span>
                    <span  style="margin-left: 35px" id="NON" class="notSelected" onclick="selectTri('NON');"><input type= "radio" name="tri" value="NON" onclick="selectTri('NON');" id="CB_NON">NON</span>
                </p>
            </td>
        </tr>


        <tr>
            <td colspan="2">
                <p class="bouton">
                    <a href="#?w=630" rel="popup_objectList" class="poplight">                        
                        <html:button property="listeObjets" value="Liste des objets" styleClass="boutonValider" />
                    </a>
                </p>
            </td>
        </tr>
    </table>






    <div id="popup_objectList" class="popup_block">
        <p class="titrePopup">Introduire la liste d'objets</p>
        <div class="center">
            <html:textarea styleId="textAreaObjectList" property="textAreaObjectList"/>            
        </div>
        <script>
                        var contextPath = "<%=request.getContextPath()%>";
        </script>
        <p class="conteneurBouton">
            <input type="button" class="boutonValider"  value="Former le pack" onclick="validerChampsComparaisonObjets(contextPath);" />
        </p>
    </div>    
</html:form>
<script>
                        $(document).ready(function() {
                            selectMenu('menuT24');
                            $("#NON").click();
                            //$("#environnementSourceName2 option[value='VERSIONNING']").remove();

                            $('#environnementSourceName1').change(function() {
                                if ($(this).val() === 'VERSIONNING') {
                                    $("#trDepotVersionning").show();
                                } else if ($(this).val() === '') {
                                    $("#trDepotVersionning").hide();
                                } else {
                                    $("#trDepotVersionning").hide();
                                }
                            });

                            $('#environnementSourceName2').change(function() {
                                if ($(this).val() === 'VERSIONNING') {
                                    $("#trDepotVersionning2").show();
                                } else if ($(this).val() === '') {
                                    $("#trDepotVersionning2").hide();
                                } else {
                                    $("#trDepotVersionning2").hide();
                                }
                            });



                            $('#environnementSourceName2').change(function() {
                                //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
                                if ($(this).val() !== '') {
                                    if ($(this).val() === $('#environnementSourceName1').val() && $(this).val() !== "VERSIONNING") {
                                        $(this).val("");
                                    }
                                }
                            });

                            $('#environnementSourceName1').change(function() {
                                //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
                                if ($(this).val() !== '') {
                                    if ($(this).val() === $('#environnementSourceName2').val() && $(this).val() !== "VERSIONNING") {
                                        $(this).val("");
                                    }
                                }
                            });

                            $('#selectedDepot').change(function() {
                                //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
                                if ($(this).val() !== '') {
                                    if ($(this).val() === $('#selectedDepot2').val()) {
                                        $(this).val("");
                                    }
                                }
                            });

                            $('#selectedDepot2').change(function() {
                                //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
                                if ($(this).val() !== '') {
                                    if ($(this).val() === $('#selectedDepot').val()) {
                                        $(this).val("");
                                    }
                                }
                            });

                        });


</script>
