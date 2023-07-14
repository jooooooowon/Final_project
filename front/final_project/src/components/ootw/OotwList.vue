<template>
    <div class="body-css">
        <!-- 좌측 검색바 -->
        <div ref="stickyPoint">
            <div class="board-search" :class="{ sticky: isSticky }">
                <ul style="list-style-type: none; width:360px;" class="board-search-ul">
                    <li style="width:360px; height:130px;"><br /><br /><br />
                        <input type="date" v-model="date1" id="date1"
                            style="font-family: 'PyeongChang-Regular'; font-size:13px; width:126px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
                            data-placeholder="시작일" aria-required="true" required>
                        &nbsp;<input type="date" v-model="date2" id="date2"
                            style="font-family: 'PyeongChang-Regular'; font-size:13.5px; width:126px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
                            data-placeholder="종료일" aria-required="true" required>
                        <button v-on:click="searchByDate"
                            style="font-family: 'PyeongChang-Regular'; width:60px; height:32px; background-color: transparent; border:none">검색</button>
                    </li>
                    <li style="width:360px; height:130px"><br /><br /><br />
                        <input type="number" v-model="temp1" id="temp1"
                            style="font-family: 'PyeongChang-Regular'; font-size:13.5px; width:123px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
                            placeholder="최저 기온" onfocus="this.placeholder=''" onblur="this.placeholder='최저 기온'">
                        &nbsp;<input type="number" v-model="temp2" id="temp2"
                            style="font-family: 'PyeongChang-Regular'; font-size:13.5px; width:123px; height: 30px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
                            placeholder="최고 기온" onfocus="this.placeholder=''" onblur="this.placeholder='최고 기온'">
                        <button v-on:click="searchByTemp"
                            style="font-family: 'PyeongChang-Regular'; width:60px; height:32px; background-color: transparent; border:none">검색</button>
                    </li>
                    <li style="width:360px; height:130px;"><br /><br /><br />
                        <input type="search" v-model="comments" id="comments"
                            style="font-family: 'PyeongChang-Regular'; font-size:13.5px; width:270px; height: 35px; text-align: center; color:rgb(161, 157, 157); font-weight: 500; border-color: lightgray; border-radius: 5px;"
                            placeholder="커멘트 검색하기" onfocus="this.placeholder=''" onblur="this.placeholder='커멘트 검색하기'">
                        <button v-on:click="searchByComments"
                            style="font-family: 'PyeongChang-Regular'; width:60px; height:32px; background-color: transparent; border:none">검색</button>
                    </li>
                </ul>
            </div>
        </div>


        <div class="ootw-title">
            <span style="font-size: 30px; font-weight: bold; margin-left: 220px;" class="ootw-title-title">
                <a href="/ootwlist" class="ootw-title-link">Outfit Of the Weather</a>
            </span>
            <span style="margin-left:150px;"><a class="ootw-title-add" href="/ootwadd" style="">게시글 작성</a></span>
        </div>
        <div class="empty-ootw-list" v-if="isList == false">등록된 게시글이 없습니다.</div>
        <div class="ootw-wrap" v-if="isList == true">
            <div class="ootw-list">
                <div class="ootw-item" v-for="(ootw, index) in ootwlist" :key="ootw.ootwnum"
                    v-on:click="detail(ootw.ootwnum)">
                    <img :src="'http://localhost:7878/closets/img/' + memnum + '/' + closetNumList[index]"><br />
                    <div class="ootw-contents">
                        {{ datelist[index] }}&nbsp;&nbsp;&nbsp;{{ ootw.weather }}&nbsp;&nbsp;&nbsp;{{ ootw.temp }}도
                    </div>
                    <div style="width:250px; text-align: left; margin-left:14px; margin-top:8px; font-size: 16px; font-weight: bold;
                    overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                        {{ ootw.comments }}
                    </div>
                </div>
            </div>
        </div>


        <div class="naver-shopping">
            <router-link to="naverShoppingList"><img src="../../assets/navershopping2.png"
                    alt="naver-shopping-icon"></router-link>
        </div>
        <!-- <div style="position: fixed; bottom: 20px; right:140px"><a v-on:click="goUp" style="font-size:40px">∧</a></div> -->
    </div>
</template>

<script>

export default {
    name: 'OotwList',
    data() {
        return {
            ootwlist: [],
            closetNumList: [],
            // displayedootw: [],
            pageIndex: [],
            datelist: [],
            // displayedDate: [],
            ootwPerPage: 5,
            currentPage: 1,
            // NumPerPage: 5,
            weather: '',
            date1: '',
            date2: '',
            temp1: '',
            temp2: '',
            comments: '',
            memnum: sessionStorage.getItem('memnum'),
            isList: '',
            // 검색창
            isSticky: false
        }
    },
    mounted() {
        window.addEventListener('scroll', this.stickyScroll);
        this.stickyScroll(); // 컴포넌트 마운트될 때 메서드 초기 호출하여 초기상태 설정
    },
    created: function () {
        const self = this;
        self.memnum = sessionStorage.getItem('memnum');
        if(self.memnum == null) {
            alert('로그인 화면으로 이동합니다.')
            location.href = '/login'
        } else {
            self.$axios.get('http://localhost:7878/boards/members/' + self.memnum)
                .then(function (res) {
                    if (res.status == 200) {
                        self.ootwlist = res.data.list;
                        self.closetNumList = res.data.closetNumList;
                        if (self.ootwlist != '') {
                            // self.checkMemnum = self.ootwlist[0].memnum.memnum;
                            self.isList = true;
                            for (let i = 0; i < self.ootwlist.length; i++) {
                                var year = self.ootwlist[i].odate.substring(0, 4);
                                var month = self.ootwlist[i].odate.substring(5, 7);
                                var day = self.ootwlist[i].odate.substring(8, 10);
                                var date = year + "/" + month + "/" + day;
                                self.datelist[i] = date;
                            }
                            // // list사이즈 나누기 한페이지에 보여줄 게시글 수 floor값 = max page
                            // let left = (self.ootwlist.length % 5);
                            // let maxPage = '';
                            // if (left == 0) {
                            //     maxPage = Math.floor(self.ootwlist.length / 5);
                            // } else if (left > 0) {
                            //     maxPage = Math.floor(self.ootwlist.length / 5) + 1;
                            // }
                            // // 한페이지에 보여줄 게시글 수보다 리스트에 담긴 수가 적으면 floor값은 0이므로, 강제로 page에 1 넣었음
                            // if (maxPage == 0) {
                            //     self.pageIndex[0] = 1;
                            // } else {
                            //     // 그 외, page 배열에 사이즈만큼 페이지 수 넣어주기
                            //     for (let j = 1; j <= maxPage; j++) {
                            //         self.pageIndex.push(j)
                            //     }
                            // }
                        } else {
                            self.isList = false;
                        }
                        // self.displayedootw = self.ootwlist.slice(0, self.ootwPerPage);
                        // self.displayedDate = self.datelist.slice(0, self.ootwPerPage);
                    } else {
                        alert('에러코드: ' + res.status);
                    }
                })
        }
    },
    methods: {
        goUp(){
            window.scrollTo({top: 0, behavior: "smooth"})
        },
        stickyScroll() {
            const stickyPoint = this.$refs.stickyPoint;
            if (!stickyPoint) {
                return; // stickyPoint가 정의되지 않은 경우 종료
            } else {
                const menuOffsetTop = stickyPoint.offsetTop;
                if (window.pageYOffset > menuOffsetTop) {
                    this.isSticky = true;
                    // this.$nextTick(() => {
                    stickyPoint.style.top = '10px'; // 원하는 간격으로 조정
                    // });
                } else {
                    this.isSticky = false;
                    // this.$nextTick(() => {
                    // stickyPoint.style.top = '-30px'; // 초기 위치로 설정
                    // });
                }
            }
        },
        // pageBtn(page, index) {
        //     const self = this;
        //     const startPage = (page - 1) * self.NumPerPage;
        //     const endPage = startPage + self.NumPerPage;
        //     self.displayedootw = self.ootwlist.slice(startPage, endPage);
        //     self.displayedDate = self.datelist.slice(startPage, endPage);
        //     let selected = document.querySelectorAll('#selected');
        //     for (let i = 0; i < this.pageIndex.length; i++) {
        //         selected[i].style.color = 'black';
        //     }
        //     selected[index].style.color = 'red';

        // },
        detail(ootwnum) {
            this.$router.push({ name: 'OotwDetail', query: { ootwnum: ootwnum } })
        },
        searchByDate() {
            const self = this;
            if (self.date2 < self.date1 || self.date1 == '' || self.date2 == '') {
                alert('날짜 범위를 다시 정해주세요.')
            } else {
                self.$axios.get('http://localhost:7878/boards/dates/' + self.memnum + "/" + self.date1 + "/" + self.date2)
                    .then(function (res) {
                        if (res.status == 200) {
                            if (res.data.list != '') {
                                self.ootwlist = res.data.list;
                                self.closetNumList = res.data.closetNumList;
                                if (self.ootwlist != '') {
                                    self.checkMemnum = self.ootwlist[0].memnum.memnum;
                                    for (let i = 0; i < self.ootwlist.length; i++) {
                                        var year = self.ootwlist[i].odate.substring(0, 4);
                                        var month = self.ootwlist[i].odate.substring(5, 7);
                                        var day = self.ootwlist[i].odate.substring(8, 10);
                                        var date = year + "/" + month + "/" + day;
                                        self.datelist[i] = date;
                                    }
                                }
                                self.date1 = '';
                                self.date2 = '';
                            } else {
                                alert('선택한 범위의 검색 결과가 없습니다.')
                            }
                        } else {
                            alert('에러코드: ' + res.status);
                        }
                    });
            }
        },
        searchByTemp() {
            const self = this;
            if (self.temp1 > self.temp2 || self.temp1 == '' || self.temp2 == '') {
                alert("기온 범위를 다시 정해주세요.")
            } else {
                self.$axios.get('http://localhost:7878/boards/temps/' + self.memnum + "/" + self.temp1 + "/" + self.temp2)
                    .then(function (res) {
                        if (res.status == 200) {
                            if (res.data.list != '') {
                                self.ootwlist = res.data.list;
                                self.closetNumList = res.data.closetNumList;
                                if (self.ootwlist != '') {
                                    self.checkMemnum = self.ootwlist[0].memnum.memnum;
                                    for (let i = 0; i < self.ootwlist.length; i++) {
                                        var year = self.ootwlist[i].odate.substring(0, 4);
                                        var month = self.ootwlist[i].odate.substring(5, 7);
                                        var day = self.ootwlist[i].odate.substring(8, 10);
                                        var date = year + "/" + month + "/" + day;
                                        self.datelist[i] = date;
                                    }
                                }
                                self.temp1 = '';
                                self.temp2 = '';
                            } else {
                                alert('선택한 범위의 검색 결과가 없습니다.')
                            }
                        } else {
                            alert('에러코드: ' + res.status);
                        }
                    })
            }
        },
        searchByComments() {
            const self = this;
            if (self.comments == '') {
                alert("커멘트 키워드를 검색해 주세요.")
            } else {
                self.$axios.get('http://localhost:7878/boards/comments/' + self.comments + "/" + self.memnum)
                    .then(function (res) {
                        if (res.status == 200) {
                            if (res.data.list != '') {
                                self.ootwlist = res.data.list;
                                self.closetNumList = res.data.closetNumList;
                                if (self.ootwlist != '') {
                                    self.checkMemnum = self.ootwlist[0].memnum.memnum;
                                    for (let i = 0; i < self.ootwlist.length; i++) {
                                        var year = self.ootwlist[i].odate.substring(0, 4);
                                        var month = self.ootwlist[i].odate.substring(5, 7);
                                        var day = self.ootwlist[i].odate.substring(8, 10);
                                        var date = year + "/" + month + "/" + day;
                                        self.datelist[i] = date;
                                    }
                                }
                                self.comments = '';
                            } else {
                                alert('검색하신 키워드에 해당하는 게시글이 없습니다.')
                            }
                        } else {
                            alert('에러코드: ' + res.status);
                        }
                    })
            }
        }
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

a {
    cursor: pointer;
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
}

button:hover {
    background-color: #85b380;
    color: #85b380;
    /* color: #85b380; */
    cursor: pointer;
    font-weight: bold;
}

.ootw-title {
    margin-top: 60px;
    margin-bottom: 5px;
    display: flex;
    justify-content: space-around;
}

.ootw-title-link {
    color: rgb(97, 97, 97);
    text-decoration: none;
}

.ootw-title-link:hover {
    color: #85b380;
    font-weight: bold;
    transition: transform 0.3s ease;
}

.ootw-title-add {
    font-weight: bold;
    text-decoration: none;
    font-size: 17px;
    color: black;
}

.ootw-title-add:hover {
    color: #85b380;
}

/* 카드 5열 형식 */
.ootw-wrap {
    position: relative;
    /* max-width: 50%; */
}

.ootw-list {
    display: grid;
    width: 63%;
    grid-template-columns: repeat(auto-fill, minmax(25%, auto));
    gap: 30px;
    row-gap: 50px;
    margin-left: 390px;
    margin-top: 5px;
}

.ootw-item {
    /* display: flex;
    flex-direction: column;
    align-items: flex-start; */
    transition: transform 0.3s ease;
    height: 250px;
}

.ootw-item:hover {
    background-color: rgb(242, 242, 242);
    transform: scale(1.05);
}

.ootw-contents {
    text-align: left;
    margin-left:14px;
    margin-top:10px;
    font-size: 14px;
    color: rgb(158, 157, 157)
}

.ootw-item img {
    position: relative;
    margin-top: 6px;
    width: 280px;
    /* 가로 사이즈 200px로 고정 */
    height: 180px;
    /* 세로 사이즈 200px로 고정 */
    object-fit: cover;
    /* 이미지가 카드 영역에 꽉 차도록 설정 */
    border-radius: 3px;
}

.empty-ootw-list {
    margin-top: 30px;
    margin-bottom: 10px;
}

.board-search {
    position: absolute;
    top: 278px;
    left: 18px;
    width: 14%;
    z-index: 1;
}

.board-search-ul {
    box-shadow: 0 1px 8px rgba(0, 0, 0, 0.1);
}

.board-search.sticky {
    position: fixed;
    top: 10px;
    transition: top 1s ease;
}

input[type='date']::before {
    content: attr(data-placeholder);
    width: 100%;
}

input[type='date']:focus::before,
input[type='date']:valid::before {
    display: none;
}

/* 네이버 쇼핑 */
.naver-shopping img {
    position: fixed;
    right: -26px;
    bottom: -30px;
    margin: 70px;
    z-index: 1;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, .45);
    transition: .5s;
    border-radius: 50%;
    width: 80px;
    height: 80px;
}

.naver-shopping:hover img {
    bottom: 15px;
}
</style>