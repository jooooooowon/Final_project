<template>
    <div id="container">
    <div id="login">
        <div class= "title_box">
            <h3 class="title">OTZZANG</h3>
        </div>
        
        <!-- 아이디 입력 폼 -->
        <div class="form_group">
            <!-- <label for ="id"  :class="{'input_label': !hasIdError, 'input_label_error': hasIdError}">아이디</label> -->
            <!-- <input type="text" id="id" v-model="id" placeholder="아이디" :class="{'input_field': !hasIdError, 'input_field_error': hasIdError }" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateId($event)"> -->
            <label for ="id"  :class="{'input_label': !hasIdError}">아이디</label>
            <input type="text" id="id" v-model="id"  :placeholder="idPlaceholder" class="input_field" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateId($event)">

            <!-- 아이디 유효성 검사 -->
            <!-- <p class="input_error" v-if="hasIdError">영문과 숫자 8자 이상 16자 이하로 입력해주세요.</p> -->
        </div>

        <!-- 패스워드 입력 폼 -->
        <div class="form_group">
            <label for="pwd" :class="{'input_label': !hasPwdError, 'input_label_error': hasPwdError}">패스워드</label>
            <div class="password-input-container">
                <input :type="hidePwd ? 'password' : 'text'" id="pwd" v-model="pwd" placeholder="패스워드" :class="{'input_field': !hasPwdError, 'input_field_error': hasPwdError }" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validatePwd($event)">
                
            </div>
            <!-- <div class="eye-icon" @click="toggleVisibility">
                <span class="material-symbols-outlined" style="font-size: 20px;" v-show="!hidePwd">visibility</span>
                <span class="material-symbols-outlined" style="font-size: 20px;" v-show="hidePwd">visibility_off</span>
            </div> -->
            <!-- 패스워드 유효성 검사 -->
            <p class="input_error" v-if="hasPwdError">대문자, 영문, 숫자, 특수문자를 조합해서 입력해주세요. (8-12자)</p>
        </div>
        
        <div class="view-pwd">
            <input type="checkbox" style="cursor: pointer;" @click="toggleVisibility" class="pwdbox"> <p style="font-size: 14px; display:flex; align-items: center;">비밀번호 표시</p>
        </div>

        <!-- 로그인 버튼 -->
        <button v-on:click ="login" :class="loginBtnClass" :disabled="loginBtnDisalbed">로그인</button>

        <!-- 유효성검사 없는 로그인버튼 -->
        <!-- <button v-on:click ="login"  :class="{'loginBtn': isEnabled}" >로그인</button> -->

        <!-- 회원가입, 아이디찾기, 비밀번호찾기 -->
        <ul class="look_box">
            <li class="look_list">
                <a href="../Join" class="look_link">가입</a>
            </li>
            <li>
              <div class="bar"></div>
            </li>
            <li class="look_list">
                <a href="../FindId" class="look_link">아이디 찾기</a>
            </li>
            <li>
              <div class="bar"></div>
            </li>
            <li class="look_list">
                <a href="../FindPwd" class="look_link">비밀번호 찾기</a>
            </li>
        </ul>
    </div>
</div>
</template>

<script>
export default{
    name: 'MemLogin',
    data(){
        return{
            id:'',
            pwd:'',
            hasIdError:false,
            hasPwdError:false,
            hidePwd:true,
            placehoder:'',
        }
    },

    computed:{
        loginBtnClass(){
            return{
                'loginBtn': !this.hasPwdError && (this.id && this.pwd),
                'loginBtn_disabled': this.hasPwdError || !(this.id && this.pwd)
            }
        },
        loginBtnDisalbed(){
            return this.hasPwdError || !(this.id && this.pwd);
        },
        idPlaceholder(){
            return this.id ? '' : '아이디';
        }
    },

    methods:{
        login(){
            const self = this;
            const form = new FormData();
            form.append('id', self.id)
            form.append('pwd',self.pwd)

            self.$axios.post('http://localhost:7878/members/login', form)
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        sessionStorage.setItem('token', res.data.token)
                        sessionStorage.setItem('memnum', res.data.memnum)
                        // alert('로그인 성공')
                        location.href='/'
                    }else{
                        alert('아이디 또는 비밀번호를 확인해주세요')
                        self.id=''
                        self.pwd=''
                        document.getElementById('id').placeholder='ID';
                        document.getElementById('pwd').placeholder='Password';
                        return;
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },
        
        //로그인, 패스워드 포커스시 초기화, 복구
        cPlaceholder(event){
            const inputField = event.target;
            if(inputField){
                const label = inputField.previousElementSibling;
                if(label){
                    label && label.classList.add('active');
                }
                inputField.placeholder='';
            }

            // const inpuField = event.target;
            // event.target.previousElementSibling.classList.add('active');
            // event.target.placeholder= '';
        },
        rPlaceholder(event){
            const inputField = event.target;
            if(inputField){
                const label = inputField.previousElementSibling;
                if(label){
                    label.classList.remove('active');
                }
                if(!inputField.value && !inputField.classList.contains('input_field_error')){
                    if(inputField.id === 'id'){
                        inputField.placeholder = '아이디';
                    } else if(inputField.id === 'pwd'){
                        inputField.placeholder = '패스워드';
                    }
                }
            }
            
            
            // const inputField = event.target;
            // const label = inputField.previousElementSibling;
            
            // if(!inputField.value){
            //     label.classList.remove('active');
            //     if(inputField.id ==='id'){
            //         inputField.placeholder = '아이디';
            //     }else if(inputField.id === 'pwd'){
            //         inputField.placeholder = '패스워드';
            //     }
            // }
        },

        //아이디 정규식(8~12자리 이상 영문+숫자, 영문, 숫자X)
        validateId(event){
            const id = event.target.value;
            const regex = /^(?=.*[A-Za-z])[A-Za-z\d]{8,12}$/;
            this.hasIdError = !regex.test(id);
            console.log("id : " +this.hasIdError)
            this.enabledState();
        },

        //비밀번호 정규식
        validatePwd(event){
            const pwd = event.target.value;
            const regex = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,12}$/;
            this.hasPwdError = !regex.test(pwd);
            console.log("pwd : " +this.hasPwdError)
            this.enabledState();
        },

        enabledState(){
            if(this.hasIdError || this.hasPwdError){
                this.isEnabled = false;
            }else{
                this.isEnabled = true;
            }
        },

        toggleVisibility() {
      this.hidePwd = !this.hidePwd;

      // 패스워드 필드에 포커스를 다시 줌
    //   this.$nextTick(() => {
    //     const pwdField = document.getElementById('pwd');
    //     if (pwdField) {
    //       pwdField.focus();
    //     }
    //   });
    }
    }
}
</script>

<style scoped>
/* 로고폰트 */
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

.title_box{
    width: 310px;
    height: 70px;
    display: flex;
    align-content: center;
    justify-content: center;
    margin-left: auto;
    margin-right: auto;
}
.title{
    font-family: "Flood Std";
    font-size: 50px;
    font-weight: bold;
    white-space: nowrap;
    text-overflow: ellipsis;
    text-align: center;
}
/* placeholder 색상 */
/* input::placeholder{
    color:#ebebeb
} */

.bar{
  width: 1px;
  height: 15px;
  background-color: #000000;
}

/* 로그인버튼 */
.loginBtn{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    background-color: #000000;
    color: #fff;
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
}

.loginBtn_disabled{
    display: block;
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 12px;
    background-color: #ededed;
    color: #fff;
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
}

.loginBtn:hover{
    cursor:pointer;
    background-color: #85b380;
    color:#fff;
    transition: .5s;
}

/* 로그인 틀 */
#login{
    display: flex;
    flex-direction: column;
    /* align-items: center; */
    padding: 40px;
    border: 1px solid #ddd;
    border-radius: 10px;
    background-color: #ffffff;
    max-width: 400px;
    margin: 55px auto;
}

/* 로고 */
/* h3 {
    margin: 0;
    margin-bottom: 20px;
    text-align: center;
} */

/* 아이디, 비밀번호 묶은 틀 */
.form_group{
    margin-bottom:25px;
    width: 100%;
}

/* ID, Password 라벨 */
label{
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
    text-align: left;
    font-size: 13px;
    padding-left: 8px;
    color:#222;
}

/* 유효성검사 통과 못하면 색상 변경 */
.input_label_error{
    color:#f15746
}

/* 아이디 입력창 */
.input_field{
    position: relative;
    width: 96%;
    padding: 8px;
    border: none;
    border-bottom: 1px solid #ebebeb;
    background-color: transparent;
    outline: none;
}

/* 아이디 입력창(유효성검사 통과X) */
.input_field_error{
    position: relative;
    width: 96%;
    padding: 8px;
    border: none;
    border-bottom: 1px solid #f15746;
    background-color: transparent;
    outline: none;
}

/* 아이디 입력창 포커스 시 아랫줄 굵어짐 */
#login .input_field:focus{
    border-bottom: 2px solid #000000;
}

/* 유효성 검사 문구 */
.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
    padding-left: 8px;
}

button {
    display: block;
    width: 100%;
    padding: 10px;
    height: 52px;
    border: none;
    border-radius: 12px;
    background-color: #000000;
    color: #ebebeb;
    font-size: 16px;
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}

.look_box{
    width: 100%;
    display: flex;
    justify-content: center;
    padding-top: 20px;
}

.look_list{
   display: inline-flex;
   align-items: flex-start;
   flex: 1;
}

.look_link{
    margin: auto;
    display: inline-flex;
    font-size: 13px;
    text-decoration: none;
    color: #222;
    letter-spacing: -.07px;
}

.password-input-container {
  position: relative;
}

.password-input-container .material-symbols-outlined {
  position: absolute;
  top: 50%;
  right: 10px;
  transform: translateY(-50%);
  pointer-events: none;
  font-size: 20px;
}

.eye-icon {
    position: absolute;
    top: 455px;
    right: 570px;
    transform: translateY(-50%);
    font-size: 20px;
    cursor: pointer;
}
.view-pwd{
    display: flex;
    position: relative;
    justify-content: flex-start;
    margin-bottom: 10px;
}
</style>