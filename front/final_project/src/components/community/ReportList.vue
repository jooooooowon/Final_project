<template>
  <h4>관리자 신고 리스트 페이지</h4> 

  <div>
    <!-- 신고 리스트 -->
    <table border="1">
      <tr>
        <td>신고회원</td>
        <td>내용</td>
        <td>확인</td>
      </tr>
      <tr v-for="report in reportList" :key="report.repnum">
        <td>{{ report.memnum.nickname }}</td>
        <td>{{ report.category }}</td>
        <td><button @click="modalOpen(report.commnum.commnum, report.repnum)">확인</button></td>
      </tr>
    </table>
  </div>

  <!-- 신고 리스트에서 확인 버튼 누르면 보여질 모달 -->
  <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
    <div class="modal-container" @click.stop="" id="container">
      <button v-on:click="delComm(repnum)">삭제</button> <!--게시글&신고리스트 둘다 삭제-->
      <button v-on:click="recComm(repnum)">복구</button> <!--신고리스트에서 삭제되고 게시글로 복구-->
      <button class="close-button" @click="modalClose">X</button><br/>
      <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 1">
      <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 2">
      <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + reportedCommnum + '/' + 3">
        <table border="1">
        <tr>
          <td>닉네임</td>
          <td>태그</td>
        </tr>
        <tr>
          <td>{{ reportedMember }}</td>
          <td>{{ commDto.tag }}</td>
        </tr>
      </table>
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
      self.$axios.get('http://localhost:8081/ocommunity/' + commnum)
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
table {
  margin-left: auto;
  margin-right: auto;
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
</style>