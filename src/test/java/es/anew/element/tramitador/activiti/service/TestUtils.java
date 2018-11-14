package es.anew.element.tramitador.activiti.service;

import es.anew.element.tramitador.activiti.model.json.FormField;
import es.anew.element.tramitador.activiti.model.json.TaskForm;

import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static TaskForm createTaskForm() {
        TaskForm form = new TaskForm();
        FormField field = new FormField();
        field.setName("customerName");
        field.setValue("Héctor");
        List<FormField> fields = new ArrayList<>();
        fields.add(field);
        form.setFields(fields);

        return form;
    }

}
