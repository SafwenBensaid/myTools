/*
 * To change this template; choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entitiesMysql.Livraison;

/**
 *
 * @author 04486
 */
public class HarmonisationDTO {

    private String circuit;
    private Livraison livParent;
    private Integer numTicketLivraisonFils;
    private String connectedUser;
    private String contenuDesLivrables;
    private String developpeur;
    private String actionManuelle;

    public HarmonisationDTO(String circuit, Livraison livParent, Integer numTicketLivraisonFils, String connectedUser, String contenuDesLivrables, String developpeur, String actionManuelle) {
        this.circuit = circuit;
        this.livParent = livParent;
        this.numTicketLivraisonFils = numTicketLivraisonFils;
        this.connectedUser = connectedUser;
        this.contenuDesLivrables = contenuDesLivrables;
        this.developpeur = developpeur;
        this.actionManuelle = actionManuelle;
    }

    public String getCircuit() {
        return circuit;
    }

    public void setCircuit(String circuit) {
        this.circuit = circuit;
    }

    public Livraison getLivParent() {
        return livParent;
    }

    public void setLivParent(Livraison livParent) {
        this.livParent = livParent;
    }

    public Integer getNumTicketLivraisonFils() {
        return numTicketLivraisonFils;
    }

    public void setNumTicketLivraisonFils(Integer numTicketLivraisonFils) {
        this.numTicketLivraisonFils = numTicketLivraisonFils;
    }

    public String getConnectedUser() {
        return connectedUser;
    }

    public void setConnectedUser(String connectedUser) {
        this.connectedUser = connectedUser;
    }

    public String getContenuDesLivrables() {
        return contenuDesLivrables;
    }

    public void setContenuDesLivrables(String contenuDesLivrables) {
        this.contenuDesLivrables = contenuDesLivrables;
    }

    public String getDeveloppeur() {
        return developpeur;
    }

    public void setDeveloppeur(String developpeur) {
        this.developpeur = developpeur;
    }

    public String getActionManuelle() {
        return actionManuelle;
    }

    public void setActionManuelle(String actionManuelle) {
        this.actionManuelle = actionManuelle;
    }
}
