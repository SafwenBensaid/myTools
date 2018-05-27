var messageLog = "";
var servletExecutionEnCours = false;



///////////////////////////// Invoquer les servlets ///////////////////

function getXMLObject()  //XML OBJECT
{
    var xmlHttp = false;
    try {
        xmlHttp = new ActiveXObject("Msxml2.XMLHTTP")  // For Old Microsoft Browsers
    }
    catch (e) {
        try {
            xmlHttp = new ActiveXObject("Microsoft.XMLHTTP")  // For Microsoft IE 6.0+
        }
        catch (e2) {
            xmlHttp = false   // No Browser accepts the XMLHTTP Object then false
        }
    }
    if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
        xmlHttp = new XMLHttpRequest();        //For Mozilla, Opera Browsers
    }
    return xmlHttp;  // Mandatory Statement returning the ajax object created
}

var xmlhttp = new getXMLObject();	//xmlhttp holds the ajax object



function invoquerAfficherMessageEtatAvancement() {
    var contextPath = extractUrlParams()["contextPath"];
    //var connectedUser = extractUrlParams()["connectedUser"];

    if (servletExecutionEnCours === false) {
        if (xmlhttp) {
            try {
                servletExecutionEnCours = true;
                //xmlhttp.open("GET", contextPath + "/AfficherMessageEtatAvancement?connectedUser="+connectedUser, true); //gettime will be the servlet name
                xmlhttp.open("GET", contextPath + "/AfficherMessageEtatAvancement", true); // `false` makes the request synchronous

                xmlhttp.onreadystatechange = traitementApresAppelServletAfficherMessageEtatAvancement;
                xmlhttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
                xmlhttp.send();


            } catch (exp) {
                postMessage("Erreur:" + exp.message);
            }
        }
    } else {
        postMessage("Servlet occupée");
    }
}

function traitementApresAppelServletAfficherMessageEtatAvancement() {
    try {
        if (xmlhttp.readyState === 4) {
            if (xmlhttp.status === 200) {
                messageLog = (xmlhttp.responseText).trim();
                postMessage(messageLog);
                setTimeout("invoquerAfficherMessageEtatAvancement()", 1000);
            }
            else {
                postMessage("Erreur de chargement (" + xmlhttp.status + ")");
                //postMessage('Error occured in XMLHttpRequest: ' + xmlhttp.statusText + '  ReadyState: ' + xmlhttp.readyState + ' Status:' + xmlhttp.status );
            }
        }
        servletExecutionEnCours = false;
    } catch (e) {
        postMessage('Error occured in XMLHttpRequest: ' + xmlhttp.statusText + '  ReadyState: ' + xmlhttp.readyState + ' Status:' + xmlhttp.status + ' E: ' + e + ' Msg:' + e.message);
    }
}
/*
 function getURLParameter(name) {
 return decodeURI(
 (RegExp(name + '=' + '(.+?)(&|$)').exec(location.search)||[,null])[1]
 );
 }
 */
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


invoquerAfficherMessageEtatAvancement();


