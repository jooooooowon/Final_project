<template>
	<!-- <span v-if="modalon=false">
			<ReportForm @close="modalon = false" />
		</span> -->

	<!-- 게시글 등록~검색까지 -->
	<div>
		<div class="add">
			<!-- 로그인 해야 게시글 등록 버튼 보임 -->
			<a v-if="isLoggedIn" href="/addform"> <!--v-if="isLoggedIn" : 로그인한 아이디-->
				<button type="button" class="btn btn-light">게시글 등록</button>
			</a>
		</div>
		<hr />
		<div>
			<p>
				<input type="search" v-model="searchTag" placeholder="예) #오피스룩 #패피">
				<button style="margin-left: 5px;" @click="search(searchTag)">검색</button>
			</p>
		</div>
	</div>

	<!-- 사진, 신고, 좋아요,삭제,북마크, 태그 포함할 전체 리스트 -->
	<div>

		<h3>전체 리스트</h3>
		<hr />
		<div v-for="(comm, i) in commlist" :key="i">
				<div v-if="isLoggedIn">
					<button @click="modalOpen(comm.commnum)" v-if="memnum != comm.memnum.memnum">신고하기</button>
				</div>
				<div>
				회원번호: {{ comm.memnum.memnum }} <!--아이디랑 프로필 사진 가져와야함-->
				게시글 번호: {{ comm.commnum }}
			</div>
			<div>
				<button v-if="memnum == comm.memnum.memnum" @click="delPost(comm.commnum)">삭제</button>
			</div>
			<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 1">
			<span v-if="comm.img2 != undefined">
				<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 2">
			</span>
			<span v-if="comm.img3 != undefined">
				<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 3">
			</span>

			<br />
			{{ i }}
			<div class="markcontainer">
				<span> <!--좋아요 버튼-->
					<button class="markbtn" @click="pushLike(comm.commnum)"><span class="material-symbols-outlined" :style="{'color' : comm.chklike ? 'red' : 'gray'}"> favorite</span></button>
					{{ comm.btnlike }}명이 좋아합니다. {{ comm.chklike }}</span>
				<span> <!--북마크 버튼-->
					<button class="markbtn" @click="bookcheck(comm.commnum)"><span class="material-symbols-outlined" :style="{'color' : comm.chkbookmark ? 'yellow' : 'gray'}">bookmark</span></button>
				</span>
			</div>
			{{ comm.tagList.length }}
			<span v-for="tag in comm.tagList" :key="tag">
				<span @click="search(tag)" @mouseover="cursorChange($event)"># {{ tag }}</span>  
			</span>
			<hr />
			</div>
	</div>


	<div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
		<div class="modal-container" @click.stop="" id="container">
			<button class="close-button" @click="modalClose">X</button>
			<img src="@/assets/reporticon.png">
			<br />
			<label>
				<input type="radio" v-model="reportContent" value="홍보성/도배성">
				홍보성/도배성
			</label>
			<br />
			<label>
				<input type="radio" v-model="reportContent" value="스팸">
				스팸
			</label>
			<br />
			<label>
				<input type="radio" v-model="reportContent" value="음란성">
				음란성
			</label>
			<br />
			<label>
				<input type="radio" v-model="reportContent" value="기타">
				기타
			</label>
			<br />
			<button class="btn btn-light" @click="submitReport">신고하기</button>

		</div>
	</div>
</template>
  
<script>
// import ReportForm from '@/components/community/ReportForm.vue'

export default {

	// components: {
	// 	ReportForm
	// },

	data() {
		return {
			commlist: [],
			reportContent: '',
			reportlist: [],
			reportedCommnums: [],
			reportCommnum: '',
			memnum: sessionStorage.getItem('memnum'),
			modalCheck: false,
			searchTag : '',
		}
	},

	//게시글 리스트 띄우는 함수
	mounted() {
		this.getReportList(this.getCommunityList); // ()는 메서드 실행.. 그냥 넣는 건 변수를 넣는 것
	},

	// 로그인 상태를 확인하는 computed 속성
	computed: {
		isLoggedIn() {
			return sessionStorage.getItem('memnum') !== null;
		}
	},
	methods: {
		// 게시글 삭제하는거
		delPost(commnum) {
			const self = this;
			self.$axios.delete('http://localhost:8081/ocommunity/' + commnum)
				.then(function (res) {
					if (res.status === 200) {
						if (res.data.flag) {
							self.commlist = self.commlist.filter(comm => comm.commnum != commnum);
						}
					} else {
						alert("오류")
					}
				})
		},
		//전체 게시글 list 받는거
		getCommunityList() { 
			const self = this;
			let url = '';
			if(self.memnum == undefined){
				url = 'http://localhost:8081/ocommunity';
			}else{
				url = `http://localhost:8081/ocommunity/${self.memnum}`
			}
			self.$axios.get(url)
			.then((response) => {
				if (response.status === 200) {
					self.commlist = response.data.list
					console.log(self.commlist)
					for(let i=0; i<self.reportedCommnums.length; i++){
						self.commlist = self.commlist.filter(comm => comm.commnum != self.reportedCommnums[i])
					}
				} else {
					alert('에러코드: ' + response.status);
				}
			})
		},search(tag){
			let self = this;
			self.searchTag = tag;
			if(self.searchTag == '' || self.searchTag == undefined){
				this.getCommunityList();
			}else{

				self.$axios.get(`http://localhost:8081/ocommunity/tags/${self.searchTag}`)
				.then(res =>{
					if(res.status == 200){
						self.commlist = res.data.tags;
					}else{
						alert("오류 띠")
					}
				})
			}
		},
		// 신고 리스트 받는거
		getReportList(method) {
			const self = this;
			self.$axios.get('http://localhost:8081/oreport')
				.then(function (res) {
					if (res.status == 200) {
						self.reportlist = res.data.list;
						console.log(res.data.list)
						console.log("aaa:" + self.reportlist)
						for (let dto of self.reportlist) {
							console.log(dto)
							self.reportedCommnums.push(dto.commnum.commnum);
						}
						console.log("reportlist : " + self.reportedCommnums)
						method();
					} else {
						alert('에러코드: ' + res.status);
					}
				})
		},
		// 신고 값 보내는거
		submitReport() {
			const self = this;
			let check = confirm('정말 신고하시겠습니까?');
			let formdata = new FormData();
			if (check) {
				self.reportContent = document.querySelector("input[type=radio]:checked").value;
				// memnum, commnum, reportcontent
				formdata.append('memnum', self.memnum);
				formdata.append('commnum', self.reportCommnum);
				formdata.append('category', self.reportContent);
				self.$axios.post('http://localhost:8081/oreport', formdata)
					.then(function (res) {
						if (res.status == 200) {
							alert('신고가 완료되었습니다.')
							location.reload();
						} else {
							alert('에러코드: ' + res.status)
						}
					})
			} else {
				alert('신고가 취소되었습니다.')
			}
		},
		// 신고 모달 띄우기
		modalOpen(commnum) {
			const self = this;
			self.reportCommnum = commnum;
			self.modalCheck = !self.modalCheck;
			// this.modalClose();
		},
		// 신고 모달 없애기
		modalClose() {
			const self = this;
			self.modalCheck = !self.modalCheck;
		},
		// 그냥 커서 바꾸는 함수.
		cursorChange(e){
			console.log("e:" + e);
			e.target.style.cursor = "pointer";
		},
		//북마크 추가(push->마이페이지에서 리스트 보여줘야함)
		// bookcheck() {
      
    // },
		
		// 좋아요 + 1 / - 1
		pushLike(commnum){
			let self = this;
			let form = new FormData();
			form.append("memnum", self.memnum);
			form.append("commnum", commnum);
			self.$axios.patch('http://localhost:8081/olikebtn',form)
			.then(res =>{
				if(res.status == 200){
					alert(res.data.flag)
					alert("성공")
					window.location.reload();
				}else{
					alert(res.data.flag)
					alert("실패")
				}
			})
		},
		bookcheck(commnum){
			let self = this;
			let form = new FormData();
			form.append("commnum",commnum);
			form.append("memnum",self.memnum);

			self.$axios.put('http://localhost:8081/obookmark',form)
			.then(res => {
				if(res.status == 200){
					alert(res.data.flag);
					alert('성공');
					window.location.reload();
				}else{
					alert(res.data.flag);
					alert('실패');
				}
			})
		}
		
		
		
	}
}
</script>
  
<style scoped>


.add {
	margin-left: 60%;
}

.search {
	padding-top: 5px;
}

hr {
	width: 70%;
	margin-left: auto;
	margin-right: auto;
	border: solid 3px #336399;
}

.img1 {
	width: 200px;
	height: 300px;
}



.modal-wrap {
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}


/* modal or popup */
.modal-container {
	overflow: auto;
	position: relative;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 800px;
	height: 80%;
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}

.close-button {
	position: absolute;
	right: 60px;
	top: 50px;
	background-color: transparent;
	border: none;
}

.markcontainer{
	width: 60%;
	display: flex;
	justify-content: space-around;	
	margin-left: 20%;

}
.markbtn{
	background-color: transparent;
	border: none; 
}
</style>
  