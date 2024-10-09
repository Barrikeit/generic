package org.barrikeit.service;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.commons.EntityConstants;
import org.barrikeit.commons.ExceptionConstants;
import org.barrikeit.commons.exceptions.NotFoundException;
import org.barrikeit.domain.GenericEntity;
import org.barrikeit.repository.GenericRepository;
import org.barrikeit.service.dto.GenericDto;
import org.barrikeit.service.mapper.GenericMapper;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>Generic Service Class</b>
 *
 * <p>This abstract class provides a generic implementation of service operations for managing
 * entities and their corresponding DTOs. It interacts with a {@link GenericRepository} for data
 * access and uses a {@link GenericMapper} for object mapping between entities and DTOs.
 *
 * @param <G> the entity type that extends {@link GenericEntity}.
 * @param <S> the type of the entity's identifier, which must be {@link Serializable}.
 * @param <D> the DTO type that extends {@link GenericDto}.
 */
@Log4j2
@AllArgsConstructor
public abstract class GenericService<
    G extends GenericEntity, S extends Serializable, D extends GenericDto> {
  private final GenericRepository<G, S> repository;
  private final GenericMapper<G, D> mapper;

  /**
   * Retrieves a list of all DTOs sorted by their identifier.
   *
   * @return a list of DTOs representing all entities.
   */
  public List<D> findAll() {
    return repository.findAll(Sort.by(Sort.Direction.ASC, EntityConstants.ID)).stream()
        .map(this.mapper::toDto)
        .toList();
  }

  /**
   * Retrieves a list of all DTOs sorted by the specified sort criteria.
   *
   * @param sort the sorting criteria.
   * @return a list of DTOs representing all entities.
   */
  public List<D> findAll(Sort sort) {
    return repository.findAll(sort).stream().map(this.mapper::toDto).toList();
  }

  /**
   * Retrieves a list of all entities sorted by their identifier.
   *
   * @return a list of entities.
   */
  public List<G> findAllEntity() {
    return repository.findAll(Sort.by(Sort.Direction.ASC, EntityConstants.ID)).stream().toList();
  }

  /**
   * Retrieves a list of all entities sorted by the specified sort criteria.
   *
   * @param sort the sorting criteria.
   * @return a list of entities.
   */
  public List<G> findAllEntity(Sort sort) {
    return repository.findAll(sort).stream().toList();
  }

  /**
   * Retrieves a DTO by its identifier.
   *
   * @param id the identifier of the entity to retrieve.
   * @return the DTO corresponding to the entity.
   * @throws NotFoundException if the entity is not found.
   */
  public D find(S id) {
    return repository
        .findById(id)
        .map(this.mapper::toDto)
        .orElseThrow(() -> new NotFoundException(ExceptionConstants.NOT_FOUND, id));
  }

  /**
   * Retrieves an entity by its identifier.
   *
   * @param id the identifier of the entity to retrieve.
   * @return the entity.
   * @throws NotFoundException if the entity is not found.
   */
  public G findEntity(S id) {
    return repository
        .findById(id)
        .orElseThrow(() -> new NotFoundException(ExceptionConstants.NOT_FOUND, id));
  }

  /**
   * Saves a new entity represented by the provided DTO.
   *
   * @param dto the DTO representing the entity to save.
   * @return the saved DTO.
   */
  @Transactional
  public D save(D dto) {
    G entity = mapper.toEntity(dto);
    return mapper.toDto(repository.save(entity));
  }

  /**
   * Saves a new entity directly.
   *
   * @param entity the entity to save.
   * @return the saved entity.
   */
  @Transactional
  public G save(G entity) {
    return repository.save(entity);
  }

  /**
   * Updates an existing entity identified by its identifier with the provided DTO.
   *
   * @param id the identifier of the entity to update.
   * @param dto the DTO containing the updated entity information.
   * @return the updated DTO.
   */
  @Transactional
  public D update(S id, D dto) {
    G entity = findEntity(id);
    mapper.updateEntity(dto, entity);
    return mapper.toDto(repository.save(entity));
  }

  /**
   * Deletes an entity identified by its identifier.
   *
   * @param id the identifier of the entity to delete.
   */
  @Transactional
  public void delete(S id) {
    repository.deleteById(id);
  }
}