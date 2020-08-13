package com.ibm.hello.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy;
import org.springframework.web.socket.server.standard.WebSphereRequestUpgradeStrategy;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.ibm.hello.util.ChannelCommunicationType;
import com.ibm.hello.util.ChannelIdEnum;


@Configuration
@EnableWebSocket
@EnableScheduling
public class WebSocketConfig implements WebSocketConfigurer {
	private static final Logger log = LoggerFactory.getLogger(WebSocketConfig.class);
	
    @Autowired
    WebSocketMessageReceiver webSocketHandler;

    
    @Override    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	log.debug("WebSocketConfig.....registerWebSocketHandlers...");
    	try {
    		for(ChannelIdEnum channelId: ChannelIdEnum.values())
    		{
    			
    			if(ChannelCommunicationType.WEBSOCKET.toString().equals("WEBSOCKET")) {
    				try {
    					Class tomcatRequestUpgradeStrategy = Class.forName("org.springframework.web.socket.server.standard.TomcatRequestUpgradeStrategy");
    					registry.addHandler(webSocketHandler, "/connectors").setAllowedOrigins("*")
		    	        .setHandshakeHandler(new DefaultHandshakeHandler((TomcatRequestUpgradeStrategy)tomcatRequestUpgradeStrategy.newInstance()));
    				} catch(ClassNotFoundException cnfe) {
    					try {
    						Class webSphereRequestUpgradeStrategy = Class.forName("org.springframework.web.socket.server.standard.WebSphereRequestUpgradeStrategy");
        					registry.addHandler(webSocketHandler, "/connectors").setAllowedOrigins("*")
    		    	        .setHandshakeHandler(new DefaultHandshakeHandler((WebSphereRequestUpgradeStrategy)webSphereRequestUpgradeStrategy.newInstance()));
    					} catch(ClassNotFoundException cnfew) {
    						log.error("WebSocket Handler could not be registered");
    						log.error(cnfew.getMessage());
    					}
    					
    				}
    				log.debug("registeredWebSocketHandlers... for channelid >>>>>>>>>{}",channelId.toString());
		    	}
    		}
    	} catch (Exception e) {
			log.error(e.getMessage());
		}
        
    }

}