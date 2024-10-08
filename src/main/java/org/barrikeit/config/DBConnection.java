package org.barrikeit.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.barrikeit.commons.ConfigurationConstants;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DBConnection {
  private final DBConfiguration config;

  public DBConnection(DBConfiguration config) {
    this.config = config;
  }

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
    try{
      if (con != null){
        log.info(ConfigurationConstants.RELEASING_ALL_OPEN_RESOURCES);
        con.close();
      }
    }catch (SQLException ex) {
      log.error(ConfigurationConstants.NOT_ABLE_TO_CLOSE_CONNECTION_TO_DATABASE);
      log.error("Error closing resource: {}", ex.getMessage());
    }
  }
}
