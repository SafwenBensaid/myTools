<%@page import="tools.Configuration"%>
<%@page import="java.util.Date"%>
<%@page import="entitiesMysql.Audit"%>
<%@page import="tools.DataBaseTools"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<body onload='document.f.j_username.focus();'>

    <%
        DataBaseTools dbTools = new DataBaseTools(Configuration.puOvTools);
        Audit audit = new Audit();
        audit.setUpdateTime(new Date());
        audit.setAction("OPERATION_ON_DB");
        dbTools.StoreObjectIntoDataBase(audit);
        dbTools.closeRessources();
    %>    
    <script>
    var contextPath = "<%=request.getContextPath()%>";
    </script>
<center>

    <div class="error" id="errorMessage">

        <%-- Reason: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message} --%>
    </div>

    <form name='f' action="<c:url value='j_spring_security_check' />" method='POST'>

        <table class="tablePrincipale">
            <tr>
                <td colspan="2"><p class="grandTitre">Formulaire d'authentification</p></td>            
            </tr>
            <tr>
                <td class="tdDemi"><p class="titres">Login:</p></td>
                <td class="tdDemi">
                    <p class="contenu">
                        <input type='text' name='j_username' id='j_username' value=''>                                 
                    </p>
                </td>
            </tr>
            <tr>
                <td class="tdDemi"><p class="titres">Mot de passe:</p></td>
                <td class="tdDemi">
                    <p class="contenu">
                        <input type='password' name='j_password' id='j_password' />                                 
                    </p>
                </td>
            </tr>

            <tr>
                <td colspan="2">
                    <div class='center'>                
                        <input name="submit" type="submit" class="boutonValider" id='boutonValider' value="Authentification"/>
                        <br/>                    
                        <!--
                        <a href="#?w=630" rel="popup_inscription" class="poplight">                        
                        <html:button property="inscription" value="Inscription" styleClass="boutonValider" onclick="resetError();" />
                    </a>
                    <br/>                        
                        -->
                        <input name="reset" type="reset" class="boutonValider"  value="Reset"/> 
                        <br/>
                    </div>
                </td>
            </tr>

            <tr>
                <td colspan='2'></td>
            </tr>
            <tr>
                <td colspan='2'></td>
            </tr>
        </table>

    </form>
    <span class='resultatInsertion vert2 logInsertion' ></span>
    <span class='alertErreur rouge'></span>
</center>
</body>



<script>
    $("#ConteneurMenuVertical").remove();
    $(".espace").remove();
    $(".pageCorps").width('1250px');

    function resetError() {
        $(":text, :password").each(function() {
            $(this).val("");
        });

        $(".erreurInscription").html("");
        $(".erreurInscription").height("10px");
    }

    function extractUrlParams() {
        //extraire le context de l'application pour faire appel aux servlets
        var t = location.search.substring(1).split('&');
        var f = [];
        for (var i = 0; i < t.length; i++) {
            var x = t[ i ].split('=');
            f[x[0]] = x[1];
        }
        return f;
    }
    if (extractUrlParams()["error"] === "true") {
        $("#errorMessage").html("Votre tentative d'authentification a échoué, veuillez vérifier vos paramètres d'accès<br>et s'assurer que vous n'avez aucune autre session active");
    }
</script>