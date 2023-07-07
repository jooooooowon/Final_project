package com.example.demo.bookmark;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface ObookmarkDao extends JpaRepository<Obookmark, Integer> {

	//Omember - memnum으로 검색
	@Transactional
	@Modifying
	@Query(value = "select * from obookmark where memnum = :memnum order by commnum desc", nativeQuery = true)
	List<Obookmark> findByMemnum(@Param("memnum") int memnum);

	@Transactional
	@Modifying
	@Query(value = "select * from obookmark where memnum = :memnum and commnum = :commnum", nativeQuery = true)
	List<Obookmark> findByMemnumAndCommnum(@Param("memnum") int memnum, @Param("commnum") int commnum);
}
