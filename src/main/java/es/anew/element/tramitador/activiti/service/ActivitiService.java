package es.anew.element.tramitador.activiti.service;

import es.anew.element.tramitador.activiti.model.Person;
import es.anew.element.tramitador.activiti.model.json.FormField;
import es.anew.element.tramitador.activiti.model.json.Process;
import es.anew.element.tramitador.activiti.model.json.TaskForm;
import org.activiti.engine.*;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ActivitiService {

	@Autowired
	private RepositoryService repositoryService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private TaskService taskService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private FormService formService;

	public ProcessInstance startProcess(Process process) {
		try {
			identityService.setAuthenticatedUserId(process.getInitiator());
			return runtimeService.startProcessInstanceByKey(process.getId());
		} finally {
			identityService.setAuthenticatedUserId(null);
		}

	}

	public TaskFormData getForm(String taskId) {
		return formService.getTaskFormData(taskId);
	}

	public List<Task> getTasks(String assignee) {
		return taskService.createTaskQuery().taskAssignee(assignee).list();
	}
	
	public void completeTask(String taskId, TaskForm form) {
		Map<String, Object> variables = getVariablesMap(form);
		taskService.complete(taskId, variables);
	}

	public Deployment deployProcess(String nombre, InputStream inputStream) {
		Deployment deployment = repositoryService.createDeployment().addInputStream(nombre, inputStream).deploy();
		return deployment;
	}


	private Map<String, Object> getVariablesMap(TaskForm form) {
		List<FormField> fields = form.getFields();
		Map<String,Object> variables = new HashMap<>();
		for (FormField field: fields) {
			variables.put(field.getName(),field.getValue());
		}
		return variables;
	}


}