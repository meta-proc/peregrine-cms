<template>
  <div class="wrap">
    <vue-multiselect
        v-model="value"
        :options="model.children"
        track-by="target"
        :multiple="false"
        :searchable="false"
        :taggable="false"
        :clear-on-select="false"
        :close-on-select="true"
        :allow-empty="false"
        :show-labels="false"
    >
      <template slot="option" slot-scope="props">
        <component v-bind:is="props.option.component" v-bind:model="props.option"></component>
      </template>
    </vue-multiselect>
  </div>
</template>
<script>
  export default {
    props: ['model'],
    data() {
      return {
        value: this.model.children[0]
      }
    },
    computed: {}
  }

</script>

<style scoped>
</style>
