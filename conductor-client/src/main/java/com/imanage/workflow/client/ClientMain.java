package com.imanage.workflow.client;


import com.netflix.conductor.client.http.*;
import com.netflix.conductor.client.task.WorkflowTaskCoordinator;
import com.netflix.conductor.client.worker.Worker;
import com.netflix.conductor.common.metadata.tasks.*;
import com.netflix.conductor.common.metadata.workflow.*;

import java.util.*;

public class ClientMain
{
    public static void main(String[] args){
        System.out.println("...and it has begun");
        ClientMain main = new ClientMain();
        main.init();
    }


    private void init(){
        TaskClient taskClient = new TaskClient();
        String rootURI = "http://localhost:8080/api/";
        taskClient.setRootURI(rootURI);		//Point this to the server API

        initializeWorkflows(rootURI);

        int threadCount = 10;			//number of threads used to execute workers.  To avoid starvation, should be same or more than number of workers

        Worker worker = new SimpleWorker("hello_world");

        //Create WorkflowTaskCoordinator
        WorkflowTaskCoordinator.Builder builder = new WorkflowTaskCoordinator.Builder();
        WorkflowTaskCoordinator coordinator = builder.withWorkers(worker).withThreadCount(threadCount).withTaskClient(taskClient).build();

        //Start for polling and execution of the tasks
        coordinator.init();
    }


    private void initializeWorkflows(String rootURI){

        MetadataClient metadataClient = new MetadataClient();
        metadataClient.setRootURI(rootURI);


        TaskDef helloWorldTask = new TaskDef("hello_world", "poc task", 5, 30);
        List<TaskDef> tasks = new LinkedList<>();
        tasks.add(helloWorldTask);
        metadataClient.registerTaskDefs(tasks);

        WorkflowTask helloWorldFlow = new WorkflowTask();
        List<WorkflowTask> workflowTasks = new LinkedList<>();
        workflowTasks.add(helloWorldFlow);

        WorkflowDef helloWorldFlowDef = new WorkflowDef();
        helloWorldFlowDef.setTasks(workflowTasks);
        metadataClient.registerWorkflowDef(helloWorldFlowDef);
    }

}
