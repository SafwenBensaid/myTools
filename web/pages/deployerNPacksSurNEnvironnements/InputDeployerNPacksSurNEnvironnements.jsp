<%@page import="tools.Tools"%>
<%@page import="tools.SessionTools"%>
<%@page import="dto.CoupleDTO"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
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
<%
    Configuration.initialisation();
    String connectedUser = Tools.getConnectedLogin();
    List<String> environnementList = new ArrayList<String>(Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).keySet());
    environnementList.remove("VERSIONNING");
    environnementList.remove("ENV.7");

    List<String> listeEnvCurrentCircuit = null;
    CoupleDTO circuit_livraison = (CoupleDTO) request.getSession().getAttribute("circuit_livraison");
    if (circuit_livraison != null) {
        String envConcat = "";
        if (circuit_livraison.getValeur1().equals("RELEASE") || circuit_livraison.getValeur1().equals("HARMONISATION_C.RELEASE")) {
            envConcat = Configuration.parametresList.get("environnementsCircuitRelease");
        } else if (circuit_livraison.getValeur1().equals("PROJET") || circuit_livraison.getValeur1().equals("HARMONISATION_C.PROJET")) {
            envConcat = Configuration.parametresList.get("environnementsCircuitProjet");
        } else if (circuit_livraison.getValeur1().equals("HOTFIX")) {
            envConcat = Configuration.parametresList.get("environnementsCircuitHotfix");
        } else if (circuit_livraison.getValeur1().equals("UPGRADE")) {
            envConcat = Configuration.parametresList.get("environnementsCircuitUpgrade");
        }
        String[] tabEnvNameCircuit = Tools.separerEnvironnementsNoms(envConcat);
        listeEnvCurrentCircuit = new ArrayList<String>(Arrays.asList(tabEnvNameCircuit));
        listeEnvCurrentCircuit.retainAll(environnementList);
        environnementList.removeAll(listeEnvCurrentCircuit);
    }
%>
<SCRIPT language="Javascript">

    function selectOneCompany(id, currentTd) {
        ($(currentTd).children('span')).each(function() {
            $(this).attr("class", "notSelected");
        });
        $(currentTd).children('#' + id).attr("class", "selected");
        $(currentTd).children('span').children('#CB_' + id).attr('checked', true);
        if (id === 'TN1' || id === 'BNK') {
            $(currentTd).children("span").children("#autreMnemonic").val(id);
            $(currentTd).parent().children(".erreur").text("");
        }
        if (id === 'Autre') {
            $(currentTd).children("span").children("#autreMnemonic").val("");
            $(currentTd).parent().children(".erreur").text("MNEMONIC obligatoire");
        }
    }

    function invoquerServletGetAllCompaniesMnemonicsNew(mnemonic, contextPath) {
        loadingAnimation1 = 1;
        autreMnemonic = $(mnemonic).val();
        $(mnemonic).parent().parent().parent().children('.erreur').text("");
        //afficher l'animation       
        $(mnemonic).parent().parent().parent().children('#loadingAnimation').children('#conteneurAnimation').show();
        //désactiver tous les input
        $("input").each(function() {
            $(this).attr("disabled", "true");
        });

        if (xmlhttp) {
            xmlhttp.open("GET", contextPath + "/GetAllCompaniesMnemonics?mnemonic=" + autreMnemonic, true); //gettime will be the servlet name

            xmlhttp.onreadystatechange = function() {
                traitementApresAppelServletGetAllCompaniesMnemonicsNew(mnemonic);
            };
            xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
            xmlhttp.send(null);
        }
    }

    function traitementApresAppelServletGetAllCompaniesMnemonicsNew(mnemonic) {

        if (xmlhttp.readyState == 4) {
            if (xmlhttp.status == 200) {
                var valRetour = (xmlhttp.responseText).trim();
                if (valRetour === "ECHEC AUTHENTIFICATION") {
                    document.location.href = "./AfficherMessageErreurs.do";
                } else if (valRetour === "MNEMONIC OK") {

                } else {
                    $(mnemonic).parent().parent().parent().children('.erreur').text(valRetour);

                }
            }
            else {
                //cache l'animation       
                $(mnemonic).parent().parent().parent().children('#loadingAnimation').children('#conteneurAnimation').hide();
                loadingAnimation1 = 0;
                //activer tous les input
                $("input").each(function() {
                    $(this).removeAttr('disabled');
                });
                alert("Error during AJAX call. Please try again");
            }
            //cache l'animation       
            $(mnemonic).parent().parent().parent().children('#loadingAnimation').children('#conteneurAnimation').hide();
            loadingAnimation1 = 0;
            //activer tous les input
            $("input").each(function() {
                $(this).removeAttr('disabled');
            });
        }
    }

    function removeElement(img) {
        if (loadingAnimation1 === 0) {
            //alert($("#listePacksDetails").children('tr').length);
            if ($("#listePacksDetails").children('tr').length > 1) {
                ($(img).parent().parent()).remove();
            }
            $(".imageAdd").each(function() {
                $(this).hide();
            });
            $("#listePacksDetails tr:last-child td:first-child img").show();
            //cacher toutes les animations
            $("#conteneurAnimation").each(function() {
                $(this).hide();
            });
        }
    }

    function verifEmptyPacksNames() {
        var canAdd = true;
        $("#listePacksDetails tr #tdNomPack #nomPack").each(function() {
            var nomLastPack = $(this).val();
            nomLastPack = nomLastPack.replace("TAF-", "");
            nomLastPack = $.trim(nomLastPack);
            if (nomLastPack.length === 0) {
                $(this).addClass("inputError");
                canAdd = false;
            } else {
                $(this).removeClass("inputError");
            }
        });
        return canAdd;
    }

    function addElement(img) {
        var canAdd = verifEmptyPacksNames();
        if (canAdd === true) {
            $("#listePacksDetails tr #tdNomPack #nomPack").each(function() {
                $(this).removeClass("inputError");
            });
            if (loadingAnimation1 === 0) {
                var contenu = '<tr class="packDetails">' + $("#listePacksDetails tr:first-child").html() + '</tr>  ';
                //alert(contenu);
                $("#listePacksDetails").append(contenu);
                $(".imageAdd").each(function() {
                    $(this).hide();
                });
                $("#listePacksDetails tr:last-child td:first-child img").show();
                //regrouper chaque 3 checkbox horizontaux ensemble
                var index = $("#listePacksDetails").children('tr').length;
                $("#listePacksDetails tr:last-child #tdMnemonics span input[type=radio]").each(function() {
                    $(this).attr('name', 'mnemonic' + index);
                });
                //select TN1
                selectOneCompany("TN1", $("#listePacksDetails tr:last-child #tdMnemonics"));
                //cacher toutes les animations
                $("#conteneurAnimation").each(function() {
                    $(this).hide();
                });
                $('#listePacksDetails tr:last-child td:last-child').text("");
            }
        }
    }


</SCRIPT>

<html:form action="/LancerDeployerNPacksSurNEnvironnementsForm" styleId="LancerDeployerNPacksSurNEnvironnementsForm">

    <div class="centre">
        <table id="tableDeploiementEnMasse"  class="tablePrincipale">
            <tr>
                <td colspan="2">
                    <p class="grandTitre">
                        <%
                            if (circuit_livraison == null) {
                                out.print("Déploiement de n packs sur m environnements");
                            } else {
                                out.print("Intégration de la livraisons #" + circuit_livraison.getValeur2());
                            }
                        %>
                    </p>
                </td>            
            </tr>  
            <%
                if (circuit_livraison == null) {
            %>
            <tr  id="trEnvSource">
                <td class="tdDemi"><p class="titres">Récupérer les packs depuis l'environnement source:</p></td>
                <td class="tdDemi">                    
                    <p class="contenu">
                        <select name="environnementSourceName" id="environnementSourceName"  style="width:313px;margin-left: 5px;">
                            <option value=""></option>
                            <%

                                List<String> environnementList1 = new ArrayList<String>(Configuration.environnementDTOMapUserHasEnvironnement.get(connectedUser).keySet());
                                environnementList1.remove("ENV.7");
                                for (String envname : environnementList1) {
                                    out.print("<option value='" + envname + "'>" + envname + "</option>");
                                }
                            %>
                        </select>                                   
                    </p>
                </td>
            </tr>
            <%
                } else {
                    String envNameSource = null;
                    if (circuit_livraison.getValeur1().equals("PROJET")) {
                        envNameSource = "ASS2";
                    } else {
                        envNameSource = "ASS";
                    }
                    out.print("<tr><td collspan='2'>");
                    out.print("<input type='hidden' name='environnementSourceName' id='environnementSourceName' value='" + envNameSource + "'/>");
                    out.print("<input type='hidden' name='circuit' id='circuit' value='" + circuit_livraison.getValeur1() + "'/>");
                    out.print("<input type='hidden' name='numLivraison' id='numLivraison' value='" + circuit_livraison.getValeur2() + "'/>");
                    out.print("</td></tr>");
                }
            %>
            <tr id="trDossierSourcePath">
                <td class="tdDemi"><p class="titres" id="racineSrcPath"></p></td>
                <td class="tdDemi">
                    <p class="contenu">                    
                        <html:text styleId="dossierSourcePath" property="dossierSourcePath"  style="width:310px;margin-left: 5px;"/>                    
                    </p>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <table  id="listePacksDetailsTable" border="0">
                        <thead>
                            <tr>
                                <th colspan="2"></th>
                                <th>Nom du Pack</th>
                                <th>Mnemonic de la company</th> 
                                <th>Iter</th> 
                            </tr>
                        </thead>
                        <tbody  id="listePacksDetails">
                            <tr class="packDetails">
                                <td style="width: 25px"><img src="images/add.png" class="imageAdd"   onclick="addElement(this);"/>     </td>                        
                                <td style="width: 25px"><img src="images/remove.png" class="imageRemove"  onclick="removeElement(this);"/> </td>
                                <td style="width: 230px" id="tdNomPack"><input type="text" id="nomPack" name="nomPack" class="nomPack"  onchange="miseAJourPackName(this, 'TAF-')" onfocus="miseAJourPackName(this, 'TAF-')" onkeypress="miseAJourPackName(this, 'TAF-')" style="width:211px"/></td>
                                <td style="width: 300px;vertical-align: middle" id="tdMnemonics">
                                    <span id="TN1" class="notSelected" onclick="selectOneCompany('TN1', $(this).parent());">
                                        <input type= "radio"   name="mnemonic" value="TN1" onclick="selectOneCompany('TN1', $(this).parent().parent());" id="CB_TN1">
                                        TN1
                                    </span>
                                    <span  style="margin-left: 5px" id="BNK" class="notSelected" onclick="selectOneCompany('BNK', $(this).parent());">
                                        <input type= "radio" name="mnemonic" value="BNK" onclick="selectOneCompany('BNK', $(this).parent().parent());" id="CB_BNK">
                                        BNK
                                    </span>
                                    <span  style="margin-left: 5px" id="Autre" class="notSelected" onclick="selectOneCompany('Autre', $(this).parent());">
                                        <input type= "radio" name="mnemonic" value="Autre" onclick="selectOneCompany('Autre', $(this).parent().parent());"  id="CB_Autre">
                                        AUTRE
                                        <script>
    var contextPath = "<%=request.getContextPath()%>";
                                        </script>
                                        <input id="autreMnemonic" name="autreMnemonic" class="autreMnemonic" onchange="invoquerServletGetAllCompaniesMnemonicsNew(this, contextPath);"  style="width:42px; height:14px;margin-left: 5px"/>
                                    </span>
                                </td>    
                                <td style="width: 30px">
                                    <input type="number" name="nbrIter" id="nbrIter" class="nbrIter" min="1" max="5" step="1" value="1" style="width:26px;height:14px;margin-top: 3px">
                                </td> 
                                <td style="width: 30px" id="loadingAnimation">
                                    <span id="conteneurAnimation">
                                        <%@include file="/pages/loadingAnimation1.jsp" %>
                                    </span>
                                </td>
                                <td style="width: 145px;vertical-align: middle;text-align: left;color: red" class="erreur">

                                </td> 

                            </tr>
                        </tbody>
                    </table>



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
                        <div class="listeEnvironnements-left">Liste des environnements</div>
                        <div class="dhe-example-section" id="ex-1-3">		
                            <div class="dhe-example-section-content">

                                <!-- BEGIN: XHTML for example 1.3 -->

                                <div id="example-1-3">

                                    <div class="columnLeft left">
                                        <ul id="stockEnvironnements" class="sortable-list stockProjetsLeft">
                                            <%


                                                if (circuit_livraison != null) {
                                                    environnementList.remove("ASS");
                                                    environnementList.remove("ASS2");
                                                }

                                                int index = 0;
                                                for (String envname : environnementList) {
                                                    index = environnementList.indexOf(envname);
                                                    if (index < environnementList.size() / 2) {
                                                        out.print("<li  class='sortable-item'>" + envname + "<input type='hidden' value='" + envname + "'/></li>");
                                                    }
                                                }
                                            %>
                                        </ul>
                                    </div>

                                    <div class="columnWithoutMargin left">
                                        <ul id="stockEnvironnements" class="sortable-list stockProjetsRight">
                                            <%
                                                index = 0;
                                                for (String envname : environnementList) {
                                                    index = environnementList.indexOf(envname);
                                                    if (index >= environnementList.size() / 2) {
                                                        out.print("<li  class='sortable-item'>" + envname + "<input type='hidden' value='" + envname + "'/></li>");
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
                        <div class="listeEnvironnements-right">Déployer sur les environnements</div>
                        <div class="dhe-example-section" id="ex-1-3">		
                            <div class="dhe-example-section-content">

                                <!-- BEGIN: XHTML for example 1.3 -->

                                <div id="example-1-3">                                

                                    <div class="columnRight left">
                                        <ul id="environnementsCibles" class="sortable-list">
                                            <%
                                                if (listeEnvCurrentCircuit != null && !listeEnvCurrentCircuit.isEmpty()) {
                                                    for (String envname : listeEnvCurrentCircuit) {
                                                        out.print("<li  class='sortable-item'>" + envname + "<input type='hidden' value='" + envname + "' id='environnementsCiblesElements' name='environnementsCiblesElements' class='environnementsCiblesElements'/></li>");
                                                    }
                                                } else {
                                            %>
                                            <span id="description">
                                                Glissez les environnements sur les quels vous voulez effectuer le déploiement ici
                                            </span>
                                            <%                                                }
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
            </tr>
        </table>
    </div>
</html:form>



<p class="conteneurBouton">
    <script>
    var contextPath = "<%=request.getContextPath()%>";
    </script>

    <%
        if (circuit_livraison != null) {
            out.print("<input type='button' class='boutonValider'  value='Annuler' onclick='document.location.href = \"./getAllMilestonesForm.do?acteur=IE\";' />");
        }
    %>

    <input type="button" class="boutonValider"  value="Déployer" onclick="validerDeploiementParalleleMultipacks(contextPath);" />
</p>

</div>

<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        selectOneCompany("TN1", $("#listePacksDetails tr:last-child #tdMnemonics"));
        //cacher toutes les animations
        $("#conteneurAnimation").each(function() {
            $(this).hide();
        });
        $('#example-1-3 .sortable-list').sortable({
            connectWith: '#example-1-3 .sortable-list',
            placeholder: 'placeholder'
        });
        $('#example-1-3 div ul li').mouseout(function() {
            //alert($("#environnementsCibles").children().size());
            setIdForSelectedEnvironnements();
        });


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
        });


        function setIdForSelectedEnvironnements() {
            $("#environnementsCibles li").each(function() {
                $(this).children('input').attr('id', 'environnementsCiblesElements');
                $(this).children('input').attr('name', 'environnementsCiblesElements');
                $(this).children('input').attr('class', 'environnementsCiblesElements');
            });
            $("#stockEnvironnements li").each(function() {
                $(this).children('input').attr('id', 'stockEnvironnementsElements');
                $(this).children('input').attr('name', 'stockEnvironnementsElements');
                $(this).children('input').attr('class', 'stockEnvironnementsElements');
            });
            if ($("#environnementsCibles").children('li').size() > 0) {
                $("#description").hide();
                $("#environnementsCibles").removeAttr("style");
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