<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@page import="entitiesMysql.ObjetsHorsReferentiel"%>
<%@page import="java.util.*"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.JSONValue"%>
<%@page import="tools.Tools"%>

<%
    String jsonObjetsDepot = (String) request.getSession().getAttribute("JsonObjetsDepot");
    JSONObject jsonDataDepot = (JSONObject) JSONValue.parse(jsonObjetsDepot);
    //récupération des données Depot
    List<String> allDepotObjectList = null;

    String parameterPageCircuit = request.getParameter("circuit");

    if (parameterPageCircuit.equals("Release")) {
        allDepotObjectList = ((List<String>) jsonDataDepot.get("Release"));
    } else if (parameterPageCircuit.equals("Projet")) {
        allDepotObjectList = ((List<String>) jsonDataDepot.get("Projet"));
    } else if (parameterPageCircuit.equals("Production")) {
        allDepotObjectList = ((List<String>) jsonDataDepot.get("Production"));
    }

    //récupération des données Base
    List<ObjetsHorsReferentiel> listeAcocherReleaseGLOB = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherReleaseGLOB");
    List<ObjetsHorsReferentiel> listeAcocherReleaseELEM = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherReleaseELEM");
    List<ObjetsHorsReferentiel> listeAcocherProjetGLOB = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherProjetGLOB");
    List<ObjetsHorsReferentiel> listeAcocherProjetELEM = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherProjetELEM");
    List<ObjetsHorsReferentiel> listeAcocherProductionGLOB = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherProductionGLOB");
    List<ObjetsHorsReferentiel> listeAcocherProductionELEM = (List<ObjetsHorsReferentiel>) request.getSession().getAttribute("ListeAcocherProductionELEM");
    

%>

<div class='center'>
    <div class="centre">
        <div class="titre1">Administration du référentiel d'objets  <%= parameterPageCircuit%></div><br>
    </div> 


    <table id="tableReferentiel" class='roundCornerTable'>
        <thead>
            <tr>
                <th>
                    Référentiel 
                    <input type='checkbox' onClick="toggle(this, 'checkboxa' + '<%=parameterPageCircuit%>');"></th>
                <th>
                    Element(s) Référentiel
                    <input type='checkbox' onClick="toggle(this, 'checkboxb' + '<%=parameterPageCircuit%>');"></th>
                <th>
                    Libellé
                </th>
            </tr>

        </thead>
        <tbody>
            <%
                for (String objType : allDepotObjectList) {
                    out.println("<tr>"
                            + "<td style='text-align: center;'><input type='checkbox' name='checkboxa" + parameterPageCircuit + "' class='radioButtonA' value='' id='" + objType.replaceAll("\\.", "_") + parameterPageCircuit + "_GLOB'> </input></td>"
                            + "<td style='text-align: center;'><input type='checkbox' name='checkboxb" + parameterPageCircuit + "' class='radioButtonB' value='' id='" + objType.replaceAll("\\.", "_") + parameterPageCircuit + "_ELEM'> </input></td>"
                            + "<td>" + objType + "</td> </tr>");
                }
            %>
        </tbody>
    </table>

    <span style="text-align: center">
        <br>

        <%
            out.println("<span class='boutonGenerer'  onclick='invoquerServletNormesEtMethodes(preparerParametres(\"" + parameterPageCircuit + "\"));'>Actualiser le référentiel</span>");
        %>
    </span> 

    <span class="resultatInsertion vert logInsertion" ></span>


</div>

<script>

                        function invoquerServletNormesEtMethodes(parametres) {
                            $.ajax({
                                type: "POST",
                                url: contextPath + "/ServletNormesEtMethodes?" + parametres,
                                success: function(response) {
                                    afficherMessageSucces("<br>Mise à jour effectuée avec succès");
                                },
                                error: function(e) {
                                    //alert('Error: ' + e);
                                }
                            });

                        }

                        function preparerParametres(circuit) {
                            var parametres = "circuitGLOB=" + circuit + "GLOB" + "&typesGLOB=";
                            $(".radioButtonA").each(function() {
                                if (this.checked) {
                                    parametres += $(this).attr("id") + "@";
                                }
                            });
                            parametres = parametres.substring(0, parametres.length - 1);
                            parametres += "&circuitELEM=" + circuit + "ELEM" + "&typesELEM=";
                            $(".radioButtonB").each(function() {
                                if (this.checked) {
                                    parametres += $(this).attr("id") + "@";
                                }
                            });
                            parametres = parametres.substring(0, parametres.length - 1);
                            return parametres;
                        }

                        function toggle(source, nomPageAccordeon) {

                            $("input[name='" + nomPageAccordeon + "']").each(function() {
                                $(this).attr('checked', $(source).attr('checked'));
                            });
                        }
</script> 

<%
    for (ObjetsHorsReferentiel objType : listeAcocherReleaseGLOB) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
    for (ObjetsHorsReferentiel objType : listeAcocherReleaseELEM) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
%>

<%
    for (ObjetsHorsReferentiel objType : listeAcocherProjetGLOB) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
%>

<%
    for (ObjetsHorsReferentiel objType : listeAcocherProjetELEM) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
%>

<%
    for (ObjetsHorsReferentiel objType : listeAcocherProductionGLOB) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
%>

<%
    for (ObjetsHorsReferentiel objType : listeAcocherProductionELEM) {
        String id = objType.getObjetsHorsReferentielPK().getTypeObjet();
%> 
<script>
    $('#' + '<%=id%>').attr('checked', 'checked');
</script> 
<%
    }
%>



