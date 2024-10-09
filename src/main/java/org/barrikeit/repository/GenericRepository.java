package org.barrikeit.repository;

import java.io.Serializable;
import org.barrikeit.domain.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * <b>Generic Repository Interface</b>
 * <p>
 * This interface serves as a base repository for generic entities,
 * extending Spring Data's JpaRepository and JpaSpecificationExecutor.
 * It provides standard data access methods for any entity type
 * that extends {@link GenericEntity}.
 * </p>
 *
 * @param <G> the entity type that extends {@link GenericEntity}.
 * @param <S> the type of the entity's identifier, which must be {@link Serializable}.
 */
@NoRepositoryBean
public interface GenericRepository<G extends GenericEntity, S extends Serializable>
    extends JpaRepository<G, S>, JpaSpecificationExecutor<G> {}
