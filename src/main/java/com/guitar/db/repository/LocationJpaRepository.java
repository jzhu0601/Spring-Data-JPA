package com.guitar.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guitar.db.model.Location;

@Repository
public interface LocationJpaRepository extends JpaRepository<Location, Long> {
	
	//this findBy+class Obj in the Entity class, Like is SQL query words.
	List<Location> findByStateLike(String stateName);
	
	List<Location> findByStateOrCountry(String value, String value2);
	
	List<Location> findByStateIsOrCountryEquals(String value, String value2);
	
	List<Location> findByStateAndCountry(String state, String country);

	List<Location> findByStateNot(String value);
	
	List<Location> findByStateStartingWith(String stateName);
	
	
	
}
