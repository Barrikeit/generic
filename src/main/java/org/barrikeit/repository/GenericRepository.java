package org.barrikeit.repository;

import java.io.Serializable;
import org.barrikeit.domain.GenericEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<G extends GenericEntity, S extends Serializable>
    extends JpaRepository<G, S>, JpaSpecificationExecutor<G> {}
