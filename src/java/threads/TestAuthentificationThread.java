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
public class TestAuthentificationThread extends Thread {

    public String resultatTestAuthentification = "";
    public EnvironnementDTO env;
    public String userName;

    public TestAuthentificationThread(EnvironnementDTO env, String userName) {
        this.env = env;
        this.userName = userName;
    }

    @Override
    public void run() {
        try {
            T24Scripts t24Scripts = new T24Scripts(userName);
            resultatTestAuthentification = t24Scripts.testLoginPasswordSystemeEtBrowser(env, userName, "logParallele").trim();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
