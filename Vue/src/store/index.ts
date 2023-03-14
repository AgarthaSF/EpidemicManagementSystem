import { createStore } from 'vuex'

declare let SessionStorage: any;

const USER = "USER";


const store = createStore({
  state: {
    // first use session to get data, if there is no session, then init user with empty list
    user: SessionStorage.get(USER) || {}
  },
  mutations: {
    setUser(state, user){
      state.user = user;
      SessionStorage.set(USER,user);
    }
  },
  actions: {
  },
  modules: {
  },
  getters: {
    getUser: (state) => () => {
      return state.user;
    }
  },

})

export default store;