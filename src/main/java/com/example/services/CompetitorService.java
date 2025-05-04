/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.services;

import com.example.PersistenceManager;
import com.example.models.Competitor;
import com.example.models.CompetitorDTO;
import com.example.models.Producto;
import com.example.models.Vehicle;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.json.simple.JSONObject;

/**
 *
 * @author Mauricio
 */
@Path("/competitors")
@Produces(MediaType.APPLICATION_JSON)
public class CompetitorService {

    @PersistenceContext(unitName = "mongoPU")
    EntityManager entityManager;

    @PostConstruct
    public void init() {
        try {
            entityManager = PersistenceManager.getInstance().getEntityManagerFactory().createEntityManager();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Path("/getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {

        Query q = entityManager.createQuery("select u from Competitor u order by u.surname ASC");
        List<Competitor> competitors = q.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(competitors).build();
    }

    @Path("/add")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCompetitor(CompetitorDTO competitor) {

        Competitor c = new Competitor();
        JSONObject rta = new JSONObject();
        c.setAddress(competitor.getAddress());
        c.setAge(competitor.getAge());
        c.setCellphone(competitor.getCellphone());
        c.setCity(competitor.getCity());
        c.setCountry(competitor.getCountry());
        c.setName(competitor.getName());
        c.setSurname(competitor.getSurname());
        c.setTelephone(competitor.getTelephone());
        c.setVehicle(competitor.getVehicle());
        c.setProducto(competitor.getProducto());

        try {
            entityManager.getTransaction().begin();
            entityManager.persist(c);
            entityManager.getTransaction().commit();
            entityManager.refresh(c);
            rta.put("competitor_id", c.getId());
        } catch (Throwable t) {
            t.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            c = null;
        } finally {
        	entityManager.clear();
        	entityManager.close();
        }
        return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(rta.toJSONString()).build();
    }
    
    @GET
    @Path("/getCompsByName/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompetitorsByName(@PathParam("name") String name) {
        TypedQuery<Competitor> query = (TypedQuery<Competitor>) entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name = :name");
        List<Competitor> competitors = query.setParameter("name", name).getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin",
                "*").entity(competitors).build();
    }
    
    @GET
    @Path("/getVehicles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getVehicles(){
        //Query q = entityManager.createNamedQuery("SELECT c.vehicle from Competitor c");
        //TypedQuery<Vehicle> q = (TypedQuery<Vehicle>) entityManager.createQuery("SELECT c.vehicle FROM Competitor c");
        //List<Vehicle> vehicl  es = q.getResultList();
        //return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vehicles).build();
        
        List<Competitor> competitors = entityManager.createQuery("SELECT c FROM Competitor c").getResultList();
        List<Vehicle> vehicles = new ArrayList();
        for (Competitor c : competitors) {
            Vehicle v = c.getVehicle();
            if (v != null) {
                vehicles.add(v);
            }
        }
        
        return Response.ok(vehicles)
                   .header("Access-Control-Allow-Origin", "*")
                   .build();
        
    }
    
    @GET
    @Path("/getProducts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts(){
        //Query q = entityManager.createNamedQuery("SELECT c.vehicle from Competitor c");
        //TypedQuery<Vehicle> q = (TypedQuery<Vehicle>) entityManager.createQuery("SELECT c.vehicle FROM Competitor c");
        //List<Vehicle> vehicl  es = q.getResultList();
        //return Response.status(200).header("Access-Control-Allow-Origin", "*").entity(vehicles).build();
        
        List<Competitor> competitors = entityManager.createQuery("SELECT c FROM Competitor c").getResultList();
        List<Producto> productos = new ArrayList();
        for (Competitor c : competitors) {
            Producto p = c.getProducto();
            if (p != null) {
                productos.add(p);
            }
        }
        
        return Response.ok(productos)
                   .header("Access-Control-Allow-Origin", "*")
                   .build();
        
    }
    
    @GET
    @Path("/getCompsByPatternName/{startPattern}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompetitorsByPattern(@PathParam("startPattern") String startPattern) {
        TypedQuery<Competitor> query = (TypedQuery<Competitor>) entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name LIKE :startPattern");
        List<Competitor> competitors = query.setParameter("startPattern", startPattern + "%").getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin",
                "*").entity(competitors).build();
    }
    
    @GET
    @Path("/getCompsByPatternNameA")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompetitorsByPatternA() {
        TypedQuery<Competitor> query = (TypedQuery<Competitor>) entityManager.createQuery("SELECT c FROM Competitor c WHERE c.name LIKE 'A%'");
        List<Competitor> competitors = query.getResultList();
        return Response.status(200).header("Access-Control-Allow-Origin",
                "*").entity(competitors).build();
    }
    
    
    @OPTIONS
    public Response cors(@javax.ws.rs.core.Context HttpHeaders requestHeaders) {
        return Response.status(200).header("Access-Control-Allow-Origin", "*").header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS").header("Access-Control-Allow-Headers", "AUTHORIZATION, content-type, accept").build();
    }

}
