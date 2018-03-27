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

        extract(this.getClass().protectionDomain.codeSource.location.path, project)

        def cmd = ["${project.getBuildDir().path}/generated/scripts/po-compile-all", "${project.projectDir}/po", "${outputDir.getPath()}", packageName]
        println(cmd)

        def sout = new StringBuilder(), serr = new StringBuilder()
        def proc = cmd.execute()

        proc.consumeProcessOutput(sout, serr)
        proc.waitForOrKill(3000)

        println "out> $sout err> $serr"

        println("PO compile done.")
    }

    void download(Project project) {

    }

    void extract(String jarPath, Project project) {
        def dest = "${project.getBuildDir().path}/generated/"
        new AntBuilder().unzip(
                src: jarPath,
                dest: dest,
                overwrite: "true") {
            patternset() {
                exclude(name: "META-INF")
                exclude(name: "com/*")
                include(name: "/po-compile*")
                include(name: "/Messages.java.tmpl")
            }
        }
        Set<PosixFilePermission> perms = new HashSet<>()
        perms.add(PosixFilePermission.OWNER_READ)
        perms.add(PosixFilePermission.OWNER_WRITE)
        perms.add(PosixFilePermission.OWNER_EXECUTE)

        File poCompileAll = new File("${dest}/scripts/po-compile-all")
        File poCompilePy = new File("${dest}/scripts/po-compile.py")
        Files.setPosixFilePermissions(poCompileAll.toPath(), perms)
        Files.setPosixFilePermissions(poCompilePy.toPath(), perms)
    }
}