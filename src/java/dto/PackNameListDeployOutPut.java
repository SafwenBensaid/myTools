/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import java.util.List;

/**
 *
 * @author 04486
 */
public class PackNameListDeployOutPut {

    private String packName;
    private String mnemonic;
    private List<String> deployOutput;

    public PackNameListDeployOutPut(String packName, String mnemonic, List<String> deployOutput) {
        this.packName = packName;
        this.mnemonic = mnemonic;
        this.deployOutput = deployOutput;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public List<String> getDeployOutput() {
        return deployOutput;
    }

    public void setDeployOutput(List<String> deployOutput) {
        this.deployOutput = deployOutput;
    }

    public String getMnemonic() {
        return mnemonic;
    }

    public void setMnemonic(String mnemonic) {
        this.mnemonic = mnemonic;
    }
}
