package io.github.lucasduete.upSafe.controllers;

import io.github.lucasduete.upSafe.dao.ArquivoDao;
import io.github.lucasduete.upSafe.infraSecurity.FilterDetect;
import io.github.lucasduete.upSafe.infraSecurity.Security;
import io.github.lucasduete.upSafe.models.Arquivo;
import io.github.lucasduete.upSafe.resources.FileManagement;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("arquivo")
public class FileController {

    @POST
    @Security
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("uploadArquivo/")
    public Response upload(Arquivo file, @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        if (!(file.getNome().contains("png") || file.getNome().contains("jpeg") || file.getNome().contains("jpg")
                || file.getNome().contains("pdf") || file.getNome().contains("mp4") || file.getNome().contains("mp3")))
            return Response.status(415).build();

        int idUsuario = Integer.parseInt(FilterDetect.getToken(requestContext));
        file.setIdUsuario(idUsuario);

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

    @GET
    @Security
    @Produces({ "image/jpg", "image/png", MediaType.APPLICATION_OCTET_STREAM, "application/pdf", "audio/mp3" })
    @Path("downloadArquivo/{idArquivo}")
    public Response download(@PathParam("idArquivo") int idArquivo,
                             @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        ArquivoDao arquivoDao = new ArquivoDao();

        try {
            Arquivo file = arquivoDao.getArquivo(idArquivo);

            if(file.getIdUsuario() != Integer.parseInt(FilterDetect.getToken(requestContext)))
                return Response.status(Response.Status.UNAUTHORIZED).build();


            if(file.getNome().contains("mp3"))
                return Response.ok(FileManagement.decodeFile(file.getContent()), "audio/mp3").build();

            if(file.getNome().contains("pdf"))
                return Response.ok(FileManagement.decodeFile(file.getContent()), "application/pdf").build();

            if(file.getNome().contains("jpg"))
                return Response.ok(FileManagement.decodeFile(file.getContent()), "image/jpg").build();

            if(file.getNome().contains("png"))
                return Response.ok(FileManagement.decodeFile(file.getContent()), "image/png").build();

            if(file.getNome().contains("mp4"))
                return Response.ok(FileManagement.decodeFile(file.getContent()), MediaType.APPLICATION_OCTET_STREAM).build();

            return Response.status(415).build();

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GET
    @Security
    @Produces(MediaType.APPLICATION_JSON)
    @Path("listarArquivos/")
    public Response listaArquivos(@Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        ArquivoDao arquivoDao = new ArquivoDao();

        try {
            ArrayList<Arquivo> arquivos = arquivoDao.listar(Integer.parseInt(FilterDetect.getToken(requestContext)));

            return Response.ok(arquivos).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DELETE
    @Security
    @Path("removerArquivo/{idArquivo}")
    public Response removerArquivo(@PathParam("idArquivo") int idArquivo,
                                   @Context ContainerRequestContext requestContext) {

        if(!FilterDetect.checkToken(requestContext))
            return Response.status(Response.Status.UNAUTHORIZED).build();

        ArquivoDao arquivoDao = new ArquivoDao();

        try {

            if (arquivoDao.getArquivo(idArquivo).getIdUsuario() !=
                    Integer.parseInt(FilterDetect.getToken(requestContext)))
                return Response.status(Response.Status.UNAUTHORIZED).build();

            arquivoDao.remover(idArquivo);

            return Response.status(Response.Status.OK).build();
        } catch (SQLException e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

    }
}
