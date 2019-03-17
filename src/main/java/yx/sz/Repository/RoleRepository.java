package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yx.sz.pojo.Role;

public interface RoleRepository extends JpaRepository<Role,Long> {
}
