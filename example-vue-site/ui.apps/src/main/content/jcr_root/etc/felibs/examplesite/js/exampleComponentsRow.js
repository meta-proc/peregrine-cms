var cmpExampleComponentsRow = (function () {
'use strict';

var template = {render: function(){var _vm=this;var _h=_vm.$createElement;var _c=_vm._self._c||_h;return _c('div',{staticClass:"row"},[_vm._l((_vm.model.children),function(child){return [_c(child.component,{tag:"component",attrs:{"model":child}})]}),_c('pagerender-vue-components-placeholder',{attrs:{"model":{ path: _vm.model.path, component: _vm.model.component }}})],2)},staticRenderFns: [],
    props: ['model']
};

return template;

}());
