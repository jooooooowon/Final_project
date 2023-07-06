<template>

    <!-- 전체리스트 -> munnum이 같은 리스트 -> member 검색(컨트롤러)확인 후 작업 -->

      <div class="mylist" v-for="(mycommlist, index) in commAllList" :key="index">

        <div id="box1">
          <!-- 프사 & 닉네임 -->
          <div class="item-1">
            <span><img style="margin-right: 8px; border-radius:50%; width: 30px; height: 30px;"
              :src="'http://localhost:8081/members/imgs/' + mycommlist.memnum.memnum"></span>
            <span style="margin-top: 8px;">
              {{ mycommlist.memnum.nickname }}</span>
          </div>

          <span >
            <button class="item-Btn" @click="delPost(mycommlist.commnum)">
              <span class="material-symbols-outlined" style="color: lightslategray; cursor: pointer;">delete</span>
            </button>
          </span>
        </div>

        <div class="imgBox">
          <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + mycommlist.commnum + '/' + 1">
          <span v-if="mycommlist.img2 != undefined">
            <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + mycommlist.commnum + '/' + 2">
          </span>
          <span v-if="mycommlist.img3 != undefined">
            <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + mycommlist.commnum + '/' + 3">
          </span>
        </div>

        <div class="box2">
          <div class="myTag">{{ mycommlist.tag }}</div>
        </div>
        <div class="box3">
          <div class="likeBtn"> <!--좋아요 버튼-->
						<button class="markbtn1" @click="pushLike(mycommlist.commnum)">
							<span class="material-symbols-outlined" :style="{'color' : mycommlist.chklike ? '#f15746' : 'lightslategray'}"> favorite</span>
						</button>
					</div>
          <div class="likeCount">{{ mycommlist.btnlike }}명이 좋아합니다.</div>
        </div>
      </div>

</template>
<script>
export default {
  data() {
    return {
      memnum: sessionStorage.getItem('memnum'),
      commAllList: [],
    }
  },
  created: function () {
    let self = this;
    self.$axios.get('http://localhost:8081/ocommunity/members/' + self.memnum)
      .then(res => {
        if (res.status === 200) {
          self.commAllList = res.data.list;
        } else {
          alert('오류 코드: ' + res.status)
        }
      })
  },
  methods: {
    delPost(commnum) {
			const self = this;
			self.$axios.delete('http://localhost:8081/ocommunity/' + commnum)
      .then(function (res) {
        if (res.status === 200) {
          alert('게시글이 삭제되었습니다.')
          location.reload();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
		},
    pushLike(commnum){
			let self = this;
			let form = new FormData();
			form.append("memnum", self.memnum);
			form.append("commnum", commnum);
			self.$axios.patch('http://localhost:8081/olikebtn',form)
			.then(res =>{
				if(res.status == 200){
					window.location.reload();
				}else{
					alert(res.data.flag)
					alert("실패")
				}
			})
		},
  }
}
</script>
<style scoped>

.backBtn {
  background-color:transparent;
	border: none;
}
.box1 {
  border: #336399 solid 2px;
  max-width: 48%;
	/* margin: auto; */
  margin-top: 2%;
  margin-left: auto;
  margin-right: auto;
	display: flex;
	flex-direction: row;
	justify-content: space-between;
}

.name-box {
	margin-block-start: auto;
	margin-left: 10px;
	margin-top: 6px;
	margin-bottom: 6px;
	display: flex;
}
.item-Btn {
	background-color:transparent;
	border: none;
	font-family: sans-serif;
	margin-top: 8px;
}
.imgBox {
	border: #336399 solid 2px;
	max-width: 48%;
	margin: auto;
	display: flex;
	justify-content: center;
}
.img1 {
  width: 200px;
  height: 250px;
}
.box2 {
  border: #336399 solid 2px;
	max-width: 48%;
	margin: auto;
	display: flex;
}
.myTag {
  margin-left: 10px;
	margin-top: 12px;
	margin-bottom: 12px;
}

.box3 {
  border: #336399 solid 2px;
	max-width: 48%;
	margin: auto;
	display: flex;
	flex-direction: row;
}
.likeCount {
	margin-top: 8px;
}
.markbtn1{
	margin-top: 3px;
	background-color: transparent;
	border: none; 
	cursor: pointer;
}
</style>