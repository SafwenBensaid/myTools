/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import java.util.Map;
import org.codehaus.jackson.map.ObjectMapper;
import servlets.AutorisationHotfixServlet;
import static servlets.AutorisationHotfixServlet.ConvertTimeTracToJavaDate;
import static tools.Configuration.mapFields;
import static tools.Configuration.mapProjetsTrac;

/**
 *
 * @author 04486
 */
public class GenericTools<T> {

    private final Class<T> clazz;

    public GenericTools(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Map<String, Object> convertDtoToMap(Object o) {
        ObjectMapper m = new ObjectMapper();
        Map<String, Object> map = m.convertValue(o, Map.class);
        return map;
    }

    public Object convertMapToDto(Map<String, Object> map) {
        ObjectMapper m = new ObjectMapper();
        T o = m.convertValue(map, clazz);
        return o;
    }

    public static String displayTable(Map<Integer, Map<String, Object>> globalMap, String[] fieldsToBeDisplayed, String siteTrac) {
        StringBuilder tableSb = new StringBuilder();
        tableSb.append("Nombre total: " + globalMap.size());
        tableSb.append("<table class='tableTicketsSummary tableDefilante'>");
        tableSb.append("<thead>");
        tableSb.append("<tr class='tableTicketsSummaryTr'>");
        for (String cle : fieldsToBeDisplayed) {
            String nomColonne = Configuration.mapFields.get(cle).getValeur1();
            String classe = Configuration.mapFields.get(cle).getValeur2();
            tableSb.append("<th class='tableTicketsSummaryTh ").append(classe).append("'>").append(nomColonne).append("</th>");
        }
        tableSb.append("</tr>");
        tableSb.append("</thead>");
        tableSb.append("<tbody style='background-color: white;'>");
        for (Map.Entry<Integer, Map<String, Object>> entry : globalMap.entrySet()) {
            Map<String, Object> ticketMap = entry.getValue();


            if (ticketMap.containsKey("nature_traitement")) {
                if (ticketMap.get("nature_traitement").equals("ACTION A CHAUD")) {
                    tableSb.append("<tr class='tableTicketsSummaryTr rougeClair'>");
                } else {
                    tableSb.append("<tr class='tableTicketsSummaryTr'>");
                }
            } else if (ticketMap.containsKey("type")) {
                if (ticketMap.get("type").equals("HOT FIXE PROD")) {
                    if (ticketMap.containsKey("contenu_des_livrables")) {
                        if (ticketMap.get("contenu_des_livrables").equals("OBJETS T24") || ticketMap.get("contenu_des_livrables").equals("A DEFINIR")) {
                            tableSb.append("<tr class='tableTicketsSummaryTr'>");
                        } else if (ticketMap.get("contenu_des_livrables").equals("DOLLAR U") || ticketMap.get("contenu_des_livrables").equals("$U T24")) {
                            tableSb.append("<tr class='tableTicketsSummaryTr vert'>");
                        } else if (ticketMap.get("contenu_des_livrables").equals("CREATION COMPANY") || ticketMap.get("contenu_des_livrables").equals("PATCH")) {
                            tableSb.append("<tr class='tableTicketsSummaryTr couleur2'>");
                        } else {
                            tableSb.append("<tr class='tableTicketsSummaryTr couleur5'>");
                        }
                    } else {
                        tableSb.append("<tr class='tableTicketsSummaryTr'>");
                    }
                } else if (ticketMap.get("type").equals("ACTION A CHAUD PROD")) {
                    tableSb.append("<tr class='tableTicketsSummaryTr rougeClair'>");
                } else {
                    tableSb.append("<tr class='tableTicketsSummaryTr'>");
                }
            } else {
                tableSb.append("<tr class='tableTicketsSummaryTr'>");
            }

            for (String cle : fieldsToBeDisplayed) {
                String classe = Configuration.mapFields.get(cle).getValeur2();
                String valeur = "";
                try {
                    valeur = ticketMap.get(cle).toString();
                } catch (Exception ex) {
                    //ne rien faire
                }
                tableSb.append("<td class='tableTicketsSummaryTd ").append(classe).append("'>").append(getValueSwitchKey(cle, valeur, siteTrac)).append("</td>");
            }
            tableSb.append("</tr>");
        }
        tableSb.append("</tbody>");
        tableSb.append("</table>");

        return tableSb.toString();
    }

    private static String getValueSwitchKey(String cle, String value, String siteTrac) {
        String resultat = null;
        if (cle.equals("id") || cle.equals("harmCr") || cle.equals("harmCp") || cle.equals("numeroLivraison")) {
            if (Configuration.mapProjetsTrac.containsKey(siteTrac)) {
                String url = Configuration.mapProjetsTrac.get(siteTrac).getValeur1() + "/ticket/";
                resultat = "<a href='" + url + value + "' target='_blank'>#" + value + "</a>";
            }
        } else if (cle.equals("numeroAnomalie") || cle.equals("ticket_origine")) {
            String url = "http://172.28.70.74/trac/anomalies_t24/ticket/";
            resultat = "<a href='" + url + value + "' target='_blank'>#" + value + "</a>";
        } else if (cle.equals("description") || cle.equals("messageTrac") || cle.equals("summary")) {
            resultat = "<a class='conteneur_info_bull'><img class='info-icon' src='images/info.png' alt='info'><span>" + value + "</span></a>";
        } else if (cle.equals("changetime") || cle.equals("time") || cle.equals("dateEnvoiProd") || cle.equals("dateDeploiement")) {
            try {
                resultat = AutorisationHotfixServlet.ConvertTimeTracToJavaDate(value);
            } catch (Exception ex) {
                resultat = value;
            }
        } else {
            resultat = value;
        }
        return resultat;
    }
}
