var mediaWikiLoadStart=(new Date()).getTime(),mwPerformance=(window.performance&&performance.mark)?performance:{mark:function(){}};mwPerformance.mark('mwLoadStart');function isCompatible(str){var ua=str||navigator.userAgent;return!!('querySelector'in document&&'localStorage'in window&&'addEventListener'in window&&!(ua.match(/webOS\/1\.[0-4]/)||ua.match(/PlayStation/i)||ua.match(/SymbianOS|Series60|NetFront|Opera Mini|S40OviBrowser|MeeGo/)||(ua.match(/Glass/)&&ua.match(/Android/))));}(function(){var NORLQ,script;if(!isCompatible()){document.documentElement.className=document.documentElement.className.replace(/(^|\s)client-js(\s|$)/,'$1client-nojs$2');NORLQ=window.NORLQ||[];while(NORLQ.length){NORLQ.shift()();}window.NORLQ={push:function(fn){fn();}};window.RLQ={push:function(){}};return;}function startUp(){mw.config=new mw.Map(true);mw.loader.addSource({"local":"/wiki/static/load.php"});mw.loader.register([["site","D4Phm7CZ"],["noscript","gRxnPtLr",[],"noscript"],["filepage","9Hb193EL"],
["user.groups","lWArb7Z9",[],"user"],["user","lSSYlWfO",[],"user"],["user.cssprefs","GqV9IPpY",[],"private"],["user.defaults","1UW3VzL1"],["user.options","C9rS/VRT",[6],"private"],["user.tokens","qF2HLxl+",[],"private"],["mediawiki.language.data","bHbxm6Jh",[174]],["mediawiki.skinning.elements","hwwML3PC"],["mediawiki.skinning.content","CLGoSxYv"],["mediawiki.skinning.interface","6Zn8yXHO"],["mediawiki.skinning.content.parsoid","isWjEJSu"],["mediawiki.skinning.content.externallinks","oCMSera5"],["jquery.accessKeyLabel","cS79sOgb",[25,130]],["jquery.appear","sysNdLOh"],["jquery.arrowSteps","xOEEGaYq"],["jquery.async","0ohNMB+A"],["jquery.autoEllipsis","JjYOnvEO",[37]],["jquery.badge","hr5j4vLs",[171]],["jquery.byteLength","P1tNRJom"],["jquery.byteLimit","ebIJoj6U",[21]],["jquery.checkboxShiftClick","hOdsWSoZ"],["jquery.chosen","7Yhow+3m"],["jquery.client","KwuWPZ64"],["jquery.color","o1twtHUr",[27]],["jquery.colorUtil","2qH9YstO"],["jquery.confirmable","CzHpcx7Y",[175]],["jquery.cookie"
,"D/BrgC+W"],["jquery.expandableField","tBXToSCG"],["jquery.farbtastic","XdyG/9Df",[27]],["jquery.footHovzer","PhbJrgFK"],["jquery.form","t24XG77y"],["jquery.fullscreen","EpKoo7JG"],["jquery.getAttrs","NFgwa4yu"],["jquery.hidpi","pmJfL924"],["jquery.highlightText","Li4U8d6P",[242,130]],["jquery.hoverIntent","w2BVecoJ"],["jquery.i18n","aBpdhAaO",[173]],["jquery.localize","QQOyWryv"],["jquery.makeCollapsible","faCoo56d"],["jquery.mockjax","Mshx+bIi"],["jquery.mw-jump","fcHBHiRB"],["jquery.mwExtension","Iv6qKpmD"],["jquery.placeholder","gUbmZz/U"],["jquery.qunit","LdQhRQRo"],["jquery.qunit.completenessTest","jWG/IDmJ",[46]],["jquery.spinner","xigBs8IK"],["jquery.jStorage","SAjjMygO",[92]],["jquery.suggestions","OA55+qeV",[37]],["jquery.tabIndex","BN1f+Saj"],["jquery.tablesorter","lEpNxQ+u",[242,130,176]],["jquery.textSelection","ik0DqzWO",[25]],["jquery.throttle-debounce","0Pd//m0W"],["jquery.xmldom","itlRTvcE"],["jquery.tipsy","keNLmGl/"],["jquery.ui.core","oR0tu3DT",[58],"jquery.ui"],[
"jquery.ui.core.styles","JTTzz2Nn",[],"jquery.ui"],["jquery.ui.accordion","Nqa03HHA",[57,77],"jquery.ui"],["jquery.ui.autocomplete","mgZkYC2A",[66],"jquery.ui"],["jquery.ui.button","if2QUp36",[57,77],"jquery.ui"],["jquery.ui.datepicker","LvaaYnHZ",[57],"jquery.ui"],["jquery.ui.dialog","Uu1gQlDK",[61,64,68,70],"jquery.ui"],["jquery.ui.draggable","2TN9iGYo",[57,67],"jquery.ui"],["jquery.ui.droppable","HO2gswmt",[64],"jquery.ui"],["jquery.ui.menu","/1NKRmWx",[57,68,77],"jquery.ui"],["jquery.ui.mouse","sAAOvxgw",[77],"jquery.ui"],["jquery.ui.position","gr6PiQ6N",[],"jquery.ui"],["jquery.ui.progressbar","VrCHJbjP",[57,77],"jquery.ui"],["jquery.ui.resizable","fWVBouXh",[57,67],"jquery.ui"],["jquery.ui.selectable","KzXFCZOD",[57,67],"jquery.ui"],["jquery.ui.slider","extFTe36",[57,67],"jquery.ui"],["jquery.ui.sortable","1+ZHZm5w",[57,67],"jquery.ui"],["jquery.ui.spinner","1D6Oci8y",[61],"jquery.ui"],["jquery.ui.tabs","TJqrjOlR",[57,77],"jquery.ui"],["jquery.ui.tooltip","m1Ftl8lh",[57,68,77],
"jquery.ui"],["jquery.ui.widget","qcZyfRFi",[],"jquery.ui"],["jquery.effects.core","b3VAe2oo",[],"jquery.ui"],["jquery.effects.blind","cylCFntc",[78],"jquery.ui"],["jquery.effects.bounce","VywkziHG",[78],"jquery.ui"],["jquery.effects.clip","dVqO3pdb",[78],"jquery.ui"],["jquery.effects.drop","EfP4wP8R",[78],"jquery.ui"],["jquery.effects.explode","FXslkxG5",[78],"jquery.ui"],["jquery.effects.fade","OvHeC1ol",[78],"jquery.ui"],["jquery.effects.fold","ztIehKCW",[78],"jquery.ui"],["jquery.effects.highlight","D09QJns3",[78],"jquery.ui"],["jquery.effects.pulsate","+o9ccr4v",[78],"jquery.ui"],["jquery.effects.scale","55dkN0Aj",[78],"jquery.ui"],["jquery.effects.shake","ij9oViW8",[78],"jquery.ui"],["jquery.effects.slide","aq0Uog0i",[78],"jquery.ui"],["jquery.effects.transfer","25haGJD3",[78],"jquery.ui"],["json","ysURimbJ",[],null,null,"return!!(window.JSON\u0026\u0026JSON.stringify\u0026\u0026JSON.parse);"],["moment","LP28iX/t"],["mediawiki.apihelp","L48uWQF9"],["mediawiki.template","p59NkwlY"
],["mediawiki.template.mustache","GQzNcf3n",[95]],["mediawiki.template.regexp","3X5/tOta",[95]],["mediawiki.apipretty","HnQ587vB"],["mediawiki.api","MKI2YjXJ",[147,8]],["mediawiki.api.category","x1/zwc9N",[135,99]],["mediawiki.api.edit","43Xg5Uug",[135,99]],["mediawiki.api.login","72qigfA5",[99]],["mediawiki.api.options","jSEwcdN/",[99]],["mediawiki.api.parse","gayl1/yT",[99]],["mediawiki.api.upload","qjxH9yUf",[242,92,101]],["mediawiki.api.user","eTDIA5Bx",[99]],["mediawiki.api.watch","0+0EFpoD",[99]],["mediawiki.api.messages","+XB/F+yB",[99]],["mediawiki.content.json","sDS7a/0F"],["mediawiki.confirmCloseWindow","oQDGN+EM"],["mediawiki.debug","iRJt+lId",[32,56]],["mediawiki.debug.init","YaHzQShD",[111]],["mediawiki.feedback","rLlVFkvw",[135,126,250]],["mediawiki.feedlink","fMDv37qe"],["mediawiki.filewarning","ihhZaVvt",[245]],["mediawiki.ForeignApi","snUbl5N3",[117]],["mediawiki.ForeignApi.core","tax8tAo8",[99,243]],["mediawiki.helplink","iQnOQgZt"],["mediawiki.hidpi","y4Y71uOO",[36],
null,null,"return'srcset'in new Image();"],["mediawiki.hlist","OevtE4bj"],["mediawiki.htmlform","9V+XylCH",[22,130]],["mediawiki.htmlform.styles","Z4W3QOGG"],["mediawiki.htmlform.ooui.styles","UrSYfkGu"],["mediawiki.icon","MLc81dkm"],["mediawiki.inspect","XIjzWNqF",[21,92,130]],["mediawiki.messagePoster","uwLKlUJF",[116]],["mediawiki.messagePoster.wikitext","gC3U6zlc",[101,126]],["mediawiki.notification","MkS1BwsZ",[183]],["mediawiki.notify","MjkjojDI"],["mediawiki.RegExp","Juo1nF/X"],["mediawiki.pager.tablePager","MAUltZ+k"],["mediawiki.searchSuggest","rNGlzVbD",[35,45,50,99]],["mediawiki.sectionAnchor","jPhfFxUO"],["mediawiki.storage","m4mEePMO"],["mediawiki.Title","Q4Ce8VDj",[21,147]],["mediawiki.Upload","7w+sdMuS",[105]],["mediawiki.ForeignUpload","JYw7spBW",[116,136]],["mediawiki.ForeignStructuredUpload.config","JtmRymYi"],["mediawiki.ForeignStructuredUpload","wfJGVzc2",[138,137]],["mediawiki.Upload.Dialog","s4nbWyRH",[141]],["mediawiki.Upload.BookletLayout","Ngyn3H2o",[136,175,
145,240,93,250,256,257]],["mediawiki.ForeignStructuredUpload.BookletLayout","41Sv1bpt",[139,141,108,179,236,234]],["mediawiki.toc","2CsJ6LRD",[151]],["mediawiki.Uri","HZNtDIva",[147,97]],["mediawiki.user","hXvQR6vV",[106,151,7]],["mediawiki.userSuggest","FdPx10ve",[50,99]],["mediawiki.util","rRsy93yQ",[15,129]],["mediawiki.viewport","+c3QYVE1"],["mediawiki.checkboxtoggle","Az2MAdaz"],["mediawiki.checkboxtoggle.styles","LOa6L0ln"],["mediawiki.cookie","fe2PEgMB",[29]],["mediawiki.toolbar","73LZOfzm"],["mediawiki.experiments","xBM+2nLA"],["mediawiki.raggett","++r47HGf"],["mediawiki.action.edit","ZzmLZzeX",[22,53,156]],["mediawiki.action.edit.styles","nViJPwu+"],["mediawiki.action.edit.collapsibleFooter","3F4k4i9d",[41,151,124]],["mediawiki.action.edit.preview","dX8sezpI",[33,48,53,161,99,175]],["mediawiki.action.edit.stash","PUeFJFQ1",[35,99]],["mediawiki.action.history","1TpZld4X"],["mediawiki.action.history.diff","e+1uyZVJ"],["mediawiki.action.view.dblClickEdit","8QFUFA5i",[183,7]],[
"mediawiki.action.view.metadata","mRMxSJc5"],["mediawiki.action.view.categoryPage.styles","K+IuBpb0"],["mediawiki.action.view.postEdit","/P7QLyvk",[151,175,95]],["mediawiki.action.view.redirect","mocKvQV1",[25]],["mediawiki.action.view.redirectPage","kzVFOo5p"],["mediawiki.action.view.rightClickEdit","d+1GkTgy"],["mediawiki.action.edit.editWarning","q7gHe487",[53,110,175]],["mediawiki.action.view.filepage","q/qR3gTu"],["mediawiki.language","zxumoJA1",[172,9]],["mediawiki.cldr","JEJUGuWb",[173]],["mediawiki.libs.pluralruleparser","FLvcVdvR"],["mediawiki.language.init","vbyslBW8"],["mediawiki.jqueryMsg","3BFJOerX",[242,171,147,7]],["mediawiki.language.months","S07727Wh",[171]],["mediawiki.language.names","dVn8oVHH",[174]],["mediawiki.language.specialCharacters","v31HqVfX",[171]],["mediawiki.libs.jpegmeta","QVzRFP+Z"],["mediawiki.page.gallery","eVkPy9Ey",[54,181]],["mediawiki.page.gallery.styles","YcSEtgqM"],["mediawiki.page.ready","HmTbjERM",[15,23,41,43,45]],["mediawiki.page.startup",
"JIkV0T2K",[147]],["mediawiki.page.patrol.ajax","CMlKe18/",[48,135,99,183]],["mediawiki.page.watch.ajax","2Nc/cBmO",[107,183]],["mediawiki.page.image.pagination","W2MvayMG",[48,147]],["mediawiki.special","ndsXRcdP"],["mediawiki.special.apisandbox.styles","iPs+Wt6v"],["mediawiki.special.apisandbox","HKPaG0BZ",[99,175,187,235,244]],["mediawiki.special.block","2r+DzZiL",[147]],["mediawiki.special.blocklist","C2x1avjH"],["mediawiki.special.changeslist","uxMbqmqh"],["mediawiki.special.changeslist.legend","fqZ/EOYq"],["mediawiki.special.changeslist.legend.js","PjIljpUb",[41,151]],["mediawiki.special.changeslist.enhanced","Z1OC+hP7"],["mediawiki.special.changeslist.visitedstatus","ng3CNaLT"],["mediawiki.special.comparepages.styles","nsaMAN4t"],["mediawiki.special.edittags","bHG9HEX4",[24]],["mediawiki.special.edittags.styles","thnz+8/g"],["mediawiki.special.import","3H2LCaJp"],["mediawiki.special.movePage","zJq/7TcD",[232]],["mediawiki.special.movePage.styles","iShZ+eTm"],[
"mediawiki.special.pageLanguage","ip3h/npv",[245]],["mediawiki.special.pagesWithProp","7KzsJR/M"],["mediawiki.special.preferences","YgyQsFUw",[110,171,128]],["mediawiki.special.preferences.styles","SqueDc2P"],["mediawiki.special.recentchanges","ftobn36U",[187]],["mediawiki.special.search","MuwixByt",[238]],["mediawiki.special.undelete","6SffxzhK"],["mediawiki.special.upload","tR7dsdwA",[48,135,99,110,175,179,95]],["mediawiki.special.userlogin.common.styles","GbHtJMwr"],["mediawiki.special.userlogin.signup.styles","thipu6rF"],["mediawiki.special.userlogin.login.styles","UfBLUudV"],["mediawiki.special.userlogin.signup.js","oXlb4vb4",[54,99,175]],["mediawiki.special.unwatchedPages","aY/vC1Xm",[135,107]],["mediawiki.special.watchlist","EIubrP6k"],["mediawiki.special.version","r1OFmofv"],["mediawiki.legacy.config","sP3oHtzX"],["mediawiki.legacy.commonPrint","eiez6sZh"],["mediawiki.legacy.protect","tx75Ql27",[22]],["mediawiki.legacy.shared","UbCiDwuG"],["mediawiki.legacy.oldshared",
"3gIsk1Tf"],["mediawiki.legacy.wikibits","VKpB886s",[147]],["mediawiki.ui","cHfB7TqW"],["mediawiki.ui.checkbox","cTLSVl40"],["mediawiki.ui.radio","u8Dp80Fa"],["mediawiki.ui.anchor","UWLuIRK1"],["mediawiki.ui.button","U6xdcKRi"],["mediawiki.ui.input","EqcZDCyp"],["mediawiki.ui.icon","kKE++9Go"],["mediawiki.ui.text","5MTcHDBW"],["mediawiki.widgets","L0BtVMUm",[19,22,135,99,233,248]],["mediawiki.widgets.styles","8xg8vBey"],["mediawiki.widgets.DateInputWidget","iaBqvLRI",[93,248]],["mediawiki.widgets.datetime","y9xC+mue",[245]],["mediawiki.widgets.CategorySelector","Nt2rF+FO",[116,135,248]],["mediawiki.widgets.UserInputWidget","Ofi2ZLma",[248]],["mediawiki.widgets.SearchInputWidget","Ho190I2I",[132,232]],["mediawiki.widgets.SearchInputWidget.styles","BFHMwQY3"],["mediawiki.widgets.StashedFileWidget","bJfYVWYs",[245]],["es5-shim","k+dmsDyH",[],null,null,"return(function(){'use strict';return!this\u0026\u0026!!Function.prototype.bind;}());"],["dom-level2-shim","dl13UCTS",[],null,null,
"return!!window.Node;"],["oojs","+FNWGI48",[241,92]],["oojs-ui","d794tT71",[249,248,250]],["oojs-ui-core","2Y6Opzhz",[171,243,246]],["oojs-ui-core.styles","v7uYth+5",[251,252,253],null,null,"return!!jQuery('meta[name=\"X-OOUI-PHP\"]').length;"],["oojs-ui.styles","ERqn1zyQ",[251,252,253],null,null,"return!!jQuery('meta[name=\"X-OOUI-PHP\"]').length;"],["oojs-ui-widgets","Ijid4dZF",[245]],["oojs-ui-toolbars","S47jqEpj",[245]],["oojs-ui-windows","KjCoZgpo",[245]],["oojs-ui.styles.icons","n9sNOy4M"],["oojs-ui.styles.indicators","iaaiNimd"],["oojs-ui.styles.textures","IZ0Lt7ez"],["oojs-ui.styles.icons-accessibility","mr5dHmpw"],["oojs-ui.styles.icons-alerts","cM8A8G+D"],["oojs-ui.styles.icons-content","ZiQdhbTP"],["oojs-ui.styles.icons-editing-advanced","4+reJe7t"],["oojs-ui.styles.icons-editing-core","ttV2FUAj"],["oojs-ui.styles.icons-editing-list","htKhsqZK"],["oojs-ui.styles.icons-editing-styling","c1S0zA64"],["oojs-ui.styles.icons-interactions","6CIJqRm+"],["oojs-ui.styles.icons-layout"
,"J/PkQl9I"],["oojs-ui.styles.icons-location","rTqDdY8Z"],["oojs-ui.styles.icons-media","XT00ubZD"],["oojs-ui.styles.icons-moderation","BClmoIpG"],["oojs-ui.styles.icons-movement","7th35flB"],["oojs-ui.styles.icons-user","hgfg1/kL"],["oojs-ui.styles.icons-wikimedia","qXR2I8Pd"],["skins.vector.styles","zjql0PAn"],["skins.vector.styles.responsive","J76JpJbh"],["skins.vector.js","MjxNgnp5",[51,54]],["ext.pygments","MFAgEp8d"],["ext.geshi.visualEditor","y8YVhAy4",["ext.visualEditor.mwcore"]],["jquery.wikiEditor","2WDtY3SI",[53,171],"ext.wikiEditor"],["jquery.wikiEditor.dialogs","ZnZ/zB3w",[51,63,279],"ext.wikiEditor"],["jquery.wikiEditor.dialogs.config","jYqa21Qa",[50,275,142,140,95],"ext.wikiEditor"],["jquery.wikiEditor.preview","LIjVLaM8",[274,99],"ext.wikiEditor"],["jquery.wikiEditor.publish","CmbMauia",[275],"ext.wikiEditor"],["jquery.wikiEditor.toolbar","ENGUMcDV",[18,29,274,281],"ext.wikiEditor"],["jquery.wikiEditor.toolbar.config","vgEBVAnz",[279,178],"ext.wikiEditor"],[
"jquery.wikiEditor.toolbar.i18n","hdrRv6rM",[],"ext.wikiEditor"],["ext.wikiEditor","PDmB2Af5",[274,145],"ext.wikiEditor"],["ext.wikiEditor.dialogs","jNVu0h9J",[286,276],"ext.wikiEditor"],["ext.wikiEditor.preview","PVwe9Qtg",[282,277],"ext.wikiEditor"],["ext.wikiEditor.publish","AaNacPYc",[282,278],"ext.wikiEditor"],["ext.wikiEditor.toolbar","GAbzw7fq",[282,280],"ext.wikiEditor"],["ext.wikiEditor.toolbar.styles","1R2G92zc",[],"ext.wikiEditor"],["ext.nuke","UPn31Jz1"],["skins.minerva.base.reset","ZSgs3q9v"],["skins.minerva.base.styles","j30ABFxL"],["skins.minerva.beta.styles","PqYUWc2v"],["skins.minerva.content.styles","1usOICeQ"],["skins.minerva.content.styles.beta","NjtHhQVp"],["mobile.pagelist.styles","8L0gBxzO"],["mobile.pagesummary.styles","aZhbPjgG"],["skins.minerva.tablet.styles","goVjwYPA"],["skins.minerva.icons.images","EYNY9ewZ"],["skins.minerva.icons.beta.images","biQvSwQj"],["mobile.overlay.images","QwBg1Dhg"],["mobile.issues.images","Tq4QGiR7"],["mobile.toc.images",
"/ttJaH6H"],["mobile.references.images","iPCXG6Wp"],["mobile.toggle.images","JvLWP919"],["skins.minerva.icons.images.scripts","dOtGC9G2"],["skins.minerva.mainPage.beta.styles","PmZ5tC3h"],["skins.minerva.mainPage.styles","ECkrr8ba"],["skins.minerva.userpage.styles","hb+3gINz"],["mobile.modules","havADl/5",[243]],["mobile.oo","DpPFybF6",[308]],["mobile.view","5ppnFWXZ",[309]],["mobile.context","UXddhptI",[308]],["mobile.browser","al+VG17D",[310]],["mobile.mainMenu","Df2U9YgW",[312,382,317]],["mobile.messageBox","4JBhqJ9F",[310,317]],["mobile.modifiedBar","v+CZSc0n",[175,308]],["mobile.microAutoSize","F5YeE8SK"],["mediawiki.template.hogan","9NxSFFco",[95]],["mobile.pagelist","JF6a2UjP",[312,294,295,317]],["mobile.pagelist.scripts","t3T5b6nF",[318,348]],["mobile.watchlist","0qybIZ0Y",[357,319]],["mobile.toc","5QDc2/mC",[301,346]],["mobile.ajax","voZlwnxi"],["mobile.settings","rnJo+GmN",[29,134,308]],["mobile.backtotop","MYZo40ua",[346]],["mobile.startup","zZg7r7k5",[54,148,312,311,315,323
,327,317]],["mobile.foreignApi","daW0Mc83",[117,325]],["mobile.user","wsh7D8Ct",[134,145,308]],["mobile.abusefilter","EYWWQOpW",[341]],["mobile.editor.api","6TQ2kcOz",[325]],["mobile.editor.common","A97Kari6",[110,329,380,314,341,343,244]],["mobile.editor.overlay","uwpUkU7e",[128,328,330,316,258]],["mobile.editor.overlay.withtoolbar","OMvJ5swz",[53,331,333,260]],["mobile.editor.overlay.withtoolbar.images","ujVVWeth"],["mobile.search","ZVGgRTJ4",[383,341,319]],["mobile.search.api","3ourocgo",[135,325]],["mobile.talk.overlays","6HL96kqi",[227,330]],["mobile.mediaViewer","4RaW8UX3",[341,339]],["mobile.mediaViewer.beta","ZVqed7ae",[337,358]],["mobile.swipe.images","DsZZu3uk"],["mobile.categories.overlays","iXZDNdri",[330,334,335]],["mobile.overlays","hsVMdKmV",[322,299,325]],["mobile.drawers","khB0zhm2",[325]],["mobile.toast","a8Psnmbq",[342]],["mobile.references","Im+KBHhO",[342,329,345,302]],["mobile.references.gateway","/CkNOTBu",[309]],["mobile.toggle","fYyklkNm",[325,303]],[
"mobile.contentOverlays","xHwnNNXF",[341]],["mobile.watchstar","Pr46ddxg",[322,343]],["mobile.buttonWithSpinner","G+RlzkPP",[244]],["mobile.languages.structured","G7SyBMIn",[381,341]],["mobile.issues","OBoMzLSa",[300,341]],["mobile.nearby","TaUPnX2V",[326,314,319,360]],["mobile.gallery","FBqDnqhx",[357,343]],["mobile.commonsCategory","FGK0WgNe",[353]],["mobile.betaoptin","FlHI40XU",[153,325]],["mobile.fontchanger","bq+nqYMc",[342]],["mobile.infiniteScroll","HLnheVcw",[309]],["mobile.swipe","14y7MDEb",[309]],["mobile.patrol.ajax","aGnLHfTc",[135,343]],["mobile.special.nearby.styles","qnNyLwUP"],["mobile.special.userlogin.scripts","FgHhxHY9"],["mobile.special.nearby.scripts","4OVqt39N",[352]],["mobile.special.uploads.scripts","lq4SK2We",[353]],["mobile.special.mobilediff.scripts","d794tT71"],["skins.minerva.scripts","IFLQoEwr",[355,351,381,313,344,334]],["skins.minerva.scripts.top","rGqLF+c9",[313]],["skins.minerva.newusers","dm6E/MzJ",[347,368]],["skins.minerva.editor","12R3/rYV",[229,
380,304,371]],["skins.minerva.categories","7tPRzCgv",[341]],["skins.minerva.talk","ywPFfDEF",[135,304,365]],["skins.minerva.toggling","kvd2MYNa",[346,365]],["skins.minerva.watchstar","PVqcO0qF",[304,365]],["skins.minerva.beta.scripts","1LXqNaOE",[324,326,365]],["skins.minerva.tablet.scripts","tfGw8l0D",[321]],["ext.categoryTree","52LaQUsA"],["ext.categoryTree.css","prZ7PT97"],["ext.scribunto.errors","Is200BXU",[63]],["ext.scribunto.logs","UjiHEpy8"],["ext.scribunto.edit","eHlrbDQe",[48,99]],["mobile.loggingSchemas.edit","d794tT71"],["mobile.loggingSchemas.mobileWebLanguageSwitcher","d794tT71"],["mobile.loggingSchemas.mobileWebMainMenuClickTracking","d794tT71"],["mobile.loggingSchemas.mobileWebSearch","d794tT71"]]);;mw.config.set({"wgLoadScript":"/wiki/static/load.php","debug":!1,"skin":"vector","stylepath":"/wiki/static/skins","wgUrlProtocols":
"bitcoin\\:|ftp\\:\\/\\/|ftps\\:\\/\\/|geo\\:|git\\:\\/\\/|gopher\\:\\/\\/|http\\:\\/\\/|https\\:\\/\\/|irc\\:\\/\\/|ircs\\:\\/\\/|magnet\\:|mailto\\:|mms\\:\\/\\/|news\\:|nntp\\:\\/\\/|redis\\:\\/\\/|sftp\\:\\/\\/|sip\\:|sips\\:|sms\\:|ssh\\:\\/\\/|svn\\:\\/\\/|tel\\:|telnet\\:\\/\\/|urn\\:|worldwind\\:\\/\\/|xmpp\\:|\\/\\/","wgArticlePath":"/wiki/$1","wgScriptPath":"/wiki/static","wgScriptExtension":".php","wgScript":"/wiki/static/index.php","wgSearchType":null,"wgVariantArticlePath":!1,"wgActionPaths":{},"wgServer":"https://www.tradingview.com","wgServerName":"www.tradingview.com","wgUserLanguage":"en","wgContentLanguage":"en","wgTranslateNumerals":!0,"wgVersion":"1.27.1","wgEnableAPI":!0,"wgEnableWriteAPI":!0,"wgMainPageTitle":"Main Page","wgFormattedNamespaces":{"-2":"Media","-1":"Special","0":"","1":"Talk","2":"User","3":"User talk","4":"Tradingview Wiki","5":"Tradingview Wiki talk","6":"File","7":"File talk","8":"MediaWiki","9":"MediaWiki talk","10":"Template","11":
"Template talk","12":"Help","13":"Help talk","14":"Category","15":"Category talk","274":"Widget","275":"Widget talk","828":"Module","829":"Module talk"},"wgNamespaceIds":{"media":-2,"special":-1,"":0,"talk":1,"user":2,"user_talk":3,"tradingview_wiki":4,"tradingview_wiki_talk":5,"file":6,"file_talk":7,"mediawiki":8,"mediawiki_talk":9,"template":10,"template_talk":11,"help":12,"help_talk":13,"category":14,"category_talk":15,"widget":274,"widget_talk":275,"module":828,"module_talk":829,"image":6,"image_talk":7,"project":4,"project_talk":5},"wgContentNamespaces":[0],"wgSiteName":"TradingView Wiki","wgDBname":"tv_wiki","wgExtraSignatureNamespaces":[],"wgAvailableSkins":{"vector":"Vector","minerva":"Minerva","fallback":"Fallback","apioutput":"ApiOutput"},"wgExtensionAssetsPath":"/wiki/static/extensions","wgCookiePrefix":"tv_wiki","wgCookieDomain":"","wgCookiePath":"/","wgCookieExpiration":15552000,"wgResourceLoaderMaxQueryLength":2000,"wgCaseSensitiveNamespaces":[],"wgLegalTitleChars":
" %!\"$&'()*,\\-./0-9:;=?@A-Z\\\\\\^_`a-z~+\\u0080-\\uFFFF","wgResourceLoaderStorageVersion":1,"wgResourceLoaderStorageEnabled":!1,"wgResourceLoaderLegacyModules":[],"wgForeignUploadTargets":[],"wgEnableUploads":!0,"wgWikiEditorMagicWords":{"redirect":"#REDIRECT","img_right":"right","img_left":"left","img_none":"none","img_center":"center","img_thumbnail":"thumb","img_framed":"frame","img_frameless":"frameless"},"wgMFSearchAPIParams":{"ppprop":"displaytitle"},"wgMFQueryPropModules":["pageprops"],"wgMFSearchGenerator":{"name":"prefixsearch","prefix":"ps"},"wgMFNearbyEndpoint":"","wgMFThumbnailSizes":{"tiny":80,"small":150},"wgMFContentNamespace":0,"wgMFEditorOptions":{"anonymousEditing":!0,"skipPreview":!1},"wgMFLicense":{"msg":"mobile-frontend-copyright","link":"","plural":1},"wgMFSchemaEditSampleRate":0.0625,"wgMFSchemaMobileWebLanguageSwitcherSampleRate":{"beta":0,"stable":0},"wgMFExperiments":{"betaoptin":{"name":"betaoptin","enabled":!1,"buckets":{"control":0.97,"A":
0.03}}},"wgMFIgnoreEventLoggingBucketing":!1,"wgMFEnableJSConsoleRecruitment":!1,"wgMFPhotoUploadEndpoint":"","wgMFDeviceWidthTablet":"720px","wgMFCollapseSectionsByDefault":!0});var RLQ=window.RLQ||[];while(RLQ.length){RLQ.shift()();}window.RLQ={push:function(fn){fn();}};window.NORLQ={push:function(){}};}script=document.createElement('script');script.src="/wiki/static/load.php?debug=false&lang=en&modules=jquery%2Cmediawiki&only=scripts&skin=vector&version=UzyLr9C7";script.onload=script.onreadystatechange=function(){if(!script.readyState||/loaded|complete/.test(script.readyState)){script.onload=script.onreadystatechange=null;script=null;startUp();}};document.getElementsByTagName('head')[0].appendChild(script);}());