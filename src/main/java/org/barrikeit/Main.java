package org.barrikeit;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Log4j2
public class Main {

  public static void main(String[] args) {
    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/config/application-context.xml");
    log.info("Cargando contexto...");
    InterfaceExample bean = (InterfaceExample) context.getBean("holamundo");
    log.info("Hola {}", bean.getNombre());
    context.close();
  }
}
