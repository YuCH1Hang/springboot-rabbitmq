package com.space.rabbitmq.model;

public class Submission {
   //private Long id;
   private String name;
   private String jsonInfo;
   private String uuid;
   private boolean ready;

   public Submission(String n) {name=n;ready=false;}
   public boolean getready() {return ready;}
   public void setReady(boolean b){ready=b;}
   //public Long getId() {return id;}
   //public void setId(Long a) { id=a;}
   public String getUuid() {return uuid;}
   public void setUuid(String a) { uuid=a;}
   public String getJsonInfo() {return jsonInfo;}
   public void setJsonInfo(String a) { jsonInfo=a;}
   public String getName() {return name;}
   public void setName(String a) { name=a;}
};
