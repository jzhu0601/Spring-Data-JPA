package com.guitar.db;

import static org.junit.Assert.*;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.guitar.db.model.Location;
import com.guitar.db.repository.LocationJpaRepository;

@ContextConfiguration(locations={"classpath:com/guitar/db/applicationTests-context.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class LocationPersistenceTests {
//	@Autowired
//	private LocationRepository locationRepository;
	
	@Autowired
	private LocationJpaRepository locationJpaRository;

	@PersistenceContext
	private EntityManager entityManager;
	
	@Test
	public void testJpaFind(){
		List<Location> locations = locationJpaRository.findAll();
		assertNotNull(locations);
	}
	
	@Test
	public void testJpaAnd(){
		List<Location> locations = locationJpaRository.findByStateAndCountry("Utah", "United States");
		assertEquals("Utah", locations.get(0).getState());
		
	}
	
	@Test
	public void testJpaOr(){
		List<Location> locations = locationJpaRository.findByStateOrCountry("Utah", "Utah");
		assertEquals("Utah", locations.get(0).getState());
	}
	
	@Test
	public void testJpaNot(){
		List<Location> locations = locationJpaRository.findByStateNot("Utah");
		assertNotSame("Utah", locations.get(0).getState());
	}

	@Test
	@Transactional
	public void testSaveAndGetAndDelete() throws Exception {
		Location location = new Location();
		location.setCountry("Canada");
		location.setState("British Columbia");
		//location = locationRepository.create(location);
		location = locationJpaRository.saveAndFlush(location);
		
		// clear the persistence context so we don't return the previously cached location object
		// this is a test only thing and normally doesn't need to be done in prod code
		entityManager.clear();

		Location otherLocation = locationJpaRository.findOne(location.getId());
		assertEquals("Canada", otherLocation.getCountry());
		assertEquals("British Columbia", otherLocation.getState());
		
		//delete BC location now
		locationJpaRository.delete(otherLocation);
	}

	@Test
	public void testFindWithLike() throws Exception {
		List<Location> locs = locationJpaRository.findByStateLike("New%");
		assertEquals(4, locs.size());
	}

	@Test
	@Transactional  //note this is needed because we will get a lazy load exception unless we are in a tx
	public void testFindWithChildren() throws Exception {
		Location arizona = locationJpaRository.findOne(3L);
		assertEquals("United States", arizona.getCountry());
		assertEquals("Arizona", arizona.getState());
		
		assertEquals(1, arizona.getManufacturers().size());
		
		assertEquals("Fender Musical Instruments Corporation", arizona.getManufacturers().get(0).getName());
	}
}
