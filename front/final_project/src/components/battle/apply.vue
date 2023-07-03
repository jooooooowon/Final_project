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
    <div v-if="chk" class="label-class">
      <label for="imgtag">
            <img src="../../assets/img-upload.png">
        </label>
        <input type="file" id="imgtag" style="display:none" accept="image/*" v-on:change="thumbnail"><br>

      <button @:click="applyBattle">신청 하기</button>
    </div>

    <div v-else style="font-weight: bold; font-size: 1.3em; margin-top:120px">
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
      console.log(file.files[0])
      if(file.files[0] == undefined){
        alert("배틀 신청 시 사진은 필수 입니다.")
      }else{

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
    },
    thumbnail() {
        const file = document.getElementById('imgtag'); // type file에 올려진 file을 상수 file에 저장한다.
        if (file.files[0]) { // file은 하나만 올리므로 files 배열의 [0]만 null인지 아닌지 확인한다.
            // FileReader(): 자바스크립트 API.. 파일을 비동기적으로 읽을 수 있으며, 주로 파일의 내용을 읽어서 데이터를 가져오는 데 사용한다.
            const reader = new FileReader(); // FileReader의 새로운 객체를 생성(new)하여(인스턴스를 생성하여) reader 변수에 담는다. 
            const self = this;
            reader.onload = function() { // reader.onload 이벤트 핸들러는 파일 읽기가 완료되었을 때 호출되는 콜백 함수를 정의하는 역할을 한다.
                self.thumbimg = reader.result; // reader.result 속성은 파일의 데이터를 담고 있는 문자열을 제공함, 이 문자열은 일반적으로 이미지 파일의 경우 base64 인코딩된 이미지 데이터를 포함한다.
                // 따라서 인코딩된 이미지 데이터를 thumbnail에 담아서 이미지를 미리보기 해주는 역할을 수행한다.
                console.log("이미지 정보: " + reader.result)
                document.querySelector('label img').src = reader.result;
            };
            reader.readAsDataURL(file.files[0]);
            // FileReader의 객체를 사용하여 여러 방식으로 데이터를 읽을 수 있다.
            // 여기에서 사용된 readAsDataURL 메서드는 파일을 데이터 url형식으로 읽는다.
            // 업로드한 파일을 해당 메서드가 읽고, 파일 읽기가 완료되면 read.onload 이벤트 핸들러 내부의 작업이 시작된다.
        }
    },
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
 height: 150px;
 left: 50%;
 position: absolute;
}

.label-class{
  margin-top : 60px;
}

.label-class img{
  width: 300px;
  height: 300px;
}

.label-class button{
  margin : 30px;
  width: 80px;
  height: 50px;
  border-radius : 20px;
  background-color: #C4D7B2;
  transition : .5s;
}

.label-class button:hover{
  background-color: #85b380;
  color: #ffffff;
  cursor:pointer;
  font-weight: bold;
}
</style>