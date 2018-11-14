package es.anew.element.tramitador.activiti.service;

import es.anew.element.tramitador.activiti.TramitadorActivitiApplication;
import es.anew.element.tramitador.activiti.model.json.Process;
import es.anew.element.tramitador.activiti.model.json.TaskForm;
import es.anew.element.tramitador.activiti.utils.TestUtils;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TramitadorActivitiApplication.class})
public class ActivitiServiceTest {

    @Autowired
    private ActivitiService activitiService;

    @Test
    public void startProcess() {
        ProcessInstance processInstance = createProcessInstance();
        assertNotNull(processInstance);

    }

    private ProcessInstance createProcessInstance() {
        Process process = new Process();
        process.setId("simpleProcess");
        process.setInitiator("kermit");
        process.setName("Simple Process");

        return activitiService.startProcess(process);
    }

    @Test
    public void getForm() {
        createProcessInstance();

        List<Task> kermitTasks = activitiService.getTasks("kermit");

        TaskFormData form = activitiService.getForm(kermitTasks.get(0).getId());
        assertNotNull(form);
        assertTrue(form.getFormProperties().size() > 0);

    }

    @Test
    public void getTasks() {

        createProcessInstance();

        List<Task> kermitTasks = activitiService.getTasks("kermit");
        assertTrue(kermitTasks.size() > 0);

    }

    @Test
    public void completeTask() {
        TaskForm taskForm = TestUtils.createTaskForm();

        createProcessInstance();

        List<Task> kermitTasks = activitiService.getTasks("kermit");
        String kermitTaskId = kermitTasks.get(0).getId();
        activitiService.completeTask(kermitTaskId, taskForm);

        kermitTasks = activitiService.getTasks("kermit");
        assertNotEquals(kermitTaskId,kermitTasks.get(0).getId());
    }
    @Test
    public void deployProcess() throws FileNotFoundException {
        File initialFile = new File("src/main/resources/processes/simpleProcess.bpmn20.xml");
        InputStream fileStream = new FileInputStream(initialFile);

        Deployment simpleProcess = activitiService.deployProcess("simpleProcess", fileStream);
        assertNotNull(simpleProcess);
        assertTrue(!simpleProcess.getId().isEmpty());

    }


}