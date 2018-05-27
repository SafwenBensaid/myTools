<%@page import="tools.Tools"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<script>
    $(".pageCorps2").show();
    $(".pageCorps").hide();
</script>

<center>
    <table>
        <tr>
            <td class="conteneurWrapper">
                <div id="wrapper">

                    <div class="accordionButton" id="gestionDesParametres"  >Plateforme</div>
                    <div class="accordionContent">
                        <%@include file="/pages/administration/AdministratiobParametres.jsp" %> 
                    </div>

                    <div class="accordionButton" id="gestionDesEnvironnements"  >Environnements</div>
                    <div class="accordionContent">
                        <%@include file="/pages/administration/AministrationEnv.jsp" %> 
                    </div>                    

                    <div class="accordionButton" id="gestionNiveauxProjets"  >Projets</div>
                    <div class="accordionContent">
                        <%@include file="/pages/administration/AdministrationNiveauProjet.jsp" %> 
                    </div>

                </div>
            </td>            
        </tr>
    </table>
</center>




<script>
    $(document).ready(function() {
        selectMenu('menuT24');
        $(".pageCorps").show();
        $(".pageCorps2").hide();
        var tabparametres = window.location.href.split("=").length;
        if (tabparametres > 1) {
            $("#" + window.location.href.split("=")[1]).click();
        }
    });

</script>