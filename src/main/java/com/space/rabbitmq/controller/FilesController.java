package com.space.rabbitmq.controller;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import com.alibaba.fastjson.JSON;
import com.space.rabbitmq.service.mq.sender.*;
import java.util.UUID;

import com.space.rabbitmq.message.ResponseMessage;
import com.space.rabbitmq.model.FileInfo;
import com.space.rabbitmq.service.FilesStorageService;
import com.space.rabbitmq.message.RabbitMQMessage;
@Controller
//@CrossOrigin("http://localhost:8081")
public class FilesController {
  private static final Logger logger = LoggerFactory.getLogger(FilesController.class);

  @Autowired
  FilesStorageService storageService;
  @Autowired
  FirstSender sender;

  @PostMapping("/upload")
  public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file, HttpSession session) {
    String username = (String)session.getAttribute("username");
    if( username== null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login.html")).build();
    }
    
    logger.info("uploadFile:"+username); 
    logger.info("path:"+file.getOriginalFilename());

    Boolean status = storageService.check(username, file.getOriginalFilename());
    logger.info("exist:"+status.toString());
    if(status)
    {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("existed."));
    }

    Long t= System.currentTimeMillis();
    String timestamp = t.toString();

    String [] files = new String[1];
    files[0]= file.getOriginalFilename();
    String uuid=UUID.randomUUID().toString();
    RabbitMQMessage msg= new RabbitMQMessage(username,files,timestamp);
    //msg.setTimestamp(timestamp);
    logger.info(JSON.toJSONString(msg));
    try {
            storageService.save(username,file);
	    sender.send(uuid,JSON.toJSONString(msg));

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(timestamp));
    } 
     catch (Exception e) {
            String message = "";
	     //e.printStackTrace();
	     logger.error ("request exception, exception information: {}", e);
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }
  }
  @PostMapping("/multi-upload")
  public ResponseEntity<ResponseMessage> uploadMultiFile(@RequestParam("files") MultipartFile[] files, HttpSession session) {
    
    String username = (String)session.getAttribute("username");
    if( username== null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login.html")).build();
    }
    String [] filenames = new String[files.length];
    for ( int i=0;i< files.length;i++)
    {
	    filenames[i]=files[i].getOriginalFilename();
    }
    Long t= System.currentTimeMillis();
    String timestamp = t.toString();
    String uuid=UUID.randomUUID().toString();
    RabbitMQMessage msg= new RabbitMQMessage(username,filenames,timestamp);
    //msg.setTimestamp(timestamp);
    logger.info("uploadMultiFile"); 
    try {
        for (MultipartFile file : files){
	    logger.info("save:"+file.getOriginalFilename()); 
            Boolean status = storageService.check(username, file.getOriginalFilename());
            if(status)
            {
                    logger.info("existed:"+file.getOriginalFilename());
                    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).
			    body(new ResponseMessage("existed:"+file.getOriginalFilename()));
            }
            storageService.save(username,file);
        }
	      sender.send(uuid,JSON.toJSONString(msg));
    
      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(timestamp));
    } catch(Exception e) {
            String message = "";
	    logger.error ("request exception, exception information: {}", e);
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
    }
  }

  @GetMapping("/list")
  public ResponseEntity<List<FileInfo>> getListFiles(HttpSession session) {
    String username = (String)session.getAttribute("username");
    if( username== null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login.html")).build();
    }
    List<FileInfo> fileInfos = storageService.loadAll(username).map(path -> {
      String filename = path.getFileName().toString();
      String url = MvcUriComponentsBuilder
          .fromMethodName(FilesController.class, "getFile", path.getFileName().toString(),session).build().toString();

      return new FileInfo(filename, url);
    }).collect(Collectors.toList());

    return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
  }

  @GetMapping("/files/{filename:.+}")
  public ResponseEntity<Resource> getFile(@PathVariable String filename,HttpSession session) {
    String username = (String)session.getAttribute("username");
    if( username== null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login.html")).build();
    }
    Resource file = storageService.load(username,filename);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
  }
  @GetMapping("/status/{timestamp:.+}")
  public ResponseEntity<ResponseMessage> getStatus(@PathVariable String timestamp,HttpSession session) {
    String username = (String)session.getAttribute("username");
    if( username== null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create("/login.html")).build();
    }
    String filename = new String();
    filename = timestamp+".jpg";
    boolean status= storageService.check(username,filename);
    if(status) 
          return ResponseEntity.ok().body(new ResponseMessage(filename));
    else return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(""));

  }
}
