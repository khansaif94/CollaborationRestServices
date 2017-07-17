package com.niit.controller;

import java.util.Date;
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

import com.niit.dao.BlogDao;
import com.niit.models.Blog;



@RestController
public class BlogRestService {
	
private static Logger log = LoggerFactory.getLogger(BlogRestService.class);
	
	@Autowired
	private Blog blog;
	
	@Autowired
	BlogDao blogDao;
	
	@GetMapping("/viewAllBlogs")
	public ResponseEntity<List<Blog>> getAllBlogs()
	{
		List<Blog> blogList=blogDao.getAllBlog();

		//ResponseEntity:  we can send the data + HTTP status codes + error message
		return new ResponseEntity<List<Blog>>(blogList,HttpStatus.OK);
	}
	
	@PostMapping("/createblog")
	public Blog createBlog(@RequestBody Blog newBlog)
	{
		log.debug("Calling createBlog method ");
		//before creating user, check whether the id exist in the db or not
		
		blog = blogDao.get(newBlog.getId());
		if( blog ==null)
		{
			log.debug("User does not exist...trying to create new user");
			//id does not exist in the db
			
			newBlog.setDate_added(new Date());
			blogDao.saveBlog(newBlog);
			//NLP - NullPointerException
			//Whenever you call any method/variable on null object - you will get NLP
			newBlog.setErrorCode("200");
			newBlog.setErrorMessage("Thank you For register in Blog.");
		}
		else
		{
			log.debug("Please choose another id as it is existed");
			//id alredy exist in db.
			newBlog.setErrorCode("800");
			newBlog.setErrorMessage("Please choose another id as it is exist");
			
		}
		log.debug("Endig of the  createBlog method ");
		return newBlog;		
}
		
}



