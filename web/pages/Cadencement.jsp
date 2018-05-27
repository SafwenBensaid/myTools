<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>


<html:form action="/ComparaisonObjetsForm" styleId="ComparaisonObjetsForm">
    <br> <br> 
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre"> <br><br> Génération Cadencement Delta <br> </p></td>            
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
            <td colspan="2">
                <p class="bouton">
                <p class="conteneurBouton">
                    <script>
                        var contextPath = "<%=request.getContextPath()%>";
                    </script>
                    <input type="button" class="boutonValider boutonValiderSubmit"  value="Génération cadencement Delta" onclick="validerChampsCadencementDelta(contextPath)" />
                </p>
                </p>
            </td>
        </tr>
    </table>
    <br>                
    <div id='conteneurAnimation'>
        <div id="loadingAnimationConteneur" class="center">
            <%@include file="/pages/loadingAnimation.jsp" %>
        </div>
        <div id='loadingAnimationConteneur1' class="center" style='display: none'>
            <%@include file='/pages/loadingAnimation1.jsp' %>
        </div>
    </div>
</html:form>

<script>
                        $(document).ready(function() {
                            selectMenu('menuT24');
                            $("#loadingAnimationConteneur").hide();
                        });
</script>