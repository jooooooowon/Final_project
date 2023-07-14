<template>
    <div class="back">
        <!-- 기온별 옷추천 -->
        <div class="rgyPostIt">{{ recommend }}</div> <br /><br />
        {{ message }}

        <!-- 태그별 옷추천 -->
        <div v-if="showRecom">

            <!-- subtag 아이콘 -->
            <div style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center; margin-top: 20px;">
                <div v-for="subtag in subtags" v-bind:value="subtag" v-bind:key="subtag">
                    <!-- subtag 값을 클래스 이름으로 사용 -->
                    <div style="padding-left: 15px;" :style="{ maxWidth: '160px', flex: '0 0 250px' }">
                        <div class="iconBG" @click="restartBounce(subtag, $event)">
                            <img :src="getIcon(subtag)" :title=subtag>
                        </div>
                    </div>
                </div>
            </div>

            <!-- subtag 별 리스트 -->
            <div>
                <!-- 옷장 추천 리스트 -->
                <div style="margin-top: 100px; margin-bottom: 50px;">
                    <!-- 옷장 비었을 때 뜰 메시지 -->
                    <div v-if="message2 !== ''">
                        {{ message2 }} <br /><br />
                    </div>
                    <div class="closet"
                        style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                        <div class="cloth" v-for="cloth in additionalCloset" :key="cloth.closetnum"
                            :style="{ maxWidth: '160px', flex: '0 0 250px' }" @click="modalOpen(cloth.closetnum)"
                            @mouseover="cursorChange" @mouseout="resetCursor">
                            <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + cloth.closetnum">
                            <span>{{ cloth.cloth }}</span>
                        </div>
                    </div>
                </div>


                <div>
                    <!-- 옷장 버튼 -->
                    <img class="plus" style="padding-right: 50px;" src="@/assets/weatherPlus.svg"
                        @click="listbytag(currentSubtag, 1)" @mouseover="cursorChange" @mouseout="resetCursor"
                        title="옷장에 추가하기">
                    <!-- 쇼핑 버튼 -->
                    <img class="plus" style="height:73px" src="@/assets/shopping.svg" @click="shopping(currentSubtag, 1)"
                        @mouseover="cursorChange" @mouseout="resetCursor" title="쇼핑하러 가기">
                </div>

                <!-- 옷이 없을 때 띄울 네이버 쇼핑 리스트 -->
                <div style="margin-top: 50px;">
                    <div class="closet"
                        style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                        <div class="cloth" v-for="(dto, i) in naverList" :key="i"
                            :style="{ maxWidth: '160px', flex: '0 0 250px' }" @click="shoppingLink(i)"
                            @mouseover="cursorChange" @mouseout="resetCursor">
                            <img :src="dto.img">
                            {{ dto.price }}원
                        </div>
                    </div>

                </div>
            </div>

            <!-- 옷 디테일 모달창 -->
            <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
                <div class="modal-container" @click.stop="" id="container">
                    <label for="imgtag"><img :src="'http://localhost:7878/closets/img/' + memnum + '/' + setClosetnum"
                            id="thumbimg" style="width:200px; height:auto;"></label>
                    <input type="file" id="imgtag" style="display: none" accept="image/*" v-on:change="thumbnail($event)">
                    <div class="modal-tags">{{ maintag }} | {{ sub }}</div>
                    <div class="modal-name"><input type="text" v-model="cloth" size="15"
                            style="height: 38px; text-align: center;"></div>
                    <div class="modal-btn">
                        <button v-on:click="change(setClosetnum)">수정</button>
                        <button v-on:click="deletecloth(setClosetnum)">삭제</button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>

<script>
export default {
    name: 'WthCloset',
    data() {
        return {
            nowTmp: sessionStorage.getItem("nowTmp"),
            memnum: sessionStorage.getItem("memnum"),
            recommend: '...로딩중 *^^*',
            message: '', // 로그인하면 옷장에서 옷 추천해드림~
            message2: '', // 옷장이 비었네요
            showRecom: false,
            gender: '', // 로그인 한 사람의 성별 담음 -> 네이버 검색에 쓰임
            subtags: [], // 기온별 옷차림 subtag 다 담겨있음
            currentSubtag: '', // 아이콘을 누르면 하나의 subtag 만 담김. -> 리스트 검색
            closetlist: [], // 처음 검색했을 때 전체 리스트 담음 --> additionalCloset 에 5개씩 부분으로 담음
            additionalCloset: [],
            goToCloset: false, // 옷장버튼 띄우는 조건으로 쓰임
            naverList: [],
            //~~~~~~~~~~~~~~~옷 디테일 모달~~~~~~~~~~~~~~~~
            setClosetnum: null,
            cloth: '',
            maintag: '',
            sub: '',
            modalCheck: false,
            uploadimg: '', // 디테일 -> 이미지수정 -> 바뀐 이미지 주소 담는 변수
            thumbimg: '',
            // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            clothesIcons: {
                '반팔': require('@/assets/clothesIcons/1.png'),
                '민소매': require('@/assets/clothesIcons/2.png'),
                '반바지': require('@/assets/clothesIcons/3.png'),
                '치마': require('@/assets/clothesIcons/4.png'),
                '셔츠': require('@/assets/clothesIcons/5.png'),
                '면바지': require('@/assets/clothesIcons/6.png'),
                '가디건': require('@/assets/clothesIcons/7.png'),
                '긴팔티': require('@/assets/clothesIcons/8.png'),
                '청바지': require('@/assets/clothesIcons/9.png'),
                '니트': require('@/assets/clothesIcons/10.png'),
                '맨투맨': require('@/assets/clothesIcons/11.png'),
                '자켓': require('@/assets/clothesIcons/12.png'),
                '야상': require('@/assets/clothesIcons/13.png'),
                '스타킹': require('@/assets/clothesIcons/14.png'),
                '트렌치코트': require('@/assets/clothesIcons/15.png'),
                '코트': require('@/assets/clothesIcons/16.png'),
                '히트텍': require('@/assets/clothesIcons/17.png'),
                '레깅스': require('@/assets/clothesIcons/18.png'),
                '패딩': require('@/assets/clothesIcons/19.png'),
                '방한용품': require('@/assets/clothesIcons/20.png'),
                '기모제품': require('@/assets/clothesIcons/21.png')
            }
        }
    },
    created() {
        // 현재 로그인 상태 확인.
        let token = sessionStorage.getItem('token');
        if (this.memnum == null) {
            this.message = "로그인 후 옷장에서 옷 추천을 받아보세요!";
            this.showRecom = false;
            this.recommendation();
        } else {
            this.$axios.get(`http://localhost:7878/members/${this.memnum}`,
                { headers: { 'token': token } })
                .then(async res => {
                    if (res.status == 200) {
                        this.showRecom = !this.showRecom;
                        const dto = res.data.dto
                        if (dto != null) {
                            if (dto.gender === 'male') {
                                this.gender = '남성'
                            } else {
                                this.gender = '여성'
                            }
                            this.recommendation();
                            this.search(this.subtags[0]);
                        } else {
                            console.log('없는 아이디거나 만료된 토큰')
                        }
                    } else {
                        alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
                    }
                })
        }
    },
    methods: {
        // 서브태그 아이콘 불러오기
        getIcon(subtag) {
            return this.clothesIcons[subtag];
        },
        // 동기 함수로 하게되면 데이터가 완전히 로드되기 전에 b-card-group 및 b-card 요소가 렌더링되므로 원하는 리스트가 생성되지 않습니다.
        // async -> 비동기 함수 정의. 
        // 온도 별 옷추천 및 소분류로 옷장 옷 검색 (전체 중 5개만)
        recommendation() {
            const tmp = this.nowTmp;
            let recommend = '';
            let subtags = [];
            if (tmp >= 28) { // 28도 이상
                recommend = '반팔, 민소매, 반바지, 치마'
                subtags = ['반팔', '민소매', '반바지', '치마']


            } else if (20 <= tmp && tmp <= 22) { // 20~22도
                recommend = '얇은가디건, 긴팔티, 면바지, 청바지'
                subtags = ['가디건', '긴팔티', '면바지', '청바지']

            } else if (17 <= tmp && tmp <= 19) { // 17~19도
                recommend = '얇은니트, 가디건, 맨투맨, 얇은재킷, 면바지, 청바지'
                subtags = ['니트', '가디건', '맨투맨', '자켓', '면바지', '청바지']

            } else if (12 <= tmp && tmp <= 16) { // 12~16도
                recommend = '자켓, 가디건, 야상, 맨투맨, 니트, 스타킹, 청바지, 면바지'
                subtags = ['자켓', '가디건', '야상', '맨투맨', '니트', '스타킹', '청바지', '면바지']

            } else if (9 <= tmp && tmp <= 11) { // 9~11도
                recommend = '자켓, 트렌치코트, 야상, 니트, 스타킹, 청바지, 면바지'
                subtags = ['자켓', '트렌치코트', '야상', '니트', '스타킹', '청바지', '면바지']

            } else if (5 <= tmp && tmp <= 8) { // 5~8도
                recommend = '코트, 히트텍, 니트, 청바지, 레깅스'
                subtags = ['코트', '히트텍', '니트', '청바지', '레깅스']

            } else if (tmp <= 4) { // 4도 이하
                recommend = '패딩, 두꺼운코트, 방한용품, 기모제품'
                subtags = ['패딩', '코트', '방한용품', '기모제품']

            } else { // 23~27도
                recommend = '반팔, 얇은셔츠, 반바지, 면바지'
                subtags = ['반팔', '셔츠', '반바지', '면바지']
            }
            this.recommend = recommend;
            this.subtags = subtags;


            // .classList.add('clicked')를 호출하는 코드가 element에 적용되기 전에 실행되므로 element가 undefined일 수 있습니다.
            this.$nextTick(() => {
                const elements = document.querySelectorAll('.iconBG');
                if (elements.length > 0) {
                    const element = elements[0];
                    element.classList.add('clicked');
                }
            });
        },

        // 아이콘 클릭하면 튕기다가 멈춤 + subtag로 옷 검색
        // Vue에서 이벤트 핸들러에 추가로 전달할 인수가 있는 경우, 해당 인수는 첫 번째 인수로 전달되어야 합니다.
        restartBounce(subtag, e) {
            e.preventDefault(); // 태그의 값을 전송하면서 해당 페이지를 새로고침 하는 기능을 막음

            // 이전에 클릭된 아이콘 배경 리셋시키기
            const elements = document.querySelectorAll('.iconBG');
            elements.forEach(element => {
                element.classList.remove('clicked');
            });

            const targetElement = e.currentTarget;
            targetElement.classList.remove("bounce"); // Removing the class
            // offsetWidth는 margin을 제외한, padding값, border값까지 계산한 값을 가져온다.
            targetElement.offsetWidth; // Triggering reflow
            targetElement.classList.add("bounce"); // Re-adding the class
            targetElement.classList.add('clicked');

            this.search(subtag);
        },

        // 리스트 검색
        async search(subtag) {
            this.currentSubtag = subtag;
            // 값 초기화 
            this.message2 = '';
            this.additionalCloset = [];
            this.naverList = [];

            // 소분류로 옷장 옷 검색 (전체 중 5개만)
            try {
                // await -> 비동기 작업인 self.$axios.get(...)의 결과를 기다립니다.
                // 메서드 내에서 옷장 데이터를 비동기적으로 요청
                const res = await this.$axios.get('http://localhost:7878/closets/subtags/' + this.memnum + '/' + subtag);
                if (res.status === 200) {
                    // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default(기본이미지) 걸러서 리스트에 넣기
                    this.closetlist = res.data.list.filter(closet => closet.closetnum !== 999999999);
                    this.additionalCloset = this.closetlist.slice(0, 20);
                    if (this.closetlist.length > 20) {
                        this.goToCloset = true;
                    } else {
                        this.goToCloset = false;
                    }

                    // 검색된 옷이 없으면 네이버 검색 띄워주기
                    if (this.closetlist.length === 0) {
                        this.message2 = '옷장이 비었네요!'
                        const result = await this.$axios.get('http://localhost:7878/naver/' + this.gender + subtag)
                        if (result.status == 200) {
                            const allList = result.data.list;
                            this.naverList = allList.slice(0, 10);

                        } else {
                            alert("네이버 실패..!")
                        }
                    }
                } else {
                    alert('에러코드 이건가?: ' + res.status);
                }
            } catch (error) {
                alert('에러 발생: ' + error);
            }
        },

        // 옷장 태그검색 더보기
        listbytag(subtag, index) {
            const self = this;
            self.$router.push({ name: 'ClosetListByTag2', query: { tag: subtag, index: index } });
        },

        // 카드 누르면 옷 디테일 페이지로 넘어감
        modalOpen(closetnum) {
            const self = this;
            this.setClosetnum = closetnum;
            self.modalCheck = !self.modalCheck;
            self.$axios.get('http://localhost:7878/closets/' + closetnum)
                .then(function (res) {
                    if (res.status == 200) {
                        let dto = res.data.dto
                        if (dto != null) {
                            self.cloth = dto.cloth
                            self.maintag = dto.maintag
                            self.sub = dto.subtag
                        } else {
                            alert("정보가 없습니다.")
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },

        // 모달창 닫기
        modalClose() {
            this.modalCheck = !this.modalCheck;
        },

        // 옷 디테일 모달에서 옷 수정하기
        change(closetnum) {
            const self = this;
            let formdata = new FormData();
            if (self.uploadimg == null) {
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                            location.reload();
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            } else {
                formdata.append('newf', self.uploadimg)
                self.$axios.patch('http://localhost:7878/closets/editcloth/' + closetnum + "/" + self.cloth, formdata)
                    .then(function (res) {
                        if (res.status == 200) {
                            let newdto = res.data.dto
                            self.cloth = newdto.cloth;
                            location.reload();
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },
        thumbnail(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    // alert(document.querySelector('#thumbimg'));
                    //console.log(reader.result);
                    document.querySelector('#thumbimg').src = reader.result;
                    //console.log(document.querySelector('#thumbimg'));
                    //console.log(document.querySelector('#thumbimg').src);
                    //this.thumb = reader.result;
                    this.uploadimg = file;
                };

            }
        },

        // 옷 디테일 창에서 옷 삭제하기
        deletecloth(closetnum) {
            const self = this;
            if (confirm('정말 삭제하시겠습니까?\n삭제 시 옷장에서도 삭제됩니다.')) {
                self.$axios.delete('http://localhost:7878/closets/' + closetnum)
                    .then(function (res) {
                        if (res.status == 200) {
                            if (res.data.flag) {
                                // this.modalClose()
                                location.reload();
                            }
                        } else {
                            alert('에러코드: ' + res.status)
                        }
                    })
            }
        },

        // {{subtag}} 쇼핑하러 가기 버튼 -- 카테고리 네이버 검색
        shopping(subtag) {
            const link = "https://search.shopping.naver.com/search/all?query=" + this.gender + subtag;
            window.open(link);
        },

        // 네이버 쇼핑 카드 누르면 해당 옷 네이버 검색창으로 이동
        shoppingLink(i) {
            const link = this.naverList[i].link;
            window.open(link, "_blank");
        },

        // 마우스를 올렸을 때 요소의 크기를 1.05배로 확대(scale up)시키는 스타일을 적용
        cursorChange(e) {
            e.currentTarget.style.transform = "scale(1.05)";
        },
        // 마우스가 요소를 벗어났을 때 원래 크기로 되돌리는 스타일을 적용
        resetCursor(e) {
            e.currentTarget.style.transform = "scale(1)";
        }

    }
}

</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");
@import url("https://fonts.googleapis.com/css?family=Nanum+Pen+Script&display=swap");
/* @font-face {
font-family: 'UhBeeSeulvely';
src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_five@.2.0/UhBeeSeulvely.woff') format('woff');
font-weight: normal;
font-style: normal;
} */


@font-face {
    font-family: 'PyeongChang-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
    font-weight: 400;
    font-style: normal;
}


.back {
    font-family: 'PyeongChang-Regular';
    /* font-family: 'Cafe24Ohsquareair'; */
    /* font-weight: 100; */
    font-style: normal;
}

.iconBG {
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: transparent;
    background-color: rgba(255, 255, 255, 0.6);
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
    margin-left: auto;
    margin-right: auto;
    cursor: pointer;
    position: relative;
    transition: transform 0.3s ease;
    /* transform 속성에 0.5초의 애니메이션을 부드럽게 적용합니다 */

}

.iconBG:hover {
    /* transform: translateY(-15px); */
    transform: scale(1.05);
    background-color: rgba(255, 255, 255, 1);
}

.iconBG.clicked {
    background-color: rgba(255, 255, 255, 1);
}

@-webkit-keyframes bounce {

    0%,
    20%,
    50%,
    80%,
    100% {
        -webkit-transform: translateY(0);
    }

    40% {
        -webkit-transform: translateY(-30px)
    }

    60% {
        -webkit-transform: translateY(-20px);
    }
}

@keyframes bounce {

    0%,
    20%,
    50%,
    80%,
    100% {
        transform: translateY(0);
    }

    40% {
        transform: translateY(-30px);
    }

    60% {
        transform: translateY(-20px);
    }
}


.bounce {
    -webkit-animation-duration: 1s;
    animation-duration: 1s;
    -webkit-animation-name: bounce;
    animation-name: bounce;
}

.iconBG img {
    padding-top: 11px;
    width: 35px;
    height: auto;
    /* align-items: center;
    justify-content: center;
    text-align: center; */
}

.closet {
    display: grid;
    /* grid-template-columns: repeat(5, 1fr); */
    /* 최소 200px의 폭을 가지는 열을 자동으로 생성 */
    /* 그리드 컨테이너의 너비에 맞추어 최대한 많은 열을 표시 */
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    grid-gap: 25px;
    justify-content: center;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
}

.cloth {
    transition: transform 0.3s ease;
    /* 변화가 일어날 때 0.3초 동안 부드럽게 전환 */
    background: transparent;
    background-color: rgba(255, 255, 255, 0.6);
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    align-items: center;
    justify-content: center;
    box-shadow: 0 5px 10px rgba(0, 0, 0, 0.1);
}

.cloth:hover {
    transform: scale(1.05);
    background-color: rgba(255, 255, 255, 0.8);
}

.cloth img {
    display: block;
    margin: 0 auto;
    position: relative;
    width: 160px;
    height: 160px;
    object-fit: cover;
    border-radius: 3px;
    margin-bottom: 10px;
}

.cloth span {
    margin: 0;
    font-size: 16px;
    /* font-family: 'Nanum Pen Script'; */
}

.plus {
    transition: transform 0.3s ease;
    /* 변화가 일어날 때 0.3초 동안 부드럽게 전환 */
    margin-top: 30px;
    margin-bottom: 50px;
}

.plus:hover {
    transform: scale(1.05);
}


/* dimmed */
.modal-wrap {
    z-index: 999;
    /* 모달의 배경을 카드보다 상위로 올립니다. */
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.4);
}


/* modal or popup */
.modal-container {
    z-index: 9999;
    /* 모달창을 다른 요소들보다 상위로 올림 */
    overflow: auto;
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 310px;
    height: 58%;
    background: #fff;
    color: #2c3e50;
    border-radius: 10px;
    padding: 20px;
    box-sizing: border-box;
    /* 가운데정렬 */
    display: flex;
    flex-direction: column;
    /* 내용 수직으로 배치 */
    justify-content: center;
    /* 수직방향 가운데 정렬 */
    align-items: center;
    /* 수평방향 가운데 정렬 */

    box-shadow: 0 20px 10px rgba(0, 0, 0, 0.1);
}

.modal-container img {
    position: relative;
    width: 250px;
    /* 가로 사이즈 200px로 고정 */
    height: 250px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 10px;
    margin-top: -110px;
    margin-bottom: 20px;
    cursor: pointer;
}

.modal-tags {
    margin-top: -5px;
    margin-bottom: 5px;
}

.modal-name {
    margin-bottom: 10px;
}

.modal-btn {
    margin-bottom: -110px;
}

.modal-btn button {
    background-color: transparent;
}

button {
    border: 1px solid rgb(177, 177, 177);
    background-color: white;
    /* background-color: rgb(196, 215, 178, 0.6); */
    /* border: none; */
    border-radius: 5px;
    transition: .5s;
    font-size: 15px;
    font-weight: normal;
    font-family: 'PyeongChang-Regular';
    margin-left: 5px;
}

button:hover {
    background-color: #85b380;
    color: #ffffff;
    /* color: #85b380; */
    cursor: pointer;
    /* font-weight: bold; */
}

/*
    친효애드온 : 포스트잇 모듈 (마크1) 시작
    https://rgy0409.tistory.com
    e-mail : rgy0409@gmail.com
*/
div.rgyPostIt {
    position: relative;
    display: inline-block;
    padding: 20px 45px 20px 15px;
    margin: 5px 0;
    margin-bottom: 50px;
    border: 1px solid #f8f861;
    border-left: 30px solid #f8f861;
    border-bottom-right-radius: 60px 10px;
    font-family: 'Nanum Pen Script';
    /* font-family: 'UhBeeSeulvely'; */
    /* font-family: 'Yoonwoo'; */
    font-size: 27px;
    /* font-size: 20px; */
    color: #555;
    word-break: break-all;
    background: #ffff88;
    /* Old browsers */
    background: -moz-linear-gradient(-45deg, #ffff88 81%, #ffff88 82%, #ffff88 82%, #ffffc6 100%);
    /* FF3.6+ */
    background: -webkit-gradient(linear, left top, right bottom, color-stop(81%, #ffff88), color-stop(82%, #ffff88), color-stop(82%, #ffff88), color-stop(100%, #ffffc6));
    /* Chrome,Safari4+ */
    background: -webkit-linear-gradient(-45deg, #ffff88 81%, #ffff88 82%, #ffff88 82%, #ffffc6 100%);
    /* Chrome10+,Safari5.1+ */
    background: -o-linear-gradient(-45deg, #ffff88 81%, #ffff88 82%, #ffff88 82%, #ffffc6 100%);
    /* Opera 11.10+ */
    background: -ms-linear-gradient(-45deg, #ffff88 81%, #ffff88 82%, #ffff88 82%, #ffffc6 100%);
    /* IE10+ */
    background: linear-gradient(135deg, #ffff88 81%, #ffff88 82%, #ffff88 82%, #ffffc6 100%);
    /* W3C */
    filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#ffff88', endColorstr='#ffffc6', GradientType=1);
    /* IE6-9 fallback on horizontal gradient */
    transition: all 0.2s;
    -webkit-transition: all 0.2s;
}

div.rgyPostIt::after {
    content: " ";
    position: absolute;
    z-index: -1;
    right: 0;
    bottom: 35px;
    width: 150px;
    height: 30px;
    background-color: rgba(0, 0, 0, 0);
    box-shadow: 2px 35px 5px rgba(0, 0, 0, 0.4);
    -webkit-box-shadow: 2px 35px 5px rgba(0, 0, 0, 0.4);
    transform: matrix(-1, -0.1, 0, 1, 0, 0);
    -webkit-transform: matrix(-1, -0.1, 0, 1, 0, 0);
    -moz-transform: matrix(-1, -0.1, 0, 1, 0, 0);
    -ms-transform: matrix(-1, -0.1, 0, 1, 0, 0);
    -o-transform: matrix(-1, -0.1, 0, 1, 0, 0);
    transition: all 0.2s;
    -webkit-transition: all 0.2s;
}

div.rgyPostIt:hover {
    border-bottom-right-radius: 75px 30px;
}

div.rgyPostIt:hover::after {
    box-shadow: 2px 37px 7px rgba(0, 0, 0, 0.37);
    -webkit-box-shadow: 2px 37px 7px rgba(0, 0, 0, 0.37);
}

div.rgyPostIt>p {
    padding: 5px 0 !important;
}

div.rgyPostIt>p::before {
    content: "\f198";
    margin-right: 7px;
    font-family: "FontAwesome";
    font-weight: normal;
    font-size: 20px;
    vertical-align: middle;
}

div.rgyPostIt>p>a {
    color: #555;
}

/* 포스트잇 모듈 (마크1) 끝 */
</style>