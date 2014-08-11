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

import java.lang.reflect.Constructor;
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

/**
 * Highly productive programming framework for Java web development.
 * @author Lourival F. de Almeida Júnior
 */
public class VoyaServlet extends HttpServlet {
    
    private String pacoteClassesControle;
    private String pacoteClassesView;
    
    //Contém os parâmetros dos métodos das classes de controles, onde as chaves 
    //são o nome do método: org.projeto.Controlado1.teste
    private HashMap<String, Class> parametrosMetodosClassesControllersMap;
    
    private HashMap<String, Class> classesControllersMap;
    private HashMap<String, Object> classesViewMap;
    
    @Override
    public void init() throws ServletException 
    {
        pacoteClassesControle = getServletConfig().getInitParameter("ControllerClassesPackage") + ".";
        pacoteClassesView = getServletConfig().getInitParameter("ViewClassesPackage") + ".";
        parametrosMetodosClassesControllersMap = new HashMap<String, Class>();
        classesControllersMap = new HashMap<String, Class>();
        classesViewMap = new HashMap<String, Object>();
        
        
        try {
            //Buscando uma classe org.voya.bootstrap.BootstrapImpl.class
            //Que deve ser colocada na aplicação cliente
            Class boot = Class.forName("org.voya.bootstrap.BootstrapImpl");
            Bootstrap bootstrap = (Bootstrap) boot.newInstance();
            bootstrap.inicializar();
            bootstrap.inicializarConverters();
        } 
        catch (Exception ex) {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.inicializar();
            bootstrap.inicializarConverters();
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
            pv = new Parametros(parametros[2], parametros[3], pacoteClassesView);
            
            Class classeController = classesControllersMap.get(p.classeCompleto);
            Object classeView = null;
            
            try
            {
                classeView = classesViewMap.get(pv.classeCompleto);
                if (classeView == null)
                {
                    classeView = Class.forName(pv.classeCompleto).newInstance();
                    classesViewMap.put(pv.classeCompleto, classeView);
                }
            }
            catch (Exception ex)
            {
                //Não é para fazer nada se não tiver um view definido
            }
            
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
        }
        return p;
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        response.setContentType("text/html;charset=UTF-8");
        
        //Processando metodos para acelerar buscar posteriores
        Parametros parametros = inicializarMaps(request.getRequestURI());

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

                if (tipo == MetodoSemParametro.class)
                {
                    metodo = classeController.getMethod(parametros.metodo);

                    Object instancia = classeController.newInstance();
                    Object retorno = metodo.invoke(instancia);

                    if (retorno != null)
                        request.setAttribute("objeto", retorno);
                }
                else if (tipo == HttpServletRequest.class)
                {
                    metodo = classeController.getMethod(parametros.metodo, tipo);

                    Object instancia = classeController.newInstance();
                    metodo.invoke(instancia, request);
                }
                else
                {
                    //metodo = classeController.getMethod(parametros.metodo, tipo, HttpServletRequest.class);
                    Object bean = tipo.newInstance();
                    BeanUtils.populate(bean, request.getParameterMap());
                    Object instancia = null;
                    
                    metodo = classeController.getMethod(parametros.metodo, tipo);
                    try
                    {
                        Constructor c = classeController.getConstructor(HttpServletRequest.class, HttpServletResponse.class);
                        instancia = c.newInstance(request, response);
                    }
                    catch (Exception e)
                    {
                        instancia = classeController.newInstance();
                    }
                    
                    Object retorno = metodo.invoke(instancia, bean);

                    if (retorno != null)
                        request.setAttribute("objeto", retorno);
                }
            }
            else
                throw new Exception("Método passado -" + parametros.metodo + "- não existe no controller " + parametros.classeCompleto);
        }
        catch (NoSuchMethodException ex) 
        {
            throw new Exception("Método passado -" + parametros.metodo + "- não existe no controller " + parametros.classeCompleto);
        }
        catch (Exception ex) 
        {
            ex.printStackTrace();
            throw new Exception("erro geral");
        }

        //Se tiver algum problema em não ter view, retorna para a view
        //correspondente ao template cujo nome é o nome do método
        String viewRetorno = null;
        try
        {
            Object instanciaView = classesViewMap.get(pacoteClassesView + parametros.classe);
            Method mView = instanciaView.getClass().getMethod(parametros.metodo, HttpServletRequest.class, HttpServletResponse.class);
            viewRetorno = (String) mView.invoke(instanciaView, request, response);
        } 
        catch (Exception ex) 
        {
            viewRetorno = "/WEB-INF/templates/" + parametros.metodo + ".vm";
        }
        finally
        {
            RequestDispatcher nextView = request.getRequestDispatcher(viewRetorno);
            nextView.forward(request,response);
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
        return "Short description";
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
