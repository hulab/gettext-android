package com.hulab.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

import java.nio.file.Files
import java.nio.file.attribute.PosixFilePermission

class GettextAndroid implements Plugin<Project> {
    @Override
    void apply(Project target) {
        def pofolder = new File("${target.projectDir}/po/")
        if (pofolder.exists()) {
            target.task("executePoCompile") {
                executePoCompile(target)
            }
        }
    }

    void executePoCompile(Project project) {
        println("Executing PO compile...")
        def packageName = "${project.android.defaultConfig.applicationId}"
        if (project.hasProperty('poPackageName'))
            packageName = "${project.poPackageName}"

        def endDir = packageName.replace('.', '/')
        def outputDir = new File("${project.buildDir}/generated/source/translations/${endDir}")

        outputDir.exists() || outputDir.mkdirs()

         project.android.sourceSets.main.java.srcDirs {
            'build/generated/source/translations'
         }

        downloadScripts(project)


        def cmd = ["${project.buildDir}/scripts/po-compile-all", "${project.projectDir}/po", "${outputDir.path}", packageName]
        println(cmd)

        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = cmd.execute()

        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(3000)

        println "out> $sout err> $serr"

        println("PO compile done.")
    }

    void downloadScripts(Project project) {
        def address = "https://git.hulab.co/hulab/gettext-android/raw/master/gradle-plugin/src/scripts.zip"
        def dest = "$project.buildDir/scripts.zip"

        new File(dest).withOutputStream { out ->
            new URL(address).withInputStream { from -> out << from
            }
        }
        extract(dest, project)
    }

    void extract(String zipPath, Project project) {
        def dest = "${project.buildDir}/"
        new AntBuilder().unzip(
                src: zipPath,
                dest: dest,
                overwrite: "true")

        Set<PosixFilePermission> perms = new HashSet<>()
        perms.add(PosixFilePermission.OWNER_READ)
        perms.add(PosixFilePermission.OWNER_WRITE)
        perms.add(PosixFilePermission.OWNER_EXECUTE)

        File poCompileAll = new File("${dest}scripts/po-compile-all")
        File poCompilePy = new File("${dest}scripts/po-compile.py")
        Files.setPosixFilePermissions(poCompileAll.toPath(), perms)
        Files.setPosixFilePermissions(poCompilePy.toPath(), perms)
    }
}