
package com.ibm.hello.util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UniqueIdMap {
	private static final Logger log = LoggerFactory.getLogger(UniqueIdMap.class);
	
	public static Map<String, String> uniqueIdMap;

	
	public static Map<String, String> createUniqueId() {
        if (uniqueIdMap == null) {
            synchronized (UniqueIdMap.class) {
                if (uniqueIdMap == null) {
                	uniqueIdMap = new HashMap<String, String>();
                }
            }
        }
        return uniqueIdMap;
    }
	
	public static void addUniqueId(String sessionId){
		createUniqueId();
		if( isUniqueIdPresent(sessionId) ){
			// session already present
			return;
		}
		uniqueIdMap.put(sessionId, UUID.randomUUID().toString());
	}
	
	public static void removeUniqueId(String sessionId){
		if( uniqueIdMap == null || !isUniqueIdPresent(sessionId) ){
			// session not available to remove
			return;
		}
		
		log.debug("removing unique id with key::" + sessionId);

		uniqueIdMap.remove(sessionId);
	}
	
	public static boolean isUniqueIdPresent(String sessionId){
		if( uniqueIdMap == null || uniqueIdMap.get(sessionId) == null ){
			return false;
		}
		return true;
	}
	
	public static String getUniqueId(String sessionId){
		if( uniqueIdMap == null ){
			return null;
		}
		return uniqueIdMap.get(sessionId);
	}
	
}
