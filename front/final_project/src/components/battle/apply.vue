<template>
  <div v-if="!prepared">
    <notYet></notYet>
  </div>

  <div v-else class="main">
    <div >
      <div style="font-size: 2.5em; font-weight: bold;  margin-top:46px; ">{{ roundcnt }}회차 배틀 신청서</div>
      <hr />
      <div class="title">이번주 배틀주제는 <span style="font-size: 1.2em; font-weight: bolder; color:rgb(41, 108, 28);">[ {{ theme }}
          ]</span> 입니다.</div>
    </div>
    <div class="formBox">

      <div class="profile">
        <div class="profile-item"> 《 신청자 프로필 》 </div>
        <div class="b-form">
          <img class="memberimg" :src="'http://localhost:7878/members/imgs/' + memnum" alt="왜 안나옴 ㅋㅋ">
          <hr style="width: 200px; border: solid gray 2px; margin-left: 15%; margin-top: -4px"/>
          <div class="form-p">
            <div class="item">☞ 아이디 : {{ id }}</div>
            <div class="item">☞ 닉네임 : {{ nickname }}</div>
            <div class="item" style="margin-bottom: 10%;">☞ 성별 : {{ gender }}</div>
          </div>
        </div>
      </div>

      <div v-if="chk" class="label-class">
        <p style="margin-left: 25%; color: gray;"># 사진을 등록해주세요!</p>
        <br />
        <label for="imgtag">
          <img src="../../assets/img-upload.png">
        </label>
        <input type="file" id="imgtag" style="display:none" accept="image/*" v-on:change="thumbnail"><br>

        <button @:click="applyBattle">신청 하기</button>
      </div>

      <div v-else class="else">
        이미 신청하였습니다.  
      </div>
    </div>

  </div>
</template>

<script>
import notYet from '@/components/battle/notYet.vue'
export default {
  name: 'battleApply',
  data() {
    return {
      theme: '',
      memnum: sessionStorage.getItem("memnum"),
      gender: '',
      id: '',
      nickname: '',
      roundcnt: 0,
      dto: {},
      chk: true,
      prepared: false
    }
  },
  components: {
    notYet: notYet
  },
  created: function () {
    let token = sessionStorage.getItem('token');
    const self = this;

    // dto 로그인 정보로 저장하기.
    if (self.memnum == undefined) {
      alert("로그인 후 사용 가능합니다.");
      location.href = "/";
    }
    self.$axios.get(`http://localhost:7878/members/${self.memnum}`, { headers: { 'token': token } })
      .then(res => {
        if (res.status == 200 || res.data.flag) {
          self.dto = res.data.dto;
          self.id = res.data.dto.id;
          self.nickname = res.data.dto.nickname;
          // self.gender = res.data.dto.gender;

          // 성별 값에 따라 처리
          if (self.dto.gender === 'male') {
            self.gender = '남자';
          } else if (self.dto.gender === 'female') {
            self.gender = '여자';
          }
        } else {
          alert("오류 발생으로 인한 로그인 정보 불러오기 실패");
        }
      });

    // 대결 테마 들고 오기.
    self.$axios.get('http://localhost:7878/battles/info')
      .then(res => {
        if (res.status == 200 || res.data.flag) {
          // 대결 테마가 변경되어있는 지 확인하는 if문
          if (res.data.changeTheme) {
            this.theme = res.data.theme;
            this.roundcnt = res.data.roundcnt;
            self.prepared = true;
          }
        } else {
          alert("오류 발생으로 인한 테마 정보 불러오기 실패");
        }
      })

    // 신청 유무 확인하기.
    self.$axios.get(`http://localhost:7878/battles/chk/${this.memnum}`)
      .then(res => {
        if (res.status == 200) {
          self.chk = res.data.chk;
        } else {
          alert("체크 확인 불가.")
        }
      })

  },
  methods: {

    // 신청 하기 버튼 클릭 시 실행되는 메소드
    applyBattle() {
      const self = this;
      let file = document.querySelector("input");
      console.log(file.files[0])
      if (file.files[0] == undefined) {
        alert("배틀 신청 시 사진은 필수 입니다.")
      } else {

        let formdata = new FormData();
        formdata.append("memnum", this.memnum);
        formdata.append("theme", this.theme);
        formdata.append("gender", this.gender);
        formdata.append("roundcnt", this.roundcnt);
        formdata.append("mf", file.files[0]);

        self.$axios.post('http://localhost:7878/battles', formdata,
          { headers: { "Content-Type": "multipart/form-data" } })
          .then(res => {
            if (res.status == 200 || res.data.flag) {
              alert("신청이 완료되었습니다.");
              location.href = "/"
            } else {
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
        reader.onload = function () { // reader.onload 이벤트 핸들러는 파일 읽기가 완료되었을 때 호출되는 콜백 함수를 정의하는 역할을 한다.
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
@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: normal;
  font-style: normal;
}

.main {
  font-family: 'PyeongChang-Regular';
  font-weight: 400;
  position: absolute;
  width: 850px;
  margin: auto;
  left: 0;
  right: 0;
}

hr {
  background-color: lightgray;
  height: 5px;
  border: none;
}

.formBox {
  display: flex;
}
.profile {
  width:800px; 
  margin-top: 6%;
  margin-left: 3%;
}
.profile-item {
  font-size: 1.3em; 
  font-weight: bold; 
  margin-top: 2%;
  /* margin-left: 9%; */
}

.memberimg {
  width: 150px;
  height: 150px;
  border-radius: 42%;
  margin-top: 5%;
  z-index: 1;
  /* margin-left: 10%; */
}

.b-form {
  display: flex;
  flex-direction: column;
  width: 500px;
  height: 100px;
}

.form-p {
  font-size: 1.3em;
  /* margin-top: 20%; */
  /* margin-left: 5%; */
}

.title {
  font-size: 1.4em;
  margin-top: 30px;
}
.item {
  margin-top: 5%;
}

.label-class {
  margin-top: -30px;
  margin-right: 1%;
}

.label-class img {
  width: 280px;
  height: 400px;
  margin-top: 3%;
  border-radius: 10%;
  object-fit: cover;
}

.label-class button {
  font-family: 'PyeongChang-Regular';
  width: 100px;
  height: 55px;
  margin-bottom: 25%;
  margin-left: 63%;
	background-color:transparent;
	border: none;
  font-size: 1.1em;
  font-weight: bold;
  color: lightslategray;
}

.label-class button:hover {
  color: rgb(41, 108, 28);
  cursor: pointer;
}
.else {
  width: 100%;
  /* margin-right: 20%;  */
  font-weight: bold; 
  font-size: 1.3em; 
  margin-top: 22%;
}
</style>