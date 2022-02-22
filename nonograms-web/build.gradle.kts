/*
 * MIT License
 *
 * Copyright (c) 2020 Andrew Krepps
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin

plugins {
    kotlin("multiplatform")
}

group = "com.askrepps"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

rootProject.plugins.withType(YarnPlugin::class.java) {
    rootProject.the<NodeJsRootExtension>().apply {
        versions.webpackDevServer.version = "4.7.4"
        versions.karma.version = "6.3.16"
        versions.karmaChromeLauncher.version = "3.1.0"
        versions.karmaMocha.version = "2.0.1"
        versions.karmaSourcemapLoader.version = "0.3.8"
        versions.karmaWebpack.version = "5.0.0"
        versions.mocha.version = "9.2.0"
    }
}

kotlin {
    js {
        browser()
    }

    js().compilations["main"].defaultSourceSet {
        dependencies {
            implementation(project(":nonograms-core"))
            implementation(kotlin("stdlib-js"))
            implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.3")
        }
    }
    js().compilations["test"].defaultSourceSet {
        dependencies {
            implementation(kotlin("test-js"))
        }
    }

    sourceSets.all {
        languageSettings.optIn("kotlin.RequiresOptIn")
    }
}
