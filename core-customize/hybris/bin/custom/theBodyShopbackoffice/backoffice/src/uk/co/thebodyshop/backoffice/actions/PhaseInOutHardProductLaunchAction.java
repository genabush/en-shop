/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.backoffice.actions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.assertj.core.util.Lists;
import org.zkoss.zul.Messagebox;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.product.ProductService;

import uk.co.thebodyshop.core.catalog.services.TbsCatalogVersionService;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;
import uk.co.thebodyshop.core.services.PhaseInPhaseOutProductService;

/**
 * @author Balakrishna
 */
public class PhaseInOutHardProductLaunchAction implements CockpitAction<Object, List>
{

	private static final String SUCCESS_ACTION = "success";
	private static final String ERROR_ACTION = "error";
	private static final String CONFIRMATION_MESSAGE = "hmc.action.phaseinouthardproductlaunchaction.confirmation.message";
	
	@Resource(name = "phaseInPhaseOutProductService")
	private PhaseInPhaseOutProductService phaseInPhaseOutProductService;
	
	@Resource(name = "tbsCatalogVersionService")
	private TbsCatalogVersionService tbsCatalogVersionService;
	
	@Resource(name = "productService")
	private ProductService productService;

	@Override
	public ActionResult<List> perform(final ActionContext<Object> actionContext)
	{
		ActionResult result = null;

		if (actionContext.getData() != null)
		{
			for (final Object obj : this.getData(actionContext))
			{
				if (obj instanceof TbsVariantProductModel)
				{
					final TbsVariantProductModel variantProductModel = (TbsVariantProductModel) obj;
					
					if (tbsCatalogVersionService.isStagedCatalog(variantProductModel.getCatalogVersion()) && checkProductsStatus(variantProductModel) && hasOnlineProduct(variantProductModel) && null!= variantProductModel.getPhaseInProduct() && hasOnlineProduct(variantProductModel.getPhaseInProduct()))
					{
						final CatalogVersionModel onlineProductCatalog = tbsCatalogVersionService.getOnlineMarketProductCatalog(variantProductModel.getCatalogVersion().getCatalog().getId());
						final TbsVariantProductModel	tbsVariantProduct = (TbsVariantProductModel) productService.getProductForCode(onlineProductCatalog, variantProductModel.getCode());
						processHardLaunch(tbsVariantProduct);
						processHardLaunch(variantProductModel);
					}
				}
			}
			
			result = new ActionResult<List>(SUCCESS_ACTION);
		}
		Messagebox.show("Success.");
		return result;
	}
	
	private void processHardLaunch(final TbsVariantProductModel	variantProductModel) 
	{
		final boolean success = phaseInPhaseOutProductService.changePhaseInOutProductStatus(variantProductModel);
		if (success)
		{
			final CMSSiteModel cmsSite = (CMSSiteModel) variantProductModel.getCatalogVersion().getCatalog().getBaseStores().stream().findFirst().get().getCmsSites().stream().findFirst().get();
			if (Objects.nonNull(cmsSite))
			{
				phaseInPhaseOutProductService.emailStockNotificationForPhaseInOutProduct(variantProductModel, cmsSite);
			}
			phaseInPhaseOutProductService.replacePhaseOutWithPhaseInProductsInWishList(variantProductModel);
		}
	}
	
	private boolean checkProductsStatus(final TbsVariantProductModel variantProductModel)
	{
		TbsVariantProductModel phaseInProduct = variantProductModel.getPhaseInProduct();
		if( variantProductModel.getApprovalStatus().equals(ArticleApprovalStatus.APPROVED) && null!= phaseInProduct && phaseInProduct.getApprovalStatus().equals(ArticleApprovalStatus.READYFORPIPO))
		{
			return true;
		}
		return false;
	}
	
	private boolean hasOnlineProduct(final TbsVariantProductModel variantProductModel)
	{
		final CatalogVersionModel onlineProductCatalog = tbsCatalogVersionService.getOnlineMarketProductCatalog(variantProductModel.getCatalogVersion().getCatalog().getId());
		final TbsVariantProductModel	tbsVariantProduct = (TbsVariantProductModel) productService.getProductForCode(onlineProductCatalog, variantProductModel.getCode());
		if(tbsVariantProduct == null)
		{
			return false;
		}
		return true;
	}
	
	@Override
	public boolean canPerform(final ActionContext<Object> ctx)
	{
		boolean result;
		if (ctx.getData() != null)
		{
			final List data = this.getData(ctx);
			result = CollectionUtils.isNotEmpty(data);
		}
		else
		{
			result = false;
		}
		return result;
	}

	@Override
	public boolean needsConfirmation(final ActionContext<Object> ctx)
	{
		return true;
	}

	@Override
	public String getConfirmationMessage(final ActionContext<Object> ctx)
	{
		return ctx.getLabel(CONFIRMATION_MESSAGE);
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
