package com.example.demo.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OmemberDao extends JpaRepository<Omember, Integer> {
	Omember findById(String id);
	Omember findByNickname(String nickname);
	void findByPwd(String pwd);
	
	@Transactional
	@Query(value="select * from omember where email=:email", nativeQuery = true)
	Omember findByEmail(@Param("email") String email);
}
