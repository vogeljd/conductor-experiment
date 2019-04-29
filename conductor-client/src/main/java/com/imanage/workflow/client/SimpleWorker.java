package com.imanage.workflow.client;

import com.netflix.conductor.client.worker.*;
import com.netflix.conductor.common.metadata.tasks.*;

public class SimpleWorker implements Worker
{
    private String taskDefName;

    public SimpleWorker(String taskDefName) {
        this.taskDefName = taskDefName;
    }

    @Override
    public String getTaskDefName() {
        return taskDefName;
    }

    @Override
    public TaskResult execute(Task task) {

        System.out.printf("Executing %s%n", taskDefName);

        if(taskDefName.equalsIgnoreCase("hello_world")){
            System.out.println("Hello " + task.getInputData().get("who"));
        }


        TaskResult result = new TaskResult(task);
        result.setStatus(TaskResult.Status.COMPLETED);

        //Register the output of the task
        result.getOutputData().put("outputKey1", "value");
        result.getOutputData().put("oddEven", 1);
        result.getOutputData().put("mod", 4);

        return result;
    }

}
