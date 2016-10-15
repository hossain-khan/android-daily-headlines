[![Build Status](https://travis-ci.org/amardeshbd/android-daily-headlines.svg?branch=develop)](https://travis-ci.org/amardeshbd/android-daily-headlines) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/33be4683227a4d1e83cea377562dcc09)](https://www.codacy.com/app/amardeshbd/android-daily-headlines) [![codebeat badge](https://codebeat.co/badges/e2dd5a2b-0d57-4946-9696-17f82366395f)](https://codebeat.co/projects/github-com-amardeshbd-android-daily-headlines) [![release](https://img.shields.io/github/release/amardeshbd/android-daily-headlines.svg?maxAge=2592000)](https://github.com/amardeshbd/android-daily-headlines/releases) [![license](https://img.shields.io/github/license/amardeshbd/android-daily-headlines.svg?maxAge=2592000)](https://github.com/amardeshbd/android-daily-headlines/blob/develop/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/amardeshbd/android-daily-headlines.svg)](https://github.com/amardeshbd/android-daily-headlines/issues) [![GitHub closed issues](https://img.shields.io/github/issues-closed-raw/amardeshbd/android-daily-headlines.svg?maxAge=2592000)](https://github.com/amardeshbd/android-daily-headlines/issues?q=is%3Aissue+is%3Aclosed) [![GitHub closed pull requests](https://img.shields.io/github/issues-pr-closed-raw/amardeshbd/android-daily-headlines.svg?maxAge=2592000)](https://github.com/amardeshbd/android-daily-headlines/pulls?q=is%3Apr+is%3Aclosed) 

# android-daily-headlines
An app that provides daily headlines from popular news source.

Initial planning stage, main goal is to create simple app with news source title '**only**' to keep it simple. This app is meant to take a glance through the headlines quickly with possible option to view details of the story _(future version)_.


## Goal and Status

Current plan is to create simple possible 📺 app with news headline only. This allows users to quickly glance through headlines over a ☕

The 📱 version of the app will follow after 2.0 release of 📺 _(TV)_ app.

### Project Planning
I am currently tracking issues and tasks using :octocat:'s kanban board

 * Release v1 - https://github.com/amardeshbd/android-daily-headlines/projects/1
 * Release v2 - https://github.com/amardeshbd/android-daily-headlines/projects/2

### Screenshot from `develop` build

![device-2016-10-10-184136](https://cloud.githubusercontent.com/assets/99822/19253463/37a6102c-8f19-11e6-97a1-d7c778a9b3d1.png)
![device-2016-10-10-184114](https://cloud.githubusercontent.com/assets/99822/19253464/37a966a0-8f19-11e6-95ed-f3663c3f45f9.png)

----

## DISCLAIMER
This is my personal project to experiment with following tools & technologies
 * Firebase - Analytics, Crash Reporting _(See [#10](https://github.com/amardeshbd/android-daily-headlines/pull/10))_, Remote Config etc.
 * Travis CI - for build automation _(See [travis.yml](https://github.com/amardeshbd/android-daily-headlines/blob/develop/.travis.yml))_
 * Code coverage (jacoco _[See [travis.yml](https://github.com/amardeshbd/android-daily-headlines/blob/develop/.travis.yml#L32)]_,  [Codecov.io](https://codecov.io/gh/amardeshbd/android-daily-headlines), [codacy](https://www.codacy.com/app/amardeshbd/android-daily-headlines), [codebeat](https://codebeat.co/projects/github-com-amardeshbd-android-daily-headlines))
 * OpenAPI Specification & Related Tools _(See [#11](https://github.com/amardeshbd/android-daily-headlines/pull/11), [wiki](https://github.com/amardeshbd/android-daily-headlines/wiki/Swagger-Codegen))_
 * RxJava _(Using for retrofit and app)_
 * Jack & Jill Toolchain - _Used for Java 8 support_
 * Retrofit 2 _(See [API Services](https://github.com/amardeshbd/android-daily-headlines/tree/develop/api-lib/src/main/java/io/swagger/client/api))_
 * Dagger 2 _(See [#29](https://github.com/amardeshbd/android-daily-headlines/issues/29), [core-lib:gradle](https://github.com/amardeshbd/android-daily-headlines/blob/develop/core-lib/build.gradle#L42))_
 * Picasso - _Used for memory efficient image loading_
 * Coordinator Layout
 * Timber (android logging) _(See [#30](https://github.com/amardeshbd/android-daily-headlines/pull/30))_
 * Android TV Leanback _(Most code samples are taken from "[leanback-showcase](https://github.com/googlesamples/leanback-showcase)" project)_
 * Design Support Library
 * Database (local caching - Realm (maybe?))
 * Scheduler - data syncing
 * Google Play Alpha Beta release
 * LeakCanary _(See [#60](https://github.com/amardeshbd/android-daily-headlines/pull/60))_
 
I'll try to update references for these items when I use in the app :sunglasses:
