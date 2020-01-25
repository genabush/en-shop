/**
 *
 */
package uk.co.thebodyshop.core.interceptors;

import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.InterceptorException;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;

import java.util.Objects;

import uk.co.thebodyshop.core.model.TbsBaseProductModel;


/**
 * @author Marcin
 *
 */
public class BaseProductVariantTypePrepareInterceptor implements PrepareInterceptor<TbsBaseProductModel>
{
	public static final String TBS_VARIANT_PRODUCT = "TbsVariantProduct";

	private final VariantsService variantsService;

	@Override
	public void onPrepare(final TbsBaseProductModel tbsBaseProductModel, final InterceptorContext interceptorContext) throws InterceptorException
	{
			if (interceptorContext.getModelService().isNew(tbsBaseProductModel) && Objects.isNull(tbsBaseProductModel.getVariantType()))
			{
				tbsBaseProductModel.setVariantType(getVariantsService().getVariantTypeForCode(TBS_VARIANT_PRODUCT));
			}
	}

	public BaseProductVariantTypePrepareInterceptor(final VariantsService variantsService)
	{
		this.variantsService = variantsService;
	}

	/**
	 * @return the variantsService
	 */
	protected VariantsService getVariantsService()
	{
		return variantsService;
	}
}
