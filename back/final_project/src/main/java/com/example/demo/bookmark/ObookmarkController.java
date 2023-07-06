package com.example.demo.bookmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.community.OcommunityDto;
import com.example.demo.community.OcommunityService;
import com.example.demo.likebnt.OlikebtnService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/obookmark")
public class ObookmarkController {

	@Autowired
	private ObookmarkService service;
	@Autowired
	private OcommunityService commservice;
	@Autowired
	private OlikebtnService likeservice; // 좋아요 서비스

	// 로그인 시와 로그아웃 시일 때 모두 일단은 전체 list를 뿌려주는 것이 필요하기 때문에
	// 게시글 전체 list를 뿌려주는 메서드를 작성한다.
	// 반복 사용될 예정 (그래봤자 두번 밖에 없지만 ㅇㅅㅇ)
	private ArrayList<OcommunityDto> allList() {
		ArrayList<OcommunityDto> list = commservice.getAll();
		Collections.sort(list, new Comparator<OcommunityDto>() {
			@Override
			public int compare(OcommunityDto dto1, OcommunityDto dto2) {
				return dto2.getCommnum() - dto1.getCommnum();
			}
		});
		return list;
	}

	// 북마크 전체목록 검색
	@GetMapping("")
	public Map getAll() {
		ArrayList<ObookmarkDto> list = service.getAll();
		Map map = new HashMap();
		map.put("list", list);
		return map;
	}

	// 회원번호로 북마크 리스트 검색
	@GetMapping("/bookmarklist/{memnum}")
	public Map getByMemnum(@PathVariable("memnum") int memnum) {
		ArrayList<OcommunityDto> list = allList();
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<ObookmarkDto> bookmarkList = service.getByMemnum(memnum);
//			for (OcommunityDto dto : list) {
//
//				// list에서 각 게시물의 좋아요 수를 좋아요 테이블에서 가져온 것을 바탕으로.
//				// 각 게시물의 dto에 있는 좋아요 수 변수에 저장한다.
//				dto.setBtnlike(likeservice.likeCount(dto.getCommnum()));
//
//				// 로그인 되어 있는 멤버가 현재 dto (list를 for문으로 돌리고 있다는 것을 잊지말자.)
//				// 에 좋아요를 눌렀는 지 확인한다.
//				// 좋아요를 눌렀었다면 chklike를 true로 바꾼다.
//				if (likeservice.getByMemnumAndCommnum(memnum, dto.getCommnum()) != null) {
//					dto.setChklike(true);
//				}
//
//				// 북마크도 좋아요와 같은 방식으로 할 수 있지만 더 좋은 방식이 생각나서 새롭게 짜보았다.
//				// 내가 저장해논 북마크를 뽑아놓은 bookmarkList를 for문을 돌린다.
//				// 현재 list의 dto(게시물)의 번호와 bookmarkList에 있는 게시물의 번호가 같다면
//				// 현재 dto는 내가 북마크 해놓은 게시물이기 때문에
//				// 북마크가 되어있다는 것을 보여준다. -> 게시물의 bookmarkchk를 true로 바꾼다.
//				for (ObookmarkDto bookmarkDto : bookmarkList) {
//					if (bookmarkDto.getCommnum().getCommnum() == dto.getCommnum()) {
//						dto.setChkbookmark(true);
//					}
//				}
//			}

			map.put("list", bookmarkList);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 게시글 번호로 북마크 리스트 검색
	@GetMapping("/{commnum}")
	public Map getByCommnum(@PathVariable("commnum") int commnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<ObookmarkDto> list = service.getByMemnum(commnum);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 북마크 추가
	@PutMapping("")
	public Map save(ObookmarkDto dto) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ObookmarkDto result = service.getByMemnumAndCommnum(dto.getMemnum().getMemnum(),
					dto.getCommnum().getCommnum());
			// 북마크가 되어 있는 지 확인하고
			// ( 회원 넘버와 게시판 넘버로 추출한다. )
			if (result == null) {
				// 없으면 넣고
				service.save(dto);
			} else {
				// 있으면 삭제
				service.delObookmark(result.getBmnum());
			}
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 북마크번호로 삭제
	@DeleteMapping("/{bmnum}")
	public Map del(@PathVariable("bmnum") int bmnum) {
		boolean flag = true;
		Map map = new HashMap();
		try {
			service.delObookmark(bmnum);
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
