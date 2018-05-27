<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html:form action="/FormerUnPackForm" styleId="FormerUnPackForm">
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Extraction physique d'objets</p></td>    
        </tr> 
        <tr>
            <td class="tdDemi"><p class="titres">Environnement source</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementSource" styleId="environnementSource" value="environnementSource"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationFormerUnPackForm" property="environnementList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>  

        <tr id="trDepotVersionning">
            <td class="tdDemi"><p class="titres">Dépôt de versionning</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="selectedDepot" styleId="selectedDepot" value="selectedDepot"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="PreparationFormerUnPackForm" property="depotList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>

        <tr>
            <td  class="tdDemi"><p class="titres">Nom du pack</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <html:text styleId="nomPack"  onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" property="nomPack" style="width:310px;margin-left: 5px;"/>                  
                </p>
            </td>
        </tr>        
        <tr>
            <td colspan="2">
                <p class="bouton">
                    <a href="#?w=630" rel="popup_objectList" class="poplight">                        
                        <html:button property="listeObjets"  value="Liste des objets" styleClass="boutonValider" />
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
        <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Former le pack" onclick="validerChampsFormerUnPack(contextPath);" /></p>
    </div>    
</html:form>
<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });


            $('#environnementSource').change(function() {
                if ($(this).val() === 'VERSIONNING') {
                    $(".grandTitre").html("Extraction SVN d'objets");
                    $("#trDepotVersionning").show();
                } else {
                    $(".grandTitre").html("Extraction physique d'objets");
                    $("#trDepotVersionning").hide();
                }
            });
</script>


