package es.anew.element.tramitador.activiti.controller;

import java.util.List;

import es.anew.element.tramitador.activiti.model.json.FormField;
import es.anew.element.tramitador.activiti.model.json.Process;
import es.anew.element.tramitador.activiti.model.json.TaskForm;
import es.anew.element.tramitador.activiti.model.json.UserTask;
import es.anew.element.tramitador.activiti.service.MappingActivitiDataService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import es.anew.element.tramitador.activiti.service.ActivitiService;


@RestController
@RequestMapping({"/api"})
public class TramitadorController {

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private MappingActivitiDataService mappingService;

	@GetMapping("/processes/{id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Process startProcessInstance(@PathVariable String id) {

		ProcessInstance pi = activitiService.startProcess(id);
		return mappingService.mapProcess(pi);

	}

	@GetMapping("/tasks/{assignee}")
	@ResponseStatus(HttpStatus.OK)
	public List<UserTask> getTasks(@PathVariable String assignee) {

		List<Task> tasks = activitiService.getTasks(assignee);
		return mappingService.mapUserTasks(tasks);
	}

	@GetMapping("/forms/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	public TaskForm getForm(@PathVariable String taskId) {

		TaskFormData formData = activitiService.getForm(taskId);
		return mappingService.mapTaskForm(formData);
	}



	@RequestMapping(value = "/completetask")
	public String completeTask(@RequestParam String id) {
		activitiService.completeTask(id);
		return "UserTask with id " + id + " has been completed!";
	}

	@PostMapping("/deploys/")
	@ResponseStatus(HttpStatus.CREATED)
	public Process deployProcess() {

		Process process = new Process();
		process.setId("123456");

		return process;

	}

}