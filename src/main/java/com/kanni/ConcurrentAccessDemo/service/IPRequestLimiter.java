package com.kanni.ConcurrentAccessDemo.service;

import com.kanni.ConcurrentAccessDemo.model.APICallInfo;
import io.github.bucket4j.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class IPRequestLimiter {

    private final Map<String, Map<String, APICallInfo>> cache = new ConcurrentHashMap<>();

    @Value("${apiCall.limit}")
    private Integer apiLimit;

    @Value("${apiCall.perMinutes}")
    private Integer perMinutes;

    public synchronized  boolean validateRegisterIP(String ipAddress,String APICall) {
        Map<String,APICallInfo> ipListMap=cache.get(ipAddress);

        if(null!=ipListMap && !ipListMap.isEmpty()){

            APICallInfo apiCallInfo=ipListMap.get(APICall);
            System.out.println("api-info"+apiCallInfo+"==="+APICall+"=="+cache);
            if(null!=apiCallInfo){
                if(apiCallInfo.isExpiry() && apiCallInfo.getApiCallCount().get() > apiLimit){
                    System.out.println("apiCallInfo.isExpiry()== "+apiCallInfo.isExpiry());
                    throw new RuntimeException("Limit API call by per IP Address : "+apiLimit);
                }
                ipListMap.put(APICall,new APICallInfo(apiCallInfo.getApiCallCount().incrementAndGet(),apiCallInfo.getDuration(),apiCallInfo.getStartingTime()));
                cache.put(ipAddress, ipListMap);
            }else{
                ipListMap.put(APICall,new APICallInfo(Duration.ofMinutes(perMinutes), LocalDateTime.now()));
                cache.put(ipAddress, ipListMap);
            }
        }else{
            Map<String,APICallInfo> tempMap=new ConcurrentHashMap<>();
            tempMap.put(APICall,new APICallInfo(Duration.ofMinutes(perMinutes), LocalDateTime.now()));
            cache.put(ipAddress, tempMap);

        }
        System.out.println(cache);
        return true;
    }


}
