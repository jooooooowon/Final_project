<template>
    <div class="body-css">
        <h3 style="font-size: 25px">Outfit Of the Weather</h3><br/>
        <!-- 옷장에서 옷 꺼내서 이미지 저장시키기 버튼-->
        <label for="addLabel1">
            <span v-if="thumbImg1 == ''"><img src="../../assets/imageadd.png" class="thumimg"></span>
            <span v-else-if="thumbImg1 != ''"><img :src="thumbImg1" class="thumimg"></span>
        </label>
        <button v-on:click="modalOpen(1)" id="addLabel1" style="display:none"></button>&nbsp;
        <label for="addLabel2">
            <span v-if="thumbImg2 == ''"><img src="../../assets/imageadd.png" class="thumimg"></span>
            <span v-else-if="thumbImg2 != ''"><img :src="thumbImg2" class="thumimg"></span>
        </label>
        <button v-on:click="modalOpen(2)" id="addLabel2" style="display:none"></button>&nbsp;
        <label for="addLabel3">
            <span v-if="thumbImg3 == ''"><img src="../../assets/imageadd.png" class="thumimg"></span>
            <span v-else-if="thumbImg3 != ''"><img :src="thumbImg3" class="thumimg"></span>
        </label>
        <button v-on:click="modalOpen(3)" id="addLabel3" style="display:none"></button>&nbsp;
        <label for="addLabel4">
            <span v-if="thumbImg4 == ''"><img src="../../assets/imageadd.png" class="thumimg"></span>
            <span v-else-if="thumbImg4 != ''"><img :src="thumbImg4" class="thumimg"></span>
        </label>
        <button v-on:click="modalOpen(4)" id="addLabel4" style="display:none"></button>&nbsp;
        <label for="addLabel5">
            <span v-if="thumbImg5 == ''"><img src="../../assets/imageadd.png" class="thumimg"></span>
            <span v-else-if="thumbImg5 != ''"><img :src="thumbImg5" class="thumimg"></span>
        </label><br />
        <button v-on:click="modalOpen(5)" id="addLabel5" style="display:none"></button><br/>
        
        <!-- 날짜 입력 -->
        <input type="date" v-model="odate" id="date" style="font-family: 'PyeongChang-Regular';
        font-size:13px; width:162px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500;
        border-color: lightgray; border-radius: 5px;" data-placeholder="날짜 선택" aria-required="true" required><br /><br/>
        
        <!-- 날씨 입력 -->
        <input type="text" v-model="weather" id="weather" style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:160px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
        placeholder="날씨 입력 ex)맑음" onfocus="this.placeholder=''" onblur="this.placeholder='날씨 입력 ex)맑음'" maxlength="10"><br /><br/>
        
        <!-- 온도 입력 -->
        <input type="number" v-model="temp" id="temp" style="font-family: 'PyeongChang-Regular'; font-size:13.5px;
        width:161px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
            placeholder="온도 입력 ex)31.5" onfocus="this.placeholder=''" onblur="this.placeholder='온도 입력 ex)31.5'"><br /><br/>
        
        <!-- 커멘트 입력 -->
        Comment<br />
        <textarea v-model="comments" cols="30" rows="5" style="resize:none; font-weight: 500; border-color: lightgray; border-radius: 5px;
        font-family: 'PyeongChang-Regular';" maxlength="100"
            placeholder="커멘트는 최대 100자까지 입력 가능합니다." onfocus="this.placeholder=''" onblur="this.placeholder='커멘트는 최대 100자까지 입력 가능합니다.'"></textarea><br />
        
        <!-- 등록 버튼 -->
        <br/><button v-on:click="addBoard">등록</button><br/><br/><br/>

        <!-- 옷장에서 옷 선택하는 모달창 -->
        <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
            <div class="modal-container" @click.stop="" id="container">
                <div class="maintags">
                    <ul v-for="(maintag, index) in maintags" v-bind:value="maintag" v-bind:key="maintag">
                        <li v-on:mouseover="selectsub(index)" v-on:click="getall(index)">{{ maintag }}</li>
                    </ul><br /><br/>
                </div>
                <div class="subtags">
                    <ul v-for="(subtag, index) in subtags" v-bind:value="subtag" v-bind:key="subtag">
                        <li class="second" v-on:click="listbytag(index)" id="sub">{{ subtag }}</li>
                    </ul>
                </div>

                    <h3 style="font-size:20px">나의 옷장</h3><br/>
                    <div v-if="closetlist != null">
                        <div class="container">
                            <div class="item" v-for="closet in closetlist" :key="closet.closetnum">
                                <span v-if="closet.img != 'basicImage'">
                                <img :src="'http://localhost:7878/closets/img/' + closet.memnum.memnum + '/' + closet.closetnum">
                                </span>
                                <span v-if="closet.img == 'basicImage'">
                                    <img src="../../assets/basic.png">
                                </span>
                                <br />
                                {{ closet.cloth }}<br/>{{ closet.maintag }}<br />
                                {{ closet.subtag }}<br/>
                                <button v-on:click="addCloth(closet.closetnum, closet.img)" style="margin-top: 3px;border:1px solid lightgray; width:50px; height:30px">추가</button>
                            </div>
                        </div>
                    </div>
                    <div v-if="closetlist == ''">등록된 옷이 없습니다.</div>
                    <br />
                    <button @click="modalClose">취소</button>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'OotwAdd',
    data() {
        return {
            thumbImg1: '',
            thumbImg2: '',
            thumbImg3: '',
            thumbImg4: '',
            thumbImg5: '',
            modalCheck: false,
            closetlist: [],
            closetPerPage: 3,
            currentPage: 1,
            maintags: ['전체', '아우터', '상의', '하의', '기타', '신발'],
            subtags: [],
            cloth: '',
            memnum: '', // 객체가 아닌 정수로 판별됨
            odate: '',
            weather: '',
            temp: '',
            comments: '',
            thumbnum: null,
            // formdata null이면 axios로 전달이 안되기 때문에 default 넣어주고 보냄..
            // default 999999999에 기본 이미지 넣어주고 리스트에서는 filter 메서드로 안보여주게 함
            // 값 넘기기 위해 만들어 놓은 더미용
            closetnumlist: []
        }
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum')
        self.$axios.get('http://localhost:7878/closets/members/' + self.memnum)
            .then(function (res) {
                if (res.status == 200) {
                    self.closetlist = res.data.list;
                    // self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                } else {
                    alert('에러코드: ' + res.status)
                }
            })
    },
    methods: {
        modalOpen(num) {
            const self = this;
            self.modalCheck = !self.modalCheck;
            self.thumbnum = num;
            self.getall(0); // 모달창 닫을 때 이전 내용 상태로 닫히므로 전체 리스트 함수 다시 적용
        },
        modalClose() {
            const self = this;
            self.modalCheck = !self.modalCheck;
        },
        getall(index) {
            const self = this;
            if (index == 0) {
                self.memnum = sessionStorage.getItem('memnum')
                self.$axios.get('http://localhost:7878/closets/members/' + self.memnum)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list;
                            // self.displayedcloset = self.closetlist.slice(0, self.closetPerPage);
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        // moreBtn() {
        //     const self = this;
        //     const startIndex = self.currentPage * self.closetPerPage;
        //     const endIndex = startIndex + self.closetPerPage;
        //     self.displayedcloset = [...self.displayedcloset, ...self.closetlist.slice(startIndex, endIndex)];
        //     self.currentPage++;
        // },
        selectsub(index) {
            const self = this;
            if (index == 0) {
                self.subtags = []
            } else if (index == 1) {
                self.subtags = ['아우터(전체)', '가디건', '자켓', '야상', '트렌치코트', '코트', '패딩', '아우터(기타)']
            } else if (index == 2) {
                self.subtags = ['상의(전체)', '민소매', '반팔', '긴팔티', '셔츠', '니트', '맨투맨', '상의(기타)']
            } else if (index == 3) {
                self.subtags = ['하의(전체)', '반바지', '치마', '면바지', '청바지', '레깅스', '하의(기타)']
            } else if (index == 4) {
                self.subtags = ['기타(전체)', '스타킹', '히트텍', '기모제품', '방한용품', 'etc']
            } else if (index == 5) {
                self.subtags = ['신발(전체)', '샌들', '슬리퍼', '운동화', '등산화', '구두', '신발(기타)']
            }
        },
        listbytag(index) {
            const self = this;
            if (index == 0) {
                self.memnum = sessionStorage.getItem('memnum')
                var maintag = self.subtags[0].split('(', 1)
                self.$axios.get('http://localhost:7878/closets/maintags/' + self.memnum + "/" + maintag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list;
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                self.memnum = sessionStorage.getItem('memnum')
                var subtag = self.subtags[index]
                self.$axios.get('http://localhost:7878/closets/subtags/' + self.memnum + "/" + subtag)
                    .then(function (res) {
                        if (res.status == 200) {
                            self.closetlist = res.data.list;
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        addCloth(closetnum, img) {
            const self = this;
            if(img == 'basicImage') { 
                alert("기본 이미지는 Ootw에 등록할 수 없습니다.")
            } else {
                if (self.thumbnum == 1) {
                    self.thumbImg1 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                    self.closetnumlist[0] = closetnum
                } else if (self.thumbnum == 2) {
                    self.thumbImg2 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                    self.closetnumlist[1] = closetnum
                } else if (self.thumbnum == 3) {
                    self.thumbImg3 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                    self.closetnumlist[2] = closetnum
                } else if (self.thumbnum == 4) {
                    self.thumbImg4 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                    self.closetnumlist[3] = closetnum
                } else if (self.thumbnum == 5) {
                    self.thumbImg5 = 'http://localhost:7878/closets/img/' + self.memnum + '/' + closetnum
                    self.closetnumlist[4] = closetnum
                }
                this.modalCheck = !this.modalCheck
            }
        },
        addBoard() {
            const self = this
            var odate = new Date(self.odate)
            let formdata = new FormData();
            if (self.closetnumlist == '') {
                alert('입으신 옷을 등록해주세요.')
            } else if (self.odate == '' || self.weather == '' || self.temp == '') {
                alert('날짜와 날씨를 입력해주세요.')
            } else if (self.comments == '') {
                alert('커멘트를 입력해주세요.')
            } else {
                formdata.append('memnum', self.memnum)
                formdata.append('odate', odate)
                formdata.append('weather', self.weather)
                formdata.append('temp', self.temp)
                formdata.append('comments', self.comments)
                formdata.append('closetnumlist', self.closetnumlist)
                self.$axios.post('http://localhost:7878/boards', formdata)
                    .then(function (res) {
                        if (res.status == 200) {
                            location.href = '/ootwlist'
                        } else {
                            alert('에러코드:' + res.status)
                        }
                    })
            }
        }
        // check() { // 체크박스 1개만 체크되도록 설정
        //     const maxAllowed = 1;
        //     const checkboxes = document.querySelectorAll('input[type=checkbox]');
        //     checkboxes.forEach(function (checkbox) {
        //         checkbox.addEventListener('click', function () {
        //             const checkedCount = document.querySelectorAll('input[type=checkbox]:checked').length;
        //             if (checkedCount > maxAllowed) {
        //                 checkbox.checked = false;
        //             }
        //         });
        //     });
        // }
    }
}
</script>

<style scoped>
@font-face {
    font-family: 'PyeongChang-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
    font-weight: 400;
    font-style: normal;
}

.body-css {
    font-family: 'PyeongChang-Regular';
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}

button {
    border: none;
    background-color: white;
    font-family: 'PyeongChang-Regular';
    border-radius: 5px;
    transition: .5s;
    font-size: 15px;
    font-weight: normal;
}

button:hover {
    /* background-color: #85b380; */
    color: #85b380;
    cursor: pointer;
    font-weight: bold;
}

input[type='date']::before {
    content: attr(data-placeholder);
    width: 100%;
}

input[type='date']:focus::before,
input[type='date']:valid::before {
    display: none;
}

h3 {
    margin: 40px 0 0;
}

img {
    width: 130px;
    height: 130px;
    cursor: pointer;
}

.thumimg {
    transition: transform 0.3s ease;
}
.thumimg:hover {
    transform: scale(1.1);
}

li {
    list-style: none;
    float: left;
}

.maintags {
    cursor: pointer;
    margin-left: 174px;
}
.maintags li {
    font-size: 17px;
    margin: 0 15px 0 15px;
}
.subtags {
    cursor: pointer;
    margin-left: 122px;
}
.subtags li {
    font-size: 15px;
    margin: 0 13px 0 13px;
}

/* dimmed */
.modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
}


/* modal or popup */
.modal-container {
    overflow: auto;
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 800px;
    height: 80%;
    background: #fff;
    border-radius: 10px;
    padding: 20px;
    box-sizing: border-box;
}

.container {
    /* display: flex;
    flex-wrap: wrap; */
    display: grid;
    width: 100%;
    grid-template-columns: repeat(auto-fill, minmax(18%, auto));
    column-gap: 3px;
    row-gap: 5px;
    margin-top: 5px;
}

.item {
    transition: transform 0.3s ease;
    font-size: 15px;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    /* Adjust the width percentage based on your needs */
}
.item:hover {
    background-color: rgb(255, 252, 252);
    transform: scale(1.05);
}
</style>