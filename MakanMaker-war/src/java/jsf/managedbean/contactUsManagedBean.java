/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Named;
import javax.enterprise.context.Dependent;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "contactUsManagedBean")
@Dependent
public class contactUsManagedBean {

    private String dropDownText;
    private String dropDownText2;
    
    public contactUsManagedBean() {
    }
    
    
    public List<String> completeTextOne(String query) {
        List<String> results = new ArrayList<String>();

        results.add(query + "Delivery/Shipping issues");
        results.add(query + "Skip upcoming deliveries");
        results.add(query + "Past and Future Payments");
        results.add(query + "Ingredients in my order");
        results.add(query + "My account settings");
        results.add(query + "Gifts, vouchers and trials");
        results.add(query + "General");
         
        return results;
    }
    
        public List<String> completeTextTwo(String query) {
        List<String> results = new ArrayList<String>();

        results.add(query + "Yes");
        results.add(query + "No. I do not have a Makan Maker account");
         
        return results;
    }

    /**
     * @return the dropDownText
     */
    public String getDropDownText() {
        return dropDownText;
    }

    /**
     * @param dropDownText the dropDownText to set
     */
    public void setDropDownText(String dropDownText) {
        this.dropDownText = dropDownText;
    }

    /**
     * @return the dropDownText2
     */
    public String getDropDownText2() {
        return dropDownText2;
    }

    /**
     * @param dropDownText2 the dropDownText2 to set
     */
    public void setDropDownText2(String dropDownText2) {
        this.dropDownText2 = dropDownText2;
    }


}
