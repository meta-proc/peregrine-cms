<template>
  <div class="wrap">
    LOLOLOLOL
    <vue-multiselect
        v-model="value"
        label="icon"
        :options="model.children"
        track-by="target"
        :allow-empty="false"
        :show-labels="false"
        :custom-label="customLabel"
    >
      <template slot="singleLabel" slot-scope="props">
        <a title="screen-dropdown" class="btn-floating waves-effect waves-light">
          <i class="material-icons">{{props.option.icon}}</i>
        </a>
      </template>
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
    methods: {
      customLabel({icon}) {
        return icon;
      }
    }
  }

</script>

<style scoped>
</style>
