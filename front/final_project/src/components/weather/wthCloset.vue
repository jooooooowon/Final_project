<template>
    <div>
        {{ recommend }} <br />
        {{ message }}

        <!-- 태그별 옷추천 -->
        <div v-if="showRecom">

            <!-- v-for) subtags 배열을 반복하면서 각 subtag에 대한 작업을 수행
                v-bind:value와 v-bind:key는 subtag 값을 해당 HTML 요소의 속성에 바인딩합니다. 
                이렇게 하면 각 subtag에 대해 고유한 값을 가지게 됩니다. -->
            <div v-for="subtag in subtags" v-bind:value="subtag" v-bind:key="subtag">
                <br />
                <h5>{{ subtag }}</h5>
                <div v-if="naverList[subtag] != null">
                    {{ message2 }} <br />
                    <router-link to="/closetadd"><button>옷장에 {{ subtag }} 추가하기</button></router-link>
                    <button @click="shopping(subtag)">{{ subtag }} 쇼핑하러 가기</button>
                </div>
                <div>
                    <!-- :key="subtag"는 Vue의 key 속성을 사용하여 "각 그룹"을 고유하게 식별 => subtag 별 그룹 생성 -->
                    <div class="card-grid" :key="subtag"
                        style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                        <!-- v-for) additionalCloset[subtag] 배열을 반복하면서 각 cloth에 대한 작업을 수행 -->
                        <div class="card" v-for="cloth in additionalCloset[subtag]" :key="cloth.closetnum"
                            :style="{ maxWidth: '200px', flex: '0 0 250px' }" @click="modalOpen(cloth.closetnum)"
                            @mouseover="cursorChange">
                            <img :src="'http://localhost:8081/closets/img/' + memnum + '/' + cloth.closetnum">
                            <h6>{{ cloth.cloth }}</h6>
                        </div>
                    </div>
                    <!-- 더 추가할 옷이 있을 때에만 더보기 버튼 뜨기 -->
                    <div v-if="gap[subtag] > 0">
                        <button @click="moreBtn(subtag)">더보기</button>
                    </div>
                </div>

                <!-- 옷이 없을 때 띄울 네이버 쇼핑 링크 -->
                <div>
                    <div class="card-grid" :key="subtag"
                        style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                        <div class="card" v-for="(dto, i) in naverList[subtag]" :key="i"
                            :style="{ maxWidth: '200px', flex: '0 0 250px' }" @click="shoppingLink(subtag, i)"
                            @mouseover="cursorChange">
                            <img :src="dto.img">
                            <h6>{{ dto.price }}원</h6>
                        </div>
                    </div>

                </div>
            </div>

            <!-- 옷 디테일 모달창 -->
            <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
                <div class="modal-container" @click.stop="" id="container">
                    <label for="imgtag"><img :src="'http://localhost:8081/closets/img/' + memnum + '/' + setClosetnum"
                            id="thumbimg" style="width:200px; height:auto;"></label>
                    <input type="file" id="imgtag" style="display: none" accept="image/*" v-on:change="thumbnail($event)">
                    <br />
                    {{ maintag }} | {{ sub }} <br />
                    <input type="text" v-model="cloth"> <br />
                    <button v-on:click="change(setClosetnum)">수정</button>
                    <button v-on:click="deletecloth(setClosetnum)">삭제</button>
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
            message2: '옷장이 비었네요!',
            showRecom: false,
            gender: '', // 로그인 한 사람의 성별 담음 -> 네이버 검색에 쓰임
            subtags: [],
            /*
            additionalCloset을 []로 데이터 속성으로 선언
            -> Vue의 반응적인(reactive) 데이터 시스템에 의해 관리되는 배열
            --> JavaScript 객체처럼 사용하여 동적으로 속성을 추가하고 값을 할당할 수 있음!!
            => this.additionalCloset[sub] :  additionalCloset 객체에 sub라는 속성을 동적으로 추가
            추가된 속성은 Vue에 의해 반응적으로 추적되지는 않지만, 반복문 등을 통해 해당 속성을 접근하여 사용할 수 있음~
            
            즉, additionalCloset 객체는 Vue의 반응성과는 별개로 동작하는 일반적인 JavaScript 객체이며, 
            동적으로 속성을 추가하고 값을 할당할 수 있는 특징을 가지고 있음~~
            */
            additionalCloset: [], // 여기에 사실상 additionalCloset['반팔'], additionalCloset['셔츠'] .. 가 다 들어있는거임!
            closetlist: [], // 처음 검색했을 때 전체 리스트 담음 --> additionalCloset 에 5개씩 부분으로 담음
            currentPage: [], // 더보기 버튼 누르면 1씩 업됨
            gap: [], // closetlist[subtag] - additionalCloset[subtag] ---> 더보기버튼 띄우는 조건으로 쓰임
            naverList: [],
            //~~~~~~~~~~~~~~~옷 디테일 모달~~~~~~~~~~~~~~~~
            setClosetnum: null,
            cloth: '',
            maintag: '',
            sub: '',
            modalCheck: false,
            uploadimg: '', // 디테일 -> 이미지수정 -> 바뀐 이미지 주소 담는 변수
            thumbimg: ''
        }
    },
    async created() {
        // 현재 로그인 상태 확인.
        let token = sessionStorage.getItem('token');
        if (this.memnum == null) {
            this.message = "로그인 후 옷장에서 옷 추천을 받아보세요!";
            this.showRecom = false;
        } else {
            this.$axios.get(`http://localhost:8081/members/${this.memnum}`,
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
                    } else {
                        console.log('없는 아이디거나 만료된 토큰')
                    }
                } else {
                    alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
                }
            })
        }
        this.recommendation();
    },
    methods: {
        // 동기 함수로 하게되면 데이터가 완전히 로드되기 전에 b-card-group 및 b-card 요소가 렌더링되므로 원하는 리스트가 생성되지 않습니다.
        // async -> 비동기 함수 정의. 
        // 온도 별 옷추천 및 소분류로 옷장 옷 검색 (전체 중 5개만)
        async recommendation() {
            const tmp = this.nowTmp;
            let recommend = '';
            let subtags = [];
            if (tmp >= 28) { // 28도 이상
                recommend = '반팔, 민소매, 반바지, 치마'
                subtags = ['반팔', '민소매', '반바지', '치마']

            } else if (23 <= tmp && tmp <= 27) { // 23~27도
                recommend = '반팔, 얇은셔츠, 반바지, 면바지'
                subtags = ['반팔', '셔츠', '반바지', '면바지']

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

            } else { // 4도 이하
                recommend = '패딩, 두꺼운코트, 방한용품, 기모제품'
                subtags = ['패딩', '코트', '방한용품', '기모제품']
            }
            this.recommend = recommend;
            this.subtags = subtags;

            // 소분류로 옷장 옷 검색 (전체 중 5개만)
            for (let i = 0; i < this.subtags.length; i++) {
                const subtag = this.subtags[i];
                try {
                    // await -> 비동기 작업인 self.$axios.get(...)의 결과를 기다립니다.
                    const res = await this.$axios.get('http://localhost:8081/closets/subtags/' + subtag);
                    if (res.status === 200) {
                        // 컴포넌트 처음 로딩될 때 옷장에서 999999999번 default(기본이미지) 걸러서 리스트에 넣기
                        this.closetlist[subtag] = res.data.list.filter(closet => closet.closetnum !== 999999999);
                        let addtionalRow = this.closetlist[subtag].slice(0, 5);
                        // 배열에 subtag 속성 추가해서 검색 결과를 각각 담음.
                        this.additionalCloset[subtag] = addtionalRow; // additionalCloset 객체에 속성으로 추가
                        this.currentPage[subtag] = 1;
                        this.gap[subtag] = this.closetlist[subtag].length - this.additionalCloset[subtag].length

                        // 검색된 옷이 없으면 네이버 검색 띄워주기
                        if (this.closetlist[subtag].length === 0) {
                            const result = await this.$axios.get('http://localhost:8081/naver/' + this.gender + subtag)
                            if (result.status == 200) {
                                const allList = result.data.list;
                                const fivelist = allList.slice(0, 5);
                                this.naverList[subtag] = fivelist;

                                // window 객체의 scrollTo 메서드는 문서를 지정된 좌표로 스크롤해주는 기능을 한다. (window는 생략 가능)
                                // top은 세로 위치, left는 가로 위치, scroll-behavior은 스크롤 효과를 지정하는 속성
                                // auto는 기본값이며, 바로 위치로 이동한다. instant도 같은 동작을 한다. smooth는 부드럽게 이동하는 애니메이션 효과를 보여준다.
                                // window.scrollTo({ top: 0, left: 0, behavior: 'smooth' });
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
            }


        },

        // 더보기 버튼 누르면 5개씩 추가 
        moreBtn(subtag) {
            // 변수의 값이 변하지 않고 상수로 사용될 경우에는 const
            // 값이 재할당될 수 있는 경우에는 let
            // this.currentPage[subtag] = 1;
            const n = this.currentPage[subtag];
            // n=1이면 5개, n=2이면 10개 떠있음
            // (0, 5) -> (5, 10) -> (10, 15)
            const start = n * 5; // n=1 -> 5
            const end = start + 5; // start=5 -> 10
            let additionalRow = this.closetlist[subtag].slice(start, end)
            this.additionalCloset[subtag] = this.additionalCloset[subtag].concat(additionalRow);
            this.currentPage[subtag]++;
            this.gap[subtag] = this.closetlist[subtag].length - this.additionalCloset[subtag].length;
        },

        // 카드 누르면 옷 디테일 페이지로 넘어감
        modalOpen(closetnum) {
            const self = this;
            this.setClosetnum = closetnum;
            self.modalCheck = !self.modalCheck;
            self.$axios.get('http://localhost:8081/closets/' + closetnum)
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
                self.$axios.patch('http://localhost:8081/closets/editcloth/' + closetnum + "/" + self.cloth)
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
                self.$axios.patch('http://localhost:8081/closets/editcloth/' + closetnum + "/" + self.cloth, formdata)
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
        // 옷 디테일 창의 옷 썸네일 띄우기
        // thumbnail() { 
        //   const file = document.getElementById('imgtag');
        //   if (file.files && file.files[0]) {
        //     //const self = this;
        //     const reader = new FileReader();
        //     // reader.onload 콜백 함수 내에서 this는 reader 객체를 가리키게 되어
        //     // 이를 해결하기 위해서는 thumbnail 메서드 내에서 화살표 함수(arrow function)를 사용하여 콜백 함수를 정의해야 합니다
        //     // 화살표 함수는 자신의 this를 갖지 않고, 상위 스코프의 this를 유지하므로 Vue 인스턴스의 데이터와 바인딩됩니다.
        //     reader.onload = () => {
        //       document.getElementById('thumbimg').src = reader.result;
        //       //this.thumb = reader.result;
        //       this.uploadimg = file.files[0];
        //     }; // bind 메서드를 사용하여 콜백 함수의 this를 Vue 인스턴스로 설정
        //     reader.readAsDataURL(file.files[0]);
        //   }
        // },

        thumbnail(event) {
            const file = event.target.files[0];
            if (file) {
                const reader = new FileReader();
                reader.readAsDataURL(file);
                reader.onload = () => {
                    alert(document.querySelector('#thumbimg'));
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
            self.$axios.delete('http://localhost:8081/closets/' + closetnum)
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
        },

        // {{subtag}} 쇼핑하러 가기 버튼 -- 카테고리 네이버 검색
        shopping(subtag) {
            const link = "https://search.shopping.naver.com/search/all?query=" + this.gender + subtag;
            window.open(link);
        },

        // 네이버 쇼핑 카드 누르면 해당 옷 네이버 검색창으로 이동
        shoppingLink(subtag, i) {
            const link = this.naverList[subtag][i].link;
            window.open(link, "_blank");
        },

        // 카드에 마우스 올리면 색깔 변하는 메서드
        cursorChange(e) {
            e.target.style.cursor = "pointer";
        }

    }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");

.card-grid {
    display: grid;
    grid-template-columns: repeat(5, 1fr);
    grid-gap: 20px;
    justify-content: center;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
}

.card {
    flex-basis: 20%;
    background-color: #fff;
    border-radius: 10px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

.card img {
    display: block;
    margin: 0 auto;
    position: relative;
    width: 150px;
    height: 150px;
    object-fit: cover;
    /* border-radius: 10%; */
    margin-bottom: 10px;
}

.card h6 {
    margin: 0;
}

/* dimmed */
.modal-wrap {
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.1);
}


/* modal or popup */
.modal-container {
    overflow: auto;
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 500px;
    height: 60%;
    background: #fff;
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
}
</style>