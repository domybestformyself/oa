package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yx.sz.pojo.Job;

import java.util.List;
import java.util.Map;

public interface JobRepository extends JpaRepository<Job,String> {
    @Query("select new Map(j.code as code ,j.name as name) from Job j")
    public List<Map<String, Object>> findJobs() throws Exception ;
}
