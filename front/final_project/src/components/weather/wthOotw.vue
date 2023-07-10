<template>
    <div class="back">
        <p style="margin-top: 100px; margin-bottom: 100px;">{{ message }}</p>

        <div v-if="showRecom">
            <p v-if="t1 === 28">{{ t1 }}℃ 이상일 때 입었던 옷 기록들</p>
            <p v-if="t1 !== 28 && t2 !== 4">{{ t1 }}℃ ~ {{ t2 }}℃일 때 입었던 옷 기록들</p>
            <p v-if="t2 === 4">{{ t2 }}℃ 이하일 때 입었던 옷 기록들</p>
            <br /><br /><br /><br />

            <!-- ootw 리스트 -->
            <div class="FlipCardcontainer">
                <div class="slider-container">
                    <span class="slider-leftBtn sliderBtn" @click="scrollLeft" ref="leftBtn"></span>

                    <!-- ref는 Vue에서 사용되는 특별한 속성으로, 
                        Vue 컴포넌트 내에서 DOM 요소나 자식 컴포넌트에 접근할 수 있도록 도와줍니다.  
                        ref를 사용하면 DOM 요소나 컴포넌트에 고유한 참조(reference)를 설정할 수 있습니다. 
                        이 참조를 사용하여 해당 요소나 컴포넌트에 직접 접근하고 조작할 수 있습니다. 
                        ref 속성을 통해 지정된 요소나 컴포넌트는 Vue 인스턴스의 $refs 객체에 저장되며, 이를 통해 참조를 얻을 수 있습니다.-->
                    <div class="ootw-list" ref="slider">

                        <div class="ootw-item" v-for="(ootw, i) in allOotwList" :key="ootw.ootwnum"
                            :style="{ width: '240px', flex: '0 0 250px' }" @click="modalOpen(ootw.ootwnum)"
                            @mouseover="cursorChange" @mouseout="resetCursor">
                            <img :src="'http://localhost:8081/closets/img/' + memnum + '/' + allImageList[i]"> <br />
                            <div class="ootw-contents"
                                style="text-align: left; margin-left:10px; margin-top:10px; font-size: 14px; color: rgb(158, 157, 157)">
                                {{ datelist[i] }}&nbsp;&nbsp;&nbsp;{{ ootw.weather }}&nbsp;&nbsp;&nbsp;{{ ootw.temp }}℃
                            </div>
                            <div style="width:250px; text-align: left; margin-left:10px; margin-top:8px; font-size: 16px; font-weight: bold;
                            overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                                {{ ootw.comments }}
                            </div>
                        </div>
                    </div>
                    <!-- 더보기 버튼 뜨기 -->
                    <!-- <div v-if="gap > 0">
                    <img class="plus" src="@/assets/weatherPlus.svg" @click="moreBtn" @mouseover="cursorChange"
                        @mouseout="resetCursor">
                    </div> -->
                    <span class="slider-rightBtn sliderBtn" @click="scrollRight" ref="rightBtn"></span>
                </div>
            </div>

            <!-- ootw 디테일 (모달) -->
            <!-- 사진 전부, 날짜, 기온, 날씨, 코멘트 -->
            <div class="modal-wrap" v-show="modalCheck" @click="modalClose" id="modalWrap">
                <div class="modal-container" @click.stop="" id="container">
                    <span v-for="num in clothnum" :key="num">
                        <img :src="'http://localhost:8081/closets/img/' + memnum + '/' + num" alt="sdf">
                    </span>
                    <br /><br />
                    <h6>{{ odate }}의 OOTW</h6>
                    날씨: {{ weather }}<br />
                    기온: {{ temp }}℃<br /><br />
                    {{ comments }}<br />
                    <button v-on:click="deleteOootw(ootwnum)">삭제</button>
                </div>
            </div>
        </div>

        <br /><br /><br /><br /><br /><br /><br />

    </div>
</template>

<script>
export default {
    name: 'WthCloset',
    data() {
        return {
            nowTmp: sessionStorage.getItem("nowTmp"),
            memnum: sessionStorage.getItem("memnum"),
            message: '', // 로그인하면 옷장에서 옷 추천해드림~
            t1: 23, // 최저기온
            t2: 27, // 최고기온
            showRecom: false, // 로그인 여부 체크
            allOotwList: [], // 기온 검색 ootw 전체 리스트
            // displayedOotw: [], // ootw dto 5개 (수정될 수 있음)만 띄울거임~
            allImageList: [], // 기온 검색 ootw 첫번째 이미지만 모여있는 리스트
            datelist: [],
            // currentPage: 1,
            // gap: 0,
            // leftBtn: [],
            // rightBtn: [],
            // slider: [],
            // containerDimension: 0,
            // ~~~~~~~~~~~~ 디테일 모달 ~~~~~~~~~~~~~~
            modalCheck: false,
            clothnum: [], // 옷 번호 담고 있는 배열
            odate: '',
            weather: '',
            temp: 0,
            comments: '',
            ootwnum: 0

        }
    },
    /*
    created() 훅은 Vue 인스턴스가 생성된 후에 호출되는 초기화 단계에서 실행됩니다. 
    이 단계에서는 Vue 인스턴스의 데이터와 메서드에 접근할 수 있습니다. 
    일반적으로 데이터를 가져오거나 초기 설정을 수행하는 데 사용됩니다.
    */
    async created() {
        // 현재 로그인 상태 확인.
        let token = sessionStorage.getItem('token');
        if (this.memnum == null) {
            this.message = "지금 날씨에 입었던 옷이 궁금하신가요? 로그인 후 OOTW를 작성해보세요!";
            this.showRecom = false;
        } else {
            this.$axios.get(`http://localhost:8081/members/${this.memnum}`,
                { headers: { 'token': token } })
                .then(async res => {
                    if (res.status == 200) {
                        this.showRecom = !this.showRecom;
                        const dto = res.data.dto
                        if (dto == null) {
                            console.log('없는 아이디거나 만료된 토큰')
                        } else {
                            this.recommendation();
                        }
                    } else {
                        alert("오류 발생으로 인한 로그인 정보 불러오기 실패")
                    }
                })
        }
    },
    /*
    mounted() 훅은 Vue 인스턴스가 DOM에 마운트된 후에 호출됩니다. 
    이 단계에서는 Vue 컴포넌트의 DOM 요소에 직접 접근할 수 있습니다. 
    일반적으로 외부 라이브러리와의 통합, DOM 조작, 이벤트 처리 등을 수행하는 데 사용됩니다.
    */
    // mounted() 훅 내에서 $refs를 사용하여 DOM 요소에 접근하려면, 해당 요소가 마운트된 후에 접근 가능
    // $nextTick()은 Vue의 비동기 큐를 사용하여 다음 DOM 업데이트 사이클 전에 콜백 함수를 실행하는 것을 보장
    mounted() {
        setTimeout(() => {
            this.$nextTick(() => {
                const sliderElement = this.$refs.slider;
                const leftBtnElement = this.$refs.leftBtn;
                const rightBtnElement = this.$refs.rightBtn;

                console.log('Slider Element:', this.$refs.slider);
                console.log('Left Button Element:', this.$refs.leftBtn);
                console.log('Right Button Element:', this.$refs.rightBtn);

                if (sliderElement) {
                    const containerDimension = sliderElement.getBoundingClientRect().width;

                    const slidingFunction = () => {
                        if (sliderElement.offsetWidth + sliderElement.scrollLeft >= sliderElement.scrollWidth - 100) {
                            sliderElement.scrollLeft = 0;
                        } else {
                            sliderElement.scrollLeft += containerDimension;
                        }
                    };

                    // 자동 슬라이딩
                    setInterval(slidingFunction, 5000);

                    // 스크롤 이벤트 핸들러 등록
                    sliderElement.addEventListener('scroll', () => {
                        if (sliderElement.offsetWidth + sliderElement.scrollLeft >= sliderElement.scrollWidth - 100) {
                            rightBtnElement.style.display = 'none';
                            leftBtnElement.style.display = 'block';
                        } else if (sliderElement.scrollLeft === 0) {
                            leftBtnElement.style.display = 'none';
                            rightBtnElement.style.display = 'block';
                        } else {
                            leftBtnElement.style.display = 'block';
                            rightBtnElement.style.display = 'block';
                        }
                    });

                    // 초기 버튼 상태 설정
                    if (sliderElement.offsetWidth >= sliderElement.scrollWidth) {
                        leftBtnElement.style.display = 'none';
                        rightBtnElement.style.display = 'none';
                    } else {
                        leftBtnElement.style.display = 'none';
                        rightBtnElement.style.display = 'block';
                    }

                    // 슬라이드 버튼 클릭 이벤트 핸들러 등록
                    rightBtnElement.addEventListener('click', () => {
                        sliderElement.scrollLeft += containerDimension;
                        leftBtnElement.style.display = 'block';
                    });

                    leftBtnElement.addEventListener('click', () => {
                        sliderElement.scrollLeft -= containerDimension;
                        rightBtnElement.style.display = 'block';
                    });
                } else {
                    console.error("Slider element is undefined.");
                }
            });
        }, 100);
    },
    methods: {
        async recommendation() {
            const tmp = this.nowTmp;
            let t1 = 0;
            let t2 = 0;

            if (tmp >= 28) { // 28도 이상
                t1 = 28;
                t2 = 100;

            } else if (20 <= tmp && tmp <= 22) { // 20~22도
                t1 = 20;
                t2 = 22;

            } else if (17 <= tmp && tmp <= 19) { // 17~19도
                t1 = 17;
                t2 = 19;

            } else if (12 <= tmp && tmp <= 16) { // 12~16도
                t1 = 12;
                t2 = 16;

            } else if (9 <= tmp && tmp <= 11) { // 9~11도
                t1 = 9;
                t2 = 11;

            } else if (5 <= tmp && tmp <= 8) { // 5~8도
                t1 = 5;
                t2 = 8;


            } else if (tmp <= 4) { // 4도 이하
                t1 = -100;
                t2 = 4;

            } else if (23 <= tmp && tmp <= 27) { // 23~27도
                t1 = 23;
                t2 = 27;
            }
            this.t1 = t1;
            this.t2 = t2;

            // OOTW 검색
            try {
                const res = await this.$axios.get('http://localhost:8081/boards/temps/' + this.memnum + '/' + t1 + '/' + t2)
                if (res.status === 200) {
                    // 게시글의 첫번째 이미지 번호만 담긴 전체 리스트 (dto 순으로 들어와있음)
                    this.allImageList = res.data.closetNumList;

                    this.allOotwList = res.data.list;
                    // const fiveOotw = this.allOotwList.slice(0, 5);
                    // this.displayedOotw = allOotwList;
                    // 더보기 조건으로 쓰임
                    // this.gap = this.allOotwList.length - this.displayedOotw.length;
                    if (this.allOotwList != '') { // 데이터가 있으면 
                        for (let i = 0; i < this.allOotwList.length; i++) {
                            const year = this.allOotwList[i].odate.substring(0, 4);
                            const month = this.allOotwList[i].odate.substring(5, 7);
                            const day = this.allOotwList[i].odate.substring(8, 10);
                            const date = year + "/" + month + "/" + day;
                            this.datelist[i] = date;
                        }
                    } else {
                        this.message = 'OOTW 가 비었습니다.';
                    }
                } else {
                    alert('에러 코드: ' + res.status)
                }
            } catch (error) {
                alert('에러 발생: ' + error);
            }


        },
        scrollLeft() {
            const sliderElement = this.$refs.slider;
            const containerDimension = sliderElement.getBoundingClientRect().width;
            sliderElement.scrollLeft -= containerDimension;
            console.log('왼쪽버튼 누름: ' + sliderElement.scrollLeft)
        },
        scrollRight() {
            const sliderElement = this.$refs.slider;
            const containerDimension = sliderElement.getBoundingClientRect().width;
            sliderElement.scrollLeft += containerDimension;
            console.log('오른쪽버튼 누름: ' + sliderElement.scrollLeft)
        },

        // 더보기 버튼 누르면 5개씩 추가 
        // moreBtn() {
        //     const n = this.currentPage;
        //     // n=1이면 5개, n=2이면 10개 떠있음
        //     // (0, 5) -> (5, 10) -> (10, 15)
        //     const start = n * 5; // n=1 -> 5
        //     const end = start + 5; // start=5 -> 10
        //     let additionalRow = this.allOotwList.slice(start, end)
        //     this.displayedOotw = this.displayedOotw.concat(additionalRow);
        //     this.currentPage++;
        //     this.gap = this.allOotwList.length - this.displayedOotw.length;
        // },

        // 카드 누르면 OOTW 디테일 페이지로 넘어감
        modalOpen(ootwnum) {
            const self = this;
            self.ootwnum = ootwnum;
            self.clothnum = [];
            self.modalCheck = !this.modalCheck;
            self.$axios.get('http://localhost:8081/boards/' + ootwnum)
                .then(function (res) {
                    if (res.status == 200) {
                        // ootw dto
                        const dto = res.data.dto;
                        if (dto !== null) {
                            // this.odate = dto.odate
                            const year = dto.odate.substring(0, 4);
                            const month = dto.odate.substring(5, 7);
                            const day = dto.odate.substring(8, 10);
                            self.odate = year + "년 " + month + "월 " + day + "일";
                            self.weather = dto.weather
                            self.temp = dto.temp
                            self.comments = dto.comments
                        } else {
                            alert("dto 비었다~")
                        }
                        // 각 dto별 이미지 번호 담긴 리스트~~
                        const list = res.data.list;
                        for (let i = 0; i < list.length; i++) {
                            self.clothnum.push(list[i].closetnum.closetnum);
                        }
                    } else {
                        alert('에러코드: ' + res.status)
                    }
                })
        },

        // // 모달창 닫기
        modalClose() {
            this.modalCheck = !this.modalCheck;
        },

        // 카드에 마우스 올리면 크기 변하는 메서드
        // 마우스를 올렸을 때 요소의 크기를 1.05배로 확대(scale up)시키는 스타일을 적용
        cursorChange(e) {
            e.currentTarget.style.transform = "scale(1.05)";
        },
        // 마우스가 요소를 벗어났을 때 원래 크기로 되돌리는 스타일을 적용
        resetCursor(e) {
            e.currentTarget.style.transform = "scale(1)";
        },

        // 모달창에서 OOTW 삭제
        deleteOootw(ootwnum) {
            const self = this;
            self.$axios.delete('http://localhost:8081/boards/' + ootwnum)
                .then(function (res) {
                    if (res.status == 200) {
                        location.reload();
                    } else {
                        alert('에러 코드: ' + res.status)
                    }
                })
        }

    }
}
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Pacifico&display=swap");

/* @font-face {
    font-family: 'Cafe24Ohsquareair';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2202@1.0/Cafe24Ohsquareair.woff') format('woff');
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
    /* font-weight: 100; */
    font-style: normal;
}

.FlipCardcontainer {
    display: flex; 
    justify-content: center; 
    align-items: center;
}

.slider-container {
    margin: 0 auto; 
    position: relative; 
    padding: 10px; 
    overflow: hidden; 
    max-width: 1100px;
}

.ootw-list {
    /* width: 80%; */
    /* grid-template-columns: repeat(auto-fill, minmax(25%, auto)); */
    row-gap: 50px;
    /* max-width: 1000px; */
    height: 300px;
    
    display: grid;
    gap: 30px;
    grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));

    /* margin-top: 5px; */
    /* margin: 0 auto; */
    /* 좌우 여백 추가 */
    padding-left: 830px;
    /* padding-right: 1000px; */
    /* 가로 스크롤바 감춤 */
    /* overflow: hidden; */
    margin-left: 50px;
    margin-right: 50px;


    grid-auto-flow: column;
    overscroll-behavior-inline: contain;
    scroll-behavior: smooth;
    scroll-snap-type: inline mandatory;
    scroll-padding-inline: 1rem;
    /* scroll-padding: 0 20px; */
    
    align-items: center;
    justify-content: center;
    
    /* display: flex; */
    overflow-x: auto;
    /* white-space: nowrap; */
}

.ootw-item {
    width: 100%;
    height: 230px;

    display: flex;
    flex-direction: column;
    align-items: center;
    /* justify-content: center; */
    /* scroll-snap-align: start; */
    transition: all 0.5s;

    background: transparent;
    background-color: rgba(255, 255, 255, 0.6);
    /* border-radius: 8px; */
    padding: 20px;
    /* 버튼과의 간격을 위한 하단 마진 추가 */
    /* margin-bottom: 20px;  */
    text-align: left;
}

/*scrollbar숨기기 */
::-webkit-scrollbar {
    display: none;
}

.ootw-item:hover {
    /* background-color: rgb(242, 242, 242); */
    transform: scale(1.05);
}

/* .ootw-contents {
    margin-left: 10px;
} */

.ootw-item img {
    display: block;
    position: relative;
    margin-top: -20px;
    width: 280px;
    height: 180px;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    object-fit: cover;


    /* margin: 0 auto; */
    /* position: relative; */
    /* width: 160px; */
    /* height: 160px; */
    /* object-fit: cover; */
    /* border-radius: 3px; */
    /* margin-bottom: 10px; */

}

/* 화살표 속성 */
.sliderBtn {
    width: 40px;
    height: 40px;
    border-top: 10px solid white;
    border-left: 10px solid white;
    position: absolute;
    cursor: pointer;
    top: 30%;
}

/* border모양을 돌려서 화살표모양만들기 */
.slider-leftBtn {
    transform: rotate(-45deg);
    left: 1rem;
}

.slider-rightBtn {
    transform: rotate(135deg);
    right: 1rem;
}

.plus {
    transition: transform 0.3s ease;
    /* 변화가 일어날 때 0.3초 동안 부드럽게 전환 */
    margin-top: 50px;
    margin-bottom: 30px;
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
    background: rgba(0, 0, 0, 0.1);
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

.modal-container img {
    width: 200px;
    height: 200px;
}
</style>