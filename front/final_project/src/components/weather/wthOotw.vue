<template>
    <div class="back">
        <p style="margin-top: 100px; margin-bottom: 100px;">{{ message }}</p> 

        <div v-if="showRecom">
            <p v-if="t1 === 28">{{ t1 }}℃ 이상일 때 입었던 옷 기록들</p>
            <p v-if="t1 !== 28 && t2 !== 4">{{ t1 }}℃ ~ {{ t2 }}℃일 때 입었던 옷 기록들</p>
            <p v-if="t2 === 4">{{ t2 }}℃ 이하일 때 입었던 옷 기록들</p>
            <br/><br/><br/><br/>
            <!-- ootw 리스트 -->
            <!-- 사진1장, 기온, 날씨, 코멘트 -->
            <!-- 더보기 -->
            <div>
                <div class="closet"
                    style="display: flex; flex-wrap: wrap; justify-content: center; align-items: center;">
                    <!-- v-for) additionalCloset[subtag] 배열을 반복하면서 각 cloth에 대한 작업을 수행 -->
                    <div class="cloth" v-for="(ootw, i) in displayedOotw" :key="ootw.ootwnum"
                        :style="{ maxWidth: '200px', flex: '0 0 250px' }" @click="modalOpen(ootw.ootwnum)"
                        @mouseover="cursorChange" @mouseout="resetCursor">
                        <img :src="'http://localhost:8081/closets/img/' + memnum + '/' + allImageList[i]"> <br />
                        <!-- <p>{{ ootw.ootwnum }}</p> -->
                        <!-- <p>{{ ootw.odate }}</p> -->
                        <p>{{ ootw.temp }}℃ | {{ ootw.weather }}</p><br />
                        <p>{{ ootw.comments }}</p>
                    </div>
                </div>
                <!-- 더보기 버튼 뜨기 -->
                <div v-if="gap > 0">
                    <img class="plus" src="@/assets/weatherPlus.svg" @click="moreBtn" @mouseover="cursorChange" @mouseout="resetCursor">
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

        <br/><br/><br/><br/><br/><br/><br/>

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
            t1: 0, // 최고기온
            t2: 0, // 최저기온
            showRecom: false, // 로그인 여부 체크
            allOotwList: [], // 기온 검색 ootw 전체 리스트
            displayedOotw: [], // ootw dto 5개 (수정될 수 있음)만 띄울거임~
            allImageList: [], // 기온 검색 ootw 첫번째 이미지만 모여있는 리스트
            currentPage: 1,
            gap: 0,
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
    methods: {
        async recommendation() {
            const tmp = this.nowTmp;
            let t1 = 0;
            let t2 = 0;

            if (tmp >= 28) { // 28도 이상
                t1 = 28;
                t2 = 100;

            } else if (23 <= tmp && tmp <= 27) { // 23~27도
                t1 = 23;
                t2 = 27;

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

            } else { // 4도 이하
                t1 = -100;
                t2 = 4;
            }
            this.t1 = t1;
            this.t2 = t2;

            // OOTW 검색
            try {
                const res = await this.$axios.get('http://localhost:8081/boards/temps/' + this.memnum + '/' + t1 + '/' + t2)
                if (res.status === 200) {
                    // 게시글 dto 5개 잘라서 리스트로 띄우기
                    this.allOotwList = res.data.list;
                    const fiveOotw = this.allOotwList.slice(0, 5);
                    this.displayedOotw = fiveOotw;

                    // 게시글의 첫번째 이미지 번호만 담긴 전체 리스트 (dto 순으로 들어와있음)
                    this.allImageList = res.data.closetNumList;

                    // 더보기 조건으로 쓰임
                    this.gap = this.allOotwList.length - this.displayedOotw.length;

                } else {
                    alert('에러 코드: ' + res.status)
                }
            } catch (error) {
                alert('에러 발생: ' + error);
            }


        },

        // 더보기 버튼 누르면 5개씩 추가 
        moreBtn() {
            const n = this.currentPage;
            // n=1이면 5개, n=2이면 10개 떠있음
            // (0, 5) -> (5, 10) -> (10, 15)
            const start = n * 5; // n=1 -> 5
            const end = start + 5; // start=5 -> 10
            let additionalRow = this.allOotwList.slice(start, end)
            this.displayedOotw = this.displayedOotw.concat(additionalRow);
            this.currentPage++;
            this.gap = this.allOotwList.length - this.displayedOotw.length;
        },

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

.closet {
    display: grid;
    /* grid-template-columns: repeat(5, 1fr); */
    /* 최소 200px의 폭을 가지는 열을 자동으로 생성 */
    /* 그리드 컨테이너의 너비에 맞추어 최대한 많은 열을 표시 */
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); 
    grid-gap: 20px;
    justify-content: center;
    align-items: center;
    max-width: 1200px;
    margin: 0 auto;
}

.cloth {
    transition: transform 0.3s ease; /* 변화가 일어날 때 0.3초 동안 부드럽게 전환 */
    background-color: #fff;
    border-radius: 10px;
    padding: 20px;
    text-align: center;
    box-shadow: 0 2px 6px rgba(255, 255, 255, 0.1);
}

.cloth:hover  {
  transform: scale(1.05);
  background-color: rgba(255, 255, 255, 0.8);
}

.cloth img {
    display: block;
    margin: 0 auto;
    position: relative;
    width: 150px;
    height: 150px;
    object-fit: cover;
    /* border-radius: 10%; */
    margin-bottom: 10px;
}

.cloth h6 {
    margin: 0;
}

.plus {
    transition: transform 0.3s ease; /* 변화가 일어날 때 0.3초 동안 부드럽게 전환 */
    margin-top: 50px;
    margin-bottom: 30px;
}

.plus:hover {
    transform: scale(1.05);
}


/* dimmed */
.modal-wrap {
    z-index: 999; /* 모달의 배경을 카드보다 상위로 올립니다. */
    position: fixed;
    left: 0;
    top: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.1);
}


/* modal or popup */
.modal-container {
    z-index: 9999; /* 모달창을 다른 요소들보다 상위로 올림 */
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