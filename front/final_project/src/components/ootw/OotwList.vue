<template>
    <div class="body-css">
        <div ref="stickyPoint">
            <div class="board-search" :class="{ sticky: isSticky }">
                <ul style="list-style-type: none;">
                    <li style="border:1px solid lightgray; width:360px; height:70px;">
                        <br />날짜 검색<br />
                        <input type="date" v-model="date1" id="date1">
                        ~ <input type="date" v-model="date2" id="date2">&nbsp;&nbsp;<button
                            v-on:click="searchByDate">검색</button>
                    </li><br />
                    <li style="border:1px solid lightgray; width:360px; height:70px">
                        <br />기온 검색<br />
                        <input type="number" v-model="temp1" id="temp1" style="width:130px;">
                        ~ <input type="number" v-model="temp2" id="temp2" style="width:130px;">
                        &nbsp;<button v-on:click="a">검색</button>
                    </li><br />
                    <li style="border:1px solid lightgray; width:360px; height:70px">
                        <br />내용 검색<br />
                        <input type="text" v-model="comments" id="comments" style="width:277px;">
                        &nbsp;<button v-on:click="b">검색</button>
                    </li>
                </ul>
            </div>
        </div>
        <div class="board-list" v-if="memnum == checkMemnum">
            <span class="board-title">Outfit Of the Weather</span>
            <a href="/ootwadd">게시글 작성</a>
            <table style="border:1px solid lightgray;">
                <tr style="border:1px solid lightgray;">
                    <th>날짜</th>
                    <th>Comment</th>
                    <th>날씨</th>
                    <th>기온</th>
                </tr>
                <tr v-for="(ootw, index) in displayedootw" :key="index">
                    <!-- <td>{{ datelist[index] }}</td> -->
                    <td>{{ datelist[index] }}</td>
                    <td><a v-on:click="detail(ootw.ootwnum)">{{ ootw.comments }}</a></td>
                    <td>{{ ootw.weather }}</td>
                    <td>{{ ootw.temp }}</td>
                </tr>
            </table><br />
            <ul>
                <il v-for="(page, index) in pageIndex" :key="index">
                    <a v-on:click="pageBtn(page, index)" style="cursor: pointer; font-size:18px" id="selected">{{ page }}&nbsp;&nbsp;</a>
                </il>
            </ul>
            <!-- <button v-on:click="moreBtn">더보기</button> -->
        </div>
        <div class="empty-board-list" v-if="memnum != checkMemnum">등록된 게시글이 없습니다.</div>
    </div>
</template>

<script>

export default {
    name: 'OotwList',
    data() {
        return {
            ootwlist: [],
            displayedootw: [],
            pageIndex: [],
            datelist: [],
            displayedDate: [],
            ootwPerPage: 5,
            currentPage: 1,
            NumPerPage: 5,
            weather: '',
            date1: '',
            date2: '',
            memnum: sessionStorage.getItem('memnum'),
            checkMemnum: '',
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
        self.$axios.get('http://localhost:8081/boards')
            .then(function (res) {
                if (res.status == 200) {
                    self.ootwlist = res.data.list;
                    if (self.ootwlist != '') {
                        self.checkMemnum = self.ootwlist[0].memnum.memnum;
                        for (let i = 0; i < self.ootwlist.length; i++) {
                            var year = self.ootwlist[i].odate.substring(0, 4);
                            var month = self.ootwlist[i].odate.substring(5, 7);
                            var day = self.ootwlist[i].odate.substring(8, 10);
                            var date = year + "년 " + month + "월 " + day + "일";
                            self.datelist[i] = date;
                        }
                        // list사이즈 나누기 한페이지에 보여줄 게시글 수 floor값 = max page
                        let left = (self.ootwlist.length % 5);
                        let maxPage = '';
                        if(left == 0) {
                            maxPage = Math.floor(self.ootwlist.length / 5);
                        } else if(left > 0) {
                            maxPage = Math.floor(self.ootwlist.length / 5) + 1;
                        }
                        // 한페이지에 보여줄 게시글 수보다 리스트에 담긴 수가 적으면 floor값은 0이므로, 강제로 page에 1 넣었음
                        if (maxPage == 0) {
                            self.pageIndex[0] = 1;
                        } else {
                            // 그 외, page 배열에 사이즈만큼 페이지 수 넣어주기
                            for (let j = 1; j <= maxPage; j++) {
                                self.pageIndex.push(j)
                            }
                        }
                    }
                    self.displayedootw = self.ootwlist.slice(0, self.ootwPerPage);
                    self.displayedDate = self.datelist.slice(0, self.ootwPerPage);
                } else {
                    alert('에러코드: ' + res.status);
                }
            })
    },
    methods: {
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
        pageBtn(page, index) {
            const self = this;
            const startPage = (page - 1) * self.NumPerPage;
            const endPage = startPage + self.NumPerPage;
            self.displayedootw = self.ootwlist.slice(startPage, endPage);
            self.displayedDate = self.datelist.slice(startPage, endPage);
            let selected = document.querySelectorAll('#selected');
            for(let i=0; i<this.pageIndex.length; i++) {
                selected[i].style.color = 'black';
            }
            selected[index].style.color = 'red';

        },
        moreBtn() {
            const self = this;
            const startIndex = self.currentPage * self.ootwPerPage;
            const endIndex = startIndex + self.ootwPerPage;
            self.displayedootw = self.displayedootw.concat(self.ootwlist.slice(startIndex, endIndex));
            // self.displayedootw = [...self.displayedootw, ...self.ootwlist.slice(startIndex, endIndex)];
            self.currentPage++;
        },
        detail(ootwnum) {
            this.$router.push({ name: 'OotwDetail', query: { ootwnum: ootwnum } })
        },
        searchByDate() {
            const self = this;
            if (self.date2 < self.date1 || self.date1 == '' || self.date2 == '') {
                alert('날짜 범위를 다시 정해주세요.')
            } else {
                self.$axios.get('http://localhost:8081/boards/dates/' + self.date1 + "/" + self.date2)
                    .then(function (res) {
                        if (res.status == 200) {
                            if (res.data.list != '') {
                                self.ootwlist = res.data.list;
                                self.displayedootw = self.ootwlist.slice(0, self.ootwPerPage);
                            } else {
                                alert('선택한 범위의 검색 결과가 없습니다.')
                            }
                        } else {
                            alert('에러코드: ' + res.status);
                        }
                    });
            }
        }
    }
}
</script>

<style scoped>
.body-css {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
}

.board-search {
    position: absolute;
    top: 186px;
    left: 40px;
    width: 14%;
    z-index: 1;
}

.board-search.sticky {
    position: fixed;
    top: 10px;
    transition: top 1s ease;
}

.board-list {
    margin-top: 100px;
    margin-bottom: 10px;
}

.empty-board-list {
    margin-top: 100px;
    margin-bottom: 10px;
}

.board-list span {
    margin-right: 180px;
    font-size: 30px;
    font-weight: bold;
}

.board-list a {
    text-decoration: none;
    text-decoration-color: black;
}

table {
    margin-left: auto;
    margin-right: auto;
}

a {
    cursor: pointer;
}
</style>