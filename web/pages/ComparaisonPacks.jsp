<%@page import="tools.Configuration"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>



<html:form action="/VerificationDeltaDownloadPackForm" styleId="VerificationDeltaDownloadPackForm">
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Comparaison de packs</p></td>            
        </tr>
        <tr>
            <td class="tdDemi"><p class="titres">Environnement source 1</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementSourceName" styleId="environnementSourceName" value="environnementList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="VerificationDeltaForm" property="environnementList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>
        <tr id="trDossierSourcePath">
            <td class="tdDemi"><p class="titres" id="racineSrcPath"></p></td>
            <td class="tdDemi">
                <p class="contenu">                    
                    <html:text styleId="dossierSourcePath" property="dossierSourcePath"  style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>
        <tr id="trDossierSource">
            <td class="tdDemi"><p class="titres" id="racineSrc"></p></td>
            <td class="tdDemi">
                <p class="contenu">                    
                    <html:text styleId="dossierSource" property="dossierSource" onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr>
        <tr>
            <td class="tdDemi"><p class="titres">Environnement source 2</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementDestinationName" styleId="environnementDestinationName" value="environnementList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="VerificationDeltaForm" property="environnementList" />
                    </html:select>                                   
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
                <p class="conteneurBouton">
                    <script>
                        var contextPath = "<%=request.getContextPath()%>";
                    </script>
                    <input type="button" class="boutonValider boutonValiderSubmit"  value="Confirmer l'action" onclick="validerChampsComparaisonDelta(contextPath);" />
                </p>
                </p>
            </td>
        </tr>
    </table>   
</html:form>

<script>
                        $(document).ready(function() {
                            selectMenu('menuT24');
                            $("#NON").click();
                            $("#environnementDestinationName option[value='VERSIONNING']").remove();

                            $('#environnementSourceName').change(function() {
                                if ($(this).val() === 'VERSIONNING') {
                                    $("#racineSrc").html("Nom du Pack");
                                    $("#racineSrcPath").html("Chemin absolu du Pack : /");
                                    $("#dossierSource").val("TAF-");
                                    $("#dossierSourcePath").val("");
                                    $("#trDossierSource").show();
                                    $("#trDepotVersionning").show();
                                    $("#trDossierSourcePath").show();
                                } else if ($(this).val() === '') {
                                    $("#racineSrc").html("");
                                    $("#racineSrcPath").html("");
                                    $("#trDossierSource").hide();
                                    $("#trDossierSourcePath").hide();
                                } else {
                                    $("#racineSrc").html("Nom du Pack");
                                    $("#racineSrcPath").html("Chemin absolu du Pack : /");
                                    $("#dossierSource").val("TAF-");
                                    $("#dossierSourcePath").val("");
                                    $("#trDossierSource").show();
                                    $("#trDossierSourcePath").hide();
                                }
                                //si l'utilisateur choist la même valeur que l'autre liste, set la valeur à vide
                                if ($(this).val() !== '') {
                                    if ($(this).val() === $('#environnementDestinationName').val()) {
                                        $(this).val("");
                                        $("#racineSrc").html("");
                                        $("#racineSrcPath").html("");
                                        $("#trDossierSource").hide();
                                        $("#trDossierSourcePath").hide();
                                    }
                                }
                            });
                        });
</script>