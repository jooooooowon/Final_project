<template>
  <div v-if = "!prepared">
    <notYet></notYet>      
  </div>

  <div v-else class="main">
    <div>
      <div style="font-size: 2.5em; font-weight: bold;  margin-top:46px; ">배틀 신청서</div>
      <hr />
      <div class="title">이번주 주제는 <span style="font-size: 1.2em; font-weight: bolder; color: blueviolet;">" {{ theme }} "</span> 입니다.</div> 
    </div>
    <div style="width:850px; margin-top: 20px;">
      <ul>
        <li>
          <p style="font-size: 1.3em; margin-bottom: 15px;"> 《 신청자 》 </p> 
          
          <div class="b-form">
            <img class="img" :src="'http://localhost:8081/members/imgs/' + memnum" alt="왜 안나옴 ㅋㅋ">
            <div class="v-line"></div>
            <div class="form-p">
              <p style="margin-top: 10px;">회원번호 : {{ memnum }}</p>
              <br>
              <p>성별 : {{ gender }}</p>
              <br>
              <p>회차 : {{ roundcnt }}</p>
            </div>  
          </div>
        </li>
      </ul>
    </div>
    <!-- theme : {{ theme }} <br> -->
    <!-- memnum : {{ memnum }} <br> -->
    <!-- gender : {{ gender }} <br> -->
    <!-- roundcnt : {{ roundcnt }} <br> -->
    <!-- dto : {{ dto }} -->
    <!-- <img :src="'http://localhost:8081/members/imgs/' + memnum" alt="왜 안나옴 ㅋㅋ"> -->
    <div v-if="chk" style="margin-top: 20px;">
      <input style="margin-bottom: 30px;" type="file"><br>
      <button @:click="applyBattle">신청 하기</button>
    </div>

    <div v-else>
      이미 신청하였습니다.
    </div>

  </div>

</template>

<script>
import notYet from '@/components/battle/notYet.vue'
export default {
  name : 'battleApply',
  data(){
    return{
      theme:'',
      memnum: sessionStorage.getItem("memnum"),
      gender: '',
      roundcnt: 0,
      dto : {},
      chk : true,
      prepared : false
    }
  },
  components:{
    notYet : notYet
  },
  created: function(){
    let token = sessionStorage.getItem('token');
    const self = this;
    
    // dto 로그인 정보로 저장하기.
    if(self.memnum == undefined){
      alert("로그인 후 사용 가능합니다.");
      location.href ="/";
    }
    self.$axios.get(`http://localhost:8081/members/${self.memnum}`,{headers:{'token':token}})
    .then(res =>{
      if(res.status == 200 || res.data.flag){
        self.dto = res.data.dto;
        self.gender = res.data.dto.gender;
      }else{
        alert("오류 발생으로 인한 로그인 정보 불러오기 실패");
      }
    });
  

    // 대결 테마 들고 오기.
    self.$axios.get('http://localhost:8081/battles/info')
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
    
    // 신청 유무 확인하기.
    self.$axios.get(`http://localhost:8081/battles/chk/${this.memnum}`)
    .then(res =>{
      if(res.status == 200){
        self.chk = res.data.chk;
      }else{
        alert("체크 확인 불가.")
      }
    })

  },
  methods:{

    // 신청 하기 버튼 클릭 시 실행되는 메소드
    applyBattle(){
      const self = this;
      let file = document.querySelector("input");
      alert(file)
      let formdata = new FormData();
      formdata.append("memnum",this.memnum);
      formdata.append("theme",this.theme);
      formdata.append("gender",this.gender);
      formdata.append("roundcnt",this.roundcnt);
      formdata.append("mf",file.files[0]);
      
      self.$axios.post('http://localhost:8081/battles',formdata,
      {headers : {"Content-Type":"multipart/form-data"}})
      .then(res =>{
        if(res.status == 200 || res.data.flag){
          alert("신청 완료.");
          location.href = "/"
        }else{
          alert("오류 발생으로 인한 신청 실패")
        }
      })
    }
  }
}
</script>

<style scoped>
.main{
  position: absolute;
  width: 850px;
  margin:auto;
  left:0;
  right:0;
}


hr {
  background-color:  lightgray;
  height: 5px;
  border: 0;
}

.b-form{
  display:flex;
  width: 500px;
  left:0;
  right:0;
  margin:auto;
  justify-content: space-evenly;
}

.form-p{
  /* margin-top:20px; */
  font-size: 1.3em;
  text-align: left;
}

.title {
  font-size: 1.4em;
  margin-bottom: 50px;
}

.v-line{
 border-left:  solid rgb(3, 129, 3, 0.3);
 height:28%;
 left: 50%;
 position: absolute;
}

</style>