package com.imanage.workflow.client;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.*;
import org.apache.http.impl.client.*;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskGenerator
{
    ExecutorService executorService = Executors.newFixedThreadPool(8);

    public static void main(String[] args){
        TaskGenerator taskGenerator = new TaskGenerator();
        taskGenerator.startLoad();
    }

    public void startLoad() {
        for(int cnt=0; cnt<=10_000_000; cnt++) {
            executorService.submit(this::run);
        }
    }

    public void run(){
        System.out.println("Running task generator.");
        String rootURI = "http://conductor-server.service.imanagecloud.com:8080/api/workflow/hello_world_workflow";
        try(CloseableHttpClient httpClient = HttpClients.createDefault()) {
            for(int i=0; i < names.length; i++){
                System.out.println("Will add task for: " + names[i]);
                String body = getBody(names[i]);
                    HttpPost post = new HttpPost(rootURI);
                    post.setHeader("Accept", "*/*");
                    post.setHeader("Content-type", "application/json");
                    post.setEntity(new StringEntity(body));
                    try(CloseableHttpResponse response = httpClient.execute(post);) {
                        if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                            System.out.println("Response: " + response.toString());
                        }
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getBody(String name){
        return "{\"who\":" + "\"" + name + "\"}";
    }


    public static String[] names = {"Joe Biden", "Seth Moulton", "Eric Swalwell", "Tim Ryan", "Beto O'Rouke", "Bernie Sanders", "Amy Klobuchar",  "Elizabeth Warrenls" +
        "", "Cory Booker", "Pete Buttigieg", "Kamala Harris", "Kirsten Gillibrand"};
}
