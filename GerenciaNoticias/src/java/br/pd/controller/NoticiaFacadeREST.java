/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.pd.controller;

import br.pd.Noticia;
import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author sergio
 */
@Stateless
@Path("br.pd.noticia")
public class NoticiaFacadeREST extends AbstractFacade<Noticia> {
    @PersistenceContext(unitName = "GerenciaNoticiasPU")
    private EntityManager em;
    
    @Resource
    private UserTransaction ut;

    public NoticiaFacadeREST() {
        super(Noticia.class);
    }

    @POST
    @Path("{noticia}")
    @Consumes({"application/xml", "application/json"})
    public Response create(
            @PathParam("autor") String autor,
            @PathParam("titulo") String titulo,
            @PathParam("dataDaNoticia") String dataDaNoticia,
            @PathParam("conteudo") String conteudo){
        Noticia noticia = new Noticia(Long.MIN_VALUE, autor, titulo, dataDaNoticia, conteudo);
        try {
            ut.begin();
            em.persist(noticia);
            em.flush();
            ut.commit();
            
        } catch (Exception e) {
            System.out.println("Erro");
            return Response.status(Response.Status.NOT_FOUND).build();   
        }
        //super.create(entity);
           System.out.println(noticia.toString());
           return Response.ok(noticia, MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("{id}")
    @Consumes({"application/xml", "application/json"})
    public Response edit(@PathParam("id") Long id,
                        @PathParam("titulo") String titulo) {
        Noticia noticia = em.find(Noticia.class, id);
        try {
            ut.begin();
            noticia.setTitulo(titulo);
            
        } catch (Exception e) {
            System.out.println("Erro");
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
        //super.edit(entity);
        System.out.println(noticia.toString());
        return Response.ok(noticia, MediaType.APPLICATION_JSON).build();
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Long id) {
        
        Noticia noticia = em.find(Noticia.class, id);
        try {
            ut.begin();
            em.remove(noticia);
            ut.commit();
            
        } catch (Exception e) {
            System.out.println("Erro");
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
        //super.edit(entity);
        System.out.println(noticia.toString());
        return Response.ok(noticia, MediaType.APPLICATION_XML).build();
        //super.remove(super.find(id));
    }

    @GET
    @Path("/noticia/{id}")
    @Produces({"application/xml", "application/json"})
    public Response find(@QueryParam("id") Long id) {
        Noticia noticia;// = em.find(Noticia.class, id);
        
        try {
            ut.begin();
            noticia = em.find(Noticia.class, id);
            ut.commit();
            
        } catch (Exception e) {
            System.out.println("Erro");
            return Response.status(Response.Status.NOT_FOUND).build(); 
        }
        //super.edit(entity);
        System.out.println(noticia.toString());
        return Response.ok(noticia, MediaType.APPLICATION_JSON).build();
        
        //return super.find(id);
    }

   /* @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Noticia> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Noticia> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }*/

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
