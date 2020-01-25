/*
 * Copyright (c)
 * 2019 THE BODY SHOP INTERNATIONAL LIMITED.
 * All rights reserved.
 */
package uk.co.thebodyshop.core.email.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import de.hybris.platform.acceleratorservices.email.impl.DefaultEmailGenerationService;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.acceleratorservices.model.email.EmailMessageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.processengine.model.BusinessProcessModel;

/**
 * @author prateek.goel
 */
public class DefaultTbsEmailGenerationService extends DefaultEmailGenerationService
{

	private static final String SEMICOLON = ";";

	@Override
	protected EmailMessageModel createEmailMessage(final String emailSubject, final String emailBody, final AbstractEmailContext<BusinessProcessModel> emailContext)
	{
		final List<EmailAddressModel> toEmails = new ArrayList<>();
		final List<String> toEmailList = new ArrayList<>(Arrays.asList(emailContext.getToEmail().split(SEMICOLON)));
		final String displayName = emailContext.getToDisplayName();
		for (final String toEmail : toEmailList)
		{
			final EmailAddressModel toAddress = getEmailService().getOrCreateEmailAddressForEmail(toEmail, StringUtils.isBlank(displayName) ? toEmail : displayName);
			toEmails.add(toAddress);
		}
		final EmailAddressModel fromAddress = getEmailService().getOrCreateEmailAddressForEmail(emailContext.getFromEmail(), emailContext.getFromDisplayName());
		return getEmailService().createEmailMessage(toEmails, new ArrayList<EmailAddressModel>(), new ArrayList<EmailAddressModel>(), fromAddress, emailContext.getFromEmail(), emailSubject, emailBody, null);
	}

}
