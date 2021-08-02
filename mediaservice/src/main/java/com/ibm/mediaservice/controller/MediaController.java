package com.ibm.mediaservice.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.mediaservice.dto.MediaDTO;
import com.ibm.mediaservice.dto.MediaRequest;
import com.ibm.mediaservice.entity.Media;
import com.ibm.mediaservice.models.AuthenticationRequest;
import com.ibm.mediaservice.models.AuthenticationResponse;
import com.ibm.mediaservice.service.MediaService;
import com.ibm.mediaservice.service.UserServiceImpl;
import com.ibm.mediaservice.util.JwtUtil;
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping({"/authenticate"})

public class MediaController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserServiceImpl userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;

	//test end point for jwt
	@GetMapping(value = "/test")
	public String test() {	return "Authentication test successful!"; }
	
	@PostMapping
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
		try {
			authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
			);
		} catch(BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}

	
	@PostMapping(value = "/media/singlefileupload/user/{userid}")
	public ResponseEntity<String> singlemediaupload(@PathVariable String userid,MediaDTO mediaDTO, HttpServletRequest request) {

		log.info("media details: " + mediaDTO);
		ResponseEntity<String> response = null ;
		try {
			response = saveuploadfile(mediaDTO, request,userid);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;
	}
	
	@PostMapping(value = "/media/multifileupload/user/{userid}")
	public ResponseEntity<String> multiuploadFiles(@PathVariable String userid,@ModelAttribute MediaRequest mediaResquest, HttpServletRequest request) {
		log.info("media details: ");
		// Add your processing logic here
		ResponseEntity<String> response = null ;
		try {
			for (MediaDTO mediadto : mediaResquest.getMediaList()) {
				response = saveuploadfile(mediadto, request, userid);
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	private ResponseEntity<String> saveuploadfile(MediaDTO mediaDTO, HttpServletRequest request, String userId) throws Exception {
		String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		MultipartFile file = mediaDTO.getFile();
		String fileName = file.getOriginalFilename();
		String filePath = Paths.get(uploadDirectory, fileName).toString();
		log.info("FileName: " + mediaDTO.getFile().getOriginalFilename());

		Date createDate = new Date();

		try {
			File dir = new File(uploadDirectory);
			if (!dir.exists()) {
				log.info("Folder Created");
				dir.mkdirs();
			}
			// Save the file locally
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
			stream.write(file.getBytes());
			stream.close();
		} catch (Exception e) {
			log.info("in catch");
			e.printStackTrace();
		}

		byte[] imageData = file.getBytes();
		log.info("uploadDirectory:: " + uploadDirectory);

		Media media = new Media();
		media.setTags(Arrays.asList(mediaDTO.getTags()));
		media.setEffects(Arrays.asList(mediaDTO.getEffect()));
		media.setUserid(userId);
		media.setFilename(fileName);
		media.setMediaurl(filePath);
		media.setType(file.getContentType());
		media.setImage(imageData);
		media.setDescription(mediaDTO.getDesc());
		media.setUpload_date(createDate);
		mediaService.saveFile(media);
		log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
		return new ResponseEntity<String>("Product Saved With File - " + fileName, HttpStatus.OK);
	}
	@GetMapping(value = "/media/user/{userid}")
	public ResponseEntity<List<Media>> getMediaListByUser(@PathVariable String userid, HttpServletRequest request) {

		List<Media> response = null ;
		try {
			response = mediaService.getMediaListByUserId(userid);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<List<Media>>(response, HttpStatus.OK);
	}

	@GetMapping(value = "/search/media/{mediaid}")
	public ResponseEntity<Optional<Media>> getMediaByMediaId(@PathVariable long mediaid, HttpServletRequest request) {

		Optional<Media> response = null ;
		try {
			response = mediaService.getMediaById(mediaid);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Optional<Media>>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/mediadetail/{mediaid}/user/{userid}")
	public ResponseEntity<Optional<Media>> mediaDetailByUserId(@PathVariable long mediaid, @PathVariable String userid, HttpServletRequest request) {

		Optional<Media> response = null ;
		try {
			response = mediaService.getMediaByUserIdAndMediaId(userid, mediaid);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Optional<Media>>(response, HttpStatus.OK);
	}
	
	@PutMapping(value = "/updatemedia/{mediaid}/user/{userid}")
	public ResponseEntity<Optional<Media>> updateMediaByUserId(@PathVariable long mediaid, @PathVariable String userid, HttpServletRequest request, Media updateMedia) {

		Optional<Media> response = null ;
		try {
			response =  mediaService.getMediaByUserIdAndMediaId(userid, mediaid);
			if (!response.isPresent())
				return ResponseEntity.notFound().build();
			updateMedia.setId(mediaid);
			updateMedia.setUserid(userid);
			mediaService.saveFile(updateMedia);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Optional<Media>>(response, HttpStatus.OK);
	}

	@PostMapping(value = "/media/addcomment/{mediaid}/user/{userid}")
	public ResponseEntity<String> addcomment(@PathVariable long mediaid, @PathVariable String userid, HttpServletRequest request , MediaDTO commentMedia) {

		return null ;
	}
	
	@GetMapping(value = "/media/getcomment/{mediaid}/user/{userid}")
	public ResponseEntity<String> getcomments(@PathVariable long mediaid, @PathVariable String userid, HttpServletRequest request , MediaDTO commentMedia) {

		return null ;
	}



}
