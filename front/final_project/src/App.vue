<template>
  <nav>
    <div class="outerHeader">
      <router-link to="/">
        <div class="text-box">
          <span class="text-wrap">
            <svg width=310 height=70 viewBox="0 0 525 160">
              <!-- <text x="0" y="50%">W</text>
              <text x="100" y="50%">E</text>
              <text x="160" y="50%">L</text>
              <text x="218" y="50%">L</text>
              <text x="255" y="50%">C</text>
              <text x="300" y="50%">O</text>
              <text x="345" y="50%">M</text>
              <text x="360" y="50%">E</text> -->
              <text x="0" y="70%">O</text>
              <text x="70" y="70%">T</text>
              <text x="125" y="70%">Z</text>
              <text x="200" y="70%">Z</text>
              <text x="275" y="70%">A</text>
              <text x="336" y="70%">N</text>
              <text x="415" y="70%">G</text>
            </svg>
          </span>
        </div>
      </router-link>

      <div class="innerHeader">
        <div>
          <ul v-if="memnum == undefined" class="memberHeader">
            <li>
              <router-link to="/login">로그인</router-link>
            </li>
            <li>
              <div class="bar"></div>
            </li>
            <li>
              <router-link to="/join">회원 가입</router-link>
            </li>
          </ul>
          <ul v-else class="memberHeader">
            <li>
              <router-link to="/myPageCss">마이 페이지</router-link>
            </li>
            <li>
              <div class="bar"></div>
            </li>
            <li>
              <div class="logout" v-on:click="logout">
                <router-link to="/">로그 아웃</router-link>
              </div>
            </li>
          </ul>
        </div>


        <div>
          <ul class="menuHeader">
            <li>
              <router-link to="/weather" style="cursor: pointer;">날씨</router-link>
            </li>
            <li>
              <router-link to="/closetlist" style="cursor: pointer;">옷장</router-link>
            </li>
            <li>
              <router-link to="/ootwlist" style="cursor: pointer;">ootw</router-link>
            </li>
            <li>
              <router-link to="/listboard">코디</router-link>
            </li>
            <li>
              <span v-if="chkDay <= 3 && chkDay > 0"><router-link to="/vote">배틀 투표</router-link></span>
              <span v-else-if="chkDay == 4"><router-link to="/weekWinner">이 주의 우승자</router-link></span>
              <span v-else><router-link to="/apply">배틀 신청</router-link></span>
            </li>

            <!-- <li>
              <span v-if="chkDay <= 3 && chkDay > 0"><router-link to="/vote">배틀 투표</router-link></span>
              <span v-else-if="chkDay == 4"><router-link to="/weekWinner">이 주의 우승자</router-link></span>
              <span v-else><router-link to="/apply">배틀 신청</router-link></span>
            </li> -->

            <li>
              <router-link to="/winnerList">명예의 전당</router-link>
            </li>
          </ul>
        </div>
      </div>
      <!-- <div class="naver-shopping">
        <router-link to="naverShoppingList"><img src="./assets/navershopping2.png"
            alt="naver-shopping-icon"></router-link>
      </div> -->
    </div>
    <!-- <router-link to = "/vote">배틀 투표</router-link><br>
    <router-link to = "/weekWinner">이 주의 우승자</router-link><br>
    <router-link to = "/apply">배틀 신청</router-link><br>
    <router-link to = "/changeTheme">테마 변경</router-link><br>
    <router-link to = "/random">랜덤 뽑기</router-link><br>
    <router-link to = "/myPageCss">myPageCss</router-link><br> -->
  </nav>
  <router-view />
</template>

<script>
export default {
  data() {
    return {
      memnum: sessionStorage.getItem("memnum"),
      //토큰만료시 자동로그아웃
      token: sessionStorage.getItem("token")
    }
  },
  computed: {
    chkDay() {
      let now = new Date();
      return now.getDay();
    }
  },
  //토큰만료시 자동로그아웃
  created(){
    this.checkTokenExpiration()
  },
  watch: {
  $route() {
    this.checkTokenExpiration();
  }
},
  methods: {
    //토큰만료시 자동로그아웃
    checkTokenExpiration(){
      if(this.token){
        const tokenParts = this.token.split('.');
      
      if(tokenParts.length === 3){
        const payload = JSON.parse(atob(tokenParts[1]));
        const expiration = payload.exp * 1000;
        const currentTime = new Date().getTime();
        if(expiration < currentTime){
          // 토큰이 만료되었을 때 로그아웃 처리
          this.tokenlogout();
        }
      }
      }
    },
    logout() {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('memnum')
      console.log('memnum')
      // alert('로그아웃 성공')
      location.href = "/"
    },
    tokenlogout() {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('memnum')
      console.log('memnum')
      alert('시간이 경과되어 로그아웃 되었습니다.')
      location.href = "/"
    }
  }
}
</script>

<style scoped>
/* @import url('https://fonts.googleapis.com/css2?family=Staatliches&display=swap');
@import url('https://fonts.googleapis.com/css2?family=Montserrat:wght@100&display=swap'); */
@font-face {
  font-family: 'PyeongChang-Bold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Bold.woff2') format('woff2');
  font-weight: 700;
  font-style: normal;
}
@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
}
@font-face {
  font-family: "Flood Std"; 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot"); 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot?#iefix") format("embedded-opentype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff2") format("woff2"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff") format("woff"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.ttf") format("truetype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.svg#Flood Std") format("svg"); }

body {
  overflow-x: hidden;
}

.text-box {
  width: 100%;
  height: 100%;
  display: flex;
  /* justify-content: center; */
  justify-content: flex-start;
  align-items: center;
  padding-top: 20px;
}

svg text {
  /* font-family: 'Staatliches', cursive; */
  /* font-family: 'PyeongChangPeace-Bold'; */
  font-family: "Flood Std";
  font-size: 8em;
  fill: transparent;
  stroke-dasharray: 326px;
  animation: stroke 1s linear;
  animation-fill-mode: forwards;
  animation-duration: 5s;
  animation-iteration-count: infinite;
}

@keyframes stroke {
  0% {
    /* stroke: #68a162; */
    stroke: #2c3e50;
    stroke-width: 3px;
    stroke-dashoffset: 326px;
  }

  30% {
    fill: transparent;
  }

  33% {
    /* stroke: #68a162; */
    stroke: #2c3e50;
    stroke-width: 1px;
  }

  51%,
  100% {
    /* fill: #68a162; */
    fill: #2c3e50;
    stroke-dashoffset: 0px;
  }
}


#app {
  font-family: 'PyeongChang-Regular';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

.outerHeader {
  display: flex;
  justify-content: space-between;
  width: 1200px;
  margin-left: auto;
  margin-right: auto;
  margin-top: 45px;
  padding-bottom: 40px;
}

.innerHeader {
  font-family: 'PyeongChang-Regular';
  position: relative;
  justify-content: flex-start;
  display: flex;
  width: 800px;
  margin: 15px 15px 0;
}

.memberHeader {
  font-family: 'PyeongChang-Regular';
  display: flex;
  position: absolute;
  right: 0;
}

.memberHeader li {
  font-size: 14px;
  padding-left: 10px;
}

.bar {
  width: 1px;
  height: 15px;
  background-color: #2c3e50;
}

.menuHeader {
  position: absolute;
  font-size: 18px;
  display: flex;
  bottom: 10px;
  top: 55px;
  right: 0;
}

.menuHeader li {
  padding-left: 25px;
  font-size: 18px;
  white-space: nowrap;
}

/* .naver-shopping img {
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

.naver-shopping:hover img {
  bottom: 15px;
} */

/* nav {
  background-color: #E1ECC8;
} */

nav a {
  font-weight: bold;
  color: #2c3e50;
  text-decoration: none;
  transition: .3s;
}

nav a:hover,
route-active {
  font-family: 'PyeongChang-Bold';
  color: #68a162;
  ;
}

nav a.router-link-exact-active {
  font-family: 'PyeongChang-Bold';
  color: #68a162;
}


img {
  width: 120px;
  height: 120px;
}

/* ----------------------- */

.merqueeclass {
  display: flex;
}

.animationclass {
  display: flex;
}

.first {
  animation-duration: 3s;
  animation-name: first;
}


/* =============================================================================== */
</style>