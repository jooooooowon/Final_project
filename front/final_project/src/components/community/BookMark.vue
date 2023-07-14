<template>

  <div id="container">
    <div class="bookmark-box" v-for="(bookmark, index) in filterBmList" :key="index">
      <!-- {{ bookmark.bmnum }} -->
      <!-- <div v-if="memnum == bookmark.memnum.memnum"> -->
      <!-- box1 start -->
      <div id="box1">
        <!-- 프사 & 닉네임 -->
        <div class="item-1">
          <span><img style="margin-right: 5px; border-radius:50%; width: 20px; height: 20px;"
            :src="'http://localhost:7878/members/imgs/' + bookmark.memnum.memnum"></span>
          <span style="margin-top: 6px; margin-left: 5px; font-size: 0.5em; font-weight: bold;" >
          {{ bookmark.memnum.nickname }}</span>
        </div>
        <!-- <div class="item2">
          <button class="delBtn" @click="delComm(bookmark.bmnum)"><span class="material-symbols-outlined">close</span></button>
        </div> -->
      </div>
      <!-- box1 End -->

      <!-- box2 start -->
      <div id="box2">
        <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + bookmark.commnum + '/' + 1">

        <div v-if="bookmark.img2 != undefined">
          <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + bookmark.commnum + '/' + 2">
        </div>

        <div v-if="bookmark.img3 != undefined">
          <img class="img1" :src="'http://localhost:7878/ocommunity/img/' + bookmark.commnum + '/' + 3">
        </div>
      </div>
      <!-- box2 End -->

      <!-- box3 start -->
      <div id="box3">
        <div class="likeBtn">
          <button class="markbtn1" @click="pushLike(bookmark.commnum)">
            <span class="material-symbols-outlined"
              :style="{ 'color': bookmark.chklike ? '#f15746' : 'lightslategray' }"> favorite</span>
          </button>
        </div>
        <div class="likeCount">{{ bookmark.btnlike }}명이 좋아합니다.</div>

        <div class="bookBtn">
          <button class="markbtn2" @click="bookcheck(bookmark.commnum)">
            <span class="material-symbols-outlined"
              :style="{ 'color': bookmark.chkbookmark ? 'black' : 'lightslategray' }">bookmark</span>
          </button>
        </div>
      </div>
      <!-- box3 End -->

      <!-- box4 start -->
      <div id="box4">
        <div class="box4-item">{{ bookmark.tag }}</div>
      </div>
      <!-- box4 End -->

    </div>
  </div>

</template>

<script>
export default {
data() {
  return {
    memnum: sessionStorage.getItem('memnum'),
    bookmarklist: [],
    allList: [],
    filterBmList: [],
    bookmarkedCommnums: [],
    bmnum: '',
  }
},
mounted() {
  this.getBookmarkList(this.getAllMyList);
},
methods: {
  getAllMyList() {
    const self = this;
    self.$axios.get(`http://localhost:7878/ocommunity/${self.memnum}`)
      .then(res => {
        if (res.status === 200) {
          self.allList = res.data.list;
          let bookmarklist = [];
          for (let i = 0; i < self.bookmarkedCommnums.length; i++) {
            bookmarklist[i] = self.allList.find(bookmark => bookmark.commnum == self.bookmarkedCommnums[i])
            self.filterBmList.push(bookmarklist[i]);
          }
        } else {
          alert('오류 코드: ' + res.status)
        }
      })
  },
  //추가된 북마크 리스트
  getBookmarkList(method) {
    const self = this;
    self.$axios.get('http://localhost:7878/obookmark/bookmarklist/' + self.memnum)
      .then(function (res) {
        if (res.status == 200) {
          self.bookmarklist = res.data.list;
          for (let dto of self.bookmarklist) {
            self.bookmarkedCommnums.push(dto.commnum.commnum);
          }
          method();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
  },
  delComm(bmnum) {
    const self = this;
    self.$axios.delete('http://localhost:7878/obookmark/' + bmnum)
      .then(function (res) {
        if (res.status == 200) {
          alert('북마크가 해제되었습니다.')
          location.reload();
        } else {
          alert('에러코드: ' + res.status)
        }
      })
  },
  // 좋아요 + 1 / - 1
  pushLike(commnum) {
    let self = this;
    let form = new FormData();
    form.append("memnum", self.memnum);
    form.append("commnum", commnum);
    self.$axios.patch('http://localhost:7878/olikebtn', form)
      .then(res => {
        if (res.status == 200) {
          window.location.reload();
        } else {
          alert(res.data.flag)
          alert("실패")
        }
      })
  },
  //북마크 값 보내는거
  bookcheck(commnum) {
    let self = this;
    let formdata = new FormData();
    formdata.append("commnum", commnum);
    formdata.append("memnum", self.memnum);
    self.$axios.put('http://localhost:7878/obookmark', formdata)
    .then(res => {
      if (res.status == 200) {
        window.location.reload();
      } else {
        alert(res.data.flag);
        alert('실패');
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
margin-left: 6px;
}
.likeCount {
margin-top: 8px;
font-size: 0.6em;
font-weight: bold;
}
.bookBtn {
margin-left: 90px;
}
.markbtn2{
margin-top: 3px;
background-color: transparent;
border: none; 
cursor: pointer;
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
margin-top: 3px;
margin-bottom: 12px;
font-size: 0.6em;
font-weight: bold;
}
</style>