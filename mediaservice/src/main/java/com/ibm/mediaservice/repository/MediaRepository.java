package com.ibm.mediaservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ibm.mediaservice.entity.Media;


@Repository
public interface MediaRepository extends CrudRepository<Media, Long>{
	
	public Media findById(long id);
	public List<Media> findByUserid(String userid);
	public Optional<Media> findByUseridAndId(String userid,long id);
}

