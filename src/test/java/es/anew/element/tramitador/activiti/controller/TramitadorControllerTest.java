package es.anew.element.tramitador.activiti.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.anew.element.tramitador.activiti.TramitadorActivitiApplication;
import es.anew.element.tramitador.activiti.model.json.Process;
import es.anew.element.tramitador.activiti.model.json.StringResponse;
import es.anew.element.tramitador.activiti.model.json.TaskForm;
import es.anew.element.tramitador.activiti.service.ActivitiService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes= TramitadorActivitiApplication.class)
@WebMvcTest(value = TramitadorController.class, secure = false)
@EnableAutoConfiguration
public class TramitadorControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    private ActivitiService activitiService;


    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    @Test
    public void startProcessInstance() throws Exception {

        /** Creación de mock **/
        ProcessInstance pi = mock(ProcessInstance.class);
        when(pi.getDeploymentId()).thenReturn("1234");
        when(pi.getName()).thenReturn("simpleProcess");

        Process process = new Process();
        process.setId("simpleProcess");
        process.setInitiator("kermit");

        when(activitiService.startProcess(Mockito.any(Process.class))).thenReturn(pi);

        /** Creación de la petición**/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/processes/").accept(
                MediaType.APPLICATION_JSON).content(mapToJson(process)).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        /** Respuesta **/
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("1234"));

    }

    @Test
    public void getTasks() throws Exception{

        /** Creación de mock **/
        Task activitiTask = mock(Task.class);
        when(activitiTask.getName()).thenReturn("Task 1");
        when(activitiTask.getId()).thenReturn("123456");
        when(activitiTask.getFormKey()).thenReturn("qdfsdgfd-dsfsdfsd");
        List<Task> taskList = new ArrayList<>();
        taskList.add(activitiTask);

        when(activitiService.getTasks(Mockito.anyString())).thenReturn(taskList);

        /** Creación de la petición**/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/tasks/kermit").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        /** Respuesta **/
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("123456"));
        assertTrue(response.getContentAsString().contains("Task 1"));

    }

    @Test
    public void getForm() throws Exception{

        /** Creación de mock **/
        TaskFormData activitiForm = mock(TaskFormData.class);
        FormProperty property = mock(FormProperty.class);
        FormType formType = mock(FormType.class);

        when(formType.getName()).thenReturn("string");
        when(property.getId()).thenReturn("id1");
        when(property.getName()).thenReturn("name");
        when(property.getType()).thenReturn(formType);
        when(property.getValue()).thenReturn(null);
        List<FormProperty> properties = new ArrayList<>();
        properties.add(property);

        when(activitiForm.getFormProperties()).thenReturn(properties);
        when(activitiForm.getFormKey()).thenReturn("qdfsdgfd-dsfsdfsd");

        when(activitiService.getForm(Mockito.anyString())).thenReturn(activitiForm);

        /** Creación de la petición**/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/forms/12345").accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        /** Respuesta **/
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("id1"));
        assertTrue(response.getContentAsString().contains("name"));


    }

    @Test
    public void completeTask() throws Exception {

        /** Creación de mock **/
        StringResponse strResponse = new StringResponse("UserTask with id 12345 has been completed!");

        doNothing().when(activitiService).completeTask(Mockito.anyString(),Mockito.any(TaskForm.class));

        /** Creación de la petición**/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/completetask/12345").accept(
                MediaType.APPLICATION_JSON).content(mapToJson(strResponse)).contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        /** Respuesta **/
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("12345"));


    }

    @Test
    public void deployProcess() throws Exception {

        /** Creación de mock **/
        MockMultipartFile bpmnFile = new MockMultipartFile("file", "simpleProcess.bpmn20.xml", "text/xml", "<xml></xml>".getBytes());

        Deployment deploy = mock(Deployment.class);
        when(deploy.getId()).thenReturn("simpleProcess");

        when(activitiService.deployProcess(Mockito.anyString(),Mockito.any(InputStream.class))).thenReturn(deploy);

        /** Creación de la petición**/
        RequestBuilder requestBuilder = MockMvcRequestBuilders.fileUpload(
                "/api/deploys/").file(bpmnFile);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        /** Respuesta **/
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

    }
}