package com.epam.blog.appliacation;


import com.epam.blog.configurations.ApplicationConfiguration;
import com.epam.blog.dao.UserDAO;
import com.epam.blog.resources.AlbumsResource;
import com.epam.blog.resources.AllUsersResource;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;



public class BlogApplication extends Application<ApplicationConfiguration> {



    public static void main(String[] args) throws Exception {
        new BlogApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> myConfigurationBootstrap) {
        myConfigurationBootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    }

    @Override
    public void run(ApplicationConfiguration applicationConfiguration, Environment environment) throws Exception {


        MongoClient mongoClient = new MongoClient(applicationConfiguration.getMongoUri(), applicationConfiguration.getMongoPort());
        DB db = mongoClient.getDB(applicationConfiguration.getMongoDbName());
        UserDAO userDAO = new UserDAO(db);
        environment.jersey().setUrlPattern("/api/*");

        environment.jersey().register(new AllUsersResource(userDAO));
        environment.jersey().register(new AlbumsResource(userDAO));
    }
}
