<%@page import="tools.Configuration"%>
<%@page import="java.util.Map"%>
<%
    Map<String, String> mapCircuits = Configuration.etatCircuitMap;
%>

<table id="tablePrincipaleWorkFlow">
    <tr>
        <td class="grandTitrePage">
            Administration du workflow des tickets T24
        </td>
    </tr>
    <tr>
        <td class="cadre">
            <div class="case moyenne">
                <div class='titre' style="font-weight: bold">
                    Activation globale:
                </div>
                <div class='bouton'>
                    <div class="onoffswitch">
                        <input type='checkbox' name='onoffswitch' class='onoffswitch-checkbox' id='GLOBAL'>                        
                        <label class="onoffswitch-label" for="GLOBAL">
                            <div class="onoffswitch-inner"></div>
                            <div class="onoffswitch-switch"></div>
                        </label>
                    </div>                    
                </div>
            </div>
        </td>
    </tr>

    <tr class="cadreActivationParCircuit">
        <td class="cadre" >
            <div class="petitTitre">
                Gestion du Workflow par circuit (Création de bordereaux de livraison / Notifications de déploiement)
            </div>
            <table id="tableCircuits" class="roundCornerTable">
                <thead>
                    <tr>
                        <th></th>
                        <th>Création de tickets de livraison</th>
                        <th>Liquidation des tickets déployés</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td>Circuit HOTFIX</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CREATION_HOTFIX" >
                                <label class="onoffswitch-label" for="CREATION_HOTFIX">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch" style="margin-left: -10px"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CLOTURE_HOTFIX" >
                                <label class="onoffswitch-label" for="CLOTURE_HOTFIX">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>Circuit RELEASE</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CREATION_RELEASE" >
                                <label class="onoffswitch-label" for="CREATION_RELEASE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CLOTURE_RELEASE" >
                                <label class="onoffswitch-label" for="CLOTURE_RELEASE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>Circuit PROJET</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CREATION_PROJET" >
                                <label class="onoffswitch-label" for="CREATION_PROJET">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CLOTURE_PROJET" >
                                <label class="onoffswitch-label" for="CLOTURE_PROJET">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td>Circuit UPGRADE</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CREATION_UPGRADE" >
                                <label class="onoffswitch-label" for="CREATION_UPGRADE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CLOTURE_UPGRADE" >
                                <label class="onoffswitch-label" for="CLOTURE_UPGRADE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <div class="petitTitre">
                Gestion du Workflow circuit HOTFIX (Autorisations / Aiguillage pour mise en PROD / Harmonisation)
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Remise des Hotfix dépassant le délais de qualification au Comité Release:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="REVERSE_HF_AUTORISED" >
                                <label class="onoffswitch-label" for="REVERSE_HF_AUTORISED">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div id="timeSelector" class="timeSelector_REVERSE_HF_AUTORISED">
                                <label>Début:</label>
                                <%
                                    out.print("<input id='START_SENDING_HF_REVERSED' class='horloge' value='" + mapCircuits.get("START_SENDING_HF_REVERSED") + "' />");
                                %>
                                <label>Fin:</label>
                                <%
                                    out.print("<input id='END_SENDING_HF_REVERSED' class='horloge' value='" + mapCircuits.get("END_SENDING_HF_REVERSED") + "' />");
                                %>                                            
                            </div>
                        </td>
                    </tr>                    
                </table>

            </div>


            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Remontée des livraisons Hotfix PROD:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HF_PROD" >
                                <label class="onoffswitch-label" for="HF_PROD">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div id="timeSelector" class="timeSelector_HF_PROD">
                                <label>Début:</label>
                                <%
                                    out.print("<input id='START_HF_PROD' class='horloge' value='" + mapCircuits.get("START_HF_PROD") + "' />");
                                %>
                                <label>Fin:</label>
                                <%
                                    out.print("<input id='END_HF_PROD' class='horloge' value='" + mapCircuits.get("END_HF_PROD") + "' />");
                                %>                                            
                            </div>
                        </td>
                    </tr>                    
                </table>

            </div>

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Envoi des Hotfix pour mise en PROD:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="SENDING_HF_PROD" >
                                <label class="onoffswitch-label" for="SENDING_HF_PROD">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div id="timeSelector" class="timeSelector_SENDING_HF_PROD">
                                <label>Début:</label>
                                <%
                                    out.print("<input id='START_SENDING_HF_PROD' class='horloge' value='" + mapCircuits.get("START_SENDING_HF_PROD") + "' />");
                                %>
                                <label>Fin:</label>
                                <%
                                    out.print("<input id='END_SENDING_HF_PROD' class='horloge' value='" + mapCircuits.get("END_SENDING_HF_PROD") + "' />");
                                %>                                            
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div>    

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Création des tickets d'harmonisation:</td>
                        <td>
                            <div class="onoffswitch" style ='margin-right: 464px'>
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HF_HARM" >
                                <label class="onoffswitch-label" for="HF_HARM">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div> 

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Harmonisation automatique des Hotfix:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HARMONISATION_HF_PROD" >
                                <label class="onoffswitch-label" for="HARMONISATION_HF_PROD">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div id="timeSelector" class="timeSelector_HARMONISATION_HF_PROD">
                                <label>Début:</label>
                                <%
                                    out.print("<input id='START_HARMONISATION_HF_PROD' class='horloge' value='" + mapCircuits.get("START_HARMONISATION_HF_PROD") + "' />");
                                %>                                                                          
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div>      

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Harmonisation des Hotfix sur le circuit UPGRADE:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HARMONISATION_CU" >
                                <label class="onoffswitch-label" for="HARMONISATION_CU">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>
                            <div id="timeSelector" class="timeSelector_HARMONISATION_CU">
                                <label>Début:</label>
                                <%
                                    out.print("<input id='START_HARMONISATION_HF_UPGRADE' class='horloge' value='" + mapCircuits.get("START_HARMONISATION_HF_UPGRADE") + "' />");
                                %>                                                                          
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div>

            <!--div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Défalcation des tickets HF sur le C.UP:</td>
                        <td>
                            <div class="onoffswitch" style ='margin-right: 464px'>
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CREATION_HARM_UPGRADE" >
                                <label class="onoffswitch-label" for="CREATION_HARM_UPGRADE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                    </tr>                    
                </table>
            </div--> 

        </td>
    </tr>

    <tr class="cadreActivationParCircuit">
        <td class="cadre">
            <div class="petitTitre">
                Gestion du déploiement automatique des livraisons sur les environnements finaux
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit HOTFIX:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HF_DEP_TEST" >
                                <label class="onoffswitch-label" for="HF_DEP_TEST">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit RELEASE:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CR_DEP_TEST" >
                                <label class="onoffswitch-label" for="CR_DEP_TEST">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit PROJET:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CP_DEP_TEST" >
                                <label class="onoffswitch-label" for="CP_DEP_TEST">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit UPGRADE:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CU_DEP_TEST" >
                                <label class="onoffswitch-label" for="CU_DEP_TEST">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
        </td>
    </tr>

    <tr class="cadreActivationParCircuit">
        <td class="cadre">
            <div class="petitTitre">
                Gestion de la prise en charge automatique des livraisons
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit HOTFIX:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="HF_PRISE_EN_CHARGE" >
                                <label class="onoffswitch-label" for="HF_PRISE_EN_CHARGE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit RELEASE:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CR_PRISE_EN_CHARGE" >
                                <label class="onoffswitch-label" for="CR_PRISE_EN_CHARGE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit PROJET:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CP_PRISE_EN_CHARGE" >
                                <label class="onoffswitch-label" for="CP_PRISE_EN_CHARGE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Circuit UPGRADE:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="CU_PRISE_EN_CHARGE" >
                                <label class="onoffswitch-label" for="CU_PRISE_EN_CHARGE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
        </td>
    </tr>

    <tr class="cadreActivationParCircuit">
        <td class="cadre">
            <div class="petitTitre">
                Livraisons Self-Service
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Livraisons Self-Service automatique:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="LIVRAISON_SELF_SERVICE" >
                                <label class="onoffswitch-label" for="LIVRAISON_SELF_SERVICE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>         
        </td>
    </tr>

    <tr class="cadreActivationParCircuit">
        <td class="cadre">
            <div class="petitTitre">
                Redémarrage des Threads
            </div>
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Thread de gestion des HOTFIX:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="THREAD_HF" >
                                <label class="onoffswitch-label" for="THREAD_HF">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Thread Harmonisation Upgrade:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="THREAD_HARM_UPGRADE" >
                                <label class="onoffswitch-label" for="THREAD_HARM_UPGRADE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>

            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Thread Auto-déploiement OV:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="THREAD_OV" >
                                <label class="onoffswitch-label" for="THREAD_OV">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>
            
            <div class="cadre" style="margin: 1px;margin-top: 2px;width: 815px;">
                <table class="longue">                    
                    <tr>
                        <td>Thread Auto-déploiement I&E:</td>
                        <td>
                            <div class="onoffswitch">
                                <input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="THREAD_IE" >
                                <label class="onoffswitch-label" for="THREAD_IE">
                                    <div class="onoffswitch-inner"></div>
                                    <div class="onoffswitch-switch"></div>
                                </label>
                            </div>
                        </td>
                        <td>

                        </td>
                    </tr>                    
                </table>
            </div>   
        </td>
    </tr>




    <tr>
        <td style="text-align: center">
            <br/>
            <input type="button" class="boutonValider"  value="Valider" onclick="invoquerServletWorkflowDataManage(preparerParametres());" />
        </td>
    </tr>
    <tr style="height: 50px">
        <td>
            <div class="resultatInsertion logInsertion" ><br><span class="vert">Mise à jour effectuée avec succès</span></div>
        </td>
    </tr>
</table>

<script>
                function preparerParametres() {
                    var parametres = "";
                    $(".onoffswitch-checkbox").each(function(index) {
                        parametres += $(this).attr("id") + "=" + this.checked + "&";
                    });
                    $(".horloge").each(function(index) {
                        parametres += $(this).attr("id") + "=" + $(this).val() + "&";
                    });
                    parametres = parametres.substring(0, parametres.length - 1);
                    parametres = parametres.replace(/true/g, "ON");
                    parametres = parametres.replace(/false/g, "OFF");
                    return parametres;
                }
</script>                                 



<%
    String GLOBAL = mapCircuits.get("GLOBAL");
    if (GLOBAL.equals("ON")) {
%>
<script>
    $('#GLOBAL').attr('checked', 'checked');
    $(".cadreActivationParCircuit").each(function() {
        $(this).show();
    });
</script>
<%} else {
%>
<script>
    $(".cadreActivationParCircuit").hide(function() {
        $(this).hide();
    });
</script>
<%                        }
%>

<!-- ****** CREATION_HOTFIX ****** -->
<%
    String CREATION_HOTFIX = mapCircuits.get("CREATION_HOTFIX");
    if (CREATION_HOTFIX.equals("ON")) {
%>
<script>
    $('#CREATION_HOTFIX').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** LIVRAISON_SELF_SERVICE ****** -->
<%
    String LIVRAISON_SELF_SERVICE = mapCircuits.get("LIVRAISON_SELF_SERVICE");
    if (LIVRAISON_SELF_SERVICE.equals("ON")) {
%>
<script>
    $('#LIVRAISON_SELF_SERVICE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CLOTURE_HOTFIX ****** -->
<%
    String CLOTURE_HOTFIX = mapCircuits.get("CLOTURE_HOTFIX");
    if (CLOTURE_HOTFIX.equals("ON")) {
%>
<script>
    $('#CLOTURE_HOTFIX').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CREATION_RELEASE ****** -->
<%
    String CREATION_RELEASE = mapCircuits.get("CREATION_RELEASE");
    if (CREATION_RELEASE.equals("ON")) {
%>
<script>
    $('#CREATION_RELEASE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CLOTURE_RELEASE ****** -->
<%
    String CLOTURE_RELEASE = mapCircuits.get("CLOTURE_RELEASE");
    if (CLOTURE_RELEASE.equals("ON")) {
%>
<script>
    $('#CLOTURE_RELEASE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CREATION_PROJET ****** -->
<%
    String CREATION_PROJET = mapCircuits.get("CREATION_PROJET");
    if (CREATION_PROJET.equals("ON")) {
%>
<script>
    $('#CREATION_PROJET').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CREATION_UPGRADE ****** -->
<%
    String CREATION_UPGRADE = mapCircuits.get("CREATION_UPGRADE");
    if (CREATION_UPGRADE.equals("ON")) {
%>
<script>
    $('#CREATION_UPGRADE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CLOTURE_PROJET ****** -->
<%
    String CLOTURE_PROJET = mapCircuits.get("CLOTURE_PROJET");
    if (CLOTURE_PROJET.equals("ON")) {
%>
<script>
    $('#CLOTURE_PROJET').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CLOTURE_UPGRADE ****** -->
<%
    String CLOTURE_UPGRADE = mapCircuits.get("CLOTURE_UPGRADE");
    if (CLOTURE_UPGRADE.equals("ON")) {
%>
<script>
    $('#CLOTURE_UPGRADE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** REVERSE_HF_AUTORISED ****** -->
<%
    String REVERSE_HF_AUTORISED = mapCircuits.get("REVERSE_HF_AUTORISED");
    if (REVERSE_HF_AUTORISED.equals("ON")) {
%>
<script>
    $('#REVERSE_HF_AUTORISED').attr('checked', 'checked');
    $(".timeSelector_REVERSE_HF_AUTORISED").show();
</script>
<%} else {
%>
<script>
    $(".timeSelector_REVERSE_HF_AUTORISED").hide();
</script>
<%                        }
%>

<!-- ****** HF_PROD ****** -->
<%
    String HF_PROD = mapCircuits.get("HF_PROD");
    if (HF_PROD.equals("ON")) {
%>
<script>
    $('#HF_PROD').attr('checked', 'checked');
    $(".timeSelector_HF_PROD").show();
</script>
<%} else {
%>
<script>
    $(".timeSelector_HF_PROD").hide();
</script>
<%                        }
%>

<!-- ****** SENDING_HF_PROD ****** -->
<%
    String SENDING_HF_PROD = mapCircuits.get("SENDING_HF_PROD");
    if (SENDING_HF_PROD.equals("ON")) {
%>
<script>
    $('#SENDING_HF_PROD').attr('checked', 'checked');
    $(".timeSelector_SENDING_HF_PROD").show();
</script>
<%} else {
%>
<script>
    $(".timeSelector_SENDING_HF_PROD").hide();
</script>
<%                        }
%>

<!-- ****** HARMONISATION_HF_PROD ****** -->
<%
    String HARMONISATION_CU = mapCircuits.get("HARMONISATION_CU");
    if (HARMONISATION_CU.equals("ON")) {
%>
<script>
    $('#HARMONISATION_CU').attr('checked', 'checked');
    $(".timeSelector_HARMONISATION_CU").show();
</script>
<%} else {
%>
<script>
    $(".timeSelector_HARMONISATION_CU").hide();
</script>
<%                        }
%>

<!-- ****** HARMONISATION_HF_UPGRADE ****** -->
<%
    String HARMONISATION_HF_PROD = mapCircuits.get("HARMONISATION_HF_PROD");
    if (HARMONISATION_HF_PROD.equals("ON")) {
%>
<script>
    $('#HARMONISATION_HF_PROD').attr('checked', 'checked');
    $(".timeSelector_HARMONISATION_HF_PROD").show();
</script>
<%} else {
%>
<script>
    $(".timeSelector_HARMONISATION_HF_PROD").hide();
</script>
<%                        }
%>

<!-- ****** HF_HARM ****** -->
<%
    String HF_HARM = mapCircuits.get("HF_HARM");
    if (HF_HARM.equals("ON")) {
%>
<script>
    $('#HF_HARM').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CREATION_HARM_UPGRADE ****** -->
<%
    String CREATION_HARM_UPGRADE = mapCircuits.get("CREATION_HARM_UPGRADE");
    if (CREATION_HARM_UPGRADE.equals("ON")) {
%>
<script>
    $('#CREATION_HARM_UPGRADE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** HF_DEP_TEST ****** -->
<%
    String HF_DEP_TEST = mapCircuits.get("HF_DEP_TEST");
    if (HF_DEP_TEST.equals("ON")) {
%>
<script>
    $('#HF_DEP_TEST').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CR_DEP_TEST ****** -->
<%
    String CR_DEP_TEST = mapCircuits.get("CR_DEP_TEST");
    if (CR_DEP_TEST.equals("ON")) {
%>
<script>
    $('#CR_DEP_TEST').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CP_DEP_TEST ****** -->
<%
    String CP_DEP_TEST = mapCircuits.get("CP_DEP_TEST");
    if (CP_DEP_TEST.equals("ON")) {
%>
<script>
    $('#CP_DEP_TEST').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CU_DEP_TEST ****** -->
<%
    String CU_DEP_TEST = mapCircuits.get("CU_DEP_TEST");
    if (CU_DEP_TEST.equals("ON")) {
%>
<script>
    $('#CU_DEP_TEST').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** HF_PRISE_EN_CHARGE ****** -->
<%
    String HF_PRISE_EN_CHARGE = mapCircuits.get("HF_PRISE_EN_CHARGE");
    if (HF_PRISE_EN_CHARGE.equals("ON")) {
%>
<script>
    $('#HF_PRISE_EN_CHARGE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CR_PRISE_EN_CHARGE ****** -->
<%
    String CR_PRISE_EN_CHARGE = mapCircuits.get("CR_PRISE_EN_CHARGE");
    if (CR_PRISE_EN_CHARGE.equals("ON")) {
%>
<script>
    $('#CR_PRISE_EN_CHARGE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CP_PRISE_EN_CHARGE ****** -->
<%
    String CP_PRISE_EN_CHARGE = mapCircuits.get("CP_PRISE_EN_CHARGE");
    if (CP_PRISE_EN_CHARGE.equals("ON")) {
%>
<script>
    $('#CP_PRISE_EN_CHARGE').attr('checked', 'checked');
</script>
<%    }
%>

<!-- ****** CU_PRISE_EN_CHARGE ****** -->
<%
    String CU_PRISE_EN_CHARGE = mapCircuits.get("CU_PRISE_EN_CHARGE");
    if (CU_PRISE_EN_CHARGE.equals("ON")) {
%>
<script>
    $('#CU_PRISE_EN_CHARGE').attr('checked', 'checked');
</script>
<%    }
%>

<script type="text/javascript">
    $("#GLOBAL").change(function() {
        if (this.checked) {
            $(".cadreActivationParCircuit").each(function() {
                $(this).show();
            });
        } else {
            $(".cadreActivationParCircuit").hide(function() {
                $(this).hide();
            });
        }
    });

    $("#REVERSE_HF_AUTORISED").change(function() {
        if (this.checked) {
            $(".timeSelector_REVERSE_HF_AUTORISED").show();
        } else {
            $(".timeSelector_REVERSE_HF_AUTORISED").hide();
        }
    });

    $("#HF_PROD").change(function() {
        if (this.checked) {
            $(".timeSelector_HF_PROD").show();
        } else {
            $(".timeSelector_HF_PROD").hide();
        }
    });

    $("#SENDING_HF_PROD").change(function() {
        if (this.checked) {
            $(".timeSelector_SENDING_HF_PROD").show();
        } else {
            $(".timeSelector_SENDING_HF_PROD").hide();
        }
    });

    $("#HARMONISATION_HF_PROD").change(function() {
        if (this.checked) {
            $(".timeSelector_HARMONISATION_HF_PROD").show();
        } else {
            $(".timeSelector_HARMONISATION_HF_PROD").hide();
        }
    });
    
    $("#HARMONISATION_CU").change(function() {
        if (this.checked) {
            $(".timeSelector_HARMONISATION_CU").show();
        } else {
            $(".timeSelector_HARMONISATION_CU").hide();
        }
    });
    
    $(document).ready(function() {
        // find the input fields and apply the time select to them.
        $('#timeSelector input').datetimepicker({
            datepicker: false,
            format: 'H:i',
            formatTime: 'H:i',
            step: 10
        });

        selectMenu('menuT24');
    });

    var contextPath = "<%=request.getContextPath()%>";
    setContextPath(contextPath);


</script>





