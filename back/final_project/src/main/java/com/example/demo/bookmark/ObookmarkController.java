package com.example.demo.bookmark;

import java.util.ArrayList;
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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/obookmark")
public class ObookmarkController {

	@Autowired
	private ObookmarkService service;

	//전체목록 검색
	@GetMapping("")
	public Map getAll() {
		ArrayList<ObookmarkDto> list = service.getAll();
		Map map = new HashMap();
		map.put("list", list);
		return map;
	}
	//회원번호로 북마크 리스트 검색
	@GetMapping("/{memnum}")
	public Map getByMemnum(@PathVariable("memnum") int memnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<ObookmarkDto> list = service.getByMemnum(memnum);
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	//게시글 번호로 북마크 리스트 검색
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
			ObookmarkDto result = service.getByMemnumAndCommnum(dto.getMemnum().getMemnum(),dto.getCommnum().getCommnum());
			// 북마크가 되어 있는 지 확인하고
			// ( 회원 넘버와 게시판 넘버로 추출한다. )
			if(result == null) {
				// 없으면 넣고
				service.save(dto);
			}else {
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
