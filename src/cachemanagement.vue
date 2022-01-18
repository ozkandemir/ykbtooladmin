<!--
  - Copyright 2014-2018 the original author or authors.
  -
  - Licensed under the Apache License, Version 2.0 (the "License");
  - you may not use this file except in compliance with the License.
  - You may obtain a copy of the License at
  -
  -     http://www.apache.org/licenses/LICENSE-2.0
  -
  - Unless required by applicable law or agreed to in writing, software
  - distributed under the License is distributed on an "AS IS" BASIS,
  - WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  - See the License for the specific language governing permissions and
  - limitations under the License.
  -->

<template>
  <section class="section">
    <p class="heading">Application / Cache</p>
    <template v-for="app in applicationList">
      <sba-panel :title="app.name" :header-sticks-below="['#navigation']" :key="app.name"> 
          <table class="beans table is-fullwidth is-hoverable">
            <tbody>
              <template v-for="cache in cacheList(app.name)">
                <tr  class="is-selectable" :key="cache">
                  <td class="is-breakable">{{cache}}</td>
                  <td class="is-narrow">
                    <button class="button is-info"
                      @click="clearCache(app.name , cache)">
                        <font-awesome-icon icon="trash" />
                        <span v-text="$t('cachemanagement.clear')" />
                    </button>
                  </td>
                </tr>
              </template>
            </tbody>
          </table>
    </template>
  </section>
</template>

<script>

import SbaPanel from '@/sba-cachepanel';
var applicationCacheMap = {};

export default {
  components: {
    SbaPanel
  },

  props: {
    applications: { //<1>
      type: Array,
      required: true,
      default: () => []
    }
  },

  data: () => ({
  }),

  methods: {
    stringify: JSON.stringify,

    filterInstances(applications) {
      return applications;
    },

    getApplicationCaches (application) {
      console.log("getApplciationCaches");
      var uri = "/toolsApps/application/"+ application + "/cache";
     
      var xhr = new XMLHttpRequest();
      xhr.open("GET", uri, false);
      xhr.onreadystatechange = function () {
        if(xhr.readyState === XMLHttpRequest.DONE) {
            var status = xhr.status;
            if (status === 0 || (status >= 200 && status < 400)) {
              applicationCacheMap[application] = {};
              var cacheResponse = JSON.parse(xhr.responseText);
              applicationCacheMap[application] = cacheResponse;
            } else {
              console.log("Can not get application Caches:");
              console.log(xhr);
            }
        }
      };
      xhr.send();
    },

    cacheList(application) {
      return applicationCacheMap[application];
    },

    clearCache(application,cache){
      var uri = "/toolsApps/application/"+ application + "/cache/" + cache ;
     
      var xhr = new XMLHttpRequest();
      xhr.open("DELETE", uri);
      xhr.onreadystatechange = function () {
        if(xhr.readyState === XMLHttpRequest.DONE) {
            var status = xhr.status;
            if (status === 0 || (status >= 200 && status < 400)) {
              var cacheDeleteResponse = JSON.parse(xhr.responseText);
              alert(JSON.stringify(cacheDeleteResponse));
            } else {
              console.log(xhr);
              alert("Error occured");
            }
        }
      };
      xhr.send();
    }


  },

  computed: {
    applicationList() {
      for(var i=0;i<this.applications.length;i++ ){
        this.getApplicationCaches(this.applications[i].name);
      }
      return this.applications;
    }
  }
};
</script>
