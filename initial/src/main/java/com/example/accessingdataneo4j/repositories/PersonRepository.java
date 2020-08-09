package com.example.accessingdataneo4j.repositories;

import com.example.accessingdataneo4j.Entities.Person;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;


public interface PersonRepository extends CrudRepository<Person, Long> {

    Person findByName(String name);

    @Query("MATCH (j:Person) WHERE j.name = $Myname RETURN j")
    Collection<Person> getMyNode(@Param("Myname") String Myname);
}