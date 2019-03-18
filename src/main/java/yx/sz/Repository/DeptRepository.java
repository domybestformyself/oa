package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yx.sz.pojo.Dept;

import java.util.List;
import java.util.Map;

public interface DeptRepository extends JpaRepository<Dept,String> {

    @Query("select new Map( p.id  as code , p.name as name ) from Dept p")
    public List<Map<String, Object>> findDepts();
}
