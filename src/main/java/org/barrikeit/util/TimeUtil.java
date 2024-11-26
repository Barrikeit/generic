package org.barrikeit.util;

import org.barrikeit.util.constants.UtilConstants;
import org.barrikeit.util.exceptions.UnExpectedException;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class TimeUtil {
  private TimeUtil() {
    throw new IllegalStateException("TimeUtil class");
  }

  private static String zone;

  @Value("${server.timeZone}")
  public void setZoneStatic(String zone) {
    TimeUtil.zone = zone;
  }

  public static Instant nowInstant() {
    return Instant.now().atZone(ZoneId.of(zone)).toInstant();
  }

  public static Date nowDate() {
    return Date.from(nowInstant());
  }

  public static LocalDate nowLocalDate() {
    return nowInstant().atZone(ZoneId.of(zone)).toLocalDate();
  }

  public static LocalDateTime nowLocalDateTime() {
    return nowInstant().atZone(ZoneId.of(zone)).toLocalDateTime();
  }

  public static LocalDate convertLocalDate(String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(UtilConstants.PATTERN_LOCAL_DATE);

    try {
      return LocalDate.parse(date, dateFormatter);
    } catch (DateTimeParseException e) {
      throw new IllegalArgumentException("Formato de fecha y hora inválido: " + date);
    }
  }

  public static LocalDateTime convertLocalDateTime(String date) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(UtilConstants.PATTERN_LOCAL_DATE);
    DateTimeFormatter dateTimeFormatter =
        DateTimeFormatter.ofPattern(UtilConstants.PATTERN_DATE_TIME);

    try {
      return LocalDateTime.parse(date, dateTimeFormatter);
    } catch (DateTimeParseException e) {
      try {
        return LocalDateTime.of(LocalDate.parse(date, dateFormatter), LocalTime.MIN);
      } catch (DateTimeParseException ex) {
        throw new UnExpectedException("Formato de fecha y hora inválido: " + date);
      }
    }
  }

  public static String formatLocalDate(LocalDate date, String format) {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
    return date.format(dateFormatter);
  }

  public static String formatLocalDateTime(LocalDateTime date, String format) {
    DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
    return date.format(dateTimeFormatter);
  }

  public static LocalDate timestampToLocalDate(Timestamp timestamp) {
    return timestamp.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime().toLocalDate();
  }

  public static LocalDateTime timestampToLocalDateTime(Timestamp timestamp) {
    return timestamp.toInstant().atZone(ZoneId.of(zone)).toLocalDateTime();
  }
}
