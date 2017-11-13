package io.github.lucasduete.upSafe.controllers;

import io.github.lucasduete.upSafe.dao.UsuarioDao;
import io.github.lucasduete.upSafe.models.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

@Path("login")
public class LoginController {

    private final String SECRETKEY = "FSM#STUD3NT-V01C3@K3Y/CR1PT";

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("loginUsuario/{email}/{senha}/")
    public Response loginAluno(@PathParam("email") String login, @PathParam("senha") String senha) {

        try {
            UsuarioDao userdao = new UsuarioDao();

            Usuario user = userdao.login(login, senha);

            String token = gerarToken(String.valueOf(user.getId()), 1);

            user.setPassword(token);

            System.gc();
            return Response.ok(user).build();

        } catch(SQLException ex) {

            if(ex.getMessage().contains("Credenciais Inválidas"))
                return Response.status(Response.Status.UNAUTHORIZED).build();
            else
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();

        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    private String gerarToken(String login, int limiteDias) {
        //Gera algoritmo de criptografia em SHA512
        try {
            SignatureAlgorithm algorithm = SignatureAlgorithm.HS512;

            Date agora = new Date();

            Calendar expira = Calendar.getInstance();
            expira.add(Calendar.DAY_OF_MONTH, limiteDias);

            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRETKEY);

            SecretKeySpec key = new SecretKeySpec(apiKeySecretBytes, algorithm.getJcaName());

            JwtBuilder construtor = Jwts.builder()
                    .setIssuedAt(agora)
                    .setIssuer(login)
                    .signWith(algorithm, key)
                    .setExpiration(expira.getTime());
            return construtor.compact();


        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Claims validaToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(DatatypeConverter.parseBase64Binary(SECRETKEY))
                    .parseClaimsJws(token).getBody();
            return claims;
        } catch(Exception ex) {
            throw ex;
        }
    }

}