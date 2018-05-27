/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import dto.DetailsLivraisonDTO;

/**
 *
 * @author 04486
 */
public class GenererMessageTrac {

    public String genererMessageTrac(DetailsLivraisonDTO detailsLivraison) {
        String messageTrac = "";
        try {
            Configuration.initialisation();

            String environnementsCircuitReleaseProjetUpgrade = "";
            String branche = "";
            switch (detailsLivraison.getCircuit()) {
                case "RELEASE":
                    environnementsCircuitReleaseProjetUpgrade = Configuration.parametresList.get("environnementsCircuitRelease");
                    branche = "C.RELEASE QL1";
                    break;
                case "PROJET":
                    environnementsCircuitReleaseProjetUpgrade = Configuration.parametresList.get("environnementsCircuitProjet");
                    branche = "C.PROJET";
                    break;
                case "UPGRADE":
                    environnementsCircuitReleaseProjetUpgrade = Configuration.parametresList.get("environnementsCircuitUpgrade");
                    branche = "C.UPGRADE";
                    break;
            }
            if (detailsLivraison.isCusExists() == false) {
                if (detailsLivraison.getCircuit().equals("RELEASE") || detailsLivraison.getCircuit().equals("PROJET") || detailsLivraison.getCircuit().equals("UPGRADE")) {
                    messageTrac = "Le packaging de la '''LIVRAISON  TICKET " + detailsLivraison.getAnomalie() + "''' s'est effectue correctement.  [[BR]]\n";
                    messageTrac += "A deployer uniquement sur '''" + environnementsCircuitReleaseProjetUpgrade + "''' avec la company '''" + detailsLivraison.getMnemonicCompany() + "''' [[BR]]\n";
                    if (detailsLivraison.getNbrIterationDeploiement() > 1) {
                        messageTrac += "'''A deployer le pack 2 fois'''[[BR]]\n";
                    }
                    messageTrac += "Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + "'''   [[BR]]\n";
                    if (detailsLivraison.getRevison().equals("AUCUNE")) {
                        messageTrac += "nb: Le pack n'a genere aucune revision[[BR]]\n";
                    } else {
                        messageTrac += "nb: Le pack est versionne sur le depot_release au niveau de la branche " + branche + " a la revision\n";
                        messageTrac += "[http://172.28.70.176/release_t24/changeset/" + detailsLivraison.getRevison() + " rev " + detailsLivraison.getRevison() + "]";
                        alertProblemeVersionning(detailsLivraison.getRevison(), detailsLivraison.getNumTicket());
                    }
                } else {
                    messageTrac = "Le packaging de la '''LIVRAISON " + Configuration.parametresList.get("phaseRelease") + " TICKET " + detailsLivraison.getAnomalie() + "''' s'est effectue correctement.  [[BR]]\n";
                    messageTrac += "A deployer uniquement sur '''INV''' avec la company '''" + detailsLivraison.getMnemonicCompany() + "''' [[BR]]\n";
                    if (detailsLivraison.getNbrIterationDeploiement() > 1) {
                        messageTrac += "'''A deployer le pack 2 fois'''[[BR]]\n";
                    }
                    messageTrac += "Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + "'''   [[BR]]";
                }
            } else {
                if (detailsLivraison.getCircuit().equals("RELEASE") || detailsLivraison.getCircuit().equals("PROJET") || detailsLivraison.getCircuit().equals("UPGRADE")) {
                    //s'il s'git de deux packs BNK et TN1
                    String revision1 = detailsLivraison.getRevison().split("%_%")[0];
                    String revision2 = detailsLivraison.getRevison().split("%_%")[1];
                    messageTrac = "Le packaging de la '''LIVRAISON TICKET " + detailsLivraison.getAnomalie() + "''' s'est effectue correctement.  [[BR]]\n";
                    messageTrac += "A deployer uniquement sur '''" + environnementsCircuitReleaseProjetUpgrade + "''' [[BR]]\n";
                    if (detailsLivraison.getNbrIterationDeploiement() > 1) {
                        messageTrac += "'''A deployer le deuxieme pack 2 fois'''[[BR]]\n";
                    }
                    messageTrac += "'''1)''' A deployer avec un utilisateur parametre sur '''BNK''': Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + ".BNK'''   [[BR]]\n";
                    messageTrac += "'''2)''' A deployer avec un utilisateur parametre sur '''" + detailsLivraison.getMnemonicCompany() + "''': Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + "." + detailsLivraison.getMnemonicCompany() + "'''   [[BR]][[BR]]\n";
                    if (revision1.equals("AUCUNE")) {
                        messageTrac += "nb: Le premier pack n'a genere aucune revision[[BR]]\n";
                    } else {
                        messageTrac += "nb: Le premier pack est versionne sur le depot_release au niveau de la branche " + branche + " a la revision\n";
                        messageTrac += "[http://172.28.70.176/release_t24/changeset/" + revision1 + " rev " + revision1 + "] [[BR]]";
                        alertProblemeVersionning(revision1, detailsLivraison.getNumTicket());
                    }
                    if (revision2.equals("AUCUNE")) {
                        messageTrac += "nb: Le deuxieme pack n'a genere aucune revision[[BR]]\n";
                    } else {
                        messageTrac += "nb: Le deuxieme pack est versionne sur le depot_release au niveau de la branche " + branche + " a la revision\n";
                        messageTrac += "[http://172.28.70.176/release_t24/changeset/" + revision2 + " rev " + revision2 + "] [[BR]]";
                        alertProblemeVersionning(revision2, detailsLivraison.getNumTicket());
                    }
                } else {
                    messageTrac = "Le packaging de la '''LIVRAISON TICKET " + detailsLivraison.getAnomalie() + "''' s'est effectue correctement.  [[BR]]\n";
                    messageTrac += "A deployer uniquement sur '''INV''' [[BR]]\n";
                    if (detailsLivraison.getNbrIterationDeploiement() > 1) {
                        messageTrac += "'''A deployer le pack 2 fois'''[[BR]]\n";
                    }
                    messageTrac += "'''1)''' A deployer avec un utilisateur parametre sur '''BNK''': Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + ".BNK'''   [[BR]]\n";
                    messageTrac += "'''2)''' A deployer avec un utilisateur parametre sur '''" + detailsLivraison.getMnemonicCompany() + "''': Le pack est sous bnk.run/PACK.TAF/'''" + detailsLivraison.getPackName() + "." + detailsLivraison.getMnemonicCompany() + "'''";
                }
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException("Num ticket qui a cause le probleme: #" + detailsLivraison.getNumTicket() + "  \n " + tools.Tools.getStackTrace(exep));
        }
        return messageTrac;
    }

    private void alertProblemeVersionning(String revision, String numLiv) {
        if (revision == null || (revision != null && revision.trim().length() == 0)) {
            String objet = "ATTENTION PROBLEME TRES GRAVE";
            String[] to = {"C24OV@biat.com.tn"};
            String[] cc = new String[]{};
            String[] bcc = new String[]{};
            String sender = "OV.Management.Solutions@biat.com.tn";
            String emailContent = "<b>Attention la livraison #" + numLiv + " a rencontré un problème de versionning!!!</b><br> Prière de vérifier sa révision et de la retraiter si c est nécessaire";
            try {
                tools.Tools.sendEMail(objet, sender, to, cc, bcc, emailContent, false);
            } catch (Exception ex) {
                Tools.traiterException("Problème envoi email notification COB:\n" + ex.toString());
            }
        }
    }
}
