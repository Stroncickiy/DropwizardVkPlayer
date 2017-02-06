package com.epam.player.configurations;


import io.dropwizard.Configuration;


public class ApplicationConfiguration extends Configuration {

    private String mongoUri;
    private String mongoDbName;
    private Integer mongoPort;


    public Integer getMongoPort() {
        return mongoPort;
    }

    public void setMongoPort(Integer mongoPort) {
        this.mongoPort = mongoPort;
    }

    public String getMongoUri() {
        return mongoUri;
    }

    public void setMongoUri(String mongoUri) {
        this.mongoUri = mongoUri;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }


}
