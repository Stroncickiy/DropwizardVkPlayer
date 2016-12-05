package com.epam.player.resources;

        import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.CookieParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.epam.player.dao.UserDAO;
import com.epam.player.model.Album;
import com.epam.player.model.Track;
import com.epam.player.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("/albums")
@Produces(MediaType.APPLICATION_JSON)
public class AlbumsResource {

    private UserDAO userDAO;

    public AlbumsResource(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    public List<Album> getAllbumsOfFriends(@CookieParam("uid") String userID){
        List<Album> albums = new ArrayList<Album>();
        User userByToken = userDAO.getUserByToken(userID);
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://api.vk.com/method/");

        Invocation invocation = target.path("execute.getFriendsMusic")
                .queryParam("access_token",userByToken.getToken())
                .queryParam("v","5.44")
                .request()
                .buildGet();

        Response response = invocation.invoke();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        String stringOfResponse = response.readEntity(String.class);
        try {
            jsonNode = objectMapper.readTree(stringOfResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode responseNode = jsonNode.get("response");
        Iterator<JsonNode> iterator = responseNode.iterator();
        while (iterator.hasNext()){
            JsonNode album = iterator.next();
            JsonNode owner = album.get("owner");
            String ownerFirstName  = owner.get("first_name").asText();
            String ownerLastName  = owner.get("last_name").asText();
            Album albumOfFriend = new Album();
            albumOfFriend.setTitle(ownerFirstName+" "+ownerLastName);
            albumOfFriend.setArtist(String.valueOf(owner.get("id").asInt()));
            List<Track> tracks = new ArrayList<Track>();
            JsonNode tracksOfFriendJson = album.get("tracks");
            Iterator<JsonNode> tracksJsonIterator = tracksOfFriendJson.iterator();
            while (tracksJsonIterator.hasNext()){
                JsonNode jsonTrack = tracksJsonIterator.next();
                Track track = new Track();
                track.setTitle(jsonTrack.get("artist").asText()+" "+jsonTrack.get("title").asText());
                track.setUrl(jsonTrack.get("url").asText());
                tracks.add(track);
            }
            albumOfFriend.setTracks(tracks);
            albums.add(albumOfFriend);
        }
        return  albums;

    }

}
