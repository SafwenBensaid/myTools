<%@page import="entitiesMysql.Environnement"%>
<%@page import="tools.Configuration"%>
<%@page import="java.util.*"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<SCRIPT language="Javascript">



    function validerFormationDuPack(contextPath) {

        var contextPath = "<%=request.getContextPath()%>";
        if ($("#nomPack").val().length > 10) {
            $("#nomPack").removeClass("inputError");
            submitDataConstitutionPackMultiprojets(contextPath);
        } else {
            $("#nomPack").addClass("inputError");
        }
    }
</SCRIPT>

<html:form action="/ConstitutionPackMultiprojetsForm" styleId="ConstitutionPackMultiprojetsForm">

    <div class="centre">
        <table id="tableDeploiementEnMasse"  class="tablePrincipale">
            <tr>
                <td colspan="2"><p class="grandTitre">Constitution de pack multi-projets</p></td>            
            </tr>  

            <tr  id="trNomPack">
                <td class="tdDemi"><p class="titres">Nom du pack</p></td>
                <td class="tdDemi">
                    <p class="contenu">
                        <html:text styleId="nomPack"  onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" property="nomPack" style="width:277px;margin-left: 5px;"/>                  
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
                    <div>

                    </div>
                </td>
            </tr>
            <tr>            
                <td>
                    <p class="contenu" >

                    </p>
                </td>
            </tr>
            <tr>
                <td class="leftTd tdDemi" >

                    <div id="center-wrapper-left">
                        <div class="listeEnvironnements-left">Liste des projets</div>
                        <div class="dhe-example-section" id="ex-1-3">		
                            <div class="dhe-example-section-content">

                                <!-- BEGIN: XHTML for example 1.3 -->

                                <div id="example-1-3">

                                    <div class="columnLeft left">
                                        <ul id="stockProjets" class="sortable-list stockProjetsLeft">
                                            <%
                                                List<String> projetsList = new ArrayList<String>(Configuration.projetsActifsCircuitReleaseEtProjetList);
                                                int index = 0;
                                                for (String projectName : projetsList) {
                                                    index = projetsList.indexOf(projectName);
                                                    if (index < projetsList.size() / 2) {
                                                        out.print("<li  class='sortable-item'>" + projectName + "<input type='hidden' value='" + projectName + "'/></li>");
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </div>

                                    <div class="columnWithoutMargin left">
                                        <ul id="stockProjets" class="sortable-list stockProjetsRight">
                                            <%
                                                index = 0;
                                                for (String projectName : projetsList) {
                                                    index = projetsList.indexOf(projectName);
                                                    if (index >= projetsList.size() / 2) {
                                                        out.print("<li  class='sortable-item'>" + projectName + "<input type='hidden' value='" + projectName + "'/></li>");
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </div>                                			

                                    <div class="clearer">&nbsp;</div>

                                </div>

                                <!-- END: XHTML for example 1.3 -->

                            </div>
                        </div>


                    </div>

                </td>    
                <td class="rightTd tdDemi">
                    <div id="center-wrapper-right">
                        <div class="listeEnvironnements-right">Projets à unir</div>
                        <div class="dhe-example-section" id="ex-1-3">		
                            <div class="dhe-example-section-content">

                                <!-- BEGIN: XHTML for example 1.3 -->

                                <div id="example-1-3">                                

                                    <div class="columnRight left">
                                        <ul id="projetsCibles" class="sortable-list">
                                            <span id="description">
                                                Glissez les projets que vous voulez unir ici
                                            </span>
                                        </ul>
                                    </div>				

                                    <div class="clearer">&nbsp;</div>

                                </div>

                                <!-- END: XHTML for example 1.3 -->

                            </div>
                        </div>


                    </div>
                </td>   
            </tr>
        </table>
    </div>
</html:form>
<!-- Example JavaScript files -->
<script type="text/javascript" src="jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="jquery-ui-1.8.custom.min.js"></script>


<p class="conteneurBouton">
    <script>
    var contextPath = "<%=request.getContextPath()%>";
    </script>
    <input type="button" class="boutonValider"  value="Former le pack" onclick="validerFormationDuPack(contextPath);" />
</p>

</div>

<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        $("#NON").click();
        //cacher toutes les animations

        $('#example-1-3 .sortable-list').sortable({
            connectWith: '#example-1-3 .sortable-list',
            placeholder: 'placeholder'
        });
        $('#example-1-3 div ul li').mouseout(function() {
            //alert($("#projetsCibles").children().size());
            setIdForSelectedEnvironnements();
        });
        function setIdForSelectedEnvironnements() {
            $("#projetsCibles li").each(function() {
                $(this).children('input').attr('id', 'projetsCiblesElements');
                $(this).children('input').attr('class', 'projetsCiblesElements');
                $(this).children('input').attr('name', 'projetsCiblesElements');
            });
            $("#stockProjets li").each(function() {
                $(this).children('input').attr('id', 'stockProjetsElements');
                $(this).children('input').attr('class', 'stockProjetsElements');
                $(this).children('input').attr('name', 'stockProjetsElements');
            });
            if ($("#projetsCibles").children('li').size() > 0) {
                $("#description").hide();
            } else {
                $("#description").show();
            }

            var nbElementsGauche = $(".stockProjetsLeft").children('li').size();
            var nbElementsDroite = $(".stockProjetsRight").children('li').size();

            if (nbElementsGauche > nbElementsDroite + 1) {
                var lastLi = $(".stockProjetsLeft").children('li').last();
                $(".stockProjetsRight").append(lastLi);
            }
            if (nbElementsDroite > nbElementsGauche + 1) {
                var lastLi = $(".stockProjetsRight").children('li').last();
                $(".stockProjetsLeft").append(lastLi);
            }
        }
    });

</script>