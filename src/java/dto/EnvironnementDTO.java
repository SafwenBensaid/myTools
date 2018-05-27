/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class EnvironnementDTO {

    private String nom;
    private String abreviationNom;
    private String type;
    private String url;
    private String envUserName;
    private String envPassword;
    private String browserUser;
    private String browserPassword;
    private int port;

    public EnvironnementDTO() {
    }

    public EnvironnementDTO(String nom, String abreviationNom, String type, String url, String envUserName, String envPassword, String browserUser, String browserPassword, int port) {
        this.nom = nom;
        this.abreviationNom = abreviationNom;
        this.type = type;
        this.url = url;
        this.envUserName = envUserName;
        this.envPassword = envPassword;
        this.browserUser = browserUser;
        this.browserPassword = browserPassword;
        this.port = port;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAbreviationNom() {
        return abreviationNom;
    }

    public void setAbreviationNom(String abreviationNom) {
        this.abreviationNom = abreviationNom;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEnvUserName() {
        return envUserName;
    }

    public void setEnvUserName(String envUserName) {
        this.envUserName = envUserName;
    }

    public String getEnvPassword() {
        return envPassword;
    }

    public void setEnvPassword(String envPassword) {
        this.envPassword = envPassword;
    }

    public String getBrowserUser() {
        return browserUser;
    }

    public void setBrowserUser(String browserUser) {
        this.browserUser = browserUser;
    }

    public String getBrowserPassword() {
        return browserPassword;
    }

    public void setBrowserPassword(String browserPassword) {
        this.browserPassword = browserPassword;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
