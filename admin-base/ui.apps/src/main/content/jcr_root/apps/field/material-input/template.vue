<template>
  <div class="wrapper">
    <div
      v-if="!schema.preview"
      class="input-field"
    >
      <input
        ref="input"
        :disabled="disabled"
        :id="getFieldID(schema)"
        :max="getMinMax('max')"
        :min="getMinMax('min')"
        :name="schema.inputName"
        :pattern="schema.pattern"
        :placeholder="schema.placeholder"
        :readonly="schema.readonly"
        @keydown="allowed()"
        @blur="minMax()"
        v-bind:style="{minHeight: `${1.1 * schema.rows + 2.2}em`, maxHeight: `${1.1 * schema.rows + 2.2}em`}"
        v-model="value"
        class="form-control materialize-input"
        type="text"
      />
      <p style="color: #607d8b">
        min: {{ getMinMax('min') != null ? getMinMax('min') : '---' }}, max: {{ getMinMax('max') != null ? getMinMax('max') : '---' }}
      </p>
    </div>
    <p v-else>
      {{ value }}
    </p>
  </div>
</template>

<script>
export default {
  mixins: [VueFormGenerator.abstractField],
  mounted() {
  },
  updated() {
  },
  methods: {
    getMinMax(s){
      switch(s){
      case "min":
        if( this.schema.min ){
          return parseInt(this.schema.min, 10);
        }
        if( this.schema.interval ){
          let interval = this.parseInterval(this.schema.interval);
          console.log("interval - min")
          return interval.min;
        }
        return null;
        break;
      case "max":
        if( this.schema.max ){
          return parseInt(this.schema.max, 10);
        }
        if( this.schema.interval ){
          let interval = this.parseInterval(this.schema.interval);
          console.log("interval - max")
          return interval.max;
        }
        return null;
        break;
      default:
        return 0;
      }
    },
    parseInterval(interval){
      let i = interval.split(",");
      if( i.length < 2 ) return null;
      let min = parseInt(i[0].substring(1), 10);
      let max = parseInt(i[1].substring(0, i[1].length-1), 10);
      let minBorder = (i[0].charAt(0) === "[") ? 0 : 1;
      let maxBorder = (i[1].charAt(i[1].length-1) === "]") ? 0 : 1;
      return {
        "min": min + minBorder,
        "max": max - maxBorder
      }
    },
    isNumber: function (evt) {
      evt = (evt) ? evt : window.event;
      let charCode = (evt.which) ? evt.which : evt.keyCode;
      if ((charCode > 31 && (charCode < 48 || charCode > 57) && (charCode < 96 || charCode > 105)) && charCode !== 46) {
        evt.preventDefault();
      } else {
        return true;
      }
    },
    isInteger: function (evt) {
      evt = (evt) ? evt : window.event;
      let charCode = (evt.which) ? evt.which : evt.keyCode;
      if (charCode > 31 && (charCode < 48 || charCode > 57) && (charCode < 96 || charCode > 105)) {
        evt.preventDefault();
      } else {
        return true;
      }
    },
    minMax(){
      let min = this.getMinMax('min');
      let max = this.getMinMax('max');
      let v = Number(this.value.replace(/[^0-9]/g, ""));
      this.value = v + "";
      if( (max != null) && (v > max) ) this.value = max;
      if( (min != null) && (v < min) ) this.value = min;
    },
    allowed() {
      switch (this.schema.allowed) {
      case 'number':
        return this.isNumber();
      case 'integer':
        return this.isInteger(window.event);
      case 'double':
        return this.isNumber();
      default:
        return () => {};
      }
    }
  }
};
</script>
