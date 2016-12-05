package com.epam.blog.resources;

import com.epam.blog.dao.UserDAO;
import com.epam.blog.model.AuthorizationResult;
import com.epam.blog.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/authorize")
@Produces(MediaType.APPLICATION_JSON)
public class AllUsersResource {

    private UserDAO userDAO;

    public AllUsersResource(UserDAO  userDAO){
        this.userDAO = userDAO;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorize(@FormParam("token") String token,@CookieParam("uid") String userID){
        System.out.println(userID+" "+token);
        if(userID==null){
            User userByToken = userDAO.getUserByToken(token);
            if(userByToken!=null){
                return  Response.ok(new AuthorizationResult(true,userByToken)).cookie(new NewCookie("uid",token,"/",null, "session", 1073741823, false)).build();
            }else{
                User newUser = new User();
                Client client = ClientBuilder.newClient();
                WebTarget target = client.target("https://api.vk.com/method/");
                Invocation invocation = target.path("users.get").queryParam("v","5.44").queryParam("access_token",token).request().buildGet();
                Response response = invocation.invoke();
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = objectMapper.readTree(response.readEntity(String.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JsonNode responseJsonObject = jsonNode.get("response");
                JsonNode user = responseJsonObject.get(0);
                String first_name = user.get("first_name").asText();
                String last_name = user.get("last_name").asText();
                newUser.setVkId(user.get("id").asInt());
                newUser.setFirstName(first_name);
                newUser.setLastName(last_name);
                newUser.setToken(token);
                userDAO.create(newUser);
                return  Response.ok(new AuthorizationResult(true,newUser)).cookie(new NewCookie("uid",token,"/",null, "session", 1073741823, false)).build();
            }
        }else{
            User userByToken = userDAO.getUserByToken(userID);
            return  Response.ok(new AuthorizationResult(true,userByToken)).build();
        }
    }
}
