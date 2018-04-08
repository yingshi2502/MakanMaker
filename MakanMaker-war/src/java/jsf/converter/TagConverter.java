package jsf.converter;

import entity.TagEntity;
import java.util.List;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;



@FacesConverter(value = "tagConverter", forClass = TagEntity.class)

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
            
            for(TagEntity t :tags)
            {
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
        if (value == null) 
        {
            return "";
        }
        
        if (value instanceof String)
        {
            return "";
        }
        
        if (value instanceof TagEntity)
        {            
            TagEntity tag = (TagEntity) value;                        
            
            try
            {
                return tag.getName();
            }
            catch(Exception ex)
            {
                throw new IllegalArgumentException("Invalid value");
            }
        }
        else 
        {
            throw new IllegalArgumentException("Invalid value");
        }
    }
}