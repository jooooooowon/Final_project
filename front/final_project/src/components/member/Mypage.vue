<template>
    <br><br>
    <div id="container">
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
                                <img :src= "profileImg()" style="width:100%; height:100%; border-radius: 50%;"><br/>
                                <!-- <input type="file" id="newimg" accept="img/*" @change="changeImg" v-show="isVisible"><br/> -->
                            </div>
                            <div class="profileDetail">
                                    
                                <div style="font-size: 18px; margin-top: 12px; font-weight: bold;">{{ nickname }}</div>
                                <!-- <input type="text" v-model="nickname" style=" border: none; outline: none;"> -->

                                    <div class="profileBtnBox" style="margin-top: 12px; font-weight: bold;">

                                            <input type="file" accept="image/*" style="display: none" ref="fileInput" @change="changeImg">
                                            <button class="edit Img btn small" @click="editImg">이미지 변경</button>

                                            <button class="del Img btn small" style="margin-left:8px;" @click="delImg" >삭제</button>
                                    </div>
                                </div>
                            </div>
                        <div class="profile_info" style="padding-top:38px; max-width:480px;">
                            <div class="profile_group">
                                <h4 class="group_title" style="font-size: 18px;">프로필 정보</h4>

                                <div class="unit">
                                    <h5 class="title">아이디</h5> 
                                    <div class="unit_content">
                                          <input type="text" v-model="id" class="info id_content" style="cursor: auto;" readonly>
                                    </div>
                                </div>

                                <div class="unit" v-show="!showModifyPwd">
                                    <h5 class="title">비밀번호</h5> 
                                    <div class="unit_content">
                                        <input type="password" v-model="pwd" class="info pwd_content" style="cursor: auto;" readonly>
                                    
                                    <button class="btn btn_modify small" @click="modifyPwd">변경</button>
                                    </div>
                                </div>
                                <div class="modify pwd" v-show="showModifyPwd">
                                    <div class="input_box">
                                        <h6 class="input_title">비밀번호</h6>
                                        <div class= "input_item">
                                            <input type="text" v-model="modifiedPwd" :class="{'info input_text': !hasPwdError, 'info input_text_error': hasPwdError}" @input="validatePwd($event)">
                                        </div>
                                        <p class="input_error" v-if="hasPwdError">대문자, 영문, 숫자, 특수문자를 조합해서 입력해주세요. (8-12자)</p>
                                        
                                        <div class="modify_btn_box">
                                            <button class="btn medium cancel" @click="cancelModifyPwd">취소</button> 
                                            <!-- <button class="btn medium save" @click="saveModifyPwd">저장</button> -->

                                            <button :class="[spbutton.spbtn ? 'btn medium save' : 'medium spbtn_disabled', 'spbutton']" @click="saveModifyPwd" :disabled="spbutton.spbtn_disabled">저장</button>
                                            <!-- <button v-on:click ="login" :class="loginBtnClass" :disabled="loginBtnDisalbed">로그인</button> -->
                                        </div>
                                    </div>
                                </div>

                                <div class="unit">
                                    <h5 class="title">이메일</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="email" class="info email_content" style="cursor: auto;" readonly>
                                    </div>

                                </div>
                                <div class="unit" v-show="!showModifyNickname">
                                    <h5 class="title">닉네임</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="nickname" class="info nickname_content" style="cursor: auto;" readonly>
                                    
                                    <button class="btn btn_modify small" @click="modifyNickname">변경</button>

                                    </div>
                                </div>
                                <div class="modify nickname" v-show="showModifyNickname">
                                    <div class="input_box">
                                        <h6 class="input_title">닉네임</h6>
                                        <div class= "input_item">
                                            <input type="text" v-model="modifiedNickname" :class="{'info input_text': !hasNicknameError, 'info input_text_error': hasNicknameError}" @input="nickcheck(); validateNickname($event)">

                                            <p class="input_error" v-if="hasNicknameError">3~8자로 입력해주세요.</p>
                                            <p class="input_nickcheck" v-else>{{ nickmsg }}</p>
                                        </div>
                                        


                                        <div class="modify_btn_box">
                                            <button class="btn medium cancel" @click="cancelModifyNickname">취소</button> 
                                            <!-- <button class="btn medium save" @click="saveModifyNickname">저장</button> -->



                                            <!-- <button :class="[sbutton.sbtn ? 'btn medium save' : 'btn medium save_disabled', 'sbutton']" @click="saveModifyNickname" :disabled="sbutton.sbtn_disabled">저장</button> -->
                                            <button :class="[sbutton.sbtn ? 'btn medium save' : 'medium sbtn_disabled', 'sbutton']" @click="saveModifyNickname" :disabled="sbutton.sbtn_disabled">저장</button>

                                            <!-- <button v-on:click ="login" :class="loginBtnClass" :disabled="loginBtnDisalbed">로그인</button> -->

                                        </div>
                                    </div>
                                </div>

                                <div class="unit">
                                    <h5 class="title">성별</h5> 
                                    <div class="unit_content">
                                        <input type="text" v-model="gender" class="info gender_content" style="cursor: auto;" readonly>
                                    </div>
                                </div>
                                <div class="out">
                                    <button v-on:click="out" class="outBtn">회원 탈퇴</button>
                                </div>
                                <!-- pwd:<input type="password" v-model="pwd"><br/> -->
                                <!-- email:<input type="text" v-model="email" readonly><br/> -->
                                <!-- nickname:<input type="text" v-model="nickname"><br/> -->
                                <!-- gender:{{ gender }}<br/> -->
                            </div>
                        </div>   
                    </label>
                        <div>

                        </div>
                </div>
                
            
                <!-- <button v-on:click="edit">수정</button> -->
                <!-- <button v-on:click="logout">로그아웃</button> -->
            </div>
        <!-- </div> -->
    <!-- </div> -->
    </div>
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
            defaultImg: require('@/assets/userImg.png'),
            previewImg:'',
            nickmsg:'',
            modifiedNickname:'',
            showModifyPwd: false,
            showModifyNickname: false,
            hasNicknameError: false,
            hasPwdError: false,
            nickdp:'',
        }
    },

    created:function(){
        let token = sessionStorage.getItem('token')
        
        const self = this;

        self.$axios.get('http://localhost:7878/members/'+self.num,
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
                    alert("여기가 아닌가?")
                    sessionStorage.removeItem("memnum");
                    sessionStorage.removeItem("token");
                    alert('없는 아이디거나 만료된 토큰')
                    location.href='/'
                }
            }else{
                alert('에러코드:'+self.status)
            }
        });
    },
    
    computed:{
        sbutton(){
            return{
                'sbtn': !this.hasNicknameError && !this.nickdp && this.nickname,
                'sbtn_disabled': this.hasNicknameError || this.nickdp || !this.nickname
            }
        },
        sbuttonDisalbed(){
            return this.hasNicknameError || this.nickdp || !this.nickname;
        },
        spbutton(){
            return{
                'spbtn': !this.hasPwdError && this.nickname,
                'spbtn_disabled': this.hasPwdError || !this.nickname
            }
        },
        spbuttonDisalbed(){
            return this.hasPwdError || !this.nickname;
        }
    },

    methods:{

        //변경버튼 클릭시 변경창 보여주기
        modifyPwd(){
            this.originalPwd = this.pwd;
            this.modifiedPwd = this.pwd;
            this.showModifyPwd = true;
        },

        //취소버튼 클릭시 기존비밀번호+ 기존변경창 보여주기
        cancelModifyPwd(){
            this.modifiedPwd = this.originalPwd;
            this.showModifyPwd = false;
        },

        //수정버튼
        saveModifyPwd(){
            this.pwd = this.modifiedPwd;
            this.edit();
        },

        //변경버튼 클릭시 변경창 보여주기
        modifyNickname(){
            this.originalNickname = this.nickname;
            this.modifiedNickname = this.nickname;
            this.showModifyNickname = true;
        },

        //취소버튼 클릭시 기존비밀번호+ 기존변경창 보여주기
        cancelModifyNickname(){
            this.modifiedNickname = this.originalNickname;
            this.showModifyNickname = false;
        },

        //수정버튼
        saveModifyNickname(){
            this.nickname = this.modifiedNickname;
            this.edit();
        },

        //이미지변경
        editImg(){
            this.$refs.fileInput.click();
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
                    self.edit();
                };
                reader.readAsDataURL(file);
            }
        },

        //프로필사진
        profileImg(){
            if(this.img != null){
                return 'http://localhost:7878/members/imgs/'+ this.num;
            } else{
                return require('@/assets/userImg.png')
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
            self.$axios.put('http://localhost:7878/members',formdata,
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
            let answer = confirm('탈퇴하시겠습니까?')
            if(answer){
            self.$axios.delete('http://localhost:7878/members/'+self.num,
            {headers:{'token':token}})
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        self.delImg()
                        alert('탈퇴완료')
                        self.logout()
                    }else{
                        alert('배틀투표에 선정되어 탈퇴가 불가능합니다.')
                    }
                }else{
                    alert('에러코드:'+res.status)
                }
            })
        }else{
            alert('탈퇴 취소되었습니다.')
        }
        },

        //이미지 삭제
        delImg(){
            const self = this;
            self.$axios.delete('http://localhost:7878/members/imgs/'+self.num)
            .then(function(res){
                if(res.status == 200){
                    if(res.data.flag){
                        self.img = null; //이미지경로 null로 설정하여 기본 이미지로 변경
                        self.uploadImg = null; //업로드된 이미지 초기화
                        location.reload();
                    }
                }else{
                    alert('에러코드:' + res.status)
                }
            });
        },

        //닉네임 중복체크
        nickcheck(){
            const self = this;
            if(self.modifiedNickname.trim() === ''){
                return;
            }
            self.$axios.get('http://localhost:7878/members/nickname/'+self.modifiedNickname)
            .then(function(res){
                if(res.status === 200){
                    if(res.data.tf === true){
                        self.nickmsg = '사용가능한 닉네임';
                        self.nickdp = false;
                    }else{
                        self.nickmsg = '중복된 닉네임';
                        self.nickdp = true;
                    }
                    console.log(res.data.tf)
                    console.log("닉네임:"+self.modifiedNickname)
                }else{
                    alert('에러코드:' + res.status)
                }
            });
        },

        //닉네임 중복체크
        // nickcheck(){
        //     const self = this;
        //     if(self.nickname.trim() === ''){
        //         return;
        //     }
        //     self.$axios.get('http://localhost:7878/members/nickname/'+self.nickname)
        //     .then(function(res){
        //         if(res.status === 200){
        //             if(res.data.tf === true){
        //                 self.nickmsg = '사용가능한 닉네임';
        //             }else{
        //                 self.nickmsg = '중복된 닉네임';
        //             }
        //             console.log(res.data.tf)
        //             console.log("닉네임:"+self.nickname)
        //         }else{
        //             alert('에러코드:' + res.status)
        //         }
        //     });
        // },


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
        },

        //비밀번호 정규식
        validatePwd(event){
            const pwd = event.target.value;
            const pattern = /^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()])[A-Za-z\d!@#$%^&*()]{8,12}$/;
            this.hasPwdError = !pattern.test(pwd);
            console.log("pwd : " +this.hasPwdError)
            // this.enabledState();
        },

        //닉네임 정규식
        validateNickname(event){
            //3~8자리, 공백X, 특수문자X 외 다 가능
            const modifiedNickname = event.target.value
            const pattern = /^(?!\s)(?!.*\s$)(?!.*[!@#$%^&*(),.?":{}|<>])(?!.*[ㄱ-ㅎㅏ-ㅣ])[^\s]{3,8}$/;
            this.hasNicknameError = !pattern.test(modifiedNickname);
            // this.enabledState();
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
#container{
    font-family: 'PyeongChang-Regular';
    font-weight: normal;
}
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

/* 유효성미통과시검사버튼 */
.sbtn_disabled{
    display: inline-flex;
    padding: 0 38px;
    height: 42px;
    border-radius: 12px;
    border: none;
    background-color: #ebebeb;
    color: rgba(34,34,34,.8);
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
    justify-content: center;
    vertical-align: middle;
    text-align: center;
    align-items: center;
    margin-left: 8px;
    pointer-events: none;
}

.spbtn_disabled{
    display: inline-flex;
    padding: 0 38px;
    height: 42px;
    border-radius: 12px;
    border: none;
    background-color: #ebebeb;
    color: rgba(34,34,34,.8);
    font-weight: bold;
    font-family: 'PyeongChang-Regular';
    justify-content: center;
    vertical-align: middle;
    text-align: center;
    align-items: center;
    margin-left: 8px;
    pointer-events: none;
}

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

.medium{
    padding: 0 38px;
    height: 42px;
    border-radius: 12px;
    font-size: 14px;
    font-weight: 500;
    border: 1px solid #d3d3d3;
}

.medium:hover{
    background: #85b380;
    color:#fff;
    transition: .5s;
}

.cancel{
    border: 1px solid #ebebeb;
}

.save{
    color: #ffffff;
    background-color: #222;
    margin-left: 8px;
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
    outline: none;
}

.input_title{
    font-size: 13px;
    font-weight: bold;
}

.modify.pwd{
    padding-top: 25px;
}

.modify.nickname{
    padding-top: 25px;
}

.input_text{
    width: 99%;
    border-bottom: 1px solid #ebebeb;
    padding-bottom: 20px;
}
.modify_btn_box{
    padding-top: 25px;
    text-align: center;
}
.out{
    display: flex;
    justify-content: right;
    padding-top: 25px;
}
.outBtn{
    background-color: #fff;
    color: rgba(34,34,34,.5);
    border: none;
    outline: none;
    font-size: 13px;
    text-align: center;
    text-decoration: underline;
    cursor: pointer;
    /* padding-right: 15px; */
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
.input_nickcheck{
    display: block;
    color:#000;
    margin-top: 5px;
    font-size: 13px;
    text-align: center;
}

.input_error{
    display: block;
    color:#f15746;
    margin-top: 5px;
    text-align: left;
    font-size: 11px;
}
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