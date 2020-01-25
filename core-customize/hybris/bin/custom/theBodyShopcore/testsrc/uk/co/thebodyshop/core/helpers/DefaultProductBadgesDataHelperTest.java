/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.helpers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.commercefacades.product.data.ImageData;

import uk.co.thebodyshop.core.helpers.impl.DefaultProductBadgesDataHelper;
import uk.co.thebodyshop.core.model.BadgePositionModel;
import uk.co.thebodyshop.core.model.ProductBadgeModel;
import uk.co.thebodyshop.core.model.TbsBaseProductModel;
import uk.co.thebodyshop.core.model.TbsVariantProductModel;

/**
 * @author Marcin
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultProductBadgesDataHelperTest
{
	private DefaultProductBadgesDataHelper defaultProductBadgesDataHelper;

	private TbsBaseProductModel baseProduct;

	private TbsVariantProductModel variantProduct;

	private ImageData imageData;

	private ProductBadgeModel productBadge;

	private BadgePositionModel badgePositionModel;

	private Set<ProductBadgeModel> productBadges;

	@Before
	public void setUp()
	{
		defaultProductBadgesDataHelper = new DefaultProductBadgesDataHelper("zoom");

		baseProduct = new TbsBaseProductModel();
		baseProduct.setCode("baseProduct");
		variantProduct = new TbsVariantProductModel();
		variantProduct.setCode("variantProduct");
		variantProduct.setBaseProduct(baseProduct);

		badgePositionModel = new BadgePositionModel();
		productBadge = new ProductBadgeModel();
		productBadge.setCode("productBadge");
		productBadge.setUrl("https://badgeurl");
		productBadge.setPosition(badgePositionModel);

		productBadges = new HashSet<>();
		productBadges.add(productBadge);

		imageData = new ImageData();
		imageData.setUrl("https://originalimageurl");
	}

	@Test
	public void testNotSupportedImageFormat()
	{
		imageData.setFormat("thumbnail");
		defaultProductBadgesDataHelper.appendProductBadgesParameters(baseProduct, imageData);
		assertThat(imageData.getUrl().contentEquals("https://originalimageurl")).isTrue();
	}

	@Test
	public void testProductWithoutBadges()
	{
		imageData.setFormat("zoom");
		defaultProductBadgesDataHelper.appendProductBadgesParameters(baseProduct, imageData);
		assertThat(imageData.getUrl().contentEquals("https://originalimageurl")).isTrue();
	}

	@Test
	public void testBaseProductWithBadges()
	{
		badgePositionModel.setAnchor("anchor");
		badgePositionModel.setTop("50");
		badgePositionModel.setLeft("50");
		badgePositionModel.setOpacity("100%");
		imageData.setFormat("zoom");
		baseProduct.setProductBadges(productBadges);
		defaultProductBadgesDataHelper.appendProductBadgesParameters(baseProduct, imageData);
		assertThat(imageData.getUrl().contentEquals("https://originalimageurl?layer1=[src=https://badgeurl&anchor=anchor&top=50&left=50&opacity=100%]")).isTrue();
	}

	@Test
	public void testVariantProductWithBadges()
	{
		badgePositionModel.setBottom("50");
		badgePositionModel.setRight("50");
		imageData.setFormat("zoom");
		baseProduct.setProductBadges(productBadges);
		defaultProductBadgesDataHelper.appendProductBadgesParameters(variantProduct, imageData);
		assertThat(imageData.getUrl().contentEquals("https://originalimageurl?layer1=[src=https://badgeurl&bottom=50&right=50]")).isTrue();
	}

	@Test
	public void testProductWithBadgesWithExistingParametersInImageUrl()
	{
		badgePositionModel.setBottom("50");
		badgePositionModel.setRight("50");
		imageData.setFormat("zoom");
		imageData.setUrl("https://originalimageurl?h=96");
		baseProduct.setProductBadges(productBadges);
		defaultProductBadgesDataHelper.appendProductBadgesParameters(baseProduct, imageData);
		assertThat(imageData.getUrl().contentEquals("https://originalimageurl?h=96&layer1=[src=https://badgeurl&bottom=50&right=50]")).isTrue();
	}
}
