/*
 * Copyright 2014 Aurélien Gâteau <mail@agateau.com>
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
package com.hulab.gettext;

import android.content.Context;
import android.util.Pair;

import java.util.HashMap;

public class Messages {

    static Messages load(Context context, String locale) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class<?> clazz = Class.forName(context.getPackageName() + ".translations." + locale);
        return (Messages) clazz.newInstance();
    }

    protected static class Entry {

        protected final String msgid;
        protected final String context;

        public Entry(String msgid) {
            this.msgid = msgid;
            this.context = "";
        }

        protected Entry(String msgid, String context) {
            this.msgid = msgid;
            this.context = context;
        }

        @Override
        public int hashCode() {
            return (context + '|' + msgid).hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Entry)) {
                return false;
            }

            Entry other = (Entry)obj;
            return context.equals(other.context) && msgid.equals(other.msgid);
        }
    }

    protected static class Plural extends Entry {

        protected final String msgid_plural;

        protected Plural(String msgid, String msgid_plural, String context) {
            super(msgid, context);
            this.msgid_plural = msgid_plural;
        }

        public Plural(String msgid, String msgid_plural) {
            super(msgid);
            this.msgid_plural = msgid_plural;
        }

        @Override
        public int hashCode() {
            return (context + '|' + msgid + '|' + msgid_plural).hashCode();
    }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Plural)) {
                return false;
            }

            Plural other = (Plural)obj;
            return context.equals(other.context) && msgid.equals(other.msgid) && msgid_plural.equals(other.msgid_plural);
        }
    }

    protected final HashMap<Entry, String> entries = new HashMap<Entry, String>();

    protected final HashMap<Plural, String[]> plurals = new HashMap<Plural, String[]>();

    protected int plural(int n) {
        return n == 1 ? 0 : 1;
    }

    String gettext(String msgid) {
        String msgstr = entries.get(new Entry(msgid));
        return msgstr == null ? msgid : msgstr;
    }

    String xgettext(String msgid, String context) {
        String msgstr = entries.get(new Entry(msgid, context));
        return msgstr == null ? msgid : msgstr;
    }

    String ngettext(String msgid, String msgid_plural, int n) {
        return resolve(new Plural(msgid, msgid_plural), n);
    }

    String nxgettext(String msgid, String msgid_plural, String context, int n) {
        return resolve(new Plural(msgid, msgid_plural, context), n);
    }

    String resolve(Plural plural, int n) {
        String[] msgstr = plurals.get(plural);

        if (msgstr == null) {
            return n == 1 ? plural.msgid : plural.msgid_plural;
        }

        // Should check bounds
        return msgstr[plural(n)];
    }
}
