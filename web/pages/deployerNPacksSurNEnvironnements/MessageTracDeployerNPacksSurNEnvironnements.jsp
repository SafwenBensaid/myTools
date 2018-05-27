<%@page import="java.util.List"%>
<%@page import="dto.*"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%
    List<TripleDTO> packName_companyMnemonic_nbrIter_liste = (List<TripleDTO>) request.getSession().getAttribute("packName_companyMnemonic_nbrIter_liste");
    CoupleDTO circuit_livraison = (CoupleDTO) request.getSession().getAttribute("circuit_livraison");
    String problemesDeploiements = (String) request.getSession().getAttribute("problemesDeploiements");
    String[] environnementsCiblesElements = (String[]) request.getSession().getAttribute("environnementsCiblesElements");
%>


<div class="centre">

    <br>
    <div class="titre">Message à écrire dans le ticket TRAC</div>

    <span id="resultatAnalysePack">
        <%
            if (problemesDeploiements != null && problemesDeploiements.length() > 0) {
                out.print("<div class='center'>L'integration de  livraison n° <b>'''" + circuit_livraison.getValeur2() + " '''</b> (Circuit " + circuit_livraison.getValeur1() + " ) a échoué[[BR]][[BR]]</div><br><br>");
                out.print(problemesDeploiements.replace("<b>", "'''").replace("</b>", "'''").replace("<br>", "[[BR]]").replace("<br>", "[[BR]]"));
            } else {
                out.print("L'integration de  livraison n° <b>'''" + circuit_livraison.getValeur2() + " '''</b> (Circuit " + circuit_livraison.getValeur1() + " )");
                out.print("a été effectuée correctement sur le(s) environnement(s) suivant(s): <b>'''");
                for (int i = 0; i < environnementsCiblesElements.length; i++) {
                    if (i < (environnementsCiblesElements.length - 2)) {
                        out.print(environnementsCiblesElements[i] + ", ");
                    } else if (i == (environnementsCiblesElements.length - 2)) {
                        out.print(environnementsCiblesElements[i] + " et ");
                    }
                    if (i == (environnementsCiblesElements.length - 1)) {
                        out.print(environnementsCiblesElements[i]);
                    }
                }
                out.print("'''</b><br>[[BR]]Le(s) pack(s) déployés est (sont):<br>[[BR]]");

                for (TripleDTO tripleDTO : packName_companyMnemonic_nbrIter_liste) {
                    out.println("Pack: '''" + tripleDTO.getValeur1() + "''' / Company: '''" + tripleDTO.getValeur2() + "''' / Nombre d'itérations: '''" + tripleDTO.getValeur3() + "'''[[BR]]");
                }
            }
        %>

    </span>   

    <p class="conteneurBouton">
        <input type="button" class="boutonValider"  value="Fin" onclick="document.location.href = './getAllMilestonesForm.do?acteur=IE';" />                        
    </p>

</div>

<script>
            $(document).ready(function() {
                selectMenu('menuT24');
            });
</script>