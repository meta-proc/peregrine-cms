<template>
  <span class="wrap material-icon-drop-down">
    <vue-multiselect
        v-model="value"
        :options="model.children"
        track-by="target"
        :allow-empty="false"
        :show-labels="false"
        :clear-on-select="false"
    >
      <template slot="singleLabel" slot-scope="props">
          <a title="screen-dropdown" class="btn-floating waves-effect waves-light">
            <i class="material-icons">{{props.option.icon}}</i>
          </a>
      </template>
      <template slot="option" slot-scope="props" @click.native="selectOption(props.option)">
        <component
            v-bind:is="props.option.component"
            v-bind:model="props.option">
        </component>
      </template>
    </vue-multiselect>
  </span>
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
      selectOption(option) {
        console.log('new OPTION', option);
        this.value = option;
      }
    }
  }

</script>

<style>
  .material-icon-drop-down .multiselect .multiselect__select {
    display: none;
  }

  .material-icon-drop-down .multiselect .multiselect__tags {
     min-height: unset;
     display: unset;
     padding: unset;
     border: unset;
     background: unset;
  }

  .material-icon-drop-down .multiselect .multiselect__tags a.btn-floating .material-icons {
    line-height: 40px;
    height: 100%;
  }
</style>
