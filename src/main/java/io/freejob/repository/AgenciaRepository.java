package io.freejob.repository;
import io.freejob.domain.Agencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Agencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Long>, JpaSpecificationExecutor<Agencia> {

}
