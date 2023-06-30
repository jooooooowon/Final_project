package com.example.demo.likebnt;

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

import com.example.demo.community.OcommunityService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/olikebtn")
public class OlikebtnController {
	@Autowired
	private OlikebtnService service;
	
	@Autowired
	private OcommunityService service2;
	
	// 좋아요 추가
	@PostMapping("/likeplus")
	public Map sava(OlikebtnDto dto) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			service2.upBtn(dto.getCommnum().getCommnum());
			OlikebtnDto result = service.save(dto);
			map.put("dto", result);
		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 좋아요 삭제
	@DeleteMapping("")
	public Map delOlikebtn(int likebtn) {
		Map map = new HashMap<>();
		OlikebtnDto dto = service.getById(likebtn);
		boolean flag = true;
		try {
			service2.downBtn(dto.getCommnum().getCommnum());
			service.delOlikebtn(likebtn);
		} catch(Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	@GetMapping("/{commnum}")
	public Map getCount(@PathVariable("commnum")int commnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			int likeCount = service.likeCount(commnum);
			System.out.println("like count : " + likeCount);
			map.put("likeCount", likeCount);
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		
		map.put("flag", flag);
		return map;
	}
	
		
}
