/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import dataBaseTracRequests.DataBaseTracGenericRequests;
import java.util.List;
import tools.Configuration;
import tools.DataBaseTools;

/**
 *
 * @author 04486
 */
public class NewMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.err.println(new Throwable().getStackTrace()[0].getLineNumber());
        DataBaseTools dbTools = new DataBaseTools(Configuration.puLivraisons);
        StringBuilder querySb = new StringBuilder("SELECT id as 'ticket', ");
        querySb.append("t.changetime as Date_arrivee, ");
        querySb.append("o.value AS deployee_sur_prod, ");
        querySb.append("o7.value AS deployee_sur_dev2, ");
        querySb.append("o5.value AS deployee_sur_ref, ");
        querySb.append("o6.value AS deployee_sur_ass2, ");
        querySb.append("o8.value AS nature_livraison ");
        querySb.append("FROM TICKET t ");
        querySb.append("LEFT OUTER JOIN ticket_custom o ON ");
        querySb.append("        (t.id=o.ticket AND o.name='biatprod') ");
        querySb.append("LEFT OUTER JOIN ticket_custom o5 ON ");
        querySb.append("        (t.id=o5.ticket AND o5.name='biatref') ");
        querySb.append("LEFT OUTER JOIN ticket_custom o6 ON ");
        querySb.append("       (t.id=o6.ticket AND o6.name='biatass2') ");
        querySb.append("LEFT OUTER JOIN ticket_custom o7 ON ");
        querySb.append("        (t.id=o7.ticket AND o7.name='biatdev2') ");
        querySb.append("LEFT OUTER JOIN ticket_custom o8 ON ");
        querySb.append("      (t.id=o8.ticket AND o8.name='nature_liv') ");
        querySb.append("where priority = 'LIVRAISON CONFIRMEE' ");
        querySb.append("and coalesce(o.value,0) = '1' ");
        querySb.append("and coalesce(o5.value,0) = '1' ");
        querySb.append("and coalesce(o6.value,0)  <> '1' ");
        querySb.append("and coalesce(o7.value,0) <> '1' ");
        querySb.append("and o8.value IN ('HARMONISATION_C.PROJET') ");
        querySb.append("order by Date_arrivee ");
        List<Object[]> resultList = new DataBaseTracGenericRequests<Object[]>().executeQueryRequest(dbTools, querySb, "NVQ_SELECT");
    }
}
