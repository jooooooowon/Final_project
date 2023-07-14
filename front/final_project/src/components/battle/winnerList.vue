<template>
  <div v-if="list.length == 0">
    <notYet></notYet>
  </div>
  <div v-else>
    <div style="display :flex">
      <div class="main-winner-list">
        <div class="body-div">
          <div class="container">
            <div class="bubbles">
              <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
              <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
              <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
            </div>
          </div>
        </div>
        <div class="title">명예의 전당</div>
        <div class="winner-list">
          <ul v-for="(winner, i) in list" :key="i">
            <li class="list-winner" v-if="winner.theme != '이번주 테마는?'">
              <div class="card">
                <div class="round-cnt">
                  ROUNDNUM <span style="font-weight: bold; font-size: 16px; color:#ebebeb">#{{ winner.roundcnt }}</span>
                </div>
                <div class="nickname">
                  {{ nickname[i] }}
                </div>
                <div class="winner-content">
                  <div class="theme">
                    <span class="small-title">테마</span> <br><br>
                    <span class="small-content">
                      {{ winner.theme }}
                    </span>
                  </div>
                  <div class="bar"></div>
                  <div class="gender">
                    <span class="small-title">성별</span> <br><br>
                    <span class="small-content">
                      {{ winner.gender }}
                    </span>
                  </div>
                </div>
                <div class="imgPosition">
                  <img :src="'http://localhost:7878/battles/imgs/' + winner.batnum" alt="이미지 불러오기 실패">
                </div>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </div>

    <div class="question" @click="modalOpen">
      <img src="@/assets/question.svg" alt="battle explanation">
    </div>
    <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
      <div class="modal-container" @click.stop="" id="container">
        <h6>배틀이란?</h6>
        <div class="modal-com">
          <p>주마다 바뀌는 테마에 맞춰 자신의 개성을 표현하세요!</p>
        </div>
        <div style="width: 600px;">
          <img src="@/assets/battleImg.png">
        </div>
        <div class="modal-info">
          <p>테마 발표 : 금 10am</p>
          <p>신청 기간 : 금 10am ~ 일 10pm</p>
          <p>투표 기간 : 월 10am ~ 수 10pm</p>
          <p>우승 발표 : 목 10am</p>
        </div>
        <div class="modal-comments">
          <p>1. 매 주 금요일 10시에 올라오는 테마에 맞는 개성 넘치는 룩을 신청하세요.</p>
          <p>2. 랜덤으로 선정된 두가지 룩 중 테마와 가장 잘 어울리는 룩을 투표하세요.</p>
          <p>3. 배틀에서 우승한 도전자는 명예의 전당에 오를 수 있습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>
  
<script>
import notYet from './notYet.vue'

export default {
  name: 'winnerList',
  data() {
    return {
      dto: {},
      list: [1],
      nickname: [],
      items: [
        11, 12, 24, 10, 14, 23, 18, 16, 19, 20, 22, 25, 18, 21, 15, 13, 26, 17, 13, 28, 10, 25, 24, 18
      ],
      modalCheck: false,
    }
  },
  components: {
    notYet: notYet
  },
  created: function () {
    this.winnerList();
  },
  methods: {
    winnerList() {
      let self = this;
      self.$axios.get('http://localhost:7878/battles/winnerlist')
        .then(res => {
          if (res.status == 200) {
            self.list = res.data.list;
            for (let dto of self.list) {
              console.log(dto.memnum.nickname);
              this.nickname.push(dto.memnum.nickname);
            }
            let container = document.querySelector('.container');
            // container.style.height = Math.trunc(((self.list.length + 2) / 3)) * 100  + 'vh';
            container.style.setProperty("--h-name", Math.trunc(((self.list.length + 2) / 3)) * 100 + 'vh');
            console.log(self.list.length);
            console.log("hname" + container.style.getPropertyValue("--h-name"));
            console.log(container.style.getPropertyValue("height"));
            console.log(this.list);
          } else {
            alert("오류로 인해 명예의 전당 활성화 불가")
          }
        });
    },
    modalOpen() {
      this.modalCheck = !this.modalCheck;
    },
    modalClose() {
      this.modalCheck = !this.modalCheck;
    }
  }
}
</script>
  
<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Diphylleia&display=swap');

@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
}

@font-face {
  font-family: 'PyeongChang-Bold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Bold.woff2') format('woff2');
  font-weight: 700;
  font-style: normal;
}

.body-div {
  position: absolute;
}

.container {
  position: relative;
  width: 100%;
  overflow: hidden;
  --h-name: 100vh;
  height: 100vh;
}

.bubbles {
  position: relative;
  display: flex;
}

.bubbles span {
  position: relative;
  width: 30px;
  height: 30px;
  background-color: #85b380;
  margin: 0 4px;
  border-radius: 50%;
  box-shadow: 0 0 0 10px #85b38044,
    0 0 50px #85b380,
    0 0 100px #85b380;
  animation: animate 15s ease-in-out infinite;
  animation-duration: calc(125s / var(--i));
}

@keyframes animate {
  0% {
    transform: translateY(var(--h-name)) scale(0);
  }

  100% {
    transform: translateY(-10vh) scale(.3);
  }

}


/* =========================================== */

.main-winner-list {
  position: absolute;
  left: 0;
  right: 0;
  margin: auto;
}

.winnerList {
  width: 100%;
  padding: auto;
}

.title {
  font-family: 'Black Han Sans', sans-serif;
  font-size: 50px;
  height: 150px;
  line-height: 150PX;
  color: #68a162;
  left: 0;
  right: 0;
  text-align: center;
}

.winner-list {
  left: 0;
  right: 0;
  margin: auto;
  width: 1000px;
}


ul {
  float: left;
}

li {
  margin-left: 30px;
  margin-bottom: 50px;
}

.card {
  width: 200px;
  padding: 20px 50px;
  margin: auto;
  left: 0;
  right: 0;
  position: relative;
  box-shadow: 0 10px 30px rgba(0, 0, 0, .25);
  animation-name: winner-list;
  animation-duration: 3s;
}

@keyframes winner-list {
  0% {
    opacity: 0;
  }

  100% {
    opacity: 1;
  }
}

.round-cnt {
  background-color: #68a162;
  width: 120px;
  margin-top: 20px;
  border-radius: 5px;
  font-size: 14px;
}

.nickname {
  font-family: 'Black Han Sans', sans-serif;
  font-size: 30px;
  text-align: left;
  margin-top: 14px;
  color: #68a162;
}

.winner-content {
  display: flex;
  justify-content: space-around;
  margin-top: 14px;
}

.small-title {
  font-weight: bold;
  font-size: 20px;
}

.small-content {
  color: #68a162;
  font-weight: bold;
}

.bar {
  width: 1px;
  height: 50px;
  background-color: black;
}

.imgPosition {
  display: absolute;
  left: 0;
  right: 0;
  margin: 25px auto;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  overflow: hidden;
}

img {
  width: 100%;
  height: 100%;
}

/* 물음표 아이콘 */

.question img {
  position: fixed;
  right: 0;
  bottom: 0;
  margin: 70px;
  z-index: 1;
  overflow: hidden;
  box-shadow: 0 10px 30px rgba(0, 0, 0, .45);
  transition: .5s;
  border-radius: 50%;
  width: 80px;
  height: 80px;
}

.question:hover img {
  bottom: 15px;
}

.modal-wrap {
  z-index: 999;
  /* 모달의 배경을 카드보다 상위로 올립니다. */
  position: fixed;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.4);
}


/* modal */
.modal-container {
  z-index: 9999;
  /* 모달창을 다른 요소들보다 상위로 올림 */
  overflow: auto;
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  max-width: 750px;
  height: auto;
  background: #fff;
  color: #2c3e50;
  box-shadow: 0 20px 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
  /* padding: 20px; */
  box-sizing: border-box;
  /* 가운데정렬 */
  display: flex;
  flex-direction: column;
  /* 내용 수직으로 배치 */
  /* justify-content: center; */
  /* 수직방향 가운데 정렬 */
  align-items: center;
  /* 수평방향 가운데 정렬 */
}

.modal-container h6 {
  font-family: 'PyeongChang-Bold';
  margin-top: 50px;
  /* margin-bottom: 10px; */
  /* font-weight: bold; */
  font-size: 20px;
}

.modal-com {
  font-family: 'PyeongChang-Regular';
  width: 500px;
  margin-top: 30px;
  margin-bottom: 15px;
  font-size: 16px;
  text-align: center;
}
.modal-comments {
  font-family: 'PyeongChang-Regular';
  width: 500px;
  margin-top: 20px;
  margin-bottom: 50px;
  font-size: 16px;
  /* font-weight: bold; */
}

.modal-comments p {
  margin-bottom: 10px;
}

.modal-info {
  margin-top: 15px;
  margin-bottom: 15px;
  text-align: left;
}

.modal-info p {
  font-family: 'PyeongChang-Regular';
  margin-top: 5px;
  font-size: 14px;
}
</style>