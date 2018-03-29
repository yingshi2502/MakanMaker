
package jsf.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 *
 * @author yingshi
 */
@FacesConverter(value = "genderConverter")

public class GenderConverter implements Converter{

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        if (value.equals("Male")) return 0;
        return 1;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        Integer gender =  (Integer)value;
        if (gender.equals(1)){
            return "Female";
        }else{
            return "Male";
        }
    }

}
