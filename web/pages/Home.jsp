<%@page import="tools.Tools"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolderStrategy"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="java.util.Collection"%>
<%@page import="org.springframework.security.ldap.userdetails.LdapUserDetailsImpl"%>
<%@page import="org.springframework.security.web.authentication.WebAuthenticationDetails"%>
<%@page import="org.springframework.security.core.userdetails.User"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="org.springframework.security.core.context.SecurityContextHolder" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<meta http-equiv="Expires" content="Mon, 26 Jul 1997 05:00:00 GMT">
<meta http-equiv="Pragma" content="no-cache">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="shortcut icon" type="image/x-icon" href="images/logoOvTools.ico">
        <link rel="stylesheet" href="css/styleModalBox.css" type="text/css"/>
        <link rel="stylesheet" href="css/accordeon.css" type="text/css"/>
        <link rel="stylesheet" href="css/loadingAnimation.css" type="text/css"/>
        <link rel="stylesheet" href="css/loadingAnimation1.css" type="text/css"/>
        <link rel="stylesheet" type="text/css" href="css/style.css">
        <link rel="stylesheet" type="text/css" href="css/droppableStyle_grandeTaille.css"/>
        <link rel="stylesheet" type="text/css" href="css/droppableStyle_petiteTaille.css"/>
        <link rel="stylesheet" type="text/css" href="css/diagrammePie.css"/>
        <link rel="stylesheet" type="text/css" href="css/parametrage-css.css"/>
        <link rel="stylesheet" type="text/css" href="css/styleMenuVertical.css"/>
        <link rel="stylesheet" type="text/css" href="css/boutonMarcheArretStyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/boutonAjouterMasquerStyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/workflowStyle.css"/>
        <link rel="stylesheet" type="text/css" href="css/jquery.ptTimeSelect.css" /> 
        <link rel="stylesheet" type="text/css" href="css/normesMethodes.css" />
        <link rel="stylesheet" type="text/css" href="css/roundTableCorners.css" />
        <link rel="stylesheet" type="text/css" href="css/gestionDesGroupes.css" />
        <link rel="stylesheet" type="text/css" href="css/gestionUserHasEnvironnement.css" />
        <link rel="stylesheet" type="text/css" href="css/accesAuEnvironnement.css" />
        <link rel="stylesheet" type="text/css" href="css/infoBulle.css" />
        <link rel="stylesheet" type="text/css" href="css/slidemenu.css" />
        <link rel="stylesheet" type="text/css" href="css/facebookNotification.css" />

        <link rel="stylesheet" type="text/css" href="css/demo.css" />
        <link rel="stylesheet" type="text/css" href="css/tabs.css" />
        <link rel="stylesheet" type="text/css" href="css/tabstyles.css" />

        <script type="text/javascript" src="javascript/jquery-1.7.min.js"></script>
        <SCRIPT type="text/javascript" src="javascript/javascript.js"> </SCRIPT>
        <SCRIPT type="text/javascript" src="javascript/common.js"> </SCRIPT>
        <SCRIPT type="text/javascript" src="javascript/validation.js"> </SCRIPT>
        <SCRIPT type="text/javascript" src="javascript/submitData.js"> </SCRIPT>
        <SCRIPT type="text/javascript" src="javascript/GestionDiagrammes.js"> </SCRIPT>
        <script type="text/javascript" src="javascript/jquery-ui-1.8.custom.min.js"></script>
        <script type="text/javascript" src="javascript/accordeonJavascript.js"></script>
        <script type="text/javascript" src="javascript/excanvas.min.js"></script>
        <script type="text/javascript" src="javascript/jquery.flot.min.js"></script>
        <script type="text/javascript" src="javascript/jquery.flot.pie.min.js"></script>
        <script type="text/javascript" src="javascript/validationParametrage.js"></script>
        <script type="text/javascript" src="javascript/jquery.ba-resize.js"></script>
        <script type="text/javascript" src="javascript/jquery.ptTimeSelect.js"></script>
        <script type="text/javascript" src="javascript/jeditable-min.js"></script>
        <script type="text/javascript" src="javascript/jquery-ui-1.8.custom.min.js"></script> 
        <script type="text/javascript" src="javascript/slidemenu.js"></script> 
        <!--datepicker + timepicker-->
        <link rel="stylesheet" type="text/css" href="css/jquery.datetimepicker.css"/>
        <script  type="text/javascript"  src="javascript/jquery.datetimepicker.full.js"></script>
        <!--fin datepicker + timepicker-->
        <title>OV Tools</title>
    </head>
    <script>
        $(window).unload(function() {
            //$("#deconnexionBouton").click();
            var myLink = document.getElementById('deconnexionBouton');
            myLink.click();
        });
        /*
         $("#deconnexionBouton").click(function(event) {
         event.preventDefault();
         url = $(this).attr("href");
         window.location.href = url;
         });
         */
    </script>
    <body onbeforeunload="confirmRefresh();">
        <div class="divPrincipal">
            <div class="banner"> 
                <div class="bannerImage" style="background-image: url('images/banner.png')">
                </div>
                <div class="bannerLogo" style="background-image: url('images/logoOvTools.png')">
                </div>
            </div>

            <div>
                <div class="pageCorps2">                    
                    <%@include file="/pages/loadingAnimation.jsp" %>                    
                </div> 
                <script>
                    $(".pageCorps2").hide();
                </script>
                <table id="conteneurMenuVerticalEtPageCorps">                    
                    <tr >
                        <td id="ConteneurMenuVertical">
                            <sec:authorize access="not isAnonymous()">
                                <div id="moduleConnexion">
                                    <span id="messageAccueil">Bienvenue<span style="color: #343971" id="connectedUser"> <%= Tools.getConnectedUserName()%> </span></span>
                                    <br/>
                                    <span id="messageDeconnexion"><a id="deconnexionBouton" style="color:#888888" href="<c:url value="/j_spring_security_logout" />" > Déconnexion</a></span>
                                </div>
                            </sec:authorize>

                            <sec:authorize access="permitAll()">
                                <tiles:insert	attribute="menuVertical" />
                            </sec:authorize>

                        </td>
                        <td class="espace"></td>
                        <td class="pageCorps">

                            <tiles:insert	attribute="corpsPage" />

                        </td>  
                    </tr>


                </table>
                <div class="clear"/>
                <div class="pageCorps1" id="messageLogContainer" > 
                    <div id ="loaderLog">                        
                        <div id="textLog" style="display: inline-block">
                            Veuillez patienter, traitement en cours
                        </div>
                        <div id="animationLog" style="display: inline-block">
                            <%@include file="/pages/loadingAnimation1.jsp" %>
                        </div>
                    </div>
                    <div id="messageLog"></div>
                    <div id="anisLog"></div>
                </div>
                <tiles:insert	attribute="footer" />
            </div>
        </div>

        <!--
        <h3>Username :  Tools.getConnectedLogin() </h3>
        <h3>isAuthentificated :  SecurityContextHolder.getContext().getAuthentication().isAuthenticated() </h3>
    
        
            Object[] droits = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toArray();
            for (Object obj : droits) {
                out.println(obj);
            }
        
        <p/>
        -->







    </body>
</html>



<script>
    var dateChargement = new Date();
    var logWorker;
    var contextPath = "<%=request.getContextPath()%>";
    function startWorker()
    {
        //ne pas afficher le log dans la page d'accueil
        if ($(location).attr('href').indexOf("login.do") === -1) {
            if (typeof(Worker) !== "undefined")
            {
                /*
                 if (typeof(logWorker) === "undefined")
                 {
                 logWorker = new Worker("javascript/WorkerAfficherMessageEtatAvancement.js");
                 } else {
                 document.getElementById("messageLog").innerHTML = "typeof(logWorker) !== undefined";
                 }*/

                //J'ai changé le code ici pour: en cas d'exception affecter une nouvelle instance worker à logWorker


                //logWorker = new Worker("javascript/WorkerAfficherMessageEtatAvancement.js?contextPath=" + contextPath+"&connectedUser="+$("#connectedUser").text().trim());
                logWorker = new Worker("javascript/WorkerAfficherMessageEtatAvancement.js?contextPath=" + contextPath);



                logWorker.onmessage = function(event) {
                    //si la page a plus que 60 min, se déconnecter
                    dateActuelle = new Date();
                    tempsEcoule = Date.daysBetween(dateChargement, dateActuelle);
                    //console.log(tempsEcoule);
                    if (tempsEcoule > 60) {
                        //alert("deconnexion");
                        window.location.href = "/OVTOOLS/j_spring_security_logout";
                    }
                    //fin
                    document.getElementById("messageLog").innerHTML = event.data;
                    // S'il ya un problème de worker, changer la valeur du div logExceptionWorker
                    // un listener est activé sur la div logExceptionWorker va tuer le worker et lancer un autre

                    if (event.data.indexOf("Erreur de chargement (503)") !== -1 || event.data.indexOf("Erreur de chargement (0)") !== -1) {
                        //alert(event.data);
                        window.location.href = "/OVTOOLS";
                        alert("OVTOOLS a été redémarré par l'administrateur");
                        document.getElementById("logExceptionWorker").innerHTML = Date.now();
                    }
                };

            }
            else
            {
                document.getElementById("messageLog").innerHTML = "Sorry, your browser does not support Web Workers...";
            }

        }
    }

    function stopWorker()
    {
        //ne pas afficher le log dans la page d'accueil
        if ($(location).attr('href').indexOf("login.do") === -1) {
            logWorker.terminate();
        }
    }

    function initialiserchamps()
    {
        $('textarea').each(function()
        {
            if ($(this).attr("id") !== "resultatAnalysePackTextarea") {
                $(this).val('');
            }
        });
    }

    $(document).ready(function() {
        initialiserchamps();
        startWorker();
        $('#loaderLog').hide();
        /*
         $(".titreMenus span").click(function() {
         alert($(this).attr("class"));
         var idzoneMenu = $("." + $(this).attr("class") + "_menu").attr("id");
         //alert(idzoneMenu);
         $('.menuvertical').each(function() {
         if ($(this).attr("id") !== idzoneMenu) {
         $(this).slideUp(600);
         } else {
         $(this).slideDown(1000);
         }
         });
         });
         */
        //$(".titreMenus span").first().click();
    });

    $('#logExceptionWorker').bind('DOMNodeInserted', function(event) {
        try {
            if (document.getElementById("logExceptionWorker").innerHTML !== "") {
                stopWorker();
                startWorker();
                document.getElementById("logExceptionWorker").innerHTML = "";
            }
        } catch (e) {
            alert(e.message);
        }
    });

    $('#messageLog').bind('DOMNodeInserted DOMNodeRemoved', function(event) {
        if (event.type === 'DOMNodeInserted') {
            $('#loaderLog').show();
        } else {
            $('#loaderLog').hide();
        }
    });
    function confirmRefresh()

    {
        stopWorker();
    }

    $(document).ready(function() {
        setInterval(function() {
            $(".clignotant").toggleClass("vert");
            $(".clignotantRouge").toggleClass("rougeClair");
            $(".clignotantBleu").toggleClass("bleu");
        }, 300);

        $(".conteneur_info_bull span").change(function() {
            alert();
            var infoBullWidth = $(this).width();
            $(".conteneur_info_bull span").css("margin-left", infoBullWidth * (-2));
        });
    });

    Date.daysBetween = function(date1, date2) {

        //Get 1 min in milliseconds
        var one_minute = 1000 * 60;
        // Convert both dates to milliseconds
        var date1_ms = date1.getTime();
        var date2_ms = date2.getTime();
        // Calculate the difference in milliseconds
        var difference_ms = date2_ms - date1_ms;
        // Convert back to days and return
        difference = difference_ms / one_minute;
        return difference;
    }
</script>


<script>
    // please note, 
// that IE11 now returns undefined again for window.chrome
// and new Opera 30 outputs true for window.chrome
// and new IE Edge outputs to true now for window.chrome
// so use the below updated condition
    if ($(location).attr('href').indexOf("login.do") === -1 && $(location).attr('href').indexOf("LoginForm.do") === -1) {
        var isChromium = window.chrome,
                vendorName = window.navigator.vendor,
                isOpera = window.navigator.userAgent.indexOf("OPR") > -1,
                isIEedge = window.navigator.userAgent.indexOf("Edge") > -1;
        if (isChromium !== null && isChromium !== undefined && vendorName === "Google Inc." && isOpera == false && isIEedge == false) {
            // is Google chrome     
        } else {
            // not Google chrome 
            alert("Veuillez utiliser le navigateur Google Chrome");
            window.location.href = "/OVTOOLS/j_spring_security_logout";
        }
    }
</script>