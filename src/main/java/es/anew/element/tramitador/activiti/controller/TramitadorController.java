package es.anew.element.tramitador.activiti.controller;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import es.anew.element.tramitador.activiti.model.json.*;
import es.anew.element.tramitador.activiti.model.json.Process;
import es.anew.element.tramitador.activiti.service.MappingActivitiDataService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import es.anew.element.tramitador.activiti.service.ActivitiService;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping({"/api"})
public class TramitadorController {

	@Autowired
	private ActivitiService activitiService;

	@Autowired
	private MappingActivitiDataService mappingService;

	@PostMapping("/processes")
	@ResponseStatus(HttpStatus.CREATED)
	public Process startProcessInstance(@RequestBody Process process) {

		ProcessInstance pi = activitiService.startProcess(process);
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



	@PostMapping("/completetask/{id}")
	public StringResponse completeTask(@PathVariable String id, @RequestBody TaskForm form) {
		activitiService.completeTask(id, form);
		return new StringResponse("UserTask with id " + id + " has been completed!");
	}

	@PostMapping("/deploys")
	@ResponseStatus(HttpStatus.CREATED)
	public Deploy deployProcess(@RequestParam("file") MultipartFile file) {

		String nombreProceso = getProcessName(file);
		try {
			Deployment deploy = activitiService.deployProcess(nombreProceso,file.getInputStream());
			return mappingService.mapDeploy(deploy);
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	private String getProcessName(MultipartFile file) {
		String nombre = file.getOriginalFilename();
		StringTokenizer tokenizer = new StringTokenizer(nombre,".");
		return tokenizer.nextToken();
	}

}