package com.mthien.post_service.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class DateTimeFormatter {

    Map<Long, Function<Instant, String>> strategyMap = new LinkedHashMap<>();

    public DateTimeFormatter() {
        strategyMap.put(60L, this::formartInSeconds);
        strategyMap.put(3600L, this::formartInMinutes);
        strategyMap.put(86400L, this::formartInHours);
        strategyMap.put(Long.MAX_VALUE, this::formartInDays);
    }

    public String formart(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        var strategy = strategyMap.entrySet().stream()
                .filter(longFunctionEntry -> {
                    return elapseSeconds < longFunctionEntry.getKey();
                }).findFirst().get();
        return strategy.getValue().apply(instant);
    }

    private String formartInSeconds(Instant instant) {
        long elapseSeconds = ChronoUnit.SECONDS.between(instant, Instant.now());
        return elapseSeconds + " seconds ago";
    }

    private String formartInMinutes(Instant instant) {
        long elapseMinutes = ChronoUnit.MINUTES.between(instant, Instant.now());
        return elapseMinutes + " minutes ago";
    }

    private String formartInHours(Instant instant) {
        long elapseHours = ChronoUnit.HOURS.between(instant, Instant.now());
        return elapseHours + " hours ago";
    }

    private String formartInDays(Instant instant) {
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ISO_DATE;
        return localDateTime.format(formatter);
    }
}
