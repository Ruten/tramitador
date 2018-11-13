package es.anew.element.tramitador.activiti.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import es.anew.element.tramitador.activiti.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

	Person findByName(String name);

}
