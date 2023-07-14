import { createRouter, createWebHistory } from 'vue-router'

import MainPage from '@/components/MainPage.vue'
import weather from '@/components/weather/weather.vue'
import Join from '@/components/member/Join.vue'
import Login from '@/components/member/Login.vue'
import Mypage from '@/components/member/Mypage.vue'
import FindId from '@/components/member/FindId.vue'
import FindPwd from '@/components/member/FindPwd.vue'
import addForm from '@/components/community/addForm.vue'
import ListBoard from '@/components/community/ListBoard.vue'
import BookMark from '@/components/community/BookMark.vue'
import ReportList from '@/components/community/ReportList.vue'
import MyList from '@/components/community/MyList.vue'
import winnerList from '../components/battle/winnerList.vue'
import weekWinner from '../components/battle/weekWinner.vue'
import vote from '../components/battle/vote.vue'
import apply from '../components/battle/apply.vue'
import randomCandidate from '@/components/battle/manager/randomCandidates.vue'
import naverShoppingList from '@/components/naverShopping/shoppingList.vue'
import changeTheme from '@/components/battle/manager/changeTheme.vue'
import myPageCss from  '@/components/member/MyPageCss.vue'
import ClosetList from '@/components/closet/ClosetList.vue'
import ClosetlistByTag from '@/components/closet/ClosetlistByTag.vue'
import ClosetlistByTag2 from '@/components/closet/ClosetlistByTag2.vue'
import ClosetListByCloth from '@/components/closet/ClosetListByCloth.vue'
import ClosetListByCloth2 from '@/components/closet/ClosetListByCloth2.vue'
import OotwList from '@/components/ootw/OotwList.vue'
import OotwAdd from '@/components/ootw/OotwAdd.vue'
import OotwDetail from '@/components/ootw/OotwDetail.vue'

const routes = [
	
	{
    path: '/',
    name: 'MainPage',
    component: MainPage
  },
	{
    path: '/weather',
    name: 'weather',
    component: weather
  },
	{
    path:'/login',
    name:'Login',
    component: Login
  }
  ,
  {
    path:'/join',
    name:'Join',
    component: Join
  },
  {
    path:'/mypage',
    name:'Mypage',
    component: Mypage
    //component: () => import('../views/member/MemberView.vue')
  },
  {
    path: '/FindId',
    name: 'FindId',
    component: FindId
  },
  {
    path: '/FindPwd',
    name: '/FindPwd',
    component: FindPwd
  },
  {
    path:'/listboard',
    name:'ListBoard',
    component: ListBoard
  },
  {
    path:'/addform',
    name:'addForm',
    component: addForm
  },
  {
    path:'/bookmark',
    name:'BookMark',
    component: BookMark
  },
  {
    path:'/reportlist',
    name:'ReportList',
    component: ReportList
  },
  {
    path:'/mylist',
    name:'MyList',
    component: MyList
  },
  {
    path:'/winnerList',
    name:'winnerList',
    component: winnerList
  },
  {
    path:'/weekWinner',
    name:'weekWinner',
    component: weekWinner
  },
  {
    path:'/vote',
    name:'vote',
    component: vote
  },
  {
    path:'/apply',
    name:'apply',
    component: apply
  },
  {
    path : '/random',
    name : 'random',
    component : randomCandidate
  },
  {
    path : '/changeTheme',
    name : 'changeTheme',
    component : changeTheme
  },
  {
    path:'/naverShoppingList',
    name:'NaverShoppingList',
    component: naverShoppingList
  },
  {
    path:'/myPageCss',
    name:'myPageCss',
    component: myPageCss
  },
{
    path: '/closetlist',
    name: 'ClosetList',
    component: ClosetList
  },
  {
    path: '/closetlstbytag',
    name: 'ClosetListByTag',
    component: ClosetlistByTag
  },
  {
    path: '/closetlstbytag2',
    name: 'ClosetListByTag2',
    component: ClosetlistByTag2
  },
  {
    path: '/closetlstbyCloth',
    name: 'ClosetListByCloth',
    component: ClosetListByCloth
  },
  {
    path: '/closetlstbyCloth2',
    name: 'ClosetListByCloth2',
    component: ClosetListByCloth2
  },
  {
    path: '/ootwlist',
    name: 'OotwList',
    component: OotwList
  },
  {
    path: '/ootwadd',
    name: 'OotwAdd',
    component: OotwAdd
  },
  {
    path: '/ootwdetail',
    name: 'OotwDetail',
    component: OotwDetail
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router