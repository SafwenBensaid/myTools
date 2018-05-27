<%@page import="tools.Configuration"%>
<%@page import="tools.Tools"%>
<%
    String ticketIdString = request.getParameter("erreur");
    if (ticketIdString != null) {
        out.println("<h1><center class='rouge'>Accès refusé</center></h1>");
    }

    String matricule = Tools.getConnectedMatricule();
    if (Configuration.matriculeLoginMap.containsKey(matricule)) {
%>

<fieldset class="fielsetAccueil" style="background-image: url(' images/DiagrammeActiviteOV.png');background-repeat: no-repeat;background-position: center;">
    <legend>
        <img src="images/logoOvTools.png"/>
    </legend>
</fieldset>
<%} else {
%>
<fieldset class="fielsetAccueil">
    <legend>
        <img src="images/logoOvTools.png"/>
    </legend>
    <table class="tablePrincipale">
        <tr>
            <td colspan="2"><p class="grandTitre">Formulaire d'inscription</p></td>            
        </tr>        
        <tr class="groupe1">
            <td  class="tdDemi"><p class="titres">Login Trac</p></td>
            <td  class="tdDemi">
                <p class="contenu">
                    <input type="text" id="login" name="login" />
                </p>
            </td>
        </tr>
        <tr class="groupe1" id="trBoutonListeObjets">
            <td colspan="2">
                <p class="bouton"> 
                    <input type="button" id="inscription" name="inscription" value="s'inscrire" class="boutonValiderStandard" onClick='validationInscription();'/>
                </p>
            </td>
        </tr>
    </table>	
</fieldset>
<center>
    <span class='resultatInsertion vert2 logInsertion' ></span>
    <span class='alertErreur rouge'></span>
</center>
<%    }
%>
<script>
                        $(document).ready(function() {
                            selectMenu('indefined');
                        });
</script>


<script>

    function validationInscription() {
        var erreur = "";
        var hauteur = 0;

        var login = $("#login").val().trim();

        if (login === "") {
            erreur += "Veuillez spécifier votre login trac<br/>";
            hauteur += 15;
        }
        if (erreur.length > 0) {
            $(".erreurInscription").height(hauteur + "px");
            $(".erreurInscription").html(erreur);
        } else {
            var dataContent = "login=" + login + "&modif=creation";
            var contextPath = "<%=request.getContextPath()%>";
            $.ajax({
                type: "POST",
                url: contextPath + "/ServletGestionUtilisateur",
                data: dataContent,
                success: function(response) {
                    if ((response.indexOf("PROBLEME_USER_DUPLIQUE") !== -1)) {
                        $(".alertErreur").html("<br>Erreur: L'utilisateur existe déjà!");
                    } else {
                        $(".resultatInsertion").html("<br>Inscription effectuée avec succès");
                        setTimeout(function() {
                            window.location.href = "./login.do";
                        }, 1500);
                    }
                },
                error: function(e) {
                    //alert('Error: ' + e);
                }
            });
        }
    }
</script>