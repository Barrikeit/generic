# **Generic Application**

This is a reusable and extensible Spring-based application template designed to streamline development by providing generic components for CRUD operations and essential configurations.

---

## **Overview**

### **Generic Classes**
The application includes a set of generic classes to simplify and standardize the implementation of CRUD operations:

1. **`GenericEntity`**: A base class for JPA entities with a primary key field `id`, implementing `Persistable<Long>` for new entity detection.
2. **`GenericDto`**: A base class for Data Transfer Objects (DTOs), ensuring they are serializable and compatible with REST APIs.
3. **`GenericMapper`**: An interface for transforming entities into DTOs and vice versa using MapStruct.
4. **`GenericService`**: Encapsulates business logic for CRUD operations using repositories and mappers.
5. **`GenericController`**: Provides RESTful endpoints for CRUD operations and integrates with `GenericService`.

These components ensure:
- **Reusability**: Use the same logic across multiple entities.
- **Extensibility**: Easily customize or override methods.
- **Modularity**: Separation of concerns for cleaner code.

---

## **Configurations and Properties**

### **Configurations**
1. Application **`.yaml`** / **`.yml`** / **`.properties`** files:
    - Centralized configuration for database settings, application profiles, and other environmental parameters.
    - Example:
      ```yaml
        server:
          port: 9991
          name: generic
          apiPath: /api/*
          timeZone: Europe/Madrid
        spring:
          profiles:
            active: @spring.profiles.active@
          config:
            import:
              - application-base.yaml
              - application-@spring.profiles.active@.yaml
      ```
2. **Profiles**:
    - Profiles for `dev`, `test`, and `default` environments to manage configurations dynamically using Maven profiles.
    - Example Maven profile:
      ```xml
      <profile>
          <id>dev</id>
          <activation>
              <property>
                  <name>env</name>
                  <value>dev</value>
              </property>
          </activation>
          <properties>
              <spring.profiles.active>dev</spring.profiles.active>
          </properties>
      </profile>
      ```

### **Usefulness**
- Simplifies environment-specific configurations.
- Ensures better maintainability and organization.
- Facilitates switching between local development and production setups.

---

## **Project Dependencies**

### **Key Dependencies**
The `pom.xml` includes a comprehensive set of dependencies to support modern Java and Spring development:

1. **Spring Framework**:
    - Core Spring libraries for web and data handling (`spring-web`, `spring-data-jpa`, `spring-webmvc`).
    - Security with `spring-security-core`.

2. **Database and ORM**:
    - `Hibernate`: ORM for database interactions.
    - `H2` and `PostgreSQL`: Drivers for in-memory testing and production-grade databases.
    - `HikariCP`: High-performance JDBC connection pool.

3. **Utilities and Enhancements**:
    - `Lombok`: Reduce boilerplate code.
    - `MapStruct`: Simplified object mapping.
    - `Apache POI` and `Jackcess`: For handling Microsoft Office and Access file formats.

4. **Testing and Logging**:
    - `Log4j`: Enhanced logging.
    - `H2`: Database for testing environments.

### **Build Plugins**
Key Maven plugins include:
- **`maven-compiler-plugin`**: Specifies Java version compatibility.
- **`maven-war-plugin`**: Builds a deployable `.war` file for web applications on the dist folder.
- **`maven-resources-plugin`**: Filters and processes resource files.

---

## **How to Use**

1. **Clone and Setup**:
    - Clone the repository and import it into your IDE.
    - Update the `application.yaml` file with your server preferences and the `application-base.yaml` with your database credentials.

2. **Define Entities**:
    - Create a new entity by extending `GenericEntity`.
    - Example:
      ```java
      @Entity
      @Table(name = "product")
      @AttributeOverride(
        name = "id",
       column = @Column(name = "id_product", nullable = false))
      public class Product extends GenericEntity {
          @Column(name = "name")
          private String name;
          @Column(name = "price")
          private double price;
      }
      ```

3. **Create DTOs and Mappers**:
    - Define DTOs by extending `GenericDto` and create a `GenericMapper` interface.
    - Example:
      ```java
      public class ProductDto extends GenericDto {
          private String name;
          private double price;
      }
 
      @Mapper(componentModel = "spring")
      public interface ProductMapper extends GenericMapper<Product, ProductDto> {}
      ```

4. **Service and Controller**:
    - Implement the service and controller classes using `GenericService` and `GenericController`.
    - Example:
      ```java
      @Service
      public class ProductService extends GenericService<Product, Long, ProductDto> {
          public ProductService(ProductRepository repository, ProductMapper mapper) {
              super(repository, mapper);
          }
      }
 
      @RestController
      @RequestMapping("/products")
      public class ProductController extends GenericController<Product, Long, ProductDto> {
          public ProductController(ProductService service) {
              super(service);
          }
      }
      ```

5. **Build and Run**:
    - Build the project using Maven:
      ```bash
      mvn clean install
      ```
    - Run the application on a web server using the `.war` file.
    - Or run the Main class that creates an embedded Tomcat to dispatch the web application.

---

## **Example Endpoints**

- **GET /products**: Retrieve all products.
- **GET /products/{id}**: Retrieve a product by ID.
- **POST /products**: Create a new product.
- **PUT /products/{id}**: Update an existing product.
- **DELETE /products/{id}**: Delete a product.

---

## **Contributing**

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature-name`.
3. Commit changes: `git commit -m "Add feature"`.
4. Push to branch: `git push origin feature-name`.
5. Open a Pull Request.

---
