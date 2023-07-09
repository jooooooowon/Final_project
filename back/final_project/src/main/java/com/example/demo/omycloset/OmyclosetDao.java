package com.example.demo.omycloset;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface OmyclosetDao extends JpaRepository<Omycloset, Integer> {
	
	// 멤버번호에 해당하는 전체리스트
	@Transactional
	@Modifying
	@Query(value="select * from omycloset where memnum=:memnum order by favorite desc, closetnum desc", nativeQuery = true)
	ArrayList<Omycloset> findAllByMemnum(@Param("memnum") int memnum);
	
	// 대분류 카테고리(1, 2, 3, 4, 5) 리스트 검색하기
	@Transactional
	@Modifying
	@Query(value="select * from omycloset where memnum=:memnum and maintag=:maintag order by favorite desc, closetnum desc", nativeQuery = true)
	ArrayList<Omycloset> findByMaintag(@Param("memnum") int memnum, @Param("maintag") String maintag);
	
	// 소분류 카테고리(11, 12, 13, 14..., 51, 52, 53, 54, 55...)리스트 검색하기
	@Transactional
	@Modifying
	@Query(value="select * from omycloset where memnum=:memnum and subtag=:subtag order by favorite desc, closetnum desc", nativeQuery = true)
	ArrayList<Omycloset> findBySubtag(@Param("memnum") int memnum, @Param("subtag") String subtag);
	
	// 옷 이름 키워드 리스트 검색하기
	@Transactional
	@Modifying
	@Query(value="select * from omycloset where memnum=:memnum and cloth like :cloth order by favorite desc, closetnum desc", nativeQuery = true)
	ArrayList<Omycloset> findByClothLike(@Param("memnum") int memnum, @Param("cloth") String cloth);
	
	// 즐겨찾기 순서 => 최신 등록순
	@Transactional
	@Modifying
	@Query(value="select * from omycloset order by favorite desc, closetnum desc", nativeQuery = true)
	ArrayList<Omycloset> findAllByClosetnumAndFavorite();
	
	// 즐겨찾기 on.. pk받아와서 0 => 1로 수정
	@Transactional
	@Modifying
	@Query(value="update omycloset set favorite=1 where closetnum=:num", nativeQuery=true)
	void updateFavoriteOn(@Param("num") int closetnum);
	
	// 즐겨찾기 off.. pk받아와서 1 => 0으로 수정
	@Transactional
	@Modifying
	@Query(value="update omycloset set favorite=0 where closetnum=:num", nativeQuery=true)
	void updateFavoriteOff(@Param("num") int closetnum);
	
}