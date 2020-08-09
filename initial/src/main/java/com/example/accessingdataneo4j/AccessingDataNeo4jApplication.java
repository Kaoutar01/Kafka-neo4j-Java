package com.example.accessingdataneo4j;

import java.util.Arrays;
import java.util.List;

import com.example.accessingdataneo4j.repositories.PersonRepository;
import com.example.accessingdataneo4j.repositories.SimpleProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
 import com.example.accessingdataneo4j.Entities.Person;
import com.example.accessingdataneo4j.repositories.PersonRepository;
@SpringBootApplication
@EnableNeo4jRepositories
public class AccessingDataNeo4jApplication {
	public  SimpleProducer kafkaProducer = new SimpleProducer() ;
	private final static Logger log = LoggerFactory.getLogger(AccessingDataNeo4jApplication.class);

	public static void main(String[] args) throws Exception {
		SpringApplication.run(AccessingDataNeo4jApplication.class, args);
	}

	@Bean
	CommandLineRunner demo(PersonRepository personRepository) {
		return args -> {

			personRepository.deleteAll();

			Person greg = new Person("Kaoutar");
			Person roy = new Person("Roy");
			Person craig = new Person("Craig");

			List<Person> team = Arrays.asList(greg, roy, craig);

			log.info("Before linking up with Neo4j...");

			team.stream().forEach(person -> log.info("\t" + person.toString()));

			personRepository.save(greg);
			personRepository.save(roy);
			personRepository.save(craig);

			greg = personRepository.findByName(greg.getName());
			greg.worksWith(roy);
			greg.worksWith(craig);
			personRepository.save(greg);

			roy = personRepository.findByName(roy.getName());
			roy.worksWith(craig);
			// We already know that roy works with greg
			personRepository.save(roy);

			// We already know craig works with roy and greg
			System.out.println(" Mus node"+ personRepository.getMyNode("Kaoutar").toString());
			log.info("Lookup each person by name...");
			team.stream().forEach(person -> log.info(
					"\t" + personRepository.findByName(person.getName()).toString()));

			kafkaProducer.active();

		};
	}

}