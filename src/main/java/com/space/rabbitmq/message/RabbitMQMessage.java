package com.space.rabbitmq.message;

public class RabbitMQMessage {
  private  String []files;
  private  String username;
  private  String timestamp;

  public RabbitMQMessage(String username, String []files,String ts) {
    this.username = username;
    this.files=files;
    this.timestamp=ts;
  }

  public void setUserName(String username) {
    this.username=username;
  }
  public void setFiles(String [] f) {
    this.files=f;
  }
  public void setTimestamp(String ts)
  {
	  this.timestamp=ts;
  }

  public String getUsername() {return username;}
  public String []getfiles() {return files;}
  public String getTimestamp() {return timestamp;}

}
