package quicksplit.servlet;

import java.util.Locale;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import quicksplit.dao.DaoFactory;

@WebServlet("/players")
public class PlayerServlet
    extends BaseServlet
{

    @Override
    protected void processRequest( final HttpServletRequest request, 
                                   final HttpServletResponse response )
        throws Exception
    {
        /*
         * Populate request attributes
         */
        request.setAttribute( "Players", 
            DaoFactory.getInstance().getPlayerDao().list() );
        
        /*
         * Write the response headers
         */
        response.setContentType("text/html;charset=UTF-8");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        
        /*
         * Obtain the TemplateEngine instance.
         */
        
        final ServletContextTemplateResolver templateResolver = 
            new ServletContextTemplateResolver( request.getServletContext() );
        // XHTML is the default mode, but we set it anyway for better understanding of code
        templateResolver.setTemplateMode(TemplateMode.HTML); 
        // This will convert "home" to "/WEB-INF/templates/home.html"
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        // Template cache TTL=1h. If not set, entries would be cached until expelled by LRU
        templateResolver.setCacheTTLMs(3600000L);
        
        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        
        final WebContext context = 
            new WebContext( request, response, request.getServletContext(), Locale.ENGLISH );
        templateEngine.process( "players", context, response.getWriter() );
    }

}
