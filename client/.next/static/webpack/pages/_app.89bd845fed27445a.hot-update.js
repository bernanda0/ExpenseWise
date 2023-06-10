"use strict";
/*
 * ATTENTION: An "eval-source-map" devtool has been used.
 * This devtool is neither made for production nor for readable output files.
 * It uses "eval()" calls to create a separate source file with attached SourceMaps in the browser devtools.
 * If you are trying to read the output file, select a different devtool (https://webpack.js.org/configuration/devtool/)
 * or disable the default devtool with "devtool: false".
 * If you are looking for production-ready output files, see mode: "production" (https://webpack.js.org/configuration/mode/).
 */
self["webpackHotUpdate_N_E"]("pages/_app",{

/***/ "./components/TopBar.js":
/*!******************************!*\
  !*** ./components/TopBar.js ***!
  \******************************/
/***/ (function(module, __webpack_exports__, __webpack_require__) {

eval(__webpack_require__.ts("__webpack_require__.r(__webpack_exports__);\n/* harmony export */ __webpack_require__.d(__webpack_exports__, {\n/* harmony export */   \"default\": function() { return /* binding */ TopBar; }\n/* harmony export */ });\n/* harmony import */ var react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! react/jsx-dev-runtime */ \"./node_modules/react/jsx-dev-runtime.js\");\n/* harmony import */ var react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0___default = /*#__PURE__*/__webpack_require__.n(react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__);\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! react */ \"./node_modules/react/index.js\");\n/* harmony import */ var react__WEBPACK_IMPORTED_MODULE_1___default = /*#__PURE__*/__webpack_require__.n(react__WEBPACK_IMPORTED_MODULE_1__);\n/* harmony import */ var _heroicons_react_24_solid__WEBPACK_IMPORTED_MODULE_4__ = __webpack_require__(/*! @heroicons/react/24/solid */ \"./node_modules/@heroicons/react/24/solid/esm/index.js\");\n/* harmony import */ var _headlessui_react__WEBPACK_IMPORTED_MODULE_5__ = __webpack_require__(/*! @headlessui/react */ \"./node_modules/@headlessui/react/dist/headlessui.esm.js\");\n/* harmony import */ var next_link__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! next/link */ \"./node_modules/next/link.js\");\n/* harmony import */ var next_link__WEBPACK_IMPORTED_MODULE_2___default = /*#__PURE__*/__webpack_require__.n(next_link__WEBPACK_IMPORTED_MODULE_2__);\n/* harmony import */ var next_router__WEBPACK_IMPORTED_MODULE_3__ = __webpack_require__(/*! next/router */ \"./node_modules/next/router.js\");\n/* harmony import */ var next_router__WEBPACK_IMPORTED_MODULE_3___default = /*#__PURE__*/__webpack_require__.n(next_router__WEBPACK_IMPORTED_MODULE_3__);\n\nvar _s = $RefreshSig$();\n\n\n\n\n\n\nfunction TopBar(param) {\n    let { showNav , setShowNav  } = param;\n    _s();\n    const router = (0,next_router__WEBPACK_IMPORTED_MODULE_3__.useRouter)();\n    const isLoginPage = router.pathname === \"/login\";\n    const isSignupPage = router.pathname === \"/signUp\";\n    if (isLoginPage || isSignupPage) {\n        return null; // Don't render the top bar in the login and signup pages\n    }\n    return /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"div\", {\n        className: \"fixed w-full h-16 flex justify-between items-center transition-all duration-[400ms] bg-blur \".concat(showNav ? \"pl-56\" : \"\"),\n        children: [\n            /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"div\", {\n                className: \"pl-4 md:pl-16\",\n                children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_heroicons_react_24_solid__WEBPACK_IMPORTED_MODULE_4__.Bars3CenterLeftIcon, {\n                    className: \"h-8 w-8 text-gray-700 cursor-pointer\",\n                    onClick: ()=>setShowNav(!showNav)\n                }, void 0, false, {\n                    fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                    lineNumber: 28,\n                    columnNumber: 17\n                }, this)\n            }, void 0, false, {\n                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                lineNumber: 27,\n                columnNumber: 13\n            }, this),\n            /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"div\", {\n                className: \"flex items-center pr-4 md:pr-16\",\n                children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Menu, {\n                    as: \"div\",\n                    className: \"relative inline-block text-left\",\n                    children: [\n                        /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"div\", {\n                            children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Menu.Button, {\n                                className: \"inline-flex w-full justify-center items-center\",\n                                children: [\n                                    /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"picture\", {\n                                        children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"img\", {\n                                            src: \"/daffa.png\",\n                                            className: \"rounded-full h-8 md:mr-4 border-2 border-white shadow-sm\",\n                                            alt: \"profile picture\"\n                                        }, void 0, false, {\n                                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                            lineNumber: 39,\n                                            columnNumber: 33\n                                        }, this)\n                                    }, void 0, false, {\n                                        fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                        lineNumber: 38,\n                                        columnNumber: 29\n                                    }, this),\n                                    /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"span\", {\n                                        className: \"hidden md:block font-medium text-gray-700\",\n                                        children: \"Daffa\"\n                                    }, void 0, false, {\n                                        fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                        lineNumber: 45,\n                                        columnNumber: 29\n                                    }, this),\n                                    /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_heroicons_react_24_solid__WEBPACK_IMPORTED_MODULE_4__.ChevronDownIcon, {\n                                        className: \"ml-2 h-4 w-4 text-gray-700\"\n                                    }, void 0, false, {\n                                        fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                        lineNumber: 48,\n                                        columnNumber: 29\n                                    }, this)\n                                ]\n                            }, void 0, true, {\n                                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                lineNumber: 37,\n                                columnNumber: 25\n                            }, this)\n                        }, void 0, false, {\n                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                            lineNumber: 36,\n                            columnNumber: 21\n                        }, this),\n                        /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Transition, {\n                            as: react__WEBPACK_IMPORTED_MODULE_1__.Fragment,\n                            enter: \"transition ease-out duration-100\",\n                            enterFrom: \"transform scale-95\",\n                            enterTo: \"transform scale-100\",\n                            leave: \"transition ease-in duration=75\",\n                            leaveFrom: \"transform scale-100\",\n                            leaveTo: \"transform scale-95\",\n                            children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Menu.Items, {\n                                className: \"absolute right-0 w-56 z-50 mt-2 origin-top-right bg-white rounded shadow-sm\",\n                                children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"div\", {\n                                    className: \"p-1\",\n                                    children: [\n                                        /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Menu.Item, {\n                                            children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)((next_link__WEBPACK_IMPORTED_MODULE_2___default()), {\n                                                href: \"#\",\n                                                passHref: true,\n                                                children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(\"a\", {\n                                                    className: \"flex hover:bg-orange-500 hover:text-white text-gray-700 rounded p-2 text-sm group transition-colors items-center\",\n                                                    children: [\n                                                        /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_heroicons_react_24_solid__WEBPACK_IMPORTED_MODULE_4__.PencilIcon, {\n                                                            className: \"h-4 w-4 mr-2\"\n                                                        }, void 0, false, {\n                                                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                                            lineNumber: 65,\n                                                            columnNumber: 7\n                                                        }, this),\n                                                        \"Edit\"\n                                                    ]\n                                                }, void 0, true, {\n                                                    fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                                    lineNumber: 64,\n                                                    columnNumber: 5\n                                                }, this)\n                                            }, void 0, false, {\n                                                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                                lineNumber: 63,\n                                                columnNumber: 3\n                                            }, this)\n                                        }, void 0, false, {\n                                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                            lineNumber: 62,\n                                            columnNumber: 29\n                                        }, this),\n                                        /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_headlessui_react__WEBPACK_IMPORTED_MODULE_5__.Menu.Item, {\n                                            children: /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)((next_link__WEBPACK_IMPORTED_MODULE_2___default()), {\n                                                href: \"#\",\n                                                className: \"flex hover:bg-orange-500 hover:text-white text-gray-700 rounded p-2 text-sm group transition-colors items-center\",\n                                                children: [\n                                                    /*#__PURE__*/ (0,react_jsx_dev_runtime__WEBPACK_IMPORTED_MODULE_0__.jsxDEV)(_heroicons_react_24_solid__WEBPACK_IMPORTED_MODULE_4__.Cog8ToothIcon, {\n                                                        className: \"h-4 w-4 mr-2\"\n                                                    }, void 0, false, {\n                                                        fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                                        lineNumber: 76,\n                                                        columnNumber: 41\n                                                    }, this),\n                                                    \"Settings\"\n                                                ]\n                                            }, void 0, true, {\n                                                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                                lineNumber: 72,\n                                                columnNumber: 37\n                                            }, this)\n                                        }, void 0, false, {\n                                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                            lineNumber: 71,\n                                            columnNumber: 33\n                                        }, this)\n                                    ]\n                                }, void 0, true, {\n                                    fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                    lineNumber: 61,\n                                    columnNumber: 29\n                                }, this)\n                            }, void 0, false, {\n                                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                                lineNumber: 60,\n                                columnNumber: 25\n                            }, this)\n                        }, void 0, false, {\n                            fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                            lineNumber: 51,\n                            columnNumber: 21\n                        }, this)\n                    ]\n                }, void 0, true, {\n                    fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                    lineNumber: 35,\n                    columnNumber: 17\n                }, this)\n            }, void 0, false, {\n                fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n                lineNumber: 33,\n                columnNumber: 13\n            }, this)\n        ]\n    }, void 0, true, {\n        fileName: \"C:\\\\Users\\\\USER\\\\OneDrive\\\\OneDrive - UNIVERSITAS INDONESIA\\\\A file Kuliah\\\\matkul\\\\Semester 4\\\\proyek\\\\ExpenseWise\\\\client\\\\components\\\\TopBar.js\",\n        lineNumber: 23,\n        columnNumber: 9\n    }, this);\n}\n_s(TopBar, \"fN7XvhJ+p5oE6+Xlo0NJmXpxjC8=\", false, function() {\n    return [\n        next_router__WEBPACK_IMPORTED_MODULE_3__.useRouter\n    ];\n});\n_c = TopBar;\nvar _c;\n$RefreshReg$(_c, \"TopBar\");\n\n\n;\n    // Wrapped in an IIFE to avoid polluting the global scope\n    ;\n    (function () {\n        var _a, _b;\n        // Legacy CSS implementations will `eval` browser code in a Node.js context\n        // to extract CSS. For backwards compatibility, we need to check we're in a\n        // browser context before continuing.\n        if (typeof self !== 'undefined' &&\n            // AMP / No-JS mode does not inject these helpers:\n            '$RefreshHelpers$' in self) {\n            // @ts-ignore __webpack_module__ is global\n            var currentExports = module.exports;\n            // @ts-ignore __webpack_module__ is global\n            var prevExports = (_b = (_a = module.hot.data) === null || _a === void 0 ? void 0 : _a.prevExports) !== null && _b !== void 0 ? _b : null;\n            // This cannot happen in MainTemplate because the exports mismatch between\n            // templating and execution.\n            self.$RefreshHelpers$.registerExportsForReactRefresh(currentExports, module.id);\n            // A module can be accepted automatically based on its exports, e.g. when\n            // it is a Refresh Boundary.\n            if (self.$RefreshHelpers$.isReactRefreshBoundary(currentExports)) {\n                // Save the previous exports on update so we can compare the boundary\n                // signatures.\n                module.hot.dispose(function (data) {\n                    data.prevExports = currentExports;\n                });\n                // Unconditionally accept an update to this module, we'll check if it's\n                // still a Refresh Boundary later.\n                // @ts-ignore importMeta is replaced in the loader\n                module.hot.accept();\n                // This field is set when the previous version of this module was a\n                // Refresh Boundary, letting us know we need to check for invalidation or\n                // enqueue an update.\n                if (prevExports !== null) {\n                    // A boundary can become ineligible if its exports are incompatible\n                    // with the previous exports.\n                    //\n                    // For example, if you add/remove/change exports, we'll want to\n                    // re-execute the importing modules, and force those components to\n                    // re-render. Similarly, if you convert a class component to a\n                    // function, we want to invalidate the boundary.\n                    if (self.$RefreshHelpers$.shouldInvalidateReactRefreshBoundary(prevExports, currentExports)) {\n                        module.hot.invalidate();\n                    }\n                    else {\n                        self.$RefreshHelpers$.scheduleUpdate();\n                    }\n                }\n            }\n            else {\n                // Since we just executed the code for the module, it's possible that the\n                // new exports made it ineligible for being a boundary.\n                // We only care about the case when we were _previously_ a boundary,\n                // because we already accepted this update (accidental side effect).\n                var isNoLongerABoundary = prevExports !== null;\n                if (isNoLongerABoundary) {\n                    module.hot.invalidate();\n                }\n            }\n        }\n    })();\n//# sourceURL=[module]\n//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiLi9jb21wb25lbnRzL1RvcEJhci5qcy5qcyIsIm1hcHBpbmdzIjoiOzs7Ozs7Ozs7Ozs7Ozs7O0FBQWlDO0FBT0U7QUFDK0I7QUFDSjtBQUNqQztBQUNXO0FBRXpCLFNBQVNhLE9BQU8sS0FBdUI7UUFBdkIsRUFBRUMsUUFBTyxFQUFFQyxXQUFVLEVBQUUsR0FBdkI7O0lBQzNCLE1BQU1DLFNBQVNKLHNEQUFTQTtJQUN4QixNQUFNSyxjQUFjRCxPQUFPRSxhQUFhO0lBQ3hDLE1BQU1DLGVBQWVILE9BQU9FLGFBQWE7SUFFekMsSUFBSUQsZUFBZUUsY0FBYztRQUM3QixPQUFPLE1BQU0seURBQXlEO0lBQzFFO0lBQ0EscUJBQ0ksOERBQUNDO1FBQ0dDLFdBQVcsK0ZBQ04sT0FEcUdQLFVBQVUsVUFBVTs7MEJBRzlILDhEQUFDTTtnQkFBSUMsV0FBVTswQkFDWCw0RUFBQ3BCLDBFQUFtQkE7b0JBQ2hCb0IsV0FBVTtvQkFDVkMsU0FBUyxJQUFNUCxXQUFXLENBQUNEOzs7Ozs7Ozs7OzswQkFHbkMsOERBQUNNO2dCQUFJQyxXQUFVOzBCQUVYLDRFQUFDYixtREFBSUE7b0JBQUNlLElBQUc7b0JBQU1GLFdBQVU7O3NDQUNyQiw4REFBQ0Q7c0NBQ0csNEVBQUNaLDBEQUFXZ0I7Z0NBQUNILFdBQVU7O2tEQUNuQiw4REFBQ0k7a0RBQ0csNEVBQUNDOzRDQUNHQyxLQUFJOzRDQUNKTixXQUFVOzRDQUNWTyxLQUFJOzs7Ozs7Ozs7OztrREFHWiw4REFBQ0M7d0NBQUtSLFdBQVU7a0RBQTRDOzs7Ozs7a0RBRzVELDhEQUFDbEIsc0VBQWVBO3dDQUFDa0IsV0FBVTs7Ozs7Ozs7Ozs7Ozs7Ozs7c0NBR25DLDhEQUFDWix5REFBVUE7NEJBQ1BjLElBQUl2QiwyQ0FBUUE7NEJBQ1o4QixPQUFNOzRCQUNOQyxXQUFVOzRCQUNWQyxTQUFROzRCQUNSQyxPQUFNOzRCQUNOQyxXQUFVOzRCQUNWQyxTQUFRO3NDQUVSLDRFQUFDM0IseURBQVU0QjtnQ0FBQ2YsV0FBVTswQ0FDbEIsNEVBQUNEO29DQUFJQyxXQUFVOztzREFDZiw4REFBQ2Isd0RBQVM2QjtzREFDcEMsNEVBQUMxQixrREFBSUE7Z0RBQUMyQixNQUFLO2dEQUFJQyxRQUFROzBEQUNyQiw0RUFBQ0M7b0RBQUVuQixXQUFVOztzRUFDWCw4REFBQ25CLGlFQUFVQTs0REFBQ21CLFdBQVU7Ozs7Ozt3REFBaUI7Ozs7Ozs7Ozs7Ozs7Ozs7O3NEQU1iLDhEQUFDYix3REFBUzZCO3NEQUNOLDRFQUFDMUIsa0RBQUlBO2dEQUNEMkIsTUFBSztnREFDTGpCLFdBQVU7O2tFQUVWLDhEQUFDaEIsb0VBQWFBO3dEQUFDZ0IsV0FBVTs7Ozs7O29EQUFpQjs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7Ozs7O0FBV2xGO0dBekV3QlI7O1FBQ0xELGtEQUFTQTs7O0tBREpDIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vX05fRS8uL2NvbXBvbmVudHMvVG9wQmFyLmpzPzYyZTIiXSwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IHsgRnJhZ21lbnQgfSBmcm9tIFwicmVhY3RcIjtcclxuaW1wb3J0IHtcclxuICAgIEJhcnMzQ2VudGVyTGVmdEljb24sXHJcbiAgICBQZW5jaWxJY29uLFxyXG4gICAgQ2hldnJvbkRvd25JY29uLFxyXG4gICAgQ3JlZGl0Q2FyZEljb24sXHJcbiAgICBDb2c4VG9vdGhJY29uLFxyXG59IGZyb20gXCJAaGVyb2ljb25zL3JlYWN0LzI0L3NvbGlkXCI7XHJcbmltcG9ydCB7IEJlbGxJY29uLCBDaGVja0ljb24gfSBmcm9tIFwiQGhlcm9pY29ucy9yZWFjdC8yNC9vdXRsaW5lXCI7XHJcbmltcG9ydCB7IE1lbnUsIFRyYW5zaXRpb24sIFBvcG92ZXIgfSBmcm9tIFwiQGhlYWRsZXNzdWkvcmVhY3RcIjtcclxuaW1wb3J0IExpbmsgZnJvbSBcIm5leHQvbGlua1wiO1xyXG5pbXBvcnQgeyB1c2VSb3V0ZXIgfSBmcm9tIFwibmV4dC9yb3V0ZXJcIjtcclxuXHJcbmV4cG9ydCBkZWZhdWx0IGZ1bmN0aW9uIFRvcEJhcih7IHNob3dOYXYsIHNldFNob3dOYXYgfSkge1xyXG4gICAgY29uc3Qgcm91dGVyID0gdXNlUm91dGVyKCk7XHJcbiAgICBjb25zdCBpc0xvZ2luUGFnZSA9IHJvdXRlci5wYXRobmFtZSA9PT0gXCIvbG9naW5cIjtcclxuICAgIGNvbnN0IGlzU2lnbnVwUGFnZSA9IHJvdXRlci5wYXRobmFtZSA9PT0gXCIvc2lnblVwXCI7XHJcblxyXG4gICAgaWYgKGlzTG9naW5QYWdlIHx8IGlzU2lnbnVwUGFnZSkge1xyXG4gICAgICAgIHJldHVybiBudWxsOyAvLyBEb24ndCByZW5kZXIgdGhlIHRvcCBiYXIgaW4gdGhlIGxvZ2luIGFuZCBzaWdudXAgcGFnZXNcclxuICAgIH1cclxuICAgIHJldHVybiAoXHJcbiAgICAgICAgPGRpdlxyXG4gICAgICAgICAgICBjbGFzc05hbWU9e2BmaXhlZCB3LWZ1bGwgaC0xNiBmbGV4IGp1c3RpZnktYmV0d2VlbiBpdGVtcy1jZW50ZXIgdHJhbnNpdGlvbi1hbGwgZHVyYXRpb24tWzQwMG1zXSBiZy1ibHVyICR7c2hvd05hdiA/IFwicGwtNTZcIiA6IFwiXCJcclxuICAgICAgICAgICAgICAgIH1gfVxyXG4gICAgICAgID5cclxuICAgICAgICAgICAgPGRpdiBjbGFzc05hbWU9XCJwbC00IG1kOnBsLTE2XCI+XHJcbiAgICAgICAgICAgICAgICA8QmFyczNDZW50ZXJMZWZ0SWNvblxyXG4gICAgICAgICAgICAgICAgICAgIGNsYXNzTmFtZT1cImgtOCB3LTggdGV4dC1ncmF5LTcwMCBjdXJzb3ItcG9pbnRlclwiXHJcbiAgICAgICAgICAgICAgICAgICAgb25DbGljaz17KCkgPT4gc2V0U2hvd05hdighc2hvd05hdil9XHJcbiAgICAgICAgICAgICAgICAvPlxyXG4gICAgICAgICAgICA8L2Rpdj5cclxuICAgICAgICAgICAgPGRpdiBjbGFzc05hbWU9XCJmbGV4IGl0ZW1zLWNlbnRlciBwci00IG1kOnByLTE2XCI+XHJcblxyXG4gICAgICAgICAgICAgICAgPE1lbnUgYXM9XCJkaXZcIiBjbGFzc05hbWU9XCJyZWxhdGl2ZSBpbmxpbmUtYmxvY2sgdGV4dC1sZWZ0XCI+XHJcbiAgICAgICAgICAgICAgICAgICAgPGRpdj5cclxuICAgICAgICAgICAgICAgICAgICAgICAgPE1lbnUuQnV0dG9uIGNsYXNzTmFtZT1cImlubGluZS1mbGV4IHctZnVsbCBqdXN0aWZ5LWNlbnRlciBpdGVtcy1jZW50ZXJcIj5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxwaWN0dXJlPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxpbWdcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgc3JjPVwiL2RhZmZhLnBuZ1wiXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIGNsYXNzTmFtZT1cInJvdW5kZWQtZnVsbCBoLTggbWQ6bXItNCBib3JkZXItMiBib3JkZXItd2hpdGUgc2hhZG93LXNtXCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgYWx0PVwicHJvZmlsZSBwaWN0dXJlXCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAvPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9waWN0dXJlPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPHNwYW4gY2xhc3NOYW1lPVwiaGlkZGVuIG1kOmJsb2NrIGZvbnQtbWVkaXVtIHRleHQtZ3JheS03MDBcIj5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBEYWZmYVxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPC9zcGFuPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPENoZXZyb25Eb3duSWNvbiBjbGFzc05hbWU9XCJtbC0yIGgtNCB3LTQgdGV4dC1ncmF5LTcwMFwiIC8+XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIDwvTWVudS5CdXR0b24+XHJcbiAgICAgICAgICAgICAgICAgICAgPC9kaXY+XHJcbiAgICAgICAgICAgICAgICAgICAgPFRyYW5zaXRpb25cclxuICAgICAgICAgICAgICAgICAgICAgICAgYXM9e0ZyYWdtZW50fVxyXG4gICAgICAgICAgICAgICAgICAgICAgICBlbnRlcj1cInRyYW5zaXRpb24gZWFzZS1vdXQgZHVyYXRpb24tMTAwXCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgZW50ZXJGcm9tPVwidHJhbnNmb3JtIHNjYWxlLTk1XCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgZW50ZXJUbz1cInRyYW5zZm9ybSBzY2FsZS0xMDBcIlxyXG4gICAgICAgICAgICAgICAgICAgICAgICBsZWF2ZT1cInRyYW5zaXRpb24gZWFzZS1pbiBkdXJhdGlvbj03NVwiXHJcbiAgICAgICAgICAgICAgICAgICAgICAgIGxlYXZlRnJvbT1cInRyYW5zZm9ybSBzY2FsZS0xMDBcIlxyXG4gICAgICAgICAgICAgICAgICAgICAgICBsZWF2ZVRvPVwidHJhbnNmb3JtIHNjYWxlLTk1XCJcclxuICAgICAgICAgICAgICAgICAgICA+XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIDxNZW51Lkl0ZW1zIGNsYXNzTmFtZT1cImFic29sdXRlIHJpZ2h0LTAgdy01NiB6LTUwIG10LTIgb3JpZ2luLXRvcC1yaWdodCBiZy13aGl0ZSByb3VuZGVkIHNoYWRvdy1zbVwiPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgPGRpdiBjbGFzc05hbWU9XCJwLTFcIj5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxNZW51Lkl0ZW0+XHJcbiAgPExpbmsgaHJlZj1cIiNcIiBwYXNzSHJlZj5cclxuICAgIDxhIGNsYXNzTmFtZT1cImZsZXggaG92ZXI6Ymctb3JhbmdlLTUwMCBob3Zlcjp0ZXh0LXdoaXRlIHRleHQtZ3JheS03MDAgcm91bmRlZCBwLTIgdGV4dC1zbSBncm91cCB0cmFuc2l0aW9uLWNvbG9ycyBpdGVtcy1jZW50ZXJcIj5cclxuICAgICAgPFBlbmNpbEljb24gY2xhc3NOYW1lPVwiaC00IHctNCBtci0yXCIgLz5cclxuICAgICAgRWRpdFxyXG4gICAgPC9hPlxyXG4gIDwvTGluaz5cclxuPC9NZW51Lkl0ZW0+XHJcblxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxNZW51Lkl0ZW0+XHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDxMaW5rXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBocmVmPVwiI1wiXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICBjbGFzc05hbWU9XCJmbGV4IGhvdmVyOmJnLW9yYW5nZS01MDAgaG92ZXI6dGV4dC13aGl0ZSB0ZXh0LWdyYXktNzAwIHJvdW5kZWQgcC0yIHRleHQtc20gZ3JvdXAgdHJhbnNpdGlvbi1jb2xvcnMgaXRlbXMtY2VudGVyXCJcclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPlxyXG4gICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgPENvZzhUb290aEljb24gY2xhc3NOYW1lPVwiaC00IHctNCBtci0yXCIgLz5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIFNldHRpbmdzXHJcbiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvTGluaz5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICA8L01lbnUuSXRlbT5cclxuICAgICAgICAgICAgICAgICAgICAgICAgICAgIDwvZGl2PlxyXG4gICAgICAgICAgICAgICAgICAgICAgICA8L01lbnUuSXRlbXM+XHJcbiAgICAgICAgICAgICAgICAgICAgPC9UcmFuc2l0aW9uPlxyXG4gICAgICAgICAgICAgICAgPC9NZW51PlxyXG4gICAgICAgICAgICA8L2Rpdj5cclxuICAgICAgICA8L2Rpdj5cclxuICAgICk7XHJcbn0iXSwibmFtZXMiOlsiRnJhZ21lbnQiLCJCYXJzM0NlbnRlckxlZnRJY29uIiwiUGVuY2lsSWNvbiIsIkNoZXZyb25Eb3duSWNvbiIsIkNyZWRpdENhcmRJY29uIiwiQ29nOFRvb3RoSWNvbiIsIkJlbGxJY29uIiwiQ2hlY2tJY29uIiwiTWVudSIsIlRyYW5zaXRpb24iLCJQb3BvdmVyIiwiTGluayIsInVzZVJvdXRlciIsIlRvcEJhciIsInNob3dOYXYiLCJzZXRTaG93TmF2Iiwicm91dGVyIiwiaXNMb2dpblBhZ2UiLCJwYXRobmFtZSIsImlzU2lnbnVwUGFnZSIsImRpdiIsImNsYXNzTmFtZSIsIm9uQ2xpY2siLCJhcyIsIkJ1dHRvbiIsInBpY3R1cmUiLCJpbWciLCJzcmMiLCJhbHQiLCJzcGFuIiwiZW50ZXIiLCJlbnRlckZyb20iLCJlbnRlclRvIiwibGVhdmUiLCJsZWF2ZUZyb20iLCJsZWF2ZVRvIiwiSXRlbXMiLCJJdGVtIiwiaHJlZiIsInBhc3NIcmVmIiwiYSJdLCJzb3VyY2VSb290IjoiIn0=\n//# sourceURL=webpack-internal:///./components/TopBar.js\n"));

/***/ })

});