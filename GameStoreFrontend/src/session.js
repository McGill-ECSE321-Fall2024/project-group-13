import { reactive } from 'vue';

export const session = reactive({
  username: sessionStorage.getItem('loggedInUsername') || 'guest',
  permissionLevel: Number(sessionStorage.getItem('permissionLevel')) || 0,
  
  updateSession(username, permissionLevel) {
    this.username = username;
    this.permissionLevel = permissionLevel;
    sessionStorage.setItem('loggedInUsername', username);
    sessionStorage.setItem('permissionLevel', permissionLevel);
  },

    logout() {
        this.username = 'guest';
        this.permissionLevel = 0;
        sessionStorage.setItem('loggedInUsername', 'guest');
        sessionStorage.setItem('permissionLevel', 0);
    }
});

