#!/usr/bin/env python
# encoding: utf-8

"""
Copyright 2014 Aurélien Gâteau <mail@agateau.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
"""

# Python 2/3 compatibility
from __future__ import division, absolute_import, print_function, unicode_literals

import argparse
import os
import sys

import jinja2
import polib

DESCRIPTION = """\
Generates .java files from .po files.

The generated .java files can then be used with the Translator class.
"""

ESCAPES = [('\'' , r'\'')]
def escape(txt):
    return txt


def escape_plural(dct):
    return [escape(dct[x]) for x in sorted(dct.keys())]


def extract_plural_code(txt):
    is_boolean = txt.startswith("nplurals=2")
    code = txt.split("plural=")[1]
    if is_boolean:
        return code.strip(";") + " ? 1 : 0;"
    else:
        return code


def process(package, locale, pofile, out):
    generator = os.path.basename(sys.argv[0])

    tmpl_path = os.path.join(os.path.dirname(__file__), "Messages.java.tmpl")
    with open(tmpl_path) as f:
        tmpl = jinja2.Template(f.read())

    entries = pofile.translated_entries()
    plain_entries = [(escape(x.msgid), escape(x.msgstr)) for x in entries if not x.msgstr_plural and not x.msgctxt]
    ctxt_entries = [(escape(x.msgid), escape(x.msgstr), escape(x.msgctxt)) for x in entries if x.msgctxt and not x.msgstr_plural]
    plain_plurals = [(escape(x.msgid), escape(x.msgid_plural), escape_plural(x.msgstr_plural)) for x in entries if x.msgstr_plural and not x.msgctxt]
    ctxt_plurals = [(escape(x.msgid), escape(x.msgid_plural), escape_plural(x.msgstr_plural), escape(x.msgctxt)) for x in entries if x.msgstr_plural and x.msgctxt]
    plural_code = extract_plural_code(pofile.metadata["Plural-Forms"])

    txt = tmpl.render(dict(
        package=package,
        locale=locale,
        plain_entries=plain_entries,
        ctxt_entries=ctxt_entries,
        plain_plurals=plain_plurals,
        ctxt_plurals=ctxt_plurals,
        plural_code=plural_code,
        generator=generator,
    ))
    out.write(txt.encode("utf-8"))


def parse_args():
    parser = argparse.ArgumentParser(description=DESCRIPTION)

    parser.add_argument("-o", "--output", default="-",
        help="write code to FILE. Defaults to stdout.", metavar="FILE")

    parser.add_argument("-l", "--locale", required=True,
        help="name of the locale (for example \"fr\" or \"pt_BR\"")

    parser.add_argument("-p", "--package", required=True,
        help="name of the java package (for example \"com.hulab.gettext.po\"")

    parser.add_argument("po_file",
        help="file to parse")

    return parser.parse_args()


def main():
    args = parse_args()
    if args.output == "-":
        out = sys.stdout
    else:
        out = open(args.output, "w")

    if not os.path.exists(args.po_file):
        print("{} does not exist".format(args.po_file), file=sys.stderr)
        return 1
    pofile = polib.pofile(args.po_file)
    process(args.package, args.locale, pofile, out)

    return 0


if __name__ == "__main__":
    sys.exit(main())
# vi: ts=4 sw=4 et
