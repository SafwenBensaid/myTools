<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<sec:authorize access='hasAnyRole("GESTION_PROJETS")'>
    <div class="titreMenus"><span class="GESTION_PROJETS">+ GESTION DES PROJETS</span></div>
    <ul id="css3menu9" class="topmenu menuvertical GESTION_PROJETS_menu">


        <sec:authorize access='hasAnyRole("VUE_CONSOLIDEE")'>
            <li class="topmenu">
                <a class="menuItem" id="menuVueConsolidee" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Vue Consolidée
                    </span>
                </a>
                <ul>

                    <li class="topfirst">
                        <a href="./vueConsolidee.do" class="pressed menuItem" style="width:250px;height:26px;line-height:26px">
                            <span>
                                <img src="images/menu_icons/screen.png" alt=""/>
                                Vue Consolidée des Activités DevOps
                            </span>
                        </a>
                    </li> 

                    <sec:authorize access='hasAnyRole("PAGE_DE_TEST")'>
                        <li class="topfirst">
                            <a href="./test.do" class="pressed menuItem" style="width:250px;height:26px;line-height:26px">
                                <span>
                                    <img src="images/menu_icons/screen.png" alt=""/>
                                    TEST
                                </span>
                            </a>
                        </li> 
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize access='hasAnyRole("CHALLENGE_24")'>
            <li class="topmenu">
                <a class="menuItem" id="menuT24" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        T24</span>
                </a>
                <ul id="css3menu1" class="topmenu challenge24_menu">

                    <sec:authorize access='hasAnyRole("ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topfirst">
                            <a href="./AccesAuxEnvironnements.do"  class="sousMenu" title="Accès aux environnements physiques" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("SUIVI_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Suivi & Reporting C. HOTFIX
                                </span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("ETAT_AVANCEMENT_HOTFIXES")'>
                                    <li>
                                        <a href="./SuiviDesHotfix.do"  title="Suivi de tous les Hotfix qualifiés, non qualifiés, déployés et non déployés." class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Etat d'avancement des Hotfix</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("AUTORISATION HOTFIXES")'>
                                    <li>
                                        <a href="./AutorisationDesHotfix.do" title="Autorisation des HOTFIX en attente de prise en charge pour traitement durant la journée." class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Autorisation de prise en charge des Hotfix</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("T24_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=LIVRAISONS_T24&menuItem=menuT24" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>   

                                <sec:authorize access='hasAnyRole("RELANCES_HOTFIX_TEST")'>
                                    <li>
                                        <a href="./RelanceDesHotfix.do" title="Relance d'un bordereau de livraison HOTFIX pour test." class="sousMenu">
                                            <img src="images/menu_icons/activity.png" alt=""/>
                                            Relance des HOTFIX pour test</a>
                                    </li>
                                </sec:authorize>    

                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("SUIVI_REPORTING_CR")'>
                        <li class="topfirst">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Suivi & Reporting C. RELEASE
                                </span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("STATISTIQUE_RELEASE_EN_COURS_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./SuiviReleaseEnCours.do?filtre=domaine" title="Suivi de l'état d'avancement de toutes les anomalies de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques release en cours par domaine</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("STATISTIQUE_RELEASE_EN_COURS_PAR_NIV_PROJ")'>
                                    <li>
                                        <a href="./SuiviReleaseEnCours.do?filtre=projet" title="Suivi de l'état d'avancement de toutes les anomalies de la release en cours par niveau projet." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques release en cours par projet</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("STATISTIQUE_RELEASE_EN_COURS_MAINTENANCE")'>
                                    <li>
                                        <a href="./SuiviReleaseEnCours.do?filtre=maintenance" title="Suivi de l'état d'avancement de toutes les anomalies du projet maintenance." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques release en cours projet maintenance</a>
                                    </li>
                                </sec:authorize>

                                <li>
                                    <a href="./AutorisationDesTicketsMaintenance.do" title="Autorisation des tickets Maintenance à intégrer sur le circuit Release." class="sousMenu">
                                        <img src="images/menu_icons/camera2.png" alt=""/>
                                        Autorisation de prise en charge des tickets Maintenance</a>
                                </li>

                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("SUIVI_REPORTING_CP")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Suivi & Reporting C. PROJET
                                </span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("STATISTIQUE_RELEASE_EN_COURS_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./SuiviReleaseEnCours.do?filtre=circuitProjet" title="Suivi de l'état d'avancement de toutes les anomalies de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Suivi & Reporting Circuit PROJET</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("SUIVI_REPORTING_CU")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Suivi & Reporting C. UPGRADE
                                </span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("STATISTIQUE_RELEASE_EN_COURS_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./SuiviReleaseEnCours.do?filtre=circuitUpgrade" title="Suivi de l'état d'avancement de toutes les anomalies du circuit UPGRADE par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Suivi & Reporting Circuit UPGRADE</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ACTIVITE_CDD")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Activité DEV</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("LIVRAISONS_SELF_SERVICES")'>
                                    <li>
                                        <a href="./getAllMilestonesForm.do?acteur=CDD" title="Livraisons Self-Service" class="sousMenu">
                                            <img src="images/menu_icons/briefcase2.png" alt=""/>
                                            Livraisons Self-Service</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ACTIVITE_OV")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/clock.png" alt=""/>
                                    Activité OV</span>
                            </a>
                            <ul>

                                <sec:authorize access='hasAnyRole("RECETTE_TECHNIQUE_DES_LIVRAISONS")'>
                                    <li>
                                        <a href="./getAllMilestonesForm.do?acteur=OV" title="Extraction et contrôle des objets livrés, déploiement, versionning et aiguillage du bordereau de livraison." class="sousMenu">
                                            <img src="images/menu_icons/briefcase2.png" alt=""/>
                                            Recette technique des livraisons</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("SUIVI_DEMANDES_COB")'>
                                    <li>
                                        <a href="./SuiviCOBs.do" title="Suivi de COBs" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Suivi de COBs</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("DEPLOIMENT_PARALLELLE_MULTI_PACK")'>
                                    <li>
                                        <a href="./InputDeployerNPacksSurNEnvironnements.do" title="Déploiement de plusieurs packs sur plusieurs environnements en parallèle"  class="sousMenu">
                                            <img src="images/menu_icons/equalizer.png" alt=""/>
                                            Déploiement parallèle multi-pack</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("ETUDE_INTERSECTION")'>
                                    <li>
                                        <a href="./EtudeIntersectionInputObjetsPath.do" title="Etude d'intersection entre une liste d'objets et les différentes branches individuelles actives du dépôt." class="sousMenu">
                                            <img src="images/menu_icons/direction.png" alt=""/>
                                            Etude d'intersection</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("EXTRACTION_PHYSIQUE_OBJETS")'>
                                    <li>
                                        <a href="./PreparationFormerUnPackForm.do" title="Extraction d'une liste d'objets T24 depuis un environnement physique sous forme d'un  pack."  class="sousMenu">
                                            <img src="images/menu_icons/folder1.png" alt=""/>
                                            Extraction physique/svn d'objets</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("CONTROLE_COHERENCE_OBJETS")'>
                                    <li>
                                        <a href="./PreparationComparaisonObjetsForm.do" title="Contrôle de cohérence d'une liste d'objets entre deux environnements physiques ou bien entre un environnement physique et une branche du dépôt. " class="sousMenu">
                                            <img src="images/menu_icons/justice.png" alt=""/>
                                            Contrôle de cohérence d'objets</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("CONTROLE_COHERENCE_PACKS")'>
                                    <li>
                                        <a href="./VerificationDeltaForm.do" title="Contrôle de cohérence entre les objets d'un pack et (un environnement physique ou une branche du dépôt)." class="sousMenu">
                                            <img src="images/menu_icons/justice.png" alt=""/>
                                            Contrôle de cohérence de packs</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("GENERATION_CADENCEMENT_DELTA")'>
                                    <li>
                                        <a href="./Cadencement.do" title="Génération automatique du cadencement d'un Delta T24 à partir du cadencement global" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Génération Cadencement Delta</a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("EXPORTATION_DELTA_PROJETS")'>
                                    <li>
                                        <a href="./ConstitutionPackMultiprojets.do" title="Extraction de tous les objets définissant un ensemble de projets." class="sousMenu">
                                            <img src="images/menu_icons/grid.png" alt=""/>
                                            Constitution d'un delta projets</a>
                                    </li>
                                </sec:authorize> 
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ACTIVITE_IE")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/calendar.png" alt=""/>
                                    Activité I&E</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("TRAITEMENT_DES_LIVRAISONS_IE")'>
                                    <li>
                                        <a href="./getAllMilestonesForm.do?acteur=IE" title="Extraction et contrôle des objets livrés, déploiement, versionning et aiguillage du bordereau de livraison." class="sousMenu">
                                            <img src="images/menu_icons/anchor.png" alt=""/>
                                            Intégration des livraisons
                                        </a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ACTIVITE_METIER")'>
                        <li class="topfirst">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Activité Métiers
                                </span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("CLOTURE_TICKET")'>
                                    <li>
                                        <a href="./ClotureTicketAnomalie.do"  class="sousMenu" title="Clôture de Ticket SFD / Anomalies / Demandes T24" style="width:250px;height:26px;line-height:26px;">
                                            <img src="images/menu_icons/briefcase2.png" alt=""/>
                                            Clôture de Tickets</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ADMINISTRATION")'>
                        <li class="toplast">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/wrench.png" alt=""/>
                                    Administration</span>
                            </a>
                            <ul>

                                <sec:authorize access='hasAnyRole("GESTION_PARAMTRES")'>
                                    <li>
                                        <a href="./GestionBaseDonnesParametrages.do?admin=gestionDesParametres" title="Gestion des paramètres de la plateforme(Création, Suppression et modification)." class="sousMenu">
                                            <img src="images/menu_icons/gear.png" alt=""/>
                                            Paramètres
                                        </a>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("GESTION_ENVIRONNEMENTS")'>
                                    <li>
                                        <a href="./GestionBaseDonnesParametrages.do?admin=gestionDesEnvironnements" title="Gestion des environnements T24 (Création, Suppression et modification)." class="sousMenu">
                                            <img src="images/menu_icons/cloud.png" alt=""/>
                                            Environnements
                                        </a>
                                    </li>
                                </sec:authorize>    

                                <sec:authorize access='hasAnyRole("GESTION_PROJETS")'>
                                    <li>
                                        <a href="./GestionBaseDonnesParametrages.do?admin=gestionNiveauxProjets" title="Gestion des niveaux projets (Création, Suppression et modification)." class="sousMenu">
                                            <img src="images/menu_icons/cabinet.png" alt=""/>
                                            Projets
                                        </a>
                                    </li>
                                </sec:authorize>  

                                <sec:authorize access='hasAnyRole("GESTION_UTILISATEURS")'>
                                    <li class="toplast">
                                        <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                            <span>
                                                <img src="images/menu_icons/wrench.png" alt=""/>
                                                Utilisateurs</span>
                                        </a>
                                        <ul>
                                            <sec:authorize access='hasAnyRole("GESTION_UTILISATEURS")'>
                                                <li>
                                                    <a href="./InitialisationUserHasEnvironnement.do" title="Gestion des profils utilisateurs (Création, Suppression et modification).Affectation d'un utilisateur à un groupe et lui attribuer les environnements dont il a le droit."  class="sousMenu">
                                                        <img src="images/menu_icons/user.png" alt=""/>
                                                        Profils de mon utilisateur
                                                    </a>
                                                </li>
                                            </sec:authorize>

                                            <sec:authorize access='hasAnyRole("AFFECTATION_GROUPES_UTILISATEURS")'>
                                                <li>
                                                    <a href="./UserHasGroupe.do" title="Affectation des groupes d'utilisateurs à des utilisateurs"  class="sousMenu">
                                                        <img src="images/menu_icons/pacman.png" alt=""/>
                                                        Gestion des utilisateurs
                                                    </a>
                                                </li>
                                            </sec:authorize>  

                                            <sec:authorize access='hasAnyRole("GESTION_GROUPES")'>
                                                <li>
                                                    <a href="./GestionGroupesUtilisateurs.do" title="Gestion des groupes d'utilisateurs (Création, Suppression et modification) et définition des différents droits associés à ce groupe."  class="sousMenu">
                                                        <img src="images/menu_icons/users.png" alt=""/>
                                                        Gestion des groupes
                                                    </a>
                                                </li>
                                            </sec:authorize>

                                            <sec:authorize access='hasAnyRole("GESTION_FONCTIONNALITES")'>
                                                <li>
                                                    <a href="./AdministrationFonctionnalite.do" title="Gestion des fonctionnalités (Création, Suppression et modification)." class="sousMenu">
                                                        <img src="images/menu_icons/grid.png" alt=""/>
                                                        Gestion des fonctionnalités
                                                    </a>
                                                </li>
                                            </sec:authorize>
                                        </ul>
                                    </li>
                                </sec:authorize>

                                <sec:authorize access='hasAnyRole("GESTION_REFERENTIEL_OBJETS")'>
                                    <li>
                                        <a href="./GestionReferentielObjet.do" title="Gestion du référentiel d'objets Release" class="sousMenu">
                                            <img src="images/menu_icons/home.png" alt=""/>
                                            Référentiel Objets
                                        </a>
                                    </li>
                                </sec:authorize> 

                                <sec:authorize access='hasAnyRole("ADMINISTRATION_CONTRAITES_INTEGRITES")'>
                                    <li>
                                        <a href="./AdministrationContraintesIntegrite.do" title="Gestion des contraintes d'integrité de déploiement." class="sousMenu">
                                            <img src="images/menu_icons/collapse.png" alt=""/>
                                            Contraintes d'integrité
                                        </a>
                                    </li>
                                </sec:authorize>    

                                <sec:authorize access='hasAnyRole("TICKETS_WORKFLOW")'>
                                    <li>
                                        <a href="./workflow.do" title="Activation et désactivation du traitement des tickets TRAC." class="sousMenu">
                                            <img src="images/menu_icons/transfer.png" alt=""/>
                                            Tickets workflow
                                        </a>
                                    </li>
                                </sec:authorize>

                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("ADMINISTRATION")'>
                        <li class="toplast">
                            <a class="menuItem"  style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/fiche.png" alt=""/>
                                    Références</span>
                            </a>
                            <ul>
                                <li>
                                    <a href="http://172.28.70.74/trac/anomalies_t24/wiki/Phase_LOT2" target="_blank" title="Historique des livraisons des release" class="sousMenu">
                                        <img src="images/menu_icons/fiche.png" alt=""/>
                                        Historique des livraisons des release
                                    </a>
                                </li>
                                <li>
                                    <a href="http://172.28.70.74/trac/anomalies_t24/wiki/Gestion_anoamlies_PORD" target="_blank" title="Principe et Typologie de Gestion des livraisons" class="sousMenu">
                                        <img src="images/menu_icons/fiche.png" alt=""/>
                                        Principe et Typologie de Gestion des livraisons
                                    </a>
                                </li>
                                <li>
                                    <a href="http://172.28.70.74/trac/livraisons_t24/wiki/Gestion" target="_blank" title="Processus de livraison" class="sousMenu">
                                        <img src="images/menu_icons/fiche.png" alt=""/>
                                        Processus de livraison
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>


        <sec:authorize access='hasAnyRole("HR.ACCESS")'>
            <li class="toplast">
                <a class="menuItem" id="menuHRaccess" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        HRaccess</span>
                </a>
                <ul id="css3menu2" class="topmenu hraccess_menu">
                    <sec:authorize access='hasAnyRole("HR.ACCESS_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsHrAccess.do" class="menuItem" title="Accès aux environnements physiques HR Access" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuHRaccess&persistenceUnit=dbTracHrAccessPU&circuit=HOTFIX&critereTri=milestone&projetTrac=HR_ACCESS" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuHRaccess&persistenceUnit=dbTracHrAccessPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=HR_ACCESS" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("HR.ACCESS_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=HR_ACCESS&menuItem=menuHRaccess" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>  
                            </ul>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>                    
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuHRaccess&persistenceUnit=dbTracHrAccessPU&circuit=RELEASE&critereTri=milestone&projetTrac=HR_ACCESS" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("HR.ACCESS_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>                    
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuHRaccess&persistenceUnit=dbTracHrAccessPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=HR_ACCESS" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>                        
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("HRACCESS_ACTIVITE_OV")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Activité OV</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("HRACCESS_RECETTE_TECHNIQUE")'>
                                    <li>
                                        <a href="./getAllTicketsHraccessForm.do?acteur=OVHR" title="Extraction et contrôle des objets livrés, déploiement, versionning et aiguillage du bordereau de livraison." class="sousMenu">
                                            <img src="images/menu_icons/briefcase2.png" alt=""/>
                                            Recette technique des livraisons</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>     
                </ul>
            </li>   
        </sec:authorize>




        <sec:authorize access='hasAnyRole("MXP")'>
            <li class="toplast">
                <a class="menuItem" id="menuMXP" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        MXP</span>
                </a>
                <ul id="css3menu3" class="topmenu MXP_menu">
                    <sec:authorize access='hasAnyRole("MXP_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsMXP.do" class="menuItem" title="Accès aux environnements physiques MXP" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("MXP_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("MXP_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuMXP&persistenceUnit=dbMxpPU&circuit=HOTFIX&critereTri=milestone&projetTrac=MXP" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("MXP_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuMXP&persistenceUnit=dbMxpPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=MXP" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("MXP_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=MXP&menuItem=menuMXP" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("MXP_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("MXP_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuMXP&persistenceUnit=dbMxpPU&circuit=RELEASE&critereTri=milestone&projetTrac=MXP" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("MXP_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuMXP&persistenceUnit=dbMxpPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=MXP' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("HRACCESS_ACTIVITE_OV")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px; color:#696969;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Activité OV</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("HRACCESS_RECETTE_TECHNIQUE")'>
                                    <li>
                                        <a style='color:#696969;' class="sousMenu">
                                            <img src="images/menu_icons/briefcase2.png" alt=""/>
                                            Recette technique des livraisons</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize> 
                </ul>
            </li>   
        </sec:authorize>  







        <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET")'>
            <li class="toplast">
                <a class="menuItem" id="menuBFI" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        BFI CARTAGO INTRANET</span>
                </a>
                <ul id="css3menu4" class="topmenu bfiCartagoIntranet_menu">
                    <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsBfiCarthago.do" class="menuItem" title="Accès aux environnements physiques BFI CARTAGO INTRANET" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuBFI&persistenceUnit=dbBfiCartagoIntranetPU&circuit=HOTFIX&critereTri=milestone&projetTrac=BFI_CARTAGO_INTRANET" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuBFI&persistenceUnit=dbBfiCartagoIntranetPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=BFI_CARTAGO_INTRANET" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=BFI_CARTAGO_INTRANET&menuItem=menuBFI" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>                    
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuBFI&persistenceUnit=dbBfiCartagoIntranetPU&circuit=RELEASE&critereTri=milestone&projetTrac=BFI_CARTAGO_INTRANET" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>

                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_CARTAGO_INTRANET_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>                    
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuBFI&persistenceUnit=dbBfiCartagoIntranetPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=BFI_CARTAGO_INTRANET" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class="sousMenu">
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>  








        <sec:authorize access='hasAnyRole("CONTENTIEUX")'>
            <li class="toplast">
                <a class="menuItem" id="menuCONTENTIEUX" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        CONTENTIEUX</span>
                </a>
                <ul id="css3menu5" class="topmenu CONTENTIEUX_menu">
                    <sec:authorize access='hasAnyRole("CONTENTIEUX_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsContentieux.do" class="menuItem" title="Accès aux environnements physiques CONTENTIEUX" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuCONTENTIEUX&persistenceUnit=dbContentieuxPU&circuit=HOTFIX&critereTri=milestone&projetTrac=CONTENTIEUX" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuCONTENTIEUX&persistenceUnit=dbContentieuxPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=CONTENTIEUX" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("CONTENTIEUX_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=CONTENTIEUX&menuItem=menuCONTENTIEUX" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuCONTENTIEUX&persistenceUnit=dbContentieuxPU&circuit=RELEASE&critereTri=milestone&projetTrac=CONTENTIEUX" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("CONTENTIEUX_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuCONTENTIEUX&persistenceUnit=dbContentieuxPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=CONTENTIEUX' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize> 







        <sec:authorize access='hasAnyRole("GTI_WINSERGE")'>
            <li class="toplast">
                <a class="menuItem" id="menuGTI" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        GTI_WINSERGE</span>
                </a>
                <ul id="css3menu6" class="topmenu  GTI_WINSERGE_menu">
                    <sec:authorize access='hasAnyRole("GTI_WINSERGE_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsGtiWinserge.do" class="menuItem" title="Accès aux environnements physiques GTI_WINSERGE" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuGTI&persistenceUnit=dbGtiWinsergePU&circuit=HOTFIX&critereTri=milestone&projetTrac=GTI_WINSERGE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuGTI&persistenceUnit=dbGtiWinsergePU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=GTI_WINSERGE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("GTI_WINSERGE_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=GTI_WINSERGE&menuItem=menuGTI" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuGTI&persistenceUnit=dbGtiWinsergePU&circuit=RELEASE&critereTri=milestone&projetTrac=GTI_WINSERGE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("GTI_WINSERGE_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuGTI&persistenceUnit=dbGtiWinsergePU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=GTI_WINSERGE' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>  











        <sec:authorize access='hasAnyRole("OGC")'>
            <li class="toplast">
                <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        OGC</span>
                </a>
                <ul id="css3menu7" class="topmenu OGC_menu">
                    <sec:authorize access='hasAnyRole("OGC_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsOGC.do" class="menuItem" title="Accès aux environnements physiques OGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("OGC_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("OGC_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuOGC&persistenceUnit=dbOgcPU&circuit=HOTFIX&critereTri=milestone&projetTrac=OGC" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("OGC_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuOGC&persistenceUnit=dbOgcPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=OGC" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("OGC_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=OGC&menuItem=menuOGC" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("OGC_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("OGC_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuOGC&persistenceUnit=dbOgcPU&circuit=RELEASE&critereTri=milestone&projetTrac=OGC" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("OGC_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuOGC&persistenceUnit=dbOgcPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=OGC' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>  







        <sec:authorize access='hasAnyRole("BFI_TITRE")'>
            <li class="toplast">
                <a class="menuItem" id="menuTITRE" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        BFI_TITRE</span>
                </a>
                <ul id="css3menu9" class="topmenu BFI_TITRE_menu">
                    <sec:authorize access='hasAnyRole("BFI_TITRE_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsBfiTitre.do" class="menuItem" title="Accès aux environnements physiques BFI_TITRE" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuTITRE&persistenceUnit=dbBfiTitrePU&circuit=HOTFIX&critereTri=milestone&projetTrac=BFI_TITRE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuTITRE&persistenceUnit=dbBfiTitrePU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=BFI_TITRE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_TITRE_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=BFI_TITRE&menuItem=menuTITRE" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuTITRE&persistenceUnit=dbBfiTitrePU&circuit=RELEASE&critereTri=milestone&projetTrac=BFI_TITRE" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("BFI_TITRE_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuTITRE&persistenceUnit=dbBfiTitrePU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=BFI_TITRE' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>  









        <sec:authorize access='hasAnyRole("ICR")'>
            <li class="toplast">
                <a class="menuItem" id="menuICR" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        ICR / PCA Agences</span>
                </a>
                <ul id="css3menu11" class="topmenu ICR_menu">
                    <sec:authorize access='hasAnyRole("ICR_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsICR.do" class="menuItem" title="Accès aux environnements physiques ICR" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("ICR_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("ICR_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuICR&persistenceUnit=dbICRPU&circuit=HOTFIX&critereTri=milestone&projetTrac=ICR" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("ICR_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuICR&persistenceUnit=dbICRPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=ICR" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("ICR_JOURNAL_HOTFIX")'>
                                    <li>
                                        <a href="./JournalHfAppliquesProd.do?projet=ICR&menuItem=menuICR" title="Journal des Hotfix appliqués sur PROD" class="sousMenu">
                                            <img src="images/menu_icons/camera2.png" alt=""/>
                                            Journal Hotfix appliqués sur PROD</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("ICR_REPORTING_CR")'>
                        <li class="topmenu">
                            <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.R</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("ICR_REPORTING_CR_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuICR&persistenceUnit=dbICRPU&circuit=RELEASE&critereTri=milestone&projetTrac=ICR" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par domaine</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("ICR_REPORTING_CR_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href='./StatistiquesnewTrac.do?menuItem=menuICR&persistenceUnit=dbICRPU&circuit=RELEASE&critereTri=nature_traitement&projetTrac=MXP' title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone." class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Release par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>
        
        <sec:authorize access='hasAnyRole("GESTION_COURRIER")'>
            <li class="toplast">
                <a class="menuItem" id="menuGESTION_COURRIER" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        GESTION COURRIER</span>
                </a>
                <ul id="css3menu7" class="topmenu OGC_menu">
                    <sec:authorize access='hasAnyRole("GESTION_COURRIER_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsGestionCourrier.do" class="menuItem" title="Accès aux environnements physiques GESTION_COURRIER" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("GESTION_COURRIER_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("GESTION_COURRIER_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuGESTION_COURRIER&persistenceUnit=dbGestionCourrierPU&circuit=HOTFIX&critereTri=milestone&projetTrac=gestion_courrier" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("GESTION_COURRIER_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuGESTION_COURRIER&persistenceUnit=dbGestionCourrierPU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=gestion_courrier" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>    

        <sec:authorize access='hasAnyRole("OPTION_CHANGE")'>
            <li class="toplast">
                <a class="menuItem" id="menuOPTION_CHANGE" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        OPTION DE CHANGE</span>
                </a>
                <ul id="css3menu7" class="topmenu OGC_menu">
                    <sec:authorize access='hasAnyRole("OPTION_CHANGE_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsOptionChange.do" class="menuItem" title="Accès aux environnements physiques OPTION_CHANGE" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("OPTION_CHANGE_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("OPTION_CHANGE_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuOPTION_CHANGE&persistenceUnit=dbOptionChangePU&circuit=HOTFIX&critereTri=milestone&projetTrac=option_change" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("OPTION_CHANGE_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuOPTION_CHANGE&persistenceUnit=dbOptionChangePU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=option_change" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>     

        <sec:authorize access='hasAnyRole("PROSPECTION_TRE")'>
            <li class="toplast">
                <a class="menuItem" id="menuPROSPECTION_TRE" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        PROSPECTION TRE</span>
                </a>
                <ul id="css3menu7" class="topmenu OGC_menu">
                    <sec:authorize access='hasAnyRole("PROSPECTION_TRE_ACCES_AUX_ENVIRONNEMENTS")'>
                        <li class="topmenu">
                            <a href="./accesAuxEnvironnementsProspectionTre.do" class="menuItem" title="Accès aux environnements physiques PROSPECTION_TRE" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/trac.png" alt=""/>
                                    Accès Aux Environnements
                                </span>
                            </a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("PROSPECTION_TRE_REPORTING_CHF")'>
                        <li class="topmenu">
                            <a class="menuItem" id="menuOGC" style="width:250px;height:26px;line-height:26px;">
                                <span>
                                    <img src="images/menu_icons/activity.png" alt=""/>
                                    Reporting C.HF</span>
                            </a>
                            <ul>
                                <sec:authorize access='hasAnyRole("PROSPECTION_TRE_REPORTING_CHF_PAR_MILESTONE")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuPROSPECTION_TRE&persistenceUnit=dbProspectionTrePU&circuit=HOTFIX&critereTri=milestone&projetTrac=prospection_tre" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par activité</a>
                                    </li>
                                </sec:authorize>
                                <sec:authorize access='hasAnyRole("PROSPECTION_TRE_REPORTING_CHF_PAR_NATURE_TRAITEMENT")'>
                                    <li>
                                        <a href="./StatistiquesnewTrac.do?menuItem=menuPROSPECTION_TRE&persistenceUnit=dbProspectionTrePU&circuit=HOTFIX&critereTri=nature_traitement&projetTrac=prospection_tre" title="Suivi de l'état d'avancement de toutes les livraisons de la release en cours par milestone."  class='sousMenu'>
                                            <img src="images/menu_icons/compass.png" alt=""/>
                                            Statistiques des tickets Hotfix par nature de traitement</a>
                                    </li>
                                </sec:authorize>
                            </ul>
                        </li>
                    </sec:authorize>
                </ul>
            </li>   
        </sec:authorize>

        <sec:authorize access='hasAnyRole("SITE_CENTRAL_CTOS")'>
            <li class="topmenu">
                <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/fiche.png" alt=""/>
                        SITE CENTRAL CTOS</span>
                </a>
                <ul>
                    <li>
                        <a href="documents/Challenge24-GestionDesLivraisonsDSI.pptx" title="Procédure de gestion"  class='sousMenu'>
                            <img src="images/menu_icons/fiche.png" alt=""/>
                            Procédure de gestion
                        </a>
                    </li>
                    <li>
                        <a href="http://172.28.70.7/ov_DSI_C24" target="_blank" title="Trac" class="sousMenu">
                            <img src="images/menu_icons/gear.png" alt=""/>
                            TRAC
                        </a>
                    </li>
                </ul>
            </li>   
        </sec:authorize>
    </ul>
</sec:authorize>



<sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI")'>
    <div class="titreMenus"><span class="GESTION_INCIDENTS_SI">+ GESTION DES INCIDENTS</span></div>
    <ul id="css3menu10" class="topmenu menuvertical GESTION_INCIDENTS_SI_menu">
        <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_CHF")'>
            <li class="topmenu">
                <a class="menuItem" id="menuVueConsolidéeIncidents" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Vue Consolidée</span>
                </a>
                <ul>
                    <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_PAR_COMPOSANT")'>
                        <li>
                            <a href="./vueConsolideeIncidentsParComposant.do" title="Vue Consolidée des Incidents SI par Criticité et par Domaine d'activité"  class='sousMenu'>
                                <img src="images/menu_icons/compass.png" alt=""/>
                                Vue Consolidée des Incidents SI par Composant</a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_PAR_RESPONSABLE")'>
                        <li>
                            <a href="./vueConsolideeIncidentsParResponsable.do" title="Vue Consolidée des Incidents SI par Criticité et par Responsable"  class='sousMenu'>
                                <img src="images/menu_icons/compass.png" alt=""/>
                                Vue Consolidée des Incidents SI par Responsable</a>
                        </li>
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>
        <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_CR")'>
            <li class="topmenu">
                <a class="menuItem" id="menuReportingIncidents" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Reporting & Statistiques</span>
                </a>
                <ul>
                    <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_CR")'>
                        <li>
                            <a href="./StatistiquesnewTrac.do?menuItem=menuReportingIncidents&persistenceUnit=dbGestionIncidentsPU&circuit=INCIDENT&critereTri=milestone&projetTrac=GESTION_INCIDENTS_SI" title="Suivi de l'état d'avancement de toutes les incidents SI par niveau d'escalade." class="sousMenu">
                                <img src="images/menu_icons/compass.png" alt=""/>
                                Statistiques des Incidents SI par Niveau d'escalade</a>
                        </li>
                    </sec:authorize>
                </ul>
            </li>
            <sec:authorize access='hasAnyRole("ADMINISTRATION")'>
                <li class="toplast">
                    <a class="menuItem" id="menuAdministration" style="width:250px;height:26px;line-height:26px;">
                        <span>
                            <img src="images/menu_icons/wrench.png" alt=""/>
                            Administration</span>
                    </a>
                    <ul>
                        <sec:authorize access='hasAnyRole("ADMIN_GESTION_INCIDENTS")'>
                            <li>
                                <a href="./DelaisGestionIncident.do" title="Parametrage des delais pour le besoin du projet Gestion des Incidents." class="sousMenu">
                                    <img src="images/menu_icons/gear.png" alt=""/>
                                    Délais de Gestion des Incidents
                                </a>
                            </li>
                        </sec:authorize>   
                    </ul>
                </li>   
            </sec:authorize> 

            <sec:authorize access='hasAnyRole("ADMINISTRATION")'>  
                <li class="toplast">
                    <a class="menuItem"  style="width:250px;height:26px;line-height:26px;">
                        <span>
                            <img src="images/menu_icons/fiche.png" alt=""/>
                            Références</span>
                    </a>
                    <ul>
                        <li>
                            <a href="documents/Visio-Processus_Gestion_des_Incidents_de_Exploitation.pdf" target="_blank" title="Procédure de gestion" class="sousMenu">
                                <img src="images/menu_icons/gear.png" alt=""/>
                                Procédure de gestion
                            </a>
                        </li>
                        <li>
                            <a href="http://172.28.70.246/trac/GESTION_INCIDENTS_SI/wiki/Workflow_D" target="_blank" title="Workflow" class="sousMenu">
                                <img src="images/menu_icons/gear.png" alt=""/>
                                Workflow
                            </a>
                        </li>
                        <li>
                            <a href="http://172.28.70.246/trac/GESTION_INCIDENTS_SI/wiki/STATISTIQUES" target="_blank" title="Statistiques" class="sousMenu">
                                <img src="images/menu_icons/gear.png" alt=""/>
                                Statistiques
                            </a>
                        </li>
                        <li>
                            <a href="http://172.28.70.246/trac/GESTION_INCIDENTS_SI" target="_blank" title="Trac" class="sousMenu">
                                <img src="images/menu_icons/trac.png" alt=""/>
                                TRAC
                            </a>
                        </li>
                    </ul>
                </li>
            </sec:authorize>


        </sec:authorize>
    </ul>
</sec:authorize>


<sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
    <div class="titreMenus"><span class="GESTION_DES_DEMANDES">+ GESTION DES DEMANDES</span></div>
    <ul id="css3menu12" class="topmenu menuvertical GESTION_DES_DEMANDES_menu">

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
            <li class="topmenu">
                <a class="menuItem" id="menuVueConsolidéeDemandes" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Vue Consolidée</span>
                </a>
                <ul>
                    <sec:authorize access='hasAnyRole("GESTION_INCIDENTS_SI_REPORTING_PAR_COMPOSANT")'>
                        <li>
                            <a href="./vueConsolideeDemandesParComposant.do" title="Vue Consolidée des Besoins Métier par Criticité et par Domaine d'activité"  class='sousMenu'>
                                <img src="images/menu_icons/compass.png" alt=""/>
                                Vue Consolidée des Besoins Métier par Domaine</a>
                        </li>
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
            <li class="topmenu">
                <a class="menuItem" id="menuReportingDemandes" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Reporting & Statistiques </span>
                </a>
                <ul> 
                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./StatistiquesnewTrac.do?menuItem=menuReportingDemandes&persistenceUnit=dbGestionDesDemandesOvtoolsPU&circuit=DEMANDE&critereTri=milestone&projetTrac=GESTION_DEMANDES_OVTOOLS" title="Suivi de l'état d'avancement de tous les besoins métiers par Domaine." class="sousMenu">
                                <img src="images/menu_icons/compass.png" alt=""/>
                                Statistiques des Besoins Metiers par Domaine</a>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=VALIDE_PMO" title="Liste des Besoins Métier Validés et planifiés par la PMO" class="sousMenu">
                                <img src="images/menu_icons/calendar.png" alt=""/>
                                Liste des Besoins métiers Planifiés
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=NON_VALIDE" title="Liste des Besoins Métier Annulés" class="sousMenu">
                                <img src="images/menu_icons/calendar.png" alt=""/>
                                Liste des Besoins métiers Annulés
                            </a>
                        </li>
                    </sec:authorize>

                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a style='color:#696969;' class='sousMenu'>
                                <img src="images/menu_icons/direction.png" alt=""/>
                                Liste des projets en dépassement de Budget</a>
                        </li>
                    </sec:authorize>

                </ul> 
            </li>
        </sec:authorize>

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
            <li class="topmenu">
                <a class="menuItem" id="menuNouveauBesoin" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/activity.png" alt=""/>
                        Définition Nouveau Besoin </span>
                </a>
                <ul> 
                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./definitionNouveauBesoin.do"  title="Création d'un nouveaau Besoin Metier" class="sousMenu">
                                <img src="images/menu_icons/file.png" alt=""/>
                                Définition Nouveau Besoin Métier </a>
                        </li>
                    </sec:authorize>
                </ul> 
            </li> 
        </sec:authorize>

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>  
            <li class="topmenu">
                <a class="menuItem" id="menuSuiviActiviteDemandes" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/transfer.png" alt=""/>
                        Workflow & Suivi de l'activité</span>
                </a>
                <ul>   
                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=NOUVEAU_BESOIN" title="Liste des Besoins Métier en cours de rédaction ou en attente de validation par le responsable du Departement" class="sousMenu">
                                <img src="images/menu_icons/paperplane.png" alt=""/>
                                Besoins en cours de rédaction / en attente de validation
                            </a>
                        </li>
                    </sec:authorize>   

                    <sec:authorize access='hasAnyRole("ADMIN_GESTION_INCIDENTS")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=VALIDE_METIER" title="Liste des Besoins en attente d'approbtion et prise en charge par les Métiers" class="sousMenu">
                                <img src="images/menu_icons/users.png" alt=""/>
                                <font color="CD5C5C">Métier : </font> Besoins en attente de prise en charge
                            </a>
                        </li>
                    </sec:authorize>   

                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=VALIDE_MOA" title="Liste des Besoins en attente de chiffrage par les équipes Métiers et IT" class="sousMenu">
                                <img src="images/menu_icons/justice.png" alt=""/>
                                <font color="CD5C5C">IT : </font> Besoins en attente de chiffrage
                            </a>
                        </li>
                    </sec:authorize> 

                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a href="./listeDesProjets.do?typeReporting=VALIDE_MOE" title="Liste des Besoins en attente de Planification par l'équipe PMO" class="sousMenu">
                                <img src="images/menu_icons/calendar.png" alt=""/>
                                <font color="CD5C5C">PMO : </font> Besoin en attente de Planification
                            </a>
                        </li>
                    </sec:authorize> 
                </ul>
            </li> 
        </sec:authorize>

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
            <li class="topmenu">
                <a class="menuItem" id="menuAdministrationDemandes" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/wrench.png" alt=""/>
                        Administration </span>
                </a>
                <ul>
                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a style='color:#696969;' class='sousMenu'>
                                <img src="images/menu_icons/direction.png" alt=""/>
                                Gestion des profils d'utilisateurs</a>
                        </li>
                    </sec:authorize>
                    <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>
                        <li>
                            <a style='color:#696969;' class='sousMenu'>
                                <img src="images/menu_icons/direction.png" alt=""/>
                                Gestion des paramètres</a>
                        </li>
                    </sec:authorize>
                </ul>
            </li>
        </sec:authorize>

        <sec:authorize access='hasAnyRole("GESTION_DES_DEMANDES")'>  
            <li class="topmenu">
                <a class="menuItem" id="item45" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/fiche.png" alt=""/>
                        Références</span>
                </a>
                <ul>
                    <li>
                        <a href="documents/Process_GD.xlsx" title="Procédure de gestion"  class='sousMenu'>
                            <img src="images/menu_icons/fiche.png" alt=""/>
                            Procédure de gestion
                        </a>
                    </li>
                    <li>
                        <a href="http://172.28.70.246/trac/gestion_demandes" target="_blank" title="Trac" class="sousMenu">
                            <img src="images/menu_icons/gear.png" alt=""/>
                            TRAC
                        </a>
                    </li>
                </ul>
            </li>        
        </sec:authorize>
    </ul>
</sec:authorize>

<sec:authorize access='hasAnyRole("GESTION_CONFIGURATION")'>
    <div class="titreMenus"><span class="SITE_CENTRAL_CTOS">+ GESTION DE CONFIGURATION</span></div>
    <ul id="css3menu13" class="topmenu menuvertical SITE_CENTRAL_CTOS_menu">
        <sec:authorize access='hasAnyRole("CONFIGURATION_SYSTEME")'>
            <li class="topmenu">
                <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/fiche.png" alt=""/>
                        Configuration Système</span>
                </a>
                <ul>    
                    <!--li>
                        <a style='color:#696969;' title="Procédure de gestion"  class='sousMenu'>
                            <img src="images/menu_icons/fiche.png" alt=""/>
                            Procédure de gestion
                        </a>
                    </li-->

                </ul>
            </li>
        </sec:authorize>

        <sec:authorize access='hasAnyRole("CONFIGURATION_RESEAU")'>
            <li class="topmenu">
                <a class="menuItem" style="width:250px;height:26px;line-height:26px;">
                    <span>
                        <img src="images/menu_icons/fiche.png" alt=""/>
                        Configuration Réseau</span>
                </a>
                <ul>
                    <!--li>
                        <a style='color:#696969;' title="Procédure de gestion"  class='sousMenu'>
                            <img src="images/menu_icons/fiche.png" alt=""/>
                            Procédure de gestion
                        </a>
                    </li-->
                </ul>
            </li> 
        </sec:authorize>
    </ul>
</sec:authorize>

<style type="text/css">
    .menuvertical{
        border-bottom: 1px #343971 solid !important;
        border-left: 1px #343971 solid !important;
    }
</style>