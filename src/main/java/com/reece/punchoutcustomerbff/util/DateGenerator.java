package com.reece.punchoutcustomerbff.util;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.stereotype.Service;

/**
 * Represents an easy mockable way to generate new dates.
 * @author john.valentino
 */
@Service
public class DateGenerator {

  public Timestamp generateTimestamp() {
    return new Timestamp(new Date().getTime());
  }

}
