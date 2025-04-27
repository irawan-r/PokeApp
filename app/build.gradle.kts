import org.jetbrains.kotlin.konan.properties.Properties

plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
	id ("kotlin-parcelize")
	id("dagger.hilt.android.plugin")
}

android {
	namespace = "com.amora.pokeapp"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.amora.pokeapp"
		minSdk = 24
		targetSdk = 33
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		vectorDrawables {
			useSupportLibrary = true
		}
	}

	// Load properties from local.properties
	val propertiesFile = file("${rootDir}/local.properties")
	val properties = Properties().apply {
		load(propertiesFile.inputStream())
	}

	// Access the properties
	val baseUrl: String = properties.getProperty("BASE_URL")
	val baseImgUrl: String = properties.getProperty("BASE_IMG_URL")

	defaultConfig {
		buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
		buildConfigField("String", "BASE_IMG_URL", "\"$baseImgUrl\"")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(
				getDefaultProguardFile("proguard-android-optimize.txt"),
				"proguard-rules.pro"
			)
			buildConfigField("Boolean", "DEBUG", "false")
		}
		debug {
			buildConfigField("Boolean", "DEBUG", "true")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}
	kotlinOptions {
		jvmTarget = "17"
	}
	buildFeatures {
		compose = true
		buildConfig = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = "1.4.3"
	}
	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
	kapt {
		arguments {
			arg("room.schemaLocation", "$projectDir/schemas")
		}
	}
}

dependencies {
	implementation(libs.androidx.core)
	implementation(libs.lifecycle.runtime.ktx)
	implementation(libs.activity.compose)
	implementation(platform(libs.compose.bom))
	implementation(libs.compose.ui)
	implementation(libs.compose.graphics)
	implementation(libs.compose.tooling.preview)
	implementation(libs.compose.material3)

	testImplementation(libs.junit)
	androidTestImplementation(libs.androidx.junit)
	androidTestImplementation(libs.espresso)
	androidTestImplementation(platform(libs.compose.bom))
	androidTestImplementation(libs.compose.ui.test.junit4)
	debugImplementation(libs.compose.tooling)
	debugImplementation(libs.compose.ui.test.manifest)

	// Compose
	implementation(libs.compose.material)
	implementation(libs.compose.material.icons)
	implementation(libs.compose.foundation)
	implementation(libs.compose.foundation.layout)
	implementation(libs.compose.animation)
	implementation(libs.compose.runtime)
	implementation(libs.navigation.compose)
	implementation(libs.constraintlayout.compose)

	// Material
	implementation(libs.material)

	// Accompanist
	implementation(libs.accompanist.systemuicontroller)

	// Hilt
	implementation(libs.hilt.android)
	implementation(libs.hilt.navigation.compose)
	kapt(libs.hilt.compiler)

	// Room
	implementation(libs.room.ktx)
	implementation(libs.room.common)
	kapt(libs.room.compiler)
	implementation(libs.room.paging)

	// Landscapist
	implementation(libs.landscapist)
	implementation(libs.landscapist.animation)
	implementation(libs.landscapist.placeholder)
	implementation(libs.landscapist.palette)

	// Coil
	implementation(libs.coil.compose)

	// Balloon
	implementation(libs.balloon)

	// Network
	implementation(libs.sandwich)
	implementation(libs.retrofit)
	implementation(libs.retrofit.moshi)
	implementation(libs.okhttp.logging)
	implementation(libs.moshi.kotlin)
	kapt(libs.moshi.codegen)

	// Paging
	implementation(libs.paging.runtime)
	implementation(libs.paging.compose)

	// Timber
	implementation(libs.timber)

	// Palette
	implementation(libs.palette.ktx)

	implementation(libs.splashscreen)
}