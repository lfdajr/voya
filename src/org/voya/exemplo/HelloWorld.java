//controlador
package org.voya.exemplo;

import java.io.File;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.voya.core.FileUploadUtil;
import org.voya.core.validator.ValidatedController;
import org.voya.core.validator.Validator;

public class HelloWorld implements ValidatedController
{
    private HttpServletRequest request;
    private HttpServletResponse response;
    
    public HelloWorld(HttpServletRequest req, HttpServletResponse res)
    {
        this.request = req;
        this.response = res;
    }
    
    public String index(HttpServletRequest req) throws Exception
    {
        req.setAttribute("objeto", "Voya!");
        
        return "/WEB-INF/templates/index.vm";
    }
    

    //@Override
    public boolean acessoPermitido(org.voya.core.security.Usuario usuario, String metodo) {
        return true;
        //return usuario.getPerfil().contains("manager");
    }

    @Override
    public String validate(Validator valida, String metodo) 
    {
        if (metodo.equals("atualizar"))
        {
            valida.required("data");
            valida.date("data", "dd/MM/YYYY");
            //return "/WEB-INF/templates/contas.vm";
        }
        
        return null;
    }
    
    public String upload()
    {
        try 
        {
            HashMap parametros = FileUploadUtil.processarParametros(request);
            FileItem arquivo = (FileItem) parametros.get("upfile");
            File uploadedFile = new File("/home/99282895491/projetos/voya/build/" + arquivo.getName());
            arquivo.write(uploadedFile);
        } 
        catch (Exception ex) 
        {
            ex.printStackTrace();
        }
        
        return "";
    }
}
