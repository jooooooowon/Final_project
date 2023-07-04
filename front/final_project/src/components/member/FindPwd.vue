<template>
    <div class="content">
        <div class="help_area">
            <div class="title">
                <h2>비밀번호 찾기</h2>
            </div>

 
            <h3>ID</h3>
            <input type="text" v-model="id" placeholder="가입하신 아이디"><br/>
            <h3>이메일 주소</h3>
            <input type="text" v-model="email" placeholder="예) intheham@tistory.com"><br/>
            <button v-on:click="find">인증번호 발송하기</button>
        </div>
    </div>
</template>

<script>
export default{
    data(){
        return{
            id:'',
            email:'',
            msg:''
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
            });
        }
    }
}
</script>

<style scoped>
.content{
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
</style>