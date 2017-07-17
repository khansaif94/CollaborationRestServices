package com.niit.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.dao.UserDao;
import com.niit.models.User;



@RestController
public class UserRestService {
	
	private static Logger log = LoggerFactory.getLogger(UserRestService.class);
	
	@Autowired
	private User user;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	HttpSession session;
	
	@PostMapping("/saveUser")
	public User saveUser(@RequestBody User newuser)
	{
		log.debug("Calling saveUser method ");
		
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			
			userDao.saveUser(newuser);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newuser.setErrorCode("200");
			newuser.setErrorMessage("Thank you For register in user.");
		
		log.debug("Endig of the  createuser method ");
		return newuser;		
}

	@PostMapping("/validate")
	public ResponseEntity<User> validateCredentials(@RequestBody User user)
	{
		log.debug("->->->->calling method authenticate"+user.getEmail()+user.getPassword());
		user = userDao.isValidate(user.getEmail(), user.getPassword());
		log.debug("user"+user);
		
		if (user == null) {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
			log.debug("->->->->InValid Credentials");

		 }
		 else
		 {
			 
					user.setErrorCode("200");
					user.setErrorMessage("You have successfully logged in.");
				user.setIsOnline('Y');
					log.debug("->->->->Valid Credentials");
				
					session.setAttribute("loggedInUserID", user.getUserid());
					session.setAttribute("loggedInUserRole", user.getUserrole());
				
					log.debug("You are loggin with the role : " +session.getAttribute("loggedInUserRole"));

				userDao.setOnline(user.getUserid());
		 }
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
}
