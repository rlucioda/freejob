package io.freejob.repository;
import io.freejob.domain.Evento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Evento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

}
