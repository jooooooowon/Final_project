package com.example.demo.likebnt;


import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class OlikebtnService {
	@Autowired
	private OlikebtnDao dao;
	
	// (dto / vo) 변경하기
		private Object change(Object obj) {
			// 매개변수의 클래스를 확인한다.
			if (obj instanceof Olikebtn) {
				// 만약 vo라면 temp에 vo클래스로 값을 넣어놓는다.
				Olikebtn temp = (Olikebtn) obj;
				// vo의 값들을 dto 생성자를 이용하여 새로 생긴 dto에 값을 기입해준 후 리턴한다.
				return new OlikebtnDto(temp.getLikenum(), temp.getCommnum(), temp.getMemnum());
						
			} else {
				// 반대로 한다.
				OlikebtnDto temp = (OlikebtnDto) obj;
				return new Olikebtn(temp.getLikebtn(), temp.getCommnum(), temp.getMemnum());
						
			}
		}
		
		// ArrayList Vo -> Dto 로 변경하기
		private ArrayList<OlikebtnDto> changeList(ArrayList<Olikebtn> list) {
			// 값을 넣은 후 리턴할 빈 dto list를 선언 및 생성한다.
			ArrayList<OlikebtnDto> listDto = new ArrayList<>();
			for (Olikebtn vo : list) {
				// 입력 값으로 받은 vo list에 있는 vo들을 dto로 변환한다.
				OlikebtnDto dto = (OlikebtnDto) change(vo);
				// method가 실행될 때 선언했던 dto list에 변경된 dto들을 삽입한다.
				listDto.add(dto);
			}
			// 완성된 dto list를 리턴한다.
			return listDto;
		}

		// 등록
		public OlikebtnDto save(OlikebtnDto dto) {
			Olikebtn like = (Olikebtn) change(dto);
			OlikebtnDto result = (OlikebtnDto) change(dao.save(like));
			return result;
		}
		
		// 검색(좋아요를 눌렀는지 확인하기 위함)
		public OlikebtnDto getByMemnumAndCommnum(int memnum, int commnum) {
			ArrayList<Olikebtn> list = (ArrayList<Olikebtn>) dao.findByMemnumAndCommnum(memnum, commnum);
			if(list == null || list.size() == 0) {
				return null;
			}
			return (OlikebtnDto) change(list.get(0));
		}
		
		// 삭제
		public void delOlikebtn(int likenum) {
			dao.deleteById(likenum);
		}
		
		public OlikebtnDto getById(int likenum) {
			return dao.findByLikenum(likenum);
		}
		
		//좋아요 cnt list
		public int likeCount(int commnum) {
			System.out.println("commnum : " + commnum);
			ArrayList<Olikebtn> list = (ArrayList<Olikebtn>)dao.likeCount(commnum);
			if(list == null) {
				return 0;
			}
			return list.size();
		}
}
