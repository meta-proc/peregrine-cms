<template>
  <div class="wrap">
    <vue-multiselect
        v-model="modelFromValue"
        :options="model.children"
        track-by="path"
        :multiple="false"
        :searchable="false"
        label=""
        deselectLabel=""
        :taggable="false"
        :clear-on-select="true"
        :close-on-select="true"
        :placeholder="placeholder"
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
    computed: {
      modelFromValue: {
        get() {
          // will catch falsy, null or undefined
          if (this.value) {
            // if model is a string, convert to object with name and value
            if (typeof this.value === 'string') {
              return this.schema.values.filter(item => item.value === this.value)[0];
            } else {
              return this.value;
            }
          } else {
            return '';
          }
        },
        set(newValue) {
          if (newValue) {
            this.value = newValue[this.trackBy];
          } else {
            this.value = '';
          }
        }
      }
    }
  }

</script>

<style scoped>
</style>
