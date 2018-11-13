package es.anew.element.tramitador.activiti.model.json;

import java.util.ArrayList;
import java.util.List;

public class TaskForm {

    private List<FormField> fields = new ArrayList<>();
    private String key;

    public String getKey() {
        return key;
    }

    public List<FormField> getFields() {
        return fields;
    }

    public void addField(FormField field) {
        fields.add(field);

    }

    public void setFields(List<FormField> fields) {
        this.fields = fields;
    }

    public void setKey(String formKey) {
        this.key = formKey;
    }
}
