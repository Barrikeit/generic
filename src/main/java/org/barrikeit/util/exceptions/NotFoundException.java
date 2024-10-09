package org.barrikeit.util.exceptions;

import org.barrikeit.util.constants.ExceptionConstants;
import org.springframework.http.HttpStatus;

import java.net.URI;

public class NotFoundException extends GenericException {

  static final URI TYPE = URI.create("");

  public NotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }

  public NotFoundException(String message, Object... messageArgs) {
    super(HttpStatus.NOT_FOUND, TYPE, ExceptionConstants.NOT_FOUND, message, messageArgs);
  }
}