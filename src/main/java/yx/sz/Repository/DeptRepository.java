package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yx.sz.pojo.Dept;

public interface DeptRepository extends JpaRepository<Dept,String> {
}
