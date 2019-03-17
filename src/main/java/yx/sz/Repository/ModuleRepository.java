package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yx.sz.pojo.Module;

public interface ModuleRepository extends JpaRepository<Module,String> {
}
