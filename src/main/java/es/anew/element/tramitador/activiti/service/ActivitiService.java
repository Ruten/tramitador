package es.anew.element.tramitador.activiti.service;

import java.util.Date;
import java.util.List;

import es.anew.element.tramitador.activiti.model.json.FormField;
import es.anew.element.tramitador.activiti.model.json.TaskForm;
import org.activiti.engine.*;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.anew.element.tramitador.activiti.model.Person;
import es.anew.element.tramitador.activiti.repo.PersonRepository;

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

	@Autowired
	private PersonRepository personRepository;

	public ProcessInstance startProcess(String id) {
		try {
			identityService.setAuthenticatedUserId("kermit");
			return runtimeService.startProcessInstanceByKey(id);
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
	
	public void completeTask(String taskId) {
		taskService.complete(taskId);
	}

	public void createPersons() {
		if (personRepository.findAll().size() == 0) {

			personRepository.save(new Person("John", new Date()));
			personRepository.save(new Person("David", new Date()));
			personRepository.save(new Person("Katherin", new Date()));
		}
	}

	private String processInfo() {
		List<Task> tasks = taskService.createTaskQuery().orderByTaskCreateTime().asc().list();
		
		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("Number of process definitions : "
				+ repositoryService.createProcessDefinitionQuery().count() + "--> Tasks >> ");

		for (Task task : tasks) {
			stringBuilder
					.append(task + " | Assignee: " + task.getAssignee() + " | Description: " + task.getDescription());
		}

		return stringBuilder.toString();
	}
}