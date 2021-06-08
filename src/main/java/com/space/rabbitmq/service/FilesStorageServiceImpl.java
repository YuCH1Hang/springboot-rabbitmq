package com.space.rabbitmq.service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import com.space.rabbitmq.config.UploadConfig;
@Service
public class FilesStorageServiceImpl implements FilesStorageService {
  private static final Logger logger = LoggerFactory.getLogger(FilesStorageServiceImpl.class);
  @Autowired
  private UploadConfig config;

  private final Path root = Paths.get("/tmp");

  @Override
  public void init() {
    try {
      Files.createDirectory(root);
    } catch (IOException e) {
      throw new RuntimeException("Could not initialize folder for upload!");
    }
  }

  @Override
  public void save(String username, MultipartFile file) {
    Path path=Paths.get(config.getPath(),username,file.getOriginalFilename());
    logger.info(path.toString()); 
    try {
      Files.copy(file.getInputStream(),path); 
    } catch (Exception e) {
      throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
    }
  }
  @Override
  public boolean check(String username, String filename) {
    Path path=Paths.get(config.getPath(),username,filename);
    return Files.exists(path);
  }
  @Override
  public Resource load(String username,String filename) {
    Path path=Paths.get(config.getPath(),username);
    try {
      Path file = path.resolve(filename);
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

  @Override
  public void deleteAll(String username) {
    Path path=Paths.get(config.getPath(),username);
    FileSystemUtils.deleteRecursively(path.toFile());
  }

  @Override
  public Stream<Path> loadAll(String username) {
    Path userpath=Paths.get(config.getPath(),username);
    try {
      return Files.walk(userpath, 1).filter(path -> !path.equals(userpath)).map(userpath::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }
  }

}
