/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class TicketFilsDTO {

    private Integer anomalie;
    private Integer livraison;
    private Integer ticketFils;
    private String EnvironnementName;
    private String developpeur;

    public TicketFilsDTO(Integer anomalie, Integer livraison, Integer ticketFils, String EnvironnementName, String developpeur) {
        this.anomalie = anomalie;
        this.livraison = livraison;
        this.ticketFils = ticketFils;
        this.EnvironnementName = EnvironnementName;
        this.developpeur = developpeur;
    }

    public Integer getAnomalie() {
        return anomalie;
    }

    public void setAnomalie(Integer anomalie) {
        this.anomalie = anomalie;
    }

    public Integer getLivraison() {
        return livraison;
    }

    public void setLivraison(Integer livraison) {
        this.livraison = livraison;
    }

    public Integer getTicketFils() {
        return ticketFils;
    }

    public void setTicketFils(Integer ticketFils) {
        this.ticketFils = ticketFils;
    }

    public String getEnvironnementName() {
        return EnvironnementName;
    }

    public void setEnvironnementName(String EnvironnementName) {
        this.EnvironnementName = EnvironnementName;
    }

    public String getDeveloppeur() {
        return developpeur;
    }

    public void setDeveloppeur(String developpeur) {
        this.developpeur = developpeur;
    }
}
