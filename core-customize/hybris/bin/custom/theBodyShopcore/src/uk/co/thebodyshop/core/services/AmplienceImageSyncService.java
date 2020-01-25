/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.services;

import de.hybris.platform.core.model.product.ProductModel;

import java.io.IOException;
import java.net.MalformedURLException;

public interface AmplienceImageSyncService {

    void syncImagesForProduct(final ProductModel product,
                                          final StringBuilder syncMessage) throws MalformedURLException, IOException;


}
