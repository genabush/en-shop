/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.actions;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.engine.impl.AbstractComponentWidgetAdapterAware;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zul.Messagebox;
import uk.co.thebodyshop.core.services.AmplienceImageSyncService;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

public class AmplienceSyncAction extends AbstractComponentWidgetAdapterAware implements CockpitAction<Object, List> {

    private static final Logger LOG= LoggerFactory.getLogger(AmplienceSyncAction.class);
    private static final String ALL_PRODUCTS_WERE_UPDATED_SUCCESFULLY = "All products were updated succesfully.";
    private static final String SUCCESS_ACTION = "success";
    private static final String STARTED_MEDIA_SYNC_JOB_ATTR = "startedMediaSyncJob";
    private static final String INCORRECT_CATALOG_VERSION_FOUND_FOR_PRODUCT = "Incorrect Catalog Version found for product :: ";
    private static final String STAGED = "Staged";
    private static final String AMPLIENCE_MEDIA_IMPORT_CATALOG_VERSION = "amplience.media.import.catalog.version";
    private static final String AMPLIENCE_MEDIA_IMPORT_CATALOG_IDS = "amplience.media.import.catalog.ids";
    private static final String ERROR_ACTION = "error";

    @Resource
    private ObjectFacade objectFacade;

    @Resource
    private AmplienceImageSyncService amplienceImageSyncService;

    @Resource
    private ConfigurationService configurationService;

    @Override
    public boolean canPerform(final ActionContext<Object> ctx)
    {
        boolean result;
        if (ctx.getData() != null)
        {
            final List data = this.getData(ctx);
            result = CollectionUtils.isNotEmpty(data) && data.stream().noneMatch(objectFacade::isModified);
        }
        else
        {
            result = false;
        }
        return result;
    }

    @Override
    public ActionResult<List> perform(final ActionContext<Object> context)
    {
        ActionResult result = new ActionResult(ERROR_ACTION);
        final StringBuilder syncMessage = new StringBuilder();
        final String[] catalogIds = configurationService.getConfiguration()
                .getString(AMPLIENCE_MEDIA_IMPORT_CATALOG_IDS).split(",");
        final String catalogVersion = configurationService.getConfiguration().getString(AMPLIENCE_MEDIA_IMPORT_CATALOG_VERSION,
                STAGED);
        if (context.getData() != null)
        {
            for (final Object obj : this.getData(context))
            {
                if (obj instanceof ProductModel)
                {
                    syncMediaForProduct(obj, catalogIds, catalogVersion, syncMessage);
                }
            }
            this.sendOutput(STARTED_MEDIA_SYNC_JOB_ATTR, SUCCESS_ACTION);
            result = new ActionResult<List>(SUCCESS_ACTION);
        }
        if (StringUtils.isEmpty(syncMessage.toString()))
        {
            syncMessage.append(ALL_PRODUCTS_WERE_UPDATED_SUCCESFULLY);
        }

        Messagebox.show(syncMessage.toString());

        return result;
    }

    private void syncMediaForProduct(Object obj, String[] catalogIds, String catalogVersion, StringBuilder syncMessage) {
        final ProductModel product = (ProductModel) obj;
        // Check if product is from staged product catalog if not
        // add error
        if (null != product.getCatalogVersion() && null != product.getCatalogVersion().getCatalog()
                && Arrays.asList(catalogIds).contains(product.getCatalogVersion().getCatalog().getId())
                && catalogVersion.equals(product.getCatalogVersion().getVersion()))
        {
            try {
                amplienceImageSyncService.syncImagesForProduct(product, syncMessage);
            } catch (Exception e) {
                LOG.error("Unable To Sync Amplience Images For :[{}]",product.getCode(),e);
            }
        }
        else
        {
            syncMessage.append(INCORRECT_CATALOG_VERSION_FOUND_FOR_PRODUCT + product.getCode())
                    .append(System.lineSeparator()).append(System.lineSeparator());
        }
    }

    private List<Object> getData(final ActionContext<Object> context)
    {
        List<Object> result;
        if (context.getData() instanceof Collection)
        {
            final Collection data = (Collection) context.getData();
            result = (List) data.stream().filter((o) -> {
                return !Objects.isNull(o);
            }).collect(Collectors.toList());
        }
        else
        {
            result = context.getData() != null ? Lists.newArrayList(new Object[]
                    { context.getData() }) : Collections.emptyList();
        }
        return result;
    }

}
