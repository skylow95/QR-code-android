package com.example.bogdan.qrcodeapp.server.comms;

public class Configuration {

    private String chatServerIp = "192.168.113.83";
    private String chatServerPort = "8080";

    private static Configuration ourInstance = new Configuration();

    public static Configuration getInstance() {
        return ourInstance;
    }

    private Configuration() {
    }

    public String getServerIp() {
        return chatServerIp;
    }

    public String getServerPort() {
        return chatServerPort;
    }
}
