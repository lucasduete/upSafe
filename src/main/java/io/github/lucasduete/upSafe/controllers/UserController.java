package io.github.lucasduete.upSafe.controllers;

import io.github.lucasduete.upSafe.dao.UsuarioDao;
import io.github.lucasduete.upSafe.infraSecurity.FilterDetect;
import io.github.lucasduete.upSafe.infraSecurity.Security;
import io.github.lucasduete.upSafe.models.Usuario;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("usuario")
public class UserController {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("cadastrarUsuario/")
    public Response cadastrarUsuario(Usuario user) {

        UsuarioDao usuarioDao = new UsuarioDao();

        try {
            usuarioDao.salvar(user);

            return Response.status(Response.Status.OK).build();
        } catch (SQLException | NullPointerException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PUT
    @Security
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("atualizarUsuario/")
    public Response atualizarUsuario(Usuario user, @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        UsuarioDao usuarioDao = new UsuarioDao();

        user.setId(Integer.parseInt(FilterDetect.getToken(requestContext)));

        try {

            usuarioDao.atualizar(user);

            return Response.status(Response.Status.OK).build();
        } catch (SQLException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Security
    @Path("removerUsuario/{idUsuario}")
    public Response removerUsuario(@PathParam("idUsuario") int idUsuario,
                                   @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        UsuarioDao usuarioDao = new UsuarioDao();

        try {
            usuarioDao.remover(idUsuario);

            return Response.status(Response.Status.OK).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checarUsuario/{idUsuario}")
    public Response getUsuario(@PathParam("idUsuario") int idUsuario,
                               @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        UsuarioDao usuarioDao = new UsuarioDao();

        try {
            Usuario user = usuarioDao.getUsuario(idUsuario);

            return Response.ok(user).build();
        } catch (SQLException ex) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException ex) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

}

