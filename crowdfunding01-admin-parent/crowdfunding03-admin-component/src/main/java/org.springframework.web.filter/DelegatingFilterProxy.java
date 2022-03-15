package org.springframework.web.filter;

import java.io.IOException;
import javax.servlet.*;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.Assert;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.servlet.FrameworkServlet;

public class DelegatingFilterProxy extends GenericFilterBean {

    private String contextAttribute;

    private WebApplicationContext webApplicationContext;

    private String targetBeanName;

    private boolean targetFilterLifecycle = false;

    private volatile Filter delegate;

    private final Object delegateMonitor = new Object();

    /**
     * Create a new {@code DelegatingFilterProxy}. For traditional (pre-Servlet 3.0) use
     * in {@code web.xml}.
     * @see #setTargetBeanName(String)
     */
    public DelegatingFilterProxy() {
    }

    /**
     * Create a new {@code DelegatingFilterProxy} with the given {@link Filter} delegate.
     * Bypasses entirely the need for interacting with a Spring application context,
     * specifying the {@linkplain #setTargetBeanName target bean name}, etc.
     * <p>For use in Servlet 3.0+ environments where instance-based registration of
     * filters is supported.
     * @param delegate the {@code Filter} instance that this proxy will delegate to and
     * manage the lifecycle for (must not be {@code null}).
     * @see #doFilter(ServletRequest, ServletResponse, FilterChain)
     * @see #invokeDelegate(Filter, ServletRequest, ServletResponse, FilterChain)
     * @see #destroy()
     * @see #setEnvironment(org.springframework.core.env.Environment)
     */
    public DelegatingFilterProxy(Filter delegate) {
        Assert.notNull(delegate, "Delegate Filter must not be null");
        this.delegate = delegate;
    }

    /**
     * Create a new {@code DelegatingFilterProxy} that will retrieve the named target
     * bean from the Spring {@code WebApplicationContext} found in the {@code ServletContext}
     * (either the 'root' application context or the context named by
     * {@link #setContextAttribute}).
     * <p>For use in Servlet 3.0+ environments where instance-based registration of
     * filters is supported.
     * <p>The target bean must implement the standard Servlet Filter.
     * @param targetBeanName name of the target filter bean to look up in the Spring
     * application context (must not be {@code null}).
     * @see #findWebApplicationContext()
     * @see #setEnvironment(org.springframework.core.env.Environment)
     */
    public DelegatingFilterProxy(String targetBeanName) {
        this(targetBeanName, null);
    }

    /**
     * Create a new {@code DelegatingFilterProxy} that will retrieve the named target
     * bean from the given Spring {@code WebApplicationContext}.
     * <p>For use in Servlet 3.0+ environments where instance-based registration of
     * filters is supported.
     * <p>The target bean must implement the standard Servlet Filter interface.
     * <p>The given {@code WebApplicationContext} may or may not be refreshed when passed
     * in. If it has not, and if the context implements {@link ConfigurableApplicationContext},
     * a {@link ConfigurableApplicationContext#refresh() refresh()} will be attempted before
     * retrieving the named target bean.
     * <p>This proxy's {@code Environment} will be inherited from the given
     * {@code WebApplicationContext}.
     * @param targetBeanName name of the target filter bean in the Spring application
     * context (must not be {@code null}).
     * @param wac the application context from which the target filter will be retrieved;
     * if {@code null}, an application context will be looked up from {@code ServletContext}
     * as a fallback.
     * @see #findWebApplicationContext()
     * @see #setEnvironment(org.springframework.core.env.Environment)
     */
    public DelegatingFilterProxy(String targetBeanName, WebApplicationContext wac) {
        Assert.hasText(targetBeanName, "Target Filter bean name must not be null or empty");
        this.setTargetBeanName(targetBeanName);
        this.webApplicationContext = wac;
        if (wac != null) {
            this.setEnvironment(wac.getEnvironment());
        }
    }

    /**
     * Set the name of the ServletContext attribute which should be used to retrieve the
     * {@link WebApplicationContext} from which to load the delegate {@link Filter} bean.
     */
    public void setContextAttribute(String contextAttribute) {
        this.contextAttribute = contextAttribute;
    }

    /**
     * Return the name of the ServletContext attribute which should be used to retrieve the
     * {@link WebApplicationContext} from which to load the delegate {@link Filter} bean.
     */
    public String getContextAttribute() {
        return this.contextAttribute;
    }

    /**
     * Set the name of the target bean in the Spring application context.
     * The target bean must implement the standard Servlet Filter interface.
     * <p>By default, the {@code filter-name} as specified for the
     * DelegatingFilterProxy in {@code web.xml} will be used.
     */
    public void setTargetBeanName(String targetBeanName) {
        this.targetBeanName = targetBeanName;
    }

    /**
     * Return the name of the target bean in the Spring application context.
     */
    protected String getTargetBeanName() {
        return this.targetBeanName;
    }

    /**
     * Set whether to invoke the {@code Filter.init} and
     * {@code Filter.destroy} lifecycle methods on the target bean.
     * <p>Default is "false"; target beans usually rely on the Spring application
     * context for managing their lifecycle. Setting this flag to "true" means
     * that the servlet container will control the lifecycle of the target
     * Filter, with this proxy delegating the corresponding calls.
     */
    public void setTargetFilterLifecycle(boolean targetFilterLifecycle) {
        this.targetFilterLifecycle = targetFilterLifecycle;
    }

    /**
     * Return whether to invoke the {@code Filter.init} and
     * {@code Filter.destroy} lifecycle methods on the target bean.
     */
    protected boolean isTargetFilterLifecycle() {
        return this.targetFilterLifecycle;
    }


    @Override
    protected void initFilterBean() throws ServletException {
        synchronized (this.delegateMonitor) {
            if (this.delegate == null) {
                // If no target bean name specified, use filter name.
                if (this.targetBeanName == null) {
                    this.targetBeanName = getFilterName();
                }
                // Fetch Spring root application context and initialize the delegate early,
                // if possible. If the root application context will be started after this

//                此处将源码注释掉，即在初始化时不再去查找IOC容器中的bean（默认查找spring容器，但是其中没有我们需要的bean）
//                直接跳过初始化时查找IOC容器的环节
                // filter proxy, we'll have to resort to lazy initialization.
//                WebApplicationContext wac = findWebApplicationContext();
//                if (wac != null) {
//                    this.delegate = initDelegate(wac);
//                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lazily initialize the delegate if necessary.
        Filter delegateToUse = this.delegate;
        if (delegateToUse == null) {
            synchronized (this.delegateMonitor) {
                delegateToUse = this.delegate;
                if (delegateToUse == null) {


//                    注释原来查找IOC容器的代码，替换为自己的代码
//                    WebApplicationContext wac = findWebApplicationContext();

//                    自己重写代码：
//                   1. 获取servletContext对象
                    ServletContext sc = this.getServletContext();
//                  2. 拼接springmvc将IOC容器存入servletContext时使用的属性
                    String servletName = "DispatcherServlet"; //web.xml中DispatcherServlet的servlet名称
                    String attrName = FrameworkServlet.SERVLET_CONTEXT_PREFIX+servletName;
//                    3. 根据attrName从servletContext域中获取IOC容器对象
                    WebApplicationContext wac = (WebApplicationContext) sc.getAttribute(attrName);

                    if (wac == null) {
                        throw new IllegalStateException("No WebApplicationContext found: " +
                                "no ContextLoaderListener or DispatcherServlet registered?");
                    }
                    delegateToUse = initDelegate(wac);
                }
                this.delegate = delegateToUse;
            }
        }

        // Let the delegate perform the actual doFilter operation.
        invokeDelegate(delegateToUse, request, response, filterChain);
    }

    @Override
    public void destroy() {
        Filter delegateToUse = this.delegate;
        if (delegateToUse != null) {
            destroyDelegate(delegateToUse);
        }
    }


    /**
     * Return the {@code WebApplicationContext} passed in at construction time, if available.
     * Otherwise, attempt to retrieve a {@code WebApplicationContext} from the
     * {@code ServletContext} attribute with the {@linkplain #setContextAttribute
     * configured name} if set. Otherwise look up a {@code WebApplicationContext} under
     * the well-known "root" application context attribute. The
     * {@code WebApplicationContext} must have already been loaded and stored in the
     * {@code ServletContext} before this filter gets initialized (or invoked).
     * <p>Subclasses may override this method to provide a different
     * {@code WebApplicationContext} retrieval strategy.
     * @return the {@code WebApplicationContext} for this proxy, or {@code null} if not found
     * @see #DelegatingFilterProxy(String, WebApplicationContext)
     * @see #getContextAttribute()
     * @see WebApplicationContextUtils#getWebApplicationContext(javax.servlet.ServletContext)
     * @see WebApplicationContext#ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE
     */
    protected WebApplicationContext findWebApplicationContext() {
        if (this.webApplicationContext != null) {
            // The user has injected a context at construction time -> use it...
            if (this.webApplicationContext instanceof ConfigurableApplicationContext) {
                ConfigurableApplicationContext cac = (ConfigurableApplicationContext) this.webApplicationContext;
                if (!cac.isActive()) {
                    // The context has not yet been refreshed -> do so before returning it...
                    cac.refresh();
                }
            }
            return this.webApplicationContext;
        }
        String attrName = getContextAttribute();
        if (attrName != null) {
            return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
        }
        else {
            return WebApplicationContextUtils.findWebApplicationContext(getServletContext());
        }
    }

    /**
     * Initialize the Filter delegate, defined as bean the given Spring
     * application context.
     * <p>The default implementation fetches the bean from the application context
     * and calls the standard {@code Filter.init} method on it, passing
     * in the FilterConfig of this Filter proxy.
     * @param wac the root application context
     * @return the initialized delegate Filter
     * @throws ServletException if thrown by the Filter
     * @see #getTargetBeanName()
     * @see #isTargetFilterLifecycle()
     * @see #getFilterConfig()
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    protected Filter initDelegate(WebApplicationContext wac) throws ServletException {
        Filter delegate = wac.getBean(getTargetBeanName(), Filter.class);
        if (isTargetFilterLifecycle()) {
            delegate.init(getFilterConfig());
        }
        return delegate;
    }

    /**
     * Actually invoke the delegate Filter with the given request and response.
     * @param delegate the delegate Filter
     * @param request the current HTTP request
     * @param response the current HTTP response
     * @param filterChain the current FilterChain
     * @throws ServletException if thrown by the Filter
     * @throws IOException if thrown by the Filter
     */
    protected void invokeDelegate(
            Filter delegate, ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        delegate.doFilter(request, response, filterChain);
    }

    /**
     * Destroy the Filter delegate.
     * Default implementation simply calls {@code Filter.destroy} on it.
     * @param delegate the Filter delegate (never {@code null})
     * @see #isTargetFilterLifecycle()
     * @see javax.servlet.Filter#destroy()
     */
    protected void destroyDelegate(Filter delegate) {
        if (isTargetFilterLifecycle()) {
            delegate.destroy();
        }
    }

}
