package com.imanage.workflow.client;

import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;

public class TaskGenerator
{
    public static void main(String[] args){
        TaskGenerator taskGenerator = new TaskGenerator();
        taskGenerator.run();
    }

    public void run(){
        System.out.println("Running task generator.");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String rootURI = "http://conductor-server.service.imanagecloud.com:8080/api/workflow/hello_world_workflow";

        for(int i=0; i < names.length; i++){

            System.out.println("Will add task for: " + names[i]);
            String body = getBody(names[i]);

            try{
                HttpPost post = new HttpPost(rootURI);
                post.setHeader("Accept", "*/*");
                post.setHeader("Content-type", "application/json");


                post.setEntity(new StringEntity(body));
                CloseableHttpResponse response = httpClient.execute(post);
                System.out.println("Response: " + response.toString());
                response.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    private String getBody(String name){
        return "{\"who\":" + "\"" + name + "\"}";
    }


    public static String[] names = {"Joe Biden", "Seth Moulton", "Eric Swalwell", "Tim Ryan", "Beto O'Rouke", "Bernie Sanders", "Amy Klobuchar",  "Elizabeth Warrenls" +
        "", "Cory Booker", "Pete Buttigieg", "Kamala Harris", "Kirsten Gillibrand"};
}
