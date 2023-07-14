<template>

  <!-- 전체리스트 -> munnum이 같은 리스트 -> member 검색(컨트롤러)확인 후 작업 -->
<div id="container">
    <div class="mylist" v-for="(mycommlist, index) in commAllList" :key="index">

      <!-- box1 start -->
      <div id="box1">
        <!-- 프사 & 닉네임 -->
        <div class="item-1">
          <span><img style="margin-left: 5px; border-radius:50%; width: 20px; height: 20px;"
            :src="'http://localhost:7878/members/imgs/' + mycommlist.memnum.memnum"></span>
          <span style="margin-top: 6px; margin-left: 7px; font-size: 0.5em; font-weight: bold; ">
            {{ mycommlist.memnum.nickname }}</span>
        </div>

        <span >
          <button class="item-Btn" @click="delPost(mycommlist.commnum)">
            <span class="material-symbols-outlined" style="color: lightslategray; cursor: pointer;">delete</span>
          </button>
        </span>
      </div>
      <!-- box1 End -->

      <!-- box2 start -->
      <div id="box2">
        <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + mycommlist.commnum + '/' + 1">

        <div v-if="mycommlist.img2 != undefined">
          <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + mycommlist.commnum + '/' + 2">
        </div>

        <div v-if="mycommlist.img3 != undefined">
          <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + mycommlist.commnum + '/' + 3">
        </div>
      </div>
      <!-- box2 End -->

      <!-- box3 start -->
      <div id="box3">
        <div class="likeBtn">
          <button class="markbtn1" @click="pushLike(mycommlist.commnum)">
            <span class="material-symbols-outlined" 
              :style="{'color' : mycommlist.chklike ? '#f15746' : 'lightslategray'}"> favorite</span>
          </button>
        </div>
        <div class="likeCount">{{ mycommlist.btnlike }}명이 좋아합니다.</div>
      </div>
      <!-- box3 End -->

      <!-- box4 start -->
      <div id="box4">
        <div class="box4-item">{{ mycommlist.tag }}</div>
      </div>
      <!-- box4 start -->
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
  self.$axios.get('http://localhost:7878/ocommunity/members/' + self.memnum + "/" + self.memnum)
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
    let check = confirm('정말 삭제하시겠습니까?');
    if(check){
      self.$axios.delete('http://localhost:7878/ocommunity/' + commnum)
      .then(function (res) {
        if (res.status === 200) {
          alert('게시글이 삭제되었습니다.')
          location.reload();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
    } else {
      alert('삭제가 취소되었습니다.')
    }
  },
  pushLike(commnum){
    let self = this;
    let form = new FormData();
    form.append("memnum", self.memnum);
    form.append("commnum", commnum);
    self.$axios.patch('http://localhost:7878/olikebtn',form)
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
@font-face {
font-family: 'PyeongChang-Regular';
src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
font-weight: normal;
font-style: normal;
}

#container {
font-family: 'PyeongChang-Regular';
font-weight: 400;
display: grid;
grid-template-columns: repeat(auto-fill, minmax(25%, auto));
gap: 15px;
}
#box1 {
border-top-left-radius: 5px;
border-top-right-radius: 5px;
background-color: #c1f2ca2a;
max-width: 601px;
margin-top: 8%;
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
.item-Btn {
background-color:transparent;
border: none;
margin-top: 5px;
color: #f15746;
}
#box2 {
max-width: 45%;
margin: auto;
display: flex;
justify-content: center;
}
.img1 {
width: 80px;
height: 120px;
transition: transform 0.3s;
object-fit: cover;
}
.img1:hover {
transform: scale(1.08);  
}
#box3 {
background: #fff;
max-width: 601px;
margin: auto;
display: flex;
flex-direction: row;
}
.markbtn1{
/* margin-top: 3px; */
background-color: transparent;
border: none; 
cursor: pointer;
margin-left: 3px;
}
.likeCount {
margin-top: 6px;
font-size: 0.7em;
font-weight: bold;
}
#box4 {
border-bottom-right-radius: 5px;
border-bottom-left-radius: 5px;
max-width: 601px;
margin: auto;
display: flex;
box-shadow: 16px 16px 16px rgba(195, 192, 192, 0.345);
}
.box4-item {
margin-left: 10px;
margin-top: 8px;
margin-bottom: 12px;
font-size: 0.6em;
font-weight: bold;
}

</style>