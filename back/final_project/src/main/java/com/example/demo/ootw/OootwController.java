package com.example.demo.ootw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.omycloset.Omycloset;
import com.example.demo.omycloset.OmyclosetDto;
import com.example.demo.omycloset.OmyclosetService;
import com.example.demo.ootwImgs.OootwimgsDto;
import com.example.demo.ootwImgs.OootwimgsService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/boards")
public class OootwController {
	@Autowired
	private OootwService service;
	@Autowired
	private OootwimgsService imgservice;
	@Autowired
	private OmyclosetService closetservice;

	// 게시글 작성.. POST
	@PostMapping("")
	public Map add(OootwDto dto, Integer[] closetnumlist) {
		// oootwdto = "ootwnum", memnum, odate, weather, temp, comments
		// oootwimgsdto = ootwimgsnum, "ootwnum", closetnum
		// 옷장 이미지 정보 제외한 것들 저장
		OootwDto odto = service.save(dto);
		Omycloset closetVO = new Omycloset();
		Oootw ootwVo = new Oootw();
		OootwimgsDto imgdto = new OootwimgsDto();
		OootwimgsDto final2 = new OootwimgsDto();
		OmyclosetDto closetdto = new OmyclosetDto();
		try {
//		// int 옷장 번호로 옷장 정보 불러오기
			if (closetnumlist[0] != null) {
				closetdto = closetservice.getMyCloth(closetnumlist[0]);
//		// 각각의 생성자에 위에서 저장한 dto의 fk들 저장
				closetVO.setClosetnum(closetdto.getClosetnum());
				ootwVo.setOotwnum(odto.getOotwnum());
//		// 마지막으로 Oootwimgs Dto에 각각 멤버변수 집어넣기
				imgdto.setClosetnum(closetVO);
				imgdto.setOotwnum(ootwVo);
//		// set을 토대로 OootwimgsDto 새로 저장
				System.out.println();
				final2 = imgservice.save(imgdto);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}

		try {
			if (closetnumlist[1] != null) {
				closetdto = closetservice.getMyCloth(closetnumlist[1]);
				closetVO.setClosetnum(closetdto.getClosetnum());
				ootwVo.setOotwnum(odto.getOotwnum());
				imgdto.setClosetnum(closetVO);
				imgdto.setOotwnum(ootwVo);
				final2 = imgservice.save(imgdto);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}

		try {
			if (closetnumlist[2] != null) {
				closetdto = closetservice.getMyCloth(closetnumlist[2]);
				closetVO.setClosetnum(closetdto.getClosetnum());
				ootwVo.setOotwnum(odto.getOotwnum());
				imgdto.setClosetnum(closetVO);
				imgdto.setOotwnum(ootwVo);
				final2 = imgservice.save(imgdto);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}

		try {
			if (closetnumlist[3] != null) {
				closetdto = closetservice.getMyCloth(closetnumlist[3]);
				closetVO.setClosetnum(closetdto.getClosetnum());
				ootwVo.setOotwnum(odto.getOotwnum());
				imgdto.setClosetnum(closetVO);
				imgdto.setOotwnum(ootwVo);
				final2 = imgservice.save(imgdto);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}

		try {
			if (closetnumlist[4] != null) {
				closetdto = closetservice.getMyCloth(closetnumlist[4]);
				closetVO.setClosetnum(closetdto.getClosetnum());
				ootwVo.setOotwnum(odto.getOotwnum());
				imgdto.setClosetnum(closetVO);
				imgdto.setOotwnum(ootwVo);
				final2 = imgservice.save(imgdto);
			}
		} catch (IndexOutOfBoundsException e) {
			System.out.println(e);
		}

		Map map = new HashMap<>();
		map.put("dto", odto);
		return map;
	}

	// 디테일 뿌리기.. GET(/ootwnum)
	@GetMapping("/{ootwnum}")
	public Map getByNum(@PathVariable("ootwnum") int ootwnum) {
		OootwDto dto = service.getMyBoard(ootwnum);
		ArrayList<OootwimgsDto> list = imgservice.getMyImgs(ootwnum); // 게시글 번호(ootwnum)로 옷장 정보(..closetnum) 불러와서 담기
		System.out.println(list);
		Map map = new HashMap<>();
		map.put("dto", dto);
		map.put("list", list); // vue에서 list.closetnum.closetnum으로 closetnum 뽑아서 옷장 img src 뿌리기
		// vue created:function에서 list for문 돌리고, closetnum 있는만큼 뽑아서 closet controller에서
		// img src 뽑아오기
		return map;
	}

	// 전체 리스트 뿌리기..
	@GetMapping("/members/{memnum}")
	public Map getAll(@PathVariable("memnum") int memnum) {
		ArrayList<OootwDto> list = service.getAll(memnum);
		// 리스트의 사이즈만큼 리스트를 dto에 담아서 Integer 배열에 게시글 번호 담기
		Integer[] ootwnums = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++) {
			System.out.println("기온범위로 뽑은 리스트: " + list.get(i));
			OootwDto dto = list.get(i);
			ootwnums[i] = dto.getOotwnum();
			System.out.println(ootwnums[i]);
		}

		// 게시글 번호 배열 크기만큼 대표 옷번호 담을 Integer 배열 생성
		// 게시글이 5개면 각 게시글의 대표사진 5개 담을 배열임
		Integer[] closetNumList = new Integer[ootwnums.length];
		ArrayList<OootwimgsDto> dtoList = new ArrayList<>();

		// 게시글 번호 배열 크기만큼 반복문 돌리면서 각 게시글에 첨부된 이미지의 정보를 dtoList에 담음
		try {
			for (int i = 0; i < ootwnums.length; i++) {
				dtoList = imgservice.getMyImgs(ootwnums[i]);
				// 게시글별 첨부된 이미지리스트의 첫번째 대표 이미지만 배열에 담음
				closetNumList[i] = dtoList.get(0).getClosetnum().getClosetnum();
				System.out.println("각 게시글 대표 옷 번호:" + closetNumList[i]);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		Map map = new HashMap<>();
		map.put("list", list);
		map.put("closetNumList", closetNumList); // 각 게시글 대표(index=0) 옷 번호
		return map;
	}

	// 날짜 검색 리스트 뿌리기.. GET(/dates/odate)
	@GetMapping("/dates/{memnum}/{date1}/{date2}")
	public Map getByDate(@PathVariable("memnum") int memnum, @PathVariable("date1") String date1, @PathVariable("date2") String date2) {
		String odate1 = date1.substring(2, 4) + "/" + date1.substring(5, 7) + "/" + date1.substring(8, 10);
		String odate2 = date2.substring(2, 4) + "/" + date2.substring(5, 7) + "/" + date2.substring(8, 10);
		System.out.println(odate1);
		System.out.println(odate2);
		ArrayList<OootwDto> list = service.getByDateBetween(memnum, odate1, odate2);
		// 리스트의 사이즈만큼 리스트를 dto에 담아서 Integer 배열에 게시글 번호 담기
		Integer[] ootwnums = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++) {
			System.out.println("기온범위로 뽑은 리스트: " + list.get(i));
			OootwDto dto = list.get(i);
			ootwnums[i] = dto.getOotwnum();
			System.out.println(ootwnums[i]);
		}

		// 게시글 번호 배열 크기만큼 대표 옷번호 담을 Integer 배열 생성
		// 게시글이 5개면 각 게시글의 대표사진 5개 담을 배열임
		Integer[] closetNumList = new Integer[ootwnums.length];
		ArrayList<OootwimgsDto> dtoList = new ArrayList<>();

		// 게시글 번호 배열 크기만큼 반복문 돌리면서 각 게시글에 첨부된 이미지의 정보를 dtoList에 담음
		try {
			for (int i = 0; i < ootwnums.length; i++) {
				dtoList = imgservice.getMyImgs(ootwnums[i]);
				// 게시글별 첨부된 이미지리스트의 첫번째 대표 이미지만 배열에 담음
				closetNumList[i] = dtoList.get(0).getClosetnum().getClosetnum();
				System.out.println("각 게시글 대표 옷 번호:" + closetNumList[i]);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		Map map = new HashMap<>();
		map.put("list", list);
		map.put("closetNumList", closetNumList); // 각 게시글 대표(index=0) 옷 번호
		return map;
	}

	// 예은 - 기온 범위 검색 리스트 날짜 최신순으로 뿌리기.. GET(/temps/{memnum}/{temp1}/{temp2})
	@GetMapping("/temps/{memnum}/{temp1}/{temp2}")
	public Map getByTemp(@PathVariable("memnum") int memnum, @PathVariable("temp1") double temp1,
			@PathVariable("temp2") double temp2) {

		// 기온 범위로 검색한 ootw 게시글 리스트
		ArrayList<OootwDto> list = service.getByTempBetween(memnum, temp1, temp2);

		// 리스트의 사이즈만큼 리스트를 dto에 담아서 Integer 배열에 게시글 번호 담기
		Integer[] ootwnums = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++) {
			System.out.println("기온범위로 뽑은 리스트: " + list.get(i));
			OootwDto dto = list.get(i);
			ootwnums[i] = dto.getOotwnum();
			System.out.println(ootwnums[i]);
		}

		// 게시글 번호 배열 크기만큼 대표 옷번호 담을 Integer 배열 생성
		// 게시글이 5개면 각 게시글의 대표사진 5개 담을 배열임
		Integer[] closetNumList = new Integer[ootwnums.length];
		ArrayList<OootwimgsDto> dtoList = new ArrayList<>();

		// 게시글 번호 배열 크기만큼 반복문 돌리면서 각 게시글에 첨부된 이미지의 정보를 dtoList에 담음
		try {
			for (int i = 0; i < ootwnums.length; i++) {
				dtoList = imgservice.getMyImgs(ootwnums[i]);
				// 게시글별 첨부된 이미지리스트의 첫번째 대표 이미지만 배열에 담음
				closetNumList[i] = dtoList.get(0).getClosetnum().getClosetnum();
				System.out.println("각 게시글 대표 옷 번호:" + closetNumList[i]);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		Map map = new HashMap<>();
		map.put("list", list); // ootw 게시글 정보
		map.put("closetNumList", closetNumList); // 각 게시글 대표(index=0) 옷 번호
		return map;
	}

	// 커멘트 like 검색
	@GetMapping("/comments/{comments}/{memnum}")
	public Map getByComments(@PathVariable("comments") String comments, @PathVariable("memnum") int memnum) {
		ArrayList<OootwDto> list = service.getByCommentsLike(comments, memnum);
		// 리스트의 사이즈만큼 리스트를 dto에 담아서 Integer 배열에 게시글 번호 담기
		Integer[] ootwnums = new Integer[list.size()];
		for (int i = 0; i < list.size(); i++) {
			System.out.println("기온범위로 뽑은 리스트: " + list.get(i));
			OootwDto dto = list.get(i);
			ootwnums[i] = dto.getOotwnum();
			System.out.println(ootwnums[i]);
		}

		// 게시글 번호 배열 크기만큼 대표 옷번호 담을 Integer 배열 생성
		// 게시글이 5개면 각 게시글의 대표사진 5개 담을 배열임
		Integer[] closetNumList = new Integer[ootwnums.length];
		ArrayList<OootwimgsDto> dtoList = new ArrayList<>();

		// 게시글 번호 배열 크기만큼 반복문 돌리면서 각 게시글에 첨부된 이미지의 정보를 dtoList에 담음
		try {
			for (int i = 0; i < ootwnums.length; i++) {
				dtoList = imgservice.getMyImgs(ootwnums[i]);
				// 게시글별 첨부된 이미지리스트의 첫번째 대표 이미지만 배열에 담음
				closetNumList[i] = dtoList.get(0).getClosetnum().getClosetnum();
				System.out.println("각 게시글 대표 옷 번호:" + closetNumList[i]);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		Map map = new HashMap<>();
		map.put("list", list); // ootw 게시글 정보
		map.put("closetNumList", closetNumList); // 각 게시글 대표(index=0) 옷 번호
		return map;
	}

	// 게시글 & 게시글 이미지 정보 삭제하기.. DELETE(/ootwnum)
	@DeleteMapping("/{ootwnum}")
	public Map delete(@PathVariable("ootwnum") int ootwnum) {
		boolean flag = true;
		try {
			service.delete(ootwnum); // 이미지 정보 제외한 게시글 정보 삭제
			imgservice.delete(ootwnum); // 게시글에 올린 옷장 이미지 정보(게시글 num으로 연결된 사진정보 전부) 삭제
		} catch (Exception e) {
			flag = false;
		}
		Map map = new HashMap<>();
		map.put("flag", flag);
		return map;
	}
}