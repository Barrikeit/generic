package org.barrikeit.rest;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.domain.GenericEntity;
import org.barrikeit.service.GenericService;
import org.barrikeit.service.dto.GenericDto;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <b>Generic Controller Class</b>
 *
 * <p>This abstract class provides common RESTful endpoint implementations for managing entities. It
 * relies on a {@link GenericService} to handle business logic and data access. This controller is
 * designed to work with entities that extend {@link GenericEntity} and their corresponding DTOs
 * that extend {@link GenericDto}.
 *
 * @param <G> the entity type that extends {@link GenericEntity}.
 * @param <S> the type of the entity's identifier, which must be {@link Serializable}.
 * @param <D> the DTO type that extends {@link GenericDto}.
 */
@Log4j2
@AllArgsConstructor
public abstract class GenericController<
    G extends GenericEntity, S extends Serializable, D extends GenericDto> {

  private final GenericService<G, S, D> service;

  /**
   * Retrieves a list of all DTOs.
   *
   * @return a response entity containing a list of DTOs.
   */
  @GetMapping
  public ResponseEntity<List<D>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  /**
   * Retrieves a specific DTO by its identifier.
   *
   * @param id the identifier of the entity to retrieve.
   * @return a response entity containing the requested DTO.
   */
  @GetMapping("/{id}")
  public ResponseEntity<D> findById(@PathVariable("id") S id) {
    return ResponseEntity.ok(service.find(id));
  }

  /**
   * Saves a new entity represented by the provided DTO.
   *
   * @param dto the DTO representing the entity to save.
   * @return a response entity containing the saved DTO.
   */
  @PostMapping()
  public ResponseEntity<D> save(@Validated @RequestBody D dto) {
    return ResponseEntity.ok(service.save(dto));
  }

  /**
   * Updates an existing entity identified by its identifier with the provided DTO.
   *
   * @param id the identifier of the entity to update.
   * @param dto the DTO containing the updated entity information.
   * @return a response entity containing the updated DTO.
   */
  @PutMapping("/{id}/update")
  public ResponseEntity<D> update(@PathVariable("id") S id, @RequestBody D dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  /**
   * Deletes an entity identified by its identifier.
   *
   * @param id the identifier of the entity to delete.
   * @return a response entity indicating the operation's result.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") S id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
