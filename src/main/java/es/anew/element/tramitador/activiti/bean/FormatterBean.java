package es.anew.element.tramitador.activiti.bean;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
public class FormatterBean implements JavaDelegate {


    @Override
    public void execute(DelegateExecution execution) throws Exception {
        String potentialProfit = (String)execution.getVariable("potentialProfit");

        potentialProfit += " €";

        execution.setVariable("potentialProfit", potentialProfit);
    }
}
