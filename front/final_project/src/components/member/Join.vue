<template>
    <div id="container">
    <div id="myjoin">
        <h2 class="join_title">회원가입</h2>

        <!-- 아이디 입력 폼 -->
        <div class="form_group">
            <label for="id" :class="{'input_label': !hasIdError, 'input_label_error': hasIdError}">아이디*</label>
            <input type="text" id="id" v-model="id" placeholder="아이디" :class="{'input_field': !hasIdError, 'input_field_error': hasIdError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="idcheck(); validateId($event)">
            <!-- {{ msg }} -->
        

        <!-- 아이디 유효성검사 메시지 -->
        <!-- <p class="input_error" v-if="id && !validateId(id)">영문과 숫자 8자 이상 16자 이하로 입력해주세요.</p> -->
        <p class="input_error" v-if="hasIdError">영문과 숫자 8자 이상 16자 이하로 입력해주세요.</p>
        <p class="input_idcheck" v-else>{{ msg }}</p>
        <!-- <p class="input_idcheck" v-else="validateId(id)"></p> -->
         </div>
        
        <!-- 비밀번호 입력 폼 -->
        <div class="form_group">
            <label for="pwd" :class="{'input_label': !hasPwdError, 'input_label_error': hasPwdError}">패스워드*</label>
            <input type="password" id="pwd" v-model="pwd" placeholder="패스워드" :class="{'input_field': !hasPwdError, 'input_field_error': hasPwdError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validatePwd($event)">

        <!-- 비밀번호 유효성검사 메시지 -->
        <p class="input_error" v-if="hasPwdError">영문, 숫자, 특수문자를 조합해서 입력해주세요. (4-12자)</p>
        </div>

        <!-- 이메일 입력 폼 -->
        <div class="form_group">
            <label for="email" :class="{'input_label': !hasEmailError, 'input_label_error': hasEmailError}">이메일*</label>
            <input type="text" id="email" v-model="emailId" placeholder="예) intheham@tistory.com" :class="{'input_field': !hasEmailError, 'input_field_error': hasEmailError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateEmail($event)">

        <!-- 이메일 유효성검사 메시지  -->
        <p class="input_error" v-if="hasEmailError">이메일 주소를 정확히 입력해주세요.</p>

        <!-- 이메일 인증 버튼 -->
            <button v-on:click="authEmail" id="authEmail" class="authEmail_error" v-if="emailId === ''" disabled>인증하기</button> 
            <button v-on:click="authEmail" id="authEmail" class="authEmail_error" v-else-if="hasEmailError" disabled>인증하기</button>
            <button v-on:click="authEmail" id="authEmail" class="authEmail" v-else>인증하기</button>
        </div>
        
        <!-- 인증확인 -->
        <div class="form_group">
            <label for="authKey" class="input_lable">인증번호*</label>
            <input type="text" v-model="authKey" id="authKey" class="input_field" disabled>
            <input type="text" v-model="compKey" style="display: none;"> <!-- 서버사이드에서 받은 인증키 비교 위해 저장해놓을 더미용 -->

            <!-- 인증번호 받기 누르면 시작되는 타이머 -->
            <div id="timer" style="display: flex;">
                <span id="displayedTime" style="text-align: center;  color:lightgray">3분0초</span>
                <div class="checkKey_btn">
                    <button v-on:click="checkKey" id="checkKey" class="checkKey_disabled" v-if="compKey==''" disabled>인증번호 확인</button>
                    <button v-on:click="checkKey" id="checkKey" class="checkKey" v-else>인증번호 확인</button>
                </div>
            </div>
        </div>

        <!-- 성별 -->
        <div class="form_group">
            <label for="gender" :class="{'input_label': gender}">성별*</label>
            <select v-model="gender" placeholder="선택하세요" class="input_field">
                <option value="male" >남성</option>
                <option value="female">여성</option>
            </select>
        </div>

        <!-- 닉네임 입력폼 -->
        <div class="form_group">
            <label for="nickname" :class="{'input_label': !hasNicknameError, 'input_label_error': hasNicknameError}">닉네임*</label>
            <input type="text" id="nickname" v-model="nickname" placeholder="닉네임" :class="{'input_field': !hasNicknameError, 'input_field_error': hasNicknameError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="nickcheck(); validateNickname($event)">

            <p class="input_error" v-if="hasNicknameError">3~8자로 입력해주세요.</p>
            <p class="input_nickcheck" v-else>{{ nickmsg }}</p>
        </div>

        <!-- 이미지 선택 -->
        <div class="form_group">
            <label for="f1" :class="{'input_label': f1}">프로필사진</label>

            <div class="Img">
                <!-- 프로필 기본 이미지 -->
                <span v-if="!f1">
                    <img style="width:150px; height:150px; border-radius:50%;" src="../../assets/imageadd.png">
                </span>
                <!-- 선택한 프로필 이미지 -->
                <span v-else>
                    <img style="width:150px; height:150px; border-radius:50%;" :src="f1">
                </span>

                
                <div class="button_group">
                    <input type="file" accept="image/*" id="f1" style="display: none" ref="fileInput" @change="profileImg">
                    <button class="s small Btn" @click="editImg">이미지 선택</button>

                    <button class="c small Btn" @click="cancelProfileImg" style="margin-left:8px;">취소</button> 
                </div>
            </div>
        </div>

        <!-- 가입버튼 -->
        <div class="join_btn_box"> 
        <!-- 발표용버튼 -->
        <button v-on:click ="join" :class="joinBtnClass" :disabled="joinBtnDisabled">가입</button>

        <!-- 테스트용버튼 -->
        <!-- <button v-on:click="join" class="joinBtn" >가입</button> -->
        </div>
    </div>
    </div>
</template>

<script>
export default{
    name: 'MemJoin',
    data(){
        return{
            id: '',
            pwd:'',
            email:'',
            emailId:'',
            emailDomain:'',
            compKey:'',
            startTimer:'',
            authComplete:0,
            gender:'',
            nickname:'',
            msg:'',
            nickmsg:'',
            hasIdError:false,
            hasPwdError:false,
            hasEmailError:false,
            hasNicknameError:false,
            f1:'',
            file:[],
            isEnabled:false,
            iddp:'',
            nickdp:'',
        }
    },

    computed:{
        joinBtnClass(){
            return{
                // 순서대로: 아이디유효성통과 && 패스워드유효성통과 && 이메일유효성통과 && 닉네임유효성통과 && 인증번호통과 && 아이디, 패스워드, 이메일, 닉네임, 성별 값 있고 && 이메일인증통과 && id중복체크통과 && 닉네임중복체크통과
                // css이슈 때문에 joinBtn_disabled 두개 넣음
                // 부족한 실력탓에 코드가 길어요 
                'joinBtn': !this.hasIdError && !this.hasPwdError && !this.hasEmailError && !this.hasNicknameError && this.authKey === this.compKey && this.id && this.pwd && this.email && this.nickname && this.gender && this.authComplete === 0 && !this.iddp && !this.nickdp,
                'joinBtn_disabled': this.hasIdError || this.hasPwdError || this.hasEmailError || this.hasNicknameError || this.authKey !== this.compKey || !this.id || !this.pwd || !this.email || !this.nickname || !this.gender || this.authComplete !== 0 || this.iddp || this.nickdp
            }
        },
        joinBtnDisabled(){
            // return this.hasIdError || !this.hasPwdError || !this.hasEmailError || !this.hasNicknameError || this.authKey !== this.compKey || !(this.id && this.pwd && this.email && this.nickname && this.gender) || this.authComplete !== 0;
            return this.hasIdError || this.hasPwdError || this.hasEmailError || this.hasNicknameError || this.authKey !== this.compKey || !this.id || !this.pwd || !this.email || !this.nickname || !this.gender || this.authComplete !== 0 || this.iddp || this.nickdp ;
        },
    },

    methods:{

        //아이디 입력값 찍기
        logId(){
            console.log('id:' + this.id);
        },

        //이미지 불러오기
        profileImg(event){
            const file = event.target.files[0];
            if(file){
                this.f1 = URL.createObjectURL(file);
                console.log('f1:'+this.f1);
            }
        },

        //이미지변경
        editImg(){
            this.$refs.fileInput.click();
        },
        //이미지삭제
        cancelProfileImg(){
            this.f1 = ''; //이미지 데이터 초기화
            this.$refs.fileInput.value = ''; //파일 선택 초기화
        },

        //회원가입
        join(){
            const self = this;
            
            let formdata = new FormData();
            formdata.append('id', self.id)
            formdata.append('pwd', self.pwd)
            formdata.append('email', self.email)
            formdata.append('gender', self.gender)
            formdata.append('nickname', self.nickname)

            const file = document.getElementById('f1')
            //이미지O 회원가입
            if(file.files[0]){
                formdata.append('mf', file.files[0]);
    
                self.$axios.post('http://localhost:7878/members', formdata,
                { headers: { "Content-Type": "multipart/form-data" } })
                .then(function(res){
                    if(res.status == 200){
                        let dto = res.data.dto
                        console.log(dto.id);
                        location.href='/'
                    }else{
                        alert('에러코드:'+res.status)
                    }
                });
            }else{
                //이미지X 회원가입
                self.$axios.post('http://localhost:7878/members/omem', formdata)
                .then(function(res){
                    if(res.status == 200){
                        let d = res.data.d
                        console.log(d);
                        location.href='/'
                    }else{
                        alert('에러코드:'+res.status)
                    }
                });
            }
        },

        //중복체크
        idcheck(){
            const self =this;
            if(self.id.trim() === ''){ //trim: 문자열 양쪽 끝 공백제거(아이디 입력 후 다 지우거나, 한글 첫글자시 오류 발생해서 넣은 코드)
                // self.msg = '아이디를 입력하시오.'
                return;
            }
            self.$axios.get('http://localhost:7878/members/check/'+self.id)
            .then(function(res){
                if(res.status == 200){
                    if(res.data.tf){
                        self.msg='사용가능한 아이디'
                        // alert('사용가능한 아이디')
                        self.iddp = false; // 중복 아이디가 아님
                    }else{
                        self.msg='중복된 아이디'
                        // alert('중복된 아이디')
                        self.iddp = true; // 중복 아이디임

                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },

        //닉네임 중복체크
        nickcheck(){
            const self = this;
            if(self.nickname.trim() === ''){
                return;
            }
            self.$axios.get('http://localhost:7878/members/nickname/'+self.nickname)
            .then(function(res){
                if(res.status === 200){
                    if(res.data.tf === true){
                        self.nickmsg = '사용가능한 닉네임';
                        self.nickdp = false; // 중복 닉네임이 아님
                    }else{
                        self.nickmsg = '중복된 닉네임';
                        self.nickdp = true; // 중복 닉네임임
                    }
                    console.log(res.data.tf)
                    console.log("닉네임:"+self.nickname)
                }else{
                    alert('에러코드:' + res.status)
                }
            });
        },

        //아이디 정규식(8~12자리 이상 영문+숫자, 영문, 숫자X)
        validateId(event){
            const id = event.target.value
            const pattern = /^(?=.*[A-Za-z])[A-Za-z\d]{8,12}$/;
            this.hasIdError = !pattern.test(id);
            console.log("id:" + this.hasIdError);
            // this.enabledState();
            // return pattern.test(id);
        },

        //비밀번호 정규식
        validatePwd(event){
            //4~12자리, 공백X, 한글X, 영문+숫자, 영대문자1개 포함, 특수문자포함
            const pwd = event.target.value
            const pattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{4,12}$/;
            this.hasPwdError = !pattern.test(pwd);
            // this.enabledState();
        },

        //이메일 정규식
        validateEmail(event){
            const emailId = event.target.value
            const pattern = /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-Za-z0-9\\-]+/;
            this.hasEmailError = !pattern.test(emailId);
            // this.enabledState();
        },

        //닉네임 정규식
        validateNickname(event){
            //3~8자리, 공백X, 특수문자X 외 다 가능
            const nickname = event.target.value
            const pattern = /^(?!\s)(?!.*\s$)(?!.*[!@#$%^&*(),.?":{}|<>])(?!.*[ㄱ-ㅎㅏ-ㅣ])[^\s]{3,8}$/;
            this.hasNicknameError = !pattern.test(nickname);
            // this.enabledState();
        },

        //인증번호 확인
        // validateChkKey(){
        //     this.isEnabled = !this.hasIdError && !this.hasPwdError && !this.hasEmailError && !this.hasNicknameError && this.authComplete === 0;
        // },

        // enabledState(){
        //     if(this.hasIdError || this.hasPwdError){
        //         this.isEnabled = false;
        //     }else{
        //         this.isEnabled = true;
        //     }
        // },

        //이메일 인증
        authEmail() {
            const self = this;
            clearInterval(self.startTimer);
            // self.email = self.emailId + '@' + self.emailDomain;
            self.email = self.emailId;
            self.$axios.get('http://localhost:7878/members/email/' + self.email)
            .then(function(res){
                if(res.status == 200){
                    let exist = res.data.exist;
                    let key = res.data.key;
                    let authEmail = document.getElementById('authEmail');
                    let authKey = document.getElementById('authKey');
                    let checkKey = document.getElementById('checkKey');
                    let displayedTime = document.getElementById('displayedTime');
                    if(key == null) {
                        alert(exist);
                    } else {
                        authEmail.innerText = '인증번호 재발급'
                        authKey.disabled = false;
                        authKey.focus();
                        checkKey.style.display = "inline-flex"; // 인증번호 확인 버튼 나타내기
                        checkKey.disabled = false;
                        self.compKey = key;
                        let secondsLeft = 180; // 3분
                        self.startTimer = setInterval(function() {
                            let minutes = Math.floor(secondsLeft / 60);
                            let seconds = secondsLeft % 60;
                            let timerText = minutes + "분" + seconds + "초";
                            displayedTime.style.color = 'black';
                            displayedTime.innerText = timerText;
                            if(secondsLeft <= 0) {
                                clearInterval(self.startTimer);
                                authKey.disabled = true;
                                checkKey.disabled = true;
                                self.compKey = '';
                            }
                            secondsLeft --;
                        }, 1000);

                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },

        //이메일 인증 완료(원본)
        // checkKey() {
        //     const self = this;
        //     let displayedTime = document.getElementById('displayedTime');
        //     if(self.authKey == self.compKey) {
        //         clearInterval(self.startTimer);
        //         displayedTime.innerText = "인증이 완료되었습니다."
        //         self.authComplete = 0;
        //     } else {
        //         alert('인증번호가 틀렸습니다.')
        //         self.authComplete = 1;
        //     }
        // },


        //이메일 인증 완료
        checkKey() {
            const self = this;
            let displayedTime = document.getElementById('displayedTime');
            let checkKeyBtn = document.getElementById('checkKey');
            let authKey = document.getElementById('authKey');

            if(self.authKey == self.compKey) {
                clearInterval(self.startTimer);
                displayedTime.innerText = "인증이 완료되었습니다."
                self.authComplete = 0;
                checkKeyBtn.style.display = "none"; // 인증번호 확인 버튼 숨기기
                checkKeyBtn.disabled = true; // 인증번호 확인 버튼 비활성화
                authKey.disabled = true; // 인증번호 입력창 비활성화
            } else {
                alert('인증번호가 틀렸습니다.')
                self.authComplete = 1;
            }
        },

        //로그인, 패스워드 폼 포커스시
        cPlaceholder(event){
            event.target.previousElementSibling.classList.add('actvie');
            event.target.placeholder='';
        },
        rPlaceholder(event){
            const inputField = event.target;
            const label = inputField.previousElementSibling;
            if(!inputField.value){
                label.classList.remove('active');
                if(inputField.id === 'id'){
                    inputField.placeholder = '아이디';
                }else if(inputField.id === 'pwd'){
                    inputField.placeholder = '패스워드';
                }else if(inputField.id === 'email'){
                    inputField.placeholder = '예) intheham@tistory.com';
                }else if(inputField.id === 'nickname'){
                    inputField.placeholder = '닉네임';
                }
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

#container{
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}
#myjoin{
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 40px;
    border: 1px solid #ddd;
    border-radius: 10px;
    background-color: #ffffff;
    max-width: 400px;
    margin: 0 auto;
}

/* 사용가능한 아이디입니다 */
.input_idcheck{
    display: block;
    color:#000;
    margin-top: 5px;
    font-size: 13px;
    text-align: center;
}

/* 사용가능한 닉네임입니다 */
.input_nickcheck{
    display: block;
    color:#000;
    margin-top: 5px;
    font-size: 13px;
    text-align: center;
}
/* 회원가입 */
.join_title{
    display: flex;
    padding: 24px 0 23px 0;
    padding-bottom: 46px;
    text-align: center;
    font-size: 28px;
    font-weight: bold;
}

.form_group{
    margin-bottom:15px;
    width: 100%;
}

label{
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
    text-align: left;
    font-size: 13px;
    padding-left: 8px;
}

.input_label_error{
    color:#f15746
}
.input_field{
    position: relative;
    padding: 8px;
    width: 96%;
    border: none;
    border-bottom: 1px solid #ebebeb;
    background-color: transparent;
    outline: none;
}

.input_field_error{
    position: relative;
    padding: 8px;
    width: 96%;
    border: none;
    border-bottom: 1px solid #f15746;
    background-color: transparent;
    outline: none;
}

.input_field:focus{
    border-bottom: 2px solid #000000;
}

/* 이메일인증 버튼 */
.authEmail{
    display: flex;
    border: none;
    background: none;
    text-decoration: underline;
    cursor: pointer;
    color: #000;
    font-weight: bolder;
    font-size: 13px;
    text-align: right;
    margin-left: auto;
    justify-content: flex-end;
}

.authEmail_error{
    display: flex;
    border: none;
    background: none;
    text-decoration: underline;
    color: #ebebeb;
    font-weight: bolder;
    font-size: 13px;
    text-align: right;
    margin-left: auto;
    justify-content: flex-end;
}

/* 이메일인증 확인버튼 */
.checkKey_btn{
    display: flex;
    justify-content: flex-end;
}

.checkKey{
    display: flex;
    justify-content: flex-end;
    border: none;
    background: none;
    text-decoration: underline;
    color: #000000;
    font-weight: bolder;
    font-size: 13px;
    margin-left: auto;
    cursor: pointer;
}

.checkKey_disabled{
    display: none;
}

/* 타이머 */
#timer{
    display:flex;
    flex-direction: column;
    justify-content: center;
}

/* 유효성 검사 메시지 */
.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
}
.Btn{
    display: inline-flex;
    cursor: pointer;
    align-items: center;
    justify-content: center;
    vertical-align: middle;
    text-align: center;
    color: rgba(34,34,34,.8);
    background-color: #fff;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}

.small{
    font-size: 12px;
    padding: 0 14px;
    height: 34px;
    border-radius: 10px;
    border: 1px solid #d3d3d3;
}

.small:hover{
    background-color: #85b380;
    color:#fff;
    transition: .5s;
}

/* 가입버튼*/
/* .joinBtnClass{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    font-size: 16px;
    background-color: #000000;
    color: #fff;
    font-weight: bold;
    cursor: pointer;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
} */

.joinBtn{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    font-size: 16px;
    background-color: #000000;
    color: #fff;
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}

.joinBtn_disabled{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    font-size: 16px;
    background-color: #ebebeb;
    color: #fff;
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}

.joinBtn:hover{
    cursor:pointer;
    background-color: #85b380;
    color:#fff;
    transition: .5s;
}
.join_btn_box{
    width: 100%;
}

.button_group{
    margin-left: auto;
    margin-top: auto;
}

.Img{
    display: flex;
    justify-content: flex-end;
}


  
h3 {
    margin: 40px 0 0;
  }
  ul {
    list-style-type: none;
    padding: 0;
  }
  li {
    display: inline-block;
    margin: 0 10px;
  }
  a {
    color: #42b983;
  }
</style>