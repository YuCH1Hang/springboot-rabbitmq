package com.space.rabbitmq.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
 
import java.util.ArrayList;
import java.util.List;
 
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("upload")
public class UploadConfig {
    private String path="/tmp";
    private List<String> hosts = new ArrayList<>();
		 
    public String getPath() {
            return path;
    }
		     
    public void setPath(String path) {
        this.path = path;
    }
			 
    public List<String> getHosts() {
          return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }
    @Override
    public String toString() {
        return "{" + this.getPath() + ", " + this.getHosts() + "}";
    }
};
