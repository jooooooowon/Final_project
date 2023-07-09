package com.example.demo.ootw;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OootwService {
	@Autowired
	private OootwDao dao;
	
	// 옷 이미지 정보 제외한 게시글 번호, 회원번호, 날짜, 날씨, 기온, 커멘트 게시글 작성
	public OootwDto save(OootwDto dto) {
		Oootw entity = dao.save(new Oootw(dto.getOotwnum(), dto.getMemnum(), dto.getOdate(), dto.getWeather(), dto.getTemp(), dto.getComments()));
		return new OootwDto(entity.getOotwnum(), entity.getMemnum(), entity.getOdate(), entity.getWeather(), entity.getTemp(), entity.getComments());
	}
	
	// 내 게시글 검색
	public OootwDto getMyBoard(int ootwnum) {
		Oootw entity = dao.findById(ootwnum).orElse(null);
		if(entity == null) {
			return null;
		} else {
			return new OootwDto(entity.getOotwnum(), entity.getMemnum(), entity.getOdate(), entity.getWeather(), entity.getTemp(), entity.getComments());
		}
	}
	
	// 전체 리스트 뿌리기
	public ArrayList<OootwDto> getAll(int memnum){
		ArrayList<Oootw> list = (ArrayList<Oootw>) dao.findAllListByOrder(memnum);
		ArrayList<OootwDto> list2 = new ArrayList<>();
		for(Oootw o : list) {
			list2.add(new OootwDto(o.getOotwnum(), o.getMemnum(), o.getOdate(), o.getWeather(), o.getTemp(), o.getComments()));
		}
		return list2;
	}
	
	// 날짜 검색 리스트 뿌리기
	public ArrayList<OootwDto> getByDateBetween(int memnum, String odate1, String odate2){
		ArrayList<Oootw> list = dao.findByOdateBetween(memnum, odate1, odate2);
		ArrayList<OootwDto> list2 = new ArrayList<>();
		for(Oootw o : list) {
			list2.add(new OootwDto(o.getOotwnum(), o.getMemnum(), o.getOdate(), o.getWeather(), o.getTemp(), o.getComments()));
		}
		return list2;
	}
	
	// 기온 검색 리스트 뿌리기(예은)
	public ArrayList<OootwDto> getByTempBetween(int memnum, double temp1, double temp2){
		ArrayList<Oootw> list = dao.findByTempBetween(memnum, temp1, temp2);
		ArrayList<OootwDto> dtoList = new ArrayList<>();
		for(Oootw o : list) {
			dtoList.add(new OootwDto(o.getOotwnum(), o.getMemnum(), o.getOdate(), o.getWeather(), o.getTemp(), o.getComments()));
		}
		return dtoList;
	}
	
	// 커멘트 like 검색
	public ArrayList<OootwDto> getByCommentsLike(String comments, int memnum) {
		ArrayList<Oootw> list = dao.findByCommentsLike("%" + comments + "%", memnum);
		ArrayList<OootwDto> dtoList = new ArrayList<>();
		for(Oootw o:list) {
			dtoList.add(new OootwDto(o.getOotwnum(), o.getMemnum(), o.getOdate(), o.getWeather(), o.getTemp(), o.getComments()));
		}
		return dtoList;
	}
	
	// 게시글 삭제
	public void delete(int ootwnum) {
		dao.deleteById(ootwnum);
	}
	
}