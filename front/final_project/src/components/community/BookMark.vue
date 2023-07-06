<template>
  <div class="body">
    <div class="bookmark-box" v-for="(bookmark, index) in filterBmList" :key="index">
      <!-- {{ bookmark.bmnum }} -->
      <!-- <div v-if="memnum == bookmark.memnum.memnum"> -->

      <div class="box1">
        <div class="item-1">
          <img style="margin-right: 8px; border-radius:50%; width: 30px; height: 30px;"
            :src="'http://localhost:8081/members/imgs/' + bookmark.memnum.memnum">
          {{ bookmark.memnum.nickname }}
        </div>
        <!-- <div class="item2">
          <button class="delBtn" @click="delComm(bookmark.bmnum)"><span class="material-symbols-outlined">close</span></button>
        </div> -->
      </div>

      <div class="box2">
        <div class="imgBox">
          <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum + '/' + 1">
          <span v-if="bookmark.img2 != undefined">
            <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum + '/' + 2">
          </span>
          <span v-if="bookmark.img3 != undefined">
            <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum + '/' + 3">
          </span>
        </div>
      </div>
      <!-- box3 Button -->
      <div id="box3">
        <div class="likeBtn"> <!--좋아요 버튼-->
          <button class="markbtn1" @click="pushLike(bookmark.commnum)">
            <span class="material-symbols-outlined"
              :style="{ 'color': bookmark.chklike ? '#f15746' : 'lightslategray' }"> favorite</span>
          </button>
        </div>

        <div class="likeCount">{{ bookmark.btnlike }}명이 좋아합니다.</div>

        <div class="bookBtn"> 북마크 버튼
          <button class="markbtn2" @click="bookcheck(bookmark.commnum)">
            <span class="material-symbols-outlined"
              :style="{ 'color': bookmark.chkbookmark ? 'black' : 'lightslategray' }">bookmark</span>
          </button>
        </div>
      </div>
      <!-- box3 End -->
      <div class="box4">
        <div>{{ bookmark.tag }}</div>
      </div>
      <!-- </div> -->

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
      bmnum: ''
    }
  },
  mounted() {
    this.getBookmarkList(this.getAllMyList);
  },
  methods: {
    getAllMyList() {
      const self = this;
      self.$axios.get(`http://localhost:8081/ocommunity/${self.memnum}`)
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
      self.$axios.get('http://localhost:8081/obookmark/bookmarklist/' + self.memnum)
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
      self.$axios.delete('http://localhost:8081/obookmark/' + bmnum)
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
      self.$axios.patch('http://localhost:8081/olikebtn', form)
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


</script>
<style scoped>
@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}

.body {
  font-family: 'PyeongChang-Regular';
  font-weight: 400;
}

.box1 {
  border: #336399 solid 2px;
  max-width: 48%;
  margin: auto;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}

.delBtn {
  background-color: transparent;
  border: none;
  font-weight: bold;
  color: rgb(9, 9, 137);
}

#box2 {
  border: #336399 solid 2px;
  max-width: 48%;
  margin: auto;
  display: flex;
  justify-content: center;
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
  height: 300px;
}</style>