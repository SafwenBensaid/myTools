/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author 04486
 */
public class TicketChangeDTO {

    private String field;
    private String date1;
    private String date2;
    private String newvalue;

    public TicketChangeDTO(String field, String date1, String date2, String newvalue) {
        this.field = field;
        this.date1 = date1;
        this.date2 = date2;
        this.newvalue = newvalue;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDate1() {
        return date1;
    }

    public void setDate1(String date1) {
        this.date1 = date1;
    }

    public String getDate2() {
        return date2;
    }

    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getNewvalue() {
        return newvalue;
    }

    public void setNewvalue(String newvalue) {
        this.newvalue = newvalue;
    }
}
