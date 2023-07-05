<template>

  <!-- 신고 리스트 -->
  
  <div class="box1">
    <span class="box1-item">신고자</span>
    <span class="box1-item">신고 내용</span>
    <span class="box1-item">확인 사항</span>
  </div>
  
      <div class="reportBox" v-for="report in reportList" :key="report.repnum">
        <div>{{ report.memnum.nickname }}</div>
        <div>{{ report.category }}</div>
        <div><button class="checkBtn" @click="modalOpen(report.commnum.commnum, report.repnum)">확인</button></div>
      </div> 


  <!-- 신고 리스트에서 확인 버튼 누르면 보여질 모달 -->
  <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
    <div class="modal-container" @click.stop="" id="container">
      <div class="close">
        <button class="checkBtn" @click="modalClose"><span class="material-symbols-outlined">close</span></button>
      </div>
      <div class="icon"><span style="color: #f15746" class="material-symbols-outlined">release_alert</span></div>
      <div class="content1">
        <div>Nickname: {{ reportedMember }}</div>
      </div>
      <div class="imgBox">
        <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 1">
        <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 2">
        <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 3">
      </div>
      <div class="content2">
        <div>{{ commDto.tag }}</div>
      </div>
      <div class="repBtn">
        <button class="repBtn-item1" v-on:click="delComm(repnum)">삭제</button> <!--게시글&신고리스트 둘다 삭제-->
        <button class="repBtn-item2" v-on:click="recComm(repnum)">복구</button> <!--신고리스트에서 삭제되고 게시글로 복구-->
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      reportList: [],
      communityList: [],
      commDto: '',
      reportedCommnum: '',
      repnum: '',
      reportedMember: '',
      reportedCommTag: '',
      modalCheck: false
    };
  },
  mounted() {
    this.getReportList();
    this.getCommunityList();
  },
  methods: {
    getReportList(){
      const self = this;
    self.$axios.get('http://localhost:8081/oreport')
      .then(function (res) {
        if (res.status == 200) {
          self.reportList = res.data.list;
        } else {
          alert('에러코드: ' + res.status)
        }
      })
    },
    getCommunityList(){
      const self = this;
    self.$axios.get('http://localhost:8081/ocommunity')
      .then(function (res) {
        if (res.status == 200) {
          self.communityList = res.data.list;
        } else {
          alert('에러코드: ' + res.status)
        }
      })
    },
    delComm(repnum) {
      const self = this;
      self.$axios.delete('http://localhost:8081/oreport/' + repnum)
      .then(function(res) {
        if(res.status == 200) {
          alert('게시글 삭제가 완료되었습니다.')
          location.reload();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
    },
    recComm(repnum) {
      const self = this;
      self.$axios.delete('http://localhost:8081/oreport/recovery/' + repnum)
      .then(function(res) {
        if(res.status == 200) {
          alert('게시글 복구가 완료되었습니다.')
          location.reload();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
    },
    modalOpen(commnum, repnum) {
			const self = this;
			self.reportedCommnum = commnum;
      self.repnum = repnum;
			self.modalCheck = !self.modalCheck;
      self.$axios.get('http://localhost:8081/ocommunity/commnum/' + commnum)
      .then(function(res) {
        if(res.status == 200) {
          self.commDto = res.data.dto;
          self.reportedMember = self.commDto.memnum.nickname
          self.reportedCommTag = self.commDto.tag
        } else {
          alert('에러코드: ' + res.status)
        }
      });
		},
		modalClose() {
			const self = this;
			self.modalCheck = !self.modalCheck;
		},
  }
}
</script>

<style scoped>
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
	width: 48%;
	height: 70%;
	background: #fff;
	border-radius: 10px;
	padding: 20px;
	box-sizing: border-box;
}
.reportBox {
  max-width: 50%;
  margin: auto;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
}

.box1 {
  max-width: 50%;
  margin-left: 24.5%;
  margin-bottom: 15px;
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  border-bottom: 2px solid darkorange
}
.box1-item {
  margin-bottom: 5px;
}

.checkBtn {
  background-color: transparent;
	border: none; 
  font-weight: bold;
  color: rgb(9, 9, 137);
}
.close {
position: absolute;
top: 10px;
right: 10px;
}

.imgBox {
  position: absolute;
  top: 12%;
  left: 20px;
  right: 20px;
}
.img1 {
  width: 220px;
  height: 330px;
}
.icon {
  position: absolute;
  top: 6%;
  left: 5%;
}
.content1 {
  position: absolute;
  top: 6%;
  left: 9%;
  font-size: 1em;
  font-weight: bold;
}
.content2 {
  position: absolute;
  bottom: 15%;
  left: 5%;
  font-size: 1em;
  font-weight: bold;
}
.repBtn {
  position: absolute;
  bottom: 15%;
  right: 6%;
}
.repBtn-item1 {
  background-color: transparent;
	border: none; 
  font-weight: bold;
  color:rgb(9, 9, 137);
  border-right: solid black 2px ;
}
.repBtn-item2 {
  background-color: transparent;
	border: none; 
  font-weight: bold;
  color:rgb(9, 9, 137);
}
</style>