package jsf.converter;

import entity.TagEntity;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



@FacesConverter(value = "tagConverter") //, forClass = TagEntity.class

public class TagConverter implements Converter
{
    public TagConverter()
    {
    }
    
    
    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value)
    {
        if (value == null || value.trim().length() == 0 || value.equals("null")) 
        {
            return null;
        }

        try
        {            
            List<TagEntity> tags = (List<TagEntity>)context.getExternalContext().getSessionMap().get("TagEntityConverter.tags");
        //    System.err.println("****Tag converter"+tags.size());
            
            for(TagEntity t :tags)
            {
              //  System.err.println("****Tag converter LOOP"+t.getName());

                if(t.getName().equals(value))
                {
                    return t;
                }
            }
        }
        catch(Exception ex)
        {
            throw new IllegalArgumentException("Please select a valid value");
        }
        
        return null;
    }

    
    
    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value)
    {
//        if (value == null) 
//        {
//            return "";
//        }
//        
//        if (value instanceof String)
//        {
//            return "";
//        }
        // System.err.println("****Inside converter getAsString ex");

        if (value instanceof TagEntity)
        {            
          //  System.err.println("****Tag converter getAsString "+((TagEntity) value).getName());
            TagEntity tag = (TagEntity) value;                        
            try
            {
                return tag.getName();
            }
            catch(Exception ex)
            {
               // System.err.println("****Tag converter getAsString ex"+ex.getMessage());
                throw new IllegalArgumentException("Invalid value");
            }
        }
        else 
        {

            throw new IllegalArgumentException("Invalid value");
        }
    }
}


/*

@FacesConverter(value = "beerConverter")
public class BeerConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent uiComponent, String beerId) {
        ValueExpression vex =
                ctx.getApplication().getExpressionFactory()
                        .createValueExpression(ctx.getELContext(),
                                "#{beersBean}", BeersBean.class);

        BeersBean beers = (BeersBean)vex.getValue(ctx.getELContext());
        return beers.getBeer(Integer.valueOf(beerId));
    }

    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object beer) {
        return ((Beer)beer).getId().toString();
    }

}
*/