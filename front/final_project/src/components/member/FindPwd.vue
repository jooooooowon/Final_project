<template>
    <div class="content">
        <div class="help_area">
            <div class="title">
                <h2>비밀번호 찾기</h2>
            </div>

            <div class="notice">
                <p>가입 시 등록하신 아이디와 이메일을 입력하시면</p>
                <p>이메일로 임시 비밀번호를 전송해 드립니다.</p>
            </div>

            <div class="input_box">
                <div :class="{'input_title':!hasIdError, 'input_title_error': hasIdError}">
                    <p>ID</p>
                </div>
                <input type="text" id="id" v-model="id" placeholder="가입하신 아이디" :class="{'input_field':!hasIdError, 'input_field_error':hasIdError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateId($event)"><br/>

                <p class="input_error" v-if="hasIdError">아이디를 정확히 입력해주세요.</p>
            </div>
            
            <div class="input_box">
                <div :class="{'input_title':!hasEmailError, 'input_title_error':hasEmailError}">
                    <p>이메일 주소</p>
                </div>
                <input type="text" id="email" v-model="email" placeholder="예) intheham@tistory.com" :class="{'input_field':!hasEmailError, input_field_error:hasEmailError}" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)" @input="validateEmail($event)"><br/>

                <p class="input_error" v-if="hasEmailError">이메일 주소를 정확히 입력해주세요.</p>
            </div>

            <div class="btn_box">
                <button v-on:click="find" :class="btn" :disabled="btn_disabled">인증번호 발송하기</button>
            </div>
        </div>
    </div>
</template>

<script>
export default{
    data(){
        return{
            id:'',
            email:'',
            msg:'',
            hasIdError: false,
            hasEmailError: false
        }
    },

    computed:{
        btn(){
            return{
                'btn': !this.hasIdError && !this.hasEmailError && (this.id && this.email),
                'btn_disabled': this.hasIdError || this.hasEmailError || !(this.id && this.email)
            }
        },
        btn_disabled(){
            return this.hasIdError || this.hasEmailError || !(this.id && this.email)
        }
    },

    methods:{
        find(){
            const self = this;
            console.log(this.id)
            let form = new FormData();
            form.append("id",self.id);
            self.$axios.post('http://localhost:8081/members/findPwd/' + self.email,
            form)
            .then(function(res){
                console.log(self.id)
                console.log(self.email)
                if(res.status==200){
                    let randomPwd = res.data.randomPwd.pwd;
                    // let id = res.data.randomPwd
                    // let email = res.data.randomPwd
                    alert(randomPwd)
                    if(randomPwd!=null){
                        self.id = randomPwd.id;
                        self.email = randomPwd.email;
                        self.msg='';
                    }else{
                        self.msg='';
                    }
                }else{
                    alert('에러코드:'+self.status)
                }
            })
            
            //데이터 일치하지 않는 경우 오류(let randomPwd = res.data.randomPwd.pwd;)
            .catch(function(error){
                console.log(error);
                alert('일치하는 사용자 정보를 찾을 수 없습니다. ')
                alert('오류가 발생했습니다.'+error);
            });
        },

        //아이디 정규식(8~12자리 이상 영문+숫자, 영문, 숫자X)
        validateId(event){
            const id = event.target.value
            const pattern = /^(?=.*[A-Za-z])[A-Za-z\d]{8,12}$/;
            this.hasIdError = !pattern.test(id);
            console.log("id:" + this.hasIdError);
            this.enabledState();
        },
        //이메일 정규식
        validateEmail(event){
            const emailId = event.target.value
            const pattern = /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-Za-z0-9\\-]+/;
            this.hasEmailError = !pattern.test(emailId);
            this.enabledState();
        },

        enabledState(){
            if(this.hasIdError || this.hasEmailError){
                this.isEnabled = false;
            }else{
                this.isEnabled = true;
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
                    inputField.placeholder = 'ID';
                }else if(inputField.id === 'pwd'){
                    inputField.placeholder = 'Password';
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
.content{
    color:#222;
    margin-left: auto;
    margin-right: auto;
    max-width: 1280px;
}

.help_area{
    margin: 0 auto;
    padding: 60px 0 160px;
    width: 400px;
}

.title{
    font-size: 32px;
    font-weight: bold;
    text-align: center;
    letter-spacing: -1.48px;
    border-bottom: 2px solid #000;
    padding-bottom: 40px;
}


.notice{
    font-size: 14px;
    padding: 40px 0 30px;
    text-align: left;
    letter-spacing: -.21px;
    line-height: 18px;
}

.input_box{
    position: relative;
    padding: 10px 0 14px;
}

.input_title{
    color: #222;
    font-size: 14px;
    font-weight: bold;
    text-align: left;
    line-height: 18px;
}
.input_title_error{
    color:#f15746;
    font-size: 14px;
    font-weight: bold;
    text-align: left;
    line-height: 18px;
}

.input_field{
    border: none;
    border-bottom: 1px solid #ebebeb;
    width: 100%;
    padding: 8px 0;
    font-size: 15px;
    outline: none;
}
.input_field_error{
    border: none;
    border-bottom: 1px solid #f15746;
    width: 100%;
    padding: 8px 0;
    font-size: 15px;
    outline: none;
}

.input_field:focus{
    border-bottom: 2px solid #000;
}

/* 유효성 검사 메시지 */
.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
}

.btn_box{
    padding-top: 40px;
}
.btn{
    border: none;
    background: #000;
    width: 100%;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    height: 52px;
    border-radius: 12px;
    cursor: pointer;
}
.btn_disabled{
    border: none;
    background: #ebebeb;
    width: 100%;
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    height: 52px;
    border-radius: 12px;
}
</style>