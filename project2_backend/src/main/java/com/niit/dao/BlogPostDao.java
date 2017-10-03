package com.niit.dao;

import com.niit.model.BlogPost;
import com.niit.model.BlogComment;
import com.niit.model.BlogPicture;

import java.util.List;


public interface BlogPostDao 
{
	void saveBlogPost(BlogPost blogPost);
	
	List<BlogPost> getBlogPosts(int approved);

	BlogPost getBlogPostById(int id);
	
	void updateBlogPost(BlogPost blogPost);
	
	void addBlogComment(BlogComment blogComment);
	
	List<BlogComment> getAllBlogComments(int blogPostId);

	public void save(BlogPicture blogPicture);
	public BlogPicture getBlogPic(int blogid);
}
