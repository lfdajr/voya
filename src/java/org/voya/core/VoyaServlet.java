/*
Voya Framework

O Voya é uma framework java para desenvolvimento de aplicações web de forma rápida e com 
funcionalidades voltadas para produtividade.

O Voya não utiliza arquivos de configuração, e tudo está feito a partir de definições do 
próprio framework para facilitar o desenvolvimento. O voya utiliza para sua camada de 
apresentação o framework de templates Apache Velocity.
*/

/*
http://localhost:8081/voya/teste/sair?Nome=30&idade=20

ContextPath: /voya
RequestURI: /voya/teste/sair
QueryString: Nome=30&idade=20
ServletPath:
RequestURL: http://localhost:8081/voya/teste/sair

*/

package org.voya.core;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConversionException;
import org.voya.core.security.LoginLogoutController;
import org.voya.core.security.SecuredController;
import org.voya.core.security.Usuario;
import org.voya.core.validator.ValidatedController;
import org.voya.core.validator.ValidationException;
import org.voya.core.validator.Validator;


/**
 * Highly productive programming framework for Java web development.
 * @author Lourival F. de Almeida Júnior
 * @version 0.1.3
 */
public class VoyaServlet extends HttpServlet {
    
    private String pacoteClassesControle;
    
    //Contém os parâmetros dos métodos das classes de controles, onde as chaves 
    //são o nome do método: org.projeto.Controlado1.teste
    private HashMap<String, Class> parametrosMetodosClassesControllersMap;
    private HashMap<String, Class> classesControllersMap;
    
    private static Bootstrap bootstrap;
    
    @Override
    public void init() throws ServletException 
    {
        pacoteClassesControle = getServletConfig().getInitParameter("ControllerClassesPackage") + ".";
        parametrosMetodosClassesControllersMap = new HashMap<String, Class>();
        classesControllersMap = new HashMap<String, Class>();
        
        classesControllersMap.put(Globals.CLASSE_LOGIN_LOGOUT_CONTROLLER, LoginLogoutController.class);
        parametrosMetodosClassesControllersMap.put(Globals.CLASSE_LOGIN_LOGOUT_CONTROLLER + ".login", HttpServletRequest.class);
        parametrosMetodosClassesControllersMap.put(Globals.CLASSE_LOGIN_LOGOUT_CONTROLLER + ".loginDo", HttpServletRequest.class);
        parametrosMetodosClassesControllersMap.put(Globals.CLASSE_LOGIN_LOGOUT_CONTROLLER + ".logout", HttpServletRequest.class);
        
        try {
            //Buscando uma classe org.voya.bootstrap.BootstrapImpl.class
            //Que deve ser colocada na aplicação cliente
            Class boot = Class.forName("org.voya.bootstrap.BootstrapImpl");
            bootstrap = (Bootstrap) boot.newInstance();
            bootstrap.inicializar();
            bootstrap.inicializarConverters();
        } 
        catch (Exception ex) {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Nenhuma classe bootstrap para ser inicializada", ex);
        }
        
    }
    
    /*
    Processa o request colocando os metodos de cada classe em um hashmap para facilitar procura
    O método retorna a classe que está sendo chamada para esta url
    */
    private Parametros inicializarMaps(String url) throws ClassNotFoundException
    {
        //TODO: Tratar o método para que se for passado somente 3 parâmetros ele não quebre e assim por diante
        Parametros p = null, pv = null;
        String[] parametros = url.split("/");
        
        if (parametros.length == 4)
        {
            p = new Parametros(parametros[2], parametros[3], pacoteClassesControle);
            
            Class classeController = classesControllersMap.get(p.classeCompleto);
            
            if (classeController == null)
            {
                classeController = Class.forName(p.classeCompleto);
                classesControllersMap.put(p.classeCompleto, classeController);
            }
            
            //Se o método não estiver mapeado, ele será adicionado no hash para facilitar
            //ser encontrado em outra requisição
            if (parametrosMetodosClassesControllersMap.get(p.metodoCompleto) == null)
            {
                Method[] metodos = classeController.getDeclaredMethods();

                for (Method metodo : metodos)
                {
                    if (metodo.getModifiers() == 1)
                    {
                        if (metodo.getParameterCount() > 0)
                            parametrosMetodosClassesControllersMap.put(p.classeCompleto + "." + metodo.getName(), metodo.getParameterTypes()[0]);
                        else
                            parametrosMetodosClassesControllersMap.put(p.classeCompleto + "." + metodo.getName(), MetodoSemParametro.class);
                    }
                }
            }
            return p;
        }
        else
        {
            p = new Parametros("LoginLogoutController", parametros[parametros.length - 1], "org.voya.core.security.");
            return p;
        }
        
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    {
        response.setContentType("text/html;charset=UTF-8");
        boolean permitidoExecucao = true;
        String viewRetorno = null;
        
        //Processando metodos para acelerar buscar posteriores
        Parametros parametros = null;
        try {
            parametros = inicializarMaps(request.getRequestURI());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        try 
        {
            Class classeController = classesControllersMap.get(parametros.classeCompleto);

            Method metodo = null;
            Class tipo = parametrosMetodosClassesControllersMap.get(parametros.metodoCompleto);

            //Se tipo é nulo significa dizer que o método requisitado não 
            //existe no controller passado como parâmetro
            //Neste if acontece a execução do método da classe passada como parâmetro
            if (tipo != null) 
            {
                Object instancia = null;
                
                try
                {
                    Constructor c = classeController.getConstructor(HttpServletRequest.class, HttpServletResponse.class);
                    instancia = c.newInstance(request, response);
                }
                catch (Exception e)
                {
                    instancia = classeController.newInstance();
                }
                
                //Se o controller é do tipo seguro efetuar o controle de acesso
                if (instancia instanceof SecuredController)
                {
                    Usuario usuarioSessao = (Usuario) request.getSession().getAttribute(Globals.SESSAO_USUARIO);
                    
                    if (usuarioSessao != null)
                        permitidoExecucao = ((SecuredController)instancia).acessoPermitido(usuarioSessao, parametros.metodo);
                    else
                        permitidoExecucao = false;
                }
                
                if (instancia instanceof ValidatedController)
                {
                    Validator validador = new Validator(request);
                    viewRetorno = ((ValidatedController)instancia).validate(validador, parametros.metodo);
                    if (validador.hasErrors())
                    {
                        request.setAttribute(Globals.ERROR_VAR, validador.getErrors());
                        throw new ValidationException();
                    }
                }

                if (tipo == MetodoSemParametro.class && permitidoExecucao)
                {
                    metodo = classeController.getMethod(parametros.metodo);
                    viewRetorno = (String) metodo.invoke(instancia);
                }
                else if (tipo == HttpServletRequest.class && permitidoExecucao)
                {
                    metodo = classeController.getMethod(parametros.metodo, tipo);
                    viewRetorno = (String) metodo.invoke(instancia, request);
                }
                else if (permitidoExecucao)
                {
                    //metodo = classeController.getMethod(parametros.metodo, tipo, HttpServletRequest.class);
                    Object bean = tipo.newInstance();
                    BeanUtils.populate(bean, request.getParameterMap());
                    
                    metodo = classeController.getMethod(parametros.metodo, tipo);
                    
                    viewRetorno = (String) metodo.invoke(instancia, bean);
                }
                else if (!permitidoExecucao)
                {
                    viewRetorno = "redirect:" + request.getContextPath() + "/login";
                    Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Acesso não permitido: " + request.getRequestURI(), viewRetorno);
                }
            }
            else
                Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Método passado -" + parametros.metodo + "- não existe no controller " + parametros.classeCompleto, "");
        }
        catch (ValidationException ex)
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Erro de validação", "");
        }
        catch (ConversionException ex)
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Problema na conversão de valores", "");
        }
        catch (NoSuchMethodException ex) 
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Método passado -" + parametros.metodo + "- não existe no controller " + parametros.classeCompleto, "");
        } 
        catch (IllegalAccessException ex) 
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (IllegalArgumentException ex) 
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        catch (InvocationTargetException ex) 
        {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        } 
        /*catch (Exception ex) 
        {
            ex.printStackTrace();
            throw new Exception("erro geral");
        }*/
        
        if (viewRetorno == null)
            viewRetorno = "/WEB-INF/templates/" + parametros.classe + "/" + parametros.metodo + ".vm";
        
        RequestDispatcher nextView = request.getRequestDispatcher(viewRetorno);
        
        if (viewRetorno.startsWith("redirect:"))
        {
            viewRetorno = viewRetorno.replace("redirect:", "");
            try 
            {
                response.sendRedirect(viewRetorno);
                //nextView.include(request, response);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Recurso não encontrado: " + viewRetorno, ex);
            }
        }
        else
        {
            try 
            {
                nextView.forward(request, response);
            } 
            catch (ServletException ex) 
            {
                Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
            } 
            catch (IOException ex) 
            {
                Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, "Recurso não encontrado: " + viewRetorno, ex);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(VoyaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Voya Servlet";
    }
    
    public static Bootstrap getBootstrap()
    {
        return bootstrap;
    }
}


class Parametros
{
    public String classeCompleto; //org.app.Action
    public String metodo; //teste
    public String metodoCompleto; //org.app.Action.teste
    public String classe; //Action
    
    public Parametros(String classe, String metodo, String pacote)
    {
        this.classe = classe;
        this.metodo = metodo;
        classeCompleto = pacote + classe;
        metodoCompleto = classeCompleto + "." + metodo;
    }
    
}

class MetodoSemParametro
{
    
}
