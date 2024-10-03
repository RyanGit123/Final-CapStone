import { createStore as _createStore } from 'vuex';
import axios from 'axios';

export function createStore(currentToken, currentUser) {
  let store = _createStore({
    state: {
      token: currentToken || '',
      user: currentUser || {},
      properties: []
    },
    getters: {
      getProperties(state) {
        return state.properties;
      },
      getUserRole(state) {
        return state.user.role || '';
      }
    },
    mutations: {
      SET_AUTH_TOKEN(state, token) {
        state.token = token;
        localStorage.setItem('token', token);
        axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
      },
      SET_USER(state, user) {
        state.user = user;
        localStorage.setItem('user', JSON.stringify(user));
      },
      SET_PROPERTIES(state, properties) {
        state.properties = properties;
      },
      LOGOUT(state) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        state.token = '';
        state.user = {};
        axios.defaults.headers.common = {};
      }
    },
    actions: {
      async fetchProperties({ commit }) {
        try {
          const response = await axios.get('/properties');
          commit('SET_PROPERTIES', response.data);
        } catch (error) {
          console.error('Failed to fetch properties:', error);
        }
      }
    }
  });
  
  return store;
}
