package com.example.demo.report;

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
import org.springframework.web.multipart.support.RequestPartServletServerHttpRequest;

import com.example.demo.community.Ocommunity;
import com.example.demo.community.OcommunityService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/oreport")
public class OreportController {

	@Autowired
	private OreportService service;
	@Autowired
	private OcommunityService commservice;

	// 전체목록 검색
	@GetMapping("")
	public Map getAll() {
		ArrayList<OreportDto> list = service.getAll();
		System.out.println("리포트 리스트: " +list);
		Map map = new HashMap();
		map.put("list", list);
		return map;
	}

	// 신고 접수 (추가)
	@PostMapping("")
	public Map save(OreportDto dto) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OreportDto result = service.save(dto);
			map.put("dto", result);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 신고번호로 신고테이블에서 삭제 및 신고된 게시글 삭제 동시에...
	@DeleteMapping("/{repnum}")
	public Map del(@PathVariable("repnum") int repnum) {
		boolean flag = true;
		OreportDto dto = service.getByRepNum(repnum);
		Ocommunity comm = new Ocommunity();
		comm = dto.getCommnum();
		Map map = new HashMap();
		try {
			service.delOreport(repnum);
			commservice.delOcommunity(comm.getCommnum());
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 신고게시글에서 신고된 게시글의 정보를 지움으로서 게시글로 복구함
	@DeleteMapping("/recovery/{repnum}")
	public Map recovery(@PathVariable("repnum") int repnum) {
		boolean flag = true;
		Map map = new HashMap();
		try {
			service.delOreport(repnum);
		} catch (Exception e) {
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
}
