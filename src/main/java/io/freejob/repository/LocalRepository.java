package io.freejob.repository;
import io.freejob.domain.Local;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Local entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocalRepository extends JpaRepository<Local, Long>, JpaSpecificationExecutor<Local> {

}
