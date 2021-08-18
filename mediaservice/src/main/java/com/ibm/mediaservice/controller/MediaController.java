package com.ibm.mediaservice.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.ibm.mediaservice.dto.FileInfo;
import com.ibm.mediaservice.dto.MediaDTO;
import com.ibm.mediaservice.dto.MediaRequest;
import com.ibm.mediaservice.entity.Media;
import com.ibm.mediaservice.exception.StorageException;
import com.ibm.mediaservice.models.AuthenticationRequest;
import com.ibm.mediaservice.service.MediaService;
import com.ibm.mediaservice.util.MediaUtil;

@CrossOrigin(origins = "*")
@RestController
//@RequestMapping({"/authenticate"})
public class MediaController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${welcome.message: default value}")
	private String welcomeMessage;
	
	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private MediaService mediaService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//@Autowired
	//private UserServiceImpl userDetailsService;
	
	//@Autowired
	//private JwtUtil jwtTokenUtil;
	
	@Autowired
	private MediaUtil mediaUtil;

	//test end point for jwt
	@GetMapping(value = "/test")
	public String test() {	return welcomeMessage; }
	
	@PostMapping(value = "/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}
		
		//final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		//final String jwt = jwtTokenUtil.generateToken(userDetails);
		//return ResponseEntity.ok(new AuthenticationResponse(jwt));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	 

	
	@PostMapping(value = "/media/singlefileupload/user/{userid}")
	public ResponseEntity<String> singlemediaupload(@PathVariable String userid,MediaDTO mediaDTO, HttpServletRequest request) {

		log.info("media details: " + mediaDTO);
		ResponseEntity<String> response = null ;
		try {
			response = saveuploadfile(mediaDTO, request,userid);
			
		} catch (Exception e) {
			throw new StorageException("Failed to store empty file " + mediaDTO.getFile());
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
			throw new StorageException("Failed to store multiple files for this user id" + userid);
		}
	}

	private ResponseEntity<String> saveuploadfile(MediaDTO mediaDTO, HttpServletRequest request, String userId) throws Exception {
		
		//String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
		String fileName = null;
		String uploadDirectory = uploadFolder +"/"+ userId;
		
		
		try {
			MultipartFile multipartFile = mediaDTO.getFile();
			fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			Path uploadPath = Paths.get(uploadDirectory);
			log.info("FileName: " + mediaDTO.getFile().getOriginalFilename());

			Date createDate = new Date();

			if (!Files.exists(uploadPath)) {
				try {
					Files.createDirectories(uploadPath);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (multipartFile.isEmpty() || fileName.isEmpty() ) {
                throw new StorageException("Failed to store empty file " + multipartFile);
            }
			if (fileName.contains("..")) {
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + fileName);
            }
			//Path destination = Paths.get(uploadPath.toString()+"\\"+fileName);
			try {
				//filePath = uploadPath.resolve(fileName);
				Files.copy(multipartFile.getInputStream(), uploadPath.resolve(fileName.trim()),StandardCopyOption.REPLACE_EXISTING);
				//Files.copy(multipartFile.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ioe) {
				throw new IOException("Could not save image file: " + fileName, ioe);
			}

			byte[] imageData = multipartFile.getBytes();
			log.info("uploadDirectory:: " + uploadDirectory);
			Path mediaUrl = Paths.get("images" + "/" + mediaDTO.getUserId().toString());
			//Resource resource = new UrlResource(filePath.toUri());

			Media media = new Media();
			media.setTags(Arrays.asList(mediaDTO.getTags()));
			media.setEffects(Arrays.asList(mediaDTO.getEffect()));
			media.setUserid(userId);
			media.setFilename(fileName);
			media.setMediaurl(mediaUrl.resolve(fileName).toString());
			media.setType(multipartFile.getContentType());
			media.setImage(imageData);
			media.setDescription(mediaDTO.getDesc());
			media.setUpload_date(createDate);
			mediaService.saveFile(media);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
		}
		catch(IOException ex) {
			throw new StorageException("Failed to store file " + fileName, ex);
		}
		return new ResponseEntity<String>("Product Saved With File - " + fileName, HttpStatus.OK);
	}
	
	
	@GetMapping(value = "/media/user/{userid}")
	public ResponseEntity<List<Media>> getMediaListByUser(@PathVariable String userid, HttpServletRequest request) {

		//List<Media> response = null ;
		try {

		//	Path userFilePath = Paths.get("images/" + userid);
			log.info("Executing loadUserFiles: with userId:" + userid);
			List<FileInfo> filePathInfos = mediaUtil.loadUserFiles(userid).map(path -> {
				String filename = path.getFileName().toString();
				String url = MvcUriComponentsBuilder
						.fromMethodName(MediaController.class, "getUserFile", userid, path.getFileName().toString()).build()
						.toString();

				return new FileInfo(filename, url);
			}).collect(Collectors.toList());

			List<Media> mediaList =  mediaService.getMediaListByUserId(userid);
			
			List<Media> mediaInfos = new ArrayList<Media>();
			for (Media media : mediaList) {
				for (FileInfo fileInfo : filePathInfos) {
					System.out.println("fileInfo.getName()->"+fileInfo.getName());
					System.out.println("media.getFilename()->"+media.getFilename());
					if (fileInfo.getName().equalsIgnoreCase(media.getFilename())) {
						Media fileDetails = new Media();
						fileDetails.setId(media.getId());
						fileDetails.setDescription(media.getDescription());
						fileDetails.setFilename(media.getFilename());
						fileDetails.setMediaurl(fileInfo.getUrl());
						fileDetails.setType(media.getType());
						fileDetails.setUpload_date(media.getUpload_date());
						fileDetails.setUserid(media.getUserid());
						
						try {
							if (mediaUtil.fileExist(media.getFilename(), userid)) {
								mediaInfos.add(fileDetails);
							}
						} catch (MalformedURLException e) {
							log.error(e.getMessage());
							return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(mediaInfos);
						}
					}
				}
			}
			return ResponseEntity.status(HttpStatus.OK).body(mediaInfos);
			
		} catch (Exception e) {
			throw new StorageException("Failed to get the files by user " + userid, e);
		}

	}

	@GetMapping(value = "/search/media/{mediaid}")
	public ResponseEntity<Optional<Media>> getMediaByMediaId(@PathVariable long mediaid, HttpServletRequest request) {

		Optional<Media> response = null ;
		try {
			response = mediaService.getMediaById(mediaid);
		} catch (Exception e) {
			throw new StorageException("Failed to get the file by search " + mediaid, e);
		}
		return new ResponseEntity<Optional<Media>>(response, HttpStatus.OK);
	}
	
	@GetMapping(value = "/mediadetail/{mediaid}/user/{userid}")
	public ResponseEntity<Optional<Media>> mediaDetailByUserId(@PathVariable long mediaid, @PathVariable String userid, HttpServletRequest request) {

		Optional<Media> response = null ;
		try {
			response = mediaService.getMediaByUserIdAndMediaId(userid, mediaid);
		} catch (Exception e) {
			throw new StorageException("Failed to get the file by media id and user id " + mediaid + userid, e);
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
			throw new StorageException("Failed to update the media " + mediaid + userid, e);
		}
		return new ResponseEntity<Optional<Media>>(response, HttpStatus.OK);
	}
	
	/***
	 * Rest Api to fetch file with user id and file name
	 */
	@GetMapping("/files/{userId}/{filename:.+}")
	public ResponseEntity<Resource> getUserFile(@PathVariable String userId, @PathVariable String filename) {
		log.info("files api executing with filename.." + filename + " and userId .." + userId);
		Resource file = mediaUtil.loadUserFile(filename, userId);
		final HttpHeaders httpHeaders = new HttpHeaders();
		 httpHeaders.setContentType(MediaType.IMAGE_PNG);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
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
