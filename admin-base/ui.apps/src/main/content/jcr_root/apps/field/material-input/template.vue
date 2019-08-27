<template>
	<div class="wrapper">
		<div v-if="!schema.preview" class="input-field">
			<input
				ref="input"
        type="text"
				class="form-control materialize-input"
				v-model="value"
				v-bind:style="{minHeight: `${1.1 * schema.rows + 2.2}em`, maxHeight: `${1.1 * schema.rows + 2.2}em`}"
				:id="getFieldID(schema)"
        :min="schema.min"
        :max="schema.max"
				:disabled="disabled"
        @keypress="allowed()"
        :pattern="schema.pattern"
				:placeholder="schema.placeholder"
				:readonly="schema.readonly"
				:name="schema.inputName">
			</input>
		</div>
		<p v-else>{{value}}</p>
	</div>
</template>

<script>
	export default {
		mixins: [ VueFormGenerator.abstractField ],
		mounted() {
		},
		updated() {
		},
    methods: {
		  isNumber: function(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if ((charCode > 31 && (charCode < 48 || charCode > 57)) && charCode !== 46) {
          evt.preventDefault();
        } else {
          return true;
        }
      },
		  isInteger: function(evt) {
        evt = (evt) ? evt : window.event;
        var charCode = (evt.which) ? evt.which : evt.keyCode;
        if (charCode > 31 && (charCode < 48 || charCode > 57)) {
          evt.preventDefault();
        } else {
          return true;
        }
      },
		  allowed(){
        switch(this.schema.allowed){
        case "number":
          return this.isNumber();
          break;
        case "integer":
          return this.isInteger();
          break;
        case "double":
          return this.isNumber();
          break;
        default:
          return () => {};
          break;
        }
      }
    }
	}
</script>
