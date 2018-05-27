/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class VerificationDeltaChampsDTO {

    private String environnementSourceName;
    private String packName;
    private String environnementDestinationName;
    private String urlCompressedFolder;
    private String nameCompressedFolder;

    public String getEnvironnementSourceName() {
        return environnementSourceName;
    }

    public void setEnvironnementSourceName(String environnementSourceName) {
        this.environnementSourceName = environnementSourceName;
    }

    public String getNameCompressedFolder() {
        return nameCompressedFolder;
    }

    public void setNameCompressedFolder(String nameCompressedFolder) {
        this.nameCompressedFolder = nameCompressedFolder;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public String getEnvironnementDestinationName() {
        return environnementDestinationName;
    }

    public void setEnvironnementDestinationName(String environnementDestinationName) {
        this.environnementDestinationName = environnementDestinationName;
    }

    public String getUrlCompressedFolder() {
        return urlCompressedFolder;
    }

    public void setUrlCompressedFolder(String urlCompressedFolder) {
        this.urlCompressedFolder = urlCompressedFolder;
    }
}
