<template>
	<div id="container">

		<!-- 게시글 등록~검색까지 -->
		<div class="add">
			<!-- 로그인 해야 게시글 등록 버튼 보임 -->
			<a v-if="isLoggedIn" href="/addform"> <!--v-if="isLoggedIn" : 로그인한 아이디-->
				<button type="button" class="addBtn">코디 등록</button>
			</a>
		</div>

		<div class="search-box">
			<input size="40" type="search" v-model="searchTag" placeholder="예) #오피스룩">
			<button style="margin-left: 5px; cursor: pointer;" @click="search(searchTag)">검색</button>
		</div>

		<!-- 사진, 신고, 좋아요,삭제,북마크, 태그 포함할 전체 리스트 -->

		<!-- 게시글 전체 리스트를 보여주는 for 함수 -->
		<div v-for="(comm, i) in commlist" :key="i">
			<!--box1: 아이디,닉네임(올린사람꺼) & 신고(다른사람꺼) & 삭제(자기꺼) -->
			<div id="box1">
				<!-- 프사 & 닉네임-->
				<div class="item-1">
					<span><img style="margin-right: 8px; border-radius:50%; width: 30px; height: 30px;"
							:src="'http://localhost:8081/members/imgs/' + comm.memnum.memnum"></span>
					<span style="margin-top: 8px;">
						<a v-on:click="searchMember(comm.memnum.memnum)"
							style="cursor: pointer; font-size: 0.8em; font-weight: bold;">{{ comm.memnum.nickname }}</a>
					</span>
				</div>

				<div class="item-2">
					<!--신고: 로그인이 되어야 보임(자기꺼 빼고) -->
					<div v-if="isLoggedIn">
						<div>
							<button class="item-Btn" @click="modalOpen(comm.commnum)" v-if="memnum != comm.memnum.memnum">
								<span class="material-symbols-outlined">sms_failed</span>
							</button>
						</div>
					</div>
					<!-- 삭제: 올린사람-->
					<div>
						<button class="item-Btn" v-if="memnum == comm.memnum.memnum" @click="delPost(comm.commnum)">
							<span class="material-symbols-outlined" style="color: lightslategray; cursor: pointer;">delete</span>
						</button>
					</div>
				</div>
			</div>
			<!--box1 End-->

			<!--box2 img-->
			<div id="box2">
				<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 1">

				<div v-if="comm.img2 != undefined">
					<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 2">
				</div>

				<div v-if="comm.img3 != undefined">
					<img class="img1" :src="'http://localhost:8081/ocommunity/img/' + comm.commnum + '/' + 3">
				</div>
			</div>
			<!--box2 End-->

			<!-- box3 Button -->
			<div id="box3">
				<div class="likeBtn"> <!--좋아요 버튼-->
					<button class="markbtn1" @click="pushLike(comm.commnum)">
						<span class="material-symbols-outlined" :style="{ 'color': comm.chklike ? '#f15746' : 'lightslategray' }">
							favorite</span>
					</button>
				</div>

				<div class="likeCount">{{ comm.btnlike }}명이 좋아합니다.</div>

				<div class="bookBtn"> <!--북마크 버튼-->
					<button class="markbtn2" @click="bookcheck(comm.commnum)">
						<span class="material-symbols-outlined"
							:style="{ 'color': comm.chkbookmark ? 'black' : 'lightslategray' }">bookmark</span>
					</button>
				</div>
			</div>
			<!-- box3 End -->

			<!-- box4 Tag -->
			<div id="box4">
				<div class="box4-item" v-for="tag in comm.tagList" :key="tag">
					<div @click="search(tag)" @mouseover="cursorChange($event)"># {{ tag }}</div>
				</div>
			</div>
			<!-- box4 End -->
			<br>
		</div>
	</div>


	<!-- 신고모달창 -->
	<div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
		<div class="modal-container" @click.stop="" id="container">
			<section>
				<!-- 닫기버튼 -->
				<article>
					<button class="close-button" @click="modalClose">
						<span class="material-symbols-outlined">close</span>
					</button>
				</article>
				<!-- 신고이모티콘 -->
				<article class="icon">
					<span style="font-size: 2em;" class="material-symbols-outlined">release_alert</span>
				</article>
				<!-- 신고내용 -->
				<article class="report-content">
					<div class="laber-box">
						<label>
							<input type="radio" v-model="reportContent" value="홍보성/도배성" id="radioChecked" checked>
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
					</div>
				</article>
				<!-- 신고하기버튼 -->
				<div>
					<button style="font-weight: bold;" class="subRep" @click="submitReport">신고하기</button>
				</div>
			</section>
		</div>

	</div>
</template>
  
<script>
export default {

	data() {
		return {
			memnum: sessionStorage.getItem('memnum'),
			commlist: [],
			reportContent: '',
			reportlist: [],
			reportedCommnums: [],
			reportCommnum: '',
			modalCheck: false,
			searchTag: '',
			isList: ''
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
		},
	},
	methods: {
		// 게시글 삭제하는거
		delPost(commnum) {
			const self = this;
			let check = confirm('정말 삭제하시겠습니까?');
			if (check) {
				self.$axios.delete('http://localhost:8081/ocommunity/' + commnum)
					.then(function (res) {
						if (res.status === 200) {
							if (res.data.flag) {
								self.commlist = self.commlist.filter(comm => comm.commnum != commnum);
							}
						} else {
							alert('에러코드: ' + res.status)
						}
					})
			} else {
				alert('삭제가 취소되었습니다.')
			}
		},

		//전체 게시글 list 받는거
		getCommunityList() {
			const self = this;
			let url = '';
			if (self.memnum == undefined) {
				url = 'http://localhost:8081/ocommunity';
			} else {
				url = `http://localhost:8081/ocommunity/${self.memnum}`
			}
			self.$axios.get(url)
				.then((response) => {
					if (response.status === 200) {
							self.commlist = response.data.list;
							console.log(self.commlist)
							for (let i = 0; i < self.reportedCommnums.length; i++) {
								//신고 받은 게시글 안보이게 필터링하는거
								self.commlist = self.commlist.filter(comm => comm.commnum != self.reportedCommnums[i])
							}
					} else {
						alert('에러코드: ' + response.status);
					}
				})
		},
		//tag 검색
		search(tag) {
			let self = this;
			self.searchTag = tag;
			if (self.searchTag == '' || self.searchTag == undefined) {
				this.getCommunityList();
			} else {
				self.$axios.get('http://localhost:8081/ocommunity/tags/' + self.searchTag + "/" + self.memnum)
					.then(res => {
						if (res.status == 200) {
							self.commlist = res.data.tags;
							for (let i = 0; i < self.reportedCommnums.length; i++) {
								//신고 받은 게시글 안보이게 필터링하는거
								self.commlist = self.commlist.filter(comm => comm.commnum != self.reportedCommnums[i])
							}
						} else {
							alert("오류 띠")
						}
					})
			}
		},
		//멤버로 검색
		searchMember(memnum) {
			// const self = this;
            // self.$router.push({ name: 'ListBoard2', query: { memnum: memnum } });
			let self = this;
			self.$axios.get('http://localhost:8081/ocommunity/members/' + memnum + "/" + self.memnum)
				.then(res => {
					if (res.status == 200) {
						self.commlist = res.data.list;
						for (let i = 0; i < self.reportedCommnums.length; i++) {
								//신고 받은 게시글 안보이게 필터링하는거
								self.commlist = self.commlist.filter(comm => comm.commnum != self.reportedCommnums[i])
							}
					} else {
						alert("오류 띠")
					}
				})
		},
		// 신고 리스트 받는거
		getReportList(method) {
			const self = this;
			self.$axios.get('http://localhost:8081/oreport')
				.then(function (res) {
					if (res.status == 200) {
						self.reportlist = res.data.list;
						// console.log(res.data.list)
						// console.log("aaa:" + self.reportlist)
						for (let dto of self.reportlist) {
							// console.log(dto)
							self.reportedCommnums.push(dto.commnum.commnum);
						}
						// console.log("reportlist : " + self.reportedCommnums)
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
			let selectedRadio = document.getElementById("radioChecked");
			selectedRadio.checked = true;
			const self = this;
			self.reportCommnum = commnum;
			self.modalCheck = !self.modalCheck;
			// this.modalClose();
		},
		// 신고 모달 없애기
		modalClose() {
			const self = this;
			self.modalCheck = !self.modalCheck;
			self.reportContent = document.querySelector("input[type=radio]:checked").value;
			self.reportContent = '';
		},
		// 그냥 커서 바꾸는 함수.
		cursorChange(e) {
			console.log("e:" + e);
			e.target.style.cursor = "pointer";
		},

		// 좋아요 + 1 / - 1
		pushLike(commnum) {

			let self = this;
			if (self.memnum == null) {
				alert('로그인 후 이용가능합니다.')
			} else {
				let form = new FormData();
				form.append("memnum", self.memnum);
				form.append("commnum", commnum);
				self.$axios.patch('http://localhost:8081/olikebtn', form)
					.then(res => {
						if (res.status == 200) {
							window.location.reload();
						} else {
							alert(res.data.flag)
							alert("실패")
						}
					})
			}
		},
		//북마크 값 보내는거
		bookcheck(commnum) {
			let self = this;
			if (self.memnum == null) {
				alert('로그인 후 이용가능합니다.')
			} else {

				let formdata = new FormData();
				formdata.append("commnum", commnum);
				formdata.append("memnum", self.memnum);
				self.$axios.put('http://localhost:8081/obookmark', formdata)
					.then(res => {
						if (res.status == 200) {
							window.location.reload();
						} else {
							alert(res.data.flag);
							alert('실패');
						}
					})
			}
		}
	}
}
</script>

<style scoped> 
@font-face {
	font-family: 'PyeongChang-Regular';
	src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
	font-weight: normal;
	font-style: normal;
}

#container {
	font-family: 'PyeongChang-Regular';
	font-weight: 400;
}

hr {
	background: linear-gradient(to left, transparent, #fff, transparent);
}

/* box1 */
#box1 {
	border-top-left-radius: 5px;
	border-top-right-radius: 5px;
	background-color: #c1f2ca2a;
	max-width: 601px;
	margin-top: 2%;
	margin-left: auto;
	margin-right: auto;
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

.item-1 {
	margin-block-start: auto;
	margin-left: 10px;
	margin-top: 6px;
	margin-bottom: 6px;
	display: flex;
}

.item-2 {
	margin-right: 5px;
}

.item-Btn {
	background-color: transparent;
	border: none;
	margin-top: 8px;
	color: #f15746;
}

/* box2(이미지) */
#box2 {
	/* border-bottom: #68a1627e solid 2px; */
	max-width: 45%;
	margin: auto;
	display: flex;
	justify-content: center;
}

.img1 {
	width: 200px;
	height: 300px;
	transition: transform 0.3s;
	object-fit: cover;
}

.img1:hover {
	transform: scale(1.08);
}

/* box3(좋아요&북마크 버튼) */
#box3 {
	/* border-top: #68a1627e solid 2px; */
	/* border-bottom: #68a1627e solid 2px; */
	background: #fff;
	max-width: 601px;
	margin: auto;
	display: flex;
	flex-direction: row;
}

.likeCount {
	margin-top: 8px;
	font-size: 0.7em;
	font-weight: bold;
}

.bookBtn {
	margin-left: 72%;
}

.markbtn1 {
	margin-top: 3px;
	background-color: transparent;
	border: none;
	cursor: pointer;
}

.markbtn2 {
	margin-top: 3px;
	background-color: transparent;
	border: none;
	cursor: pointer;
}

/* box4 Tag */
#box4 {
	border-bottom-right-radius: 5px;
	border-bottom-left-radius: 5px;
	max-width: 601px;
	margin: auto;
	display: flex;
	box-shadow: 16px 16px 16px rgba(195, 192, 192, 0.345);
	/* border-radius: 5	0px; */
}

.box4-item {
	margin-left: 10px;
	margin-top: 8px;
	margin-bottom: 12px;
	font-size: 0.8em;
	font-weight: bold;
}

/* 게시글 등록 */
.add {
	display: flex;
	justify-content: right;
	margin-top: 1%;
	margin-right: 25%;
}

.addBtn {
	background-color: transparent;
	border: none;
	color: #363433;
	font-size: 1em;
	font-weight: bold;
	cursor: pointer;
}

.addBtn:hover {
	color: #f15746;
}

.search-box {
	display: flex;
	justify-content: center;
	margin-bottom: 15px;
}

input:focus {
	outline: 2px solid #f15746;
	/* border-radius: 10px ; */
	border: none;
}

/* modal or popup */
.modal-wrap {
	position: fixed;
	left: 0;
	top: 0;
	width: 100%;
	height: 100%;
	background: rgba(0, 0, 0, 0.4);
}

.modal-container {
	overflow: auto;
	position: relative;
	top: 50%;
	left: 50%;
	transform: translate(-50%, -50%);
	width: 400px;
	height: 300px;
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}

.close-button {
	position: absolute;
	top: 10px;
	right: 20px;
	background-color: transparent;
	border: none;
	cursor: pointer;
}

.icon {
	color: #f15746;
	position: absolute;
	top: 13%;
	left: 48%;
}

.report-content {
	position: absolute;
	top: 32%;
	left: 35%;
}

.laber-box {
	display: flex;
	flex-direction: column;
}

.subRep {
	background-color: transparent;
	border: none;
	color: #f15746;
	position: absolute;
	bottom: 10%;
	right: 20px;
	cursor: pointer;
	font-size: 1em;
  }
</style>
  