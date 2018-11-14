package es.anew.element.tramitador.activiti.utils;


import es.anew.element.tramitador.activiti.model.json.*;
import es.anew.element.tramitador.activiti.model.json.Process;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.ArrayList;
import java.util.List;

public class ActivitiDataMapper {

    public static List<UserTask> mapUserTasks(List<Task> activitiTasks) {
        List<UserTask> userTasks = new ArrayList<>();
        for (Task task: activitiTasks) {
            UserTask userTask = new UserTask();
            userTask.setName(task.getName());
            userTask.setId(task.getId());
            userTask.setForm(task.getFormKey());
            userTasks.add(userTask);
        }
        return userTasks;
    }


    public static Process mapProcess(ProcessInstance activitiProcess) {
        Process process = new Process();
        process.setId(activitiProcess.getId());
        process.setName(activitiProcess.getProcessDefinitionKey());
        process.setDeploymentId(activitiProcess.getDeploymentId());
        return process;
    }

    public static TaskForm mapTaskForm(TaskFormData formData) {
        TaskForm form = new TaskForm();
        form.setKey(formData.getFormKey());

        List<FormProperty> propiedades = formData.getFormProperties();

        for (FormProperty property: propiedades) {
            FormField field = new FormField();
            field.setId(property.getId());
            field.setName(property.getName());
            field.setType(property.getType().getName());
            field.setValue(property.getValue());
            form.addField(field);
        }
        return form;
    }


    public static Deploy mapDeploy(Deployment activitiDeployment) {
        Deploy deploy = new Deploy();
        deploy.setId(activitiDeployment.getId());
        return deploy;
    }
}
