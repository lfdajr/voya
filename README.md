Voya Framework
====

Voya Framework is an integrated full-stack Java framework for rapid web development.
It's a simple but powerful dispatch action with no configuration required (convention over configuration).


Get Started!
====

Use the VoyaServlet in the web.xml configuration file and you're good to go.
The url are mapped over conventions. 

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.0" xmlns="http://java.sun.com/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd">
    <servlet>
        <servlet-name>VelocityView</servlet-name>
        <servlet-class>org.apache.velocity.tools.view.VelocityViewServlet</servlet-class>
    </servlet>
    
    <servlet>
        <servlet-name>VoyaServlet</servlet-name>
        <servlet-class>org.voya.core.VoyaServlet</servlet-class>
        <init-param>
            <param-name>ControllerClassesPackage</param-name>
            <param-value>assistente.controle</param-value>
        </init-param>
    </servlet>
    
    <servlet>
        <servlet-name>VoyaJsonServlet</servlet-name>
        <servlet-class>org.voya.core.VoyaJsonServlet</servlet-class>
    </servlet>
    
    <servlet-mapping>
        <servlet-name>VelocityView</servlet-name>
        <url-pattern>*.vm</url-pattern>
    </servlet-mapping>
    
    <servlet-mapping>
        <servlet-name>VoyaJsonServlet</servlet-name>
        <url-pattern>*.json</url-pattern>
    </servlet-mapping>
    
    <servlet-mapping>
            <servlet-name>default</servlet-name>
            <url-pattern>/resources/*</url-pattern>
    </servlet-mapping>
    
    <servlet-mapping>
        <servlet-name>VoyaServlet</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>    
    
    <session-config>
        <session-timeout>
            30
        </session-timeout>
    </session-config>
</web-app>
```

VoyaServlet is the main servlet, VelocityViewServlet gets the templates processing tasks and the VoyaJsonServlet is responsible for returning objects as json to the browser.

<domain>/ExampleController/method?name=john&year=1...
This url maps to a controller class method.

```
class MyBean
{
  private String name;
  private Integer year;
  
  set's...
  get's...
}


class ExampleController
{
  private HttpServletRequest request;
  private HttpServletResponse response;

  public ExampleController()
  {
  }
  
  public ExampleController(HttpServletRequest r, HttpServletResponse res)
  {
    this.request = r;
    this.response = res;
  }


  public String method(MyBean b)
  {
    ...
    request.setAttribute("myVar", "<h2>Any text or object can go here</h2>");
    request.setAttribute("object", b);
    return "WEB-INF/templates/method.vm";
  }
}

```

Once the controller is done, you can redirect the flow to the next view. Whatever you put in the request gets to the view (templates). Voya uses Apache Velocity as the template engine. So, in method.vm you'll have:

```
<html>
  ...
  <body>
    <h1>Hello $object.name</h1>
    $myVar
  </body>
</html>
```

This is it! Any variables you pass to the request can also be accessed from the template.

