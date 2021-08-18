package com.ibm.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.model.Follow;
import com.ibm.service.FollowService;

//@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class FollowController {
	
	@Autowired
	FollowService followService;
	
	@PostMapping("/follow/create")
	public Follow createFollower(@RequestBody Follow follow) {
    	
		return followService.createFollower(follow);
    }

	 @GetMapping("/follow/following/{myid}")
	 public List<Follow> getFollowing(@PathVariable int myid) {	 
		 return followService.getFollowerByMyid(myid);
	 }
	 
	 @GetMapping("/follow/follower/{userid}")
	 public List<Follow> getFollower(@PathVariable int userid){	 
		 return followService.getFollowingByUserid(userid);
	 }
	 
	 @DeleteMapping("/follow/delete/{myid}/{userid}")
	 public void deleteFollowing(@PathVariable int myid,@PathVariable int userid)
	 {
		 followService.deleteFollowingByMyidAndUserid(myid,userid);
	 }
}
