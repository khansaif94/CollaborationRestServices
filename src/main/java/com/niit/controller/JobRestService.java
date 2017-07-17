package com.niit.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.JobDao;
import com.niit.models.Job;


@RestController
public class JobRestService {

	private static Logger log = LoggerFactory.getLogger(JobRestService.class);
	
	@Autowired
	private Job job;
	
	@Autowired
	JobDao jobDao;
	
	@GetMapping("/viewAllJobs")
	public ResponseEntity<List<Job>> getAllJobs()
	{
		List<Job> jobList=jobDao.getAllJob();

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<Job>>(jobList,HttpStatus.OK);
	}
	
	@PostMapping("/createjob")
	public Job createJob(@RequestBody Job newJob)
	{
		log.debug("Calling createJob method ");
		//before creating user, check whether the id exist in the db or not
		
		job = jobDao.getJobById(newJob.getJobid());
		if( job ==null)
		{
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			jobDao.saveJob(newJob);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newJob.setErrorCode("200");
			newJob.setErrorMessage("Thank you For register in Blog.");
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id alredy exist in db.
			newJob.setErrorCode("800");
			newJob.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Endig of the  createJob method ");
		return newJob;		
}		
}



