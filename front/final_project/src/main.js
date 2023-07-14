import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import axios from 'axios'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { library } from '@fortawesome/fontawesome-svg-core';
import { faCamera } from '@fortawesome/free-solid-svg-icons';
import { far } from '@fortawesome/free-regular-svg-icons';

// Font Awesome 아이콘을 라이브러리에 추가합니다.
library.add(faCamera, far);


//createApp(App).use(store).use(router).mount('#app')
const app = createApp(App)//vue 객체 생성
app.use(store)// 전역 저장 라이브러리 사용
app.use(router)//라우터 사용설정 . url 별로 실행될 component등록
app.config.globalProperties.$axios = axios;
app.component('font-awesome-icon', FontAwesomeIcon);
app.mount('#app')



