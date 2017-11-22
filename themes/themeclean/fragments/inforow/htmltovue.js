module.exports = {
    convert: function($, f) {
    	f.wrap($, 'themeclean-components-block')
        f.bindAttribute($.parent(),'model','model')
        f.addIf($.find('.perIsEditAndEmpty').first(), 'isEditAndEmpty')

        //Container
        f.bindAttribute( $, 'class', 'model.reverselayout', false)

        //Text
        f.addIf($.find('h2').first(), 'model.showtitle == \'true\'')
        f.mapRichField($.find('h2').first(), "model.title")
        f.addIf($.find('h4').first(), 'model.showsubtitle == \'true\'')
        f.mapRichField($.find('h4').first(), "model.subtitle")
        f.addIf($.find('div.col-md-auto').first(), 'model.showbutton == \'true\'')
        f.addFor($.find('div.col-md-auto>a').first(), 'model.buttons')

        //Buttons
        f.bindAttribute($.find('a.btn').first(), 'href', f.pathToUrl('item.buttonlink'))
        f.mapRichField($.find('a.btn').first(), "item.buttontext")
        f.addStyle($.find('a.btn').first(), 'backgroundColor', 'item.buttoncolor')
        f.addStyle($.find('a.btn').first(), 'borderColor', 'item.buttoncolor')
        f.addStyle($.find('a.btn').first(), 'margin-left', "i == 0 ? 0 : '0.5rem'")
        f.addStyle($.find('a.btn').first(), 'margin-right', "i == model.buttons.length-1 ? 0: '0.5rem'")

    }
}