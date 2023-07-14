<template>
<div class = "title">My WinList</div>
<div class="winner-list">
  <ul v-for="winner in list" :key="winner">
    <li class="list-winner">
    <div class="card">
      <div class="round-cnt">
        ROUNDNUM <span style = "font-weight: bold; font-size: 16px; color:#ebebeb">#{{ winner.roundcnt }}</span> 
      </div>
      <!-- <div class="nickname">
        here : {{ winner.nickname }}
      </div> -->
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
</template>

<script>
export default{
  name:'myWinList',
  props:{
    memnum : Number
  },
  data(){
    return{
      list:[1],
    }
  },
  created:function(){
    let self = this;
    self.$axios.get(`http://localhost:7878/battles/winnerlist/${self.memnum}`)
    .then(res =>{
      if(res.status == 200){
        self.list = res.data.list;
        console.log(self.list)
      }else{
        alert("오류로 인해 명예의 전당 활성화 불가")
      }
    });
  }
}
</script>

<style scoped>
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
  width:400px;
}



li{
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