package com.kanni.ConcurrentAccessDemo.model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class APICallInfo {

    AtomicInteger apiCallCount;


    private Duration duration;

    @Override
    public String toString() {
        return "APICallInfo{" +
                "apiCallCount=" + apiCallCount +
                ", duration=" + duration +
                ", startingTime=" + startingTime +
                ", expiryTime=" + expiryTime +
                '}';
    }

    private LocalDateTime startingTime;

    private LocalDateTime expiryTime;


    public APICallInfo() {
    }

    public APICallInfo(int apiCallCount, Duration duration, LocalDateTime startingTime) {
        setApiCallCount(new AtomicInteger(apiCallCount));
        this.duration = duration;
        this.startingTime = startingTime;
        this.expiryTime = startingTime.plusMinutes(duration.toMinutes());
    }

    public APICallInfo(Duration duration, LocalDateTime startingTime) {
        setApiCallCount(new AtomicInteger(1));
        this.duration = duration;
        this.startingTime = startingTime;
        this.expiryTime = startingTime.plusMinutes(duration.toMinutes());
    }


    public AtomicInteger getApiCallCount() {
        return apiCallCount;
    }

    public void setApiCallCount(AtomicInteger apiCallCount) {
        this.apiCallCount = apiCallCount;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(LocalDateTime expiryTime) {
        this.expiryTime = expiryTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalDateTime startingTime) {
        this.startingTime = startingTime;
    }

    public boolean isExpiry() {
        if (getExpiryTime().isAfter(LocalDateTime.now())) {
            return  true;
        }else{
            isReleaseAPITime();
        }

        return false;
    }

    public boolean isReleaseAPITime() {
        if (getExpiryTime().isBefore(LocalDateTime.now())) {
            setStartingTime(LocalDateTime.now());
            setExpiryTime(LocalDateTime.now().plusMinutes(getDuration().toMinutes()));
            setApiCallCount(new AtomicInteger(1));
        }
        return true;
    }
}


