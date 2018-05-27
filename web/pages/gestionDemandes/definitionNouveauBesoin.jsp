<%@page import="tools.Tools"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/trac.css">
<link rel="stylesheet"  type="text/css" href="css/ticket.css">
<link rel="stylesheet" type="text/css" href="css/styleMenuHorizontal.css"/>

<SCRIPT type="text/javascript" src="javascript/common.js"> </SCRIPT>

<p class="grandTitre">Gestion des Demandes Metiers</p>

<table>
    <tr>
        <td class="conteneurWrapper">
            <div id="wrapper">
                <div class="accordionButton" id="nouvreauProjet"  >Définition Nouveau Besoin</div>
                <div class="accordionContent" id="nouvreauProjetContent">


                    <table>
                        <tbody>
                            <tr>
                                <th class="col1">
                        <div class='bouton'>
                            <div class="showhide">
                                <input type='checkbox' name='showhide' class='showhide-checkbox' id='specification'>                        
                                <label class="showhide-label" for="specification">
                                    <div class="showhide-inner"></div>
                                    <div class="showhide-switch"></div>
                                </label>
                            </div>                    
                        </div>
                        </th>
                        <td class="col1">
                            <div class='titre couleur5'>
                                Identification du Besoin
                            </div>
                        </td>
                        </tr>
                        </tbody>
                    </table>

                    <table id="specificationBloc">
                        <tbody>
                            <tr>
                                <th><label for="fieldMETIER-summary">Intitulé du besoin:</label></th>
                                <td class="fullrow" colspan="3">
                                    <input type="text" id="fieldMETIER-summary" name="field_summary" size="70">
                                </td>
                            </tr>
                            <tr>
                                <th><label for="fieldMETIER-description" 
                                           title=
                                           "Décrire le besoin: 
                                           De quoi s'agit-il? 
                                           Le contexte du besoin (concurrence, stratégie, éléments chiffrés...)? 
                                           Les justifications de ce besoin?
                                           Les applictions et/ou les processus concernés?
                                           "
                                           >Description: </label></th>
                                <td class="fullrow" colspan="3">
                                    <fieldset>
                                        <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="fieldMETIER-description" name="field_description" class="wikitext trac-resizable" rows="10" cols="68"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: -8px;"></div></div></div>
                      </fieldset>
                    </td>
                  </tr>
                <tr>
                    <th class="col1">
                      <label for="fieldMETIER-milestone">Axe Métier:</label>
                    </th>
                    <td class="col1">
                        <select id="fieldMETIER-milestone" name="field_milestone">
                            <option selected="selected" value="A DEFINIR">A DEFINIR</option><option value="ASSURANCES BIAT">ASSURANCES BIAT</option><option value="BIAT CAPITAL">BIAT CAPITAL</option><option value="DEPARTEMENT CONTROLE DE GESTION">DEPARTEMENT CONTROLE DE GESTION</option><option value="DEPARTEMENT CONTROLE GENERAL">DEPARTEMENT CONTROLE GENERAL</option><option value="DEPARTEMENT DES OPERATIONS BANCAIRES">DEPARTEMENT DES OPERATIONS BANCAIRES</option><option value="DEPARTEMENT FINANCE - COMPTABILITE">DEPARTEMENT FINANCE - COMPTABILITE</option><option value="DEPARTEMENT MAITRISE D'OUVRAGE ET COORDINATION METIERS">DEPARTEMENT MAITRISE D'OUVRAGE ET COORDINATION METIERS</option><option value="DEPARTEMENT RECOUVREMENT ET CONTENTIEUX">DEPARTEMENT RECOUVREMENT ET CONTENTIEUX</option><option value="DEPARTEMENT RISQUES">DEPARTEMENT RISQUES</option><option value="DEPARTEMENT SYSTEMES D'INFORMATION">DEPARTEMENT SYSTEMES D'INFORMATION</option><option value="DGA BANQUE DE DETAIL">DGA BANQUE DE DETAIL</option><option value="DGA RESSOURCES">DGA RESSOURCES</option><option value="DIRECTION CENTRALE PLANIFICATION &amp; BUDGET">DIRECTION CENTRALE PLANIFICATION &amp; BUDGET</option><option value="DIRECTION CENTRALE RESSOURCES HUMAINES">DIRECTION CENTRALE RESSOURCES HUMAINES</option><option value="DIRECTION GENERALE">DIRECTION GENERALE</option><option value="DIRECTION PMO BANQUE">DIRECTION PMO BANQUE</option><option value="EQUIPE OPTIMISATION DES PROCESS">EQUIPE OPTIMISATION DES PROCESS</option><option value="EQUIPE REFONTE">EQUIPE REFONTE</option><option value="POLE GRANDES ENTREPRISES ET INSTITUTIONNELS">POLE GRANDES ENTREPRISES ET INSTITUTIONNELS</option><option value="POLE INVESTISSEURS">POLE INVESTISSEURS</option><option value="POLE STRATEGIE ET BANQUE DE FINANCEMENT ET D'INVESTISSEMENT">POLE STRATEGIE ET BANQUE DE FINANCEMENT ET D'INVESTISSEMENT</option><option value="PROTECTRICE">PROTECTRICE</option>
                        </select>
                    </td>
                    </tr>
                    <tr>
                    <th class="col2">
                      <label for="fieldMETIER-component">Activité:</label>
                    </th>
                    <td class="col2">
                        <select id="fieldMETIER-component" name="field_component">
                          <option value="A DEFINIR">A DEFINIR</option><option value="ASSURANCES BIAT">ASSURANCES BIAT</option><option value="COORDINATION ETUDES ET DEVELOPPEMENTS SI">COORDINATION ETUDES ET DEVELOPPEMENTS SI</option><option value="COORDINATION INFRASTRUCTURE &amp; PRODUCTION">COORDINATION INFRASTRUCTURE &amp; PRODUCTION</option><option value="DIRECTION ACHAT">DIRECTION ACHAT</option><option value="DIRECTION ADMINISTATION DES CREDITS">DIRECTION ADMINISTATION DES CREDITS</option><option value="DIRECTION ADMINISTRATION &amp; INTEGRATION DES SYSTEMES">DIRECTION ADMINISTRATION &amp; INTEGRATION DES SYSTEMES</option><option value="DIRECTION ADMINISTRATION DES CREDITS">DIRECTION ADMINISTRATION DES CREDITS</option><option value="DIRECTION ADMINISTRATIVE ET FINANCIERE DU PERSONNEL">DIRECTION ADMINISTRATIVE ET FINANCIERE DU PERSONNEL</option><option value="DIRECTION ANIMATION COMMERCIALE ET PILOTAGE">DIRECTION ANIMATION COMMERCIALE ET PILOTAGE</option><option value="DIRECTION ANIMATION QUALITE">DIRECTION ANIMATION QUALITE</option><option value="DIRECTION BACK OFFICE DES MARCHES">DIRECTION BACK OFFICE DES MARCHES</option><option value="DIRECTION CENTRALE PLANIFICATION ET BUDGET">DIRECTION CENTRALE PLANIFICATION ET BUDGET</option><option value="DIRECTION CENTRALE RESSOURCES HUMAINES">DIRECTION CENTRALE RESSOURCES HUMAINES</option><option value="DIRECTION CONCEPTION &amp; ORGANISATION COMPTABLE">DIRECTION CONCEPTION &amp; ORGANISATION COMPTABLE</option><option value="DIRECTION CONDUITE DU CHANGEMENT ET PROCEDURES">DIRECTION CONDUITE DU CHANGEMENT ET PROCEDURES</option><option value="DIRECTION CONTROLE DES DEPENSES">DIRECTION CONTROLE DES DEPENSES</option><option value="DIRECTION CONTROLE GESTION &amp; PRICING">DIRECTION CONTROLE GESTION &amp; PRICING</option><option value="DIRECTION COORDINATION ET PILOTAGE">DIRECTION COORDINATION ET PILOTAGE</option><option value="DIRECTION CORPORATE FINANCE">DIRECTION CORPORATE FINANCE</option><option value="DIRECTION CREDIT GRANDES ENTREPRISES">DIRECTION CREDIT GRANDES ENTREPRISES</option><option value="DIRECTION CREDITS">DIRECTION CREDITS</option><option value="DIRECTION DE L'AUDIT">DIRECTION DE L'AUDIT</option><option value="DIRECTION DE L'EXPLOITATION &amp; DE LA SUPERVISION">DIRECTION DE L'EXPLOITATION &amp; DE LA SUPERVISION</option><option value="DIRECTION DE L'INSPECTION">DIRECTION DE L'INSPECTION</option><option value="DIRECTION DE L'ORGANISATION">DIRECTION DE L'ORGANISATION</option><option value="DIRECTION DE LA CLIENTELE PRIVEE">DIRECTION DE LA CLIENTELE PRIVEE</option><option value="DIRECTION DE LA COMMUNICATION">DIRECTION DE LA COMMUNICATION</option><option value="DIRECTION DE LA CONFORMITE">DIRECTION DE LA CONFORMITE</option><option value="DIRECTION DE LA FORMATION &amp; DU DEVELOPPEMENT DES COMPETENCES">DIRECTION DE LA FORMATION &amp; DU DEVELOPPEMENT DES COMPETENCES</option><option value="DIRECTION DE LA LOGISTIQUE">DIRECTION DE LA LOGISTIQUE</option><option value="DIRECTION DE LA LOGISTIQUE ET DES OPERATIONS SFAX ET SUD">DIRECTION DE LA LOGISTIQUE ET DES OPERATIONS SFAX ET SUD</option><option value="DIRECTION DE LA PLANIFICATION">DIRECTION DE LA PLANIFICATION</option><option value="DIRECTION DE LA SECURITE ET DE CONTINUITE D'ACTIVITE">DIRECTION DE LA SECURITE ET DE CONTINUITE D'ACTIVITE</option><option value="DIRECTION DES AFFAIRES JURIDIQUES">DIRECTION DES AFFAIRES JURIDIQUES</option><option value="DIRECTION DES GARANTIES">DIRECTION DES GARANTIES</option><option value="DIRECTION DES INSTITUTIONNELS">DIRECTION DES INSTITUTIONNELS</option><option value="DIRECTION DES SERVICES ET DU SUPPORT INFORMATIQUES">DIRECTION DES SERVICES ET DU SUPPORT INFORMATIQUES</option><option value="DIRECTION DES TRANSFERTS">DIRECTION DES TRANSFERTS</option><option value="DIRECTION DES TRE ET DE CLIENTELE PARTICULIERS NON RESIDENTE">DIRECTION DES TRE ET DE CLIENTELE PARTICULIERS NON RESIDENTE</option><option value="DIRECTION DEVELOPPEMENT">DIRECTION DEVELOPPEMENT</option><option value="DIRECTION DEVELOPPEMENT A L'INTERNATIONAL">DIRECTION DEVELOPPEMENT A L'INTERNATIONAL</option><option value="DIRECTION DEVELOPPEMENT DE LA PME">DIRECTION DEVELOPPEMENT DE LA PME</option><option value="DIRECTION DEVELOPPEMENTS SI">DIRECTION DEVELOPPEMENTS SI</option><option value="DIRECTION DU BUDGET">DIRECTION DU BUDGET</option><option value="DIRECTION DU COMMERCE EXTERIEUR">DIRECTION DU COMMERCE EXTERIEUR</option><option value="DIRECTION DU CONTENTIEUX">DIRECTION DU CONTENTIEUX</option><option value="DIRECTION DU CONTROLE PERMANENT">DIRECTION DU CONTROLE PERMANENT</option><option value="DIRECTION DU COOMMERCE EXTERIEUR">DIRECTION DU COOMMERCE EXTERIEUR</option><option value="DIRECTION DU RECOUVREMENT">DIRECTION DU RECOUVREMENT</option><option value="DIRECTION ENQUETE ET ANTI-BLANCHIMENT">DIRECTION ENQUETE ET ANTI-BLANCHIMENT</option><option value="DIRECTION EXPLOITATION &amp; SUPPORT ADMINISTRATIF">DIRECTION EXPLOITATION &amp; SUPPORT ADMINISTRATIF</option><option value="DIRECTION FRONT OFFICE DES MARCHES">DIRECTION FRONT OFFICE DES MARCHES</option><option value="DIRECTION GESTION DES CARRIERES">DIRECTION GESTION DES CARRIERES</option><option value="DIRECTION GROUPES &amp; GRANDES ENTREPRISES 1">DIRECTION GROUPES &amp; GRANDES ENTREPRISES 1</option><option value="DIRECTION GROUPES &amp; GRANDES ENTREPRISES 2">DIRECTION GROUPES &amp; GRANDES ENTREPRISES 2</option><option value="DIRECTION INGENIERIE IT RESEAU &amp; SECURITE">DIRECTION INGENIERIE IT RESEAU &amp; SECURITE</option><option value="DIRECTION JUSTIFICATION ET CONTROLE COMPTABLE">DIRECTION JUSTIFICATION ET CONTROLE COMPTABLE</option><option value="DIRECTION LOGISTIQUE &amp; OPERATIONS SOUSSE CENTRE &amp; SAHEL">DIRECTION LOGISTIQUE &amp; OPERATIONS SOUSSE CENTRE &amp; SAHEL</option><option value="DIRECTION MARKETING">DIRECTION MARKETING</option><option value="DIRECTION METHODES ET OUTILS">DIRECTION METHODES ET OUTILS</option><option value="DIRECTION MOA BANQUE DE DETAIL &amp; NVX. CANAUX DE DISTRIB.">DIRECTION MOA BANQUE DE DETAIL &amp; NVX. CANAUX DE DISTRIB.</option><option value="DIRECTION MOA BFI ET FONCTIONS SUPPORT">DIRECTION MOA BFI ET FONCTIONS SUPPORT</option><option value="DIRECTION MOA ENGAGEMENTS ET RISQUES">DIRECTION MOA ENGAGEMENTS ET RISQUES</option><option value="DIRECTION MOA INTERNATIONAL">DIRECTION MOA INTERNATIONAL</option><option value="DIRECTION MOA MOYENS DE PAIEMENT">DIRECTION MOA MOYENS DE PAIEMENT</option><option value="DIRECTION MOYENS DE PAIEMENT">DIRECTION MOYENS DE PAIEMENT</option><option value="DIRECTION PARTICIPATIONS &amp; CAPITAL INVESTISSEMENT">DIRECTION PARTICIPATIONS &amp; CAPITAL INVESTISSEMENT</option><option value="DIRECTION PMO BANQUE">DIRECTION PMO BANQUE</option><option value="DIRECTION POLITIQUE CREDIT &amp; GESTION PORTEFEUILLE">DIRECTION POLITIQUE CREDIT &amp; GESTION PORTEFEUILLE</option><option value="DIRECTION PRODUCTIVITE">DIRECTION PRODUCTIVITE</option><option value="DIRECTION PROGICIELS HORS C24 &amp; INTRANET">DIRECTION PROGICIELS HORS C24 &amp; INTRANET</option><option value="DIRECTION REFERENTIEL SI ET DECISIONNEL">DIRECTION REFERENTIEL SI ET DECISIONNEL</option><option value="DIRECTION REPORTING &amp; CONTROLE">DIRECTION REPORTING &amp; CONTROLE</option><option value="DIRECTION RISQUE DE MARCHE">DIRECTION RISQUE DE MARCHE</option><option value="DIRECTION RISQUE OPERATIONNEL">DIRECTION RISQUE OPERATIONNEL</option><option value="DIRECTION RISQUES CREDIT PME &amp; CLIENTELE DE DETAIL">DIRECTION RISQUES CREDIT PME &amp; CLIENTELE DE DETAIL</option><option value="DIRECTION RISQUES DE CREDIT GROUPES &amp; GRANDES ENTREPRISES">DIRECTION RISQUES DE CREDIT GROUPES &amp; GRANDES ENTREPRISES</option><option value="DIRECTION SITUATIONS COMPTABLES ET FISCALES">DIRECTION SITUATIONS COMPTABLES ET FISCALES</option><option value="DIRECTION URBANISATION DU SI ET INTEGRATION">DIRECTION URBANISATION DU SI ET INTEGRATION</option><option value="EQUIPE OPTIMISATION DES PROCESS">EQUIPE OPTIMISATION DES PROCESS</option><option value="EQUIPE REFONTE">EQUIPE REFONTE</option><option value="POLE GRANDES ENTREPRISES ET INSTITUTIONNELS">POLE GRANDES ENTREPRISES ET INSTITUTIONNELS</option><option value="POLE INVESTISSSEUR">POLE INVESTISSSEUR</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th class="col1">
                      <label for="fieldMETIER-type">Type du besoin:</label>
                    </th>
                    <td class="col1">
                        <select id="fieldMETIER-type" name="field_type">
                            <option selected="selected" value="A DEFINIR">A DEFINIR</option><option value="REGLEMENTAIRE">REGLEMENTAIRE</option><option value="METIER">METIER</option><option value="STRATEGIQUE">STRATEGIQUE</option>
                        </select>
                    </td>
                    </tr>
                    <tr>
                    <th class="col1">
                      <label for="fieldMETIER-priority">Priorité du besoin:</label>
                    </th>
                    <td class="col1">
                        <select id="fieldMETIER-priority" name="field_priority">
                            <option selected="selected" value="A DEFINIR">A DEFINIR</option><option value="BLOQUANTE">BLOQUANTE</option><option value="MAJEURE">MAJEURE</option><option value="MINEURE">MINEURE</option>
                        </select>
                    </td>
                    </tr>
                
              </tbody>
        </table>
                        
       <table>
                            <tbody>
                                <tr>
                                    <th class="col1">
                            <div class='bouton'>
                                <div class="showhide">
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='impact'>                        
                                    <label class="showhide-label" for="impact">
                                        <div class="showhide-inner"></div>
                                        <div class="showhide-switch"></div>
                                    </label>
                                </div>                    
                            </div>
                            </th><td class="col1">

                                <div class='titre couleur5'>
                                    Impact/Enjeux
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>
                        <table id="impactBloc">
                            <tbody>
                                <tr>
                                    <th class="col1">
                                        <label for="fieldMETIER-impact_reglementaire">Impact Réglementaire:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="fieldMETIER-impact_reglementaire" name="field_impact_reglementaire">
                                            <option></option>
                                            <option value="OUI">OUI</option><option value="NON">NON</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="fieldMETIER-impact_pnb">Impact PNB:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="fieldMETIER-impact_pnb" name="field_impact_pnb">
                                            <option></option>
                                            <option value="1 : (0 - 50 000 TND)">1 : (0 - 50 000 TND)</option><option value="2 : (50 000 - 100 000 TND)">2 : (50 000 - 100 000 TND)</option><option value="3 : (100 000 - 300 000 TND)">3 : (100 000 - 300 000 TND)</option><option value="4 : (> 300 000 TND)">4 : (&gt; 300 000 TND)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="fieldMETIER-impact_qs_client">Impact Qualité Client:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="fieldMETIER-impact_qs_client" name="field_impact_qs_client">
                                            <option></option>
                                            <option value="1 : (Impact Faible -Cible Limitée)">1 : (Impact Faible -Cible Limitée)</option><option value="2 : (Impact Moyen - Cible Limitée)">2 : (Impact Moyen - Cible Limitée)</option><option value="3 : (Impact Important - Cible Importante)">3 : (Impact Important - Cible Importante)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="fieldMETIER-impact_productivite">Impact Productivité:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="fieldMETIER-impact_productivite" name="field_impact_productivite">
                                            <option></option>
                                            <option value="1 : (0-2 ETP)">1 : (0-2 ETP)</option><option value="2 : (3 - 5 ETP)">2 : (3 - 5 ETP)</option><option value="3 : (6 - 10 ETP)">3 : (6 - 10 ETP)</option><option value="4 : (> 10 ETP)">4 : (&gt; 10 ETP)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="fieldMETIER-impact_risque">Impact Risque:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="fieldMETIER-impact_risque" name="field_impact_risque">
                                            <option></option>
                                            <option value="1 : (Impact Faible -Cible Limitée)">1 : (Impact Faible -Cible Limitée)</option><option value="2 : (Impact Moyen - Cible Limitée)">2 : (Impact Moyen - Cible Limitée)</option><option value="3 : (Impact Important - Cible Importante)">3 : (Impact Important - Cible Importante)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="fieldMETIER-impact_performance">Impact Performance SI:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="fieldMETIER-impact_performance" name="field_impact_performance">
                                            <option></option>
                                            <option value="OUI">OUI</option><option value="NON">NON</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="fieldMETIER-impact_autres">Autres Impacts:</label>
                                    </th>
                                    <td class="col1">
                                        <input type="text" id="fieldMETIER-impact_autres" name="field_impact_autres">
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                    </th>
                                    <td class="col2">
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="fieldMETIER-contraintes">Contraintes/Commentaires:</label>
                                    </th>
                                    <td class="fullrow" colspan="3">
                                        <fieldset>
                                            <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="fieldMETIER-contraintesImpact" name="field_description" class="wikitext trac-resizable" rows="10" cols="68"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: -8px;"></div></div></div>
                      </fieldset>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="fieldMETIER-metier_concerne">Métiers concernés/impactés:</label>
                    </th>
                    <td class="col1">
                            <select id="fieldMETIER-metier_concerne" name="field_metier_concerne">
                                <option></option>
                                <option>DEPARTEMENT_CONTROLE_DE_GESTION</option><option>DEPARTEMENT_CONTROLE_GENERAL</option><option>DEPARTEMENT_DES_OPERATIONS_BANCAIRES</option><option>DEPARTEMENT_FINANCE_-_COMPTABILITE</option><option>DEPARTEMENT_MAITRISE_D'OUVRAGE_ET_COORDINATION_METIERS</option><option>DEPARTEMENT_RECOUVREMENT_ET_CONTENTIEUX</option><option>DEPARTEMENT_RISQUES</option><option>DEPARTEMENT_SYSTEMES_D'INFORMATION</option><option>DGA_BANQUE_DE_DETAIL</option><option>DGA_RESSOURCES</option><option>DIRECTION_GENERALE</option><option>POLE_STRATEGIE_ET_BANQUE_DE_FINANCEMENT_ET_D'INVESTISSEMENT</option><option>DIRECTION_CENTRALE_PLANIFICATION_&amp;_BUDGET</option><option>DIRECTION_CENTRALE_RESSOURCES_HUMAINES</option><option>POLE_GRANDES_ENTREPRISES_ET_INSTITUTIONNELS</option><option>POLE_INVESTISSEURS</option><option>ASSURANCE_BIAT</option><option>BIAT_CAPITAL</option><option>LA_PROTECTRICE</option><option>DIRECTION_PMO_BANQUE</option><option>EQUIPE_REFONTE</option></select>
                    </td>
                    </tr><tr>
                    <th class="col2">
                      <label for="fieldMETIER-client_impact">Clients Impactés:</label>
                    </th>
                    <td class="col2">
                            <select id="fieldMETIER-client_impact" name="field_client_impact">
                                <option></option>
                                <option>Clients_BDD</option><option>Clients_PGEI</option><option>Clients_Personne_Physique</option><option>Clients_Profesionnel</option><option>Clients_Personne_Morale</option><option>Autres</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="fieldMETIER-date_realisation">Date de réalisation souhaitée: </label>
                    </th>
                    <td  class="tdDemi">
                        <p class="contenu">                       
                            <input type="text" id="fieldMETIER-date_realisation" class="datePicker"/>
                        </p>
                </tr> 
              </tbody>
  
              
</table>
                 
                    
                      <table>
                        <tbody>
                            <tr>
                                <th class="col1">
                        <div class='bouton'>
                            <div class="showhide">
                                <input type='checkbox' name='showhide' class='showhide-checkbox' id='attachment'>                        
                                <label class="showhide-label" for="attachment">
                                    <div class="showhide-inner"></div>
                                    <div class="showhide-switch"></div>
                                </label>
                            </div>                    
                        </div>
                        </th>
                        <td class="col1">
                            <div class='titre couleur5'>
                                Joindre un fichier
                            </div>
                        </td>
                        </tr>
                        </tbody>
                    </table>

                    <table id="attachmentBloc">
                        <tbody>
                          <tr>
                              <th class="col1">
                                        <label for="fieldMETIER-fichier_joint" title="Insérer un document pour détailler davantage le besoin">Documents joints: </label>
                                    </th>
                    <td class="fieldcontainer col2">     
                        <form enctype="multipart/form-data" method="post" id="attachfileform" name="attachfileform" >
                        <input type="file" name="attachfile" class="regi_textbox"/> 
                        </form>   
                    </td>
</tr>
</tbody>
                        </table>
                        
<br/> <br/>
                <center><span id='messageResultatPersist' class='vert clignotant'></span></center>
                <br/>
                <center><input type="button" class="boutonValider" id='boutonValiderBesoin'  value="Valider la creation du Besoin" onclick="mergeBesoinIntoDB('insert');" /> </center>
                <center><input type="button" class="boutonValider" id='boutonConsulterBesoin'  value="Consulter le ticket du Besoin"/> </center>
                <center><input type="button" class="boutonValider" id='boutonCreerAutreBesoin'  value="Créer Un Autre Besoin" onclick="refreshPageCreation();" /> </center>
                
                
                
                </div>
                    </div>
                </td>
        </tr>
    </table>

<script>
$(document).ready(function() {
    /*var idTicket = window.location.href.split("=")[1];
     alert("idTicket " + idTicket);
     $("#loadingAnimationConteneur").hide();
     if (idTicket != null) {
     loadOneBesoinFromDB(idTicket);
     }
     */
    selectMenu('menuNouveauBesoin');
    $("#nouvreauProjet").show();
    $("#nouvreauProjet").addClass('over');
    $("#nouvreauProjetContent").show();

    $('#specification').attr('checked', true);
    $('#impact').attr('checked', true);
    $('#attachment').attr('checked', true);
    $('#boutonConsulterBesoin').hide();
    $('#boutonCreerAutreBesoin').hide();

    $("input").click(function() {
        $("#" + this.id + "Bloc").toggle();
    });
    ActivateDateTimePicker('fieldMETIER-date_realisation');
});

function refreshPageCreation() {
    var contextPath = "<%=request.getContextPath()%>";
    document.location.href = contextPath + "/definitionNouveauBesoin.do";
}

/*
function loadOneBesoinFromDB(idTicket) {
    var contextPath = "<%=request.getContextPath()%>";
    $("#loadingAnimationConteneur").show();
    $.ajax({
        type: "POST",
        url: contextPath + "/GestionDemandesMetiersServlet",
        data: "action=loadOne&idTicket=" + idTicket,
        success: function(response) {
            $("#loadingAnimationConteneur").hide();
            var objetJson = JSON.parse(response);
            $("#specificationBloc").html(objetJson["specificationBloc"]);
            $("#impactBloc").html(objetJson["impactBloc"]);
            $("#attachmentBloc").html(objetJson["attachmentBloc"]);
            //alert('impactBloc ' + objetJson["impactBloc"]);
            //alert('attachmentBloc ' + objetJson["attachmentBloc"]);
            $("#nouvreauProjet").html("Identification du Besoin <strong> #" + idTicket + ": " + $("#fieldMETIER-summary").val() + "</strong>");

            ActivateDateTimePicker('fieldMETIER-date_realisation');
        },
        error: function(e) {
            //alert('Error: ' + e);
        }
    });
}
*/
function mergeBesoinIntoDB(action) {
    //alert(action);
    var contextPath = "<%=request.getContextPath()%>";
    var field, value = "";
    var dataContent = "action=" + action;
    if (action === 'insert') {
        dataContent = dataContent + "&status=NOUVEAU_BESOIN";
    } else if (action === 'update') {
        dataContent = dataContent + "&idTicket=" + $('#fieldMETIER-id').val();
    }
    //alert(dataContent);
    $('[id^=fieldMETIER-]').each(function() {
        field = this.id.split('-').pop();
        value = $('#' + this.id).val();
        dataContent = dataContent + "&" + field + "=" + value;
    });
    //alert(dataContent);
    $.ajax({
        type: "POST",
        url: contextPath + "/GestionDemandesMetiersServlet",
        data: dataContent,
        success: function(response) {
            //alert(response);
            var objetJson = JSON.parse(response);
            var idTicket = objetJson["idTicket"];
            //alert(idTicket);
            $("#messageResultatPersist").text(objetJson["message"]);
            $("#messageResultatPersist").show();

            $('#boutonValiderBesoin').hide();
            $('#boutonConsulterBesoin').show();
            $('#boutonCreerAutreBesoin').show();

            $("#boutonConsulterBesoin").attr("onclick", "openTracTicketInNewTab('" + idTicket + "', 'GESTION_DEMANDES');OpenTicket('" + idTicket + "', 'NOUVEAU_BESOIN');");
            setTimeout(function() {
                $("#messageResultatPersist").hide();
            }, 3000);
        },
        error: function(e) {
            //alert('Error: '+e);
        }
    });

    var formElement = $("[name='attachfileform']")[0];
    var fd = new FormData(formElement);
    $.ajax({
        url: 'UploadFileServlet',
        data: fd,
        processData: false,
        contentType: false,
        type: 'POST',
        success: function(data) {
        }
    });
}

function OpenTicket(idTicket, param) {
    var contextPath = "<%=request.getContextPath()%>";
    document.location.href = contextPath + "/specificationProjet.do?idTicket=" + idTicket + "&param=" + param;
}
</script>