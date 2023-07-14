<template>
  <div class="body-css">
  <div class="battle-info">
    <div class="roundcnt" style="font-family: 'PyeongChang-Regular';">
      {{ roundcnt }}회차
    </div><br/>
    <div class="theme" style="font-family: 'PyeongChang-Regular';">
      테마 : {{ theme }}
    </div><br/>
    <div class="buttons">
      <input type="button" value="랜덤 두명 뽑기" @click="find" style="font-family: 'PyeongChang-Regular';">
      <input type="button" value="후보 확정 하기" @click="firm" style="font-family: 'PyeongChang-Regular';">
    </div>
  </div>
  <div v-show="show" class="show">
    <div class="first-candidate">
      <div class="nickname">
        {{ firstMember }}
      </div> 
      <img :src="'http://localhost:7878/battles/imgs/'+firstCandidate.batnum" alt="첫 번째">
    </div>
    <div class="second-candidate">
      <div class="nickname">
        {{ secondMember }}
      </div>
      <img :src="'http://localhost:7878/battles/imgs/'+secondCandidate.batnum" alt="두 번째">
      <br/><br/><br/>
    </div>
  </div>
</div>
</template>

<script>
export default{
  name : 'randomCandidates',
  data(){
    return{
      firstCandidate : {},
      firstMember : '',
      secondCandidate : {},
      secondMember : '',
      show : false,
      theme : '',
      roundcnt : 0
    }
  },
  created: function(){
    let self = this;
    self.$axios.get('http://localhost:7878/battles/info')
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        // 대결 테마가 변경되어있는 지 확인하는 if문
        if(res.data.changeTheme){
          this.theme = res.data.theme;
          this.roundcnt = res.data.roundcnt;
          self.prepared = true;
        }
      }else{
        alert("오류 발생으로 인한 테마 정보 불러오기 실패");
      }
    })
  },
  methods:{
    find : function(){
      let self = this;

      self.$axios.get('http://localhost:7878/battles/manager/random')
      .then(res => {
        if(res.status == 200){
          if(res.data.len != 2){
            alert('아직 신청자의 수가 충분하지 않습니다.');
          }else{
            self.firstCandidate = res.data.list.pop();
            self.secondCandidate = res.data.list.pop();
            self.show = true;
            setTimeout(() => {
              self.firstMember = self.firstCandidate.memnum.nickname;
              self.secondMember = self.secondCandidate.memnum.nickname;
            }, 100);
          }
        }else{
          alert('오류로 인해 후보자 랜덤 뽑기 실행 불가.')
        }
      })
    },
    firm : function(){
      let self = this;
      let form = new FormData();
      if(!self.show){
        alert('랜덤으로 뽑힌 후보자가 없습니다.');
      }else{

        form.append("num1",self.firstCandidate.batnum);
        form.append("num2",self.secondCandidate.batnum);
        
        self.$axios.delete(`http://localhost:7878/battles/manager/random/${self.firstCandidate.batnum}/${self.secondCandidate.batnum}`)
        .then(res => {
          if(res.status == 200){
            alert('후보가 확정되었습니다.');
            location.href = "/";  
          }else{
            alert('오류로 인해 후보자 확정 불가.')
          }
        })
      }
      
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
.body-css {
    font-family: 'PyeongChang-Regular';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}
.battle-info{
  width: 500px;
  position:absolute;
  left:0;
  right:0;
  margin: 50px auto;
  text-overflow: ellipsis;
}

.battle-info .roundcnt{
  font-size: 1.4em;
  font-weight: bold;
}
.battle-info .theme{
  font-size: 1.2em;
  font-weight: bold;

}

.buttons{
  margin: 0 auto;
}

input[type="button"]{
  font-family: 'PyeongChang-Regular';
  font-weight: 550;
  width: 100px;
  height: 40px;
  border-radius: 30px;
  background-color: #C4D7B2;
  transition : .5s;
  margin : 0 5px;
}

input[type="button"]:hover{
  background-color: #85b380;
  color: #ffffff;
  cursor:pointer;
  font-weight: bold;
}

.show{
  display:flex;
  justify-content: space-between;
  width: 650px;
  position:absolute;
  left:0;
  right:0;
  margin: 160px auto;
}

.nickname{
  font-family: 'PyeongChang-Regular';
  font-size: 1.2em;
  font-weight: bold;
  text-overflow: ellipsis;
  margin:5px;
}

.show div img{
  width: 300px;
  height: 420px;
  border-radius: 5%;
  object-fit: cover;
}

</style>