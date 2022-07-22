package com.ifaSeriousGame.services;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

@WebFilter(filterName="XssFilter",urlPatterns={"/*"})
public class XssFilter implements Filter {

    public XssFilter() {
        // TODO Auto-generated constructor stub
    }

	public void destroy() {
		// TODO Auto-generated method stub
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		chain.doFilter(new XssRequestWrapper( (HttpServletRequest) request), response);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

	 private static class XssRequestWrapper extends HttpServletRequestWrapper{
		 private static final Pattern [] XSS_PATTERNS = {
	                Pattern.compile( "<.*?>" ),
	                Pattern.compile( "&.*?;" ),
	                Pattern.compile( "%[0-9a-fA-F]*" )
	        };

		public XssRequestWrapper(HttpServletRequest request) {
			super(request);
			// TODO Auto-generated constructor stub
		}
		
		 @Override
	        public String getParameter( String parameter ) {
	            return removeTags( super.getParameter(parameter) );
	        }
		 
		 private String removeTags( String value ) {
	            
	            if ( value != null ) {
	                // On retire le code ASCII 0, au cas ou
	                value = value.replaceAll( "\0", "" );

	                // Supprime l'ensemble de tags et des entit�s existants
	                for ( Pattern pattern : XSS_PATTERNS ) {
	                    value = pattern.matcher( value ).replaceAll( "" );
	                }
	                
	                // Au cas ou les caract�res < et > ne seraient pas en nombres pairs
	                value = value.replaceAll( "<", "" );
	                value = value.replaceAll( ">", "" );
	            }
	            
	            return value;
	        }
		 
	 }
}
