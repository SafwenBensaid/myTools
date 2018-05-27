package tools;

import java.io.*;

public class ManipulationTool {

    public static byte[] Serialiser(Object objectToBeSerialised) {
        byte[] fichierEnByte = null;
        try {
            File fichier = new File("/work/SERIALISEDOBJECT");
            // ouverture d'un flux sur un fichier
            ObjectOutputStream oos = null;
            try {
                oos = new ObjectOutputStream(new FileOutputStream(fichier));
                oos.writeObject(objectToBeSerialised);

            } catch (IOException ex) {
            } finally {
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException ex) {
                }
            }
            fichierEnByte = convertFileToByte(fichier);
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return fichierEnByte;
    }

    public static byte[] convertFileToByte(File file) {
        byte[] b = new byte[(int) file.length()];
        try {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                fileInputStream.read(b);
                fileInputStream.close();
            } catch (FileNotFoundException e) {
                Tools.showConsolLog("File Not Found.");
                e.printStackTrace();
            } catch (IOException e1) {
                Tools.showConsolLog("Error Reading The File.");
                e1.printStackTrace();
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return b;
    }

    public boolean authentification(String login, String password) {
        if (login.equals("login") && password.equals("pass")) {
            return true;
        } else {
            return false;
        }
    }

    public Object deserialisation(File file) {
        FileInputStream fichier = null;
        ObjectInputStream ois = null;
        Object obj = null;
        try {
            fichier = new FileInputStream(file);
            ois = new ObjectInputStream(fichier);
            obj = (Object) ois.readObject();
            return obj;
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                fichier.close();
                ois.close();
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return obj;
    }

    public File createFile(byte[] serializedObjectByteArray) {
        FileOutputStream fileOuputStream = null;
        File file = null;
        try {
            file = new File("/work/SERIALISEDOBJECT");
            fileOuputStream = new FileOutputStream(file);
            fileOuputStream.write(serializedObjectByteArray);
            fileOuputStream.close();
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        } finally {
            try {
                fileOuputStream.close();
            } catch (Exception exep) {
                exep.printStackTrace();
                tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
            }
        }
        return file;
    }
}
