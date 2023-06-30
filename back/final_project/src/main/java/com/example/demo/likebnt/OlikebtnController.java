package com.example.demo.likebnt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.community.Ocommunity;
import com.example.demo.community.OcommunityDto;
import com.example.demo.community.OcommunityService;
import com.example.demo.member.Omember;
import com.example.demo.member.OmemberDto;
import com.example.demo.member.OmemberService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/olikebtn")
public class OlikebtnController {
	@Autowired
	private OlikebtnService service;
	
	@Autowired
	private OcommunityService service2;
	
	@Autowired
	private OmemberService memService;
	
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
	
	// 좋아요 부분 수정
	@PatchMapping("")
	public Map likeUpAndDown(int memnum, int commnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		OlikebtnDto dto = service.getByMemnumAndCommnum(memnum, commnum);
		if (dto == null) { // 좋아요 안누름
			OmemberDto memberDto = memService.getByMemnum(memnum);
			OcommunityDto communityDto = service2.getByCommnum(commnum);
			
			// 좋아요 안눌러져있으니 데이터 베이스에 없다.
			// likebtn entity는 dto가 아니라 vo들을 담고 있으니 다시 만들어서 해줘야된다.
			// 옘병띠
			Omember member = new Omember();
			member.setEmail(memberDto.getEmail());
			member.setGender(memberDto.getGender());
			member.setId(memberDto.getId());
			member.setMemnum(memberDto.getMemnum());
			member.setImg(memberDto.getImg());
			member.setNickname(memberDto.getNickname());
			member.setPwd(memberDto.getPwd());
			
			// 이하 동문
			Ocommunity community = new Ocommunity();
			community.setBtnlike(communityDto.getBtnlike());
			community.setCommnum(communityDto.getCommnum());
			community.setImg1(communityDto.getImg1());
			community.setImg2(communityDto.getImg2());
			community.setImg3(communityDto.getImg3());
			community.setMemnum(communityDto.getMemnum());
			community.setTag(communityDto.getTag());
			
			dto = new OlikebtnDto();
			dto.setCommnum(community);
			dto.setMemnum(member);
		
			service.save(dto);
		} else { // 좋아요 누름
			service.delOlikebtn(dto.getLikebtn());
			flag = false; // 좋아요 눌려있으면 false 보내서
		}
		map.put("flag", flag);
		return map;
	}
	
	
	// 좋아요 눌렀는 지 확인 빠끄
	@GetMapping("/{commnum}/{memnum}")
	public Map chkLikeBtn(@PathVariable("commnum") int commnum, @PathVariable("memnum") int memnum) {
		
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OlikebtnDto dto = service.getByMemnumAndCommnum(memnum, commnum);
			boolean chk = true;
			if(dto == null) {
				chk = false;
			}	
			map.put("chk", chk);
		}catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
		
}
