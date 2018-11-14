package es.anew.element.tramitador.activiti;

import es.anew.element.tramitador.activiti.service.ActivitiService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TramitadorActivitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TramitadorActivitiApplication.class, args);
	}

}
