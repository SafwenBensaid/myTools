<%@page import="org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper"%>
<%@page import="org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>
<script>
    var hotfixToBeRemoved;
</script>

<br><br>
<div class='grandTitre'>
    Liste des HOTFIX & Actions A Chaud Curatives<br>
    en attente d'autorisation par le Comité Release
</div>


<div id='conteneurAnimation'>
    <div id="loadingAnimationConteneur" class="center">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>
    <div id='loadingAnimationConteneur1' class="center" style='display: none'>
        <%@include file='/pages/loadingAnimation1.jsp' %>
    </div>
</div>

<br><br><br>    

<div id="resultatHotfix">

</div>

<div id="popup_motif_rejet" class="popup_block">
    <p class="titrePopup"></p>


    <div class="wikitoolbar">
        <input type="button" class="wikiButton" id="strong" title="Bold text: '''Example'''" tabindex="400" style="background-image: url('images/strong.png')" />

        <input type="button" class="wikiButton" id="em" title="Italic text: ''Example''" tabindex="400" style="background-image: url('images/em.png')" />

        <input type="button" class="wikiButton" id="heading" title="Heading: == Example ==" tabindex="400" style="background-image: url('images/heading.png')" />

        <input type="button" class="wikiButton" id="link" title="Link: [http://www.example.com/ Example]" tabindex="400" style="background-image: url('images/link.png')" />

        <input type="button" class="wikiButton" id="code" title="Code block: {{{ example }}}" tabindex="400" style="background-image: url('images/code.png')" />

        <input type="button" class="wikiButton" id="hr" title="Horizontal rule: ----" tabindex="400" style="background-image: url('images/hr.png')" />

        <input type="button" class="wikiButton" id="np" title="New paragraph" tabindex="400" style="background-image: url('images/np.png')" />

        <input type="button" class="wikiButton" id="br" title="Line break: [[BR]]" tabindex="400" style="background-image: url('images/br.png')" />
    </div>
    <span id="conteneurMessageTrac">
        <textarea id="textAreaManuel" class="common" style="min-height: 50px"></textarea>   
    </span>
    <p>
        <span id="messageResultatPersist" class="vert clignotant">

        </span>

    <p class="conteneurBouton"><input type="button" class="boutonValider"  value="Enregistrer" onclick="validerTickets($('#textAreaManuel').val());" /></p>
</div>

<a href='#?w=630' rel='popup_motif_rejet' class='poplight' id="openopupMotifRejet">                        

</a>

<SCRIPT type="text/javascript" src="javascript/common.js"></SCRIPT>

<script>
    $(document).ready(function() {
        loadAllTicketsTable(true);
        resizeTextArea();
        $("#popup_motif_rejet .boutonValider").click(
                function() {
                    $(".btn_close").click();
                }
        );
        /*
         $(".btn_close").click(
         function() {                    
         alert(hotfixToBeRemoved);
         alert($("#R"+hotfixToBeRemoved).attr('checked'));
         $("#R"+hotfixToBeRemoved).attr('checked', false);
         alert($("#R"+hotfixToBeRemoved).attr('checked'));
         }
         );
         */
    });

    $("#resultatHotfix").change(function() {
        $("input:checked").each(function() {
            if ($(this).is(':checked')) {
                var id = $(this).closest("input").attr("id").trim();
                if (id.substring(0, 1) === "R") {
                    $("#textAreaManuel").val("");
                    hotfixToBeRemoved = id.substring(1, id.length);
                    $(".titrePopup").html("Motif de rejet du hotfix N°" + hotfixToBeRemoved);
                    $("#openopupMotifRejet").click();
                }

            }
        });

    });

    function loadAllTicketsTable(cyclique) {
        var contextPath = "<%=request.getContextPath()%>";
        //$("#loadingAnimationConteneur").show();
        $.ajax({
            type: "POST",
            url: contextPath + "/AutorisationHotfixServlet",
            data: "action=load",
            success: function(response) {
                $("#loadingAnimationConteneur").hide();
                $("#resultatHotfix").html(response);

                if (cyclique === true) {
                    setTimeout(function() {
                        loadAllTicketsTable(true);
                    }, 60000);
                }
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }

    function validerTickets(motifRejet) {
        var ticketsAvalMOA = "";
        var ticketsAvalMOE = "";
        var ticketsRejet = "";
        $("input:checked").each(function() {
            if ($(this).is(':checked')) {
                var id = $(this).closest("input").attr("id").trim();
                if (id.substring(0, 1) === "A") {
                    ticketsAvalMOA += id;
                } else if (id.substring(0, 1) === "E") {
                    ticketsAvalMOE += id;
                } else if (id.substring(0, 1) === "R") {
                    ticketsRejet += id;
                }
            }
        });
        submitEtatTickets(ticketsAvalMOA, ticketsAvalMOE, ticketsRejet, motifRejet);
    }
    ;

    function submitEtatTickets(ticketsAvalMOA, ticketsAvalMOE, ticketsRejet, motifRejet) {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur1").show();
        $.ajax({
            type: "POST",
            url: contextPath + "/AutorisationHotfixServlet",
            data: "action=submit&ticketsAvalMOA=" + ticketsAvalMOA + "&ticketsAvalMOE=" + ticketsAvalMOE + "&ticketsRejet=" + ticketsRejet + "&motifRejet=" + motifRejet,
            success: function(response) {
                $("#loadingAnimationConteneur1").hide();
                $("#messageResultatPersist").text(response);
                $("#messageResultatPersist").show();
                loadAllTicketsTable(false);
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 3000);
            },
            error: function(e) {
                //alert('Error: ' + e);
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 3000);
            }
        });
    }

    $(document).ready(function() {
        selectMenu('menuT24');
        $('#tableTicketsHotfix tr').hover(function() {
            $(this).addClass('hover');
        }, function() {
            $(this).removeClass('hover');
        });
    });


    function updateMotifRemoveHotfix(hotfixRejected, motifRejet) {
        alert(motifRejet)
    }



</script>


<style type="text/css">

    #tableTicketsHotfix tr:hover {
        background-color:#FAFAD2;
    }


    #conteneurAnimation{
        position:absolute;
        left: 300px;
        right: 0;
        margin: 0 auto;
        background-position: absolute; 
        z-index:1;
    }

    .conteneur_info_bull span{
        position:absolute;
        margin-top:22px;
        margin-left:-400px;
        color:#666666;
        background-color: white;
        max-width: 800px;
        white-space: pre;
        padding:10px;
        border-radius:2px;
        box-shadow:0 0 2px rgba(0,0,0,.5);
        transform:scale(0) rotate(-12deg);
        transition:all .25s;
        opacity:0;
    }
    /* quand le .info-icon est hover ou focus, son voisin span sera ... */  
    .info-icon:hover ~span, .info-icon:focus ~span{
        transform:scale(1) rotate(0); 
        opacity:1;
    }
    .info-icon{
        cursor: pointer;
        vertical-align: bottom;
        margin-left: 12px
    }

    input[type=checkbox].active_checkbox {
        position:absolute;
        z-index:-1000;
        overflow: hidden;
        clip: rect(0 0 0 0);
        height:2px;
        width:2px;
        margin:-2px;
        padding:0;
        border:0;
    }

    input[type=checkbox].active_checkbox + label.css-label {
        padding-left:40px;
        height:22px; 
        display:inline-block;
        line-height:22px;
        background-repeat:no-repeat;
        background-position: 0 0;
        font-size:22px;
        vertical-align:middle;
        cursor:pointer;
        margin-left: 40%;
    }

    input[type=checkbox].active_checkbox:checked + label.css-label {
        background-position: 0 -22px;
    }

    label.css-label {
        -webkit-touch-callout: none;
        -webkit-user-select: none;
        -khtml-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }

    .bout1{
        background-image:url(images/radio.png);
    }
    .bout2{
        background-image:url(images/switch_rejet.png) !important;
    }
</style>