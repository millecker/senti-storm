/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package at.illecker.sentistorm.commons.util;

public class HtmlUtils {

  public static boolean containsHtml(String str) {
    if (str == null) {
      return false;
    }
    return RegexUtils.CONTAINS_HTML_SYMBOLS_PATTERN.matcher(str).find();
  }

  public static String replaceHtmlSymbols(String str) {
    String result = str;
    result = result.replaceAll("<p>|</p>", "");

    // http://www.ascii.cl/htmlcodes.htm
    // space
    result = result.replaceAll("&#32;|&#032;", " ");
    // exclamation point
    result = result.replaceAll("&#33;|&#033;", "!");
    // double quotes
    result = result.replaceAll("&quot;|&#34;|&#034;", "\"");
    // number sign
    result = result.replaceAll("&#35;|&#035;", "#");
    // dollar sign
    result = result.replaceAll("&#36;|&#036;", "$");
    // percent sign
    result = result.replaceAll("&#37;|&#037;", "%");
    // ampersand
    result = result.replaceAll("&amp;|&#38;|&#038;", "&");
    // single quote
    result = result.replaceAll("&#39;|&#039;", "'");
    // opening parenthesis
    result = result.replaceAll("&#40;|&#040;", "(");
    // closing parenthesis
    result = result.replaceAll("&#41;|&#041;", ")");
    // asterisk
    result = result.replaceAll("&#42;|&#042;", "*");
    // plus sign
    result = result.replaceAll("&#43;|&#043;", "+");
    // comma
    result = result.replaceAll("&#44;|&#044;", ",");
    // minus sign - hyphen
    result = result.replaceAll("&#45;|&#045;", "-");
    // period
    result = result.replaceAll("&#46;|&#046;", ".");
    // slash
    result = result.replaceAll("&#47;|&#047;", "/");
    // colon
    result = result.replaceAll("&#58;|&#058;", ":");
    // semicolon
    result = result.replaceAll("&#59;|&#059;", ";");
    // less than sign
    result = result.replaceAll("&lt;|&#60;|&#060;", "<");
    // equal sign
    result = result.replaceAll("&#61;|&#061;", "=");
    // greater than sign
    result = result.replaceAll("&gt;|&#62;|&#062;", ">");
    // question mark
    result = result.replaceAll("&#63;|&#063;", "?");
    // at symbol
    result = result.replaceAll("&#64;|&#064;", "@");
    // opening bracket
    result = result.replaceAll("&#91;|&#091;", "[");
    // backslash
    result = result.replaceAll("&#92;|&#092;", "\\");
    // closing bracket
    result = result.replaceAll("&#93;|&#093;", "]");
    // caret - circumflex
    result = result.replaceAll("&#94;|&#094;", "^");
    // underscore
    result = result.replaceAll("&#95;|&#095;", "_");
    // opening brace
    result = result.replaceAll("&#123;", "{");
    // vertical bar
    result = result.replaceAll("&#124;", "|");
    // closing brace
    result = result.replaceAll("&#125;", "}");
    // equivalency sign - tilde
    result = result.replaceAll("&#126;", "~");

    // non-breaking space
    result = result.replaceAll("&nbsp;|&#160;", " ");

    // en dash & em dash
    result = result.replaceAll("&#8211;|&#8212;", "-");
    // left & right single quotation mark
    result = result.replaceAll("&#8216;|&#8217;", "'");
    // single low-9 quotation mark
    result = result.replaceAll("&#8218;", ",");
    // left & right double quotation mark
    result = result.replaceAll("&#8220;|&#8221;", "\"");

    return result.trim();
  }

}
