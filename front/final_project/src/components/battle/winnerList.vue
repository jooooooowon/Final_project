<template>

<div v-if ="list.length == 0">
  <notYet></notYet>
</div>
<div v-else>
  <div style="display :flex">
    <div class = "main-winner-list">
      <div class="body-div">
        <div class="container">
          <div class="bubbles">
            <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
            <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
            <span v-for="i in items" :style="`--i:${i}`" :key="i"></span>
          </div>
        </div>
      </div>
      <div class = "title">명예의 전당</div>
      <div class="winner-list">
        <ul v-for="(winner,i) in list" :key="i">
          <li class="list-winner" v-if="winner.theme != '이번주 테마는?'">
          <div class="card">
            <div class="round-cnt">
              ROUNDNUM <span style = "font-weight: bold; font-size: 16px; color:#ebebeb">#{{ winner.roundcnt }}</span> 
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
              <img :src="'http://localhost:8081/battles/imgs/' + winner.batnum" alt="이미지 불러오기 실패">
            </div>
          </div>
        </li>
        </ul>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import notYet from './notYet.vue'

export default{
  name:'winnerList',
  data(){
    return{
      dto:{},
      list:[1],
      nickname:[],
      items: [
        11, 12, 24, 10, 14, 23, 18, 16, 19, 20, 22, 25, 18, 21, 15, 13, 26, 17, 13, 28, 10, 25, 24, 18
      ],
    }
  },
  components : {
    notYet : notYet
  },
  created: function(){
    this.winnerList();
  },
  methods:{
    winnerList(){
      let self = this;
    self.$axios.get('http://localhost:8081/battles/winnerlist')
    .then(res =>{
      if(res.status == 200){
        self.list = res.data.list;
        for(let dto of self.list){
          console.log(dto.memnum.nickname);
          this.nickname.push(dto.memnum.nickname);
        }
        let container = document.querySelector('.container');
        // container.style.height = Math.trunc(((self.list.length + 2) / 3)) * 100  + 'vh';
        container.style.setProperty("--h-name",Math.trunc(((self.list.length + 2) / 3)) * 100 + 'vh');
        console.log(self.list.length);
        console.log("hname" + container.style.getPropertyValue("--h-name"));
        console.log(container.style.getPropertyValue("height"));
        console.log(this.list);
      }else{
        alert("오류로 인해 명예의 전당 활성화 불가")
      }
    });
    },
  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Diphylleia&display=swap');

  .body-div{
  position:absolute;
}

.container{
  position:relative;
  width: 100%;
  overflow : hidden;
  --h-name: 100vh;
  height: 100vh;
}

.bubbles{
  position: relative;
  display:flex;
}

.bubbles span{
  position:relative;
  width: 30px;
  height: 30px;
  background-color: #85b380;
  margin: 0 4px;
  border-radius : 50%;
  box-shadow: 0 0 0 10px #85b38044,
  0 0 50px #85b380,
  0 0 100px #85b380;
  animation : animate 15s ease-in-out infinite;
  animation-duration: calc(125s / var(--i));
}

@keyframes animate {
  0%{
    transform: translateY(var(--h-name)) scale(0); 
  }
  100%{
    transform: translateY(-10vh) scale(.3);
  }
  
}


/* =========================================== */

.main-winner-list{
  position : absolute;
  left : 0;
  right: 0;
  margin : auto;
}

.winnerList{
  width:100%;
  padding:auto;
}

.title{
  font-family: 'Black Han Sans', sans-serif;
  font-size: 50px;
  height:150px;
  line-height: 150PX;
  color: #68a162;
  left:0;
  right:0;
  text-align: center;
}

.winner-list{
  left:0;
  right:0;
  margin:auto;
  width:1000px;
}


ul{
  float:left;
}

li{
  margin-left : 30px;
  margin-bottom: 50px;
}

.card{
  width: 200px;
  padding: 20px 50px;
  margin:auto;
  left:0;
  right:0;
  position: relative;
  box-shadow: 0 10px 30px rgba(0,0,0,.25);
  animation-name: winner-list;
  animation-duration:3s;
}

@keyframes winner-list{
  0%{
    opacity: 0;
  }
  100%{
    opacity: 1;
  }
}
.round-cnt{
  background-color:#68a162;
  width: 120px;
  margin-top:20px;
  border-radius: 5px;
  font-size: 14px;
}

.nickname{
  font-family: 'Black Han Sans', sans-serif;
  font-size: 30px;
  text-align: left;
  margin-top:14px;
  color: #68a162;
}

.winner-content{
  display: flex;
  justify-content: space-around;
  margin-top:14px;
}

.small-title{
  font-weight: bold;
  font-size: 20px;
}
.small-content{
  color: #68a162;
  font-weight: bold;
}
.bar{
  width : 1px;
  height: 50px;
  background-color:black;
}

.imgPosition{
  display:absolute;
  left:0;
  right:0;
  margin: 25px auto;
  width: 160px;
  height: 160px;
  border-radius: 50%;
  overflow: hidden;
}

img{
  width: 100%;
  height: 100%;
}


</style>