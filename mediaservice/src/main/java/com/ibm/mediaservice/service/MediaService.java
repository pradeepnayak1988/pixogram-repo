package com.ibm.mediaservice.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ibm.mediaservice.entity.Media;
import com.ibm.mediaservice.repository.MediaRepository;



@Service
public class MediaService {

	@Autowired
	private MediaRepository mediaRepository;
	
	public void saveFile(Media media) {
		mediaRepository.save(media);	
	}

	public  Optional<Media> getMediaByUserIdAndMediaId(String userid ,long mediaId) {
		return mediaRepository.findByUseridAndId(userid, mediaId);
	}
	
	public  List<Media> getMediaListByUserId(String userid) {
		return mediaRepository.findByUserid(userid);
	}
	
	public  Optional<Media> getMediaById(Long mediaId) {
		return mediaRepository.findById(mediaId);
	}
}

