package com.imanage.workflow.client;


import com.netflix.conductor.client.exceptions.*;
import com.netflix.conductor.client.http.*;
import com.netflix.conductor.client.task.WorkflowTaskCoordinator;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.*;
import com.netflix.conductor.common.metadata.workflow.*;

import java.util.*;

public class ClientMain
{
    public static void main(String[] args){
        System.out.println("Starting.");
        ClientMain main = new ClientMain();
        main.init();
    }


    private void init(){
        TaskClient taskClient = new TaskClient();
        String rootURI = "http://conductor-server.service.imanagecloud.com:8080/api/";

        taskClient.setRootURI(rootURI);  

        initializeWorkflows(rootURI);

        int threadCount = 10;			//number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

        Worker worker = new SimpleWorker("hello_world");

        //Create WorkflowTaskCoordinator
        WorkflowTaskCoordinator.Builder builder = new WorkflowTaskCoordinator.Builder();
        WorkflowTaskCoordinator coordinator = builder.withWorkers(worker).withThreadCount(threadCount).withTaskClient(taskClient).build();

        //Start for polling and execution of the tasks
        coordinator.init();
    }



    private void waitForServer(MetadataClient client){
        try{
            client.getTaskDef("access_test");
        }
        catch(ConductorClientException cce){
            if(cce.getStatus() == 404 || cce.getStatus() == 500){  //Status is 0 when service is not reachable.
                return;
            }else{
                logAndWait(cce);
                waitForServer(client);
            }

        }
        catch(Exception e){
            logAndWait(e);
            waitForServer(client);
        }

    }

    private void logAndWait(Exception ex){
        System.out.println("Unable to connect to server.  Will retry.");
        ex.printStackTrace();
        try{
            Thread.sleep(5000);
        }catch(InterruptedException ie){}

    }


    private WorkflowDef getWorkflow(MetadataClient metadataClient, String name, int version){
        try{
            return metadataClient.getWorkflowDef(name, version);
        }catch(ConductorClientException cce){
           return null;
        }
    }


    private void initializeWorkflows(String rootURI){

        MetadataClient metadataClient = new MetadataClient();
        metadataClient.setRootURI(rootURI);

        waitForServer(metadataClient);


        WorkflowDef retrievedWorkflow = getWorkflow(metadataClient,"hello_world", 2);
        if(retrievedWorkflow != null){
            return;
        }

        TaskDef helloWorldTask = new TaskDef("hello_world", "poc task", 5, 300);
        helloWorldTask.setRetryLogic(TaskDef.RetryLogic.FIXED);
        helloWorldTask.setRetryDelaySeconds(10);
        helloWorldTask.setResponseTimeoutSeconds(180);
        helloWorldTask.setTimeoutPolicy(TaskDef.TimeoutPolicy.TIME_OUT_WF);
        List<TaskDef> tasks = new LinkedList<>();
        tasks.add(helloWorldTask);
        metadataClient.registerTaskDefs(tasks);

        WorkflowTask helloWorldFlowTask = new WorkflowTask();
        helloWorldFlowTask.setName("hello_world");
        helloWorldFlowTask.setTaskReferenceName("parameterized_hello_world");
        Map<String, Object> inputParams = new HashMap<>();
        inputParams.put("who", "${workflow.input.who}");
        helloWorldFlowTask.setInputParameters(inputParams);
        helloWorldFlowTask.setType("SIMPLE");

        List<WorkflowTask> workflowTasks = new LinkedList<>();
        workflowTasks.add(helloWorldFlowTask);

        WorkflowDef helloWorldFlowDef = new WorkflowDef();
        helloWorldFlowDef.setName("hello_world_workflow");
        helloWorldFlowDef.setDescription("Say hello -- with param.");
        helloWorldFlowDef.setVersion(2);
        helloWorldFlowDef.setSchemaVersion(2);

        List<String> lst = new ArrayList<>();
        lst.add("who");
        helloWorldFlowDef.setInputParameters(lst);
        helloWorldFlowDef.setTasks(workflowTasks);
        metadataClient.registerWorkflowDef(helloWorldFlowDef);
      }

}
