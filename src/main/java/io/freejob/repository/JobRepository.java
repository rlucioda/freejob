package io.freejob.repository;
import io.freejob.domain.Job;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Job entity.
 */
@SuppressWarnings("unused")
@Repository
public interface JobRepository extends JpaRepository<Job, Long>, JpaSpecificationExecutor<Job> {

}
