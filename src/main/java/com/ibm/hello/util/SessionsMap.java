package com.ibm.hello.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;



public class SessionsMap {
	private static final Logger log = LoggerFactory.getLogger(SessionsMap.class);
	
	private static Map<String, WebSocketSession> sessionsMap;

	
	public static Map<String, WebSocketSession> createSession() {
        if (sessionsMap == null) {
            synchronized (SessionsMap.class) {
                if (sessionsMap == null) {
                	sessionsMap = new HashMap<String, WebSocketSession>();
                }
            }
        }
        return sessionsMap;
    }
	
	public static void addSession(String uniqueId, WebSocketSession session){
		createSession();
		if( isSessionPresent(uniqueId) ){
			// session already present
			return;
		}
		sessionsMap.put(uniqueId, session);
		log.debug("Session with id = " + uniqueId + " is added to sessions map" );
	}
	
	public static void removeSession(String uniqueId, WebSocketSession session, CloseStatus reason){
		if( sessionsMap == null || !isSessionPresent(uniqueId) ){
			// session not available to remove
			return;
		}
		sessionsMap.remove(uniqueId);
		log.debug("Session close reason = " + reason.getReason() + " and close code = " + reason.getCode());
		log.debug("Session with id = " + uniqueId + " is removed from sessions map" );
		log.debug("SessionMap size = " + getSessionMap().size());
	}
	
	public static boolean isSessionPresent(String uniqueId){
		if( sessionsMap == null || sessionsMap.get(uniqueId) == null ){
			return false;
		}
		return true;
	}
	
	public static WebSocketSession getSession(String uniqueId){
		if( sessionsMap == null ){
			return null;
		}
		return sessionsMap.get(uniqueId);
	}
	
	public static Map<String, WebSocketSession> getSessionMap(){
		return sessionsMap;
	}
	
	public static WebSocketSession getSomeSession(){
		WebSocketSession session = null;
		Iterator it = sessionsMap.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        session = (WebSocketSession)pair.getValue();
	    }
	    return session;
	}
}
