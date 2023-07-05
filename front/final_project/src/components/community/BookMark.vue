<template>
  <div class="body">
    
  <div class="bookmark-box" v-for="bookmark in bookmarklist" :key="bookmark.booknum">
    <!-- {{ bookmark.bmnum }} -->
    <div v-if="memnum == bookmark.memnum.memnum">

      <div class="box1">
        <div class="item-1">
          <img style="margin-right: 8px; border-radius:50%; width: 30px; height: 30px;" 
          :src="'http://localhost:8081/members/imgs/' + bookmark.commnum.memnum.memnum">
          {{ bookmark.commnum.memnum.nickname }}
        </div>
        <div class="item2">
          <button class="delBtn" @click="delComm(bookmark.bmnum)"><span class="material-symbols-outlined">close</span></button>
        </div>
      </div>

      <div class="box2">
        <div class="imgBox">
            <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum.commnum + '/' + 1">
            <span v-if="bookmark.commnum.img2 != undefined">
              <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum.commnum + '/' + 2">
            </span>
            <span v-if="bookmark.commnum.img3 != undefined">
              <img class="img1" :src="'http://localhost:8081/ocommunity/img/' + bookmark.commnum.commnum + '/' + 3">
            </span>
        </div>
      </div>
      <div class="box3">
        <div>{{ bookmark.commnum.tag }}</div>
      </div>  
    </div>

  </div>

</div>
</template>
<script>
export default {
  data() {
    return {
      memnum: sessionStorage.getItem('memnum'),
      bookmarklist: [],
      communitylist: [],
      bmnum: '',
      bookmarkCommnum: ''
    }
  },
  mounted() {
    this.getBookmarkList();
    this.getCommunityList();
  },
  methods: {
    //추가된 북마크 리스트
    getBookmarkList() {
      const self = this;
      self.$axios.get('http://localhost:8081/obookmark')
        .then(function (res) {
          if (res.status == 200) {
            self.bookmarklist = res.data.list;
          } else {
            alert('에러코드: ' + res.status)
          }
        })
    },
    getCommunityList() {
      const self = this;
      self.$axios.get('http://localhost:8081/ocommunity')
        .then(function (res) {
          if (res.status == 200) {
            self.communitylist = res.data.list;
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
  color:rgb(9, 9, 137);
}
.img1 {
  width: 200px;
  height: 300px;
}

</style>