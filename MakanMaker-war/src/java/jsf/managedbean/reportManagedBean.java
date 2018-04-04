/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperRunManager;

/**
 *
 * @author Ismahfaris
 */
@Named(value = "reportManagedBean")
@RequestScoped
public class reportManagedBean {

    @Resource(name = "makanMakerDataSource")
    private DataSource makanMakerDataSource;

    /**
     * Creates a new instance of reportManagedBean
     */
    public reportManagedBean() {
    }
    
    public void generateReport(ActionEvent event){
        try{
            InputStream reportStream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/jasperreports/newmealkit.jasper");
            OutputStream outputStream = FacesContext.getCurrentInstance().getExternalContext().getResponseOutputStream();
            
            JasperRunManager.runReportToPdfStream(reportStream,outputStream, null, makanMakerDataSource.getConnection());
                    
                    
         }
        
        catch(JRException ex){
            ex.printStackTrace();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }      
        catch(IOException ex){
            
        }
    }
    
}
