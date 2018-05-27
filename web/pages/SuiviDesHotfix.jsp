<%@page import="org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper"%>
<%@page import="org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>


<br><br>
<div class='grandTitre'>
    Liste des HOTFIX valides par le comite release<br>sur le chemin de la PROD
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

<script>
    $(document).ready(function() {
        loadAllTicketsTable();
        $("#loadingAnimationConteneur1").hide();
    });
    function validerTickets() {
        var tickets_valides = "";
        $("input:checked").each(function() {
            if ($(this).is(':checked')) {
                tickets_valides += $(this).closest("td").attr("id").trim();
            }
        });
        submitEtatTickets(tickets_valides);
    }
    ;

    function submitEtatTickets(tickets_valides) {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur1").show();
        $.ajax({
            type: "POST",
            url: contextPath + "/SuiviHotfixServlet",
            data: "action=submit&tickets_valides=" + tickets_valides,
            success: function(response) {
                $("#messageResultatPersist").show();
                $("#loadingAnimationConteneur1").hide();
                $("#messageResultatPersist").text(response);
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 1500);
            },
            error: function(e) {
                //alert('Error: ' + e);
                setTimeout(function() {
                    $("#messageResultatPersist").hide();
                }, 1500);
            }
        });
    }


    function loadAllTicketsTable() {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        $.ajax({
            type: "POST",
            url: contextPath + "/SuiviHotfixServlet",
            data: "action=load",
            success: function(response) {
                $("#loadingAnimationConteneur").hide();
                $("#resultatHotfix").html(response);
                setTimeout(function() {
                    loadAllTicketsTable();
                }, 60000);
            },
            error: function(e) {
                //alert('Error: ' + e);
            }
        });
    }


    $(document).ready(function() {
        selectMenu('menuT24');
    });



</script>


