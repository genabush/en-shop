/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services.impl;

import de.hybris.platform.media.services.impl.DefaultMimeService;
import org.apache.commons.lang.StringUtils;
import uk.co.thebodyshop.core.util.TbsMediaUtil;

/**
 * @author Lakshmi
 **/
public class DefaultTbsMimeService extends DefaultMimeService {

    public String getMimeFromFileExtension(String fileName) {
        String fileExtension = TbsMediaUtil.getFileExtension(fileName);
        return StringUtils.isNotBlank(fileExtension) ? this.getConfiguredMimeForFileExtension(fileExtension) : null;
    }

    private String getConfiguredMimeForFileExtension(String fileExtension) {
        return this.getConfigParameter("mediatype.by.fileextension." + fileExtension.toLowerCase());
    }
}
