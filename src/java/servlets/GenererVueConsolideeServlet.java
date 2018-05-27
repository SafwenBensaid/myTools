/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import dto.RepresentationListTicketDTO;
import dto.UserDTO;
import dto.VueConsolideeDTO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import newTrac.thread.ThreadVueConsolidee;
import newTrac.thread.ThreadVueConsolideeT24;
import springSecurity.SpringSecurityTools;
import tools.Configuration;
import tools.ManipulationObjectsTool;
import tools.Tools;

/**
 *
 * @author 04486
 */
public class GenererVueConsolideeServlet extends HttpServlet {

    private static int checkEndProcess = 0;

    public static synchronized void endProcess() {
        checkEndProcess++;
    }
    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private static Date lastExecutionDate;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String resultatHtml;
        PrintWriter out = response.getWriter();
        try {
            String connectedUser = Tools.getConnectedLogin();
            Tools.clearMapTicketsEnCours(connectedUser);
            String matricule = Tools.getConnectedMatricule();
            UserDTO userDTO = SpringSecurityTools.getUserSearch(matricule, "");
            String userName = userDTO.getNom().toLowerCase();
            request.getSession().setAttribute("userName", userName);
            Map<String, String> matriculeLoginMap = Configuration.matriculeLoginMap;
            Map<String, String> loginMatriculeMap = Configuration.loginMatriculeMap;
            //Si l'utilisateur n'est pas inscrit dans la base ovtools (parametrage des permissions)
            if (!matriculeLoginMap.containsKey(matricule) && !loginMatriculeMap.containsKey(userName)) {
                Tools.insertNewUserIntoDB(matricule);
            }
            resultatHtml = getOldResultOrCalculateIt();
            out.println(resultatHtml);
        } finally {
            out.close();
        }
    }

    public synchronized String getOldResultOrCalculateIt() {
        String resultat = null;
        boolean execution = true;
        String fileName = "VUE.CONSOLIDEE";
        ManipulationObjectsTool man = new ManipulationObjectsTool();
        if (lastExecutionDate == null) {
            lastExecutionDate = new Date("01/01/2010");
        }

        //s'il y a moins de 1 min que l'ancienne requete a été executée, retourner l'ancien résultat, sinon reexecuter la requete 
        if (!Tools.testTempsEcoule(lastExecutionDate, 60)) {
            File serialisedFile = new File(Configuration.parametresList.get("espaceLocal") + System.getProperty("file.separator") + "statistiques" + System.getProperty("file.separator") + fileName);
            try {
                resultat = (String) man.deserialisation(serialisedFile);
                execution = false;
            } catch (Exception ex) {
                execution = true;
            }
        }
        if (execution == true) {
            lastExecutionDate = new Date();
            resultat = afficherVueConsolidee();
            man.serialisation(resultat, fileName, "statistiques");
        }

        return resultat;
    }

    private String afficherVueConsolidee() {
        String resultatHtml = "";
        try {
            Map<String, VueConsolideeDTO> projectDtoMap = new LinkedHashMap<>();
            projectDtoMap.put("T24", new VueConsolideeDTO(null, "livraisons_t24", "172.28.70.74"));

            projectDtoMap.put("HR Access", new VueConsolideeDTO("dbTracHrAccessPU", "HR_ACCESS", "172.28.70.74"));
            projectDtoMap.put("MXP", new VueConsolideeDTO("dbMxpPU", "MXP", "172.28.70.74"));
            projectDtoMap.put("BFI Cartago Intranet", new VueConsolideeDTO("dbBfiCartagoIntranetPU", "BFI_CARTAGO_INTRANET", "172.28.70.74"));
            projectDtoMap.put("Contentieux", new VueConsolideeDTO("dbContentieuxPU", "contentieux", "172.28.70.74"));
            projectDtoMap.put("GTI Winserge", new VueConsolideeDTO("dbGtiWinsergePU", "GTI_WINSERGE", "172.28.70.74"));
            projectDtoMap.put("OGC Compta", new VueConsolideeDTO("dbOgcPU", "OGC", "172.28.70.74"));
            projectDtoMap.put("BFI TITRE", new VueConsolideeDTO("dbBfiTitrePU", "BFI_TITRE", "172.28.70.74"));
            projectDtoMap.put("ICR / PCA", new VueConsolideeDTO("dbICRPU", "ICR", "172.28.70.74"));
            projectDtoMap.put("DAV", new VueConsolideeDTO("dbDavPU", "DAV", "172.28.70.246"));
            projectDtoMap.put("Profilage", new VueConsolideeDTO("dbProfilagePU", "Profilage", "172.28.70.246"));
            projectDtoMap.put("Gestion_Courrier", new VueConsolideeDTO("dbGestionCourrierPU", "Gestion_Courrier", "172.28.70.246"));
            projectDtoMap.put("Option_Change", new VueConsolideeDTO("dbOptionChangePU", "Option_Change", "172.28.70.246"));
            projectDtoMap.put("Prospection_TRE", new VueConsolideeDTO("dbProspectionTrePU", "Prospection_Tre", "172.28.70.246"));

            checkEndProcess = 0;
            //fin
            ///<projet, Map<circuit, Map<acteur, listeTickets>>>
            Map<String, Map<String, Map<String, RepresentationListTicketDTO>>> globalMap = new LinkedHashMap<String, Map<String, Map<String, RepresentationListTicketDTO>>>();

            Map<String, StringBuilder> htmlMap = new LinkedHashMap<String, StringBuilder>();
            Map<String, StringBuilder> htmlPopusMap = new LinkedHashMap<String, StringBuilder>();

            for (Map.Entry<String, VueConsolideeDTO> entry : projectDtoMap.entrySet()) {
                VueConsolideeDTO vueConsolideeDTO = entry.getValue();
                String projetName = entry.getKey();
                Map<String, Map<String, RepresentationListTicketDTO>> projetActuelMap;
                if (globalMap.containsKey(projetName)) {
                    projetActuelMap = globalMap.get(projetName);
                } else {
                    projetActuelMap = new LinkedHashMap<String, Map<String, RepresentationListTicketDTO>>();
                    globalMap.put(projetName, projetActuelMap);
                }
                if (entry.getKey().equals("T24")) {
                    ThreadVueConsolideeT24 threadVueConsolideeProjetCourant = new ThreadVueConsolideeT24(vueConsolideeDTO, projetActuelMap, htmlMap, htmlPopusMap);
                    threadVueConsolideeProjetCourant.start();
                } else {
                    ThreadVueConsolidee threadVueConsolideeProjetCourant = new ThreadVueConsolidee(vueConsolideeDTO, projetActuelMap, htmlMap, htmlPopusMap);
                    threadVueConsolideeProjetCourant.start();
                }

            }
            while (checkEndProcess < projectDtoMap.size()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(GenererVueConsolideeServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //construction de la map
            resultatHtml = generateHtmlCode(projectDtoMap, htmlMap, htmlPopusMap).toString();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return resultatHtml;

    }

    public StringBuilder generateHtmlCode(Map<String, VueConsolideeDTO> projectDtoMap, Map<String, StringBuilder> htmlMap, Map<String, StringBuilder> htmlPopusMap) {
        StringBuilder htmlSb = new StringBuilder();
        htmlSb.append("<table id='tableTicketsHotfix' class='roundCornerTable statistiquesTable'>");
        htmlSb.append("<thead>");
        htmlSb.append("<tr>");
        htmlSb.append("<th class='traitEpaisBas'>");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='4'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Circuit Release");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='5'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Circuit Hotfix (HF/AC)");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='4'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Circuit Projet");
        htmlSb.append("</th>");
        htmlSb.append("<th colspan='4'class='traitEpaisGauche traitEpaisBas'>");
        htmlSb.append("Circuit Upgrade");
        htmlSb.append("</th>");
        htmlSb.append("</tr>");
        htmlSb.append("</thead>");
        htmlSb.append("<tbody>");
        htmlSb.append("<tr class='titre'>");
        htmlSb.append("<td class='traitEpaisBas coin'>Projet</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>DEV</td>");
        htmlSb.append("<td class='traitEpaisBas'>OV</td>");
        htmlSb.append("<td class='traitEpaisBas'>I&E</td>");
        htmlSb.append("<td class='traitEpaisBas'>TEST</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>DEV</td>");
        htmlSb.append("<td class='traitEpaisBas'>OV</td>");
        htmlSb.append("<td class='traitEpaisBas'>I&E</td>");
        htmlSb.append("<td class='traitEpaisBas'>TEST</td>");
        htmlSb.append("<td class='traitEpaisBas'>PROD</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>DEV</td>");
        htmlSb.append("<td class='traitEpaisBas'>OV</td>");
        htmlSb.append("<td class='traitEpaisBas'>I&E</td>");
        htmlSb.append("<td class='traitEpaisBas'>TEST</td>");
        htmlSb.append("<td class='traitEpaisGauche traitEpaisBas'>DEV</td>");
        htmlSb.append("<td class='traitEpaisBas'>OV</td>");
        htmlSb.append("<td class='traitEpaisBas'>I&E</td>");
        htmlSb.append("<td class='traitEpaisBas'>TEST</td>");
        htmlSb.append("</tr>");
        for (Map.Entry<String, VueConsolideeDTO> entry : projectDtoMap.entrySet()) {
            String projetName = entry.getValue().getProjectName();
            htmlSb.append(htmlMap.get(projetName));
        }
        htmlSb.append("</tbody>");
        htmlSb.append("</table>");

        for (Map.Entry<String, StringBuilder> entry : htmlPopusMap.entrySet()) {
            String idPopup = entry.getKey();
            htmlSb.append(htmlPopusMap.get(idPopup));
        }
        return htmlSb;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
