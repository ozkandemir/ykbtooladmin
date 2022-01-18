/*
 * Copyright 2014-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/* global SBA */
import cachemanagement from './cachemanagement';
//import cachemanagementEndpoint from './cachemanagement-endpoint';

// tag::customization-ui-toplevel[]
SBA.use({
  install({viewRegistry, vueI18n}) {
    viewRegistry.addView({
      name: 'cachemanagement',  //<1>
      path: '/cachemanagement', //<2>
      component: cachemanagement, //<3>
      label: 'cachemanagement.label', //<4>
      order: 1000, //<5>
    });

    vueI18n.mergeLocaleMessage('en', { // <4>
      cachemanagement: {
          label : "Cache Management",
          clear : "Clear"
      }
    });

  }
});
// end::customization-ui-toplevel[]

// tag::customization-ui-endpoint[]
// SBA.use({
//   install({viewRegistry, vueI18n}) {
//     viewRegistry.addView({
//       name: 'instances/cachemanagement',
//       parent: 'instances', // <1>
//       path: 'cachemanagement',
//  //     component: cachemanagementEndpoint,
//       label: 'CacheManagement',
//       group: 'cachemanagement', // <2>
//       order: 1000,
//       isEnabled: ({instance}) => instance.hasEndpoint('cachemanagement') // <3>
//     });

//     vueI18n.mergeLocaleMessage('en', { // <4>
//       cachemanagement: {
//           label : "Cache Management",
//           clear : "Clear"
//       }
//     });
//   }
// });
// end::customization-ui-endpoint[]
