import com.gtnewhorizons.retrofuturagradle.mcp.DeobfuscateTask

plugins {
    id("java-library")
    id("maven-publish")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.1.8"
    id("eclipse")
    id("com.gtnewhorizons.retrofuturagradle") version "2.0.2"
}

version = "0.2.1"
group = "com.rebirthofthenight.rotntweaker" // http://maven.apache.org/guides/mini/guide-naming-conventions.html

sourceSets {
    main {
        java.srcDirs("src/main/java", "src/main/lenses")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
        vendor.set(JvmVendorSpec.AZUL)
    }
}

minecraft {
    mcVersion.set("1.12.2")
    mcpMappingChannel.set("snapshot")
    mcpMappingVersion.set("20171003")

    injectedTags.put("VERSION", project.version)
    useDependencyAccessTransformers.set(true)

    extraRunJvmArguments.addAll(
        "-Dfml.coreMods.load=gloomyfolken.hooklib.minecraft.MainHookLoader",
        "-Dforge.logging.markers=SCAN,REGISTRIES,REGISTRYDUMP",
        "-Dforge.logging.console.level=debug"
    )
    username.set("Player")
}

tasks.withType<DeobfuscateTask> {
    accessTransformerFiles.from(
        "libs_at/bwm_at.cfg",
        "libs_at/crafttweaker_at.cfg",
        "libs_at/quark_at.cfg"
    )
}

tasks.injectTags {
    outputClassName.set("${project.group}.RotNTweakerConsts")
}

tasks.applyJST {
    // https://github.com/GTNewHorizons/RetroFuturaGradle/issues/101
    javaLauncher.set(minecraft.getToolchainLauncher(project, 25))
}

repositories {
    mavenLocal()
    mavenCentral()
    maven {
        url = uri("https://cursemaven.com")
        content {
            includeGroup("curse.maven")
        }
    }
    maven {
        name = "ModMaven"
        url = uri("https://modmaven.dev/")
    }
    maven {
        url = uri("https://maven.blamejared.com")
    }
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(rfg.deobf("abc:hooklib:3.4"))

    implementation("CraftTweaker2:CraftTweaker2-MC1120-Main:1.12-4.1.20.702:deobf")
    compileOnly("mezz.jei:jei_1.12.2:4.15.0.291:api")

    implementation(rfg.deobf("curse.maven:rustic-256141:3107974"))

    //https://www.curseforge.com/minecraft/mc-mods/glaretorch/download/2448495
    implementation(rfg.deobf("curse.local:GlareTorch:Ver.8.11"))

    implementation(rfg.deobf("curse.maven:primalcore-247907:2734701"))

    implementation(rfg.deobf("curse.maven:totem-expansion-291724:2599101"))

    implementation(rfg.deobf("curse.maven:quark-rotn-edition-417392:4641362"))
    implementation(rfg.deobf("vazkii.autoreglib:AutoRegLib:1.3-32.33"))

    implementation(rfg.deobf("curse.maven:bwm-suite-246760:3289033"))
    implementation("betterwithmods.core:BetterWithLib:1.12-1.5:dev")

    implementation(rfg.deobf("curse.maven:pyrotech-306676:4798313"))
    implementation(rfg.deobf("curse.maven:athenaeum-284350:4633750"))
    implementation(rfg.deobf("curse.maven:dropt-284973:3758733"))
    implementation(rfg.deobf("curse.maven:patchouli-306770:3162874"))

    implementation(rfg.deobf("curse.maven:applecore-224472:2969118"))
    // implementation("albedo:albedo:1.12.2:1.1.0")
}

tasks.jar {
    archiveBaseName.set("RotN-Tweaker")
    destinationDirectory.set(project.file("$rootDir/production"))
    finalizedBy(tasks.reobfJar)
}

tasks.processResources {
    inputs.property("version", project.version)
    inputs.property("mcversion", "1.12.2")

    from("./src/main/unprepared_resources") {
        include("*")
        include("*/*")
        expand("version" to project.version, "mcversion" to "1.12.2")
    }
}
