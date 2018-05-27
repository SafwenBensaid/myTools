/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class VueConsolideeDTO {

    private String persistenceUnit;
    private String projectName;
    private String adresseIP;

    public VueConsolideeDTO(String persistenceUnit, String projectName, String adresseIP) {
        this.persistenceUnit = persistenceUnit;
        this.projectName = projectName;
        this.adresseIP = adresseIP;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }

    public String getPersistenceUnit() {
        return persistenceUnit;
    }

    public void setPersistenceUnit(String persistenceUnit) {
        this.persistenceUnit = persistenceUnit;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
