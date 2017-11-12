package io.github.lucasduete.upSafe.controllers;

import io.github.lucasduete.upSafe.dao.ArquivoDao;
import io.github.lucasduete.upSafe.infraSecurity.FilterDetect;
import io.github.lucasduete.upSafe.infraSecurity.Security;
import io.github.lucasduete.upSafe.models.Arquivo;

import javax.validation.constraints.Null;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.sql.SQLException;

@Path("arquivo")
public class FileController {

    @POST
    @Security
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("uploadArquivo/")
    public Response upload(Arquivo file, @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        int idUsuario = Integer.parseInt(FilterDetect.getToken(requestContext));

        ArquivoDao arquivoDao = new ArquivoDao();

        try {
            arquivoDao.salvar(file);

            return Response.status(Response.Status.OK).build();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

}
