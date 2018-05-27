/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

/**
 *
 * @author 04486
 */
public class StringBuilderTools {

    /**
     * @param args the command line arguments
     */
    public static boolean stringBuilderContainsString(StringBuilder sb, String ch) {
        try {
            if (sb.indexOf(ch) != -1) {
                return true;
            } else {
                return false;
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
        return false;
    }

    public static void clearStringBuilder(StringBuilder sb) {
        sb.setLength(0);
    }

    public static String getStringValueOfStringBuilder(StringBuilder sb) {
        return sb.toString();
    }

    public static void replaceFirstOccurenceIntoStringBuilder(StringBuilder sb, String oldString, String newString) {
        int firstIndex = 0;
        try {
            firstIndex = sb.indexOf(oldString);
            if (firstIndex != -1) {
                sb.replace(firstIndex, firstIndex + oldString.length(), newString);
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }

    public static void deleteFirstOccurenceIntoStringBuilder(StringBuilder sb, String toBeDeletedString) {
        int firstIndex = 0;
        try {
            firstIndex = sb.indexOf(toBeDeletedString);
            if (firstIndex != -1) {
                sb.delete(firstIndex, firstIndex + toBeDeletedString.length());
            }
        } catch (Exception exep) {
            exep.printStackTrace();
            tools.Tools.traiterException(tools.Tools.getStackTrace(exep));
        }
    }
}
