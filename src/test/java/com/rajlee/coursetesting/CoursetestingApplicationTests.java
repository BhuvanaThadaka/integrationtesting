package com.rajlee.coursetesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.xdevapi.JsonString;
import com.rajlee.coursetesting.controller.CourseController;
import com.rajlee.coursetesting.entity.Course;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CoursetestingApplicationTests {

	@Autowired
	private MockMvc mockMvc;  //we can also use testresttemplate

	//asking the test container to give us a mysql image/instance in a container and define as
	// static to use in other code
    // should not insert any test data to db
	
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest");

	// In application starting I want to connect to container and at ending I want to destroy the container
	// in order to avoid corrupted values
	// we have to tell test case to use this container rather than actual db by using dynamicpropertysource annotation

	@DynamicPropertySource
	static void configureProperties(DynamicPropertyRegistry registry){
		// define data source related properties by fetching from mysqlcontainer
		registry.add("spring.datasource.url",mySQLContainer::getJdbcUrl);
		registry.add("spring.datasource.username",mySQLContainer::getUsername);
		registry.add("spring.datasource.password",mySQLContainer::getPassword);

	}

	// Before starting all the applications just start the container
	@BeforeAll //executes before all the methods
	static void beforeAll(){
		mySQLContainer.start();
	}

	@AfterAll //to close container (executes after all the methods)
	static void afterAll(){
		mySQLContainer.stop();
	}

	@Before("")
	public void setup() {
		this.mockMvc = MockMvcBuilders
				.standaloneSetup(CourseController.class)
				.build();
	}

	//as our entry point of our application is controller so if we trigger any request to controller
	// it will talk to service and that will talk to repository
	// In integration test the flow should go to each layer

	@Test
	public void addNewCourseTest() throws Exception{
		//build request body
		Course course=Course.builder()
				.name("test-course")
				.price(100)
				.duration("0 month")
				.build();
		//call controller endpoints
		mockMvc.perform(MockMvcRequestBuilders
						.post("/courses")
						.contentType("application/json")
						.content(asJsonString(course))  //body is in json format
						.accept("application/json"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());  //should have some id associate to it
	}

	@Test
	public void getAllCourseTest() throws Exception{
		mockMvc.perform(MockMvcRequestBuilders
						.get("/courses")
						.accept("application/json")
						.contentType("application/json"))
				.andExpect(status().isOk())  //i.e.,200
				.andExpect(MockMvcResultMatchers.jsonPath("$.*").exists())  //should not be an empty array(value shoul be present)
				.andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1));  //id should be present
	}

	private String asJsonString(Object object) {
		try{
			return new ObjectMapper().writeValueAsString(object);
		}catch (JsonProcessingException e){
			e.printStackTrace();
		}
		return null;
	}


}
