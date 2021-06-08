package com.space.rabbitmq.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  public void init();

  public void save(String username,MultipartFile file);

  public Resource load(String username,String filename);
  public boolean check(String username,String filename);

  public void deleteAll(String username);

  public Stream<Path> loadAll(String username);

}
