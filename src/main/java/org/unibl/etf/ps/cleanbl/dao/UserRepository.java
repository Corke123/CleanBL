package org.unibl.etf.ps.cleanbl.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.unibl.etf.ps.cleanbl.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
