// This file has been dynamically generated by po-compile.
// Do not modify it, all changes will be lost.

package com.hulab.gettext.example.translations;

public class fr extends com.hulab.gettext.Messages {

    public fr() {
        entries.put(new Entry("Hello!"), "Bonjour !");
        plurals.put(
            new Plural("%# file.", "%# files."),
            new String[] {
                "%# fichier.",
                "%# fichiers.",
            }
        );
    }

    @Override
    protected int plural(int n) {
        return (n > 1) ? 1 : 0;
    }
}