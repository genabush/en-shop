!function(e){var t={};function __webpack_require__(o){if(t[o])return t[o].exports;var r=t[o]={i:o,l:!1,exports:{}};return e[o].call(r.exports,r,r.exports,__webpack_require__),r.l=!0,r.exports}__webpack_require__.m=e,__webpack_require__.c=t,__webpack_require__.d=function(e,t,o){__webpack_require__.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:o})},__webpack_require__.r=function(e){"undefined"!=typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},__webpack_require__.t=function(e,t){if(1&t&&(e=__webpack_require__(e)),8&t)return e;if(4&t&&"object"==typeof e&&e&&e.__esModule)return e;var o=Object.create(null);if(__webpack_require__.r(o),Object.defineProperty(o,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var r in e)__webpack_require__.d(o,r,function(t){return e[t]}.bind(null,r));return o},__webpack_require__.n=function(e){var t=e&&e.__esModule?function getDefault(){return e.default}:function getModuleExports(){return e};return __webpack_require__.d(t,"a",t),t},__webpack_require__.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},__webpack_require__.p="",__webpack_require__(__webpack_require__.s=10)}([function(e,t,o){e.exports=o(2)(0)},function(e,t,o){e.exports=o(9)(1)},function(e,t){e.exports=vendor_chunk},function(e,t,o){var r={"./services/abAnalyticsService.js":4,"./templates.js":5};function webpackContext(e){var t=webpackContextResolve(e);return o(t)}function webpackContextResolve(e){var t=r[e];if(!(t+1)){var o=new Error("Cannot find module '"+e+"'");throw o.code="MODULE_NOT_FOUND",o}return t}webpackContext.keys=function webpackContextKeys(){return Object.keys(r)},webpackContext.resolve=webpackContextResolve,e.exports=webpackContext,webpackContext.id=3},function(e,t){angular.module("abAnalyticsServiceModule",[]).service("abAnalyticsService",["$q",function(e){this.getABAnalyticsForComponent=function(){return e.when({aValue:30,bValue:70})}}])},function(e,t){},function(e,t,o){var r={"./abAnalyticsToolbarItem/abAnalyticsToolbarItem.js":7,"./templates.js":8};function webpackContext(e){var t=webpackContextResolve(e);return o(t)}function webpackContextResolve(e){var t=r[e];if(!(t+1)){var o=new Error("Cannot find module '"+e+"'");throw o.code="MODULE_NOT_FOUND",o}return t}webpackContext.keys=function webpackContextKeys(){return Object.keys(r)},webpackContext.resolve=webpackContextResolve,e.exports=webpackContext,webpackContext.id=6},function(e,t){angular.module("abAnalyticsToolbarItemModule",["theBodyShopsmarteditContainerTemplates"]).component("abAnalyticsToolbarItem",{templateUrl:"abAnalyticsToolbarItemTemplate.html"})},function(e,t){angular.module("theBodyShopsmarteditContainerTemplates",[]).run(["$templateCache",function(e){"use strict";e.put("web/features/theBodyShopsmarteditContainer/abAnalyticsToolbarItem/abAnalyticsToolbarItemTemplate.html","<h2>AB Analytics</h2>\n<p>This is a dummy toolbar item used to demonstrate functionality.</p>\n"),e.put("web/features/theBodyShopsmarteditContainer/abAnalyticsToolbarItem/abAnalyticsToolbarItemWrapperTemplate.html","<ab-analytics-toolbar-item></ab-analytics-toolbar-item>")}])},function(e,t){e.exports=smarteditcommons},function(e,t,o){"use strict";o.r(t);var r=o(0);function importAll(e){e.keys().forEach(function(t){e(t)})}var n=o(1);o.d(t,"TheBodyShopsmarteditContainer",function(){return a}),function doImport(){importAll(o(3)),importAll(o(6))}();var a=function(){function TheBodyShopsmarteditContainer(){}return TheBodyShopsmarteditContainer=r.__decorate([Object(n.SeModule)({imports:["smarteditServicesModule","abAnalyticsToolbarItemModule"],initialize:["featureService",function(e){"ngInject";e.addToolbarItem({toolbarId:"smartEditPerspectiveToolbar",key:"abAnalyticsToolbarItem",type:"HYBRID_ACTION",nameI18nKey:"ab.analytics.toolbar.item.name",priority:2,section:"left",iconClassName:"icon-message-information se-toolbar-menu-ddlb--button__icon",include:"abAnalyticsToolbarItemWrapperTemplate.html"})}]})],TheBodyShopsmarteditContainer)}()}]);