package quicksplit.core;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class WebAppInitializer
    extends AbstractAnnotationConfigDispatcherServletInitializer
{

    @Override
    protected Class<?>[] getRootConfigClasses()
    {
        System.out.println( "getRootConfigClasses()" );
        return new Class<?>[] { AppConfig.class };
    }

    @Override
    protected Class<?>[] getServletConfigClasses()
    {
        System.out.println( "getServletConfigClasses()" );
        return new Class<?>[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings()
    {
        System.out.println( "getServletMappings()" );
        return new String[] { "/" };
    }

}
