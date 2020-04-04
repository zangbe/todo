<template>
  <v-app>
    <v-app-bar
      app
      color="primary"
      dark
    >
    </v-app-bar>

    <v-content>
      <Todo/>
    </v-content>
  </v-app>
</template>

<script>
import Todo from './components/Todo';

export default {
  name: 'App',

  components: {
    Todo,
  },

};
</script>
