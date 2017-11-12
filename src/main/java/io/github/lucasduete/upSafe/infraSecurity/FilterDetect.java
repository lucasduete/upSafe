package io.github.lucasduete.upSafe.infraSecurity;

import java.io.IOException;

import java.security.Principal;
import java.util.logging.Logger;

import javax.annotation.Priority;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import io.github.lucasduete.upSafe.controllers.LoginController;
import io.jsonwebtoken.Claims;

@Security
@Provider
@Priority(Priorities.AUTHENTICATION)
public class FilterDetect implements ContainerRequestFilter{

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
		
			if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
				throw new NotAuthorizedException("Necessário Informar Authorization Header Para Acessar Este Recurso.");
			
			String token = authorizationHeader.substring("Bearer".length()).trim();

			Claims claims = new LoginController().validaToken(token);

			if(claims==null)
				throw new IOException("Token inválido");

			modificarRequestContext(requestContext, claims.getIssuer());
		}

	private void modificarRequestContext(ContainerRequestContext requestContext,String indentificador){

		final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
		
		requestContext.setSecurityContext(new SecurityContext() {
			 @Override
			  public Principal getUserPrincipal() {
			    return new Principal() {	
			    	
			      @Override
			      public String getName() {
			        return indentificador;
			      }
			    };
			  }

			  @Override
			  public boolean isUserInRole(String role) {
			    return true;
			  }

			  @Override
			  public boolean isSecure() {
			    return currentSecurityContext.isSecure();
			  }

			  @Override
			  public String getAuthenticationScheme() {
			    return "Bearer";
			  }

			});
	}

	public static boolean checkToken(ContainerRequestContext requestContext) {

		try {
			new FilterDetect().filter(requestContext);

			Integer.parseInt(requestContext.getSecurityContext().getUserPrincipal().getName());

			return true;
		} catch (NumberFormatException | IOException Ex) {
			Ex.printStackTrace();
			Logger.getLogger("LoginController-log").info("Erro:" + Ex.getStackTrace());
			return false;
		}
	}

	public static String getToken(ContainerRequestContext requestContext) {
		return requestContext
				.getSecurityContext()
					.getUserPrincipal()
						.getName();
	}
}
