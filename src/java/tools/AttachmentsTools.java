/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import entitiesTrac.Attachment;
import entitiesTrac.AttachmentPK;
import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import org.apache.commons.io.FileUtils;
import org.apache.struts.upload.FormFile;

/**
 *
 * @author 04486
 */
public class AttachmentsTools {

    public static void createAttachment(String pu, String fileName, byte[] fileData, String idTicketLivraison, String destinationFolderAttachments, String connectedUser) {
        try {
            String id = idTicketLivraison;
            String type = "ticket";
            String extention = null;
            String sha1ID = Tools.sha1(id);
            String sha1FileName = Tools.sha1(fileName);
            String sha1ID3 = sha1ID.substring(0, 3);
            if (fileName.contains(".")) {
                String[] auxTab = fileName.split("\\.");
                extention = "." + auxTab[auxTab.length - 1];
            } else {
                extention = "";
            }
            String destinationFolder = destinationFolderAttachments + "\\" + type + "\\" + sha1ID3 + "\\" + sha1ID;
            new File(destinationFolder).mkdirs();
            //créer fichiers
            try {
                String fileDestination = destinationFolder + "\\" + sha1FileName + extention;
                FileUtils.writeByteArrayToFile(new File(fileDestination), fileData);
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
            //insert attachment object into database
            int fileSize = fileData.length;
            long timeTrac = UpdateTicketsTools.generateTracDateNow();
            Attachment attachment = new Attachment();
            AttachmentPK attachmentPK = new AttachmentPK();
            attachmentPK.setType("ticket");
            attachmentPK.setId(idTicketLivraison);
            attachmentPK.setFilename(fileName);
            attachment.setAttachmentPK(attachmentPK);
            attachment.setTime(timeTrac);
            attachment.setAuthor(connectedUser);
            attachment.setDescription("");
            attachment.setSize(fileSize);
            attachment.setIpnr("127.0.0.1");
            DataBaseTools dbTools = new DataBaseTools(pu);
            dbTools.update(attachment);
            dbTools.closeRessources();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static File createFileFromFormFile(String fileName, byte[] fileData,String destinationFolderAttachments, String numLivraison) {
        String destinationFolder = destinationFolderAttachments + "\\attachmentsDecrypted\\" + numLivraison;
        new File(destinationFolder).mkdirs();
        //créer fichiers
        String fileDestination = null;
        try {
            fileDestination = destinationFolder + "\\" + fileName;
            FileUtils.writeByteArrayToFile(new File(fileDestination), fileData);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return new File(fileDestination);
    }
}
