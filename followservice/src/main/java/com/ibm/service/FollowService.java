package com.ibm.service;

import java.util.List;

import com.ibm.model.Follow;

public interface FollowService {

	Follow createFollower(Follow follow);

	List<Follow> getFollowerByMyid(int myid);

	List<Follow> getFollowingByUserid(int userid);

	void deleteFollowingByMyidAndUserid(int myid, int userid);

}
