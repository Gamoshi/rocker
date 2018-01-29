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
        StringBuilder builder = new StringBuilder();

        int i = 0;
        while (i < str.length()) {
            char delimiter = str.charAt(i); i++;

            if(delimiter == '\\' && i < str.length()) {

                // consume first after backslash
                char ch = str.charAt(i); i++;

                if(ch == '\\' || ch == '/' || ch == '"' || ch == '\'') {
                    builder.append(ch);
                }
                else if(ch == 'n') builder.append('\n');
                else if(ch == 'r') builder.append('\r');
                else if(ch == 't') builder.append('\t');
                else if(ch == 'b') builder.append('\b');
                else if(ch == 'f') builder.append('\f');
                else if(ch == 'u') {

                    StringBuilder hex = new StringBuilder();

                    // expect 4 digits
                    if (i+4 > str.length()) {
                        throw new RuntimeException("Not enough unicode digits! ");
                    }
                    for (char x : str.substring(i, i + 4).toCharArray()) {
                        if(!Character.isLetterOrDigit(x)) {
                            throw new RuntimeException("Bad character in unicode escape.");
                        }
                        hex.append(Character.toLowerCase(x));
                    }
                    i+=4; // consume those four digits.

                    int code = Integer.parseInt(hex.toString(), 16);
                    builder.append((char) code);
                } else {
                    throw new RuntimeException("Illegal escape sequence: \\"+ch);
                }
            } else {
                builder.append(delimiter);
            }
        }

        return builder.toString();

    }   
}

