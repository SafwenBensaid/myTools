/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tools.AttachmentsTools;
import tools.Configuration;
import tools.DataBaseTools;
import tools.Tools;

/**
 *
 * @author 04494
 */
@WebServlet(name = "UploadFileServlet", urlPatterns = {"/UploadFileServlet"})
public class UploadFileServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        // Check that we have a file upload request
        String idTicket = "";
        String pu = Configuration.puGestionDesDemandes;
        DataBaseTools dbTools = new DataBaseTools(pu);
        GestionDemandesMetiersServlet gd = new GestionDemandesMetiersServlet();

        if (request.getParameterMap().containsKey("idTicket") && request.getParameter("idTicket") != null && !request.getParameter("idTicket").isEmpty()) {
            // Si la session contient un clé idTicket -> il s'agit d'un update
            idTicket = request.getParameter("idTicket");
        } else {
            // Sinon -> il s'a&git d'une nouvelle création
            idTicket = ((Integer) (gd.getNbrTickets(dbTools) + 1)).toString();
        }
        String connectedUser = Tools.getConnectedLogin();
        // Create a factory for disk-based file items
        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Configure a repository (to ensure a secure temp location is used)
        ServletContext servletContext = this.getServletConfig().getServletContext();
        File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);
        List<FileItem> items = new ArrayList<>();
        try {
            // Parse the request
            items = upload.parseRequest(request);
        } catch (FileUploadException ex) {
            Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        String destinationFolderAttachments = Configuration.parametresList.get("cheminAttachmentsGD");
        // Process the uploaded items
        Iterator<FileItem> iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = iter.next();
            if (!item.isFormField()) {
                String fileName = item.getName();
                byte[] fileData = item.get();
                
                if (!fileName.isEmpty() && fileData.length > 0) {
                    System.out.println("Upload File  " + item.getName() + " Ticket " + idTicket.toString());
                    //Create Decrypted file (normal file)
                    File file = AttachmentsTools.createFileFromFormFile(fileName, fileData, destinationFolderAttachments, idTicket.toString());
                    //Add attachement record into DB + create encrypted files
                    AttachmentsTools.createAttachment(pu, fileName, fileData, idTicket.toString(), destinationFolderAttachments, connectedUser);
                } else {
                    System.out.println("Aucun fichier a attacher  " + item.getName() + " Ticket " + idTicket.toString());
                }
            }
        }


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
