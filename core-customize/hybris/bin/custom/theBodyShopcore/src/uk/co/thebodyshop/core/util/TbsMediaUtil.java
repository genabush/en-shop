/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.util;

/**
 * @author Lakshmi
 **/
public class TbsMediaUtil {

    public static final String getFileExtension(String filename) {
        if (filename == null) {
            return null;
        } else {
            int ix = filename.lastIndexOf(46);
            String extension = filename.substring(ix + 1).trim();
            String result= ix > -1 ? extension.toLowerCase() : extension;
            return result.contains("?")?result.split("\\?")[0].toLowerCase():result;
        }
    }
}
