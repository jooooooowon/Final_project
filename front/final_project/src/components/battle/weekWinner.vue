<template>
  <div id="container">
  <div v-if="chk" style="display : flex">
    
    <div class="main">
        <div v-for="particle in particles" :key="particle.id" class="particule" :style="{ 
          transform: 'translate3d(' + particle.x + 'px, ' + particle.y + 'px, 1px)', 
          width: particle.size + 'px',
          height: particle.size + 'px',
          background: particle.color
        }"></div>
    </div>

    <div class="content-list">
      <div id="congratulation">
        <!-- 이번주 우승자입니다. -->
        이번주  [{{ theme }}] 우승자
      </div>
      <!-- <img :src="'http://localhost:7878/battles/imgs/'+batnum" alt=""> -->
      <hr>
      <div class="win-info">
        <!-- <div class="batnum-img"> -->
          <img style="margin-left: 90px;" :src="'http://localhost:7878/battles/imgs/'+batnum" alt="">
        <!-- </div> -->
          
        <div class="main-content">
          <img class="winner-img" style="width:170px; height:170px;" :src="require('@/assets/winner.png')" alt="">
        <div class = "content-main">
          <!-- <img :src="'http://localhost:7878/battles/imgs/'+num" alt=""> -->
          <div class="content-title">
            닉네임:
          </div>
          <!-- <div class="bar" style="margin-left:30px;"></div> -->
          <div class="content">
            &nbsp; {{ nickname }}
          </div> 
        </div>
        <!-- <div class = "content-main">
          <div class="content-title">
            성별:
          </div> -->
          <!-- <div class="bar" style="margin-left:91px;"></div> -->
          <!-- <div class="content">
            {{ gender }}
          </div> 
        </div> -->
        <!-- <div class = "content-main">
          <div class="content-title">
            주제:
          </div> -->
          <!-- <div class="bar" style="margin-left:110px;"></div> -->
          <!-- <div class="content">
            {{ theme }}
          </div> 
        </div> -->
        <div class = "content-main">
          <div class="content-title">
            우승:
          </div>
          <!-- <div class="bar" style="margin-left:43px;"></div> -->
          <div class="content">
            &nbsp; {{ roundcnt }}
          </div> 
        </div>
      </div>
      </div>
      <div class="left-time">
        <hr>
        다음 배틀 신청까지 남은 시간
        <div class="timer">
          {{ hour }} : {{ min }} : {{ sec }}
        </div>
      </div>
    </div>
  </div>
  <div v-else>
    <notYet></notYet>
  </div>
</div>
</template>

<script>
import notYet from '@/components/battle/notYet.vue'


export default {
  data(){
    return{
      dto : {},
      winner : {},
      chk : true,
      hour : "",
      min : "",
      sec : "",
      nickname : '',
      gender : '',
      theme : '',
      roundcnt : 0,
      batnum : 0,
      colors:["#eb6383", "#fa9191", "#ffe9c5", "#b4f2e1"],
      particles:[],
      img:'',
      memnum:''
    }
  },
  created:function(){
    this.findWinner();
    setInterval(() => {
      this.chkLeftTime();
    }, 1000); 
  },
  mounted(){
      this.pop();
      this.render();
  },
  components: {
    notYet
  },
  methods:{
    pop() {
      for (let i = 0; i < 250; i++) {
        const particle = {
          id: i,
          x: window.innerWidth * 0.5,
          y: window.innerHeight + Math.random() * window.innerHeight * 0.3,
          vel: {
            x: (Math.random() - 0.5) * 10,
            y: Math.random() * -20 - 15
          },
          mass: Math.random() * 0.2 + 0.8,
          size: Math.random() * 15 + 5,
          color: this.colors[Math.floor(Math.random() * this.colors.length)]
        };
        this.particles.push(particle);
      }
    },
    render() {
      for (let i = this.particles.length; i--; i > -1) {
        const particle = this.particles[i];
        particle.x += particle.vel.x;
        particle.y += particle.vel.y;
        particle.vel.y += 0.5 * particle.mass;
        if (
          particle.y > window.innerHeight * 1.2 ||
          particle.x > window.innerWidth
        ) {
          this.particles.splice(i, 1);
        }
      }
      requestAnimationFrame(this.render);
    },
    findWinner(){
      let self = this;
    
      // chk는 혹시나 오류가 났을 때 대처할 페이지로 이동의 유무를 따지는 역할을 한다.
      self.$axios.get("http://localhost:7878/battles/winner")
      .then(res =>{
        if(res.status == 200 || res.data.flag){
          if(res.data.dto == undefined || res.data.dto.theme == '이번주 테마는?'){
            self.chk = false;
          }
          console.log(res.data)
          self.nickname = res.data.dto.memnum.nickname;
          self.gender = res.data.dto.gender;
          self.roundcnt = res.data.dto.roundcnt;
          self.theme = res.data.dto.theme;
          self.batnum = res.data.dto.batnum;
          self.img = res.data.dto.memnum.img;
          self.num = res.data.dto.memnum;
          console.log(self.batnum);
          console.log(self.img);
          console.log(self.num);

          self.img = decodeURIComponent(self.img);
        }else{
          self.chk = false;
        }
      })
    },

    findProfileImg(){
      const self = this;
      self.$axios.get('http://localhost:7878/members/' + this.batnum)
        .then(function(res){
            if(res.status==200){
                let dto = res.data.dto
                if(dto!=null){
                    self.img=dto.img
                }else{
                    alert("사진 못 불러온다")
                }
            }else{
                alert('에러코드:'+self.status)
            }
        });
    },
    chkLeftTime(){
      let now = new Date();
      let end = new Date(now.getFullYear(),now.getMonth(),now.getDate()+1);
      let nt = now.getTime();
      let et = end.getTime();
      let sec =parseInt(et - nt) / 1000;
      let day  = parseInt(sec/60/60/24);
      sec = (sec - (day * 60 * 60 * 24));
      let hour = parseInt(sec/60/60);
      sec = (sec - (hour*60*60));
      let min = parseInt(sec/60);
      sec = parseInt(sec-(min*60));
      this.hour = hour;
      this.min = min;
      this.sec = sec;
      if(hour<10){this.hour="0"+hour;}
      if(min<10){this.min="0"+min;}
      if(sec<10){this.sec="0"+sec;}
    }
  }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");

@font-face {
  font-family: "Flood Std"; 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot"); 
  src: url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.eot?#iefix") format("embedded-opentype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff2") format("woff2"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.woff") format("woff"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.ttf") format("truetype"), url("//db.onlinewebfonts.com/t/bfbdc6a04609bff0d5ccdfcf6b766d95.svg#Flood Std") format("svg"); 
}

@font-face {
    font-family: 'PyeongChang-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
    font-weight: normal;
    font-style: normal;
}

#container{
  font-family: 'PyeongChang-Regular';
  font-weight: normal;
}
.main-content{
  display: flex;
  flex-direction: column;
  margin-left: 100px;
  margin-top:auto;
  margin-bottom:auto;
}

.win-info{
  display:flex;
}
.main {
  place-items: center;
  width: 100%;
  height: 100%;
  overflow-x: hidden;
  overflow-y: scroll;
  z-index : 2;
}

.particule {
  position: absolute;
  border-radius: 50%;
  box-shadow: 1px 1px 4px #eb6383;
}



.content-list{
  z-index: 1;
  position:absolute;
  left : 0;
  right : 0;
  margin:auto;
  width: 800px;
}

#congratulation{
  font-family: "PyeongChang-Regular", cursive;
  font-size: 35px;
  color: #222;
  text-align: center;
  font-weight: bold;
  padding-top: 20px;
}

.content-list img{
  width: 294px;
  height: 294px;
  display: flex;
  border-radius: 8px;
  /* transform: translateX(-50px); */
}

hr{
  border : 2px solid #ebebeb;
  margin-bottom : 20px;
  margin-top: 20px;
}

.content-main{
  text-align : center;
  font-weight: bold;
  font-size: 30px;
  display: flex;
  width: 800px;
  padding-top:10px;
}

.content-title{
  color: #222;
  text-align: center;
  /* -webkit-text-stroke: 1px #000; */
}

.batnum-img,
/* .winner-img {
  position: absolute;
  top: 0;
  left: 0;
}

.batnum-img {
  z-index: 1;
}

.winner-img {
  z-index: 2;
} */


/* .bar{
  width: 4px;
  height: 35px;
  margin-top:6px;
  border : 1px solid #85b380;
} */
.content{
 text-align: center;
}
.timer{
  margin-top: 20px;
  color:#222;
}

.left-time{
  font-size: 25px;
  color:#222;
  text-align: center;
  margin:0 auto;
}

h2{
  font-size: 20px;
}

</style>