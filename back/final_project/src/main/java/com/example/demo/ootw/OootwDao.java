package com.example.demo.ootw;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OootwDao extends JpaRepository<Oootw, Integer> {
	
	// 날짜 범위로 검색해서 리스트 뿌리기
	@Transactional
	@Modifying
	@Query(value="select * from oootw where memnum=:memnum and odate between :odate1 and :odate2 order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findByOdateBetween(@Param("memnum") int memnum, @Param("odate1") String odate1, @Param("odate2") String odate2);
	
	// 최신 날짜순 전체 리스트 조회
	@Transactional
	@Modifying
	@Query(value="select * from oootw where memnum=:memnum order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findAllListByOrder(@Param("memnum") int memnum);
	
	// 기온 between 자기 게시글만 검색(예은)
	@Transactional
	@Modifying
	@Query(value="select * from oootw where memnum=:memnum and temp between :temp1 and :temp2 order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findByTempBetween(@Param("memnum") int memnum, @Param("temp1") double temp1, @Param("temp2") double temp2);
	
	// 커멘트 like 검색
	@Transactional
	@Modifying
	@Query(value="select * from oootw where comments like :comments and memnum=:memnum order by odate desc", nativeQuery = true)
	ArrayList<Oootw> findByCommentsLike(@Param("comments") String comments, @Param("memnum") int memnum);
}