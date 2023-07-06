<template>
    <!-- <div class="layout_base"> -->
        <!-- <div class="container"> -->
            <div class="content_title border">
                <div class="title">
                    <h3>프로필 관리</h3>
                </div>
            </div>
            <div class="content" id="Mypage">
                <div class="memberInfo">
                    
                    <label for ="newimg">
                        <div class="userProfile">
                            <div class="profile_thumb">
                                <img :src= "profileImg()" style="width:100%; height:100%; cursor: pointer; border-radius: 50%;"><br/>
                                <!-- <input type="file" id="newimg" accept="img/*" @change="changeImg" v-show="isVisible"><br/> -->
                            </div>
                            <div class="profileDetail">
                                    
                                    <div style="font-size: 18px; margin-top: 12px; font-weight: bold;">{{ nickname }}</div>
                                    <!-- <input type="text" v-model="nickname" style=" border: none; outline: none;"> -->

                                <div class="profileBtnBox" style="margin-top: 12px; font-weight: bold;">
                                    <button class="edit Img btn small">이미지 변경</button> <button class="del Img btn small">삭제</button>
                                </div>
                            </div>
                        </div>
                        <div class="profile_info" style="padding-top:38px; max-width:480px;">
                            <div class="profile_group">
                                <h4 class="group_title" style="font-size: 18px;">프로필 정보</h4>

                                <div class="unit">
                                    <h5 class="title">아이디</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="id" class="info id_content" readonly>
                                    </div>
                                </div>

                                <div class="unit" v-show="!showModify">
                                    <h5 class="title">비밀번호</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="pwd" class="info pwd_content">
                                    
                                    <button class="btn btn_modify small" @click="modifyPwd">변경</button>
                                    </div>
                                </div>
                                <div class="modify pwd" v-show="showModify">
                                    <div class="input_box">
                                        <h6 class="input_title">비밀번호</h6>
                                        <input type="text" v-model="pwd" class="info pwd_content">
                                        <p class="input_error"></p>
                                        <button class="btn medium">취소</button><button class="btn medium">저장</button>
                                    </div>
                                </div>

                                <div class="unit">
                                    <h5 class="title">이메일</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="email" class="info email_content" readonly>
                                    </div>
                                </div>
                                <div class="unit">
                                    <h5 class="title">닉네임</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="nickname" class="info nickname_content">
                                    
                                    <button class="btn btn_modify small">변경</button>

                                    </div>
                                </div>
                                <div class="unit">
                                    <h5 class="title">성별</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="gender" class="info gender_content" readonly>
                                    </div>
                                </div>
                                pwd:<input type="password" v-model="pwd"><br/>
                                email:<input type="text" v-model="email" readonly><br/>
                                nickname:<input type="text" v-model="nickname"><br/>
                                gender:{{ gender }}<br/>
                            </div>
                        </div>   
                    </label>
                        <div>

                        </div>
                </div>
                
               
            
                
                <button v-on:click="edit">수정</button>
                <button v-on:click="out">탈퇴</button>
                <button v-on:click="logout">로그아웃</button>
            </div>
        <!-- </div> -->
    <!-- </div> -->
</template>
<script>
export default{
    name: 'MyPage',
    data(){
        return{
            id:'',
            pwd:'',
            email:'',
            gender:'',
            nickname:'',
            num : sessionStorage.getItem('memnum'),
            img:'',
            uploadImg:'null',
            defaultImg: require('@/assets/default.jpg'),
            previewImg:'',
            showModify: false
        }
    },

    created:function(){
        let token = sessionStorage.getItem('token')
        
        const self = this;

        self.$axios.get('http://localhost:8081/members/'+self.num,
        {headers:{'token':token}})
        .then(function(res){
            if(res.status==200){
                let dto = res.data.dto
                if(dto!=null){
                    self.id=dto.id
                    self.pwd=dto.pwd
                    self.email=dto.email
                    self.gender=dto.gender
                    self.nickname=dto.nickname
                    self.img=dto.img
                }else{
                    alert('없는 아이디거나 만료된 토큰')
                }
            }else{
                alert('에러코드:'+self.status)
            }
        });
    },
    
    methods:{

        //변경버튼 클릭시 변경창 보여주기
        modifyPwd(){
            this.showModify = !this.showModify;
        },

        //프로필사진
        profileImg(){
            if(this.img != null){
                return 'http://localhost:8081/members/imgs/'+ this.num;
            } else{
                return require('@/assets/default.jpg')
            }
        },

        //내정보수정(비밀번호, 닉네임, 이미지)
        edit(){
            const self = this
            let formdata = new FormData();
            formdata.append('memnum',self.num)
            formdata.append('pwd',self.pwd)
            formdata.append('nickname', self.nickname)
            if(self.uploadImg){
                formdata.append('mf', self.uploadImg)
            }else{
                formdata.append('mf', null)
            }
             let token = sessionStorage.getItem('token')
            self.$axios.put('http://localhost:8081/members',formdata,
            {headers:{'token':token}})
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        let dto = res.data.dto
                        self.pwd = dto.pwd
                        self.nickname = dto.nickname
                        self.img = dto.img
                        location.reload()
                    }else{
                        alert("false가 넘어옴")
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        },

        //프로필 이미지 수정
        changeImg(event){
            const file = event.target.files[0];
            if(file){
                const reader = new FileReader();
                const self = this;
                reader.onload = function(){
                    self.previewImg = reader.result;
                    self.uploadImg = file;
                };
                reader.readAsDataURL(file);
            }
        },

        //로그아웃
        logout(){
            sessionStorage.removeItem('token')
            sessionStorage.removeItem('memnum')
            console.log('memnum')
            location.href='/'
        },

        //탈퇴
        out(){
            const self = this;
            let token = sessionStorage.getItem('token')
            self.$axios.delete('http://localhost:8081/members/'+self.num,
            {headers:{'token':token}})
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        alert('탈퇴완료')
                        self.logout()
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            });
        }
    }
}
</script>

<style scoped>
/* .layout_base{
    margin-top:100px;
}
.container{
    margin-left: auto;
    margin-right: auto;
    max-width: 1200px;
    padding: 40px 40px 160px;
} */
/* .memberInfo{
    display:flex;
    width:100%;
    border: 1px solid #ebebeb;
    border-radius: 10px;
    margin:0 auto;
    background-color:#ffffff;
} */

.content_title.border{
    padding:10px 0 16px;
    border-bottom: 3px solid #222;
}

.userProfile{
    display: flex;
    text-align: left;
    border-bottom: 1px solid #ebebeb;
    padding: 38px 0;
}

.profile_thumb{
    width: 90px;
    height: 90px;
    flex: none;
    margin-right: 12px;
}

.btn {
    display: inline-flex;
    cursor: pointer;
    align-items: center;
    justify-content: center;
    vertical-align: middle;
    text-align: center;
    color: rgba(34,34,34,.8);
    background-color: #fff;
}

.small{
    font-size: 12px;
    padding: 0 14px;
    height: 34px;
    border-radius: 10px;
    border: 1px solid #d3d3d3;
}

.unit{
    display:flex;
    flex-direction: column;
    padding: 25px 0 12px;
    position: relative;
    border-bottom: 1px solid #ebebeb;
}
.unit_content{
    width:100%;
    display:flex;
    justify-content: center;
    align-items: center;
    border: none;
}

.info{
    flex: 1;
    padding-top: 8px;
    padding-bottom: 8px;
    font-size: 16px;
    overflow: hidden;
    text-overflow: ellipsis;
    border: none;
}

.input_title{
    font-size: 13px;
}

/* .editImgBtn{
    border: 1px solid #d3d3d3;
    color: rgba(34, 34, 34, .8);
    font-size: 12px;
    display: inline-flex;
    cursor: pointer;
    align-items: center;
    justify-content: center;
    vertical-align: middle;
    text-align: center;
    background-color: #fff;
}
.delImgBtn{
    margin-left: 8px;
} */

h3{
    display: block;
    font-size: 24px;
    font-weight:bolder;
    text-align: left;
}
h5{
    font-size: 13px;
    font-weight: 400;
    color: rgba(34,34,34,.5);
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