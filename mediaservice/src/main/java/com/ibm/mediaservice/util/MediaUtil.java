package com.ibm.mediaservice.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

@Service
public class MediaUtil {
	
	public Stream<Path> loadUserFiles(String userId) {
		try {
			Path userFilePath = Paths.get("C:\\images\\"+userId);
			return Files.walk(userFilePath, 1).filter(path -> !path.equals(userFilePath)).map(userFilePath::relativize);
		} catch (IOException e) {
			throw new RuntimeException("Could not load the files!");
		}
	}
	
	public boolean fileExist(String filename, String userId) throws MalformedURLException {
		Path file = Paths.get("C:\\images\\"+userId).resolve(filename);
		Resource resource = new UrlResource(file.toUri());
		if (resource.exists() || resource.isReadable()) {
			return true;
		} else {
			return false;
		}
	}
	public Resource loadUserFile(String filename, String userId) {
		try {
			Path file = Paths.get("C:\\images\\"+userId).resolve(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
}
