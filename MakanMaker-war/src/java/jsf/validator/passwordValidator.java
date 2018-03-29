package jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author yingshi
 */
@FacesValidator(value = "passwordValidator")
public class passwordValidator implements Validator {

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {

        if (value != null && value.toString().trim().length() > 0) {
            String password = (String) value;
            String pattern = "(?=.*[0-9])(?=.*[a-z])(?=\\S+$).{6,}";
            if (!password.matches(pattern)) {
                throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Password invalid!", null));
            }
        } else {
            throw new ValidatorException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "password empty", null));
        }

    }

}
