package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yx.sz.pojo.Job;

public interface JobRepository extends JpaRepository<Job,String> {
}
