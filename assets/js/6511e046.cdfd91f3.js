"use strict";(self.webpackChunkwebdoc=self.webpackChunkwebdoc||[]).push([[1575],{4456:(n,t,i)=>{i.r(t),i.d(t,{assets:()=>r,contentTitle:()=>l,default:()=>p,frontMatter:()=>o,metadata:()=>e,toc:()=>d});const e=JSON.parse('{"id":"startpoint-getting-started/installation-guide","title":"Installation Guide","description":"StartPoint is a composable and extensible framework designed to simplify application development by providing a structured way to manage navigation, configuration, and plugin integration. The core module acts as the foundation for building scalable and customizable applications.","source":"@site/docs/startpoint-getting-started/installation-guide.md","sourceDirName":"startpoint-getting-started","slug":"/startpoint-getting-started/installation-guide","permalink":"/startpoint-android/docs/startpoint-getting-started/installation-guide","draft":false,"unlisted":false,"editUrl":"https://github.com/lavinou/startpoint-android/tree/main/docs/docs/startpoint-getting-started/installation-guide.md","tags":[],"version":"current","sidebarPosition":1,"frontMatter":{"sidebar_position":1,"title":"Installation Guide"},"sidebar":"tutorialSidebar","previous":{"title":"Getting Started","permalink":"/startpoint-android/docs/category/getting-started"},"next":{"title":"Plugins","permalink":"/startpoint-android/docs/category/plugins"}}');var a=i(4848),s=i(8453);const o={sidebar_position:1,title:"Installation Guide"},l=void 0,r={},d=[{value:"Installation",id:"installation",level:2},{value:"Step 1: Add JitPack repository",id:"step-1-add-jitpack-repository",level:3},{value:"Step 2: Add StartPoint BOM",id:"step-2-add-startpoint-bom",level:3},{value:"Step 3: Add Core Module",id:"step-3-add-core-module",level:3},{value:"Basic Setup",id:"basic-setup",level:2},{value:"Example",id:"example",level:3},{value:"Key Components",id:"key-components",level:3},{value:"Installing Plugins",id:"installing-plugins",level:2},{value:"Accessing Plugins",id:"accessing-plugins",level:2},{value:"Navigation",id:"navigation",level:2},{value:"Summary",id:"summary",level:2}];function c(n){const t={a:"a",code:"code",h2:"h2",h3:"h3",img:"img",li:"li",p:"p",pre:"pre",strong:"strong",ul:"ul",...(0,s.R)(),...n.components};return(0,a.jsxs)(a.Fragment,{children:[(0,a.jsx)(t.p,{children:"StartPoint is a composable and extensible framework designed to simplify application development by providing a structured way to manage navigation, configuration, and plugin integration. The core module acts as the foundation for building scalable and customizable applications."}),"\n",(0,a.jsx)(t.h2,{id:"installation",children:"Installation"}),"\n",(0,a.jsxs)(t.p,{children:["StartPoint is published on JitPack as a BOM (Bill of Materials). To install, add the following to your ",(0,a.jsx)(t.code,{children:"build.gradle"})," or ",(0,a.jsx)(t.code,{children:"build.gradle.kts"}),":"]}),"\n",(0,a.jsx)(t.h3,{id:"step-1-add-jitpack-repository",children:"Step 1: Add JitPack repository"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-gradle",children:"repositories {\n    maven { url 'https://jitpack.io' }\n}\n"})}),"\n",(0,a.jsx)(t.h3,{id:"step-2-add-startpoint-bom",children:"Step 2: Add StartPoint BOM"}),"\n",(0,a.jsx)(t.p,{children:(0,a.jsx)(t.a,{href:"https://jitpack.io/#lavinou/startpoint-android",children:(0,a.jsx)(t.img,{src:"https://jitpack.io/v/lavinou/startpoint-android.svg",alt:""})})}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-gradle",children:'implementation(platform("com.github.lavinou.startpoint-android:bom:<latest-version>"))\n'})}),"\n",(0,a.jsx)(t.h3,{id:"step-3-add-core-module",children:"Step 3: Add Core Module"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-gradle",children:'dependencies {\n    ...\n    implementation(platform("com.github.lavinou.startpoint-android:bom:<latest-version>"))\n    // highlight-next-line\n    implementation("com.github.lavinou.startpoint-android:core")\n    ...\n}\n\n'})}),"\n",(0,a.jsx)(t.h2,{id:"basic-setup",children:"Basic Setup"}),"\n",(0,a.jsxs)(t.p,{children:["Once the core module is installed, you can initialize StartPoint by using the ",(0,a.jsx)(t.code,{children:"rememberStartPoint"})," composable. This function creates and configures a ",(0,a.jsx)(t.code,{children:"StartPoint"})," instance that persists across recompositions."]}),"\n",(0,a.jsx)(t.h3,{id:"example",children:"Example"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",children:"\nclass MainActivity : ComponentActivity() {\n    override fun onCreate(savedInstanceState: Bundle?) {\n        super.onCreate(savedInstanceState)\n\n        setContent {\n            val navHostController = rememberNavController()\n\n            // highlight-start\n            val startPoint = rememberStartPoint {\n                install(Plugin) {\n                    myCustomPluginSettings = // custom settings\n                }\n            }\n            // highlight-end\n\n            StartPointAppTheme {\n                // highlight-next-line\n                StartPointScaffold(startPoint = startPoint) {\n                    AppNavHost(\n                        startpoint = startPoint,\n                        navHostController = navHostController,\n                        modifier = Modifier.fillMaxWidth()\n                            .padding(8.dp)\n                    )\n                }\n            }\n        }\n    }\n}\n"})}),"\n",(0,a.jsx)(t.h3,{id:"key-components",children:"Key Components"}),"\n",(0,a.jsxs)(t.ul,{children:["\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"StartPoint"}),": The main entry point that manages the application's configuration, navigation, and plugins."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"StartPointConfiguration"}),": Defines the settings and plugins that can be applied to extend the application's functionality."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"StartPointPlugin"}),": Provides a mechanism to install and manage additional features within StartPoint."]}),"\n",(0,a.jsxs)(t.li,{children:[(0,a.jsx)(t.strong,{children:"StartPointScaffold"}),": A composable scaffold that integrates the StartPoint instance with the app's UI structure."]}),"\n"]}),"\n",(0,a.jsx)(t.h2,{id:"installing-plugins",children:"Installing Plugins"}),"\n",(0,a.jsx)(t.p,{children:"To extend StartPoint's functionality, you can install plugins directly during configuration:"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",children:"val startPoint = rememberStartPoint {\n    install(MyCustomPlugin) {\n        // Custom configuration for the plugin\n    }\n}\n"})}),"\n",(0,a.jsx)(t.h2,{id:"accessing-plugins",children:"Accessing Plugins"}),"\n",(0,a.jsx)(t.p,{children:"Once installed, plugins can be retrieved or checked using the following functions:"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",children:"val pluginInstance = startPoint.pluginOrNull(MyCustomPlugin)\n"})}),"\n",(0,a.jsx)(t.p,{children:"Or, to force retrieval (throws if not installed):"}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",children:"val pluginInstance = startPoint.plugin(MyCustomPlugin)\n"})}),"\n",(0,a.jsx)(t.h2,{id:"navigation",children:"Navigation"}),"\n",(0,a.jsxs)(t.p,{children:["StartPoint manages navigation through ",(0,a.jsx)(t.code,{children:"NavHostController"}),". Plugins can contribute to the app's navigation graph:"]}),"\n",(0,a.jsx)(t.pre,{children:(0,a.jsx)(t.code,{className:"language-kotlin",children:'startPoint.navigation.navigate("destination")\n'})}),"\n",(0,a.jsx)(t.p,{children:"Ensure that each plugin defines its own navigation graph within its implementation."}),"\n",(0,a.jsx)(t.h2,{id:"summary",children:"Summary"}),"\n",(0,a.jsx)(t.p,{children:"StartPoint's core module provides a lightweight yet powerful way to manage application state, navigation, and extensibility through plugins. By leveraging composable functions and DSL configurations, it offers a seamless experience for building scalable applications."})]})}function p(n={}){const{wrapper:t}={...(0,s.R)(),...n.components};return t?(0,a.jsx)(t,{...n,children:(0,a.jsx)(c,{...n})}):c(n)}},8453:(n,t,i)=>{i.d(t,{R:()=>o,x:()=>l});var e=i(6540);const a={},s=e.createContext(a);function o(n){const t=e.useContext(s);return e.useMemo((function(){return"function"==typeof n?n(t):{...t,...n}}),[t,n])}function l(n){let t;return t=n.disableParentContext?"function"==typeof n.components?n.components(a):n.components||a:o(n.components),e.createElement(s.Provider,{value:t},n.children)}}}]);