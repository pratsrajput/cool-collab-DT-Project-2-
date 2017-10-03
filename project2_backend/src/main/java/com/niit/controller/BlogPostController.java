package com.niit.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.niit.dao.BlogPostDao;
import com.niit.dao.UserDao;
import com.niit.model.BlogComment;
import com.niit.model.BlogPicture;
import com.niit.model.BlogPost;
import com.niit.model.Error;
import com.niit.model.ProfilePicture;
import com.niit.model.User;

@Controller
public class BlogPostController 
{
	@Autowired
	private BlogPostDao blogPostDao;
		@Autowired
	private UserDao userDao;
		@RequestMapping(value="/saveblogpost",method=RequestMethod.POST)
		public ResponseEntity<?> saveBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
			if(session.getAttribute("username")==null){
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);//401 - 2nd call back func will be executed
			}
			String username=(String)session.getAttribute("username");
			User user=userDao.getUserByUsername(username);
			blogPost.setPostedOn(new Date());
			blogPost.setPostedBy(user);
			try{
			blogPostDao.saveBlogPost(blogPost);
			int blogid=blogPost.getId();
			session.setAttribute("blogId", blogid);
		
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);//200 - 1st call back function will be called
			}
			catch(Exception e)
			{System.out.println("call");
			
				Error error=new Error(6,"Unable to insert blog post details " + e.getMessage());
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);//500 - 2nd call back func will be executed
			}
		}

		
		
		@RequestMapping(value="/addBlogPicture",method=RequestMethod.POST)
		public ResponseEntity<?> addBlogPicture(@RequestParam CommonsMultipartFile image,HttpSession session){

				System.out.println("In addblogpic" + session.getAttribute("blogId"));
			
			//String username=(String)session.getAttribute("username");
			Integer picname= (Integer) session.getAttribute("blogId");
		
				byte[] arr;
				if(!image.isEmpty())
				{
					try{
				arr=image.getBytes();
				
				String path="F:\\New folder\\Backend\\project2_frontend\\WebContent\\pic"+picname+".jpg";
				File f=new File(path);
				BufferedOutputStream bf=new BufferedOutputStream(new FileOutputStream(f));
				bf.write(arr);
				bf.close();
				System.out.println("Image Uploaded");
			
			}
			catch(Exception e)
			{System.out.println("catch");
			}
				Error error=new Error(6,"Unable to upload profile picture");
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
				return new ResponseEntity<Void>(HttpStatus.OK);	
			}
			
		
		@RequestMapping(value="/getblogimage/{blogid}",method=RequestMethod.GET)
		public @ResponseBody byte[] getImage(@PathVariable int blogid,HttpSession session)
		{
			
				BlogPicture blogPicture=blogPostDao.getBlogPic(blogid);
				if(blogPicture==null)
					return null;
				System.out.println(blogPicture.getImage());
				return blogPicture.getImage();
			
		}
		
		
		@RequestMapping(value="/getblogposts/{approved}")
		public ResponseEntity<?> getBlogPosts(@PathVariable int approved,HttpSession session)
		{
			if(session.getAttribute("username")==null)
			{
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
				
			}
			List<BlogPost> blogPosts=blogPostDao.getBlogPosts(approved);
			return new ResponseEntity<List<BlogPost>>(blogPosts,HttpStatus.OK);
		}
		
		@RequestMapping(value="/getblogpostbyid/{id}",method=RequestMethod.GET)
		public ResponseEntity<?> getBlogPostById(@PathVariable int id,HttpSession session){
			if(session.getAttribute("username")==null){
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
				
			}
			
			BlogPost blogPost=blogPostDao.getBlogPostById(id);
			return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
		}
		
		@RequestMapping(value="/updateblogpost",method=RequestMethod.PUT)
		public ResponseEntity<?> updateBlogPost(@RequestBody BlogPost blogPost,HttpSession session){
			if(session.getAttribute("username")==null){
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
			
			try{
				blogPostDao.updateBlogPost(blogPost);
				return new ResponseEntity<BlogPost>(blogPost,HttpStatus.OK);
			}
			catch(Exception e)
			{
				Error error=new Error(6,"Unable to update blog post"+e.getMessage());
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		@RequestMapping(value="/addblogcomment",method=RequestMethod.POST)
		public ResponseEntity<?>addBlogComment(@RequestBody BlogComment blogComment,HttpSession session)
		{
			if(session.getAttribute("username")==null)
			{
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);
			}
			String username=(String)session.getAttribute("username");
			User user=userDao.getUserByUsername(username);
			blogComment.setCommentedBy(user);//set the value for foreign key 'username' in blogcomment table
			blogComment.setCommentedOn(new Date());//set the value for commentedOn
			try{
			blogPostDao.addBlogComment(blogComment);
			return new ResponseEntity<BlogComment>(blogComment,HttpStatus.OK);
			
			}
			catch(Exception e)
			{System.out.print(e);
				Error error=new Error(7,"Unable to add blogcomment "+e.getMessage());
				return new ResponseEntity<Error>(error,HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		
		@RequestMapping(value="/getblogcomments/{blogPostId}")
		public ResponseEntity<?> getBlogComments(@PathVariable int blogPostId,HttpSession session)
		{
			if(session.getAttribute("username")==null)
			{
				Error error=new Error(5,"UnAuthorized User");
				return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED);//401-2nd call back function
			}
			List<BlogComment> blogComments=blogPostDao.getAllBlogComments(blogPostId);
			return new ResponseEntity<List<BlogComment>>(blogComments,HttpStatus.OK);
		}	
				
}
		
