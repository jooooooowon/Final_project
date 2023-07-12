package com.example.demo.community;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.bookmark.ObookmarkDto;
import com.example.demo.bookmark.ObookmarkService;
import com.example.demo.likebnt.OlikebtnDto;
import com.example.demo.likebnt.OlikebtnService;
import com.example.demo.member.Omember;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/ocommunity")
public class OcommunityController {
	@Autowired
	private OcommunityService service;

	@Autowired
	private OlikebtnService likeservice; // 좋아요 서비스

	@Autowired
	private ObookmarkService bookmarkservice; // 좋아요 서비스

	@Value("${spring.servlet.multipart.location}")
	private String path; // C:/comm/
//	String myPath = path + "comm/"; .. 공용으로 설정된 path는 c:/final/.. 여기에 각자 쓰던 path 뒤에 추가해서 그대로 사용하기
	
	// 로그인 시와 로그아웃 시일 때 모두 일단은 전체 list를 뿌려주는 것이 필요하기 때문에
	// 전체 list를 뿌려주는 메서드를 작성한다.
	// 반복 사용될 예정 (그래봤자 두번 밖에 없지만 ㅇㅅㅇ)
	private ArrayList<OcommunityDto> allList(){
		ArrayList<OcommunityDto> list = service.getAll();
		Collections.sort(list, new Comparator<OcommunityDto>() {
			@Override
			public int compare(OcommunityDto dto1, OcommunityDto dto2) {
				return dto2.getCommnum() - dto1.getCommnum();
			}
		});
		return list;
	}
	
	
	// 게시글 전체목록 검색 (로그인 시)
	@GetMapping("/{memnum}")
	public Map getAll(@PathVariable("memnum") int memnum) {
		// 앞서 만들었던 allList()로 일단 전체 리스트를 받아온다.
		ArrayList<OcommunityDto> list = allList();
		Map map = new HashMap();
		
		// 현재 로그인 되어있는 멤버가 북마크한 list를 가져온다.
		ArrayList<ObookmarkDto> bookmarkList = bookmarkservice.getByMemnum(memnum);
		// 북마크 확인.
		System.out.println(bookmarkList);
		
		// 전체 리스트에서 하나하나 뽑아서
		for(OcommunityDto dto : list) {
			
			// list에서 각 게시물의 좋아요 수를 좋아요 테이블에서 가져온 것을 바탕으로.
			// 각 게시물의 dto에 있는 좋아요 수 변수에 저장한다.
			dto.setBtnlike(likeservice.likeCount(dto.getCommnum()));
			
			// 로그인 되어 있는 멤버가 현재 dto (list를 for문으로 돌리고 있다는 것을 잊지말자.)
			// 에 좋아요를 눌렀는 지 확인한다.
			// 좋아요를 눌렀었다면 chklike를 true로 바꾼다.
			if(likeservice.getByMemnumAndCommnum(memnum, dto.getCommnum()) != null) {
				dto.setChklike(true);
			}
			
			// 북마크도 좋아요와 같은 방식으로 할 수 있지만 더 좋은 방식이 생각나서 새롭게 짜보았다.
			// 내가 저장해논 북마크를 뽑아놓은 bookmarkList를 for문을 돌린다.
			// 현재 list의 dto(게시물)의 번호와 bookmarkList에 있는 게시물의 번호가 같다면
			// 현재 dto는 내가 북마크 해놓은 게시물이기 때문에 
			// 북마크가 되어있다는 것을 보여준다. -> 게시물의 bookmarkchk를 true로 바꾼다.
			for(ObookmarkDto bookmarkDto : bookmarkList) {
				if(bookmarkDto.getCommnum().getCommnum() == dto.getCommnum()) {
					dto.setChkbookmark(true);
				}
			}

			// 끝!
			System.out.println("getAll Dto : " + dto);
		}
		map.put("list", list);
		return map;
	}

	
	
	// 게시글 전체목록 검색 (비 로그인 시)
	@GetMapping("")
	public Map getAll() {
		ArrayList<OcommunityDto> list = allList();	
		Map map = new HashMap<>();
		for(OcommunityDto dto : list) {
			dto.setBtnlike(likeservice.likeCount(dto.getCommnum()));
		}
		map.put("list", list);
		return map;
	}
	
	

	// 커뮤니티 게시글 이미지 추출하기
	@GetMapping("/img/{commnum}/{index}")
	public ResponseEntity<byte[]> read_img(@PathVariable("commnum") int commnum, @PathVariable("index") int index) {
		String fname = "";
		OcommunityDto dto = service.getByCommnum(commnum);
		
//		if(index == 1) {
//			fname = dto.getImg1();
//		} else if(index == 2) {
//			fname = dto.getImg2();
//		} else if(index == 3) {
//			fname = dto.getImg3();
//		}
		
		switch (index) {
		case 1:
			fname = dto.getImg1();
			break;
		case 2:
			fname = dto.getImg2();
			break;
		case 3:
			fname = dto.getImg3();
			break;
		default:
			return null;
		}
		// 응답 객체를 생성해서 반환. 응답 객체는 헤더와 바디. 헤더:목적지주소, 나의주소, 마임타입, 크기...
		// 바디. 전송할 데이터
		File f = new File(fname);
		HttpHeaders header = new HttpHeaders(); // HttpHeaders 객체 생성
		ResponseEntity<byte[]> result = null; // 선언
		try {
			header.add("Content-Type", Files.probeContentType(f.toPath()));// 응답 데이터의 종류를 설정
			// 응답 객체 생성
			result = new ResponseEntity<byte[]>(FileCopyUtils.copyToByteArray(f), header, HttpStatus.OK);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("result:" + result);
		return result;
	}

	// 게시글 번호로 검색
	@GetMapping("/commnum/{commnum}")
	public Map getByTag(@PathVariable("commnum") int commnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			OcommunityDto dto = service.getByCommnum(commnum);
			map.put("dto", dto);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 작성자의 멤버 번호로 검색 => 로그인한 유저의 좋아요 및 북마크를 검색해서 리스트에 업데이트 
	// 태그 검색도 똑같은 로직
	@GetMapping("/members/{memnum}/{loginmemnum}")
	public Map getByMemnum(@PathVariable("memnum") int memnum, @PathVariable("loginmemnum") int loginmemnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<OcommunityDto> list = service.getByMemnum(memnum);
			// 기존에는 로그인 멤버로 게시글 검색 후, 해당 멤버의 좋아요 및 북마크를 검색했었음
			// 하지만 이 멤버 검색은 게시글을 작성한 멤버의 게시글을 검색해서 뿌려주는 것임
			// memnum은 작성자의 memnum이므로 기존대로 아래에서 작성자의 memnum으로 조회하면
			// 작성자의 좋아요 게시글을 조회하는 것이므로 null로 뜸
			// 따라서 위에서는 작성자의 멤버번호로 리스트를 뽑은 후,
			ArrayList<ObookmarkDto> bookmarkList = bookmarkservice.getByMemnum(loginmemnum);
			// 북마크 리스트도 마찬가지로 로그인한 멤버(웹페이지 사용 중인 멤버)의 번호로 조회해야 함
			for(OcommunityDto dto : list) {
				dto.setBtnlike(likeservice.likeCount(dto.getCommnum()));
				
				// 여기서는 로그인한 멤버, 즉 현재 웹페이지를 이용 중인 멤버의 번호로 좋아요를 눌렀는 지 안눌렀는 지 확인해야 함
				// 따라서 파람값으로 loginmemnum을 추가로 받아서 아래에서 조회해야 함
				if(likeservice.getByMemnumAndCommnum(loginmemnum, dto.getCommnum()) != null) {
					dto.setChklike(true);
				}
				
				for(ObookmarkDto bookmarkDto : bookmarkList) {
					if(bookmarkDto.getCommnum().getCommnum() == dto.getCommnum()) {
						dto.setChkbookmark(true);
					}
				}
			}
			map.put("list", list);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}
	
	// 게시글 태그 검색
	@GetMapping("/tags/{tag}/{loginmemnum}")
	public Map getByTag(@PathVariable("tag") String tag, @PathVariable("loginmemnum") int loginmemnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {
			ArrayList<OcommunityDto> tags = service.getByTag(tag);
			ArrayList<ObookmarkDto> bookmarkList = bookmarkservice.getByMemnum(loginmemnum);
			Collections.sort(tags, new Comparator<OcommunityDto>() {
				@Override
				public int compare(OcommunityDto dto1, OcommunityDto dto2) {
					return dto2.getCommnum() - dto1.getCommnum();
				}
			});
			
			for(OcommunityDto dto : tags) {
				dto.setBtnlike(likeservice.likeCount(dto.getCommnum()));
				
				if(likeservice.getByMemnumAndCommnum(loginmemnum, dto.getCommnum()) != null) {
					dto.setChklike(true);
				}
				
				for(ObookmarkDto bookmarkDto : bookmarkList) {
					if(bookmarkDto.getCommnum().getCommnum() == dto.getCommnum()) {
						dto.setChkbookmark(true);
					}
				}
			}
			map.put("tags", tags);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 게시글 생성.
	@PostMapping("")
	public Map save(OcommunityDto dto, MultipartFile[] mfArr) {
		Map map = new HashMap<>();
		System.out.println("mfARr : " + mfArr);
		System.out.println("mfArr.length : " + mfArr.length);
		boolean flag = true;
		try {
			
			// 게시글 이미지들이 들어가있을 폴더
			dto.setImg1("temp");
			OcommunityDto odto = service.save(dto);
			String dirPath = path + odto.getCommnum();
			System.out.println("odto: " + odto);
			File dir = new File(dirPath); // C:/comm/ 게시글번호 / 1, 2, 3
			System.out.println("경로: " + dirPath);
			dir.mkdir();
			
			// 이미지들 폴더에 생성하기.
			String[] imgs = new String[3];
			for (int i = 0; i < mfArr.length; i++) {
				MultipartFile mf = mfArr[i];

				// 배열로 받은 파일들 중 없을 수도 있으니 조건문.
				if (mf != null && !mf.isEmpty()) {
					// 파일 삽입.
					String fname = mf.getOriginalFilename();
					String newpath = dir.getAbsolutePath() + "//" + fname;
					File newFile = new File(newpath);
					try {
						mf.transferTo(newFile);
						imgs[i] = newpath;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 삽입된 파일이 있다면 dto 업데이트 후 다시 저장.
			odto.setImg1(imgs[0]);
			odto.setImg2(imgs[1]);
			odto.setImg3(imgs[2]);
			service.save(odto);

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	// 게시물 삭제하기 ( 신고당했을 때도 이용하기 )
	@DeleteMapping("/{commnum}")
	public Map delOcommunity(@PathVariable("commnum") int commnum) {
		Map map = new HashMap<>();
		boolean flag = true;
		try {

			// commnum의 게시물 뽑아오기.
			OcommunityDto dto = service.getByPost(commnum);

			// 게시물의 img들 뽑아오기.
			String img1 = dto.getImg1();
			String img2 = dto.getImg2();
			String img3 = dto.getImg3();
			// for문을 돌리기 위한 배열 생성
			String[] imgArr = {img1,img2,img3};
			
			for(int i = 0 ; i < 3; i++) {
				// 배열에 들어 있는 img 경로를 담을 임시 String
				// 파일에 직접 접근하기 위해서 만들었다.
				String tempImg = imgArr[i];
				// 파일의 경로가 null일 수도 있으므로 예외처리를 해준다.
				if(tempImg != null) {
					// file을 찾고 삭제한다.
					File delFile = new File(tempImg);
					delFile.delete();
				}
			}
			// 게시물 이미지가 담겨있는 경로를 저장한다.
			String folderPath = path + commnum;
			// File 객체에 경로 담기
			File delFolder = new File(folderPath);
			// 폴더 삭제하기
			delFolder.delete();
			// 게시물 최종 삭제
			service.delOcommunity(commnum);
		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
		}
		map.put("flag", flag);
		return map;
	}

	

}