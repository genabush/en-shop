/**
 *
 */
package uk.co.thebodyshop.backoffice.renderers;

import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Locale;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zul.Span;

import com.hybris.backoffice.renderer.ProductApprovalStatusRenderer;
import com.hybris.cockpitng.util.UITools;


/**
 * @author Marcin
 *
 */
public class TbsProductApprovalStatusRenderer extends ProductApprovalStatusRenderer
{
	@Override
	protected HtmlBasedComponent createIcon(final ProductModel productModel, final ArticleApprovalStatus approvalStatus)
	{
		final Span icon = new Span();
		UITools.addSClass(icon, YW_IMAGE_ATTRIBUTE_ICON);

		if (getPermissionFacade().canReadInstanceProperty(productModel, ProductModel.APPROVALSTATUS))
		{
			String sclass;
			if (ArticleApprovalStatus.READYTOBELOCALISED.equals(approvalStatus))
			{
				sclass = YW_IMAGE_ATTRIBUTE_APPROVAL_STATUS.concat(ArticleApprovalStatus.CHECK.name().toLowerCase(Locale.ENGLISH));
			}
			else
			{
				sclass = YW_IMAGE_ATTRIBUTE_APPROVAL_STATUS.concat(approvalStatus.name().toLowerCase(Locale.ENGLISH));
			}
			UITools.addSClass(icon, sclass);
			icon.setTooltiptext(getTooltipText(productModel));
		}
		else
		{
			UITools.addSClass(icon, SCLASS_YW_IMAGE_ATTRIBUTE_SYNC_STATUS_NO_READ);
			icon.setTooltiptext(getLabelService().getAccessDeniedLabel(productModel));
		}
		return icon;
	}

}
