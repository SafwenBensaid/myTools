/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package threads;

import dto.EnvironnementDTO;
import t24Scripts.T24Scripts;

/**
 *
 * @author 04486
 */
public class TestCobThread extends Thread {

    public boolean problemeEnvironnement;
    public boolean CobEnCours;
    public EnvironnementDTO env;

    public TestCobThread(EnvironnementDTO env) {
        this.env = env;
    }

    @Override
    public void run() {
        try {
            T24Scripts t24Scripts = new T24Scripts();
            String resultatVerifCob = t24Scripts.executerCommandeEnvironnementJSH(env, "LIST F.TSA.SERVICE SERVICE.CONTROL WITH @ID EQ COB\n", "Records Listed");
            String msgErreur = "jpqn : Cannot find proc 'loginproc' in file";
            if (resultatVerifCob.contains(msgErreur)) {
                problemeEnvironnement = true;
            } else {
                problemeEnvironnement = false;
            }
            if (resultatVerifCob.contains("STOP")) {
                CobEnCours = false;
            } else {
                CobEnCours = true;
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
