package com.ibm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.model.Follow;
import com.ibm.repository.FollowRepository;

@Service
public class FollowServiceImpl implements FollowService{

	@Autowired
	FollowRepository followRepository;

	@Override
	public Follow createFollower(Follow follow) {
		return followRepository.save(follow);
	}

	@Override
	public List<Follow> getFollowerByMyid(int myid) {
		return followRepository.getByMyid(myid);
	}

	@Override
	public List<Follow> getFollowingByUserid(int userid) {
		return followRepository.getByUserid(userid);
	}

	@Override
	public void deleteFollowingByMyidAndUserid(int myid, int userid) {
		followRepository.deleteByMyidAndUserid(myid, userid);		
	}
}
