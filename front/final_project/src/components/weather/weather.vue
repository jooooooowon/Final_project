<template>
  <div class="background">
    <div :class="getBackground(nowSky)">
      
      <div>
        <h4>{{ realDate }}</h4>
        <div>
          <div class="address">{{ address }}</div>
          <div style="display: flex; justify-content: center; align-items: center;">
            <div style="display: flex; align-items: center;">
              <img class="iconImg" :src="getIcon(nowSky)"> <br />
              <!-- <div v-if="nowPop !== '0%'">
              {{ nowPop }}
            </div> -->
            </div>
            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center;">
              <div style="text-align: center; color: #2c3e50;">
                <div class="tmp">{{ nowTmp }}°</div>
                <div class="tmx">{{ todayTmx }}° / {{ todayTmn }}°</div>
              </div>
            </div>
          </div>
        </div>
        <!-- <p>{{ textContent }}</p> -->
        <br /><br />
        <!-- <h3>24HOURS</h3> -->
        <div class="allWeather">
          <div class="inside">
            <table>
              <tr>
                <td v-for="eachTime in everyTime" :key="eachTime.fcstTime">
                  <div class="wInfo"><span class="time">{{ eachTime.fcstTime }}</span></div>
                  <div class="wInfoTwo" v-if="eachTime.pop !== ''">
                    <img :src="getIcon(eachTime.sky)"><br />
                    <span class="pop">{{ eachTime.pop }}</span><br />
                  </div>
                  <div class="wInfoOne" v-else>
                    <img :src="getIcon(eachTime.sky)"><br />
                  </div>
                  <div class="wInfo">{{ eachTime.tmp }}</div>
                </td>
              </tr>
            </table>
          </div>
        </div>

        <!-- 옷 추천~~ -->
        <hr />
        <div>
          <h3>기온별 옷차림</h3>
          <closet></closet>
        </div>
        <hr />

        <div>
          <h3>Outfit Of The Weather</h3>
          <ootw></ootw>
        </div>

        <!-- 네이버 쇼핑 아이콘 -->
        <div class="naver-shopping">
          <router-link to="naverShoppingList"><img src="@/assets/navershopping2.png"
              alt="naver-shopping-icon"></router-link>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import wthCloset from './wthCloset.vue'
import wthOotw from './wthOotw.vue'

export default {
  name: 'TodayWeather',
  data() {
    return {
      // textContent: '',
      latitude: '',
      longitude: '',
      x: '', // latitude를 변환한 x좌표
      y: '', // longitude를 변환한 y좌표
      today: '', // 오늘 날짜 20230624 -- 오늘 일출일몰 불러오는 데 쓰임
      yesterday: '', // 어제 날짜 20230623 -- 오늘내일 날씨 불러오는 데 쓰임
      tomorrow: '', // 내일 날짜 20230625 -- 내일 일출일몰 불러오는 데 쓰임
      nowTime: '', // 현재 시간 1600
      today_sunrise: '', // 오늘 일출
      today_sunset: '', // 오늘 일몰
      tomorrow_sunrise: '', // 내일 일출
      tomorrow_sunset: '', // 내일 일몰
      everyTime: [], // 오늘0000~내일2300까지의 정보객체를 담은 배열
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      address: '', // 주소명
      nowTmp: '', // 1시간 기온 
      nowSky: '', // 하늘 상태
      nowPty: '', // 강수 형태 -- 안씀~~
      nowPop: '', // 강수확률
      todayTmn: '', // 오늘 최저기온
      todayTmx: '', // 오늘 최고기온
      // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      weatherIcons: {
        1: require('@/assets/weatherIcons/1.svg'),
        3: require('@/assets/weatherIcons/3.svg'),
        4: require('@/assets/weatherIcons/4.svg'),
        5: require('@/assets/weatherIcons/5.svg'),
        6: require('@/assets/weatherIcons/6.svg'),
        7: require('@/assets/weatherIcons/7.svg'),
        8: require('@/assets/weatherIcons/8.svg'),
        9: require('@/assets/weatherIcons/9.svg'),
        11: require('@/assets/weatherIcons/11.svg'),
        12: require('@/assets/weatherIcons/12.svg'),
        13: require('@/assets/weatherIcons/13.svg'),
        14: require('@/assets/weatherIcons/14.svg'),
        15: require('@/assets/weatherIcons/15.svg'),
        16: require('@/assets/weatherIcons/16.svg'),
        100: require('@/assets/weatherIcons/100.svg'),
        200: require('@/assets/weatherIcons/200.svg')
      },
      background: ''
    }
  },
  components: {
    closet: wthCloset,
    ootw: wthOotw
  },
  computed: {
    realDate() {
      // Date 객체에는 slice 메서드가 없기 때문에
      // 날짜를 영어로 표시하기 위해 toLocaleString 메서드
      // 월을 영어로 표시하기 위해 toLocaleString 메서드에 options 객체를 전달하여 옵션을 설정
      const options = { month: 'long', day: 'numeric', year: 'numeric' };
      const d = new Date();
      return d.toLocaleString('en-US', options);
    },
    // realTime() {
    //   const time = new Date();
    //   let hh = time.getHours();
    //   let mm = time.getMinutes();

    //   return (hh < 10 ? '0' + hh : hh) + ':' + (mm < 10 ? '0' + mm : mm);
    // }
  },
  created() {
    this.getDate()

    // if (!("geolocation" in navigator)) {
    //   this.textContent = 'Geolocation is not available.';
    //   return;
    // }
    // this.textContent = 'Locating...';

    // Get position
    navigator.geolocation.getCurrentPosition(async pos => {
      this.latitude = pos.coords.latitude;
      this.longitude = pos.coords.longitude;
      // this.textContent = 'Your location data is ' + this.latitude + ', ' + this.longitude;
      this.dfsXyConv(this.latitude, this.longitude)
      this.toAddress()
      this.sunTime(this.today)
      this.sunTime(this.tomorrow)
      this.weather(this.recommendation) // 파라메터로 메서드도 넣을 수 있다구~~
    }, err => {
      console.log(err);
      this.textContent = err.message;
      this.latitude = '37.582425';
      this.longitude = '126.983977777777';
      this.dfsXyConv(this.latitude, this.longitude)
      this.toAddress()
      this.sunTime(this.today)
      this.sunTime(this.tomorrow)
      this.weather(this.recommendation)
    });

  },
  methods: {
    // 시계 업데이트
    // updateClockDisplay() {
    //   // computed로 할당된 realTime 속성값을 clockDisplay 요소의 내용으로 설정
    //   document.getElementById('clockDisplay').textContent = this.realTime;
    //   // 1초마다 updateClockDisplay 함수를 호출하여 시계를 업데이트
    //   // setInterval(this.updateClockDisplay(), 1000)를 setInterval(this.updateClockDisplay, 1000)로 수정
    //   // this.updateClockDisplay()를 호출하는 대신에 함수 자체를 setInterval에 전달해야 합니다.
    //   // this.updateClockDisplay()를 호출할 때, 함수를 호출하는 대신에 호출된 결과를 setInterval에 전달하게 됩니다. 이로 인해 updateClockDisplay()가 무한히 재귀적으로 호출되며, 호출 스택 크기를 초과
    //   setInterval(this.updateClockDisplay, 1000);
    // },
    // background() {
    //   const sky = this.nowSky;
    // },

    // 하늘 상태에 따른 아이콘 불러오기
    getIcon(sky) {
      return this.weatherIcons[sky];
    },

    // 위경도 좌표를 xy좌표로 변환하는 메서드
    dfsXyConv(v1, v2) {
      const { PI, tan, log, cos, pow, floor, sin } = Math
      // LCC DFS 좌표변환을 위한 기초 자료
      const RE = 6371.00877 // 지구 반경(km)
      const GRID = 5.0 // 격자 간격(km)
      const SLAT1 = 30.0 // 투영 위도1(degree)
      const SLAT2 = 60.0 // 투영 위도2(degree)
      const OLON = 126.0 // 기준점 경도(degree)
      const OLAT = 38.0 // 기준점 위도(degree)
      const XO = 43 // 기준점 X좌표(GRID)
      const YO = 136 // 기1준점 Y좌표(GRID)

      const DEGRAD = PI / 180.0
      //const RADDEG = 180.0 / PI

      const re = RE / GRID
      const slat1 = SLAT1 * DEGRAD
      const slat2 = SLAT2 * DEGRAD
      const olon = OLON * DEGRAD
      const olat = OLAT * DEGRAD

      let sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
      sn = log(cos(slat1) / cos(slat2)) / log(sn)
      let sf = tan(PI * 0.25 + slat1 * 0.5)
      sf = pow(sf, sn) * cos(slat1) / sn
      let ro = tan(PI * 0.25 + olat * 0.5)
      ro = re * sf / pow(ro, sn)
      const rs = {}
      let ra, theta

      // 위경도 -> x, y좌표 
      rs.lat = v1
      rs.lon = v2
      ra = tan(PI * 0.25 + (v1) * DEGRAD * 0.5)
      ra = re * sf / pow(ra, sn)
      theta = v2 * DEGRAD - olon
      if (theta > PI) theta -= 2.0 * PI
      if (theta < -PI) theta += 2.0 * PI
      theta *= sn
      rs.x = floor(ra * sin(theta) + XO + 0.5)
      rs.y = floor(ro - ra * cos(theta) + YO + 0.5)

      this.x = rs.x
      this.y = rs.y
    },

    // 날짜 및 시간 구하는 메서드
    getDate() {
      const d = new Date(); // 현재 날짜 및 시간 구하는 메서드
      // 오늘이 20230630일 때 내일이 20230631 이 되지 않도록 내일 날짜를 따로 지정 
      let tom = new Date(d.getFullYear(), d.getMonth(), d.getDate() + 1); // 내일
      let yes = new Date(d.getFullYear(), d.getMonth(), d.getDate() - 1); // 어제
      // console.log("내일: " + tom)
      // console.log("어제: " + yes)
      // console.log("내일 날짜: " + tom.getDate())

      // 오늘이 20231231 이거나 20240101 일 경우를 생각해서 year도 따로 담음
      const to_year = d.getFullYear();
      const yes_year = yes.getFullYear();
      const tom_year = tom.getFullYear();

      //getMonth(), getDate(), getHours(), getMinutes(), getSeconds() 함수는 수치 값을 반환하기 때문에 2자리를 맞추기 위해서는 "0"을 붙여서 뒤에서 2자리만 잘라서 값을 변환해야 한다.
      const to_month = ('0' + (d.getMonth() + 1)).slice(-2); //getMonth()함수는 0~11을 반환하기 때문에 항상 +1을 해줘야한다
      const yes_month = ('0' + (yes.getMonth() + 1)).slice(-2);
      const tomo_month = ('0' + (tom.getMonth() + 1)).slice(-2);

      const to_day = ('0' + d.getDate()).slice(-2);
      const yes_day = ('0' + yes.getDate()).slice(-2);
      const tomo_day = ('0' + tom.getDate()).slice(-2);

      const hours = ('0' + d.getHours()).slice(-2);

      this.today = to_year + to_month + to_day;
      this.yesterday = yes_year + yes_month + yes_day;
      this.tomorrow = tom_year + tomo_month + tomo_day;
      this.nowTime = hours + '00';
      console.log("오늘: " + this.today)
      console.log("어제: " + this.yesterday)
      console.log("내일: " + this.tomorrow)
    },

    // 시간별 날씨 구하는 메서드 
    weather() {
      const self = this;
      const apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst";
      const serviceKey = "Xs3HgrDkm6MWCtXs7NMgjrav%2By3qQRPpajwpJi45ktD6E8oGeF5HSc7WXJK3%2Fx43dBH1j7qRCv66dIkTP1JzyA%3D%3D";
      const pageNo = "1";
      const numOfRows = "580"; // 오늘 0000 ~ 내일 2300 
      const dataType = "JSON";
      const base_date = self.yesterday;
      const base_time = "2300";
      const nx = self.x;
      const ny = self.y;

      self.$axios.get(apiUrl + "?serviceKey=" + serviceKey + "&pageNo=" + pageNo + "&numOfRows=" + numOfRows + "&dataType=" + dataType + "&base_date=" + base_date + "&base_time=" + base_time + "&nx=" + nx + "&ny=" + ny)
        .then(res => {
          //console.log(res.data);
          if (res.data) {
            const weatherList = res.data.response.body.items.item; // Object 타입임
            if (weatherList != null) {
              let fcstTime = '';
              let eachTime = null; // 하루치 정보값을 담을 dto 
              //let wList = JSON.stringify(weatherList); // Object타입은 for문 안돼서 String 타입으로 바꿔주기
              //console.log(wList)
              for (const element of weatherList) { // 하루치 전체 배열 길이만큼 반복하며 객체 하나씩 꺼내기 
                const fcstDate = element.fcstDate;
                const temp = element.fcstTime; // 현재 객체의 예보 시간 값 담기
                if (fcstTime != temp) { // fcstTime 중복이 아닐경우 (다른 시간대일 경우)
                  if (eachTime !== null) { // 빈 dto(첫 dto) 거르기.
                    // push: 배열의 끝에 하나 또는 그 이상의 엘리먼트를 추가하고 배열의 변경된 길이(length)를 return
                    this.everyTime.push(eachTime); // dto에 새로운 dto를 할당하기 전에 완성된 dto를 list에 add한다.
                  }
                  eachTime = { fcstDate: '', fcstTime: '', sky: 0, pty: 0, pop: '', tmp: 0 }; // dto를 새로 만들어준다.
                  eachTime.fcstDate = fcstDate;
                  fcstTime = temp;
                  eachTime.fcstTime = fcstTime;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    eachTime.fcstTime = 'now';
                  }
                }
                const category = element.category;
                const fcstValue = element.fcstValue;

                if (category == 'TMP') {
                  eachTime.tmp = fcstValue + '°';
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowTmp = fcstValue;
                    sessionStorage.setItem('nowTmp', fcstValue)
                  }
                } else if (category == 'SKY') {
                  eachTime.sky = fcstValue;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowSky = fcstValue;
                  }
                } else if (category == 'PTY' && fcstValue != 0) {
                  eachTime.pty = +fcstValue + 4;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowPty = +fcstValue + 4;
                  }
                } else if (category == 'POP') {
                  let pop = fcstValue + '%';
                  eachTime.pop = pop;
                  if (this.today == fcstDate && this.nowTime == temp) {
                    this.nowPop = pop;
                  }
                }

                // 0600은 tmn, 1500은 tmx가 추가로 들어있음.
                if (temp == '0600') {
                  if (this.today == fcstDate) {
                    this.todayTmn = fcstValue;
                  }
                }
                if (temp == '1500') {
                  if (this.today == fcstDate) {
                    this.todayTmx = fcstValue;
                  }
                }

              }
              this.everyTime.push(eachTime); // 마지막 2300 dto 담기

              // filter(): 새로운 배열을 반환하며, 조건을 충족하는 요소만 남김.
              // 오늘 0000 ~ 내일 2300 담겨있는 배열에서 
              // now ~ 내일 now 까지 필터링해서 담기 
              this.everyTime = this.everyTime.filter(eachTime => {
                if (eachTime.fcstTime === 'now') {
                  return true;
                } else if (eachTime.fcstDate === this.today && eachTime.fcstTime > this.nowTime) { // 오늘 + now 이후시간
                  return true;
                } else if (eachTime.fcstDate != this.today && eachTime.fcstTime <= this.nowTime) { // 내일 + now 이전시간
                  return true;
                }
                return false;
              });


              // 날씨 배열에 끼워넣을 일출일몰 객체 
              const todaySunriseObject = { fcstDate: this.today, fcstTime: this.today_sunrise, sky: 200, pty: '', pop: '', tmp: 'Sunrise' };
              const todaySunsetObject = { fcstDate: this.today, fcstTime: this.today_sunset, sky: 100, pty: '', pop: '', tmp: 'Sunset' };
              const tomorrowSunriseObject = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunrise, sky: 200, pty: '', pop: '', tmp: 'Sunrise' };
              const tomorrowSunsetObject = { fcstDate: this.tomorrow, fcstTime: this.tomorrow_sunset, sky: 100, pty: '', pop: '', tmp: 'Sunset' };

              // 조건별로 일출일몰 객체 다르게 끼워넣기~
              // (1) now = 0000 ~ 일출
              // (2) now = 일출 ~ 일몰
              // (3) now = 일몰 ~ 0000
              if (this.nowTime.substring(0, 2) <= this.today_sunrise.substring(0, 2)) {
                // (1) 막 하루가 지났을 때 => now(0000) ..(밤).. 오늘 일출 ... 오늘 일몰 ..(밤).. (0000)
                // findIndex(): 조건에 맞는 첫 번째 요소의 인덱스 값을 반환. 없으면 -1.
                let todaySR = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) === parseInt(this.today_sunrise.substring(0, 2)));
                let todaySS = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) === parseInt(this.today_sunset.substring(0, 2)));
                // 일출, 일몰 기준으로 배열 자르기~~
                let first = this.everyTime.slice(0, todaySR + 1);
                let second = this.everyTime.slice(todaySR + 1, todaySS + 1);
                let third = this.everyTime.slice(todaySS + 1, this.everyTime.length);

                for (const eachTime of first) { // now ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                for (const eachTime of third) { // 일몰 ~ (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }

                // now 아이콘 밤으로 바꾸기
                this.nowSky = +this.nowSky + 8;
                if (this.nowPty != 0) {
                  this.nowPty = +this.nowPty + 8;
                }

                // 일출일몰 껴서 배열 다시 생성~
                first.push(todaySunriseObject); // 오늘 일출 추가
                second.push(todaySunsetObject); // 오늘 일몰 추가
                this.everyTime = first.concat(second, third);

              } else if (this.nowTime.substring(0, 2) <= this.today_sunset.substring(0, 2)) {
                // (2) 해는 떴지만 아직 지지 않았을 때 => now(0700) ... 오늘 일몰 ..(밤).. 내일 일출 ... (0700)
                let todaySS = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) > parseInt(this.today_sunset.substring(0, 2)));
                let tomorrowSR = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) === parseInt(this.tomorrow_sunrise.substring(0, 2)));
                console.log(todaySS)

                let first = this.everyTime.slice(0, todaySS);
                let second = this.everyTime.slice(todaySS, tomorrowSR + 1);
                let third = this.everyTime.slice(tomorrowSR + 1, this.everyTime.length);

                for (const eachTime of second) { // 일몰 ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }

                first.push(todaySunsetObject); // 오늘 일몰 추가
                second.push(tomorrowSunriseObject); // 내일 일출 추가
                this.everyTime = first.concat(second, third);

              } else {
                // (3) 해가 졌을 때 => now(2100) ..(밤).. 내일 일출 ... 내일 일몰 ..(밤).. (2100)
                let tomorrowSR = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) === parseInt(this.tomorrow_sunrise.substring(0, 2)));
                let tomorrowSS = this.everyTime.findIndex((eachTime) => parseInt(eachTime.fcstTime.substring(0, 2)) === parseInt(this.tomorrow_sunset.substring(0, 2)));

                let first = this.everyTime.slice(0, tomorrowSR + 1);
                let second = this.everyTime.slice(tomorrowSR + 1, tomorrowSS + 1);
                let third = this.everyTime.slice(tomorrowSS + 1, this.everyTime.length);

                for (const eachTime of first) { // now ~ 일출 (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }
                for (const eachTime of third) { // 일몰 ~ (밤)
                  eachTime.sky = +eachTime.sky + 8;
                  if (eachTime.pty != 0) {
                    eachTime.pty = +eachTime.pty + 8;
                  }
                }

                // now 아이콘 밤으로 바꾸기
                this.nowSky = +this.nowSky + 8;
                if (this.nowPty != 0) {
                  this.nowPty = +this.nowPty + 8;
                }

                first.push(tomorrowSunriseObject); // 내일 일출 추가
                second.push(tomorrowSunsetObject); // 내일 일몰 추가
                this.everyTime = first.concat(second, third);
              }

              // 하늘상태와 강수형태가 섞이지 않게 하기~
              for (const eachTime of this.everyTime) { // 날씨 리스트
                if (eachTime.pty != 0) { // 비가 내리면~
                  eachTime.sky = eachTime.pty; // sky에 값 몰아주기. pty는 안쓸변수임
                  eachTime.pty = '';
                } else { // 비 안오면 sky 제외한 변수값 초기화
                  eachTime.pty = '';
                  eachTime.pop = '';
                }
              }
              if (this.nowPty != 0) { // 현재
                this.nowSky = this.nowPty;
                this.nowPty = '';
              }

              // 시간 am pm 붙이기
              for (const eachTime of this.everyTime) {
                if (eachTime.fcstTime !== 'now') {
                  let a = parseInt(eachTime.fcstTime.slice(0, 2)); // ex. 1
                  let b = eachTime.fcstTime.slice(2, 4); // ex. 00
                  if (b === '00' && a !== 0) { // 1 ~ 23
                    if (a < 12) { // 1 ~ 11
                      eachTime.fcstTime = a + 'am'; // 10am
                    } else if (a > 12) { // 13 ~ 23
                      eachTime.fcstTime = (a - 12) + 'pm'; // 8pm
                    } else { // 12
                      eachTime.fcstTime = a + 'pm'
                    }
                  } else if (a === 0) { // 0000
                    eachTime.fcstTime = '12am'
                  } else if (b !== '00') { // 0515, 1956
                    if (a <= 12) { // 05
                      b = b + 'am'; // 15am
                    } else { // 19
                      a = a - 12; // 7
                      b = b + 'pm'; // 56pm
                    }
                    eachTime.fcstTime = a + ':' + b; // 5:15am, 7:56pm
                  }
                }
              }
            } else {
              console.log('에러코드:', res.status);
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },

    // 위경도 값에 따른 주소명 구하는 메서드 
    toAddress() {
      const REST_API_KEY = "KakaoAK cf647b246dec58429d3ed126a442d1e0"
      const self = this;
      self.$axios.get('https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=' + self.longitude + '&y=' + self.latitude, { headers: { 'Authorization': REST_API_KEY } })
        .then(res => {
          if (res.data) {
            const totalCount = res.data.meta.total_count; //총 문서 수
            if (totalCount > 0) {
              this.address = res.data.documents[0].region_2depth_name; //지역 2Depth, 구 단위
            } else {
              console.log('에러코드:', res.status);
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },

    // 일출 일몰 구하는 메서드
    sunTime(day) {
      const self = this;
      const apiUrl = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getLCRiseSetInfo";
      const longitude = self.longitude;
      const latitude = self.latitude;
      const locdate = day;
      //console.log("날짜: " + locdate)
      const dnYn = "Y";
      const serviceKey = "Xs3HgrDkm6MWCtXs7NMgjrav%2By3qQRPpajwpJi45ktD6E8oGeF5HSc7WXJK3%2Fx43dBH1j7qRCv66dIkTP1JzyA%3D%3D";

      self.$axios.get(apiUrl + "?longitude=" + longitude + "&latitude=" + latitude + "&locdate=" + locdate + "&dnYn=" + dnYn + "&serviceKey=" + serviceKey)
        .then(res => {
          if (res.data) {
            // xml 응답값을 json으로 변환
            const xmlData = res.data;
            const jsonData = JSON.stringify(xmlData); // xml -> json 형태의 String
            const objectData = JSON.parse(jsonData); // json 형태의 String -> object 타입의 json
            if (locdate === this.today) {
              this.today_sunrise = objectData.response.body.items.item.sunrise;
              this.today_sunset = objectData.response.body.items.item.sunset;
              // console.log("오늘일출: " + this.today_sunrise)
              // console.log("오늘일몰: " + this.today_sunset)
            } else if (locdate === this.tomorrow) {
              this.tomorrow_sunrise = objectData.response.body.items.item.sunrise;
              this.tomorrow_sunset = objectData.response.body.items.item.sunset;
              // console.log("내일일출: " + this.tomorrow_sunrise)
              // console.log("내일일몰: " + this.tomorrow_sunset)
            }
          } else {
            console.log('데이터 없음');
          }
        })
        .catch(err => {
          console.log(err);
          this.textContent = err.message;
        });
    },
    // 하늘 상태에 따라 다른 배경화면 적용 
    getBackground(sky) {
      const background = {
        1: 'dayClear',
        3: 'dayCloudy', 4: 'dayCloudy',
        5: 'dayRainy', 8: 'dayRainy',
        6: 'daySnowy', 7: 'daySnowy',
        9: 'nightClear',
        11: 'nightCloudy', 12: 'nightCloudy', 14: 'nightCloudy', 15: 'nightCloudy',
        13: 'nightRainy', 16: 'nightRainy'
      };
      const backdrop = {
        'dayClear': 'backdrop-dayClear',
        'dayCloudy': 'backdrop-dayCloudy',
        'dayRainy': 'backdrop-dayRainy',
        'daySnowy': 'backdrop-daySnowy',
        'nightClear': 'backdrop-nightClear',
        'nightCloudy': 'backdrop-nightCloudy',
        'nightRainy': 'backdrop-nightRainy'
      };
      return backdrop[background[sky]];
    }

  }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Staatliches&display=swap');

@font-face {
  font-family: 'PyeongChangPeace-Light';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChangPeace-Light.woff2') format('woff2');
  font-weight: 300;
  font-style: normal;
}

@font-face {
  font-family: 'PyeongChang-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChang-Regular.woff2') format('woff2');
  font-weight: 400;
  font-style: normal;
}

@font-face {
  font-family: 'PyeongChangPeace-Bold';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2206-02@1.0/PyeongChangPeace-Bold.woff2') format('woff2');
  font-weight: 700;
  font-style: normal;
}

.background {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  min-width: 335px;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(white, #4dc4ff);
}

/* 낮 맑음 */
.backdrop-dayClear {
  background-image: linear-gradient(white, #ffcc66, #4dc4ff);
}

/* 낮 흐림 */
.backdrop-dayCloudy {
  background-image: linear-gradient(white, #9fb1c5, #fbd997);
}

/* 낮 비 */
.backdrop-dayRainy {
  background-image: linear-gradient(white, #9fb1c5, #507774);
}

/* 낮 눈 */
.backdrop-daySnowy {
  background-image: linear-gradient(white, #b5c8cd, #89b0bf);
}

/* 밤 맑음 */
.backdrop-nightClear {
  background-image: linear-gradient(white, #0d1f5a, #004c5f);
  color: #fff;
}

/* 밤 흐림, 눈 */
.backdrop-nightCloudy {
  background-image: linear-gradient(white, #3a4159, #1e1f26);
  color: #fff;
}

/* 밤 비 */
.backdrop-nightRainy {
  background-image: linear-gradient(white, #28434e, #001813);
  color: #fff;
}

.allWeather {
  max-width: 900px;
  margin: 0 auto;
  margin-bottom: 180px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  background: transparent;
  background-color: rgba(255, 255, 255, 0.6);
  color: #2c3e50;
  border-radius: 10px;
  padding: 20px;
  /* 가로 스크롤 */
  overflow: auto;
  /* 자동 줄바꿈 없앰 */
  white-space: nowrap;
}

.allWeather::-webkit-scrollbar {
  /* display: none; */
  width: 95%;
  height: 7px;
}

/* 스크롤바 막대 설정 */
.allWeather::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0.8);
  /* 스크롤바 둥글게 설정    */
  border-radius: 20px;
}

/* 마우스 안올리면 스크롤바 안보이게 */
.allWeather::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0);
  transition: background-color 0.3s;
}

/* 마우스 올리면 스크롤바 보이게~ */
.allWeather:hover::-webkit-scrollbar-thumb {
  background-color: rgba(255, 255, 255, 0.8);
}

.inside {
  padding: 10px;
  display: inline-block;
}

.wInfo,
.wInfoOne,
.wInfoTwo {
  padding-left: 5px;
  padding-right: 5px;
}

.wInfo {
  padding-top: 10px;
  padding-bottom: 0px;
}

.wInfoOne {
  padding-top: 27px;
  padding-bottom: 7px;
}

.wInfoTwo {
  padding-top: 11px;
  padding-bottom: 7px;
}

.pop {
  font-size: 12px;
  color: blue;
}

.time {
  font-size: 14px;
}

.iconImg {
  width: 300px;
  height: auto;
  /* display: block;
  margin-left: auto;
  margin-right: auto; */
}

.tmp {
  font-size: 80px;
}

.tmx {
  font-size: 20px;
}


hr {
  margin-top: 100px;
  margin-left: auto;
  margin-right: auto;
  width: 500px;
  height: 5px;
  border: 0px;
  background: linear-gradient(to left, transparent, #fff, transparent);
}

h3 {
  font-family: 'PyeongChangPeace-Light';
  font-weight: 300;
  font-size: 27px;
  margin-top: 100px;
  margin-bottom: 50px;
  margin-left: auto;
  margin-right: auto;
  /* text-shadow: 1.5px 1.5px 1.5px white; */
}

h4 {
  font-family: 'PyeongChangPeace-Light';
  font-weight: 300;
  font-size: 20px;
  margin-top: 100px;
  margin-bottom: 20px;
  margin-left: auto;
  margin-right: auto;
  color: #2c3e50;
}

.address {
  font-family: 'PyeongChang-Regular';
  font-weight: 400;
  font-size: 15px;
  margin-bottom: 50px;
  margin-left: auto;
  margin-right: auto;
  color: #2c3e50;

}


table {
  margin-left: auto;
  margin-right: auto;
}

td {
  margin-left: 5px;
  margin-right: 5px
}



/* 네이버 쇼핑 아이콘 */

.naver-shopping img {
  position: fixed;
  right: 0;
  bottom: 0;
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