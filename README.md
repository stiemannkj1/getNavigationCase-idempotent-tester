# getNavigationCase-idempotent-tester (reproducer for [Mojarra #4279](https://github.com/javaserverfaces/mojarra/issues/4279))

## Steps to reproduce:

1. Clone the [getNavigationCase-idempotent-tester](https://github.com/stiemannkj1/getNavigationCase-idempotent-tester) project:

    ```
    git clone https://github.com/stiemannkj1/getNavigationCase-idempotent-tester.git
    ```

2. Build the project:

    ```
    cd getNavigationCase-idempotent-tester && mvn clean package
	```

3. Deploy the project to Tomcat:

    ```
    cp target/*.war $TOMCAT_HOME/webapps/getNavigationCase-idempotent-tester.war
    ```

4. Navigate to the deployed webapp at [http://localhost:8080/getNavigationCase-idempotent-tester/](http://localhost:8080/getNavigationCase-idempotent-tester/).
5. Click the *flow1* button to navigate to flow1.
6. Click the *callFlow2* button to navigate to flow2 as a nested flow (inside flow1).
7. Click the *Call NavigationHandler.getNavigationCase(facesContext, "returnFromFlow2", "returnFromFlow2") then returnFromFlow2* to execute the action `#{flow2Bean.returnFromFlow2}` which calls `NavigationHandler.getNavigationCase(facesContext, "returnFromFlow2", "returnFromFlow2")` and returns returnFromFlow2 in order to exit flow2.

If the bug still exists, the browser will fail to navigate to **`returnFromFlow2.xhtml`** and the following messages will appear in the logs indicating that flow1 was exited prematurely:

```
Flow2Bean @PreDestroy Called.
Flow1Bean @PreDestroy Called.
```

The following stacktrace will also appear in the logs:

```
13-Sep-2017 17:37:55.305 SEVERE [http-nio-8181-exec-62] com.sun.faces.application.view.FaceletViewHandlingStrategy.handleRenderException Error Rendering View[/flow1/returnFromFlow2.xhtml]
 javax.el.ELException: /flow1/returnFromFlow2.xhtml: WELD-001303: No active contexts for scope type javax.faces.flow.FlowScoped
        at com.sun.faces.facelets.compiler.TextInstruction.write(TextInstruction.java:90)
        at com.sun.faces.facelets.compiler.UIInstructions.encodeBegin(UIInstructions.java:82)
        at com.sun.faces.facelets.compiler.UILeaf.encodeAll(UILeaf.java:183)
        at javax.faces.render.Renderer.encodeChildren(Renderer.java:176)
        at javax.faces.component.UIComponentBase.encodeChildren(UIComponentBase.java:890)
        at javax.faces.component.UIComponent.encodeAll(UIComponent.java:1856)
        at javax.faces.component.UIComponent.encodeAll(UIComponent.java:1859)
        at javax.faces.component.UIComponent.encodeAll(UIComponent.java:1859)
        at com.sun.faces.application.view.FaceletViewHandlingStrategy.renderView(FaceletViewHandlingStrategy.java:458)
        at com.sun.faces.application.view.MultiViewHandler.renderView(MultiViewHandler.java:134)
        at javax.faces.application.ViewHandlerWrapper.renderView(ViewHandlerWrapper.java:337)
        at com.sun.faces.lifecycle.RenderResponsePhase.execute(RenderResponsePhase.java:120)
        at com.sun.faces.lifecycle.Phase.doPhase(Phase.java:101)
        at com.sun.faces.lifecycle.LifecycleImpl.render(LifecycleImpl.java:219)
        at javax.faces.webapp.FacesServlet.service(FacesServlet.java:659)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:292)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:207)
        at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52)
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:240)
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:207)
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:212)
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:106)
        at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:502)
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:141)
        at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:79)
        at org.apache.catalina.valves.AbstractAccessLogValve.invoke(AbstractAccessLogValve.java:616)
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:88)
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:522)
        at org.apache.coyote.http11.AbstractHttp11Processor.process(AbstractHttp11Processor.java:1095)
        at org.apache.coyote.AbstractProtocol$AbstractConnectionHandler.process(AbstractProtocol.java:672)
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1500)
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.run(NioEndpoint.java:1456)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
        at java.lang.Thread.run(Thread.java:748)
Caused by: org.jboss.weld.context.ContextNotActiveException: WELD-001303: No active contexts for scope type javax.faces.flow.FlowScoped
        at org.jboss.weld.manager.BeanManagerImpl.getContext(BeanManagerImpl.java:689)
        at org.jboss.weld.bean.ContextualInstanceStrategy$DefaultContextualInstanceStrategy.get(ContextualInstanceStrategy.java:95)
        at org.jboss.weld.bean.ContextualInstance.get(ContextualInstance.java:50)
        at org.jboss.weld.manager.BeanManagerImpl.getReference(BeanManagerImpl.java:742)
        at org.jboss.weld.el.AbstractWeldELResolver.lookup(AbstractWeldELResolver.java:107)
        at org.jboss.weld.el.AbstractWeldELResolver.getValue(AbstractWeldELResolver.java:90)
        at org.jboss.weld.environment.servlet.util.ForwardingELResolver.getValue(ForwardingELResolver.java:49)
        at javax.el.CompositeELResolver.getValue(CompositeELResolver.java:66)
        at com.sun.faces.el.DemuxCompositeELResolver._getValue(DemuxCompositeELResolver.java:176)
        at com.sun.faces.el.DemuxCompositeELResolver.getValue(DemuxCompositeELResolver.java:203)
        at org.apache.el.parser.AstIdentifier.getValue(AstIdentifier.java:80)
        at org.apache.el.parser.AstValue.getValue(AstValue.java:137)
        at org.apache.el.ValueExpressionImpl.getValue(ValueExpressionImpl.java:184)
        at org.jboss.weld.el.WeldValueExpression.getValue(WeldValueExpression.java:50)
        at com.sun.faces.facelets.el.ELText$ELTextVariable.writeText(ELText.java:238)
        at com.sun.faces.facelets.compiler.TextInstruction.write(TextInstruction.java:85)
        ... 35 more
```

If the bug is fixed, the browser will navigate to **`returnFromFlow2.xhtml`** and the following message will appear in the logs indicating that only flow2 was exited:

```
Flow2Bean @PreDestroy Called.
```

To see the correct behavior, simply repeat steps 4-6 and click the *returnFromFlow2* button. MyFaces also handles the navigation correctly. You can include MyFaces in place of Mojarra by performing step 2 and including the `myfaces` profile: `mvn clean package -P myfaces`.
