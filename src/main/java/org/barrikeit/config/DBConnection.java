package org.barrikeit.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.util.constants.ConfigurationConstants;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@AllArgsConstructor
public class DBConnection {

  private final ApplicationProperties config;

  public Connection getConnection() {
    try {
      Connection con = DriverManager.getConnection(config.getPath());
      log.info(ConfigurationConstants.CONNECTION_SUCCESSFUL);
      return con;
    } catch (SQLException ex) {
      log.error(ConfigurationConstants.NOT_ABLE_TO_CONNECT_TO_DATABASE);
      log.error("Error opening connection: {}", ex.getMessage(), ex);
      return null;
    }
  }

  public void closeConnection(Connection con) {
    try {
      if (con != null) {
        log.info(ConfigurationConstants.RELEASING_ALL_OPEN_RESOURCES);
        con.close();
      }
    } catch (SQLException ex) {
      log.error(ConfigurationConstants.NOT_ABLE_TO_CLOSE_CONNECTION_TO_DATABASE);
      log.error("Error closing resource: {}", ex.getMessage());
    }
  }
}
