<template>
    <div id="container">
    <div class="content">
        <div class="help_area">
            <div class="title">
                <h2>아이디 찾기</h2>
            </div>

            <div class="notice">
                <p>가입 시 등록한 이메일을 입력하면</p>
                <p>아이디 일부를 알려드립니다.</p>
            </div>
            <div class="input_box">
                <div class="input_title">
                    <p>이메일</p>
                </div>
                <input type="text" id="email" v-model="email" placeholder="가입하신 이메일" class="input_field" @focus="cPlaceholder($event)" @blur="rPlaceholder($event)"><br/>
            </div>

            <div class="find-id">
                <div class="id-content">
                    <p v-if="idMasked">아이디: &nbsp;</p> {{ idMasked }} <br/>
                </div>
                <div class="find_btn">
                    <router-link to="../FindPwd" class="btn medium pbtn" style="display: flex; align-items: center; text-decoration: none;" v-if="idMasked">비밀번호찾기</router-link>
                    <router-link to="../Login" class="btn medium lbtn" style="display: flex; align-items: center; text-decoration: none;" v-if="idMasked">로그인</router-link> 
                </div>
                <!-- <a href="../FindPwd" v-if="idMasked">비밀번호찾기</a> -->
            </div>
            <div class="btn_box">
                <button v-on:click="find" class="big btn">아이디 찾기</button>
                <!-- <button v-on:click="find" class="btn_disabled">아이디 찾기</button> -->
            </div>
            
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
            idMasked:''
        }
    },
    
    methods:{
        find(){
            const self = this;
            self.$axios.get('http://localhost:8081/members/email/find/'+self.email)
            .then(function(res){
                if(res.status==200){
                    let dto = res.data.dto
                    if(dto!=null){
                        self.id=dto.id;
                        let str = self.id;
                        let idPre = str.substring(0, 3); //찾은 아이디 앞에 3자리 뽑기
                        self.idMasked = idPre+ '*'.repeat(str.length -3); //나머지는 *로 대체(repeat:문자열이 있는만큼 반복, length -3: 앞에서 세 자리 뺀 길이)
                    }else{
                        alert('일치하는 사용자 정보를 찾을 수 없습니다.')
                    }
                }else{
                    alert('에러코드:'+self.status)
                }
            })
            //데이터 없을 경우 에러 발생
            .catch(function(error){
                console.log(error);
                alert('일치하는 사용자 정보를 찾을 수 없습니다.');
            });

        },

        cPlaceholder(event){
            event.target.previousElementSibling.classList.add('active');
            event.target.placeholder='';
        },
        rPlaceholder(event){
            const inputField = event.target;
            const label = inputField.previousElementSibling;
            if(!inputField.value){
                label.classList.remove('active');
                if(inputField.id === 'email'){
                    inputField.placeholder = '가입하신 이메일';
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

.content{
    color:#222;
    margin-left: auto;
    margin-right: auto;
    max-width: 1280px;
}

.id-content{
    display: flex;
    justify-content: center;
    font-size: 25px;
    margin-bottom: 10px;
}
.help_area{
    margin: 0 auto;
    padding: 60px 0 160px;
    width:400px;
}

.title{
    font-size: 32px;
    font-weight: bold;
    text-align: center;
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
    color:#222;
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

.input_field:focus{
    border-bottom: 2px solid #000;
}

.btn_box{
    padding-top: 30px;
}
.btn{
    border: none;
    background: #000;
    /* width: 100%; */
    color: #fff;
    font-size: 16px;
    font-weight: bold;
    height: 52px;
    border-radius: 12px;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}

.btn_disabled{
    border: none;
    background: #ebebeb;
    width: 100%;
    color:#fff;
    font-size: 16px;
    font-weight: bold;
    height: 52px;
    border-radius: 12px;
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}
.big{
    padding:0px 40%;
}
.big:hover{
    background: #85b380;
    color:#fff;
    transition: .5s;
    cursor: pointer;
}

.medium{
    /* padding: 0 55px; */
    height: 42px;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 500;
    border: 1px solid #d3d3d3;
}

.pbtn{
    background-color: #fff;
    color: #222;
}
.lbtn{
    margin-left: 10px;
    padding: 0 25.5px;
}
.find_btn{
    display: flex;
    justify-content: center;
}

.medium:hover{
    background: #85b380;
    color:#fff;
    transition: .5s;
    cursor: pointer;
}

.find-id{
    margin-top: 10px;
}

p{
    display: inline-block;
    font-weight: bold;

}
</style>