package servlets.utils;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.stream.Collectors;


//this class manage the request and the response
public class ServletUtils {
    public Gson gson = new Gson();
    public ResponseJson responseJson = new ResponseJson();
    public BufferedReader reader;
    public String lines;

    public ServletUtils(HttpServletRequest req) throws IOException {
        reader = req.getReader();
        lines = reader.lines().collect(Collectors.joining()); //getting the request lines
    }

    public Object gsonFromJson(Class<?> classType){
        Object obj = gson.fromJson(lines,classType); // creating object from lines
        return obj;
    }

    public String gsonToJson(Object obj) {
       return gson.toJson(obj);
    }

    public void writeJsonResponse(Object message){
        if (message.getClass() == String.class)
            writeStringJsonResponse((String)message);
        else
            responseJson.message = gsonToJson(message);
    }

    private void writeStringJsonResponse(String message){
        responseJson.message = message;
    }

    public void writeJsonResponse(String status,String message){
        responseJson.status = status;
        responseJson.message = message;
    }

    public String createOutResponse(){
        return gsonToJson(responseJson);
    }

}
