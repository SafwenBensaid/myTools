<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>



<html:form action="/LancerEtudeIntersectionInputObjets" styleId="LancerEtudeIntersectionInputObjets">
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Etude d'intersection (Circuit Release / Circuit Projet)</p></td>            
        </tr>
        <tr>
            <td class="tdDemi"><p class="titres">Etude d'intersection depuis</p></td>
            <td class="tdDemi">

                <form>
                    <p class="contenu">
                        <span style="margin-left: 5px" id="PACK" class="notSelected" onclick="selectObjets_Pack('PACK');"><input type= "radio" name="objetsSource" value="PACK" onclick="selectObjets_Pack('PACK');" id="CB_PACK">Un pack</span>
                        <span style="margin-left: 35px"  id="OBJETS" class="notSelected" onclick="selectObjets_Pack('OBJETS');"><input type= "radio" name="objetsSource" value="OBJETS" onclick="selectObjets_Pack('OBJETS');" id="CB_OBJETS">Une Liste d'objets</span>
                    </p>
                </form>

            </td>
        </tr>
        <tr  id="trEnvSource">
            <td class="tdDemi"><p class="titres">Environnement source</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:select property="environnementSourceName" styleId="environnementSourceName" value="environnementList"  style="width:313px;margin-left: 5px;">
                        <html:option value=""></html:option>
                        <html:options name="EtudeIntersectionInputObjetsForm" property="environnementList" />
                    </html:select>                                   
                </p>
            </td>
        </tr>        
        <tr  id="trNomPack">
            <td class="tdDemi"><p class="titres">Nom du pack</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:text styleId="nomPack" value=""  onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" property="nomPack" style="width:310px;margin-left: 5px;"/>                  
                </p>
            </td>
        </tr>    
        <tr  id="trNumHotfix">
            <td class="tdDemi"><p class="titres">Numéro de livraison HOTFIX</p></td>
            <td class="tdDemi">
                <p class="contenu">
                    <html:text styleId="numHotfix" value="" property="numHotfix" style="width:310px;margin-left: 5px;"/>                    
                </p>
            </td>
        </tr> 
        <tr  id="trBoutonListeObjets">
            <td colspan="2">
                <p class="bouton">
                    <a href="#?w=630" rel="popup_objectList" class="poplight">                        
                        <html:button property="listeObjets" value="Liste des objets" styleClass="boutonValider" />
                    </a>
                </p>
            </td>
        </tr>
        <tr id="trBoutonPack">
            <td colspan="2">
                <script>
                            var contextPath = "<%=request.getContextPath()%>";
                </script>
                <p class="bouton conteneurBouton"><input type="button" class="boutonValider boutonValiderSubmit"  value="Confirmer l'action" onclick="validerChampsEtudeIntersectionPack(contextPath);" /></p>
            </td>
        </tr>
    </table>
    <div id="popup_objectList" class="popup_block">
        <p class="titrePopup">Introduire la liste d'objets</p>
        <div class="center">
            <html:textarea styleId="textAreaObjectList" value=""  property="textAreaObjectList"/>            
        </div>
        <script>
                            var contextPath = "<%=request.getContextPath()%>";
        </script>
        <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Confirmer l'action" onclick="validerChampsEtudeIntersectionObjets(contextPath);" /></p>
    </div>
</html:form>
<script>
                            $(document).ready(function() {
                                selectMenu('menuT24');
                                $("#OBJETS").click();
                                $("#environnementSourceName option[value='VERSIONNING']").remove();
                            });

</script>
