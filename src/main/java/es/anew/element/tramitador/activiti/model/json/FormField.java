package es.anew.element.tramitador.activiti.model.json;

import org.activiti.engine.form.FormType;

public class FormField {
    private String id;
    private String name;
    private String type;
    private String value;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
