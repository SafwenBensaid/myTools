<%@page import="servlets.GestionDemandesMetiersServlet"%>
<%@page import="java.util.Map"%>
<%@page import="entitiesTrac.Ticket"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<link rel="stylesheet" type="text/css" href="css/style.css">
<link rel="stylesheet" type="text/css" href="css/trac.css">
<link rel="stylesheet"  type="text/css" href="css/ticket.css">
<link rel="stylesheet" type="text/css" href="css/styleMenuHorizontal.css"/>


<p class="grandTitre">Gestion des Demandes Metiers</p>

<center>
    <table>
        <tr>
            <td class="conteneurWrapper">
                <div id="wrapper">
                    <div class="accordionButton" id="specProjet"></div>
                    <div class="accordionContent" id="specProjetContent">

                        <table>
                            <tbody>
                                <tr>
                                    <th class='col1'>
                            <div class='bouton'>
                                <div class='showhide'>
                                    <input type='checkbox' name='showhide' class='showhide-checkbox' id='detailsSpec'>
                                    <label class='showhide-label' for='detailsSpec'>
                                        <div class='showhide-inner'></div>
                                        <div class='showhide-switch'></div>
                                    </label>
                                </div>
                            </div>
                            </th>
                            <td class='col1'>
                                <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
                                    Détails de la demande
                                </div>
                            </td>
                            </tr>
                            </tbody>
                        </table>
                        <table id='detailsSpecBloc'>
                            <div id="attachments" class="">
                                <h3 class="foldable"><a id="no1" href="#no1">Fichier joint</a></h3>
                                <div class="attachments">
                                    <dl class="attachments">
                                        <dt>
                                        <a href="/trac/GESTION_DEMANDES_OVTOOLS/attachment/ticket/241/Gestion%20Des%20Besoins%20M%C3%A9rtier.png" title="Voir la pièce jointe">Gestion Des Besoins Mértier.png</a><a href="/trac/GESTION_DEMANDES_OVTOOLS/raw-attachment/ticket/241/Gestion%20Des%20Besoins%20M%C3%A9rtier.png" class="trac-rawlink" title="Télécharger">&#8203;</a>
                                        (<span title="109575 octets">107.0 KB</span>) -
                                        ajouté par <em>safwen ben said</em> <a class="timeline" href="/trac/GESTION_DEMANDES_OVTOOLS/timeline?from=2017-12-05T11%3A49%3A25%2B01%3A00&amp;precision=second" title="Voir l'activité du 5 déc. 2017 11:49:25">il y a 74 minutes</a>.
                                        </dt>
                                    </dl>
                                    <form method="get" action="http://172.28.70.74/trac/GESTION_DEMANDES_OVTOOLS/attachment/ticket/249/?action=new&attachfilebutton=Joindre+un+fichier" id="attachfile">
                                        <div>
                                            <input type="hidden" name="action" value="new">
                                            <input type="submit" name="attachfilebutton" value="Joindre un fichier">
                                        </div>
                                    </form>
                                </div>
                            </div>
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

                                <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
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
                                        <label for="field-impact_reglementaire">Impact Réglementaire:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="field-impact_reglementaire" name="field_impact_reglementaire">
                                            <option></option>
                                            <option value=""></option><option value="OUI">OUI</option><option value="NON">NON</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="field-impact_pnb">Impact PNB:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="field-impact_pnb" name="field_impact_pnb">
                                            <option></option>
                                            <option value="1 : (0 - 50 000 TND)">1 : (0 - 50 000 TND)</option><option value="2 : (50 000 - 100 000 TND)">2 : (50 000 - 100 000 TND)</option><option value="3 : (100 000 - 300 000 TND)">3 : (100 000 - 300 000 TND)</option><option value="4 : (> 300 000 TND)">4 : (&gt; 300 000 TND)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="field-impact_qs_client">Impact Qualité Client:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="field-impact_qs_client" name="field_impact_qs_client">
                                            <option></option>
                                            <option value="1 : (Impact Faible -Cible Limitée)">1 : (Impact Faible -Cible Limitée)</option><option value="2 : (Impact Moyen - Cible Limitée)">2 : (Impact Moyen - Cible Limitée)</option><option value="3 : (Impact Important - Cible Importante)">3 : (Impact Important - Cible Importante)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="field-impact_productivite">Impact Productivité:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="field-impact_productivite" name="field_impact_productivite">
                                            <option></option>
                                            <option value="1 : (0-2 ETP)">1 : (0-2 ETP)</option><option value="2 : (3 - 5 ETP)">2 : (3 - 5 ETP)</option><option value="3 : (6 - 10 ETP)">3 : (6 - 10 ETP)</option><option value="4 : (> 10 ETP)">4 : (&gt; 10 ETP)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="field-impact_risque">Impact Risque:</label>
                                    </th>
                                    <td class="col1">
                                        <select id="field-impact_risque" name="field_impact_risque">
                                            <option></option>
                                            <option value="1 : (Impact Faible -Cible Limitée)">1 : (Impact Faible -Cible Limitée)</option><option value="2 : (Impact Moyen - Cible Limitée)">2 : (Impact Moyen - Cible Limitée)</option><option value="3 : (Impact Important - Cible Importante)">3 : (Impact Important - Cible Importante)</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                        <label for="field-impact_performance">Impact Performance SI:</label>
                                    </th>
                                    <td class="col2">
                                        <select id="field-impact_performance" name="field_impact_performance">
                                            <option></option>
                                            <option value="OUI">OUI</option><option value="NON">NON</option>
                                        </select>
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="field-impact_autres">Impact Autres:</label>
                                    </th>
                                    <td class="col1">
                                        <input type="text" id="field-impact_autres" name="field_impact_autres">
                                    </td>
                                </tr><tr>
                                    <th class="col2">
                                    </th>
                                    <td class="col2">
                                    </td>
                                </tr><tr>
                                    <th class="col1">
                                        <label for="field-contraintes">Contraintes:</label>
                                    </th>
                                    <td class="fullrow" colspan="3">
                                        <fieldset>
                                            <div class="wikitoolbar">
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="strong" title="Bold text: '''Example'''" tabindex="400" style="background-image: url('images/strong.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="em" title="Italic text: ''Example''" tabindex="400" style="background-image: url('images/em.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="heading" title="Heading: == Example ==" tabindex="400" style="background-image: url('images/heading.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="link" title="Link: [http://www.example.com/ Example]" tabindex="400" style="background-image: url('images/link.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="hr" title="Horizontal rule: ----" tabindex="400" style="background-image: url('images/hr.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="np" title="New paragraph" tabindex="400" style="background-image: url('images/np.png')">    
                                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="br" title="Line break: [[BR]]" tabindex="400" style="background-image: url('images/br.png')">
                                            </div>
                                            <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="field-contraintesImpact" name="field_description" class="wikitext trac-resizable" rows="10" cols="68"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: -8px;"></div></div></div>
                      </fieldset>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-metier_concerne">Métiers concernés/impactés:</label>
                    </th>
                    <td class="col1">
                            <select id="field-metier_concerne" name="field_metier_concerne"><option>DEPARTEMENT_CONTROLE_DE_GESTION</option><option>DEPARTEMENT_CONTROLE_GENERAL</option><option>DEPARTEMENT_DES_OPERATIONS_BANCAIRES</option><option>DEPARTEMENT_FINANCE_-_COMPTABILITE</option><option>DEPARTEMENT_MAITRISE_D'OUVRAGE_ET_COORDINATION_METIERS</option><option>DEPARTEMENT_RECOUVREMENT_ET_CONTENTIEUX</option><option>DEPARTEMENT_RISQUES</option><option>DEPARTEMENT_SYSTEMES_D'INFORMATION</option><option>DGA_BANQUE_DE_DETAIL</option><option>DGA_RESSOURCES</option><option>DIRECTION_GENERALE</option><option>POLE_STRATEGIE_ET_BANQUE_DE_FINANCEMENT_ET_D'INVESTISSEMENT</option><option>DIRECTION_CENTRALE_PLANIFICATION_&amp;_BUDGET</option><option>DIRECTION_CENTRALE_RESSOURCES_HUMAINES</option><option>POLE_GRANDES_ENTREPRISES_ET_INSTITUTIONNELS</option><option>POLE_INVESTISSEURS</option><option>ASSURANCE_BIAT</option><option>BIAT_CAPITAL</option><option>LA_PROTECTRICE</option><option>DIRECTION_PMO_BANQUE</option><option>EQUIPE_REFONTE</option></select>
                    </td>
                    </tr><tr>
                    <th class="col2">
                      <label for="field-client_impact">Clients Impactés:</label>
                    </th>
                    <td class="col2">
                            <select id="field-client_impact" name="field_client_impact"><option>Clients_BDD</option><option>Clients_PGEI</option><option>Clients_Personne_Physique</option><option>Clients_Profesionnel</option><option>Clients_Personne_Morale</option><option>Autres</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-visavis_metier">Vis-à-vis Métier:</label>
                    </th>
                    <td class="col1">
                            <input type="text" id="field-visavis_metier" name="field_visavis_metier">
                    </td>
                    </tr><tr>
                    <th class="col2">
                      <label for="field-date_demande">Date de la demande:</label>
                    </th>
                    <td  class="tdDemi">
                        <p class="contenu">                       
                            <input type="text"  class="datePicker" id="field-date_demande"/>
                        </p>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-date_livraison">Date souhaitée de livraison:</label>
                    </th>
                    <td  class="tdDemi">
                        <p class="contenu">                       
                            <input type="text"  class="datePicker" id="field-date_livraison"/>
                        </p>
                    </td>
                    <th class="col2">
                    </th>
                    <td class="col2">
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-commentaire">Commentaires:</label>
                    </th>
                    <td class="col1" colspan="3">
                        <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="field-commentaireImpact" name="field_commentaire" cols="120" rows="5" class="wikitext trac-resizable"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: 391px;"></div></div></div>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-status">Status:</label>
                    </th>
                    <td class="col1">
                            <select id="field-status" name="field_status"><option>Validé</option><option>En attente validation</option></select>
                    </td>
                    </tr>
                <tr>
            <td style="text-align: center" colspan="2">
                <br/>
                <input type="button" class="boutonValider"  value="Enregister" onclick="" />
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
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='partiesPrenantes'>                        
            <label class="showhide-label" for="partiesPrenantes">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Parties Prenantes
    </div>
</td>
</tr>
</tbody>
</table>

<table id="partiesPrenantesBloc">
    <tbody><tr>
            <th class="col1">
                <label for="field-resmoa">Direction MOA:</label>
            </th>
            <td class="col1">
                <select id="field-resmoa" name="field_resmoa">
                    <option></option>
                    <option value=""></option><option value="DIRECTION_CONCEPTION_ET_ORGANISATION_COMPTABLE">DIRECTION_CONCEPTION_ET_ORGANISATION_COMPTABLE</option><option value="DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION">DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION</option><option value="DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT">DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT</option><option value="DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES">DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES</option><option value="DIRECTION_MOA_INTERNATIONAL">DIRECTION_MOA_INTERNATIONAL</option><option value="DIRECTION_MOA_MOYENS_DE_PAIEMENT">DIRECTION_MOA_MOYENS_DE_PAIEMENT</option>
                </select>
            </td>
        </tr><tr>
            <th class="col2">
                <label for="field-vis_a_vis_moa">Vis-à-vis MOA:</label>
            </th>
            <td class="col2">
                <select id="field-vis_a_vis_moa" name="field_vis_a_vis_moa">
                    <option></option>
                    <option value="abdeljlil.regaieg">abdeljlil.regaieg</option><option value="abdessattar.guetari">abdessattar.guetari</option><option value="ali.dallagi">ali.dallagi</option><option value="aymen.loukil">aymen.loukil</option><option value="bassem.hamdi">bassem.hamdi</option><option value="bechir.nouioui">bechir.nouioui</option><option value="dhouha.ayadi">dhouha.ayadi</option><option value="faouzi.fourati">faouzi.fourati</option><option value="hassen.ghorbel">hassen.ghorbel</option><option value="hassen.longo">hassen.longo</option><option value="imen.selmi">imen.selmi</option><option value="intissar.abdelli">intissar.abdelli</option><option value="jalel.ellouze">jalel.ellouze</option><option value="jamel.ben.tkhayat">jamel.ben.tkhayat</option><option value="kamel.malouche">kamel.malouche</option><option value="kaouther.hachicha">kaouther.hachicha</option><option value="lassaad.hakimi">lassaad.hakimi</option><option value="makki.sahnoun">makki.sahnoun</option><option value="marouene.ben.soltane">marouene.ben.soltane</option><option value="mohamed.bouattour">mohamed.bouattour</option><option value="mohamed.el.euch">mohamed.el.euch</option><option value="mohamed.tamedda">mohamed.tamedda</option><option value="mohamed.tarik.ben.jalloun">mohamed.tarik.ben.jalloun</option><option value="mohamed.walid.hamdi">mohamed.walid.hamdi</option><option value="mongi.bouassida">mongi.bouassida</option><option value="najoua.ellouze">najoua.ellouze</option><option value="nasreddine.rouabeh">nasreddine.rouabeh</option><option value="niazi.jedidi">niazi.jedidi</option><option value="rachid.fetoui">rachid.fetoui</option><option value="radhouane.khayati">radhouane.khayati</option><option value="rakia.moalla">rakia.moalla</option><option value="ramy.aydi">ramy.aydi</option><option value="rym.ben.ayed.mhiri">rym.ben.ayed.mhiri</option><option value="rym.ben.yahia">rym.ben.yahia</option><option value="safa.benlatifa">safa.benlatifa</option><option value="sami.babbou">sami.babbou</option><option value="sonia.moalla">sonia.moalla</option><option value="tarek.elleuch">tarek.elleuch</option><option value="tarek.sellami">tarek.sellami</option><option value="thouraya.zghal">thouraya.zghal</option><option value="yamen.khemir">yamen.khemir</option><option value="yosra.grami">yosra.grami</option><option value="zeineb.labidi">zeineb.labidi</option>
                </select>
            </td>
        </tr><tr>
            <th class="col1">
                <label for="field-directions_moa">Autre direction(s) MOA:</label>
            </th>
            <td class="col1">
                <select id="field-directions_moa" name="field_directions_moa"><option>DIRECTION_CONCEPTION_ET_ORGANISATION_COMPTABLE</option><option>DIRECTION_MOA_BANQUE_DE_DETAIL_et_NVX__CANAUX_DE_DISTRIBUTION</option><option>DIRECTION_MOA_BFI_ET_FONCTIONS_SUPPORT</option><option>DIRECTION_MOA_ENGAGEMENTS_ET_RISQUES</option><option>DIRECTION_MOA_INTERNATIONAL</option><option>DIRECTION_MOA_MOYENS_DE_PAIEMENT</option></select>
            </td>
        </tr><tr>
            <th class="col2">
                <label for="field-directions_moe">Direction(s) MOE:</label>
            </th>
            <td class="col2">
                <select id="field-directions_moe" name="field_directions_moe"><option>COORDINATION_ETUDES_ET_DEVELOPPEMENTS_SI</option><option>COORDINATION_INFRASTRUCTURE_ET_PRODUCTION</option><option>DIRECTION_DEVELOPPEMENTS_SI</option><option>DIRECTION_PROLOGICIELS_HORS_C24_ET_INTRANET</option><option>DIRECTION_REFERENTIEL_SI_ET_DECISIONNEL</option><option>DIRECTION_URBANISATION_DU_SI_ET_INTEGRATION</option></select>
            </td>
        </tr><tr>
            <th class="col1">
                <label for="field-visavis_technique">Vis-à-vis Technique:</label>
            </th>
            <td class="col1">
                <select id="field-visavis_technique" name="field_visavis_technique">
                    <option></option>
                    <option value="abdelkhalek.chaari">abdelkhalek.chaari</option><option value="ahmed.daadaa">ahmed.daadaa</option><option value="akram.lassoued">akram.lassoued</option><option value="amir.ktari">amir.ktari</option><option value="anis.sboui">anis.sboui</option><option value="aymen.dammak">aymen.dammak</option><option value="chedly.ben.ammar">chedly.ben.ammar</option><option value="emna.ben.chabchoubi">emna.ben.chabchoubi</option><option value="faten.zhioua">faten.zhioua</option><option value="ferjani.riahi">ferjani.riahi</option><option value="hafedh.boukadida">hafedh.boukadida</option><option value="hatem.chaaben">hatem.chaaben</option><option value="ibtihel.troudi">ibtihel.troudi</option><option value="ikbel.hamdi">ikbel.hamdi</option><option value="khalil.fekih">khalil.fekih</option><option value="khalid.ka">khalid.ka</option><option value="laroussi.jlidi">laroussi.jlidi</option><option value="meha.meftah">meha.meftah</option><option value="mehdi.zaibi">mehdi.zaibi</option><option value="mohamed.benabdesslem">mohamed.benabdesslem</option><option value="mohamed.fares.bouzaienne">mohamed.fares.bouzaienne</option><option value="mohsen.ouertani">mohsen.ouertani</option><option value="moncef.mallek">moncef.mallek</option><option value="mondher.moalla">mondher.moalla</option><option value="najoua.jaibi">najoua.jaibi</option><option value="omar.mahmoud">omar.mahmoud</option><option value="salem.belhajsalem">salem.belhajsalem</option><option value="sami.fe">sami.fe</option><option value="sami.lassoued">sami.lassoued</option><option value="sami.loukil">sami.loukil</option><option value="sarra.sakesli">sarra.sakesli</option><option value="skander.amdouni">skander.amdouni</option><option value="walid.be">walid.be</option><option value="yessine.bouafif">yessine.bouafif</option>
                </select>
            </td>
        </tr><tr>
            <th class="col2">
                <label for="field-visavis_comptable">Vis-à-vis Comptable:</label>
            </th>
            <td class="col2">
                <select id="field-visavis_comptable" name="field_visavis_comptable">
                    <option></option>
                    <option value="bassem.hamdi">bassem.hamdi</option><option value="intissar.abdelli">intissar.abdelli</option><option value="lassaad.hakimi">lassaad.hakimi</option><option value="nasreddine.rouabeh">nasreddine.rouabeh</option><option value="niazi.jedidi">niazi.jedidi</option><option value="radhouane.khayati">radhouane.khayati</option><option value="tarek.elleuch">tarek.elleuch</option><option value="yamen.khemiri">yamen.khemiri</option>
                </select>
            </td>
        </tr><tr>
            <th class="col1">
                <label for="field-visavis_dsi">Vis-à-vis DSI:</label>
            </th>
            <td class="col1">
                <select id="field-visavis_dsi" name="field_visavis_dsi">
                    <option></option>
                    <option value="ahmed.ayadi">ahmed.ayadi</option><option value="anis.benhamdene">anis.benhamdene</option><option value="anis.garbouj">anis.garbouj</option><option value="aymen.ben.bnina">aymen.ben.bnina</option><option value="bilel.ismail">bilel.ismail</option><option value="chaouki.bouchouicha">chaouki.bouchouicha</option><option value="charfeddine.mabrouk">charfeddine.mabrouk</option><option value="fethi.cheikh">fethi.cheikh</option><option value="ghassen.bezzine">ghassen.bezzine</option><option value="hamdi.belhadjali">hamdi.belhadjali</option><option value="kamel.mabrouk">kamel.mabrouk</option><option value="lassaad.srasra">lassaad.srasra</option><option value="leila.mehri">leila.mehri</option><option value="moez.bensalem">moez.bensalem</option><option value="mohamed.bahy">mohamed.bahy</option><option value="mongi.guesmi">mongi.guesmi</option><option value="najeh.ennajeh">najeh.ennajeh</option><option value="radhia.bencheikh">radhia.bencheikh</option><option value="riadh.anouar.ben.dakhlia">riadh.anouar.ben.dakhlia</option><option value="sami.bouaine">sami.bouaine</option><option value="sinda.rahmouni">sinda.rahmouni</option><option value="sofiene.moalla">sofiene.moalla</option><option value="tarek.ayadi">tarek.ayadi</option><option value="thameur.bensalem">thameur.bensalem</option><option value="wajih.mili">wajih.mili</option>
                </select>
            </td>
            <th class="col2">
            </th>
            <td class="col2">
            </td>
        </tr><tr>
            <th class="col1">
                <label for="field-interv_externes">Intervenants externes:</label>
            </th>
            <td class="col1" colspan="3"><div style="display: none;">
                    <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="field-interv_externes" name="field_interv_externes" cols="10" rows="1" class="wikitext trac-resizable" columns="true">||=Nom=||=Jour Homme=||=Budget=||</textarea><div class="trac-grip" style="margin-left: 2px; margin-right: 1161px;"></div></div></div>
                    </div><table class="fieldoftable wiki" for="field-interv_externes"><tbody><tr class="fieldoftableline"><th>Nom</th><th>Jour Homme</th><th>Budget</th></tr><tr class="lastline fieldoftableline"><td><input type="text" value=""></td><td><input type="text" value=""></td><td><input type="text" value=""></td></tr></tbody></table></td>
                </tr>
              </tbody>
        </table>


<table>
    <tbody>
        <tr>
            <th class="col1">
    <div class='bouton'>
        <div class="showhide">
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='impactSI'>                        
            <label class="showhide-label" for="impactSI">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Impact SI
    </div>
</td>
</tr>
</tbody>
</table>

        <table id="impactSIBloc">
                <tbody><tr>
                    <th class="col1">
                      <label for="field-fonctionnel">Impact SI Fonctionnel:</label>
                    </th>
                    <td class="col1">
                            <select id="field-fonctionnel" name="field_fonctionnel"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                    <th class="col2">
                      <label for="field-habilitation">Impact SI Habilitation:</label>
                    </th>
                    <td class="col2">
                            <select id="field-habilitation" name="field_habilitation"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-editique">Impact SI Editique:</label>
                    </th>
                    <td class="col1">
                            <select id="field-editique" name="field_editique"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                    <th class="col2">
                      <label for="field-comptable">Impact SI Comptable:</label>
                    </th>
                    <td class="col2">
                            <select id="field-comptable" name="field_comptable"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-tarification">Impact SI Tarification:</label>
                    </th>
                    <td class="col1">
                            <select id="field-tarification" name="field_tarification"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                    <th class="col2">
                      <label for="field-interfaces">Impact SI Interfaces:</label>
                    </th>
                    <td class="col2">
                            <select id="field-interfaces" name="field_interfaces"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-migration">Impact SI Migration:</label>
                    </th>
                    <td class="col1">
                            <select id="field-migration" name="field_migration"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                    <th class="col2">
                      <label for="field-autres">Impact SI Autres:</label>
                    </th>
                    <td class="col2">
                            <select id="field-autres" name="field_autres"><option>ARIMA</option><option>BFI_REMISE</option><option>BIATNET</option><option>CARATNOS</option><option>CARTAGO</option><option>GARGANTUA</option><option>GTI</option><option>HR_ACCESS</option><option>MEESSAGIS</option><option>MEGARA_OPCVM</option><option>MEGARA_SVT</option><option>MEGARA_TITRES</option><option>MULTIVIR</option><option>MXP</option><option>OGC</option><option>PLATEFORME_CONTENTIEUX</option><option>SITE_CENTRAL</option><option>SMI</option><option>STREAMSERVE</option><option>SWIFT</option><option>T24</option></select>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-appexterne">Syst/Application externe:</label>
                    </th>
                    <td class="col1">
                            <input type="text" id="field-appexterne" name="field_appexterne">
                    </td>
                    <th class="col2">
                    </th>
                    <td class="col2">
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
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='chiffrageMoa'>                        
            <label class="showhide-label" for="chiffrageMoa">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Chiffrage des travaux MOA
    </div>
</td>
</tr>
</tbody>
</table>



                        <p>Table with thead, tfoot, and tbody</p>
<table id='chiffrageMoaBloc' class='tableParamStyle roundCornerTable' style="">
<thead>
<tr>
<th>
Chiffrage des travaux par Domaine
</th>
<th>
j/h estimé
</th>
<th>
j/h realisé
</th>
</tr>
</thead>
<tbody>
        <tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA BDD</td>
		<td contenteditable="true" id="lig_0_col_1" class='ciffrageMOA'>2</td>
		<td contenteditable="true" id="lig_0_col_2" class='ciffrageMOA'>3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="tdChiffrageMOA" style="width: 90px;">MOA BFI</td>
		<td contenteditable="true" id="lig_1_col_1" class='ciffrageMOA' style="width: 90px;overflow: hidden; text-overflow: ellipsis;">5</td>
		<td contenteditable="true" id="lig_1_col_2" class='ciffrageMOA' style="width: 90px;overflow: hidden; text-overflow: ellipsis;">6</td>
	</tr>
		
        <tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA Comptabilite</td>
		<td contenteditable="true" id="lig_2_col_1" class='ciffrageMOA' style="white-space: normal; width:30px; height: 15px;">2</td>
		<td contenteditable="true" id="lig_2_col_2" class='ciffrageMOA'>3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA Engagement</td>
		<td contenteditable="true" id="lig_3_col_1" class='ciffrageMOA'>5</td>
		<td contenteditable="true" id="lig_3_col_2" class='ciffrageMOA'>6</td>
	</tr>
        
        <tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA International</td>
		<td contenteditable="true" id="lig_4_col_1" class='ciffrageMOA'>2</td>
		<td contenteditable="true" id="lig_4_col_2" class='ciffrageMOA'>3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA MDP</td>
		<td contenteditable="true" id="lig_5_col_1" class='ciffrageMOA'>5</td>
		<td contenteditable="true" id="lig_5_col_2" class='ciffrageMOA'>6</td>
	</tr>
        
        <tr>
		<td contenteditable="true" id="tdChiffrageMOA">MOA CDC</td>
		<td contenteditable="true" id="lig_6_col_1" class='ciffrageMOA'>5</td>
		<td contenteditable="true" id="lig_6_col_2" class='ciffrageMOA'>6</td>
	</tr>
        
	<tr>
		<td id="total" >Total (j/h)</td>
		<td id="somme_1" class='somme'></td>
		<td id="somme_2" class='somme'></td>
	</tr>
</tbody>
  <tfoot>
    <tr>
        <td colspan="3">
            <input type='button' class='boutonValider' id='validerBesoin'  value='Valider' onclick="mergeBesoinIntoDBbyField('MOE', 'update');" />                 
</td>
    </tr>
  </tfoot>
</table>
                        
                        
                        
                        
<table>
    <tbody>
        <tr>
            <th class="col1">
    <div class='bouton'>
        <div class="showhide">
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='chiffrageMoe'>                        
            <label class="showhide-label" for="chiffrageMoe">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Chiffrage des travaux MOE
    </div>
</td>
</tr>
</tbody>
</table>

<table id='chiffrageMoeBloc' class='tableParamStyle roundCornerTable dpc'>
<thead>
<tr>
<th>
Chiffrage des travaux par Domaine
</th>
<th>
j/h estimé
</th>
<th>
j/h realisé
</th>
</tr>
</thead>
<tbody>
        <tr>
		<td contenteditable="true" id="chiffrage">Dir Coordination Etudes et Dev SI</td>
		<td contenteditable="true" id="lig_0_col_3" class='ciffrageMOE'>2</td>
		<td contenteditable="true" id="lig_0_col_4" class='ciffrageMOE'>3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="chiffrage">Dir Coordination Infrastructure et Production</td>
		<td contenteditable="true" id="lig_1_col_3" class='ciffrageMOE'>5</td>
		<td contenteditable="true" id="lig_1_col_4" class='ciffrageMOE'>6</td>
	</tr>
		
        <tr>
		<td contenteditable="true" id="chiffrage">Dir Développements SI</td>
		<td contenteditable="true" id="lig_2_col_3" class='ciffrageMOE'>2</td>
		<td contenteditable="true" id="lig_2_col_4" class='ciffrageMOE'>3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="chiffrage">Dir Progiciels hors C24 et Intranet</td>
		<td contenteditable="true" id="lig_3_col_3" class='ciffrageMOE'>5</td>
		<td contenteditable="true" id="lig_3_col_4" class='ciffrageMOE'>6</td>
	</tr>
        
        <tr>
		<td contenteditable="true" id="chiffrage">Dir Referentiel SI et Décisionnel</td>
		<td contenteditable="true" id="lig_4_col_3" class='ciffrageMOE' size="14" maxlength="14">2</td>
		<td contenteditable="true" id="lig_4_col_4" class='ciffrageMOE' size="14" maxlength="14">3</td>
	</tr>
			 
	<tr>
		<td contenteditable="true" id="chiffrage">Dir Urbanisation du SI et Intégration</td>
		<td contenteditable="true" id="fixe" class='ciffrageMOE'><input name="Fixe" type="text" id="lig_5_col_3" style="border:1px solid #CCCCCC; background-color:#FFFFCC; height:16px; " onblur="" size="14" maxlength="14"></td>
		<td contenteditable="true" id="fixe" class='ciffrageMOE'><input name="Fixe" type="text" id="lig_5_col_4" style="border:1px solid #CCCCCC; background-color:#FFFFCC; height:16px; " onblur="" size="14" maxlength="14"></td>
	</tr>
        
	<tr>
		<td id="total" >Total (j/h)</td>
		<td id="somme_3" class='somme'></td>
		<td id="somme_4" class='somme'></td>
	</tr>
</tbody>
  <tfoot>
    <tr>
        <td colspan="3">
            <input type='button' class='boutonValider' id='validerBesoin'  value='Valider' onclick="mergeBesoinIntoDBbyField('MOE', 'update');" />                 
</td>
    </tr>
  </tfoot>
</table>

<table>
    <tbody>
        <tr>
            <th class="col1">
    <div class='bouton'>
        <div class="showhide">
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='planification'>                        
            <label class="showhide-label" for="planification">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Planification
    </div>
</td>
</tr>
</tbody>
</table>

        <table id="planificationBloc">
            <tbody><tr>
                    <th class="col1">
                        <label for="field-niveauprojet">Niveau Projet:</label>
                    </th>
                    <td class="col1">
                        <select id="field-niveauprojet" name="field_niveauprojet">
                            <option></option>
                            <option value="AMELIORATION FONCTIONNEMENT CHEQUEPARGNE">AMELIORATION FONCTIONNEMENT CHEQUEPARGNE</option><option value="AMELIORATION PROCESSUS GESTION DES SUSPENS">AMELIORATION PROCESSUS GESTION DES SUSPENS</option><option value="AMELIORATION_CARTE_DE_CREDIT">AMELIORATION_CARTE_DE_CREDIT</option><option value="AMELIORATION_OD_RDI">AMELIORATION_OD_RDI</option><option value="BANQUE PRIVEE">BANQUE PRIVEE</option><option value="BFI MARCHE ETAPE 2">BFI MARCHE ETAPE 2</option><option value="BFI TITRE 1-2-3">BFI TITRE 1-2-3</option><option value="BFI_INTRANET">BFI_INTRANET</option><option value="BIAT CASH">BIAT CASH</option><option value="BORNE INTERACTIVE ETAPE1">BORNE INTERACTIVE ETAPE1</option><option value="CARTE CORPORATE">CARTE CORPORATE</option><option value="CARTE DE CREDIT SAFIR">CARTE DE CREDIT SAFIR</option><option value="CARTE PREPAYEE INTERNATIONALE">CARTE PREPAYEE INTERNATIONALE</option><option value="CARTE TECHNOLOGIQUE">CARTE TECHNOLOGIQUE</option><option value="CARTE VISA EXPRESS CPTE EN DVS">CARTE VISA EXPRESS CPTE EN DVS</option><option value="CASH POOLING">CASH POOLING</option><option value="CHEQUIER PERSONNALISE">CHEQUIER PERSONNALISE</option><option value="CLOTURE DE COMPTE AVEC CONTROLE DES INSTANCES">CLOTURE DE COMPTE AVEC CONTROLE DES INSTANCES</option><option value="CMT en Devise OD">CMT en Devise OD</option><option value="COMPENSATION MONETIQUE NATIONALE">COMPENSATION MONETIQUE NATIONALE</option><option value="COMPENSATION_MONETIQUE_INTERNATIONALE">COMPENSATION_MONETIQUE_INTERNATIONALE</option><option value="COMPTABILITE MANUELLE">COMPTABILITE MANUELLE</option><option value="COMPTES DORMANTS COMPTABILITE">COMPTES DORMANTS COMPTABILITE</option><option value="CONDITIONS PREFERENTIELLES POUR COMPTE EN DEVISE">CONDITIONS PREFERENTIELLES POUR COMPTE EN DEVISE</option><option value="COURS DE DESAGREGATION">COURS DE DESAGREGATION</option><option value="CREDIT RENOV">CREDIT RENOV</option><option value="CREDITS EN DEVISE">CREDITS EN DEVISE</option><option value="CRM GESTION DES CONTACTES ETAPE 3">CRM GESTION DES CONTACTES ETAPE 3</option><option value="CRM GESTION DES CONTACTS ETAPE2">CRM GESTION DES CONTACTS ETAPE2</option><option value="DECLARATION > 5000 TND SOUS DELEGATAIRE">DECLARATION &gt; 5000 TND SOUS DELEGATAIRE</option><option value="DEVELOPPEMENT PME">DEVELOPPEMENT PME</option><option value="E-BANKING">E-BANKING</option><option value="E-BANKING ETAPE2">E-BANKING ETAPE2</option><option value="EDITION ECHELLES INTERET">EDITION ECHELLES INTERET</option><option value="ENGAGEMENT LOT3 PROCESSUS CREDITS DE GESTION">ENGAGEMENT LOT3 PROCESSUS CREDITS DE GESTION</option><option value="ENGAGEMENT PROCESSUS DE CREDIT DE GESTION PHASE 1">ENGAGEMENT PROCESSUS DE CREDIT DE GESTION PHASE 1</option><option value="ENRICHISSEMENT OD INTERNATIONAL">ENRICHISSEMENT OD INTERNATIONAL</option><option value="ENTREE EN RELATION A DISTANCE TRE">ENTREE EN RELATION A DISTANCE TRE</option><option value="ENTREE EN RELATION A DISTANCE TRE.2">ENTREE EN RELATION A DISTANCE TRE.2</option><option value="EXECDENTS DE CAISSE">EXECDENTS DE CAISSE</option><option value="FIABILISATION_DES_DONNEES_CLIENT">FIABILISATION_DES_DONNEES_CLIENT</option><option value="GAB CASH DEPOSIT">GAB CASH DEPOSIT</option><option value="GAB CHANGE">GAB CHANGE</option><option value="Gestion des frais de tenue de comptes professionnels en DVS">Gestion des frais de tenue de comptes professionnels en DVS</option><option value="GESTION DES PACKS">GESTION DES PACKS</option><option value="GESTION RETOUR COURRIER">GESTION RETOUR COURRIER</option><option value="GESTION_DES_CONTACTS">GESTION_DES_CONTACTS</option><option value="GESTION_DES_CONTACTS">GESTION_DES_CONTACTS</option><option value="GIP">GIP</option><option value="IMPAYES EN DEVISE">IMPAYES EN DEVISE</option><option value="INTEGRATION T24-HR ACCESS">INTEGRATION T24-HR ACCESS</option><option value="INTEGRATION_OPVCM">INTEGRATION_OPVCM</option><option value="INTERFACE MEGARA SVT T24">INTERFACE MEGARA SVT T24</option><option value="INTERNATIONAL GBIE">INTERNATIONAL GBIE</option><option value="INTERNATIONAL REMISE IMPORT">INTERNATIONAL REMISE IMPORT</option><option value="INTERNATIONAL_CDI">INTERNATIONAL_CDI</option><option value="INTERNATIONAL_CGBIR">INTERNATIONAL_CGBIR</option><option value="INTERNATIONAL_F1F2">INTERNATIONAL_F1F2</option><option value="INTERNATIONAL_FDE">INTERNATIONAL_FDE</option><option value="INTERNATIONAL_FDI">INTERNATIONAL_FDI</option><option value="INTERNATIONAL_GF">INTERNATIONAL_GF</option><option value="INTERNATIONAL_GFI">INTERNATIONAL_GFI</option><option value="INTERNATIONAL_RE">INTERNATIONAL_RE</option><option value="INTERNATIONAL_TCE">INTERNATIONAL_TCE</option><option value="INTERNATIONAL_TE">INTERNATIONAL_TE</option><option value="INTERNATIONAL_TR">INTERNATIONAL_TR</option><option value="INTERNATIONAL_TR_ETAPE2">INTERNATIONAL_TR_ETAPE2</option><option value="INTERNATIONAL_TRANSVERSE">INTERNATIONAL_TRANSVERSE</option><option value="KYC">KYC</option><option value="LOI FATCA">LOI FATCA</option><option value="MAINTENANCE">MAINTENANCE</option><option value="MAINTENANCE-MDP">MAINTENANCE-MDP</option><option value="MEGARA">MEGARA</option><option value="MIGRATION COMPLEMENT DE CREDITS">MIGRATION COMPLEMENT DE CREDITS</option><option value="MOBILE BANKING">MOBILE BANKING</option><option value="MODULE GENERIQUE DE COMMUNICATION AVEC T24">MODULE GENERIQUE DE COMMUNICATION AVEC T24</option><option value="NEGO DES DEVISES AUPRES DE BNQS CONSOEURS">NEGO DES DEVISES AUPRES DE BNQS CONSOEURS</option><option value="NOSTRO">NOSTRO</option><option value="OD_BFI_TITRES (GR96)">OD_BFI_TITRES (GR96)</option><option value="OD_GR84_ETAPE2">OD_GR84_ETAPE2</option><option value="OD_INT_CDE">OD_INT_CDE</option><option value="OD_INT_ENCAISS.CHQ (GR84)">OD_INT_ENCAISS.CHQ (GR84)</option><option value="OPERATIONS ANNEXES CHANGE">OPERATIONS ANNEXES CHANGE</option><option value="OPTION DE CHANGE">OPTION DE CHANGE</option><option value="PACK BUSNESS">PACK BUSNESS</option><option value="PACK LOW COST">PACK LOW COST</option><option value="PACK OFFRE HAUT DE GAMME.2">PACK OFFRE HAUT DE GAMME.2</option><option value="PACK PRO SANTE">PACK PRO SANTE</option><option value="PACK TOUNESSNA DEVISES">PACK TOUNESSNA DEVISES</option><option value="PACKS OFFRE HAUT DE GAMME">PACKS OFFRE HAUT DE GAMME</option><option value="PACKS OFFRE JEUNES">PACKS OFFRE JEUNES</option><option value="PRELEVEMENT EMIS ET RECUS HORS PLATEFORME">PRELEVEMENT EMIS ET RECUS HORS PLATEFORME</option><option value="PROCESS CREDITS DE GESTION REVOLVING">PROCESS CREDITS DE GESTION REVOLVING</option><option value="PROCESSUS CREDITS A L IMMOBILIER">PROCESSUS CREDITS A L IMMOBILIER</option><option value="PROCESSUS CREDITS INVESTISSEMENT">PROCESSUS CREDITS INVESTISSEMENT</option><option value="PROCESSUS CREDITS SUR RESSOURCES SPECIALES">PROCESSUS CREDITS SUR RESSOURCES SPECIALES</option><option value="PROCESSUS DES CREDIT REVOLVING">PROCESSUS DES CREDIT REVOLVING</option><option value="PROJET BFI MARCHE FOREX">PROJET BFI MARCHE FOREX</option><option value="PROJET CHEQUE 32/33">PROJET CHEQUE 32/33</option><option value="PROJET CONTENTIEUX LOT1">PROJET CONTENTIEUX LOT1</option><option value="PROJET DECLARATION MONATANT > 5000 TND">PROJET DECLARATION MONATANT &gt; 5000 TND</option><option value="PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT1">PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT1</option><option value="PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT2">PROJET_MIGRATION_HR_ACCESS_DE_V5_A_SUITE9 LOT2</option><option value="RATIO_DE_LIQUIDITE">RATIO_DE_LIQUIDITE</option><option value="RECOUVREMENT AGENCE">RECOUVREMENT AGENCE</option><option value="REFERENTIEL ENGAGEMENTS ETAPE 1">REFERENTIEL ENGAGEMENTS ETAPE 1</option><option value="REFONTE DECLARATION DE L?EMPLOYEUR LOT2">REFONTE DECLARATION DE L?EMPLOYEUR LOT2</option><option value="REFONTE_DECLARATION_DE_L_EMPLOYEUR">REFONTE_DECLARATION_DE_L_EMPLOYEUR</option><option value="REMPLACEMENT GRAPPE 91/39(OD / CDE)">REMPLACEMENT GRAPPE 91/39(OD / CDE)</option><option value="REMPLACEMENT GRAPPE 95(OD)">REMPLACEMENT GRAPPE 95(OD)</option><option value="REMPLACEMENT_GRAPPE97_OD">REMPLACEMENT_GRAPPE97_OD</option><option value="REVISION OPERATION CLOTURE DE COMPTE">REVISION OPERATION CLOTURE DE COMPTE</option><option value="SITE DE TELECHARGEMENT">SITE DE TELECHARGEMENT</option><option value="TARIFICATION / ECHELLES INTERET">TARIFICATION / ECHELLES INTERET</option><option value="TARIFICATION PREFERENTIELLE PAR TRANSACTION">TARIFICATION PREFERENTIELLE PAR TRANSACTION</option><option value="TELEMATIQUE 1">TELEMATIQUE 1</option><option value="TOILETTAGE CHEQUES EN BO">TOILETTAGE CHEQUES EN BO</option><option value="TRAITEMENT DES CARTES DE CREDIT SUR T24">TRAITEMENT DES CARTES DE CREDIT SUR T24</option><option value="TRAITEMENT OBLIGATION CAUTIONNEE VIA TTN">TRAITEMENT OBLIGATION CAUTIONNEE VIA TTN</option><option value="TRANSFERT AU CONTENTIEUX ETAPE 2">TRANSFERT AU CONTENTIEUX ETAPE 2</option><option value="TRANSFERT ENGAGEMENTS COMPTE A COMPTE">TRANSFERT ENGAGEMENTS COMPTE A COMPTE</option><option value="TRANSFERT ENGAGEMENTS ENTRE AGENCES">TRANSFERT ENGAGEMENTS ENTRE AGENCES</option><option value="TRANSFERT TOUNESSNA">TRANSFERT TOUNESSNA</option><option value="UTILISATION CHEQUIER BIAT/BCT">UTILISATION CHEQUIER BIAT/BCT</option><option value="VIREMENTS RECUS VERS SC">VIREMENTS RECUS VERS SC</option><option value="VIREMENTS_HORS_PLATEFORME">VIREMENTS_HORS_PLATEFORME</option><option value="WESTERNE UNION">WESTERNE UNION</option><option value="WS MULTIVIR T24">WS MULTIVIR T24</option><option value="WS MXP T24">WS MXP T24</option><option value="WS__MXP_T24_CHARGEMENT">WS__MXP_T24_CHARGEMENT</option>
                        </select>
                    </td>
                </tr><tr>
                    <th class="col2">
                        <label for="field-projettrac">Projet TRAC concerné:</label>
                    </th>
                    <td class="col2">
                        <select id="field-projettrac" name="field_projettrac">
                            <option></option>
                            <option value=""></option><option value="ANOMALIES_T24">ANOMALIES_T24</option><option value="BFI_CARTAGO_INTRANET">BFI_CARTAGO_INTRANET</option><option value="BFI_TITRE">BFI_TITRE</option><option value="CONTENTIEUX">CONTENTIEUX</option><option value="GTI_WINSERGE">GTI_WINSERGE</option><option value="HR_ACCESS">HR_ACCESS</option><option value="MXP">MXP</option><option value="OGC">OGC</option><option value=""></option>
                        </select>
                    </td>
                </tr><tr>
                    <th class="col1">
                        <label for="field-nature_traitement">Nature de traitement:</label>
                    </th>
                    <td class="col1">
                        <select id="field-nature_traitement" name="field_nature_traitement">
                            <option></option>
                            <option value=""></option><option value="PROJET">PROJET</option><option value="MAINTENANCE">MAINTENANCE</option>
                        </select>
                    </td>
                </tr><tr>
                    <th class="col2">
                        <label for="field-refticket">Ref ticket maintenance:</label>
                    </th>
                    <td class="col2">
                        <input type="text" id="field-refticket" name="field_refticket">
                    </td>
                </tr><tr>
                    <th class="col1">
                        <label for="field-type_traitement">Type de traitement:</label>
                    </th>
                    <td class="col1">
                        <select id="field-type_traitement" name="field_type_traitement">
                            <option></option>
                            <option value=""></option><option value="RELEASE">RELEASE</option><option value="HOTFIX">HOTFIX</option><option value="MODIFICATION_A_CHAUD">MODIFICATION_A_CHAUD</option>
                        </select>
                    </td>
                </tr><tr>
                    <th class="col2">
                        <label for="field-num_release">Num Release:</label>
                    </th>
                    <td class="col2">
                        <select id="field-num_release" name="field_num_release">
                            <option></option>
                            <option value="R11">R11</option><option value="R12">R12</option><option value="R13">R13</option><option value="R14">R14</option><option value="R15">R15</option><option value="R16">R16</option><option value="R17">R17</option><option value="R18">R18</option><option value="R19">R19</option><option value="R20">R20</option>
                        </select>
                    </td>
                </tr><tr>
                    <th class="col1">
                        <label for="field-date_demarrage">Date de démarrage:</label>
                    </th>
                    <td  class="tdDemi">
                        <p class="contenu">                       
                            <input type="text" class="datePicker" id="date_demarrage"/>
                        </p>
                    </td>
                </tr><tr>
                    <th class="col2">
                        <label for="field-date_app_prod">Date Application en Prod:</label>
                    </th>
                    <td  class="tdDemi">
                        <p class="contenu">                       
                            <input type="text"  class="datePicker" id="date_appliation"/>
                        </p>
                    </td>
                </tr><tr>
                    <th class="col1">
                        <label for="field-invest">Investissement:</label>
                    </th>
                    <td class="col1" colspan="3">
                        <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="field-invest" name="field_invest" cols="120" rows="5" class="wikitext trac-resizable"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: 391px;"></div></div></div>
                    </td>
                </tr><tr>
                    <th>
                      <label for="field-commentairepmo">Commentaires:</label>
                    </th>
                   <td class="fullrow" colspan="3">
<fieldset>
                            <div class="wikitoolbar">
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="strong" title="Bold text: '''Example'''" tabindex="400" style="background-image: url('images/strong.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="em" title="Italic text: ''Example''" tabindex="400" style="background-image: url('images/em.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="heading" title="Heading: == Example ==" tabindex="400" style="background-image: url('images/heading.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="link" title="Link: [http://www.example.com/ Example]" tabindex="400" style="background-image: url('images/link.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="hr" title="Horizontal rule: ----" tabindex="400" style="background-image: url('images/hr.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="np" title="New paragraph" tabindex="400" style="background-image: url('images/np.png')">    
                                <input type="button" onclick="wikiButtonClick(this);" class="wikiButton" id="br" title="Line break: [[BR]]" tabindex="400" style="background-image: url('images/br.png')">
                            </div>
                            <div class="wikitoolbar"><a href="#" id="strong" title="Texte en gras: '''Exemple'''" tabindex="400"></a><a href="#" id="em" title="Texte en italique: ''Exemple''" tabindex="400"></a><a href="#" id="heading" title="Titre: == Exemple ==" tabindex="400"></a><a href="#" id="link" title="Lien: [http://www.exemple.com/ Exemple]" tabindex="400"></a><a href="#" id="code" title="Bloc de code: {{{ exemple }}}" tabindex="400"></a><a href="#" id="hr" title="Filet horizontal: ----" tabindex="400"></a><a href="#" id="np" title="Nouveau paragraphe" tabindex="400"></a><a href="#" id="br" title="Saut de ligne: [[BR]]" tabindex="400"></a><a href="#" id="img" title="Image: [[Image()]]" tabindex="400"></a></div><div class="trac-resizable"><div><textarea id="field-description" name="field_description" class="wikitext trac-resizable" rows="10" cols="68"></textarea><div class="trac-grip" style="margin-left: 2px; margin-right: -8px;"></div></div></div>
                      </fieldset>
                    </td>
                </tr><tr>
                    <th class="col1">
                      <label for="field-responsablepmo">Responsable PMO:</label>
                    </th>
                    <td class="col1">
                            <input type="text" id="field-responsablepmo" name="field_responsablepmo" value="nizar.jarboui">
                    </td>
                    </tr><tr>
                    <th class="col2">
                      <label for="field-owner">Propriétaire:</label>
                    </th>
                    <td class="col2">
                        <select id="field-owner" name="field_owner">
                          <option></option>
                          <option value="< default >">&lt; default &gt;</option><option value="abdeljalil.regaieg">abdeljalil.regaieg</option><option value="abdeljlil.regaieg">abdeljlil.regaieg</option><option value="ahmed.lasram">ahmed.lasram</option><option value="ali">ali</option><option value="anis.moalla">anis.moalla</option><option value="fares hamza">fares hamza</option><option value="fares.hamza">fares.hamza</option><option value="faten slim">faten slim</option><option value="faten.slim">faten.slim</option><option value="ferjani.riahi">ferjani.riahi</option><option value="hamza ouri (talys consulting)">hamza ouri (talys consulting)</option><option value="kaouther.hachicha">kaouther.hachicha</option><option value="moe1">moe1</option><option value="mohamed.bouattour">mohamed.bouattour</option><option value="mohsen ouertani">mohsen ouertani</option><option value="mohsen.ouertani">mohsen.ouertani</option><option value="molka borchani">molka borchani</option><option value="molka.borchani">molka.borchani</option><option value="moncef mallek">moncef mallek</option><option value="mouna.laroussi">mouna.laroussi</option><option value="nasreddine.rouabeh">nasreddine.rouabeh</option><option value="nizar.jarboui">nizar.jarboui</option><option value="raafet">raafet</option><option value="raafet dormok">raafet dormok</option><option value="riadh lamouri">riadh lamouri</option><option value="safwen ben said">safwen ben said</option><option value="safwen.ben.said">safwen.ben.said</option><option value="safwen.bensaid">safwen.bensaid</option><option value="samir jribi">samir jribi</option><option value="sonia.moalla">sonia.moalla</option><option value="yamen khemiri">yamen khemiri</option>
                        </select>
                    </td>
                </tr>

                <tr>
            <td style="text-align: center" colspan="2">
                <br/>
                <input type="button" class="boutonValider"  value="Enregister" onclick="" />
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
            <input type='checkbox' name='showhide' class='showhide-checkbox' id='autres'>                        
            <label class="showhide-label" for="autres">
                <div class="showhide-inner"></div>
                <div class="showhide-switch"></div>
            </label>
        </div>                    
    </div>
</th>

<td class="col1">

    <div class='titre' style="font-weight: bold; background-color: #DCDCDC;">
        Autres
    </div>
</td>
</tr>
</tbody>       
    </table>
    
                        
                        
                        
                      
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
                        
<input name="Fixe" type="text" id="Fixe" style="border:1px solid #CCCCCC; background-color:#FFFFCC; height:16px; " onblur="" size="14" maxlength="14">  
                        
                        
                        
                        
                        
     </div>
            </td>            
        </tr>
    
</table>
</center>

<script>
function formatTel(cible) {
    var temp = $(cible).value;
    temp = temp.replace(/ /gi, "");
    if(temp.length > 10) return;
    if(isNaN(temp) == true) {
        $(cible).value = $(cible).value.substring(0, $(cible).value.length -1)
        return
    }
    ($(cible).value.length == 2 || $(cible).value.length == 5 || $(cible).value.length == 8 || $(cible).value.length == 11) ? $(cible).value += ' ' : $(cible).value;
}
 
function formatDateNaissance(cible) {
    var temp = $(cible).value;
    temp = temp.replace(/\//gi, "");
    if(temp.length > 8) return;
    if(isNaN(temp) == true) {
        $(cible).value = $(cible).value.substring(0, $(cible).value.length -1)
        return
    }
    ($(cible).value.length == 2 || $(cible).value.length == 5 ) ? $(cible).value += '/' : $(cible).value;
}


                                                    $(document).ready(function() {
                                                        $("#specProjetContent").show();


                                                    });
//Defnir nbr de colonnes de calcul de somme

                                                    var nbrColonneDeCalcul = 4;
                                                    //Initialisation colonnes de calcul de somme
                                                    for (i = 1; i <= nbrColonneDeCalcul; i++) {
                                                        var somme = 0;
                                                        $('[id*="_col_' + i + '"]').each(function() {
                                                            var val = $(this).text();
                                                            if ($.isNumeric(val)) {
                                                                somme += parseInt(val);
                                                            }
                                                        });
                                                        $("#somme_" + i).text(somme);
                                                    }
                                                    //Calcul de somme pour chaque modification
                                                    $("td").on("keyup", function() {
                                                        if ($(this).hasClass("ciffrageMOA")) {
                                                            var num_col = $(this).index();
                                                        } else if ($(this).hasClass("ciffrageMOE")) {
                                                            var num_col = $(this).index() + 2;
                                                        }
                                                        var somme = 0;
                                                        $('[id*="_col_' + num_col + '"]').each(function() {
                                                            var val = $(this).text();
                                                            if ($.isNumeric(val)) {
                                                                somme += parseInt(val);
                                                            }
                                                        });
                                                        $("#somme_" + num_col).text(somme);
                                                    });






                                                    $("input").click(function() {
                                                        $("#" + this.id + "Bloc").toggle();
                                                    });

                                                    //DATE TIME PICKER
                                                    $('#date_demarrage').datetimepicker({
                                                        formatTime: 'H:i',
                                                        formatDate: 'd-m-Y',
                                                        defaultTime: '08:00',
                                                        format: 'd-m-Y H:i',
                                                        step: 10
                                                    });
                                                    $('#date_appliation').datetimepicker({
                                                        formatTime: 'H:i',
                                                        formatDate: 'd-m-Y',
                                                        defaultTime: '17:00',
                                                        format: 'd-m-Y H:i',
                                                        step: 10
                                                    });
                                                    $('#date_demande').datetimepicker({
                                                        formatTime: 'H:i',
                                                        formatDate: 'd-m-Y',
                                                        defaultTime: '08:00',
                                                        format: 'd-m-Y H:i',
                                                        step: 10
                                                    });
                                                    $('#date_livraison').datetimepicker({
                                                        formatTime: 'H:i',
                                                        formatDate: 'd-m-Y',
                                                        defaultTime: '17:00',
                                                        format: 'd-m-Y H:i',
                                                        step: 10
                                                    });
</script>
 