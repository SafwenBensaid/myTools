<%@page import="org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper"%>
<%@page import="org.springframework.security.core.authority.mapping.SimpleAttributes2GrantedAuthoritiesMapper"%>
<%@page import="org.springframework.security.core.GrantedAuthority"%>
<%@page import="org.springframework.security.core.context.SecurityContextHolder"%>

<style>
    #tableTicketsHotfix th, #tableTicketsHotfix td {
        max-width: 230px;
    }
</style>

<br><br>
<div class='grandTitre'>
    Vue consolid�e: Gestion des Incidents SI par Responsable
</div>


<div id='conteneurAnimation'>
    <div id="loadingAnimationConteneur" class="center">
        <%@include file="/pages/loadingAnimation.jsp" %>
    </div>    
</div>

<br>

<a href='#?w=1100' rel='popup_table' class='poplight'></a>
<div id='popup_table' class='popup_block'></div>
<br><br>
<div id="zoneResultat"></div>

<script>

    function openPopup(a) {
        var id = $(a).attr("id");
        $(".popup_block").html($("." + id).html());
        $(".poplight").click();
    }

    $(document).ready(function() {
        loadAllTicketsTable();
    });




    function loadAllTicketsTable() {
        var contextPath = "<%=request.getContextPath()%>";
        $("#loadingAnimationConteneur").show();
        $.ajax({
            type: "POST",
            url: contextPath + "/GenererVueConsolideeIncidentsServlet?param=responsable",
            success: function(response) {
                $("#loadingAnimationConteneur").hide();
                $("#zoneResultat").html(response);
                //colorer les tr (horizontaux)
                $('.statistiquesTable tr:not(.titre)').hover(function() {
                    $(this).addClass('selectedTr');
                }, function() {
                    $(this).removeClass('selectedTr');
                });
                //colorer les td (verticaux)
                $('.statistiquesTable tr td:not(.coin)').hover(function() {
                    if (!$(this).hasClass("hachures") && !$(this).hasClass("projectName")) {
                        indexTd = $(this).index();
                        $('.statistiquesTable td:nth-child(' + (indexTd + 1) + '):not(.hachures)').addClass('selectedTd');
                        //indexTh = Math.ceil(indexTd/4)+1;
                        if (indexTd < 5) {
                            indexTh = 2;
                        } else if (indexTd >= 5 && indexTd < 10) {
                            indexTh = 3;
                        } else {
                            indexTh = 4;
                        }
                        if (indexTh > 1) {
                            $('.statistiquesTable th:nth-child(' + indexTh + '):not(.hachures)').addClass('selectedTd');
                        }
                    }
                }, function() {
                    if (!$(this).hasClass("hachures") && !$(this).hasClass("projectName")) {
                        indexTd = $(this).index();
                        $('.statistiquesTable td:nth-child(' + (indexTd + 1) + ')').removeClass('selectedTd');
                        //indexTh = Math.ceil(indexTd / 4) + 1;
                        if (indexTd < 5) {
                            indexTh = 2;
                        } else if (indexTd >= 5 && indexTd < 10) {
                            indexTh = 3;
                        } else {
                            indexTh = 4;
                        }
                        if (indexTh > 1) {
                            $('.statistiquesTable th:nth-child(' + indexTh + '):not(.hachures)').addClass('selectedTd');
                        }
                        if (indexTh > 0) {
                            $('.statistiquesTable th:nth-child(' + indexTh + ')').removeClass('selectedTd');
                        }
                    }
                });
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
        selectMenu('menuVueConsolid�eIncidents');
    });



</script>


