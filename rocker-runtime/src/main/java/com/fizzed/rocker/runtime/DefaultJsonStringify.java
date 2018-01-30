/*
 * Copyright 2015 Fizzed Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fizzed.rocker.runtime;

/**
 * Default implementation of HtmlStringify.
 */
public class DefaultJsonStringify extends RawStringify {

    @Override
    public String s(String str) {
        return escape(str);
    }

    @Override
    public String s(Object obj) {
        // what to do with null objects?
        if (obj == null) {
            throw new NullPointerException();
        }
        return s(obj.toString());
    }
    
    // all primitives require no further escaping

    static public final String escape(String str) {
        if (str == null) {
            return str;
        }

        int size = str.length();

        // if empty string immediately return it
        if (size == 0) {
            return str;
        }

        StringBuilder output = new StringBuilder(size);
        for(int i=0; i<str.length(); i++) {
            char ch = str.charAt(i);
            int chx = (int) ch;

            // let's not put any nulls in our strings
            assert(chx != 0);

            if(ch == '\n') {
                output.append("\\n");
            } else if(ch == '\t') {
                output.append("\\t");
            } else if(ch == '\r') {
                output.append("\\r");
            } else if(ch == '\\') {
                output.append("\\\\");
            } else if(ch == '"') {
                output.append("\\\"");
            } else if(ch == '\b') {
                output.append("\\b");
            } else if(ch == '\f') {
                output.append("\\f");
            } else if(chx >= 0x10000) {
                assert false : "Java stores as u16, so it should never give us a character that's bigger than 2 bytes. It literally can't.";
            } else if(chx > 127) {
                output.append(String.format("\\u%04x", chx));
            } else {
                output.append(ch);
            }
        }


        if (output == null) {
            return str;
        }
        return output.toString();
    }


}

