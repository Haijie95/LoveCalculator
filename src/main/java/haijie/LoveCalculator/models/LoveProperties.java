package haijie.LoveCalculator.models;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class LoveProperties implements Serializable {
    private String fname;
    private String sname;
    private String percentage;
    private String result;
        
    //constructors
    public LoveProperties() {
        this.id = this.generateId(8);
    }

    public LoveProperties(String fname, String sname) {
        this.id = this.generateId(8);
        this.fname=fname;
        this.sname=sname;
    }

    //setter and getters
    
    public String getFname() {
        return fname;
    }
    public void setFname(String fname) {
        this.fname = fname;
    }
    public String getSname() {
        return sname;
    }
    public void setSname(String sname) {
        this.sname = sname;
    }
    public String getPercentage() {
        return percentage;
    }
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
    public static LoveProperties create(String json) throws IOException{
        LoveProperties l = new LoveProperties();
        try(InputStream is = new ByteArrayInputStream(json.getBytes())){ 
            //get the input stream
            JsonReader r = Json.createReader(is);
            //use a reader to read the stream
            JsonObject o = r.readObject();
            //create a json object
            l.setFname(o.getString("fname"));
            l.setSname(o.getString("sname"));
            l.setResult(o.getString("result"));
            l.setPercentage(o.getString("percentage"));

        }
        return l;
    }

    //redis id
    private String id;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    
    private synchronized String generateId(int numChars) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < numChars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numChars);
    }

    
    
}
