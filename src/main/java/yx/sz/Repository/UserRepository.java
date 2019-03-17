package yx.sz.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import yx.sz.pojo.User;


public interface UserRepository extends JpaRepository<User,String>, JpaSpecificationExecutor<User> {



}
